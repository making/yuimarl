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
package jp.yuimarl.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import jp.yuimarl.ejb.PartyService;
import jp.yuimarl.ejb.YuimarlUserService;
import jp.yuimarl.entity.Party;
import jp.yuimarl.entity.YuimarlUser;
import jp.yuimarl.util.JsfUtil;
import jp.yuimarl.util.PageNavigation;
import jp.yuimarl.util.YuimarlPagination;
import jp.yuimarl.util.YuimarlUtil;

/**
 * Yuimarlユーザー制御クラス
 *
 * @author Masahiro Nitta
 */
@Named(value = "yuimarlUserController")
@SessionScoped
public class YuimarlUserController implements Serializable {

    private static final String BUNDLE = "bundles.Bundle";

    @EJB
    private YuimarlUserService userService;
    @EJB
    private PartyService partyService;

    private YuimarlUser current = null;
    private DataModel items = null;
    private List<YuimarlUser> userList = null;

    private YuimarlPagination pagination;
    private boolean modeCreate = false;
    private boolean modeUpdate = false;
    private String password1 = "";
    private String password2 = "";
    private Integer partyNo = null;
    private boolean authPw = false;
    private boolean auth1V = false;
    private boolean auth1C = false;
    private boolean auth1U = false;
    private boolean auth1D = false;
    private boolean auth2V = false;
    private boolean auth2C = false;
    private boolean auth2U = false;
    private boolean auth2D = false;
    private boolean auth3V = false;
    private boolean auth3C = false;
    private boolean auth3U = false;
    private boolean auth3D = false;
    private boolean auth4V = false;
    private boolean auth4C = false;
    private boolean auth4U = false;
    private boolean auth4D = false;
    private String authRegUser = "";
    private Date authRegTime = null;
    private String authUpdUser = "";
    private Date authUpdTime = null;
    private Integer lineNo = 0;
    private String conf;

    /**
     * コンストラクタ
     */
    public YuimarlUserController() {
    }

    /**
     * ユーザーのリストを取得する。
     * 
     * @return 
     */
    public List<YuimarlUser> getUserList() {
        return userList;
    }

    /**
     * 登録画面であるかを取得する。
     *
     * @return true:登録画面, false:編集画面
     */
    public boolean isModeCreate() {
        return modeCreate;
    }

    /**
     * 編集画面であるかを取得する。
     *
     * @return true:編集画面, false:照会画面
     */
    public boolean isModeUpdate() {
        return modeUpdate;
    }

    /**
     * パスワードを取得する。
     *
     * @return
     */
    public String getPassword1() {
        return password1;
    }

    /**
     * パスワードを設定する。
     *
     * @param password1
     */
    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    /**
     * パスワード（再度）を取得する。
     *
     * @return
     */
    public String getPassword2() {
        return password2;
    }

    /**
     * パスワード（再度）を設定する。
     *
     * @param password2
     */
    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    /**
     * Party No.を取得する。
     *
     * @return
     */
    public Integer getPartyNo() {
        return partyNo;
    }

    /**
     * Party No.を設定する。
     *
     * @param partyNo
     */
    public void setPartyNo(Integer partyNo) {
        this.partyNo = partyNo;
    }

    /**
     * 名前を取得する。
     *
     * @return
     */
    public String getPartyName() {
        if (partyNo == null || partyNo == 0) {
            return "";
        }
        Party p = partyService.getPartyByPartyNo(partyNo);
        if (p == null) {
            return "";
        }
        return p.getName();
    }

    /**
     * パスワード変更権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuthPw() {
        return authPw;
    }

    /**
     * パスワード変更権限を設定する。
     *
     * @param authPw
     */
    public void setAuthPw(boolean authPw) {
        this.authPw = authPw;
    }

    /**
     * ユーザー 照会権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth1V() {
        return auth1V;
    }

    /**
     * ユーザー 照会権限を設定する。
     *
     * @param auth1V
     */
    public void setAuth1V(boolean auth1V) {
        this.auth1V = auth1V;
    }

