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
import javax.persistence.Basic;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Yuimarl ユーザークラス
 * <p>
 * Yuimarl にログインできるユーザーの情報を保持する。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Entity
@Table(name = "YUIMARL_USER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YuimarlUser.findAll", query = "SELECT y FROM YuimarlUser y WHERE y.delFlg = '0' ORDER BY y.userId"),
    @NamedQuery(name = "YuimarlUser.findByUserNo", query = "SELECT y FROM YuimarlUser y WHERE y.delFlg = '0' AND y.userNo = :userNo"),
    @NamedQuery(name = "YuimarlUser.findByUserId", query = "SELECT y FROM YuimarlUser y WHERE y.delFlg = '0' AND y.userId = :userId"),
    @NamedQuery(name = "YuimarlUser.findByPartyNo", query = "SELECT y FROM YuimarlUser y WHERE y.delFlg = '0' AND y.party.partyNo = :partyNo")})
public class YuimarlUser implements Serializable {

    /**
     * 権限マップの文字列長
     */
    public static final int AUTH_MAP_LENGTH = 17;
    /**
     * パスワード変更権限
     */
    public static final int AUTH_PASSWORD_CHANGE = 0;
    /**
     * ユーザー 照会権限
     */
    public static final int AUTH_USER_VIEW = 1;
    /**
     * ユーザー 登録権限
     */
    public static final int AUTH_USER_CREATE = 2;
    /**
     * ユーザー 更新権限
     */
    public static final int AUTH_USER_UPDATE = 3;
    /**
     * ユーザー 削除権限
     */
    public static final int AUTH_USER_DELETE = 4;
    /**
     * ユーザー権限 照会権限
     */
    public static final int AUTH_USER_AUTH_VIEW = 5;
    /**
     * ユーザー権限 登録権限
     */
    public static final int AUTH_USER_AUTH_CREATE = 6;
    /**
     * ユーザー権限 更新権限
     */
    public static final int AUTH_USER_AUTH_UPDATE = 7;
    /**
     * ユーザー権限 削除権限
     */
    public static final int AUTH_USER_AUTH_DELETE = 8;
    /**
     * 物品カテゴリー 照会権限
     */
    public static final int AUTH_GOODS_CATEGORY_VIEW = 9;
    /**
     * 物品カテゴリー 登録権限
     */
    public static final int AUTH_GOODS_CATEGORY_CREATE = 10;
    /**
     * 物品カテゴリー 更新権限
     */
    public static final int AUTH_GOODS_CATEGORY_UPDATE = 11;
    /**
     * 物品カテゴリー 削除権限
     */
    public static final int AUTH_GOODS_CATEGORY_DELETE = 12;
    /**
     * 人/組織/物 照会権限
     */
    public static final int AUTH_PARTY_VIEW = 13;
    /**
     * 人/組織/物 登録権限
     */
    public static final int AUTH_PARTY_CREATE = 14;
    /**
     * 人/組織/物 更新権限
     */
    public static final int AUTH_PARTY_UPDATE = 15;
    /**
     * 人/組織/物 削除権限
     */
    public static final int AUTH_PARTY_DELETE = 16;
    /**
     * 権限: 可能
     */
    public static final char AUTH_ENABLE = '1';
    /**
     * 権限: 不可
     */
    public static final char AUTH_DISABLE = '0';

    private static final long serialVersionUID = 1L;

    /**
     * User No.
     */
    @TableGenerator(name = "USER_NO_GEN", table = "GENERATED_ID", pkColumnName = "KEY_NAME", valueColumnName = "VALUE", pkColumnValue = "USER_NO_MAX", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "USER_NO_GEN")
    @Column(name = "USER_NO")
    private Integer userNo;

    /**
     * ユーザーID
     */
    @Column(name = "USER_ID")
    @Size(min = 3, max = 10, message = "{yuimarlUser.userId}")
    private String userId;

    /**
     * パスワード
     * <p>
     * DBには、パスワードの文字列を直接保存するのではなく、パスワードから生成たMD5を保存する。
     * </p>
     */
    @Basic(optional = false)
    @Column(name = "PASSWORD")
    private String password;

    /**
     * Party
     */
    @ManyToOne
    @JoinColumn(name = "PARTY", referencedColumnName = "PARTY_NO")
    private Party party;

