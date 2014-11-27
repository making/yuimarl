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
package jp.yuimarl.ejb;

import java.util.List;
import javax.ejb.Local;
import jp.yuimarl.entity.Party;

/**
 * Party操作インターフェース
 *
 * @author Masahiro Nitta
 */
@Local
public interface PartyService {

    /**
     * 登録する。
     *
     * @param entity
     */
    public void create(Party entity);

    /**
     * 更新する。
     *
     * @param entity
     */
    public void edit(Party entity);

    /**
     * 削除する。
     *
     * @param entity
     */
    public void remove(Party entity);

    /**
     * 検索する。
     *
     * @param range
     * @return
     */
    public List<Party> findRange(int[] range);

    /**
     * すべての件数を取得する。
     *
     * @return
     */
    public int count();

    /**
     * 検索条件を指定して件数を取得する。
     *
     * @param cond 検索条件
     * @return
     */
    public int condCount(String cond);

    /**
     * Party No.を指定してPartyを取得する。
     *
     * @param partyNo
     * @return
     */
    public Party getPartyByPartyNo(Integer partyNo);

    /**
     * 検索条件と範囲を指定してPartyを取得する。
     *
     * @param condition 検索条件
     * @param range 範囲
     * @return
     */
    public List<Party> findCondRange(String condition, int[] range);

    /**
     * PartyとParty関連を削除する。
     * 
     * @param party 
     */
    public void removePartyAndRelation(Party party);
}