    /**
     * ユーザー 登録権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth1C() {
        return auth1C;
    }

    /**
     * ユーザー 登録権限を設定する。
     *
     * @param auth1C
     */
    public void setAuth1C(boolean auth1C) {
        this.auth1C = auth1C;
    }

    /**
     * ユーザー 更新権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth1U() {
        return auth1U;
    }

    /**
     * ユーザー 更新権限を設定する。
     *
     * @param auth1U
     */
    public void setAuth1U(boolean auth1U) {
        this.auth1U = auth1U;
    }

    /**
     * ユーザー 削除権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth1D() {
        return auth1D;
    }

    /**
     * ユーザー 削除権限を設定する。
     *
     * @param auth1D
     */
    public void setAuth1D(boolean auth1D) {
        this.auth1D = auth1D;
    }

    /**
     * ユーザー権限 照会権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth2V() {
        return auth2V;
    }

    /**
     * ユーザー権限 照会権限を設定する。
     *
     * @param auth2V
     */
    public void setAuth2V(boolean auth2V) {
        this.auth2V = auth2V;
    }

    /**
     * ユーザー権限 登録権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth2C() {
        return auth2C;
    }

    /**
     * ユーザー権限 登録権限を設定する。
     *
     * @param auth2C
     */
    public void setAuth2C(boolean auth2C) {
        this.auth2C = auth2C;
    }

    /**
     * ユーザー権限 更新権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth2U() {
        return auth2U;
    }

    /**
     * ユーザー権限 更新権限を設定する。
     *
     * @param auth2U
     */
    public void setAuth2U(boolean auth2U) {
        this.auth2U = auth2U;
    }

    /**
     * ユーザー権限 削除権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth2D() {
        return auth2D;
    }

    /**
     * ユーザー権限 削除権限を設定する。
     *
     * @param auth2D
     */
    public void setAuth2D(boolean auth2D) {
        this.auth2D = auth2D;
    }

    /**
     * 物品カテゴリー 照会権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth3V() {
        return auth3V;
    }

    /**
     * 物品カテゴリー 照会権限を設定する。
     *
     * @param auth3V
     */
    public void setAuth3V(boolean auth3V) {
        this.auth3V = auth3V;
    }

    /**
     * 物品カテゴリー 登録権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth3C() {
        return auth3C;
    }

    /**
     * 物品カテゴリー 登録権限を設定する。
     *
     * @param auth3C
     */
    public void setAuth3C(boolean auth3C) {
        this.auth3C = auth3C;
    }

    /**
     * 物品カテゴリー 更新権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth3U() {
        return auth3U;
    }

    /**
     * 物品カテゴリー 更新権限を設定する。
     *
     * @param auth3U
     */
    public void setAuth3U(boolean auth3U) {
        this.auth3U = auth3U;
    }

    /**
     * 物品カテゴリー 削除権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth3D() {
        return auth3D;
    }

    /**
     * 物品カテゴリー 削除権限を設定する。
     *
     * @param auth3D
     */
    public void setAuth3D(boolean auth3D) {
        this.auth3D = auth3D;
    }

    /**
     * 人/組織/物 照会権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth4V() {
        return auth4V;
    }

    /**
     * 人/組織/物 照会権限を設定する。
     *
     * @param auth4V
     */
    public void setAuth4V(boolean auth4V) {
        this.auth4V = auth4V;
    }

    /**
     * 人/組織/物 登録権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth4C() {
        return auth4C;
    }

    /**
     * 人/組織/物 登録権限を設定する。
     *
     * @param auth4C
     */
    public void setAuth4C(boolean auth4C) {
        this.auth4C = auth4C;
    }

