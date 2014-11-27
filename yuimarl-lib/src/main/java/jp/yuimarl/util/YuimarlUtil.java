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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import jp.yuimarl.ejb.YuimarlUserService;
import jp.yuimarl.entity.YuimarlUser;

/**
 * ユーティリティクラス
 *
 * @author Masahiro Nitta
 */
public class YuimarlUtil {

    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");

    /**
     * 文字列からMD5を生成する。
     *
     * @param str 文字列
     * @return MD5
     */
    public static String generateMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(str.getBytes());

            BigInteger number = new BigInteger(1, messageDigest);
            return number.toString(16);
        } catch (NoSuchAlgorithmException nsae) {
            return null;
        }
    }

    /**
     * ログインユーザーを取得する。
     *
     * @param userService
     * @return
     */
    public static YuimarlUser getLoginUser(YuimarlUserService userService) {
        String userId = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get("loginUserId");
        if (userId == null || userId.equals("")) {
            return null;
        }
        YuimarlUser user = userService.getYuimarlUserByUserId(userId);
        return user;
    }

    /**
     * ログファイルにログを出力する。
     * <p>
     * GlassFishの場合、出力されるログファイルは、glassfish/domains/domain1/logs/server.log
     * </p>
     *
     * @param msg 出力メッセージ
     */
    public static void writeLog(String msg) {
        String userId = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get("loginUserId");
        HttpServletRequest servletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.printf("*** Yuimarl.Log %s | IP:%s/%s | Login User:%s| %s.%s  | %s%n",
                sdf1.format(new Date()),
                servletRequest.getRemoteAddr(),
                servletRequest.getLocalAddr(),
                userId,
                Thread.currentThread().getStackTrace()[2].getClassName(),
                Thread.currentThread().getStackTrace()[2].getMethodName(),
                msg);
    }

    /**
     * 日付を文字列(yyyy/MM/dd)に変換する。
     *
     * @param dt
     * @return
     */
    public static String dtToStr1(Date dt) {
        if (dt == null) {
            return "";
        }
        return sdf2.format(dt);
    }

    /**
     * 文字列がNullなら空文字に変換する。
     *
     * @param str
     * @return
     */
    public static String nullToStr(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

    /**
     * Integerを文字列に変換する。
     *
     * @param val
     * @return
     */
    public static String intToStr(Integer val) {
        if (val == null) {
            return "";
        }
        return Integer.toString(val);
    }

    /**
     * Longを文字列に変換する。
     *
     * @param val
     * @return
     */
    public static String longToStr(Long val) {
        if (val == null) {
            return "";
        }
        return Long.toString(val);
    }

    /**
     * Date型の日付の和暦年を返す。
     *
     * @param dt Date型の日付
     * @param addParenthesis true:()でくくる. false:()でくくらない.
     * @return 和暦年
     */
    public static String dateToWareki(Date dt, boolean addParenthesis) {
        if (dt == null) {
            return "";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH) + 1;
        int d = cal.get(Calendar.DAY_OF_MONTH);
        int md = m * 100 + d;
        int g;  // 1:明治, 2:大正, 3:昭和, 4:平成
        int wy;

        if (y < 1868) {
            // 明治元年より前は非対応。
            return "";
        }
        // 明治: 1868年1月1日～1912年7月29日
        // 大正: 1912年7月30日～1926年12月24日
        // 昭和: 1926年12月25日～1989年1月7日
        // 平成: 1989年1月8日～
        switch (y) {
            case 1912:  // 大正元年
                if (md >= 730) {
                    g = 2;
                    wy = y - 1911;
                } else {
                    g = 1;
                    wy = y - 1867;
                }
                break;
            case 1926:  // 昭和元年
                if (md >= 1225) {
                    g = 3;
                    wy = y - 1925;
                } else {
                    g = 2;
                    wy = y - 1911;
                }
                break;
            case 1989:  // 平成元年
                if (md >= 108) {
                    g = 4;
                    wy = y - 1988;
                } else {
                    g = 3;
                    wy = y - 1925;
                }
                break;
            default:
                if (y < 1912) {
                    g = 1;
                    wy = y - 1867;
                } else if (y < 1926) {
                    g = 2;
                    wy = y - 1911;
                } else if (y < 1989) {
                    g = 3;
                    wy = y - 1925;
                } else {
                    g = 4;
                    wy = y - 1988;
                }
                break;
        }

        StringBuilder buf = new StringBuilder();
        if (addParenthesis) {
            buf.append("（");
        }
        switch (g) {
            case 1:
                buf.append("明治");
                break;
            case 2:
                buf.append("大正");
                break;
            case 3:
                buf.append("昭和");
                break;
            case 4:
                buf.append("平成");
                break;
        }
        buf.append(wy).append("年");
        if (addParenthesis) {
            buf.append("）");
        }
        return buf.toString();
    }

    /**
     * 日付文字列(yyyyMMdd)をCalendarに変換する。
     *
     * @param str 日付文字列(yyyyMMdd)
     * @return Calendar
     */
    public static Calendar strToCal(String str) {
        if (str == null || str.length() != 8) {
            return null;
        }
        int y = Integer.parseInt(str.substring(0, 4));
        int m = Integer.parseInt(str.substring(4, 6));
        int d = Integer.parseInt(str.substring(6));
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(y, m - 1, d);
        return cal;
    }

    /**
     * Calendarを日付文字列(yyyyMMdd)に変換する。
     *
     * @param cal Calendar
     * @return 日付文字列(yyyyMMdd)
     */
    public static String calToStr(Calendar cal) {
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH) + 1;
        int d = cal.get(Calendar.DAY_OF_MONTH);
        return String.format("%04d%02d%02d", y, m, d);
    }

    /**
     * 2つのCalendarの日付の差を返す。
     *
     * @param cal1
     * @param cal2
     * @return
     */
    public static int dateDiff(Calendar cal1, Calendar cal2) {
        long diffTime = cal2.getTimeInMillis() - cal1.getTimeInMillis();
        return (int) (diffTime / 1000 / 60 / 60 / 24);
    }
}
