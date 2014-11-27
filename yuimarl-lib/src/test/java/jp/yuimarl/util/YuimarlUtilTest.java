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

import java.util.Calendar;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * ユーティリティテストクラス
 *
 * @author Masahiro Nitta
 */
public class YuimarlUtilTest {
    
    public YuimarlUtilTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of generateMD5 method, of class YuimarlUtil.
     */
    @Test
    public void testGenerateMD5() {
        System.out.println("generateMD5");
        String str = "test";
        String expResult = "98f6bcd4621d373cade4e832627b4f6";
        String result = YuimarlUtil.generateMD5(str);
        assertEquals(expResult, result);
    }

    /**
     * Test of dtToStr1 method, of class YuimarlUtil.
     */
    @Test
    public void testDtToStr1() {
        System.out.println("dtToStr1");
        Date dt = new Date(114, 1, 20);
        String expResult = "2014/02/20";
        String result = YuimarlUtil.dtToStr1(dt);
        assertEquals(expResult, result);
    }

    /**
     * Test of nullToStr method, of class YuimarlUtil.
     */
    @Test
    public void testNullToStr() {
        System.out.println("nullToStr");
        String str = null;
        String expResult = "";
        String result = YuimarlUtil.nullToStr(str);
        assertEquals(expResult, result);
    }

    /**
     * Test of intToStr method, of class YuimarlUtil.
     */
    @Test
    public void testIntToStr() {
        System.out.println("intToStr");
        Integer val = 100;
        String expResult = "100";
        String result = YuimarlUtil.intToStr(val);
        assertEquals(expResult, result);
    }

    /**
     * Test of longToStr method, of class YuimarlUtil.
     */
    @Test
    public void testLongToStr() {
        System.out.println("longToStr");
        Long val = 10000L;
        String expResult = "10000";
        String result = YuimarlUtil.longToStr(val);
        assertEquals(expResult, result);
    }

    /**
     * Test of dateToWareki method, of class YuimarlUtil.
     */
    @Test
    public void testDateToWareki1() {
        System.out.println("dateToWareki1");
        Calendar cal = Calendar.getInstance();
        cal.set(1867, 11, 31);
        Date dt = cal.getTime();
        boolean addParenthesis = false;
        String expResult = "";
        String result = YuimarlUtil.dateToWareki(dt, addParenthesis);
        assertEquals(expResult, result);
    }

    /**
     * Test of dateToWareki method, of class YuimarlUtil.
     */
    @Test
    public void testDateToWareki2() {
        System.out.println("dateToWareki2");
        Calendar cal = Calendar.getInstance();
        cal.set(1868, 0, 1);
        Date dt = cal.getTime();
        boolean addParenthesis = false;
        String expResult = "明治1年";
        String result = YuimarlUtil.dateToWareki(dt, addParenthesis);
        assertEquals(expResult, result);
    }

    /**
     * Test of dateToWareki method, of class YuimarlUtil.
     */
    @Test
    public void testDateToWareki3() {
        System.out.println("dateToWareki3");
        Calendar cal = Calendar.getInstance();
        cal.set(1912, 6, 29);
        Date dt = cal.getTime();
        boolean addParenthesis = false;
        String expResult = "明治45年";
        String result = YuimarlUtil.dateToWareki(dt, addParenthesis);
        assertEquals(expResult, result);
    }

    /**
     * Test of dateToWareki method, of class YuimarlUtil.
     */
    @Test
    public void testDateToWareki4() {
        System.out.println("dateToWareki4");
        Calendar cal = Calendar.getInstance();
        cal.set(1912, 6, 30);
        Date dt = cal.getTime();
        boolean addParenthesis = false;
        String expResult = "大正1年";
        String result = YuimarlUtil.dateToWareki(dt, addParenthesis);
        assertEquals(expResult, result);
    }

    /**
     * Test of dateToWareki method, of class YuimarlUtil.
     */
    @Test
    public void testDateToWareki5() {
        System.out.println("dateToWareki5");
        Calendar cal = Calendar.getInstance();
        cal.set(1926, 11, 24);
        Date dt = cal.getTime();
        boolean addParenthesis = false;
        String expResult = "大正15年";
        String result = YuimarlUtil.dateToWareki(dt, addParenthesis);
        assertEquals(expResult, result);
    }

    /**
     * Test of dateToWareki method, of class YuimarlUtil.
     */
    @Test
    public void testDateToWareki6() {
        System.out.println("dateToWareki6");
        Calendar cal = Calendar.getInstance();
        cal.set(1926, 11, 25);
        Date dt = cal.getTime();
        boolean addParenthesis = false;
        String expResult = "昭和1年";
        String result = YuimarlUtil.dateToWareki(dt, addParenthesis);
        assertEquals(expResult, result);
    }

    /**
     * Test of dateToWareki method, of class YuimarlUtil.
     */
    @Test
    public void testDateToWareki7() {
        System.out.println("dateToWareki7");
        Calendar cal = Calendar.getInstance();
        cal.set(1989, 0, 7);
        Date dt = cal.getTime();
        boolean addParenthesis = false;
        String expResult = "昭和64年";
        String result = YuimarlUtil.dateToWareki(dt, addParenthesis);
        assertEquals(expResult, result);
    }

    /**
     * Test of dateToWareki method, of class YuimarlUtil.
     */
    @Test
    public void testDateToWareki8() {
        System.out.println("dateToWareki8");
        Calendar cal = Calendar.getInstance();
        cal.set(1989, 0, 8);
        Date dt = cal.getTime();
        boolean addParenthesis = false;
        String expResult = "平成1年";
        String result = YuimarlUtil.dateToWareki(dt, addParenthesis);
        assertEquals(expResult, result);
    }

    /**
     * Test of dateToWareki method, of class YuimarlUtil.
     */
    @Test
    public void testDateToWareki9() {
        System.out.println("dateToWareki9");
        Calendar cal = Calendar.getInstance();
        cal.set(2014, 0, 8);
        Date dt = cal.getTime();
        boolean addParenthesis = true;
        String expResult = "（平成26年）";
        String result = YuimarlUtil.dateToWareki(dt, addParenthesis);
        assertEquals(expResult, result);
    }

    /**
     * Test of strToCal method, of class YuimarlUtil.
     */
    @Test
    public void testStrToCal() {
        System.out.println("strToCal1");
        Calendar cal = YuimarlUtil.strToCal("20140930");
        assertEquals(2014, cal.get(Calendar.YEAR));
        assertEquals(8, cal.get(Calendar.MONTH));
        assertEquals(30, cal.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Test of calToStr method, of class YuimarlUtil.
     */
    @Test
    public void testCalToStr() {
        System.out.println("calToStr");
        Calendar cal = Calendar.getInstance();
        cal.set(2014, 8, 30);
        String str = YuimarlUtil.calToStr(cal);
        assertEquals("20140930", str);
        cal.add(Calendar.DATE, 1);
        str = YuimarlUtil.calToStr(cal);
        assertEquals("20141001", str);
    }

    /**
     * Test of dateDiff method, of class YuimarlUtil.
     */
    @Test
    public void testDateDiff() {
        System.out.println("dateDiff");
        Calendar cal1 = Calendar.getInstance();
        cal1.clear();
        cal1.set(2014, 8, 30);
        Calendar cal2 = Calendar.getInstance();
        cal2.clear();
        cal2.set(2014, 9, 3);
        int diff = YuimarlUtil.dateDiff(cal1, cal2);
        assertEquals(3, diff);
        cal2.set(2015, 8, 30);
        diff = YuimarlUtil.dateDiff(cal1, cal2);
        assertEquals(365, diff);
    }
    
}
