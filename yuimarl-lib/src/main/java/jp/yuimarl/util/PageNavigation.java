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
/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package jp.yuimarl.util;

/**
 * Simple ENUM to centralize strings for common navigation destinations
 *
 * @author markito
 * @author Masahiro Nitta
 */
public enum PageNavigation {

    LIST("List"),
    M_LIST("m_List"),
    PARTY_LIST("/party/List"),
    USER_LIST("/yuimarlUser/List"),
    EDIT("Edit"),
    EDIT_PASSWORD("/yuimarlUser/EditPassword"),
    PARTY_EDIT("/party/Edit"),
    GOODSCATEGORY_EDIT("/goodsCategory/Edit"),
    VIEW("View"),
    M_VIEW("m_View"),
    AUTHORITY("Authority"),
    REL_LIST("RelList"),
    REL_EDIT("RelEdit"),
    M_INDEX("/main/m_index"),
    INDEX("/main/index");

    private final String text;

    PageNavigation(final String s) {
        this.text = s;
    }

    public String getText() {
        return this.text;
    }
    
    @Override
    public String toString() {
        return this.text;
    }
}
