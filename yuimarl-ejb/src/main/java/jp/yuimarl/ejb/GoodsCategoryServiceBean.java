/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 BitFarm Corporation
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jp.yuimarl.ejb;

import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jp.yuimarl.entity.GoodsCategory;
import jp.yuimarl.util.GCItem;

/**
 * 物品カテゴリー操作クラス
 *
 * @author Masahiro Nitta
 */
@Stateless
public class GoodsCategoryServiceBean extends AbstractFacade<GoodsCategory> implements GoodsCategoryService {

    @PersistenceContext(unitName = "yuimarlPU")
    private EntityManager em;

    /**
     * コンストラクタ
     */
    public GoodsCategoryServiceBean() {
        super(GoodsCategory.class);
    }

    /**
     * EntityManagerを取得する。
     *
     * @return
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * すべての物品カテゴリーを取得する。
     *
     * @return
     */
    @Override
    public List<GoodsCategory> findAll() {
        Query q = em.createNamedQuery("GoodsCategory.findAll");
        return q.getResultList();
    }

    /**
     * カテゴリNo.を指定して物品カテゴリーを取得する。
     *
     * @param categoryNo
     * @return
     */
    @Override
    public GoodsCategory findByCategoryNo(Integer categoryNo) {
        Query createNamedQuery = em.createNamedQuery("GoodsCategory.findByCategoryNo");

        createNamedQuery.setParameter("categoryNo", categoryNo);

        if (createNamedQuery.getResultList().size() > 0) {
            return (GoodsCategory) createNamedQuery.getSingleResult();
        }

        return null;
    }

    /**
     * dynatreeのツリー用タグを生成する。
     *
     * @param keyPlusKana
     * @return
     */
    @Override
    public String makeTag(boolean keyPlusKana) {
        List<GoodsCategory> list = findAll();
        GCItem root = new GCItem();
        root.level = 0;
        searchList(list, root);
        StringBuilder buf = new StringBuilder();
        makeTagSub(root, buf, keyPlusKana);
        return buf.toString();
    }

    /**
     * GoodsCategoryのリストから、GCItemの階層ツリーを生成する。
     *
     * @param list GoodsCategoryのリスト
     * @param item GCItem
     */
    private void searchList(List<GoodsCategory> list, GCItem item) {
        GCItem c;
        for (GoodsCategory g : list) {
            if (item.level == 0) {
                if (g.getParentCategory() == null) {
                    c = new GCItem();
                    c.level = item.level + 1;
                    c.categoryNo = g.getCategoryNo();
                    c.name = g.getName();
                    c.nameKana = g.getNameKana();
                    item.children.add(c);
                    searchList(list, c);
                }
            } else {
                if (g.getParentCategory() != null && Objects.equals(g.getParentCategory().getCategoryNo(), item.categoryNo)) {
                    c = new GCItem();
                    c.level = item.level + 1;
                    c.categoryNo = g.getCategoryNo();
                    c.name = g.getName();
                    c.nameKana = g.getNameKana();
                    item.children.add(c);
                    searchList(list, c);
                }
            }
        }
    }

    /**
     * GCItemの階層ツリーから、dynatreeのツリー用タグを生成する。
     *
     * @param item
     * @param buf
     * @param keyPlusKana
     */
    private void makeTagSub(GCItem item, StringBuilder buf, boolean keyPlusKana) {
        GCItem c;
        buf.append("<ul>");
        for (int i = 0; i < item.children.size(); i++) {
            c = item.children.get(i);
            buf.append("<li id=\"");
            buf.append(c.categoryNo);
            if (keyPlusKana) {
                buf.append(",").append(c.nameKana);
            }
            buf.append("\" class=\"expanded\" data=\"icon: false");
            if (item.level == 0 && i == 0) {
                buf.append(", activate: true");
            }
            buf.append("\">");
            buf.append(c.name);
            if (c.children.size() > 0) {
                makeTagSub(c, buf, keyPlusKana);
            }
            buf.append("</li>");
        }
        buf.append("</ul>");
    }

    /**
     * 物品カテゴリーを削除する。
     * <p>
     * 削除される物品カテゴリーを使用している物品のカテゴリーのクリアも行う。
     * </p>
     *
     * @param gc 物品カテゴリー
     */
    @Override
    public void deleteWithGoods(GoodsCategory gc) {
        Query q;
        // 削除される物品カテゴリーを使用している物品のカテゴリーをクリアする。
        q = em.createQuery("UPDATE Goods g SET g.category = null WHERE g.category = :category").setParameter("category", gc);
        q.executeUpdate();
        // 物品カテゴリーを論理削除する。
        gc.setDelFlg('1');
        edit(gc);
    }
}