    /**
     * 人/組織/物 更新権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth4U() {
        return auth4U;
    }

    /**
     * 人/組織/物 更新権限を設定する。
     *
     * @param auth4U
     */
    public void setAuth4U(boolean auth4U) {
        this.auth4U = auth4U;
    }

    /**
     * 人/組織/物 削除権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth4D() {
        return auth4D;
    }

    /**
     * 人/組織/物 削除権限を設定する。
     *
     * @param auth4D
     */
    public void setAuth4D(boolean auth4D) {
        this.auth4D = auth4D;
    }

    /**
     * 権限登録ユーザーを取得する。
     *
     * @return
     */
    public String getAuthRegUser() {
        return authRegUser;
    }

    /**
     * 権限登録ユーザーを設定する。
     *
     * @param authRegUser
     */
    public void setAuthRegUser(String authRegUser) {
        this.authRegUser = authRegUser;
    }

    /**
     * 権限登録日時を取得する。
     *
     * @return
     */
    public Date getAuthRegTime() {
        return authRegTime;
    }

    /**
     * 権限登録日時を設定する。
     *
     * @param authRegTime
     */
    public void setAuthRegTime(Date authRegTime) {
        this.authRegTime = authRegTime;
    }

    /**
     * 権限更新ユーザーを取得する。
     *
     * @return
     */
    public String getAuthUpdUser() {
        return authUpdUser;
    }

    /**
     * 権限更新ユーザーを設定する。
     *
     * @param authUpdUser
     */
    public void setAuthUpdUser(String authUpdUser) {
        this.authUpdUser = authUpdUser;
    }

    /**
     * 権限更新日時を取得する。
     *
     * @return
     */
    public Date getAuthUpdTime() {
        return authUpdTime;
    }

    /**
     * 権限更新日時を設定する。
     *
     * @param authUpdTime
     */
    public void setAuthUpdTime(Date authUpdTime) {
        this.authUpdTime = authUpdTime;
    }

    /**
     * 選択された一覧の行番号を取得する。
     *
     * @return
     */
    public Integer getLineNo() {
        return lineNo;
    }

    /**
     * 選択された一覧の行番号を設定する。
     *
     * @param lineNo
     */
    public void setLineNo(Integer lineNo) {
        this.lineNo = lineNo;
    }

    /**
     * 削除確認の選択結果を取得する。
     *
     * @return 1: OK, 0:Cancel
     */
    public String getConf() {
        return conf;
    }

    /**
     * 削除確認の選択結果を設定する。
     *
     * @param conf
     */
    public void setConf(String conf) {
        this.conf = conf;
    }

    /**
     * 選択されているユーザーを取得する。
     *
     * @return
     */
    public YuimarlUser getSelected() {
        if (current == null) {
            current = new YuimarlUser();
        }
        return current;
    }

    /**
     * 一覧画面の一覧ページ制御クラスのインスタンスを取得する。
     *
     * @return
     */
    public YuimarlPagination getPagination() {
        if (pagination == null) {
            initPagination();
        }
        return pagination;
    }

    /**
     * 一覧画面のDataModelを取得する。
     *
     * @return
     */
    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    /**
     * 一覧画面の一覧ページ制御クラスのインスタンスを初期化する。
     */
    private void initPagination() {
        pagination = new YuimarlPagination(YuimarlPagination.DEFAULT_SIZE, userService.count()) {

            @Override
            public DataModel createPageDataModel() {
                return new ListDataModel(userService.findRange(new int[]{getPageFirstItem(),
                    getPageFirstItem() + getPageSize()}));
            }
        };
        itemsToList();
    }

    /**
     * アイテムの配列をリストにセットする。
     */
    private void itemsToList() {
        items = pagination.createPageDataModel();
        Iterator it = items.iterator();
        if (userList == null) {
            userList = new ArrayList<>();
        } else {
            userList.clear();
        }
        while (it.hasNext()) {
            userList.add((YuimarlUser) it.next());
        }
    }

