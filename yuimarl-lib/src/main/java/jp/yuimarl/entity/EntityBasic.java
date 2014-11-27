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

/**
 * Entityの基本インターフェース
 *
 * @author Masahiro Nitta
 */
public interface EntityBasic {

    /**
     * 登録ユーザーを取得する。
     *
     * @return registUser
     */
    public YuimarlUser getRegistUser();

    /**
     * 登録ユーザーを設定する。
     *
     * @param registUser セットする registUser
     */
    public void setRegistUser(YuimarlUser registUser);

    /**
     * 登録日時を取得する。
     *
     * @return registTime
     */
    public Date getRegistTime();

    /**
     * 登録日時を設定する。
     *
     * @param registTime セットする registTime
     */
    public void setRegistTime(Date registTime);

    /**
     * 更新ユーザーを取得する。
     *
     * @return updateUser
     */
    public YuimarlUser getUpdateUser();

    /**
     * 更新ユーザーを設定する。
     *
     * @param updateUser セットする updateUser
     */
    public void setUpdateUser(YuimarlUser updateUser);

    /**
     * 更新日時を取得する。
     *
     * @return updateTime
     */
    public Date getUpdateTime();

    /**
     * 更新日時を設定する。
     *
     * @param updateTime セットする updateTime
     */
    public void setUpdateTime(Date updateTime);

    /**
     * バージョン番号を取得する。
     *
     * @return
     */
    public Integer getVersionNo();

    /**
     * バージョン番号を設定する。
     *
     * @param versionno セットする versionno
     */
    public void setVersionNo(Integer versionno);

}
