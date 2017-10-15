package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.*;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.localization.sichuan.util.PasswordUtil;
import com.ailk.biapp.ci.model.ImportColumn;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.model.ReturnMsgModel;
import com.ailk.biapp.ci.service.*;
import com.ailk.biapp.ci.util.*;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.dimtable.service.impl.DimTableServiceImpl;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
@Scope("prototype")
public class CustomersManagerAction extends CIBaseAction {
    private Logger log = Logger.getLogger(CustomersManagerAction.class);
    private Pager pager;
    private String createTime;
    private String customGroupId;
    private String initOrSearch;
    private String customGroupName;
    private String dataDate;
    private CiCustomListInfo ciCustomListInfo;
    private CiCustomGroupInfo ciCustomGroupInfo;
    private List<CiCustomGroupInfo> ciCustomGroupInfoList;
    private List<CiCustomSourceRel> ciCustomSourceRelList;
    private List<CiTemplateInfo> ciTemplateInfoList;
    private List<CiLabelInfo> ciLabelInfoList;
    private List<CiLabelInfo> vertLabelAttrList;
    private List<CiGroupAttrRel> ciGroupAttrRelList;
    private List<CiGroupAttrRel> ciGroupAttrRelListModify;
    private List<CiGroupAttrRel> sortAttrs;
    private List<CiLabelRule> ciLabelRuleList;
    private List<CiSysInfo> ciSysInfoList;
    private CiCustomFileRel customFile;
    private String isSaveTemplate;
    private CiTemplateInfo ciTemplateInfo;
    private CiCustomPushReq ciCustomPushReq;
    private String pushCycle;
    private List<String> sysIds;
    private Integer isAllDisposable;
    private List<CiSysInfo> sysInfoCycleList;
    private String isPush;
    private List<CiCustomGroupPushCycle> isPushCycleList;
    private Integer havePushFun;
    private Integer exploreFromModuleFlag;
    private Integer saveCustomersFlag;
    private String isLastLabel;
    private String labelName;
    private int page;
    private int rows;
    private Integer labelId;
    private String attrVal;
    private String attrName;
    private String columnId;
    private String otherAttrs;
    private String sortAttrNum;
    private CiSysInfo sysinfo;
    private Boolean isCustomGroupMarket;
    private List<DimScene> dimScenes;
    private List<DimCustomCreateType> customCreateTypes;
    private List<DimCustomDataStatus> customDataStatus;
    private Map<String, String> cityMap = new LinkedHashMap();
    private Long totalCustomNum;
    private List<ImportColumn> importColumnList;
    private String isSysRecommendCustomsFlag;
    private List<CiCustomSceneRel> ciCustomSceneRelList;
    private int rankingListType = 1;
    private String refreshType;
    private Map<String, Object> resultMap;
    private List<CiCustomListExeInfo> ciCustomListExeInfos;
    @Autowired
    private ICustomersManagerService customersService;
    @Autowired
    private ICiCustomFileRelService loadCustomGroupFileService;
    @Autowired
    private ICiTemplateInfoService ciTemplateInfoService;
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;
    @Autowired
    private ICiGroupAttrRelService ciGroupAttrRelService;
    @Autowired
    private ICiCustomSceneService ciCustomSceneService;
    private String sceneIds;
    private String parentId;
    private String customListDate;
    private String listTableName;
    private Integer level;
    private Integer labelOrCustom;
    private Integer oldIsHasList;
    private boolean flagConstraint;
    private String ipAdd;

    public CustomersManagerAction() {
    }

    public String getIpAdd() {
        return this.ipAdd;
    }

    public void setIpAdd(String ipAdd) {
        this.ipAdd = ipAdd;
    }

    public String search() throws Exception {
        String createCityId;
        String customerAnalysisMenu;
        if ("init".equals(this.initOrSearch)) {
            Cookie[] var8 = this.getRequest().getCookies();
            boolean var9 = false;
            createCityId = ServiceConstants.COOKIE_NAME_FOR_IMPORT + "_" + this.getUserId();

            for (int var10 = 0; var10 < var8.length; ++var10) {
                Cookie var12 = var8[var10];
                if (createCityId.equals(var12.getName())) {
                    var9 = true;
                    customerAnalysisMenu = var12.getValue();
                    int nums_value_int = Integer.parseInt(customerAnalysisMenu);
                    ++nums_value_int;
                    var12.setValue(String.valueOf(nums_value_int));
                    var12.setMaxAge(31536000);
                    this.getResponse().addCookie(var12);
                }
            }

            if (!var9) {
                Cookie var11 = new Cookie(ServiceConstants.COOKIE_NAME_FOR_IMPORT + "_" + this.getUserId(), "1");
                var11.setMaxAge(31536000);
                this.getResponse().addCookie(var11);
            }

            CacheBase var13 = CacheBase.getInstance();
            this.dimScenes = (List<DimScene>) var13.getObjectList("DIM_SCENE");
            this.customCreateTypes = (List<DimCustomCreateType>) var13.getObjectList("DIM_CUSTOM_CREATE_TYPE");
            this.customDataStatus = (List<DimCustomDataStatus>) var13.getObjectList("DIM_CUSTOM_DATA_STATUS");
            return "init";
        } else {
            List ciSysInfoRightList = this.customersService.queryCiSysInfo(1);
            if (ciSysInfoRightList != null && ciSysInfoRightList.size() > 0) {
                this.havePushFun = Integer.valueOf(1);
            } else {
                this.havePushFun = Integer.valueOf(0);
            }

            if (this.ciCustomGroupInfo == null) {
                this.ciCustomGroupInfo = new CiCustomGroupInfo();
            }

            this.ciCustomGroupInfo.setSceneId(StringUtil.isEmpty(this.ciCustomGroupInfo.getSceneId()) ? "" : this.ciCustomGroupInfo.getSceneId() + ",");
            if (StringUtils.isBlank(this.ciCustomGroupInfo.getDataDate())) {
                this.newLabelMonth = this.getNewLabelMonth();
            }

            if (this.pager == null) {
                this.pager = new Pager();
            }

            String userId = this.getUserId();
            createCityId = PrivilegeServiceUtil.getCityIdFromSession();
            String root = Configure.getInstance().getProperty("CENTER_CITYID");
            if (!PrivilegeServiceUtil.isAdminUser(userId)) {
                this.ciCustomGroupInfo.setCreateUserId(userId);
                this.ciCustomGroupInfo.setCreateCityId(createCityId);
            } else if (!createCityId.equals(root)) {
                this.ciCustomGroupInfo.setCreateCityId(createCityId);
            }

            this.ciCustomGroupInfo.setIsMyCustom("true");
            this.ciCustomGroupInfo.setUserId(userId);
            Pager p2 = new Pager((long) this.customersService.queryCustomersCount(this.ciCustomGroupInfo));  //获取查询出的结果行数传入pager对象
            this.pager.setTotalPage(p2.getTotalPage());     //设置总页数
            this.pager.setTotalSize(p2.getTotalSize());     //设置总大小 -- > 每页大小被设置为固定10（pageSize）
            this.pager = this.pager.pagerFlip();
            this.pager.setResult(this.customersService.queryCustomersList(this.pager, this.newLabelMonth, this.ciCustomGroupInfo));
            customerAnalysisMenu = Configure.getInstance().getProperty("CUSTOMER_ANALYSIS");    //返回true（字符串）
            this.ciCustomGroupInfo.setCustomerAnalysisMenu(customerAnalysisMenu);
            this.log.info("Exit CustomersManagerAction.search() method");
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_SELECT", "", StringUtil.isEmpty(this.ciCustomGroupInfo.getCustomGroupName()) ? "" : this.ciCustomGroupInfo.getCustomGroupName(), "查询我的客户群名称中含有【" + (StringUtil.isEmpty(this.ciCustomGroupInfo.getCustomGroupName()) ? "" : this.ciCustomGroupInfo.getCustomGroupName()) + "】的记录成功", OperResultEnum.Success, LogLevelEnum.Medium);
            return "search";
        }
    }

    public String customersSearch() {
        return "customersSearch";
    }

