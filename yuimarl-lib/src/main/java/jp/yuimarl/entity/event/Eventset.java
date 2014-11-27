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
package jp.yuimarl.entity.event;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.xml.bind.annotation.XmlRootElement;
import jp.yuimarl.entity.YuimarlUser;

/**
 * イベントセットクラス
 * <p>
 * イベントの集合を管理するクラス。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Entity
@Table(name = "EVENTSET")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Eventset.findAll", query = "SELECT e FROM Eventset e WHERE e.delFlg = '0'"),
    @NamedQuery(name = "Eventset.findByEventsetNo", query = "SELECT e FROM Eventset e WHERE e.eventsetNo = :eventsetNo AND e.delFlg = '0'"),
    @NamedQuery(name = "Eventset.findByName", query = "SELECT e FROM Eventset e WHERE e.name = :name AND e.delFlg = '0'")})
public class Eventset implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * イベントセットNo.
     * <p>
     * 1:日本の休日・記念日
     * </p>
     */
    @TableGenerator(name = "EVENTSET_NO_GEN", table = "GENERATED_ID", pkColumnName = "KEY_NAME", valueColumnName = "VALUE", pkColumnValue = "EVENTSET_NO_MAX", allocationSize = 1)
    @Id
    @Basic(optional = false)
    @Column(name = "EVENTSET_NO")
    private Integer eventsetNo;

    /**
     * 名前
     */
    @Column(length = 100)
    private String name;

    /**
     * オーナー
     */
    @JoinColumn(name = "OWNER", referencedColumnName = "USER_NO")
    @ManyToOne
    private YuimarlUser owner;

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
     * バージョン番号
     */
    @Version
    @Column(name = "VERSION_NO")
    private Integer versionNo;

    /**
     * コンストラクタ
     */
    public Eventset() {
    }

    /**
     * コンストラクタ
     *
     * @param eventsetNo
     */
    public Eventset(Integer eventsetNo) {
        this.eventsetNo = eventsetNo;
    }

    /**
     * イベントセットNo.を取得する。
     *
     * @return
     */
    public Integer getEventsetNo() {
        return eventsetNo;
    }

    /**
     * イベントセットNo.を設定する。
     *
     * @param eventsetNo
     */
    public void setEventsetNo(Integer eventsetNo) {
        this.eventsetNo = eventsetNo;
    }

    /**
     * 名前を取得する。
     *
     * @return
     */
    public String getName() {
        return name;
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
     * 削除フラグを取得する。
     *
     * @return
     */
    public Character getDelFlg() {
        return delFlg;
    }

    /**
     * 削除フラグを設定する。
     *
     * @param delFlg
     */
    public void setDelFlg(Character delFlg) {
        this.delFlg = delFlg;
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
     * オーナーを取得する。
     *
     * @return
     */
    public YuimarlUser getOwner() {
        return owner;
    }

    /**
     * オーナーを設定する。
     *
     * @param owner
     */
    public void setOwner(YuimarlUser owner) {
        this.owner = owner;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventsetNo != null ? eventsetNo.hashCode() : 0);
        return hash;
    }

    /**
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Eventset)) {
            return false;
        }
        Eventset other = (Eventset) object;
        return (this.eventsetNo != null || other.eventsetNo == null) && (this.eventsetNo == null || this.eventsetNo.equals(other.eventsetNo));
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "jp.yuimarl.entity.Eventset[ eventsetNo=" + eventsetNo + " ]";
    }

}
