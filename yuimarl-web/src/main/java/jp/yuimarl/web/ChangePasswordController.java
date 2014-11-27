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
import java.util.Date;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import jp.yuimarl.ejb.YuimarlUserService;
import jp.yuimarl.entity.YuimarlUser;
import jp.yuimarl.util.JsfUtil;
import jp.yuimarl.util.PageNavigation;
import jp.yuimarl.util.YuimarlUtil;

/**
 * パスワード変更制御クラス
 *
 * @author Masahiro Nitta
 */
@Named(value = "changePasswordController")
@RequestScoped
public class ChangePasswordController implements Serializable {

    private static final String BUNDLE = "bundles.Bundle";

    @EJB
    private YuimarlUserService userService;

    private String currentPassword = null;
    private String newPassword1 = null;
    private String newPassword2 = null;

    /**
     * 現在のパスワードを取得する。
     *
     * @return
     */
    public String getCurrentPassword() {
        return currentPassword;
    }

    /**
     * 現在のパスワードを設定する。
     *
     * @param currentPassword
     */
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    /**
     * 新パスワードを取得する。
     *
     * @return
     */
    public String getNewPassword1() {
        return newPassword1;
    }

    /**
     * 新パスワードを設定する。
     *
     * @param newPassword1
     */
    public void setNewPassword1(String newPassword1) {
        this.newPassword1 = newPassword1;
    }

    /**
     * 新パスワード（再度）を取得する。
     *
     * @return
     */
    public String getNewPassword2() {
        return newPassword2;
    }

    /**
     * 新パスワード（再度）を設定する。
     *
     * @param newPassword2
     */
    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    /**
     * パスワード変更画面に遷移する。
     *
     * @return
     */
    public PageNavigation prepareEditPassword() {
        this.currentPassword = "";
        this.newPassword1 = "";
        this.newPassword2 = "";
        return PageNavigation.EDIT_PASSWORD;
    }

    /**
     * 更新を実行する。
     *
     * @return
     */
    public PageNavigation update() {
        YuimarlUser user = YuimarlUtil.getLoginUser(userService);
        if (user == null) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE).getString("user.userId.exist"));
            return null;
        }
        String passMd5 = YuimarlUtil.generateMD5(this.currentPassword);
        if (user.getPassword().equals(passMd5) == false) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE).getString("CurrentPasswordError"));
            return null;
        }
        if (this.newPassword1.equals(this.newPassword2) == false) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE).getString("user.passwordAgain"));
            return null;
        }
        if (this.newPassword1.length() < 4) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE).getString("user.password"));
            return null;
        }
        passMd5 = YuimarlUtil.generateMD5(this.newPassword1);
        user.setPassword(passMd5);
        user.setUpdateUser(user);
        user.setUpdateTime(new Date());
        userService.edit(user);
        YuimarlUtil.writeLog("Password changed YuimarlUser No.:" + user.getUserNo() + ", ID:" + user.getUserId() + ".");
        JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("UserUpdated"));
        return PageNavigation.INDEX;
    }

}