    /**
     * 一覧画面に遷移する。
     *
     * @return
     */
    public PageNavigation prepareList() {
        initPagination();
        return PageNavigation.USER_LIST;
    }

    /**
     * 一覧画面の先頭ページに遷移する。
     *
     * @return
     */
    public PageNavigation pageTop() {
        getPagination().topPage();
        itemsToList();
        return null;
    }

    /**
     * 一覧画面の1ページ前に遷移する。
     *
     * @return
     */
    public PageNavigation pagePrevious() {
        getPagination().previousPage();
        itemsToList();
        return null;
    }

    /**
     * 一覧画面の2ページ前に遷移する。
     *
     * @return
     */
    public PageNavigation pagePrevious2() {
        getPagination().previous2Page();
        itemsToList();
        return null;
    }

    /**
     * 一覧画面の1ページ後に遷移する。
     *
     * @return
     */
    public PageNavigation pageNext() {
        getPagination().nextPage();
        itemsToList();
        return null;
    }

    /**
     * 一覧画面の2ページ後に遷移する。
     *
     * @return
     */
    public PageNavigation pageNext2() {
        getPagination().next2Page();
        itemsToList();
        return null;
    }

    /**
     * 一覧画面の最終ページに遷移する。
     *
     * @return
     */
    public PageNavigation pageLast() {
        getPagination().lastPage();
        itemsToList();
        return null;
    }

    /**
     * 表示画面に遷移する。
     *
     * @return
     */
    public PageNavigation prepareView() {
        current = userList.get(this.lineNo);
        return PageNavigation.VIEW;
    }

    /**
     * 登録画面に遷移する。
     *
     * @return
     */
    public PageNavigation prepareCreate() {
        current = new YuimarlUser();
        modeCreate = true;
        password1 = "";
        password2 = "";
        partyNo = null;
        return PageNavigation.EDIT;
    }

    /**
     * 権限画面に遷移する。
     *
     * @return
     */
    public PageNavigation prepareAuth() {
        modeUpdate = true;
        current = userList.get(this.lineNo);
        return prepareAuthSub();
    }

