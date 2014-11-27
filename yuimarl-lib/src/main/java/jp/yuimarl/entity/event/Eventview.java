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
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import jp.yuimarl.entity.YuimarlUser;

/**
 * イベントビュークラス
 * <p>
 * イベントビューの情報を管理するクラス。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Eventview.findAll", query = "SELECT e FROM Eventview e WHERE e.delFlg = '0'"),
    @NamedQuery(name = "Eventview.findByEventviewNo", query = "SELECT e FROM Eventview e WHERE e.eventviewNo = :eventviewNo AND e.delFlg = '0'")})
public class Eventview implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * イベントビューNo.
     */
    @TableGenerator(name = "EVENTVIEW_NO_GEN", table = "GENERATED_ID", pkColumnName = "KEY_NAME", valueColumnName = "VALUE", pkColumnValue = "EVENTVIEW_NO_MAX", allocationSize = 1)
    @Id
    @Basic(optional = false)
    @Column(name = "EVENTVIEW_NO", nullable = false)
    private Integer eventviewNo;

    /**
     * オーナー
     */
    @JoinColumn(name = "OWNER", referencedColumnName = "USER_NO")
    @ManyToOne
    private YuimarlUser owner;

    /**
     * 名前
     */
    @Column(length = 100)
    private String name;

    /**
     * 表示順
     */
    private Integer seq;

    /**
     * 日本の休日
     * <p>
     * 1:日本の休日を使用する, 0:使用しない
     * </p>
     */
    @Column(name = "JAPANESE_HOLIDAY")
    private Character japaneseHoliday = '0';

    /**
     * 展開年
     * <p>
     * yyyy,yyyy,・・・
     * </p>
     */
    @Column(name = "SPREAD_YEARS")
    private String spreadYears;

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
     * イベントビューセットの配列
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventview")
    private Collection<Eventviewset> eventviewsetCollection;

    /**
     * コンストラクタ
     */
    public Eventview() {
    }

    /**
     * イベントビューNo.を取得する。
     *
     * @return
     */
    public Integer getEventviewNo() {
        return eventviewNo;
    }

    /**
     * イベントビューNo.を設定する。
     *
     * @param eventviewNo
     */
    public void setEventviewNo(Integer eventviewNo) {
        this.eventviewNo = eventviewNo;
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
     * 表示順を取得する。
     *
     * @return
     */
    public Integer getSeq() {
        return seq;
    }

    /**
     * 表示順を設定する。
     *
     * @param seq
     */
    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    /**
     * 日本の休日を取得する。
     *
     * @return
     */
    public Character getJapaneseHoliday() {
        return japaneseHoliday;
    }

    /**
     * 日本の休日を設定する。
     *
     * @param japaneseHoliday
     */
    public void setJapaneseHoliday(Character japaneseHoliday) {
        this.japaneseHoliday = japaneseHoliday;
    }

    /**
     * 展開年を取得する。
     *
     * @return
     */
    public String getSpreadYears() {
        return spreadYears;
    }

    /**
     * 展開年を設定する。
     *
     * @param spreadYears
     */
    public void setSpreadYears(String spreadYears) {
        this.spreadYears = spreadYears;
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
     * イベントビューセットの配列を取得する。
     *
     * @return
     */
    @XmlTransient
    public Collection<Eventviewset> getEventviewsetCollection() {
        return eventviewsetCollection;
    }

    /**
     * イベントビューセットの配列を設定する。
     *
     * @param eventviewsetCollection
     */
    public void setEventviewsetCollection(Collection<Eventviewset> eventviewsetCollection) {
        this.eventviewsetCollection = eventviewsetCollection;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventviewNo != null ? eventviewNo.hashCode() : 0);
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
        if (!(object instanceof Eventview)) {
            return false;
        }
        Eventview other = (Eventview) object;
        return (this.eventviewNo != null || other.eventviewNo == null) && (this.eventviewNo == null || this.eventviewNo.equals(other.eventviewNo));
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "jp.yuimarl.entity.Eventview[ eventviewNo=" + eventviewNo + " ]";
    }

}
