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

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import jp.yuimarl.qualifiers.ValidPhoneNumber;

/**
 * 人クラス
 * <p>
 * Partyクラスから継承。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Entity
@DiscriminatorValue(value = "1")
public class Person extends Party {

    /**
     * 姓
     */
    @Column(name = "LAST_NAME", length = 20)
    private String lastName;

    /**
     * 名
     */
    @Column(name = "FIRST_NAME", length = 20)
    private String firstName;

    /**
     * 姓ヨミ
     */
    @Column(name = "LAST_NAME_KANA", length = 30)
    private String lastNameKana;

    /**
     * 名ヨミ
     */
    @Column(name = "FIRST_NAME_KANA", length = 30)
    private String firstNameKana;

    /**
     * 性別
     * <p>
     * 1:男性, 2:女性, 0:不明
     * </p>
     */
    private Integer gender;

    /**
     * 携帯電話番号
     */
    @ValidPhoneNumber(message = "{person.cellPhoneNumber}")
    @Column(name = "CELL_PHONE_NUMBER", length = 20)
    private String cellPhoneNumber;

    /**
     * コンストラクタ
     */
    public Person() {
        super();
        this.setPartyType(Party.PARTY_TYPE_PERSON);
    }

    /**
     * 種類名を取得する。
     *
     * @return
     */
    @Override
    public String getCategoryName() {
        return "人";
    }

    /**
     * 姓を取得する。
     *
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * 姓を設定する。
     *
     * @param lastName セットする lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.setName(this.getName());
    }

    /**
     * 名を取得する。
     *
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * 名を設定する。
     *
     * @param firstName セットする firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.setName(this.getName());
    }

    /**
     * 姓ヨミを取得する。
     *
     * @return lastNameKana
     */
    public String getLastNameKana() {
        return lastNameKana;
    }

    /**
     * 姓ヨミを設定する。
     *
     * @param lastNameKana セットする lastNameKana
     */
    public void setLastNameKana(String lastNameKana) {
        this.lastNameKana = lastNameKana;
        this.setNameKana(this.getNameKana());
    }

    /**
     * 名ヨミを取得する。
     *
     * @return firstNameKana
     */
    public String getFirstNameKana() {
        return firstNameKana;
    }

    /**
     * 名ヨミを設定する。
     *
     * @param firstNameKana セットする firstNameKana
     */
    public void setFirstNameKana(String firstNameKana) {
        this.firstNameKana = firstNameKana;
        this.setNameKana(this.getNameKana());
    }

    /**
     * 名前を取得する。
     *
     * @return
     */
    @Override
    public String getName() {
        if (lastName == null || lastName.equals("")) {
            if (firstName == null || firstName.equals("")) {
                return "";
            } else {
                return firstName;
            }
        } else {
            if (firstName == null || firstName.equals("")) {
                return lastName;
            } else {
                return lastName + " " + firstName;
            }
        }
    }

    /**
     * 名前ヨミを取得する。
     *
     * @return
     */
    @Override
    public String getNameKana() {
        if (lastNameKana == null || lastNameKana.equals("")) {
            if (firstNameKana == null || firstNameKana.equals("")) {
                return "";
            } else {
                return firstNameKana;
            }
        } else {
            if (firstNameKana == null || firstNameKana.equals("")) {
                return lastNameKana;
            } else {
                return lastNameKana + " " + firstNameKana;
            }
        }
    }

    /**
     * 誕生日を取得する。
     *
     * @return birthday
     */
    public Date getBirthday() {
        return getStartDate();
    }

    /**
     * 誕生日を設定する。
     *
     * @param birthday セットする birthday
     */
    public void setBirthday(Date birthday) {
        setStartDate(birthday);
    }

    /**
     * 死亡日を取得する。
     *
     * @return dateOfDeath
     */
    public Date getDateOfDeath() {
        return getEndDate();
    }

    /**
     * 死亡日を設定する。
     *
     * @param dateOfDeath セットする dateOfDeath
     */
    public void setDateOfDeath(Date dateOfDeath) {
        setEndDate(dateOfDeath);
    }

    /**
     * 性別を取得する。
     *
     * @return gender
     */
    public Integer getGender() {
        return gender;
    }

    /**
     * 性別を設定する。
     *
     * @param gender セットする gender
     */
    public void setGender(Integer gender) {
        this.gender = gender;
    }

    /**
     * 性別名を取得する。
     *
     * @return
     */
    public String getGenderAsString() {
        switch (this.gender) {
            case 1:
                return "男性";
            case 2:
                return "女性";
        }
        return "";
    }

    /**
     * 携帯電話番号を取得する。
     *
     * @return cellPhoneNumber
     */
    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }

    /**
     * 携帯電話番号を設定する。
     *
     * @param cellPhoneNumber セットする cellPhoneNumber
     */
    public void setCellPhoneNumber(String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
    }

}
