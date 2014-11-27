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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 組織クラス
 * <p>
 * Partyクラスから継承。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Entity
@DiscriminatorValue(value = "2")
public class Organization extends Party {

    /**
     * 種類
     */
    @ManyToOne
    @JoinColumn(name = "CATEGORY", referencedColumnName = "CATEGORY_NO")
    private OrganizationCategory category;

    /**
     * 所属者数
     */
    @Column(name = "PERSON_COUNT")
    private Integer personCount;

    /**
     * コンストラクタ
     */
    public Organization() {
        super();
        this.setPartyType(Party.PARTY_TYPE_ORGANIZATION);
    }

    /**
     * 種類を取得する。
     *
     * @return category
     */
    public OrganizationCategory getCategory() {
        return category;
    }

    /**
     * 種類の名前を取得する。
     *
     * @return
     */
    @Override
    public String getCategoryName() {
        if (category == null) {
            return "";
        }
        return category.getName();
    }

    /**
     * 種類を設定する。
     *
     * @param category セットする category
     */
    public void setCategory(OrganizationCategory category) {
        this.category = category;
    }

    /**
     * 所属者数を取得する。
     *
     * @return personCount
     */
    public Integer getPersonCount() {
        return personCount;
    }

    /**
     * 所属者数を設定する。
     *
     * @param personCount セットする personCount
     */
    public void setPersonCount(Integer personCount) {
        this.personCount = personCount;
    }

    /**
     * 設立年月日を取得する。
     *
     * @return establishDate
     */
    public Date getEstablishDate() {
        return getStartDate();
    }

    /**
     * 設立年月日を設定する。
     *
     * @param establishDate セットする establishDate
     */
    public void setEstablishDate(Date establishDate) {
        setStartDate(establishDate);
    }

    /**
     * 廃止年月日を取得する。
     *
     * @return abolitionDate
     */
    public Date getAbolitionDate() {
        return getEndDate();
    }

    /**
     * 廃止年月日を設定する。
     *
     * @param abolitionDate セットする abolitionDate
     */
    public void setAbolitionDate(Date abolitionDate) {
        setEndDate(abolitionDate);
    }
}