    public void findCustomTotalNum() throws Exception {
        if (this.ciCustomGroupInfo == null) {
            this.ciCustomGroupInfo = new CiCustomGroupInfo();
        }

        if (this.pager == null) {
            this.pager = new Pager();
        }

        boolean success = false;
        HashMap result = new HashMap();
        String msg = "";
        int totalPage = 0;
        String userId = this.getUserId();
        String createCityId = PrivilegeServiceUtil.getCityIdFromSession();
        String root = Configure.getInstance().getProperty("CENTER_CITYID");
        if (!PrivilegeServiceUtil.isAdminUser(userId)) {
            this.ciCustomGroupInfo.setCreateUserId(userId);
            this.ciCustomGroupInfo.setCreateCityId(createCityId);
        } else if (!createCityId.equals(root)) {
            this.ciCustomGroupInfo.setCreateCityId(createCityId);
        }

        if (StringUtil.isNotEmpty(this.ciCustomGroupInfo.getIsMyCustom()) && this.ciCustomGroupInfo.getIsMyCustom().equals("true")) {
            this.ciCustomGroupInfo.setUserId(this.getUserId());
        }

        if (StringUtil.isNotEmpty(this.isSysRecommendCustomsFlag) && this.isSysRecommendCustomsFlag.equals("true")) {
            this.ciCustomGroupInfo.setIsPrivate(Integer.valueOf(0));
        }

        try {
            byte response = 5;
            int e = this.customersService.queryCustomersCount(this.ciCustomGroupInfo);
            if (StringUtil.isNotEmpty(this.isSysRecommendCustomsFlag) && this.isSysRecommendCustomsFlag.equals("true") && e >= ServiceConstants.SHOW_RECOMMEND_CUSTOMS_NUM) {
                e = ServiceConstants.SHOW_RECOMMEND_CUSTOMS_NUM;
            }

            this.totalCustomNum = Long.valueOf((long) e);
            totalPage = (int) Math.ceil((double) e / (double) response);
            this.pager.setTotalPage(totalPage);
            this.pager.setPageSize(response);
            success = true;
            String name = this.ciCustomGroupInfo.getCustomGroupName();
            CILogServiceUtil.getLogServiceInstance().log("COC_HOME_CUTOMER_SELECT", "", name, "客户群列表查询成功，查询条件：【" + (StringUtil.isEmpty(name) ? "无" : "客户群名称：" + name) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var12) {
            msg = "查询客户群列表总页数错误";
            this.log.error(msg, var12);
            success = false;
        }

        result.put("success", Boolean.valueOf(success));
        if (success) {
            result.put("totalPage", Integer.valueOf(totalPage));
            result.put("totalSize", this.totalCustomNum);
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
    }

    public String customersList() throws Exception {
        boolean totalPage = false;
        if (this.ciCustomGroupInfo == null) {
            this.ciCustomGroupInfo = new CiCustomGroupInfo();
        }

        if (StringUtils.isBlank(this.ciCustomGroupInfo.getDataDate())) {
            this.newLabelMonth = this.getNewLabelMonth();
        }

        String userId = this.getUserId();
        String createCityId = PrivilegeServiceUtil.getCityIdFromSession();
        String root = Configure.getInstance().getProperty("CENTER_CITYID");
        if (!PrivilegeServiceUtil.isAdminUser(userId)) {
            this.ciCustomGroupInfo.setCreateUserId(userId);
            this.ciCustomGroupInfo.setCreateCityId(createCityId);
        } else if (!createCityId.equals(root)) {
            this.ciCustomGroupInfo.setCreateCityId(createCityId);
        }

        if (StringUtil.isNotEmpty(this.ciCustomGroupInfo.getIsMyCustom()) && this.ciCustomGroupInfo.getIsMyCustom().equals("true")) {
            this.ciCustomGroupInfo.setUserId(this.getUserId());
        }

        if (StringUtil.isNotEmpty(this.isSysRecommendCustomsFlag) && this.isSysRecommendCustomsFlag.equals("true")) {
            this.ciCustomGroupInfo.setIsPrivate(Integer.valueOf(0));
        }

        if (this.pager == null) {
            this.pager = new Pager((long) this.customersService.queryCustomersCount(this.ciCustomGroupInfo));
            byte currentPage = 10;
            this.pager.setTotalPage((int) Math.ceil((double) this.pager.getTotalSize() / (double) currentPage));
        }

        int currentPage1;
        if (StringUtil.isNotEmpty(this.isSysRecommendCustomsFlag) && this.isSysRecommendCustomsFlag.equals("true")) {
            currentPage1 = ServiceConstants.SHOW_RECOMMEND_CUSTOMS_NUM;
            this.pager.setTotalSize((long) currentPage1);
        }

        currentPage1 = Integer.valueOf(this.getRequest().getParameter("currentPage") == null ? "1" : this.getRequest().getParameter("currentPage")).intValue();
        this.pager.setPageNum(currentPage1);
        int totalPage1 = this.pager.getTotalPage();
        if (currentPage1 <= totalPage1) {
            byte ciSysInfoRightList = 5;
            this.pager.setPageSize(ciSysInfoRightList);
            this.pager.setTotalPage(totalPage1);
            this.ciCustomGroupInfoList = this.customersService.queryCustomersList(this.pager, this.newLabelMonth, this.ciCustomGroupInfo);
            if (this.ciCustomGroupInfoList != null) {
                Iterator var8 = this.ciCustomGroupInfoList.iterator();

                label95:
                while (true) {
                    CiCustomGroupInfo info;
                    do {
                        do {
                            if (!var8.hasNext()) {
                                break label95;
                            }

                            info = (CiCustomGroupInfo) var8.next();
                            String offsetStr = this.customersService.queryCustomerOffsetStr(info);
                            info.setKpiDiffRule((info.getKpiDiffRule() == null ? "" : info.getKpiDiffRule()) + offsetStr);
                            if (info.getMonthLabelDate() != null && !info.getMonthLabelDate().equals("")) {
                                info.setMonthLabelDate(DateUtil.string2StringFormat(info.getMonthLabelDate(), "yyyyMM", "yyyy-MM"));
                            }

                            if (info.getDayLabelDate() != null && !info.getDayLabelDate().equals("")) {
                                info.setDayLabelDate(DateUtil.string2StringFormat(info.getDayLabelDate(), "yyyyMMdd", "yyyy-MM-dd"));
                            }
                        } while (info.getMonthLabelDate() != null && !info.getMonthLabelDate().equals(""));
                    } while (info.getDayLabelDate() != null && !info.getDayLabelDate().equals(""));

                    if (info.getDataDate() == null) {
                        info.setDataDateStr("");
                    } else if (info.getDataDate().length() > 6) {
                        info.setDataDateStr(DateUtil.string2StringFormat(info.getDataDate(), "yyyyMMdd", "yyyy-MM-dd"));
                    } else {
                        info.setDataDateStr(DateUtil.string2StringFormat(info.getDataDate(), "yyyyMM", "yyyy-MM"));
                    }
                }
            }
        }

        this.dataDate = this.getNewLabelMonth();
        List ciSysInfoRightList1 = this.customersService.queryCiSysInfo(1);
        if (ciSysInfoRightList1 != null && ciSysInfoRightList1.size() > 0) {
            this.havePushFun = Integer.valueOf(1);
        } else {
            this.havePushFun = Integer.valueOf(0);
        }

        return "customGroupMarket";
    }

    public String sysHotCustomRankingList() throws Exception {
        this.rankingListType = 2;
        if (this.ciCustomGroupInfo == null) {
            this.ciCustomGroupInfo = new CiCustomGroupInfo();
        }

        if (StringUtils.isBlank(this.ciCustomGroupInfo.getDataDate())) {
            this.newLabelMonth = this.getNewLabelMonth();
        }

        String userId = this.getUserId();
        String createCityId = PrivilegeServiceUtil.getCityIdFromSession();
        String root = Configure.getInstance().getProperty("CENTER_CITYID");
        if (!PrivilegeServiceUtil.isAdminUser(userId)) {
            this.ciCustomGroupInfo.setCreateUserId(userId);
            this.ciCustomGroupInfo.setCreateCityId(createCityId);
        } else if (!createCityId.equals(root)) {
            this.ciCustomGroupInfo.setCreateCityId(createCityId);
        }

        this.ciCustomGroupInfo.setIsPrivate(Integer.valueOf(0));
        if (this.pager == null) {
            this.pager = new Pager(1L);
        }

        this.pager.setPageNum(1);
        this.pager.setPageSize(ServiceConstants.SHOW_RECOMMEND_CUSTOMS_NUM);
        this.pager.setTotalPage(1);
        this.pager.setOrder("desc");
        this.pager.setOrderBy("USECOUNT_HOT");
        this.ciCustomGroupInfoList = this.customersService.queryCustomersList(this.pager, this.newLabelMonth, this.ciCustomGroupInfo);
        this.dataDate = this.getNewLabelMonth();
        this.pager.setResult(this.ciCustomGroupInfoList);
        return "sysHotCustomRankingList";
    }

    public void delete() throws Exception {
        boolean success = false;
        String msg = "";
        String msgFailed = "";
        int successCount = 0;
        int failedCount = 0;
        HashMap returnMsg = new HashMap();

        try {
            if (this.ciCustomGroupInfo.getCustomGroupId().contains(",")) {
                String[] response = this.ciCustomGroupInfo.getCustomGroupId().split(",");

                for (int e = 0; e < response.length; ++e) {
                    if (StringUtils.isNotBlank(response[e])) {
                        CiCustomGroupInfo name = this.customersService.queryCiCustomGroupInfo(response[e]);
                        if (name == null || !name.getCreateUserId().equals(this.getUserId()) && !PrivilegeServiceUtil.isAdminUser(this.getUserId())) {
                            success = false;
                            ++failedCount;
                            msgFailed = msgFailed + "客户群id:\'" + this.ciCustomGroupInfo.getCustomGroupId();
                        } else {
                            this.customersService.deleteUserAttentionCustom(name.getCustomGroupId(), this.getUserId());
                            this.customersService.deleteCiCustomGroupInfo(name);
                            CIAlarmServiceUtil.deleteAlarmThresholdByBusiId("CustomersAlarm", response[e]);
                            success = true;
                            msg = "删除成功";
                            ++successCount;
                        }
                    }
                }

                if (failedCount > 0) {
                    msgFailed = msgFailed + "\'无法删除。";
                    this.log.debug("删除失败的数量：" + failedCount + "," + msgFailed);
                }

                if (successCount > 0) {
                    msg = "成功删除" + successCount + "个客户群";
                    CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_DELETE", "-1", "批量删除客户群", "客户群删除成功【" + msg + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                }
            } else {
                CiCustomGroupInfo var12 = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupInfo.getCustomGroupId());
                String var14 = var12.getCustomGroupId();
                String var15 = var12.getCustomGroupName();
                if (var12 == null || !var12.getCreateUserId().equals(this.getUserId()) && !PrivilegeServiceUtil.isAdminUser(this.getUserId())) {
                    success = false;
                    msg = "删除失败，客户群id\'" + this.ciCustomGroupInfo.getCustomGroupId() + "\'不存在。";
                    CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_DELETE", var14, var15, "客户群删除失败【客户群ID：" + var12.getCustomGroupId() + "客户群名称：" + var12.getCustomGroupName() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
                } else {
                    this.customersService.deleteUserAttentionCustom(var14, this.getUserId());
                    this.customersService.deleteCiCustomGroupInfo(var12);
                    CIAlarmServiceUtil.deleteAlarmThresholdByBusiId("CustomersAlarm", var14);
                    success = true;
                    msg = "删除成功";
                    CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_DELETE", var14, var15, "客户群删除成功【客户群ID：" + var12.getCustomGroupId() + "客户群名称：" + var12.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                }
            }
        } catch (Exception var11) {
            this.log.error("删除客户群失败，customGroupId = " + this.ciCustomGroupInfo.getCustomGroupId());
            success = false;
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_DELETE", "-1", "客户群删除失败", "客户群删除失败【客户群ID：" + this.ciCustomGroupInfo.getCustomGroupId() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
            msg = var11.getMessage();
        }

        returnMsg.put("msg", msg);
        returnMsg.put("success", Boolean.valueOf(success));
        HttpServletResponse var13 = this.getResponse();

        try {
            this.sendJson(var13, JsonUtil.toJson(returnMsg));
        } catch (Exception var10) {
            this.log.error("发送json串异常", var10);
            throw new CIServiceException(var10);
        }
    }

    public void saveCustomer() throws Exception {
        if (this.ciCustomGroupInfo.getIsHasList().intValue() == 0) {
            this.ciCustomGroupInfo.setUpdateCycle(Integer.valueOf(4));
        }

        String LOG_CUSTOMER_MANAGE_ADD = "";
        if (this.saveCustomersFlag != null && 1 == this.saveCustomersFlag.intValue()) {
            LOG_CUSTOMER_MANAGE_ADD = "COC_CUSTOMER_MANAGE_ADD_INDEX_LABEL";
        } else if (this.saveCustomersFlag != null && 3 == this.saveCustomersFlag.intValue()) {
            LOG_CUSTOMER_MANAGE_ADD = "COC_CUSTOMER_MANAGE_ADD_INDEX_PRODUCT";
        } else if (this.saveCustomersFlag != null && 4 == this.saveCustomersFlag.intValue()) {
            LOG_CUSTOMER_MANAGE_ADD = "COC_CUSTOMER_MANAGE_ADD_LABEL_ANALYSIS";
        } else if (this.saveCustomersFlag != null && 5 == this.saveCustomersFlag.intValue()) {
            LOG_CUSTOMER_MANAGE_ADD = "COC_CUSTOMER_MANAGE_ADD_MANAGE";
        }

        Map returnMap = null;
        Map returnTemplateMap = null;

        try {
            this.ciLabelRuleList = this.resolveCustomRule();
            String response = this.getUserId();
            if (StringUtil.isNotEmpty(this.ciCustomGroupInfo.getIsPrivate())) {
                if (this.ciCustomGroupInfo.getIsPrivate().intValue() == 1) {
                    returnMap = this.customersService.isNameExist(this.ciCustomGroupInfo, response);
                }

                if (this.ciCustomGroupInfo.getIsPrivate().intValue() == 0) {
                    returnMap = this.customersService.isShareNameExist(this.ciCustomGroupInfo, response, (String) null);
                    if (StringUtil.isNotEmpty(returnMap.get("msg"))) {
                        returnMap.put("cmsg", returnMap.get("msg"));
                    }
                }
            } else {
                this.ciCustomGroupInfo.setIsPrivate(Integer.valueOf(1));
                returnMap = this.customersService.isNameExist(this.ciCustomGroupInfo, response);
            }

            if (StringUtil.isNotEmpty(this.isSaveTemplate)) {
                returnTemplateMap = this.ciTemplateInfoService.isNameExist(this.ciTemplateInfo, response);
                returnMap.put("success", Boolean.valueOf(((Boolean) returnTemplateMap.get("success")).booleanValue() && ((Boolean) returnMap.get("success")).booleanValue()));
                returnMap.put("msg", returnTemplateMap.get("msg"));
            }

            ArrayList var25 = new ArrayList();
            String dbType = Configure.getInstance().getProperty("CI_BACK_DBTYPE");
            int columnNameIndex = 0;
            ArrayList sceneList = new ArrayList();
            if (this.ciGroupAttrRelList != null && this.ciGroupAttrRelList.size() > 0) {
                for (int cache = 0; cache < this.ciGroupAttrRelList.size(); ++cache) {
                    CiGroupAttrRel msg = (CiGroupAttrRel) this.ciGroupAttrRelList.get(cache);
                    if (!StringUtil.isEmpty(msg.getId().getAttrCol())) {
                        if (StringUtil.isEmpty(msg.getAttrColType())) {
                            if ("ORACLE".equalsIgnoreCase(dbType)) {
                                msg.setAttrColType("VARCHAR2(512)");
                            } else {
                                msg.setAttrColType("VARCHAR(512)");
                            }
                        }

                        msg.setLabelOrCustomColumn(msg.getId().getAttrCol());
                        msg.getId().setAttrCol(CiUtil.genCustomAttrColumnName(columnNameIndex));
                        msg.setAttrSource(3);
                        msg.setIsVerticalAttr(0);
                        msg.setStatus(Integer.valueOf(0));
                        ++columnNameIndex;
                        var25.add(msg);
                    }
                }
            }

            if (this.vertLabelAttrList != null) {
                this.ciLabelInfoList.addAll(this.vertLabelAttrList);
            }

            CacheBase var26 = CacheBase.getInstance();
            if (this.ciLabelInfoList != null && this.ciLabelInfoList.size() > 0) {
                for (int var27 = 0; var27 < this.ciLabelInfoList.size(); ++var27) {
                    CiLabelInfo i = (CiLabelInfo) this.ciLabelInfoList.get(var27);
                    if (i != null) {
                        CiGroupAttrRel item = new CiGroupAttrRel();
                        CiGroupAttrRelId relId = new CiGroupAttrRelId();
                        String attrVal = i.getAttrVal();
                        String columnId = "";
                        String columnCnName = "";
                        if (StringUtil.isNotEmpty(i.getColumnId())) {
                            columnId = i.getColumnId();
                        }

                        if (StringUtil.isNotEmpty(i.getColumnCnName())) {
                            columnCnName = i.getColumnCnName();
                        }

                        i = var26.getEffectiveLabel(String.valueOf(i.getLabelId()));
                        if (i != null) {
                            if (StringUtil.isNotEmpty(columnId)) {
                                item.setLabelOrCustomColumn(columnId);
                            }

                            if (i.getLabelTypeId() != null && i.getLabelTypeId().intValue() == 8) {
                                item.setIsVerticalAttr(1);
                            } else {
                                item.setIsVerticalAttr(0);
                            }

                            String dataType = "";
                            if (8 != i.getLabelTypeId().intValue()) {
                                if (5 != i.getLabelTypeId().intValue()) {
                                    dataType = i.getCiLabelExtInfo().getCiMdaSysTableColumn().getDataType();
                                }
                            } else if (8 == i.getLabelTypeId().intValue()) {
                                Set attrValArr = i.getCiLabelExtInfo().getCiLabelVerticalColumnRels();
                                Iterator var20 = attrValArr.iterator();

                                while (var20.hasNext()) {
                                    CiLabelVerticalColumnRel e1 = (CiLabelVerticalColumnRel) var20.next();
                                    if (columnId.equals(e1.getCiMdaSysTableColumn().getColumnId().toString())) {
                                        if (5 != e1.getLabelTypeId().intValue()) {
                                            dataType = e1.getCiMdaSysTableColumn().getDataType();
                                        }
                                        break;
                                    }
                                }
                            }

                            if (StringUtil.isNotEmpty(dataType)) {
                                item.setAttrColType(dataType);
                            } else if ("ORACLE".equalsIgnoreCase(dbType)) {
                                item.setAttrColType("VARCHAR2(512)");
                            } else {
                                item.setAttrColType("VARCHAR(512)");
                            }

                            if (StringUtil.isNotEmpty(columnCnName)) {
                                item.setAttrColName(columnCnName);
                            } else {
                                item.setAttrColName(i.getLabelName());
                            }

                            item.setAttrSource(2);
                            item.setLabelOrCustomId(String.valueOf(i.getLabelId()));
                            relId.setAttrCol(CiUtil.genCustomAttrColumnName(columnNameIndex));
                            ++columnNameIndex;
                            item.setId(relId);
                            item.setStatus(Integer.valueOf(0));
                            if (StringUtils.isNotEmpty(attrVal)) {
                                String[] var32 = attrVal.split(",");
                                if (var32.length > 20) {
                                    try {
                                        String var33 = this.customersService.createValueTableName();
                                        item.setTableName(var33);
                                        this.customersService.addExactValueToTable(attrVal, var33);
                                        item.setAttrVal((String) null);
                                    } catch (Exception var22) {
                                        this.log.error("枚举值存于数据库表中失败", var22);
                                    }
                                } else {
                                    item.setAttrVal(attrVal);
                                }
                            }

                            var25.add(item);
                        }
                    }
                }
            }

            this.setSortGroupAttrs(var25);
            if (StringUtil.isNotEmpty(this.sceneIds)) {
                String[] var28 = this.sceneIds.split(",");

                for (int var30 = 0; var30 < var28.length; ++var30) {
                    if (StringUtil.isNotEmpty(var28[var30].trim())) {
                        CiCustomSceneRel var31 = new CiCustomSceneRel();
                        var31.setId(new CiCustomSceneRelId());
                        var31.getId().setSceneId(var28[var30].trim());
                        var31.getId().setUserId(response);
                        var31.getId().setCustomGroupId(this.ciCustomGroupInfo.getCustomGroupId());
                        var31.setStatus(1);
                        sceneList.add(var31);
                    }
                }

                this.ciCustomGroupInfo.setSceneList(sceneList);
            }

            if (((Boolean) returnMap.get("success")).booleanValue()) {
                if (!StringUtil.isNotEmpty(this.isSaveTemplate)) {
                    this.customersService.addCiCustomGroupInfo(this.ciCustomGroupInfo, this.ciLabelRuleList, this.ciCustomSourceRelList, (CiTemplateInfo) null, response, false, var25);
                } else {
                    this.ciTemplateInfo.setIsPrivate(Integer.valueOf(1));
                    if (this.ciCustomGroupInfo.getSceneId() != null || !"".equals(this.ciCustomGroupInfo.getSceneId())) {
                        this.ciTemplateInfo.setSceneId(this.ciCustomGroupInfo.getSceneId());
                    }

                    this.customersService.addCiCustomGroupInfo(this.ciCustomGroupInfo, this.ciLabelRuleList, this.ciCustomSourceRelList, this.ciTemplateInfo, response, true, var25);
                }

                String var29 = "创建成功！";
                returnMap.put("cmsg", var29);
                returnMap.put("customGroupId", this.ciCustomGroupInfo.getCustomGroupId());
                returnMap.put("isHasList", this.ciCustomGroupInfo.getIsHasList());
                CILogServiceUtil.getLogServiceInstance().log(LOG_CUSTOMER_MANAGE_ADD, this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "创建客户群【" + this.ciCustomGroupInfo.getCustomGroupName() + "】成功", OperResultEnum.Success, LogLevelEnum.Medium);
            }
        } catch (Exception var23) {
            String e = "创建客户群";
            this.log.error(e + " : " + this.ciCustomGroupInfo.getCustomGroupName() + " 失败", var23);
            returnMap.put("cmsg", e + "失败");
            returnMap.put("success", Boolean.valueOf(false));
            CILogServiceUtil.getLogServiceInstance().log(LOG_CUSTOMER_MANAGE_ADD, "-1", this.ciCustomGroupInfo.getCustomGroupName(), "创建客户群【" + this.ciCustomGroupInfo.getCustomGroupName() + "】失败", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        HttpServletResponse var24 = this.getResponse();

        try {
            this.sendJson(var24, JsonUtil.toJson(returnMap));
        } catch (Exception var21) {
            this.log.error("发送json串异常", var21);
            throw new CIServiceException(var21);
        }
    }

    private void setSortGroupAttrs(List<CiGroupAttrRel> attrList) {
        if (this.sortAttrs != null) {
            for (int i = 0; i < this.sortAttrs.size(); ++i) {
                CiGroupAttrRel sortAttr = (CiGroupAttrRel) this.sortAttrs.get(i);
                Integer attrSource = Integer.valueOf(sortAttr.getAttrSource());
                if (attrSource.intValue() == 3) {
                    for (int var8 = 0; var8 < attrList.size(); ++var8) {
                        CiGroupAttrRel var9 = (CiGroupAttrRel) attrList.get(var8);
                        if (sortAttr.getLabelOrCustomId().equals(var9.getLabelOrCustomId()) && sortAttr.getLabelOrCustomColumn().equals(var9.getLabelOrCustomColumn())) {
                            var9.setSortNum(sortAttr.getSortNum());
                            var9.setSortType(sortAttr.getSortType());
                            break;
                        }
                    }
                } else {
                    Integer isVert = Integer.valueOf(sortAttr.getIsVerticalAttr());
                    int j;
                    CiGroupAttrRel attrRel;
                    if (isVert.intValue() == 1) {
                        for (j = 0; j < attrList.size(); ++j) {
                            attrRel = (CiGroupAttrRel) attrList.get(j);
                            if (sortAttr.getLabelOrCustomColumn().equals(attrRel.getLabelOrCustomColumn())) {
                                attrRel.setSortNum(sortAttr.getSortNum());
                                attrRel.setSortType(sortAttr.getSortType());
                                break;
                            }
                        }
                    } else {
                        for (j = 0; j < attrList.size(); ++j) {
                            attrRel = (CiGroupAttrRel) attrList.get(j);
                            if (sortAttr.getLabelOrCustomId().equals(attrRel.getLabelOrCustomId())) {
                                attrRel.setSortNum(sortAttr.getSortNum());
                                attrRel.setSortType(sortAttr.getSortType());
                                break;
                            }
                        }
                    }
                }
            }
        }

    }

    public void editCustomer() throws Exception {
        Map returnMap = null;
        Map returnTemplateMap = null;

        try {
            String response = this.getUserId();
            Integer var23 = this.ciCustomGroupInfo.getIsPrivate();
            if (var23.intValue() == 0) {
                returnMap = this.customersService.isShareNameExist(this.ciCustomGroupInfo, response, (String) null);
                if (returnMap.get("msg") != null && !"".equals(returnMap.get("msg"))) {
                    returnMap.put("cmsg", returnMap.get("msg"));
                }
            } else {
                returnMap = this.customersService.isNameExist(this.ciCustomGroupInfo, response);
            }

            if (StringUtil.isNotEmpty(this.isSaveTemplate)) {
                returnTemplateMap = this.ciTemplateInfoService.isNameExist(this.ciTemplateInfo, response);
                returnMap.put("success", Boolean.valueOf(((Boolean) returnTemplateMap.get("success")).booleanValue() && ((Boolean) returnMap.get("success")).booleanValue()));
                returnMap.put("msg", returnTemplateMap.get("msg"));
            }

            ArrayList attrList = new ArrayList();
            String dbType = Configure.getInstance().getProperty("CI_DBTYPE");
            int columnNameIndex = 0;
            int sceneList;
            if (this.ciGroupAttrRelList != null && this.ciGroupAttrRelList.size() > 0) {
                for (sceneList = 0; sceneList < this.ciGroupAttrRelList.size(); ++sceneList) {
                    CiGroupAttrRel ciCustomSceneRelList = (CiGroupAttrRel) this.ciGroupAttrRelList.get(sceneList);
                    if (!StringUtil.isEmpty(ciCustomSceneRelList.getId().getAttrCol())) {
                        ciCustomSceneRelList.getId().setCustomGroupId(this.ciCustomGroupInfo.getCustomGroupId());
                        if (StringUtil.isEmpty(ciCustomSceneRelList.getAttrColType())) {
                            if ("ORACLE".equalsIgnoreCase(dbType)) {
                                ciCustomSceneRelList.setAttrColType("VARCHAR2(512)");
                            } else {
                                ciCustomSceneRelList.setAttrColType("VARCHAR(512)");
                            }
                        }

                        ciCustomSceneRelList.setLabelOrCustomColumn(ciCustomSceneRelList.getId().getAttrCol());
                        ciCustomSceneRelList.getId().setAttrCol(CiUtil.genCustomAttrColumnName(columnNameIndex));
                        ciCustomSceneRelList.setAttrSource(3);
                        ciCustomSceneRelList.setIsVerticalAttr(0);
                        ciCustomSceneRelList.setStatus(Integer.valueOf(0));
                        ++columnNameIndex;
                        attrList.add(ciCustomSceneRelList);
                    }
                }
            }

            if (this.vertLabelAttrList != null) {
                this.ciLabelInfoList.addAll(this.vertLabelAttrList);
            }

            String msg;
            if (this.ciLabelInfoList != null && this.ciLabelInfoList.size() > 0) {
                for (sceneList = 0; sceneList < this.ciLabelInfoList.size(); ++sceneList) {
                    CiLabelInfo var25 = (CiLabelInfo) this.ciLabelInfoList.get(sceneList);
                    if (var25 != null) {
                        CiGroupAttrRel updateCustomGroupInfo = new CiGroupAttrRel();
                        CiGroupAttrRelId session = new CiGroupAttrRelId();
                        msg = var25.getAttrVal();
                        String ciCustomSceneRelChild = "";
                        String columnCnName = "";
                        if (StringUtil.isNotEmpty(var25.getColumnId())) {
                            ciCustomSceneRelChild = var25.getColumnId();
                        }

                        if (StringUtil.isNotEmpty(var25.getColumnCnName())) {
                            columnCnName = var25.getColumnCnName();
                        }

                        var25 = CacheBase.getInstance().getEffectiveLabel(String.valueOf(var25.getLabelId()));
                        if (var25 != null) {
                            if (StringUtil.isNotEmpty(ciCustomSceneRelChild)) {
                                updateCustomGroupInfo.setLabelOrCustomColumn(ciCustomSceneRelChild);
                            }

                            if (var25.getLabelTypeId() != null && var25.getLabelTypeId().intValue() == 8) {
                                updateCustomGroupInfo.setIsVerticalAttr(1);
                            } else {
                                updateCustomGroupInfo.setIsVerticalAttr(0);
                            }

                            if (StringUtil.isNotEmpty(columnCnName)) {
                                updateCustomGroupInfo.setAttrColName(columnCnName);
                            } else {
                                updateCustomGroupInfo.setAttrColName(var25.getLabelName());
                            }

                            updateCustomGroupInfo.setAttrColName(var25.getLabelName());
                            updateCustomGroupInfo.setAttrSource(2);
                            updateCustomGroupInfo.setLabelOrCustomId(String.valueOf(var25.getLabelId()));
                            String dataType = "";
                            if (8 != var25.getLabelTypeId().intValue()) {
                                if (5 != var25.getLabelTypeId().intValue()) {
                                    dataType = var25.getCiLabelExtInfo().getCiMdaSysTableColumn().getDataType();
                                }
                            } else if (8 == var25.getLabelTypeId().intValue()) {
                                Set attrValArr = var25.getCiLabelExtInfo().getCiLabelVerticalColumnRels();
                                Iterator var18 = attrValArr.iterator();

                                while (var18.hasNext()) {
                                    CiLabelVerticalColumnRel e1 = (CiLabelVerticalColumnRel) var18.next();
                                    if (ciCustomSceneRelChild.equals(e1.getCiMdaSysTableColumn().getColumnId().toString())) {
                                        if (5 != e1.getLabelTypeId().intValue()) {
                                            dataType = e1.getCiMdaSysTableColumn().getDataType();
                                        }
                                        break;
                                    }
                                }
                            }

                            if (StringUtil.isNotEmpty(dataType)) {
                                updateCustomGroupInfo.setAttrColType(dataType);
                            } else if ("ORACLE".equalsIgnoreCase(dbType)) {
                                updateCustomGroupInfo.setAttrColType("VARCHAR2(512)");
                            } else {
                                updateCustomGroupInfo.setAttrColType("VARCHAR(512)");
                            }

                            if (StringUtil.isNotEmpty(columnCnName)) {
                                updateCustomGroupInfo.setAttrColName(columnCnName);
                            } else {
                                updateCustomGroupInfo.setAttrColName(var25.getLabelName());
                            }

                            updateCustomGroupInfo.setAttrSource(2);
                            updateCustomGroupInfo.setLabelOrCustomId(String.valueOf(var25.getLabelId()));
                            session.setAttrCol(CiUtil.genCustomAttrColumnName(columnNameIndex));
                            ++columnNameIndex;
                            updateCustomGroupInfo.setId(session);
                            updateCustomGroupInfo.setStatus(Integer.valueOf(0));
                            if (StringUtils.isNotEmpty(msg)) {
                                String[] var38 = msg.split(",");
                                if (var38.length > 20) {
                                    try {
                                        String var39 = this.customersService.createValueTableName();
                                        updateCustomGroupInfo.setTableName(var39);
                                        this.customersService.addExactValueToTable(msg, var39);
                                        updateCustomGroupInfo.setAttrVal((String) null);
                                    } catch (Exception var20) {
                                        this.log.error("枚举值存于数据库表中失败", var20);
                                    }
                                } else {
                                    updateCustomGroupInfo.setAttrVal(msg);
                                }
                            }

                            attrList.add(updateCustomGroupInfo);
                        }
                    }
                }
            }

            this.setSortGroupAttrs(attrList);
            ArrayList var24 = new ArrayList();
            if (StringUtil.isNotEmpty(this.sceneIds)) {
                String[] var26 = this.sceneIds.split(",");

                for (int var27 = 0; var27 < var26.length; ++var27) {
                    if (StringUtil.isNotEmpty(var26[var27].trim())) {
                        CiCustomSceneRel var30 = new CiCustomSceneRel();
                        var30.setId(new CiCustomSceneRelId());
                        var30.getId().setSceneId(var26[var27].trim());
                        var30.getId().setUserId(this.ciCustomGroupInfo.getCreateUserId());
                        var30.getId().setCustomGroupId(this.ciCustomGroupInfo.getCustomGroupId());
                        var30.setStatus(1);
                        var24.add(var30);
                    }
                }
            }

            List var28 = this.ciCustomSceneService.queryCustomScenesListByCustomId(this.ciCustomGroupInfo.getCustomGroupId());
            Iterator var32 = var28.iterator();

            label170:
            while (true) {
                CiCustomSceneRel var29;
                do {
                    if (!var32.hasNext()) {
                        if (var24.size() > 0) {
                            this.ciCustomGroupInfo.setSceneList(var24);
                        }

                        if (((Boolean) returnMap.get("success")).booleanValue()) {
                            CiCustomGroupInfo var31 = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupInfo.getCustomGroupId());
                            var31.setOldIsHasList(var31.getIsHasList());
                            var31.setCustomGroupName(this.ciCustomGroupInfo.getCustomGroupName());
                            var31.setCustomGroupDesc(this.ciCustomGroupInfo.getCustomGroupDesc());
                            var31.setSceneList(this.ciCustomGroupInfo.getSceneList());
                            var31.setIsPrivate(this.ciCustomGroupInfo.getIsPrivate());
                            var31.setIsHasList(this.ciCustomGroupInfo.getIsHasList());
                            var31.setEndDate(this.ciCustomGroupInfo.getEndDate());
                            var31.setLabelOptRuleShow(this.ciCustomGroupInfo.getLabelOptRuleShow());
                            var31.setListCreateTime(this.ciCustomGroupInfo.getListCreateTime());
                            var31.setTacticsId(this.ciCustomGroupInfo.getTacticsId());
                            var31.setListMaxNum(this.ciCustomGroupInfo.getListMaxNum());
                            if (this.oldIsHasList != null && this.oldIsHasList.intValue() == 0 && this.ciCustomGroupInfo.getIsHasList().intValue() == 1) {
                                var31.setUpdateCycle(this.ciCustomGroupInfo.getUpdateCycle());
                                var31.setStartDate(this.ciCustomGroupInfo.getStartDate());
                                if (this.ciCustomGroupInfo.getUpdateCycle().intValue() == 3) {
                                    CacheBase var33 = CacheBase.getInstance();
                                    var31.setDataDate(var33.getNewLabelDay());
                                }
                            }

                            var31.setCreateUserId(response);
                            HttpSession var35 = this.getSession();
                            this.ciLabelRuleList = (List) var35.getAttribute("sessionModelList");
                            if (StringUtil.isNotEmpty(this.isSaveTemplate)) {
                                this.customersService.modifyCiCustomGroupInfo(var31, this.ciLabelRuleList, this.ciTemplateInfo, response, true, (List) null);
                            } else {
                                this.customersService.modifyCiCustomGroupInfo(var31, this.ciLabelRuleList, (CiTemplateInfo) null, response, false, attrList);
                            }

                            returnMap.put("customGroupId", this.ciCustomGroupInfo.getCustomGroupId());
                            returnMap.put("isHasList", this.ciCustomGroupInfo.getIsHasList());
                            msg = "修改成功！";
                            returnMap.put("cmsg", msg);
                            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_UPDATE", this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "修改客户群【" + this.ciCustomGroupInfo.getCustomGroupName() + "】成功", OperResultEnum.Success, LogLevelEnum.Medium);
                        }
                        break label170;
                    }

                    var29 = (CiCustomSceneRel) var32.next();
                } while (!StringUtil.isNotEmpty(var29.getId().getSceneId()));

                var29.setStatus(0);
                boolean var34 = false;
                Iterator var37 = var24.iterator();

                label167:
                {
                    CiCustomSceneRel var36;
                    do {
                        if (!var37.hasNext()) {
                            break label167;
                        }

                        var36 = (CiCustomSceneRel) var37.next();
                    }
                    while (!var29.getId().getSceneId().equals(var36.getId().getSceneId()) && var29.getId().getSceneId() != var36.getId().getSceneId());

                    var34 = true;
                }

                if (!var34) {
                    var24.add(var29);
                }
            }
        } catch (Exception var21) {
            String e = "修改客户群";
            this.log.error(e + " : " + this.ciCustomGroupInfo.getCustomGroupName() + " 失败", var21);
            returnMap.put("cmsg", e + "失败");
            returnMap.put("success", Boolean.valueOf(false));
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_UPDATE", this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "修改客户群【" + this.ciCustomGroupInfo.getCustomGroupName() + "】失败", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        HttpServletResponse var22 = this.getResponse();

        try {
            this.sendJson(var22, JsonUtil.toJson(returnMap));
        } catch (Exception var19) {
            this.log.error("发送json串异常", var19);
            throw new CIServiceException(var19);
        }
    }

    public void editCustomSimple() {
        Map returnMap = null;
        Map returnTemplateMap = null;

        try {
            String response = this.getUserId();
            Integer var15 = this.ciCustomGroupInfo.getIsPrivate();
            if (var15.intValue() == 0) {
                returnMap = this.customersService.isShareNameExist(this.ciCustomGroupInfo, response, (String) null);
                if (returnMap.get("msg") != null && !"".equals(returnMap.get("msg"))) {
                    returnMap.put("cmsg", returnMap.get("msg"));
                }
            } else {
                returnMap = this.customersService.isNameExist(this.ciCustomGroupInfo, response);
            }

            if (StringUtil.isNotEmpty(this.isSaveTemplate)) {
                returnTemplateMap = this.ciTemplateInfoService.isNameExist(this.ciTemplateInfo, response);
                returnMap.put("success", Boolean.valueOf(((Boolean) returnTemplateMap.get("success")).booleanValue() && ((Boolean) returnMap.get("success")).booleanValue()));
                returnMap.put("msg", returnTemplateMap.get("msg"));
            }

            ArrayList sceneList = new ArrayList();
            if (StringUtil.isNotEmpty(this.sceneIds)) {
                String[] ciCustomSceneRelList = this.sceneIds.split(",");

                for (int updateCustomGroupInfo = 0; updateCustomGroupInfo < ciCustomSceneRelList.length; ++updateCustomGroupInfo) {
                    if (StringUtil.isNotEmpty(ciCustomSceneRelList[updateCustomGroupInfo].trim())) {
                        CiCustomSceneRel session = new CiCustomSceneRel();
                        session.setId(new CiCustomSceneRelId());
                        session.getId().setSceneId(ciCustomSceneRelList[updateCustomGroupInfo].trim());
                        session.getId().setUserId(this.ciCustomGroupInfo.getCreateUserId());
                        session.getId().setCustomGroupId(this.ciCustomGroupInfo.getCustomGroupId());
                        session.setStatus(1);
                        sceneList.add(session);
                    }
                }
            }

            List var16 = this.ciCustomSceneService.queryCustomScenesListByCustomId(this.ciCustomGroupInfo.getCustomGroupId());
            Iterator var19 = var16.iterator();

            label80:
            while (true) {
                CiCustomSceneRel var17;
                do {
                    if (!var19.hasNext()) {
                        if (sceneList.size() > 0) {
                            this.ciCustomGroupInfo.setSceneList(sceneList);
                        }

                        if (((Boolean) returnMap.get("success")).booleanValue()) {
                            CiCustomGroupInfo var18 = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupInfo.getCustomGroupId());
                            var18.setCustomGroupName(this.ciCustomGroupInfo.getCustomGroupName());
                            var18.setCustomGroupDesc(this.ciCustomGroupInfo.getCustomGroupDesc());
                            var18.setSceneList(this.ciCustomGroupInfo.getSceneList());
                            var18.setCreateUserId(response);
                            HttpSession var20 = this.getSession();
                            this.ciLabelRuleList = (List) var20.getAttribute("sessionModelList");
                            if (StringUtil.isNotEmpty(this.isSaveTemplate)) {
                                this.customersService.modifyCiCustomGroupInfo(var18, this.ciLabelRuleList, this.ciTemplateInfo, response, true, (List) null);
                            } else {
                                this.customersService.modifyCiCustomGroupInfoSimple(var18);
                            }

                            String var21 = "修改成功！";
                            returnMap.put("cmsg", var21);
                            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_UPDATE", this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "修改客户群【" + this.ciCustomGroupInfo.getCustomGroupName() + "】成功", OperResultEnum.Success, LogLevelEnum.Medium);
                        }
                        break label80;
                    }

                    var17 = (CiCustomSceneRel) var19.next();
                } while (!StringUtil.isNotEmpty(var17.getId().getSceneId()));

                var17.setStatus(0);
                boolean msg = false;
                Iterator var11 = sceneList.iterator();

                label77:
                {
                    CiCustomSceneRel ciCustomSceneRelChild;
                    do {
                        if (!var11.hasNext()) {
                            break label77;
                        }

                        ciCustomSceneRelChild = (CiCustomSceneRel) var11.next();
                    }
                    while (!var17.getId().getSceneId().equals(ciCustomSceneRelChild.getId().getSceneId()) && var17.getId().getSceneId() != ciCustomSceneRelChild.getId().getSceneId());

                    msg = true;
                }

                if (!msg) {
                    sceneList.add(var17);
                }
            }
        } catch (Exception var13) {
            String e = "修改客户群";
            this.log.error(e + " : " + this.ciCustomGroupInfo.getCustomGroupName() + " 失败", var13);
            returnMap.put("cmsg", e + "失败");
            returnMap.put("success", Boolean.valueOf(false));
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_UPDATE", this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "修改客户群【" + this.ciCustomGroupInfo.getCustomGroupName() + "】失败", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        HttpServletResponse var14 = this.getResponse();

        try {
            this.sendJson(var14, JsonUtil.toJson(returnMap));
        } catch (Exception var12) {
            this.log.error("发送json串异常", var12);
            throw new CIServiceException(var12);
        }
    }

    public void shareCustom() throws Exception {
        Map returnMap = null;
        String message = "共享客户群失败";
        boolean success = false;

        try {
            String response = this.ciCustomGroupInfo.getCreateCityId();
            String e = this.getUserId();
            returnMap = this.customersService.isShareNameExist(this.ciCustomGroupInfo, e, response);
            String ciCustomGroupInfoId = this.ciCustomGroupInfo.getCustomGroupId();
            CiCustomGroupInfo info = this.customersService.queryCiCustomGroupInfo(ciCustomGroupInfoId);
            if (((Boolean) returnMap.get("success")).booleanValue()) {
                if (info.getStatus().intValue() != 0 && 2 != info.getDataStatus().intValue()) {
                    info.setIsPrivate(Integer.valueOf(0));
                    info.setCustomGroupName(this.ciCustomGroupInfo.getCustomGroupName());
                    info.setNewModifyTime(new Date());
                    this.customersService.modifyCiCustomGroupInfo(info, e);
                    success = true;
                } else {
                    success = false;
                    message = "不能分享已删除或者创建中的客户群！";
                }
            }
        } catch (Exception var9) {
            this.log.error(message + " : " + this.ciCustomGroupInfo.getCustomGroupName() + " 失败", var9);
            returnMap.put("msg", message);
        }

        returnMap.put("success", Boolean.valueOf(success));
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMap));
        } catch (Exception var8) {
            this.log.error("发送json串异常", var8);
            throw new CIServiceException(var8);
        }
    }

    public void findIfShare() throws Exception {
        Map returnMap = null;
        String message = "查询是否可以共享客户群失败";

        try {
            returnMap = this.customersService.isUsingCustom(this.ciCustomGroupInfo.getCustomGroupId());
        } catch (Exception var6) {
            this.log.error(message + " : " + this.ciCustomGroupInfo.getCustomGroupName() + " 失败", var6);
            returnMap.put("msg", message);
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(returnMap));
        } catch (Exception var5) {
            this.log.error("发送json串异常", var5);
            throw new CIServiceException(var5);
        }
    }

    public void cancelShareCustom() throws Exception {
        Map returnMap = null;
        String message = "取消共享客户群失败";
        boolean success = false;

        try {
            String response = this.ciCustomGroupInfo.getCustomGroupId();
            returnMap = this.customersService.isUsingCustom(response);
            CiCustomGroupInfo e = this.customersService.queryCiCustomGroupInfo(response);
            if (((Boolean) returnMap.get("success")).booleanValue()) {
                if (e.getStatus().intValue() != 0 && 2 != e.getDataStatus().intValue()) {
                    e.setIsPrivate(Integer.valueOf(1));
                    e.setNewModifyTime(new Date());
                    this.customersService.modifyCiCustomGroupInfo(e, this.userId);
                    success = true;
                } else {
                    success = false;
                    message = "不能取消分享已删除或者创建中的客户群！";
                }
            }
        } catch (Exception var7) {
            this.log.error(message + " : " + this.ciCustomGroupInfo.getCustomGroupName() + " 失败", var7);
            returnMap.put("msg", message);
        }

        returnMap.put("success", Boolean.valueOf(success));
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMap));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public String customerInfoList() throws Exception {
        CiCustomListInfo customListInfo = new CiCustomListInfo();
        customListInfo.setCustomGroupId(this.ciCustomGroupInfo.getCustomGroupId());
        if (this.pager == null) {
            this.pager = new Pager();
        }

        if (this.pager.getTotalSize() == 0L) {
            this.pager.setTotalSize((long) this.customersService.queryCustomerListCount(customListInfo));
        }

        this.pager = this.pager.pagerFlip();
        this.pager.setResult(this.customersService.queryCustomersListInfo(this.pager.getPageNum(), this.pager.getPageSize(), customListInfo));
        return null;
    }

