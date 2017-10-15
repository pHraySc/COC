package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dataservice.service.ICiCampaignsService;
import com.ailk.biapp.ci.dataservice.service.ICiLabelsAndIndexesService;
import com.ailk.biapp.ci.entity.*;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.*;
import com.ailk.biapp.ci.search.service.ISearchService;
import com.ailk.biapp.ci.service.*;
import com.ailk.biapp.ci.task.AbstractJob;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.http.ResponseEncodingUtil;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.BSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class CiIndexAction extends CIBaseAction {
    private Logger log = Logger.getLogger(CiIndexAction.class);
    private String city;
    private File file;
    private String fileFileName;
    private String callback;
    private List<String> top10;
    private Integer classId;
    private String keyWord;
    private String categoryId;
    private String labelId;
    private int labelType;
    private Pager pager;
    private String labelKeyWord;
    private String productName;
    private String id;
    private List<ProductDetailInfo> productDetailList;
    private List<LabelDetailInfo> labelDetailList;
    private List<CiLabelInfo> labelClass;
    private List<CiProductCategory> firstLevelProductCatogery;
    private boolean labelPage = false;
    private boolean productPage = false;
    private Integer exploreFromModuleFlag;
    private String sysFlag;
    private String noticeId;
    private String dataScope = "all";
    private List<DimCustomCreateType> createTypeList;
    private List<CrmTypeModel> peopleBaseList;
    private Collection<CrmTypeModel> peopleLifeList;
    private List<CiLabelRule> ciLabelRuleList;
    private List<CiCustomListInfo> ciCustomListInfoList;
    private List<CiGroupAttrRel> ciGroupAttrRelList;
    private Integer minSortNum;
    private Integer maxSortNum;
    private boolean flag;
    private String calcuElement;
    private String newLabelMonthFormat;
    private String newLabelDayFormat;
    private String jobName;
    private String methodName;
    private String dataDate;
    private Long customNum;
    private String columnId;
    private String url;
    private List<CiLabelInfoTree> categoryTreeList;
    private CiShopSessionModel ciShopSessionModel;
    private HashMap<String, Object> pojo = new HashMap();
    private List<CiShopSessionModel> ciShopSessionModelList;
    private CiCustomGroupInfo ciCustomGroupInfo;
    private List<CiLabelVerticalColumnRel> ciLabelVerticalColumnRelList;
    private CiLabelInfo searchBean;
    private CiCustomGroupInfo searchBeanCustom;
    private List<CiMdaSysTableColumn> ciMdaSysTableColumns;
    private String seachTypeId;
    private String seachKeyWord;
    private int userOperType;
    private List<DimScene> dimScenes;
    private List<CiCustomGroupInfo> recommendCustomInfos;
    private List<LabelDetailInfo> recommendLabelIfos;
    private int attentionTypeId = 1;
    private String refreshType = "4";
    private String labelTypeMap = "";
    private String c1LabelId = "";
    private String c2LabelId = "";
    private String c3LabelId = "";
    private String l1LabelName = "";
    private String l2LabelName = "";
    private String l3LabelName = "";
    private String rankingTypeId = "";
    private int forwardType;
    private String labelSearchType = "";
    private String messageSearchType = "";
    private String resourceShowType = "";
    private String alarmShowType = "";
    private String noticeType = "";
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;
    @Autowired
    private ICustomersManagerService customersManagerService;
    @Autowired
    private ICiProductService ciProductService;
    @Autowired
    private ICiMarketTacticsService marketTacticsService;
    @Autowired
    private ICiLabelsAndIndexesService iCiLabelsAndIndexesService;
    @Autowired
    private ICiCampaignsService iCiCampaignsService;
    @Autowired
    private ILogTransferService logTransferService;
    @Autowired
    private ICiCustomUserUseService ciCustomUserUseService;
    @Autowired
    private ICiTemplateInfoService ciTemplateInfoService;
    @Autowired
    private ICiLabelUserUseService ciLabelUserUseService;
    @Autowired
    private ICiGroupAttrRelService ciGroupAttrRelService;
    @Autowired
    private ISearchService searchService;
    private List<DimListTactics> dimListTacticsList;
    private String source;

    public CiIndexAction() {
    }

    public String findLabelCategory() throws Exception {
        this.categoryTreeList = this.ciLabelInfoService.queryAllLabelCategory();
        return "labelCategory";
    }

    public void findAllLabelMapTotalPage() throws Exception {
        List labelListB = this.ciLabelInfoService.queryThirdLabelList(this.labelId, this.labelType);
        ArrayList labelList = new ArrayList();
        Iterator thirdLevelLabelCount = labelListB.iterator();

        while(thirdLevelLabelCount.hasNext()) {
            CiLabelInfo secLabelSize = (CiLabelInfo)thirdLevelLabelCount.next();
            if(this.ciLabelInfoService.queryChildrenById(secLabelSize.getLabelId()).size() > 0) {
                labelList.add(secLabelSize);
            }
        }

        int var15 = 0;
        if(labelList != null && labelList.size() > 0) {
            var15 = labelList.size();
        }

        String var16 = Configure.getInstance().getProperty("SEC_LABELSIZE");
        int pageSize = "".equals(var16)?3:Integer.valueOf(var16).intValue();
        int totalPage = (int)Math.ceil((double)var15 / (double)pageSize);
        Cookie[] myCookies = this.getRequest().getCookies();
        boolean IsExitNums = false;

        for(int response = 0; response < myCookies.length; ++response) {
            Cookie e = myCookies[response];
            String numsStr = ServiceConstants.COOKIE_NAME_FOR_INDEX_LABEL_TIP + "_" + this.getUserId();
            if(numsStr.equals(e.getName())) {
                IsExitNums = true;
                String nums_value = e.getValue();
                int nums_value_int = Integer.parseInt(nums_value);
                ++nums_value_int;
                e.setValue(String.valueOf(nums_value_int));
                e.setMaxAge(31536000);
                this.getResponse().addCookie(e);
            }
        }

        if(!IsExitNums) {
            Cookie var17 = new Cookie(ServiceConstants.COOKIE_NAME_FOR_INDEX_LABEL_TIP + "_" + this.getUserId(), "1");
            var17.setMaxAge(31536000);
            this.getResponse().addCookie(var17);
        }

        CILogServiceUtil.getLogServiceInstance().log("COC_HOME_LABELS_MAP_SELECT", this.labelId, "", "查询标签全景视图,查询条件：【父标签ID:" + (this.labelId == null?"":this.labelId) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        HttpServletResponse var18 = this.getResponse();

        try {
            this.sendJson(var18, JsonUtil.toJson(Integer.valueOf(totalPage)));
        } catch (Exception var14) {
            this.log.error("发送json串异常", var14);
            throw new CIServiceException(var14);
        }
    }

    /**
     * 查询top100标签
     * @return
     */
    public String findAllTop100Map(){
        return "findAllTop100Map";
    }

    public String findAllLabelMap() throws Exception {
        List thirdLevelTreeList = null;
        String secLabelSize = Configure.getInstance().getProperty("SEC_LABELSIZE");
        int pageSize = "".equals(secLabelSize)?5:Integer.valueOf(secLabelSize).intValue();
        String totalPageStr = this.getRequest().getParameter("totalPage");
        int totalPage;
        if(StringUtil.isEmpty(totalPageStr)) {
            List currentPage = this.ciLabelInfoService.queryThirdLabelList(this.labelId, this.labelType);
            ArrayList showSimpleLabel = new ArrayList();
            Iterator totalSize = currentPage.iterator();
            while(totalSize.hasNext()) {
                CiLabelInfo ciLabelInfo = (CiLabelInfo)totalSize.next();
                if(this.ciLabelInfoService.queryChildrenById(ciLabelInfo.getLabelId()).size() > 0) {
                    showSimpleLabel.add(ciLabelInfo);
                }
            }
            int totalSize1 = showSimpleLabel.size();
            totalPage = (int)Math.ceil((double)totalSize1 / (double)pageSize);
        } else {
            totalPage = Integer.valueOf(totalPageStr).intValue();
        }

        int currentPage1 = 1;
        if(this.getRequest().getParameter("currentPage") != null) {
            currentPage1 = Integer.valueOf(this.getRequest().getParameter("currentPage")).intValue();
        }

        if(currentPage1 <= totalPage) {
            thirdLevelTreeList = this.ciLabelInfoService.queryMapPageObj(currentPage1, pageSize, this.labelId, this.labelType);

            this.getRequest().setAttribute("secondLTreeList", thirdLevelTreeList);
            boolean showSimpleLabel1 = Boolean.valueOf(Configure.getInstance().getProperty("SHOW_SIMPLE_LABEL").toLowerCase().trim()).booleanValue();
            return showSimpleLabel1?"loadSimpleMapList":"loadMapList";
        } else {
            return null;
        }
    }

    private List<CiLabelInfoTree> hiddenLabel(List<CiLabelInfoTree> list){
        List<CiLabelInfoTree> rList=new ArrayList<CiLabelInfoTree>();
        rList.addAll(list);
        Map<String,String> hidden=CacheBase.getInstance().getHiddenLabelMap();
        if (hidden==null){
//            CacheBase.getInstance().initHiddenLabelMap();
            hidden=CacheBase.getInstance().getHiddenLabelMap();
        }
        List removeList=new ArrayList();
        for (int i=0;i<rList.size();i++){
            CiLabelInfoTree tree=rList.get(i);
            this.log.info("标签："+tree.getDepth()+"--"+tree.getCiLabelInfo().getLabelId()+"--"+tree.getCiLabelInfo().getLabelName()+"--"+tree.getCiLabelInfoTree().size());
            if (hidden.get(String.valueOf(tree.getCiLabelInfo().getLabelId()))!=null){
                this.log.info("--->隐藏标签："+tree.getCiLabelInfo().getLabelId()+"--"+tree.getCiLabelInfo().getLabelName());
                removeList.add(i);
            }else {
                if (tree.getCiLabelInfoTree()!=null&&tree.getCiLabelInfoTree().size()!=0){
                    rList.get(i).setCiLabelInfoTree(hiddenLabel(tree.getCiLabelInfoTree()));
                }
            }
        }
        for (int j=0;j<removeList.size();j++){
            int index=(Integer) removeList.get(j);
            rList.remove(index);
        }
        return rList;
    }

    public void indexQueryCustomGroupName() {
        try {
            if(StringUtil.isNotEmpty(this.keyWord)) {
                String e = this.getUserId();
                HashMap result = new HashMap();
                CiCustomGroupInfo customGroupInfo = new CiCustomGroupInfo();
                customGroupInfo.setCustomGroupName(this.keyWord);
                String createCityId = PrivilegeServiceUtil.getCityIdFromSession();
                String root = Configure.getInstance().getProperty("CENTER_CITYID");
                if(!PrivilegeServiceUtil.isAdminUser(e)) {
                    customGroupInfo.setCreateUserId(e);
                    customGroupInfo.setCreateCityId(createCityId);
                } else if(!createCityId.equals(root)) {
                    customGroupInfo.setCreateCityId(createCityId);
                }

                List customersList = this.customersManagerService.indexQueryCustomersName(customGroupInfo, this.classId);
                result.put("totalResultsCount", Integer.valueOf(customersList.size()));
                result.put("geonames", customersList);
                HttpServletResponse response = this.getResponse();
                this.sendJson(response, this.callback + "(" + JsonUtil.toJson(result) + ")");
            }
        } catch (Exception e) {
            this.log.error("首页客户群查询框动态搜索关联报错", e);
        }

    }

    public void indexQueryCiMarketTacticsName() {
        try {
            if(StringUtil.isNotEmpty(this.keyWord)) {
                String e = this.getUserId();
                HashMap result = new HashMap();
                List marketTacticsList = this.marketTacticsService.queryCiMarketTacticsName(this.keyWord, (String)null, e);
                result.put("totalResultsCount", Integer.valueOf(marketTacticsList.size()));
                result.put("geonames", marketTacticsList);
                HttpServletResponse response = this.getResponse();
                this.sendJson(response, this.callback + "(" + JsonUtil.toJson(result) + ")");
            }
        } catch (Exception var5) {
            this.log.error("首页爱营销动态查询名称报错", var5);
        }

    }

    public String customGroupIndex() {
        CacheBase cache = CacheBase.getInstance();
        this.createTypeList = (List<DimCustomCreateType>)cache.getObjectList("DIM_CUSTOM_CREATE_TYPE");
        return "customGroupIndex";
    }

    public String peopleBaseIndex() {
        boolean operRight = false;

        try {
            operRight = PrivilegeServiceUtil.getUserPrivilegeService().haveOperRight(this.getUserId(), "999071600", "CI-TC-SEARCHLIFE");
        } catch (Throwable var3) {
            this.log.error("PrivilegeServiceUtil.getUserPrivilegeService().haveOperRight error", var3);
        }

        this.getRequest().setAttribute("operRight", Boolean.valueOf(operRight));
        return "peopleBasesIndex";
    }

    public String queryPeopleBase() {
        String pageSign = "queryPeopleBase";
        String province = Configure.getInstance().getProperty("PROVINCE");

        try {
            if("beijing".equals(province)) {
                this.peopleBaseList = this.iCiLabelsAndIndexesService.getCrmTypeModelByMobile(this.keyWord);
            } else {
                this.peopleBaseList = this.iCiLabelsAndIndexesService.getCrmTypeModelByMobile4OtherProvince(this.keyWord);
                pageSign = "queryPeopleBase4OtherProvince";
            }

            boolean e = false;

            try {
                e = PrivilegeServiceUtil.getUserPrivilegeService().haveOperRight(this.getUserId(), "999071600", "CI-TC-SEARCHLIFE");
            } catch (Throwable var5) {
                this.log.error("PrivilegeServiceUtil.getUserPrivilegeService().haveOperRight error", var5);
            }

            this.getRequest().setAttribute("operRight", Boolean.valueOf(e));
            return pageSign;
        } catch (Exception var6) {
            var6.printStackTrace();
            this.peopleBaseList = null;
            this.log.error(var6.getMessage());
            throw new CIServiceException(var6);
        }
    }

    public String queryPeopleCampaign() {
        try {
            this.top10 = this.iCiCampaignsService.getCampaignsList(this.keyWord);
        } catch (Exception var3) {
            String message = "查询个人营销活动TOP10错误";
            this.log.error(message, var3);
        }

        return "queryPeopleCampaign";
    }

    public String queryPeopleLife() {
        try {
            this.peopleLifeList = this.customersManagerService.queryAllLabelsOfUser(this.keyWord);
        } catch (Exception var3) {
            String message = "查询个人生活标签错误";
            this.log.error(message, var3);
        }

        return "queryPeopleLife";
    }

    public void checkPeoplePhone() {
        HashMap returnMap = new HashMap();
        String message = null;
        String province = Configure.getInstance().getProperty("PROVINCE");

        try {
            boolean response = false;
            if("beijing".equals(province)) {
                BSONObject e = this.iCiLabelsAndIndexesService.getLabelsAndIndexesByMobile(this.keyWord);
                if(e != null) {
                    response = true;
                }

                returnMap.put("isBeijing", "beijing");
            } else {
                Integer e1 = this.iCiLabelsAndIndexesService.queryPhoneNum(this.keyWord);
                if(e1.intValue() > 0) {
                    response = true;
                }

                returnMap.put("isBeijing", "other");
            }

            if(!response) {
                message = "查询的手机号：" + this.keyWord + " 系统中不存在，请检查！";
                this.log.error(message);
                returnMap.put("msg", message);
                returnMap.put("success", Boolean.valueOf(false));
            } else {
                returnMap.put("success", Boolean.valueOf(true));
            }
        } catch (Exception var7) {
            message = "查询的手机号：" + this.keyWord + " 系统中不存在，请检查！";
            this.log.error(message, var7);
            returnMap.put("msg", message);
            returnMap.put("success", Boolean.valueOf(false));
        }

        returnMap.put("isBeijing", province);
        CILogServiceUtil.getLogServiceInstance().log("COC_HOME_PEOPLE_SELECT", "", this.keyWord, "首页人为本查询，查询条件：【手机号：" + (StringUtil.isEmpty(this.keyWord)?"无":this.keyWord) + "】", OperResultEnum.Success, LogLevelEnum.Risk);
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMap));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public String marketingStrategyIndex() {
        return "marketingStrategyIndex";
    }

    public String labelIndex() {
        Cookie cookieVersion = new Cookie(ServiceConstants.COOKIE_NAME_FOR_VERSION + "_" + this.getUserId(), ServiceConstants.CHANGE_VERSION_NO);
        cookieVersion.setMaxAge(31536000);
        cookieVersion.setPath("/");
        this.getResponse().addCookie(cookieVersion);
        this.getRequest().setAttribute("source", this.source);
        String sceneId = this.getRequest().getParameter("sceneId");
        String sceneName = this.getRequest().getParameter("sceneName");
        this.getRequest().setAttribute("sceneId", sceneId);
        this.getRequest().setAttribute("sceneName", sceneName);
        CacheBase cache = CacheBase.getInstance();
        this.labelClass = new ArrayList();

        try {
            boolean e = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
            boolean hasAuthority = false;
            this.log.debug("needAuthority" + e);
            if (e) {
                this.userId = PrivilegeServiceUtil.getUserId();
                hasAuthority = e && !PrivilegeServiceUtil.isAdminUser(this.userId);
            }

            this.log.debug("hasAuthority" + e);
            this.log.debug("needAuthority" + e);
            CopyOnWriteArrayList labelIds = cache.getKeyList("ALL_EFFECTIVE_LABEL_MAP");
            Iterator myCookies = labelIds.iterator();

            while (myCookies.hasNext()) {
                String IsExitNums = (String) myCookies.next();
                CiLabelInfo province = null;
                if (hasAuthority) {
                    province = cache.getEffectiveLabelByUser(IsExitNums, this.userId);
                } else {
                    province = cache.getEffectiveLabel(IsExitNums);
                }

                CiLabelExtInfo isDefaultLoad = province.getCiLabelExtInfo();
                if (isDefaultLoad != null && isDefaultLoad.getLabelLevel().intValue() == 1) {
                    this.labelClass.add(province);
                }
            }

            Cookie[] var16 = this.getRequest().getCookies();
            boolean var17 = false;

            for (int var18 = 0; var18 < var16.length; ++var18) {
                Cookie var19 = var16[var18];
                String numsStr = ServiceConstants.COOKIE_NAME_FOR_INDEX_PAGE + "_" + this.getUserId();
                if (numsStr.equals(var19.getName())) {
                    var17 = true;
                    String nums_value = var19.getValue();
                    int nums_value_int = Integer.parseInt(nums_value);
                    ++nums_value_int;
                    var19.setValue(String.valueOf(nums_value_int));
                    var19.setMaxAge(31536000);
                    this.getResponse().addCookie(var19);
                }
            }

            if (!var17) {
                Cookie var21 = new Cookie(ServiceConstants.COOKIE_NAME_FOR_INDEX_PAGE + "_" + this.getUserId(), "1");
                var21.setMaxAge(31536000);
                this.getResponse().addCookie(var21);
            }

            String var23 = Configure.getInstance().getProperty("PROVINCE");
            HttpSession var20 = PrivilegeServiceUtil.getSession();
            var20.setAttribute("SYS_FLAG", this.sysFlag);


        String var22 = this.getRequest().getParameter("isDefaultLoad") == null ? "" : this.getRequest().getParameter("isDefaultLoad");
        this.getRequest().setAttribute("isDefaultLoad", var22);
        }catch(Exception var15){
            this.log.error("首页初始化分类错误", var15);
            var15.printStackTrace();
        }

        this.labelPage = true;
        return "labelIndex";
    }

    public String productIndex() {
        CacheBase cache = CacheBase.getInstance();
        this.firstLevelProductCatogery = new ArrayList();
        CopyOnWriteArrayList categoryIds = cache.getKeyList("ALL_PRODUCT_CATEGORY_MAP");
        Iterator i$ = categoryIds.iterator();

        while(i$.hasNext()) {
            String idStr = (String)i$.next();
            CiProductCategory ciProductCategory = cache.getProductCategory(idStr);
            if(ciProductCategory != null && ciProductCategory.getParentId().intValue() == -1) {
                this.firstLevelProductCatogery.add(ciProductCategory);
            }
        }

        this.productPage = true;
        return "productIndex";
    }

    public String labelListIndex() {
        CacheBase cache = CacheBase.getInstance();
        this.labelClass = new ArrayList();

        try {
            boolean e = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
            boolean hasAuthority = false;
            if(e) {
                this.userId = PrivilegeServiceUtil.getUserId();
                hasAuthority = e && !PrivilegeServiceUtil.isAdminUser(this.userId);
            }

            CopyOnWriteArrayList labelIds = cache.getKeyList("ALL_EFFECTIVE_LABEL_MAP");
            Iterator i$ = labelIds.iterator();

            while(i$.hasNext()) {
                String idStr = (String)i$.next();
                CiLabelInfo ciLabelInfo = null;
                if(hasAuthority) {
                    ciLabelInfo = cache.getEffectiveLabelByUser(idStr, this.userId);
                } else {
                    ciLabelInfo = cache.getEffectiveLabel(idStr);
                }

                CiLabelExtInfo ext = ciLabelInfo.getCiLabelExtInfo();
                if(ext != null && ext.getLabelLevel().intValue() == 1) {
                    this.labelClass.add(ciLabelInfo);
                }
            }

            CILogServiceUtil.getLogServiceInstance().log("COC_HOME_LABELS_MAP_SELECT", "", "", "首页标签控标签地图查询", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var9) {
            this.log.error("首页标签列表查询错误", var9);
            CILogServiceUtil.getLogServiceInstance().log("COC_HOME_LABELS_MAP_SELECT", "-1", "", "首页标签控标签地图查询错误", OperResultEnum.Failure, LogLevelEnum.Medium);
            var9.printStackTrace();
        }

        this.labelPage = true;
        return "labelListIndex";
    }

    public String productListIndex() {
        CacheBase cache = CacheBase.getInstance();
        this.firstLevelProductCatogery = new ArrayList();
        CopyOnWriteArrayList categoryIds = cache.getKeyList("ALL_PRODUCT_CATEGORY_MAP");
        Iterator i$ = categoryIds.iterator();

        while(i$.hasNext()) {
            String idStr = (String)i$.next();
            CiProductCategory ciProductCategory = cache.getProductCategory(idStr);
            if(ciProductCategory != null && ciProductCategory.getParentId().intValue() == -1) {
                this.firstLevelProductCatogery.add(ciProductCategory);
            }
        }

        this.productPage = true;
        return "productListIndex";
    }

    public void indexQueryLabelName() {
        try {
            if(StringUtil.isNotEmpty(this.keyWord)) {
                HashMap e = new HashMap();
                String searchByDBFlag = Configure.getInstance().getProperty("SEARCH_BY_DB_FLAG");
                int totalResultsCount;
                List response;
                if(StringUtil.isNotEmpty(searchByDBFlag) && "false".equalsIgnoreCase(searchByDBFlag)) {
                    if(this.pager == null) {
                        this.pager = new Pager();
                    }

                    this.pager.setPageSize(9);
                    this.pager.setPageNum(1);
                    response = this.searchService.searchLabelForPage(this.pager, this.keyWord, "", new CiLabelInfo());
                    totalResultsCount = response.size();
                    e.put("geonames", response);
                } else {
                    response = this.ciLabelInfoService.queryLabelName(this.keyWord, this.classId);
                    totalResultsCount = response.size();
                    e.put("geonames", response);
                }

                e.put("totalResultsCount", Integer.valueOf(totalResultsCount));
                HttpServletResponse response1 = this.getResponse();
                this.sendJson(response1, this.callback + "(" + JsonUtil.toJson(e) + ")");
            }
        } catch (Exception var5) {
            this.log.error("首页关联查询标签名称报错", var5);
        }

    }

    public void indexQueryProductName() {
        try {
            if(StringUtil.isNotEmpty(this.keyWord)) {
                HashMap e = new HashMap();
                List ciProductInfoList = this.ciProductService.queryProductName(this.keyWord, this.classId);
                e.put("totalResultsCount", Integer.valueOf(ciProductInfoList.size()));
                e.put("geonames", ciProductInfoList);
                HttpServletResponse response = this.getResponse();
                this.sendJson(response, this.callback + "(" + JsonUtil.toJson(e) + ")");
            }
        } catch (Exception var4) {
            this.log.error("首页关联查询产品名称报错", var4);
        }

    }

    public void getProductFirstCategoryList() throws Exception {
        List productCategoryList = this.ciProductService.queryProductFirstCategoryList();
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(productCategoryList));
        } catch (Exception var4) {
            this.log.error("发送json串异常", var4);
            throw new CIServiceException(var4);
        }
    }

    public void getProductSecondCategory() throws Exception {
        List categoryList = this.ciProductService.queryProductSecondCategory(Integer.valueOf(this.categoryId));
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(categoryList));
        } catch (Exception var4) {
            this.log.error("发送json串异常", var4);
            throw new CIServiceException(var4);
        }
    }

    public void queryProductMapTotalPage() throws Exception {
        List categoryList = this.ciProductService.queryProductSecondCategory(Integer.valueOf(this.categoryId));
        int categorysCount = categoryList.size();
        byte pageSize = 3;
        int totalPage = (int)Math.ceil((double)categorysCount / (double)pageSize);
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(Integer.valueOf(totalPage)));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException(var7);
        }
    }

    public String getAllEffictiveProductByFirstL() throws Exception {
        List secondCategoryAndProductsList = null;
        byte pageSize = 3;
        String totalPageStr = this.getRequest().getParameter("totalPage");
        int totalPage;
        int currentPage;
        if(StringUtil.isEmpty(totalPageStr)) {
            currentPage = this.ciProductService.queryProductSecondCategoryCount(Integer.valueOf(this.categoryId));
            totalPage = (int)Math.ceil((double)currentPage / (double)pageSize);
        } else {
            totalPage = Integer.valueOf(totalPageStr).intValue();
        }

        currentPage = 1;
        if(this.getRequest().getParameter("currentPage") != null) {
            currentPage = Integer.valueOf(this.getRequest().getParameter("currentPage")).intValue();
        }

        if(currentPage <= totalPage) {
            secondCategoryAndProductsList = this.ciProductService.queryProductPage(currentPage, pageSize, Integer.valueOf(this.categoryId).intValue());
            this.getRequest().setAttribute("secondCateAndProdList", secondCategoryAndProductsList);
            CILogServiceUtil.getLogServiceInstance().log("COC_HOME_PRODUCTS_MAP_SELECT", this.categoryId, "", "查询一级产品下的产品地图成功,查询条件：【一级产品ID:" + (this.categoryId == null?"":this.categoryId) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            return "loadProductPage";
        } else {
            return null;
        }
    }

    public String getSecondCategoryProductTree() throws Exception {
        try {
            ArrayList e = new ArrayList();
            e.add(this.ciProductService.querySecondCategoryProductPage(Integer.valueOf(this.categoryId).intValue()));
            this.getRequest().setAttribute("secondCateAndProdList", e);
            CILogServiceUtil.getLogServiceInstance().log("COC_HOME_PRODUCT_MAP_SELECT", this.categoryId, "", "查询二级产品下的产品地图成功,查询条件：【二级产品ID:" + (this.categoryId == null?"":this.categoryId) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            return "loadProductPage";
        } catch (Exception var2) {
            this.log.error("CiIndexAction.getSecondCategoryProductTree", var2);
            CILogServiceUtil.getLogServiceInstance().log("COC_HOME_PRODUCT_MAP_SELECT", "-1", "", "查询二级产品下的产品地图成功,查询条件：【二级产品ID:" + (this.categoryId == null?"":this.categoryId) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            throw new CIServiceException("CiIndexAction.getSecondCategoryProductTree异常");
        }
    }

    public String findEffectiveProductNum() {
        String dataScope = this.getRequest().getParameter("dataScope");
        String topLabelIdStr = this.getRequest().getParameter("topLabelId");
        Integer topLabelId = null;
        if(StringUtil.isNotEmpty(topLabelIdStr)) {
            topLabelId = Integer.valueOf(topLabelIdStr.trim());
        }

        if(StringUtils.isEmpty(dataScope)) {
            dataScope = "all";
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        boolean success = false;
        HashMap result = new HashMap();
        String msg = "";
        int totalPage = 0;

        try {
            byte response = 5;
            long e = this.ciProductService.queryEffectiveCiProductInfoNum(this.productName, topLabelId, dataScope);
            totalPage = (int)Math.ceil((double)e / (double)response);
            this.pager.setTotalPage(totalPage);
            this.pager.setPageSize(response);
            success = true;
        } catch (Exception var12) {
            msg = "首页标签记录查询发生异常";
            this.log.error(msg, var12);
            success = false;
        }

        result.put("success", Boolean.valueOf(success));
        if(success) {
            result.put("totalPage", Integer.valueOf(totalPage));
        } else {
            result.put("msg", msg);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var11) {
            this.log.error("发送json串异常", var11);
            throw new CIServiceException(var11);
        }

        CILogServiceUtil.getLogServiceInstance().log("COC_HOME_PRODUCT_SELECT", "", this.productName, "首页产品查询成功,查询条件：【产品分类ID:" + (topLabelId == null?"无":topLabelId) + "，产品名称:" + (StringUtils.isBlank(this.productName)?"无":this.productName) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return null;
    }

    public String findEffectiveProduct() {
        String dataScope = this.getRequest().getParameter("dataScope");
        String topLabelIdStr = this.getRequest().getParameter("topLabelId");
        Integer topLabelId = null;
        if(StringUtil.isNotEmpty(topLabelIdStr)) {
            topLabelId = Integer.valueOf(topLabelIdStr.trim());
        }

        if(StringUtils.isEmpty(dataScope)) {
            dataScope = "all";
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        try {
            String e = "time";
            int msg1 = this.pager.getTotalPage();
            int currentPage = 1;
            if(this.getRequest().getParameter("currentPage") != null) {
                currentPage = Integer.valueOf(this.getRequest().getParameter("currentPage")).intValue();
            }

            if(currentPage <= msg1) {
                byte pageSize = 5;
                this.productDetailList = this.ciProductService.queryEffectiveCiProductInfo(currentPage, pageSize, e, this.productName, topLabelId, dataScope);
                this.pager.setPageSize(pageSize);
                this.pager.setTotalPage(msg1);
            }

            return "queryMoreProduct";
        } catch (Exception var8) {
            String msg = "首页标签列表查询发生异常";
            this.log.error(msg, var8);
            throw new CIServiceException(var8);
        }
    }

    public void getProductDetail() {
        String productId = this.getRequest().getParameter("productId");
        CiProductInfo productInfo = CacheBase.getInstance().getEffectiveProduct(productId);
        CILogServiceUtil.getLogServiceInstance().log("COC_PRODUCT_DETAIL_VIEW", productId, productInfo.getProductName(), "首页觅产品产品详情查看成功,查询条件：【产品ID：" + productId + "，产品名称:" + productInfo.getProductName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
    }

    public void getFirstLevelLabelInfoList() throws Exception {
        HttpServletResponse response = this.getResponse();

        List labelList;
        try {
            labelList = this.ciLabelInfoService.queryChildrenById(Integer.valueOf(-1));
        } catch (Exception var5) {
            this.log.error("CiIndexAction.getFirstLevelLabelInfoList() method 发生异常", var5);
            throw new CIServiceException(var5);
        }

        try {
            this.sendJson(response, JsonUtil.toJson(labelList));
        } catch (Exception var4) {
            this.log.error("发送json串异常", var4);
            throw new CIServiceException(var4);
        }
    }

    public void getChildrenLabelById() throws Exception {
        HttpServletResponse response = this.getResponse();

        List labelList;
        try {
            labelList = this.ciLabelInfoService.queryChildrenById(Integer.valueOf(this.labelId));
        } catch (Exception var5) {
            this.log.error("CiIndexAction.getChildrenLabelById() method 发生异常", var5);
            throw new CIServiceException(var5);
        }

        try {
            this.sendJson(response, JsonUtil.toJson(labelList));
        } catch (Exception var4) {
            this.log.error("发送json串异常", var4);
            throw new CIServiceException(var4);
        }
    }

    public String getSecondLLabelTree() throws Exception {
        ArrayList secondLTreeList = new ArrayList();
        CiLabelInfo ciLabelInfo = null;
        CacheBase cache = CacheBase.getInstance();
        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        if(needAuthority) {
            this.userId = PrivilegeServiceUtil.getUserId();
        }

        if(needAuthority && !PrivilegeServiceUtil.isAdminUser(this.userId)) {
            ciLabelInfo = cache.getEffectiveLabelByUser(this.labelId, this.userId);
        } else {
            ciLabelInfo = cache.getEffectiveLabel(this.labelId);
        }

        CopyOnWriteArrayList allEffectiveLabel = cache.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
        CiLabelInfoTree ciLabelInfoTree = this.ciLabelInfoService.getLabelTree(ciLabelInfo, allEffectiveLabel);
        secondLTreeList.add(ciLabelInfoTree);
        this.getRequest().setAttribute("secondLTreeList", secondLTreeList);
        CILogServiceUtil.getLogServiceInstance().log("COC_HOME_LABEL_MAP_SELECT", this.labelId, ciLabelInfo.getLabelName(), "查询二级标签下的标签地图成功,查询条件：【二级标签ID:" + (this.labelId == null?"":this.labelId) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "loadMapList";
    }

    public void queryLabelMapTotalPage() throws Exception {
        List labelList = this.ciLabelInfoService.queryChildrenById(Integer.valueOf(this.labelId));
        int secondLLabelCount = labelList.size();
        String secLabelSize = Configure.getInstance().getProperty("SEC_LABELSIZE");
        int pageSize = "".equals(secLabelSize)?5:Integer.valueOf(secLabelSize).intValue();
        int totalPage = (int)Math.ceil((double)secondLLabelCount / (double)pageSize);
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(Integer.valueOf(totalPage)));
        } catch (Exception var8) {
            this.log.error("发送json串异常", var8);
            throw new CIServiceException(var8);
        }
    }

    public String getAllEffictiveLabelByFirstL() throws Exception {
        List secondLTreeList = null;
        String secLabelSize = Configure.getInstance().getProperty("SEC_LABELSIZE");
        int pageSize = "".equals(secLabelSize)?3:Integer.valueOf(secLabelSize).intValue();
        String totalPageStr = this.getRequest().getParameter("totalPage");
        int totalPage;
        int currentPage;
        if(StringUtil.isEmpty(totalPageStr)) {
            currentPage = this.ciLabelInfoService.queryChildrenById(Integer.valueOf(this.labelId)).size();
            totalPage = (int)Math.ceil((double)currentPage / (double)pageSize);
        } else {
            totalPage = Integer.valueOf(totalPageStr).intValue();
        }

        currentPage = 1;
        if(this.getRequest().getParameter("currentPage") != null) {
            currentPage = Integer.valueOf(this.getRequest().getParameter("currentPage")).intValue();
        }

        if(currentPage <= totalPage) {
            secondLTreeList = this.ciLabelInfoService.queryMapPageObject(currentPage, pageSize, Integer.valueOf(this.labelId).intValue());
            this.getRequest().setAttribute("secondLTreeList", secondLTreeList);
            this.log.info("Exit CiIndexAction.getAllEffictiveLabel() method");
            CILogServiceUtil.getLogServiceInstance().log("COC_HOME_LABELS_MAP_SELECT", this.labelId, "", "查询一级标签下的标签地图成功,查询条件：【一级标签ID:" + (this.labelId == null?"":this.labelId) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            boolean showSimpleLabel = Boolean.valueOf(Configure.getInstance().getProperty("SHOW_SIMPLE_LABEL").toLowerCase().trim()).booleanValue();
            return showSimpleLabel?"loadSimpleMapList":"loadMapList";
        } else {
            return null;
        }
    }

    public void getLabelInfoCustomNum() throws Exception {
        HashMap result = new HashMap();
        boolean success = true;
        Long retCustomNum = Long.valueOf(0L);

        try {
            String response = PrivilegeServiceUtil.getUserId();
            boolean e = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
            this.log.info("e" + " " + e);
            if(e && !PrivilegeServiceUtil.isAdminUser(response)) {
                retCustomNum = this.ciLabelInfoService.getCustomNum(this.dataDate, Integer.valueOf(this.labelId), Integer.valueOf(-1), Integer.valueOf(-1), this.customNum, response);
                this.log.info("retCustomNum" + " " + retCustomNum);
            } else {
                if(StringUtil.isEmpty(this.customNum)) {
                    this.customNum = Long.valueOf(0L);
                    this.log.info("customNum" + " " + customNum);
                }
                retCustomNum = this.customNum;
                this.log.info("retCustomNum" + " " + retCustomNum);
            }

            CacheBase cache = CacheBase.getInstance();
            CiLabelInfo ciLabelInfo = this.ciLabelInfoService.queryCiLabelInfoById(Integer.valueOf(this.labelId));
            if(ciLabelInfo.getUpdateCycle().intValue() == 1) {
                ciLabelInfo.setNewDataDate(DateUtil.string2StringFormat(cache.getNewLabelDay(), "yyyyMMdd", "yyyy-MM-dd"));
            } else {
                ciLabelInfo.setNewDataDate(DateUtil.string2StringFormat(cache.getNewLabelMonth(), "yyyyMM", "yyyy-MM"));
            }

            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_DETAIL_VIEW_INDEX", this.labelId, ciLabelInfo.getLabelName(), "查询首页标签地图showTip详细信息成功,查询条件：【标签ID:" + (this.labelId == null?"":this.labelId) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            result.put("retCustomNum", retCustomNum);
            result.put("busiCaliber", ciLabelInfo.getBusiCaliber());
            result.put("applySuggest", ciLabelInfo.getApplySuggest());
            result.put("newDataDate", ciLabelInfo.getNewDataDate());
            result.put("isAttention", ciLabelInfo.getIsAttention());
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var9) {
            this.log.error("获取首页标签地图showTip展示详细信息时出错", var9);
            success = false;
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var8) {
            this.log.error("发送json串异常", var8);
            throw new CIServiceException(var8);
        }
    }

    public void getSingleLabelCustomNum() throws Exception {
        HashMap result = new HashMap();
        boolean success = true;
        Long retCustomNum = Long.valueOf(0L);
        String LOG_DATA_EXPLORE = "";
        if(this.exploreFromModuleFlag != null && 1 == this.exploreFromModuleFlag.intValue()) {
            LOG_DATA_EXPLORE = "COC_INDEX_LABEL_DATA_EXPLORE";
        } else {
            LOG_DATA_EXPLORE = "COC_LABEL_ANALYSIS_DATA_EXPLORE";
        }

        try {
            String response = PrivilegeServiceUtil.getUserId();
            CiLabelInfo e = CacheBase.getInstance().getEffectiveLabel(this.labelId);
            if(e.getUpdateCycle().intValue() == 1) {
                this.dataDate = CacheBase.getInstance().getNewLabelDay();
            }

            retCustomNum = this.ciLabelInfoService.getCustomNum(this.dataDate, Integer.valueOf(this.labelId), Integer.valueOf(-1), Integer.valueOf(-1), (Long)null, response);
            result.put("retCustomNum", retCustomNum);
            result.put("success", Boolean.valueOf(success));
            CILogServiceUtil.getLogServiceInstance().log(LOG_DATA_EXPLORE, this.labelId, e.getLabelName(), "数据探索成功【标签ID:" + (this.labelId == null?"无":this.labelId) + "，用户数为：" + retCustomNum + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var8) {
            this.log.error("数据探索单个标签查询用户数错误", var8);
            success = false;
            result.put("success", Boolean.valueOf(success));
            CILogServiceUtil.getLogServiceInstance().log(LOG_DATA_EXPLORE, "-1", "", "数据探索失败，查询标签统计表错误【标签ID:" + (this.labelId == null?"无":this.labelId) + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException(var7);
        }
    }

    public String findEffectiveLabelNum() {
        String dataScope = this.getRequest().getParameter("dataScope");
        String topLabelIdStr = this.getRequest().getParameter("topLabelId");
        Integer topLabelId = null;
        if(StringUtil.isNotEmpty(topLabelIdStr)) {
            topLabelId = Integer.valueOf(topLabelIdStr.trim());
        }

        if(StringUtils.isEmpty(dataScope)) {
            dataScope = "all";
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        if(this.searchBean == null) {
            this.searchBean = new CiLabelInfo();
        }

        boolean success = false;
        HashMap result = new HashMap();
        String msg = "";
        int totalPage = 0;
        long resultNum = 0L;

        try {
            byte response = 5;
            long e = 0L;
            String searchByDBFlag = Configure.getInstance().getProperty("SEARCH_BY_DB_FLAG");
            if(StringUtil.isNotEmpty(searchByDBFlag) && "false".equalsIgnoreCase(searchByDBFlag)) {
                e = this.searchService.searchLabelCount(this.labelKeyWord.trim(), dataScope, this.searchBean);
            } else {
                e = this.ciLabelInfoService.queryEffectiveLabelNum(this.labelKeyWord.trim(), topLabelId, dataScope, this.searchBean);
            }

            totalPage = (int)Math.ceil((double)e / (double)response);
            resultNum = e;
            this.pager.setTotalPage(totalPage);
            this.pager.setPageSize(response);
            success = true;
        } catch (Exception var15) {
            msg = "首页标签记录查询发生异常";
            this.log.error(msg, var15);
            success = false;
        }

        result.put("success", Boolean.valueOf(success));
        if(success) {
            result.put("totalPage", Integer.valueOf(totalPage));
            result.put("totalSize", Long.valueOf(resultNum));
        } else {
            result.put("msg", msg);
        }

        HttpServletResponse response1 = this.getResponse();
        CILogServiceUtil.getLogServiceInstance().log("COC_HOME_LABEL_LIST_SELECT", "", this.labelKeyWord, "首页标签控查询，查询参数【标签类型ID：" + (topLabelId == null?"无":topLabelId) + "，查询标签关键字名称：" + (StringUtils.isBlank(this.labelKeyWord)?"无":this.labelKeyWord) + "】", OperResultEnum.Success, LogLevelEnum.Medium);

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
            return null;
        } catch (Exception var14) {
            this.log.error("发送json串异常", var14);
            throw new CIServiceException(var14);
        }
    }

    public String findEffectiveLabel() {
        String dataScope = this.getRequest().getParameter("dataScope");
        String topLabelIdStr = this.getRequest().getParameter("topLabelId");
        Integer topLabelId = null;
        if(StringUtil.isNotEmpty(topLabelIdStr)) {
            topLabelId = Integer.valueOf(topLabelIdStr.trim());
        }

        if(StringUtils.isEmpty(dataScope)) {
            dataScope = "all";
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        if(this.searchBean == null) {
            this.searchBean = new CiLabelInfo();
        }

        try {
            String e = "time";
            int msg1 = this.pager.getTotalPage();
            int currentPage = 1;
            if(this.getRequest().getParameter("currentPage") != null) {
                currentPage = Integer.valueOf(this.getRequest().getParameter("currentPage")).intValue();
            }

            this.pager.setPageNum(currentPage);
            if(currentPage <= msg1) {
                byte pageSize = 5;
                if(StringUtil.isNotEmpty(this.labelKeyWord)) {
                    this.labelKeyWord = this.labelKeyWord.trim();
                }

                this.pager.setPageSize(pageSize);
                this.pager.setTotalPage(msg1);
                String searchByDBFlag = Configure.getInstance().getProperty("SEARCH_BY_DB_FLAG");
                if(StringUtil.isNotEmpty(searchByDBFlag) && "false".equalsIgnoreCase(searchByDBFlag)) {
                    this.labelDetailList = this.searchService.searchLabelForPage(this.pager, this.labelKeyWord, dataScope, this.searchBean);
                } else {
                    this.labelDetailList = this.ciLabelInfoService.queryEffectiveLabel(this.pager, e, this.labelKeyWord, topLabelId, dataScope, this.searchBean);
                }
            }
            return "querySearchMoreLabel";
        } catch (Exception var9) {
            String msg = "首页标签列表查询发生异常";
            this.log.error(msg, var9);
            throw new CIServiceException(var9);
        }
    }

    private List<LabelDetailInfo> hiddenLabelDetail(List<LabelDetailInfo> labelDetailInfos){
        this.log.info("开始过滤标签："+labelDetailInfos.size());
        List<LabelDetailInfo> rList=new ArrayList<LabelDetailInfo>();
        rList.addAll(labelDetailInfos);
        Map<String,String> hidden=CacheBase.getInstance().getHiddenLabelMap();
        List removeList=new ArrayList();
        for (int i=0;i<rList.size();i++){
            LabelDetailInfo detailInfo=rList.get(i);
            this.log.info("标签详情："+detailInfo.getLabelId()+"-"+detailInfo.getLabelName()+"-"+detailInfo.getParentId());
            if (hidden.get(String.valueOf(detailInfo.getLabelId()))!=null){
                this.log.info("过滤标签："+detailInfo.getLabelId()+"--"+detailInfo.getLabelName());
                removeList.add(i);
            }
        }
        for (int j=0;j<removeList.size();j++){
            int index=(Integer) removeList.get(j);
            rList.remove(index);
        }
        this.log.info("过滤标签结束："+rList.size());
        return rList;
    }

    public String effectiveLabelIndex() {
        CacheBase cache = CacheBase.getInstance();
        this.dimScenes = (List<DimScene>)cache.getObjectList("DIM_SCENE");
        return "querySearchMoreLabelIndex";
    }

    public void refreshCache() throws Exception {
        HashMap result = new HashMap();
        boolean success = false;

        try {
            String response = this.getUserId();
            if(StringUtil.isNotEmpty(response) && PrivilegeServiceUtil.isAdminUser(response)) {
                CacheBase.getInstance().init(false);
                success = true;
                result.put("success", Boolean.valueOf(success));
                result.put("msg", "更新缓存成功！");
            } else {
                success = false;
                result.put("success", Boolean.valueOf(success));
                result.put("msg", "非管理员用户不能更新缓存！");
            }
        } catch (Exception var6) {
            String e = "更新缓存错误！";
            this.log.error(e, var6);
            success = false;
            result.put("success", Boolean.valueOf(success));
            result.put("msg", e);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var5) {
            this.log.error("发送json串异常", var5);
            throw new CIServiceException("发送json串异常");
        }
    }

    public void rebuildLuceneIndex() throws Exception {
        HashMap result = new HashMap();
        boolean success = false;

        String e;
        try {
            String response = this.getUserId();
            if(StringUtil.isNotEmpty(response) && PrivilegeServiceUtil.isAdminUser(response)) {
                CacheBase.getInstance().init(false);
                e = Configure.getInstance().getProperty("SEARCH_BY_DB_FLAG");
                if(StringUtil.isNotEmpty(e) && "false".equalsIgnoreCase(e)) {
                    this.searchService.createAllLabelIndex();
                }

                success = true;
                result.put("success", Boolean.valueOf(success));
                result.put("msg", "重建索引成功！");
            } else {
                success = false;
                result.put("success", Boolean.valueOf(success));
                result.put("msg", "非管理员用户不能重建索引！");
            }
        } catch (Exception var6) {
            e = "重建索引错误！";
            this.log.error(e, var6);
            success = false;
            result.put("success", Boolean.valueOf(success));
            result.put("msg", e);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var5) {
            this.log.error("发送json串异常", var5);
            throw new CIServiceException("发送json串异常");
        }
    }

    public void initLabelIdPath() throws Exception {
        HashMap result = new HashMap();
        boolean success = false;

        try {
            String response = this.getUserId();
            if(StringUtil.isNotEmpty(response) && PrivilegeServiceUtil.isAdminUser(response)) {
                CacheBase e1 = CacheBase.getInstance();
                e1.initAllEffectiveLabel();
                this.ciLabelInfoService.batchUpdateLabelIdLevel();
                e1.initAllEffectiveLabel();
                success = true;
                result.put("success", Boolean.valueOf(success));
                result.put("msg", "构建标签路径成功！");
            } else {
                success = false;
                result.put("success", Boolean.valueOf(success));
                result.put("msg", "非管理员用户不能构建标签路径！");
            }
        } catch (Exception var6) {
            String e = "构建标签路径错误！";
            this.log.error(e, var6);
            success = false;
            result.put("success", Boolean.valueOf(success));
            result.put("msg", e);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var5) {
            this.log.error("发送json串异常", var5);
            throw new CIServiceException("发送json串异常");
        }
    }

    public void getNotice() throws CIServiceException {
        HashMap result = new HashMap();
        String userId = this.getUserId();
        CiNoticeModel noticeModel = CacheBase.getInstance().getNoticeModelByUser(userId);
        if(noticeModel == null) {
            noticeModel = new CiNoticeModel();
        }

        String userName = this.getUserName();
        noticeModel.setUserName(userName);
        HttpServletResponse response = this.getResponse();

        try {
            result.put("noticeModel", noticeModel);
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException(var7);
        }
    }

    public void runJob() throws Exception {
        this.log.info(this.getUserId() + " run job[" + this.jobName + "." + this.methodName + "]");
        HashMap result = new HashMap();
        boolean success = false;
        String msg = "";

        try {
            if(StringUtils.isNotEmpty(this.jobName)) {
                try {
                    AbstractJob response = (AbstractJob)SystemServiceLocator.getInstance().getService(this.jobName);
                    if(StringUtils.isNotEmpty(this.dataDate)) {
                        response.setDataTime(this.dataDate);
                    }

                    response.work();
                    success = true;
                    msg = "执行成功";
                } catch (Exception var7) {
                    this.log.error("run service 异常," + this.jobName, var7);
                    msg = var7.getMessage();
                }
            } else {
                msg = "jobName is empty";
            }
        } catch (Exception var8) {
            this.log.error("run job failed run job[" + this.jobName + "." + this.methodName + "]", var8);
            msg = var8.getMessage();
        }

        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException("发送json串异常");
        }
    }

    public void closeNotice() throws CIServiceException {
        String userId = this.getUserId();
        CacheBase.getInstance().closeNotice(userId, this.noticeId);
    }

    public void down() {
        ServletOutputStream out = null;
        FileInputStream fileInputStream = null;
        String fileName = null;
        if("1".equals(this.id)) {
            fileName = "客户运营中心培训材料.pdf";
        } else if("2".equals(this.id)) {
            fileName = "COC用户手册.pdf";
        } else if("3".equals(this.id)) {
            fileName = "北京移动客户经营中心（COC）数据字典v1.xlsx";
        } else {
            fileName = "install_flash_player_ax.zip";
        }

        String localFilePath = "";

        try {
            HttpServletResponse e = this.getResponse();
            HttpServletRequest request = this.getRequest();
            String mpmPath = Configure.getInstance().getProperty("SYS_COMMON_DOWN_PATH");
            if(!mpmPath.endsWith(File.separator)) {
                mpmPath = mpmPath + File.separator;
            }

            mpmPath = request.getSession().getServletContext().getRealPath("/") + File.separator + mpmPath;
            File pathFile = new File(mpmPath);
            if(pathFile.exists() || pathFile.mkdirs()) {
                localFilePath = mpmPath + fileName;
                String clientLanguage = request.getHeader("Accept-Language");
                String guessCharset = ResponseEncodingUtil.getGuessCharset(clientLanguage);
                String fileNameEncode = ResponseEncodingUtil.encodingFileName(fileName, guessCharset);
                e.addHeader("Content-Disposition", "attachment; filename=" + fileNameEncode);
                this.log.debug("offline download from web server================the file path is: " + localFilePath);
                e.setContentType("application/octet-stream;charset=" + guessCharset);
                out = e.getOutputStream();
                fileInputStream = new FileInputStream(localFilePath);
                boolean bytesRead = false;
                byte[] buffer = new byte[1024];

                int bytesRead1;
                while((bytesRead1 = fileInputStream.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, bytesRead1);
                }

                return;
            }
        } catch (IOException var29) {
            return;
        } finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException var28) {
                    ;
                }
            }

            if(fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException var27) {
                    ;
                }
            }

        }

    }

    public String toHelpPage() {
        CILogServiceUtil.getLogServiceInstance().log("COC_HELP_LINK", "跳转", "帮助页面", "跳转到帮助页面...", OperResultEnum.Success, LogLevelEnum.Normal);
        return "help";
    }

    public void helpDown() {
        ServletOutputStream out = null;
        FileInputStream fileInputStream = null;
        String fileName = null;
        String localFilePath = "";

        try {
            fileName = URLDecoder.decode(URLDecoder.decode(this.id, "UTF-8"), "UTF-8");
            HttpServletResponse e = this.getResponse();
            HttpServletRequest request = this.getRequest();
            String mpmPath = Configure.getInstance().getProperty("SYS_COMMON_UPLOAD_PATH");
            if(!mpmPath.endsWith(File.separator)) {
                mpmPath = mpmPath + File.separator;
            }

            mpmPath = mpmPath + "down" + File.separator;
            File pathFile = new File(mpmPath);
            if(pathFile.exists() || pathFile.mkdirs()) {
                localFilePath = mpmPath + fileName;
                String clientLanguage = request.getHeader("Accept-Language");
                String guessCharset = ResponseEncodingUtil.getGuessCharset(clientLanguage);
                e.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
                this.log.debug("offline download from web server================the file path is: " + localFilePath);
                if(fileName.contains("wmv")) {
                    e.setContentType("video/x-ms-wmv;charset=" + guessCharset);
                } else {
                    e.setContentType("application/octet-stream;charset=" + guessCharset);
                }

                out = e.getOutputStream();
                fileInputStream = new FileInputStream(localFilePath);
                boolean bytesRead = false;
                byte[] buffer = new byte[1024];

                int bytesRead1;
                while((bytesRead1 = fileInputStream.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, bytesRead1);
                }

                CILogServiceUtil.getLogServiceInstance().log("COC_HELP_FILE_DOWNLOAD", "下载", fileName, "首页帮助中心下载成功,查询条件：【文档名称:" + (fileName == null?"":fileName) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                return;
            }
        } catch (IOException var28) {
            this.log.error("help down error : ", var28);
            CILogServiceUtil.getLogServiceInstance().log("COC_HELP_FILE_DOWNLOAD", "-1", fileName, "首页帮助中心下载失败,查询条件：【文档名称:" + (fileName == null?"":fileName) + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
            return;
        } finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException var27) {
                    ;
                }
            }

            if(fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException var26) {
                    ;
                }
            }

        }

    }

    public void transferLog() {
        HashMap result = new HashMap();
        boolean success = false;
        String msg = "";
        this.log.debug(this.dataDate);
        this.log.debug("start transportLog......");
        String jndi = Configure.getInstance().getProperty("JNDI_WEBOS_CI");
        if(!StringUtil.isEmpty(jndi)) {
            if(this.logTransferService.logTableIfExists(this.dataDate)) {
                this.logTransferService.removeDirtyData(this.dataDate);
                success = true;
                msg = "日志迁移成功！";
            } else {
                success = false;
                msg = "南研数据库没有对应的日志表！";
            }
        } else {
            success = false;
            msg = "没有配置南研数据库链接，请配置CI-beijing.properties中的JNDI_WEBOS_CI属性！";
        }

        this.log.debug("end transportLog!");
        HttpServletResponse response = this.getResponse();
        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException("发送json串异常");
        }
    }

    public String customGroupMarket() {
        CacheBase cache = CacheBase.getInstance();
        this.createTypeList = (List<DimCustomCreateType> )cache.getObjectList("DIM_CUSTOM_CREATE_TYPE");
        return "customGroupMarket";
    }

    public void saveShopSession() {
        HashMap result = new HashMap();
        boolean success = false;
        String msg = "失败";
        int numValue = 0;

        try {
            HttpSession response = PrivilegeServiceUtil.getSession();
            CacheBase e = CacheBase.getInstance();
            String isEditCustomFlag = "";
            if(StringUtils.isEmpty((String)this.pojo.get("isEditCustomFlag"))) {
                isEditCustomFlag = "0";
            } else {
                isEditCustomFlag = this.pojo.get("isEditCustomFlag").toString();
            }

            CiCustomGroupInfo rules;
            String calculationsId;
            String typeId;
            if(StringUtils.isNotEmpty((String)this.pojo.get("calculationsId"))) {
                if(!ServiceConstants.CUSTOM_GROUP_INFO_CALCULATIONS_TYPEID.equals(this.pojo.get("typeId").toString()) && this.pojo.get("typeId").toString() != ServiceConstants.CUSTOM_GROUP_INFO_CALCULATIONS_TYPEID) {
                    success = true;
                } else {
                    success = true;
                    rules = this.customersManagerService.queryCiCustomGroupInfo((String)this.pojo.get("calculationsId"));
                    calculationsId = Configure.getInstance().getProperty("CENTER_CITYID");
                    String sort = rules.getCreateUserId();
                    String cityId = PrivilegeServiceUtil.getCityIdFromSession();
                    typeId = this.getUserId();
                    String rule = rules.getCreateCityId();
                    Integer ciTemplateInfo = rules.getDataStatus();
                    if(rules.getUpdateCycle().intValue() == 4 && rules.getIsLabelOffline() != null && rules.getIsLabelOffline().intValue() == 1) {
                        success = false;
                        msg = "抱歉，该客户群无规则、无清单可用，不能添加到收纳篮！";
                    } else {
                        if(rules.getIsPrivate().intValue() == 0) {
                            if(!rule.equals(calculationsId) && !rule.equals(cityId) && rules.getIsContainLocalList().intValue() != 0) {
                                success = false;
                                msg = "抱歉，您无权限添加到收纳篮！";
                            } else if((rules.getCreateTypeId().intValue() != 7 || ciTemplateInfo.intValue() == 3) && (rules.getCreateTypeId().intValue() != 12 || ciTemplateInfo.intValue() == 3)) {
                                if(ciTemplateInfo.intValue() == 0 && rules.getCustomNum() == null) {
                                    success = false;
                                    msg = "抱歉，该客户群无规则、无清单可用，不能添加到收纳篮！";
                                }
                            } else {
                                success = false;
                                msg = "抱歉，该客户群无规则、无清单可用，不能添加到收纳篮！";
                            }
                        }

                        if(rules.getIsPrivate().intValue() == 1) {
                            if(!typeId.equals(sort)) {
                                success = false;
                                msg = "抱歉，您无权限添加到收纳篮！";
                            } else if((rules.getCreateTypeId().intValue() != 7 || ciTemplateInfo.intValue() == 3) && (rules.getCreateTypeId().intValue() != 12 || ciTemplateInfo.intValue() == 3)) {
                                if(ciTemplateInfo.intValue() == 0 && rules.getCustomNum() == null) {
                                    success = false;
                                    msg = "抱歉，该客户群无规则、无清单可用，不能添加到收纳篮！";
                                }
                            } else {
                                success = false;
                                msg = "抱歉，该客户群无规则、无清单可用，不能添加到收纳篮！";
                            }
                        }
                    }
                }
            }

            if(success) {
                rules = null;
                calculationsId = this.pojo.get("calculationsId").toString();
                msg = "加入购物车成功";
                if(StringUtils.isNotEmpty((String)this.pojo.get("typeId"))) {
                    if(ServiceConstants.EDIT_CUSTOM_FLAG.equals(isEditCustomFlag) || isEditCustomFlag == ServiceConstants.EDIT_CUSTOM_FLAG) {
                        response.removeAttribute("sessionModelList");
                    }

                    Object var22 = (List)response.getAttribute("sessionModelList");
                    if(null == var22 || ((List)var22).size() <= 0) {
                        var22 = new ArrayList();
                    }

                    long var23 = 0L;
                    if(((List)var22).size() > 0) {
                        var23 = ((CiLabelRule)((List)var22).get(((List)var22).size() - 1)).getSortNum().longValue();
                        ++var23;
                        ((List)var22).add(this.generateRule("and", 1, var23));
                    }

                    ++var23;
                    typeId = this.pojo.get("typeId").toString();
                    CiLabelRule var24 = new CiLabelRule();
                    if(ServiceConstants.LABEL_INFO_CALCULATIONS_TYPEID.equals(typeId) || typeId == ServiceConstants.LABEL_INFO_CALCULATIONS_TYPEID) {
                        CiLabelInfo var25 = e.getEffectiveLabel(calculationsId);
                        Date templateRules = var25.getEffecTime();
                        int isAddBrackets = var25.getUpdateCycle().intValue();
                        String i$;
                        if(isAddBrackets == 1) {
                            i$ = (new SimpleDateFormat("yyyyMMdd")).format(templateRules);
                            var24.setEffectDate(DateUtil.getOffsetDateByDate(i$, -1, 1));
                        } else if(isAddBrackets == 2) {
                            i$ = (new SimpleDateFormat("yyyyMM")).format(templateRules);
                            var24.setEffectDate(DateUtil.getOffsetDateByDate(i$, -1, 0));
                        }

                        var24.setCalcuElement(String.valueOf(var25.getLabelId()));
                        var24.setCustomOrLabelName(var25.getLabelName());
                        var24.setMaxVal(new Double(1.0D));
                        var24.setMinVal(new Double(0.0D));
                        var24.setLabelFlag(Integer.valueOf(1));
                        var24.setSortNum(Long.valueOf(var23));
                        var24.setLabelTypeId(var25.getLabelTypeId());
                        var24.setElementType(Integer.valueOf(2));
                        var24.setUnit(var25.getCiLabelExtInfo().getCiMdaSysTableColumn().getUnit());
                        var24.setDataDate(var25.getDataDate());
                        var24.setUpdateCycle(var25.getUpdateCycle());
                        ((List)var22).add(var24);
                        this.ciLabelUserUseService.addLabelUserUseLog(String.valueOf(var25.getLabelId()), 2);
                    }

                    if(ServiceConstants.CUSTOM_GROUP_INFO_CALCULATIONS_TYPEID.equals(typeId) || typeId == ServiceConstants.CUSTOM_GROUP_INFO_CALCULATIONS_TYPEID) {
                        CiCustomGroupInfo var26 = this.customersManagerService.queryCiCustomGroupInfo(calculationsId);
                        var24.setCustomCreateTypeId(var26.getCreateTypeId());
                        var24.setCustomId(var26.getCustomGroupId());
                        var24.setCustomOrLabelName(var26.getCustomGroupName());
                        var24.setSortNum(Long.valueOf(var23));
                        var24.setElementType(Integer.valueOf(5));
                        var24.setCustomStatus(var26.getStatus());
                        int var28 = this.customersManagerService.getCustomGroupInfoDataCycle(var26.getCreateTypeId(), var26.getUpdateCycle(), var26.getDataDate()).intValue();
                        if(var28 != -1) {
                            var24.setLabelFlag(Integer.valueOf(var28));
                        }

                        ((List)var22).add(var24);
                        Integer var30 = Integer.valueOf(0);
                        if(var26.getUpdateCycle().intValue() == 4) {
                            var30 = Integer.valueOf(1);
                        }

                        this.ciCustomUserUseService.addCustomUserUseLog(var26.getCustomGroupId(), 2, var30.intValue());
                    }

                    if(ServiceConstants.TEMPLATE_INFO_CALCULATIONS_TYPEID.equals(typeId) || typeId == ServiceConstants.TEMPLATE_INFO_CALCULATIONS_TYPEID) {
                        CiTemplateInfo var27 = this.ciTemplateInfoService.queryTemplateInfoById(calculationsId);
                        this.ciCustomUserUseService.addCustomUserUseLog(var27.getTemplateId(), 2, 1);
                        List var29 = this.customersManagerService.queryCiLabelRuleList(var27.getTemplateId(), Integer.valueOf(2));
                        if(var29 != null && var29.size() > 0) {
                            boolean var31 = var29.size() > 1 && var23 > 1L;
                            if(var31) {
                                ((List)var22).add(this.generateRule("(", 3, var23));
                            }

                            Iterator var32 = var29.iterator();

                            while(var32.hasNext()) {
                                CiLabelRule item = (CiLabelRule)var32.next();
                                ++var23;
                                item.setSortNum(Long.valueOf(var23));
                                ((List)var22).add(item);
                            }

                            ++var23;
                            if(var31) {
                                ((List)var22).add(this.generateRule(")", 3, var23));
                            }
                        }
                    }

                    response.setAttribute("sessionModelList", var22);
                    numValue = this.findLabelOrCustomNum((List)var22).intValue();
                    response.setAttribute("calcElementNum", String.valueOf(numValue));
                    this.editSessionRules((List)var22);
                } else {
                    success = false;
                    msg = "标签客户群模板类型未添加,加入购物车失败!";
                }
            }
        } catch (Exception var20) {
            success = false;
            msg = "加入购物车失败!";
            this.log.error(msg, var20);
        }

        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);
        result.put("numValue", Integer.valueOf(numValue));
        HttpServletResponse var21 = this.getResponse();

        try {
            this.sendJson(var21, JsonUtil.toJson(result));
        } catch (Exception var19) {
            this.log.error("发送json串异常", var19);
            throw new CIServiceException(var19);
        }
    }

    private Integer findLabelOrCustomNum(List<CiLabelRule> rules) {
        int numValue = 0;
        if(rules != null) {
            Iterator i$ = rules.iterator();

            while(true) {
                CiLabelRule item;
                do {
                    do {
                        if(!i$.hasNext()) {
                            return Integer.valueOf(numValue);
                        }

                        item = (CiLabelRule)i$.next();
                    } while(item == null);
                } while(item.getElementType().intValue() != 2 && item.getElementType().intValue() != 5 && item.getElementType().intValue() != 6);

                ++numValue;
                item.setLabelOrCustomSort(Integer.valueOf(numValue));
            }
        } else {
            return Integer.valueOf(numValue);
        }
    }

    public void findCusotmValidate() {
        HashMap result = new HashMap();
        CiCustomGroupInfo item = null;
        boolean success = true;
        String msg = null;

        try {
            item = this.customersManagerService.queryCiCustomGroupInfo(this.ciCustomGroupInfo.getCustomGroupId());
            if(item != null) {
                if(item.getStatus().intValue() == 0) {
                    success = false;
                    msg = "客户群已经被删除，无法加入到购物车！";
                } else {
                    String response = Configure.getInstance().getProperty("CENTER_CITYID");
                    String e = item.getCreateUserId();
                    String cityId = PrivilegeServiceUtil.getCityIdFromSession();
                    String userId = this.getUserId();
                    String creatCityId = item.getCreateCityId();
                    Integer dataStatus = item.getDataStatus();
                    if(item.getIsPrivate().intValue() == 0) {
                        if(!creatCityId.equals(response) && !creatCityId.equals(cityId) && item.getIsContainLocalList().intValue() != 0) {
                            success = false;
                            msg = "抱歉，您无权限添加到收纳篮！";
                        } else if(item.getCreateTypeId().intValue() == 7 && dataStatus.intValue() != 3 || item.getCreateTypeId().intValue() == 12 && dataStatus.intValue() != 3) {
                            success = false;
                            msg = "抱歉，该客户群无规则、无清单可用，不能添加到收纳篮！";
                        } else if(dataStatus.intValue() == 0 && item.getCustomNum() == null) {
                            success = false;
                            msg = "抱歉，该客户群无规则、无清单可用，不能添加到收纳篮！";
                        }
                    }

                    if(item.getIsPrivate().intValue() == 1) {
                        if(!userId.equals(e)) {
                            success = false;
                            msg = "抱歉，您无权限添加到收纳篮！";
                        } else if((item.getCreateTypeId().intValue() != 7 || dataStatus.intValue() == 3) && (item.getCreateTypeId().intValue() != 12 || dataStatus.intValue() == 3)) {
                            if(dataStatus.intValue() == 0 && item.getCustomNum() == null) {
                                success = false;
                                msg = "抱歉，该客户群无规则、无清单可用，不能添加到收纳篮！";
                            }
                        } else {
                            success = false;
                            msg = "抱歉，该客户群无规则、无清单可用，不能添加到收纳篮！";
                        }
                    }
                }
            } else {
                success = false;
                msg = "客户群已经删除，无法加入到购物车！";
            }
        } catch (Exception var12) {
            this.log.error("查询客户群异常", var12);
            throw new CIServiceException(var12);
        }

        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var11) {
            this.log.error("发送json串异常", var11);
            throw new CIServiceException(var11);
        }
    }

    public void editSessionByOperator() {
        HashMap result = new HashMap();
        boolean success = false;
        String msg = "修改购物车缓存失败！";

        try {
            HttpSession response = PrivilegeServiceUtil.getSession();
            List e = (List)response.getAttribute("sessionModelList");
            if(e != null && e.size() > 0) {
                for(int i = 0; i < e.size(); ++i) {
                    CiLabelRule rule = (CiLabelRule)e.get(i);
                    if(rule.getElementType().intValue() == 1) {
                        rule.setCalcuElement(this.calcuElement);
                    }
                }
            }

            response.setAttribute("sessionModelList", e);
            msg = "修改购物车缓存成功";
            success = true;
        } catch (Exception var9) {
            this.log.error(msg, var9);
            throw new CIServiceException(var9);
        }

        HttpServletResponse var10 = this.getResponse();
        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);

        try {
            this.sendJson(var10, JsonUtil.toJson(result));
        } catch (Exception var8) {
            this.log.error("发送json串异常", var8);
            throw new CIServiceException(var8);
        }
    }

    private void editSessionRules(List<CiLabelRule> rules) {
        if(rules != null && rules.size() > 0) {
            Stack stack = new Stack();

            for(int i = 0; i < rules.size(); ++i) {
                CiLabelRule rule = (CiLabelRule)rules.get(i);
                if(rule != null) {
                    String brackets = rule.getCalcuElement();
                    if("(".equals(brackets)) {
                        stack.add(Integer.valueOf(i));
                    } else if(")".equals(brackets)) {
                        int index = ((Integer)stack.pop()).intValue();
                        if(index >= 0) {
                            String randomStr = this.findRandom();
                            rule.setCreateBrackets(randomStr);
                            ((CiLabelRule)rules.get(index)).setCreateBrackets(randomStr);
                        }
                    }
                }
            }
        }

    }

    private String findRandom() {
        String guid = "";

        for(int i = 1; i <= 32; ++i) {
            String n = String.valueOf((int)Math.floor(Math.random() * 10.0D));
            guid = guid + n;
            if(i == 8 || i == 12 || i == 16 || i == 20) {
                guid = guid + "-";
            }
        }

        return guid;
    }

    private CiLabelRule generateRule(String element, int type, long sortNum) {
        CiLabelRule result = new CiLabelRule();
        result.setCalcuElement(element);
        result.setElementType(Integer.valueOf(type));
        result.setSortNum(Long.valueOf(sortNum));
        return result;
    }

    public String queryShopCarList() {
        try {
            HttpSession e = PrivilegeServiceUtil.getSession();
            this.ciLabelRuleList = (ArrayList)e.getAttribute("sessionModelList");
            ArrayList list = new ArrayList();
            if(this.ciLabelRuleList != null && this.ciLabelRuleList.size() > 0) {
                for(int i = this.ciLabelRuleList.size() - 1; i >= 0; --i) {
                    list.add(this.ciLabelRuleList.get(i));
                }

                this.ciLabelRuleList = list;
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            this.log.error("查询缓存异常!");
        }

        return "shopCartList";
    }

    public void findCalcElement() {
        HashMap result = new HashMap();

        try {
            HttpSession response = PrivilegeServiceUtil.getSession();
            List e = (List)response.getAttribute("sessionModelList");
            ArrayList ruleList = new ArrayList();
            if(e != null && e.size() > 0) {
                for(int num = e.size() - 1; num >= 0; --num) {
                    CiLabelRule item = (CiLabelRule)e.get(num);
                    Integer elementType = item.getElementType();
                    if(elementType.intValue() == 2 || elementType.intValue() == 5) {
                        ruleList.add(item);
                    }
                }
            }

            result.put("rules", ruleList);
            String var11 = (String)response.getAttribute("calcElementNum");
            result.put("num", var11);
        } catch (Exception var9) {
            this.log.error("查询计算元素异常", var9);
            throw new CIServiceException(var9);
        }

        HttpServletResponse var10 = this.getResponse();

        try {
            this.sendJson(var10, JsonUtil.toJson(result));
        } catch (Exception var8) {
            this.log.error("发送json串异常", var8);
            throw new CIServiceException(var8);
        }
    }

    public void delShopSession() {
        HashMap result = new HashMap();
        boolean success = false;
        String msg = "失败";
        int numValue = 0;

        try {
            HttpSession response = PrivilegeServiceUtil.getSession();
            if(StringUtils.isNotEmpty((String)this.pojo.get("calculationsSort"))) {
                String e = this.pojo.get("calculationsSort").toString();
                this.ciLabelRuleList = (List)response.getAttribute("sessionModelList");
                if(null == this.ciLabelRuleList || this.ciLabelRuleList.size() <= 0) {
                    this.ciLabelRuleList = new ArrayList();
                }

                Integer shopCarItemIndex = this.findShopCarItemIndex(this.ciLabelRuleList, e);
                ArrayList removeList = new ArrayList();
                if(shopCarItemIndex != null) {
                    CiLabelRule removedRule = (CiLabelRule)this.ciLabelRuleList.get(shopCarItemIndex.intValue());
                    removeList.add(removedRule);
                    CiLabelRule preRule = null;
                    CiLabelRule nextRule = null;
                    if(shopCarItemIndex.intValue() != 0) {
                        preRule = (CiLabelRule)this.ciLabelRuleList.get(shopCarItemIndex.intValue() - 1);
                    }

                    if(shopCarItemIndex.intValue() != this.ciLabelRuleList.size() - 1) {
                        nextRule = (CiLabelRule)this.ciLabelRuleList.get(shopCarItemIndex.intValue() + 1);
                    }

                    if(preRule != null) {
                        if(3 == preRule.getElementType().intValue()) {
                            if(nextRule != null) {
                                if(3 == nextRule.getElementType().intValue()) {
                                    removeList.add(preRule);
                                    removeList.add(nextRule);
                                    if(shopCarItemIndex.intValue() - 2 > 0) {
                                        removeList.add(this.ciLabelRuleList.get(shopCarItemIndex.intValue() - 2));
                                    } else if(shopCarItemIndex.intValue() + 2 < this.ciLabelRuleList.size()) {
                                        removeList.add(this.ciLabelRuleList.get(shopCarItemIndex.intValue() + 2));
                                    }
                                } else if(1 == nextRule.getElementType().intValue() && nextRule != null) {
                                    removeList.add(nextRule);
                                }
                            }
                        } else if(1 == preRule.getElementType().intValue()) {
                            removeList.add(preRule);
                        }
                    } else if(nextRule != null) {
                        removeList.add(nextRule);
                    }
                }

                this.ciLabelRuleList.removeAll(removeList);
                success = true;
                msg = "从购物车移出成功";
                numValue = this.findLabelOrCustomNum(this.ciLabelRuleList).intValue();
                if(numValue == 0) {
                    this.ciLabelRuleList = null;
                }

                response.setAttribute("sessionModelList", this.ciLabelRuleList);
                response.setAttribute("calcElementNum", String.valueOf(numValue));
            } else {
                success = false;
                msg = "标签客户群模板sort不存在,移出购物车失败!";
            }
        } catch (Exception var13) {
            msg = "移出购物车失败!";
            this.log.error(msg, var13);
        }

        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);
        result.put("numValue", Integer.valueOf(numValue));
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var12) {
            this.log.error("发送json串异常", var12);
            throw new CIServiceException(var12);
        }
    }

    public void delCarShopByBrackets() {
        HashMap result = new HashMap();
        boolean success = false;
        String msg = "失败";
        HttpSession session = PrivilegeServiceUtil.getSession();

        try {
            this.delCarShopItemBySort(this.minSortNum, this.maxSortNum, session);
            success = true;
        } catch (Exception var8) {
            success = false;
            msg = "通过括号删除购物车失败！";
            this.log.error(msg, var8);
            throw new CIServiceException(var8);
        }

        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException(var7);
        }
    }

    public void delAllCarShopSession() {
        try {
            HttpSession e = PrivilegeServiceUtil.getSession();
            e.setAttribute("sessionModelList", (Object)null);
            e.setAttribute("calcElementNum", String.valueOf(0));
            String customGroupId = (String)e.getAttribute("customGroupId");
            if(customGroupId != null) {
                e.removeAttribute("customGroupId");
            }

        } catch (Exception var3) {
            this.log.error("清空购物车发生异常!", var3);
            throw new CIServiceException();
        }
    }

    private void delCarShopItemBySort(Integer minSortNum, Integer maxSortNum, HttpSession session) {
        if(minSortNum != null && maxSortNum != null) {
            boolean numValue = false;
            List ciLabelRuleList = (List)session.getAttribute("sessionModelList");
            ArrayList removeRule = new ArrayList();

            for(int i = 0; i < ciLabelRuleList.size(); ++i) {
                CiLabelRule rule = (CiLabelRule)ciLabelRuleList.get(i);
                int sortNum = rule.getSortNum().intValue();
                if(sortNum == minSortNum.intValue() || sortNum == maxSortNum.intValue()) {
                    removeRule.add(rule);
                }
            }

            ciLabelRuleList.removeAll(removeRule);
            session.setAttribute("sessionModelList", ciLabelRuleList);
            int var10 = this.findLabelOrCustomNum(ciLabelRuleList).intValue();
            if(var10 == 0) {
                ciLabelRuleList = null;
            }

            session.setAttribute("calcElementNum", String.valueOf(var10));
        }
    }

    public String findCalculateCenter() {
        Cookie[] cookies = this.getRequest().getCookies();
        boolean isExitCalculates = false;

        String customGroupId;
        for(int e = 0; e < cookies.length; ++e) {
            Cookie newLablDay = cookies[e];
            String cacheBase = ServiceConstants.COOKIE_NAME_FOR_CALCULATE_PAGE + "_" + this.getUserId();
            if(cacheBase.equals(newLablDay.getName())) {
                isExitCalculates = true;
                customGroupId = newLablDay.getValue();
                int editByUpdateUrl = Integer.parseInt(customGroupId);
                ++editByUpdateUrl;
                newLablDay.setValue(String.valueOf(editByUpdateUrl));
                newLablDay.setMaxAge(31536000);
                this.getResponse().addCookie(newLablDay);
                break;
            }
        }

        if(!isExitCalculates) {
            Cookie var16 = new Cookie(ServiceConstants.COOKIE_NAME_FOR_CALCULATE_PAGE + "_" + this.getUserId(), "1");
            var16.setMaxAge(31536000);
            this.getResponse().addCookie(var16);
        }

        try {
            String var17 = this.getNewLabelMonth();
            this.newLabelMonthFormat = DateUtil.string2StringFormat(var17, "yyyyMM", "yyyy-MM");
            String var18 = this.getNewLabelDay();
            this.newLabelDayFormat = DateUtil.string2StringFormat(var18, "yyyyMMdd", "yyyy-MM-dd");
            CacheBase var19 = CacheBase.getInstance();
            this.dimListTacticsList = (List<DimListTactics>)var19.getObjectList("DIM_LIST_TACTICS");
            customGroupId = null;
            boolean var20 = true;
            if(this.ciCustomGroupInfo != null && StringUtils.isNotEmpty(this.ciCustomGroupInfo.getCustomGroupId())) {
                customGroupId = this.ciCustomGroupInfo.getCustomGroupId();
                super.getSession().setAttribute("customGroupId", customGroupId);
            } else {
                customGroupId = (String)super.getSession().getAttribute("customGroupId");
                var20 = false;
            }

            if(StringUtil.isNotEmpty(customGroupId)) {
                this.ciCustomGroupInfo = this.customersManagerService.queryCiCustomGroupInfo(customGroupId);
                List rules = this.customersManagerService.queryCiLabelRuleList(customGroupId, Integer.valueOf(1));
                if(this.ciCustomGroupInfo.getUpdateCycle().intValue() != 4) {
                    this.setRulesOffsetDate(rules);
                }

                CiCustomListInfo listInfo = this.customersManagerService.queryCustomListInfo(this.ciCustomGroupInfo);
                if(null != listInfo) {
                    this.ciGroupAttrRelList = this.ciGroupAttrRelService.queryNewestGroupAttrRelList(customGroupId);
                    Iterator sceneRels = this.ciGroupAttrRelList.iterator();

                    while(sceneRels.hasNext()) {
                        CiGroupAttrRel sceneRelIds = (CiGroupAttrRel)sceneRels.next();
                        if(sceneRelIds.getAttrSource() == 3) {
                            CiCustomGroupInfo session = this.customersManagerService.queryCiCustomGroupInfoByListInfoId(sceneRelIds.getLabelOrCustomId());
                            sceneRelIds.setCustomId(session.getCustomGroupId());
                            sceneRelIds.setCustomName(session.getCustomGroupName());
                        }
                    }
                }

                List var21 = this.ciCustomGroupInfo.getSceneList();
                StringBuffer var22 = new StringBuffer();
                if(var21 != null) {
                    for(int var23 = 0; var23 < var21.size(); ++var23) {
                        CiCustomSceneRel numValue = (CiCustomSceneRel)var21.get(var23);
                        if(StringUtil.isNotEmpty(numValue) && numValue.getStatus() == 1 && StringUtil.isNotEmpty(numValue.getId().getSceneId())) {
                            String sceneId = numValue.getId().getSceneId();
                            var22.append(sceneId).append(",");
                        }
                    }

                    if(StringUtil.isNotEmpty(var22.toString())) {
                        var22.deleteCharAt(var22.length() - 1);
                    }
                }

                this.ciCustomGroupInfo.setSceneId(var22.toString());
                HttpSession var24 = super.getSession();
                if(var20) {
                    var24.setAttribute("sessionModelList", rules);
                    int var25 = this.findLabelOrCustomNum(rules).intValue();
                    if(var25 == 0) {
                        this.ciLabelRuleList = null;
                    }

                    var24.setAttribute("calcElementNum", String.valueOf(var25));
                }
            }

            this.resolveCustomRule();
        } catch (Exception var15) {
            this.log.error("跳转到计算中心页面发生异常！", var15);
            throw new CIServiceException();
        }

        this.forwardType = 1;
        return "calculateCenter";
    }

    public String findCalculateCenterOld() {
        Cookie[] cookies = this.getRequest().getCookies();
        boolean isExitCalculates = false;

        String customGroupId;
        for(int e = 0; e < cookies.length; ++e) {
            Cookie newLablDay = cookies[e];
            String cacheBase = ServiceConstants.COOKIE_NAME_FOR_CALCULATE_PAGE + "_" + this.getUserId();
            if(cacheBase.equals(newLablDay.getName())) {
                isExitCalculates = true;
                customGroupId = newLablDay.getValue();
                int editByUpdateUrl = Integer.parseInt(customGroupId);
                ++editByUpdateUrl;
                newLablDay.setValue(String.valueOf(editByUpdateUrl));
                newLablDay.setMaxAge(31536000);
                this.getResponse().addCookie(newLablDay);
                break;
            }
        }

        if(!isExitCalculates) {
            Cookie var16 = new Cookie(ServiceConstants.COOKIE_NAME_FOR_CALCULATE_PAGE + "_" + this.getUserId(), "1");
            var16.setMaxAge(31536000);
            this.getResponse().addCookie(var16);
        }

        try {
            String var17 = this.getNewLabelMonth();
            this.newLabelMonthFormat = DateUtil.string2StringFormat(var17, "yyyyMM", "yyyy-MM");
            String var18 = this.getNewLabelDay();
            this.newLabelDayFormat = DateUtil.string2StringFormat(var18, "yyyyMMdd", "yyyy-MM-dd");
            CacheBase var19 = CacheBase.getInstance();
            this.dimListTacticsList = (List<DimListTactics>)var19.getObjectList("DIM_LIST_TACTICS");
            customGroupId = null;
            boolean var20 = true;
            if(this.ciCustomGroupInfo != null && StringUtils.isNotEmpty(this.ciCustomGroupInfo.getCustomGroupId())) {
                customGroupId = this.ciCustomGroupInfo.getCustomGroupId();
                super.getSession().setAttribute("customGroupId", customGroupId);
            } else {
                customGroupId = (String)super.getSession().getAttribute("customGroupId");
                var20 = false;
            }

            if(StringUtil.isNotEmpty(customGroupId)) {
                this.ciCustomGroupInfo = this.customersManagerService.queryCiCustomGroupInfo(customGroupId);
                List rules = this.customersManagerService.queryCiLabelRuleList(customGroupId, Integer.valueOf(1));
                if(this.ciCustomGroupInfo.getUpdateCycle().intValue() != 4) {
                    this.setRulesOffsetDate(rules);
                }

                CiCustomListInfo listInfo = this.customersManagerService.queryCustomListInfo(this.ciCustomGroupInfo);
                if(null != listInfo) {
                    this.ciGroupAttrRelList = this.ciGroupAttrRelService.queryNewestGroupAttrRelList(customGroupId);
                    Iterator sceneRels = this.ciGroupAttrRelList.iterator();

                    while(sceneRels.hasNext()) {
                        CiGroupAttrRel sceneRelIds = (CiGroupAttrRel)sceneRels.next();
                        if(sceneRelIds.getAttrSource() == 3) {
                            CiCustomGroupInfo session = this.customersManagerService.queryCiCustomGroupInfoByListInfoId(sceneRelIds.getLabelOrCustomId());
                            sceneRelIds.setCustomId(session.getCustomGroupId());
                            sceneRelIds.setCustomName(session.getCustomGroupName());
                        }
                    }
                }

                List var21 = this.ciCustomGroupInfo.getSceneList();
                StringBuffer var22 = new StringBuffer();
                if(var21 != null) {
                    for(int var23 = 0; var23 < var21.size(); ++var23) {
                        CiCustomSceneRel numValue = (CiCustomSceneRel)var21.get(var23);
                        if(StringUtil.isNotEmpty(numValue) && numValue.getStatus() == 1 && StringUtil.isNotEmpty(numValue.getId().getSceneId())) {
                            String sceneId = numValue.getId().getSceneId();
                            var22.append(sceneId).append(",");
                        }
                    }

                    if(StringUtil.isNotEmpty(var22.toString())) {
                        var22.deleteCharAt(var22.length() - 1);
                    }
                }

                this.ciCustomGroupInfo.setSceneId(var22.toString());
                HttpSession var24 = super.getSession();
                if(var20) {
                    var24.setAttribute("sessionModelList", rules);
                    int var25 = this.findLabelOrCustomNum(rules).intValue();
                    if(var25 == 0) {
                        this.ciLabelRuleList = null;
                    }

                    var24.setAttribute("calcElementNum", String.valueOf(var25));
                }
            }

            this.resolveCustomRule();
        } catch (Exception var15) {
            this.log.error("跳转到计算中心页面发生异常！", var15);
            throw new CIServiceException();
        }

        this.forwardType = 1;
        return "calculateCenterOld";
    }

    private void resolveCustomRule() {
        HttpSession session = super.getSession();
        List rules = (List)session.getAttribute("sessionModelList");
        ArrayList newRules = new ArrayList();
        if(rules != null) {
            int i;
            for(i = 0; i < rules.size(); ++i) {
                CiLabelRule rule = (CiLabelRule)rules.get(i);
                int elementType = rule.getElementType().intValue();
                if(elementType == 6) {
                    List children = rule.getChildCiLabelRuleList();
                    newRules.add(this.generateRule("(", 3, 0L));
                    newRules.addAll(children);
                    newRules.add(this.generateRule(")", 3, 0L));
                } else {
                    newRules.add(rule);
                }
            }

            for(i = 0; i < newRules.size(); ++i) {
                ((CiLabelRule)newRules.get(i)).setSortNum(Long.valueOf((long)(i + 1)));
            }

            this.findLabelOrCustomNum(newRules);
            this.editSessionRules(newRules);
            session.setAttribute("sessionModelList", newRules);
        }

    }

    private void setRulesOffsetDate(List<CiLabelRule> rules) {
        if(rules != null) {
            for(int i = 0; i < rules.size(); ++i) {
                CiLabelRule rule = (CiLabelRule)rules.get(i);
                if(rule.getElementType().intValue() == 2) {
                    if(rule.getIsNeedOffset() != null && rule.getIsNeedOffset().intValue() == ServiceConstants.IS_NEED_OFFSET_YES) {
                        this.setRuleOffsetDate(rule);
                    } else if(rule.getLabelTypeId().intValue() == 8) {
                        List children = rule.getChildCiLabelRuleList();
                        if(children != null) {
                            Iterator i$ = children.iterator();

                            while(i$.hasNext()) {
                                CiLabelRule item = (CiLabelRule)i$.next();
                                if(item.getIsNeedOffset() != null && item.getIsNeedOffset().intValue() == ServiceConstants.IS_NEED_OFFSET_YES) {
                                    this.setRuleOffsetDate(item);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private void setRuleOffsetDate(CiLabelRule rule) {
        CacheBase cache = CacheBase.getInstance();
        String dataDate = cache.getNewLabelDay();
        String offsetDate = this.ciCustomGroupInfo.getOffsetDate();
        String startDate = rule.getStartTime();
        String endDate = rule.getEndTime();
        String fmt = "yyyyMMdd";
        if(this.ciCustomGroupInfo.getUpdateCycle().intValue() == 2) {
            fmt = "yyyyMM";
            dataDate = cache.getNewLabelMonth();
        }

        int offset = DateUtil.dateInterval(offsetDate, dataDate, fmt, this.ciCustomGroupInfo.getUpdateCycle().intValue());
        String currentStartDate = DateUtil.calculateOffsetDate(startDate, offset, "yyyy-MM-dd", this.ciCustomGroupInfo.getUpdateCycle().intValue());
        String currentEndDate = DateUtil.calculateOffsetDate(endDate, offset, "yyyy-MM-dd", this.ciCustomGroupInfo.getUpdateCycle().intValue());
        rule.setStartTime(currentStartDate);
        rule.setEndTime(currentEndDate);
    }

    public void saveSession() {
        try {
            boolean e = false;
            HttpSession session = PrivilegeServiceUtil.getSession();
            int e1 = this.findLabelOrCustomNum(this.ciLabelRuleList).intValue();
            if(e1 == 0) {
                this.ciLabelRuleList = null;
            }

            session.setAttribute("sessionModelList", this.ciLabelRuleList);
            session.setAttribute("calcElementNum", String.valueOf(e1));
        } catch (Exception var3) {
            this.log.error("重新设置session异常！", var3);
        }

    }

    public void findEnumByImport() {
        HashMap result = new HashMap();
        Object map = null;
        boolean success = true;
        String msg = null;

        try {
            if(this.flag) {
                CacheBase e = CacheBase.getInstance();
                CiLabelInfo info = e.getEffectiveLabel(this.columnId);
                this.columnId = String.valueOf(info.getCiLabelExtInfo().getCiMdaSysTableColumn().getColumnId());
            }

            String e1 = PrivilegeServiceUtil.getUserId();
            map = this.ciLabelInfoService.queryVerticalValueByImport(e1, Integer.valueOf(this.columnId), this.file, this.fileFileName);
        } catch (Exception var17) {
            this.log.error("上传文件解析异常", var17);
            success = false;
            msg = "上传文件数据类型错误，请检查上传文件！";
            map = new ArrayList();
        } finally {
            result.put("data", map);
            result.put("success", Boolean.valueOf(success));
            result.put("msg", msg);
            HttpServletResponse response = this.getResponse();

            try {
                this.sendJson(response, JsonUtil.toJson(result));
            } catch (Exception var16) {
                this.log.error("数据传输异常！", var16);
                throw new CIServiceException();
            }
        }

    }

    public void findDelCustom() {
        HashMap result = new HashMap();
        HttpSession session = PrivilegeServiceUtil.getSession();
        StringBuffer customIds = new StringBuffer();
        Object list = new ArrayList();

        try {
            List response = (List)session.getAttribute("sessionModelList");
            if(response != null && response.size() > 0) {
                Iterator e = response.iterator();

                while(e.hasNext()) {
                    CiLabelRule item = (CiLabelRule)e.next();
                    if(item.getElementType().intValue() == 5) {
                        customIds.append("\'").append(item.getCustomId()).append("\',");
                    }
                }

                customIds.deleteCharAt(customIds.length() - 1);
                list = this.customersManagerService.queryDelCustomGroup(customIds.toString());
            }
        } catch (Exception var9) {
            this.log.error(" 查询计算中心要计算的被删除的客户群异常！", var9);
            throw new CIServiceException();
        }

        result.put("result", list);
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var8) {
            this.log.error("查询被删除的客户群,数据传输异常！", var8);
            throw new CIServiceException();
        }
    }

    public String findCalculateBodyOld() {
        return "calculateBodyOld";
    }

    public String findCalculateBody() {
        return "calculateBody";
    }

    public void findCarShopNum() {
        HttpSession session = PrivilegeServiceUtil.getSession();
        String num = (String)session.getAttribute("calcElementNum");
        HashMap result = new HashMap();
        result.put("num", num);
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("数据传输异常！", var6);
            throw new CIServiceException();
        }
    }

    private Integer findShopCarItemIndex(List<CiLabelRule> ciLabelRuleList, String calculationsSort) {
        Integer result = null;
        if(ciLabelRuleList != null && ciLabelRuleList.size() > 0) {
            for(int i = 0; i < ciLabelRuleList.size(); ++i) {
                CiLabelRule item = (CiLabelRule)ciLabelRuleList.get(i);
                Integer sortNum = item.getLabelOrCustomSort();
                if(calculationsSort.equals(String.valueOf(sortNum))) {
                    result = Integer.valueOf(i);
                    break;
                }
            }
        }

        return result;
    }

    public void findCustomTipInfo() {
        HashMap result = new HashMap();

        try {
            String response = null;
            CiCustomGroupInfo e = new CiCustomGroupInfo();
            e.setCustomGroupId(this.ciCustomGroupInfo.getCustomGroupId());
            e = this.customersManagerService.queryCiCustomGroupInfoAndAttention(e);
            CacheBase cache = CacheBase.getInstance();
            if(cache.getHotCustomByKey("HOT_CUSTOMS", this.ciCustomGroupInfo.getCustomGroupId()) != null) {
                e.setIsHotCustom("true");
            }

            if(e.getCreateTime() != null) {
                response = DateUtil.date2String(e.getCreateTime(), "yyyy-MM-dd");
                e.setCreateTimeView(response);
            }

            if(e.getNewModifyTime() != null) {
                response = DateUtil.date2String(e.getNewModifyTime(), "yyyy-MM-dd");
                e.setProductDate(response);
            }

            if(e.getStartDate() != null) {
                response = DateUtil.date2String(e.getStartDate(), "yyyy-MM-dd");
                e.setStartDateStr(response);
            }

            if(e.getEndDate() != null) {
                response = DateUtil.date2String(e.getEndDate(), "yyyy-MM-dd");
                e.setEndDateStr(response);
            }

            result.put("customGroupInfo", e);
        } catch (Exception var6) {
            this.log.error("查询客户群提示信息异常！", var6);
            throw new CIServiceException();
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var5) {
            this.log.error("数据传输异常！", var5);
            throw new CIServiceException();
        }
    }

    public void findLabelTipInfo() {
        HashMap result = new HashMap();
        String date = null;

        try {
            CacheBase response = CacheBase.getInstance();
            CiLabelInfo e = response.getEffectiveLabel(this.labelId);
            CiLabelInfo searchBean = new CiLabelInfo();
            searchBean.setLabelId(e.getLabelId());
            LabelDetailInfo detailInfo = this.ciLabelInfoService.getSysRecommendLabelInfo(e.getLabelId());
            if(e.getCreateTime() != null) {
                date = DateUtil.date2String(e.getCreateTime(), "yyyy-MM-dd");
                e.setCreateTimeStr(date);
            }

            if(e.getEffecTime() != null) {
                date = DateUtil.date2String(e.getEffecTime(), "yyyy-MM-dd");
                e.setEffecTimeStr(date);
            }

            if(e.getFailTime() != null) {
                date = DateUtil.date2String(e.getFailTime(), "yyyy-MM-dd");
                e.setFailTimeStr(date);
            }

            if(e.getUpdateCycle().intValue() == 1) {
                e.setNewDataDate(DateUtil.string2StringFormat(e.getDataDate(), "yyyyMMdd", "yyyy-MM-dd"));
            } else {
                e.setNewDataDate(DateUtil.string2StringFormat(e.getDataDate(), "yyyyMM", "yyyy-MM"));
            }

            if(e.getLabelTypeId().intValue() == 1) {
                if(e.getCiLabelExtInfo() != null && e.getCiLabelExtInfo().getCustomNum() != null && e.getCiLabelExtInfo().getCustomNum() != null) {
                    e.setCustomerNum(Long.valueOf(e.getCiLabelExtInfo().getCustomNum().longValue()));
                } else {
                    e.setCustomerNum(Long.valueOf(-1L));
                }
            }

            result.put("labelInfo", e);
            if(detailInfo != null) {
                result.put("detailInfo", detailInfo);
            } else {
                result.put("detailInfo", new LabelDetailInfo());
            }
        } catch (Exception var8) {
            this.log.error("查询标签提示信息异常！", var8);
            throw new CIServiceException();
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("数据传输异常！", var7);
            throw new CIServiceException();
        }
    }

    public void indexQueryTemplateName() {
        try {
            if(StringUtil.isNotEmpty(this.keyWord)) {
                String e = this.getUserId();
                HashMap result = new HashMap();
                List templatesList = this.ciTemplateInfoService.indexQueryTemplateName(this.keyWord, e);
                result.put("totalResultsCount", Integer.valueOf(templatesList.size()));
                result.put("geonames", templatesList);
                HttpServletResponse response = this.getResponse();
                this.sendJson(response, this.callback + "(" + JsonUtil.toJson(result) + ")");
            }
        } catch (Exception var5) {
            this.log.error("首页模板查询框动态搜索关联报错", var5);
        }

    }

    public String verticalSettingsDialog() {
        CacheBase cache = CacheBase.getInstance();
        String labelId = this.getRequest().getParameter("labelId");
        CiLabelInfo ciLabelInfo = cache.getEffectiveLabel(labelId);
        Set columnRels = ciLabelInfo.getCiLabelExtInfo().getCiLabelVerticalColumnRels();
        Iterator it = columnRels.iterator();
        ArrayList ciLabelVerticalColumnRels = new ArrayList();

        while(it.hasNext()) {
            CiLabelVerticalColumnRel columnsMustColumns = (CiLabelVerticalColumnRel)it.next();
            ciLabelVerticalColumnRels.add(columnsMustColumns);
        }

        if(this.ciMdaSysTableColumns == null) {
            this.ciMdaSysTableColumns = new ArrayList();
        }

        ArrayList columnsMustColumns1 = new ArrayList();
        ArrayList columnsNoMustColumns = new ArrayList();
        Iterator i$ = ciLabelVerticalColumnRels.iterator();

        while(i$.hasNext()) {
            CiLabelVerticalColumnRel columnRel = (CiLabelVerticalColumnRel)i$.next();
            CiMdaSysTableColumn mdaSysTableColumn = columnRel.getCiMdaSysTableColumn();
            if(columnRel.getIsMustColumn().intValue() == 1) {
                columnsMustColumns1.add(mdaSysTableColumn);
            } else if(columnRel.getIsMustColumn().intValue() == 0) {
                columnsNoMustColumns.add(mdaSysTableColumn);
            }
        }

        this.ciMdaSysTableColumns.addAll(columnsMustColumns1);
        this.ciMdaSysTableColumns.addAll(columnsNoMustColumns);
        return "verticalSettingsDialog";
    }

    public String findCustomListAndRule() {
        try {
            if(null != this.pojo.get("valueId") && !"".equals(this.pojo.get("valueId"))) {
                String e = this.pojo.get("valueId").toString();
                this.ciCustomListInfoList = this.customersManagerService.querySuccessCiCustomListInfoByCGroupId(e);
                this.ciCustomGroupInfo = this.customersManagerService.queryCiCustomGroupInfo(e);
                this.ciLabelRuleList = this.customersManagerService.queryCiLabelRuleList(e, Integer.valueOf(1));
                this.setRulesOffsetDate(this.ciLabelRuleList);
                if(this.ciCustomGroupInfo.getUpdateCycle().intValue() == 3 && this.ciCustomListInfoList.size() > 8) {
                    this.ciCustomListInfoList = this.ciCustomListInfoList.subList(0, 8);
                }
            }
        } catch (Exception var2) {
            this.log.error("根据客户群ID或者清单列表及规则失败", var2);
        }

        return "customDialog";
    }

    public String findVerticalLabel() {
        try {
            this.ciLabelVerticalColumnRelList = this.ciLabelInfoService.queryCiLabelVerticalColumnRelByLabelId(Integer.valueOf(this.labelId));
            return "verticalLabelSetDialog";
        } catch (Exception var2) {
            this.log.error("查询纵表标签异常", var2);
            throw new CIServiceException();
        }
    }

    public String findVerticalLabelAttr() {
        try {
            this.ciLabelVerticalColumnRelList = this.ciLabelInfoService.queryCiLabelVerticalColumnRelByLabelId(Integer.valueOf(this.labelId));
            return "verticalLabelAttrDialog";
        } catch (Exception var2) {
            this.log.error("查询纵表标签异常", var2);
            throw new CIServiceException();
        }
    }

    public String attentionIndex() {
        return "attentionIndex";
    }

    public String marketNavigationIndex() {
        CacheBase cache = CacheBase.getInstance();
        this.dimScenes = (List<DimScene>)cache.getObjectList("DIM_SCENE");
        CILogServiceUtil.getLogServiceInstance().log("COC_HOME_MARKET_NAVIGATION_LINK", "", "", "营销导航跳转成功", OperResultEnum.Success, LogLevelEnum.Normal);
        return "marketNavigationIndex";
    }

    public String showMarketNavigation() throws Exception {
        this.recommendLabelIfos = this.ciLabelInfoService.getSysRecommendLabelInfos(this.searchBean);
        String userId = this.getUserId();
        String createCityId = PrivilegeServiceUtil.getCityIdFromSession();
        String root = Configure.getInstance().getProperty("CENTER_CITYID");
        if(!PrivilegeServiceUtil.isAdminUser(userId)) {
            this.searchBeanCustom.setCreateUserId(userId);
            this.searchBeanCustom.setCreateCityId(createCityId);
        } else if(!createCityId.equals(root)) {
            this.searchBeanCustom.setCreateCityId(createCityId);
        }

        this.recommendCustomInfos = this.customersManagerService.getSysRecommendCustomInfos(this.searchBeanCustom);
        return "marketNavigationLabelAndCustoms";
    }

    public String rankingListViewIndex() {
        return "rankingListViewIndex";
    }

    public String rankingListIndex() {
        return "rankingListIndex";
    }

    public void findMyCollection() {
        HashMap result = new HashMap();

        try {
            if(this.pager == null) {
                this.pager = new Pager();
            }

            byte response = 5;
            this.pager.setPageNum(1);
            this.pager.setPageSize(response);
            List e = this.customersManagerService.queryLastLabelCustomAttentionList(this.pager);
            result.put("rules", e);
        } catch (Exception var5) {
            this.log.error("查询我的收藏异常", var5);
            throw new CIServiceException(var5);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var4) {
            this.log.error("发送json串异常", var4);
            throw new CIServiceException(var4);
        }
    }

    public void findLikeLabelByImport() {
        HashMap result = new HashMap();
        String resultStr = "";
        boolean success = true;
        String msg = null;

        try {
            resultStr = this.ciLabelInfoService.queryLikeLabelValueByImport(this.file, this.fileFileName);
        } catch (Exception var16) {
            this.log.error("上传文件解析异常", var16);
            success = false;
            msg = "上传文件数据类型错误，请检查上传文件！";
        } finally {
            result.put("data", resultStr);
            result.put("success", Boolean.valueOf(success));
            result.put("msg", msg);
            HttpServletResponse response = this.getResponse();

            try {
                this.sendJson(response, JsonUtil.toJson(result));
            } catch (Exception var15) {
                this.log.error("数据传输异常！", var15);
                throw new CIServiceException();
            }
        }

    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String forwardIndex() {
        this.getRequest().setAttribute("source", this.source);
        return "labelIndex";
    }

    public String getCallback() {
        return this.callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getClassId() {
        return this.classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public List<CiLabelInfo> getLabelClass() {
        return this.labelClass;
    }

    public void setLabelClass(List<CiLabelInfo> labelClass) {
        this.labelClass = labelClass;
    }

    public List<DimCustomCreateType> getCreateTypeList() {
        return this.createTypeList;
    }

    public void setCreateTypeList(List<DimCustomCreateType> createTypeList) {
        this.createTypeList = createTypeList;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isLabelPage() {
        return this.labelPage;
    }

    public void setLabelPage(boolean labelPage) {
        this.labelPage = labelPage;
    }

    public boolean isProductPage() {
        return this.productPage;
    }

    public void setProductPage(boolean productPage) {
        this.productPage = productPage;
    }

    public String getNoticeId() {
        return this.noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public List<CiProductCategory> getFirstLevelProductCatogery() {
        return this.firstLevelProductCatogery;
    }

    public void setFirstLevelProductCatogery(List<CiProductCategory> firstLevelProductCatogery) {
        this.firstLevelProductCatogery = firstLevelProductCatogery;
    }

    public String getDataScope() {
        return this.dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public String getJobName() {
        return this.jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDataDate() {
        return this.dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public String getLabelId() {
        return this.labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public int getLabelType() {
        return this.labelType;
    }

    public void setLabelType(int labelType) {
        this.labelType = labelType;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public String getLabelKeyWord() {
        return this.labelKeyWord;
    }

    public void setLabelKeyWord(String labelKeyWord) {
        this.labelKeyWord = labelKeyWord;
    }

    public void setLabelDetailList(List<LabelDetailInfo> labelDetailList) {
        this.labelDetailList = labelDetailList;
    }

    public List<LabelDetailInfo> getLabelDetailList() {
        return this.labelDetailList;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<ProductDetailInfo> getProductDetailList() {
        return this.productDetailList;
    }

    public void setProductDetailList(List<ProductDetailInfo> productDetailList) {
        this.productDetailList = productDetailList;
    }

    public List<CrmTypeModel> getPeopleBaseList() {
        return this.peopleBaseList;
    }

    public void setPeopleBaseList(List<CrmTypeModel> peopleBaseList) {
        this.peopleBaseList = peopleBaseList;
    }

    public List<String> getTop10() {
        return this.top10;
    }

    public void setTop10(List<String> top10) {
        this.top10 = top10;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public Collection<CrmTypeModel> getPeopleLifeList() {
        return this.peopleLifeList;
    }

    public void setPeopleLifeList(Collection<CrmTypeModel> peopleLifeList) {
        this.peopleLifeList = peopleLifeList;
    }

    public Long getCustomNum() {
        return this.customNum;
    }

    public void setCustomNum(Long customNum) {
        this.customNum = customNum;
    }

    public Integer getExploreFromModuleFlag() {
        return this.exploreFromModuleFlag;
    }

    public void setExploreFromModuleFlag(Integer exploreFromModuleFlag) {
        this.exploreFromModuleFlag = exploreFromModuleFlag;
    }

    public List<CiLabelInfoTree> getCategoryTreeList() {
        return this.categoryTreeList;
    }

    public void setCategoryTreeList(List<CiLabelInfoTree> categoryTreeList) {
        this.categoryTreeList = categoryTreeList;
    }

    public CiShopSessionModel getCiShopSessionModel() {
        return this.ciShopSessionModel;
    }

    public void setCiShopSessionModel(CiShopSessionModel ciShopSessionModel) {
        this.ciShopSessionModel = ciShopSessionModel;
    }

    public HashMap<String, Object> getPojo() {
        return this.pojo;
    }

    public void setPojo(HashMap<String, Object> pojo) {
        this.pojo = pojo;
    }

    public List<CiShopSessionModel> getCiShopSessionModelList() {
        return this.ciShopSessionModelList;
    }

    public void setCiShopSessionModelList(List<CiShopSessionModel> ciShopSessionModelList) {
        this.ciShopSessionModelList = ciShopSessionModelList;
    }

    public List<CiLabelRule> getCiLabelRuleList() {
        return this.ciLabelRuleList;
    }

    public void setCiLabelRuleList(List<CiLabelRule> ciLabelRuleList) {
        this.ciLabelRuleList = ciLabelRuleList;
    }

    public CiLabelInfo getSearchBean() {
        return this.searchBean;
    }

    public void setSearchBean(CiLabelInfo searchBean) {
        this.searchBean = searchBean;
    }

    public List<CiCustomListInfo> getCiCustomListInfoList() {
        return this.ciCustomListInfoList;
    }

    public void setCiCustomListInfoList(List<CiCustomListInfo> ciCustomListInfoList) {
        this.ciCustomListInfoList = ciCustomListInfoList;
    }

    public CiCustomGroupInfo getCiCustomGroupInfo() {
        return this.ciCustomGroupInfo;
    }

    public void setCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo) {
        this.ciCustomGroupInfo = ciCustomGroupInfo;
    }

    public Integer getMinSortNum() {
        return this.minSortNum;
    }

    public void setMinSortNum(Integer minSortNum) {
        this.minSortNum = minSortNum;
    }

    public Integer getMaxSortNum() {
        return this.maxSortNum;
    }

    public void setMaxSortNum(Integer maxSortNum) {
        this.maxSortNum = maxSortNum;
    }

    public boolean isFlag() {
        return this.flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getNewLabelMonthFormat() {
        return this.newLabelMonthFormat;
    }

    public void setNewLabelMonthFormat(String newLabelMonthFormat) {
        this.newLabelMonthFormat = newLabelMonthFormat;
    }

    public String getSysFlag() {
        return this.sysFlag;
    }

    public void setSysFlag(String sysFlag) {
        this.sysFlag = sysFlag;
    }

    public List<CiMdaSysTableColumn> getCiMdaSysTableColumns() {
        return this.ciMdaSysTableColumns;
    }

    public void setCiMdaSysTableColumns(List<CiMdaSysTableColumn> ciMdaSysTableColumns) {
        this.ciMdaSysTableColumns = ciMdaSysTableColumns;
    }

    public String getSeachTypeId() {
        return this.seachTypeId;
    }

    public void setSeachTypeId(String seachTypeId) {
        this.seachTypeId = seachTypeId;
    }

    public String getSeachKeyWord() {
        return this.seachKeyWord;
    }

    public void setSeachKeyWord(String seachKeyWord) {
        this.seachKeyWord = seachKeyWord;
    }

    public int getUserOperType() {
        return this.userOperType;
    }

    public void setUserOperType(int userOperType) {
        this.userOperType = userOperType;
    }

    public List<DimScene> getDimScenes() {
        return this.dimScenes;
    }

    public void setDimScenes(List<DimScene> dimScenes) {
        this.dimScenes = dimScenes;
    }

    public List<CiLabelVerticalColumnRel> getCiLabelVerticalColumnRelList() {
        return this.ciLabelVerticalColumnRelList;
    }

    public void setCiLabelVerticalColumnRelList(List<CiLabelVerticalColumnRel> ciLabelVerticalColumnRelList) {
        this.ciLabelVerticalColumnRelList = ciLabelVerticalColumnRelList;
    }

    public CiCustomGroupInfo getSearchBeanCustom() {
        return this.searchBeanCustom;
    }

    public void setSearchBeanCustom(CiCustomGroupInfo searchBeanCustom) {
        this.searchBeanCustom = searchBeanCustom;
    }

    public List<CiCustomGroupInfo> getRecommendCustomInfos() {
        return this.recommendCustomInfos;
    }

    public void setRecommendCustomInfos(List<CiCustomGroupInfo> recommendCustomInfos) {
        this.recommendCustomInfos = recommendCustomInfos;
    }

    public List<LabelDetailInfo> getRecommendLabelIfos() {
        return this.recommendLabelIfos;
    }

    public void setRecommendLabelIfos(List<LabelDetailInfo> recommendLabelIfos) {
        this.recommendLabelIfos = recommendLabelIfos;
    }

    public int getAttentionTypeId() {
        return this.attentionTypeId;
    }

    public void setAttentionTypeId(int attentionTypeId) {
        this.attentionTypeId = attentionTypeId;
    }

    public String getRefreshType() {
        return this.refreshType;
    }

    public void setRefreshType(String refreshType) {
        this.refreshType = refreshType;
    }

    public String getNewLabelDayFormat() {
        return this.newLabelDayFormat;
    }

    public void setNewLabelDayFormat(String newLabelDayFormat) {
        this.newLabelDayFormat = newLabelDayFormat;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileFileName() {
        return this.fileFileName;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public String getColumnId() {
        return this.columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public List<DimListTactics> getDimListTacticsList() {
        return this.dimListTacticsList;
    }

    public void setDimListTacticsList(List<DimListTactics> dimListTacticsList) {
        this.dimListTacticsList = dimListTacticsList;
    }

    public String getLabelTypeMap() {
        return this.labelTypeMap;
    }

    public void setLabelTypeMap(String labelTypeMap) {
        this.labelTypeMap = labelTypeMap;
    }

    public String getC1LabelId() {
        return this.c1LabelId;
    }

    public void setC1LabelId(String c1LabelId) {
        this.c1LabelId = c1LabelId;
    }

    public String getC2LabelId() {
        return this.c2LabelId;
    }

    public void setC2LabelId(String c2LabelId) {
        this.c2LabelId = c2LabelId;
    }

    public String getC3LabelId() {
        return this.c3LabelId;
    }

    public void setC3LabelId(String c3LabelId) {
        this.c3LabelId = c3LabelId;
    }

    public String getL1LabelName() {
        return this.l1LabelName;
    }

    public void setL1LabelName(String l1LabelName) {
        this.l1LabelName = l1LabelName;
    }

    public String getL2LabelName() {
        return this.l2LabelName;
    }

    public void setL2LabelName(String l2LabelName) {
        this.l2LabelName = l2LabelName;
    }

    public String getL3LabelName() {
        return this.l3LabelName;
    }

    public void setL3LabelName(String l3LabelName) {
        this.l3LabelName = l3LabelName;
    }

    public String getRankingTypeId() {
        return this.rankingTypeId;
    }

    public void setRankingTypeId(String rankingTypeId) {
        this.rankingTypeId = rankingTypeId;
    }

    public List<CiGroupAttrRel> getCiGroupAttrRelList() {
        return this.ciGroupAttrRelList;
    }

    public void setCiGroupAttrRelList(List<CiGroupAttrRel> ciGroupAttrRelList) {
        this.ciGroupAttrRelList = ciGroupAttrRelList;
    }

    public int getForwardType() {
        return this.forwardType;
    }

    public void setForwardType(int forwardType) {
        this.forwardType = forwardType;
    }

    public String getLabelSearchType() {
        return this.labelSearchType;
    }

    public void setLabelSearchType(String labelSearchType) {
        this.labelSearchType = labelSearchType;
    }

    public String getMessageSearchType() {
        return this.messageSearchType;
    }

    public void setMessageSearchType(String messageSearchType) {
        this.messageSearchType = messageSearchType;
    }

    public String getResourceShowType() {
        return this.resourceShowType;
    }

    public void setResourceShowType(String resourceShowType) {
        this.resourceShowType = resourceShowType;
    }

    public String getAlarmShowType() {
        return this.alarmShowType;
    }

    public void setAlarmShowType(String alarmShowType) {
        this.alarmShowType = alarmShowType;
    }

    public String getNoticeType() {
        return this.noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
