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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import jp.yuimarl.util.YuimarlUtil;

/**
 * パーティ関連クラス
 * <p>
 * パーティ間の関連情報を保持する。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Entity
@Table(name = "PARTY_RELATION")
public class PartyRelation implements EntityBasic, Serializable {

    /**
     * 組織←→創立者
     */
    public static final Integer RELATION_TYPE_FOUNDER = 1;
    /**
     * 組織←→代表者
     */
    public static final Integer RELATION_TYPE_DELEGATE = 2;
    /**
     * 親←→子
     */
    public static final Integer RELATION_TYPE_PARENT = 3;
    /**
     * きょうだい←→きょうだい
     */
    public static final Integer RELATION_TYPE_BROTHER = 4;
    /**
     * 夫←→妻
     */
    public static final Integer RELATION_TYPE_COUPLE = 5;
    /**
     * 親戚←→親戚
     */
    public static final Integer RELATION_TYPE_RELATIVE = 6;
    /**
     * パートナー←→パートナー
     */
    public static final Integer RELATION_TYPE_PARTNER = 7;
    /**
     * 友人←→友人
     */
    public static final Integer RELATION_TYPE_FRIEND = 8;
    /**
     * 取引先←→取引先
     */
    public static final Integer RELATION_TYPE_ACQUAINTANCE = 9;
    /**
     * 勤務先←→勤務者
     */
    public static final Integer RELATION_TYPE_EMPLOY = 10;
    /**
     * 所属先←→所属者
     */
    public static final Integer RELATION_TYPE_BELONG = 11;
    /**
     * 購入先←→顧客
     */
    public static final Integer RELATION_TYPE_CUSTOMER = 12;
    /**
     * 所有者←→所有物
     */
    public static final Integer RELATION_TYPE_OWNER = 13;

    /**
     * 関連1
     */
    public static final String[] relationTypes1 = {"組織", "組織", "親", "きょうだい",
        "夫", "親戚", "パートナー", "友人", "取引先", "勤務先", "所属先", "購入先", "所有者"};
    /**
     * 関連2
     */
    public static final String[] relationTypes2 = {"創立者", "代表者", "子",
        "きょうだい", "妻", "親戚", "パートナー", "友人", "取引先", "勤務者", "所属者", "顧客", "所有物"};

    /**
     * Party関連No.
     */
    @TableGenerator(name = "RELATION_NO_GEN", table = "GENERATED_ID", pkColumnName = "KEY_NAME", valueColumnName = "VALUE", pkColumnValue = "RELATION_NO_MAX", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "RELATION_NO_GEN")
    @Column(name = "RELATION_NO")
    private Integer relationNo;

    /**
     * Party関連種別
     */
    @Column(name = "RELATION_TYPE")
    private Integer relationType;

    /**
     * Party1
     */
    @ManyToOne(targetEntity = Party.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "PARTY1", referencedColumnName = "PARTY_NO")
    private Party party1;

    /**
     * 役割1
     */
    @Column(length = 30)
    private String role1;

    /**
     * Party2
     */
    @ManyToOne(targetEntity = Party.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "PARTY2", referencedColumnName = "PARTY_NO")
    private Party party2;

    /**
     * 役割2
     */
    @Column(length = 30)
    private String role2;

    /**
     * 開始日
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "TERM_FROM")
    private Date termFrom;

    /**
     * 終了日
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "TERM_TO")
    private Date termTo;

    /**
     * 登録ユーザー
     */
    @ManyToOne(targetEntity = YuimarlUser.class, fetch = FetchType.LAZY)
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
    @ManyToOne(targetEntity = YuimarlUser.class, fetch = FetchType.LAZY)
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
     * Party関連No.を取得する。
     *
     * @return relationNo
     */
    public Integer getRelationNo() {
        return relationNo;
    }

    /**
     * Party関連No.を設定する。
     *
     * @param relationNo セットする relationNo
     */
    public void setRelationNo(Integer relationNo) {
        this.relationNo = relationNo;
    }

    /**
     * Party関連種別を取得する。
     *
     * @return relationType
     */
    public Integer getRelationType() {
        return relationType;
    }

    /**
     * Party関連種別を設定する。
     *
     * @param relationType セットする relationType
     */
    public void setRelationType(Integer relationType) {
        this.relationType = relationType;
    }

    /**
     * Party1を取得する。
     *
     * @return party1
     */
    public Party getParty1() {
        return party1;
    }

    /**
     * Party1を設定する。
     *
     * @param party1 セットする party1
     */
    public void setParty1(Party party1) {
        this.party1 = party1;
    }

    /**
     * 役割1を取得する。
     *
     * @return role1
     */
    public String getRole1() {
        return role1;
    }

    /**
     * 役割1を設定する。
     *
     * @param role1 セットする role1
     */
    public void setRole1(String role1) {
        this.role1 = role1;
    }

    /**
     * Party2を取得する。
     *
     * @return party2
     */
    public Party getParty2() {
        return party2;
    }

    /**
     * Party2を設定する。
     *
     * @param party2 セットする party2
     */
    public void setParty2(Party party2) {
        this.party2 = party2;
    }

    /**
     * 役割2を取得する。
     *
     * @return role2
     */
    public String getRole2() {
        return role2;
    }

    /**
     * 役割2を設定する。
     *
     * @param role2 セットする role2
     */
    public void setRole2(String role2) {
        this.role2 = role2;
    }

    /**
     * 開始日を取得する。
     *
     * @return termFrom
     */
    public Date getTermFrom() {
        return termFrom;
    }

    /**
     * 開始日を設定する。
     *
     * @param termFrom セットする termFrom
     */
    public void setTermFrom(Date termFrom) {
        this.termFrom = termFrom;
    }

    /**
     * 終了日を取得する。
     *
     * @return termTo
     */
    public Date getTermTo() {
        return termTo;
    }

    /**
     * 終了日を設定する。
     *
     * @param termTo セットする termTo
     */
    public void setTermTo(Date termTo) {
        this.termTo = termTo;
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
     * @return versionNo
     */
    @Override
    public Integer getVersionNo() {
        return versionNo;
    }

    /**
     * バージョン番号を設定する。
     *
     * @param versionNo セットする versionNo
     */
    @Override
    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    /**
     * Party関連種別名を取得する。
     *
     * @return
     */
    public String getRelationTypeName() {
        if (this.relationType == null) {
            return null;
        }
        int i = this.relationType - 1;
        return (relationTypes1[i] + "←→" + relationTypes2[i]);
    }

    /**
     * 開始日の和暦年を取得する。
     *
     * @return
     */
    public String getTermFromWareki() {
        return YuimarlUtil.dateToWareki(this.termFrom, true);
    }

    /**
     * 終了日の和暦年を取得する。
     *
     * @return
     */
    public String getTermToWareki() {
        return YuimarlUtil.dateToWareki(this.termTo, true);
    }
}
