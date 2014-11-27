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
package jp.yuimarl.web;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.json.Json;
import javax.json.stream.JsonParser;
import jp.yuimarl.ejb.GoodsCategoryService;
import jp.yuimarl.ejb.YuimarlUserService;
import jp.yuimarl.entity.GoodsCategory;
import jp.yuimarl.entity.YuimarlUser;
import jp.yuimarl.util.GCItem;
import jp.yuimarl.util.JsfUtil;
import jp.yuimarl.util.PageNavigation;
import jp.yuimarl.util.YuimarlUtil;

/**
 * 物品カテゴリ制御クラス
 *
 * @author Masahiro Nitta
 */
@Named(value = "goodsCategoryController")
@RequestScoped
public class GoodsCategoryController implements Serializable {

    private static final String BUNDLE = "bundles.Bundle";

    @EJB
    private GoodsCategoryService goodsCategoryService;
    @EJB
    private YuimarlUserService userService;

    private String treeTag;
    private String json;
    private YuimarlUser user;

    /**
     * ツリー生成用タグを取得する。
     *
     * @return
     */
    public String getTreeTag() {
        return treeTag;
    }

    /**
     * ツリー生成用タグを設定する。
     *
     * @param treeTag
     */
    public void setTreeTag(String treeTag) {
        this.treeTag = treeTag;
    }

    /**
     * ツリー内容のJSONを取得する。
     *
     * @return
     */
    public String getJson() {
        return json;
    }

    /**
     * ツリー内容のJSONを設定する。
     *
     * @param json
     */
    public void setJson(String json) {
        this.json = json;
    }

    /**
     * 編集画面準備
     *
     * @return
     */
    public PageNavigation prepareEdit() {
        // dynatreeのツリー用タグを生成する。
        treeTag = goodsCategoryService.makeTag(true);
        return PageNavigation.GOODSCATEGORY_EDIT;
    }

    /**
     * 保存ボタン押下時の動作
     *
     * @return
     */
    public PageNavigation update() {
        try {
            user = YuimarlUtil.getLoginUser(userService);
            GCItem item = null;
            GCItem parent;
            // レベルの系譜を保持する配列
            List<GCItem> itemList = new ArrayList<>();
            List<GCItem> genealogy = new ArrayList<>();
            String key = null;
            String wk;
            int idx;
            int level = 0;

            // ツリーの内容がjsonに格納されているので、解析を行う。
            JsonParser parser = Json.createParser(new StringReader(this.json));

            while (parser.hasNext()) {
                JsonParser.Event event = parser.next();
                switch (event) {
                    case START_OBJECT:
                        level++;
                        item = new GCItem();
                        item.level = level;
                        if (genealogy.size() < level) {
                            genealogy.add(item);
                        } else {
                            genealogy.set(level - 1, item);
                        }
                        if (level > 1) {
                            parent = genealogy.get(level - 2);
                            parent.children.add(item);
                            item.parent = parent.categoryNo;
                        }
                        itemList.add(item);
                        break;
                    case END_OBJECT:
                        level--;
                        break;
                    case KEY_NAME:
                        key = parser.getString();
                        break;
                    case VALUE_STRING:
                        if (level > 1) {
                            if (key != null) {
                                switch (key) {
                                    case "title":
                                        item.name = parser.getString();
                                        break;
                                    case "key":
                                        wk = parser.getString();
                                        idx = wk.indexOf(",");
                                        item.categoryNo = Integer.parseInt(wk.substring(0, idx));
                                        item.nameKana = wk.substring(idx + 1);
                                        if (item.categoryNo == 0) {
                                            // カテゴリNo.が0の場合、新規登録する。
                                            GoodsCategory g = new GoodsCategory();
                                            g.setName(item.name);
                                            g.setNameKana(item.nameKana);
                                            g.setRegistUser(user);
                                            g.setRegistTime(new Date());
                                            goodsCategoryService.create(g);
                                            item.categoryNo = g.getCategoryNo();
                                        }
                                        break;
                                }
                            }
                        }
                        break;
                }
            }

            matchingUpdate(itemList);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("PartyUpdated"));
            return prepareEdit();
        } catch (NumberFormatException e) {
            e.printStackTrace(System.err);
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * ユーザーが編集したリストと、DBに保存されているデータを比較し、異なっている場合に変更をDBに反映する。
     *
     * @param itemList
     */
    private void matchingUpdate(List<GCItem> itemList) {
        boolean exist;
        List<GoodsCategory> list = goodsCategoryService.findAll();
        for (GoodsCategory g : list) {
            exist = false;
            for (GCItem item : itemList) {
                if (Objects.equals(g.getCategoryNo(), item.categoryNo)) {
                    exist = true;
                    if (g.getName().equals(item.name) == false
                            || g.getNameKana().equals(item.nameKana) == false
                            || g.getParentCategoryNo() != item.parent) {
                        // 同じカテゴリNo.で、内容が異なっている場合は、更新する。
                        g.setName(item.name);
                        g.setNameKana(item.nameKana);
                        if (item.parent == 0) {
                            g.setParentCategory(null);
                        } else {
                            for (GoodsCategory p : list) {
                                if (p.getCategoryNo() == item.parent) {
                                    g.setParentCategory(p);
                                    break;
                                }
                            }
                        }
                        g.setUpdateUser(user);
                        g.setUpdateTime(new Date());
                        goodsCategoryService.edit(g);
                    }
                    break;
                }
            }
            if (exist == false) {
                // リストになくなった場合は、論理削除する。
                g.setUpdateUser(user);
                g.setUpdateTime(new Date());
                goodsCategoryService.deleteWithGoods(g);
            }
        }
    }
}
