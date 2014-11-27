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
package jp.yuimarl.util;

import javax.faces.model.DataModel;

/**
 * 一覧ページ制御クラス
 *
 * @author Masahiro Nitta
 */
public abstract class YuimarlPagination {

    /**
     * デフォルトの一覧表示行数
     */
    public static final int DEFAULT_SIZE = 20;
    /**
     * 一覧表示行数
     */
    protected int pageSize = DEFAULT_SIZE;
    /**
     * 表示中のページ番号
     */
    protected int page = 0;
    /**
     * データ全体の件数
     */
    protected int itemsCount = 0;

    /**
     * コンストラクタ
     *
     * @param pageSize 一覧表示行数
     * @param itemsCount データ全体の件数
     */
    public YuimarlPagination(int pageSize, int itemsCount) {
        this.pageSize = pageSize;
        this.itemsCount = itemsCount;
    }

    /**
     * データ全体の件数を取得する。
     *
     * @return
     */
    public int getItemsCount() {
        return itemsCount;
    }

    /**
     * 表示ページのDataModelを取得する。
     *
     * @return
     */
    public abstract DataModel createPageDataModel();

    /**
     * 表示ページの最初のデータのインデックスを取得する。
     *
     * @return
     */
    public int getPageFirstItem() {
        return page * pageSize;
    }

    /**
     * 複数のページが存在するかを返す。
     *
     * @return
     */
    public boolean isHasPluralPage() {
        return itemsCount > pageSize;
    }

    /**
     * 前のページが存在するかを返す。
     *
     * @return
     */
    public boolean isHasPreviousPage() {
        return page > 0;
    }

    /**
     * 前のページに遷移する。
     */
    public void previousPage() {
        if (isHasPreviousPage()) {
            page--;
        }
    }

    /**
     * 2ページ前が存在するかを返す。
     *
     * @return
     */
    public boolean isHasPrevious2Page() {
        return page > 1;
    }

    /**
     * 2ページ前に遷移する。
     */
    public void previous2Page() {
        if (isHasPrevious2Page()) {
            page -= 2;
        }
    }

    /**
     * 次のページが存在するかを返す。
     *
     * @return
     */
    public boolean isHasNextPage() {
        return (page + 1) * pageSize + 1 <= itemsCount;
    }

    /**
     * 次のページに遷移する。
     */
    public void nextPage() {
        if (isHasNextPage()) {
            page++;
        }
    }

    /**
     * 2ページ後が存在するかを返す。
     *
     * @return
     */
    public boolean isHasNext2Page() {
        return (page + 2) * pageSize + 1 <= itemsCount;
    }

    /**
     * 2ページ後に遷移する。
     */
    public void next2Page() {
        if (isHasNextPage()) {
            page += 2;
        }
    }

    /**
     * 先頭のページに遷移する。
     */
    public void topPage() {
        page = 0;
    }

    /**
     * 最後のページに遷移する。
     */
    public void lastPage() {
        page = (int) ((itemsCount - 1) / pageSize);
    }

    /**
     * 一覧表示行数を取得する。
     *
     * @return
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 現在のページ番号を取得する。
     *
     * @return
     */
    public int getCurPageNo() {
        return page + 1;
    }

    /**
     * 1ページ前のページ番号を取得する。
     *
     * @return
     */
    public int getPrevPageNo() {
        return page;
    }

    /**
     * 2ページ前のページ番号を取得する。
     *
     * @return
     */
    public int getPrev2PageNo() {
        return page - 1;
    }

    /**
     * 1ページ後のページ番号を取得する。
     *
     * @return
     */
    public int getNextPageNo() {
        return page + 2;
    }

    /**
     * 2ページ後のページ番号を取得する。
     *
     * @return
     */
    public int getNext2PageNo() {
        return page + 3;
    }

}
