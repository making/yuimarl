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

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 法人クラス
 * <p>
 * 組織クラスから継承。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Entity
@DiscriminatorValue(value = "3")
public class Corporation extends Organization {

    /**
     * URL
     */
    @Column(length = 60)
    private String url;

    /**
     * 資本金
     */
    private Long capital;

    /**
     * 決算月
     */
    @Column(name = "ACCOUNT_MONTH")
    private Integer accountMonth;

    /**
     * コンストラクタ
     */
    public Corporation() {
        super();
        this.setPartyType(Party.PARTY_TYPE_CORPORATION);
    }

    /**
     * URLを取得する。
     *
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * URLを設定する。
     *
     * @param url セットする url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 資本金を取得する。
     *
     * @return capital
     */
    public Long getCapital() {
        return capital;
    }

    /**
     * 資本金を設定する。
     *
     * @param capital セットする capital
     */
    public void setCapital(Long capital) {
        this.capital = capital;
    }

    /**
     * 資本金を取得する。
     *
     * @return accountMonth
     */
    public Integer getAccountMonth() {
        return accountMonth;
    }

    /**
     * 資本金を設定する。
     *
     * @param accountMonth セットする accountMonth
     */
    public void setAccountMonth(Integer accountMonth) {
        this.accountMonth = accountMonth;
    }
}
