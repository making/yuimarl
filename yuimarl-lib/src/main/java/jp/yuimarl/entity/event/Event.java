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
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
 * イベントクラス
 * <p>
 * イベントの情報を管理するクラス。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Entity
@Table(name = "EVENT")
@XmlRootElement
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "EVENT_CLASS", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue(value = "2")
@NamedQueries({
    @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e WHERE e.delFlg = '0'"),
    @NamedQuery(name = "Event.findByEventNo", query = "SELECT e FROM Event e WHERE e.eventNo = :eventNo AND e.delFlg = '0'"),
    @NamedQuery(name = "Event.findByEventsetNo", query = "SELECT e FROM Event e WHERE e.eventsetNo = :eventsetNo AND e.delFlg = '0'"),
    @NamedQuery(name = "Event.findByName", query = "SELECT e FROM Event e WHERE e.name = :name AND e.delFlg = '0'")})
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 休日・記念日イベント
     */
    public static final char EVENT_CLASS_HOLIDAY = '1';
    /**
     * 単イベント
     */
    public static final char EVENT_CLASS_SINGLE = '2';
    /**
     * 日イベント
     */
    public static final char EVENT_CLASS_DAILY = '3';
    /**
     * 週イベント
     */
    public static final char EVENT_CLASS_WEEKLY = '4';
    /**
     * 月イベント
     */
    public static final char EVENT_CLASS_MONTHLY = '5';
    /**
     * 年イベント
     */
    public static final char EVENT_CLASS_YEARLY = '6';
    /**
     * タスク
     */
    public static final char EVENT_CLASS_TASK = '7';

    /**
     * イベントNo.
     */
    @TableGenerator(name = "EVENT_NO_GEN", table = "GENERATED_ID", pkColumnName = "KEY_NAME", valueColumnName = "VALUE", pkColumnValue = "EVENT_NO_MAX", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "EVENT_NO_GEN")
    @Column(name = "EVENT_NO")
    private Integer eventNo;

    /**
     * イベントセットNo.
     */
    @Column(name = "EVENTSET_NO")
    private Integer eventsetNo;

    /**
     * イベント型
     * <p>
     * 1:休日・記念日, 2:単イベント, 3:日イベント, 4:週イベント, 5:月イベント, 6:年イベント, 7:タスク
     * </p>
     */
    @Basic(optional = false)
    @Column(name = "EVENT_CLASS")
    private Character eventClass = EVENT_CLASS_SINGLE;

    /**
     * 詳細種別
     */
    @Column(name = "EVENT_TYPE")
    private Integer eventType = 0;

    /**
     * 名前
     */
    @Column(length = 50)
    private String name;

    /**
     * 期間開始日
     * <p>
     * yyyyMMdd<br/>
     * 00000000:開始無期限
     * </p>
     */
    @Column(name = "TERM_FROM", length = 8)
    private String termFrom;

    /**
     * 期間終了日
     * <p>
     * yyyyMMdd<br/>
     * 99999999:終了無期限
     * </p>
     */
    @Column(name = "TERM_TO", length = 8)
    private String termTo;

    /**
     * 開始時刻
     * <p>
     * HHmm<br/>
     * " ":終日
     * </p>
     */
    @Column(name = "START_TIME", length = 4)
    private String startTime = " ";

    /**
     * 終了時刻
     * <p>
     * HHmm<br/>
     * " ":終日
     * </p>
     */
    @Column(name = "END_TIME", length = 4)
    private String endTime = " ";

    /**
     * 場所
     */
    @Column(length = 50)
    private String place;

    /**
     * 関係者
     */
    @Column(length = 2048)
    private String affiliate;

    /**
     * 日曜日
     * <p>
     * 1:実施, 0:実施しない
     * </p>
     */
    @Column(name = "SUNDAY")
    private Character sunday = '1';

    /**
     * 月曜日
     * <p>
     * 1:実施, 0:実施しない
     * </p>
     */
    @Column(name = "MONDAY")
    private Character monday = '1';

    /**
     * 火曜日
     * <p>
     * 1:実施, 0:実施しない
     * </p>
     */
    @Column(name = "TUESDAY")
    private Character tuesday = '1';

    /**
     * 水曜日
     * <p>
     * 1:実施, 0:実施しない
     * </p>
     */
    @Column(name = "WEDNESDAY")
    private Character wednesday = '1';

    /**
     * 木曜日
     * <p>
     * 1:実施, 0:実施しない
     * </p>
     */
    @Column(name = "THURSDAY")
    private Character thursday = '1';

    /**
     * 金曜日
     * <p>
     * 1:実施, 0:実施しない
     * </p>
     */
    @Column(name = "FRIDAY")
    private Character friday = '1';

    /**
     * 土曜日
     * <p>
     * 1:実施, 0:実施しない
     * </p>
     */
    @Column(name = "SATURDAY")
    private Character saturday = '1';

    /**
     * 祝日
     * <p>
     * 1:実施, 0:実施しない
     * </p>
     */
    @Column(name = "HOLIDAY")
    private Character holiday = '1';

    /**
     * 例外日の扱い
     * <p>
     * 0:中止, 1:前にずらす, 2:後ろにずらす
     * </p>
     */
    @Column(name = "EXCEPT_MOVE")
    private Character exceptMove = '0';

    /**
     * 実施日のリスト
     * <p>
     * yyyyMMdd,yyyyMMdd,・・・
     * </p>
     */
    @Column(name = "INCLUDE_DAYS", length = 2048)
    private String includeDays;

    /**
     * 除外日のリスト
     * <p>
     * yyyyMMdd,yyyyMMdd,・・・
     * </p>
     */
    @Column(name = "EXCLUDE_DAYS", length = 2048)
    private String excludeDays;

    /**
     * 公開
     * <p>
     * 0:他者に非公開, 1:他者に公開, 2:他者には「予定あり」と表示
     * </p>
     */
    @Column(name = "OPEN_OTHERS")
    private Character openOthers = '0';

    /**
     * 表示色
     * <p>
     * 16進数RGB, 例: 白=FFFFFF
     * </p>
     */
    @Column(length = 6)
    private String color;

    /**
     * メモ
     */
    @Column(length = 4096)
    private String memo;

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
    public Event() {
    }

    /**
     * イベントNo.を取得する。
     *
     * @return
     */
    public Integer getEventNo() {
        return eventNo;
    }

    /**
     * イベントNo.を設定する。
     *
     * @param eventNo
     */
    public void setEventNo(Integer eventNo) {
        this.eventNo = eventNo;
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
     * イベント型を取得する。
     *
     * @return
     */
    public Character getEventClass() {
        return eventClass;
    }

    /**
     * イベント型を設定する。
     *
     * @param eventClass
     */
    public void setEventClass(Character eventClass) {
        this.eventClass = eventClass;
    }

    /**
     * 詳細種別を取得する。
     *
     * @return
     */
    public Integer getEventType() {
        return eventType;
    }

    /**
     * 詳細種別を設定する。
     *
     * @param eventType
     */
    public void setEventType(Integer eventType) {
        this.eventType = eventType;
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
     * 期間開始日を取得する。
     *
     * @return
     */
    public String getTermFrom() {
        return termFrom;
    }

    /**
     * 期間開始日を設定する。
     *
     * @param termFrom
     */
    public void setTermFrom(String termFrom) {
        this.termFrom = termFrom;
    }

    /**
     * 期間終了日を取得する。
     *
     * @return
     */
    public String getTermTo() {
        return termTo;
    }

    /**
     * 期間終了日を設定する。
     *
     * @param termTo
     */
    public void setTermTo(String termTo) {
        this.termTo = termTo;
    }

    /**
     * 開始時刻を取得する。
     *
     * @return
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * 開始時刻を設定する。
     *
     * @param startTime
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * 終了時刻を取得する。
     *
     * @return
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * 終了時刻を設定する。
     *
     * @param endTime
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * 場所を取得する。
     *
     * @return
     */
    public String getPlace() {
        return place;
    }

    /**
     * 場所を設定する。
     *
     * @param place
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * 関係者を取得する。
     *
     * @return
     */
    public String getAffiliate() {
        return affiliate;
    }

    /**
     * 関係者を設定する。
     *
     * @param affiliate
     */
    public void setAffiliate(String affiliate) {
        this.affiliate = affiliate;
    }

    /**
     * 日曜日を取得する。
     *
     * @return
     */
    public Character getSunday() {
        return sunday;
    }

    /**
     * 日曜日を設定する。
     *
     * @param sunday
     */
    public void setSunday(Character sunday) {
        this.sunday = sunday;
    }

    /**
     * 月曜日を取得する。
     *
     * @return
     */
    public Character getMonday() {
        return monday;
    }

    /**
     * 月曜日を設定する。
     *
     * @param monday
     */
    public void setMonday(Character monday) {
        this.monday = monday;
    }

    /**
     * 火曜日を取得する。
     *
     * @return
     */
    public Character getTuesday() {
        return tuesday;
    }

    /**
     * 火曜日を設定する。
     *
     * @param tuesday
     */
    public void setTuesday(Character tuesday) {
        this.tuesday = tuesday;
    }

    /**
     * 水曜日を取得する。
     *
     * @return
     */
    public Character getWednesday() {
        return wednesday;
    }

    /**
     * 水曜日を設定する。
     *
     * @param wednesday
     */
    public void setWednesday(Character wednesday) {
        this.wednesday = wednesday;
    }

    /**
     * 木曜日を取得する。
     *
     * @return
     */
    public Character getThursday() {
        return thursday;
    }

    /**
     * 木曜日を設定する。
     *
     * @param thursday
     */
    public void setThursday(Character thursday) {
        this.thursday = thursday;
    }

    /**
     * 金曜日を取得する。
     *
     * @return
     */
    public Character getFriday() {
        return friday;
    }

    /**
     * 金曜日を設定する。
     *
     * @param friday
     */
    public void setFriday(Character friday) {
        this.friday = friday;
    }

    /**
     * 土曜日を取得する。
     *
     * @return
     */
    public Character getSaturday() {
        return saturday;
    }

    /**
     * 土曜日を設定する。
     *
     * @param saturday
     */
    public void setSaturday(Character saturday) {
        this.saturday = saturday;
    }

    /**
     * 祝日を取得する。
     *
     * @return
     */
    public Character getHoliday() {
        return holiday;
    }

    /**
     * 祝日を設定する。
     *
     * @param holiday
     */
    public void setHoliday(Character holiday) {
        this.holiday = holiday;
    }

    /**
     * 例外日の扱いを取得する。
     *
     * @return
     */
    public Character getExceptMove() {
        return exceptMove;
    }

    /**
     * 例外日の扱いを設定する。
     *
     * @param exceptMove
     */
    public void setExceptMove(Character exceptMove) {
        this.exceptMove = exceptMove;
    }

    /**
     * 実施日のリストを取得する。
     *
     * @return
     */
    public String getIncludeDays() {
        return includeDays;
    }

    /**
     * 実施日のリストを設定する。
     *
     * @param includeDays
     */
    public void setIncludeDays(String includeDays) {
        this.includeDays = includeDays;
    }

    /**
     * 除外日のリストを取得する。
     *
     * @return
     */
    public String getExcludeDays() {
        return excludeDays;
    }

    /**
     * 除外日のリストを設定する。
     *
     * @param excludeDays
     */
    public void setExcludeDays(String excludeDays) {
        this.excludeDays = excludeDays;
    }

    /**
     * 公開を取得する。
     *
     * @return
     */
    public Character getOpenOthers() {
        return openOthers;
    }

    /**
     * 公開を設定する。
     *
     * @param openOthers
     */
    public void setOpenOthers(Character openOthers) {
        this.openOthers = openOthers;
    }

    /**
     * 表示色を取得する。
     *
     * @return
     */
    public String getColor() {
        return color;
    }

    /**
     * 表示色を設定する。
     *
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * メモを取得する。
     *
     * @return
     */
    public String getMemo() {
        return memo;
    }

    /**
     * メモを設定する。
     *
     * @param memo
     */
    public void setMemo(String memo) {
        this.memo = memo;
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
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventNo != null ? eventNo.hashCode() : 0);
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
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        return (this.eventNo != null || other.eventNo == null) && (this.eventNo == null || this.eventNo.equals(other.eventNo));
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Event [No." + eventNo + "] " + this.getName();
    }

    /**
     * 他のイベントから中身をコピーする。
     *
     * @param src コピー元イベント
     */
    public void copyContents(Event src) {
        this.setEventClass(src.getEventClass());
        this.setEventType(src.getEventType());
        this.setName(src.getName());
        this.setTermFrom(src.getTermFrom());
        this.setTermTo(src.getTermTo());
        this.setStartTime(src.getStartTime());
        this.setEndTime(src.getEndTime());
        this.setPlace(src.getPlace());
        this.setAffiliate(src.getAffiliate());
        this.setSunday(src.getSunday());
        this.setMonday(src.getMonday());
        this.setTuesday(src.getTuesday());
        this.setWednesday(src.getWednesday());
        this.setThursday(src.getThursday());
        this.setFriday(src.getFriday());
        this.setSaturday(src.getSaturday());
        this.setHoliday(src.getHoliday());
        this.setExceptMove(src.getExceptMove());
        this.setIncludeDays(src.getIncludeDays());
        this.setExcludeDays(src.getExcludeDays());
        this.setOpenOthers(src.getOpenOthers());
        this.setColor(src.getColor());
        this.setMemo(src.getMemo());
    }
}