    /**
     * 権限画面の設定を行う。
     *
     * @return
     */
    private PageNavigation prepareAuthSub() {
        authPw = false;
        auth1V = false;
        auth1C = false;
        auth1U = false;
        auth1D = false;
        auth2V = false;
        auth2C = false;
        auth2U = false;
        auth2D = false;
        auth3V = false;
        auth3C = false;
        auth3U = false;
        auth3D = false;
        auth4V = false;
        auth4C = false;
        auth4U = false;
        auth4D = false;
        authRegUser = "";
        authRegTime = null;
        authUpdUser = "";
        authUpdTime = null;

        String authMap = current.getAuthMap();
        if (authMap == null || authMap.length() != YuimarlUser.AUTH_MAP_LENGTH) {
            return PageNavigation.AUTHORITY;
        }

        authPw = (authMap.charAt(YuimarlUser.AUTH_PASSWORD_CHANGE) == YuimarlUser.AUTH_ENABLE);
        auth1V = (authMap.charAt(YuimarlUser.AUTH_USER_VIEW) == YuimarlUser.AUTH_ENABLE);
        auth1C = (authMap.charAt(YuimarlUser.AUTH_USER_CREATE) == YuimarlUser.AUTH_ENABLE);
        auth1U = (authMap.charAt(YuimarlUser.AUTH_USER_UPDATE) == YuimarlUser.AUTH_ENABLE);
        auth1D = (authMap.charAt(YuimarlUser.AUTH_USER_DELETE) == YuimarlUser.AUTH_ENABLE);
        auth2V = (authMap.charAt(YuimarlUser.AUTH_USER_AUTH_VIEW) == YuimarlUser.AUTH_ENABLE);
        auth2C = (authMap.charAt(YuimarlUser.AUTH_USER_AUTH_CREATE) == YuimarlUser.AUTH_ENABLE);
        auth2U = (authMap.charAt(YuimarlUser.AUTH_USER_AUTH_UPDATE) == YuimarlUser.AUTH_ENABLE);
        auth2D = (authMap.charAt(YuimarlUser.AUTH_USER_AUTH_DELETE) == YuimarlUser.AUTH_ENABLE);
        auth3V = (authMap.charAt(YuimarlUser.AUTH_GOODS_CATEGORY_VIEW) == YuimarlUser.AUTH_ENABLE);
        auth3C = (authMap.charAt(YuimarlUser.AUTH_GOODS_CATEGORY_CREATE) == YuimarlUser.AUTH_ENABLE);
        auth3U = (authMap.charAt(YuimarlUser.AUTH_GOODS_CATEGORY_UPDATE) == YuimarlUser.AUTH_ENABLE);
        auth3D = (authMap.charAt(YuimarlUser.AUTH_GOODS_CATEGORY_DELETE) == YuimarlUser.AUTH_ENABLE);
        auth4V = (authMap.charAt(YuimarlUser.AUTH_PARTY_VIEW) == YuimarlUser.AUTH_ENABLE);
        auth4C = (authMap.charAt(YuimarlUser.AUTH_PARTY_CREATE) == YuimarlUser.AUTH_ENABLE);
        auth4U = (authMap.charAt(YuimarlUser.AUTH_PARTY_UPDATE) == YuimarlUser.AUTH_ENABLE);
        auth4D = (authMap.charAt(YuimarlUser.AUTH_PARTY_DELETE) == YuimarlUser.AUTH_ENABLE);

        if (current.getAuthRegistUser() != null) {
            authRegUser = current.getAuthRegistUser().getUserId() + " " + current.getAuthRegistUser().getParty().getName();
            authRegTime = current.getAuthRegistTime();
        }
        if (current.getAuthUpdateUser() != null) {
            authUpdUser = current.getAuthUpdateUser().getUserId() + " " + current.getAuthUpdateUser().getParty().getName();
            authUpdTime = current.getAuthUpdateTime();
        }

        return PageNavigation.AUTHORITY;
    }

