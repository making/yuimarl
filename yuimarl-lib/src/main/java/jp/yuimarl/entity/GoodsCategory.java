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
package jp.yuimarl.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * 物品カテゴリクラス
 *
 * @author Masahiro Nitta
 */
@Entity
@Table(name = "GOODS_CATEGORY")
@NamedQueries({
    @NamedQuery(name = "GoodsCategory.findAll", query = "SELECT g FROM GoodsCategory g WHERE g.delFlg = '0' ORDER BY g.nameKana"),
    @NamedQuery(name = "GoodsCategory.findByCategoryNo", query = "SELECT g FROM GoodsCategory g WHERE g.delFlg = '0' AND g.categoryNo = :categoryNo")})
public class GoodsCategory implements EntityBasic, Serializable {

    /**
     * カテゴリNo.
     */
    @TableGenerator(name = "CATEGORY_NO_GEN", table = "GENERATED_ID", pkColumnName = "KEY_NAME", valueColumnName = "VALUE", pkColumnValue = "CATEGORY_NO_MAX", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CATEGORY_NO_GEN")
    @Column(name = "CATEGORY_NO")
    private Integer categoryNo;

    /**
     * 名前
     */
    @Column(length = 50)
    private String name;

    /**
     * 名前ヨミ
     */
    @Column(name = "NAME_KANA", length = 50)
    private String nameKana;

    /**
     * 親カテゴリ
     */
    @ManyToOne
    @JoinColumn(name = "PARENT_CATEGORY", referencedColumnName = "CATEGORY_NO")
    private GoodsCategory parentCategory;

    /**
     * 削除フラグ
     * <p>
     * 0:有効, 1:削除済み
     * </p>
     */
    @Column(name = "DEL_FLG")
    private Character delFlg = '0';

    /**
     * 登録ユーザー
     */
    @ManyToOne
    @JoinColumn(name = "REGIST_USER", referencedColumnName = "USER_NO")
    private YuimarlUser registUser;

    /**
     * 登録日時
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REGIST_TIME")
    private Date registTime;

    /**
     * 更新ユーザー
     */
    @ManyToOne
    @JoinColumn(name = "UPDATE_USER", referencedColumnName = "USER_NO")
    private YuimarlUser updateUser;

    /**
     * 更新日時
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    /**
     * バージョン番号
     */
    @Version
    @Column(name = "VERSION_NO")
    private Integer versionNo;

    /**
     * コンストラクタ
     */
    public GoodsCategory() {
    }

    /**
     * カテゴリNo.を取得する。
     *
     * @return
     */
    public Integer getCategoryNo() {
        return this.categoryNo;
    }

    /**
     * カテゴリNo.を設定する。
     *
     * @param categoryNo
     */
    public void setCategoryNo(Integer categoryNo) {
        this.categoryNo = categoryNo;
    }

    /**
     * 名前を取得する。
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * 名前を設定する。
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 名前ヨミを取得する。
     *
     * @return
     */
    public String getNameKana() {
        return this.nameKana;
    }

    /**
     * 名前ヨミを設定する。
     *
     * @param nameKana
     */
    public void setNameKana(String nameKana) {
        this.nameKana = nameKana;
    }

    /**
     * 親カテゴリを取得する。
     *
     * @return
     */
    public GoodsCategory getParentCategory() {
        return this.parentCategory;
    }

    /**
     * 親カテゴリを設定する。
     *
     * @param parentCategory
     */
    public void setParentCategory(GoodsCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    /**
     * 親カテゴリNo.を取得する。
     *
     * @return
     */
    public int getParentCategoryNo() {
        GoodsCategory p = this.getParentCategory();
        if (p == null) {
            return 0;
        }
        return p.categoryNo;
    }

    /**
     * 削除フラグを取得する。
     *
     * @return delFlg
     */
    public Character getDelFlg() {
        return delFlg;
    }

    /**
     * 削除フラグを設定する。
     *
     * @param delFlg セットする delFlg
     */
    public void setDelFlg(Character delFlg) {
        this.delFlg = delFlg;
    }

    /**
     * 登録ユーザーを取得する。
     *
     * @return registUser
     */
    @Override
    public YuimarlUser getRegistUser() {
        return registUser;
    }

    /**
     * 登録ユーザーを設定する。
     *
     * @param registUser セットする registUser
     */
    @Override
    public void setRegistUser(YuimarlUser registUser) {
        this.registUser = registUser;
    }

    /**
     * 登録日時を取得する。
     *
     * @return registTime
     */
    @Override
    public Date getRegistTime() {
        return registTime;
    }

    /**
     * 登録日時を設定する。
     *
     * @param registTime セットする registTime
     */
    @Override
    public void setRegistTime(Date registTime) {
        this.registTime = registTime;
    }

    /**
     * 更新ユーザーを取得する。
     *
     * @return updateUser
     */
    @Override
    public YuimarlUser getUpdateUser() {
        return updateUser;
    }

    /**
     * 更新ユーザーを設定する。
     *
     * @param updateUser セットする updateUser
     */
    @Override
    public void setUpdateUser(YuimarlUser updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * 更新日時を取得する。
     *
     * @return updateTime
     */
    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 更新日時を設定する。
     *
     * @param updateTime セットする updateTime
     */
    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * バージョン番号を取得する。
     *
     * @return
     */
    @Override
    public Integer getVersionNo() {
        return this.versionNo;
    }

    /**
     * バージョン番号を設定する。
     *
     * @param versionNo
     */
    @Override
    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }
}
