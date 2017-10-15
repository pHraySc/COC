package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.constant.CommonConstants;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomGroupPushCycle;
import com.ailk.biapp.ci.entity.CiCustomListExeInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiCustomModifyHistory;
import com.ailk.biapp.ci.entity.CiCustomPushReq;
import com.ailk.biapp.ci.entity.CiCustomSourceRel;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.entity.CiLabelExtInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiLabelVerticalColumnRel;
import com.ailk.biapp.ci.entity.CiListExeSqlAll;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.entity.CiPersonNotice;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ICiCustomFileRelService;
import com.ailk.biapp.ci.service.ICiGroupAttrRelService;
import com.ailk.biapp.ci.service.ICiPersonNoticeService;
import com.ailk.biapp.ci.service.ICustomGroupStatService;
import com.ailk.biapp.ci.service.ICustomersAnalysisService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.task.CiProductMatchThread;
import com.ailk.biapp.ci.task.CustomerFileCreaterThread;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.GroupCalcSqlPaser;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.ThreadPool;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.ailk.biapp.ci.webservice.IWsSendNoticeToIMcdClient;
import com.ailk.biapp.ci.webservice.IWsSendNoticeToVgopClient;
import com.asiainfo.biframe.dimtable.model.DimTableDefine;
import com.asiainfo.biframe.dimtable.service.impl.DimTableServiceImpl;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class CustomerListCreaterThread extends Thread {
    private Logger log = Logger.getLogger(CustomerListCreaterThread.class);
    @Autowired
    private ICustomersManagerService customersManagerService;
    @Autowired
    ICustomGroupStatService customGroupStatService;
    @Autowired
    ICiPersonNoticeService ciPersonNoticeService;
    @Autowired
    private ICustomersAnalysisService customersAnalysisService;
    @Autowired
    private ICiGroupAttrRelService ciGroupAttrRelService;
    @Autowired
    private ICiCustomFileRelService ciCustomFileRelService;
    @Autowired
    private DimTableServiceImpl dimTableServiceImpl;
    @Autowired
    private IWsSendNoticeToVgopClient sendNoticeClient;
    @Autowired
    private IWsSendNoticeToIMcdClient sendNoticeToIMcdClient;
    private String customGroupId;
    private CiCustomListInfo ciCustomListInfo;
    private CiCustomGroupInfo ciCustomGroupInfo;
    private List<CiGroupAttrRel> ciGroupAttrRelList;
    private boolean needRecreateListFile = false;
    private Boolean isPeriodCreateListTable = Boolean.valueOf(false);

    public CustomerListCreaterThread() {
    }

    public Boolean getIsPeriodCreateListTable() {
        return this.isPeriodCreateListTable;
    }

    public void setIsPeriodCreateListTable(Boolean isPeriodCreateListTable) {
        this.isPeriodCreateListTable = isPeriodCreateListTable;
    }

    public void setCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo) {
        try {
            this.ciCustomGroupInfo = ciCustomGroupInfo.clone();
        } catch (Exception var3) {
            this.log.error("copy propertis error:", var3);
        }

        this.customGroupId = ciCustomGroupInfo.getCustomGroupId();
        if (this.customGroupId == null) {
            throw new CIServiceException("ciCustomGroupInfo.customGroupId can not be null!" + ciCustomGroupInfo);
        }
    }

    public void run() {
        this.log.info("Enter CustomerListCreaterThread.run()");
        if (this.ciCustomGroupInfo.getCreateTypeId().intValue() != 7 && this.ciCustomGroupInfo.getCreateTypeId().intValue() != 12 && this.ciCustomGroupInfo.getCreateTypeId().intValue() != 10 && this.ciCustomGroupInfo.getCreateTypeId().intValue() != 11) {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException var19) {
                this.log.error("InterruptedException", var19);
            }

            List listInfos = this.customersManagerService.queryCiCustomListInfoByCGroupId(this.customGroupId);
            ArrayList createdListInfo = new ArrayList();
            int waitListNum = 0;
            String LabelUpdateInBatch = Configure.getInstance().getProperty("LABEL_UPDATE_IN_BATCHES");
            boolean ifLabelUpdateInBatch = false;
            if (StringUtil.isNotEmpty(LabelUpdateInBatch) && "true".equalsIgnoreCase(LabelUpdateInBatch)) {
                ifLabelUpdateInBatch = true;
            }

            boolean isLatestTurnRun = false;
            if (ifLabelUpdateInBatch) {
                String i$ = "";
                Calendar list = Calendar.getInstance();
                long e = list.getTimeInMillis();
                if (3 == this.ciCustomGroupInfo.getUpdateCycle().intValue()) {
                    i$ = Configure.getInstance().getProperty("LATEST_TURN_RUN_DAILY_CUSTOMGROUP");
                    if (StringUtil.isNotEmpty(i$)) {
                        String[] sql1 = i$.split(":");
                        list.set(11, Integer.valueOf(sql1[0]).intValue());
                        list.set(12, Integer.valueOf(sql1[1]).intValue());
                        list.set(13, Integer.valueOf(sql1[2]).intValue());
                        long deleteSql = list.getTimeInMillis();
                        if (e > deleteSql) {
                            isLatestTurnRun = true;
                        }
                    } else {
                        this.log.debug("LATEST_TURN_RUN_DAILY_CUSTOMGROUP配置信息为空");
                    }
                } else if (2 == this.ciCustomGroupInfo.getUpdateCycle().intValue()) {
                    i$ = Configure.getInstance().getProperty("LATEST_TURN_RUN_MONTHY_CUSTOMGROUP");
                    if (StringUtil.isNotEmpty(i$)) {
                        int var27 = Integer.valueOf(i$).intValue();
                        if (list.get(5) >= var27) {
                            isLatestTurnRun = true;
                        }
                    } else {
                        this.log.debug("LATEST_TURN_RUN_MONTHY_CUSTOMGROUP配置信息为空");
                    }
                }
            }

            CiCustomListInfo var22;
            for (int var21 = listInfos.size() - 1; var21 >= 0; --var21) {
                var22 = (CiCustomListInfo) listInfos.get(var21);
                if (var22.getDataStatus().intValue() == 1) {
                    this.ciCustomListInfo = var22;
                    List var24 = null;

                    try {
                        var24 = this.ciGroupAttrRelService.queryGroupAttrRelList(this.customGroupId, this.ciCustomListInfo.getDataTime());
                        Object sql = new ArrayList();
                        if (var24 == null || var24.size() != 1 || var24.get(0) == null || ((CiGroupAttrRel) var24.get(0)).getStatus() == null || ((CiGroupAttrRel) var24.get(0)).getStatus().intValue() != 2) {
                            sql = var24;
                        }

                        this.setCiGroupAttrRelList((List) sql);
                    } catch (Exception var20) {
                        this.log.error("查询清单属性发生异常！", var20);
                    }

                    String validateFlag;
                    String var26;
                    Date var28;
                    String var29;
                    if (this.isPeriodCreateListTable.booleanValue()) {
                        if (ifLabelUpdateInBatch && !isLatestTurnRun) {
                            var28 = new Date();

                            try {
                                validateFlag = this.customersManagerService.validateLabelDataDate(this.ciCustomGroupInfo, var24, this.ciCustomListInfo.getMonthLabelDate(), this.ciCustomListInfo.getDayLabelDate());
                            } catch (Exception var18) {
                                this.delException(var18, "验证标签状态出错！", var28);
                                return;
                            }

                            this.log.debug("分批次跑客户群   validateFlag: " + validateFlag);
                            if ("2".equals(validateFlag)) {
                                this.log.debug("走分批次跑客户群,listInfoId：" + var22.getListTableName());
                                var29 = this.getSelectSql();
                                this.genListTable(var29);
                                this.sendNotice(var22);
                                createdListInfo.add(var22);
                                ++waitListNum;
                            } else {
                                StringBuffer var30;
                                String var31;
                                if ("1".equals(validateFlag)) {
                                    this.log.debug("走分批次跑客户群,验证未通过：" + var22.getListTableName());
                                    var30 = new StringBuffer();
                                    var30.append("delete from CI_CUSTOM_LIST_INFO where LIST_TABLE_NAME = \'").append(var22.getListTableName()).append("\'");
                                    var31 = var30.toString();
                                    this.customersManagerService.executeInFrontDataBase(var31);
                                    this.ciCustomGroupInfo.setDataStatus(this.ciCustomGroupInfo.getLastDataStatus());
                                    this.customersManagerService.syncUpdateCiCustomGroupInfo(this.ciCustomGroupInfo);
                                } else if ("3".equals(validateFlag)) {
                                    this.log.debug("第一次创建一次性客户群，或月周期客户群时，日标签还没有生成好：" + var22.getListTableName());
                                    var30 = new StringBuffer();
                                    var30.append("delete from CI_CUSTOM_LIST_INFO where LIST_TABLE_NAME = \'").append(var22.getListTableName()).append("\'");
                                    var31 = var30.toString();
                                    this.customersManagerService.executeInFrontDataBase(var31);
                                    this.ciCustomGroupInfo.setDataStatus(Integer.valueOf(4));
                                    if (StringUtils.isNotEmpty(this.ciCustomGroupInfo.getListCreateTime()) && 0 == this.ciCustomGroupInfo.getCustomListCreateFailed().intValue()) {
                                        this.ciCustomGroupInfo.setCustomListCreateFailed(Integer.valueOf(1));
                                    }

                                    if (StringUtils.isEmpty(this.ciCustomGroupInfo.getListCreateTime())) {
                                        this.ciCustomGroupInfo.setIsFirstFailed(Integer.valueOf(1));
                                    }

                                    this.customersManagerService.syncUpdateCiCustomGroupInfo(this.ciCustomGroupInfo);
                                }
                            }
                        } else {
                            this.log.debug("未走分批次跑客户群或最后一批生成,正常生成,listInfoId：" + var22.getListTableName());
                            var26 = this.getSelectSql();
                            this.genListTable(var26);
                            this.sendNotice(var22);
                            createdListInfo.add(var22);
                            ++waitListNum;
                        }
                    } else if (ifLabelUpdateInBatch) {
                        var28 = new Date();

                        try {
                            validateFlag = this.customersManagerService.validateLabelDataDate(this.ciCustomGroupInfo, var24, this.ciCustomListInfo.getMonthLabelDate(), this.ciCustomListInfo.getDayLabelDate());
                        } catch (Exception var17) {
                            this.delException(var17, "验证标签状态出错！", var28);
                            return;
                        }

                        this.log.debug("第一次跑客户群   validateFlag: " + validateFlag);
                        if ("2".equals(validateFlag)) {
                            this.log.debug("第一次跑客户群,验证通过GO，listInfoId：" + var22.getListTableName());
                            var29 = this.getSelectSql();
                            this.genListTable(var29);
                            this.sendNotice(var22);
                            createdListInfo.add(var22);
                            ++waitListNum;
                        } else if ("1".equals(validateFlag)) {
                            this.log.debug("第一次跑周期客户群,验证未通过，改为预约状态：" + var22.getListTableName());
                            this.ciCustomGroupInfo.setDataStatus(Integer.valueOf(4));
                            if (StringUtils.isNotEmpty(this.ciCustomGroupInfo.getListCreateTime()) && (this.ciCustomGroupInfo.getCustomListCreateFailed() == null || this.ciCustomGroupInfo.getCustomListCreateFailed().intValue() == 0)) {
                                this.ciCustomGroupInfo.setCustomListCreateFailed(Integer.valueOf(1));
                            }

                            this.customersManagerService.syncUpdateCiCustomGroupInfo(this.ciCustomGroupInfo);
                            var22.setDataStatus(Integer.valueOf(1));
                            this.customersManagerService.syncUpdateCiCustomListInfo(var22);
                        } else if ("3".equals(validateFlag)) {
                            this.log.debug("第一次创建一次性客户群，或月周期客户群时，日标签还没有生成好：" + var22.getListTableName());
                            this.ciCustomGroupInfo.setDataStatus(Integer.valueOf(4));
                            if (StringUtils.isNotEmpty(this.ciCustomGroupInfo.getListCreateTime()) && (this.ciCustomGroupInfo.getCustomListCreateFailed() == null || this.ciCustomGroupInfo.getCustomListCreateFailed().intValue() == 0)) {
                                this.ciCustomGroupInfo.setCustomListCreateFailed(Integer.valueOf(1));
                            }

                            this.ciCustomGroupInfo.setIsFirstFailed(Integer.valueOf(1));
                            this.customersManagerService.syncUpdateCiCustomGroupInfo(this.ciCustomGroupInfo);
                            var22.setDataStatus(Integer.valueOf(1));
                            this.customersManagerService.syncUpdateCiCustomListInfo(var22);
                        }
                    } else {
                        this.log.debug("第一次创建，分批次跑开关关闭，正常生成,listInfoId：" + var22.getListTableName());
                        var26 = this.getSelectSql();
                        this.genListTable(var26);
                        this.sendNotice(var22);
                        createdListInfo.add(var22);
                        ++waitListNum;
                    }
                }
            }

            if (waitListNum == 0) {
                this.log.debug("没有清单需要跑！");
            } else {
                listInfos = this.customersManagerService.queryCiCustomListInfoByCGroupId(this.customGroupId);
                this.refreshCache(listInfos);

                try {
                    if (this.ciCustomGroupInfo.getDataStatus().intValue() == 3) {
                        this.ciCustomGroupInfo.setThumbnailData(this.customersAnalysisService.getCustomersTrendChartJson(this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getDataDate(), this.ciCustomGroupInfo.getUpdateCycle().intValue()));
                        if (StringUtils.isNotEmpty(this.ciCustomGroupInfo.getThumbnailData())) {
                            this.customersManagerService.syncUpdateCiCustomGroupInfo(this.ciCustomGroupInfo);
                        }
                    }
                } catch (Exception var16) {
                    this.log.error("更新客户群趋势图json串异常", var16);
                }

                if (this.ciCustomGroupInfo.getProductAutoMacthFlag() != null && 1 == this.ciCustomGroupInfo.getProductAutoMacthFlag().intValue()) {
                    this.ciCustomGroupInfo.setProductAutoMacthFlag(Integer.valueOf(this.ciCustomGroupInfo.getProductAutoMacthFlag().intValue() + 2));
                    this.customersManagerService.syncUpdateCiCustomGroupInfo(this.ciCustomGroupInfo);
                    Iterator var23 = createdListInfo.iterator();

                    while (var23.hasNext()) {
                        var22 = (CiCustomListInfo) var23.next();

                        try {
                            CiProductMatchThread var25 = this.getMatcher();
                            var25.setCiCustomListInfo(var22);
                            ThreadPool.getInstance().execute(var25, true);
                        } catch (Exception var15) {
                            this.log.error("自动匹配产品错误", var15);
                        }
                    }
                }

                this.processAutoPush(createdListInfo);
                if (this.getNeedRecreateListFile()) {
                    this.reCreateCustomListFile(createdListInfo);
                }

                this.sendNoticeBySysId((CiCustomListInfo) listInfos.get(0));
                this.log.info("Exit CustomerListCreaterThread.run()");
            }
        } else {
            this.log.info("Exit CustomerListCreaterThread.run() cause : CreateType is import or from SA");
            this.log.info("退出客户群创建线程 : 因为客户群创建类型是导入的或者是sa创建的");
        }
    }

    private void reCreateCustomListFile(List<CiCustomListInfo> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); ++i) {
                CiCustomListInfo info = (CiCustomListInfo) list.get(i);
                CustomerFileCreaterThread upLoadThread = null;
                String msg = null;

                try {
                    upLoadThread = (CustomerFileCreaterThread) SystemServiceLocator.getInstance().getService("customerFileCreaterThread");
                    upLoadThread.setListTableName(info.getListTableName());
                    String e = null;

                    try {
                        CiCustomModifyHistory e1 = this.customersManagerService.queryNewestCustomModifyHistory(info.getCustomGroupId());
                        e = PrivilegeServiceUtil.getCityId(e1.getModifyUserId());
                    } catch (Exception var8) {
                        var8.printStackTrace();
                        this.log.error("根据userId获得cityId失败");
                    }

                    upLoadThread.setDownloadUserCityId(e);
                    ThreadPool.getInstance().execute(upLoadThread);
                    info.setFileCreateStatus(Integer.valueOf(3));
                    this.customersManagerService.updateCustomListInfo(info);
                } catch (Exception var9) {
                    msg = "创建客户群清单文件失败";
                    this.log.error(msg, var9);
                }
            }
        }

    }

    private void statCustomGroup(List<CiCustomListInfo> createdListInfo) {
        Iterator i$ = createdListInfo.iterator();

        while (i$.hasNext()) {
            CiCustomListInfo list = (CiCustomListInfo) i$.next();
            if (list.getDataStatus().intValue() == 3) {
                try {
                    this.customGroupStatService.statCustomGroupCustomNumById(this.ciCustomGroupInfo.getCustomGroupId(), list.getListTableName(), list.getDataDate());
                } catch (CIServiceException var5) {
                    this.log.error("call customGroupStatService.statCustomGroupCustomNumById has exception");
                }
            }
        }

    }

    private void processAutoPush(List<CiCustomListInfo> createdListInfo) {
        if (createdListInfo != null) {
            for (int i = createdListInfo.size() - 1; i >= 0; --i) {
                CiCustomListInfo list = (CiCustomListInfo) createdListInfo.get(i);
                if (list.getDataStatus().intValue() == 3) {
                    String cgId = list.getCustomGroupId();
                    List pushCycleList = this.customersManagerService.queryAllPushCycleByGroupId(cgId);
                    if (pushCycleList != null) {
                        Iterator i$ = pushCycleList.iterator();

                        while (true) {
                            CiCustomGroupPushCycle item;
                            do {
                                if (!i$.hasNext()) {
                                    return;
                                }

                                item = (CiCustomGroupPushCycle) i$.next();
                            } while (item.getPushCycle().intValue() == 1 && item.getIsPushed().intValue() == 1);

                            CiCustomPushReq ciCustomPushReq = new CiCustomPushReq();
                            ciCustomPushReq.setUserId(this.ciCustomGroupInfo.getCreateUserId());
                            ciCustomPushReq.setSysId(item.getId().getSysId());
                            ciCustomPushReq.setReqTime(new Date());
                            ciCustomPushReq.setStatus(Integer.valueOf(1));
                            ciCustomPushReq.setListTableName(list.getListTableName());
                            ciCustomPushReq.setReqId("COC" + CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L));

                            try {
                                ArrayList e = new ArrayList();
                                e.add(item.getId().getSysId());
                                ciCustomPushReq.setSysIds(e);
                                ArrayList customPushRegList = new ArrayList();
                                customPushRegList.add(ciCustomPushReq);
                                this.customersManagerService.pushCustomers(customPushRegList, String.valueOf(item.getPushCycle()));
                                if (item.getPushCycle().intValue() == 1) {
                                    item.setIsPushed(Integer.valueOf(1));
                                    this.customersManagerService.saveCiCustomGroupPushCycle(item);
                                }
                            } catch (Exception var11) {
                                this.log.error("processAutoPush error", var11);
                            }
                        }
                    }
                    break;
                }
            }
        }

    }

    public void sendNotice(CiCustomListInfo listInfo) {
        try {
            StringBuffer e = new StringBuffer();
            e.append("客户群：").append(this.ciCustomGroupInfo.getCustomGroupName()).append("  数据日期为").append(listInfo.getDataDate()).append("的清单生成");
            CiPersonNotice ciPersonNotice = new CiPersonNotice();
            if (listInfo.getDataStatus().intValue() == 3) {
                e.append("成功");
                ciPersonNotice.setIsSuccess(Integer.valueOf(1));
            } else {
                e.append("失败");
                ciPersonNotice.setIsSuccess(Integer.valueOf(0));
            }

            ciPersonNotice.setStatus(Integer.valueOf(1));
            ciPersonNotice.setCustomerGroupId(this.ciCustomGroupInfo.getCustomGroupId());
            ciPersonNotice.setNoticeName(this.ciCustomGroupInfo.getCustomGroupName() + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_PUBLISH_CUSTOMERS_GENERATE);
            ciPersonNotice.setNoticeDetail(e.toString());
            ciPersonNotice.setNoticeSendTime(new Date());
            ciPersonNotice.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_PUBLISH_CUSTOMERS_GENERATE));
            ciPersonNotice.setReadStatus(Integer.valueOf(1));
            ciPersonNotice.setReceiveUserId(this.ciCustomGroupInfo.getCreateUserId());
            this.ciPersonNoticeService.addPersonNotice(ciPersonNotice);
        } catch (CIServiceException var4) {
            this.log.error("send notice error", var4);
        }

    }

    public void sendNoticeBySysId(CiCustomListInfo listInfo) {
        if ("vgop".equalsIgnoreCase(this.ciCustomGroupInfo.getSysId())) {
            String wsdl = Configure.getInstance().getProperty("VGOP_NOTICE_WSDL");
            String targetNamespace = Configure.getInstance().getProperty("VGOP_NOTICE_TARGET_NAMESPACE");
            String methodName = Configure.getInstance().getProperty("VGOP_NOTICE_METHOD_NAME");
            String customGroupId = listInfo.getCustomGroupId();
            String customNum = String.valueOf(listInfo.getCustomNum());
            Integer status = listInfo.getDataStatus();
            if (status.intValue() == 3) {
                status = Integer.valueOf(1);
            } else {
                status = Integer.valueOf(0);
                customNum = "-1";
            }

            this.sendNoticeClient.sendNotice(wsdl, targetNamespace, methodName, customGroupId, customNum, String.valueOf(status));
        }

    }

    private void refreshCache(List<CiCustomListInfo> listInfos) {
        CacheBase.getInstance().put("CI_CUSTOM_LIST_INFO_MAP", this.ciCustomGroupInfo.getCustomGroupId(), listInfos);
    }

    private CiProductMatchThread getMatcher() throws Exception {
        CiProductMatchThread matcher = (CiProductMatchThread) SystemServiceLocator.getInstance().getService("ciProductMatchThread");
        return matcher;
    }

    private String getSelectSql() {
        String sql = null;
        Date startDate = new Date();
        log.info("this.ciCustomGroupInfo.getCreateTypeId().intValue()" + this.ciCustomGroupInfo.getCreateTypeId().intValue());
        try {
            switch (this.ciCustomGroupInfo.getCreateTypeId().intValue()) {//第一次跑客户群，value = 1
                case 1:
                case 2:
                case 3:
                case 8:
                case 13:
                    List e = this.customersManagerService.queryCiLabelRuleList(this.customGroupId, Integer.valueOf(1));
                    boolean hasCustomListOrVerticalLabel = false;
                    CacheBase cache = CacheBase.getInstance();
                    Iterator ciLabelRuleList21 = e.iterator();

                    while (ciLabelRuleList21.hasNext()) {
                        CiLabelRule rule = (CiLabelRule) ciLabelRuleList21.next();
                        if (rule.getElementType() == null) {
                            throw new CIServiceException("计算元素类型为空");
                        }

                        if (5 == rule.getElementType().intValue()) {
                            hasCustomListOrVerticalLabel = true;
                            break;
                        }

                        if (2 == rule.getElementType().intValue() && StringUtil.isEmpty(rule.getParentId())) {
                            CiLabelInfo labelInfo = cache.getEffectiveLabel(rule.getCalcuElement());
                            if (8 == labelInfo.getLabelTypeId().intValue()) {
                                hasCustomListOrVerticalLabel = true;
                                break;
                            }
                        }
                    }

                    if (hasCustomListOrVerticalLabel) {
                        sql = this.getSelectSqlByCustomersRels(e, this.ciCustomListInfo.getMonthLabelDate(), this.ciCustomListInfo.getDayLabelDate(), this.ciCustomListInfo.getDataDate());
                        log.info("当hasCustomListOrVerticalLabel为true时" + hasCustomListOrVerticalLabel);
                    } else {
                        sql = this.getSelectSqlByLabelRules(e, this.ciCustomListInfo.getMonthLabelDate(), this.ciCustomListInfo.getDayLabelDate());
                        log.info("当hasCustomListOrVerticalLabel为false时" + hasCustomListOrVerticalLabel);
                    }
                    break;
                case 4:
                    List ciLabelRuleList2 = this.customersManagerService.queryCiLabelRuleList(this.customGroupId, Integer.valueOf(1));
                    sql = this.getSelectSqlByCustomersRels(ciLabelRuleList2, this.ciCustomListInfo.getMonthLabelDate(), this.ciCustomListInfo.getDayLabelDate(), this.ciCustomGroupInfo.getDataDate());
                    break;
                case 5:
                case 6:
                case 9:
                    sql = this.getSelectSqlByMixedRules();
                case 7:
                case 10:
                case 11:
                case 12:
            }
        } catch (Exception var9) {
            this.delException(var9, this.ciCustomGroupInfo.toString(), startDate);
            this.log.error("CustomerListCreaterThread getSelectSql failed," + this.ciCustomGroupInfo, var9);
        }

        if (sql == null) {
            this.log.error("CustomerListCreaterThread getSelectSql failed," + this.ciCustomGroupInfo);
        }

        return sql;
    }

    private void changeListStatus(int status) {
        Integer lastDataStatus = this.ciCustomGroupInfo.getLastDataStatus();
        this.ciCustomListInfo.setDataStatus(Integer.valueOf(status));
        this.customersManagerService.syncUpdateCiCustomListInfo(this.ciCustomListInfo);
        if (3 == status) {
            this.ciCustomGroupInfo.setDataTime(new Date());
            if (!StringUtils.isEmpty(this.ciCustomGroupInfo.getDataDate()) && Integer.valueOf(this.ciCustomListInfo.getDataDate()).intValue() < Integer.valueOf(this.ciCustomGroupInfo.getDataDate()).intValue()) {
                this.ciCustomGroupInfo.setDataStatus(lastDataStatus);
            } else {
                this.ciCustomGroupInfo.setDataDate(this.ciCustomListInfo.getDataDate());
                this.ciCustomGroupInfo.setDataStatus(Integer.valueOf(status));
                this.ciCustomGroupInfo.setMonthLabelDate(this.ciCustomListInfo.getMonthLabelDate());
                this.ciCustomGroupInfo.setDayLabelDate(this.ciCustomListInfo.getDayLabelDate());
                this.ciCustomGroupInfo.setCustomNum(this.ciCustomListInfo.getCustomNum());
                this.ciCustomGroupInfo.setDuplicateNum(this.ciCustomListInfo.getDuplicateNum());
            }

            if (StringUtils.isNotEmpty(this.ciCustomGroupInfo.getListCreateTime()) && (this.ciCustomGroupInfo.getCustomListCreateFailed() == null || this.ciCustomGroupInfo.getCustomListCreateFailed().intValue() == 1)) {
                this.ciCustomGroupInfo.setCustomListCreateFailed(Integer.valueOf(0));
            }

            if (this.ciCustomGroupInfo.getIsFirstFailed() != null && this.ciCustomGroupInfo.getIsFirstFailed().intValue() == 1) {
                this.ciCustomGroupInfo.setIsFirstFailed(Integer.valueOf(0));
            }

            String provice = Configure.getInstance().getProperty("PROVINCE");
            if (provice != null && "yunnan".equalsIgnoreCase(provice) && (this.ciCustomGroupInfo.getUpdateCycle().intValue() == 2 || this.ciCustomGroupInfo.getUpdateCycle().intValue() == 3)) {
                String wsdl = Configure.getInstance().getProperty("IMCD_NOTICE_WSDL");
                String targetNamespace = Configure.getInstance().getProperty("IMCD_NOTICE_TARGET_NAMESPACE");
                String methodName = Configure.getInstance().getProperty("IMCD_NOTICE_METHOD_NAME");
                this.sendNoticeToIMcdClient.sendNotice(wsdl, targetNamespace, methodName, "coc", this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getDataDate());
            }
        } else if (0 == status) {
            if (!StringUtils.isEmpty(this.ciCustomGroupInfo.getDataDate()) && Integer.valueOf(this.ciCustomListInfo.getDataDate()).intValue() < Integer.valueOf(this.ciCustomGroupInfo.getDataDate()).intValue()) {
                this.ciCustomGroupInfo.setDataStatus(lastDataStatus);
            } else {
                this.ciCustomGroupInfo.setCustomNum((Long) null);
                this.ciCustomGroupInfo.setDataStatus(Integer.valueOf(status));
                this.ciCustomGroupInfo.setDuplicateNum((Long) null);
            }

            if (StringUtils.isNotEmpty(this.ciCustomGroupInfo.getListCreateTime()) && (this.ciCustomGroupInfo.getCustomListCreateFailed() == null || this.ciCustomGroupInfo.getCustomListCreateFailed().intValue() == 1)) {
                this.ciCustomGroupInfo.setCustomListCreateFailed(Integer.valueOf(0));
            }

            if (this.ciCustomGroupInfo.getIsFirstFailed() != null && this.ciCustomGroupInfo.getIsFirstFailed().intValue() == 1) {
                this.ciCustomGroupInfo.setIsFirstFailed(Integer.valueOf(0));
            }
        } else if (2 == status) {
            this.ciCustomGroupInfo.setDataStatus(Integer.valueOf(status));
        } else {
            this.ciCustomGroupInfo.setDataStatus(Integer.valueOf(status));
        }

        this.customersManagerService.syncUpdateCiCustomGroupInfo(this.ciCustomGroupInfo);
    }

    private void genListTable(String selectSQL) {
        this.changeListStatus(2);
        String tableName = this.ciCustomListInfo.getListTableName();
        String tabNameTemplet = Configure.getInstance().getProperty("CUST_LIST_TMP_TABLE");
        String column = Configure.getInstance().getProperty("RELATED_COLUMN");
        String zqScheme = Configure.getInstance().getProperty("ZQCI_BACK_SCHEMA");
        String zqColumn = Configure.getInstance().getProperty("ZQCI_KEYCOLUMN");
        String zqListTableNameInfo = this.ciCustomGroupInfo.getListTableName();
        String[] zqListTableNameInfos = null;
        if (StringUtil.isNotEmpty(zqListTableNameInfo)) {
            zqListTableNameInfos = zqListTableNameInfo.split(",");
        }

        String progressSql = "";
        int distinctTotal = 0;
        Integer listMaxNum = this.ciCustomListInfo.getListMaxNum();
        Date startDate = new Date();

        try {
            try {
                this.customersManagerService.dropTable(tableName);
            } catch (Exception var58) {
                this.log.error("drop清单表出错,可能清单表不存在", var58);
            }

            startDate = new Date();
            progressSql = this.ciCustomFileRelService.createTable(tableName, tabNameTemplet, column, this.ciGroupAttrRelList);
            this.logNormal(progressSql, startDate);
        } catch (Exception var59) {
            this.delException(var59, progressSql, startDate);
            return;
        }

        StringBuffer alertSql = new StringBuffer();

        String tempTableName;
        try {
            startDate = new Date();
            List rows = this.ciCustomFileRelService.addAttr2TmpTable(tableName, this.ciGroupAttrRelList);
            alertSql = new StringBuffer();
            Iterator translateSuccess = rows.iterator();

            while (translateSuccess.hasNext()) {
                tempTableName = (String) translateSuccess.next();
                alertSql.append(tempTableName).append(";");
            }

            if (StringUtil.isNotEmpty(alertSql.toString())) {
                this.logNormal(alertSql.toString(), startDate);
            }
        } catch (Exception var62) {
            this.delException(var62, alertSql.toString(), startDate);
            return;
        }

        boolean var63 = false;
        startDate = new Date();

        int var64;
        try {
            progressSql = this.getInsertAsSelectSql(selectSQL, tableName, column);
            var64 = this.customersManagerService.addInBackDataBase(progressSql, this.ciCustomGroupInfo.getCreateCityId());
            this.logNormal(progressSql, startDate);
        } catch (Exception var57) {
            this.delException(var57, progressSql, startDate);
            return;
        }

        boolean var65 = true;
        startDate = new Date();
        tempTableName = tableName.toLowerCase() + "_T";

        try {
            String duplicateNum = Configure.getInstance().getProperty("CI_BACK_DBTYPE");
            CacheBase cache = CacheBase.getInstance();
            StringBuffer selectSqlSB = new StringBuffer();
            StringBuffer leftJoinSqlSB = new StringBuffer();
            String tableAsName = "list_table_as_name";
            String productNo = Configure.getInstance().getProperty("MAIN_COLUMN");
            StringBuffer attrSqlStr = new StringBuffer(productNo);
            TreeMap orderByColumnMap = new TreeMap();
            int i = 0;
            Iterator orderBySqlSB = this.ciGroupAttrRelList.iterator();

            while (true) {
                String var73;
                String var75;
                while (orderBySqlSB.hasNext()) {
                    CiGroupAttrRel orderBySqlStr = (CiGroupAttrRel) orderBySqlSB.next();
                    if (null != orderBySqlStr.getSortNum() && StringUtil.isNotEmpty(orderBySqlStr.getSortType())) {
                        orderByColumnMap.put(orderBySqlStr.getSortNum(), orderBySqlStr.getId().getAttrCol() + " " + orderBySqlStr.getSortType());
                    }

                    String _index;
                    if (orderBySqlStr.getAttrSource() == 2) {
                        _index = orderBySqlStr.getLabelOrCustomId();
                        CiLabelInfo createTranslatedTableSqlTemp = cache.getEffectiveLabel(_index);
                        CiLabelExtInfo renameSql = createTranslatedTableSqlTemp.getCiLabelExtInfo();
                        CiMdaSysTableColumn e = renameSql.getCiMdaSysTableColumn();
                        boolean createTranslatedTableSql = false;
                        boolean insertSelectSql = false;
                        String colId = orderBySqlStr.getLabelOrCustomColumn();
                        if (StringUtil.isNotEmpty(colId) && 8 == createTranslatedTableSqlTemp.getLabelTypeId().intValue()) {
                            Set dimId = renameSql.getCiLabelVerticalColumnRels();
                            Iterator dimTableDefine = dimId.iterator();

                            while (dimTableDefine.hasNext()) {
                                CiLabelVerticalColumnRel dimTableName = (CiLabelVerticalColumnRel) dimTableDefine.next();
                                if (colId.equals(dimTableName.getCiMdaSysTableColumn().getColumnId().toString())) {
                                    if (5 == dimTableName.getLabelTypeId().intValue()) {
                                        insertSelectSql = true;
                                        if (1 == dimTableName.getCiMdaSysTableColumn().getColumnDataTypeId().intValue()) {
                                            createTranslatedTableSql = true;
                                        }
                                    }
                                    break;
                                }
                            }
                        }

                        if (5 == createTranslatedTableSqlTemp.getLabelTypeId().intValue() || 8 == createTranslatedTableSqlTemp.getLabelTypeId().intValue() && insertSelectSql) {
                            String var78 = "";
                            if (5 == createTranslatedTableSqlTemp.getLabelTypeId().intValue()) {
                                var78 = e.getDimTransId();
                            } else if (8 == createTranslatedTableSqlTemp.getLabelTypeId().intValue() && insertSelectSql) {
                                var78 = cache.getEffectiveColumn(colId).getDimTransId();
                            }

                            DimTableDefine var79 = this.dimTableServiceImpl.findDefineById(var78);
                            if (var79 == null) {
                                this.log.error("生成清单表，有列需要翻译时，未能正确获得维表信息，清单表名为：" + tableName + ",列名为：" + orderBySqlStr.getColumnName() + ",维表ID：" + var78);
                                continue;
                            }

                            String var80 = var79.getDimTablename();
                            String dimTableAsName = var80.toLowerCase() + "_" + i;
                            String dimValueCol = dimTableAsName + "." + var79.getDimValueCol();
                            selectSqlSB.append("," + dimValueCol + " " + orderBySqlStr.getId().getAttrCol());
                            String dimCodeCol = dimTableAsName + "." + var79.getDimCodeCol();
                            if ((5 == createTranslatedTableSqlTemp.getLabelTypeId().intValue() && 1 == e.getColumnDataTypeId().intValue() || createTranslatedTableSql) && "DB2".equalsIgnoreCase(duplicateNum)) {
                                dimCodeCol = "char(" + dimCodeCol + ")";
                            }

                            leftJoinSqlSB.append(" left join " + var80 + " " + dimTableAsName + " on ").append(tableAsName + "." + orderBySqlStr.getId().getAttrCol()).append(" = " + dimCodeCol);
                        } else {
                            selectSqlSB.append("," + tableAsName + "." + orderBySqlStr.getId().getAttrCol());
                        }
                    } else if (13 == this.ciCustomGroupInfo.getCreateTypeId().intValue()) {
                        _index = zqListTableNameInfos[0];
                        String var70 = zqScheme + "." + _index;
                        var73 = _index + "." + zqColumn;
                        var75 = _index + "." + zqListTableNameInfos[i];
                        selectSqlSB.append("," + var75 + " " + orderBySqlStr.getId().getAttrCol());
                        if (leftJoinSqlSB.indexOf(var70) < 0) {
                            leftJoinSqlSB.append(" left join " + var70 + " " + _index + " on ").append(tableAsName + "." + ((CiGroupAttrRel) this.ciGroupAttrRelList.get(0)).getId().getAttrCol()).append(" = " + var73);
                        }
                    } else {
                        selectSqlSB.append("," + tableAsName + "." + orderBySqlStr.getId().getAttrCol());
                    }

                    attrSqlStr.append("," + orderBySqlStr.getId().getAttrCol());
                    ++i;
                }

                StringBuffer var67 = new StringBuffer();
                String var68 = "";
                int var69 = 0;

                for (Iterator var71 = orderByColumnMap.keySet().iterator(); var71.hasNext(); ++var69) {
                    var73 = (String) orderByColumnMap.get(var71.next());
                    if (var69 == 0) {
                        var67.append(" ").append(var73);
                    } else {
                        var67.append(" ,").append(var73);
                    }
                }

                if (StringUtil.isNotEmpty(var67.toString())) {
                    var68 = " ORDER BY " + var67.toString();
                }

                if (leftJoinSqlSB.length() > 0 || null != listMaxNum && listMaxNum.intValue() > 0) {
                    StringBuffer var72 = new StringBuffer();
                    StringBuffer var76;
                    if ("DB2".equalsIgnoreCase(duplicateNum)) {
                        progressSql = this.ciCustomFileRelService.createTable(tempTableName, tableName, column, this.ciGroupAttrRelList);
                        this.logNormal(progressSql, startDate);
                        StringBuffer var74 = new StringBuffer();
                        var74.append(" SELECT ").append(tableAsName + "." + productNo).append(selectSqlSB).append(" from " + tableName + " " + tableAsName);
                        if (leftJoinSqlSB.length() > 0) {
                            var74.append(leftJoinSqlSB);
                        }

                        if (StringUtil.isNotEmpty(var68)) {
                            var74.append(var68);
                        }

                        var75 = "";
                        if (null != listMaxNum && listMaxNum.intValue() > 0) {
                            var75 = this.customersManagerService.queryListMaxNumSql(var74.toString(), listMaxNum.intValue(), attrSqlStr.toString());
                        } else {
                            var75 = var74.toString();
                        }

                        var76 = new StringBuffer();
                        var76.append("insert into ").append(tempTableName).append(" ").append(var75);
                        String var77 = var76.toString();
                        this.log.info("insertSelectSql:" + var77);
                        var64 = this.customersManagerService.addInBackDataBase(var77, this.ciCustomGroupInfo.getCreateCityId());
                        this.logNormal(var77.toString(), startDate);
                    } else {
                        var72.append(" SELECT ").append(tableAsName + "." + productNo).append(selectSqlSB).append(" from " + tableName + " " + tableAsName);
                        if (leftJoinSqlSB.length() > 0) {
                            var72.append(leftJoinSqlSB);
                        }

                        if (StringUtil.isNotEmpty(var68)) {
                            var72.append(var68);
                        }

                        var73 = "";
                        if (null != listMaxNum && listMaxNum.intValue() > 0) {
                            var73 = this.customersManagerService.queryListMaxNumSql(var72.toString(), listMaxNum.intValue(), attrSqlStr.toString());
                        } else {
                            var73 = var72.toString();
                        }

                        var75 = Configure.getInstance().getProperty("CI_TABLESPACE");
                        var76 = new StringBuffer("create table " + tempTableName);
                        if (StringUtil.isNotEmpty(var75)) {
                            var76.append(" tablespace ").append(var75);
                        }

                        var76.append(" AS ").append(var73);
                        var64 = this.customersManagerService.addInBackDataBase(var76.toString(), this.ciCustomGroupInfo.getCreateCityId());
                        this.log.info("createTranslatedTableSql:" + var76.toString());
                        this.logNormal(var76.toString(), startDate);
                    }

                    try {
                        startDate = new Date();
                        var73 = "drop table " + tableName;
                        this.customersManagerService.dropTable(tableName);
                        this.logNormal(var73, startDate);
                    } catch (Exception var56) {
                        this.log.error("drop清单表出错", var56);
                    }

                    if ("DB2".equalsIgnoreCase(duplicateNum)) {
                        var73 = "RENAME TABLE " + tempTableName + " TO " + tableName;
                    } else {
                        var73 = "ALTER TABLE " + tempTableName + " RENAME TO " + tableName;
                    }

                    this.log.debug("renameSql:============" + var73);

                    try {
                        startDate = new Date();
                        this.customersManagerService.addInBackDataBase(var73, this.ciCustomGroupInfo.getCreateCityId());
                        this.logNormal(var73, startDate);
                    } catch (Exception var55) {
                        this.log.error("rename表名出错", var55);
                    }
                }

                distinctTotal = this.customersManagerService.queryDistinctListNumByTableName(tableName);
                break;
            }
        } catch (Exception var60) {
            this.delException(var60, "生成清单表时，翻译表列数据出错", startDate);
            var65 = false;
        } finally {
            try {
                this.customersManagerService.dropTable(tempTableName);
            } catch (Exception var54) {
                ;
            }

        }

        int var66 = 0;
        if (var64 >= distinctTotal) {
            var66 = var64 - distinctTotal;
        }

        this.ciCustomListInfo.setDuplicateNum(Long.valueOf((long) var66));
        this.ciCustomListInfo.setListTableName(tableName);
        this.ciCustomListInfo.setCustomNum(Long.valueOf((long) var64));
        this.ciCustomGroupInfo.setCustomNum(this.ciCustomListInfo.getCustomNum());
        this.ciCustomGroupInfo.setDuplicateNum(Long.valueOf((long) var66));
        if (var65) {
            this.changeListStatus(3);
        }

    }

    private void logNormal(String sql, Date startDate) {
        this.log.debug("exe sql:" + sql);
        CiCustomListExeInfo exeInfo = new CiCustomListExeInfo();
        exeInfo.setStartTime(startDate);
        exeInfo.setEndTime(new Date());
        exeInfo.setListTableName(this.ciCustomListInfo.getListTableName());
        this.customersManagerService.saveCiCustomListExeInfo(exeInfo);
        List ciListExeSqlAllList = this.generateCiListExeSqlAllList(exeInfo, sql);
        this.customersManagerService.saveCiListExeSqlAllList(ciListExeSqlAllList);
    }

    private String getInsertAsSelectSql(String sql, String intoTableName, String column) {
        StringBuffer columns = new StringBuffer(column);
        if (this.ciGroupAttrRelList != null && this.ciGroupAttrRelList.size() > 0) {
            columns.append(",");
            if (13 == this.ciCustomGroupInfo.getCreateTypeId().intValue()) {
                columns.append(((CiGroupAttrRel) this.ciGroupAttrRelList.get(0)).getId().getAttrCol());
            } else {
                for (int i = 0; i < this.ciGroupAttrRelList.size(); ++i) {
                    columns.append(((CiGroupAttrRel) this.ciGroupAttrRelList.get(i)).getId().getAttrCol());
                    if (i < this.ciGroupAttrRelList.size() - 1) {
                        columns.append(",");
                    }
                }
            }
        }

        return (new StringBuffer("insert ")).append(CommonConstants.sqlAppend).append(" into ").append(intoTableName).append(" ( ").append(columns).append(") ").append(sql).toString();
    }

    private void delException(Exception e, String msg, Date startDate) {
        this.log.info("delException:" + msg);
        this.log.error("delException", e);
        CiCustomListExeInfo exeInfo = new CiCustomListExeInfo();
        exeInfo.setStartTime(startDate);
        exeInfo.setEndTime(new Date());
        String expctionInfo = "";

        try {
            StringWriter expctionInfoNum = new StringWriter();
            PrintWriter expr = new PrintWriter(expctionInfoNum);
            e.printStackTrace(expr);
            expctionInfo = expctionInfoNum.toString();
        } catch (Exception var9) {
            this.log.error("sw.toString() error", var9);
        }

        int expctionInfoNum1 = CiUtil.getChineseCharactersNum(expctionInfo);
        if (expctionInfoNum1 * 3 + (expctionInfo.length() - expctionInfoNum1) > 2000) {
            if (3 * expctionInfoNum1 >= 2000) {
                expctionInfo = expctionInfo.substring(0, expctionInfo.length() / 3);
            } else {
                expctionInfo = expctionInfo.substring(0, 2000 - 2 * expctionInfoNum1);
            }
        }

        exeInfo.setExcpInfo(expctionInfo);
        exeInfo.setListTableName(this.ciCustomListInfo.getListTableName());
        this.customersManagerService.saveCiCustomListExeInfo(exeInfo);
        List ciListExeSqlAllList = this.generateCiListExeSqlAllList(exeInfo, msg);
        this.customersManagerService.saveCiListExeSqlAllList(ciListExeSqlAllList);
        this.ciCustomListInfo.setExcpInfo(expctionInfo);
        this.changeListStatus(0);
    }

    private List<CiListExeSqlAll> generateCiListExeSqlAllList(CiCustomListExeInfo exeInfo, String expr) {
        if (StringUtil.isEmpty(expr)) {
            return null;
        } else {
            ArrayList result = new ArrayList();
            String temp = expr;

            String s;
            for (int row = 0; temp.length() > 0; temp = temp.substring(s.length(), temp.length())) {
                s = CiUtil.subStringByByte(temp, 4000, "UTF-8");
                ++row;
                CiListExeSqlAll exeSqlAll = new CiListExeSqlAll((Integer) null, exeInfo.getExeInfoId(), s, Integer.valueOf(row));
                result.add(exeSqlAll);
            }

            return result;
        }
    }

    private String getSelectSqlByMixedRules() {
        List ciCustomSourceRelList = this.customersManagerService.queryCiCustomSourceRelByCustomGroupId(this.customGroupId);
        List ciLabelRuleList = this.customersManagerService.queryCiLabelRuleList(this.customGroupId, Integer.valueOf(1));
        String dayDate = CacheBase.getInstance().getNewLabelDay();
        int interval = 0;
        String format = "";
        if (3 == this.ciCustomGroupInfo.getUpdateCycle().intValue()) {
            format = "yyyyMMdd";
        }

        if (2 == this.ciCustomGroupInfo.getUpdateCycle().intValue()) {
            format = "yyyyMM";
        }

        if (StringUtil.isNotEmpty(format)) {
            interval = DateUtil.dateInterval(this.ciCustomGroupInfo.getOffsetDate(), this.ciCustomGroupInfo.getDataDate(), format, this.ciCustomGroupInfo.getUpdateCycle().intValue());
        }

        String fromSql = this.customersManagerService.getWithColumnSqlStr(ciLabelRuleList, this.ciCustomListInfo.getDataDate(), dayDate, this.ciCustomGroupInfo.getCreateUserId(), (List) null, Integer.valueOf(interval), this.ciCustomGroupInfo.getUpdateCycle());
        String calcExprStr = this.getCalcExpr(ciCustomSourceRelList);
        char opt = 32;
        int optIndex = 0;

        for (int table1 = 0; table1 < ciCustomSourceRelList.size(); ++table1) {
            CiCustomSourceRel table2 = (CiCustomSourceRel) ciCustomSourceRelList.get(table1);
            if (table2.getElementType().intValue() == 0) {
                if ("OR".equalsIgnoreCase(table2.getCalcuElement())) {
                    opt = 8746;
                } else if ("AND".equalsIgnoreCase(table2.getCalcuElement())) {
                    opt = 8745;
                } else if ("NOT".equalsIgnoreCase(table2.getCalcuElement())) {
                    opt = 45;
                }

                optIndex = table1;
            }
        }

        calcExprStr = calcExprStr.replace(opt, ' ');
        calcExprStr = calcExprStr.trim();
        String var13 = "";
        String var14 = "";
        if (optIndex == 0) {
            var13 = fromSql;
            var14 = calcExprStr;
        } else {
            var14 = fromSql;
            var13 = calcExprStr;
        }

        String sql = (new GroupCalcSqlPaser(Configure.getInstance().getProperty("CI_BACK_DBTYPE"))).toSql(var13, String.valueOf(opt), var14);
        return sql;
    }

    private String getSelectSqlByCustomersRels(List<CiLabelRule> ciLabelRuleList, String monthLabelDate, String dayLabelDate, String dataDate) {
        int interval = 0;
        String format = "";
        if (3 == this.ciCustomGroupInfo.getUpdateCycle().intValue()) {
            format = "yyyyMMdd";
        }

        if (2 == this.ciCustomGroupInfo.getUpdateCycle().intValue()) {
            format = "yyyyMM";
        }

        if (StringUtil.isNotEmpty(format)) {
            interval = DateUtil.dateInterval(this.ciCustomGroupInfo.getOffsetDate(), dataDate, format, this.ciCustomGroupInfo.getUpdateCycle().intValue());
        }

        return this.customersManagerService.getSelectSqlByCustomersRels(this.ciGroupAttrRelList, ciLabelRuleList, monthLabelDate, dayLabelDate, dataDate, this.ciCustomGroupInfo.getCreateUserId(), Integer.valueOf(interval), this.ciCustomGroupInfo.getUpdateCycle(), false);
    }

    private String getCalcExpr(List<CiCustomSourceRel> ciCustomSourceRelList) {
        StringBuffer calcExpr = new StringBuffer();
        Iterator i$ = ciCustomSourceRelList.iterator();

        while (i$.hasNext()) {
            CiCustomSourceRel rel = (CiCustomSourceRel) i$.next();
            if (rel.getElementType().intValue() == 0) {
                if ("OR".equalsIgnoreCase(rel.getCalcuElement())) {
                    calcExpr.append('∪');
                } else if ("AND".equalsIgnoreCase(rel.getCalcuElement())) {
                    calcExpr.append('∩');
                } else if ("NOT".equalsIgnoreCase(rel.getCalcuElement())) {
                    calcExpr.append('-');
                } else {
                    calcExpr.append(rel.getCalcuElement());
                }
            } else {
                calcExpr.append(rel.getCalcuElement());
            }
        }

        return calcExpr.toString();
    }

    private String getSelectSqlByLabelRules(List<CiLabelRule> ciLabelRuleList, String monthLabelDate, String dayLabelDate) {
        List newList = this.customersManagerService.getNewCiLabelRuleList(ciLabelRuleList);
        String sql = "";
        int interval = 0;
        String format = "";
        if (3 == this.ciCustomGroupInfo.getUpdateCycle().intValue()) {
            format = "yyyyMMdd";
        }

        if (2 == this.ciCustomGroupInfo.getUpdateCycle().intValue()) {
            format = "yyyyMM";
        }

        if (StringUtil.isNotEmpty(format)) {
            interval = DateUtil.dateInterval(this.ciCustomGroupInfo.getOffsetDate(), this.ciCustomListInfo.getDataDate(), format, this.ciCustomGroupInfo.getUpdateCycle().intValue());
        }

        sql = this.customersManagerService.getWithColumnSqlStr(newList, monthLabelDate, dayLabelDate, this.ciCustomGroupInfo.getCreateUserId(), this.ciGroupAttrRelList, Integer.valueOf(interval), this.ciCustomGroupInfo.getUpdateCycle());
        return sql;
    }

    public String toString() {
        return "CustomerListCreaterThread [customGroupId=" + this.customGroupId + "]";
    }

    public List<CiGroupAttrRel> getCiGroupAttrRelList() {
        return this.ciGroupAttrRelList;
    }

    public void setCiGroupAttrRelList(List<CiGroupAttrRel> ciGroupAttrRelList) {
        this.ciGroupAttrRelList = ciGroupAttrRelList;
    }

    public boolean getNeedRecreateListFile() {
        return this.needRecreateListFile;
    }

    public void setNeedRecreateListFile(boolean needRecreateListFile) {
        this.needRecreateListFile = needRecreateListFile;
    }
}
