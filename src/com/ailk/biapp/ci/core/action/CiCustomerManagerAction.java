package com.ailk.biapp.ci.core.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiCustomSceneRel;
import com.ailk.biapp.ci.entity.CiCustomSceneRelId;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.entity.CiGroupAttrRelId;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiLabelVerticalColumnRel;
import com.ailk.biapp.ci.entity.CiSysInfo;
import com.ailk.biapp.ci.entity.CiTemplateInfo;
import com.ailk.biapp.ci.entity.DimCustomCreateType;
import com.ailk.biapp.ci.entity.DimCustomDataStatus;
import com.ailk.biapp.ci.entity.DimScene;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.market.service.ICiMarketService;
import com.ailk.biapp.ci.model.Column;
import com.ailk.biapp.ci.model.ImportColumn;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiCustomSceneService;
import com.ailk.biapp.ci.service.ICiTemplateInfoService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.CIAlarmServiceUtil;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.http.ResponseEncodingUtil;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiCustomerManagerAction extends CIBaseAction {
    private Logger log = Logger.getLogger(CiCustomerManagerAction.class);
    private int page;
    private int rows;
    private Pager pager;
    private String isPush;
    private Integer havePushFun;
    private String isSaveTemplate;
    private String customerName;
    private File file;
    private String fileFileName;
    private List<String> attrList;
    private String isHaveAttrTitle;
    private CiTemplateInfo ciTemplateInfo;
    private Map<String, Object> resultMap;
    private CiCustomGroupInfo ciCustomGroupInfo;
    private CiCustomListInfo ciCustomListInfo;
    private List<CiSysInfo> ciSysInfoList;
    private List<CiLabelRule> ciLabelRuleList;
    private List<CiSysInfo> sysInfoCycleList;
    private List<CiGroupAttrRel> ciGroupAttrRelList;
    private List<CiLabelInfo> vertLabelAttrList;
    private List<CiLabelInfo> ciLabelInfoList;
    private List<CiGroupAttrRel> sortAttrs;
    private List<ImportColumn> importColumnList;
    private Integer oldIsHasList;
    private String sceneIds;
    private List<DimScene> dimScenes;
    private List<DimCustomCreateType> customCreateTypes;
    private List<DimCustomDataStatus> customDataStatus;
    @Autowired
    private ICiTemplateInfoService ciTemplateInfoService;
    @Autowired
    private ICustomersManagerService customersService;
    @Autowired
    private ICiCustomSceneService ciCustomSceneService;
    @Autowired
    private ICiMarketService ciMarketService;

    public CiCustomerManagerAction() {
    }

    public String init() throws Exception {
        CacheBase cache = CacheBase.getInstance();
        this.dimScenes = (List<DimScene>)cache.getObjectList("DIM_SCENE");
        this.customCreateTypes =(List<DimCustomCreateType>) cache.getObjectList("DIM_CUSTOM_CREATE_TYPE");
        this.customDataStatus = (List<DimCustomDataStatus>)cache.getObjectList("DIM_CUSTOM_DATA_STATUS");
        return "init";
    }

    public String queryCustomersByPage() throws Exception {
        List ciSysInfoRightList = this.customersService.queryCiSysInfo(1);
        if(ciSysInfoRightList != null && ciSysInfoRightList.size() > 0) {
            this.havePushFun = Integer.valueOf(1);
        } else {
            this.havePushFun = Integer.valueOf(0);
        }

        if(this.ciCustomGroupInfo == null) {
            this.ciCustomGroupInfo = new CiCustomGroupInfo();
        }

        this.ciCustomGroupInfo.setSceneId(StringUtil.isEmpty(this.ciCustomGroupInfo.getSceneId())?"":this.ciCustomGroupInfo.getSceneId() + ",");
        if(StringUtils.isBlank(this.ciCustomGroupInfo.getDataDate())) {
            this.newLabelMonth = this.getNewLabelMonth();
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        String userId = this.getUserId();
        String createCityId = PrivilegeServiceUtil.getCityIdFromSession();
        String root = Configure.getInstance().getProperty("CENTER_CITYID");
        if(!PrivilegeServiceUtil.isAdminUser(userId)) {
            this.ciCustomGroupInfo.setCreateUserId(userId);
            this.ciCustomGroupInfo.setCreateCityId(createCityId);
        } else if(!createCityId.equals(root)) {
            this.ciCustomGroupInfo.setCreateCityId(createCityId);
        }

        this.ciCustomGroupInfo.setIsMyCustom("true");
        this.ciCustomGroupInfo.setUserId(userId);
        Pager p2 = new Pager((long)this.customersService.queryCustomersCount(this.ciCustomGroupInfo));
        this.pager.setTotalPage(p2.getTotalPage());
        this.pager.setTotalSize(p2.getTotalSize());
        this.pager = this.pager.pagerFlip();
        List list = this.customersService.queryCustomersList(this.pager, this.newLabelMonth, this.ciCustomGroupInfo);
        if(list != null) {
            Iterator customerAnalysisMenu = list.iterator();

            label83:
            while(true) {
                CiCustomGroupInfo info;
                do {
                    do {
                        if(!customerAnalysisMenu.hasNext()) {
                            break label83;
                        }

                        info = (CiCustomGroupInfo)customerAnalysisMenu.next();
                        String offsetStr = this.customersService.queryCustomerOffsetStr(info);
                        info.setKpiDiffRule((info.getKpiDiffRule() == null?"":info.getKpiDiffRule()) + offsetStr);
                        if(info.getMonthLabelDate() != null && !info.getMonthLabelDate().equals("")) {
                            info.setMonthLabelDate(DateUtil.string2StringFormat(info.getMonthLabelDate(), "yyyyMM", "yyyy-MM"));
                        }

                        if(info.getDayLabelDate() != null && !info.getDayLabelDate().equals("")) {
                            info.setDayLabelDate(DateUtil.string2StringFormat(info.getDayLabelDate(), "yyyyMMdd", "yyyy-MM-dd"));
                        }
                    } while(info.getMonthLabelDate() != null && !info.getMonthLabelDate().equals(""));
                } while(info.getDayLabelDate() != null && !info.getDayLabelDate().equals(""));

                if(info.getDataDate() == null) {
                    info.setDataDateStr("");
                } else {
                    info.setDataDateStr(this.ciMarketService.queryCustomDataDate(info));
                }
            }
        }

        this.pager.setResult(list);
        String customerAnalysisMenu1 = Configure.getInstance().getProperty("CUSTOMER_ANALYSIS");
        this.ciCustomGroupInfo.setCustomerAnalysisMenu(customerAnalysisMenu1);
        this.log.info("Exit CustomersManagerAction.search() method");
        CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_SELECT", "", StringUtil.isEmpty(this.ciCustomGroupInfo.getCustomGroupName())?"":this.ciCustomGroupInfo.getCustomGroupName(), "查询我的用户群名称中含有【" + (StringUtil.isEmpty(this.ciCustomGroupInfo.getCustomGroupName())?"":this.ciCustomGroupInfo.getCustomGroupName()) + "】的记录成功", OperResultEnum.Success, LogLevelEnum.Medium);
        return "customManagerList";
    }

    public void editCustomer() throws Exception {
        Map returnMap = null;
        Map returnTemplateMap = null;

        try {
            String response = this.getUserId();
            Integer var23 = this.ciCustomGroupInfo.getIsPrivate();
            if(var23.intValue() == 0) {
                returnMap = this.customersService.isShareNameExist(this.ciCustomGroupInfo, response, (String)null);
                if(returnMap.get("msg") != null && !"".equals(returnMap.get("msg"))) {
                    returnMap.put("cmsg", returnMap.get("msg"));
                }
            } else {
                returnMap = this.customersService.isNameExist(this.ciCustomGroupInfo, response);
            }

            if(StringUtil.isNotEmpty(this.isSaveTemplate)) {
                returnTemplateMap = this.ciTemplateInfoService.isNameExist(this.ciTemplateInfo, response);
                returnMap.put("success", Boolean.valueOf(((Boolean)returnTemplateMap.get("success")).booleanValue() && ((Boolean)returnMap.get("success")).booleanValue()));
                returnMap.put("msg", returnTemplateMap.get("msg"));
            }

            ArrayList attrList = new ArrayList();
            String dbType = Configure.getInstance().getProperty("CI_DBTYPE");
            int columnNameIndex = 0;
            int sceneList;
            if(this.ciGroupAttrRelList != null && this.ciGroupAttrRelList.size() > 0) {
                for(sceneList = 0; sceneList < this.ciGroupAttrRelList.size(); ++sceneList) {
                    CiGroupAttrRel ciCustomSceneRelList = (CiGroupAttrRel)this.ciGroupAttrRelList.get(sceneList);
                    if(!StringUtil.isEmpty(ciCustomSceneRelList.getId().getAttrCol())) {
                        ciCustomSceneRelList.getId().setCustomGroupId(this.ciCustomGroupInfo.getCustomGroupId());
                        if(StringUtil.isEmpty(ciCustomSceneRelList.getAttrColType())) {
                            if("ORACLE".equalsIgnoreCase(dbType)) {
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

            if(this.vertLabelAttrList != null) {
                this.ciLabelInfoList.addAll(this.vertLabelAttrList);
            }

            String msg;
            if(this.ciLabelInfoList != null && this.ciLabelInfoList.size() > 0) {
                for(sceneList = 0; sceneList < this.ciLabelInfoList.size(); ++sceneList) {
                    CiLabelInfo var25 = (CiLabelInfo)this.ciLabelInfoList.get(sceneList);
                    if(var25 != null) {
                        CiGroupAttrRel updateCustomGroupInfo = new CiGroupAttrRel();
                        CiGroupAttrRelId session = new CiGroupAttrRelId();
                        msg = var25.getAttrVal();
                        String i$ = "";
                        String ciCustomSceneRelChild = "";
                        if(StringUtil.isNotEmpty(var25.getColumnId())) {
                            i$ = var25.getColumnId();
                        }

                        if(StringUtil.isNotEmpty(var25.getColumnCnName())) {
                            ciCustomSceneRelChild = var25.getColumnCnName();
                        }

                        var25 = CacheBase.getInstance().getEffectiveLabel(String.valueOf(var25.getLabelId()));
                        if(var25 != null) {
                            if(StringUtil.isNotEmpty(i$)) {
                                updateCustomGroupInfo.setLabelOrCustomColumn(i$);
                            }

                            if(null != var25.getLabelTypeId() && var25.getLabelTypeId().intValue() == 8) {
                                updateCustomGroupInfo.setIsVerticalAttr(1);
                            } else {
                                updateCustomGroupInfo.setIsVerticalAttr(0);
                            }

                            if(StringUtil.isNotEmpty(ciCustomSceneRelChild)) {
                                updateCustomGroupInfo.setAttrColName(ciCustomSceneRelChild);
                            } else {
                                updateCustomGroupInfo.setAttrColName(var25.getLabelName());
                            }

                            updateCustomGroupInfo.setAttrColName(var25.getLabelName());
                            updateCustomGroupInfo.setAttrSource(2);
                            updateCustomGroupInfo.setLabelOrCustomId(String.valueOf(var25.getLabelId()));
                            String dataType = "";
                            if(8 != var25.getLabelTypeId().intValue()) {
                                if(5 != var25.getLabelTypeId().intValue()) {
                                    dataType = var25.getCiLabelExtInfo().getCiMdaSysTableColumn().getDataType();
                                }
                            } else if(8 == var25.getLabelTypeId().intValue()) {
                                Set attrValArr = var25.getCiLabelExtInfo().getCiLabelVerticalColumnRels();
                                Iterator e1 = attrValArr.iterator();

                                while(e1.hasNext()) {
                                    CiLabelVerticalColumnRel re = (CiLabelVerticalColumnRel)e1.next();
                                    if(i$.equals(re.getCiMdaSysTableColumn().getColumnId().toString())) {
                                        if(5 != re.getLabelTypeId().intValue()) {
                                            dataType = re.getCiMdaSysTableColumn().getDataType();
                                        }
                                        break;
                                    }
                                }
                            }

                            if(StringUtil.isNotEmpty(dataType)) {
                                updateCustomGroupInfo.setAttrColType(dataType);
                            } else if("ORACLE".equalsIgnoreCase(dbType)) {
                                updateCustomGroupInfo.setAttrColType("VARCHAR2(512)");
                            } else {
                                updateCustomGroupInfo.setAttrColType("VARCHAR(512)");
                            }

                            if(StringUtil.isNotEmpty(ciCustomSceneRelChild)) {
                                updateCustomGroupInfo.setAttrColName(ciCustomSceneRelChild);
                            } else {
                                updateCustomGroupInfo.setAttrColName(var25.getLabelName());
                            }

                            updateCustomGroupInfo.setAttrSource(2);
                            updateCustomGroupInfo.setLabelOrCustomId(String.valueOf(var25.getLabelId()));
                            session.setAttrCol(CiUtil.genCustomAttrColumnName(columnNameIndex));
                            ++columnNameIndex;
                            updateCustomGroupInfo.setId(session);
                            updateCustomGroupInfo.setStatus(Integer.valueOf(0));
                            if(StringUtils.isNotEmpty(msg)) {
                                String[] var37 = msg.split(",");
                                if(var37.length > 20) {
                                    try {
                                        String var38 = this.customersService.createValueTableName();
                                        updateCustomGroupInfo.setTableName(var38);
                                        this.customersService.addExactValueToTable(msg, var38);
                                        updateCustomGroupInfo.setAttrVal((String)null);
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
            CiCustomSceneRel var30;
            if(StringUtil.isNotEmpty(this.sceneIds)) {
                String[] var26 = this.sceneIds.split(",");

                for(int var27 = 0; var27 < var26.length; ++var27) {
                    if(StringUtil.isNotEmpty(var26[var27].trim())) {
                        var30 = new CiCustomSceneRel();
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
            Iterator var29 = var28.iterator();

            label173:
            while(true) {
                do {
                    if(!var29.hasNext()) {
                        if(var24.size() > 0) {
                            this.ciCustomGroupInfo.setSceneList(var24);
                        }

                        if(((Boolean)returnMap.get("success")).booleanValue()) {
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
                            if(this.oldIsHasList != null && this.oldIsHasList.intValue() == 0 && this.ciCustomGroupInfo.getIsHasList().intValue() == 1) {
                                var31.setUpdateCycle(this.ciCustomGroupInfo.getUpdateCycle());
                                var31.setStartDate(this.ciCustomGroupInfo.getStartDate());
                                if(this.ciCustomGroupInfo.getUpdateCycle().intValue() == 3) {
                                    CacheBase var32 = CacheBase.getInstance();
                                    var31.setDataDate(var32.getNewLabelDay());
                                }
                            }

                            var31.setCreateUserId(response);
                            HttpSession var34 = this.getSession();
                            this.ciLabelRuleList = (List)var34.getAttribute("sessionModelList");
                            if(StringUtil.isNotEmpty(this.isSaveTemplate)) {
                                this.customersService.modifyCiCustomGroupInfo(var31, this.ciLabelRuleList, this.ciTemplateInfo, response, true, (List)null);
                            } else {
                                this.customersService.modifyCiCustomGroupInfo(var31, this.ciLabelRuleList, (CiTemplateInfo)null, response, false, attrList);
                            }

                            returnMap.put("customGroupId", this.ciCustomGroupInfo.getCustomGroupId());
                            returnMap.put("isHasList", this.ciCustomGroupInfo.getIsHasList());
                            msg = "修改成功！";
                            returnMap.put("cmsg", msg);
                            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_UPDATE", this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "修改用户群【" + this.ciCustomGroupInfo.getCustomGroupName() + "】成功", OperResultEnum.Success, LogLevelEnum.Medium);
                        }
                        break label173;
                    }

                    var30 = (CiCustomSceneRel)var29.next();
                } while(!StringUtil.isNotEmpty(var30.getId().getSceneId()));

                var30.setStatus(0);
                boolean var33 = false;
                Iterator var35 = var24.iterator();

                label170: {
                    CiCustomSceneRel var36;
                    do {
                        if(!var35.hasNext()) {
                            break label170;
                        }

                        var36 = (CiCustomSceneRel)var35.next();
                    } while(!var30.getId().getSceneId().equals(var36.getId().getSceneId()) && var30.getId().getSceneId() != var36.getId().getSceneId());

                    var33 = true;
                }

                if(!var33) {
                    var24.add(var30);
                }
            }
        } catch (Exception var21) {
            String e = "修改用户群";
            this.log.error(e + " : " + this.ciCustomGroupInfo.getCustomGroupName() + " 失败", var21);
            returnMap.put("cmsg", e + "失败");
            returnMap.put("success", Boolean.valueOf(false));
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_UPDATE", this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "修改用户群【" + this.ciCustomGroupInfo.getCustomGroupName() + "】失败", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        HttpServletResponse var22 = this.getResponse();

        try {
            this.sendJson(var22, JsonUtil.toJson(returnMap));
        } catch (Exception var19) {
            this.log.error("发送json串异常", var19);
            throw new CIServiceException(var19);
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
            if(cg != null && (cg.getCreateUserId().equals(this.getUserId()) || PrivilegeServiceUtil.isAdminUser(this.getUserId()))) {
                createTypeId = cg.getCreateTypeId();
                updateCycleId = cg.getUpdateCycle();
                if(createTypeId != null && updateCycleId != null) {
                    success = true;
                } else {
                    success = false;
                    msg = "没有查询到创建方式或者更新周期！";
                }
            } else {
                success = false;
                msg = "没有权限修改用户群！";
            }

            if(success && (0 == cg.getStatus().intValue() || 2 == cg.getDataStatus().intValue())) {
                success = false;
                msg = "不能修改已删除的用户群！";
            }
        } catch (Exception var7) {
            this.log.error("预编辑用户群失败，customGroupId = " + this.ciCustomGroupInfo.getCustomGroupId(), var7);
            success = false;
            msg = var7.getMessage();
            throw new CIServiceException(msg, var7);
        }

        this.resultMap = new HashMap();
        this.resultMap.put("success", Boolean.valueOf(success));
        this.resultMap.put("msg", msg);
        this.ciCustomGroupInfo = cg;
        return "preEdit";
    }

    public void editCustomSimple() {
        Map returnMap = null;
        Map returnTemplateMap = null;

        try {
            String response = this.getUserId();
            Integer e1 = this.ciCustomGroupInfo.getIsPrivate();
            if(e1.intValue() == 0) {
                returnMap = this.customersService.isShareNameExist(this.ciCustomGroupInfo, response, (String)null);
                if(returnMap.get("msg") != null && !"".equals(returnMap.get("msg"))) {
                    returnMap.put("cmsg", returnMap.get("msg"));
                }
            } else {
                returnMap = this.customersService.isNameExist(this.ciCustomGroupInfo, response);
            }

            if(StringUtil.isNotEmpty(this.isSaveTemplate)) {
                returnTemplateMap = this.ciTemplateInfoService.isNameExist(this.ciTemplateInfo, response);
                returnMap.put("success", Boolean.valueOf(((Boolean)returnTemplateMap.get("success")).booleanValue() && ((Boolean)returnMap.get("success")).booleanValue()));
                returnMap.put("msg", returnTemplateMap.get("msg"));
            }

            if(((Boolean)returnMap.get("success")).booleanValue()) {
                CiCustomGroupInfo updateCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupInfo.getCustomGroupId());
                updateCustomGroupInfo.setCustomGroupName(this.ciCustomGroupInfo.getCustomGroupName());
                updateCustomGroupInfo.setCustomGroupDesc(this.ciCustomGroupInfo.getCustomGroupDesc());
                updateCustomGroupInfo.setSceneList(this.ciCustomGroupInfo.getSceneList());
                updateCustomGroupInfo.setCreateUserId(response);
                HttpSession session = this.getSession();
                this.ciLabelRuleList = (List)session.getAttribute("sessionModelList");
                if(StringUtil.isNotEmpty(this.isSaveTemplate)) {
                    this.customersService.modifyCiCustomGroupInfo(updateCustomGroupInfo, this.ciLabelRuleList, this.ciTemplateInfo, response, true, (List)null);
                } else {
                    this.customersService.modifyCiCustomGroupInfoSimple(updateCustomGroupInfo);
                }

                String msg = "修改成功！";
                returnMap.put("cmsg", msg);
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_UPDATE", this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "修改用户群【" + this.ciCustomGroupInfo.getCustomGroupName() + "】成功", OperResultEnum.Success, LogLevelEnum.Medium);
            }
        } catch (Exception var9) {
            String e = "修改用户群";
            this.log.error(e + " : " + this.ciCustomGroupInfo.getCustomGroupName() + " 失败", var9);
            returnMap.put("cmsg", e + "失败");
            returnMap.put("success", Boolean.valueOf(false));
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_UPDATE", this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "修改用户群【" + this.ciCustomGroupInfo.getCustomGroupName() + "】失败", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMap));
        } catch (Exception var8) {
            this.log.error("发送json串异常", var8);
            throw new CIServiceException(var8);
        }
    }

    public void isNameExist() throws Exception {
        boolean success = false;

        try {
            String response = this.getUserId();
            String e = this.ciCustomGroupInfo.getCustomGroupName().trim();
            if(StringUtil.isNotEmpty(e) && StringUtil.isNotEmpty(response)) {
                if(PrivilegeServiceUtil.isAdminUser(response)) {
                    response = null;
                }

                success = true;
            }

            if(success) {
                Object createCityId = null;
                List list = null;
                if(StringUtil.isNotEmpty(this.ciCustomGroupInfo.getIsPrivate())) {
                    if(this.ciCustomGroupInfo.getIsPrivate().intValue() == 1) {
                        list = this.customersService.queryCiCustomGroupInfoListByName(e, response, (String)createCityId);
                    }

                    if(this.ciCustomGroupInfo.getIsPrivate().intValue() == 0) {
                        list = this.customersService.queryCiCustomGroupInfoListByShareName(e);
                    }
                } else {
                    list = this.customersService.queryCiCustomGroupInfoListByName(e, response, (String)createCityId);
                }

                if(list != null && list.size() > 0) {
                    CiCustomGroupInfo customGroupInfo = (CiCustomGroupInfo)list.get(0);
                    if(!customGroupInfo.getCustomGroupId().equals(this.ciCustomGroupInfo.getCustomGroupId())) {
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

    private void setSortGroupAttrs(List<CiGroupAttrRel> attrList) {
        if(this.sortAttrs != null) {
            for(int i = 0; i < this.sortAttrs.size(); ++i) {
                CiGroupAttrRel sortAttr = (CiGroupAttrRel)this.sortAttrs.get(i);
                Integer attrSource = Integer.valueOf(sortAttr.getAttrSource());
                if(attrSource.intValue() == 3) {
                    for(int var8 = 0; var8 < attrList.size(); ++var8) {
                        CiGroupAttrRel var9 = (CiGroupAttrRel)attrList.get(var8);
                        if(sortAttr.getLabelOrCustomId().equals(var9.getLabelOrCustomId()) && sortAttr.getLabelOrCustomColumn().equals(var9.getLabelOrCustomColumn())) {
                            var9.setSortNum(sortAttr.getSortNum());
                            var9.setSortType(sortAttr.getSortType());
                            break;
                        }
                    }
                } else {
                    Integer isVert = Integer.valueOf(sortAttr.getIsVerticalAttr());
                    int j;
                    CiGroupAttrRel attrRel;
                    if(isVert.intValue() == 1) {
                        for(j = 0; j < attrList.size(); ++j) {
                            attrRel = (CiGroupAttrRel)attrList.get(j);
                            if(sortAttr.getLabelOrCustomColumn().equals(attrRel.getLabelOrCustomColumn())) {
                                attrRel.setSortNum(sortAttr.getSortNum());
                                attrRel.setSortType(sortAttr.getSortType());
                                break;
                            }
                        }
                    } else {
                        for(j = 0; j < attrList.size(); ++j) {
                            attrRel = (CiGroupAttrRel)attrList.get(j);
                            if(sortAttr.getLabelOrCustomId().equals(attrRel.getLabelOrCustomId())) {
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

    public void delete() throws Exception {
        boolean success = false;
        String msg = "";
        String msgFailed = "";
        int successCount = 0;
        int failedCount = 0;
        HashMap returnMsg = new HashMap();

        try {
            if(this.ciCustomGroupInfo.getCustomGroupId().contains(",")) {
                String[] response = this.ciCustomGroupInfo.getCustomGroupId().split(",");

                for(int e = 0; e < response.length; ++e) {
                    if(StringUtils.isNotBlank(response[e])) {
                        CiCustomGroupInfo name = this.customersService.queryCiCustomGroupInfo(response[e]);
                        if(name == null || !name.getCreateUserId().equals(this.getUserId()) && !PrivilegeServiceUtil.isAdminUser(this.getUserId())) {
                            success = false;
                            ++failedCount;
                            msgFailed = msgFailed + "用户群id:\'" + this.ciCustomGroupInfo.getCustomGroupId();
                        } else {
                            this.customersService.deleteCustomSceneRels(name.getCustomGroupId(), Integer.valueOf(ServiceConstants.RECOM_SHOW_TYPE_CUSTOM));
                            this.customersService.deleteUserAttentionCustom(name.getCustomGroupId(), this.getUserId());
                            this.customersService.deleteCiCustomGroupInfo(name);
                            CIAlarmServiceUtil.deleteAlarmThresholdByBusiId("CustomersAlarm", response[e]);
                            success = true;
                            msg = "删除成功";
                            ++successCount;
                        }
                    }
                }

                if(failedCount > 0) {
                    msgFailed = msgFailed + "\'无法删除。";
                    this.log.debug("删除失败的数量：" + failedCount + "," + msgFailed);
                }

                if(successCount > 0) {
                    msg = "成功删除" + successCount + "个用户群";
                    CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_DELETE", "-1", "批量删除用户群", "用户群删除成功【" + msg + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                }
            } else {
                CiCustomGroupInfo var12 = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupInfo.getCustomGroupId());
                String var14 = var12.getCustomGroupId();
                String var15 = var12.getCustomGroupName();
                if(var12 == null || !var12.getCreateUserId().equals(this.getUserId()) && !PrivilegeServiceUtil.isAdminUser(this.getUserId())) {
                    success = false;
                    msg = "删除失败，用户群id\'" + this.ciCustomGroupInfo.getCustomGroupId() + "\'不存在。";
                    CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_DELETE", var14, var15, "用户群删除失败【用户群ID：" + var12.getCustomGroupId() + "用户群名称：" + var12.getCustomGroupName() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
                } else {
                    this.customersService.deleteCustomSceneRels(var12.getCustomGroupId(), Integer.valueOf(ServiceConstants.RECOM_SHOW_TYPE_CUSTOM));
                    this.customersService.deleteUserAttentionCustom(var14, this.getUserId());
                    this.customersService.deleteCiCustomGroupInfo(var12);
                    CIAlarmServiceUtil.deleteAlarmThresholdByBusiId("CustomersAlarm", var14);
                    success = true;
                    msg = "删除成功";
                    CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_DELETE", var14, var15, "用户群删除成功【用户群ID：" + var12.getCustomGroupId() + "用户群名称：" + var12.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                }
            }
        } catch (Exception var11) {
            this.log.error("删除用户群失败，customGroupId = " + this.ciCustomGroupInfo.getCustomGroupId());
            success = false;
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_DELETE", "-1", "用户群删除失败", "用户群删除失败【用户群ID：" + this.ciCustomGroupInfo.getCustomGroupId() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
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

    public String importInit() throws CIServiceException {
        CacheBase cache = CacheBase.getInstance();
        this.dimScenes =(List<DimScene>) cache.getObjectList("DIM_SCENE");
        return "importInit";
    }

    public void downImportTempletFile() {
        ServletOutputStream out = null;
        FileInputStream fileInputStream = null;
        String fileName = Configure.getInstance().getProperty("SYS_COMMON_DOWN_NAME");
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
            this.log.error(var29);
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

    public String uploadCustomGroupFile() throws CIServiceException {
        String forward = "importSuccess";

        try {
            ArrayList e = new ArrayList();
            if(this.attrList != null) {
                Iterator createCityId = this.attrList.iterator();

                while(createCityId.hasNext()) {
                    String sceneList = (String)createCityId.next();
                    Column userId = new Column();
                    if(StringUtil.isNotEmpty(sceneList)) {
                        userId.setColumnName(sceneList);
                        e.add(userId);
                    }
                }
            }

            this.customerName = this.ciCustomGroupInfo.getCustomGroupName();
            String var10 = PrivilegeServiceUtil.getCityIdFromSession();
            this.ciCustomGroupInfo.setCreateCityId(var10);
            ArrayList var11 = new ArrayList();
            String var12 = this.getUserId();
            if(StringUtil.isNotEmpty(this.sceneIds)) {
                String[] ctx = this.sceneIds.split(",");

                for(int dataDate = 0; dataDate < ctx.length; ++dataDate) {
                    if(StringUtil.isNotEmpty(ctx[dataDate].trim())) {
                        CiCustomSceneRel item = new CiCustomSceneRel();
                        item.setId(new CiCustomSceneRelId());
                        item.getId().setSceneId(ctx[dataDate].trim());
                        item.getId().setUserId(var12);
                        item.setStatus(1);
                        var11.add(item);
                    }
                }

                this.ciCustomGroupInfo.setSceneList(var11);
            }

            ActionContext var13 = ActionContext.getContext();
            var13.put("customerName", this.customerName);
            String var14 = this.ciCustomGroupInfo.getDataDate();
            if(StringUtil.isNotEmpty(var14)) {
                if(var14.length() == 10) {
                    this.ciCustomGroupInfo.setDataDate(DateUtil.string2StringFormat(var14, "yyyy-MM-dd", "yyyyMMdd"));
                } else if(var14.length() == 7) {
                    this.ciCustomGroupInfo.setDataDate(DateUtil.string2StringFormat(var14, "yyyy-MM", "yyyyMM"));
                }
            }

            if(this.file != null && this.file.length() > 0L) {
                this.customersService.importCustomListFile(this.file, this.fileFileName, var12, this.ciCustomGroupInfo, e, Integer.valueOf(this.isHaveAttrTitle));
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_IMPORT", this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "导入创建用户群【" + this.ciCustomGroupInfo.getCustomGroupName() + "】成功！", OperResultEnum.Success, LogLevelEnum.Risk);
            }
        } catch (Exception var9) {
            this.log.error("导入失败", var9);
            forward = "importFail";
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_IMPORT", "-1", "-1", "导入用户群失败", OperResultEnum.Failure, LogLevelEnum.Risk);
        }

        return forward;
    }

    public void createCustomByImportTable() throws Exception {
        Map returnMap = null;

        try {
            String response = PrivilegeServiceUtil.getCityIdFromSession();
            this.ciCustomGroupInfo.setCreateCityId(response);
            this.ciCustomGroupInfo.setCreateTypeId(Integer.valueOf(12));
            ArrayList var16 = new ArrayList();
            String userId = this.getUserId();
            if(StringUtil.isNotEmpty(this.sceneIds)) {
                String[] dbType = this.sceneIds.split(",");

                for(int ciGroupAttrRelList = 0; ciGroupAttrRelList < dbType.length; ++ciGroupAttrRelList) {
                    if(StringUtil.isNotEmpty(dbType[ciGroupAttrRelList].trim())) {
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

            if(StringUtil.isNotEmpty(this.ciCustomGroupInfo.getIsPrivate())) {
                if(this.ciCustomGroupInfo.getIsPrivate().intValue() == 1) {
                    returnMap = this.customersService.isNameExist(this.ciCustomGroupInfo, userId);
                }

                if(this.ciCustomGroupInfo.getIsPrivate().intValue() == 0) {
                    returnMap = this.customersService.isShareNameExist(this.ciCustomGroupInfo, userId, (String)null);
                    if(StringUtil.isNotEmpty(returnMap.get("msg"))) {
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
            if(this.importColumnList != null && this.importColumnList.size() > 0) {
                for(int msg = 0; msg < this.importColumnList.size(); ++msg) {
                    ImportColumn importColumn = (ImportColumn)this.importColumnList.get(msg);
                    CiGroupAttrRel rel = new CiGroupAttrRel();
                    CiGroupAttrRelId relId = new CiGroupAttrRelId();
                    String attrCol = "";
                    if(StringUtil.isNotEmpty(importColumn.getColumnName())) {
                        attrCol = importColumn.getColumnName();
                    }

                    if(!attrCol.equalsIgnoreCase(var19)) {
                        rel.setAttrColName(importColumn.getColumnCnName());
                        rel.setAttrSource(1);
                        if(StringUtil.isNotEmpty(importColumn.getColumnType())) {
                            rel.setAttrColType(importColumn.getColumnType());
                        } else if("ORACLE".equalsIgnoreCase(var17)) {
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

            if(((Boolean)returnMap.get("success")).booleanValue()) {
                this.customersService.addCiCustomGroupInfo(this.ciCustomGroupInfo, (List)null, (List)null, (CiTemplateInfo)null, userId, false, var18);
                String var20 = "创建成功！";
                returnMap.put("cmsg", var20);
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_IMPORT", this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "创建用户群【" + this.ciCustomGroupInfo.getCustomGroupName() + "】成功", OperResultEnum.Success, LogLevelEnum.Medium);
            }
        } catch (Exception var14) {
            String e = "创建用户群";
            this.log.error(e + " : " + this.ciCustomGroupInfo.getCustomGroupName() + " 失败", var14);
            returnMap.put("cmsg", e + "失败");
            returnMap.put("success", Boolean.valueOf(false));
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_IMPORT", "-1", this.ciCustomGroupInfo.getCustomGroupName(), "创建用户群【" + this.ciCustomGroupInfo.getCustomGroupName() + "】失败", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        HttpServletResponse var15 = this.getResponse();

        try {
            this.sendJson(var15, JsonUtil.toJson(returnMap));
        } catch (Exception var13) {
            this.log.error("发送json串异常", var13);
            throw new CIServiceException(var13);
        }
    }

    public String findGroupInfo() throws Exception {
        this.ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupInfo.getCustomGroupId());
        String offsetStr = this.customersService.queryCustomerOffsetStr(this.ciCustomGroupInfo);
        this.ciCustomGroupInfo.setKpiDiffRule((this.ciCustomGroupInfo.getKpiDiffRule() == null?"":this.ciCustomGroupInfo.getKpiDiffRule()) + offsetStr);
        if(this.ciCustomGroupInfo.getMonthLabelDate() != null && !this.ciCustomGroupInfo.getMonthLabelDate().equals("")) {
            this.ciCustomGroupInfo.setMonthLabelDate(DateUtil.string2StringFormat(this.ciCustomGroupInfo.getMonthLabelDate(), "yyyyMM", "yyyy-MM"));
        }

        if(this.ciCustomGroupInfo.getDayLabelDate() != null && !this.ciCustomGroupInfo.getDayLabelDate().equals("")) {
            this.ciCustomGroupInfo.setDayLabelDate(DateUtil.string2StringFormat(this.ciCustomGroupInfo.getDayLabelDate(), "yyyyMMdd", "yyyy-MM-dd"));
        }

        if((this.ciCustomGroupInfo.getMonthLabelDate() == null || this.ciCustomGroupInfo.getMonthLabelDate().equals("")) && (this.ciCustomGroupInfo.getDayLabelDate() == null || this.ciCustomGroupInfo.getDayLabelDate().equals(""))) {
            if(this.ciCustomGroupInfo.getDataDate() == null) {
                this.ciCustomGroupInfo.setDataDateStr("");
            } else if(this.ciCustomGroupInfo.getDataDate().length() > 6) {
                this.ciCustomGroupInfo.setDataDateStr(DateUtil.string2StringFormat(this.ciCustomGroupInfo.getDataDate(), "yyyyMMdd", "yyyy-MM-dd"));
            } else {
                this.ciCustomGroupInfo.setDataDateStr(DateUtil.string2StringFormat(this.ciCustomGroupInfo.getDataDate(), "yyyyMM", "yyyy-MM"));
            }
        }

        return "queryGroupInfoList";
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
            if(this.pager == null) {
                this.pager = new Pager();
            }

            this.pager.setPageNum(this.page);
            this.pager.setPageSize(this.rows);
            this.pager.setTotalSize((long)this.customersService.queryListExeInfoNumByTableName(this.ciCustomListInfo.getListTableName()));
            list = this.customersService.queryListExeInfosByTableName(this.pager, this.ciCustomListInfo.getListTableName());
            success = true;
        } catch (Exception var8) {
            msg = "查询用户群清单执行sql数据错误";
            this.log.error(msg, var8);
            success = false;
        }

        if(success) {
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

    public String showRule() throws Exception {
        try {
            this.ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupInfo.getCustomGroupId());
            String e = this.customersService.queryCustomerOffsetStr(this.ciCustomGroupInfo);
            this.ciCustomGroupInfo.setKpiDiffRule((this.ciCustomGroupInfo.getKpiDiffRule() == null?"":this.ciCustomGroupInfo.getKpiDiffRule()) + e);
            return "showRule";
        } catch (Exception var3) {
            String message = "查看用户群完整规则失败";
            this.log.error(message + ": customGroupId = " + this.ciCustomGroupInfo.getCustomGroupId(), var3);
            throw new CIServiceException(message + ": customGroupId = " + this.ciCustomGroupInfo.getCustomGroupId(), var3);
        }
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public Integer getHavePushFun() {
        return this.havePushFun;
    }

    public void setHavePushFun(Integer havePushFun) {
        this.havePushFun = havePushFun;
    }

    public Map<String, Object> getResultMap() {
        return this.resultMap;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }

    public CiCustomGroupInfo getCiCustomGroupInfo() {
        return this.ciCustomGroupInfo;
    }

    public void setCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo) {
        this.ciCustomGroupInfo = ciCustomGroupInfo;
    }

    public CiCustomListInfo getCiCustomListInfo() {
        return this.ciCustomListInfo;
    }

    public void setCiCustomListInfo(CiCustomListInfo ciCustomListInfo) {
        this.ciCustomListInfo = ciCustomListInfo;
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

    public String getIsPush() {
        return this.isPush;
    }

    public void setIsPush(String isPush) {
        this.isPush = isPush;
    }

    public List<CiSysInfo> getCiSysInfoList() {
        return this.ciSysInfoList;
    }

    public void setCiSysInfoList(List<CiSysInfo> ciSysInfoList) {
        this.ciSysInfoList = ciSysInfoList;
    }

    public List<CiSysInfo> getSysInfoCycleList() {
        return this.sysInfoCycleList;
    }

    public void setSysInfoCycleList(List<CiSysInfo> sysInfoCycleList) {
        this.sysInfoCycleList = sysInfoCycleList;
    }

    public String getIsSaveTemplate() {
        return this.isSaveTemplate;
    }

    public void setIsSaveTemplate(String isSaveTemplate) {
        this.isSaveTemplate = isSaveTemplate;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getIsHaveAttrTitle() {
        return this.isHaveAttrTitle;
    }

    public void setIsHaveAttrTitle(String isHaveAttrTitle) {
        this.isHaveAttrTitle = isHaveAttrTitle;
    }

    public CiTemplateInfo getCiTemplateInfo() {
        return this.ciTemplateInfo;
    }

    public void setCiTemplateInfo(CiTemplateInfo ciTemplateInfo) {
        this.ciTemplateInfo = ciTemplateInfo;
    }

    public List<CiLabelRule> getCiLabelRuleList() {
        return this.ciLabelRuleList;
    }

    public void setCiLabelRuleList(List<CiLabelRule> ciLabelRuleList) {
        this.ciLabelRuleList = ciLabelRuleList;
    }

    public List<CiGroupAttrRel> getCiGroupAttrRelList() {
        return this.ciGroupAttrRelList;
    }

    public void setCiGroupAttrRelList(List<CiGroupAttrRel> ciGroupAttrRelList) {
        this.ciGroupAttrRelList = ciGroupAttrRelList;
    }

    public List<CiLabelInfo> getVertLabelAttrList() {
        return this.vertLabelAttrList;
    }

    public void setVertLabelAttrList(List<CiLabelInfo> vertLabelAttrList) {
        this.vertLabelAttrList = vertLabelAttrList;
    }

    public List<CiLabelInfo> getCiLabelInfoList() {
        return this.ciLabelInfoList;
    }

    public void setCiLabelInfoList(List<CiLabelInfo> ciLabelInfoList) {
        this.ciLabelInfoList = ciLabelInfoList;
    }

    public List<CiGroupAttrRel> getSortAttrs() {
        return this.sortAttrs;
    }

    public void setSortAttrs(List<CiGroupAttrRel> sortAttrs) {
        this.sortAttrs = sortAttrs;
    }

    public Integer getOldIsHasList() {
        return this.oldIsHasList;
    }

    public void setOldIsHasList(Integer oldIsHasList) {
        this.oldIsHasList = oldIsHasList;
    }

    public String getSceneIds() {
        return this.sceneIds;
    }

    public void setSceneIds(String sceneIds) {
        this.sceneIds = sceneIds;
    }

    public List<String> getAttrList() {
        return this.attrList;
    }

    public void setAttrList(List<String> attrList) {
        this.attrList = attrList;
    }

    public List<ImportColumn> getImportColumnList() {
        return this.importColumnList;
    }

    public void setImportColumnList(List<ImportColumn> importColumnList) {
        this.importColumnList = importColumnList;
    }
}
