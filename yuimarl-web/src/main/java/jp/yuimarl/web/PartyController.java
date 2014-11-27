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
import javax.faces.component.UISelectBoolean;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import jp.yuimarl.ejb.GoodsCategoryService;
import jp.yuimarl.ejb.OrganizationCategoryService;
import jp.yuimarl.ejb.PartyService;
import jp.yuimarl.ejb.PartyRelationService;
import jp.yuimarl.ejb.PrefectureService;
import jp.yuimarl.ejb.YuimarlUserService;
import jp.yuimarl.entity.Address;
import jp.yuimarl.entity.Corporation;
import jp.yuimarl.entity.Goods;
import jp.yuimarl.entity.Organization;
import jp.yuimarl.entity.OrganizationCategory;
import jp.yuimarl.entity.Party;
import jp.yuimarl.entity.PartyRelation;
import jp.yuimarl.entity.Person;
import jp.yuimarl.entity.Prefecture;
import jp.yuimarl.entity.YuimarlUser;
import jp.yuimarl.util.JsfUtil;
import jp.yuimarl.util.PageNavigation;
import jp.yuimarl.util.YuimarlPagination;
import jp.yuimarl.util.YuimarlUtil;

/**
 * Party制御クラス
 *
 * @author Masahiro Nitta
 */
@Named(value = "partyController")
@SessionScoped
public class PartyController implements Serializable {

    private static final String BUNDLE = "bundles.Bundle";

    @EJB
    private PartyService partyService;
    @EJB
    private YuimarlUserService userService;
    @EJB
    private PrefectureService prefectureService;
    @EJB
    private OrganizationCategoryService organizationCategoryService;
    @EJB
    private GoodsCategoryService goodsCategoryService;
    @EJB
    private PartyRelationService partyRelationService;

    private Party current = null;
    private PartyRelation curRel = null;
    private DataModel items = null;
    private List<PartyRelation> relList = null;
    private List<PartyRelation> relPageList = null;
    private List<Party> mPartyList = null;
    private YuimarlPagination pagination;
    private YuimarlPagination relPagination;
    private List<SelectItem> genderList;
    private List<SelectItem> prefectureList;
    private List<SelectItem> organizationCategoryList;
    private List<SelectItem> corporationCategoryList;
    private List<SelectItem> relationTypeList;
    private List<SelectItem> partyTypeList;
    private boolean modeCreate = false;
    private String goodsCategoryTree;
    private Integer searchPartyNo;
    private Integer dlgPartyNo;
    private String searchPartyName;
    private String[] partyTypes;
    private Integer lineNo = 0;
    private Integer party1No;
    private Integer party2No;
    private String conf;
    private String searchCondition = "";
    private String partyJson;
    private UISelectBoolean partyType1 = null;
    private UISelectBoolean partyType2 = null;
    private UISelectBoolean partyType3 = null;
    private UISelectBoolean partyType4 = null;

    /**
     * コンストラクタ
     */
    public PartyController() {
    }

    /**
     * 選択されているPartyを取得する。
     *
     * @return
     */
    public Party getSelected() {
        if (current == null) {
            current = new Party();
        }
        return current;
    }

    /**
     * 選択されているParty関連を取得する。
     *
     * @return
     */
    public PartyRelation getRelSelected() {
        if (curRel == null) {
            curRel = new PartyRelation();
        }
        return curRel;
    }

    /**
     * Party一覧画面のDataModelを取得する。
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
     *
     * @return
     */
    public List<Party> getmPartyList() {
        return mPartyList;
    }

    /**
     * Party関連一覧画面のデータのリスト（全体）を取得する。
     *
     * @return
     */
    public List<PartyRelation> getRelList() {
        return relList;
    }

    /**
     * Party関連一覧画面のデータのリスト（ページ内）を取得する。
     *
     * @return
     */
    public List<PartyRelation> getRelPageList() {
        return relPageList;
    }