    /**
     * 権限マップ
     * <p>
     * AUTH_MAP_LENGTHの長さの文字列内の各文字で、権限あり('1')、権限なし('0')を設定する。
     * </p>
     */
    @Column(name = "AUTH_MAP")
    private String authMap;

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
    @Column(name = "REGIST_TIME")
    @Temporal(TemporalType.TIMESTAMP)
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
    @Column(name = "UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    /**
     * 権限登録ユーザー
     */
    @ManyToOne
    @JoinColumn(name = "AUTH_REGIST_USER", referencedColumnName = "USER_NO")
    private YuimarlUser authRegistUser;

    /**
     * 権限登録日時
     */
    @Column(name = "AUTH_REGIST_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date authRegistTime;

    /**
     * 権限更新ユーザー
     */
    @ManyToOne
    @JoinColumn(name = "AUTH_UPDATE_USER", referencedColumnName = "USER_NO")
    private YuimarlUser authUpdateUser;

    /**
     * 権限更新日時
     */
    @Column(name = "AUTH_UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date authUpdateTime;

    /**
     * バージョン番号
     */
    @Version
    @Column(name = "VERSION_NO")
    private Integer versionNo;

    /**
     * コンストラクタ
     */
    public YuimarlUser() {
    }

    /**
     * コンストラクタ
     *
     * @param userNo
     */
    public YuimarlUser(Integer userNo) {
        this.userNo = userNo;
    }

    /**
     * User No.を取得する。
     *
     * @return
     */
    public Integer getUserNo() {
        return userNo;
    }

    /**
     * User No.を設定する。
     *
     * @param userNo
     */
    public void setUserNo(Integer userNo) {
        this.userNo = userNo;
    }

    /**
     * ユーザーIDを取得する。
     *
     * @return
     */
    public String getUserId() {
        return userId;
    }

    /**
     * ユーザーIDを設定する。
     *
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * パスワードを取得する。
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * パスワードを設定する。
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Partyを取得する。
     *
     * @return
     */
    public Party getParty() {
        return party;
    }

    /**
     * Partyを設定する。
     *
     * @param party
     */
    public void setParty(Party party) {
        this.party = party;
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
     * @return
     */
    public YuimarlUser getRegistUser() {
        return registUser;
    }

    /**
     * 登録ユーザーを設定する。
     *
     * @param registUser
     */
    public void setRegistUser(YuimarlUser registUser) {
        this.registUser = registUser;
    }

    /**
     * 登録日時を取得する。
     *
     * @return
     */
    public Date getRegistTime() {
        return registTime;
    }

    /**
     * 登録日時を設定する。
     *
     * @param registTime
     */
    public void setRegistTime(Date registTime) {
        this.registTime = registTime;
    }

    /**
     * 更新ユーザーを取得する。
     *
     * @return
     */
    public YuimarlUser getUpdateUser() {
        return updateUser;
    }

    /**
     * 更新ユーザーを設定する。
     *
     * @param updateUser
     */
    public void setUpdateUser(YuimarlUser updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * 更新日時を取得する。
     *
     * @return
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 更新日時を設定する。
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * バージョン番号を取得する。
     *
     * @return
     */
    public Integer getVersionNo() {
        return versionNo;
    }

    /**
     * バージョン番号を設定する。
     *
     * @param versionNo
     */
    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    /**
     * 権限マップを取得する。
     *
     * @return
     */
    public String getAuthMap() {
        return authMap;
    }

    /**
     * 権限マップを設定する。
     *
     * @param authMap
     */
    public void setAuthMap(String authMap) {
        this.authMap = authMap;
    }

    /**
     * 権限登録ユーザーを取得する。
     *
     * @return
     */
    public YuimarlUser getAuthRegistUser() {
        return authRegistUser;
    }

    /**
     * 権限登録ユーザーを設定する。
     *
     * @param authRegistUser
     */
    public void setAuthRegistUser(YuimarlUser authRegistUser) {
        this.authRegistUser = authRegistUser;
    }

    /**
     * 権限登録日時を取得する。
     *
     * @return
     */
    public Date getAuthRegistTime() {
        return authRegistTime;
    }

    /**
     * 権限登録日時を設定する。
     *
     * @param arthRegistTime
     */
    public void setAuthRegistTime(Date arthRegistTime) {
        this.authRegistTime = arthRegistTime;
    }

    /**
     * 権限更新ユーザーを取得する。
     *
     * @return
     */
    public YuimarlUser getAuthUpdateUser() {
        return authUpdateUser;
    }

    /**
     * 権限更新ユーザーを設定する。
     *
     * @param authUpdateUser
     */
    public void setAuthUpdateUser(YuimarlUser authUpdateUser) {
        this.authUpdateUser = authUpdateUser;
    }

    /**
     * 権限更新日時を取得する。
     *
     * @return
     */
    public Date getAuthUpdateTime() {
        return authUpdateTime;
    }

    /**
     * 権限更新日時を設定する。
     *
     * @param authUpdateTime
     */
    public void setAuthUpdateTime(Date authUpdateTime) {
        this.authUpdateTime = authUpdateTime;
    }

    /**
     * ハッシュコードを生成する。
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userNo != null ? userNo.hashCode() : 0);
        return hash;
    }

    /**
     * 比較する。
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YuimarlUser)) {
            return false;
        }
        YuimarlUser other = (YuimarlUser) object;
        if ((this.userNo == null && other.userNo != null) || (this.userNo != null && !this.userNo.equals(other.userNo))) {
            return false;
        }
        return true;
    }

    /**
     * 文字列にする。
     *
     * @return
     */
    @Override
    public String toString() {
        return "jp.yuimarl.yuimarl.entity.YuimarlUser[ userNo=" + userNo + " ]";
    }

}