    public void isNameExist() throws Exception {
        boolean success = false;

        try {
            String response = this.getUserId();
            String e = this.ciCustomGroupInfo.getCustomGroupName().trim();
            if (StringUtil.isNotEmpty(e) && StringUtil.isNotEmpty(response)) {
                if (PrivilegeServiceUtil.isAdminUser(response)) {
                    response = null;
                }

                success = true;
            }

            if (success) {
                Object createCityId = null;
                List list = null;
                if (StringUtil.isNotEmpty(this.ciCustomGroupInfo.getIsPrivate())) {
                    if (this.ciCustomGroupInfo.getIsPrivate().intValue() == 1) {
                        list = this.customersService.queryCiCustomGroupInfoListByName(e, response, (String) createCityId);
                    }

                    if (this.ciCustomGroupInfo.getIsPrivate().intValue() == 0) {
                        list = this.customersService.queryCiCustomGroupInfoListByShareName(e);
                    }
                } else {
                    list = this.customersService.queryCiCustomGroupInfoListByName(e, response, (String) createCityId);
                }

                if (list != null && list.size() > 0) {
                    CiCustomGroupInfo customGroupInfo = (CiCustomGroupInfo) list.get(0);
                    if (!customGroupInfo.getCustomGroupId().equals(this.ciCustomGroupInfo.getCustomGroupId())) {
                        success = false;
                    }
                }
            }
        } catch (Exception var8) {
            success = false;
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.log.debug(Boolean.valueOf(success));
            this.sendJson(response1, String.valueOf(success));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException(var7);
        }
    }

    public void isTemplateNameExist() throws Exception {
        boolean success = false;

        try {
            String response = this.getUserId();
            String e = this.ciTemplateInfo.getTemplateName().trim();
            if (!StringUtil.isEmpty(response) && !StringUtil.isEmpty(e)) {
                success = true;
            }

            Map returnTemplateMap = this.ciTemplateInfoService.isNameExist(this.ciTemplateInfo, response);
            success = ((Boolean) returnTemplateMap.get("success")).booleanValue();
        } catch (Exception var6) {
            success = false;
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.log.debug(Boolean.valueOf(success));
            this.sendJson(response1, String.valueOf(success));
        } catch (Exception var5) {
            this.log.error("发送json串异常", var5);
            throw new CIServiceException(var5);
        }
    }