    /**
     * Party一覧画面の一覧ページ制御クラスのインスタンスを取得する。
     *
     * @return
     */
    public YuimarlPagination getPagination() {
        if (pagination == null) {
            pagination = new YuimarlPagination(YuimarlPagination.DEFAULT_SIZE, 0) {

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel();
                }
            };
        }
        return pagination;
    }

    /**
     * Party関連一覧画面の一覧ページ制御クラスのインスタンスを取得する。
     *
     * @return
     */
    public YuimarlPagination getRelPagination() {
        return relPagination;
    }

    /**
     * 選択されているPartyのParty種別を取得する。
     *
     * @return
     */
    public Character getPartyType() {
        return current.getPartyType();
    }

    /**
     * 選択されているPartyが組織であるかを取得する。
     *
     * @return
     */
    public boolean isOrganization() {
        return (current instanceof Organization);
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
     * dynatreeのツリー用タグを取得する。
     *
     * @return
     */
    public String getGoodsCategoryTree() {
        return goodsCategoryTree;
    }

    /**
     * 選択されている物品のカテゴリNo.を取得する。
     *
     * @return
     */
    public Integer getCategoryNo() {
        if ((current instanceof Goods) == false) {
            return null;
        }
        Goods goods = (Goods) current;
        if (goods.getCategory() == null) {
            return null;
        }
        return goods.getCategory().getCategoryNo();
    }

    /**
     * 選択されている物品のカテゴリNo.を設定する。
     *
     * @param categoryNo
     */
    public void setCategoryNo(Integer categoryNo) {
        if ((current instanceof Goods) == false) {
            return;
        }
        Goods goods = (Goods) current;
        if (categoryNo == null || categoryNo == 0) {
            goods.setCategory(null);
        } else {
            goods.setCategory(goodsCategoryService.findByCategoryNo(categoryNo));
        }
    }

    /**
     * 選択されているParty関連のParty関連種別を取得する。
     *
     * @return
     */
    public int getRelationType() {
        return getRelSelected().getRelationType();
    }

    /**
     * 選択されているParty関連のParty関連種別を設定する。
     *
     * @param relationType
     */
    public void setRelationType(int relationType) {
        getRelSelected().setRelationType(relationType);
    }

    /**
     * 検索用Party No.を取得する。
     *
     * @return
     */
    public Integer getSearchPartyNo() {
        return searchPartyNo;
    }

    /**
     * 検索用Party No.を設定する。
     *
     * @param searchPartyNo
     */
    public void setSearchPartyNo(Integer searchPartyNo) {
        this.searchPartyNo = searchPartyNo;
    }

    /**
     * Partyダイアログ用Party No.を取得する。
     *
     * @return
     */
    public Integer getDlgPartyNo() {
        return dlgPartyNo;
    }

    /**
     * PartyダイアログParty No.を設定する。
     *
     * @param dlgPartyNo
     */
    public void setDlgPartyNo(Integer dlgPartyNo) {
        this.dlgPartyNo = dlgPartyNo;
    }

    /**
     * 検索用名前を取得する。
     *
     * @return
     */
    public String getSearchPartyName() {
        return searchPartyName;
    }

    /**
     * 検索用名前を設定する。
     *
     * @param searchPartyName
     */
    public void setSearchPartyName(String searchPartyName) {
        this.searchPartyName = searchPartyName;
    }

    /**
     * 検索用Party種別を取得する。
     *
     * @return
     */
    public String[] getPartyTypes() {
        return partyTypes;
    }

    /**
     * 検索用Party種別を設定する。
     *
     * @param partyTypes
     */
    public void setPartyTypes(String[] partyTypes) {
        this.partyTypes = partyTypes;
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
     * Party関連編集画面のParty1のParty No.を取得する。
     *
     * @return
     */
    public Integer getParty1No() {
        return party1No;
    }

    /**
     * Party関連編集画面のParty1のParty No.を設定する。
     *
     * @param party1No
     */
    public void setParty1No(Integer party1No) {
        this.party1No = party1No;
    }

    /**
     * Party関連編集画面のParty2のParty No.を取得する。
     *
     * @return
     */
    public Integer getParty2No() {
        return party2No;
    }

    /**
     * Party関連編集画面のParty2のParty No.を設定する。
     *
     * @param party2No
     */
    public void setParty2No(Integer party2No) {
        this.party2No = party2No;
    }

    /**
     * Party関連編集画面のParty1の名前をAjaxで取得する。
     *
     * @return
     */
    public String getParty1Name() {
        if (party1No == null || party1No == 0) {
            return "";
        }
        Party p1 = partyService.getPartyByPartyNo(party1No);
        if (p1 == null) {
            return "";
        }
        return p1.getName();
    }

    /**
     * Party関連編集画面のParty2の名前をAjaxで取得する。
     *
     * @return
     */
    public String getParty2Name() {
        if (party2No == null || party2No == 0) {
            return "";
        }
        Party p2 = partyService.getPartyByPartyNo(party2No);
        if (p2 == null) {
            return "";
        }
        return p2.getName();
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
     * Party関連一覧画面のデータ件数を取得する。
     *
     * @return
     */
    public int getRelListCount() {
        if (relList == null) {
            return 0;
        }
        return relList.size();
    }

    /**
     * 性別選択リストを取得する。
     *
     * @return
     */
    public List<SelectItem> getGenderList() {
        if (genderList == null) {
            genderList = new ArrayList<>();
            genderList.add(new SelectItem("1", "男性"));
            genderList.add(new SelectItem("2", "女性"));
            genderList.add(new SelectItem("0", "(不明)"));
        }
        return genderList;
    }

    /**
     * 性別選択リストを設定する。
     *
     * @param genderList
     */
    public void setGenderList(List<SelectItem> genderList) {
        this.genderList = genderList;
    }

    /**
     * 都道府県選択リストを取得する。
     *
     * @return
     */
    public List<SelectItem> getPrefectureList() {
        if (prefectureList == null) {
            prefectureList = new ArrayList<>();
            prefectureList.add(new SelectItem("0", "(不明)"));
            List<Prefecture> list = prefectureService.findAll();
            for (Prefecture p : list) {
                prefectureList.add(new SelectItem(p.getPrefectureNo().toString(), p.getName()));
            }
        }
        return prefectureList;
    }

    /**
     * 都道府県選択リストを設定する。
     *
     * @param prefectureList
     */
    public void setPrefectureList(List<SelectItem> prefectureList) {
        this.prefectureList = prefectureList;
    }

    /**
     * 都道府県No.を取得する。
     *
     * @return
     */
    public int getPrefectureNo() {
        if (current.getAddress().getPrefecture() == null) {
            return 0;
        }
        return current.getAddress().getPrefecture().getPrefectureNo();
    }

    /**
     * 都道府県No.を設定する。
     *
     * @param prefectureNo
     */
    public void setPrefectureNo(int prefectureNo) {
        if (prefectureNo == 0) {
            current.getAddress().setPrefecture(null);
        }
        current.getAddress().setPrefecture(prefectureService.getPrefectureByPrefectureNo(prefectureNo));
    }

    /**
     * 組織種類選択リストを取得する。
     *
     * @return
     */
    public List<SelectItem> getOrganizationCategoryList() {
        if (organizationCategoryList == null) {
            organizationCategoryList = new ArrayList<>();
            organizationCategoryList.add(new SelectItem("0", "(不明)"));
            List<OrganizationCategory> list = organizationCategoryService.findAllOrganization();
            for (OrganizationCategory c : list) {
                organizationCategoryList.add(new SelectItem(c.getCategoryNo().toString(), c.getName()));
            }
        }
        return organizationCategoryList;
    }

    /**
     * 法人種類選択リストを取得する。
     *
     * @return
     */
    public List<SelectItem> getCorporationCategoryList() {
        if (corporationCategoryList == null) {
            corporationCategoryList = new ArrayList<>();
            corporationCategoryList.add(new SelectItem("0", "(不明)"));
            List<OrganizationCategory> list = organizationCategoryService.findAllCorporation();
            for (OrganizationCategory c : list) {
                corporationCategoryList.add(new SelectItem(c.getCategoryNo().toString(), c.getName()));
            }
        }
        return corporationCategoryList;
    }

    /**
     * 組織種類No.を取得する。
     *
     * @return
     */
    public int getOrganizationCategoryNo() {
        if (((Organization) current).getCategory() == null) {
            return 0;
        }
        return ((Organization) current).getCategory().getCategoryNo();
    }

    /**
     * 組織種類No.を設定する。
     *
     * @param categoryNo
     */
    public void setOrganizationCategoryNo(int categoryNo) {
        Organization org = (Organization) current;
        if (categoryNo == 0) {
            org.setCategory(null);
        }
        org.setCategory(organizationCategoryService.getOrganizationCategoryByCategoryNo(categoryNo));
    }

    /**
     * Party関連種別選択リストを取得する。
     *
     * @return
     */
    public List<SelectItem> getRelationTypeList() {
        if (relationTypeList == null) {
            relationTypeList = new ArrayList<>();
            relationTypeList.add(new SelectItem("0", "(不明)"));
            for (int i = 0; i < PartyRelation.relationTypes1.length; i++) {
                relationTypeList.add(new SelectItem(String.valueOf(i + 1),
                        PartyRelation.relationTypes1[i] + "←→" + PartyRelation.relationTypes2[i]));
            }
        }
        return relationTypeList;
    }

    /**
     * Party関連種別選択リストを設定する。
     *
     * @param relationTypeList
     */
    public void setRelationTypeList(List<SelectItem> relationTypeList) {
        this.relationTypeList = relationTypeList;
    }

    /**
     * Party種別選択リストを取得する。
     *
     * @return
     */
    public List<SelectItem> getPartyTypeList() {
        if (partyTypeList == null) {
            partyTypeList = new ArrayList<>();
            partyTypeList.add(new SelectItem("1", "人"));
            partyTypeList.add(new SelectItem("2", "組織"));
            partyTypeList.add(new SelectItem("3", "法人"));
            partyTypeList.add(new SelectItem("4", "物品"));
        }
        return partyTypeList;
    }

    public String getPartyJson() {
        return partyJson;
    }

    public void setPartyJson(String partyJson) {
        this.partyJson = partyJson;
    }

    /**
     * Party種別選択リストを設定する。
     *
     * @param partyTypeList
     */
    public void setPartyTypeList(List<SelectItem> partyTypeList) {
        this.partyTypeList = partyTypeList;
    }

    public UISelectBoolean getPartyType1() {
        return partyType1;
    }

    public void setPartyType1(UISelectBoolean partyType1) {
        this.partyType1 = partyType1;
    }

    public UISelectBoolean getPartyType2() {
        return partyType2;
    }

    public void setPartyType2(UISelectBoolean partyType2) {
        this.partyType2 = partyType2;
    }

    public UISelectBoolean getPartyType3() {
        return partyType3;
    }

    public void setPartyType3(UISelectBoolean partyType3) {
        this.partyType3 = partyType3;
    }

    public UISelectBoolean getPartyType4() {
        return partyType4;
    }

    public void setPartyType4(UISelectBoolean partyType4) {
        this.partyType4 = partyType4;
    }

    /**
     * Party一覧画面のDataModelを初期化する。
     */
    private void recreateModel() {
        items = null;
    }

    /**
     * Party一覧画面に遷移する。
     *
     * @return
     */
    public PageNavigation prepareList() {
        initPagination();
        return PageNavigation.PARTY_LIST;
    }

    /**
     * Party一覧画面の先頭ページに遷移する。
     *
     * @return
     */
    public PageNavigation pageTop() {
        getPagination().topPage();
        recreateModel();
        return null;
    }

    /**
     * Party一覧画面の先頭ページに遷移する。（モバイル用）
     *
     * @return
     */
    public PageNavigation pageTopM() {
        getPagination().topPage();
        items = getPagination().createPageDataModel();
        itemsToList();
        return null;
    }

    /**
     * Party一覧画面の1ページ前に遷移する。
     *
     * @return
     */
    public PageNavigation pagePrevious() {
        getPagination().previousPage();
        recreateModel();
        return null;
    }

    /**
     * Party一覧画面の1ページ前に遷移する。（モバイル用）
     *
     * @return
     */
    public PageNavigation pagePreviousM() {
        getPagination().previousPage();
        items = getPagination().createPageDataModel();
        itemsToList();
        return null;
    }

    /**
     * Party一覧画面の2ページ前に遷移する。
     *
     * @return
     */
    public PageNavigation pagePrevious2() {
        getPagination().previous2Page();
        recreateModel();
        return null;
    }

    /**
     * Party一覧画面の1ページ後に遷移する。
     *
     * @return
     */
    public PageNavigation pageNext() {
        getPagination().nextPage();
        recreateModel();
        return null;
    }

    /**
     * Party一覧画面の1ページ後に遷移する。（モバイル用）
     *
     * @return
     */
    public PageNavigation pageNextM() {
        getPagination().nextPage();
        items = getPagination().createPageDataModel();
        itemsToList();
        return null;
    }

    /**
     * Party一覧画面の2ページ後に遷移する。
     *
     * @return
     */
    public PageNavigation pageNext2() {
        getPagination().next2Page();
        recreateModel();
        return null;
    }

    /**
     * Party一覧画面の最終ページに遷移する。
     *
     * @return
     */
    public PageNavigation pageLast() {
        getPagination().lastPage();
        recreateModel();
        return null;
    }

    /**
     * Party一覧画面の最終ページに遷移する。（モバイル用）
     *
     * @return
     */
    public PageNavigation pageLastM() {
        getPagination().lastPage();
        items = getPagination().createPageDataModel();
        itemsToList();
        return null;
    }

    /**
     * Party表示画面に遷移する。
     *
     * @return
     */
    public PageNavigation prepareView() {
        current = (Party) getItems().getRowData();
        return PageNavigation.VIEW;
    }

    /**
     * Party表示画面に遷移する。（モバイル用）
     *
     * @return
     */
    public PageNavigation prepareMView() {
        current = mPartyList.get(this.lineNo);
        return PageNavigation.M_VIEW;
    }

    /**
     * 人の登録画面に遷移する。
     *
     * @return
     */
    public PageNavigation preparePersonCreate() {
        Person p = new Person();
        p.setAddress(new Address());
        p.setGender(0);
        current = p;
        modeCreate = true;
        return PageNavigation.PARTY_EDIT;
    }

    /**
     * 組織の登録画面に遷移する。
     *
     * @return
     */
    public PageNavigation prepareOrganizationCreate() {
        Organization o = new Organization();
        o.setAddress(new Address());
        current = o;
        modeCreate = true;
        return PageNavigation.PARTY_EDIT;
    }

    /**
     * 法人の登録画面に遷移する。
     *
     * @return
     */
    public PageNavigation prepareCorporationCreate() {
        Corporation c = new Corporation();
        c.setAddress(new Address());
        current = c;
        modeCreate = true;
        return PageNavigation.PARTY_EDIT;
    }

    /**
     * 物品の登録画面に遷移する。
     *
     * @return
     */
    public PageNavigation prepareGoodsCreate() {
        Goods g = new Goods();
        g.setAddress(new Address());
        goodsCategoryTree = goodsCategoryService.makeTag(false);
        current = g;
        modeCreate = true;
        return PageNavigation.PARTY_EDIT;
    }

    /**
     * Partyの編集画面に遷移する。
     *
     * @return
     */
    public PageNavigation prepareEdit() {
        modeCreate = false;
        current = (Party) getItems().getRowData();
        if (current.getAddress() == null) {
            current.setAddress(new Address());
        }
        if (current instanceof Goods) {
            goodsCategoryTree = goodsCategoryService.makeTag(false);
        }
        return PageNavigation.EDIT;
    }

    /**
     * Party関連一覧画面に遷移する。
     *
     * @return
     */
    public PageNavigation prepareRelationList() {
        current = (Party) getItems().getRowData();
        initRelList();

        return PageNavigation.REL_LIST;
    }

    /**
     * Party関連一覧画面の先頭ページに遷移する。
     *
     * @return
     */
    public PageNavigation relPageTop() {
        relPagination.topPage();
        relPageChange();
        return null;
    }

    /**
     * Party関連一覧画面の1ページ前に遷移する。
     *
     * @return
     */
    public PageNavigation relPagePrevious() {
        relPagination.previousPage();
        relPageChange();
        return null;
    }

    /**
     * Party関連一覧画面の2ページ前に遷移する。
     *
     * @return
     */
    public PageNavigation relPagePrevious2() {
        relPagination.previous2Page();
        relPageChange();
        return null;
    }

    /**
     * Party関連一覧画面の1ページ後に遷移する。
     *
     * @return
     */
    public PageNavigation relPageNext() {
        relPagination.nextPage();
        relPageChange();
        return null;
    }

    /**
     * Party関連一覧画面の2ページ後に遷移する。
     *
     * @return
     */
    public PageNavigation relPageNext2() {
        relPagination.next2Page();
        relPageChange();
        return null;
    }

    /**
     * Party関連一覧画面の最終ページに遷移する。
     *
     * @return
     */
    public PageNavigation relPageLast() {
        relPagination.lastPage();
        relPageChange();
        return null;
    }

    /**
     * Party関連の登録画面に遷移する。
     *
     * @return
     */
    public PageNavigation prepareRelCreate() {
        PartyRelation r = new PartyRelation();
        r.setRelationType(1);
        curRel = r;
        this.party1No = null;
        this.party2No = null;
        modeCreate = true;
        return PageNavigation.REL_EDIT;
    }

    /**
     * Party関連の編集画面に遷移する。
     *
     * @return
     */
    public PageNavigation prepareRelEdit() {
        modeCreate = false;
        curRel = relPageList.get(this.lineNo);
        this.party1No = curRel.getParty1().getPartyNo();
        this.party2No = curRel.getParty2().getPartyNo();
        return PageNavigation.REL_EDIT;
    }

    /**
     * Party一覧画面の一覧ページ制御クラスのインスタンスを生成する。
     */
    private void initPagination() {
        pagination = new YuimarlPagination(YuimarlPagination.DEFAULT_SIZE, partyService.condCount(searchCondition)) {

            @Override
            public DataModel createPageDataModel() {
                return new ListDataModel(partyService.findCondRange(searchCondition, new int[]{getPageFirstItem(),
                    getPageFirstItem() + getPageSize()}));
            }
        };
        items = pagination.createPageDataModel();
    }

    /**
     * Party関連一覧画面のデータを取得し、データのリストを構成する。
     */
    private void initRelList() {
        relList = partyRelationService.findByParty(current);
        // 一覧ページ制御クラスのインスタンスを生成する。
        relPagination = new YuimarlPagination(YuimarlPagination.DEFAULT_SIZE, relList.size()) {

            @Override
            public DataModel createPageDataModel() {
                return null;
            }
        };
        relPageChange();
    }

    /**
     * Party関連一覧画面の、データのリスト（ページ内）を構成する。
     */
    private void relPageChange() {
        relPageList = new ArrayList<>();
        int j = relPagination.getPageFirstItem();
        for (int i = 0; i < YuimarlPagination.DEFAULT_SIZE; i++) {
            if (j >= relList.size()) {
                break;
            }
            relPageList.add(relList.get(j));
            j++;
        }
    }

    /**
     * Partyの登録を行う。
     *
     * @return
     */
    public PageNavigation create() {
        try {
            YuimarlUser user = YuimarlUtil.getLoginUser(userService);
            current.setRegistUser(user);
            current.setRegistTime(new Date());
            partyService.create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("PartyCreated"));
            YuimarlUtil.writeLog("Created party No.:" + current.getPartyNo() + ", Name:" + current.getName() + ".");
            initPagination();
            return PageNavigation.VIEW;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * Partyの更新を行う。
     *
     * @return
     */
    public PageNavigation update() {
        try {
            YuimarlUtil.writeLog("Updating party No.:" + current.getPartyNo() + ", Name:" + current.getName() + ".");
            YuimarlUser user = YuimarlUtil.getLoginUser(userService);
            current.setUpdateUser(user);
            current.setUpdateTime(new Date());
            partyService.edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("PartyUpdated"));
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
            return null;
        }
    }

    /**
     * Partyの削除を行う。
     *
     * @return
     */
    public PageNavigation destroy() {
        try {
            if (this.conf.equals("0")) {
                // 削除確認がOKでなければ、削除しない。
                return null;
            }
            current = (Party) getItems().getRowData();
            // このPartyがYuimarlUserで使用されていれば、削除できない。
            YuimarlUser u = userService.getYuimarlUserByPartyNo(current.getPartyNo());
            if (u != null) {
                JsfUtil.addErrorMessage("This party is used by YuimarlUser: " + u.getUserId() + ".");
                return null;
            }
            u = YuimarlUtil.getLoginUser(userService);
            current.setUpdateUser(u);
            current.setUpdateTime(new Date());
            YuimarlUtil.writeLog("Deleted party No.:" + current.getPartyNo() + ", Name:" + current.getName() + ".");
            // PartyとParty関連を削除する。
            partyService.removePartyAndRelation(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("PartyDeleted"));
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
            return null;
        }
    }

    /**
     * Party関連の登録を行う。
     *
     * @return
     */
    public PageNavigation relCreate() {
        try {
            Party p1 = partyService.getPartyByPartyNo(party1No);
            if (p1 == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE).getString("user.partyNo"));
                return null;
            }
            Party p2 = partyService.getPartyByPartyNo(party2No);
            if (p2 == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE).getString("user.partyNo"));
                return null;
            }
            if (Objects.equals(party1No, party2No)) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE).getString("party1equal2"));
                return null;
            }
            YuimarlUser user = YuimarlUtil.getLoginUser(userService);
            curRel.setParty1(p1);
            curRel.setParty2(p2);
            curRel.setRegistUser(user);
            curRel.setRegistTime(new Date());
            partyRelationService.create(curRel);
            YuimarlUtil.writeLog("Created partyRelation No.:" + curRel.getRelationNo() + ".");
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("PartyCreated"));
            initRelList();
            return PageNavigation.REL_LIST;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * Party関連の更新を行う。
     *
     * @return
     */
    public PageNavigation relUpdate() {
        try {
            Party p1 = partyService.getPartyByPartyNo(party1No);
            if (p1 == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE).getString("user.partyNo"));
                return null;
            }
            Party p2 = partyService.getPartyByPartyNo(party2No);
            if (p2 == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE).getString("user.partyNo"));
                return null;
            }
            if (Objects.equals(party1No, party2No)) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE).getString("party1equal2"));
                return null;
            }
            YuimarlUtil.writeLog("Updating partyRelation No.:" + curRel.getRelationNo() + ".");
            YuimarlUser user = YuimarlUtil.getLoginUser(userService);
            curRel.setParty1(p1);
            curRel.setParty2(p2);
            curRel.setUpdateUser(user);
            curRel.setUpdateTime(new Date());
            partyRelationService.edit(curRel);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("PartyUpdated"));
            initRelList();
            return PageNavigation.REL_LIST;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * Party関連の削除を行う。
     *
     * @return
     */
    public PageNavigation relDestroy() {
        try {
            if (this.conf.equals("0")) {
                // 削除確認がOKでなければ、削除しない。
                return null;
            }
            curRel = relPageList.get(this.lineNo);
            YuimarlUtil.writeLog("Deleting partyRelation No.:" + curRel.getRelationNo() + ".");
            partyRelationService.remove(curRel);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("PartyDeleted"));
            initRelList();
            return PageNavigation.REL_LIST;
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
            return null;
        }
    }

    /**
     * 検索を実行する。
     *
     * @return
     */
    public PageNavigation search() {
        // 検索用のJPQL条件文を生成する。
        searchCondition = "";
        if (searchPartyNo != null && searchPartyNo != 0) {
            searchCondition = "AND p.partyNo = " + searchPartyNo;
        } else {
            if (searchPartyName != null && searchPartyName.trim().length() > 0) {
                searchCondition = "AND p.name LIKE '%" + searchPartyName.trim().replaceAll("'", "''") + "%'";
            }
            if (partyTypes.length > 0 && partyTypes.length != 4) {
                searchCondition += "AND p.partyType IN (";
                for (int i = 0; i < partyTypes.length; i++) {
                    if (i > 0) {
                        searchCondition += ",";
                    }
                    searchCondition += "'" + partyTypes[i] + "'";
                }
                searchCondition += ")";
            }
        }

        initPagination();
        return null;
    }

    /**
     * 検索を実行する。（モバイル用）
     *
     * @return
     */
    public PageNavigation searchM() {
        ArrayList l = new ArrayList<>();
        if (this.getPartyType1().getValue().toString().equals("true")) {
            l.add(Party.PARTY_TYPE_PERSON);
        }
        if (this.getPartyType2().getValue().toString().equals("true")) {
            l.add(Party.PARTY_TYPE_ORGANIZATION);
        }
        if (this.getPartyType3().getValue().toString().equals("true")) {
            l.add(Party.PARTY_TYPE_CORPORATION);
        }
        if (this.getPartyType4().getValue().toString().equals("true")) {
            l.add(Party.PARTY_TYPE_GOODS);
        }

        // 検索用のJPQL条件文を生成する。
        searchCondition = "";
        if (searchPartyNo != null && searchPartyNo != 0) {
            searchCondition = "AND p.partyNo = " + searchPartyNo;
        } else {
            if (searchPartyName != null && searchPartyName.trim().length() > 0) {
                searchCondition = "AND p.name LIKE '%" + searchPartyName.trim().replaceAll("'", "''") + "%'";
            }
            if (l.size() > 0 && l.size() != 4) {
                searchCondition = "AND p.partyType IN (";
                for (int i = 0; i < l.size(); i++) {
                    if (i > 0) {
                        searchCondition += ",";
                    }
                    searchCondition += "'" + l.get(i) + "'";
                }
                searchCondition += ")";
            }
        }

        initPagination();
        itemsToList();
        return PageNavigation.M_LIST;
    }

    /**
     * アイテムの配列をリストにセットする。
     */
    private void itemsToList() {
        Iterator it = items.iterator();
        if (mPartyList == null) {
            mPartyList = new ArrayList<>();
        } else {
            mPartyList.clear();
        }
        while (it.hasNext()) {
            mPartyList.add((Party) it.next());
        }
    }

    /**
     * AjaxでPartyの検索を行い、結果をJSONで返す。
     *
     * @param event
     * @throws AbortProcessingException
     */
    public void ajaxPartySearch(AjaxBehaviorEvent event)
            throws AbortProcessingException {
        Party p = partyService.getPartyByPartyNo(this.dlgPartyNo);
        if (p == null) {
            this.partyJson = "";
            return;
        }
        this.partyJson = p.partyToJson();
    }
}