    /**
     * 登録を行う。
     *
     * @return
     */
    public PageNavigation create() {
        try {
            Party p = checkInput();
            if (p == null) {
                return null;
            }
            String passMd5 = YuimarlUtil.generateMD5(password1);
            current.setPassword(passMd5);
            current.setParty(p);
            YuimarlUser user = YuimarlUtil.getLoginUser(userService);
            current.setRegistUser(user);
            current.setRegistTime(new Date());
            userService.create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("UserCreated"));
            YuimarlUtil.writeLog("Created YuimarlUser No.:" + current.getUserNo() + ", ID:" + current.getUserId() + ".");
            initPagination();
            return PageNavigation.VIEW;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            e.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * 編集画面に遷移する。
     *
     * @return
     */
    public PageNavigation prepareEdit() {
        modeCreate = false;
        current = userList.get(this.lineNo);
        if (current.getParty() == null) {
            partyNo = null;
        } else {
            partyNo = current.getParty().getPartyNo();
        }
        password1 = "";
        password2 = "";
        return PageNavigation.EDIT;
    }

    /**
     * 更新を行う。
     *
     * @return
     */
    public PageNavigation update() {
        try {
            Party p = checkInput();
            if (p == null) {
                return null;
            }
            String passMd5 = YuimarlUtil.generateMD5(password1);
            current.setPassword(passMd5);
            current.setParty(p);
            YuimarlUser user = YuimarlUtil.getLoginUser(userService);
            current.setUpdateUser(user);
            current.setUpdateTime(new Date());
            userService.edit(current);
            YuimarlUtil.writeLog("Updated YuimarlUser No.:" + current.getUserNo() + ", ID:" + current.getUserId() + ".");
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("UserUpdated"));
            initPagination();
            return PageNavigation.VIEW;
        } catch (OptimisticLockException e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("OptimisticLockException"));
            return null;
        } catch (EJBException e) {
            if (e.getCausedByException() != null
                    && e.getCausedByException().getClass().getTypeName().equals("javax.persistence.OptimisticLockException")) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("OptimisticLockException"));
            } else {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            }
            return null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            e.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * ユーザー権限を更新する。
     *
     * @return
     */
    public PageNavigation authUpdate() {
        try {
            StringBuilder buf = new StringBuilder();
            auth1V = (auth1V || auth1C || auth1U || auth1D);
            auth2V = (auth2V || auth2C || auth2U || auth2D);
            auth3V = (auth3V || auth3C || auth3U || auth3D);
            auth4V = (auth4V || auth4C || auth4U || auth4D);
            buf.append(authPw ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
            buf.append(auth1V ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
            buf.append(auth1C ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
            buf.append(auth1U ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
            buf.append(auth1D ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
            buf.append(auth2V ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
            buf.append(auth2C ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
            buf.append(auth2U ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
            buf.append(auth2D ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
            buf.append(auth3V ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
            buf.append(auth3C ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
            buf.append(auth3U ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
            buf.append(auth3D ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
            buf.append(auth4V ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
            buf.append(auth4C ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
            buf.append(auth4U ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
            buf.append(auth4D ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);

            if (current.getAuthMap() == null || current.getAuthMap().length() == 0) {
                current.setAuthRegistUser(YuimarlUtil.getLoginUser(userService));
                current.setAuthRegistTime(new Date());
            } else {
                current.setAuthUpdateUser(YuimarlUtil.getLoginUser(userService));
                current.setAuthUpdateTime(new Date());
            }
            current.setAuthMap(buf.toString());
            userService.edit(current);

            YuimarlUtil.writeLog("Updated YuimarlUser Authority ID:" + current.getUserId() + ".");
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("UserAuthUpdated"));
            prepareAuthSub();
            modeUpdate = false;
            return null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            e.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * 編集画面の入力内容をチェックする。
     *
     * @return 正常:Partyのインスタンス, 不正:null
     */
    private Party checkInput() {
        if (current.getUserId().length() < 3) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE).getString("user.userId"));
            return null;
        }
        if (password1.equals(password2) == false) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE).getString("user.passwordAgain"));
            return null;
        }
        if (password1.length() < 4) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE).getString("user.password"));
            return null;
        }
        // User IDの存在チェック
        YuimarlUser user = userService.getYuimarlUserByUserId(current.getUserId());
        if (user != null) {
            if (modeCreate || (modeCreate == false && !Objects.equals(user.getUserNo(), current.getUserNo()))) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE).getString("user.userId.exist"));
                return null;
            }
        }
        Party p = partyService.getPartyByPartyNo(partyNo);
        if (p == null) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE).getString("user.partyNo"));
            return null;
        }
        return p;
    }

    /**
     * 論理削除を行う。
     *
     * @return
     */
    public PageNavigation destroy() {
        if (this.conf.equals("0")) {
            return null;
        }
        try {
            current.setDelFlg('1');
            YuimarlUser user = YuimarlUtil.getLoginUser(userService);
            current.setUpdateUser(user);
            current.setUpdateTime(new Date());
            userService.edit(current);
            YuimarlUtil.writeLog("Deleted YuimarlUser No.:" + current.getUserNo() + ", ID:" + current.getUserId() + ".");
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("UserDeleted"));
            initPagination();
            return PageNavigation.LIST;
        } catch (OptimisticLockException e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("OptimisticLockException"));
            return null;
        } catch (EJBException e) {
            if (e.getCausedByException() != null
                    && e.getCausedByException().getClass().getTypeName().equals("javax.persistence.OptimisticLockException")) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("OptimisticLockException"));
            } else {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            }
            return null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            e.printStackTrace(System.err);
            return null;
        }
    }

}