    public String showRule() throws Exception {
        try {
            this.ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupInfo.getCustomGroupId());
            String e = this.customersService.queryCustomerOffsetStr(this.ciCustomGroupInfo);
            this.ciCustomGroupInfo.setKpiDiffRule((this.ciCustomGroupInfo.getKpiDiffRule() == null ? "" : this.ciCustomGroupInfo.getKpiDiffRule()) + e);
            return "showRule";
        } catch (Exception var3) {
            String message = "查看客户群完整规则失败";
            this.log.error(message + ": customGroupId = " + this.ciCustomGroupInfo.getCustomGroupId(), var3);
            throw new CIServiceException(message + ": customGroupId = " + this.ciCustomGroupInfo.getCustomGroupId(), var3);
        }
    }

    public void findCustomer() throws Exception {
        boolean success = false;
        HashMap returnMap = new HashMap();

        String e;
        try {
            this.ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupInfo.getCustomGroupId());
            if (this.ciCustomGroupInfo != null && StringUtil.isNotEmpty(this.ciCustomGroupInfo.getCreateTypeId()) && 7 == this.ciCustomGroupInfo.getCreateTypeId().intValue()) {
                this.customFile = this.loadCustomGroupFileService.queryCustomFileByCustomGroupId(this.ciCustomGroupInfo.getCustomGroupId());
            }

            IUser response = PrivilegeServiceUtil.getUserById(this.ciCustomGroupInfo.getCreateUserId());
            if (response != null) {
                this.ciCustomGroupInfo.setCreateUserName(response.getUsername());
            }

            e = this.customersService.queryCustomerOffsetStr(this.ciCustomGroupInfo);
            this.ciCustomGroupInfo.setKpiDiffRule((this.ciCustomGroupInfo.getKpiDiffRule() == null ? "" : this.ciCustomGroupInfo.getKpiDiffRule()) + e);
            success = true;
            returnMap.put("success", Boolean.valueOf(success));
            returnMap.put("customGroupInfo", this.ciCustomGroupInfo);
            CacheBase cache = CacheBase.getInstance();
            CopyOnWriteArrayList customCreateTypeList = cache.getObjectList("DIM_CUSTOM_CREATE_TYPE");
            CopyOnWriteArrayList customDataStatusList = cache.getObjectList("DIM_CUSTOM_DATA_STATUS");
            returnMap.put("customCreateType", customCreateTypeList);
            returnMap.put("customDataStatus", customDataStatusList);
            returnMap.put("customFile", this.customFile);
        } catch (Exception var9) {
            e = "查看客户群详情失败";
            this.log.error(e + ": customGroupId = " + this.ciCustomGroupInfo.getCustomGroupId(), var9);
            success = false;
            returnMap.put("success", Boolean.valueOf(success));
            returnMap.put("msg", e);
            throw new CIServiceException(e + ": customGroupId = " + this.ciCustomGroupInfo.getCustomGroupId(), var9);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            SimpleDateFormat e1 = new SimpleDateFormat("yyyy-MM-dd");
            this.sendJson(response1, JsonUtil.toJson(returnMap, e1));
        } catch (Exception var8) {
            this.log.error("发送json串异常", var8);
            throw new CIServiceException(var8);
        }
    }

    public String preEditCustomer() throws Exception {
        boolean success = false;
        String msg = "";
        CiCustomGroupInfo cg = null;
        Integer createTypeId = null;
        Integer updateCycleId = null;

        try {
            cg = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupInfo.getCustomGroupId());
            CacheBase e = CacheBase.getInstance();
            this.dimScenes = (List<DimScene>) e.getObjectList("DIM_SCENE");
            List ciCustomSceneRelList = cg.getSceneList();
            StringBuffer sceneIdsBuf = new StringBuffer("");
            Iterator var10 = ciCustomSceneRelList.iterator();

            while (var10.hasNext()) {
                CiCustomSceneRel _index = (CiCustomSceneRel) var10.next();
                if (StringUtil.isNotEmpty(_index) && _index.getStatus() == 1 && StringUtil.isNotEmpty(_index.getId().getSceneId())) {
                    String sceneId = _index.getId().getSceneId();
                    sceneIdsBuf.append(sceneId).append(",");
                }
            }

            if (StringUtil.isNotEmpty(sceneIdsBuf)) {
                int _index1 = sceneIdsBuf.lastIndexOf(",");
                this.sceneIds = sceneIdsBuf.substring(0, _index1);
            }

            if (cg != null && (cg.getCreateUserId().equals(this.getUserId()) || PrivilegeServiceUtil.isAdminUser(this.getUserId()))) {
                createTypeId = cg.getCreateTypeId();
                updateCycleId = cg.getUpdateCycle();
                if (createTypeId != null && updateCycleId != null) {
                    success = true;
                } else {
                    success = false;
                    msg = "没有查询到创建方式或者更新周期！";
                }
            } else {
                success = false;
                msg = "没有权限修改客户群！";
            }

            if (success && (cg.getStatus().intValue() == 0 || 2 == cg.getDataStatus().intValue())) {
                success = false;
                msg = "不能修改已删除的客户群！";
            }
        } catch (Exception var12) {
            this.log.error("预编辑客户群失败，customGroupId = " + this.ciCustomGroupInfo.getCustomGroupId(), var12);
            success = false;
            msg = var12.getMessage();
            throw new CIServiceException(msg, var12);
        }

        this.resultMap = new HashMap();
        this.resultMap.put("success", Boolean.valueOf(success));
        this.resultMap.put("msg", msg);
        this.ciCustomGroupInfo = cg;
        return "preEdit";
    }

    public String prePushCustomerSingle() throws Exception {
        String msg = "";
        boolean success = false;
        CiCustomGroupInfo cg = null;
        List pushedCycle = null;

        try {
            cg = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupInfo.getCustomGroupId());
            pushedCycle = this.customersService.queryPushCycleByGroupId(this.ciCustomGroupInfo.getCustomGroupId());
            if (cg.getUpdateCycle().intValue() != 1 && pushedCycle != null && pushedCycle.size() > 0) {
                this.isPush = "1";
            } else {
                this.isPush = "0";
            }

            if (cg.getUpdateCycle().intValue() == 2 || cg.getUpdateCycle().intValue() == 3) {
                this.sysInfoCycleList = this.customersService.queryCiSysInfo(cg);
            }

            success = true;
            this.ciSysInfoList = this.customersService.queryCiSysInfo(1);
            if (success && cg.getStatus().intValue() == 0) {
                success = false;
                msg = "不能推送已删除的客户群！";
            }
        } catch (Exception var6) {
            this.log.error("客户群初始化页面失败，customGroupId = " + this.ciCustomGroupInfo.getCustomGroupId(), var6);
            msg = var6.getMessage();
            success = false;
            throw new CIServiceException(msg, var6);
        }

        this.resultMap = new HashMap();
        this.resultMap.put("success", Boolean.valueOf(success));
        this.resultMap.put("msg", msg);
        if (success) {
            this.ciCustomGroupInfo = cg;
        }

        return "pushCustomerGroupSingleInit";
    }

    public void findPushedCycle() throws Exception {
        String msg = "";
        boolean success = false;
        HashMap result = new HashMap();
        List pushedCycle = null;
        if (this.ciCustomGroupInfo == null) {
            msg = "接收参数失败！";
            success = false;
        }

        try {
            pushedCycle = this.customersService.queryPushCycleByGroupId(this.ciCustomGroupInfo.getCustomGroupId());
            if (pushedCycle != null && pushedCycle.size() > 0) {
                StringBuffer response = new StringBuffer();
                Iterator var7 = pushedCycle.iterator();

                while (var7.hasNext()) {
                    CiCustomGroupPushCycle e = (CiCustomGroupPushCycle) var7.next();
                    response.append(e.getId().getSysId()).append(",");
                }

                msg = response.substring(0, response.length() - 1).toString();
            }

            success = true;
        } catch (Exception var9) {
            this.log.error("查询已推送的平台信息失败，customGroupId = " + this.ciCustomGroupInfo.getCustomGroupId(), var9);
            msg = var9.getMessage();
            success = false;
            throw new CIServiceException(msg, var9);
        }

        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var8) {
            this.log.error("发送json串异常", var8);
            throw new CIServiceException("发送json串异常");
        }
    }

    public String prePushCustomerBatch() throws Exception {
        String msg = "";
        boolean success = false;
        CiCustomGroupInfo cg = null;
        List sysInfo = null;
        List sysInfoCycle = null;
        ArrayList customList = new ArrayList();
        this.isAllDisposable = Integer.valueOf(1);

        try {
            if (this.ciCustomGroupInfoList != null) {
                for (int e = 0; e < this.ciCustomGroupInfoList.size(); ++e) {
                    CiCustomGroupInfo item = (CiCustomGroupInfo) this.ciCustomGroupInfoList.get(e);
                    cg = this.customersService.queryCiCustomGroupInfo(item.getCustomGroupId());
                    if (cg == null || cg.getUpdateCycle() == null || cg.getUpdateCycle().intValue() != 1) {
                        this.isAllDisposable = Integer.valueOf(0);
                    }

                    customList.add(cg);
                }
            }

            sysInfo = this.customersService.queryCiSysInfo(1);
            if (this.isAllDisposable.intValue() == 0) {
                CiCustomGroupInfo var10 = new CiCustomGroupInfo();
                var10.setUpdateCycle(Integer.valueOf(2));
                sysInfoCycle = this.customersService.queryCiSysInfo(var10);
                this.sysInfoCycleList = sysInfoCycle;
            }

            success = true;
        } catch (Exception var9) {
            this.log.error("批量客户群初始化页面失败，customGroupId = " + this.ciCustomGroupInfo.getCustomGroupId(), var9);
            msg = var9.getMessage();
            success = false;
            throw new CIServiceException(msg, var9);
        }

        if (success) {
            this.ciSysInfoList = sysInfo;
            this.ciCustomGroupInfoList = customList;
        }

        return "pushCustomerGroupBatchInit";
    }

    public void havaSuccessCustomGroup() throws Exception {
        String msg = "";
        boolean success = true;
        HashMap result = new HashMap();
        CiCustomGroupInfo cg = null;

        try {
            if (this.ciCustomGroupInfoList != null) {
                for (int response = 0; response < this.ciCustomGroupInfoList.size(); ++response) {
                    CiCustomGroupInfo e = (CiCustomGroupInfo) this.ciCustomGroupInfoList.get(response);
                    cg = this.customersService.queryCiCustomGroupInfo(e.getCustomGroupId());
                    if (cg.getDataStatus().intValue() != 3) {
                        cg.setDataStatusStr(IdToName.getName("DIM_CUSTOM_DATA_STATUS", cg.getDataStatus()));
                        success = false;
                        msg = "【" + cg.getCustomGroupName() + "】客户群状态为【" + cg.getDataStatusStr() + "】，请重新选择！";
                        break;
                    }
                }
            }
        } catch (Exception var8) {
            this.log.error("判断客户群状态失败，customGroupId = " + this.ciCustomGroupInfo.getCustomGroupId(), var8);
            msg = var8.getMessage();
            success = false;
            throw new CIServiceException(msg, var8);
        }

        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);
        HttpServletResponse var9 = this.getResponse();

        try {
            this.sendJson(var9, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException("发送json串异常");
        }
    }

    private List<CiLabelRule> resolveCustomRule() {
        HttpSession session = super.getSession();
        List rules = (List) session.getAttribute("sessionModelList");
        ArrayList newRules = new ArrayList();
        if (rules != null) {
            int i;
            for (i = 0; i < rules.size(); ++i) {
                CiLabelRule rule = (CiLabelRule) rules.get(i);
                CiLabelRule clone = null;

                try {
                    clone = rule.clone();
                } catch (CloneNotSupportedException var9) {
                    var9.printStackTrace();
                }

                int elementType = rule.getElementType().intValue();
                if (elementType == 6) {
                    List children = rule.getChildCiLabelRuleList();
                    newRules.add(this.generateRule("(", 3, 0L));
                    newRules.addAll(children);
                    newRules.add(this.generateRule(")", 3, 0L));
                } else {
                    newRules.add(clone);
                }
            }

            for (i = 0; i < newRules.size(); ++i) {
                ((CiLabelRule) newRules.get(i)).setSortNum(Long.valueOf((long) (i + 1)));
            }
        }

        return newRules;
    }

    private CiLabelRule generateRule(String element, int type, long sortNum) {
        CiLabelRule result = new CiLabelRule();
        result.setCalcuElement(element);
        result.setElementType(Integer.valueOf(type));
        result.setSortNum(Long.valueOf(sortNum));
        return result;
    }

    public String saveCustomByRulesInit() {
        try {
            HttpSession e = this.getSession();
            CacheBase cache = CacheBase.getInstance();
            this.dimScenes = (List<DimScene>) cache.getObjectList("DIM_SCENE");
            this.ciLabelRuleList = (List) e.getAttribute("sessionModelList");
            this.flagConstraint = this.customersService.isConstraintCreateCustom(this.resolveCustomRule());
            this.ciLabelInfoList = this.customersService.queryCiLabelInfoList4SaveCustom();
            this.ciGroupAttrRelList = this.customersService.queryCustomGroupAttrRelList(this.ciLabelRuleList);
            this.sortAttrNum = Configure.getInstance().getProperty("SORT_ATTR_NUM");        //限制清单数量 排序字段的个数 SORT_ATTR_NUM=1
            String customGroupId = (String) e.getAttribute("customGroupId");
            StringBuffer rule;
            if (StringUtil.isNotEmpty(customGroupId)) {
                CiCustomListInfo dayLabelDate = this.customersService.queryCustomListInfo(this.ciCustomGroupInfo);
                if (dayLabelDate != null) {
                    this.ciGroupAttrRelListModify = this.ciGroupAttrRelService.queryNewestGroupAttrRelList(this.ciCustomGroupInfo.getCustomGroupId());
                    Iterator tacticsId = this.ciGroupAttrRelListModify.iterator();

                    while (true) {
                        int vertLabelInfo;
                        while (tacticsId.hasNext()) {
                            CiGroupAttrRel monthLabelDate = (CiGroupAttrRel) tacticsId.next();
                            if (monthLabelDate.getAttrSource() == 3) {
                                CiCustomGroupInfo var21 = this.customersService.queryCiCustomGroupInfoByListInfoId(monthLabelDate.getLabelOrCustomId());
                                monthLabelDate.setCustomId(var21.getCustomGroupId());
                                monthLabelDate.setCustomName(var21.getCustomGroupName());
                            } else if (monthLabelDate.getIsVerticalAttr() == 1 && (StringUtils.isNotEmpty(monthLabelDate.getAttrVal()) || StringUtils.isNotEmpty(monthLabelDate.getTableName()))) {
                                DimTableServiceImpl labelOptRuleShow = (DimTableServiceImpl) SystemServiceLocator.getInstance().getService("dimTable_serviceTarget");
                                String createTypeId = cache.getEffectiveColumn(monthLabelDate.getLabelOrCustomColumn()).getDimTransId();
                                if (StringUtils.isEmpty(monthLabelDate.getAttrVal())) {
                                    String ciLabelRuleListTemp = this.customersService.queryAllValueStrByValueTable(monthLabelDate.getTableName());
                                    monthLabelDate.setAttrVal(ciLabelRuleListTemp);
                                }

                                String[] var30 = monthLabelDate.getAttrVal().split(",");
                                rule = new StringBuffer("");

                                for (vertLabelInfo = 0; vertLabelInfo < var30.length; ++vertLabelInfo) {
                                    String dimId = var30[vertLabelInfo];
                                    String name = labelOptRuleShow.parseName(dimId, "null", createTypeId);
                                    rule.append(name);
                                    if (vertLabelInfo != var30.length - 1) {
                                        rule.append(",");
                                    }
                                }

                                String var40 = rule.toString();
                                monthLabelDate.setAttrName(var40);
                            }
                        }

                        ArrayList var16 = new ArrayList();
                        Iterator var22 = this.ciGroupAttrRelListModify.iterator();

                        while (true) {
                            while (var22.hasNext()) {
                                CiGroupAttrRel var18 = (CiGroupAttrRel) var22.next();
                                if (var18.getAttrSource() == 3) {
                                    Iterator var32 = this.ciLabelRuleList.iterator();

                                    while (var32.hasNext()) {
                                        CiLabelRule var25 = (CiLabelRule) var32.next();
                                        int var34 = var25.getElementType().intValue();
                                        if (5 == var34 && StringUtil.isNotEmpty(var25.getCalcuElement()) && StringUtil.isNotEmpty(var18.getLabelOrCustomId()) && var25.getCalcuElement().equals(var18.getLabelOrCustomId())) {
                                            var16.add(var18);
                                        }
                                    }
                                } else {
                                    var16.add(var18);
                                }
                            }

                            this.ciGroupAttrRelListModify = var16;
                            this.sortAttrs = this.ciGroupAttrRelService.queryNewestGroupAttrRelListBySort(this.ciCustomGroupInfo.getCustomGroupId());
                            ArrayList var19 = new ArrayList();
                            Iterator var26 = this.sortAttrs.iterator();

                            while (true) {
                                while (var26.hasNext()) {
                                    CiGroupAttrRel var23 = (CiGroupAttrRel) var26.next();
                                    if (var23.getAttrSource() == 3) {
                                        Iterator var37 = this.ciLabelRuleList.iterator();

                                        while (var37.hasNext()) {
                                            CiLabelRule var33 = (CiLabelRule) var37.next();
                                            vertLabelInfo = var33.getElementType().intValue();
                                            if (5 == vertLabelInfo && StringUtil.isNotEmpty(var33.getCalcuElement()) && StringUtil.isNotEmpty(var23.getLabelOrCustomId()) && var33.getCalcuElement().equals(var23.getLabelOrCustomId())) {
                                                var19.add(var23);
                                            }
                                        }
                                    } else {
                                        var19.add(var23);
                                    }
                                }

                                this.sortAttrs = var19;
                                if (this.sortAttrs != null) {
                                    for (int var24 = 0; var24 < this.sortAttrs.size(); ++var24) {
                                        CiGroupAttrRel var28 = (CiGroupAttrRel) this.sortAttrs.get(var24);
                                        Integer var35 = Integer.valueOf(var28.getIsVerticalAttr());
                                        if (var28.getAttrSource() == 3) {
                                            CiCustomGroupInfo var38 = this.customersService.queryCiCustomGroupInfoByListInfoId(var28.getLabelOrCustomId());
                                            var28.setCustomId(var38.getCustomGroupId());
                                            var28.setCustomName(var38.getCustomGroupName());
                                        }

                                        if (var35.intValue() == 1) {
                                            String var39 = var28.getLabelOrCustomId();
                                            CiLabelInfo var41 = CacheBase.getInstance().getEffectiveLabel(var39);
                                            var28.setAttrColName(var41.getLabelName() + "-" + var28.getAttrColName());
                                        }
                                    }
                                }

                                return "createCustomer";
                            }
                        }
                    }
                }
            } else {
                String var15 = "";
                String var17 = "";
                String var20 = "";
                String var27 = "";
                boolean var29 = true;
                List var36 = (List) e.getAttribute("sessionModelList");
                rule = new StringBuffer();
                this.customersService.shopCartRule(var36, rule);
                int var31;
                if (this.ciCustomGroupInfo == null) {
                    var15 = this.getNewLabelDay();
                    var17 = this.getNewLabelMonth();
                    var20 = "2";
                    var27 = rule.toString();
                    var31 = 1;
                } else {
                    var15 = this.ciCustomGroupInfo.getDayLabelDate();
                    var17 = this.ciCustomGroupInfo.getMonthLabelDate();
                    var20 = this.ciCustomGroupInfo.getTacticsId();
                    var27 = this.ciCustomGroupInfo.getLabelOptRuleShow();
                    var31 = this.ciCustomGroupInfo.getCreateTypeId().intValue();
                }

                this.ciCustomGroupInfo = new CiCustomGroupInfo();
                this.ciCustomGroupInfo.setDayLabelDate(var15);
                this.ciCustomGroupInfo.setMonthLabelDate(var17);
                this.ciCustomGroupInfo.setTacticsId(var20);
                this.ciCustomGroupInfo.setLabelOptRuleShow(rule.toString());
                this.ciCustomGroupInfo.setDataDate(var17);
                this.ciCustomGroupInfo.setCreateTypeId(Integer.valueOf(var31));
            }

            return "createCustomer";
        } catch (Exception var14) {
            this.log.error("初始化创建客户群异常！", var14);
            throw new CIServiceException("初始化创建客户群异常！");
        }
    }

    public void findLabelsByLabelName() {
        LinkedHashMap result = new LinkedHashMap();

        try {
            HttpSession response = this.getSession();
            this.ciLabelRuleList = (List) response.getAttribute("sessionModelList");
            this.ciLabelInfoList = this.customersService.queryCiLabelInfoList4SaveCustom(this.labelName, this.parentId, this.level);
        } catch (Exception var5) {
            this.log.error("查询标签异常！", var5);
            throw new CIServiceException("查询标签异常！");
        }

        if (this.ciLabelInfoList != null && this.ciLabelInfoList.size() > 0) {
            Iterator e = this.ciLabelInfoList.iterator();

            while (e.hasNext()) {
                CiLabelInfo response1 = (CiLabelInfo) e.next();
                result.put(String.valueOf(response1.getLabelId()), response1.getLabelName());
            }
        }

        HttpServletResponse response2 = this.getResponse();

        try {
            this.sendJson(response2, JsonUtil.toJson(this.ciLabelInfoList));
            this.log.debug("========>>>>" + JsonUtil.toJson(result));
        } catch (Exception var4) {
            this.log.error("发送json串异常", var4);
            throw new CIServiceException("发送json串异常");
        }
    }

    public void findCustomAttrByColumnName() {
        try {
            HttpSession response = this.getSession();
            this.ciLabelRuleList = (List) response.getAttribute("sessionModelList");
            this.ciGroupAttrRelList = this.customersService.queryCustomGroupAttrRelList(this.ciLabelRuleList, this.labelName, this.parentId, this.customListDate);
        } catch (Exception var4) {
            this.log.error("查询标签异常！", var4);
            throw new CIServiceException("查询标签异常！");
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(this.ciGroupAttrRelList));
            this.log.info("========>>>>" + JsonUtil.toJson(this.ciGroupAttrRelList));
        } catch (Exception var3) {
            this.log.error("发送json串异常", var3);
            throw new CIServiceException("发送json串异常");
        }
    }

    public void validatePower() throws Exception {
        List labelArr = new ArrayList();
        CacheBase cacheBase = CacheBase.getInstance();
        HashMap result = new HashMap();
        StringBuffer label_Ids = new StringBuffer(" ");
        String labelIds;
        boolean flag = false;
        String json = null;
        String vertAttrTableNameMap;
        int elementType;
        try {
            this.log.info("ciCustomGroupInfo" + "    " + this.ciCustomGroupInfo);
//            this.log.info("user_Id" + " " + this.getUserId());
            String userId = this.getUserId();
            HttpSession session = this.getSession();
            this.ciLabelRuleList = (List) session.getAttribute("sessionModelList");
            List<CiLabelRule> rules = this.customersService.getNewCiLabelRuleList(this.ciLabelRuleList);
            this.log.info("rules" + " " + rules + " " + "rules.size()" + " " + rules.size());
            for (int i = 0; i < rules.size(); i++) {
                CiLabelRule ciLabelRule = (CiLabelRule) rules.get(i);
                elementType = ciLabelRule.getElementType().intValue();
                if (elementType == 2) {
                    vertAttrTableNameMap = ciLabelRule.getCalcuElement();
                    this.log.info("vertAttrTableNameMap" + " " + vertAttrTableNameMap);
                    CiLabelInfo ciLabelInfo = cacheBase.getEffectiveLabel(vertAttrTableNameMap);
                    this.log.info("ciLabelInfo" + " " + ciLabelInfo);
                    CiLabelExtInfo ciLabelExtInfo = ciLabelInfo.getCiLabelExtInfo();
                    this.log.info("label_id" + " " + ciLabelExtInfo.getLabelId());
                    labelArr.add(ciLabelExtInfo.getLabelId());
                }
            }
            if (labelArr.size() != 0) {
                labelIds = StringUtils.join(labelArr, ",");
                if (Integer.valueOf(customersService.queryForExist(labelIds)) == 0) {
                    flag = true;
                    result.put("msg", "Ok");
                } else if (Integer.valueOf(customersService.queryForPowerMatch(userId, labelIds)) == 0) {
                    flag = false;
                    result.put("msg", "Sorry！您没有操作对应标签的权限" + "，如果疑问，请联系管理员！");
                } else {
                    flag = true;
                    result.put("msg", "Ok");
                }
            } else {
                flag = true;
                result.put("msg", "Ok");
            }
        } catch (Exception e) {
            String errorMsg = "Sorry！您没有操作对应标签的权限";
            this.log.error(errorMsg, e);
            result.put("msg", errorMsg + "，如果疑问，请联系管理员！");
            flag = false;
        }
        result.put("success", Boolean.valueOf(flag));

        try {
            json = JsonUtil.toJson(result);
        } catch (IOException e) {
            this.log.error("json转换异常", e);
            throw new CIServiceException("json转换异常", e);
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, json);
        } catch (Exception e) {
            this.log.error("json串发送异常", e);
            throw new CIServiceException("json串发送异常", e);
        }
    }


    public void validateSql() throws Exception {
        String jsonObj = null;
        HashMap result = new HashMap();
        String validateSql = "";
        boolean flag = false;

        try {
            this.log.info("ciCustomGroupInfo" + "    " + this.ciCustomGroupInfo);
            HttpSession response = this.getSession();
            this.ciLabelRuleList = (List) response.getAttribute("sessionModelList");
            List e1 = this.customersService.getNewCiLabelRuleList(this.ciLabelRuleList);
            validateSql = this.customersService.getValidateSqlStr(e1, this.ciCustomGroupInfo.getMonthLabelDate(), this.ciCustomGroupInfo.getDayLabelDate(), this.getUserId());
            this.log.info("ciCustomGroupInfo" + "    " + this.ciCustomGroupInfo);
            this.log.debug("validate SQL : " + validateSql);
            flag = true;
        } catch (Exception var9) {
            String e = "SQL拼接异常";
            this.log.error(e, var9);
            result.put("msg", e + "，请联系管理员！");
            flag = false;
        }

        if (flag) {
            flag = this.customersService.queryValidateSql(validateSql);
            if (!flag) {
                result.put("msg", "您拖动的条件验证未通过，请检查所选条件是否正确！");
            } else {
                result.put("msg", "ok");
            }
        }

        result.put("success", Boolean.valueOf(flag));

        try {
            jsonObj = JsonUtil.toJson(result);
        } catch (IOException var8) {
            this.log.error("json转换异常", var8);
            throw new CIServiceException("json转换异常", var8);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, jsonObj);
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException("发送json串异常");
        }
    }

    private Set<String> findInvalidCustomListInfoIds(List<CiLabelRule> ciLabelRuleList) {
        String userId = this.getUserId();
        HashSet ids = new HashSet();

        try {
            Iterator var5 = ciLabelRuleList.iterator();

            while (var5.hasNext()) {
                CiLabelRule e = (CiLabelRule) var5.next();
                int elementType = e.getElementType().intValue();
                if (elementType == 5) {
                    String listInfoId = e.getCalcuElement();
                    CiCustomGroupInfo ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfoByListInfoId(listInfoId);
                    if (1 == ciCustomGroupInfo.getStatus().intValue()) {
                        if (1 == ciCustomGroupInfo.getIsPrivate().intValue() && !userId.equals(ciCustomGroupInfo.getCreateUserId())) {
                            ids.add(listInfoId);
                        }
                    } else {
                        ids.add(listInfoId);
                    }
                }
            }
        } catch (Exception var9) {
            this.log.error("查询无效的客户群清单id集合错误", var9);
        }

        return ids;
    }

    public void findCustomerNumForExplore() throws Exception {
        String msg = "";
        boolean num = false;
        String countSql = null;
        boolean success = false;
        String LOG_DATA_EXPLORE = "";
        if (this.exploreFromModuleFlag != null && 1 == this.exploreFromModuleFlag.intValue()) {
            LOG_DATA_EXPLORE = "COC_INDEX_LABEL_DATA_EXPLORE";
        } else if (this.exploreFromModuleFlag != null && 2 == this.exploreFromModuleFlag.intValue()) {
            LOG_DATA_EXPLORE = "COC_INDEX_PRODUCT_DATA_EXPLORE";
        } else if (this.exploreFromModuleFlag != null && 3 == this.exploreFromModuleFlag.intValue()) {
            LOG_DATA_EXPLORE = "COC_LABEL_ANALYSIS_DATA_EXPLORE";
        }

        HashMap result = new HashMap();

        try {
            Date e = new Date();
            this.ciLabelRuleList = (List) this.getSession().getAttribute("sessionModelList");
            List newList = this.customersService.getNewCiLabelRuleList(this.ciLabelRuleList);
            countSql = this.customersService.getCountSqlStr(newList, this.ciCustomGroupInfo.getMonthLabelDate(), this.ciCustomGroupInfo.getDayLabelDate(), this.getUserId());
            this.log.info("count SQL : " + countSql);
            int num1 = this.customersService.queryCount(countSql);
            this.logExploreRecord(countSql, e, this.ciLabelRuleList);
            success = true;
            result.put("success", Boolean.valueOf(success));
            result.put("msg", Integer.valueOf(num1));
            CILogServiceUtil.getLogServiceInstance().log(LOG_DATA_EXPLORE, "", "数据探索", "数据探索成功，用户数为：" + num1, OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var10) {
            msg = "查询用户数量失败";
            this.log.error(msg + "EXCUTE SQL IS : " + countSql, var10);
            success = false;
            result.put("success", Boolean.valueOf(success));
            result.put("msg", msg);
            CILogServiceUtil.getLogServiceInstance().log(LOG_DATA_EXPLORE, "-1", "数据探索", "数据探索失败，执行SQL : " + countSql, OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        try {
            HttpServletResponse e1 = this.getResponse();
            this.sendJson(e1, JsonUtil.toJson(result));
        } catch (Exception var9) {
            this.log.error("发送json串异常", var9);
            throw new CIServiceException(var9);
        }
    }

    public void countOfCustomerCalc() throws Exception {
        HashMap result = new HashMap();
        String LOG_DATA_EXPLORE = "COC_INDEX_LABEL_DATA_EXPLORE";

        try {
            Date e = new Date();
            this.ciLabelRuleList = (List) this.getSession().getAttribute("sessionModelList");
            String querySql = this.customersService.getSelectSqlByCustomersRels(this.ciGroupAttrRelList, this.ciLabelRuleList, this.ciCustomGroupInfo.getMonthLabelDate(), this.ciCustomGroupInfo.getDayLabelDate(), this.ciCustomGroupInfo.getDataDate(), this.getUserId(), (Integer) null, (Integer) null, false);
            StringBuffer countSqlBuf = new StringBuffer();
            countSqlBuf.append("select count(1) from (").append(querySql).append(") abc");
            String countSql = countSqlBuf.toString();
            this.log.info("COC_SQL >>>>>>> " + countSql);
            int count = this.customersService.queryCount(countSql);
            this.logExploreRecord(countSql, e, this.ciLabelRuleList);
            result.put("success", Boolean.valueOf(true));
            result.put("msg", Integer.valueOf(count));
            CILogServiceUtil.getLogServiceInstance().log(LOG_DATA_EXPLORE, "", "数据探索", "数据探索成功，用户数为：" + count, OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var9) {
            result.put("success", Boolean.valueOf(false));
            result.put("msg", var9.getMessage());
            this.log.error("数据探索出错 in countOfCustomerCalc");
        }

        try {
            HttpServletResponse e1 = this.getResponse();
            this.sendJson(e1, JsonUtil.toJson(result));
        } catch (Exception var8) {
            this.log.error("发送json串异常", var8);
            throw new CIServiceException(var8);
        }
    }

    public void saveCustomerByCustomerCalc() throws Exception {
        Object result = new HashMap();

        try {
            if (this.ciCustomGroupInfo != null && StringUtils.isNotEmpty(this.ciCustomGroupInfo.getStartDateStr()) && StringUtils.isNotEmpty(this.ciCustomGroupInfo.getEndDateStr())) {
                Date e = null;
                Date endDate = null;
                String minDate;
                String[] dates;
                String date;
                int var7;
                int var8;
                String[] var9;
                if (this.ciCustomGroupInfo.getStartDateStr() != null && this.ciCustomGroupInfo.getStartDateStr().contains(",")) {
                    minDate = "";
                    dates = this.ciCustomGroupInfo.getStartDateStr().split(",");
                    var9 = dates;
                    var8 = dates.length;

                    for (var7 = 0; var7 < var8; ++var7) {
                        date = var9[var7];
                        date = date.trim().replaceAll("\\D", "");
                        if (!StringUtils.isEmpty(date)) {
                            if (date.length() == 6) {
                                date = date + "01";
                            }

                            if (minDate.length() == 0 || Long.valueOf(date).longValue() > Long.valueOf(minDate).longValue()) {
                                minDate = date;
                            }
                        }
                    }

                    this.ciCustomGroupInfo.setStartDateStr(minDate);
                    if (StringUtils.isEmpty(minDate)) {
                        this.ciCustomGroupInfo.setStartDateStr(DateUtil.date2String(new Date(), "yyyyMMdd"));
                    }
                }

                if (this.ciCustomGroupInfo.getEndDateStr() != null && this.ciCustomGroupInfo.getEndDateStr().contains(",")) {
                    minDate = "";
                    dates = this.ciCustomGroupInfo.getEndDateStr().split(",");
                    var9 = dates;
                    var8 = dates.length;

                    for (var7 = 0; var7 < var8; ++var7) {
                        date = var9[var7];
                        date = date.trim().replaceAll("\\D", "");
                        if (!StringUtils.isEmpty(date)) {
                            if (date.length() == 6) {
                                date = date + "01";
                            }

                            if (minDate.length() == 0 || Long.valueOf(date).longValue() < Long.valueOf(minDate).longValue()) {
                                minDate = date;
                            }
                        }
                    }

                    this.ciCustomGroupInfo.setEndDateStr(minDate);
                    if (StringUtils.isEmpty(minDate)) {
                        this.ciCustomGroupInfo.setEndDateStr(DateUtil.date2String(new Date(), "yyyyMMdd"));
                    }
                }

                if (this.ciCustomGroupInfo.getUpdateCycle() != null && 2 == this.ciCustomGroupInfo.getUpdateCycle().intValue()) {
                    e = DateUtil.string2Date(this.ciCustomGroupInfo.getStartDateStr().replaceAll("\\D", ""), "yyyyMMdd");
                    endDate = DateUtil.string2Date(this.ciCustomGroupInfo.getEndDateStr().replaceAll("\\D", ""), "yyyyMMdd");
                    endDate = DateUtil.lastDayOfMonth(endDate);
                    e = DateUtil.firstDayOfMonth(e);
                } else {
                    e = DateUtil.string2Date(this.ciCustomGroupInfo.getStartDateStr().replaceAll("\\D", ""), "yyyyMMdd");
                    endDate = DateUtil.string2Date(this.ciCustomGroupInfo.getEndDateStr().replaceAll("\\D", ""), "yyyyMMdd");
                }

                this.ciCustomGroupInfo.setStartDate(e);
                this.ciCustomGroupInfo.setEndDate(endDate);
            }

            if (this.ciLabelRuleList == null) {
                this.log.debug("客户群计算保存客户群 start");
                result = this.saveByCusomerCalc((Map) result);
            }

            if (this.ciCustomSourceRelList == null) {
                result = this.saveBylabelDifferential((Map) result);
            }

            if (this.ciLabelRuleList != null && this.ciCustomSourceRelList != null) {
                result = this.saveByCustomerDifferential((Map) result);
            }
        } catch (Exception var11) {
            this.log.error("保存客户群异常", var11);
            ((Map) result).put("success", Boolean.valueOf(false));
            ((Map) result).put("msg", var11.getMessage());
        }

        try {
            HttpServletResponse var12 = this.getResponse();
            this.sendJson(var12, JsonUtil.toJson(result));
        } catch (Exception var10) {
            this.log.error("发送json串异常", var10);
            throw new CIServiceException(var10);
        }
    }

    private Map<String, Object> saveByCustomerDifferential(Map<String, Object> result) throws CloneNotSupportedException {
        this.log.debug("客户群标签微分保存客户群 start");
        StringBuilder nameSb = new StringBuilder();
        if (this.ciCustomGroupInfoList != null && this.ciCustomGroupInfoList.size() > 0) {
            for (int e = 0; e < this.ciCustomGroupInfoList.size(); ++e) {
                CiCustomGroupInfo info = (CiCustomGroupInfo) this.ciCustomGroupInfoList.get(e);
                nameSb.append(",").append(info.getCustomGroupName());
                CiCustomGroupInfo mainInfo = this.ciCustomGroupInfo.clone();
                mainInfo.setCustomGroupName(info.getCustomGroupName());
                mainInfo.setCustomOptRuleShow(info.getCustomOptRuleShow());
                mainInfo.setCustomOptRuleShow(info.getLabelOptRuleShow());
                mainInfo.setCreateTypeId(Integer.valueOf(6));
                mainInfo.setStatus(Integer.valueOf(1));
                mainInfo.setSceneId(info.getSceneId());
                ArrayList baseciLabelRuleList = new ArrayList();

                CiLabelRule baseRule;
                for (int ciLabelRuleList2 = 0; ciLabelRuleList2 < this.ciLabelRuleList.size(); ++ciLabelRuleList2) {
                    if (((CiLabelRule) this.ciLabelRuleList.get(ciLabelRuleList2)).getBaseParam()) {
                        baseRule = ((CiLabelRule) this.ciLabelRuleList.get(ciLabelRuleList2)).clone();
                        baseRule.setSortNum(Long.valueOf((long) ciLabelRuleList2));
                        baseciLabelRuleList.add(baseRule);
                    }
                }

                ArrayList var12 = new ArrayList();
                var12.addAll(baseciLabelRuleList);
                baseRule = (CiLabelRule) this.ciLabelRuleList.get(baseciLabelRuleList.size() + e);
                baseRule.setSortNum(Long.valueOf((long) (baseciLabelRuleList.size() + e)));
                var12.add(baseRule);

                try {
                    this.customersService.addCiCustomGroupInfo(mainInfo, var12, this.ciCustomSourceRelList, (CiTemplateInfo) null, this.getUserId(), false, (List) null);
                    CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_ADD_LABEL_ANALYSIS", mainInfo.getCustomGroupId().toString(), mainInfo.getCustomGroupName().toString(), "客户群分析->标签微分保存成功,【客户群名称 :" + mainInfo.getCustomGroupName() + ",客户群描述:" + mainInfo.getCustomGroupDesc() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                } catch (Exception var11) {
                    this.log.error("保存客户群分析->标签微分异常", var11);
                    CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_ADD_LABEL_ANALYSIS", "-1", mainInfo.getCustomGroupName().toString(), "客户群分析->标签微分保存失败,【客户群名称 :" + mainInfo.getCustomGroupName() + ",客户群描述:" + mainInfo.getCustomGroupDesc() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
                    throw new CIServiceException(var11);
                }
            }

            result.put("names", nameSb.substring(1));
        } else {
            try {
                this.customersService.addCiCustomGroupInfo(this.ciCustomGroupInfo, this.ciLabelRuleList, this.ciCustomSourceRelList, (CiTemplateInfo) null, this.getUserId(), false, (List) null);
                result.put("names", this.ciCustomGroupInfo.getCustomGroupName());
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_ADD_LABEL_ANALYSIS", this.ciCustomGroupInfo.getCustomGroupId().toString(), this.ciCustomGroupInfo.getCustomGroupName().toString(), "客户群分析->标签微分保存成功,【客户群名称 :" + this.ciCustomGroupInfo.getCustomGroupName() + ",客户群描述:" + this.ciCustomGroupInfo.getCustomGroupDesc() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            } catch (Exception var10) {
                this.log.error("保存客户群分析->标签微分异常", var10);
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_ADD_LABEL_ANALYSIS", "-1", this.ciCustomGroupInfo.getCustomGroupName().toString(), "客户群分析->标签微分保存失败,【客户群名称 :" + this.ciCustomGroupInfo.getCustomGroupName() + ",客户群描述:" + this.ciCustomGroupInfo.getCustomGroupDesc() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
                throw new CIServiceException(var10);
            }
        }

        result.put("success", Boolean.valueOf(true));
        return result;
    }

    public void saveCustomerByCustomerCalcOnly() throws Exception {
        Object result = new HashMap();

        try {
            this.log.debug("客户群计算保存客户群 start");
            result = this.saveByCusomerCalc((Map) result);
        } catch (Exception var4) {
            this.log.error("保存客户群异常", var4);
            ((Map) result).put("success", Boolean.valueOf(false));
            ((Map) result).put("msg", var4.getMessage());
        }

        try {
            HttpServletResponse e = this.getResponse();
            this.sendJson(e, JsonUtil.toJson(result));
        } catch (Exception var3) {
            this.log.error("发送json串异常", var3);
            throw new CIServiceException(var3);
        }
    }

    private Map<String, Object> saveByCusomerCalc(Map<String, Object> result) {
        HttpSession session = PrivilegeServiceUtil.getSession();
        this.ciLabelRuleList = (List) session.getAttribute("sessionModelList");
        String COC_CUSTOMER_MANAGE_ADD = "";
        if (this.saveCustomersFlag != null && 2 == this.saveCustomersFlag.intValue()) {
            COC_CUSTOMER_MANAGE_ADD = "COC_CUSTOMER_MANAGE_ADD_INDEX_CUS_CALCULATE";
        } else {
            COC_CUSTOMER_MANAGE_ADD = "COC_CUSTOMER_MANAGE_ADD_CUS_ANALYSIS";
        }

        result = this.customersService.isNameExist(this.ciCustomGroupInfo, this.getUserId());
        if (((Boolean) result.get("success")).booleanValue()) {
            this.ciCustomGroupInfo.setCreateTypeId(Integer.valueOf(4));
            this.ciCustomGroupInfo.setStatus(Integer.valueOf(1));
            this.ciCustomGroupInfo.setUpdateCycle(Integer.valueOf(1));
            Iterator var5 = this.ciLabelRuleList.iterator();

            while (true) {
                while (true) {
                    CiLabelRule e;
                    do {
                        if (!var5.hasNext()) {
                            try {
                                this.customersService.addCiCustomGroupInfo(this.ciCustomGroupInfo, this.ciLabelRuleList, this.ciCustomSourceRelList, (CiTemplateInfo) null, this.getUserId(), false, (List) null);
                                result.put("names", this.ciCustomGroupInfo.getCustomGroupName());
                                CILogServiceUtil.getLogServiceInstance().log(COC_CUSTOMER_MANAGE_ADD, this.ciCustomGroupInfo.getCustomGroupId().toString(), this.ciCustomGroupInfo.getCustomGroupName().toString(), "客户群计算保存客户群保存成功,【客户群名称 :" + this.ciCustomGroupInfo.getCustomGroupName() + ",客户群描述:" + this.ciCustomGroupInfo.getCustomGroupDesc() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                                return result;
                            } catch (Exception var9) {
                                this.log.error("客户群计算保存客户群异常", var9);
                                CILogServiceUtil.getLogServiceInstance().log(COC_CUSTOMER_MANAGE_ADD, "-1", this.ciCustomGroupInfo.getCustomGroupName().toString(), "客户群计算保存客户群保存失败,【客户群名称 :" + this.ciCustomGroupInfo.getCustomGroupName() + ",客户群描述:" + this.ciCustomGroupInfo.getCustomGroupDesc() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
                                throw new CIServiceException(var9);
                            }
                        }

                        e = (CiLabelRule) var5.next();
                    } while (e.getElementType().intValue() != 5);

                    List infoList = this.customersService.queryCiCustomListInfoByCGroupId(e.getCalcuElement());
                    Iterator var8 = infoList.iterator();

                    while (var8.hasNext()) {
                        CiCustomListInfo info = (CiCustomListInfo) var8.next();
                        if (3 == info.getDataStatus().intValue()) {
                            e.setCalcuElement(info.getListTableName());
                            break;
                        }
                    }
                }
            }
        } else {
            result.put("success", Boolean.valueOf(false));
            result.put("msg", "名称已经存在");
            return result;
        }
    }

    private Map<String, Object> saveBylabelDifferential(Map<String, Object> result) throws CloneNotSupportedException {
        this.log.debug("标签微分保存客户群 start");
        this.ciCustomGroupInfo.setCreateTypeId(Integer.valueOf(2));
        this.ciCustomGroupInfo.setStatus(Integer.valueOf(1));
        StringBuilder nameSb = new StringBuilder();
        StringBuilder nameSbTemplate = new StringBuilder("");
        if (this.ciCustomGroupInfoList != null && this.ciCustomGroupInfoList.size() > 0) {
            boolean e = true;

            int i;
            for (i = 0; i < this.ciCustomGroupInfoList.size(); ++i) {
                CiCustomGroupInfo baseciLabelRuleList = (CiCustomGroupInfo) this.ciCustomGroupInfoList.get(i);
                nameSb.append(",").append(baseciLabelRuleList.getCustomGroupName());
                Map info = this.customersService.isNameExist(baseciLabelRuleList, this.getUserId());
                if (!((Boolean) info.get("success")).booleanValue()) {
                    String var18 = "客户群名称\'" + baseciLabelRuleList.getCustomGroupName() + "\'已经存在";
                    result.put("success", Boolean.valueOf(false));
                    result.put("msg", var18);
                    e = false;
                    break;
                }

                if (baseciLabelRuleList.getSaveTemplate()) {
                    this.ciTemplateInfo = (CiTemplateInfo) this.ciTemplateInfoList.get(i);
                    this.ciTemplateInfo.setSceneId(baseciLabelRuleList.getSceneId());
                    nameSbTemplate.append(",").append(this.ciTemplateInfo.getTemplateName());
                    Map mainInfo = this.ciTemplateInfoService.isNameExist(this.ciTemplateInfo, this.getUserId());
                    if (!((Boolean) mainInfo.get("success")).booleanValue()) {
                        String ciLabelRuleList2 = "模板名称\'" + this.ciTemplateInfo.getTemplateId() + "\'已经存在";
                        result.put("success", Boolean.valueOf(false));
                        result.put("msg", ciLabelRuleList2);
                        e = false;
                        break;
                    }
                }
            }

            if (e) {
                for (i = 0; i < this.ciCustomGroupInfoList.size(); ++i) {
                    ArrayList var15 = new ArrayList();

                    for (int var16 = 0; var16 < this.ciLabelRuleList.size(); ++var16) {
                        if (((CiLabelRule) this.ciLabelRuleList.get(var16)).getBaseParam()) {
                            CiLabelRule var19 = ((CiLabelRule) this.ciLabelRuleList.get(var16)).clone();
                            var19.setSortNum(Long.valueOf((long) var16));
                            var15.add(var19);
                        }
                    }

                    CiCustomGroupInfo var17 = (CiCustomGroupInfo) this.ciCustomGroupInfoList.get(i);
                    CiCustomGroupInfo var20 = this.ciCustomGroupInfo.clone();
                    var20.setSaveTemplate(var17.getSaveTemplate());
                    var20.setCustomGroupName(var17.getCustomGroupName());
                    var20.setLabelOptRuleShow(var17.getLabelOptRuleShow());
                    var20.setSceneId(var17.getSceneId());
                    ArrayList var21 = new ArrayList();
                    var21.addAll(var15);
                    CiLabelRule baseRule = (CiLabelRule) this.ciLabelRuleList.get(var15.size() + i);
                    baseRule.setSortNum(Long.valueOf((long) (var15.size() + i)));
                    var21.add(baseRule);
                    CiTemplateInfo template = null;
                    if (var20.getSaveTemplate()) {
                        template = ((CiTemplateInfo) this.ciTemplateInfoList.get(i)).clone();
                        template.setLabelOptRuleShow(var17.getLabelOptRuleShow());
                        template.setIsPrivate(Integer.valueOf(1));
                    }

                    try {
                        this.customersService.addCiCustomGroupInfo(var20, var21, this.ciCustomSourceRelList, template, this.getUserId(), var20.getSaveTemplate(), (List) null);
                        CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_ADD_LABEL_ANALYSIS", var20.getCustomGroupId(), nameSb.toString(), "标签微分，保存客户群【客户群ID:" + var20.getCustomGroupId() + ",客户群名称:" + nameSb.toString() + "客户群描述：" + var20.getCustomGroupDesc() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                    } catch (Exception var14) {
                        CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_ADD_LABEL_ANALYSIS", "-1", nameSb.toString(), "标签微分，保存客户群【客户群ID:" + var20.getCustomGroupId() + ",客户群名称:" + nameSb.toString() + "客户群描述：" + var20.getCustomGroupDesc() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
                    }
                }

                result.put("names", nameSb.substring(1));
                if (nameSbTemplate.length() > 0) {
                    result.put("templateNames", nameSbTemplate.substring(1));
                }
            }
        } else {
            try {
                this.customersService.addCiCustomGroupInfo(this.ciCustomGroupInfo, this.ciLabelRuleList, this.ciCustomSourceRelList, (CiTemplateInfo) null, this.getUserId(), false, (List) null);
                result.put("names", this.ciCustomGroupInfo.getCustomGroupName());
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_ADD_LABEL_ANALYSIS", this.ciCustomGroupInfo.getCustomGroupId(), nameSb.toString(), "标签微分，保存客户群【客户群ID:" + this.ciCustomGroupInfo.getCustomGroupId() + ",客户群名称:" + nameSb.toString() + "客户群描述：" + this.ciCustomGroupInfo.getCustomGroupDesc() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            } catch (Exception var13) {
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_ADD_LABEL_ANALYSIS", "-1", nameSb.toString(), "标签微分，保存客户群【客户群ID:" + this.ciCustomGroupInfo.getCustomGroupId() + ",客户群名称:" + nameSb.toString() + "客户群描述：" + this.ciCustomGroupInfo.getCustomGroupDesc() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
            }
        }

        result.put("success", Boolean.valueOf(true));
        return result;
    }

    public void countIntersect4Labels() throws Exception {
        String labelIds = "";
        HashMap result = new HashMap();

        try {
            String e = this.getNewLabelDay();
            if (this.ciLabelRuleList.size() % 2 == 0) {
                this.ciLabelRuleList = this.ciLabelRuleList.subList(0, this.ciLabelRuleList.size() - 1);
            }

            CiLabelInfo e2 = CacheBase.getInstance().getEffectiveLabel(((CiLabelRule) this.ciLabelRuleList.get(0)).getCalcuElement());
            boolean totalCount = false;
            String dateDate;
            if (e2 != null && e2.getUpdateCycle() != null && e2.getUpdateCycle().intValue() == 1) {
                dateDate = e;
            } else {
                dateDate = this.ciCustomGroupInfo.getDataDate();
            }

            int totalCount1 = this.ciLabelInfoService.getCustomNum(dateDate, Integer.valueOf(((CiLabelRule) this.ciLabelRuleList.get(0)).getCalcuElement()), Integer.valueOf(-1), Integer.valueOf(-1), (Long) null, this.userId).intValue();
            boolean count = false;
            int count1;
            if (this.ciLabelRuleList.size() == 1) {
                count1 = totalCount1;
            } else {
                String e1 = this.customersService.getCountSqlStr(this.ciLabelRuleList, this.ciCustomGroupInfo.getDataDate(), e, this.getUserId());
                this.log.debug("countSql:" + e1);
                count1 = this.customersService.queryCountInBackDB(e1);
                result.put("sql", e1);
            }

            if (totalCount1 > 0) {
                BigDecimal e3 = new BigDecimal(totalCount1);
                BigDecimal b = (new BigDecimal(count1)).multiply(new BigDecimal(100));
                double c = b.divide(e3, 2, RoundingMode.HALF_UP).doubleValue();
                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setMaximumFractionDigits(2);
                nf.setMinimumFractionDigits(2);
                result.put("percent", nf.format(c));
            } else {
                result.put("percent", "0.00");
            }

            result.put("success", Boolean.valueOf(true));
            result.put("count", Integer.valueOf(count1));

            try {
                if (this.ciLabelRuleList != null && this.ciLabelRuleList.size() > 2) {
                    for (int e4 = 2; e4 < this.ciLabelRuleList.size(); e4 += 2) {
                        labelIds = labelIds + ((CiLabelRule) this.ciLabelRuleList.get(e4)).getCalcuElement() + ",";
                    }

                    if ("1".equals(this.isLastLabel)) {
                        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_DIFFERENTIAL", ((CiLabelRule) this.ciLabelRuleList.get(0)).getCalcuElement(), IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", ((CiLabelRule) this.ciLabelRuleList.get(0)).getCalcuElement()), "标签微分,取数成功【微分标签ID：" + labelIds + "主标签ID：" + ((CiLabelRule) this.ciLabelRuleList.get(0)).getCalcuElement() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                    }
                }
            } catch (Exception var15) {
                this.log.error("log to CILogServiceUtil error", var15);
            }
        } catch (Exception var16) {
            this.log.error("标签微分，计数" + this.ciLabelRuleList + "\n" + this.ciCustomGroupInfo, var16);
            result.put("success", Boolean.valueOf(false));
            result.put("msg", "标签微分，计数" + var16.getMessage());

            try {
                if ("1".equals(this.isLastLabel)) {
                    CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_DIFFERENTIAL", ((CiLabelRule) this.ciLabelRuleList.get(0)).getCalcuElement(), IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", ((CiLabelRule) this.ciLabelRuleList.get(0)).getCalcuElement()), "标签微分,取数失败【微分标签ID：" + labelIds + "主标签ID：" + ((CiLabelRule) this.ciLabelRuleList.get(0)).getCalcuElement() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
                }
            } catch (Exception var14) {
                this.log.error("log to CILogServiceUtil error", var14);
            }
        }

        try {
            HttpServletResponse e2 = this.getResponse();
            this.sendJson(e2, JsonUtil.toJson(result));
        } catch (Exception var13) {
            this.log.error("发送json串异常", var13);
            throw new CIServiceException(var13);
        }
    }

    public void countIntersectOfCustomersAndLabel() throws Exception {
        String labelIds = "";
        CiCustomGroupInfo customGroupInfoForLog = this.customersService.queryCiCustomGroupInfo(this.ciCustomListInfo.getCustomGroupId());
        HashMap result = new HashMap();
        boolean writeLog = true;

        try {
            String e = this.getNewLabelDay();
            if (this.ciLabelRuleList != null && this.ciLabelRuleList.size() % 2 == 0) {
                this.ciLabelRuleList = this.ciLabelRuleList.subList(0, this.ciLabelRuleList.size() - 1);
                writeLog = false;
            }

            if (this.ciCustomListInfo.getListTableName() == null && this.ciCustomListInfo.getDataDate() != null && this.ciCustomListInfo.getCustomGroupId() != null) {
                this.ciCustomListInfo = this.customersService.queryCiCustomListInfoByCGroupIdAndDataDate(this.ciCustomListInfo.getCustomGroupId(), this.ciCustomListInfo.getDataDate());
            }

            int e2 = this.customersService.queryCustomerListCount(this.ciCustomListInfo);
            String countSql = "";
            int count = e2;
            if (this.ciLabelRuleList != null && this.ciLabelRuleList.size() > 0) {
                countSql = this.customersService.getIntersectSqlOfCustomersAndLabel(this.ciLabelRuleList, this.ciCustomGroupInfo.getDataDate(), e, this.ciCustomListInfo, this.getUserId());
                StringBuffer e1 = new StringBuffer();
                e1.append("select count(*) from (").append(countSql).append(") abc");
                countSql = e1.toString();
                count = this.customersService.queryCountInBackDB(countSql);

                for (int b = 0; b < this.ciLabelRuleList.size(); b += 2) {
                    labelIds = labelIds + ((CiLabelRule) this.ciLabelRuleList.get(b)).getCalcuElement() + ",";
                }
            }

            if (e2 > 0) {
                BigDecimal e3 = new BigDecimal(e2);
                BigDecimal b1 = (new BigDecimal(count)).multiply(new BigDecimal(100));
                double c = b1.divide(e3, 2, RoundingMode.HALF_UP).doubleValue();
                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setMaximumFractionDigits(2);
                nf.setMinimumFractionDigits(2);
                result.put("percent", nf.format(c));
            } else {
                result.put("percent", "0.00");
            }

            this.log.debug("countSql:" + countSql);
            result.put("success", Boolean.valueOf(true));
            result.put("count", Integer.valueOf(count));
            result.put("sql", countSql);
            if (writeLog) {
                try {
                    if (this.ciLabelRuleList != null && "1".equals(this.isLastLabel)) {
                        CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_LABEL_DIFFERENTIAL", customGroupInfoForLog.getCustomGroupId(), customGroupInfoForLog.getCustomGroupName(), "客户群标签微分,取数成功,【微分使用标签ID：" + labelIds + "主客户群ID：" + customGroupInfoForLog.getCustomGroupId() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                    }
                } catch (Exception var16) {
                    this.log.error("log to CILogServiceUtil error", var16);
                }
            }
        } catch (Exception var17) {
            this.log.error("客户群微分，计数" + this.ciLabelRuleList + this.ciCustomListInfo.getDataDate() + this.ciCustomListInfo, var17);
            result.put("success", Boolean.valueOf(false));
            result.put("msg", var17.getMessage());
            if (writeLog && this.ciLabelRuleList != null && this.ciLabelRuleList.size() > 0 && "1".equals(this.isLastLabel)) {
                try {
                    CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_LABEL_DIFFERENTIAL", customGroupInfoForLog.getCustomGroupId(), customGroupInfoForLog.getCustomGroupName(), "客户群标签微分,取数失败,【微分使用标签ID：" + labelIds + "主客户群ID：" + customGroupInfoForLog.getCustomGroupId() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
                } catch (Exception var15) {
                    this.log.error("log to CILogServiceUtil error", var15);
                }
            }
        }

        try {
            HttpServletResponse e2 = this.getResponse();
            this.sendJson(e2, JsonUtil.toJson(result));
        } catch (Exception var14) {
            this.log.error("发送json串异常", var14);
            throw new CIServiceException(var14);
        }
    }

    public String saveManyCiCustoms() throws Exception {
        this.log.debug("ciCustomGroupInfoList size=" + this.ciCustomGroupInfoList.size());
        this.ciCustomGroupInfo.setCustomGroupName(URLDecoder.decode(this.ciCustomGroupInfo.getCustomGroupName(), "UTF-8"));
        Iterator var2 = this.ciCustomGroupInfoList.iterator();

        while (var2.hasNext()) {
            CiCustomGroupInfo info = (CiCustomGroupInfo) var2.next();
            info.setCustomGroupName(URLDecoder.decode(info.getCustomGroupName(), "UTF-8"));
            info.setLabelOptRuleShow(this.ciCustomGroupInfo.getCustomGroupName() + " 且 " + info.getCustomGroupName());
            info.setCustomGroupName(this.ciCustomGroupInfo.getCustomGroupName() + " 且 " + info.getCustomGroupName());
        }

        return "saveManyCustomers";
    }

    public String customerDifferential() throws Exception {
        String fromPageFlag = this.getRequest().getParameter("fromPageFlag");
        String COC_CUS_LABEL_DIFF_LINK = "";
        if ("1".equals(fromPageFlag)) {
            COC_CUS_LABEL_DIFF_LINK = "COC_CUS_LABEL_DIFF_LINK_INDEX";
        } else {
            COC_CUS_LABEL_DIFF_LINK = "COC_CUS_LABEL_DIFF_LINK";
        }

        CiCustomListInfo info = null;
        if (StringUtils.isNotBlank(this.ciCustomListInfo.getListTableName())) {
            info = this.customersService.queryCiCustomListInfoById(this.ciCustomListInfo.getListTableName());
        } else if (StringUtils.isBlank(this.ciCustomListInfo.getDataDate())) {
            List infoList = this.customersService.queryCiCustomListInfoByCGroupId(this.ciCustomListInfo.getCustomGroupId());
            if (infoList.size() > 0) {
                Iterator var6 = infoList.iterator();

                while (var6.hasNext()) {
                    CiCustomListInfo ciCustomListInfo = (CiCustomListInfo) var6.next();
                    if (3 == ciCustomListInfo.getDataStatus().intValue()) {
                        info = ciCustomListInfo;
                        break;
                    }
                }
            }
        } else {
            info = this.customersService.queryCiCustomListInfoByCGroupIdAndDataDate(this.ciCustomListInfo.getCustomGroupId(), this.ciCustomListInfo.getDataDate());
        }

        this.ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(this.ciCustomListInfo.getCustomGroupId());
        if (info == null) {
            CILogServiceUtil.getLogServiceInstance().log(COC_CUS_LABEL_DIFF_LINK, this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "客户群标签微分【客户群ID：" + this.ciCustomGroupInfo.getCustomGroupId() + "名称：" + this.ciCustomGroupInfo.getCustomGroupName() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
            throw new Exception("客户群清单不存在，" + this.ciCustomListInfo);
        } else {
            CILogServiceUtil.getLogServiceInstance().log(COC_CUS_LABEL_DIFF_LINK, this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "客户群标签微分【客户群ID：" + this.ciCustomGroupInfo.getCustomGroupId() + "名称：" + this.ciCustomGroupInfo.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            this.ciCustomListInfo = info;
            this.getRequest().setAttribute("ciCustomGroupInfo", this.ciCustomGroupInfo);
            return "customerDifferential";
        }
    }

    public void findCustomGroupTree() throws Exception {
        List treeList = null;
        boolean success = false;
        HashMap result = new HashMap();

        try {
            treeList = this.customersService.queryCustomGroupTree(this.customGroupName);
            success = true;
            result.put("treeList", treeList);
            result.put("success", Boolean.valueOf(success));
            if (StringUtils.isNotBlank(this.customGroupName)) {
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_TREE_SELECT", (String) null, (String) null, "查询客户群树名称中含有【" + this.customGroupName + "】的记录成功", OperResultEnum.Success, LogLevelEnum.Medium);
            }
        } catch (CIServiceException var7) {
            String e = "获得标签树失败";
            this.log.error(e, var7);
            success = false;
            result.put("msg", e);
            result.put("success", Boolean.valueOf(success));
            if (StringUtils.isNotBlank(this.customGroupName)) {
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_TREE_SELECT", (String) null, (String) null, "查询客户群树名称中含有【" + this.customGroupName + "】的记录失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            }
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void findNewestMonth() throws Exception {
        this.newLabelMonth = this.getNewLabelMonth();
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(this.newLabelMonth));
        } catch (Exception var3) {
            this.log.error("发送json串异常", var3);
            throw new CIServiceException(var3);
        }
    }

    public void findNewestDay() throws Exception {
        this.newLabelDay = this.getNewLabelDay();
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(this.newLabelDay));
        } catch (Exception var3) {
            this.log.error("发送json串异常", var3);
            throw new CIServiceException(var3);
        }
    }

    public void publishCustomer() throws Exception {
        boolean success = true;
        String msg = "";
        HashMap result = new HashMap();
        this.ciCustomPushReq.setUserId(this.getUserId());
        this.ciCustomPushReq.setReqTime(new Date());
        this.ciCustomPushReq.setStatus(Integer.valueOf(1));
        this.ciCustomPushReq.setReqId("COC" + CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L));
        if (this.ciCustomPushReq == null || StringUtils.isEmpty(this.ciCustomPushReq.getSysId())) {
            success = false;
            msg = "参数错误，系统信息不正确";
        }

        if (this.ciCustomListInfo != null) {
            this.ciCustomListInfo = this.customersService.queryCiCustomListInfoByCGroupIdAndDataDate(this.ciCustomListInfo.getCustomGroupId(), this.ciCustomListInfo.getDataDate());
        }

        if (this.ciCustomListInfo == null) {
            success = false;
            msg = "参数错误，清单信息不正确";
        }

        if (success) {
            this.ciCustomPushReq.setListTableName(this.ciCustomListInfo.getListTableName());

            try {
                ArrayList response = new ArrayList();
                response.add(this.ciCustomPushReq.getSysId());
                this.ciCustomPushReq.setSysIds(response);
                ArrayList e = new ArrayList();
                e.add(this.ciCustomPushReq);
                this.customersService.pushCustomers(e, String.valueOf(1));
                msg = "已提交客户群推送任务，完成时会在个人通知中通知您。";
                success = true;
                CILogServiceUtil.getLogServiceInstance().log("COC_CUS_LIST_PUSH", this.ciCustomListInfo.getCustomGroupId(), "推送", "客户群清单推送成功【客户群ID:" + (this.ciCustomListInfo.getCustomGroupId() == null ? "无" : this.ciCustomListInfo.getCustomGroupId()) + "】", OperResultEnum.Success, LogLevelEnum.Risk);
            } catch (Exception var7) {
                msg = var7.getMessage();
                success = false;
                CILogServiceUtil.getLogServiceInstance().log("COC_CUS_LIST_PUSH", this.ciCustomListInfo.getCustomGroupId(), "推送", "客户群清单推送失败【客户群ID:" + (this.ciCustomListInfo.getCustomGroupId() == null ? "无" : this.ciCustomListInfo.getCustomGroupId()) + "】", OperResultEnum.Failure, LogLevelEnum.Risk);
            }
        }

        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void pushCustomerAfterSave() throws Exception {
        boolean success = true;
        String msg = "推送设置成功！";
        HashMap result = new HashMap();

        try {
            this.ciCustomPushReq = this.genCiCustomPushReq(this.ciCustomGroupInfo.getCustomGroupId());
            if (this.ciCustomPushReq != null) {
                this.customersService.pushCustomersAfterSave(this.ciCustomPushReq, this.pushCycle, this.ciCustomGroupInfo);
            }
        } catch (Exception var7) {
            msg = "推送设置失败，请联系管理员！";
            success = false;
        }

        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    private CiCustomPushReq genCiCustomPushReq(String customGroupId) {
        CiCustomPushReq req = new CiCustomPushReq();
        req.setUserId(this.getUserId());
        req.setReqTime(new Date());
        req.setStatus(Integer.valueOf(1));
        this.ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
        List customListList = this.customersService.queryCiCustomListInfoByCGroupId(this.ciCustomGroupInfo.getCustomGroupId());
        if (customListList == null) {
            return null;
        } else {
            CiCustomListInfo customList = (CiCustomListInfo) customListList.get(0);
            if (customList == null) {
                return null;
            } else {
                req.setListTableName(customList.getListTableName());
                req.setSysIds(this.sysIds);
                req.setReqId("COC" + CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L));
                return req;
            }
        }
    }

    public void pushCustomerGroupSingle() throws Exception {
        boolean success = true;
        String msg = "";
        HashMap result = new HashMap();
        if (success) {
            try {
                this.ciCustomPushReq = this.genCiCustomPushReq(this.ciCustomGroupInfo.getCustomGroupId());
                this.customersService.pushCustomersAfterSave(this.ciCustomPushReq, this.pushCycle, this.ciCustomGroupInfo);
                msg = "推送设置成功！";
                success = true;
                if (this.sysIds == null && (String.valueOf(2).equals(this.pushCycle) || String.valueOf(3).equals(this.pushCycle))) {
                    msg = "取消推送设置成功！";
                }

                if (this.sysIds == null && this.ciCustomGroupInfo.getUpdateCycle().intValue() == 1) {
                    success = false;
                    msg = "请选择推送平台！";
                }

                if (this.sysIds == null && (this.ciCustomGroupInfo.getUpdateCycle().intValue() == 2 || this.ciCustomGroupInfo.getUpdateCycle().intValue() == 3) && !"1".equals(this.isPush)) {
                    success = false;
                    msg = "请选择推送平台！";
                }

                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_PUSH_SET", this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "客户群推送设置", OperResultEnum.Success, LogLevelEnum.Risk);
            } catch (Exception var7) {
                msg = "推送设置失败，请联系管理员！";
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_PUSH_SET", this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "客户群推送设置失败", OperResultEnum.Failure, LogLevelEnum.Risk);
                success = false;
            }
        }

        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void pushCustomerGroupBatch() throws Exception {
        boolean success = false;
        String msg = "";
        HashMap result = new HashMap();
        if (this.ciCustomPushReq == null) {
            this.ciCustomPushReq = new CiCustomPushReq();
        }

        this.ciCustomPushReq.setUserId(this.getUserId());
        this.ciCustomPushReq.setReqTime(new Date());
        this.ciCustomPushReq.setStatus(Integer.valueOf(1));
        this.ciCustomPushReq.setReqId("COC" + CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L));
        if (this.ciCustomGroupInfoList == null) {
            msg = "参数错误，客户群信息提交错误！";
        } else {
            try {
                msg = "批量推送设置成功!";
                boolean response = false;
                if (this.sysIds == null && (String.valueOf(2).equals(this.pushCycle) || String.valueOf(3).equals(this.pushCycle))) {
                    Iterator customGroupInfo = this.ciCustomGroupInfoList.iterator();

                    while (customGroupInfo.hasNext()) {
                        CiCustomGroupInfo e = (CiCustomGroupInfo) customGroupInfo.next();
                        List pushedCycle = this.customersService.queryPushCycleByGroupId(e.getCustomGroupId());
                        if (pushedCycle.size() > 0) {
                            success = true;
                            msg = "取消推送设置成功!";
                            break;
                        }
                    }

                    if (!success) {
                        response = true;
                        msg = "请选择推送平台";
                    }
                }

                if (!response) {
                    success = true;
                }

                ArrayList e1 = new ArrayList();
                Iterator pushedCycle1 = this.ciCustomGroupInfoList.iterator();

                while (pushedCycle1.hasNext()) {
                    CiCustomGroupInfo customGroupInfo1 = (CiCustomGroupInfo) pushedCycle1.next();
                    List customListList = this.customersService.queryCiCustomListInfoByCGroupId(customGroupInfo1.getCustomGroupId());
                    CiCustomListInfo customList = (CiCustomListInfo) customListList.get(0);
                    CiCustomPushReq customPushReg = this.ciCustomPushReq.clone();
                    customPushReg.setListTableName(customList.getListTableName());
                    customPushReg.setSysIds(this.sysIds);
                    e1.add(customPushReg);
                }

                this.customersService.pushCustomers(e1, this.pushCycle);
                if (this.sysIds == null && String.valueOf(1).equals(this.pushCycle)) {
                    success = false;
                    msg = "请选择推送平台!";
                }

                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_BATCH_PUSH_SET", "-1", "客户群批量推送设置", "客户群批量推送设置", OperResultEnum.Success, LogLevelEnum.Risk);
            } catch (Exception var12) {
                msg = "推送设置失败，请联系管理员！";
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_BATCH_PUSH_SET", "-1", "客户群批量推送设置", "客户群批量推送设置失败", OperResultEnum.Failure, LogLevelEnum.Risk);
                success = false;
            }
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

    public void reGenerateCustomer() {
        boolean success = false;
        String msg = "";
        HashMap result = new HashMap();

        try {
            if (this.ciCustomGroupInfo != null && this.ciCustomGroupInfo.getCustomGroupId() != null) {
                if (StringUtil.isNotEmpty(this.ciCustomGroupInfo.getDataDate())) {
                    this.listTableName = this.customersService.getLastListTableName(this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getDataDate());
                }

                this.ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupInfo.getCustomGroupId());
                if (this.ciCustomGroupInfo.getStatus().intValue() != 0 && 2 != this.ciCustomGroupInfo.getDataStatus().intValue()) {
                    if (this.ciCustomGroupInfo != null) {
                        this.customersService.reGenerate(this.ciCustomGroupInfo, this.getUserId(), this.listTableName);
                        msg = "已提重新生成任务，完成时会在个人通知中通知您。";
                        success = true;
                        CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_REGENERATE", this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "客户群重新生成成功【客户群ID：" + this.ciCustomGroupInfo.getCustomGroupId() + "名称：" + this.ciCustomGroupInfo.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                    } else {
                        msg = "客户群不存在";
                    }
                } else {
                    success = false;
                    msg = "不能重新生成已删除或者创建中的客户群！";
                }
            } else {
                msg = "参数不正确";
            }
        } catch (Exception var7) {
            this.log.error(var7);
            msg = var7.getMessage();
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_REGENERATE", "-1", this.ciCustomGroupInfo.getCustomGroupName(), "客户群重新生成失败【客户群ID：" + this.ciCustomGroupInfo.getCustomGroupId() + "名称：" + this.ciCustomGroupInfo.getCustomGroupName() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void checkReGenerateCustomer() {
        boolean success = false;
        String msg = "";
        HashMap result = new HashMap();

        try {
            if (this.ciCustomGroupInfo != null && this.ciCustomGroupInfo.getCustomGroupId() != null) {
                this.ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupInfo.getCustomGroupId());
                if (this.ciCustomGroupInfo != null && this.ciCustomGroupInfo.getDataStatus().intValue() != 3) {
                    success = true;
                    msg = "抱歉，该客户群正在被使用，无法重新生成，请稍后再试";
                }
            } else {
                msg = "参数不正确";
            }
        } catch (Exception var7) {
            this.log.error(var7);
        }

        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void validateCreateCustomGroupSql() {
        String jsonObj = null;
        HashMap result = new HashMap();
        String validateSql = "";
        boolean flag = false;

        try {
            HttpSession response = PrivilegeServiceUtil.getSession();
            this.ciLabelRuleList = (List) response.getAttribute("sessionModelList");
            Set e1 = this.findInvalidCustomListInfoIds(this.ciLabelRuleList);
            if (e1 != null && e1.size() > 0) {
                result.put("msg", "您选择的客户群已失效！");
                result.put("ids", e1);
                flag = false;
            } else {
                validateSql = this.customersService.getSelectSqlByCustomersRels(this.ciGroupAttrRelList, this.ciLabelRuleList, this.ciCustomGroupInfo.getMonthLabelDate(), this.ciCustomGroupInfo.getDayLabelDate(), this.ciCustomGroupInfo.getDataDate(), this.getUserId(), (Integer) null, (Integer) null, true);
                this.log.info(validateSql + validateSql);
                StringBuffer validateSqlBuf = new StringBuffer();
                validateSqlBuf.append("select 1 from (").append(validateSql).append(") where 1=2");
                validateSql = validateSqlBuf.toString();
                this.log.info("COC_SQL >>>>>>> " + validateSql);
                flag = true;
            }
        } catch (Exception var10) {
            String e = "SQL拼接异常";
            this.log.error(e, var10);
            result.put("msg", e + "，请联系管理员！");
            flag = false;
        }

        if (flag) {
            flag = this.customersService.queryValidateSql(validateSql);
            if (!flag) {
                result.put("msg", "您拖动的条件验证未通过，请检查所选条件是否正确！");
            } else {
                result.put("msg", "ok");
            }
        }

        result.put("success", Boolean.valueOf(flag));

        try {
            jsonObj = JsonUtil.toJson(result);
        } catch (IOException var9) {
            this.log.error("json转换异常", var9);
            throw new CIServiceException("json转换异常", var9);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, jsonObj);
        } catch (Exception var8) {
            this.log.error("发送json串异常", var8);
            throw new CIServiceException("发送json串异常");
        }
    }

    public void attrDiffer() {
        String fromPageFlag = this.getRequest().getParameter("fromPageFlag");
        String COC_CUS_INDEX_DIFF_LINK = "";
        if ("1".equals(fromPageFlag)) {
            COC_CUS_INDEX_DIFF_LINK = "COC_CUS_INDEX_DIFF_LINK_INDEX";
        } else {
            COC_CUS_INDEX_DIFF_LINK = "COC_CUS_INDEX_DIFF_LINK";
        }

        boolean success = false;
        String msg = "";
        HashMap result = new HashMap();
        this.ciCustomPushReq = new CiCustomPushReq();
        this.ciCustomPushReq.setUserId(PrivilegeServiceUtil.getUserId());
        this.ciCustomPushReq.setSysId(Configure.getInstance().getProperty("TO_SA_SYS_ID"));
        CiCustomGroupInfo cust = this.customersService.queryCiCustomGroupInfo(this.ciCustomListInfo.getCustomGroupId());
        if (this.ciCustomListInfo != null) {
            if (1 == cust.getUpdateCycle().intValue()) {
                this.ciCustomListInfo = (CiCustomListInfo) this.customersService.queryCiCustomListInfoByCGroupId(this.ciCustomListInfo.getCustomGroupId()).get(0);
            } else {
                this.ciCustomListInfo = this.customersService.queryCiCustomListInfoByCGroupIdAndDataDate(this.ciCustomListInfo.getCustomGroupId(), this.ciCustomListInfo.getDataDate());
            }
        }

        if (this.ciCustomListInfo == null) {
            success = false;
            msg = "参数错误，清单信息不正确";
            this.log.error(msg);
        } else {
            this.ciCustomPushReq.setListTableName(this.ciCustomListInfo.getListTableName());
            List response = this.customersService.queryCiCustomPushReq(this.ciCustomPushReq);
            Calendar e = Calendar.getInstance();
            int offset = -1;

            try {
                offset = -1 * Math.abs(Integer.valueOf(Configure.getInstance().getProperty("SA_FILE_EFFCITVE_DAY")).intValue());
            } catch (NumberFormatException var15) {
                this.log.error(" worng config SA_FILE_EFFCITVE_DAY = " + Configure.getInstance().getProperty("SA_FILE_EFFCITVE_DAY"));
            }

            e.add(5, offset);
            if (response.size() > 0) {
                Iterator var11 = response.iterator();

                while (var11.hasNext()) {
                    CiCustomPushReq e1 = (CiCustomPushReq) var11.next();
                    if (e1.getReqTime().after(e.getTime()) && e1.getStatus().intValue() != 0) {
                        this.ciCustomPushReq = e1;
                        break;
                    }
                }
            }

            if (this.ciCustomPushReq.getReqId() == null) {
                try {
                    this.ciCustomPushReq.setReqId("COC" + CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L));
                    this.ciCustomPushReq.setReqTime(new Date());
                    this.ciCustomPushReq.setStatus(Integer.valueOf(1));
                    this.customersService.pushSaCustomers(this.ciCustomPushReq, String.valueOf(1));
                    msg = this.ciCustomPushReq.getReqId();
                    success = true;
                } catch (Exception var14) {
                    msg = var14.getMessage();
                    success = false;
                    this.log.error(msg);
                }
            } else {
                success = true;
                msg = this.ciCustomPushReq.getReqId();
            }
        }

        if (success) {
            String response1 = Configure.getInstance().getProperty("SA_URL");
            if (StringUtils.isEmpty(response1)) {
                success = false;
                msg = "自助分析平台URL未配置";
            } else {
                if (response1.contains("?")) {
                    msg = response1 + "&SyncTaskId=" + msg + "&staffCode=" + this.getUserId();
                } else {
                    msg = response1 + "?SyncTaskId=" + msg + "&staffCode=" + this.getUserId();
                }

                if (Boolean.valueOf(Configure.getInstance().getProperty("SA_IS_NEED_SSO").toLowerCase().trim()).booleanValue()) {
                    try {
                        msg = msg + this.saveSSOLoginInfo4SA();
                    } catch (Exception var13) {
                        success = false;
                        msg = "调取自助分析平台嵌入页面前LKG_LOGIN_RANDOMCODE表插入记录失败";
                        this.log.error(msg, var13);
                    }
                }
            }

            if (success) {
                CILogServiceUtil.getLogServiceInstance().log(COC_CUS_INDEX_DIFF_LINK, this.ciCustomListInfo.getCustomGroupId(), cust.getCustomGroupName(), "客户群指标微分页面加载成功，参数【客户群ID:" + cust.getCustomGroupId() + "，客户群名称：" + cust.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            } else {
                CILogServiceUtil.getLogServiceInstance().log(COC_CUS_INDEX_DIFF_LINK, this.ciCustomListInfo.getCustomGroupId(), cust.getCustomGroupName(), "客户群指标微分页面加载失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            }
        } else {
            CILogServiceUtil.getLogServiceInstance().log(COC_CUS_INDEX_DIFF_LINK, this.ciCustomListInfo.getCustomGroupId(), cust.getCustomGroupName(), "客户群指标微分页面加载失败", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);
        HttpServletResponse response2 = this.getResponse();

        try {
            this.sendJson(response2, JsonUtil.toJson(result));
        } catch (Exception var12) {
            this.log.error("发送json串异常", var12);
            throw new CIServiceException(var12);
        }
    }

    private String saveSSOLoginInfo4SA() throws Exception {
        String saSchema = Configure.getInstance().getProperty("SA_SCHEMA").trim();
        String userId = PrivilegeServiceUtil.getUserId();
        String randomCode = "" + (new Date()).getTime();

        try {
            JdbcBaseDao e = (JdbcBaseDao) SystemServiceLocator.getInstance().getService("jdbcBaseDao");
            SimpleJdbcTemplate saDBTemplate = e.getSimpleJdbcTemplate(Configure.getInstance().getProperty("SA_JNDI"));
            StringBuffer sbff = (new StringBuffer()).append("INSERT INTO ").append(saSchema).append(".LKG_LOGIN_RANDOMCODE(RANDOM_CODE, STAFF_ID, OPER_TIME, INVALID_FLAG) VALUES(?,?,?,0)");
            saDBTemplate.update(sbff.toString(), new Object[]{randomCode, userId, new Date()});
        } catch (Exception var7) {
            this.log.error("调取自助分析平台嵌入页面前LKG_LOGIN_RANDOMCODE表插入记录失败", var7);
            throw new Exception(var7);
        }

        return "&randomCode=" + randomCode;
    }

    public String findGroupList() throws Exception {
        boolean success = false;
        String msg = "";
        CiCustomGroupInfo cg = null;

        try {
            cg = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupInfo.getCustomGroupId());
            success = true;
            CiCustomListInfo e = this.customersService.queryCustomListInfo(this.ciCustomGroupInfo);
            if (e == null) {
                success = false;
                msg = "该客户群无清单信息";
            } else {
                if (success && (cg.getStatus().intValue() == 0 || 2 == cg.getDataStatus().intValue())) {
                    success = false;
                    msg = "已删除的客户群不能查看清单！";
                }

                if (success) {
                    CiCustomListInfo listInfo = this.customersService.queryCustomListInfo(this.ciCustomGroupInfo);
                    if (listInfo != null) {
                        List groupAttrRelList = this.ciGroupAttrRelService.queryGroupAttrRelList(listInfo.getCustomGroupId(), listInfo.getDataTime());
                        Object attrRelList = new ArrayList();
                        if (groupAttrRelList == null || groupAttrRelList.size() != 1 || groupAttrRelList.get(0) == null || ((CiGroupAttrRel) groupAttrRelList.get(0)).getStatus() == null || ((CiGroupAttrRel) groupAttrRelList.get(0)).getStatus().intValue() != 2) {
                            attrRelList = groupAttrRelList;
                        }

                        this.getRequest().setAttribute("groupAttrRelList", attrRelList);
                    }
                }
            }
        } catch (Exception var8) {
            success = false;
            msg = "客户群清单表头查询出错";
            this.log.error(msg, var8);
            throw new CIServiceException(var8);
        }

        this.resultMap = new HashMap();
        this.resultMap.put("success", Boolean.valueOf(success));
        this.resultMap.put("msg", msg);
        return "queryGroupList";
    }

    public void findGroupInfoList() throws Exception {
        boolean success = false;
        String msg = "";
        List list = null;
        HashMap result = new HashMap();

        try {
            if (this.pager == null) {
                this.pager = new Pager();
            }

            CiCustomListInfo response = this.customersService.queryCustomListInfo(this.ciCustomGroupInfo);
            if (response != null && response.getDataStatus().intValue() == 3) {
                this.pager.setPageNum(this.page);
                this.pager.setPageSize(this.rows);
                this.pager.setTotalSize(response.getCustomNum().longValue());
                list = this.customersService.queryCustomerPhoneNumList(this.pager.getPageNum(), this.pager.getPageSize(), this.ciCustomGroupInfo);
                success = true;
            } else {
                this.pager.setTotalSize(0L);
            }
        } catch (Exception var8) {
            msg = "查询客户群清单数据错误";
            this.log.error(msg, var8);
            success = false;
        }

        if (success) {
            result.put("total", Long.valueOf(this.pager.getTotalSize()));
            result.put("rows", list);
        } else {
            result.put("msg", msg);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
        }

    }

    public void createTemplateByCustomers() throws Exception {
        String customId = this.ciCustomGroupInfo.getCustomGroupId();
        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(customId);
        List customRuleList = this.customersService.queryCiLabelRuleList(customGroupInfo.getCustomGroupId(), Integer.valueOf(1));
        Map returnMap = null;

        String e;
        try {
            String response = this.getUserId();
            returnMap = this.ciTemplateInfoService.isNameExist(this.ciTemplateInfo, response);
            if (((Boolean) returnMap.get("success")).booleanValue()) {
                this.ciTemplateInfo.setIsPrivate(Integer.valueOf(1));
                this.ciTemplateInfo.setTemplateDesc(customGroupInfo.getCustomGroupDesc());
                this.ciTemplateInfo.setLabelOptRuleShow(customGroupInfo.getLabelOptRuleShow());
                this.ciTemplateInfoService.addTemplateInfo(this.ciTemplateInfo, customRuleList, response);
                e = "创建成功！";
                returnMap.put("msg", e);
                CILogServiceUtil.getLogServiceInstance().log("COC_TEMPLATE_MANAGE_ADD", this.ciTemplateInfo.getTemplateId(), this.ciTemplateInfo.getTemplateName(), "创建模板【" + this.ciTemplateInfo.getTemplateName() + "】成功", OperResultEnum.Success, LogLevelEnum.Medium);
            }
        } catch (Exception var8) {
            e = "创建模板";
            this.log.error(e + " : " + this.ciTemplateInfo.getTemplateName() + " 失败", var8);
            returnMap.put("msg", e + "失败");
            returnMap.put("success", Boolean.valueOf(false));
            CILogServiceUtil.getLogServiceInstance().log("COC_TEMPLATE_MANAGE_ADD", "-1", "创建模板错误", "创建模板【" + this.ciTemplateInfo.getTemplateName() + "】失败", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMap));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException(var7);
        }
    }

    public String latestCustomerList() throws Exception {
        try {
            this.rankingListType = 1;
            List e = this.customersService.queryCiSysInfo(1);
            if (e != null && e.size() > 0) {
                this.havePushFun = Integer.valueOf(1);
            } else {
                this.havePushFun = Integer.valueOf(0);
            }

            if (this.ciCustomGroupInfo == null) {
                this.ciCustomGroupInfo = new CiCustomGroupInfo();
            }

            if (StringUtils.isBlank(this.ciCustomGroupInfo.getDataDate())) {
                this.newLabelMonth = this.getNewLabelMonth();
            }

            String userId = this.getUserId();
            String createCityId = PrivilegeServiceUtil.getCityIdFromSession();
            String root = Configure.getInstance().getProperty("CENTER_CITYID");
            if (!PrivilegeServiceUtil.isAdminUser(userId)) {
                this.ciCustomGroupInfo.setCreateUserId(userId);
                this.ciCustomGroupInfo.setCreateCityId(createCityId);
            } else if (!createCityId.equals(root)) {
                this.ciCustomGroupInfo.setCreateCityId(createCityId);
            }

            this.ciCustomGroupInfo.setIsPrivate(Integer.valueOf(0));
            if (this.pager == null) {
                this.pager = new Pager((long) ServiceConstants.SHOW_RECOMMEND_CUSTOMS_NUM);
            }

            this.pager.setPageNum(1);
            this.pager.setPageSize(ServiceConstants.SHOW_RECOMMEND_CUSTOMS_NUM);
            this.pager.setTotalPage(1);
            this.ciCustomGroupInfoList = this.customersService.queryCustomersList(this.pager, this.newLabelMonth, this.ciCustomGroupInfo);
            String customerAnalysisMenu = Configure.getInstance().getProperty("CUSTOMER_ANALYSIS");
            this.ciCustomGroupInfo.setCustomerAnalysisMenu(customerAnalysisMenu);
            this.log.info("Exit CustomersManagerAction.search() method");
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_SELECT", "", StringUtil.isEmpty(this.ciCustomGroupInfo.getCustomGroupName()) ? "" : this.ciCustomGroupInfo.getCustomGroupName(), "查询最新客户群名称中含有【" + (StringUtil.isEmpty(this.ciCustomGroupInfo.getCustomGroupName()) ? "" : this.ciCustomGroupInfo.getCustomGroupName()) + "】的记录成功", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var6) {
            this.log.error("客户群列表查询出错：" + var6);
            throw new CIServiceException(var6);
        }

        this.pager.setResult(this.ciCustomGroupInfoList);
        return "sysHotCustomRankingList";
    }

    public void findLatestCustomTotalNum() throws Exception {
        if (this.ciCustomGroupInfo == null) {
            this.ciCustomGroupInfo = new CiCustomGroupInfo();
        }

        if (this.pager == null) {
            this.pager = new Pager();
        }

        boolean success = false;
        HashMap result = new HashMap();
        String msg = "";
        int totalPage = 0;
        String userId = this.getUserId();
        String createCityId = PrivilegeServiceUtil.getCityIdFromSession();
        String root = Configure.getInstance().getProperty("CENTER_CITYID");
        if (!PrivilegeServiceUtil.isAdminUser(userId)) {
            this.ciCustomGroupInfo.setCreateUserId(userId);
            this.ciCustomGroupInfo.setCreateCityId(createCityId);
        } else if (!createCityId.equals(root)) {
            this.ciCustomGroupInfo.setCreateCityId(createCityId);
        }

        this.ciCustomGroupInfo.setIsPrivate(Integer.valueOf(0));
        Date modifyEndDate = new Date();
        Date modifyStartDate = DateUtil.getStartDate(modifyEndDate, 15);
        if (modifyStartDate != null) {
            this.ciCustomGroupInfo.setModifyStartDate(modifyStartDate);
        }

        if (modifyEndDate != null) {
            this.ciCustomGroupInfo.setModifyEndDate(modifyEndDate);
        }

        int totalNum = 0;

        try {
            byte response = 5;
            int e = this.customersService.queryCustomersCount(this.ciCustomGroupInfo);
            totalNum = e;
            totalPage = (int) Math.ceil((double) e / (double) response);
            this.pager.setTotalPage(totalPage);
            this.pager.setPageSize(response);
            success = true;
            String name = this.ciCustomGroupInfo.getCustomGroupName();
            CILogServiceUtil.getLogServiceInstance().log("COC_HOME_CUTOMER_SELECT", "", name, "群乐享客户群查询成功，查询条件：【" + (StringUtil.isEmpty(name) ? "无" : "客户群名称：" + name) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var15) {
            msg = "查询客户群列表总页数错误";
            this.log.error(msg, var15);
            success = false;
        }

        result.put("success", Boolean.valueOf(success));
        if (success) {
            result.put("totalPage", Integer.valueOf(totalPage));
            result.put("totalSize", Integer.valueOf(totalNum));
        } else {
            result.put("msg", msg);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var14) {
            this.log.error("发送json串异常", var14);
            throw new CIServiceException(var14);
        }
    }

    public String loadPagedCustomGroupMarketIndex() throws Exception {
        CacheBase cache = CacheBase.getInstance();
        this.dimScenes = (List<DimScene>) cache.getObjectList("DIM_SCENE");
        this.customCreateTypes = (List<DimCustomCreateType>) cache.getObjectList("DIM_CUSTOM_CREATE_TYPE");
        this.customDataStatus = (List<DimCustomDataStatus>) cache.getObjectList("DIM_CUSTOM_DATA_STATUS");
        Map cityMapT = cache.getCityMap();
        this.cityMap.put(Configure.getInstance().getProperty("CENTER_CITYID"), "省中心");
        CopyOnWriteArrayList onlyCityIds = cache.getKeyList("CITY_IDS_LIST");
        Iterator var5 = onlyCityIds.iterator();

        while (var5.hasNext()) {
            String s = (String) var5.next();
            Object cityName = cityMapT.get(Integer.valueOf(s));
            this.cityMap.put(s, cityName.toString());
        }

        CILogServiceUtil.getLogServiceInstance().log("COC_HOME_CUTOMER_MARKET_LINK", "", "", "客户群集市链接跳转成功", OperResultEnum.Success, LogLevelEnum.Normal);
        return "customGroupMarketIndex";
    }

    public void createCustomByImportTable() throws Exception {
        Map returnMap = null;

        try {
            String response = PrivilegeServiceUtil.getCityIdFromSession();
            this.ciCustomGroupInfo.setCreateCityId(response);
            this.ciCustomGroupInfo.setCreateTypeId(Integer.valueOf(12));
            ArrayList var16 = new ArrayList();
            String userId = this.getUserId();
            if (StringUtil.isNotEmpty(this.sceneIds)) {
                String[] dbType = this.sceneIds.split(",");

                for (int ciGroupAttrRelList = 0; ciGroupAttrRelList < dbType.length; ++ciGroupAttrRelList) {
                    if (StringUtil.isNotEmpty(dbType[ciGroupAttrRelList].trim())) {
                        CiCustomSceneRel relatedColumn = new CiCustomSceneRel();
                        relatedColumn.setId(new CiCustomSceneRelId());
                        relatedColumn.getId().setSceneId(dbType[ciGroupAttrRelList].trim());
                        relatedColumn.getId().setUserId(userId);
                        relatedColumn.setStatus(1);
                        var16.add(relatedColumn);
                    }
                }

                this.ciCustomGroupInfo.setSceneList(var16);
            }

            if (StringUtil.isNotEmpty(this.ciCustomGroupInfo.getIsPrivate())) {
                if (this.ciCustomGroupInfo.getIsPrivate().intValue() == 1) {
                    returnMap = this.customersService.isNameExist(this.ciCustomGroupInfo, userId);
                }

                if (this.ciCustomGroupInfo.getIsPrivate().intValue() == 0) {
                    returnMap = this.customersService.isShareNameExist(this.ciCustomGroupInfo, userId, (String) null);
                    if (StringUtil.isNotEmpty(returnMap.get("msg"))) {
                        returnMap.put("cmsg", returnMap.get("msg"));
                    }
                }
            } else {
                this.ciCustomGroupInfo.setIsPrivate(Integer.valueOf(1));
                returnMap = this.customersService.isNameExist(this.ciCustomGroupInfo, userId);
            }

            String var17 = Configure.getInstance().getProperty("CI_DBTYPE");
            ArrayList var18 = new ArrayList();
            String var19 = Configure.getInstance().getProperty("RELATED_COLUMN");
            if (this.importColumnList != null && this.importColumnList.size() > 0) {
                for (int msg = 0; msg < this.importColumnList.size(); ++msg) {
                    ImportColumn importColumn = (ImportColumn) this.importColumnList.get(msg);
                    CiGroupAttrRel rel = new CiGroupAttrRel();
                    CiGroupAttrRelId relId = new CiGroupAttrRelId();
                    String attrCol = "";
                    if (StringUtil.isNotEmpty(importColumn.getColumnName())) {
                        attrCol = importColumn.getColumnName();
                    }

                    if (!attrCol.equalsIgnoreCase(var19)) {
                        rel.setAttrColName(importColumn.getColumnCnName());
                        rel.setAttrSource(1);
                        if (StringUtil.isNotEmpty(importColumn.getColumnType())) {
                            rel.setAttrColType(importColumn.getColumnType());
                        } else if ("ORACLE".equalsIgnoreCase(var17)) {
                            rel.setAttrColType("VARCHAR2(4000)");
                        } else {
                            rel.setAttrColType("VARCHAR(4000)");
                        }

                        relId.setAttrCol(attrCol);
                        rel.setId(relId);
                        var18.add(rel);
                    }
                }
            }

            if (((Boolean) returnMap.get("success")).booleanValue()) {
                this.customersService.addCiCustomGroupInfo(this.ciCustomGroupInfo, (List) null, (List) null, (CiTemplateInfo) null, userId, false, var18);
                String var20 = "创建成功！";
                returnMap.put("cmsg", var20);
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_IMPORT", this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "创建客户群【" + this.ciCustomGroupInfo.getCustomGroupName() + "】成功", OperResultEnum.Success, LogLevelEnum.Medium);
            }
        } catch (Exception var14) {
            String e = "创建客户群";
            this.log.error(e + " : " + this.ciCustomGroupInfo.getCustomGroupName() + " 失败", var14);
            returnMap.put("cmsg", e + "失败");
            returnMap.put("success", Boolean.valueOf(false));
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_IMPORT", "-1", this.ciCustomGroupInfo.getCustomGroupName(), "创建客户群【" + this.ciCustomGroupInfo.getCustomGroupName() + "】失败", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        HttpServletResponse var15 = this.getResponse();

        try {
            this.sendJson(var15, JsonUtil.toJson(returnMap));
        } catch (Exception var13) {
            this.log.error("发送json串异常", var13);
            throw new CIServiceException(var13);
        }
    }

    public void isRemovable() {
        HashMap returnMap = new HashMap();
        ReturnMsgModel msg = this.customersService.deleteCustomGroupById(this.customGroupName, this.getUserId());
        returnMap.put("success", Boolean.valueOf(msg.getSuccess()));
        returnMap.put("msg", msg.getMsg());
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(returnMap));
        } catch (Exception var5) {
            this.log.error("发送json串异常", var5);
            throw new CIServiceException(var5);
        }
    }

    public void addUserAttentionCustom() {
        String userId = PrivilegeServiceUtil.getUserId();
        String versionFlag = "false";
        Cookie[] myCookies = this.getRequest().getCookies();

        for (int customGroupId = 0; customGroupId < myCookies.length; ++customGroupId) {
            Cookie ciCustomGroupInfoNow = myCookies[customGroupId];
            String returnMsg = ServiceConstants.COOKIE_NAME_FOR_VERSION + "_" + userId;
            if (returnMsg.equals(ciCustomGroupInfoNow.getName())) {
                versionFlag = ciCustomGroupInfoNow.getValue();
            }
        }

        String var13 = this.getRequest().getParameter("customGroupId");
        CiCustomGroupInfo var14 = this.ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(var13);
        HashMap var15 = new HashMap();
        boolean success = true;
        String message = "";
        if ("false".equals(versionFlag)) {
            message = "客户群收藏成功";
        } else {
            message = "用户群收藏成功";
        }

        if (var14.getStatus().intValue() != 0 && 2 != var14.getDataStatus().intValue()) {
            if (StringUtil.isNotEmpty(var13) && StringUtil.isNotEmpty(userId)) {
                try {
                    this.customersService.userAttentionCustom(var13, userId);
                } catch (Exception var12) {
                    success = false;
                    if ("false".equals(versionFlag)) {
                        message = "客户群收藏失败";
                    } else {
                        message = "用户群收藏失败";
                    }

                    this.log.error("收藏客户群失败", var12);
                    throw new CIServiceException("收藏客户群失败");
                }
            } else {
                success = false;
                message = "参数有误";
            }
        } else {
            success = false;
            if ("false".equals(versionFlag)) {
                message = "不能收藏已删除或者创建中的客户群！";
            } else {
                message = "不能收藏已删除或者创建中的用户群！";
            }
        }

        HttpServletResponse response = this.getResponse();
        var15.put("success", Boolean.valueOf(success));
        var15.put("message", message);

        try {
            this.sendJson(response, JsonUtil.toJson(var15));
        } catch (Exception var11) {
            this.log.error("发送json串异常", var11);
            throw new CIServiceException(var11);
        }
    }

    public void delUserAttentionCustom() {
        String userId = PrivilegeServiceUtil.getUserId();
        String versionFlag = "false";
        Cookie[] myCookies = this.getRequest().getCookies();

        for (int customGroupId = 0; customGroupId < myCookies.length; ++customGroupId) {
            Cookie ciCustomGroupInfoNow = myCookies[customGroupId];
            String returnMsg = ServiceConstants.COOKIE_NAME_FOR_VERSION + "_" + userId;
            if (returnMsg.equals(ciCustomGroupInfoNow.getName())) {
                versionFlag = ciCustomGroupInfoNow.getValue();
            }
        }

        String var13 = this.getRequest().getParameter("customGroupId");
        CiCustomGroupInfo var14 = this.ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(var13);
        HashMap var15 = new HashMap();
        boolean success = true;
        String message = "";
        if ("false".equals(versionFlag)) {
            message = "取消客户群收藏成功";
        } else {
            message = "取消用户群收藏成功";
        }

        if (var14.getStatus().intValue() != 0 && 2 != var14.getDataStatus().intValue()) {
            if (StringUtil.isNotEmpty(var13) && StringUtil.isNotEmpty(userId)) {
                try {
                    this.customersService.deleteUserAttentionCustom(var13, userId);
                } catch (Exception var12) {
                    success = false;
                    if ("false".equals(versionFlag)) {
                        message = "取消客户群收藏失败";
                    } else {
                        message = "取消用户群收藏失败";
                    }

                    this.log.error(message, var12);
                    throw new CIServiceException(message, var12);
                }
            }
        } else {
            success = false;
            if ("false".equals(versionFlag)) {
                message = "不能取消对已删除或者创建中的客户群的收藏！";
            } else {
                message = "不能取消对已删除或者创建中的用户群的收藏！";
            }
        }

        HttpServletResponse response = this.getResponse();
        var15.put("success", Boolean.valueOf(success));
        var15.put("message", message);

        try {
            this.sendJson(response, JsonUtil.toJson(var15));
        } catch (Exception var11) {
            this.log.error("发送json串异常", var11);
            throw new CIServiceException(var11);
        }
    }

    public String findUserAttentionCustomNum() {
        if (this.ciCustomGroupInfo == null) {
            this.ciCustomGroupInfo = new CiCustomGroupInfo();
        }

        if (this.pager == null) {
            this.pager = new Pager();
        }

        boolean success = false;
        HashMap result = new HashMap();
        String msg = "";
        int totalPage = 0;
        long totalNum = 0L;

        try {
            byte response = 5;
            long e = this.customersService.getUserAttentionCustomserCount(this.ciCustomGroupInfo).longValue();
            totalNum = e;
            totalPage = (int) Math.ceil((double) e / (double) response);
            this.pager.setTotalPage(totalPage);
            this.pager.setPageSize(response);
            success = true;
        } catch (Exception var11) {
            msg = "用户收藏客户群总数查询发生异常";
            this.log.error(msg, var11);
            success = false;
        }

        result.put("success", Boolean.valueOf(success));
        if (success) {
            result.put("totalPage", Integer.valueOf(totalPage));
            result.put("totalSize", Long.valueOf(totalNum));
        } else {
            result.put("msg", msg);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
            return null;
        } catch (Exception var10) {
            this.log.error("发送json串异常", var10);
            throw new CIServiceException(var10);
        }
    }

    public String findUserAttentionCustom() {
        String msg;
        try {
            boolean e = false;
            if (this.ciCustomGroupInfo == null) {
                this.ciCustomGroupInfo = new CiCustomGroupInfo();
            }

            if (StringUtils.isBlank(this.ciCustomGroupInfo.getDataDate())) {
                this.newLabelMonth = this.getNewLabelMonth();
            }

            msg = this.getUserId();
            String createCityId = PrivilegeServiceUtil.getCityIdFromSession();
            String root = Configure.getInstance().getProperty("CENTER_CITYID");
            if (!PrivilegeServiceUtil.isAdminUser(msg)) {
                this.ciCustomGroupInfo.setCreateUserId(msg);
                this.ciCustomGroupInfo.setCreateCityId(createCityId);
            } else if (!createCityId.equals(root)) {
                this.ciCustomGroupInfo.setCreateCityId(createCityId);
            }

            if (this.pager == null) {
                this.pager = new Pager(this.customersService.getUserAttentionCustomserCount(this.ciCustomGroupInfo).longValue());
                byte currentPage = 10;
                this.pager.setTotalPage((int) Math.ceil((double) this.pager.getTotalSize() / (double) currentPage));
            }

            int currentPage1 = Integer.valueOf(this.getRequest().getParameter("currentPage") == null ? "1" : this.getRequest().getParameter("currentPage")).intValue();
            this.pager.setPageNum(currentPage1);
            int e1 = this.pager.getTotalPage();
            if (currentPage1 <= e1) {
                byte ciSysInfoRightList = 5;
                this.pager.setPageSize(ciSysInfoRightList);
                this.pager.setTotalPage(e1);
                this.ciCustomGroupInfoList = this.customersService.queryUserAttentionCustomersList(this.pager, this.newLabelMonth, this.ciCustomGroupInfo);
                if (this.ciCustomGroupInfoList != null) {
                    Iterator var8 = this.ciCustomGroupInfoList.iterator();

                    label84:
                    while (true) {
                        CiCustomGroupInfo info;
                        do {
                            do {
                                if (!var8.hasNext()) {
                                    break label84;
                                }

                                info = (CiCustomGroupInfo) var8.next();
                                String offsetStr = this.customersService.queryCustomerOffsetStr(info);
                                info.setKpiDiffRule((info.getKpiDiffRule() == null ? "" : info.getKpiDiffRule()) + offsetStr);
                                if (info.getMonthLabelDate() != null && !info.getMonthLabelDate().equals("")) {
                                    info.setMonthLabelDate(DateUtil.string2StringFormat(info.getMonthLabelDate(), "yyyyMM", "yyyy-MM"));
                                }

                                if (info.getDayLabelDate() != null && !info.getDayLabelDate().equals("")) {
                                    info.setDayLabelDate(DateUtil.string2StringFormat(info.getDayLabelDate(), "yyyyMMdd", "yyyy-MM-dd"));
                                }
                            } while (info.getMonthLabelDate() != null && !info.getMonthLabelDate().equals(""));
                        } while (info.getDayLabelDate() != null && !info.getDayLabelDate().equals(""));

                        if (info.getDataDate() == null) {
                            info.setDataDateStr("");
                        } else if (info.getDataDate().length() > 6) {
                            info.setDataDateStr(DateUtil.string2StringFormat(info.getDataDate(), "yyyyMMdd", "yyyy-MM-dd"));
                        } else {
                            info.setDataDateStr(DateUtil.string2StringFormat(info.getDataDate(), "yyyyMM", "yyyy-MM"));
                        }
                    }
                }
            }

            List ciSysInfoRightList1 = this.customersService.queryCiSysInfo(1);
            if (ciSysInfoRightList1 != null && ciSysInfoRightList1.size() > 0) {
                this.havePushFun = Integer.valueOf(1);
            } else {
                this.havePushFun = Integer.valueOf(0);
            }

            return "customGroupMarket";
        } catch (Exception var10) {
            msg = "用户关注标签列表查询发生异常";
            this.log.error(msg, var10);
            throw new CIServiceException(var10);
        }
    }

    public void changeRecommendCustom() {
        HashMap returnMap = new HashMap();
        boolean success = false;

        String json;
        try {
            String response = this.ciCustomGroupInfo.getCustomGroupId();
            CiCustomGroupInfo json1 = null;
            if (StringUtil.isNotEmpty(response)) {
                json1 = this.customersService.queryCiCustomGroupInfo(response);
            }

            if (json1 == null) {
                throw new CIServiceException("未查询到id为：" + response + " 的客户群信息！");
            }

            String e = "";
            if (json1.getIsSysRecom() != null && json1.getIsSysRecom().intValue() != ServiceConstants.IS_NOT_SYS_RECOM) {
                if (json1.getIsSysRecom() != null && json1.getIsSysRecom().intValue() == ServiceConstants.IS_SYS_RECOM) {
                    json1.setIsSysRecom(Integer.valueOf(ServiceConstants.IS_NOT_SYS_RECOM));
                    e = "取消推荐设置成功！";
                }
            } else {
                json1.setIsSysRecom(Integer.valueOf(ServiceConstants.IS_SYS_RECOM));
                e = "系统推荐设置成功！";
            }

            success = this.customersService.modifyCiCustomGroupInfo(json1, this.getUserId());
            returnMap.put("msg", e);
            returnMap.put("success", Boolean.valueOf(success));
        } catch (Exception var7) {
            success = false;
            json = "系统推荐设置失败！";
            this.log.error(json, var7);
            returnMap.put("msg", json);
            returnMap.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();
        json = null;

        try {
            json = JsonUtil.toJson(returnMap);
            this.sendJson(response1, json);
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void findCurrentUserId() {
        String userId = this.getUserId();
        HttpServletResponse response = this.getResponse();
        String json = null;

        try {
            json = JsonUtil.toJson(userId);
            this.sendJson(response, json);
        } catch (Exception var5) {
            this.log.error("发送json串异常", var5);
            throw new CIServiceException(var5);
        }
    }

    public void checkIfDailyCustomGroupCanCreate() {
        String jsonObj = null;
        HashMap result = new HashMap();
        boolean flag = false;

        try {
            flag = this.customersService.checkIfDailyCustomGroupCanCreate(this.getUserId());
        } catch (Exception var8) {
            String e = "检查日周期客户群创建条件异常";
            this.log.error(e, var8);
            result.put("msg", e + "，请联系管理员！");
            flag = false;
        }

        result.put("success", Boolean.valueOf(flag));

        try {
            jsonObj = JsonUtil.toJson(result);
        } catch (IOException var7) {
            this.log.error("json转换异常", var7);
            throw new CIServiceException("json转换异常", var7);
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, jsonObj);
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException("发送json串异常");
        }
    }

    public String findGroupInfo() throws Exception {
        this.ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupInfo.getCustomGroupId());
        String offsetStr = this.customersService.queryCustomerOffsetStr(this.ciCustomGroupInfo);
        this.ciCustomGroupInfo.setKpiDiffRule((this.ciCustomGroupInfo.getKpiDiffRule() == null ? "" : this.ciCustomGroupInfo.getKpiDiffRule()) + offsetStr);
        if (this.ciCustomGroupInfo.getMonthLabelDate() != null && !this.ciCustomGroupInfo.getMonthLabelDate().equals("")) {
            this.ciCustomGroupInfo.setMonthLabelDate(DateUtil.string2StringFormat(this.ciCustomGroupInfo.getMonthLabelDate(), "yyyyMM", "yyyy-MM"));
        }

        if (this.ciCustomGroupInfo.getDayLabelDate() != null && !this.ciCustomGroupInfo.getDayLabelDate().equals("")) {
            this.ciCustomGroupInfo.setDayLabelDate(DateUtil.string2StringFormat(this.ciCustomGroupInfo.getDayLabelDate(), "yyyyMMdd", "yyyy-MM-dd"));
        }

        if ((this.ciCustomGroupInfo.getMonthLabelDate() == null || this.ciCustomGroupInfo.getMonthLabelDate().equals("")) && (this.ciCustomGroupInfo.getDayLabelDate() == null || this.ciCustomGroupInfo.getDayLabelDate().equals(""))) {
            if (this.ciCustomGroupInfo.getDataDate() == null) {
                this.ciCustomGroupInfo.setDataDateStr("");
            } else if (this.ciCustomGroupInfo.getDataDate().length() > 6) {
                this.ciCustomGroupInfo.setDataDateStr(DateUtil.string2StringFormat(this.ciCustomGroupInfo.getDataDate(), "yyyyMMdd", "yyyy-MM-dd"));
            } else {
                this.ciCustomGroupInfo.setDataDateStr(DateUtil.string2StringFormat(this.ciCustomGroupInfo.getDataDate(), "yyyyMM", "yyyy-MM"));
            }
        }

        return "queryGroupInfoList";
    }

    public void findGroupInfoLists() throws Exception {
        boolean success = false;
        String msg = "";
        Object list = new ArrayList();
        HashMap result = new HashMap();

        try {
            if (this.pager == null) {
                this.pager = new Pager();
            }

            List response = this.customersService.queryCiCustomListInfoByCGroupId(this.ciCustomGroupInfo.getCustomGroupId());
            if (response != null && response.size() != 0) {
                this.pager.setPageNum(this.page);
                this.pager.setPageSize(this.rows);
                this.pager.setTotalSize((long) response.size());
                list = this.customersService.queryCustomerLists(this.pager.getPageNum(), this.pager.getPageSize(), this.ciCustomGroupInfo);
            } else {
                this.pager.setTotalSize(0L);
            }

            success = true;
        } catch (Exception var8) {
            msg = "查询客户群清单数据错误";
            this.log.error(msg, var8);
            success = false;
        }

        if (success) {
            result.put("total", Long.valueOf(this.pager.getTotalSize()));
            result.put("rows", list);
        } else {
            result.put("msg", msg);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
        }

    }

    public String findSqlInfoDialog() throws Exception {
        return "viewSqlList";
    }

    public void findSqlInfo() throws Exception {
        boolean success = false;
        String msg = "";
        List list = null;
        HashMap result = new HashMap();

        try {
            if (this.pager == null) {
                this.pager = new Pager();
            }

            this.pager.setPageNum(this.page);
            this.pager.setPageSize(this.rows);
            this.pager.setTotalSize((long) this.customersService.queryListExeInfoNumByTableName(this.ciCustomListInfo.getListTableName()));
            list = this.customersService.queryListExeInfosByTableName(this.pager, this.ciCustomListInfo.getListTableName());
            success = true;
        } catch (Exception var8) {
            msg = "查询客户群清单执行sql数据错误";
            this.log.error(msg, var8);
            success = false;
        }

        if (success) {
            result.put("total", Long.valueOf(this.pager.getTotalSize()));
            result.put("rows", list);
        } else {
            result.put("msg", msg);
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
        }

    }

    public void findCiLabelInfoListBySys() {
        Object result = new ArrayList();
        StringBuffer labelIds = new StringBuffer();

        try {
            List response = this.resolveCustomRule();
            int e = 0;
            if (response != null && response.size() > 0) {
                Iterator var6 = response.iterator();

                while (var6.hasNext()) {
                    CiLabelRule item = (CiLabelRule) var6.next();
                    if (item.getElementType().intValue() == 2) {
                        if (e > 0) {
                            labelIds.append(",").append(item.getCalcuElement());
                        } else {
                            labelIds.append(item.getCalcuElement());
                        }

                        ++e;
                    }
                }
            }

            if (StringUtil.isNotEmpty(labelIds)) {
                result = this.customersService.queryCiLabelInfoListBySys(labelIds.toString());
            }
        } catch (Exception var8) {
            this.log.error("查询标签异常！", var8);
            throw new CIServiceException("查询标签异常！");
        }

        HttpServletResponse var9 = this.getResponse();

        try {
            this.sendJson(var9, JsonUtil.toJson(result));
            this.log.debug("========>>>>" + JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException("发送json串异常");
        }
    }

    public void queryVertLabelAttrValFromSession() {
        CiLabelRule result = null;
        String mustColumnId = "";

        try {
            List response = this.ciLabelInfoService.queryCiLabelVerticalColumnRelByLabelId(this.labelId);
            if (response != null) {
                Iterator session = response.iterator();

                while (session.hasNext()) {
                    CiLabelVerticalColumnRel e = (CiLabelVerticalColumnRel) session.next();
                    if (e.getIsMustColumn().intValue() == 1) {
                        mustColumnId = "" + e.getId().getColumnId();
                        break;
                    }
                }
            }

            String e1 = "" + this.labelId;
            HttpSession session1 = this.getSession();
            List ciLabelRuleListTemp = (List) session1.getAttribute("sessionModelList");
            if (ciLabelRuleListTemp != null && ciLabelRuleListTemp.size() > 0) {
                Iterator var8 = ciLabelRuleListTemp.iterator();

                label60:
                while (true) {
                    CiLabelRule item;
                    do {
                        do {
                            do {
                                if (!var8.hasNext()) {
                                    break label60;
                                }

                                item = (CiLabelRule) var8.next();
                            } while (!e1.equals(item.getCalcuElement()));
                        } while (item.getElementType().intValue() != 2);
                    } while (item.getLabelTypeId().intValue() != 8);

                    Iterator var10 = item.getChildCiLabelRuleList().iterator();

                    while (var10.hasNext()) {
                        CiLabelRule childRule = (CiLabelRule) var10.next();
                        if (mustColumnId.equals(childRule.getCalcuElement())) {
                            result = childRule;
                        }
                    }
                }
            }
        } catch (Exception var13) {
            this.log.error("查询标签异常！", var13);
            throw new CIServiceException("查询标签异常！");
        }

        HttpServletResponse response1 = this.getResponse();
        if (result != null) {
            try {
                this.sendJson(response1, JsonUtil.toJson(result));
                this.log.debug("========>>>>" + JsonUtil.toJson(result));
            } catch (Exception var12) {
                this.log.error("发送json串异常", var12);
                throw new CIServiceException("发送json串异常");
            }
        } else {
            try {
                this.log.debug("========>>>>" + JsonUtil.toJson(result));
            } catch (Exception var11) {
                this.log.error("发送json串异常", var11);
                throw new CIServiceException("发送json串异常");
            }
        }

    }

    public void queryVertLabelWithAllSelectedColumnFromSession() {
        Object result = new ArrayList();
        String mustColumnId = "";

        try {
            List response = this.ciLabelInfoService.queryCiLabelVerticalColumnRelByLabelId(this.labelId);
            if (response != null) {
                Iterator ciLabelRuleListTemp = response.iterator();

                while (ciLabelRuleListTemp.hasNext()) {
                    CiLabelVerticalColumnRel e = (CiLabelVerticalColumnRel) ciLabelRuleListTemp.next();
                    if (e.getIsMustColumn().intValue() == 1) {
                        mustColumnId = "" + e.getId().getColumnId();
                        break;
                    }
                }
            }

            String e1 = "" + this.labelId;
            List ciLabelRuleListTemp1 = this.resolveCustomRule();
            if (ciLabelRuleListTemp1 != null && ciLabelRuleListTemp1.size() > 0) {
                Iterator var7 = ciLabelRuleListTemp1.iterator();

                label63:
                while (true) {
                    CiLabelRule item;
                    do {
                        do {
                            do {
                                if (!var7.hasNext()) {
                                    break label63;
                                }

                                item = (CiLabelRule) var7.next();
                            } while (!e1.equals(item.getCalcuElement()));
                        } while (item.getElementType().intValue() != 2);
                    } while (item.getLabelTypeId().intValue() != 8);

                    Iterator var9 = item.getChildCiLabelRuleList().iterator();

                    while (var9.hasNext()) {
                        CiLabelRule childRule = (CiLabelRule) var9.next();
                        if (mustColumnId.equals(childRule.getCalcuElement())) {
                            childRule.setIsMustColumn(Integer.valueOf(1));
                        } else {
                            childRule.setIsMustColumn(Integer.valueOf(0));
                        }
                    }

                    result = item.getChildCiLabelRuleList();
                }
            }
        } catch (Exception var12) {
            this.log.error("查询标签异常！", var12);
            throw new CIServiceException("查询标签异常！");
        }

        HttpServletResponse response1 = this.getResponse();
        if (result != null) {
            try {
                this.sendJson(response1, JsonUtil.toJson(result));
                this.log.debug("========>>>>" + JsonUtil.toJson(result));
            } catch (Exception var11) {
                this.log.error("发送json串异常", var11);
                throw new CIServiceException("发送json串异常");
            }
        } else {
            try {
                this.sendJson(response1, JsonUtil.toJson(result));
                this.log.debug("========>>>>" + JsonUtil.toJson(result));
            } catch (Exception var10) {
                this.log.error("发送json串异常", var10);
                throw new CIServiceException("发送json串异常");
            }
        }

    }

    public void queryVertLabelIdFormSession() {
        ArrayList result = new ArrayList();

        try {
            HttpSession response = this.getSession();
            List e = (List) response.getAttribute("sessionModelList");
            if (e != null && e.size() > 0) {
                Iterator var5 = e.iterator();

                while (var5.hasNext()) {
                    CiLabelRule item = (CiLabelRule) var5.next();
                    if (item.getElementType().intValue() == 2 && item.getLabelTypeId().intValue() == 8) {
                        result.add(item.getCalcuElement());
                    }
                }
            }
        } catch (Exception var7) {
            this.log.error("查询session里全部纵表标签ID异常！", var7);
            throw new CIServiceException("查询session里全部纵表标签ID异常！");
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
            this.log.debug("========>>>>" + JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("查询session里全部纵表标签ID异常！", var6);
            throw new CIServiceException("查询session里全部纵表标签ID异常！");
        }
    }

    public void queryLabelTypeTree() {
        HashMap returnMsg = new HashMap();
        boolean success = false;
        String message = "";

        try {
            String response = this.getRequest().getParameter("labelNameKeyWord");
            List e = this.customersService.queryCiLabelInfoTreeList(response);
            int labelSize = e.size();
            HttpSession session = this.getSession();
            this.ciLabelRuleList = (List) session.getAttribute("sessionModelList");
            this.ciGroupAttrRelList = this.customersService.queryCustomGroupAttrRelList(this.ciLabelRuleList, this.labelName);
            HashSet set = new HashSet();
            if (this.ciLabelRuleList != null && this.ciGroupAttrRelList.size() > 0) {
                Iterator var10 = this.ciLabelRuleList.iterator();

                while (var10.hasNext()) {
                    CiLabelRule rule = (CiLabelRule) var10.next();
                    if (rule.getElementType().intValue() == 5) {
                        String customGroupId = rule.getCustomId();
                        String listTableName = rule.getCalcuElement();
                        CiCustomListInfo listInfo = this.customersService.queryCiCustomListInfoById(listTableName);
                        CiCustomGroupInfo info = this.customersService.queryCiCustomGroupInfo(customGroupId);
                        CiLabelInfo labelInfo;
                        if (response == null) {
                            labelInfo = new CiLabelInfo();
                            labelInfo.setLabelOrCustomId(info.getCustomGroupId());
                            labelInfo.setLabelOrCustomName(info.getCustomGroupName() + "[" + listInfo.getDataDate() + "]");
                            labelInfo.setParentId(Integer.valueOf(-2));
                            set.add(labelInfo);
                        } else if (response != null && info.getCustomGroupName().toLowerCase().contains(response.toLowerCase())) {
                            labelInfo = new CiLabelInfo();
                            labelInfo.setLabelOrCustomId(info.getCustomGroupId());
                            labelInfo.setLabelOrCustomName(info.getCustomGroupName() + "[" + listInfo.getDataDate() + "]");
                            labelInfo.setParentId(Integer.valueOf(-2));
                            set.add(labelInfo);
                        }
                    }
                }
            }

            e.addAll(set);
            returnMsg.put("labelList", e);
            returnMsg.put("customSize", Integer.valueOf(set.size()));
            returnMsg.put("labelSize", Integer.valueOf(labelSize));
            success = true;
        } catch (Exception var17) {
            message = "查询标签客户群分类树异常";
            this.log.error(message, var17);
            returnMsg.put("message", message);
            success = false;
        }

        returnMsg.put("result", Boolean.valueOf(success));
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
        } catch (Exception var16) {
            this.log.error("发送json串异常", var16);
            success = false;
            throw new CIServiceException("发送json串异常");
        }
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public List<CiLabelRule> getCiLabelRuleList() {
        return this.ciLabelRuleList;
    }

    public void setCiLabelRuleList(List<CiLabelRule> ciLabelRuleList) {
        this.ciLabelRuleList = ciLabelRuleList;
    }

    public CiCustomGroupInfo getCiCustomGroupInfo() {
        return this.ciCustomGroupInfo;
    }

    public void setCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo) {
        this.ciCustomGroupInfo = ciCustomGroupInfo;
    }

    public String getCustomGroupId() {
        return this.customGroupId;
    }

    public void setCustomGroupId(String customGroupId) {
        this.customGroupId = customGroupId;
    }

    public CiCustomListInfo getCiCustomListInfo() {
        return this.ciCustomListInfo;
    }

    public void setCiCustomListInfo(CiCustomListInfo ciCustomListInfo) {
        this.ciCustomListInfo = ciCustomListInfo;
    }

    public List<CiCustomSourceRel> getCiCustomSourceRelList() {
        return this.ciCustomSourceRelList;
    }

    public void setCiCustomSourceRelList(List<CiCustomSourceRel> ciCustomSourceRelList) {
        this.ciCustomSourceRelList = ciCustomSourceRelList;
    }

    public CiCustomFileRel getCustomFile() {
        return this.customFile;
    }

    public void setCustomFile(CiCustomFileRel customFile) {
        this.customFile = customFile;
    }

    public List<CiCustomGroupInfo> getCiCustomGroupInfoList() {
        return this.ciCustomGroupInfoList;
    }

    public void setCiCustomGroupInfoList(List<CiCustomGroupInfo> ciCustomGroupInfoList) {
        this.ciCustomGroupInfoList = ciCustomGroupInfoList;
    }

    public String getInitOrSearch() {
        return this.initOrSearch;
    }

    public void setInitOrSearch(String initOrSearch) {
        this.initOrSearch = initOrSearch;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIsSaveTemplate() {
        return this.isSaveTemplate;
    }

    public void setIsSaveTemplate(String isSaveTemplate) {
        this.isSaveTemplate = isSaveTemplate;
    }

    public CiTemplateInfo getCiTemplateInfo() {
        return this.ciTemplateInfo;
    }

    public void setCiTemplateInfo(CiTemplateInfo ciTemplateInfo) {
        this.ciTemplateInfo = ciTemplateInfo;
    }

    public List<CiTemplateInfo> getCiTemplateInfoList() {
        return this.ciTemplateInfoList;
    }

    public void setCiTemplateInfoList(List<CiTemplateInfo> ciTemplateInfoList) {
        this.ciTemplateInfoList = ciTemplateInfoList;
    }

    public List<CiSysInfo> getCiSysInfoList() {
        return this.ciSysInfoList;
    }

    public void setCiSysInfoList(List<CiSysInfo> ciSysInfoList) {
        this.ciSysInfoList = ciSysInfoList;
    }

    public String getCustomGroupName() {
        return this.customGroupName;
    }

    public void setCustomGroupName(String customGroupName) {
        this.customGroupName = customGroupName;
    }

    public CiSysInfo getSysinfo() {
        return this.sysinfo;
    }

    public void setSysinfo(CiSysInfo sysinfo) {
        this.sysinfo = sysinfo;
    }

    public Boolean getIsCustomGroupMarket() {
        return this.isCustomGroupMarket;
    }

    public void setIsCustomGroupMarket(Boolean isCustomGroupMarket) {
        this.isCustomGroupMarket = isCustomGroupMarket;
    }

    public List<DimScene> getDimScenes() {
        return this.dimScenes;
    }

    public void setDimScenes(List<DimScene> dimScenes) {
        this.dimScenes = dimScenes;
    }

    public List<DimCustomCreateType> getCustomCreateTypes() {
        return this.customCreateTypes;
    }

    public void setCustomCreateTypes(List<DimCustomCreateType> customCreateTypes) {
        this.customCreateTypes = customCreateTypes;
    }

    public List<DimCustomDataStatus> getCustomDataStatus() {
        return this.customDataStatus;
    }

    public void setCustomDataStatus(List<DimCustomDataStatus> customDataStatus) {
        this.customDataStatus = customDataStatus;
    }

    public Long getTotalCustomNum() {
        return this.totalCustomNum;
    }

    public void setTotalCustomNum(Long totalCustomNum) {
        this.totalCustomNum = totalCustomNum;
    }

    public CiCustomPushReq getCiCustomPushReq() {
        return this.ciCustomPushReq;
    }

    public void setCiCustomPushReq(CiCustomPushReq ciCustomPushReq) {
        this.ciCustomPushReq = ciCustomPushReq;
    }

    public String getDataDate() {
        return this.dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public String getPushCycle() {
        return this.pushCycle;
    }

    public void setPushCycle(String pushCycle) {
        this.pushCycle = pushCycle;
    }

    public List<String> getSysIds() {
        return this.sysIds;
    }

    public void setSysIds(List<String> sysIds) {
        this.sysIds = sysIds;
    }

    public Integer getIsAllDisposable() {
        return this.isAllDisposable;
    }

    public void setIsAllDisposable(Integer isAllDisposable) {
        this.isAllDisposable = isAllDisposable;
    }

    public List<CiSysInfo> getSysInfoCycleList() {
        return this.sysInfoCycleList;
    }

    public void setSysInfoCycleList(List<CiSysInfo> sysInfoCycleList) {
        this.sysInfoCycleList = sysInfoCycleList;
    }

    public String getIsPush() {
        return this.isPush;
    }

    public void setIsPush(String isPush) {
        this.isPush = isPush;
    }

    public List<CiCustomGroupPushCycle> getIsPushCycleList() {
        return this.isPushCycleList;
    }

    public void setIsPushCycleList(List<CiCustomGroupPushCycle> isPushCycleList) {
        this.isPushCycleList = isPushCycleList;
    }

    public Integer getHavePushFun() {
        return this.havePushFun;
    }

    public void setHavePushFun(Integer havePushFun) {
        this.havePushFun = havePushFun;
    }

    public Integer getExploreFromModuleFlag() {
        return this.exploreFromModuleFlag;
    }

    public void setExploreFromModuleFlag(Integer exploreFromModuleFlag) {
        this.exploreFromModuleFlag = exploreFromModuleFlag;
    }

    public Integer getSaveCustomersFlag() {
        return this.saveCustomersFlag;
    }

    public void setSaveCustomersFlag(Integer saveCustomersFlag) {
        this.saveCustomersFlag = saveCustomersFlag;
    }

    public String getIsLastLabel() {
        return this.isLastLabel;
    }

    public void setIsLastLabel(String isLastLabel) {
        this.isLastLabel = isLastLabel;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return this.rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public List<CiLabelInfo> getCiLabelInfoList() {
        return this.ciLabelInfoList;
    }

    public void setCiLabelInfoList(List<CiLabelInfo> ciLabelInfoList) {
        this.ciLabelInfoList = ciLabelInfoList;
    }

    public String getLabelName() {
        return this.labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public List<CiGroupAttrRel> getCiGroupAttrRelList() {
        return this.ciGroupAttrRelList;
    }

    public void setCiGroupAttrRelList(List<CiGroupAttrRel> ciGroupAttrRelList) {
        this.ciGroupAttrRelList = ciGroupAttrRelList;
    }

    public List<ImportColumn> getImportColumnList() {
        return this.importColumnList;
    }

    public void setImportColumnList(List<ImportColumn> importColumnList) {
        this.importColumnList = importColumnList;
    }

    public String getIsSysRecommendCustomsFlag() {
        return this.isSysRecommendCustomsFlag;
    }

    public void setIsSysRecommendCustomsFlag(String isSysRecommendCustomsFlag) {
        this.isSysRecommendCustomsFlag = isSysRecommendCustomsFlag;
    }

    public List<CiCustomSceneRel> getCiCustomSceneRelList() {
        return this.ciCustomSceneRelList;
    }

    public void setCiCustomSceneRelList(List<CiCustomSceneRel> ciCustomSceneRelList) {
        this.ciCustomSceneRelList = ciCustomSceneRelList;
    }

    public String getSceneIds() {
        return this.sceneIds;
    }

    public void setSceneIds(String sceneIds) {
        this.sceneIds = sceneIds;
    }

    public int getRankingListType() {
        return this.rankingListType;
    }

    public void setRankingListType(int rankingListType) {
        this.rankingListType = rankingListType;
    }

    public String getRefreshType() {
        return this.refreshType;
    }

    public void setRefreshType(String refreshType) {
        this.refreshType = refreshType;
    }

    public Map<String, Object> getResultMap() {
        return this.resultMap;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }

    public Map<String, String> getCityMap() {
        return this.cityMap;
    }

    public void setCityMap(Map<String, String> cityMap) {
        this.cityMap = cityMap;
    }

    public List<CiCustomListExeInfo> getCiCustomListExeInfos() {
        return this.ciCustomListExeInfos;
    }

    public void setCiCustomListExeInfos(List<CiCustomListExeInfo> ciCustomListExeInfos) {
        this.ciCustomListExeInfos = ciCustomListExeInfos;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getListTableName() {
        return this.listTableName;
    }

    public void setListTableName(String listTableName) {
        this.listTableName = listTableName;
    }

    public Integer getLevel() {
        return this.level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getOldIsHasList() {
        return this.oldIsHasList;
    }

    public void setOldIsHasList(Integer oldIsHasList) {
        this.oldIsHasList = oldIsHasList;
    }

    public boolean getFlagConstraint() {
        return this.flagConstraint;
    }

    public void setFlagConstraint(boolean flagConstraint) {
        this.flagConstraint = flagConstraint;
    }

    public Integer getLabelOrCustom() {
        return this.labelOrCustom;
    }

    public void setLabelOrCustom(Integer labelOrCustom) {
        this.labelOrCustom = labelOrCustom;
    }

    public List<CiGroupAttrRel> getCiGroupAttrRelListModify() {
        return this.ciGroupAttrRelListModify;
    }

    public void setCiGroupAttrRelListModify(List<CiGroupAttrRel> ciGroupAttrRelListModify) {
        this.ciGroupAttrRelListModify = ciGroupAttrRelListModify;
    }

    public String getCustomListDate() {
        return this.customListDate;
    }

    public void setCustomListDate(String customListDate) {
        this.customListDate = customListDate;
    }

    public List<CiLabelInfo> getVertLabelAttrList() {
        return this.vertLabelAttrList;
    }

    public void setVertLabelAttrList(List<CiLabelInfo> vertLabelAttrList) {
        this.vertLabelAttrList = vertLabelAttrList;
    }

    public Integer getLabelId() {
        return this.labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }

    public String getAttrVal() {
        return this.attrVal;
    }

    public void setAttrVal(String attrVal) {
        this.attrVal = attrVal;
    }

    public String getAttrName() {
        return this.attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getColumnId() {
        return this.columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getOtherAttrs() {
        return this.otherAttrs;
    }

    public void setOtherAttrs(String otherAttrs) {
        this.otherAttrs = otherAttrs;
    }

    public String getSortAttrNum() {
        return this.sortAttrNum;
    }

    public void setSortAttrNum(String sortAttrNum) {
        this.sortAttrNum = sortAttrNum;
    }

    public List<CiGroupAttrRel> getSortAttrs() {
        return this.sortAttrs;
    }

    public void setSortAttrs(List<CiGroupAttrRel> sortAttrs) {
        this.sortAttrs = sortAttrs;
    }

    private List<CiExploreSqlAll> generateCiExploreSqlAllList(CiExploreLogRecord ciExploreLogRecord, String sql) {
        if (StringUtil.isEmpty(sql)) {
            return null;
        } else {
            ArrayList result = new ArrayList();
            String temp = sql;

            String s;
            for (int row = 0; temp.length() > 0; temp = temp.substring(s.length(), temp.length())) {
                s = CiUtil.subStringByByte(temp, 4000, "UTF-8");
                ++row;
                CiExploreSqlAll exploreSqlAll = new CiExploreSqlAll((Integer) null, ciExploreLogRecord.getRecordId(), s, Integer.valueOf(row));
                result.add(exploreSqlAll);
            }

            return result;
        }
    }

    private void logExploreRecord(String sql, Date startDate, List<CiLabelRule> ciLabelRuleList) {
        this.log.debug("exe sql:" + sql);
        CiExploreLogRecord logRecord = new CiExploreLogRecord();
        Long startTime = Long.valueOf(startDate.getTime());
        Long endTime = Long.valueOf(System.currentTimeMillis());
        Timestamp exeTime = new Timestamp(startTime.longValue());
        logRecord.setExeTime(exeTime);
        Long exploreTimesLongValue = Long.valueOf(endTime.longValue() - startTime.longValue());
        Integer exploreTimes = Integer.valueOf(exploreTimesLongValue.intValue());
        logRecord.setExploreTimes(exploreTimes);
        CacheBase cache = CacheBase.getInstance();
        StringBuffer useElements = new StringBuffer();
        Iterator ciListExeSqlAllList = ciLabelRuleList.iterator();

        while (ciListExeSqlAllList.hasNext()) {
            CiLabelRule useElementsStr = (CiLabelRule) ciListExeSqlAllList.next();
            int elementType = useElementsStr.getElementType().intValue();
            if (elementType == 2) {
                String labelId = useElementsStr.getCalcuElement();
                CiLabelInfo labelInfo = cache.getEffectiveLabel(labelId);
                if (labelInfo != null) {
                    useElements.append(labelInfo.getLabelName() + ",");
                }
            } else if (elementType == 5) {
                useElements.append(useElementsStr.getCalcuElement() + ",");
            }
        }

        String useElementsStr1 = useElements.toString();
        if (StringUtil.isNotEmpty(useElementsStr1)) {
            useElementsStr1 = useElementsStr1.substring(0, useElementsStr1.lastIndexOf(","));
        }

        logRecord.setUseLabels(useElementsStr1);
        logRecord.setUserId(this.getUserId());

        try {
            this.customersService.saveCiExploreLogRecord(logRecord);
        } catch (Exception var17) {
            var17.printStackTrace();
        }

        List ciListExeSqlAllList1 = this.generateCiExploreSqlAllList(logRecord, sql);
        this.customersService.saveCiExploreSqlAllList(ciListExeSqlAllList1);
    }

    public String queryGroup() throws Exception {
        String deParam = PasswordUtil.decrypt(this.getRequest().getParameter("param"));
        this.log.info("param:" + deParam);
        String[] var5;
        int var4 = (var5 = deParam.split("&")).length;

        for (int var3 = 0; var3 < var4; ++var3) {
            String params = var5[var3];
            if (params.startsWith("ip_add=")) {
                this.ipAdd = params.replace("ip_add=", "");
            }
        }

        return "queryGroup";
    }

    public void searchCustomGroupList() throws Exception {
        boolean success = true;
        String msg = "";
        Pager p2 = null;
        HashMap result = new HashMap();

        try {
            if (this.ciCustomGroupInfo == null) {
                this.ciCustomGroupInfo = new CiCustomGroupInfo();
            }

            if (StringUtils.isBlank(this.ciCustomGroupInfo.getDataDate())) {
                this.newLabelMonth = this.getNewLabelMonth();
            }

            if (this.pager == null) {
                this.pager = new Pager();
            }

            String response = this.getUserId();
            if (!PrivilegeServiceUtil.isAdminUser(response)) {
                this.ciCustomGroupInfo.setCreateUserId(response);
            }

            p2 = new Pager((long) this.customersService.queryCustomersCount(this.ciCustomGroupInfo));
            this.pager.setPageNum(this.page);
            this.pager.setPageSize(this.rows);
            this.pager.setTotalPage(p2.getTotalPage());
            this.pager.setTotalSize(p2.getTotalSize());
            this.pager.setResult(this.customersService.queryCustomersList(this.pager, this.newLabelMonth, this.ciCustomGroupInfo));
        } catch (Exception var8) {
            this.log.error("客户群列表查询出错：" + var8);
            success = false;
        }

        if (success) {
            result.put("total", Long.valueOf(p2.getTotalSize()));
            result.put("rows", this.pager.getResult());
        } else {
            result.put("msg", msg);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
        }

    }
}
