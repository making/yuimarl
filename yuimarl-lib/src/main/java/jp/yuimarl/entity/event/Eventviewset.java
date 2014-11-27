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
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import jp.yuimarl.entity.YuimarlUser;

/**
 * イベントビューセットクラス
 * <p>
 * イベントビューとイベントセットの関連情報を管理するクラス。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Eventviewset.findAll", query = "SELECT e FROM Eventviewset e"),
    @NamedQuery(name = "Eventviewset.findByEventview", query = "SELECT e FROM Eventviewset e WHERE e.eventviewsetPK.eventview = :eventview"),
    @NamedQuery(name = "Eventviewset.findByEventset", query = "SELECT e FROM Eventviewset e WHERE e.eventviewsetPK.eventset = :eventset")})
public class Eventviewset implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * イベントビューセット主キー
     */
    @EmbeddedId
    protected EventviewsetPK eventviewsetPK;

    /**
     * イベントビュー
     */
    @JoinColumn(name = "EVENTVIEW", referencedColumnName = "EVENTVIEW_NO", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Eventview eventview;

    /**
     * イベントセット
     */
    @JoinColumn(name = "EVENTSET", referencedColumnName = "EVENTSET_NO", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Eventset eventset;

    /**
     * オーナー権限
     * <p>
     * 1:オーナー, 0:オーナーでない
     * </p>
     */
    @Column(name = "AUTH_OWNER")
    private Character authOwner;

    /**
     * 更新権限
     * <p>
     * 1:更新可, 0:更新不可
     * </p>
     */
    @Column(name = "AUTH_UPDATE")
    private Character authUpdate;

    /**
     * 登録ユーザー
     */
    @JoinColumn(name = "REGIST_USER", referencedColumnName = "USER_NO")
    @ManyToOne
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
    @JoinColumn(name = "UPDATE_USER", referencedColumnName = "USER_NO")
    @ManyToOne
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
    public Eventviewset() {
    }

    /**
     * コンストラクタ
     *
     * @param eventviewsetPK
     */
    public Eventviewset(EventviewsetPK eventviewsetPK) {
        this.eventviewsetPK = eventviewsetPK;
    }

    /**
     * コンストラクタ
     *
     * @param eventviewNo
     * @param eventsetNo
     */
    public Eventviewset(int eventviewNo, int eventsetNo) {
        this.eventviewsetPK = new EventviewsetPK(eventviewNo, eventsetNo);
    }

    /**
     * イベントビューセット主キーを取得する。
     *
     * @return
     */
    public EventviewsetPK getEventviewsetPK() {
        return eventviewsetPK;
    }

    /**
     * イベントビューセット主キーを設定する。
     *
     * @param eventviewsetPK
     */
    public void setEventviewsetPK(EventviewsetPK eventviewsetPK) {
        this.eventviewsetPK = eventviewsetPK;
    }

    /**
     * イベントビューを取得する。
     *
     * @return
     */
    public Eventview getEventview() {
        return eventview;
    }

    /**
     * イベントビューを設定する。
     *
     * @param eventview
     */
    public void setEventview(Eventview eventview) {
        this.eventview = eventview;
    }

    /**
     * イベントセットを取得する。
     *
     * @return
     */
    public Eventset getEventset() {
        return eventset;
    }

    /**
     * イベントセットを設定する。
     *
     * @param eventset
     */
    public void setEventset(Eventset eventset) {
        this.eventset = eventset;
    }

    /**
     * オーナー権限を取得する。
     *
     * @return
     */
    public Character getAuthOwner() {
        return authOwner;
    }

    /**
     * オーナー権限を設定する。
     *
     * @param authOwner
     */
    public void setAuthOwner(Character authOwner) {
        this.authOwner = authOwner;
    }

    /**
     * 更新権限を取得する。
     *
     * @return
     */
    public Character getAuthUpdate() {
        return authUpdate;
    }

    /**
     * 更新権限を設定する。
     *
     * @param authUpdate
     */
    public void setAuthUpdate(Character authUpdate) {
        this.authUpdate = authUpdate;
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
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventviewsetPK != null ? eventviewsetPK.hashCode() : 0);
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
        if (!(object instanceof Eventviewset)) {
            return false;
        }
        Eventviewset other = (Eventviewset) object;
        return (this.eventviewsetPK != null || other.eventviewsetPK == null) && (this.eventviewsetPK == null || this.eventviewsetPK.equals(other.eventviewsetPK));
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "jp.yuimarl.entity.Eventviewset[ eventviewsetPK=" + eventviewsetPK + " ]";
    }

}
