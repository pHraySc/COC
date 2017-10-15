package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.constant.CommonConstants;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.ICiCustomCampsegRelHDao;
import com.ailk.biapp.ci.dao.ICiCustomGroupFormJDao;
import com.ailk.biapp.ci.dao.ICiCustomGroupInfoHDao;
import com.ailk.biapp.ci.dao.ICiCustomListExeInfoHDao;
import com.ailk.biapp.ci.dao.ICiCustomListExeInfoJDao;
import com.ailk.biapp.ci.dao.ICiCustomListInfoHDao;
import com.ailk.biapp.ci.dao.ICiCustomListInfoJDao;
import com.ailk.biapp.ci.dao.ICiCustomModifyHistoryHDao;
import com.ailk.biapp.ci.dao.ICiCustomPushReqHDao;
import com.ailk.biapp.ci.dao.ICiCustomSceneHDao;
import com.ailk.biapp.ci.dao.ICiCustomSourceRelHDao;
import com.ailk.biapp.ci.dao.ICiExploreLogRecordHDao;
import com.ailk.biapp.ci.dao.ICiExploreSqlAllHDao;
import com.ailk.biapp.ci.dao.ICiGroupAttrRelHDao;
import com.ailk.biapp.ci.dao.ICiLabelRuleHDao;
import com.ailk.biapp.ci.dao.ICiLabelRuleJDao;
import com.ailk.biapp.ci.dao.ICiListExeSqlAllHDao;
import com.ailk.biapp.ci.dao.ICiLoadCustomGroupFileJDao;
import com.ailk.biapp.ci.dao.ICiSysInfoHDao;
import com.ailk.biapp.ci.dao.ICiTaskServerCityRelHDao;
import com.ailk.biapp.ci.dao.ICiUserAttentionCustomHDao;
import com.ailk.biapp.ci.dao.ICiUserUseLabelHDao;
import com.ailk.biapp.ci.dao.ICustomGroupInfoJDao;
import com.ailk.biapp.ci.dao.ICustomProductMatchJDao;
import com.ailk.biapp.ci.dao.IVCiLabelCustomListJDao;
import com.ailk.biapp.ci.entity.CiCustomCampsegRel;
import com.ailk.biapp.ci.entity.CiCustomFileRel;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomGroupPushCycle;
import com.ailk.biapp.ci.entity.CiCustomGroupPushCycleId;
import com.ailk.biapp.ci.entity.CiCustomListExeInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiCustomModifyHistory;
import com.ailk.biapp.ci.entity.CiCustomPushReq;
import com.ailk.biapp.ci.entity.CiCustomSceneRel;
import com.ailk.biapp.ci.entity.CiCustomSourceRel;
import com.ailk.biapp.ci.entity.CiExploreLogRecord;
import com.ailk.biapp.ci.entity.CiExploreSqlAll;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.entity.CiGroupAttrRelId;
import com.ailk.biapp.ci.entity.CiLabelExtInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiLabelVerticalColumnRel;
import com.ailk.biapp.ci.entity.CiListExeSqlAll;
import com.ailk.biapp.ci.entity.CiMdaSysTable;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.entity.CiProductInfo;
import com.ailk.biapp.ci.entity.CiSysInfo;
import com.ailk.biapp.ci.entity.CiTaskServerCityRel;
import com.ailk.biapp.ci.entity.CiTemplateInfo;
import com.ailk.biapp.ci.entity.CiUserAttentionCustom;
import com.ailk.biapp.ci.entity.CiUserAttentionCustomId;
import com.ailk.biapp.ci.entity.CiUserUseLabel;
import com.ailk.biapp.ci.entity.CiUserUseLabelId;
import com.ailk.biapp.ci.entity.DimCustomCreateType;
import com.ailk.biapp.ci.entity.VCiLabelCustomList;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.localization.nonstandard.IImportTableService;
import com.ailk.biapp.ci.market.entity.CiCustomLabelSceneRel;
import com.ailk.biapp.ci.market.service.ICiMarketService;
import com.ailk.biapp.ci.model.CrmLabelOrIndexModel;
import com.ailk.biapp.ci.model.CrmTypeModel;
import com.ailk.biapp.ci.model.LabelElementFactory;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.model.PowerShowRequestXmlBean;
import com.ailk.biapp.ci.model.ReturnMsgModel;
import com.ailk.biapp.ci.model.TreeNode;
import com.ailk.biapp.ci.model.PowerShowRequestXmlBean.Column;
import com.ailk.biapp.ci.service.ICiGroupAttrRelService;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.service.ICiTemplateInfoService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.service.impl.ReviseDateModel;
import com.ailk.biapp.ci.task.CustomGroupUpLoadFileThread;
import com.ailk.biapp.ci.task.CustomerListCreaterThread;
import com.ailk.biapp.ci.task.CustomerPublishThread;
import com.ailk.biapp.ci.util.Bean2XMLUtils;
import com.ailk.biapp.ci.util.CIAlarmServiceUtil;
import com.ailk.biapp.ci.util.CiCustomGroupInfoIdGenUseSequence;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.DataBaseAdapter;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.ExpressionPaser;
import com.ailk.biapp.ci.util.FileUtil;
import com.ailk.biapp.ci.util.FtpUtil;
import com.ailk.biapp.ci.util.GroupCalcSqlPaser;
import com.ailk.biapp.ci.util.IdToName;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.ThreadPool;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.dimtable.service.impl.DimTableServiceImpl;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.DES;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

import java.beans.IntrospectionException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

@Service
@Transactional
public class CustomersManagerServiceImpl implements ICustomersManagerService {
    private Logger log = Logger.getLogger(CustomersManagerServiceImpl.class);
    @Autowired
    private ICiCustomGroupInfoHDao ciCustomGroupInfoHDao;
    @Autowired
    private ICustomGroupInfoJDao customersJdbcDao;
    @Autowired
    private ICiCustomListInfoHDao ciCustomListInfoHDao;
    @Autowired
    private ICiCustomListInfoJDao ciCustomListInfoJDao;
    @Autowired
    private ICiCustomModifyHistoryHDao ciCustomModifyHistoryHDao;
    @Autowired
    private ICiLabelRuleHDao ciLabelRuleHDao;
    @Autowired
    private ICiCustomSourceRelHDao ciCustomSourceRelHDao;
    @Autowired
    private ICiTemplateInfoService ciTemplateInfoService;
    @Autowired
    private ICiCustomPushReqHDao ciCustomPushReqHDao;
    @Autowired
    private ICiSysInfoHDao ciSysInfoHDao;
    @Autowired
    private ICiCustomCampsegRelHDao ciCustomCampsegRelHDao;
    @Autowired
    private ICiUserUseLabelHDao ciUserUseLabelHDao;
    @Autowired
    private ICiCustomListExeInfoHDao ciCustomListExeInfoHDao;
    @Autowired
    private ICustomProductMatchJDao customProductMatchDao;
    @Autowired
    private ICiCustomSceneHDao ciCustomSceneHDao;
    @Autowired
    private ICiGroupAttrRelService ciGroupAttrRelService;
    @Autowired
    private ICiCustomGroupFormJDao ciCustomGroupFormJDao;
    @Autowired
    private ICiGroupAttrRelHDao ciGroupAttrRelHDao;
    @Autowired
    private CiCustomGroupInfoIdGenUseSequence customGroupIdGen;
    @Autowired
    private ICiUserAttentionCustomHDao ciUserAttentionCustomHDao;
    @Autowired
    private ICiLabelRuleJDao ciLabelRuleJDao;
    @Autowired
    private ICiCustomListExeInfoJDao ciCustomListExeInfoJDao;
    @Autowired
    private IVCiLabelCustomListJDao ciLabelCustomListJDao;
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;
    @Autowired
    private ICiLoadCustomGroupFileJDao ciLoadCustomGroupFileJdao;
    @Autowired
    private ICiListExeSqlAllHDao ciListExeSqlAllHDao;
    @Autowired
    private ICiTaskServerCityRelHDao ciTaskServerCityRelHDao;
    @Autowired
    private ICiExploreLogRecordHDao ciExploreLogRecordHDao;
    @Autowired
    private ICiExploreSqlAllHDao ciExploreSqlAllHDao;
    @Autowired
    private ICiMarketService marketService;
    public static Lock customLock = new ReentrantLock();
    public static Lock listLock = new ReentrantLock();
    public static Lock labelRuleLock = new ReentrantLock();

    public CustomersManagerServiceImpl() {
    }

    public int queryCustomersAutoMacthFlag(String customGroupId) throws CIServiceException {
        boolean customersAutoMacthFlag = false;

        try {
            int customersAutoMacthFlag1 = this.customersJdbcDao.getCustomersAutoMacthFlag(customGroupId);
            return customersAutoMacthFlag1;
        } catch (Exception var4) {
            this.log.error(var4);
            throw new CIServiceException("查询客户群系统匹配标志失败");
        }
    }

    public CiCustomListInfo queryCustomListInfo(CiCustomGroupInfo ciCustomGroupInfo) throws CIServiceException {
        String customGroupId = ciCustomGroupInfo.getCustomGroupId();
        String dataDate = ciCustomGroupInfo.getDataDate();
        CiCustomListInfo customListInfo = null;

        try {
            customListInfo = this.ciCustomListInfoHDao.selectByCustomGroupIdAndDataDate(customGroupId, dataDate);
            return customListInfo;
        } catch (Exception var6) {
            this.log.error(var6);
            throw new CIServiceException("查询客户群清单详细信息失败");
        }
    }

    public void deleteCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo) throws CIServiceException {
        try {
            CiCustomGroupInfo e = this.ciCustomGroupInfoHDao.selectCustomGroupById(ciCustomGroupInfo.getCustomGroupId());
            e.setStatus(Integer.valueOf(0));
            this.ciCustomGroupInfoHDao.insertCustomGroup(e);
            if (12 == e.getCreateTypeId().intValue()) {
                String implClassName = Configure.getInstance().getProperty("IMPORT_BY_TABLE_CLASS_NAME");
                IImportTableService importService = (IImportTableService) SystemServiceLocator.getInstance().getService(implClassName);
                importService.deleteCustomGroup(e);
            }

        } catch (Exception var5) {
            this.log.error("deleteCiCustomGroupInfo " + ciCustomGroupInfo, var5);
            throw new CIServiceException("删除客户群失败", var5);
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public int queryCustomersCount(CiCustomGroupInfo bean) throws CIServiceException {
        if (bean.getSceneId() != null && bean.getSceneId().contains("all")) {
            bean.setSceneId((String) null);
        }

        boolean count = false;

        try {
            int count1 = this.customersJdbcDao.getCustomersTotolCount(bean);
            return count1;
        } catch (Exception var4) {
            this.log.error("查询客户群总数失败", var4);
            throw new CIServiceException("查询客户群总数失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiCustomGroupInfo> queryCustomersList(Pager pager, String dataDate, CiCustomGroupInfo bean) {
        List customsersList = null;

        try {
            if (bean.getSceneId() != null && bean.getSceneId().contains("all")) {
                bean.setSceneId((String) null);
            }

            if (pager.getOrderBy() != null && pager.getOrderBy().indexOf("USECOUNT_HOT") != -1) {
                customsersList = this.customersJdbcDao.getUserUseCustomsersList(pager, bean, (String) null);
            } else {
                customsersList = this.customersJdbcDao.getCustomsersList(pager, bean);
            }

            CacheBase e = CacheBase.getInstance();
            Iterator var7 = customsersList.iterator();

            while (var7.hasNext()) {
                CiCustomGroupInfo ciCustomGroupInfo = (CiCustomGroupInfo) var7.next();
                ciCustomGroupInfo.setAlarm(false);
                if (ciCustomGroupInfo.getUpdateCycle() != null && ciCustomGroupInfo.getUpdateCycle().intValue() == 2 && ciCustomGroupInfo.getDataStatus().intValue() == 3) {
                    try {
                        ciCustomGroupInfo.setAlarm(CIAlarmServiceUtil.haveAlarmRecords(ciCustomGroupInfo.getDataDate(), ciCustomGroupInfo.getCreateUserId(), "CustomersAlarm", ciCustomGroupInfo.getCustomGroupId()));
                    } catch (Exception var15) {
                        this.log.error(var15.getMessage(), var15);
                    }
                }

                if (ciCustomGroupInfo.getDataStatus() != null) {
                    ciCustomGroupInfo.setDataStatusStr(IdToName.getName("DIM_CUSTOM_DATA_STATUS", ciCustomGroupInfo.getDataStatus()));
                }

                if (ciCustomGroupInfo.getCreateCityId() != null) {
                    ciCustomGroupInfo.setCreateCityName(IdToName.getName("DIM_CITY", Integer.valueOf(ciCustomGroupInfo.getCreateCityId())));
                }

                if (ciCustomGroupInfo.getCreateTypeId() != null) {
                    ciCustomGroupInfo.setCreateType(IdToName.getName("DIM_CUSTOM_CREATE_TYPE", ciCustomGroupInfo.getCreateTypeId()));
                }

                ciCustomGroupInfo.setLabelName(this.customersJdbcDao.getLableNameById(ciCustomGroupInfo.getCustomGroupId()));
                List infoList = (List) e.get("CI_CUSTOM_LIST_INFO_MAP", ciCustomGroupInfo.getCustomGroupId(), List.class);
                if (infoList == null) {
                    infoList = this.ciCustomListInfoHDao.selectByCustomGroupId(ciCustomGroupInfo.getCustomGroupId());
                    e.put("CI_CUSTOM_LIST_INFO_MAP", ciCustomGroupInfo.getCustomGroupId(), infoList);
                }

                if (infoList != null && infoList.size() > 0 && StringUtils.isNotBlank(dataDate)) {
                    CiCustomListInfo iuser;
                    if (1 == ciCustomGroupInfo.getUpdateCycle().intValue()) {
                        iuser = (CiCustomListInfo) infoList.get(0);
                        ciCustomGroupInfo.setListTableName(iuser.getListTableName());
                    } else {
                        Iterator isPush = infoList.iterator();

                        while (isPush.hasNext()) {
                            iuser = (CiCustomListInfo) isPush.next();
                            if (ciCustomGroupInfo.getDataDate() != null && ciCustomGroupInfo.getDataDate().equals(iuser.getDataDate())) {
                                ciCustomGroupInfo.setListTableName(iuser.getListTableName());
                            }
                        }
                    }
                }

                if (ciCustomGroupInfo.getDataStatus().intValue() == 3 && StringUtils.isNotBlank(ciCustomGroupInfo.getListTableName())) {
                    CiCustomGroupInfo var17 = new CiCustomGroupInfo();
                    String var19 = CacheBase.getInstance().getNewLabelMonth();
                    var17.setDataDate(var19);
                    var17.setListTableName(ciCustomGroupInfo.getListTableName());
                    if (this.customProductMatchDao.isCustomProductMatch(var17)) {
                        ciCustomGroupInfo.setSysMatchStatus(Integer.valueOf(3));
                    } else if (ciCustomGroupInfo.getProductAutoMacthFlag() != null && ciCustomGroupInfo.getProductAutoMacthFlag().intValue() > 1) {
                        ciCustomGroupInfo.setSysMatchStatus(Integer.valueOf(2));
                    } else {
                        ciCustomGroupInfo.setSysMatchStatus(Integer.valueOf(1));
                    }
                } else {
                    ciCustomGroupInfo.setSysMatchStatus(Integer.valueOf(1));
                }

                IUser var18 = PrivilegeServiceUtil.getUserById(ciCustomGroupInfo.getCreateUserId());
                if (var18 != null) {
                    ciCustomGroupInfo.setCreateUserName(var18.getUsername());
                }

                int var20 = this.customersJdbcDao.selectCustomerGroupIsPush(ciCustomGroupInfo.getCustomGroupId());
                if (var20 > 0) {
                    ciCustomGroupInfo.setIsPush(1);
                } else {
                    ciCustomGroupInfo.setIsPush(0);
                }

                StringBuilder customSceneNames = new StringBuilder();
                List ciCustomSceneRels = this.ciCustomSceneHDao.getCustomScenesByCustomId(ciCustomGroupInfo.getCustomGroupId());
                if (ciCustomSceneRels != null && ciCustomSceneRels.size() != 0) {
                    for (int info = ciCustomSceneRels.size() - 1; info >= 0; --info) {
                        if (((CiCustomSceneRel) ciCustomSceneRels.get(info)).getId() != null) {
                            String dimSceneName = e.getDimScene(((CiCustomSceneRel) ciCustomSceneRels.get(info)).getId().getSceneId());
                            customSceneNames.append(dimSceneName).append(info > 0 ? "&nbsp;&nbsp;" : "");
                        }
                    }

                    ciCustomGroupInfo.setCustomSceneNames(customSceneNames.toString());
                }

                CiCustomGroupInfo var21 = e.getHotCustomByKey("HOT_CUSTOMS", ciCustomGroupInfo.getCustomGroupId());
                if (var21 != null) {
                    ciCustomGroupInfo.setIsHotCustom("true");
                }
            }

            return customsersList;
        } catch (Exception var16) {
            this.log.error("查询客户群分页失败," + pager.getPageNum() + "," + pager.getPageSize() + "," + bean, var16);
            throw new CIServiceException("查询客户群分页失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiCustomGroupInfo> queryCustomersListTask(Pager pager, String dataDate, CiCustomGroupInfo bean) {
        List customsersList = null;

        try {
            customsersList = this.customersJdbcDao.getUserUseCustomsersList(pager, bean, "callByBackStage");
            return customsersList;
        } catch (Exception var6) {
            this.log.error("后台查询客户群分页失败," + pager.getPageNum() + "," + pager.getPageSize() + "," + bean, var6);
            throw new CIServiceException("后台查询客户群分页失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiCustomGroupInfo> queryUserAttentionCustomersList(Pager pager, String dataDate, CiCustomGroupInfo bean) {
        List customsersList = null;

        try {
            pager.setOrderBy("T4.ATTENTION_TIME");
            pager.setOrder("DESC");
            customsersList = this.customersJdbcDao.getCustomsersList(pager, bean, "userAttention");
            CacheBase e = CacheBase.getInstance();
            Iterator var7 = customsersList.iterator();

            while (true) {
                CiCustomGroupInfo ciCustomGroupInfo;
                StringBuilder customSceneNames;
                List ciCustomSceneRels;
                do {
                    do {
                        if (!var7.hasNext()) {
                            return customsersList;
                        }

                        ciCustomGroupInfo = (CiCustomGroupInfo) var7.next();
                        ciCustomGroupInfo.setAlarm(false);
                        if (ciCustomGroupInfo.getUpdateCycle() != null && ciCustomGroupInfo.getUpdateCycle().intValue() == 2 && ciCustomGroupInfo.getDataStatus().intValue() == 3) {
                            try {
                                ciCustomGroupInfo.setAlarm(CIAlarmServiceUtil.haveAlarmRecords(ciCustomGroupInfo.getDataDate(), ciCustomGroupInfo.getCreateUserId(), "CustomersAlarm", ciCustomGroupInfo.getCustomGroupId()));
                            } catch (Exception var16) {
                                this.log.error(var16.getMessage(), var16);
                            }
                        }

                        if (ciCustomGroupInfo.getCreateCityId() != null) {
                            ciCustomGroupInfo.setCreateCityName(IdToName.getName("DIM_CITY", Integer.valueOf(ciCustomGroupInfo.getCreateCityId())));
                        }

                        if (ciCustomGroupInfo.getDataStatus() != null) {
                            ciCustomGroupInfo.setDataStatusStr(IdToName.getName("DIM_CUSTOM_DATA_STATUS", ciCustomGroupInfo.getDataStatus()));
                        }

                        if (ciCustomGroupInfo.getCreateTypeId() != null) {
                            ciCustomGroupInfo.setCreateType(IdToName.getName("DIM_CUSTOM_CREATE_TYPE", ciCustomGroupInfo.getCreateTypeId()));
                        }

                        ciCustomGroupInfo.setLabelName(this.customersJdbcDao.getLableNameById(ciCustomGroupInfo.getCustomGroupId()));
                        List infoList = (List) e.get("CI_CUSTOM_LIST_INFO_MAP", ciCustomGroupInfo.getCustomGroupId(), List.class);
                        if (infoList == null) {
                            infoList = this.ciCustomListInfoHDao.selectByCustomGroupId(ciCustomGroupInfo.getCustomGroupId());
                            e.put("CI_CUSTOM_LIST_INFO_MAP", ciCustomGroupInfo.getCustomGroupId(), infoList);
                        }

                        if (infoList != null && infoList.size() > 0 && StringUtils.isNotBlank(dataDate)) {
                            CiCustomListInfo iuser;
                            if (1 == ciCustomGroupInfo.getUpdateCycle().intValue()) {
                                iuser = (CiCustomListInfo) infoList.get(0);
                                ciCustomGroupInfo.setListTableName(iuser.getListTableName());
                            } else {
                                Iterator isPush = infoList.iterator();

                                while (isPush.hasNext()) {
                                    iuser = (CiCustomListInfo) isPush.next();
                                    if (ciCustomGroupInfo.getDataDate().equals(iuser.getDataDate())) {
                                        ciCustomGroupInfo.setListTableName(iuser.getListTableName());
                                    }
                                }
                            }
                        }

                        if (ciCustomGroupInfo.getDataStatus().intValue() == 3 && StringUtils.isNotBlank(ciCustomGroupInfo.getListTableName())) {
                            CiCustomGroupInfo var18 = new CiCustomGroupInfo();
                            String var20 = CacheBase.getInstance().getNewLabelMonth();
                            var18.setDataDate(var20);
                            var18.setListTableName(ciCustomGroupInfo.getListTableName());
                            if (this.customProductMatchDao.isCustomProductMatch(var18)) {
                                ciCustomGroupInfo.setSysMatchStatus(Integer.valueOf(3));
                            } else if (ciCustomGroupInfo.getProductAutoMacthFlag() != null && ciCustomGroupInfo.getProductAutoMacthFlag().intValue() > 1) {
                                ciCustomGroupInfo.setSysMatchStatus(Integer.valueOf(2));
                            } else {
                                ciCustomGroupInfo.setSysMatchStatus(Integer.valueOf(1));
                            }
                        } else {
                            ciCustomGroupInfo.setSysMatchStatus(Integer.valueOf(1));
                        }

                        IUser var19 = PrivilegeServiceUtil.getUserById(ciCustomGroupInfo.getCreateUserId());
                        if (var19 != null) {
                            ciCustomGroupInfo.setCreateUserName(var19.getUsername());
                        }

                        int var21 = this.customersJdbcDao.selectCustomerGroupIsPush(ciCustomGroupInfo.getCustomGroupId());
                        if (var21 > 0) {
                            ciCustomGroupInfo.setIsPush(1);
                        } else {
                            ciCustomGroupInfo.setIsPush(0);
                        }

                        CiCustomGroupInfo info = e.getHotCustomByKey("HOT_CUSTOMS", ciCustomGroupInfo.getCustomGroupId());
                        if (info != null) {
                            ciCustomGroupInfo.setIsHotCustom("true");
                        }

                        customSceneNames = new StringBuilder();
                        ciCustomSceneRels = this.ciCustomSceneHDao.getCustomScenesByCustomId(ciCustomGroupInfo.getCustomGroupId());
                    } while (ciCustomSceneRels == null);
                } while (ciCustomSceneRels.size() == 0);

                for (int i = ciCustomSceneRels.size() - 1; i >= 0; --i) {
                    if (((CiCustomSceneRel) ciCustomSceneRels.get(i)).getId() != null) {
                        String dimSceneName = e.getDimScene(((CiCustomSceneRel) ciCustomSceneRels.get(i)).getId().getSceneId());
                        customSceneNames.append(dimSceneName).append(i > 0 ? "&nbsp;&nbsp;" : "");
                    }
                }

                ciCustomGroupInfo.setCustomSceneNames(customSceneNames.toString());
            }
        } catch (Exception var17) {
            this.log.error("查询客户群分页失败," + pager.getPageNum() + "," + pager.getPageSize() + "," + bean, var17);
            throw new CIServiceException("查询客户群分页失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiCustomGroupInfo> queryCustomersListByCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo, String orderByProperty, int orderByType) {
        return this.ciCustomGroupInfoHDao.selectCiCustomGroupInfoListByCustomGroupInfo(ciCustomGroupInfo, orderByProperty, orderByType);
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public CiCustomGroupInfo queryCiCustomGroupInfoForCache(String ciCustomGroupInfoId) {
        try {
            CiCustomGroupInfo e = this.ciCustomGroupInfoHDao.selectCustomGroupById(ciCustomGroupInfoId);
            return e;
        } catch (Exception var3) {
            this.log.error("查询客户群失败," + ciCustomGroupInfoId);
            throw new CIServiceException("查询客户群分页失败");
        }
    }

    public String queryCustomerOffsetStr(CiCustomGroupInfo ciCustomGroupInfo) {
        StringBuffer result = new StringBuffer("");
        String offsetDate = ciCustomGroupInfo.getOffsetDate();
        if (offsetDate != null && !"".equals(offsetDate)) {
            String fmt = "yyyyMMdd";
            String unit = "天]";
            if (ciCustomGroupInfo.getUpdateCycle().intValue() == 2) {
                fmt = "yyyyMM";
                unit = "个月]";
            }

            boolean offset = false;

            int offset1;
            try {
                SimpleDateFormat e = new SimpleDateFormat(fmt);
                e.parse(ciCustomGroupInfo.getDataDate());
                offset1 = DateUtil.dateInterval(offsetDate, ciCustomGroupInfo.getDataDate(), fmt, ciCustomGroupInfo.getUpdateCycle().intValue());
            } catch (Exception var8) {
                offset1 = 0;
            }

            if (offset1 < 0) {
                offset1 = 0;
            }

            result.append("[已动态偏移更新").append(offset1).append(unit);
        }

        return result.toString();
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public CiCustomGroupInfo queryCiCustomGroupInfo(String ciCustomGroupInfoId) {
        try {
            CiCustomGroupInfo e = this.ciCustomGroupInfoHDao.selectCustomGroupById(ciCustomGroupInfoId);
            if (e.getCreateCityId() != null) {
                e.setCreateCityName(IdToName.getName("DIM_CITY", Integer.valueOf(e.getCreateCityId())));
            }

            CacheBase cache = CacheBase.getInstance();
            StringBuilder customSceneNames = new StringBuilder();
            List ciCustomSceneRels = this.ciCustomSceneHDao.getCustomScenesByCustomId(e.getCustomGroupId());
            if (ciCustomSceneRels != null && ciCustomSceneRels.size() != 0) {
                for (int info = ciCustomSceneRels.size() - 1; info >= 0; --info) {
                    if (((CiCustomSceneRel) ciCustomSceneRels.get(info)).getId() != null) {
                        String iuser = cache.getDimScene(((CiCustomSceneRel) ciCustomSceneRels.get(info)).getId().getSceneId());
                        customSceneNames.append(iuser).append(info > 0 ? "&nbsp;&nbsp;" : "");
                    }
                }

                e.setCustomSceneNames(customSceneNames.toString());
            }

            CiCustomGroupInfo var9 = cache.getHotCustomByKey("HOT_CUSTOMS", e.getCustomGroupId());
            if (var9 != null) {
                e.setIsHotCustom("true");
            }

            IUser var10 = PrivilegeServiceUtil.getUserById(e.getCreateUserId());
            if (var10 != null) {
                e.setCreateUserName(var10.getUsername());
            }

            if (e.getCreateTypeId() != null) {
                e.setCreateType(IdToName.getName("DIM_CUSTOM_CREATE_TYPE", e.getCreateTypeId()));
            }

            e.setAlarm(CIAlarmServiceUtil.haveAlarmRecords(e.getDataDate(), e.getCreateUserId(), "CustomersAlarm", e.getCustomGroupId()));
            return e;
        } catch (Exception var8) {
            this.log.error("查询客户群失败," + ciCustomGroupInfoId);
            var8.printStackTrace();
            throw new CIServiceException("查询客户群分页失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public int queryCustomerListCount(CiCustomListInfo ciCustomListInfo) throws CIServiceException {
        boolean count = false;

        try {
            CiCustomListInfo e = this.ciCustomListInfoHDao.selectById(ciCustomListInfo.getListTableName());
            int count1 = e.getCustomNum().intValue();
            return count1;
        } catch (Exception var4) {
            this.log.error("查询客户群清单总数失败," + ciCustomListInfo, var4);
            throw new CIServiceException("查询客户群清单总数失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiCustomListInfo> queryCiCustomListInfoByCGroupId(String customGroupId) throws CIServiceException {
        return this.ciCustomListInfoHDao.selectByCustomGroupId(customGroupId);
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiCustomListInfo> querySuccessCiCustomListInfoByCGroupId(String customGroupId) throws CIServiceException {
        Object ciCustomListInfos = new ArrayList();
        if (this.ciCustomListInfoHDao.selectSuccessByCustomGroupId(customGroupId) != null) {
            ciCustomListInfos = this.ciCustomListInfoHDao.selectSuccessByCustomGroupId(customGroupId);
        }

        return (List) ciCustomListInfos;
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiCustomListInfo> queryCiCustomListInfoByCustomListInfo(CiCustomListInfo customListInfo) throws CIServiceException {
        return this.ciCustomListInfoHDao.selectByCustomListInfo(customListInfo);
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public CiCustomListInfo queryCiCustomListInfoByCGroupIdAndDataDate(String customGroupId, String dataDate) throws CIServiceException {
        return this.ciCustomListInfoHDao.selectByCustomGroupIdAndDataDate(customGroupId, dataDate);
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public String getTabelExistSqlStr(String tableName) throws CIServiceException {
        String sql = "select 1 from" + tableName + " where 1=2";
        return sql;
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public String getValidateSqlStr(List<CiLabelRule> ciLabelRuleList, String monthDate, String dayDate, String userId) throws CIServiceException {
        String fromSqlStr = null;

        try {
            fromSqlStr = this.getFromSqlStr(ciLabelRuleList, monthDate, dayDate, (List) null, false, userId, Integer.valueOf(0), (Integer) null, true);
            fromSqlStr = fromSqlStr.replace("where 1=1", "where 1=2 ");
        } catch (ParseException var7) {
            this.log.error("日期转换异常", var7);
            throw new CIServiceException("日期转换异常", var7);
        } catch (Exception var8) {
            this.log.error("SQL拼接数据异常", var8);
            throw new CIServiceException("SQL拼接数据异常", var8);
        }

        if (StringUtil.isEmpty(fromSqlStr)) {
            this.log.error("没有获得查询sql");
            throw new CIServiceException("没有获得查询sql");
        } else {
            String sql = "select " + CommonConstants.sqlParallel + " 1" + fromSqlStr;
            this.log.info("COC_SQL >>>>>>> " + sql);
            return sql;
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public String getWithColumnSqlStr(List<CiLabelRule> ciLabelRuleList, String monthDate, String dayDate, String userId, List<CiGroupAttrRel> ciGroupAttrRelList, Integer interval, Integer updateCycle) throws CIServiceException {
        String fromSqlStr = null;

        try {
            fromSqlStr = this.getFromSqlStr(ciLabelRuleList, monthDate, dayDate, ciGroupAttrRelList, true, userId, interval, updateCycle, false);
        } catch (ParseException var10) {
            this.log.error("日期转换异常", var10);
            throw new CIServiceException("日期转换异常", var10);
        } catch (Exception var11) {
            this.log.error("SQL拼接数据异常", var11);
            throw new CIServiceException("SQL拼接数据异常", var11);
        }

        if (StringUtil.isEmpty(fromSqlStr)) {
            this.log.error("没有获得查询sql");
            throw new CIServiceException("没有获得查询sql");
        } else {
            String sql = "select " + CommonConstants.sqlParallel + " " + fromSqlStr;
            this.log.info("COC_SQL >>>>>>> " + sql);
            return sql;
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public String getCountSqlStr(List<CiLabelRule> ciLabelRuleList, String monthDate, String dayDate, String userId) throws CIServiceException {
        String fromSqlStr = null;

        try {
            fromSqlStr = this.getFromSqlStr(ciLabelRuleList, monthDate, dayDate, (List) null, false, userId, Integer.valueOf(0), (Integer) null, false);
        } catch (ParseException var7) {
            this.log.error("日期转换异常", var7);
            throw new CIServiceException("日期转换异常", var7);
        } catch (Exception var8) {
            this.log.error("SQL拼接数据异常", var8);
            throw new CIServiceException("SQL拼接数据异常", var8);
        }

        if (StringUtil.isEmpty(fromSqlStr)) {
            this.log.error("没有获得查询sql");
            throw new CIServiceException("没有获得查询sql");
        } else {
            String sql = "select " + CommonConstants.sqlParallel + " count(1)" + fromSqlStr;
            this.log.info("COC_SQL >>>>>>> " + sql);
            return sql;
        }
    }

    public String validateLabelDataDate(CiCustomGroupInfo ciCustomGroupInfo, List<CiLabelRule> ciLabelRuleList, List<CiGroupAttrRel> ciGroupAttrRelList, String month, String day) {
        if (ciLabelRuleList == null) {
            ciLabelRuleList = this.ciLabelRuleHDao.selectCiLabelRuleList(ciCustomGroupInfo.getCustomGroupId(), Integer.valueOf(1));
        }

        String tactics = ciCustomGroupInfo.getTacticsId();
        CacheBase cache = CacheBase.getInstance();
        String result = null;
        boolean satisfyMonth = true;
        boolean satisfyDay = true;
        int monthNum = 0;
        int dayNum = 0;
        if (StringUtil.isNotEmpty(month)) {
            monthNum = Integer.valueOf(month).intValue();
        }

        if (StringUtil.isNotEmpty(day)) {
            dayNum = Integer.valueOf(day).intValue();
        }

        Iterator var14;
        int attrSource;
        String labelIdStr;
        CiLabelInfo ciLabelInfo;
        String dataDate;
        int tempNum;
        if (ciLabelRuleList != null && ciLabelRuleList.size() > 0) {
            var14 = ciLabelRuleList.iterator();

            label135:
            do {
                CiLabelRule updateCycle;
                do {
                    do {
                        if (!var14.hasNext()) {
                            break label135;
                        }

                        updateCycle = (CiLabelRule) var14.next();
                    } while (StringUtil.isNotEmpty(updateCycle.getParentId()));

                    attrSource = updateCycle.getElementType().intValue();
                } while (attrSource != 2);

                labelIdStr = updateCycle.getCalcuElement();
                ciLabelInfo = cache.getEffectiveLabel(labelIdStr);
                dataDate = ciLabelInfo.getDataDate();
                if (1 == ciLabelInfo.getUpdateCycle().intValue() && StringUtil.isNotEmpty(dataDate) && dayNum != 0) {
                    tempNum = Integer.valueOf(dataDate).intValue();
                    if (tempNum < dayNum) {
                        satisfyDay = false;
                    }
                } else if (2 == ciLabelInfo.getUpdateCycle().intValue() && StringUtil.isNotEmpty(dataDate) && monthNum != 0) {
                    tempNum = Integer.valueOf(dataDate).intValue();
                    if (tempNum < monthNum) {
                        satisfyMonth = false;
                    }
                }
            } while (satisfyDay || satisfyMonth);
        }

        if (ciGroupAttrRelList != null && ciGroupAttrRelList.size() > 0) {
            var14 = ciGroupAttrRelList.iterator();

            label111:
            do {
                CiGroupAttrRel updateCycle1;
                do {
                    if (!var14.hasNext()) {
                        break label111;
                    }

                    updateCycle1 = (CiGroupAttrRel) var14.next();
                    attrSource = updateCycle1.getAttrSource();
                } while (attrSource != 2);

                labelIdStr = updateCycle1.getLabelOrCustomId();
                ciLabelInfo = cache.getEffectiveLabel(labelIdStr);
                dataDate = ciLabelInfo.getDataDate();
                if (1 == ciLabelInfo.getUpdateCycle().intValue() && StringUtil.isNotEmpty(dataDate) && dayNum != 0) {
                    tempNum = Integer.valueOf(dataDate).intValue();
                    if (tempNum < dayNum) {
                        satisfyDay = false;
                    }
                } else if (2 == ciLabelInfo.getUpdateCycle().intValue() && StringUtil.isNotEmpty(dataDate) && monthNum != 0) {
                    tempNum = Integer.valueOf(dataDate).intValue();
                    if (tempNum < monthNum) {
                        satisfyMonth = false;
                    }
                }
            } while (satisfyDay || satisfyMonth);
        }

        int updateCycle2 = ciCustomGroupInfo.getUpdateCycle().intValue();
        this.log.debug("周期：" + updateCycle2 + "  策略：" + tactics + "    ciCustomGroupInfo：" + ciCustomGroupInfo.getCustomGroupId() + "     satisfyDay:=============" + satisfyDay + "     satisfyMonth:=============" + satisfyMonth);
        if (1 == updateCycle2) {
            if ("1".equals(tactics)) {
                if (!satisfyDay) {
                    result = "3";
                } else {
                    result = "2";
                }
            } else if ("2".equals(tactics)) {
                result = "2";
            }
        } else if (2 == updateCycle2) {
            if (!satisfyMonth) {
                result = "1";
            } else if (satisfyMonth && !satisfyDay) {
                result = "3";
            } else if (satisfyMonth && satisfyDay) {
                result = "2";
            }
        } else if (3 == updateCycle2) {
            if (!satisfyDay) {
                result = "1";
            } else {
                result = "2";
            }
        }

        return result;
    }

    public String validateLabelDataDate(CiCustomGroupInfo ciCustomGroupInfo, List<CiGroupAttrRel> ciGroupAttrRelList, String month, String day) {
        return this.validateLabelDataDate(ciCustomGroupInfo, (List) null, ciGroupAttrRelList, month, day);
    }

    private ReviseDateModel queryReviseDate(CiCustomGroupInfo ciCustomGroupInfo, List<CiLabelRule> ciLabelRuleList, List<CiGroupAttrRel> ciGroupAttrRelList, String month) {
        ReviseDateModel reviseDateModel = new ReviseDateModel();
        String reviseType = null;
        CacheBase cache = CacheBase.getInstance();
        boolean satisfyMonth = true;
        int monthNum = 0;
        if (StringUtil.isNotEmpty(month)) {
            monthNum = Integer.valueOf(month).intValue();
        }

        int reviseMonth = 2147483647;
        this.log.debug("所选月分 monthNum:" + monthNum);
        Iterator var12;
        int attrSource;
        String labelIdStr;
        CiLabelInfo ciLabelInfo;
        int updateCycle1;
        String dataDate;
        int tempNum;
        if (ciLabelRuleList != null && ciLabelRuleList.size() > 0) {
            var12 = ciLabelRuleList.iterator();

            while (var12.hasNext()) {
                CiLabelRule updateCycle = (CiLabelRule) var12.next();
                if (!StringUtil.isNotEmpty(updateCycle.getParentId())) {
                    attrSource = updateCycle.getElementType().intValue();
                    if (attrSource == 2) {
                        labelIdStr = updateCycle.getCalcuElement();
                        ciLabelInfo = cache.getEffectiveLabel(labelIdStr);
                        updateCycle1 = ciLabelInfo.getUpdateCycle().intValue();
                        dataDate = ciLabelInfo.getDataDate();
                        if (2 == updateCycle1 && StringUtil.isNotEmpty(dataDate) && monthNum != 0) {
                            tempNum = Integer.valueOf(dataDate).intValue();
                            if (tempNum < monthNum) {
                                satisfyMonth = false;
                                if (reviseMonth > tempNum) {
                                    reviseMonth = tempNum;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (ciGroupAttrRelList != null && ciGroupAttrRelList.size() > 0) {
            var12 = ciGroupAttrRelList.iterator();

            while (var12.hasNext()) {
                CiGroupAttrRel updateCycle2 = (CiGroupAttrRel) var12.next();
                attrSource = updateCycle2.getAttrSource();
                if (attrSource == 2) {
                    labelIdStr = updateCycle2.getLabelOrCustomId();
                    ciLabelInfo = cache.getEffectiveLabel(labelIdStr);
                    updateCycle1 = ciLabelInfo.getUpdateCycle().intValue();
                    dataDate = ciLabelInfo.getDataDate();
                    if (2 == updateCycle1 && StringUtil.isNotEmpty(dataDate) && monthNum != 0) {
                        tempNum = Integer.valueOf(dataDate).intValue();
                        if (tempNum < monthNum) {
                            satisfyMonth = false;
                            if (reviseMonth > tempNum) {
                                reviseMonth = tempNum;
                            }
                        }
                    }
                }
            }
        }

        int updateCycle3 = ciCustomGroupInfo.getUpdateCycle().intValue();
        if (1 == updateCycle3) {
            if (!satisfyMonth) {
                reviseType = "2";
            } else {
                reviseType = "4";
            }
        } else if (2 == updateCycle3) {
            reviseType = "4";
        } else if (3 == updateCycle3) {
            if (!satisfyMonth) {
                reviseType = "2";
            } else {
                reviseType = "4";
            }
        }

        reviseDateModel.setReviseType(reviseType);
        if (reviseMonth != 2147483647) {
            reviseDateModel.setReviseMonth(String.valueOf(reviseMonth));
        }

        this.log.debug("return reviseDateModel: type:" + reviseDateModel.getReviseType() + " ReviseMonth:" + reviseDateModel.getReviseMonth());
        return reviseDateModel;
    }

    private void reviseDayLabelDate(CiCustomListInfo ciCustomListInfo, List<CiLabelRule> ciLabelRuleList, List<CiGroupAttrRel> ciGroupAttrRelList) {
        CacheBase cache = CacheBase.getInstance();
        String day = ciCustomListInfo.getDayLabelDate();
        int dayNum = 0;
        if (StringUtil.isNotEmpty(day)) {
            dayNum = Integer.valueOf(day).intValue();
        }

        int reviseDay = 2147483647;
        this.log.debug("所选日期 dayNum:" + dayNum);
        Iterator var9;
        int attrSource;
        String labelIdStr;
        CiLabelInfo ciLabelInfo;
        int updateCycle;
        String labelDataDate;
        int tempNum;
        if (ciLabelRuleList != null && ciLabelRuleList.size() > 0) {
            var9 = ciLabelRuleList.iterator();

            while (var9.hasNext()) {
                CiLabelRule rule = (CiLabelRule) var9.next();
                if (!StringUtil.isNotEmpty(rule.getParentId())) {
                    attrSource = rule.getElementType().intValue();
                    if (attrSource == 2) {
                        labelIdStr = rule.getCalcuElement();
                        ciLabelInfo = cache.getEffectiveLabel(labelIdStr);
                        updateCycle = ciLabelInfo.getUpdateCycle().intValue();
                        labelDataDate = ciLabelInfo.getDataDate();
                        if (1 == updateCycle && StringUtil.isNotEmpty(labelDataDate) && dayNum != 0) {
                            tempNum = Integer.valueOf(labelDataDate).intValue();
                            if (tempNum < dayNum && reviseDay > tempNum) {
                                reviseDay = tempNum;
                            }
                        }
                    }
                }
            }
        }

        if (ciGroupAttrRelList != null && ciGroupAttrRelList.size() > 0) {
            var9 = ciGroupAttrRelList.iterator();

            while (var9.hasNext()) {
                CiGroupAttrRel rule1 = (CiGroupAttrRel) var9.next();
                attrSource = rule1.getAttrSource();
                if (attrSource == 2) {
                    labelIdStr = rule1.getLabelOrCustomId();
                    ciLabelInfo = cache.getEffectiveLabel(labelIdStr);
                    updateCycle = ciLabelInfo.getUpdateCycle().intValue();
                    labelDataDate = ciLabelInfo.getDataDate();
                    if (1 == updateCycle && StringUtil.isNotEmpty(labelDataDate) && dayNum != 0) {
                        tempNum = Integer.valueOf(labelDataDate).intValue();
                        if (tempNum < dayNum && reviseDay > tempNum) {
                            reviseDay = tempNum;
                        }
                    }
                }
            }
        }

        if (reviseDay != 2147483647) {
            ciCustomListInfo.setDayLabelDate(String.valueOf(reviseDay));
        }

    }

    private String getFromSqlStr(List<CiLabelRule> ciLabelRuleList, String monthDate, String dayDate, List<CiGroupAttrRel> ciGroupAttrRelList, boolean isNeedColumn, String userId, Integer interval, Integer updateCycle, boolean isValidate) throws Exception {
        if (ciLabelRuleList != null && ciLabelRuleList.size() != 0) {
            if (interval == null) {
                interval = Integer.valueOf(0);
            }

            if (updateCycle == null) {
                interval = Integer.valueOf(0);
            } else if (2 != updateCycle.intValue() && 3 != updateCycle.intValue()) {
                interval = Integer.valueOf(0);
            }

            StringBuffer fromSqlSb = new StringBuffer("");
            LinkedHashSet tableNameSet = new LinkedHashSet();
            StringBuffer wherelabel = new StringBuffer(" (");
            StringBuffer whereSb = new StringBuffer("where 1=1 and ");
            String dbType = Configure.getInstance().getProperty("CI_BACK_DBTYPE");
            String monthStr = null;
            if (StringUtil.isNotEmpty(monthDate)) {
                monthStr = monthDate;
            } else {
                monthStr = CacheBase.getInstance().getNewLabelMonth();
            }

            CacheBase cache = CacheBase.getInstance();
            Object monthTabelAlias = null;
            String dayTabelAlias = null;
            String dayTableName = null;
            String monthTableName = null;
            String firstDayTableName = null;
            String firstMonthTableName = null;
            LabelElementFactory fac = new LabelElementFactory();

            CiMdaSysTableColumn selectSql;
            CiMdaSysTable singleTableAlias;
            String columnName;
            String table;
            String var60;
            for (int whereForCity = 0; whereForCity < ciLabelRuleList.size(); ++whereForCity) {
                CiLabelRule attrSelectColSql = (CiLabelRule) ciLabelRuleList.get(whereForCity);
                int dayTableName4Attr = attrSelectColSql.getElementType().intValue();
                String vertAttrTableNameMap;
                String isVerticalAttr;
                String column;
                if (dayTableName4Attr == 2) {
                    vertAttrTableNameMap = attrSelectColSql.getCalcuElement();
                    CiLabelInfo leftJoinSqlStr = cache.getEffectiveLabel(vertAttrTableNameMap);
                    CiLabelExtInfo result = leftJoinSqlStr.getCiLabelExtInfo();
                    log.info("leftJoinSqlStr" + " " + leftJoinSqlStr);
                    log.info("result" + " " + result);
                    selectSql = result.getCiMdaSysTableColumn();
                    log.info("selectSql" + " " + selectSql);
                    singleTableAlias = selectSql.getCiMdaSysTable();
                    columnName = singleTableAlias.getTableName();
                    isVerticalAttr = "t_" + singleTableAlias.getTableId();
                    column = isVerticalAttr + "." + selectSql.getColumnName();
                    String v;
                    if (1 == leftJoinSqlStr.getUpdateCycle().intValue()) {
                        if (StringUtil.isEmpty(dayDate)) {
                            dayDate = CacheBase.getInstance().getNewLabelDay();
                            this.log.warn("使用日标签时，日期为空");
                        }

                        v = "_" + dayDate;
                        columnName = columnName + v;
                        dayTableName = columnName.toUpperCase() + " " + isVerticalAttr;
                        if (StringUtil.isEmpty(firstDayTableName)) {
                            firstDayTableName = dayTableName;
                        }

                        tableNameSet.add(dayTableName);
                        if (StringUtil.isEmpty(dayTabelAlias)) {
                            dayTabelAlias = isVerticalAttr;
                        }
                    } else if (2 == leftJoinSqlStr.getUpdateCycle().intValue()) {
                        if (StringUtil.isEmpty(monthStr)) {
                            monthStr = CacheBase.getInstance().getNewLabelMonth();
                            this.log.warn("使用月标签时，月份为空");
                        }

                        v = "_" + monthStr;
                        columnName = columnName + v;
                        monthTableName = columnName.toUpperCase() + " " + isVerticalAttr;
                        if (StringUtil.isEmpty(firstMonthTableName)) {
                            firstMonthTableName = monthTableName;
                        }

                        tableNameSet.add(monthTableName);
                        if (StringUtil.isEmpty(dayTabelAlias)) {
                            dayTabelAlias = isVerticalAttr;
                        }
                    }

                    int var67 = leftJoinSqlStr.getLabelTypeId().intValue();
                    fac.setLabelElement(Integer.valueOf(var67));
                    table = fac.getLabelElement().getConditionSql(attrSelectColSql, selectSql, column, interval, updateCycle, isValidate);
                    log.info("table" + " " + table);
                    wherelabel.append(table);
                    log.info("wherelabel" + " " + wherelabel);
                } else if (dayTableName4Attr == 4) {
                    vertAttrTableNameMap = attrSelectColSql.getCalcuElement();
                    CiProductInfo var52 = cache.getEffectiveProduct(vertAttrTableNameMap);
                    CiMdaSysTableColumn var55 = var52.getCiMdaSysTableColumn();
                    CiMdaSysTable var58 = var55.getCiMdaSysTable();
                    var60 = var58.getTableName();
                    columnName = "t_" + var58.getTableId();
                    isVerticalAttr = columnName + "." + var55.getColumnName();
                    if (StringUtil.isNotEmpty(monthStr)) {
                        column = "_" + monthStr;
                        var60 = var60 + column;
                    } else {
                        var60 = var60 + "_" + var58.getTablePostfix();
                    }

                    tableNameSet.add(var60.toUpperCase() + " " + columnName);
                    int var66 = -1;
                    if (attrSelectColSql.getLabelFlag() != null) {
                        var66 = attrSelectColSql.getLabelFlag().intValue();
                    }

                    if (var66 == 0) {
                        wherelabel.append(" ").append(isVerticalAttr).append(" = ").append(attrSelectColSql.getMinVal());
                    } else if (1 == var66) {
                        wherelabel.append(" ").append(isVerticalAttr).append(" = ").append(attrSelectColSql.getMaxVal());
                    }
                } else {
                    wherelabel.append(" ").append(attrSelectColSql.getCalcuElement());
                }
            }

            wherelabel.append(")");
            log.info("wherelabel2" + " " + wherelabel);


            String var48 = this.getWhereForCity(userId, (String) monthTabelAlias, dayTabelAlias);
            if (StringUtil.isNotEmpty(var48)) {
                whereSb.append(var48);
            }

            whereSb.append(wherelabel);
            String var49 = "";
            String var50 = null;
            HashMap var51 = new HashMap();
            if (isNeedColumn) {
                DataBaseAdapter var53 = new DataBaseAdapter(dbType);
                Iterator var59 = ciGroupAttrRelList.iterator();

                label302:
                while (true) {
                    String columnName1;
                    String columnNameForTableName;
                    StringBuffer vertAttrTableName;
                    String attrValsStr;
                    String[] vals;
                    StringBuffer wherelabelSb;
                    int k;
                    CiGroupAttrRel var56;
                    CiLabelInfo var62;
                    CiMdaSysTableColumn var68;
                    String var71;
                    String var72;
                    do {
                        do {
                            while (true) {
                                do {
                                    do {
                                        if (!var59.hasNext()) {
                                            break label302;
                                        }

                                        var56 = (CiGroupAttrRel) var59.next();
                                        var62 = cache.getEffectiveLabel(var56.getLabelOrCustomId());
                                    } while (var62 == null);
                                } while (var62.getDataStatusId().intValue() != 2);

                                CiLabelExtInfo var63 = var62.getCiLabelExtInfo();
                                int var65 = var56.getIsVerticalAttr();
                                var68 = null;
                                CiLabelVerticalColumnRel var69 = null;
                                if (var65 == 1) {
                                    table = var56.getLabelOrCustomColumn();
                                    Set tableName = var63.getCiLabelVerticalColumnRels();
                                    Iterator tablePostfix = tableName.iterator();

                                    while (tablePostfix.hasNext()) {
                                        CiLabelVerticalColumnRel alias = (CiLabelVerticalColumnRel) tablePostfix.next();
                                        if (table.equals(alias.getId().getColumnId().toString())) {
                                            var69 = alias;
                                            var68 = alias.getCiMdaSysTableColumn();
                                            break;
                                        }
                                    }
                                } else {
                                    var68 = var63.getCiMdaSysTableColumn();
                                }

                                CiMdaSysTable var70 = var68.getCiMdaSysTable();
                                var71 = var70.getTableName();
                                var72 = "t_" + var70.getTableId();
                                String asName;
                                String var73;
                                if (1 == var62.getUpdateCycle().intValue()) {
                                    if (StringUtil.isEmpty(dayDate) || dayDate != null && dayDate.equals(CacheBase.getInstance().getNewLabelDay())) {
                                        dayDate = CacheBase.getInstance().getNewLabelDay();
                                        var73 = var62.getDataDate();
                                        if (StringUtil.isNotEmpty(dayDate) && StringUtil.isNotEmpty(var73)) {
                                            dayDate = DateUtil.getEarlierDate(dayDate, var73, "yyyyMMdd");
                                        }

                                        this.log.warn("使用日标签时，日期为空");
                                    }

                                    var73 = "_" + dayDate;
                                    var71 = var71 + var73;
                                    var72 = var72 + "_" + dayDate;
                                    asName = var72 + "." + var68.getColumnName();
                                    columnName1 = var68.getColumnName();
                                    columnNameForTableName = var68.getColumnName();
                                    if (5 == var62.getLabelTypeId().intValue() && 1 == var68.getColumnDataTypeId().intValue()) {
                                        asName = var53.getColumnToChar(asName);
                                        columnNameForTableName = var53.getColumnToChar(columnNameForTableName);
                                    } else if (8 == var62.getLabelTypeId().intValue() && var69 != null && 5 == var69.getLabelTypeId().intValue() && 1 == var68.getColumnDataTypeId().intValue()) {
                                        asName = var53.getColumnToChar(asName);
                                        columnNameForTableName = var53.getColumnToChar(columnNameForTableName);
                                    }

                                    var49 = var49 + ", " + asName;
                                    var50 = var71.toUpperCase() + " " + var72;
                                    tableNameSet.add(var50);
                                    break;
                                }

                                if (2 == var62.getUpdateCycle().intValue()) {
                                    if (monthStr.equals(CacheBase.getInstance().getNewLabelMonth())) {
                                        monthStr = CacheBase.getInstance().getNewLabelMonth();
                                        var73 = var62.getDataDate();
                                        if (StringUtil.isNotEmpty(monthStr) && StringUtil.isNotEmpty(var73)) {
                                            monthStr = DateUtil.getEarlierDate(monthStr, var73, "yyyyMM");
                                        }

                                        this.log.warn("使用月标签时，月份为空");
                                    }

                                    var73 = "_" + monthStr;
                                    var71 = var71 + var73;
                                    var72 = var72 + "_" + monthStr;
                                    asName = var72 + "." + var68.getColumnName();
                                    columnName1 = var68.getColumnName();
                                    columnNameForTableName = var68.getColumnName();
                                    if (5 == var62.getLabelTypeId().intValue() && 1 == var68.getColumnDataTypeId().intValue()) {
                                        asName = var53.getColumnToChar(asName);
                                        columnNameForTableName = var53.getColumnToChar(columnNameForTableName);
                                    } else if (8 == var62.getLabelTypeId().intValue() && var69 != null && 5 == var69.getLabelTypeId().intValue() && 1 == var68.getColumnDataTypeId().intValue()) {
                                        asName = var53.getColumnToChar(asName);
                                        columnNameForTableName = var53.getColumnToChar(columnNameForTableName);
                                    }

                                    var49 = var49 + ", " + asName;
                                    monthTableName = var71.toUpperCase() + " " + var72;
                                    tableNameSet.add(monthTableName);
                                    if (8 == var62.getLabelTypeId().intValue() && (StringUtil.isNotEmpty(var56.getAttrVal()) || StringUtil.isNotEmpty(var56.getTableName()))) {
                                        vertAttrTableName = (new StringBuffer("(select * from ")).append(var71.toUpperCase()).append(" where 1=1 ");
                                        if (StringUtil.isNotEmpty(var56.getTableName())) {
                                            attrValsStr = "VALUE_ID";
                                            vertAttrTableName.append("and ").append(columnNameForTableName).append(" in (select ").append(attrValsStr).append(" from ").append(var56.getTableName()).append(" ))").append(" ").append(var72);
                                        } else {
                                            attrValsStr = var56.getAttrVal();
                                            vals = attrValsStr.split(",");
                                            wherelabelSb = new StringBuffer(" and ");
                                            if (vals.length == 1) {
                                                if (2 == var68.getColumnDataTypeId().intValue()) {
                                                    wherelabelSb.append(" ").append(columnName1).append(" = \'").append(vals[0]).append("\'");
                                                } else if (1 == var68.getColumnDataTypeId().intValue()) {
                                                    wherelabelSb.append(" ").append(columnName1).append(" = ").append(vals[0]);
                                                }
                                            } else {
                                                if (attrValsStr.length() == attrValsStr.lastIndexOf(",")) {
                                                    attrValsStr = attrValsStr.substring(0, attrValsStr.length() - 1);
                                                }

                                                if (2 != var68.getColumnDataTypeId().intValue()) {
                                                    if (1 == var68.getColumnDataTypeId().intValue()) {
                                                        wherelabelSb.append(" ").append(columnName1).append(" in (").append(attrValsStr).append(")");
                                                    }
                                                } else {
                                                    wherelabelSb.append(" ").append(columnName1).append(" in ( ");

                                                    for (k = 0; k < vals.length; ++k) {
                                                        wherelabelSb.append("\'" + vals[k] + "\'");
                                                        if (k != vals.length - 1) {
                                                            wherelabelSb.append(",");
                                                        }
                                                    }

                                                    wherelabelSb.append(" )");
                                                }
                                            }

                                            vertAttrTableName.append(wherelabelSb).append(")").append(" ").append(var72);
                                        }

                                        var51.put(var71.toUpperCase() + " " + var72, vertAttrTableName.toString());
                                    }
                                }
                            }
                        } while (8 != var62.getLabelTypeId().intValue());
                    }
                    while (!StringUtil.isNotEmpty(var56.getAttrVal()) && !StringUtil.isNotEmpty(var56.getTableName()));

                    vertAttrTableName = new StringBuffer("(select * from " + var71.toUpperCase() + " where 1=1 ");
                    if (StringUtil.isNotEmpty(var56.getTableName())) {
                        attrValsStr = "VALUE_ID";
                        vertAttrTableName.append("and ").append(columnNameForTableName).append(" in (select ").append(attrValsStr).append(" from ").append(var56.getTableName()).append(" )) ").append(var72);
                    } else {
                        attrValsStr = var56.getAttrVal();
                        vals = attrValsStr.split(",");
                        wherelabelSb = new StringBuffer(" and ");
                        if (vals.length == 1) {
                            if (2 == var68.getColumnDataTypeId().intValue()) {
                                wherelabelSb.append(" ").append(columnName1).append(" = \'").append(vals[0]).append("\'");
                            } else if (1 == var68.getColumnDataTypeId().intValue()) {
                                wherelabelSb.append(" ").append(columnName1).append(" = ").append(vals[0]);
                            }
                        } else {
                            if (attrValsStr.length() == attrValsStr.lastIndexOf(",")) {
                                attrValsStr = attrValsStr.substring(0, attrValsStr.length() - 1);
                            }

                            if (2 == var68.getColumnDataTypeId().intValue()) {
                                wherelabelSb.append(" ").append(columnName1).append(" in ( ");

                                for (k = 0; k < vals.length; ++k) {
                                    wherelabelSb.append("\'" + vals[k] + "\'");
                                    if (k != vals.length - 1) {
                                        wherelabelSb.append(",");
                                    }
                                }

                                wherelabelSb.append(" )");
                            } else if (1 == var68.getColumnDataTypeId().intValue()) {
                                wherelabelSb.append(" ").append(columnName1).append(" in (").append(attrValsStr + ")");
                            }
                        }

                        vertAttrTableName.append(wherelabelSb + ")" + " " + var72);
                    }

                    var51.put(var71.toUpperCase() + " " + var72, vertAttrTableName.toString());
                }
            }

            String var54 = this.getLeftJoinSqlStr(tableNameSet, firstDayTableName, var51);
            fromSqlSb.append(" from ").append(var54).append(whereSb);
            String var57 = fromSqlSb.toString();
            selectSql = null;
            if (isNeedColumn) {
                singleTableAlias = null;
                String[] var64;
                if (StringUtil.isNotEmpty(firstDayTableName)) {
                    var64 = firstDayTableName.split(" ");
                    if (var64.length < 2) {
                        throw new CIServiceException("日表没有别名");
                    }

                    var60 = var64[1];
                } else {
                    var64 = firstMonthTableName.split(" ");
                    if (var64.length < 2) {
                        throw new CIServiceException("月表没有别名");
                    }

                    var60 = var64[1];
                }

                columnName = Configure.getInstance().getProperty("RELATED_COLUMN");
                String var61 = " " + var60 + "." + columnName;
                var61 = var61 + var49;
                var57 = var61 + var57;
            }

            return var57;
        } else {
            return null;
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public String getFromSqlStrForVerticalLabel(CiLabelRule verticalLabelRule, String monthDate, String dayDate, boolean isNeedColumn, String userId, Integer interval, Integer updateCycle, boolean isValidate) throws Exception {
        if (verticalLabelRule != null && !StringUtil.isEmpty(verticalLabelRule.getCalcuElement())) {
            if (interval == null) {
                interval = Integer.valueOf(0);
            }

            if (updateCycle == null) {
                interval = Integer.valueOf(0);
            } else if (2 != updateCycle.intValue() && 3 != updateCycle.intValue()) {
                interval = Integer.valueOf(0);
            }

            StringBuffer fromSqlSb = new StringBuffer("");
            LinkedHashSet tableNameSet = new LinkedHashSet();
            StringBuffer wherelabel = new StringBuffer(" (");
            StringBuffer whereSb = new StringBuffer("where 1=1 and ");
            String monthStr = null;
            if (StringUtil.isNotEmpty(monthDate)) {
                monthStr = monthDate;
            } else {
                monthStr = CacheBase.getInstance().getNewLabelMonth();
            }

            CacheBase cache = CacheBase.getInstance();
            CiLabelInfo labelInfo = cache.getEffectiveLabel(verticalLabelRule.getCalcuElement());
            HashMap map = new HashMap();
            Set ciLabelVerticalColumnRels = labelInfo.getCiLabelExtInfo().getCiLabelVerticalColumnRels();
            Iterator monthTabelAlias = ciLabelVerticalColumnRels.iterator();

            String dayTabelAlias;
            while (monthTabelAlias.hasNext()) {
                CiLabelVerticalColumnRel ciLabelRuleList = (CiLabelVerticalColumnRel) monthTabelAlias.next();
                dayTabelAlias = "" + ciLabelRuleList.getId().getColumnId();
                map.put(dayTabelAlias, ciLabelRuleList);
            }

            List var37 = this.madeVerticalLabelRules(verticalLabelRule);
            String var38 = null;
            dayTabelAlias = null;
            String dayTableName = null;
            String monthTableName = null;
            String firstDayTableName = null;
            String firstMonthTableName = null;
            LabelElementFactory fac = new LabelElementFactory();

            CiLabelVerticalColumnRel selectSql;
            CiMdaSysTableColumn singleTableAlias;
            for (int whereForCity = 0; whereForCity < var37.size(); ++whereForCity) {
                CiLabelRule leftJoinSqlStr = (CiLabelRule) var37.get(whereForCity);
                int result = leftJoinSqlStr.getElementType().intValue();
                if (result == 2) {
                    selectSql = (CiLabelVerticalColumnRel) map.get(leftJoinSqlStr.getCalcuElement());
                    leftJoinSqlStr.setLabelTypeId(selectSql.getLabelTypeId());
                    singleTableAlias = selectSql.getCiMdaSysTableColumn();
                    CiMdaSysTable columnName = singleTableAlias.getCiMdaSysTable();
                    String tableName = columnName.getTableName();
                    String alias = "t_" + columnName.getTableId();
                    String asName = alias + "." + singleTableAlias.getColumnName();
                    String labelTypeId;
                    if (1 == labelInfo.getUpdateCycle().intValue()) {
                        if (StringUtil.isEmpty(dayDate)) {
                            dayDate = CacheBase.getInstance().getNewLabelDay();
                            this.log.warn("使用日标签时，日期为空");
                        }

                        labelTypeId = "_" + dayDate;
                        tableName = tableName + labelTypeId;
                        dayTableName = tableName.toUpperCase() + " " + alias;
                        if (StringUtil.isEmpty(firstDayTableName)) {
                            firstDayTableName = dayTableName;
                        }

                        tableNameSet.add(dayTableName);
                        if (StringUtil.isEmpty(dayTabelAlias)) {
                            dayTabelAlias = alias;
                        }
                    } else if (2 == labelInfo.getUpdateCycle().intValue()) {
                        if (StringUtil.isEmpty(monthStr)) {
                            monthStr = CacheBase.getInstance().getNewLabelMonth();
                            this.log.warn("使用月标签时，月份为空");
                        }

                        labelTypeId = "_" + monthStr;
                        tableName = tableName + labelTypeId;
                        monthTableName = tableName.toUpperCase() + " " + alias;
                        if (StringUtil.isEmpty(firstMonthTableName)) {
                            firstMonthTableName = monthTableName;
                        }

                        tableNameSet.add(monthTableName);
                        if (StringUtil.isEmpty(var38)) {
                            var38 = alias;
                        }
                    }

                    int var46 = selectSql.getLabelTypeId().intValue();
                    fac.setLabelElement(Integer.valueOf(var46));
                    String labelConditionSql = fac.getLabelElement().getConditionSql(leftJoinSqlStr, singleTableAlias, asName, interval, updateCycle, isValidate);
                    wherelabel.append(labelConditionSql);
                } else {
                    wherelabel.append(" ").append(leftJoinSqlStr.getCalcuElement());
                }
            }

            wherelabel.append(")");
            String var39 = this.getWhereForCity(userId, var38, dayTabelAlias);
            if (StringUtil.isNotEmpty(var39)) {
                whereSb.append(var39);
            }

            whereSb.append(wherelabel);
            String var40 = this.getLeftJoinSqlStr(tableNameSet, firstDayTableName, (Map) null);
            fromSqlSb.append(" from ").append(var40).append(whereSb);
            String var41 = fromSqlSb.toString();
            selectSql = null;
            if (isNeedColumn) {
                singleTableAlias = null;
                String var43;
                String[] var44;
                if (StringUtil.isNotEmpty(firstDayTableName)) {
                    var44 = firstDayTableName.split(" ");
                    if (var44.length < 2) {
                        throw new CIServiceException("日表没有别名");
                    }

                    var43 = var44[1];
                } else {
                    var44 = firstMonthTableName.split(" ");
                    if (var44.length < 2) {
                        throw new CIServiceException("月表没有别名");
                    }

                    var43 = var44[1];
                }

                String var45 = Configure.getInstance().getProperty("RELATED_COLUMN");
                String var42 = " " + var43 + "." + var45;
                var41 = var42 + var41;
            }

            return var41;
        } else {
            return null;
        }
    }

    private List<CiLabelRule> madeVerticalLabelRules(CiLabelRule verticalLabelRule) throws Exception {
        List oldRules = verticalLabelRule.getChildCiLabelRuleList();
        ArrayList newRules = new ArrayList();
        int flag = -1;
        if (verticalLabelRule.getLabelFlag() != null) {
            flag = verticalLabelRule.getLabelFlag().intValue();
        }

        for (int i = 0; i < oldRules.size(); ++i) {
            CiLabelRule rule = (CiLabelRule) oldRules.get(i);
            rule.setLabelFlag(Integer.valueOf(flag));
            newRules.add(rule);
            if (i != oldRules.size() - 1) {
                CiLabelRule symbolRule = new CiLabelRule();
                if (flag == 0) {
                    symbolRule.setCalcuElement("or");
                } else {
                    symbolRule.setCalcuElement("and");
                }

                symbolRule.setElementType(Integer.valueOf(1));
                symbolRule.setCustomType(Integer.valueOf(1));
                newRules.add(symbolRule);
            }
        }

        return newRules;
    }

    private String getWhereForCity(String userId, String monthTabelAlias, String dayTabelAlias) {
        String whereCity = null;

        try {
            StringBuffer e = new StringBuffer("( ");
            boolean var22 = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
            if (StringUtil.isEmpty(userId)) {
                this.log.error("拼接sql时用户ID为空");
                throw new CIServiceException("拼接sql时用户ID为空");
            } else {
                String highLevelCityId = PrivilegeServiceUtil.getCityId(userId);
                String centerCity = Configure.getInstance().getProperty("CENTER_CITYID");
                boolean isCenterCityUser = highLevelCityId.equals(centerCity);
                boolean hasAuthority = var22 && !isCenterCityUser;
                List icities = null;
                if (hasAuthority) {
                    String cityColumn = Configure.getInstance().getProperty("CITY_COLUMN").toUpperCase();
                    String cityColumnType = Configure.getInstance().getProperty("CITY_COLUMN_TYPE").toLowerCase();
                    String countyColumn = Configure.getInstance().getProperty("COUNTY_COLUMN").toUpperCase();
                    String countyColumnType = Configure.getInstance().getProperty("COUNTY_COLUMN_TYPE").toLowerCase();
                    CacheBase cache = CacheBase.getInstance();
                    icities = PrivilegeServiceUtil.getUserCityIds(userId);
                    CopyOnWriteArrayList onlyCityIds = cache.getKeyList("CITY_IDS_LIST");
                    String var24;
                    if (StringUtil.isEmpty(monthTabelAlias) && StringUtil.isNotEmpty(dayTabelAlias) || StringUtil.isNotEmpty(monthTabelAlias) && StringUtil.isEmpty(dayTabelAlias)) {
                        for (int var23 = 0; var23 < icities.size(); ++var23) {
                            var24 = (String) icities.get(var23);
                            if (var24.equals(centerCity)) {
                                return null;
                            }

                            if (onlyCityIds.contains(var24)) {
                                if ("char".equals(cityColumnType)) {
                                    if (StringUtil.isNotEmpty(monthTabelAlias)) {
                                        e.append(monthTabelAlias).append(".").append(cityColumn).append(" = \'").append(var24).append("\' ");
                                    } else if (StringUtil.isNotEmpty(dayTabelAlias)) {
                                        e.append(dayTabelAlias).append(".").append(cityColumn).append(" = \'").append(var24).append("\' ");
                                    }
                                } else if ("number".equals(cityColumnType)) {
                                    if (StringUtil.isNotEmpty(monthTabelAlias)) {
                                        e.append(monthTabelAlias).append(".").append(cityColumn).append(" = ").append(var24).append(" ");
                                    } else if (StringUtil.isNotEmpty(dayTabelAlias)) {
                                        e.append(dayTabelAlias).append(".").append(cityColumn).append(" = ").append(var24).append(" ");
                                    }
                                }
                            } else if ("char".equals(countyColumnType)) {
                                if (StringUtil.isNotEmpty(monthTabelAlias)) {
                                    e.append(monthTabelAlias).append(".").append(countyColumn).append(" = \'").append(var24).append("\' ");
                                } else if (StringUtil.isNotEmpty(dayTabelAlias)) {
                                    e.append(dayTabelAlias).append(".").append(countyColumn).append(" = \'").append(var24).append("\' ");
                                }
                            } else if ("number".equals(countyColumnType)) {
                                if (StringUtil.isNotEmpty(monthTabelAlias)) {
                                    e.append(monthTabelAlias).append(".").append(countyColumn).append(" = ").append(var24).append(" ");
                                } else if (StringUtil.isNotEmpty(dayTabelAlias)) {
                                    e.append(dayTabelAlias).append(".").append(countyColumn).append(" = ").append(var24).append(" ");
                                }
                            }

                            if (icities.size() > 1 && var23 != icities.size() - 1) {
                                e.append("or ");
                            }
                        }
                    } else if (StringUtil.isNotEmpty(monthTabelAlias) && StringUtil.isNotEmpty(dayTabelAlias)) {
                        StringBuffer monthCitySb = new StringBuffer("( ");

                        for (int dayCityStr = 0; dayCityStr < icities.size(); ++dayCityStr) {
                            String cityId = (String) icities.get(dayCityStr);
                            if (cityId.equals(centerCity)) {
                                return null;
                            }

                            if (onlyCityIds.contains(cityId)) {
                                if ("char".equals(cityColumnType)) {
                                    monthCitySb.append(monthTabelAlias).append(".").append(cityColumn).append(" = \'").append(cityId).append("\' ");
                                } else if ("number".equals(cityColumnType)) {
                                    monthCitySb.append(monthTabelAlias).append(".").append(cityColumn).append(" = ").append(cityId).append(" ");
                                }
                            } else if ("char".equals(countyColumnType)) {
                                monthCitySb.append(monthTabelAlias).append(".").append(countyColumn).append(" = \'").append(cityId).append("\' ");
                            } else if ("number".equals(countyColumnType)) {
                                monthCitySb.append(monthTabelAlias).append(".").append(countyColumn).append(" = ").append(cityId).append(" ");
                            }

                            if (icities.size() > 1 && dayCityStr != icities.size() - 1) {
                                monthCitySb.append("or ");
                            }
                        }

                        monthCitySb.append(") ");
                        var24 = monthCitySb.toString().replace(monthTabelAlias + ".", dayTabelAlias + ".");
                        e.append(monthCitySb).append("and ").append(var24);
                    }

                    e.append(") and ");
                    whereCity = e.toString();
                }

                return whereCity;
            }
        } catch (Exception var21) {
            String msg = "根据userId拼接sql时地市条件错误";
            this.log.error(msg, var21);
            throw new CIServiceException(msg, var21);
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public String getLastDateStr(List<CiLabelRule> ciLabelRuleList) throws CIServiceException {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date nowDate = new Date();
        String dayDate = null;
        String currentDay = df.format(nowDate);
        boolean success = false;

        for (int i = 1; i <= 3; ++i) {
            String lastDate = DateUtil.getLastDate(currentDay, i);
            String sql = this.getValidateDayTableNameSql(ciLabelRuleList, lastDate);
            if (!StringUtil.isNotEmpty(sql)) {
                throw new CIServiceException("拖动的标签不涉及日表");
            }

            success = this.queryValidateSql(sql);
            if (success) {
                dayDate = lastDate;
                break;
            }
        }

        return dayDate;
    }

    private String getValidateDayTableNameSql(List<CiLabelRule> ciLabelRuleList, String lastDateStr) throws CIServiceException {
        String result = null;
        LinkedHashSet tableNameSet = new LinkedHashSet();

        try {
            CacheBase e = CacheBase.getInstance();
            int var17 = 0;

            while (true) {
                if (var17 >= ciLabelRuleList.size()) {
                    StringBuffer var18 = new StringBuffer();
                    if (tableNameSet.size() > 0) {
                        Iterator var19 = tableNameSet.iterator();

                        while (var19.hasNext()) {
                            String var20 = (String) var19.next();
                            var18.append(" ").append(var20).append(",");
                        }

                        result = var18.substring(0, var18.length() - 1);
                    }
                    break;
                }

                CiLabelRule it = (CiLabelRule) ciLabelRuleList.get(var17);
                int tableNames = it.getElementType().intValue();
                if (tableNames == 2) {
                    String labelIdStr = it.getCalcuElement();
                    CiLabelInfo ciLabelInfo = e.getEffectiveLabel(labelIdStr);
                    CiLabelExtInfo ciLabelExtInfo = ciLabelInfo.getCiLabelExtInfo();
                    CiMdaSysTableColumn column = ciLabelExtInfo.getCiMdaSysTableColumn();
                    CiMdaSysTable table = column.getCiMdaSysTable();
                    String tableName = table.getTableName().toUpperCase();
                    if (1 == ciLabelInfo.getUpdateCycle().intValue()) {
                        if (StringUtil.isNotEmpty(lastDateStr)) {
                            String tablePostfix = "_" + lastDateStr;
                            tableName = tableName + tablePostfix;
                        } else {
                            tableName = tableName + "_" + table.getTablePostfix();
                        }

                        tableNameSet.add(tableName.toUpperCase());
                    }
                }

                ++var17;
            }
        } catch (Exception var16) {
            String message = "拼接验证日表是否存在SQL出错";
            this.log.error(message, var16);
            throw new CIServiceException(message, var16);
        }

        if (StringUtil.isNotEmpty(result)) {
            result = this.getTabelExistSqlStr(result);
        }

        return result;
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiLabelRule> getNewCiLabelRuleList(List<CiLabelRule> originalList) throws CIServiceException {
        ArrayList newList = new ArrayList();

        String message;
        try {
            StringBuffer e = new StringBuffer();
            Iterator firstExceptPos = originalList.iterator();

            while (firstExceptPos.hasNext()) {
                CiLabelRule var11 = (CiLabelRule) firstExceptPos.next();
                e.append(var11.getCalcuElement()).append(" ");
            }

            message = e.substring(0, e.lastIndexOf(" ")).toString();
            int var12 = message.indexOf("-");
            String resultStr = "";
            if (var12 > 0) {
                resultStr = ExpressionPaser.getNewString(message, var12);
                String[] resultArr = resultStr.split(" ");

                for (int i = 0; i < resultArr.length; ++i) {
                    CiLabelRule rule = ((CiLabelRule) originalList.get(i)).clone();
                    if (resultArr[i].startsWith("_")) {
                        rule.setCalcuElement(resultArr[i].replace("_", ""));
                        if (rule.getLabelFlag() != null) {
                            if (rule.getLabelFlag().intValue() == 0) {
                                rule.setLabelFlag(Integer.valueOf(1));
                            } else if (rule.getLabelFlag().intValue() == 1) {
                                rule.setLabelFlag(Integer.valueOf(0));
                            } else if (rule.getLabelFlag().intValue() == 2) {
                                rule.setLabelFlag(Integer.valueOf(3));
                            } else if (rule.getLabelFlag().intValue() == 3) {
                                rule.setLabelFlag(Integer.valueOf(2));
                            }
                        } else {
                            rule.setLabelFlag(Integer.valueOf(0));
                        }
                    } else {
                        rule.setCalcuElement(resultArr[i]);
                    }

                    newList.add(rule);
                }

                return newList;
            } else {
                return originalList;
            }
        } catch (Exception var10) {
            message = "转换CiLabelRule List，替换“剔除”错误。";
            this.log.error(message, var10);
            throw new CIServiceException(message, var10);
        }
    }
    public int queryForExist(String labelIds){
        if(labelIds.length() != 0 && !"".equals(labelIds)){
            return customersJdbcDao.queryForExist(labelIds);
        }else {
            return 0;
        }
    }

    public int queryForPowerMatch(String userId, String labelIds){
        if(!"".equals(userId) && labelIds.length() != 0){
            return customersJdbcDao.queryForPowerMatch(userId, labelIds);
        }else {
            return 0;
        }
    }


    private String getLeftJoinSqlStr(Set<String> tableNameSet, String dayTableName, Map<String, String> vertAttrTableNameMap) throws Exception {
        if (tableNameSet != null && tableNameSet.size() != 0) {
            StringBuffer joinSb = new StringBuffer("");
            String relatedColumn = Configure.getInstance().getProperty("RELATED_COLUMN");
            int i = 0;
            Iterator it = tableNameSet.iterator();
            String lastAlias = "";
            if (StringUtil.isEmpty(dayTableName)) {
                while (it.hasNext()) {
                    String var12 = (String) it.next();
                    String[] var13 = var12.split(" ");
                    if (var13.length < 2) {
                        throw new CIServiceException("没有别名");
                    }

                    if (vertAttrTableNameMap != null && vertAttrTableNameMap.containsKey(var12)) {
                        var12 = (String) vertAttrTableNameMap.get(var12);
                    }

                    if (i == 0) {
                        joinSb.append(var12).append(" ");
                    } else {
                        joinSb.append("left join ").append(var12).append(" on ").append(lastAlias).append(".").append(relatedColumn).append(" = ").append(var13[1]).append(".").append(relatedColumn).append(" ");
                    }

                    lastAlias = var13[1];
                    ++i;
                }
            } else {
                joinSb.append(dayTableName).append(" ");
                String[] dayTableNames = dayTableName.split(" ");
                if (dayTableNames.length < 2) {
                    throw new CIServiceException("没有别名");
                }

                lastAlias = dayTableNames[1];
                if (tableNameSet.size() > 1) {
                    while (it.hasNext()) {
                        String tableNames = (String) it.next();
                        if (!tableNames.equals(dayTableName)) {
                            String[] tableName = tableNames.split(" ");
                            if (tableName.length < 2) {
                                throw new CIServiceException("没有别名");
                            }

                            joinSb.append("left join ").append(tableNames).append(" on ").append(lastAlias).append(".").append(relatedColumn).append(" = ").append(tableName[1]).append(".").append(relatedColumn).append(" ");
                            lastAlias = tableName[1];
                            ++i;
                        }
                    }
                }
            }

            return joinSb.toString();
        } else {
            throw new CIServiceException("没有需要的表");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public int queryCount(String countSql) throws CIServiceException {
        boolean num = false;

        try {
            String e = Configure.getInstance().getProperty("CYCLE_CUSTOM_WEBSERVICE");
            String cityId = PrivilegeServiceUtil.getCityIdFromSession();
            int num1;
            if (e != null && StringUtil.isNotEmpty(e) && e.equalsIgnoreCase("true") && StringUtil.isNotEmpty(cityId)) {
                CiTaskServerCityRel cityRel = this.ciTaskServerCityRelHDao.selectById(Integer.valueOf(cityId));
                String databaseUrl = "";
                if (cityRel != null && StringUtil.isNotEmpty(cityRel.getDatabseUrl())) {
                    databaseUrl = cityRel.getDatabseUrl();
                }

                String username = "";
                if (cityRel != null && StringUtil.isNotEmpty(cityRel.getUsername())) {
                    username = cityRel.getUsername();
                }

                String password = "";
                if (cityRel != null && StringUtil.isNotEmpty(cityRel.getPassword())) {
                    password = cityRel.getPassword();
                }

                num1 = this.customersJdbcDao.selectCountByJdbc(countSql, databaseUrl, username, password);
            } else {
                num1 = this.customersJdbcDao.selectCountByJdbc(countSql, "", "", "");
            }

            return num1;
        } catch (Exception var9) {
            this.log.error("查询总数异常", var9);
            throw new CIServiceException("查询总数异常", var9);
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public boolean queryValidateSql(String validateSql) throws CIServiceException {
        boolean flag = false;

        try {
            this.customersJdbcDao.selectValidate(validateSql);
            flag = true;
        } catch (Exception var4) {
            this.log.error("SQL验证未通过", var4);
        }

        return flag;
    }

    public boolean modifyCiCustomGroupInfoSimple(CiCustomGroupInfo ciCustomGroupInfo) {
        boolean flag = false;

        try {
            String e = ciCustomGroupInfo.getCreateUserId();
            CiCustomModifyHistory message1 = this.addCiCustomModifyHistory(ciCustomGroupInfo, e);
            Date date = new Date();
            message1.setModifyTime(date);
            ciCustomGroupInfo.setNewModifyTime(date);
            StringBuffer sceneIdsBuf = new StringBuffer("");
            String sceneIds = "";
            if (ciCustomGroupInfo.getSceneList() != null && ciCustomGroupInfo.getSceneList().size() > 0) {
                List _index = ciCustomGroupInfo.getSceneList();
                Iterator var10 = _index.iterator();

                while (var10.hasNext()) {
                    CiCustomSceneRel ciCustomSceneRel = (CiCustomSceneRel) var10.next();
                    ciCustomSceneRel.setModifyTime(date);
                    if (StringUtil.isNotEmpty(ciCustomSceneRel.getId().getSceneId()) && ciCustomSceneRel.getStatus() == 1) {
                        String sceneId = ciCustomSceneRel.getId().getSceneId();
                        sceneIdsBuf.append(sceneId).append(",");
                    }
                }
            }

            if (StringUtil.isNotEmpty(sceneIdsBuf)) {
                int _index1 = sceneIdsBuf.lastIndexOf(",");
                sceneIds = sceneIdsBuf.substring(0, _index1);
            }

            message1.setSceneIds(sceneIds);
            flag = this.addOrModifyCiCustomGroupInfoByOnlyLabel(ciCustomGroupInfo, message1, (List) null, (List) null, (List) null, true);
            return flag;
        } catch (Exception var12) {
            String message = "修改客户群错误";
            this.log.error(message, var12);
            throw new CIServiceException(message, var12);
        }
    }

    public boolean modifyCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo, List<CiLabelRule> ciLabelRuleList, CiTemplateInfo ciTemplateInfo, String userId, boolean isSaveTemplate, List<CiGroupAttrRel> ciGroupAttrRelList) throws CIServiceException {
        boolean flag = false;

        try {
            String e = ciCustomGroupInfo.getCustomGroupId();
            CiCustomModifyHistory message1 = this.addCiCustomModifyHistory(ciCustomGroupInfo, userId);
            Date date = new Date();
            message1.setModifyTime(date);
            ciCustomGroupInfo.setNewModifyTime(date);
            StringBuffer sceneIdsBuf = new StringBuffer("");
            String sceneIds = "";
            if (ciCustomGroupInfo.getSceneList() != null && ciCustomGroupInfo.getSceneList().size() > 0) {
                List dayFlag = ciCustomGroupInfo.getSceneList();
                Iterator dateLabelFlag = dayFlag.iterator();

                while (dateLabelFlag.hasNext()) {
                    CiCustomSceneRel monthFlag = (CiCustomSceneRel) dateLabelFlag.next();
                    monthFlag.setModifyTime(date);
                    if (StringUtil.isNotEmpty(monthFlag.getId().getSceneId()) && monthFlag.getStatus() == 1) {
                        String cache = monthFlag.getId().getSceneId();
                        sceneIdsBuf.append(cache).append(",");
                    }
                }
            }

            if (StringUtil.isNotEmpty(sceneIdsBuf)) {
                int dayFlag1 = sceneIdsBuf.lastIndexOf(",");
                sceneIds = sceneIdsBuf.substring(0, dayFlag1);
            }

            message1.setSceneIds(sceneIds);
            boolean dayFlag2 = false;
            boolean monthFlag1 = false;
            boolean dateLabelFlag1 = false;
            CacheBase cache1 = CacheBase.getInstance();
            if (ciLabelRuleList != null && ciLabelRuleList.size() > 0) {
                Iterator customType = ciLabelRuleList.iterator();

                label234:
                while (true) {
                    while (true) {
                        CiLabelRule ciCustomListInfoList;
                        CiLabelInfo tableNameList1;
                        do {
                            while (true) {
                                if (!customType.hasNext()) {
                                    break label234;
                                }

                                ciCustomListInfoList = (CiLabelRule) customType.next();
                                if (2 == ciCustomListInfoList.getElementType().intValue()) {
                                    tableNameList1 = cache1.getEffectiveLabel(ciCustomListInfoList.getCalcuElement());
                                    break;
                                }

                                if (5 == ciCustomListInfoList.getElementType().intValue()) {
                                    Integer tableNameList = ciCustomListInfoList.getLabelFlag();
                                    if (tableNameList != null) {
                                        if (tableNameList.intValue() == 2) {
                                            dayFlag2 = true;
                                        }

                                        if (tableNameList.intValue() == 1) {
                                            monthFlag1 = true;
                                        }
                                    }
                                }
                            }
                        } while (tableNameList1 == null);

                        if (tableNameList1.getUpdateCycle().intValue() == 1) {
                            dayFlag2 = true;
                        }

                        if (tableNameList1.getUpdateCycle().intValue() == 2) {
                            monthFlag1 = true;
                        }

                        if (6 == tableNameList1.getLabelTypeId().intValue() && ciCustomListInfoList.getIsNeedOffset() != null && ciCustomListInfoList.getIsNeedOffset().intValue() == ServiceConstants.IS_NEED_OFFSET_YES) {
                            dateLabelFlag1 = true;
                        } else if (8 == tableNameList1.getLabelTypeId().intValue()) {
                            List listInfoDataDate = ciCustomListInfoList.getChildCiLabelRuleList();
                            if (listInfoDataDate != null) {
                                Iterator newAttr = listInfoDataDate.iterator();

                                while (newAttr.hasNext()) {
                                    CiLabelRule ciCustomListInfo = (CiLabelRule) newAttr.next();
                                    if (6 == ciCustomListInfo.getLabelTypeId().intValue() && ciCustomListInfo.getIsNeedOffset().intValue() == ServiceConstants.IS_NEED_OFFSET_YES) {
                                        dateLabelFlag1 = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (!dayFlag2) {
                ciCustomGroupInfo.setDayLabelDate((String) null);
            }

            if (!monthFlag1) {
                ciCustomGroupInfo.setMonthLabelDate((String) null);
            }

            if (dateLabelFlag1) {
                if (ciCustomGroupInfo.getUpdateCycle().intValue() == 2) {
                    ciCustomGroupInfo.setOffsetDate(cache1.getNewLabelMonth());
                } else {
                    ciCustomGroupInfo.setOffsetDate(cache1.getNewLabelDay());
                }
            } else {
                ciCustomGroupInfo.setOffsetDate((String) null);
            }

            switch (ciCustomGroupInfo.getCreateTypeId().intValue()) {
                case 1:
                    ArrayList ciCustomListInfoList1 = new ArrayList();
                    if (ciCustomGroupInfo.getOldIsHasList().intValue() == 0 && ciCustomGroupInfo.getIsHasList().intValue() == 1) {
                        ciCustomGroupInfo.setDataStatus(Integer.valueOf(1));
                        String customType1;
                        String listInfoDataDate1;
                        if (dayFlag2) {
                            customType1 = cache1.getNewLabelDay();
                            if ("2".equals(ciCustomGroupInfo.getTacticsId())) {
                                listInfoDataDate1 = cache1.getNewLabelDay();
                                int ciCustomListInfo1 = 0;
                                if (StringUtil.isNotEmpty(listInfoDataDate1)) {
                                    ciCustomListInfo1 = Integer.valueOf(listInfoDataDate1).intValue();
                                }

                                int newAttr1 = 2147483647;
                                Iterator var24;
                                int attrSource;
                                String labelIdStr;
                                CiLabelInfo ciLabelInfo;
                                int updateCycle;
                                String labelDataDate;
                                int tempNum;
                                if (ciLabelRuleList != null && ciLabelRuleList.size() > 0) {
                                    var24 = ciLabelRuleList.iterator();

                                    while (var24.hasNext()) {
                                        CiLabelRule relId = (CiLabelRule) var24.next();
                                        if (!StringUtil.isNotEmpty(relId.getParentId())) {
                                            attrSource = relId.getElementType().intValue();
                                            if (attrSource == 2) {
                                                labelIdStr = relId.getCalcuElement();
                                                ciLabelInfo = cache1.getEffectiveLabel(labelIdStr);
                                                updateCycle = ciLabelInfo.getUpdateCycle().intValue();
                                                labelDataDate = ciLabelInfo.getDataDate();
                                                if (1 == updateCycle && StringUtil.isNotEmpty(labelDataDate) && ciCustomListInfo1 != 0) {
                                                    tempNum = Integer.valueOf(labelDataDate).intValue();
                                                    if (tempNum < ciCustomListInfo1 && newAttr1 > tempNum) {
                                                        newAttr1 = tempNum;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                if (ciGroupAttrRelList != null && ciGroupAttrRelList.size() > 0) {
                                    var24 = ciGroupAttrRelList.iterator();

                                    while (var24.hasNext()) {
                                        CiGroupAttrRel relId1 = (CiGroupAttrRel) var24.next();
                                        attrSource = relId1.getAttrSource();
                                        if (attrSource == 2) {
                                            labelIdStr = relId1.getLabelOrCustomId();
                                            ciLabelInfo = cache1.getEffectiveLabel(labelIdStr);
                                            updateCycle = ciLabelInfo.getUpdateCycle().intValue();
                                            labelDataDate = ciLabelInfo.getDataDate();
                                            if (1 == updateCycle && StringUtil.isNotEmpty(labelDataDate) && ciCustomListInfo1 != 0) {
                                                tempNum = Integer.valueOf(labelDataDate).intValue();
                                                if (tempNum < ciCustomListInfo1 && newAttr1 > tempNum) {
                                                    newAttr1 = tempNum;
                                                }
                                            }
                                        }
                                    }
                                }

                                if (newAttr1 != 2147483647) {
                                    customType1 = String.valueOf(newAttr1);
                                }
                            }
                        } else {
                            customType1 = null;
                        }

                        String tableNameList2;
                        if (monthFlag1) {
                            tableNameList2 = cache1.getNewLabelMonth();
                        } else {
                            tableNameList2 = null;
                        }

                        listInfoDataDate1 = ciCustomGroupInfo.getDataDate();
                        if (ciCustomGroupInfo.getUpdateCycle().intValue() == 3) {
                            if (customType1 != null) {
                                listInfoDataDate1 = customType1;
                            } else {
                                listInfoDataDate1 = cache1.getNewLabelDay();
                            }

                            ciCustomGroupInfo.setStartDate(ciCustomGroupInfo.getStartDate());
                        } else {
                            if (tableNameList2 != null) {
                                listInfoDataDate1 = tableNameList2;
                            } else {
                                listInfoDataDate1 = cache1.getNewLabelMonth();
                            }

                            ciCustomGroupInfo.setStartDate(ciCustomGroupInfo.getStartDate());
                        }

                        CiCustomListInfo ciCustomListInfo2 = this.newCiCustomListInfo(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList, listInfoDataDate1, tableNameList2, customType1);
                        ciCustomGroupInfo.setDayLabelDate(ciCustomListInfo2.getDayLabelDate());
                        ciCustomGroupInfo.setMonthLabelDate(ciCustomListInfo2.getMonthLabelDate());
                        ciCustomListInfoList1.add(ciCustomListInfo2);
                        if (ciGroupAttrRelList == null || ciGroupAttrRelList.size() == 0) {
                            CiGroupAttrRel newAttr2 = new CiGroupAttrRel();
                            CiGroupAttrRelId relId2 = new CiGroupAttrRelId();
                            relId2.setCustomGroupId(e);
                            relId2.setAttrCol("ATTR_COL");
                            relId2.setModifyTime(new Date());
                            newAttr2.setId(relId2);
                            newAttr2.setStatus(Integer.valueOf(2));
                            ciGroupAttrRelList.add(newAttr2);
                        }
                    }

                    if (1 == ciCustomGroupInfo.getUpdateCycle().intValue() && ciCustomGroupInfo.getOldIsHasList().intValue() != 0) {
                        if (isSaveTemplate) {
                            this.saveTemplate(ciLabelRuleList, ciCustomGroupInfo, ciTemplateInfo, userId);
                        }

                        flag = this.addOrModifyCiCustomGroupInfoByOnlyLabel(ciCustomGroupInfo, message1, ciCustomListInfoList1, (List) null, ciGroupAttrRelList, true);
                    } else {
                        if (isSaveTemplate) {
                            this.saveTemplate(ciLabelRuleList, ciCustomGroupInfo, ciTemplateInfo, userId);
                        }

                        byte customType2 = 1;
                        List tableNameList3 = this.queryAllValueTableNameByCustomerId(e, Integer.valueOf(customType2));
                        this.ciLabelRuleHDao.deleteCiLabelRuleListByCustomerOrTemplateId(e, Integer.valueOf(customType2));
                        this.deletePreUpdateGroupAttrRel(e, ciCustomGroupInfo.getDataDate());
                        this.ciGroupAttrRelService.updateCiGroupAttrRelStatusByGroupInfoId(e);
                        flag = this.addOrModifyCiCustomGroupInfoByOnlyLabel(ciCustomGroupInfo, message1, ciCustomListInfoList1, ciLabelRuleList, ciGroupAttrRelList, true);
                        if (flag) {
                            this.dropAllValueTableByTableList(tableNameList3);
                        }
                    }
                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                    flag = this.addOrModifyCiCustomGroupInfoByOnlyLabel(ciCustomGroupInfo, message1, (List) null, (List) null, (List) null, true);
            }

            return flag;
        } catch (Exception var31) {
            String message = "修改客户群错误";
            this.log.error(message, var31);
            throw new CIServiceException(message, var31);
        }
    }

    private void deletePreUpdateGroupAttrRel(String customGroupId, String customDataDate) {
        CiCustomListInfo listInfo = this.queryCiCustomListInfoByCGroupIdAndDataDate(customGroupId, customDataDate);
        if (listInfo != null) {
            Date dateTime = listInfo.getDataTime();
            List groupAttrRelList = this.ciGroupAttrRelService.queryGroupAttrRelList(customGroupId, dateTime);
            if (groupAttrRelList != null && groupAttrRelList.size() > 0) {
                CiGroupAttrRel attrRel = (CiGroupAttrRel) groupAttrRelList.get(0);
                Date attrModifyTime = attrRel.getId().getModifyTime();
                this.ciGroupAttrRelService.deleteGroupAttrRelList(customGroupId, attrModifyTime);
            }
        }

    }

    public boolean addCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo, List<CiLabelRule> ciLabelRuleList, List<CiCustomSourceRel> ciCustomSourceRelList, CiTemplateInfo ciTemplateInfo, String userId, boolean isSaveTemplate, List<CiGroupAttrRel> ciGroupAttrRelList) throws CIServiceException {
        boolean flag = false;

        try {
            String e = this.customGroupIdGen.getId(userId);
            this.log.debug("新建客户群生成的客户群ID：===" + e);
            ciCustomGroupInfo.setCustomGroupId(e);
            ciCustomGroupInfo.setIsFirstFailed(Integer.valueOf(0));
            if (StringUtil.isNotEmpty(ciCustomGroupInfo.getListCreateTime())) {
                ciCustomGroupInfo.setCustomListCreateFailed(Integer.valueOf(0));
            }

            ciCustomGroupInfo.setCreateUserId(userId);
            Date message1 = new Date();
            ciCustomGroupInfo.setCreateTime(message1);
            ciCustomGroupInfo.setNewModifyTime(message1);
            String createCityId = "";
            if (13 == ciCustomGroupInfo.getCreateTypeId().intValue()) {
                createCityId = ciCustomGroupInfo.getCreateCityId();
            } else {
                createCityId = PrivilegeServiceUtil.getCityIdFromSession();
            }

            ciCustomGroupInfo.setCreateCityId(createCityId);
            StringBuffer sceneIdsBuf = new StringBuffer("");
            String sceneIds = "";
            if (ciCustomGroupInfo.getSceneList() != null && ciCustomGroupInfo.getSceneList().size() > 0) {
                List hasLocalList = ciCustomGroupInfo.getSceneList();
                Iterator history = hasLocalList.iterator();

                while (history.hasNext()) {
                    CiCustomSceneRel centerCityId = (CiCustomSceneRel) history.next();
                    centerCityId.setModifyTime(message1);
                    centerCityId.getId().setCustomGroupId(e);
                    if (StringUtil.isNotEmpty(centerCityId.getId().getSceneId())) {
                        String dayFlag = centerCityId.getId().getSceneId();
                        sceneIdsBuf.append(dayFlag).append(",");
                    }
                }
            }

            if (StringUtil.isNotEmpty(sceneIdsBuf)) {
                int hasLocalList1 = sceneIdsBuf.lastIndexOf(",");
                sceneIds = sceneIdsBuf.substring(0, hasLocalList1);
            }

            boolean hasLocalList2 = false;
            String centerCityId1 = Configure.getInstance().getProperty("CENTER_CITYID");
            if (!centerCityId1.equals(createCityId) && ciLabelRuleList != null && ciLabelRuleList.size() != 0) {
                Iterator dayFlag1 = ciLabelRuleList.iterator();

                while (dayFlag1.hasNext()) {
                    CiLabelRule history1 = (CiLabelRule) dayFlag1.next();
                    if (5 == history1.getElementType().intValue()) {
                        CiCustomGroupInfo monthFlag = this.customersJdbcDao.queryCiCustomGroupInfoByListInfoId(history1.getCalcuElement());
                        if (createCityId.equals(monthFlag.getCreateCityId())) {
                            hasLocalList2 = true;
                            break;
                        }
                    }
                }
            }

            if (hasLocalList2) {
                ciCustomGroupInfo.setIsContainLocalList(Integer.valueOf(1));
            } else {
                ciCustomGroupInfo.setIsContainLocalList(Integer.valueOf(0));
            }

            ciCustomGroupInfo.setIsSysRecom(Integer.valueOf(ServiceConstants.IS_NOT_SYS_RECOM));
            CiCustomModifyHistory history2 = this.addCiCustomModifyHistory(ciCustomGroupInfo, userId);
            history2.setModifyTime(message1);
            history2.setSceneIds(sceneIds);
            boolean dayFlag2 = false;
            boolean monthFlag1 = false;
            boolean dateLabelFlag = false;
            List customListInfoList;
            if (ciLabelRuleList != null && ciLabelRuleList.size() > 0) {
                CacheBase serverId = CacheBase.getInstance();
                Iterator ciCustomListInfo = ciLabelRuleList.iterator();

                label179:
                while (true) {
                    while (true) {
                        CiLabelRule ciCustomListInfoList;
                        CiLabelInfo customListInfo1;
                        do {
                            while (true) {
                                if (!ciCustomListInfo.hasNext()) {
                                    break label179;
                                }

                                ciCustomListInfoList = (CiLabelRule) ciCustomListInfo.next();
                                if (2 == ciCustomListInfoList.getElementType().intValue()) {
                                    customListInfo1 = serverId.getEffectiveLabel(ciCustomListInfoList.getCalcuElement());
                                    break;
                                }

                                if (5 == ciCustomListInfoList.getElementType().intValue()) {
                                    Integer customListInfo = ciCustomListInfoList.getLabelFlag();
                                    if (customListInfo != null) {
                                        if (customListInfo.intValue() == 2) {
                                            dayFlag2 = true;
                                        }

                                        if (customListInfo.intValue() == 1) {
                                            monthFlag1 = true;
                                        }
                                    }
                                }
                            }
                        } while (customListInfo1 == null);

                        if (customListInfo1.getUpdateCycle().intValue() == 1) {
                            dayFlag2 = true;
                        }

                        if (customListInfo1.getUpdateCycle().intValue() == 2) {
                            monthFlag1 = true;
                        }

                        if (6 == customListInfo1.getLabelTypeId().intValue() && ciCustomListInfoList.getIsNeedOffset().intValue() == ServiceConstants.IS_NEED_OFFSET_YES) {
                            dateLabelFlag = true;
                        } else if (8 == customListInfo1.getLabelTypeId().intValue()) {
                            customListInfoList = ciCustomListInfoList.getChildCiLabelRuleList();
                            if (customListInfoList != null) {
                                Iterator tableName = customListInfoList.iterator();

                                while (tableName.hasNext()) {
                                    CiLabelRule implClassName = (CiLabelRule) tableName.next();
                                    if (6 == implClassName.getLabelTypeId().intValue() && implClassName.getIsNeedOffset().intValue() == ServiceConstants.IS_NEED_OFFSET_YES) {
                                        dateLabelFlag = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (!dayFlag2) {
                ciCustomGroupInfo.setDayLabelDate((String) null);
            }

            if (!monthFlag1) {
                ciCustomGroupInfo.setMonthLabelDate((String) null);
            }

            if (dateLabelFlag) {
                ciCustomGroupInfo.setOffsetDate(ciCustomGroupInfo.getDataDate());
            }

            switch (ciCustomGroupInfo.getCreateTypeId().intValue()) {
                case 1:
                case 2:
                case 3:
                case 8:
                    if (ciCustomGroupInfo.getIsHasList().intValue() == 0) {
                        ciCustomGroupInfo.setDataStatus(Integer.valueOf(3));
                        ciCustomGroupInfo.setDayLabelDate((String) null);
                        ciCustomGroupInfo.setMonthLabelDate((String) null);
                    } else {
                        ciCustomGroupInfo.setDataStatus(Integer.valueOf(1));
                    }

                    String serverId1 = Configure.getInstance().getProperty("CURRENT_SERVER_ID");
                    if (StringUtil.isNotEmpty(serverId1)) {
                        ciCustomGroupInfo.setServerId(serverId1);
                    }

                    ciCustomGroupInfo.setStatus(Integer.valueOf(1));
                    ciCustomGroupInfo.setCustomNum((Long) null);
                    ciCustomGroupInfo.setDataTime((Date) null);
                    if (isSaveTemplate) {
                        this.saveTemplate(ciLabelRuleList, ciCustomGroupInfo, ciTemplateInfo, userId);
                    }

                    List ciCustomListInfoList1 = this.newCiCustomListInfoList(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList);
                    if (ciGroupAttrRelList == null || ciGroupAttrRelList.size() == 0) {
                        CiGroupAttrRel ciCustomListInfo2 = new CiGroupAttrRel();
                        CiGroupAttrRelId customListInfo3 = new CiGroupAttrRelId();
                        customListInfo3.setCustomGroupId(ciCustomGroupInfo.getCustomGroupId());
                        customListInfo3.setAttrCol("ATTR_COL");
                        customListInfo3.setModifyTime(new Date());
                        ciCustomListInfo2.setId(customListInfo3);
                        ciCustomListInfo2.setStatus(Integer.valueOf(2));
                        ciGroupAttrRelList.add(ciCustomListInfo2);
                    }

                    flag = this.addOrModifyCiCustomGroupInfoByOnlyLabel(ciCustomGroupInfo, history2, ciCustomListInfoList1, ciLabelRuleList, ciGroupAttrRelList, false);
                    break;
                case 4:
                    CiCustomListInfo ciCustomListInfo1 = this.newCiCustomListInfo(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList);
                    flag = this.addCiCustomGroupInfoWithCustomer(ciCustomGroupInfo, history2, ciLabelRuleList, (List) null, ciCustomListInfo1);
                    break;
                case 5:
                case 6:
                case 9:
                    CiCustomListInfo customListInfo2 = this.newCiCustomListInfo(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList);
                    flag = this.addCiCustomGroupInfoWithCustomer(ciCustomGroupInfo, history2, ciLabelRuleList, ciCustomSourceRelList, customListInfo2);
                    break;
                case 7:
                case 10:
                case 11:
                    ciCustomGroupInfo.setDataStatus(Integer.valueOf(1));
                    ciCustomGroupInfo.setStatus(Integer.valueOf(1));
                    ciCustomGroupInfo.setCustomNum((Long) null);
                    ciCustomGroupInfo.setDataTime((Date) null);
                    ciCustomGroupInfo.setUpdateCycle(Integer.valueOf(1));
                    if (!centerCityId1.equals(createCityId)) {
                        ciCustomGroupInfo.setIsContainLocalList(Integer.valueOf(1));
                    }

                    history2.setUpdateCycle(Integer.valueOf(1));
                    customListInfoList = this.newCiCustomListInfoList(ciCustomGroupInfo, (List) null, (List) null);
                    flag = this.addOrModifyCiCustomGroupInfoByOnlyLabel(ciCustomGroupInfo, history2, customListInfoList, (List) null, ciGroupAttrRelList, false);
                    break;
                case 12:
                    ciCustomGroupInfo.setDataStatus(Integer.valueOf(1));
                    ciCustomGroupInfo.setStatus(Integer.valueOf(1));
                    ciCustomGroupInfo.setCustomNum((Long) null);
                    ciCustomGroupInfo.setDataTime((Date) null);
                    ciCustomGroupInfo.setIsHasList(Integer.valueOf(1));
                    if (!centerCityId1.equals(createCityId)) {
                        ciCustomGroupInfo.setIsContainLocalList(Integer.valueOf(1));
                    }

                    history2.setUpdateCycle(ciCustomGroupInfo.getUpdateCycle());
                    String implClassName1 = Configure.getInstance().getProperty("IMPORT_BY_TABLE_CLASS_NAME");
                    String tableName1 = ciCustomGroupInfo.getTabelName();
                    String customListInfoListAuto1;
                    if (!StringUtil.isNotEmpty(implClassName1) || !StringUtil.isNotEmpty(tableName1)) {
                        customListInfoListAuto1 = "";
                        if (StringUtil.isEmpty(implClassName1)) {
                            customListInfoListAuto1 = "导入表创建客户群service实现类名称未配置！";
                        } else if (StringUtil.isEmpty(tableName1)) {
                            customListInfoListAuto1 = "数据来源表为空！";
                        }

                        this.log.error(customListInfoListAuto1);
                        throw new CIServiceException(customListInfoListAuto1);
                    }

                    customListInfoListAuto1 = ciCustomGroupInfo.getWhereSql();
                    this.log.debug("表导入创建客户群的whereSql：" + customListInfoListAuto1);
                    flag = this.addOrModifyCiCustomGroupInfoByOnlyLabel(ciCustomGroupInfo, history2, (List) null, (List) null, ciGroupAttrRelList, false);
                    if (flag) {
                        IImportTableService importService = (IImportTableService) SystemServiceLocator.getInstance().getService(implClassName1);
                        flag = importService.importTable(ciCustomGroupInfo, tableName1, customListInfoListAuto1, ciGroupAttrRelList, userId);
                    }
                    break;
                case 13:
                    ciCustomGroupInfo.setDataStatus(Integer.valueOf(1));
                    ciCustomGroupInfo.setStatus(Integer.valueOf(1));
                    ciCustomGroupInfo.setCustomNum((Long) null);
                    ciCustomGroupInfo.setDataTime((Date) null);
                    ciCustomGroupInfo.setUpdateCycle(Integer.valueOf(1));
                    if (!centerCityId1.equals(createCityId)) {
                        ciCustomGroupInfo.setIsContainLocalList(Integer.valueOf(1));
                    }

                    history2.setUpdateCycle(Integer.valueOf(1));
                    List customListInfoListAuto = this.newCiCustomListInfoList(ciCustomGroupInfo, (List) null, (List) null);
                    flag = this.addOrModifyCiCustomGroupInfoByOnlyLabel(ciCustomGroupInfo, history2, customListInfoListAuto, ciLabelRuleList, ciGroupAttrRelList, false);
            }

            return flag;
        } catch (Exception var29) {
            String message = "创建客户群错误";
            this.log.error(message, var29);
            throw new CIServiceException(message, var29);
        }
    }

    private boolean addCiCustomGroupInfoWithCustomer(CiCustomGroupInfo ciCustomGroupInfo, CiCustomModifyHistory ciCustomModifyHistory, List<CiLabelRule> ciLabelRuleList, List<CiCustomSourceRel> ciCustomSourceRelList, CiCustomListInfo ciCustomListInfo) throws CIServiceException {
        boolean flag = false;

        String message;
        try {
            ciCustomGroupInfo.setDataStatus(Integer.valueOf(1));
            this.ciCustomGroupInfoHDao.insertCustomGroup(ciCustomGroupInfo);
            String e = ciCustomGroupInfo.getCustomGroupId();
            if (ciCustomModifyHistory != null && StringUtil.isNotEmpty(ciCustomModifyHistory.getCustomGroupName())) {
                ciCustomModifyHistory.setCustomGroupId(e);
                this.ciCustomModifyHistoryHDao.insertCiCustomModifyHistory(ciCustomModifyHistory);
            }

            if (ciCustomListInfo != null && StringUtil.isNotEmpty(ciCustomListInfo.getListTableName())) {
                ciCustomListInfo.setCustomGroupId(e);
                this.ciCustomListInfoHDao.insertCiCustomListInfo(ciCustomListInfo);
            }

            if (ciLabelRuleList != null && ciLabelRuleList.size() != 0) {
                message = ciCustomGroupInfo.getCreateUserId();
                Integer deptId = PrivilegeServiceUtil.getUserDeptId(message);
                Iterator var11 = ciLabelRuleList.iterator();

                while (var11.hasNext()) {
                    CiLabelRule rule = (CiLabelRule) var11.next();
                    rule.setRuleId((String) null);
                    rule.setCustomId(e);
                    rule.setCustomType(Integer.valueOf(1));
                    this.ciLabelRuleHDao.insertCiLabelRule(rule);
                    if (2 == rule.getElementType().intValue()) {
                        CiUserUseLabel ciUserUseLabel = new CiUserUseLabel();
                        CiUserUseLabelId ciUserUseLabelId = new CiUserUseLabelId();
                        ciUserUseLabelId.setUserId(message);
                        ciUserUseLabelId.setDeptId("" + deptId);
                        ciUserUseLabelId.setLabelId(new Integer(rule.getCalcuElement()));
                        ciUserUseLabelId.setUseTime(new Date());
                        ciUserUseLabel.setId(ciUserUseLabelId);
                        ciUserUseLabel.setLabelUseTypeId(Integer.valueOf(2));
                        this.ciUserUseLabelHDao.insertOrUpdateCiUserUseLabel(ciUserUseLabel);
                    }
                }
            }

            if (ciCustomSourceRelList != null && ciCustomSourceRelList.size() != 0) {
                Iterator deptId1 = ciCustomSourceRelList.iterator();

                while (deptId1.hasNext()) {
                    CiCustomSourceRel message1 = (CiCustomSourceRel) deptId1.next();
                    message1.setIndexId((String) null);
                    message1.setCustomGroupId(e);
                }

                this.ciCustomSourceRelHDao.insertCiCustomSourceRelList(ciCustomSourceRelList);
            }

            this.genCusomerList(ciCustomGroupInfo);
            flag = true;
            return flag;
        } catch (Exception var14) {
            message = "保存客户群相关信息错误";
            this.log.error(message, var14);
            throw new CIServiceException(message, var14);
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )

    public void genCusomerList(CiCustomGroupInfo ciCustomGroupInfo, Boolean isPeriodCreateListTable) {
        //isPeriodCreateListTable 为false
        CustomerListCreaterThread creator = null;

        try {
            creator = (CustomerListCreaterThread) SystemServiceLocator.getInstance().getService("customerListCreaterThread");
            CiCustomGroupInfo e = ciCustomGroupInfo.clone();
            creator.setCiCustomGroupInfo(e);
            creator.setIsPeriodCreateListTable(isPeriodCreateListTable);
            creator.setNeedRecreateListFile(this.isNeedRecreateListFile(ciCustomGroupInfo));
            ThreadPool.getInstance().execute(creator, false, e.getCreateCityId());
        } catch (Exception var5) {
            this.log.error("线程池异常", var5);
        }

    }

    private boolean isNeedRecreateListFile(CiCustomGroupInfo ciCustomGroupInfo) {
        boolean result = false;
        Integer updateCycle = ciCustomGroupInfo.getUpdateCycle();
        if (1 == updateCycle.intValue()) {
            List listInfos = this.ciCustomListInfoHDao.selectByCustomGroupId(ciCustomGroupInfo.getCustomGroupId());
            Iterator var6 = listInfos.iterator();

            while (var6.hasNext()) {
                CiCustomListInfo info = (CiCustomListInfo) var6.next();
                if (info.getFileCreateStatus() != null && 3 == info.getFileCreateStatus().intValue()) {
                    result = true;
                    info.setFileCreateStatus(Integer.valueOf(2));
                    this.ciCustomListInfoHDao.updateCiCustomListInfo(info);
                }
            }
        }

        return result;
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public void genCusomerList(CiCustomGroupInfo ciCustomGroupInfo) {
        this.genCusomerList(ciCustomGroupInfo, Boolean.valueOf(false));
    }

    private void saveTemplate(List<CiLabelRule> ciLabelRuleList, CiCustomGroupInfo ciCustomGroupInfo, CiTemplateInfo ciTemplateInfo, String userId) throws Exception {
        ArrayList newLabelRuleList = new ArrayList();
        Iterator var7 = ciLabelRuleList.iterator();

        while (var7.hasNext()) {
            CiLabelRule rule = (CiLabelRule) var7.next();
            CiLabelRule newRule = rule.clone();
            newLabelRuleList.add(newRule);
        }

        ciTemplateInfo.setTemplateDesc(ciCustomGroupInfo.getCustomGroupDesc());
        ciTemplateInfo.setLabelOptRuleShow(ciCustomGroupInfo.getLabelOptRuleShow());
        this.ciTemplateInfoService.addTemplateInfo(ciTemplateInfo, newLabelRuleList, userId);
    }

    private boolean addOrModifyCiCustomGroupInfoByOnlyLabel(CiCustomGroupInfo ciCustomGroupInfo, CiCustomModifyHistory ciCustomModifyHistory, List<CiCustomListInfo> ciCustomListInfoList, List<CiLabelRule> ciLabelRuleList, List<CiGroupAttrRel> ciGroupAttrRelList, boolean isEdit) throws CIServiceException {
        boolean flag = false;
        try {
            labelRuleLock.lock();
            this.ciCustomGroupInfoHDao.insertCustomGroup(ciCustomGroupInfo);
            String toHasList = ciCustomGroupInfo.getCustomGroupId();
            if (ciCustomModifyHistory != null && StringUtil.isNotEmpty(ciCustomModifyHistory.getCustomGroupName())) {
                ciCustomModifyHistory.setCustomGroupId(toHasList);
                this.ciCustomModifyHistoryHDao.insertCiCustomModifyHistory(ciCustomModifyHistory);
            }

            if (ciCustomListInfoList != null && ciCustomListInfoList.size() > 0) {
                Iterator ciGroupAttrRel = ciCustomListInfoList.iterator();

                while (ciGroupAttrRel.hasNext()) {
                    CiCustomListInfo message1 = (CiCustomListInfo) ciGroupAttrRel.next();
                    message1.setCustomGroupId(toHasList);
                    this.ciCustomListInfoHDao.insertCiCustomListInfo(message1);
                }
            }

            if (ciLabelRuleList != null && ciLabelRuleList.size() != 0) {
                ArrayList message2 = new ArrayList();
                CacheBase ciGroupAttrRel1 = CacheBase.getInstance();

                CiLabelRule verticalRule;
                Iterator e;
                for (e = ciLabelRuleList.iterator(); e.hasNext(); this.ciLabelRuleHDao.insertCiLabelRule(verticalRule)) {
                    verticalRule = (CiLabelRule) e.next();
                    verticalRule.setRuleId((String) null);
                    verticalRule.setCustomId(toHasList);
                    verticalRule.setCustomType(Integer.valueOf(1));
                    int parentRuleId = verticalRule.getElementType().intValue();
                    if (parentRuleId == 2) {
                        String childRules = verticalRule.getCalcuElement();
                        CiLabelInfo childRule = ciGroupAttrRel1.getEffectiveLabel(childRules);
                        int labelTypeId = childRule.getLabelTypeId().intValue();
                        if (labelTypeId == 8) {
                            message2.add(verticalRule);
                        }

                        String labelTypeId1;
                        String[] exactValue;
                        String exactValueArr;
                        if (labelTypeId == 5) {
                            labelTypeId1 = verticalRule.getAttrVal();
                            if (StringUtils.isNotEmpty(labelTypeId1)) {
                                exactValue = labelTypeId1.split(",");
                                if (exactValue.length > 20) {
                                    try {
                                        exactValueArr = this.createValueTableName();
                                        verticalRule.setTableName(exactValueArr);
                                        this.addExactValueToTable(labelTypeId1, exactValueArr);
                                        verticalRule.setAttrVal((String) null);
                                    } catch (Exception var33) {
                                        this.log.error("枚举值存于数据库表中失败", var33);
                                    }
                                }
                            }
                        }

                        if (labelTypeId == 7) {
                            labelTypeId1 = verticalRule.getExactValue();
                            if (StringUtils.isNotEmpty(labelTypeId1)) {
                                exactValue = labelTypeId1.split(",");
                                if (exactValue.length > 20) {
                                    try {
                                        exactValueArr = this.createValueTableName();
                                        verticalRule.setTableName(exactValueArr);
                                        this.addExactValueToTable(labelTypeId1, exactValueArr);
                                        verticalRule.setExactValue((String) null);
                                    } catch (Exception var32) {
                                        this.log.error("精确值存于数据库表中失败", var32);
                                    }
                                }
                            }
                        }
                    }
                }

                e = message2.iterator();

                while (e.hasNext()) {
                    verticalRule = (CiLabelRule) e.next();
                    String parentRuleId1 = verticalRule.getRuleId();
                    List childRules1 = verticalRule.getChildCiLabelRuleList();

                    CiLabelRule childRule1;
                    for (Iterator labelTypeId2 = childRules1.iterator(); labelTypeId2.hasNext(); this.ciLabelRuleHDao.insertCiLabelRule(childRule1)) {
                        childRule1 = (CiLabelRule) labelTypeId2.next();
                        childRule1.setRuleId((String) null);
                        childRule1.setParentId(parentRuleId1);
                        childRule1.setCustomId(toHasList);
                        childRule1.setCustomType(Integer.valueOf(1));
                        int labelTypeId3 = childRule1.getLabelTypeId().intValue();
                        String e1;
                        String exactValue1;
                        String[] exactValueArr1;
                        if (labelTypeId3 == 5) {
                            exactValue1 = childRule1.getAttrVal();
                            if (StringUtils.isNotEmpty(exactValue1)) {
                                exactValueArr1 = exactValue1.split(",");
                                if (exactValueArr1.length > 20) {
                                    try {
                                        e1 = this.createValueTableName();
                                        childRule1.setTableName(e1);
                                        this.addExactValueToTable(exactValue1, e1);
                                        childRule1.setAttrVal((String) null);
                                    } catch (Exception var31) {
                                        this.log.error("枚举值存于数据库表中失败", var31);
                                    }
                                }
                            }
                        }

                        if (labelTypeId3 == 7) {
                            exactValue1 = childRule1.getExactValue();
                            if (StringUtils.isNotEmpty(exactValue1)) {
                                exactValueArr1 = exactValue1.split(",");
                                if (exactValueArr1.length > 20) {
                                    try {
                                        e1 = this.createValueTableName();
                                        childRule1.setTableName(e1);
                                        this.addExactValueToTable(exactValue1, e1);
                                        childRule1.setExactValue((String) null);
                                    } catch (Exception var30) {
                                        this.log.error("精确值存于数据库表中失败", var30);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Date message3 = new Date();
            if (ciGroupAttrRelList != null && ciGroupAttrRelList.size() > 0) {
                Iterator verticalRule1 = ciGroupAttrRelList.iterator();

                while (verticalRule1.hasNext()) {
                    CiGroupAttrRel ciGroupAttrRel2 = (CiGroupAttrRel) verticalRule1.next();

                    try {
                        ciGroupAttrRel2.getId().setCustomGroupId(toHasList);
                        ciGroupAttrRel2.getId().setModifyTime(message3);
                        this.ciGroupAttrRelHDao.insertCiGroupAttrRelHDao(ciGroupAttrRel2);
                    } catch (Exception var29) {
                        this.log.error("保存客户群属性错误", var29);
                    }
                }
            }

            flag = true;
        } catch (Exception var34) {
            String message = "保存客户群相关信息错误";
            this.log.error(message, var34);
            throw new CIServiceException(message, var34);
        } finally {
            labelRuleLock.unlock();
        }

        boolean toHasList1 = false;
        if (ciCustomGroupInfo.getOldIsHasList() != null && ciCustomGroupInfo.getOldIsHasList().intValue() == 0 && ciCustomGroupInfo.getIsHasList() != ciCustomGroupInfo.getOldIsHasList()) {
            toHasList1 = true;
        }

        if ((ciCustomGroupInfo.getIsHasList() != null && ciCustomGroupInfo.getIsHasList().intValue() == 1 && !isEdit || toHasList1) && StringUtil.isEmpty(ciCustomGroupInfo.getListCreateTime())) {
            this.genCusomerList(ciCustomGroupInfo);
        }

        return flag;
    }

    private CiCustomListInfo newCiCustomListInfo(CiCustomGroupInfo ciCustomGroupInfo, List<CiLabelRule> ciLabelRuleList, List<CiGroupAttrRel> ciGroupAttrRelList) {
        CiCustomListInfo ciCustomListInfo = new CiCustomListInfo();
        String tabName = Configure.getInstance().getProperty("CUST_LIST_TMP_TABLE");
        tabName = tabName.replace("YYMMDDHHMISSTTTTTT", "") + CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L);
        ciCustomListInfo.setListTableName(tabName);
        ciCustomListInfo.setDataDate(ciCustomGroupInfo.getDataDate());
        ciCustomListInfo.setDataTime(new Date());
        ciCustomListInfo.setDataStatus(Integer.valueOf(1));
        ciCustomListInfo.setDayLabelDate(ciCustomGroupInfo.getDayLabelDate());
        ciCustomListInfo.setListMaxNum(ciCustomGroupInfo.getListMaxNum());
        ReviseDateModel reviseDateModel = this.queryReviseDate(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList, ciCustomGroupInfo.getMonthLabelDate());
        if (reviseDateModel != null && "2".equals(reviseDateModel.getReviseType())) {
            ciCustomListInfo.setMonthLabelDate(reviseDateModel.getReviseMonth());
        } else if (reviseDateModel != null && "4".equals(reviseDateModel.getReviseType())) {
            ciCustomListInfo.setMonthLabelDate(ciCustomGroupInfo.getMonthLabelDate());
        }

        return ciCustomListInfo;
    }

    private List<CiCustomListInfo> newCiCustomListInfoList(CiCustomGroupInfo ciCustomGroupInfo, List<CiLabelRule> ciLabelRuleList, List<CiGroupAttrRel> ciGroupAttrRelList) {
        CacheBase cache = CacheBase.getInstance();
        ArrayList list = new ArrayList();
        if (1 == ciCustomGroupInfo.getUpdateCycle().intValue()) {
            CiCustomListInfo startDate = this.newCiCustomListInfo(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList, ciCustomGroupInfo.getDataDate(), ciCustomGroupInfo.getMonthLabelDate(), ciCustomGroupInfo.getDayLabelDate());
            if ("2".equals(ciCustomGroupInfo.getTacticsId())) {
                this.reviseDayLabelDate(startDate, ciLabelRuleList, ciGroupAttrRelList);
            }

            list.add(startDate);
        } else {
            SimpleDateFormat yyyyMMddDateFormat;
            String startDateStr;
            String newestDate;
            Date newest;
            CiCustomListInfo i;
            int j;
            String result;
            CiCustomListInfo ciCustomListInfo;
            Date var17;
            byte var18;
            if (2 == ciCustomGroupInfo.getUpdateCycle().intValue()) {
                var17 = ciCustomGroupInfo.getStartDate();
                yyyyMMddDateFormat = new SimpleDateFormat("yyyyMM");
                startDateStr = yyyyMMddDateFormat.format(var17);
                newestDate = cache.getNewLabelMonth();
                newest = var17;

                try {
                    newest = yyyyMMddDateFormat.parse(newestDate);
                } catch (ParseException var16) {
                    this.log.error("parse newestDate error " + newestDate);
                }

                if (startDateStr.equals(newestDate)) {
                    i = this.newCiCustomListInfo(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList, startDateStr, ciCustomGroupInfo.getMonthLabelDate(), ciCustomGroupInfo.getDayLabelDate());
                    list.add(i);
                } else if (var17.before(newest)) {
                    var18 = 0;
                    j = 0;

                    while (!startDateStr.equals(newestDate) && StringUtils.isNotEmpty(newestDate)) {
                        newestDate = DateUtil.getOffsetDateByDate(newestDate, var18, 0);
                        result = this.validateLabelDataDate(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList, newestDate, ciCustomGroupInfo.getDayLabelDate());
                        if ("2".equals(result)) {
                            ciCustomListInfo = this.newCiCustomListInfo(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList, newestDate, newestDate, ciCustomGroupInfo.getDayLabelDate());
                            list.add(ciCustomListInfo);
                        }

                        var18 = -1;
                        ++j;
                        if (j > 100) {
                            break;
                        }
                    }
                }
            } else if (3 == ciCustomGroupInfo.getUpdateCycle().intValue()) {
                var17 = ciCustomGroupInfo.getStartDate();
                yyyyMMddDateFormat = new SimpleDateFormat("yyyyMMdd");
                startDateStr = yyyyMMddDateFormat.format(var17);
                newestDate = cache.getNewLabelDay();
                newest = var17;

                try {
                    newest = yyyyMMddDateFormat.parse(newestDate);
                } catch (ParseException var15) {
                    this.log.error("parse newestDate error " + newestDate);
                }

                if (startDateStr.equals(newestDate)) {
                    i = this.newCiCustomListInfo(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList, startDateStr, ciCustomGroupInfo.getMonthLabelDate(), ciCustomGroupInfo.getDayLabelDate());
                    list.add(i);
                } else if (var17.before(newest)) {
                    var18 = 0;
                    j = 0;

                    while (!startDateStr.equals(newestDate) && StringUtils.isNotEmpty(newestDate)) {
                        newestDate = DateUtil.getOffsetDateByDate(newestDate, var18, 1);
                        result = this.validateLabelDataDate(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList, ciCustomGroupInfo.getMonthLabelDate(), newestDate);
                        if ("2".equals(result)) {
                            ciCustomListInfo = this.newCiCustomListInfo(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList, newestDate, ciCustomGroupInfo.getMonthLabelDate(), newestDate);
                            list.add(ciCustomListInfo);
                        }

                        var18 = -1;
                        ++j;
                        if (j > 1000) {
                            break;
                        }
                    }
                }
            }
        }

        this.log.debug("list.size():=====" + list.size());
        return list;
    }

    private CiCustomListInfo newCiCustomListInfo(CiCustomGroupInfo ciCustomGroupInfo, List<CiLabelRule> ciLabelRuleList, List<CiGroupAttrRel> ciGroupAttrRelList, String dataDate, String month, String day) {
        CiCustomListInfo ciCustomListInfo = new CiCustomListInfo();
        String tabName = Configure.getInstance().getProperty("CUST_LIST_TMP_TABLE");

        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException var10) {
            this.log.error("生成CiCustomListInfo对象出错");
            var10.printStackTrace();
        }

        tabName = tabName.replace("YYMMDDHHMISSTTTTTT", "") + CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L);
        ciCustomListInfo.setListTableName(tabName);
        ciCustomListInfo.setDataDate(dataDate);
        ciCustomListInfo.setDataTime(new Date());
        ciCustomListInfo.setDataStatus(Integer.valueOf(1));
        ciCustomListInfo.setDayLabelDate(day);
        ciCustomListInfo.setListMaxNum(ciCustomGroupInfo.getListMaxNum());
        ReviseDateModel reviseDateModel = this.queryReviseDate(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList, month);
        if (reviseDateModel != null && "2".equals(reviseDateModel.getReviseType())) {
            ciCustomListInfo.setMonthLabelDate(reviseDateModel.getReviseMonth());
        } else if (reviseDateModel != null && "4".equals(reviseDateModel.getReviseType())) {
            ciCustomListInfo.setMonthLabelDate(month);
        }

        return ciCustomListInfo;
    }

    private CiCustomModifyHistory addCiCustomModifyHistory(CiCustomGroupInfo ciCustomGroupInfo, String userId) throws CIServiceException {
        CiCustomModifyHistory ciCustomModifyHistory = null;

        try {
            ciCustomModifyHistory = new CiCustomModifyHistory();
            String e = ciCustomGroupInfo.getCustomGroupId();
            if (StringUtil.isNotEmpty(e)) {
                ciCustomModifyHistory.setCustomGroupId(e);
            }

            ciCustomModifyHistory.setModifyUserId(userId);
            ciCustomModifyHistory.setCustomGroupName(ciCustomGroupInfo.getCustomGroupName());
            ciCustomModifyHistory.setCustomGroupDesc(ciCustomGroupInfo.getCustomGroupDesc());
            ciCustomModifyHistory.setParentCustomId(ciCustomGroupInfo.getParentCustomId());
            ciCustomModifyHistory.setTemplateId(ciCustomGroupInfo.getTemplateId());
            ciCustomModifyHistory.setUpdateCycle(ciCustomGroupInfo.getUpdateCycle());
            ciCustomModifyHistory.setProdOptRuleShow(ciCustomGroupInfo.getProdOptRuleShow());
            ciCustomModifyHistory.setCustomOptRuleShow(ciCustomGroupInfo.getCustomOptRuleShow());
            ciCustomModifyHistory.setLabelOptRuleShow(ciCustomGroupInfo.getLabelOptRuleShow());
            ciCustomModifyHistory.setKpiDiffRule(ciCustomGroupInfo.getKpiDiffRule());
            ciCustomModifyHistory.setStartDate(ciCustomGroupInfo.getStartDate());
            ciCustomModifyHistory.setEndDate(ciCustomGroupInfo.getEndDate());
            ciCustomModifyHistory.setIsPrivate(ciCustomGroupInfo.getIsPrivate());
            ciCustomModifyHistory.setCreateCityId(ciCustomGroupInfo.getCreateCityId());
            ciCustomModifyHistory.setIsSysRecom(ciCustomGroupInfo.getIsSysRecom());
            ciCustomModifyHistory.setIsContainLocalList(ciCustomGroupInfo.getIsContainLocalList());
            ciCustomModifyHistory.setIsHasList(ciCustomGroupInfo.getIsHasList());
            ciCustomModifyHistory.setDayLabelDate(ciCustomGroupInfo.getDayLabelDate());
            ciCustomModifyHistory.setMonthLabelDate(ciCustomGroupInfo.getMonthLabelDate());
            ciCustomModifyHistory.setTacticsId(ciCustomGroupInfo.getTacticsId());
            ciCustomModifyHistory.setListMaxNum(ciCustomGroupInfo.getListMaxNum());
            return ciCustomModifyHistory;
        } catch (Exception var6) {
            String message = "封装客户群历史记录对象错误";
            this.log.error(message, var6);
            throw new CIServiceException(message, var6);
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public int queryCiCustomGroupCalcCount(String calcExpr, Map<String, String> labelRuleToSql, List<String> ciCustomGroupInfoIds, CiCustomGroupInfo ciCustomGroupInfo) throws CIServiceException {
        this.log.debug("countCiCustomGroupCalc:calcExpr=" + calcExpr + "ciCustomGroupInfoIds=" + ciCustomGroupInfoIds);

        try {
            if (ciCustomGroupInfoIds != null && ciCustomGroupInfoIds.size() != 0) {
                Iterator var6 = ciCustomGroupInfoIds.iterator();

                String e;
                while (var6.hasNext()) {
                    e = (String) var6.next();
                    List infoList = this.queryCiCustomListInfoByCGroupId(e);
                    CiCustomListInfo defaultInfo = null;
                    CiCustomListInfo dateEqlInfo = null;
                    Iterator var11 = infoList.iterator();

                    while (var11.hasNext()) {
                        CiCustomListInfo info = (CiCustomListInfo) var11.next();
                        if (3 == info.getDataStatus().intValue()) {
                            if (defaultInfo == null) {
                                defaultInfo = info;
                            }

                            if (ciCustomGroupInfo.getDataDate().equals(info.getDataDate())) {
                                dateEqlInfo = info;
                            }
                        }
                    }

                    if (dateEqlInfo != null) {
                        calcExpr = calcExpr.replace(e, dateEqlInfo.getListTableName());
                    } else if (defaultInfo != null) {
                        calcExpr = calcExpr.replace(e, defaultInfo.getListTableName());
                    }
                }

                if (ciCustomGroupInfoIds.size() == 1) {
                    CiCustomListInfo e1 = new CiCustomListInfo();
                    calcExpr = calcExpr.replace(String.valueOf('('), "").replace(String.valueOf(')'), "");
                    e1.setListTableName(calcExpr);
                    return this.queryCustomerListCount(e1);
                } else {
                    e = (new GroupCalcSqlPaser(Configure.getInstance().getProperty("CI_BACK_DBTYPE"))).parseExprToSql(calcExpr, labelRuleToSql);
                    e = " select count(*) from (" + e + ") abc ";
                    this.log.debug(" calc sql:" + e);
                    return this.ciCustomListInfoJDao.selectBackCountBySql(e);
                }
            } else {
                return 0;
            }
        } catch (Exception var12) {
            this.log.error("queryCiCustomGroupCalcCount errorcountCiCustomGroupCalc:calcExpr=" + calcExpr + "ciCustomGroupInfoIds=" + ciCustomGroupInfoIds, var12);
            throw new CIServiceException("计算客户群运算（交，并，差）的数量错误");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public CiCustomListInfo queryCiCustomListInfoById(String listTableName) throws CIServiceException {
        return this.ciCustomListInfoHDao.selectById(listTableName);
    }

    public void saveCiCustomSourceRel(List<CiCustomSourceRel> ciCustomSourceRels) throws CIServiceException {
        try {
            String e = null;
            if (ciCustomSourceRels.size() > 0) {
                e = ((CiCustomSourceRel) ciCustomSourceRels.get(0)).getCustomGroupId();
                this.ciCustomSourceRelHDao.deleteByCustomGroupId(e);
                this.ciCustomSourceRelHDao.insertCiCustomSourceRelList(ciCustomSourceRels);
            }

        } catch (Exception var3) {
            this.log.error("保存客户群运算条件报错，" + ciCustomSourceRels, var3);
            throw new CIServiceException("保存客户群运算条件报错");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public int queryCustomersListInfoCount(CiCustomListInfo bean) throws CIServiceException {
        boolean customersListInfoCount = false;

        try {
            int customersListInfoCount1 = this.ciCustomListInfoJDao.selectCustomersListInfoCount(bean);
            return customersListInfoCount1;
        } catch (Exception var4) {
            this.log.error("查询客户群清单数量报错，" + bean, var4);
            throw new CIServiceException("查询客户群清单数量报错");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiCustomListInfo> queryCustomersListInfo(int currPage, int pageSize, CiCustomListInfo bean) throws CIServiceException {
        return this.ciCustomListInfoJDao.selectCustomersListInfo(currPage, pageSize, bean);
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiLabelRule> queryCiLabelRuleList(String id, Integer customType) throws CIServiceException {
        String message = "";
        log.info("客户群id和customType" + id + customType);
        if (!StringUtil.isEmpty(id) && (1 == customType.intValue() || 2 == customType.intValue())) {
            List ciLabelRuleList = null;
            ArrayList resultRuleList = null;

            try {
                CacheBase e = CacheBase.getInstance();
                CiCustomGroupInfo ciCustomGroupInfoObj = this.ciCustomGroupInfoHDao.selectCustomGroupById(id);
                ciLabelRuleList = this.ciLabelRuleHDao.selectCiLabelRuleList(id, customType);
                log.info("ciLabelRuleList大小" + ciLabelRuleList.size() + "\n" + ciLabelRuleList);
                resultRuleList = new ArrayList();
                ArrayList childrenRulesList = new ArrayList();
                Iterator var10 = ciLabelRuleList.iterator();

                CiLabelRule rule;
                while (var10.hasNext()) {
                    rule = (CiLabelRule) var10.next();
                    if (StringUtil.isEmpty(rule.getParentId())) {
                        resultRuleList.add(rule);
                    } else {
                        childrenRulesList.add(rule);
                    }
                }

                var10 = resultRuleList.iterator();

                while (true) {
                    while (true) {
                        label130:
                        while (var10.hasNext()) {
                            rule = (CiLabelRule) var10.next();
                            String custListId;
                            String ciCustomGroupInfoId;
                            if (2 == rule.getElementType().intValue()) {
                                custListId = rule.getCalcuElement();
                                CiLabelInfo var31 = e.getEffectiveLabel(custListId);
                                ciCustomGroupInfoId = var31.getLabelName();
                                rule.setLabelName(ciCustomGroupInfoId);
                                rule.setUpdateCycle(var31.getUpdateCycle());
                                rule.setCustomOrLabelName(ciCustomGroupInfoId);
                                Date var32 = var31.getEffecTime();
                                int updateCycle = var31.getUpdateCycle().intValue();
                                String dimService;
                                if (updateCycle == 1) {
                                    dimService = (new SimpleDateFormat("yyyyMMdd")).format(var32);
                                    rule.setEffectDate(DateUtil.getOffsetDateByDate(dimService, -1, 1));
                                } else if (updateCycle == 2) {
                                    dimService = (new SimpleDateFormat("yyyyMM")).format(var32);
                                    rule.setEffectDate(DateUtil.getOffsetDateByDate(dimService, -1, 0));
                                }

                                rule.setLabelTypeId(var31.getLabelTypeId());
                                rule.setDataDate(var31.getDataDate());
                                DimTableServiceImpl var33 = (DimTableServiceImpl) SystemServiceLocator.getInstance().getService("dimTable_serviceTarget");
                                boolean var35;
                                int var36;
                                if (4 == var31.getLabelTypeId().intValue()) {
                                    var35 = false;
                                    if (StringUtil.isEmpty(rule.getExactValue())) {
                                        var36 = ServiceConstants.QUERY_WAY_FOR_KPI_LABEL_RANGE;
                                    } else {
                                        var36 = ServiceConstants.QUERY_WAY_FOR_KPI_LABEL_EXACT;
                                    }

                                    rule.setQueryWay("" + var36);
                                    rule.setUnit(var31.getCiLabelExtInfo().getCiMdaSysTableColumn().getUnit());
                                } else if (6 == var31.getLabelTypeId().intValue()) {
                                    var35 = false;
                                    if (StringUtil.isEmpty(rule.getExactValue())) {
                                        var36 = ServiceConstants.QUERY_WAY_FOR_KPI_LABEL_RANGE;
                                    } else {
                                        var36 = ServiceConstants.QUERY_WAY_FOR_KPI_LABEL_EXACT;
                                    }

                                    rule.setQueryWay("" + var36);
                                } else {
                                    String var37;
                                    if (7 == var31.getLabelTypeId().intValue()) {
                                        var35 = false;
                                        if (StringUtil.isEmpty(rule.getExactValue()) && StringUtils.isEmpty(rule.getTableName())) {
                                            var36 = ServiceConstants.QUERY_WAY_FOR_KPI_LABEL_RANGE;
                                        } else {
                                            var36 = ServiceConstants.QUERY_WAY_FOR_KPI_LABEL_EXACT;
                                            if (StringUtil.isEmpty(rule.getExactValue())) {
                                                var37 = this.queryAllValueStrByValueTable(rule.getTableName());
                                                rule.setExactValue(var37);
                                            }
                                        }

                                        rule.setQueryWay("" + var36);
                                    } else {
                                        String key;
                                        if (5 == var31.getLabelTypeId().intValue()) {
                                            String var34 = var31.getCiLabelExtInfo().getCiMdaSysTableColumn().getDimTransId();
                                            if (StringUtils.isEmpty(rule.getAttrVal())) {
                                                var37 = this.queryAllValueStrByValueTable(rule.getTableName());
                                                rule.setAttrVal(var37);
                                            }

                                            if (13 == ciCustomGroupInfoObj.getCreateTypeId().intValue()) {
                                                rule.setAttrName(rule.getAttrVal());
                                            } else {
                                                String[] var38 = rule.getAttrVal().split(",");
                                                StringBuffer var40 = new StringBuffer("");

                                                for (int var42 = 0; var42 < var38.length; ++var42) {
                                                    key = var38[var42];
                                                    String var45 = var33.parseName(key, "null", var34);
                                                    var40.append(var45);
                                                    if (var42 != var38.length - 1) {
                                                        var40.append(",");
                                                    }
                                                }

                                                String var44 = var40.toString();
                                                rule.setAttrName(var44);
                                            }
                                        } else if (8 == var31.getLabelTypeId().intValue()) {
                                            HashMap map = new HashMap();
                                            Set ciLabelVerticalColumnRels = var31.getCiLabelExtInfo().getCiLabelVerticalColumnRels();
                                            Iterator childRule = ciLabelVerticalColumnRels.iterator();

                                            while (childRule.hasNext()) {
                                                CiLabelVerticalColumnRel childRuleList = (CiLabelVerticalColumnRel) childRule.next();
                                                key = "" + childRuleList.getId().getColumnId();
                                                map.put(key, childRuleList);
                                            }

                                            ArrayList var39 = new ArrayList();
                                            Iterator var43 = childrenRulesList.iterator();

                                            while (true) {
                                                CiLabelRule var41;
                                                do {
                                                    if (!var43.hasNext()) {
                                                        rule.setChildCiLabelRuleList(var39);
                                                        continue label130;
                                                    }

                                                    var41 = (CiLabelRule) var43.next();
                                                } while (!rule.getRuleId().equals(var41.getParentId()));

                                                CiLabelVerticalColumnRel rel = (CiLabelVerticalColumnRel) map.get(var41.getCalcuElement());
                                                var41.setLabelTypeId(rel.getLabelTypeId());
                                                String relName = rel.getCiMdaSysTableColumn().getColumnCnName();
                                                var41.setLabelName(relName);
                                                var41.setCustomOrLabelName(relName);
                                                boolean var46;
                                                int var47;
                                                if (4 == var41.getLabelTypeId().intValue()) {
                                                    var46 = false;
                                                    if (StringUtil.isEmpty(var41.getExactValue())) {
                                                        var47 = ServiceConstants.QUERY_WAY_FOR_KPI_LABEL_RANGE;
                                                    } else {
                                                        var47 = ServiceConstants.QUERY_WAY_FOR_KPI_LABEL_EXACT;
                                                    }

                                                    var41.setQueryWay("" + var47);
                                                    var41.setUnit(rel.getCiMdaSysTableColumn().getUnit());
                                                } else if (6 == var41.getLabelTypeId().intValue()) {
                                                    var46 = false;
                                                    if (StringUtil.isEmpty(var41.getExactValue())) {
                                                        var47 = ServiceConstants.QUERY_WAY_FOR_KPI_LABEL_RANGE;
                                                    } else {
                                                        var47 = ServiceConstants.QUERY_WAY_FOR_KPI_LABEL_EXACT;
                                                    }

                                                    var41.setQueryWay("" + var47);
                                                } else {
                                                    String attrVals;
                                                    if (7 == var41.getLabelTypeId().intValue()) {
                                                        var46 = false;
                                                        if (StringUtil.isEmpty(var41.getExactValue()) && StringUtils.isEmpty(var41.getTableName())) {
                                                            var47 = ServiceConstants.QUERY_WAY_FOR_KPI_LABEL_RANGE;
                                                        } else {
                                                            var47 = ServiceConstants.QUERY_WAY_FOR_KPI_LABEL_EXACT;
                                                            if (StringUtil.isEmpty(var41.getExactValue())) {
                                                                attrVals = this.queryAllValueStrByValueTable(var41.getTableName());
                                                                var41.setExactValue(attrVals);
                                                            }
                                                        }

                                                        var41.setQueryWay("" + var47);
                                                    } else if (5 == var41.getLabelTypeId().intValue()) {
                                                        String dimTableId = rel.getCiMdaSysTableColumn().getDimTransId();
                                                        if (StringUtils.isEmpty(var41.getAttrVal())) {
                                                            attrVals = this.queryAllValueStrByValueTable(var41.getTableName());
                                                            var41.setAttrVal(attrVals);
                                                        }

                                                        String[] var48 = var41.getAttrVal().split(",");
                                                        StringBuffer sf = new StringBuffer("");

                                                        for (int attrName = 0; attrName < var48.length; ++attrName) {
                                                            String dimId = var48[attrName];
                                                            String name = var33.parseName(dimId, "null", dimTableId);
                                                            sf.append(name);
                                                            if (attrName != var48.length - 1) {
                                                                sf.append(",");
                                                            }
                                                        }

                                                        String var49 = sf.toString();
                                                        var41.setAttrName(var49);
                                                    }
                                                }

                                                var41.setColumnCnName(rel.getCiMdaSysTableColumn().getColumnCnName());
                                                var39.add(var41);
                                            }
                                        }
                                    }
                                }
                            } else if (5 == rule.getElementType().intValue()) {
                                custListId = rule.getCalcuElement();
                                CiCustomListInfo listInfo = this.ciCustomListInfoHDao.selectById(custListId);
                                if (listInfo != null) {
                                    rule.setAttrVal(listInfo.getDataDate());
                                    ciCustomGroupInfoId = listInfo.getCustomGroupId();
                                    CiCustomGroupInfo ciCustomGroupInfo = this.ciCustomGroupInfoHDao.selectCustomGroupById(ciCustomGroupInfoId);
                                    if (ciCustomGroupInfo != null) {
                                        rule.setCustomOrLabelName(ciCustomGroupInfo.getCustomGroupName());
                                        rule.setCustomCreateTypeId(ciCustomGroupInfo.getCreateTypeId());
                                        rule.setCustomId(ciCustomGroupInfo.getCustomGroupId());
                                    }
                                }
                            }
                        }

                        return resultRuleList;
                    }
                }
            } catch (Exception var30) {
                message = "查询标签规则错误，";
                if (1 == customType.intValue()) {
                    message = message + "客户群Id = " + id;
                } else if (2 == customType.intValue()) {
                    message = message + "模板Id = " + id;
                }

                this.log.error(message, var30);
                throw new CIServiceException(message, var30);
            }
        } else {
            message = "客户群或者模板Id为空，或者来源类型为空";
            this.log.error(message);
            throw new CIServiceException(message);
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiCustomGroupInfo> queryCiCustomGroupInfoListByName(String customGroupName, String userId, String createCityId) throws CIServiceException {
        List list = null;

        try {
            list = this.ciCustomGroupInfoHDao.selectCiCustomGroupInfoListByName(customGroupName, userId, createCityId);
            return list;
        } catch (Exception var7) {
            String message = "根据名称及用户Id查询客户群错误";
            this.log.error(message, var7);
            throw new CIServiceException(message, var7);
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public String getIntersectSqlOfCustomersAndLabel(List<CiLabelRule> rule, String monthDate, String dayDate, CiCustomListInfo ciCustomListInfo, String userId) throws CIServiceException {
        CiCustomListInfo info = null;
        String fromSql = "";

        try {
            fromSql = this.getWithColumnSqlStr(rule, monthDate, dayDate, userId, (List) null, (Integer) null, (Integer) null);
            if (StringUtils.isNotBlank(ciCustomListInfo.getListTableName())) {
                info = this.queryCiCustomListInfoById(ciCustomListInfo.getListTableName());
            } else if (StringUtils.isBlank(ciCustomListInfo.getDataDate())) {
                List e = this.queryCiCustomListInfoByCGroupId(ciCustomListInfo.getCustomGroupId());
                if (e != null && e.size() > 0) {
                    Iterator var10 = e.iterator();

                    while (var10.hasNext()) {
                        CiCustomListInfo cinfo = (CiCustomListInfo) var10.next();
                        if (3 == cinfo.getDataStatus().intValue()) {
                            info = ciCustomListInfo;
                            break;
                        }
                    }
                }
            } else {
                info = this.queryCiCustomListInfoByCGroupIdAndDataDate(ciCustomListInfo.getCustomGroupId(), ciCustomListInfo.getDataDate());
            }

            return (new GroupCalcSqlPaser(Configure.getInstance().getProperty("CI_BACK_DBTYPE"))).getIntersectOfTable(info.getListTableName(), fromSql);
        } catch (Exception var11) {
            this.log.error("客户群和标签的交集运算sql出错," + rule + "," + monthDate + "," + ciCustomListInfo, var11);
            throw new CIServiceException("客户群和标签的交集运算sql出错");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiCustomGroupInfo> indexQueryCustomersName(CiCustomGroupInfo ciCustomGroupInfo, Integer createTypeId) throws CIServiceException {
        List customersNameList = null;

        try {
            customersNameList = this.customersJdbcDao.indexQueryCustomersName(ciCustomGroupInfo, createTypeId);
            return customersNameList;
        } catch (Exception var5) {
            this.log.error("首页关联查询客户群名称报错", var5);
            throw new CIServiceException("首页关联查询客户群名称报错");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public Map<String, Object> isNameExist(CiCustomGroupInfo ciCustomGroupInfo, String userId) throws CIServiceException {
        boolean success = false;
        HashMap returnMap = new HashMap();
        String msg = "";

        try {
            String e = ciCustomGroupInfo.getCustomGroupName();
            if (StringUtil.isEmpty(userId)) {
                msg = "没有找到用户！";
            } else if (StringUtil.isEmpty(e)) {
                msg = "客户群名称不能为空！";
            } else {
                success = true;
            }

            if (success) {
                if (StringUtil.isNotEmpty(ciCustomGroupInfo.getCustomGroupId()) && !ciCustomGroupInfo.getCreateUserId().equals(userId)) {
                    userId = ciCustomGroupInfo.getCreateUserId();
                }

                if (PrivilegeServiceUtil.isAdminUser(userId)) {
                    userId = null;
                }

                Object createCityId = null;
                List list = this.queryCiCustomGroupInfoListByName(e, userId, (String) createCityId);
                if (list != null && list.size() > 0) {
                    CiCustomGroupInfo customGroupInfo = (CiCustomGroupInfo) list.get(0);
                    if (!customGroupInfo.getCustomGroupId().equals(ciCustomGroupInfo.getCustomGroupId())) {
                        msg = "输入的客户群名称重名，请重新输入！";
                        success = false;
                    }
                }
            }

            returnMap.put("success", Boolean.valueOf(success));
            returnMap.put("cmsg", msg);
        } catch (Exception var10) {
            msg = "重名验证错误！";
            this.log.error(msg, var10);
            success = false;
            returnMap.put("success", Boolean.valueOf(success));
            returnMap.put("cmsg", msg);
        }

        return returnMap;
    }

    public CiCustomGroupInfo reGenerate(CiCustomGroupInfo ciCustomGroupInfo, String userId, String listTableName) {
        try {
            ciCustomGroupInfo = this.ciCustomGroupInfoHDao.selectCustomGroupById(ciCustomGroupInfo.getCustomGroupId());
            List e = this.ciCustomListInfoHDao.selectByCustomGroupId(ciCustomGroupInfo.getCustomGroupId());
            ciCustomGroupInfo.setLastDataStatus(ciCustomGroupInfo.getDataStatus());
            Date oldListInfoDateTime;
            if (ciCustomGroupInfo.getUpdateCycle().intValue() == 1 && ciCustomGroupInfo.getDataStatus().intValue() != 0) {
                String hasFailed1 = CacheBase.getInstance().getNewLabelMonth();
                String listInfo1 = CacheBase.getInstance().getNewLabelDay();
                Object ciGroupAttrRelList1 = new ArrayList();
                Iterator history = e.iterator();

                while (history.hasNext()) {
                    CiCustomListInfo oldListInfoDateTime1 = (CiCustomListInfo) history.next();
                    ciGroupAttrRelList1 = this.ciGroupAttrRelService.queryNewestGroupAttrRelList(ciCustomGroupInfo.getCustomGroupId());
                    oldListInfoDateTime1.setDataStatus(Integer.valueOf(1));
                    oldListInfoDateTime1.setDataDate(hasFailed1);
                    oldListInfoDateTime1.setCustomGroupId(ciCustomGroupInfo.getCustomGroupId());
                    oldListInfoDateTime1.setDataTime(new Date());
                    if (StringUtil.isNotEmpty(ciCustomGroupInfo.getMonthLabelDate())) {
                        oldListInfoDateTime1.setMonthLabelDate(hasFailed1);
                    }

                    if (StringUtil.isNotEmpty(ciCustomGroupInfo.getDayLabelDate())) {
                        oldListInfoDateTime1.setDayLabelDate(listInfo1);
                    }

                    this.ciCustomListInfoHDao.insertCiCustomListInfo(oldListInfoDateTime1);
                    this.ciCustomListInfoJDao.dropTable(oldListInfoDateTime1.getListTableName());
                }

                oldListInfoDateTime = new Date();
                CiCustomModifyHistory history1 = this.addCiCustomModifyHistory(ciCustomGroupInfo, userId);
                history1.setModifyTime(oldListInfoDateTime);
                ciCustomGroupInfo.setNewModifyTime(oldListInfoDateTime);
                ciCustomGroupInfo.setDataDate(hasFailed1);
                ciCustomGroupInfo.setDataStatus(Integer.valueOf(1));
                this.ciCustomGroupFormJDao.deleteByCustomGroupIdAndDataDate(ciCustomGroupInfo.getCustomGroupId(), ((CiCustomListInfo) e.get(0)).getDataDate());
                this.addOrModifyCiCustomGroupInfoByOnlyLabel(ciCustomGroupInfo, history1, (List) null, (List) null, (List) ciGroupAttrRelList1, false);
            } else {
                boolean hasFailed = false;
                CiCustomListInfo listInfo;
                Iterator ciGroupAttrRelList;
                if (StringUtil.isNotEmpty(listTableName)) {
                    ciGroupAttrRelList = e.iterator();

                    while (ciGroupAttrRelList.hasNext()) {
                        listInfo = (CiCustomListInfo) ciGroupAttrRelList.next();
                        if (listInfo.getDataStatus().intValue() == 0 && listTableName.equals(listInfo.getListTableName())) {
                            oldListInfoDateTime = listInfo.getDataTime();
                            listInfo.setDataTime(new Date());
                            listInfo.setDataStatus(Integer.valueOf(1));
                            this.ciCustomListInfoHDao.insertCiCustomListInfo(listInfo);
                            this.ciGroupAttrRelService.updateCiGroupAttrRelListModifyTime(ciCustomGroupInfo.getCustomGroupId(), oldListInfoDateTime);
                            hasFailed = true;
                        }
                    }
                } else {
                    ciGroupAttrRelList = e.iterator();

                    while (ciGroupAttrRelList.hasNext()) {
                        listInfo = (CiCustomListInfo) ciGroupAttrRelList.next();
                        if (listInfo.getDataStatus().intValue() == 0) {
                            oldListInfoDateTime = listInfo.getDataTime();
                            listInfo.setDataTime(new Date());
                            listInfo.setDataStatus(Integer.valueOf(1));
                            this.ciCustomListInfoHDao.insertCiCustomListInfo(listInfo);
                            this.ciGroupAttrRelService.updateCiGroupAttrRelListModifyTime(ciCustomGroupInfo.getCustomGroupId(), oldListInfoDateTime);
                            hasFailed = true;
                        }
                    }
                }

                if (!hasFailed && e.size() > 0 && ((CiCustomListInfo) e.get(0)).getDataStatus().intValue() == 3) {
                    ciCustomGroupInfo.setDataDate(((CiCustomListInfo) e.get(0)).getDataDate());
                    ciCustomGroupInfo.setDataStatus(Integer.valueOf(3));
                }

                ciCustomGroupInfo.setNewModifyTime(new Date());
                this.ciCustomGroupInfoHDao.insertCustomGroup(ciCustomGroupInfo);
                this.genCusomerList(ciCustomGroupInfo);
            }

            return ciCustomGroupInfo;
        } catch (Exception var10) {
            this.log.error("客户群重新生成 ,reGenerate error", var10);
            throw new CIServiceException("客户群重新生成失败");
        }
    }

    public void generateNewPeriodData(CiCustomGroupInfo ciCustomGroupInfo, String dataDate) {
        try {
            ciCustomGroupInfo.setLastDataStatus(ciCustomGroupInfo.getDataStatus());
            ciCustomGroupInfo.setDataStatus(Integer.valueOf(1));
            String e = Configure.getInstance().getProperty("CURRENT_SERVER_ID");
            if (StringUtil.isNotEmpty(e)) {
                this.log.debug("跑周期性客户群时，客户群[" + ciCustomGroupInfo.getCustomGroupId() + "] 保存ServerId：        " + e);
                ciCustomGroupInfo.setServerId(e);
            }

            CiCustomListInfo oldInfo = this.ciCustomListInfoHDao.selectByCustomGroupIdAndDataDate(ciCustomGroupInfo.getCustomGroupId(), dataDate);
            if (oldInfo != null) {
                this.log.debug("已经存在老的清单信息,清单表名为：" + oldInfo.getListTableName());
                if (oldInfo.getDataStatus().intValue() == 3 || oldInfo.getDataStatus().intValue() == 0) {
                    return;
                }

                this.ciCustomListInfoHDao.delete(oldInfo);
            }

            this.ciCustomGroupInfoHDao.insertCustomGroup(ciCustomGroupInfo);
            CiCustomListInfo ciCustomListInfo = new CiCustomListInfo();
            ciCustomListInfo.setCustomGroupId(ciCustomGroupInfo.getCustomGroupId());
            String tabName = Configure.getInstance().getProperty("CUST_LIST_TMP_TABLE");
            tabName = tabName.replace("YYMMDDHHMISSTTTTTT", "") + CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L);
            ciCustomListInfo.setListTableName(tabName);
            ciCustomListInfo.setDataTime(new Date());
            ciCustomListInfo.setDataDate(dataDate);
            ciCustomListInfo.setListMaxNum(ciCustomGroupInfo.getListMaxNum());
            CacheBase cache = CacheBase.getInstance();
            String newestDay = cache.getNewLabelDay();
            String newestMonth = cache.getNewLabelMonth();
            List ciLabelRuleList = this.ciLabelRuleHDao.selectCiLabelRuleList(ciCustomGroupInfo.getCustomGroupId(), Integer.valueOf(1));
            boolean dayFlag = false;
            boolean monthFlag = false;
            CiLabelRule ciGroupAttrRelList;
            if (ciLabelRuleList != null && ciLabelRuleList.size() > 0) {
                Iterator d = ciLabelRuleList.iterator();

                while (d.hasNext()) {
                    ciGroupAttrRelList = (CiLabelRule) d.next();
                    if (2 == ciGroupAttrRelList.getElementType().intValue()) {
                        CiLabelInfo newAttr = cache.getEffectiveLabel(ciGroupAttrRelList.getCalcuElement());
                        if (newAttr != null) {
                            if (newAttr.getUpdateCycle().intValue() == 1) {
                                dayFlag = true;
                            }

                            if (newAttr.getUpdateCycle().intValue() == 2) {
                                monthFlag = true;
                            }
                        }
                    } else if (5 == ciGroupAttrRelList.getElementType().intValue()) {
                        Integer newAttr1 = ciGroupAttrRelList.getLabelFlag();
                        if (newAttr1 != null) {
                            if (newAttr1.intValue() == 2) {
                                dayFlag = true;
                            }

                            if (newAttr1.intValue() == 1) {
                                monthFlag = true;
                            }
                        }
                    }
                }
            }

            if (dayFlag) {
                ciCustomListInfo.setDayLabelDate(newestDay);
            }

            if (monthFlag) {
                ciCustomListInfo.setMonthLabelDate(newestMonth);
            }

            ciCustomListInfo.setDataStatus(Integer.valueOf(1));
            ciGroupAttrRelList = null;
            List ciGroupAttrRelList1 = this.ciGroupAttrRelService.queryNewestGroupAttrRelList(ciCustomGroupInfo.getCustomGroupId());
            if (monthFlag) {
                ReviseDateModel d1 = this.queryReviseDate(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList1, ciCustomListInfo.getMonthLabelDate());
                if (d1 != null && "2".equals(d1.getReviseType())) {
                    ciCustomListInfo.setMonthLabelDate(d1.getReviseMonth());
                } else if (d1 != null && "4".equals(d1.getReviseType())) {
                    ciCustomListInfo.setMonthLabelDate(ciCustomListInfo.getMonthLabelDate());
                }
            }

            this.ciCustomListInfoHDao.insertCiCustomListInfo(ciCustomListInfo);
            Date d2 = new Date();
            CiGroupAttrRel newAttr2;
            if (ciGroupAttrRelList1 != null && ciGroupAttrRelList1.size() > 0) {
                Iterator relId1 = ciGroupAttrRelList1.iterator();

                while (relId1.hasNext()) {
                    newAttr2 = (CiGroupAttrRel) relId1.next();

                    try {
                        newAttr2.getId().setCustomGroupId(ciCustomGroupInfo.getCustomGroupId());
                        newAttr2.getId().setModifyTime(d2);
                        this.ciGroupAttrRelHDao.insertCiGroupAttrRelHDao(newAttr2);
                    } catch (Exception var18) {
                        this.log.error("保存客户群属性错误", var18);
                        throw new CIServiceException("保存客户群属性错误", var18);
                    }
                }
            } else {
                newAttr2 = new CiGroupAttrRel();
                CiGroupAttrRelId relId = new CiGroupAttrRelId();
                relId.setCustomGroupId(ciCustomGroupInfo.getCustomGroupId());
                relId.setAttrCol("ATTR_COL");
                relId.setModifyTime(new Date());
                newAttr2.setId(relId);
                newAttr2.setStatus(Integer.valueOf(2));
                this.ciGroupAttrRelHDao.insertCiGroupAttrRelHDao(newAttr2);
            }

            this.genCusomerList(ciCustomGroupInfo, Boolean.valueOf(true));
        } catch (Exception var19) {
            this.log.error("生成新的清单信息 ,generateNewPeriodData error", var19);
            throw new CIServiceException("生成新的清单信息失败");
        }
    }

    public void generateNewTimingData(CiCustomGroupInfo ciCustomGroupInfo) {
        try {
            CacheBase e = CacheBase.getInstance();
            String dataDate = "";
            if (2 == ciCustomGroupInfo.getUpdateCycle().intValue()) {
                dataDate = e.getNewLabelMonth();
            } else if (3 == ciCustomGroupInfo.getUpdateCycle().intValue()) {
                dataDate = e.getNewLabelDay();
            }

            ciCustomGroupInfo.setLastDataStatus(ciCustomGroupInfo.getDataStatus());
            ciCustomGroupInfo.setDataStatus(Integer.valueOf(1));
            String serverId = Configure.getInstance().getProperty("CURRENT_SERVER_ID");
            if (StringUtil.isNotEmpty(serverId)) {
                ciCustomGroupInfo.setServerId(serverId);
            }

            CiCustomListInfo oldInfo = this.ciCustomListInfoHDao.selectByCustomGroupIdAndDataDate(ciCustomGroupInfo.getCustomGroupId(), dataDate);
            if (oldInfo != null) {
                this.log.debug("已经存在老的清单信息,清单表名为：" + oldInfo.getListTableName());
                if (oldInfo.getDataStatus().intValue() == 3 || oldInfo.getDataStatus().intValue() == 0) {
                    return;
                }

                this.ciCustomListInfoHDao.delete(oldInfo);
            }

            this.ciCustomGroupInfoHDao.insertCustomGroup(ciCustomGroupInfo);
            CiCustomListInfo ciCustomListInfo = new CiCustomListInfo();
            ciCustomListInfo.setCustomGroupId(ciCustomGroupInfo.getCustomGroupId());
            String tabName = Configure.getInstance().getProperty("CUST_LIST_TMP_TABLE");
            tabName = tabName.replace("YYMMDDHHMISSTTTTTT", "") + CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L);
            ciCustomListInfo.setListTableName(tabName);
            ciCustomListInfo.setDataTime(new Date());
            ciCustomListInfo.setDataDate(dataDate);
            ciCustomListInfo.setListMaxNum(ciCustomGroupInfo.getListMaxNum());
            String newestDay = e.getNewLabelDay();
            String newestMonth = e.getNewLabelMonth();
            boolean dayFlag = false;
            boolean monthFlag = false;
            List ciLabelRuleList = this.ciLabelRuleHDao.selectCiLabelRuleList(ciCustomGroupInfo.getCustomGroupId(), Integer.valueOf(1));
            if (ciLabelRuleList != null && ciLabelRuleList.size() > 0) {
                Iterator d = ciLabelRuleList.iterator();

                while (d.hasNext()) {
                    CiLabelRule ciGroupAttrRelList = (CiLabelRule) d.next();
                    if (2 == ciGroupAttrRelList.getElementType().intValue()) {
                        CiLabelInfo endedCustomersListInfoCount = e.getEffectiveLabel(ciGroupAttrRelList.getCalcuElement());
                        if (endedCustomersListInfoCount != null) {
                            if (endedCustomersListInfoCount.getUpdateCycle().intValue() == 1) {
                                dayFlag = true;
                            }

                            if (endedCustomersListInfoCount.getUpdateCycle().intValue() == 2) {
                                monthFlag = true;
                            }
                        }
                    } else if (5 == ciGroupAttrRelList.getElementType().intValue()) {
                        Integer endedCustomersListInfoCount1 = ciGroupAttrRelList.getLabelFlag();
                        if (endedCustomersListInfoCount1 != null) {
                            if (endedCustomersListInfoCount1.intValue() == 2) {
                                dayFlag = true;
                            }

                            if (endedCustomersListInfoCount1.intValue() == 1) {
                                monthFlag = true;
                            }
                        }
                    }
                }
            }

            if (dayFlag) {
                ciCustomListInfo.setDayLabelDate(newestDay);
            }

            if (monthFlag) {
                ciCustomListInfo.setMonthLabelDate(newestMonth);
            }

            ciCustomListInfo.setDataStatus(Integer.valueOf(1));
            List ciGroupAttrRelList1 = this.ciGroupAttrRelService.queryNewestGroupAttrRelList(ciCustomGroupInfo.getCustomGroupId());
            if (monthFlag) {
                ReviseDateModel d1 = this.queryReviseDate(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList1, ciCustomListInfo.getMonthLabelDate());
                if (ciCustomGroupInfo.getUpdateCycle().intValue() == 2 && "2".equals(ciCustomGroupInfo.getTacticsId())) {
                    if (d1 != null && d1.getReviseMonth() != null) {
                        ciCustomListInfo.setMonthLabelDate(d1.getReviseMonth());
                    }
                } else if (d1 != null && "2".equals(d1.getReviseType())) {
                    ciCustomListInfo.setMonthLabelDate(d1.getReviseMonth());
                } else if (d1 != null && "4".equals(d1.getReviseType())) {
                    ciCustomListInfo.setMonthLabelDate(ciCustomListInfo.getMonthLabelDate());
                }
            }

            if (dayFlag && "2".equals(ciCustomGroupInfo.getTacticsId())) {
                this.reviseDayLabelDate(ciCustomListInfo, ciLabelRuleList, ciGroupAttrRelList1);
            }

            this.log.debug("指定时间跑清单 ，新建的清单对象：ciCustomListInfo.getListTableName：" + ciCustomListInfo.getListTableName() + "    ciCustomListInfo.getDayLabelDate:" + ciCustomListInfo.getDayLabelDate() + "    ciCustomListInfo.getMonthLabelDate:" + ciCustomListInfo.getMonthLabelDate());
            this.ciCustomListInfoHDao.insertCiCustomListInfo(ciCustomListInfo);
            Date d2 = new Date();
            CiGroupAttrRel endedCustomersListInfoCount2;
            if (ciGroupAttrRelList1 != null && ciGroupAttrRelList1.size() > 0) {
                Iterator relId1 = ciGroupAttrRelList1.iterator();

                while (relId1.hasNext()) {
                    endedCustomersListInfoCount2 = (CiGroupAttrRel) relId1.next();

                    try {
                        endedCustomersListInfoCount2.getId().setCustomGroupId(ciCustomGroupInfo.getCustomGroupId());
                        endedCustomersListInfoCount2.getId().setModifyTime(d2);
                        this.ciGroupAttrRelHDao.insertCiGroupAttrRelHDao(endedCustomersListInfoCount2);
                    } catch (Exception var18) {
                        this.log.error("保存客户群属性错误", var18);
                    }
                }
            } else {
                endedCustomersListInfoCount2 = new CiGroupAttrRel();
                CiGroupAttrRelId relId = new CiGroupAttrRelId();
                relId.setCustomGroupId(ciCustomGroupInfo.getCustomGroupId());
                relId.setAttrCol("ATTR_COL");
                relId.setModifyTime(new Date());
                endedCustomersListInfoCount2.setId(relId);
                endedCustomersListInfoCount2.setStatus(Integer.valueOf(2));
                this.ciGroupAttrRelHDao.insertCiGroupAttrRelHDao(endedCustomersListInfoCount2);
            }

            int endedCustomersListInfoCount3 = this.selectEndedCustomersListInfoCount(ciCustomListInfo);
            if (endedCustomersListInfoCount3 > 0) {
                this.genCusomerList(ciCustomGroupInfo, Boolean.valueOf(true));
            } else {
                this.genCusomerList(ciCustomGroupInfo);
            }

        } catch (Exception var19) {
            this.log.error("生成新的清单信息 ,generateNewTimingData error", var19);
            throw new CIServiceException("生成新的清单信息失败");
        }
    }

    public List<CiCustomGroupInfo> findCiCustomGroupInfo4CycleCreate(int updateCycle, Date dataDate, String cityIds) {
        return this.ciCustomGroupInfoHDao.selectCiCustomGroupInfo4CycleCreate(updateCycle, dataDate, cityIds);
    }

    public List<CiCustomGroupInfo> findCiCustomGroupInfo4TimingCreate(Date nowDate) {
        Object customList = new ArrayList();

        try {
            String e = this.customersJdbcDao.getCiCustomExeTime();
            if (nowDate == null) {
                nowDate = new Date();
            }

            if (StringUtil.isNotEmpty(e)) {
                customList = this.customersJdbcDao.findCiCustomGroupInfo4TimingCreate(e, nowDate);
            } else {
                e = "2014-01-01 00:00:00";
                customList = this.customersJdbcDao.findCiCustomGroupInfo4TimingCreate(e, nowDate);
            }
        } catch (Exception var4) {
            this.log.error("查询需要定时跑清单的客户群列表出错", var4);
        }

        return (List) customList;
    }

    public List<TreeNode> queryUserCustomerGroupTree(String userId) {
        ArrayList treeList = new ArrayList();
        CiCustomGroupInfo customGroupInfoVO = new CiCustomGroupInfo();
        customGroupInfoVO.setCreateUserId(userId);
        customGroupInfoVO.setStatus(Integer.valueOf(1));
        customGroupInfoVO.setDataStatus(Integer.valueOf(3));
        List customGroupInfoList = this.queryCustomersListByCustomGroupInfo(customGroupInfoVO, "customGroupName", 1);
        Iterator var6 = customGroupInfoList.iterator();

        while (var6.hasNext()) {
            CiCustomGroupInfo customGroupInfo = (CiCustomGroupInfo) var6.next();
            if (customGroupInfo.getUpdateCycle().intValue() != 1 && customGroupInfo.getUpdateCycle().intValue() != 4) {
                TreeNode treeNode = new TreeNode();
                treeNode.setId(customGroupInfo.getCustomGroupId());
                treeNode.setpId(customGroupInfo.getParentCustomId());
                treeNode.setName(customGroupInfo.getCustomGroupName());
                treeList.add(treeNode);
            }
        }

        return treeList;
    }

    public List<TreeNode> queryCustomGroupTree(String customGroupName) throws CIServiceException {
        CacheBase cache = CacheBase.getInstance();
        CiCustomGroupInfo customGroup = new CiCustomGroupInfo();
        String userId = PrivilegeServiceUtil.getUserId();
        customGroup.setCreateUserId(userId);
        customGroup.setDataStatus(Integer.valueOf(3));

        try {
            if (StringUtils.isNotBlank(customGroupName)) {
                customGroup.setCustomGroupName(customGroupName);
            }

            if (!PrivilegeServiceUtil.isAdminUser(userId)) {
                customGroup.setCreateUserId(userId);
            } else {
                customGroup.setCreateUserId("");
            }

            Pager e = new Pager();
            e.setPageNum(0);
            e.setPageSize(0);
            List customGroupList = this.customersJdbcDao.getCustomsersList(e, customGroup);
            CopyOnWriteArrayList createTypeList = cache.getObjectList("DIM_CUSTOM_CREATE_TYPE");
            List treeList = this.newTreeNode(customGroupList, createTypeList);
            return treeList;
        } catch (Exception var10) {
            String message = "标签树查询错误";
            this.log.error(message, var10);
            throw new CIServiceException(message, var10);
        }
    }

    private List<TreeNode> newTreeNode(List<CiCustomGroupInfo> customGroupList, List<DimCustomCreateType> createTypeList) {
        ArrayList treeList = new ArrayList();
        HashSet createTypeSet = new HashSet();
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx.get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
        String contextPath = request.getContextPath();
        CacheBase cache = CacheBase.getInstance();
        Iterator var10 = customGroupList.iterator();

        TreeNode treeNode;
        while (var10.hasNext()) {
            CiCustomGroupInfo createType = (CiCustomGroupInfo) var10.next();
            if (createType.getCustomGroupName().equals("标准全球通用户 且 养卡用户 且 在网时长10个月以内的用户")) {
                System.out.println(createType);
            }

            treeNode = new TreeNode();
            treeNode.setId(createType.getCustomGroupId());
            treeNode.setName(createType.getCustomGroupName());
            treeNode.setpId("" + createType.getCreateTypeId());
            treeNode.setIsParent(Boolean.FALSE);
            treeNode.setOpen(Boolean.FALSE);
            List list = (List) cache.get("CI_CUSTOM_LIST_INFO_MAP", createType.getCustomGroupId(), List.class);
            if (list == null) {
                list = this.ciCustomListInfoHDao.selectByCustomGroupId(createType.getCustomGroupId());
                cache.put("CI_CUSTOM_LIST_INFO_MAP", createType.getCustomGroupId(), list);
            }

            treeNode.setParam(createType.getJson());
            if (createType.getUpdateCycle() != null) {
                if (createType.getUpdateCycle().intValue() == 1) {
                    treeNode.setTip("一次性");
                } else if (createType.getUpdateCycle().intValue() == 2) {
                    treeNode.setTip("月周期");
                } else {
                    treeNode.setTip("日周期");
                }
            }

            treeNode.setTip(createType.getCustomGroupName());
            String custIconPath = contextPath + Configure.getInstance().getProperty("CUSTOMER_FOR_TREE");
            treeNode.setIcon(custIconPath);
            treeList.add(treeNode);
            createTypeSet.add(createType.getCreateTypeId());
        }

        var10 = createTypeList.iterator();

        while (var10.hasNext()) {
            DimCustomCreateType createType1 = (DimCustomCreateType) var10.next();
            if (createTypeSet.contains(createType1.getCreateTypeId())) {
                treeNode = new TreeNode();
                treeNode.setId("" + createType1.getCreateTypeId());
                treeNode.setName(createType1.getCreateTypeName());
                treeNode.setpId("0");
                treeNode.setIsParent(Boolean.TRUE);
                treeNode.setOpen(Boolean.FALSE);
                treeList.add(treeNode);
                treeNode.setTip(createType1.getCreateTypeName());
            }
        }

        return treeList;
    }

    public List<CiCustomListInfo> queryCiCustomListInfoAll() {
        return this.ciCustomListInfoHDao.selectAll();
    }

    public void saveCiCustomListInfo(CiCustomListInfo listInfo) {
        this.ciCustomListInfoHDao.insertCiCustomListInfo(listInfo);
    }

    public void syncUpdateCiCustomListInfo(CiCustomListInfo listInfo) {
        try {
            listLock.lock();
            this.ciCustomListInfoHDao.insertCiCustomListInfo(listInfo);
        } catch (Exception var6) {
            this.log.error("同步方法更新客户群信息错误", var6);
        } finally {
            listLock.unlock();
        }

    }

    public void pushCustomers(List<CiCustomPushReq> customPushReqList, String pushCycle) {
        boolean validateParameter = true;
        if (customPushReqList != null) {
            for (int e = 0; e < customPushReqList.size(); ++e) {
                CiCustomPushReq ciCustomPushReq = (CiCustomPushReq) customPushReqList.get(e);
                List ids = ciCustomPushReq.getSysIds();
                if (ids != null) {
                    Iterator var8 = ids.iterator();

                    while (var8.hasNext()) {
                        String ciCustomListInfo = (String) var8.next();
                        CiSysInfo sysInfo = this.ciSysInfoHDao.selectById(ciCustomListInfo);
                        if (sysInfo == null) {
                            validateParameter = false;
                            break;
                        }
                    }
                }

                if (!validateParameter) {
                    break;
                }

                CiCustomListInfo var12 = this.ciCustomListInfoHDao.selectById(ciCustomPushReq.getListTableName());
                if (var12 == null || var12.getDataStatus().intValue() != 3) {
                    validateParameter = false;
                    break;
                }
            }
        } else {
            validateParameter = false;
        }

        if (validateParameter) {
            try {
                CustomerPublishThread var11 = (CustomerPublishThread) SystemServiceLocator.getInstance().getService("customerPublishThread");
                var11.initParamter(customPushReqList, pushCycle);
                ThreadPool.getInstance().execute(var11);
            } catch (Exception var10) {
                this.log.error("创建推送请求失败", var10);
                throw new CIServiceException("创建推送请求失败");
            }
        }
    }

    public void pushCustomersAfterSave(CiCustomPushReq ciCustomPushReq, String pushCycle, CiCustomGroupInfo ciCustomGroupInfo) {
        this.ciCustomGroupInfoHDao.deleteCiCustomGroupPushCycle(ciCustomGroupInfo.getCustomGroupId(), Integer.valueOf(pushCycle));
        List sysIds = ciCustomPushReq.getSysIds();
        if (sysIds != null) {
            for (int listTableName = 0; listTableName < sysIds.size(); ++listTableName) {
                CiCustomGroupPushCycleId customListInfo = new CiCustomGroupPushCycleId(ciCustomGroupInfo.getCustomGroupId(), (String) sysIds.get(listTableName));
                CiCustomGroupPushCycle e = new CiCustomGroupPushCycle(customListInfo, Integer.valueOf(1));
                e.setPushCycle(Integer.valueOf(pushCycle));
                e.setIsPushed(Integer.valueOf(0));
                this.saveCiCustomGroupPushCycle(e);
            }
        }

        String var10 = ciCustomPushReq.getListTableName();
        CiCustomListInfo var11 = this.queryCiCustomListInfoById(var10);
        if (var11.getDataStatus().intValue() == 3) {
            try {
                ArrayList var12 = new ArrayList();
                var12.add(ciCustomPushReq);
                CustomerPublishThread pub = (CustomerPublishThread) SystemServiceLocator.getInstance().getService("customerPublishThread");
                pub.initParamter(var12, pushCycle);
                ThreadPool.getInstance().execute(pub);
            } catch (Exception var9) {
                this.log.error("创建推送请求失败", var9);
                throw new CIServiceException("创建推送请求失败");
            }
        }

    }

    public boolean pushSaCustomers(CiCustomPushReq customPushReq, String pushCycle) {
        boolean success = false;
        String fileName = "COC_" + customPushReq.getUserId() + "_" + customPushReq.getReqId().replace("COC", "");
        CiCustomListInfo customListInfo = this.queryCiCustomListInfoById(customPushReq.getListTableName());
        CiCustomGroupInfo customGroupInfo = this.queryCiCustomGroupInfo(customListInfo.getCustomGroupId());
        CiSysInfo sysInfo = this.ciSysInfoHDao.selectById(customPushReq.getSysId());
        sysInfo.setReqId(customPushReq.getReqId());
        String zipFile = sysInfo.getLocalPath() + File.separator + fileName + ".zip";
        String xmlFile = sysInfo.getLocalPath() + File.separator + fileName + ".xml";
        success = this.uploadXmlFile(xmlFile, zipFile, customListInfo, customGroupInfo.getCustomGroupName(), customPushReq, sysInfo);
        if (!success) {
            this.log.error("向SA推送XML失败");
        } else {
            ArrayList sysIds = new ArrayList();
            sysIds.add(customPushReq.getSysId());
            ArrayList customPushReqList = new ArrayList();
            customPushReq.setSysIds(sysIds);
            customPushReqList.add(customPushReq);
            this.pushCustomers(customPushReqList, pushCycle);
        }

        return success;
    }

    private boolean uploadXmlFile(String xmlFile, String zipFile, CiCustomListInfo ciCustomListInfo, String customGroupName, CiCustomPushReq ciCustomPushReq, CiSysInfo sysInfo) {
        PowerShowRequestXmlBean bean = new PowerShowRequestXmlBean();
        bean.getTitle().setInterfaceAddress(StringUtil.isEmpty(sysInfo.getWebserviceWSDL()) ? "http://webservice.querytool.linkage.com" : sysInfo.getWebserviceWSDL().replace("?wsdl", ""));
        bean.getTitle().setSendTime(new Date());
        bean.getData().setSyncTaskId(ciCustomPushReq.getReqId());
        bean.getData().setPlatformCode("COC");
        String fileName = (new File(zipFile)).getName();
        bean.getData().setUploadFileName(fileName);
        bean.getData().setRowNumber(ciCustomListInfo.getCustomNum().toString());
        bean.getData().setApplicant(ciCustomPushReq.getUserId());
        Calendar c = Calendar.getInstance();
        int offset = 1;

        try {
            offset = Math.abs(Integer.valueOf(Configure.getInstance().getProperty("SA_FILE_EFFCITVE_DAY")).intValue());
        } catch (NumberFormatException var23) {
            this.log.error(" worng config SA_FILE_EFFCITVE_DAY = " + Configure.getInstance().getProperty("SA_FILE_EFFCITVE_DAY"));
        }

        c.add(5, offset);
        bean.getData().setExpireDate(DateUtil.date2String(c.getTime(), "yyyy-MM-dd"));
        bean.getData().setLinkId("1");
        bean.getData().setTarget("客户群微分");
        bean.getData().setUploadFileDesc(customGroupName);
        Column column = new Column();
        String title = Configure.getInstance().getProperty("RELATED_COLUMN_CN_NAME");
        if (StringUtil.isEmpty(title)) {
            title = "手机号码";
        }

        column.setColumnName(title);
        String phoneNoLength = Configure.getInstance().getProperty("SA_PHONENO_LENGTH").trim();
        if (!"".equals(phoneNoLength) && phoneNoLength != null) {
            column.setColumnLength(phoneNoLength);
        } else {
            column.setColumnLength("11");
        }

        column.setColumnType("VARCHAR".toUpperCase());
        bean.getData().addColumn(column);
        String xmlStr = "";
        boolean result = false;

        try {
            xmlStr = Bean2XMLUtils.bean2XmlString(bean);
            this.log.debug(xmlStr);
        } catch (IOException var19) {
            this.log.error("生成xml文件，uploadXmlFile IOException", var19);
            this.delException(ciCustomPushReq, "生成xml文件，uploadXmlFile IOException", var19);
            return false;
        } catch (SAXException var20) {
            this.log.error("生成xml文件，uploadXmlFile SAXException", var20);
            this.delException(ciCustomPushReq, "生成xml文件，uploadXmlFile SAXException", var20);
            return false;
        } catch (IntrospectionException var21) {
            this.log.error("生成xml文件，uploadXmlFile IntrospectionException", var21);
            this.delException(ciCustomPushReq, "生成xml文件，uploadXmlFile IntrospectionException", var21);
            return false;
        } catch (Exception var22) {
            this.log.error("生成xml文件，uploadXmlFile", var22);
            this.delException(ciCustomPushReq, "生成xml文件，uploadXmlFile", var22);
            return false;
        }

        try {
            FileUtil.writeByteFile(xmlStr.getBytes("UTF-8"), new File(xmlFile));
        } catch (Exception var18) {
            this.log.error("生成xml文件，uploadXmlFile", var18);
            this.delException(ciCustomPushReq, "生成xml文件，uploadXmlFile", var18);
            return false;
        }

        try {
            this.log.debug("ftp :" + sysInfo.getFtpServerIp() + ":" + sysInfo.getFtpPort() + sysInfo.getFtpPath());
            result = FtpUtil.ftp(sysInfo.getFtpServerIp(), sysInfo.getFtpPort(), sysInfo.getFtpUser(), DES.decrypt(sysInfo.getFtpPwd()), xmlFile, sysInfo.getFtpPath() + "/");
            if (!result) {
                this.delException(ciCustomPushReq, "FTP出错", new CIServiceException("ftp error"));
            }

            result = (new File(xmlFile)).delete();
            return result;
        } catch (Exception var17) {
            this.log.error("XML FTP出错" + sysInfo + xmlFile, var17);
            this.delException(ciCustomPushReq, "XML FTP出错", var17);
            return false;
        }
    }

    private void delException(CiCustomPushReq ciCustomPushReq, String msg, Exception e) {
        ciCustomPushReq.setStatus(Integer.valueOf(0));
        String expctionInfo = "";

        try {
            StringWriter e2 = new StringWriter();
            PrintWriter pw = new PrintWriter(e2);
            e.printStackTrace(pw);
            expctionInfo = e2.toString();
        } catch (Exception var7) {
            this.log.error(" get message error");
        }

        expctionInfo = msg + expctionInfo;
        if (expctionInfo.length() > 2048) {
            expctionInfo = expctionInfo.substring(0, 1024);
        }

        ciCustomPushReq.setExeInfo(expctionInfo);
        this.saveCiCustomPushReq(ciCustomPushReq);
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiCustomPushReq> queryCiCustomPushReq(CiCustomPushReq ciCustomPushReq) {
        return this.ciCustomPushReqHDao.select(ciCustomPushReq);
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public CiCustomPushReq queryCiCustomPushReqById(String reqId) throws CIServiceException {
        CiCustomPushReq ciCustomPushReq = null;

        try {
            ciCustomPushReq = this.ciCustomPushReqHDao.selectCiCustomPushReqById(reqId);
            return ciCustomPushReq;
        } catch (Exception var5) {
            String message = "根据任务id查询推送请求失败";
            this.log.error(message, var5);
            throw new CIServiceException(message, var5);
        }
    }

    public void saveCiCustomPushReq(CiCustomPushReq req) {
        this.ciCustomPushReqHDao.insertCiCustomPushReq(req);
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiSysInfo> queryCiSysInfo(int showOrNot) {
        List list = this.ciSysInfoHDao.selectByShowOrNOt(showOrNot);
        ArrayList filteredList = new ArrayList();
        Iterator var5 = list.iterator();

        while (true) {
            while (var5.hasNext()) {
                CiSysInfo sys = (CiSysInfo) var5.next();
                if (StringUtils.isNotEmpty(sys.getFunctionId()) && StringUtils.isNotEmpty(sys.getActionId())) {
                    try {
                        boolean e = PrivilegeServiceUtil.getUserPrivilegeService().haveOperRight(PrivilegeServiceUtil.getUserId(), sys.getFunctionId(), sys.getActionId());
                        if (e) {
                            filteredList.add(sys);
                        }
                    } catch (Throwable var7) {
                        this.log.error("PrivilegeServiceUtil.getUserPrivilegeService().haveOperRight error," + sys, var7);
                    }
                } else {
                    filteredList.add(sys);
                }
            }

            return filteredList;
        }
    }

    public void saveCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo) {
        this.ciCustomGroupInfoHDao.insertCustomGroup(ciCustomGroupInfo);
    }

    public void syncUpdateCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo) {
        try {
            customLock.lock();
            this.ciCustomGroupInfoHDao.insertCustomGroup(ciCustomGroupInfo);
        } catch (Exception var6) {
            this.log.error("同步方法更新客户群信息错误", var6);
        } finally {
            customLock.unlock();
        }

    }

    public void saveCiCustomCampsegRel(CiCustomCampsegRel ciCustomCampsegRel) {
        this.ciCustomCampsegRelHDao.insertCiCustomCampsegRel(ciCustomCampsegRel);
    }

    public void executeInBackDataBase(String sql) {
        this.ciCustomListInfoJDao.executeInBackDataBase(sql);
    }

    public void executeInFrontDataBase(String sql) {
        this.ciCustomListInfoJDao.executeInFrontDataBase(sql);
    }

    public int addInBackDataBase(String sql) throws Exception {
        boolean rows = false;
        int rows1 = this.ciCustomListInfoJDao.insertInBackDataBase(sql);
        return rows1;
    }

    public int addInBackDataBase(String sql, String cityId) throws Exception {
        boolean rows = false;
        String mutliServerFlag = Configure.getInstance().getProperty("CYCLE_CUSTOM_WEBSERVICE");
        int rows1;
        if (mutliServerFlag != null && StringUtil.isNotEmpty(mutliServerFlag) && mutliServerFlag.equalsIgnoreCase("true") && StringUtil.isNotEmpty(cityId)) {
            CiTaskServerCityRel cityRel = this.ciTaskServerCityRelHDao.selectById(Integer.valueOf(cityId));
            String databaseUrl = "";
            if (cityRel != null && StringUtil.isNotEmpty(cityRel.getDatabseUrl())) {
                databaseUrl = cityRel.getDatabseUrl();
            }

            String username = "";
            if (cityRel != null && StringUtil.isNotEmpty(cityRel.getUsername())) {
                username = cityRel.getUsername();
            }

            String password = "";
            if (cityRel != null && StringUtil.isNotEmpty(cityRel.getPassword())) {
                password = cityRel.getPassword();
            }

            rows1 = this.ciCustomListInfoJDao.insertInBackDataBase(sql, databaseUrl, username, password);
        } else {
            rows1 = this.ciCustomListInfoJDao.insertInBackDataBase(sql);
        }

        return rows1;
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public int queryCustomersTotalCount(CiCustomListInfo bean) {
        return this.ciCustomListInfoJDao.selectCustomersTotalCount(bean);
    }

    public void saveCiCustomListExeInfo(CiCustomListExeInfo exeInfo) {
        this.ciCustomListExeInfoHDao.insertCiCustomListExeInfo(exeInfo);
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiCustomSourceRel> queryCiCustomSourceRelByCustomGroupId(String customGroupId) {
        return this.ciCustomSourceRelHDao.selectByCustomGroupId(customGroupId);
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public int queryCountInBackDB(String sql) {
        return this.ciCustomListInfoJDao.selectBackCountBySql(sql);
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public Map<String, Object> query4Map(String sql) {
        return this.ciCustomListInfoJDao.selectBackForMap(sql, new Object[0]);
    }

    public void resetProductAutoMacthFlag4CiCustomGroupInfo() {
        int count = this.ciCustomGroupInfoHDao.resetProductAutoMacthFlag();
        this.log.debug("resetProductAutoMacthFlag4CiCustomGroupInfo:" + count);
    }

    public String getSqlCreateAsTable(String tableName, String tabNameTemplet, String column) {
        return this.ciCustomListInfoJDao.getSqlCreateAsTable(tableName, tabNameTemplet, column);
    }

    public Collection<CrmTypeModel> queryAllLabelsOfUser(String productNo) {
        long start = System.currentTimeMillis();
        CopyOnWriteArrayList allLabel = CacheBase.getInstance().getObjectList("ALL_EFFECTIVE_LABEL_MAP");
        HashMap crmtypeMap = new HashMap();
        List types = this.ciCustomListInfoJDao.selectCrmTypeModel();
        Iterator tableNams = types.iterator();

        while (tableNams.hasNext()) {
            CrmTypeModel labelColumn2IdMap = (CrmTypeModel) tableNams.next();
            crmtypeMap.put(labelColumn2IdMap.getTypeId().toString(), labelColumn2IdMap);
        }

        HashMap var19 = new HashMap();
        ArrayList var20 = new ArrayList();
        Iterator var10 = allLabel.iterator();

        while (true) {
            CiLabelInfo l;
            do {
                do {
                    if (!var10.hasNext()) {
                        this.log.info("step1 const:" + (System.currentTimeMillis() - start));
                        this.getAllLabelsFromTable(productNo, var20, crmtypeMap, var19);
                        this.log.info("step2 const:" + (System.currentTimeMillis() - start));
                        return crmtypeMap.values();
                    }

                    l = (CiLabelInfo) var10.next();
                } while (l.getCiLabelExtInfo().getCiMdaSysTableColumn() == null);
            } while (l.getCiLabelExtInfo().getCiMdaSysTableColumn().getCiMdaSysTable() == null);

            var19.put(l.getCiLabelExtInfo().getCiMdaSysTableColumn().getColumnName().toUpperCase(), l.getLabelId());
            String name = l.getCiLabelExtInfo().getCiMdaSysTableColumn().getCiMdaSysTable().getTableName() + "_";
            if (l.getUpdateCycle().intValue() == 1) {
                name = name + CacheBase.getInstance().getNewLabelDay();
            } else if (l.getUpdateCycle().intValue() == 2) {
                name = name + CacheBase.getInstance().getNewLabelMonth();
            }

            String prefix = Configure.getInstance().getProperty("QUERY_USER_LABELS_TABLES_PREFIX");
            if (StringUtils.isEmpty(prefix)) {
                prefix = "DW_COC_LABEL_USER_003,DW_COC_LABEL_USER_004,DW_COC_LABEL_USER_005";
            }

            String[] pre = prefix.split(",");
            boolean isStartWithPrefix = false;
            String[] var18 = pre;
            int var17 = pre.length;

            for (int var16 = 0; var16 < var17; ++var16) {
                String key = var18[var16];
                if (key != null && name.toUpperCase().startsWith(key.toUpperCase())) {
                    isStartWithPrefix = true;
                    break;
                }
            }

            if (!var20.contains(name) && isStartWithPrefix) {
                var20.add(name);
            }
        }
    }

    private void getAllLabelsFromTable(String productNo, List<String> tableNams, Map<String, CrmTypeModel> crmtypeMap, Map<String, Integer> labelMap) {
        String column = Configure.getInstance().getProperty("RELATED_COLUMN");
        Iterator var7 = tableNams.iterator();

        label67:
        while (true) {
            Map labels;
            do {
                if (!var7.hasNext()) {
                    return;
                }

                String tableName = (String) var7.next();
                StringBuffer sqlBuf = new StringBuffer();
                sqlBuf.append("select * from ").append(tableName).append(" where ").append(column).append("=?");
                labels = this.ciCustomListInfoJDao.selectBackForMap(sqlBuf.toString(), new Object[]{productNo});
            } while (labels == null);

            Iterator var11 = labels.entrySet().iterator();

            while (true) {
                while (true) {
                    int labelValue;
                    CiLabelInfo labelInfo;
                    do {
                        Entry en;
                        do {
                            if (!var11.hasNext()) {
                                continue label67;
                            }

                            en = (Entry) var11.next();
                            labelValue = 0;

                            try {
                                labelValue = Integer.valueOf(en.getValue().toString()).intValue();
                            } catch (Exception var17) {
                                ;
                            }
                        } while (!labelMap.containsKey(((String) en.getKey()).toString().toUpperCase()));

                        labelInfo = CacheBase.getInstance().getEffectiveLabel(((Integer) labelMap.get(((String) en.getKey()).toString().toUpperCase())).toString());
                    } while (labelInfo == null);

                    String typeId = null;
                    if (labelInfo.getCiLabelExtInfo().getLabelLevel().intValue() == 3) {
                        typeId = labelInfo.getLabelId().toString();
                        if (labelValue == 0) {
                            crmtypeMap.remove(typeId);
                        } else {
                            this.log.info("Not null level3 id:" + typeId);
                        }
                    } else if (labelInfo.getCiLabelExtInfo().getLabelLevel().intValue() != 4) {
                        if (labelInfo.getCiLabelExtInfo().getLabelLevel().intValue() == 5) {
                            if (labelValue == 0) {
                                continue;
                            }

                            CiLabelInfo model = CacheBase.getInstance().getEffectiveLabel(labelInfo.getParentId().toString());
                            CiLabelInfo lv3 = CacheBase.getInstance().getEffectiveLabel(model.getParentId().toString());
                            typeId = lv3.getLabelId().toString();
                        }

                        if (crmtypeMap.containsKey(typeId)) {
                            CrmLabelOrIndexModel model1 = new CrmLabelOrIndexModel();
                            model1.setId(labelInfo.getLabelId().toString());
                            model1.setLabel(true);
                            model1.setName(labelInfo.getLabelName());
                            model1.setValue(String.valueOf(labelValue));
                            model1.setHighLight(false);
                            model1.setBusiCaliber(labelInfo.getBusiCaliber());
                            ((CrmTypeModel) crmtypeMap.get(typeId)).addDisplayLabelOrIndex(model1);
                        }
                    }
                }
            }
        }
    }

    public List<CiSysInfo> queryCiSysInfo(CiCustomGroupInfo cg) {
        List result = this.queryCiSysInfo(1);
        ArrayList removeCiSysInfo = new ArrayList();
        if (cg.getUpdateCycle().intValue() == 2 || cg.getUpdateCycle().intValue() == 3) {
            for (int i = 0; i < result.size(); ++i) {
                CiSysInfo item = (CiSysInfo) result.get(i);
                if (item.getIsNeedCycle() == null || item.getIsNeedCycle().intValue() == 0) {
                    removeCiSysInfo.add(item);
                }
            }

            result.removeAll(removeCiSysInfo);
        }

        return result;
    }

    public void saveCiCustomGroupPushCycle(CiCustomGroupPushCycle pushCycle) {
        this.ciCustomGroupInfoHDao.insertCustomGroupPushCycle(pushCycle);
    }

    public void deleteCiCustomGroupPushCycleByCustomGroupId(String customGroupId) {
        this.ciCustomGroupInfoHDao.deleteCiCustomGroupPushCycleByCustomGroupId(customGroupId);
    }

    public List<CiCustomGroupPushCycle> queryPushCycleByGroupId(String customGroupId) {
        return this.ciCustomGroupInfoHDao.selectushCycleByGroupId(customGroupId);
    }

    public List<CiCustomGroupPushCycle> queryAllPushCycleByGroupId(String customGroupId) {
        return this.ciCustomGroupInfoHDao.selectPushCycleByGroupId(customGroupId);
    }

    public boolean importCustomListFile(File file, String fileName, String userId, CiCustomGroupInfo groupInfo, List<com.ailk.biapp.ci.model.Column> colList, Integer isHaveAttrTitle) throws Exception {
        boolean success = false;
        CiCustomFileRel customFileModel = new CiCustomFileRel();
        if (file != null) {
            String customGroupFilePath = null;

            try {
                customGroupFilePath = CiUtil.uploadTargetUserFile(file, fileName);
                if ("-1".equals(customGroupFilePath)) {
                    throw new Exception("上传文件目录不存在");
                }
            } catch (Exception var15) {
                success = false;
                this.log.error("获取文件目录信息报错", var15);
            }

            ArrayList ciGroupAttrRelList = new ArrayList();
            groupInfo.setCreateUserId(userId);
            groupInfo.setCreateTypeId(Integer.valueOf(7));
            groupInfo.setCreateTime(new Timestamp((new Date()).getTime()));
            groupInfo.setStatus(Integer.valueOf(1));
            groupInfo.setDataStatus(Integer.valueOf(1));
            groupInfo.setCustomNum((Long) null);
            groupInfo.setUpdateCycle(Integer.valueOf(1));
            groupInfo.setIsHasList(Integer.valueOf(1));
            this.genGroupAttrRelList(customGroupFilePath, colList, ciGroupAttrRelList, isHaveAttrTitle);
            this.addCiCustomGroupInfo(groupInfo, (List) null, (List) null, (CiTemplateInfo) null, userId, false, ciGroupAttrRelList);
            customFileModel.setCustomGroupId(groupInfo.getCustomGroupId());
            customFileModel.setFileUrl(customGroupFilePath);
            String ext = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            customFileModel.setFileName(fileName);
            customFileModel.setFileType(ext);
            customFileModel.setFileStartTime(new Timestamp((new Date()).getTime()));
            CustomGroupUpLoadFileThread upLoadThread = null;

            try {
                upLoadThread = (CustomGroupUpLoadFileThread) SystemServiceLocator.getInstance().getService("customGroupUpLoadFileThread");
                upLoadThread.setCiGroupAttrRelList(ciGroupAttrRelList);
                upLoadThread.setHasTitle(isHaveAttrTitle.intValue() == 1);
                upLoadThread.setUserSession(PrivilegeServiceUtil.getUserSession());
                upLoadThread.setCiCustomFileRel(customFileModel);
                upLoadThread.setCustomGroupInfo(groupInfo);
                ThreadPool.getInstance().execute(upLoadThread);
            } catch (Exception var14) {
                success = false;
                this.log.error("导入客户群异常", var14);
            }

            success = true;
        }

        return success;
    }

    private void genGroupAttrRelList(String customGroupFilePath, List<com.ailk.biapp.ci.model.Column> colList, List<CiGroupAttrRel> ciGroupAttrRelList, Integer isHaveAttrTitle) throws Exception {
        if (isHaveAttrTitle.intValue() == 1) {
            String i = CiUtil.getTxtType(new File(customGroupFilePath));
            BufferedReader item = new BufferedReader(new InputStreamReader(new FileInputStream(customGroupFilePath), i));
            String ciGroupAttrRel = item.readLine();
            if (ciGroupAttrRel != null) {
                String[] attrTempArr = ciGroupAttrRel.split(",");
                int length = 21;
                if (attrTempArr.length < 21) {
                    length = attrTempArr.length;
                }

                for (int i1 = 1; i1 < length; ++i1) {
                    String item1 = attrTempArr[i1];
                    if (StringUtil.isEmpty(item1)) {
                        item1 = "默认值" + i1;
                    }

                    CiGroupAttrRel ciGroupAttrRel1 = this.genGroupAttrRel(i1, item1);
                    ciGroupAttrRelList.add(ciGroupAttrRel1);
                }
            }

            item.close();
        } else {
            for (int var13 = 0; var13 < colList.size(); ++var13) {
                String var14 = ((com.ailk.biapp.ci.model.Column) colList.get(var13)).getColumnName();
                CiGroupAttrRel var15 = this.genGroupAttrRel(var13, var14);
                ciGroupAttrRelList.add(var15);
            }
        }

    }

    private CiGroupAttrRel genGroupAttrRel(int i, String item) {
        CiGroupAttrRelId ciGroupAttrRelId = new CiGroupAttrRelId();
        CiGroupAttrRel ciGroupAttrRel = new CiGroupAttrRel();
        ciGroupAttrRelId.setAttrCol(CiUtil.genCustomAttrColumnName(i));
        ciGroupAttrRel.setAttrColName(item);
        ciGroupAttrRel.setId(ciGroupAttrRelId);
        String dbType = Configure.getInstance().getProperty("CI_DBTYPE");
        if ("ORACLE".equalsIgnoreCase(dbType)) {
            ciGroupAttrRel.setAttrColType("VARCHAR2(512)");
        } else {
            ciGroupAttrRel.setAttrColType("VARCHAR(512)");
        }

        ciGroupAttrRel.setAttrSource(1);
        return ciGroupAttrRel;
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<Map<String, Object>> queryCustomerPhoneNumList(int pageNum, int pageSize, CiCustomGroupInfo ciCustomGroupInfo) {
        Object result = new ArrayList();
        List ciCustomListInfoList = this.ciCustomListInfoHDao.selectByCustomGroupId(ciCustomGroupInfo.getCustomGroupId());
        if (ciCustomListInfoList != null && ciCustomListInfoList.size() > 0) {
            CiCustomListInfo groupListInfo = (CiCustomListInfo) ciCustomListInfoList.get(0);
            if (groupListInfo != null && groupListInfo.getDataStatus().intValue() == 3) {
                result = this.ciCustomListInfoJDao.selectGroupListData(pageNum, pageSize, groupListInfo.getListTableName());
                String hasPrivacy = Configure.getInstance().getProperty("PRODUCT_NO_HAS_PRIVACY");
                Map map;
                String productNo;
                if (StringUtil.isNotEmpty(hasPrivacy) && "true".equalsIgnoreCase(hasPrivacy) && result != null && ((List) result).size() != 0) {
                    for (Iterator var9 = ((List) result).iterator(); var9.hasNext(); map.put(ServiceConstants.MAINTABLE_KEYCOLUMN, productNo)) {
                        map = (Map) var9.next();
                        productNo = (String) map.get(ServiceConstants.MAINTABLE_KEYCOLUMN);
                        if (StringUtil.isNotEmpty(productNo)) {
                            if (productNo.length() > 3) {
                                String start = productNo.substring(0, 3);
                                String end = productNo.substring(3, productNo.length());
                                if (end.length() >= 4) {
                                    end = end.replaceFirst("[0-9]{4}", "****");
                                } else {
                                    end = end.replaceFirst("[0-9]", "*");
                                }

                                productNo = start + end;
                            }
                        } else {
                            productNo = "";
                        }
                    }
                }
            }
        }

        return (List) result;
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public Map<String, Object> isShareNameExist(CiCustomGroupInfo ciCustomGroupInfo, String userId, String createCityId) throws CIServiceException {
        boolean success = false;
        HashMap returnMap = new HashMap();
        String msg = "";

        try {
            String e = ciCustomGroupInfo.getCustomGroupName();
            if (StringUtil.isEmpty(e)) {
                msg = "客户群名称不能为空！";
            } else {
                success = true;
            }

            if (success) {
                List list = this.queryCiCustomGroupInfoListByShareName(e);
                if (list != null && list.size() > 0) {
                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        CiCustomGroupInfo customGroupInfo = (CiCustomGroupInfo) iterator.next();
                        if (!customGroupInfo.getCustomGroupId().equals(ciCustomGroupInfo.getCustomGroupId())) {
                            msg = "输入的客户群名称重名，请重新输入！";
                            success = false;
                            break;
                        }
                    }
                }
            }

            returnMap.put("success", Boolean.valueOf(success));
            returnMap.put("msg", msg);
        } catch (Exception var11) {
            msg = "重名验证错误！";
            this.log.error(msg, var11);
            success = false;
            returnMap.put("success", Boolean.valueOf(success));
            returnMap.put("msg", msg);
        }

        return returnMap;
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiCustomGroupInfo> queryCiCustomGroupInfoListByShareName(String customGroupName) throws CIServiceException {
        List list = null;

        try {
            list = this.ciCustomGroupInfoHDao.selectCiCustomGroupInfoListByShareName(customGroupName);
            return list;
        } catch (Exception var5) {
            String message = "根据名称及用户Id查询客户群错误";
            this.log.error(message, var5);
            throw new CIServiceException(message, var5);
        }
    }

    public boolean modifyCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo, String userId) throws CIServiceException {
        boolean flag = false;

        try {
            CiCustomModifyHistory e = this.addCiCustomModifyHistory(ciCustomGroupInfo, userId);
            e.setModifyTime(new Date());
            ciCustomGroupInfo.setNewModifyTime(new Date());
            this.ciCustomGroupInfoHDao.insertCustomGroup(ciCustomGroupInfo);
            this.ciCustomModifyHistoryHDao.insertCiCustomModifyHistory(e);
            flag = true;
            return flag;
        } catch (Exception var6) {
            String message = "分析客户群错误";
            this.log.error(message, var6);
            throw new CIServiceException(message, var6);
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiLabelInfo> queryCiLabelInfoList4SaveCustom() {
        return this.queryCiLabelInfoList4SaveCustom((String) null, (String) null, (Integer) null);
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiLabelInfo> queryCiLabelInfoList4SaveCustom(String labelName, String parentId, Integer labelLevel) {
        return this.queryLabelInfoByNameOrLabelIdsOrParentId(labelName, (String) null, parentId, labelLevel);
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiGroupAttrRel> queryCustomGroupAttrRelList(List<CiLabelRule> ciLabelRuleList) {
        return this.queryCustomGroupAttrRelList(ciLabelRuleList, (String) null);
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiGroupAttrRel> queryCustomGroupAttrRelList(List<CiLabelRule> ciLabelRuleList, String attrName) {
        if (ciLabelRuleList != null && ciLabelRuleList.size() != 0) {
            ArrayList attrRelResultList = new ArrayList();
            Iterator var5 = ciLabelRuleList.iterator();

            while (true) {
                CiLabelRule ciLabelRule;
                int elementType;
                do {
                    do {
                        if (!var5.hasNext()) {
                            return attrRelResultList;
                        }

                        ciLabelRule = (CiLabelRule) var5.next();
                        elementType = ciLabelRule.getElementType().intValue();
                    } while (5 != elementType);
                } while (!StringUtil.isNotEmpty(ciLabelRule.getCalcuElement()));

                String listTableName = ciLabelRule.getCalcuElement();
                List attrRelsList = this.queryAttrRelByCustomerID(listTableName, attrName);
                CiCustomListInfo listInfo = this.queryCiCustomListInfoById(listTableName);
                Iterator var11 = attrRelsList.iterator();

                while (var11.hasNext()) {
                    CiGroupAttrRel ciGroupAttrRel = (CiGroupAttrRel) var11.next();
                    ciGroupAttrRel.setDataDateStr(listInfo.getDataDate());
                }

                attrRelResultList.addAll(attrRelsList);
            }
        } else {
            return null;
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiGroupAttrRel> queryCustomGroupAttrRelList(List<CiLabelRule> ciLabelRuleList, String attrName, String customId, String customListDate) {
        if (ciLabelRuleList != null && ciLabelRuleList.size() != 0) {
            ArrayList attrRelResultList = new ArrayList();
            Iterator var7 = ciLabelRuleList.iterator();

            while (true) {
                String listTableName;
                CiCustomListInfo listInfo;
                do {
                    CiLabelRule ciLabelRule;
                    do {
                        int elementType;
                        do {
                            do {
                                if (!var7.hasNext()) {
                                    return attrRelResultList;
                                }

                                ciLabelRule = (CiLabelRule) var7.next();
                                elementType = ciLabelRule.getElementType().intValue();
                            } while (5 != elementType);
                        } while (!customId.equals(ciLabelRule.getCustomId()) && !customId.equals(""));
                    } while (!StringUtil.isNotEmpty(ciLabelRule.getCalcuElement()));

                    listTableName = ciLabelRule.getCalcuElement();
                    listInfo = this.queryCiCustomListInfoById(listTableName);
                } while (!listInfo.getDataDate().equals(customListDate) && !customListDate.equals(""));

                List attrRelsList = this.queryAttrRelByCustomerID(listTableName, attrName);
                Iterator var13 = attrRelsList.iterator();

                while (var13.hasNext()) {
                    CiGroupAttrRel ciGroupAttrRel = (CiGroupAttrRel) var13.next();
                    ciGroupAttrRel.setDataDateStr(listInfo.getDataDate());
                }

                attrRelResultList.addAll(attrRelsList);
            }
        } else {
            return null;
        }
    }

    public List<CiGroupAttrRel> queryAttrRelByCustomerID(String listTableName, String attrName) {
        Object attrRelList = new ArrayList();

        try {
            CiCustomListInfo e = this.queryCiCustomListInfoById(listTableName);
            String groupId = e.getCustomGroupId();
            if (StringUtil.isNotEmpty(groupId)) {
                groupId = "\'" + groupId + "\'";
                List ciGroupAttrRel = this.ciCustomListInfoJDao.selectGroupAttrRelByCustomeIds(groupId, e.getDataTime(), attrName);
                if (ciGroupAttrRel == null || ciGroupAttrRel.size() != 1 || ciGroupAttrRel.get(0) == null || ((CiGroupAttrRel) ciGroupAttrRel.get(0)).getStatus() == null || ((CiGroupAttrRel) ciGroupAttrRel.get(0)).getStatus().intValue() != 2) {
                    attrRelList = ciGroupAttrRel;
                }
            }

            Iterator var7 = ((List) attrRelList).iterator();

            while (var7.hasNext()) {
                CiGroupAttrRel ciGroupAttrRel1 = (CiGroupAttrRel) var7.next();
                ciGroupAttrRel1.setLabelOrCustomId(listTableName);
            }
        } catch (Exception var8) {
            this.log.error("查询客户群属性列表失败", var8);
        }

        return (List) attrRelList;
    }

    public ReturnMsgModel modifyPushCustomerEndTime(String customGroupId, String sysId, Date endDate, String userId) {
        ReturnMsgModel returnMsgModel = new ReturnMsgModel();
        String msg = "客户群修改成功！";
        boolean success = false;

        try {
            label65:
            {
                CiCustomGroupInfo e = this.ciCustomGroupInfoHDao.selectCustomGroupById(customGroupId);
                if (e.getStatus().intValue() == 0) {
                    success = false;
                    msg = "该客户群已删除，不能修改！";
                    returnMsgModel.setMsg(msg);
                    returnMsgModel.setSuccess(success);
                    return returnMsgModel;
                }

                if (e.getCreateUserId() == null || !userId.equals(e.getCreateUserId())) {
                    success = false;
                    msg = "该客户群只能由其创建者才能删除！";
                    returnMsgModel.setMsg(msg);
                    returnMsgModel.setSuccess(success);
                    return returnMsgModel;
                }

                if (e.getDataStatus().intValue() != 2 && e.getDataStatus().intValue() != 1) {
                    if (e.getIsPrivate() != null && e.getIsPrivate().intValue() != 0) {
                        Date date = new Date();
                        int dateFlag = endDate.compareTo(date);
                        if (dateFlag < 0) {
                            success = false;
                            msg = "失效日期必须是今天（包括今天）之后的日期！";
                            returnMsgModel.setMsg(msg);
                            returnMsgModel.setSuccess(success);
                            return returnMsgModel;
                        }

                        int count = this.ciCustomListInfoJDao.selectCountPustReq(customGroupId, sysId);
                        if (count > 0) {
                            success = false;
                            msg = "其他系统正在使用该客户群，不能修改！";
                            returnMsgModel.setMsg(msg);
                            returnMsgModel.setSuccess(success);
                            return returnMsgModel;
                        }

                        e.setEndDate(endDate);
                        CiCustomModifyHistory history = this.addCiCustomModifyHistory(e, userId);
                        history.setModifyTime(date);
                        this.ciCustomGroupInfoHDao.insertCustomGroup(e);
                        this.ciCustomModifyHistoryHDao.insertCiCustomModifyHistory(history);
                        success = true;
                        msg = "客户群修改成功！";
                        break label65;
                    }

                    success = false;
                    msg = "客户群是公有客户群，不能进行修改！";
                    returnMsgModel.setMsg(msg);
                    returnMsgModel.setSuccess(success);
                    return returnMsgModel;
                }

                success = false;
                msg = "该客户群为待创建或创建中的客户群，不能修改！";
                returnMsgModel.setMsg(msg);
                returnMsgModel.setSuccess(success);
                return returnMsgModel;
            }
        } catch (Exception var13) {
            success = false;
            msg = "客户群群修改失败！";
            this.log.error(msg, var13);
        }

        returnMsgModel.setMsg(msg);
        returnMsgModel.setSuccess(success);
        this.log.info(msg);
        return returnMsgModel;
    }

    public ReturnMsgModel deleteCustomGroupById(String customGroupId, String userId) {
        ReturnMsgModel result = new ReturnMsgModel(true, "客户群删除成功！");
        CiCustomGroupInfo customGroupInfo = this.queryCiCustomGroupInfo(customGroupId);
        String msg = "";
        if (customGroupInfo.getCreateUserId() != null && userId.equals(customGroupInfo.getCreateUserId())) {
            if (customGroupInfo.getDataStatus().intValue() != 2 && customGroupInfo.getDataStatus().intValue() != 1) {
                if (customGroupInfo.getStatus().intValue() == 0) {
                    msg = "该客户群已删除，不能再进行删除！";
                    result.setMsg(msg);
                    result.setSuccess(false);
                    this.log.info(msg);
                    return result;
                } else {
                    Integer isPrivate = customGroupInfo.getIsPrivate();
                    if (isPrivate != null && isPrivate.intValue() == 1) {
                        int num = this.ciCustomListInfoJDao.selectCiLabelRuleCountByListTableName(customGroupId);
                        if (num > 0) {
                            result.setSuccess(false);
                            msg = "客户群已经被引用，不能删除！";
                            result.setMsg(msg);
                            this.log.info(msg);
                            return result;
                        } else {
                            this.deleteCiCustomGroupInfo(customGroupInfo);
                            return result;
                        }
                    } else {
                        result.setSuccess(false);
                        msg = "客户群是共享客户群，不能删除！";
                        result.setMsg(msg);
                        this.log.info(msg);
                        return result;
                    }
                }
            } else {
                msg = "该客户群为待创建或创建中的客户群，不能删除！";
                result.setMsg(msg);
                result.setSuccess(false);
                this.log.info(msg);
                return result;
            }
        } else {
            result.setSuccess(false);
            msg = "客户群只能是其创建者才能删除！";
            result.setMsg(msg);
            this.log.info(msg);
            return result;
        }
    }

    public void translateListTableCol(List<String> translationSqls) {
        this.customersJdbcDao.batchUpdateSql(translationSqls);
    }

    public String getSelectSqlByCustomersRels(List<CiGroupAttrRel> ciGroupAttrRelList, List<CiLabelRule> ciLabelRuleList, String monthLabelDate, String dayLabelDate, String dataDate, String userId, Integer interval, Integer updateCycle, boolean isValidate) {
        ArrayList listInfoIds = new ArrayList();
        StringBuffer calcExpr = new StringBuffer();
        HashMap labelRuleToSql = new HashMap();
        Integer duplicateLabelIdCount = Integer.valueOf(1);
        CacheBase cache = CacheBase.getInstance();
        boolean needCutFlag = false;
        boolean hasDailyLabel = false;
        String nowUserCityId = null;

        try {
            nowUserCityId = PrivilegeServiceUtil.getCityId(userId);
        } catch (Exception var26) {
            var26.printStackTrace();
            this.log.error("根据userId获得cityId失败");
        }

        Iterator needAuthority = ciLabelRuleList.iterator();

        while (needAuthority.hasNext()) {
            CiLabelRule sql = (CiLabelRule) needAuthority.next();
            if (sql.getElementType().intValue() == 1) {
                if ("OR".equalsIgnoreCase(sql.getCalcuElement())) {
                    calcExpr.append('∪');
                } else if ("AND".equalsIgnoreCase(sql.getCalcuElement())) {
                    calcExpr.append('∩');
                } else if ("-".equalsIgnoreCase(sql.getCalcuElement())) {
                    calcExpr.append('-');
                }
            } else if (sql.getElementType().intValue() == 3) {
                calcExpr.append(sql.getCalcuElement());
            } else {
                String listTableId;
                if (2 == sql.getElementType().intValue()) {
                    listTableId = "";
                    CiLabelInfo customGroupDataCycle = cache.getEffectiveLabel(sql.getCalcuElement());
                    if (1 == customGroupDataCycle.getUpdateCycle().intValue()) {
                        hasDailyLabel = true;
                    }

                    if (customGroupDataCycle.getLabelTypeId().intValue() == 8) {
                        try {
                            listTableId = this.getFromSqlStrForVerticalLabel(sql, monthLabelDate, dayLabelDate, false, userId, interval, updateCycle, isValidate);
                            StringBuffer isCenterCityUser = new StringBuffer();
                            isCenterCityUser.append("( select ").append(CommonConstants.sqlParallel).append(" distinct ").append(Configure.getInstance().getProperty("RELATED_COLUMN")).append(" ").append(listTableId).append(" ) ");
                            listTableId = isCenterCityUser.toString();
                        } catch (Exception var25) {
                            this.log.error("根据标签ID获得生成客户群的sql出错，标签Id为：" + sql.getCalcuElement(), var25);
                        }
                    } else {
                        ArrayList isCenterCityUser1 = new ArrayList();
                        isCenterCityUser1.add(sql);

                        try {
                            listTableId = this.getFromSqlStr(isCenterCityUser1, monthLabelDate, dayLabelDate, (List) null, false, userId, interval, updateCycle, isValidate);
                        } catch (Exception var24) {
                            this.log.error("根据标签ID获得生成客户群的sql出错，标签Id为：" + sql.getCalcuElement(), var24);
                        }
                    }

                    String isCenterCityUser2 = listTableId.trim();
                    if (isCenterCityUser2.startsWith("from")) {
                        listTableId = isCenterCityUser2.substring(4);
                    }

                    this.log.debug("根据标签ID获得混合运算生成客户群,标签Id为：" + sql.getCalcuElement() + ",生成的sql为：" + listTableId);
                    if (listInfoIds.contains(sql.getCalcuElement())) {
                        labelRuleToSql.put(sql.getCalcuElement() + "_" + duplicateLabelIdCount, listTableId);
                        calcExpr.append(sql.getCalcuElement() + "_" + duplicateLabelIdCount);
                        listInfoIds.add(sql.getCalcuElement());
                        duplicateLabelIdCount = Integer.valueOf(duplicateLabelIdCount.intValue() + 1);
                    } else {
                        labelRuleToSql.put(sql.getCalcuElement(), listTableId);
                        calcExpr.append(sql.getCalcuElement());
                        listInfoIds.add(sql.getCalcuElement());
                    }
                } else if (5 == sql.getElementType().intValue()) {
                    needCutFlag = true;
                    listTableId = sql.getCalcuElement();
                    Integer customGroupDataCycle1 = sql.getLabelFlag();
                    if (customGroupDataCycle1 != null && customGroupDataCycle1.intValue() == 2) {
                        hasDailyLabel = true;
                    }

                    calcExpr.append(listTableId);
                    listInfoIds.add(listTableId);
                    boolean isCenterCityUser3 = false;
                    String centerCity = Configure.getInstance().getProperty("CENTER_CITYID");
                    if (StringUtil.isNotEmpty(nowUserCityId)) {
                        isCenterCityUser3 = nowUserCityId.equals(centerCity);
                    }

                    if (isCenterCityUser3) {
                        needCutFlag = false;
                    }
                }
            }
        }

        if (listInfoIds != null && listInfoIds.size() != 0) {
            String sql1 = "";
            String needAuthority1;
            if (listInfoIds.size() == 1) {
                needAuthority1 = calcExpr.toString();
                needAuthority1 = needAuthority1.replace(String.valueOf('('), "").replace(String.valueOf(')'), "");
                if (labelRuleToSql.containsKey(needAuthority1)) {
                    sql1 = "select " + CommonConstants.sqlParallel + " " + Configure.getInstance().getProperty("RELATED_COLUMN") + " from " + (String) labelRuleToSql.get(needAuthority1);
                } else {
                    sql1 = "select " + CommonConstants.sqlParallel + " " + Configure.getInstance().getProperty("RELATED_COLUMN") + " from " + needAuthority1;
                }
            } else if (listInfoIds.size() > 1) {
                needAuthority1 = calcExpr.toString();
                sql1 = (new GroupCalcSqlPaser(Configure.getInstance().getProperty("CI_BACK_DBTYPE"))).parseExprToSql(needAuthority1, labelRuleToSql);
            }

            boolean needAuthority2 = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
            if (needAuthority2 && needCutFlag) {
                sql1 = this.getCutCenterCitySql(sql1, monthLabelDate, dayLabelDate, dataDate, userId, hasDailyLabel);
            }

            if (ciGroupAttrRelList != null && ciGroupAttrRelList.size() > 0) {
                sql1 = this.getMultiColSelectSql(sql1, ciGroupAttrRelList, monthLabelDate, dayLabelDate);
            }

            this.log.info("COC_SQL >>>>>>> " + sql1);
            return sql1;
        } else {
            throw new CIServiceException("listInfoIds is empty");
        }
    }

    private String getCutCenterCitySql(String sql, String monthLabelDate, String dayLabelDate, String dataDate, String userId, boolean hasDailyLabel) {
        StringBuffer resultSB = new StringBuffer();
        String relatedColumn = Configure.getInstance().getProperty("RELATED_COLUMN");
        String dwLabelFormTablePrefix = Configure.getInstance().getProperty("DW_LABEL_FORM_TABLE");
        String cityIdColumnName = Configure.getInstance().getProperty("CITY_ID");
        String countyIdColumnName = Configure.getInstance().getProperty("COUNTY_ID");
        String cityIdColumnType = Configure.getInstance().getProperty("CITY_COLUMN_TYPE");
        String countyIdColumnType = Configure.getInstance().getProperty("COUNTY_COLUMN_TYPE");
        resultSB.append("select needCutResult.").append(relatedColumn).append(" from ");
        if (hasDailyLabel) {
            if (dayLabelDate != null) {
                resultSB.append(dwLabelFormTablePrefix).append(dayLabelDate);
            } else {
                resultSB.append(dwLabelFormTablePrefix).append(dataDate);
            }
        } else if (monthLabelDate != null) {
            resultSB.append(dwLabelFormTablePrefix).append(monthLabelDate);
        } else {
            resultSB.append(dwLabelFormTablePrefix).append(dataDate);
        }

        resultSB.append(" dwLabelFormTable inner join ").append("(").append(sql).append(") needCutResult on ").append("dwLabelFormTable.").append(relatedColumn).append(" = needCutResult.").append(relatedColumn);
        String whereCity = null;

        String msg;
        try {
            StringBuffer e = new StringBuffer(" where ( ");
            if (StringUtil.isEmpty(userId)) {
                this.log.error("拼接sql时用户ID为空");
                throw new CIServiceException("拼接sql时用户ID为空");
            }

            msg = Configure.getInstance().getProperty("CENTER_CITYID");
            List icities = null;
            CacheBase cache = CacheBase.getInstance();
            icities = PrivilegeServiceUtil.getUserCityIds(userId);
            CopyOnWriteArrayList onlyCityIds = cache.getKeyList("CITY_IDS_LIST");

            for (int i = 0; i < icities.size(); ++i) {
                String cityId = (String) icities.get(i);
                if (cityId.equals(msg)) {
                    break;
                }

                if (onlyCityIds.contains(cityId)) {
                    e.append("dwLabelFormTable.").append(cityIdColumnName);
                    if (cityIdColumnType.contains("number")) {
                        e.append(" = ").append(cityId).append(" ");
                    } else {
                        e.append(" = \'").append(cityId).append("\' ");
                    }
                } else {
                    e.append("dwLabelFormTable.").append(countyIdColumnName);
                    if (countyIdColumnType.contains("number")) {
                        e.append(" = ").append(cityId).append(" ");
                    } else {
                        e.append(" = \'").append(cityId).append("\' ");
                    }
                }

                if (icities.size() > 1 && i != icities.size() - 1) {
                    e.append("or ");
                }
            }

            e.append(") ");
            whereCity = e.toString();
        } catch (Exception var22) {
            msg = "根据userId拼接sql时地市条件错误";
            this.log.error(msg, var22);
            throw new CIServiceException(msg, var22);
        }

        resultSB.append(whereCity);
        return resultSB.toString();
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public CiCustomGroupInfo queryCiCustomGroupInfoByListInfoId(String listInfoId) {
        CiCustomGroupInfo groupInfo = null;

        try {
            groupInfo = this.customersJdbcDao.queryCiCustomGroupInfoByListInfoId(listInfoId);
        } catch (Exception var4) {
            this.log.error("根据清单id查询客户群错误", var4);
        }

        return groupInfo;
    }

    private String getMultiColSelectSql(String sql, List<CiGroupAttrRel> ciGroupAttrRelList, String monthLabelDate, String dayLabelDate) {
        CacheBase cache = CacheBase.getInstance();
        StringBuffer resultSB = new StringBuffer();
        StringBuffer selectColumnsSB = new StringBuffer();
        StringBuffer fromTableNameSB = new StringBuffer(" ");
        LinkedHashSet tableNameList = new LinkedHashSet();
        HashMap vertAttrTableNameMap = new HashMap();
        String dbType = Configure.getInstance().getProperty("CI_BACK_DBTYPE");
        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(dbType);
        Iterator i = ciGroupAttrRelList.iterator();

        while (true) {
            CiGroupAttrRel relatedColumn;
            CiMdaSysTableColumn column;
            String columnName;
            String columnNameForTableName;
            StringBuffer vertAttrTableName;
            String attrValsStr;
            String[] vals;
            StringBuffer wherelabel;
            int k;
            CiLabelInfo var37;
            String var41;
            String var42;
            do {
                do {
                    while (true) {
                        int var35;
                        do {
                            label153:
                            do {
                                String lastAlias;
                                String tableNames;
                                while (i.hasNext()) {
                                    relatedColumn = (CiGroupAttrRel) i.next();
                                    if (relatedColumn.getAttrSource() == 2) {
                                        var35 = relatedColumn.getIsVerticalAttr();
                                        lastAlias = relatedColumn.getLabelOrCustomId();
                                        var37 = cache.getEffectiveLabel(lastAlias);
                                        continue label153;
                                    }

                                    if (relatedColumn.getAttrSource() == 3) {
                                        String it = relatedColumn.getLabelOrCustomId();
                                        lastAlias = relatedColumn.getLabelOrCustomColumn();
                                        tableNames = it.toLowerCase();
                                        String tableName = tableNames + "." + lastAlias;
                                        selectColumnsSB.append(",").append(tableName);
                                        tableNameList.add(it.toUpperCase() + " " + tableNames);
                                    }
                                }

                                String var33 = Configure.getInstance().getProperty("RELATED_COLUMN");
                                int var34 = 0;
                                Iterator var36 = tableNameList.iterator();

                                for (lastAlias = ""; var36.hasNext(); ++var34) {
                                    tableNames = (String) var36.next();
                                    String[] var39 = tableNames.split(" ");
                                    if (var39.length < 2) {
                                        throw new CIServiceException("没有别名");
                                    }

                                    if (vertAttrTableNameMap.containsKey(tableNames)) {
                                        tableNames = (String) vertAttrTableNameMap.get(tableNames);
                                    }

                                    if (var34 == 0) {
                                        fromTableNameSB.append("left join ").append(tableNames).append(" on ").append("mainResult.").append(var33).append(" = ").append(var39[1]).append(".").append(var33).append(" ");
                                    } else {
                                        fromTableNameSB.append("left join ").append(tableNames).append(" on ").append(lastAlias).append(".").append(var33).append(" = ").append(var39[1]).append(".").append(var33).append(" ");
                                    }

                                    lastAlias = var39[1];
                                }

                                resultSB.append("select mainResult.").append(var33).append(selectColumnsSB).append(" from ").append("(").append(sql).append(") mainResult ").append(fromTableNameSB);
                                return resultSB.toString();
                            } while (var37 == null);
                        } while (var37.getDataStatusId().intValue() != 2);

                        CiLabelExtInfo var38 = var37.getCiLabelExtInfo();
                        column = null;
                        CiLabelVerticalColumnRel v = null;
                        if (var35 == 1) {
                            String table = relatedColumn.getLabelOrCustomColumn();
                            Set tableName1 = var38.getCiLabelVerticalColumnRels();
                            Iterator tablePostfix = tableName1.iterator();

                            while (tablePostfix.hasNext()) {
                                CiLabelVerticalColumnRel alias = (CiLabelVerticalColumnRel) tablePostfix.next();
                                if (table.equals(alias.getId().getColumnId().toString())) {
                                    v = alias;
                                    column = alias.getCiMdaSysTableColumn();
                                    break;
                                }
                            }
                        } else {
                            column = var38.getCiMdaSysTableColumn();
                        }

                        CiMdaSysTable var40 = column.getCiMdaSysTable();
                        var41 = var40.getTableName();
                        var42 = "t_" + var40.getTableId();
                        String asName;
                        String var43;
                        if (1 == var37.getUpdateCycle().intValue()) {
                            if (StringUtil.isEmpty(dayLabelDate)) {
                                dayLabelDate = CacheBase.getInstance().getNewLabelDay();
                                var43 = var37.getDataDate();
                                if (StringUtil.isNotEmpty(dayLabelDate) && StringUtil.isNotEmpty(var43)) {
                                    dayLabelDate = DateUtil.getEarlierDate(dayLabelDate, var43, "yyyyMMdd");
                                }

                                this.log.info("使用日标签时，日期为空");
                            }

                            var43 = "_" + dayLabelDate;
                            var41 = var41 + var43;
                            var42 = var42 + "_" + dayLabelDate;
                            asName = var42 + "." + column.getColumnName();
                            columnName = column.getColumnName();
                            columnNameForTableName = column.getColumnName();
                            if (5 == var37.getLabelTypeId().intValue() && 1 == column.getColumnDataTypeId().intValue()) {
                                asName = dataBaseAdapter.getColumnToChar(asName);
                                columnNameForTableName = dataBaseAdapter.getColumnToChar(columnNameForTableName);
                            } else if (8 == var37.getLabelTypeId().intValue() && v != null && 5 == v.getLabelTypeId().intValue() && 1 == column.getColumnDataTypeId().intValue()) {
                                asName = dataBaseAdapter.getColumnToChar(asName);
                                columnNameForTableName = dataBaseAdapter.getColumnToChar(columnNameForTableName);
                            }

                            selectColumnsSB.append(",").append(asName);
                            tableNameList.add(var41.toUpperCase() + " " + var42);
                            break;
                        }

                        if (2 == var37.getUpdateCycle().intValue()) {
                            if (StringUtil.isEmpty(monthLabelDate)) {
                                monthLabelDate = CacheBase.getInstance().getNewLabelMonth();
                                var43 = var37.getDataDate();
                                if (StringUtil.isNotEmpty(monthLabelDate) && StringUtil.isNotEmpty(var43)) {
                                    monthLabelDate = DateUtil.getEarlierDate(monthLabelDate, var43, "yyyyMM");
                                }

                                this.log.warn("使用月标签时，月份为空");
                            }

                            var43 = "_" + monthLabelDate;
                            var41 = var41 + var43;
                            var42 = var42 + "_" + monthLabelDate;
                            asName = var42 + "." + column.getColumnName();
                            columnName = column.getColumnName();
                            columnNameForTableName = column.getColumnName();
                            if (5 == var37.getLabelTypeId().intValue() && 1 == column.getColumnDataTypeId().intValue()) {
                                asName = dataBaseAdapter.getColumnToChar(asName);
                                columnNameForTableName = dataBaseAdapter.getColumnToChar(columnNameForTableName);
                            } else if (8 == var37.getLabelTypeId().intValue() && v != null && 5 == v.getLabelTypeId().intValue() && 1 == column.getColumnDataTypeId().intValue()) {
                                asName = dataBaseAdapter.getColumnToChar(asName);
                                columnNameForTableName = dataBaseAdapter.getColumnToChar(columnNameForTableName);
                            }

                            selectColumnsSB.append(",").append(asName);
                            tableNameList.add(var41.toUpperCase() + " " + var42);
                            if (8 == var37.getLabelTypeId().intValue() && (StringUtil.isNotEmpty(relatedColumn.getAttrVal()) || StringUtil.isNotEmpty(relatedColumn.getTableName()))) {
                                vertAttrTableName = new StringBuffer("(select * from " + var41.toUpperCase() + " where 1=1 ");
                                if (StringUtil.isNotEmpty(relatedColumn.getTableName())) {
                                    attrValsStr = "VALUE_ID";
                                    vertAttrTableName.append("and ").append(columnNameForTableName).append(" in (select ").append(attrValsStr).append(" from ").append(relatedColumn.getTableName()).append(" ))").append(" ").append(var42);
                                } else {
                                    attrValsStr = relatedColumn.getAttrVal();
                                    vals = attrValsStr.split(",");
                                    wherelabel = new StringBuffer(" and ");
                                    if (vals.length == 1) {
                                        if (2 == column.getColumnDataTypeId().intValue()) {
                                            wherelabel.append(" ").append(columnName).append(" = \'").append(vals[0]).append("\'");
                                        } else if (1 == column.getColumnDataTypeId().intValue()) {
                                            wherelabel.append(" ").append(columnName).append(" = ").append(vals[0]);
                                        }
                                    } else {
                                        if (attrValsStr.length() == attrValsStr.lastIndexOf(",")) {
                                            attrValsStr = attrValsStr.substring(0, attrValsStr.length() - 1);
                                        }

                                        if (2 != column.getColumnDataTypeId().intValue()) {
                                            if (1 == column.getColumnDataTypeId().intValue()) {
                                                wherelabel.append(" ").append(columnName).append(" in (").append(attrValsStr).append(")");
                                            }
                                        } else {
                                            wherelabel.append(" ").append(columnName).append(" in ( ");

                                            for (k = 0; k < vals.length; ++k) {
                                                wherelabel.append("\'" + vals[k] + "\'");
                                                if (k != vals.length - 1) {
                                                    wherelabel.append(",");
                                                }
                                            }

                                            wherelabel.append(" )");
                                        }
                                    }

                                    vertAttrTableName.append(wherelabel + ")" + " " + var42);
                                }

                                vertAttrTableNameMap.put(var41.toUpperCase() + " " + var42, vertAttrTableName.toString());
                            }
                        }
                    }
                } while (8 != var37.getLabelTypeId().intValue());
            }
            while (!StringUtil.isNotEmpty(relatedColumn.getAttrVal()) && !StringUtil.isNotEmpty(relatedColumn.getTableName()));

            vertAttrTableName = new StringBuffer("(select * from " + var41.toUpperCase() + " where 1=1 ");
            if (StringUtil.isNotEmpty(relatedColumn.getTableName())) {
                attrValsStr = "VALUE_ID";
                vertAttrTableName.append("and ").append(columnNameForTableName).append(" in (select ").append(attrValsStr).append(" from ").append(relatedColumn.getTableName()).append(" ))").append(" ").append(var42);
            } else {
                attrValsStr = relatedColumn.getAttrVal();
                vals = attrValsStr.split(",");
                wherelabel = new StringBuffer(" and ");
                if (vals.length == 1) {
                    if (2 == column.getColumnDataTypeId().intValue()) {
                        wherelabel.append(" ").append(columnName + " = \'" + vals[0] + "\'");
                    } else if (1 == column.getColumnDataTypeId().intValue()) {
                        wherelabel.append(" ").append(columnName).append(" = ").append(vals[0]);
                    }
                } else {
                    if (attrValsStr.length() == attrValsStr.lastIndexOf(",")) {
                        attrValsStr = attrValsStr.substring(0, attrValsStr.length() - 1);
                    }

                    if (2 == column.getColumnDataTypeId().intValue()) {
                        wherelabel.append(" ").append(columnName).append(" in ( ");

                        for (k = 0; k < vals.length; ++k) {
                            wherelabel.append("\'" + vals[k] + "\'");
                            if (k != vals.length - 1) {
                                wherelabel.append(",");
                            }
                        }

                        wherelabel.append(" )");
                    } else if (1 == column.getColumnDataTypeId().intValue()) {
                        wherelabel.append(" ").append(columnName).append(" in (").append(attrValsStr).append(")");
                    }
                }

                vertAttrTableName.append(wherelabel).append(")").append(" ").append(var42);
            }

            vertAttrTableNameMap.put(var41.toUpperCase() + " " + var42, vertAttrTableName.toString());
        }
    }

    public void deleteCiCustomSceneRel(CiCustomSceneRel ciCustomSceneRel) throws CIServiceException {
        try {
            ciCustomSceneRel.setStatus(0);
            this.ciCustomSceneHDao.insertCiCustomSceneRel(ciCustomSceneRel);
        } catch (Exception var3) {
            this.log.error("deleteCiCustomSceneRel " + ciCustomSceneRel, var3);
            throw new CIServiceException("删除客户群关联场景失败");
        }
    }

    public void userAttentionCustom(String customGroupId, String userId) throws CIServiceException {
        try {
            Date e = new Date();
            CiUserAttentionCustom ciUserAttentionCustom = new CiUserAttentionCustom();
            CiUserAttentionCustomId ciUserAttentionCustomId = new CiUserAttentionCustomId();
            ciUserAttentionCustomId.setUserId(userId);
            ciUserAttentionCustomId.setCustomId(customGroupId);
            ciUserAttentionCustom.setId(ciUserAttentionCustomId);
            ciUserAttentionCustom.setAttentionTime(e);
            this.ciUserAttentionCustomHDao.insertOrUpdateCiUserAttentionCustom(ciUserAttentionCustom);
        } catch (Exception var6) {
            this.log.error("增加客户群关注失败", var6);
            throw new CIServiceException("增加客户群关注失败");
        }
    }

    public void deleteUserAttentionCustom(String customGroupId, String userId) throws CIServiceException {
        try {
            CiUserAttentionCustom e = new CiUserAttentionCustom();
            CiUserAttentionCustomId ciUserAttentionCustomId = new CiUserAttentionCustomId();
            ciUserAttentionCustomId.setUserId(userId);
            ciUserAttentionCustomId.setCustomId(customGroupId);
            e.setId(ciUserAttentionCustomId);
            this.ciUserAttentionCustomHDao.deleteCiUserAttentionCustom(e);
        } catch (Exception var5) {
            this.log.error("删除客户群关注记录失败", var5);
            throw new CIServiceException("删除客户群关注记录失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public Long getUserAttentionCustomserCount(CiCustomGroupInfo bean) throws CIServiceException {
        long count;
        try {
            count = this.customersJdbcDao.getUserAttentionCustomserCount(bean).longValue();
        } catch (Exception var5) {
            this.log.error("查询用户收藏客户群总数失败", var5);
            throw new CIServiceException("查询用户收藏客户群总数失败");
        }

        return Long.valueOf(count);
    }

    public List<CiCustomGroupInfo> getSysRecommendCustomInfos(CiCustomGroupInfo searchBeanCustom) {
        List recommendCustomInfos = null;

        try {
            recommendCustomInfos = this.customersJdbcDao.getSysRecommendCustomInfoList(searchBeanCustom);
            CacheBase e = CacheBase.getInstance();
            Iterator var5 = recommendCustomInfos.iterator();

            while (var5.hasNext()) {
                CiCustomGroupInfo ciCustomGroupInfo = (CiCustomGroupInfo) var5.next();
                CiCustomGroupInfo info = e.getHotCustomByKey("HOT_CUSTOMS", ciCustomGroupInfo.getCustomGroupId());
                if (info != null) {
                    ciCustomGroupInfo.setIsHotCustom("true");
                }
            }

            return recommendCustomInfos;
        } catch (Exception var7) {
            this.log.error("查询系统推荐客户群列表失败", var7);
            throw new CIServiceException("查询系统推荐客户群列表失败");
        }
    }

    public CiCustomGroupInfo queryCiCustomGroupInfoAndAttention(CiCustomGroupInfo info) {
        return this.customersJdbcDao.selectCustomGroupAndAttention(info);
    }

    public void dropTable(String tableName) {
        this.ciCustomListInfoJDao.dropTable(tableName);
    }

    public boolean checkIfDailyCustomGroupCanCreate(String userId) {
        CacheBase cache = CacheBase.getInstance();
        Map cityCustomGroupNumMap = cache.getCityCustomGroupNumMap();
        String cityId = null;

        try {
            cityId = PrivilegeServiceUtil.getCityId(userId);
        } catch (Exception var9) {
            this.log.error("根据userId获得cityId失败");
            var9.printStackTrace();
        }

        Integer configNum = null;

        try {
            configNum = (Integer) cityCustomGroupNumMap.get(cityId);
        } catch (Exception var8) {
            this.log.error("根据cityId获得配置的可创建日客户群数量失败");
            var8.printStackTrace();
        }

        if (configNum == null) {
            String bean = Configure.getInstance().getProperty("DEFAULT_DAILY_CUSTOM_GROUP_NUM");
            if (StringUtil.isNotEmpty(bean)) {
                configNum = Integer.valueOf(bean);
            }
        }

        CiCustomGroupInfo bean1 = new CiCustomGroupInfo();
        bean1.setUpdateCycle(Integer.valueOf(3));
        bean1.setCreateCityId(cityId);
        Integer nowNum = Integer.valueOf(this.getDailyCustomersTotolCount(bean1));
        this.log.debug("现有的所属地市日客户群数量为： " + nowNum + ",根据cityId获得配置的可创建日客户群数量为：" + configNum);
        return nowNum.intValue() < configNum.intValue();
    }

    public void delCustomListTableAndListInfoBefore(CiCustomGroupInfo ciCustomGroupInfo, String dataDateStr) {
        Date dataDate = DateUtil.string2Date(dataDateStr, "yyyyMMdd");
        String daysConfig = Configure.getInstance().getProperty("DAILY_CUSTOMGROUP_REMAIN_DAYS");
        String beforeDateStr = "";
        Date endDate = ciCustomGroupInfo.getEndDate();
        if (StringUtil.isNotEmpty(daysConfig)) {
            int listInfoListBefore7Days;
            Date bean;
            if (dataDate.after(endDate)) {
                listInfoListBefore7Days = Integer.valueOf(daysConfig).intValue();
                bean = DateUtil.addDays(endDate, -listInfoListBefore7Days);
                beforeDateStr = DateUtil.date2String(bean, "yyyyMMdd");
            } else {
                listInfoListBefore7Days = Integer.valueOf(daysConfig).intValue();
                bean = DateUtil.addDays(dataDate, -listInfoListBefore7Days);
                beforeDateStr = DateUtil.date2String(bean, "yyyyMMdd");
            }
        } else {
            Date listInfoListBefore7Days1 = DateUtil.addWeeks(dataDate, -1);
            beforeDateStr = DateUtil.date2String(listInfoListBefore7Days1, "yyyyMMdd");
        }

        this.log.debug("生成id为 <" + ciCustomGroupInfo.getCustomGroupId() + "> 的日周期客户群清单后，需要删除 <" + beforeDateStr + "> 之前的清单 ");
        List listInfoListBefore7Days2 = this.ciCustomListInfoHDao.selectByCustomGroupIdAndBeforeDataDate(ciCustomGroupInfo.getCustomGroupId(), beforeDateStr);
        this.log.debug("listInfoListBefore7Days ：" + listInfoListBefore7Days2.size());
        CiCustomListInfo bean1 = new CiCustomListInfo();
        bean1.setCustomGroupId(ciCustomGroupInfo.getCustomGroupId());
        int totalListCount = this.ciCustomListInfoJDao.selectCustomersListInfoCount(bean1);
        if (totalListCount != 1) {
            if (listInfoListBefore7Days2 != null && listInfoListBefore7Days2.size() == totalListCount) {
                listInfoListBefore7Days2.remove(0);
            }

            Iterator var11 = listInfoListBefore7Days2.iterator();

            while (var11.hasNext()) {
                CiCustomListInfo listInfo = (CiCustomListInfo) var11.next();
                int useingCount = this.ciLabelRuleJDao.getIsUsingCustomCount(listInfo.getListTableName(), ciCustomGroupInfo.getCreateUserId());
                this.log.debug("useingCount:====" + useingCount);
                if (useingCount <= 0) {
                    this.ciCustomListInfoJDao.dropTable(listInfo.getListTableName());
                    this.ciCustomListInfoHDao.delete(listInfo);
                }
            }

        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public Map<String, Object> isUsingCustom(String customGroupId) throws CIServiceException {
        CiCustomGroupInfo info = this.queryCiCustomGroupInfo(customGroupId);
        HashMap returnMap = new HashMap();
        String msg = "";

        try {
            List e = this.queryCiCustomListInfoByCGroupId(customGroupId);
            StringBuffer listTableNames = new StringBuffer();
            if (e != null && e.size() > 0) {
                Iterator count = e.iterator();

                while (count.hasNext()) {
                    CiCustomListInfo listTableNameStrs = (CiCustomListInfo) count.next();
                    listTableNames.append("\'" + listTableNameStrs.getListTableName() + "\',");
                }

                String listTableNameStrs1 = listTableNames.toString();
                listTableNameStrs1 = listTableNameStrs1.substring(0, listTableNameStrs1.length() - 1);
                int count1 = this.ciLabelRuleJDao.getIsUsingCustomCount(listTableNameStrs1);
                if (count1 > 0) {
                    msg = "抱歉,该客户群已被其他用户使用,不能取消共享！";
                    returnMap.put("success", Boolean.valueOf(false));
                    returnMap.put("msg", msg);
                    return returnMap;
                }
            }

            if (info.getIsSysRecom().intValue() == ServiceConstants.IS_SYS_RECOM) {
                msg = "抱歉,该客户群为系统推荐客户群,不能取消共享！";
                returnMap.put("success", Boolean.valueOf(false));
                returnMap.put("msg", msg);
                return returnMap;
            }

            returnMap.put("success", Boolean.valueOf(true));
        } catch (Exception var9) {
            msg = "查询是否存在正在使用的客户群错误！";
            this.log.error(msg, var9);
            returnMap.put("success", Boolean.valueOf(false));
            returnMap.put("msg", msg);
        }

        return returnMap;
    }

    public List<CiCustomGroupInfo> queryDelCustomGroup(String customIds) {
        return this.ciCustomGroupInfoHDao.selectDelCustomGroup(customIds);
    }

    public int getDailyCustomersTotolCount(CiCustomGroupInfo bean) {
        boolean count = false;

        try {
            int count1 = this.customersJdbcDao.getDailyCustomersTotolCount(bean);
            return count1;
        } catch (Exception var4) {
            this.log.error("", var4);
            throw new CIServiceException("查询可用日客户群总数失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<Map<String, Object>> queryCustomerLists(int pageNum, int pageSize, CiCustomGroupInfo ciCustomGroupInfo) {
        ArrayList result = new ArrayList();
        List ciCustomListInfoList = this.ciCustomListInfoHDao.selectByCustomGroupId(ciCustomGroupInfo.getCustomGroupId());
        if (ciCustomListInfoList != null && ciCustomListInfoList.size() > 0) {
            CiCustomListInfo groupListInfo = (CiCustomListInfo) ciCustomListInfoList.get(0);
            if (groupListInfo != null) {
                CiCustomListInfo customListInfo = new CiCustomListInfo();
                customListInfo.setCustomGroupId(ciCustomGroupInfo.getCustomGroupId());
                List list2 = this.ciCustomListInfoJDao.selectCustomersListInfo(pageNum, pageSize, customListInfo);
                ciCustomGroupInfo = this.queryCiCustomGroupInfo(ciCustomGroupInfo.getCustomGroupId());
                Iterator var10 = list2.iterator();

                while (var10.hasNext()) {
                    CiCustomListInfo ciCustomListInfo = (CiCustomListInfo) var10.next();
                    HashMap map = new HashMap();
                    map.put("listTableName", ciCustomListInfo.getListTableName());
                    map.put("dataDate", ciCustomListInfo.getDataDate());
                    map.put("customNum", ciCustomListInfo.getCustomNum());
                    map.put("ringNum", ciCustomListInfo.getRingNum());
                    map.put("duplicateNum", ciCustomListInfo.getDuplicateNum());
                    String monthLabelDateStr = "-";
                    if (StringUtil.isNotEmpty(ciCustomListInfo.getMonthLabelDate())) {
                        monthLabelDateStr = DateUtil.string2StringFormat(ciCustomListInfo.getMonthLabelDate(), "yyyyMM", "yyyy-MM");
                    }

                    map.put("monthLabelDate", monthLabelDateStr);
                    String dayLabelDateStr = "-";
                    if (StringUtil.isNotEmpty(ciCustomListInfo.getDayLabelDate())) {
                        dayLabelDateStr = DateUtil.string2StringFormat(ciCustomListInfo.getDayLabelDate(), "yyyyMMdd", "yyyy-MM-dd");
                    }

                    map.put("dayLabelDate", dayLabelDateStr);
                    String dataStatusStr = IdToName.getName("DIM_CUSTOM_DATA_STATUS", ciCustomListInfo.getDataStatus());
                    Integer createTypeId = ciCustomGroupInfo.getCreateTypeId();
                    map.put("createTypeId", createTypeId);
                    map.put("dataStatusStr", dataStatusStr);
                    Integer dataStatus = ciCustomListInfo.getDataStatus();
                    map.put("dataStatus", dataStatus);
                    String dataTimeStr = DateUtil.date2String(ciCustomListInfo.getDataTime(), "yyyy-MM-dd HH:mm:ss");
                    map.put("dataTimeStr", dataTimeStr);
                    map.put("excpInfo", ciCustomListInfo.getExcpInfo());
                    result.add(map);
                }
            }
        }

        return result;
    }

    public List<Map<String, Object>> queryListExeInfosByTableName(Pager pager, String listTableName) {
        ArrayList result = new ArrayList();
        List list = this.ciCustomListExeInfoJDao.queryListExeInfosByTableName(pager, listTableName);
        Iterator var6 = list.iterator();

        while (var6.hasNext()) {
            CiCustomListExeInfo ciCustomListExeInfo = (CiCustomListExeInfo) var6.next();
            HashMap map = new HashMap();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTimeStr = ft.format(ciCustomListExeInfo.getStartTime());
            String endTimeStr = ft.format(ciCustomListExeInfo.getEndTime());
            map.put("startTimeStr", startTimeStr);
            map.put("endTimeStr", endTimeStr);
            boolean runTime = false;
            Date startTime = ciCustomListExeInfo.getStartTime();
            Date endTime = ciCustomListExeInfo.getEndTime();
            double between = (double) ((endTime.getTime() - startTime.getTime()) / 1000L);
            int runTime1;
            if (between < 1.0D) {
                runTime1 = 1;
            } else {
                runTime1 = (int) Math.rint(between);
            }

            String runTimeStr = runTime1 + " s";
            map.put("runTime", runTimeStr);
            StringBuffer excpInfo = new StringBuffer("");
            if (StringUtil.isNotEmpty(ciCustomListExeInfo.getExpression())) {
                excpInfo.append(ciCustomListExeInfo.getExpression());
            }

            List sqlAllList = this.ciListExeSqlAllHDao.selectCiListExeSqlAllByExeInfoId(ciCustomListExeInfo.getExeInfoId());
            if (sqlAllList != null && sqlAllList.size() > 0) {
                Iterator var20 = sqlAllList.iterator();

                while (var20.hasNext()) {
                    CiListExeSqlAll expression = (CiListExeSqlAll) var20.next();
                    excpInfo.append(expression.getSqlPart());
                }
            }

            String expression1 = excpInfo.toString();
            map.put("expression", expression1);
            if (StringUtil.isEmpty(ciCustomListExeInfo.getExcpInfo())) {
                ciCustomListExeInfo.setExcpInfo("");
            }

            map.put("excpInfo", ciCustomListExeInfo.getExcpInfo());
            result.add(map);
        }

        return result;
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public int queryListExeInfoNumByTableName(String listTableName) throws CIServiceException {
        boolean count = false;

        try {
            int count1 = this.ciCustomListExeInfoJDao.getListExeInfoNumByTableName(listTableName);
            return count1;
        } catch (Exception var4) {
            this.log.error("", var4);
            throw new CIServiceException("查询清单扩展信息列表总数失败");
        }
    }

    public List<CiLabelInfo> queryCiLabelInfoListBySys(String labelIds) {
        List list = this.queryLabelInfoByNameOrLabelIdsOrParentId((String) null, labelIds, (String) null, (Integer) null);
        return list;
    }

    public List<CiCustomGroupInfo> queryCiCustomGroupInfoByIsFirstFailed(Integer isFirstFailed) throws CIServiceException {
        return this.ciCustomGroupInfoHDao.selectCiCustomGroupInfoByIsFirstFailed(isFirstFailed);
    }

    public void modifyAllCustomGroupIsFirstFailed(Integer customGroupIsFirstSuccess) {
        this.ciCustomGroupInfoHDao.updateAllCiCustomGroupIsFirstFailed(customGroupIsFirstSuccess);
    }

    public void modifyCustomGroupIsFirstFailed(String customGroupId, Integer customGroupIsFirstSuccess) {
        if (StringUtil.isEmpty(customGroupId)) {
            throw new CIServiceException("客户群ID不能为空！");
        } else {
            this.ciCustomGroupInfoHDao.updateCiCustomGroupIsFirstFailed(customGroupId, customGroupIsFirstSuccess);
        }
    }

    public List<VCiLabelCustomList> queryLastLabelCustomAttentionList(Pager pager) {
        String userId = PrivilegeServiceUtil.getUserId();
        return this.ciLabelCustomListJDao.selectLabelCustomAttentionList(userId, pager);
    }

    public List<CiLabelInfo> queryCiLabelInfoTreeList(String labelNameKeyWord) {
        if (labelNameKeyWord == null) {
            labelNameKeyWord = "";
        } else {
            labelNameKeyWord = labelNameKeyWord.trim();
        }

        ArrayList categoryList = new ArrayList();
        CacheBase cache = CacheBase.getInstance();
        CopyOnWriteArrayList allEffectiveLabelList = cache.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
        int labelCategoryType = Integer.valueOf(Configure.getInstance().getProperty("LABEL_CATEGORY_TYPE")).intValue();
        CiLabelInfo list;
        Iterator set;
        String newlist;
        if (labelCategoryType == 2) {
            set = allEffectiveLabelList.iterator();

            label91:
            while (true) {
                while (true) {
                    do {
                        do {
                            if (!set.hasNext()) {
                                break label91;
                            }

                            list = (CiLabelInfo) set.next();
                        } while (list.getCiLabelExtInfo().getLabelLevel().intValue() >= 3);
                    } while (!list.getLabelName().toLowerCase().contains(labelNameKeyWord.toLowerCase()));

                    newlist = Configure.getInstance().getProperty("IS_CUSTOM_VERT_ATTR");
                    if (StringUtil.isNotEmpty(newlist) && "true".equalsIgnoreCase(newlist)) {
                        categoryList.add(list);
                    } else if (list.getLabelTypeId() != null) {
                        if (list.getLabelTypeId().intValue() != 8) {
                            categoryList.add(list);
                        }
                    } else {
                        categoryList.add(list);
                    }
                }
            }
        } else {
            set = allEffectiveLabelList.iterator();

            label72:
            while (true) {
                while (true) {
                    do {
                        do {
                            if (!set.hasNext()) {
                                break label72;
                            }

                            list = (CiLabelInfo) set.next();
                        } while (list.getCiLabelExtInfo().getLabelLevel().intValue() >= 4);
                    } while (!list.getLabelName().contains(labelNameKeyWord));

                    newlist = Configure.getInstance().getProperty("IS_CUSTOM_VERT_ATTR");
                    if (StringUtil.isNotEmpty(newlist) && "true".equalsIgnoreCase(newlist)) {
                        categoryList.add(list);
                    } else if (list.getLabelTypeId() != null) {
                        if (list.getLabelTypeId().intValue() != 8) {
                            categoryList.add(list);
                        }
                    } else {
                        categoryList.add(list);
                    }
                }
            }
        }

        ArrayList list1 = new ArrayList();
        Iterator newlist1 = categoryList.iterator();

        while (newlist1.hasNext()) {
            CiLabelInfo set1 = (CiLabelInfo) newlist1.next();
            list1.addAll(this.queryLabelParents(set1));
            list1.addAll(this.queryLabelChildren(set1));
        }

        categoryList.addAll(list1);
        HashSet set2 = new HashSet();
        set2.addAll(categoryList);
        ArrayList newlist2 = new ArrayList();
        newlist2.addAll(set2);
        Iterator var10 = newlist2.iterator();

        while (var10.hasNext()) {
            CiLabelInfo ciLabelInfo = (CiLabelInfo) var10.next();
            ciLabelInfo.setLabelOrCustomId(Integer.toString(ciLabelInfo.getLabelId().intValue()));
            ciLabelInfo.setLabelOrCustomName(ciLabelInfo.getLabelName());
        }

        return newlist2;
    }

    private List<CiLabelInfo> queryLabelChildren(CiLabelInfo ciLabelInfo) {
        CacheBase cache = CacheBase.getInstance();
        ArrayList ciLabelInfos = new ArrayList();
        int labelCategoryType = Integer.valueOf(Configure.getInstance().getProperty("LABEL_CATEGORY_TYPE")).intValue();
        CopyOnWriteArrayList allEffectiveLabelList = cache.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
        if (labelCategoryType == 2) {
            switch (ciLabelInfo.getCiLabelExtInfo().getLabelLevel().intValue()) {
                case 1:
                    ciLabelInfos.addAll(this.ciLabelInfoService.queryChildrenById(Integer.valueOf(ciLabelInfo.getLabelId().intValue()), allEffectiveLabelList));
            }
        } else {
            switch (ciLabelInfo.getCiLabelExtInfo().getLabelLevel().intValue()) {
                case 1:
                    List ciLabelInfos2 = this.ciLabelInfoService.queryChildrenById(Integer.valueOf(ciLabelInfo.getLabelId().intValue()), allEffectiveLabelList);
                    ArrayList ciLabelInfos3 = new ArrayList();
                    ciLabelInfos.addAll(ciLabelInfos2);
                    Iterator var9 = ciLabelInfos2.iterator();

                    while (var9.hasNext()) {
                        CiLabelInfo info1 = (CiLabelInfo) var9.next();
                        ciLabelInfos3.addAll(this.ciLabelInfoService.queryChildrenById(Integer.valueOf(info1.getLabelId().intValue()), allEffectiveLabelList));
                    }

                    ciLabelInfos.addAll(ciLabelInfos3);
                    break;
                case 2:
                    ciLabelInfos.addAll(this.ciLabelInfoService.queryChildrenById(Integer.valueOf(ciLabelInfo.getLabelId().intValue()), allEffectiveLabelList));
            }
        }

        return ciLabelInfos;
    }

    private List<CiLabelInfo> queryLabelParents(CiLabelInfo ciLabelInfo) {
        CacheBase cache = CacheBase.getInstance();
        ArrayList ciLabelInfos = new ArrayList();
        int labelLevel = ciLabelInfo.getCiLabelExtInfo().getLabelLevel().intValue();
        CiLabelInfo info = ciLabelInfo;

        for (int i = 0; i < labelLevel - 1; ++i) {
            CiLabelInfo parent = cache.getEffectiveLabel("" + info.getParentId());
            if (parent == null) {
                this.log.error("Not find parent labelId is " + info.getLabelId());
            } else {
                ciLabelInfos.add(parent);
                info = parent;
            }
        }

        return ciLabelInfos;
    }

    public String getLastListTableName(String customGroupId, String dataDate) {
        return this.ciCustomListInfoJDao.getListTableNameByCroupIdAndDataDate(customGroupId, dataDate);
    }

    public int queryDistinctListNumByTableName(String tableName) {
        return this.ciCustomListInfoJDao.selecrDistinctListNum(tableName);
    }

    public List<CiLabelInfo> queryLabelInfoByNameOrLabelIdsOrParentId(String labelName, String labelIds, String parentId, Integer labelLevel) {
        Object labelList = new ArrayList();
        CacheBase cache = CacheBase.getInstance();
        CopyOnWriteArrayList allEffectiveLabel = cache.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
        ArrayList resultList = new ArrayList();
        int labelCategoryType = Integer.valueOf(Configure.getInstance().getProperty("LABEL_CATEGORY_TYPE")).intValue();
        if (StringUtil.isNotEmpty(labelIds)) {
            String[] ciLabelInfo = labelIds.split(",");

            for (int ciLabelInfo1 = 0; ciLabelInfo1 < ciLabelInfo.length; ++ciLabelInfo1) {
                CiLabelInfo extInfo = cache.getEffectiveLabel(ciLabelInfo[ciLabelInfo1]);
                ((List) labelList).add(extInfo);
            }

            resultList.addAll((Collection) labelList);
        } else {
            CiLabelInfo var17;
            if (StringUtil.isNotEmpty(parentId)) {
                List var16;
                CiLabelInfo var18;
                Iterator var20;
                if (labelLevel.intValue() != 1) {
                    if (labelLevel.intValue() == 2) {
                        if (labelCategoryType == 2) {
                            ((List) labelList).add(cache.getEffectiveLabel(parentId));
                        } else {
                            var16 = this.ciLabelInfoService.queryChildrenById(Integer.valueOf(parentId), allEffectiveLabel);
                            var20 = var16.iterator();

                            while (var20.hasNext()) {
                                var18 = (CiLabelInfo) var20.next();
                                ((List) labelList).add(var18);
                            }
                        }
                    } else if (labelLevel.intValue() == 3 && labelCategoryType != 2) {
                        var17 = cache.getEffectiveLabel(parentId);
                        ((List) labelList).add(var17);
                    }
                } else {
                    var16 = this.ciLabelInfoService.queryChildrenById(Integer.valueOf(parentId), allEffectiveLabel);
                    if (labelCategoryType == 2) {
                        labelList = var16;
                    } else {
                        var20 = var16.iterator();

                        while (var20.hasNext()) {
                            var18 = (CiLabelInfo) var20.next();
                            List isStatUserNum = this.ciLabelInfoService.queryChildrenById(var18.getLabelId(), allEffectiveLabel);
                            Iterator var15 = isStatUserNum.iterator();

                            while (var15.hasNext()) {
                                CiLabelInfo ciLabelInfo2 = (CiLabelInfo) var15.next();
                                ((List) labelList).add(ciLabelInfo2);
                            }
                        }
                    }
                }

                this.queryAllChildrenBy(resultList, (List) labelList, "");
                this.queryLabelListByName(resultList, labelName);
            } else {
                Iterator var19 = allEffectiveLabel.iterator();

                while (var19.hasNext()) {
                    var17 = (CiLabelInfo) var19.next();
                    CiLabelExtInfo var21 = var17.getCiLabelExtInfo();
                    Integer var22 = var21.getIsStatUserNum();
                    if (var22.intValue() == 1) {
                        if (StringUtil.isNotEmpty(labelName)) {
                            if (var17.getLabelName().toLowerCase().contains(labelName.toLowerCase())) {
                                resultList.add(var17);
                            }
                        } else {
                            resultList.add(var17);
                        }
                    }
                }
            }
        }

        return resultList;
    }

    private void queryLabelListByName(List<CiLabelInfo> list, String labelName) {
        ArrayList resultList = new ArrayList();
        Iterator var5 = list.iterator();

        while (var5.hasNext()) {
            CiLabelInfo ciLabelInfo = (CiLabelInfo) var5.next();
            CiLabelExtInfo extInfo = ciLabelInfo.getCiLabelExtInfo();
            Integer isStatUserNum = extInfo.getIsStatUserNum();
            if (isStatUserNum.intValue() == 1) {
                if (StringUtil.isNotEmpty(labelName)) {
                    if (ciLabelInfo.getLabelName().toLowerCase().contains(labelName.toLowerCase())) {
                        resultList.add(ciLabelInfo);
                    }
                } else {
                    resultList.add(ciLabelInfo);
                }
            }
        }

        list.clear();
        list.addAll(resultList);
    }

    private void queryAllChildrenBy(List<CiLabelInfo> resultList, List<CiLabelInfo> thirdList, String labelName) {
        if (resultList == null) {
            resultList = new ArrayList();
        }

        Iterator var5 = thirdList.iterator();

        while (var5.hasNext()) {
            CiLabelInfo labelInfo = (CiLabelInfo) var5.next();
            Integer labelId = labelInfo.getLabelId();
            List childrenLabelList = this.ciLabelInfoService.queryChildrenByIdAndName(labelId, labelName);
            if (childrenLabelList.size() > 0) {
                ((List) resultList).addAll(childrenLabelList);
                this.queryAllChildrenBy((List) resultList, childrenLabelList, labelName);
            }
        }

    }

    public void updateCiCustomExeTime(String ciCustomExeTime) {
        try {
            this.customersJdbcDao.updateCiCustomExeTime(ciCustomExeTime);
        } catch (Exception var3) {
            this.log.error("更新上次生成时间失败", var3);
        }

    }

    public void batchUpdateValueList(List<String> valueList, String tabName, String columns) throws CIServiceException {
        try {
            this.ciLabelRuleJDao.batchUpdateValueList(valueList, tabName, columns);
        } catch (Exception var6) {
            String message = "批量插入精确值表失败";
            this.log.error(message, var6);
            throw new CIServiceException(message, var6);
        }
    }

    public String createValueTableName() {
        String tabName = Configure.getInstance().getProperty("LABEL_EXACT_VALUE_TABLE");

        try {
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException var7) {
                this.log.error("生成精确值表对象出错");
                var7.printStackTrace();
            }

            tabName = tabName.replace("YYMMDDHHMISSTTTTTT", "") + CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L);
            String e = Configure.getInstance().getProperty("LABEL_EXACT_VALUE_TABLE");
            String column = "VALUE_ID";

            try {
                try {
                    this.dropTable(tabName);
                } catch (Exception var5) {
                    this.log.error("drop精确值表出错,可能精确值表不存在", var5);
                    return null;
                }

                this.ciLoadCustomGroupFileJdao.createTable(tabName, e, column);
            } catch (Exception var6) {
                this.log.error("创建精确值表失败", var6);
                return null;
            }
        } catch (Exception var8) {
            this.log.error("创建精确值表失败", var8);
        }

        return tabName;
    }

    public int addExactValueToTable(String exactValue, String tabName) {
        int distinctTotal = 0;
        if (StringUtils.isNotEmpty(exactValue)) {
            try {
                String e = "VALUE_ID";
                ArrayList exactValueList = new ArrayList();
                String[] exactValueArr = exactValue.split(",");
                int currentRowNum = 0;

                for (int i = 0; i < exactValueArr.length; ++i) {
                    if (StringUtils.isNotEmpty(exactValueArr[i])) {
                        exactValueList.add(exactValueArr[i]);
                        ++currentRowNum;
                        ++distinctTotal;
                        if (currentRowNum % 1000000 == 0) {
                            this.batchUpdateValueList(exactValueList, tabName, e);
                            exactValueList.clear();
                            currentRowNum = 0;
                        }
                    }
                }

                if (currentRowNum > 0) {
                    this.batchUpdateValueList(exactValueList, tabName, e);
                    exactValueList.clear();
                }
            } catch (Exception var9) {
                this.log.error("addExactValueToTable error:", var9);
                return -1;
            }
        }

        return distinctTotal;
    }

    public String queryAllValueStrByValueTable(String tableName) {
        StringBuffer sb = new StringBuffer();
        String column = "VALUE_ID";

        try {
            if (StringUtils.isNotEmpty(tableName)) {
                List e = this.ciLabelRuleJDao.findValueListData(tableName);
                int i = 0;
                Iterator var7 = e.iterator();

                while (var7.hasNext()) {
                    Map map = (Map) var7.next();
                    if (map.get(column) != null) {
                        if (i == 0) {
                            sb.append(map.get(column));
                        } else {
                            sb.append(",").append(map.get(column));
                        }

                        ++i;
                    }
                }
            }
        } catch (Exception var8) {
            this.log.error("从精确值表中获取数据集失败", var8);
        }

        return sb.toString();
    }

    private List<String> queryAllValueTableNameByCustomerId(String id, Integer customType) {
        new ArrayList();
        ArrayList valueTableNameList = new ArrayList();

        try {
            List ciLabelRuleList = this.ciLabelRuleHDao.selectCiLabelRuleList(id, customType);
            Iterator var6 = ciLabelRuleList.iterator();

            while (var6.hasNext()) {
                CiLabelRule e = (CiLabelRule) var6.next();
                if (StringUtil.isNotEmpty(e.getTableName())) {
                    valueTableNameList.add(e.getTableName());
                }
            }
        } catch (Exception var7) {
            this.log.error("获取规则中的精确值数据表列表错误", var7);
        }

        return valueTableNameList;
    }

    private boolean dropAllValueTableByTableList(List<String> tableList) {
        boolean flag = true;
        Iterator var4 = tableList.iterator();

        while (var4.hasNext()) {
            String tableName = (String) var4.next();

            try {
                this.dropTable(tableName);
            } catch (Exception var6) {
                flag = false;
                this.log.error("批量删除规则中的精确值数据表失败,表名为：" + tableName, var6);
            }
        }

        return flag;
    }

    public void saveCiListExeSqlAllList(List<CiListExeSqlAll> ciListExeSqlAllList) {
        try {
            if (ciListExeSqlAllList == null || ciListExeSqlAllList.size() == 0) {
                return;
            }

            Iterator var3 = ciListExeSqlAllList.iterator();

            while (var3.hasNext()) {
                CiListExeSqlAll e = (CiListExeSqlAll) var3.next();
                this.ciListExeSqlAllHDao.insertCiListExeSqlAll(e);
            }
        } catch (Exception var4) {
            this.log.error("保存生成清单时执行的SQL时失败");
        }

    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public int selectEndedCustomersListInfoCount(CiCustomListInfo bean) {
        boolean customersListInfoCount = false;

        try {
            int customersListInfoCount1 = this.ciCustomListInfoJDao.selectEndedCustomersListInfoCount(bean);
            return customersListInfoCount1;
        } catch (Exception var4) {
            this.log.error("查询客户群清单数量报错，" + bean, var4);
            throw new CIServiceException("查询客户群清单数量报错");
        }
    }

    public boolean isConstraintCreateCustom(List<CiLabelRule> ciLabelRuleList) {
        boolean flag = false;
        Iterator var4 = ciLabelRuleList.iterator();

        while (var4.hasNext()) {
            CiLabelRule ciLabelRule = (CiLabelRule) var4.next();
            if (2 == ciLabelRule.getElementType().intValue()) {
                CacheBase listTableName = CacheBase.getInstance();
                CiLabelInfo groupInfo = listTableName.getEffectiveLabel(ciLabelRule.getCalcuElement());
                if (2 != groupInfo.getUpdateCycle().intValue()) {
                    flag = true;
                    return flag;
                }
            } else if (5 == ciLabelRule.getElementType().intValue() && StringUtil.isNotEmpty(ciLabelRule.getCalcuElement())) {
                String listTableName1 = ciLabelRule.getCalcuElement();
                CiCustomGroupInfo groupInfo1 = this.queryCiCustomGroupInfoByListInfoId(listTableName1);
                if (3 == groupInfo1.getUpdateCycle().intValue()) {
                    flag = true;
                    return flag;
                }
            }
        }

        return flag;
    }

    public boolean isEarlierThanCustomExeTime(CiCustomGroupInfo groupInfo) {
        boolean isEarlierThanCustomExeTime = false;

        try {
            String e = this.customersJdbcDao.getCiCustomExeTime();
            String groupInfoListCreateTime = groupInfo.getListCreateTime();
            Integer updateCycle = groupInfo.getUpdateCycle();
            if (StringUtil.isNotEmpty(e)) {
                String monthCycleExeTime;
                Calendar nowCalendar1;
                String[] groupInfoListCreateTimeArray1;
                if (updateCycle.intValue() == 3) {
                    monthCycleExeTime = e.substring(e.length() - 8);
                    nowCalendar1 = Calendar.getInstance();
                    groupInfoListCreateTimeArray1 = groupInfoListCreateTime.split(":");
                    nowCalendar1.set(11, Integer.valueOf(groupInfoListCreateTimeArray1[0]).intValue());
                    nowCalendar1.set(12, Integer.valueOf(groupInfoListCreateTimeArray1[1]).intValue());
                    nowCalendar1.set(13, Integer.valueOf(groupInfoListCreateTimeArray1[2]).intValue());
                    long groupInfoListCreateTimeArray = nowCalendar1.getTimeInMillis();
                    Calendar nowCalendar2 = Calendar.getInstance();
                    String[] nowCalendar21 = monthCycleExeTime.split(":");
                    nowCalendar2.set(11, Integer.valueOf(nowCalendar21[0]).intValue());
                    nowCalendar2.set(12, Integer.valueOf(nowCalendar21[1]).intValue());
                    nowCalendar2.set(13, Integer.valueOf(nowCalendar21[2]).intValue());
                    long ciCustomExeTimeArray1 = nowCalendar2.getTimeInMillis();
                    if (ciCustomExeTimeArray1 > groupInfoListCreateTimeArray) {
                        isEarlierThanCustomExeTime = true;
                    }
                } else if (updateCycle.intValue() == 2) {
                    monthCycleExeTime = e.substring(e.length() - 11);
                    nowCalendar1 = Calendar.getInstance();
                    groupInfoListCreateTimeArray1 = groupInfoListCreateTime.split(" ");
                    nowCalendar1.set(5, Integer.valueOf(groupInfoListCreateTimeArray1[0]).intValue());
                    groupInfoListCreateTimeArray1 = groupInfoListCreateTimeArray1[1].split(":");
                    nowCalendar1.set(11, Integer.valueOf(groupInfoListCreateTimeArray1[0]).intValue());
                    nowCalendar1.set(12, Integer.valueOf(groupInfoListCreateTimeArray1[1]).intValue());
                    nowCalendar1.set(13, Integer.valueOf(groupInfoListCreateTimeArray1[2]).intValue());
                    long groupInfoListCreateTimeMillis = nowCalendar1.getTimeInMillis();
                    Calendar nowCalendar22 = Calendar.getInstance();
                    String[] ciCustomExeTimeArray11 = monthCycleExeTime.split(" ");
                    nowCalendar22.set(5, Integer.valueOf(ciCustomExeTimeArray11[0]).intValue());
                    String[] ciCustomExeTimeArray = ciCustomExeTimeArray11[1].split(":");
                    nowCalendar22.set(11, Integer.valueOf(ciCustomExeTimeArray[0]).intValue());
                    nowCalendar22.set(12, Integer.valueOf(ciCustomExeTimeArray[1]).intValue());
                    nowCalendar22.set(13, Integer.valueOf(ciCustomExeTimeArray[2]).intValue());
                    long ciCustomExeTimeArrayMillis = nowCalendar22.getTimeInMillis();
                    if (ciCustomExeTimeArrayMillis > groupInfoListCreateTimeMillis) {
                        isEarlierThanCustomExeTime = true;
                    }
                }
            }
        } catch (Exception var17) {
            this.log.error("判断该客户群的指定执行时间是否早于库里的上次执行时间出错", var17);
        }

        return isEarlierThanCustomExeTime;
    }

    public Integer getCustomGroupInfoDataCycle(Integer createTypeId, Integer updateCycle, String dataDate) {
        Integer result = Integer.valueOf(-1);
        if (createTypeId.intValue() == 12) {
            return updateCycle.intValue() == 2 ? Integer.valueOf(1) : Integer.valueOf(2);
        } else {
            if (createTypeId.intValue() == 7) {
                if (StringUtil.isNotEmpty(dataDate)) {
                    if (dataDate.length() == 6) {
                        return Integer.valueOf(1);
                    }

                    if (dataDate.length() == 8) {
                        return Integer.valueOf(2);
                    }
                }
            } else if (createTypeId.intValue() == 1) {
                if (updateCycle.intValue() == 2) {
                    return Integer.valueOf(1);
                }

                return Integer.valueOf(2);
            }

            return result;
        }
    }

    public String queryListMaxNumSql(String sql, int listMaxNum, String attrSqlStr) {
        String result = "";

        try {
            result = this.ciCustomListInfoJDao.selectListMaxNumSql(sql, listMaxNum, attrSqlStr);
        } catch (Exception var6) {
            this.log.error("获取限制客户群数量SQL出错", var6);
        }

        return result;
    }

    public void saveCiExploreLogRecord(CiExploreLogRecord ciExploreLogRecord) {
        this.ciExploreLogRecordHDao.insertCiExploreLogRecord(ciExploreLogRecord);
    }

    public void saveCiExploreSqlAllList(List<CiExploreSqlAll> ciExploreSqlAllList) {
        try {
            if (ciExploreSqlAllList == null || ciExploreSqlAllList.size() == 0) {
                return;
            }

            Iterator var3 = ciExploreSqlAllList.iterator();

            while (var3.hasNext()) {
                CiExploreSqlAll e = (CiExploreSqlAll) var3.next();
                this.ciExploreSqlAllHDao.insertCiExploreSqlAll(e);
            }
        } catch (Exception var4) {
            this.log.error("保存生成清单时执行的SQL时失败");
        }

    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public int queryCustomListCount(CiCustomListInfo ciCustomListInfo) {
        return this.ciCustomListInfoJDao.selectCustomListCount(ciCustomListInfo);
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiCustomListInfo> queryCustomList(int start, int pageSize, CiCustomListInfo ciCustomListInfo) throws Exception {
        List list = this.ciCustomListInfoJDao.selectCustomList(start, pageSize, ciCustomListInfo);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        CiCustomListInfo customListInfo;
        for (Iterator var7 = list.iterator(); var7.hasNext(); customListInfo.setDataTimeStr(formatter.format(customListInfo.getDataTime()))) {
            customListInfo = (CiCustomListInfo) var7.next();
            IUser iuser = PrivilegeServiceUtil.getUserById(customListInfo.getCreateUserId());
            if (iuser != null) {
                customListInfo.setCreateUserName(iuser.getUsername());
            }
        }

        return list;
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public CiCustomModifyHistory queryNewestCustomModifyHistory(String customGroupId) {
        List list = this.ciCustomModifyHistoryHDao.selectCustomModifyHistory(customGroupId);
        return list != null && list.size() > 0 ? (CiCustomModifyHistory) list.get(0) : null;
    }

    public void updateCustomListInfo(CiCustomListInfo ciCustomListInfo) {
        this.ciCustomListInfoHDao.updateCiCustomListInfo(ciCustomListInfo);
    }

    public void deleteCustomSceneRels(String customOrLabelId, Integer showType) {
        List rels = this.marketService.queryCustomLabelSceneRelsByType(customOrLabelId, showType);
        Iterator var5 = rels.iterator();

        while (var5.hasNext()) {
            CiCustomLabelSceneRel ciCustomLabelSceneRel = (CiCustomLabelSceneRel) var5.next();
            this.marketService.deleteCustomOrLabelSceneRel(ciCustomLabelSceneRel);
        }

    }

    public StringBuffer shopCartRule(List<CiLabelRule> ciLabelRuleListTemp, StringBuffer rule) {
        if (ciLabelRuleListTemp != null && ciLabelRuleListTemp.size() > 0) {
            for (int i = 0; i < ciLabelRuleListTemp.size(); ++i) {
                CiLabelRule labelRule = (CiLabelRule) ciLabelRuleListTemp.get(i);
                int elementType = labelRule.getElementType().intValue();
                if (elementType == 3) {
                    rule.append(labelRule.getCalcuElement());
                } else {
                    String list;
                    if (elementType == 1) {
                        list = "";
                        if ("and".equals(labelRule.getCalcuElement())) {
                            list = "且";
                        } else if ("or".equals(labelRule.getCalcuElement())) {
                            list = "或";
                        } else {
                            list = "剔除";
                        }

                        rule.append(list);
                    } else if (elementType == 2) {
                        if (labelRule.getLabelFlag().intValue() != 1) {
                            rule.append("(非)");
                        }

                        rule.append(labelRule.getCustomOrLabelName());
                        list = this.ruleAttrVal(labelRule);
                        if (StringUtil.isNotEmpty(list)) {
                            rule.append("[");
                            rule.append(this.ruleAttrVal(labelRule));
                            rule.append("]");
                        }
                    } else if (elementType == 5) {
                        rule.append("客户群：");
                        rule.append(labelRule.getCustomOrLabelName());
                        if (StringUtil.isNotEmpty(labelRule.getAttrVal())) {
                            rule.append("[已选清单：");
                            rule.append(labelRule.getAttrVal());
                            rule.append("]");
                        }
                    } else if (elementType == 6) {
                        rule.append("(");
                        List var7 = labelRule.getChildCiLabelRuleList();
                        this.shopCartRule(var7, rule);
                        rule.append(")");
                    }
                }
            }
        }

        return rule;
    }

    public String ruleAttrVal(CiLabelRule labelRule) {
        StringBuffer attrValStr = new StringBuffer();
        if (labelRule.getLabelTypeId().intValue() == 4) {
            if ("1".equals(labelRule.getQueryWay())) {
                attrValStr.append("数值范围：");
                if (StringUtil.isNotEmpty(labelRule.getContiueMinVal())) {
                    if (labelRule.getLeftZoneSign().equals(">=")) {
                        attrValStr.append("大于等于");
                    }

                    if (labelRule.getLeftZoneSign().equals(">")) {
                        attrValStr.append("大于");
                    }

                    attrValStr.append(labelRule.getContiueMinVal());
                }

                if (StringUtil.isNotEmpty(labelRule.getContiueMaxVal())) {
                    if (labelRule.getRightZoneSign().equals("<=")) {
                        attrValStr.append("小于等于");
                    }

                    if (labelRule.getRightZoneSign().equals("<")) {
                        attrValStr.append("小于");
                    }

                    attrValStr.append(labelRule.getContiueMaxVal());
                }

                if (StringUtil.isNotEmpty(labelRule.getUnit())) {
                    attrValStr.append("(");
                    attrValStr.append(labelRule.getUnit());
                    attrValStr.append(")");
                }
            } else if ("2".equals(labelRule.getQueryWay()) && StringUtil.isNotEmpty(labelRule.getExactValue())) {
                attrValStr.append("数值范围：");
                attrValStr.append(labelRule.getExactValue());
                if (StringUtil.isNotEmpty(labelRule.getUnit())) {
                    attrValStr.append("(");
                    attrValStr.append(labelRule.getUnit());
                    attrValStr.append(")");
                }
            }
        } else if (labelRule.getLabelTypeId().intValue() == 5) {
            if (StringUtil.isNotEmpty(labelRule.getAttrVal())) {
                attrValStr.append("已选择条件：");
                attrValStr.append(labelRule.getAttrName());
            }
        } else if (labelRule.getLabelTypeId().intValue() == 6) {
            if ("1".equals(labelRule.getQueryWay())) {
                if (StringUtil.isNotEmpty(labelRule.getStartTime()) || StringUtil.isNotEmpty(labelRule.getEndTime())) {
                    attrValStr.append("已选择条件：");
                    if (StringUtil.isNotEmpty(labelRule.getStartTime())) {
                        if (labelRule.getLeftZoneSign().equals(">=")) {
                            attrValStr.append("大于等于");
                        }

                        if (labelRule.getLeftZoneSign().equals(">")) {
                            attrValStr.append("大于");
                        }

                        attrValStr.append(labelRule.getStartTime());
                    }

                    if (StringUtil.isNotEmpty(labelRule.getEndTime())) {
                        if (labelRule.getRightZoneSign().equals("<=")) {
                            attrValStr.append("小于等于");
                        }

                        if (labelRule.getRightZoneSign().equals("<")) {
                            attrValStr.append("小于");
                        }

                        attrValStr.append(labelRule.getEndTime());
                    }

                    if (labelRule.getIsNeedOffset().intValue() == 1) {
                        attrValStr.append("（动态偏移更新）");
                    }
                }
            } else if ("2".equals(labelRule.getQueryWay()) && StringUtil.isNotEmpty(labelRule.getExactValue())) {
                attrValStr.append("已选择条件：");
                String[] i = labelRule.getExactValue().split(",");
                if (StringUtil.isNotEmpty(i) && !i[0].equals("-1")) {
                    attrValStr.append(i[0]);
                    attrValStr.append("年");
                }

                if (StringUtil.isNotEmpty(i) && !i[1].equals("-1")) {
                    attrValStr.append(i[1]);
                    attrValStr.append("月");
                }

                if (StringUtil.isNotEmpty(i) && !i[2].equals("-1")) {
                    attrValStr.append(i[2]);
                    attrValStr.append("日");
                }
            }
        } else if (labelRule.getLabelTypeId().intValue() == 7) {
            if ("1".equals(labelRule.getQueryWay())) {
                if (StringUtil.isNotEmpty(labelRule.getDarkValue())) {
                    attrValStr.append("模糊值：");
                    attrValStr.append(labelRule.getDarkValue());
                }
            } else if ("2".equals(labelRule.getQueryWay()) && StringUtil.isNotEmpty(labelRule.getExactValue())) {
                attrValStr.append("精确值：");
                attrValStr.append(labelRule.getExactValue());
            }
        } else if (labelRule.getLabelTypeId().intValue() == 8 && labelRule.getChildCiLabelRuleList() != null && labelRule.getChildCiLabelRuleList().size() > 0) {
            for (int var6 = 0; var6 < labelRule.getChildCiLabelRuleList().size(); ++var6) {
                CiLabelRule rule = (CiLabelRule) labelRule.getChildCiLabelRuleList().get(var6);
                if (rule.getLabelTypeId().intValue() == 4) {
                    if ("1".equals(rule.getQueryWay())) {
                        attrValStr.append("[");
                        attrValStr.append(rule.getColumnCnName());
                        attrValStr.append("：");
                        if (StringUtil.isNotEmpty(rule.getContiueMinVal())) {
                            if (rule.getLeftZoneSign().equals(">=")) {
                                attrValStr.append("大于等于");
                            }

                            if (rule.getLeftZoneSign().equals(">")) {
                                attrValStr.append("大于");
                            }

                            attrValStr.append(rule.getContiueMinVal());
                        }

                        if (StringUtil.isNotEmpty(rule.getContiueMaxVal())) {
                            if (rule.getRightZoneSign().equals("<=")) {
                                attrValStr.append("小于等于");
                            }

                            if (rule.getRightZoneSign().equals("<")) {
                                attrValStr.append("小于");
                            }

                            attrValStr.append(rule.getContiueMaxVal());
                        }

                        if (StringUtil.isNotEmpty(rule.getUnit())) {
                            attrValStr.append("(");
                            attrValStr.append(rule.getUnit());
                            attrValStr.append(")");
                        }

                        attrValStr.append("]");
                    } else if ("2".equals(rule.getQueryWay())) {
                        attrValStr.append(rule.getColumnCnName());
                        attrValStr.append("：");
                        if (StringUtil.isNotEmpty(rule.getExactValue())) {
                            attrValStr.append(rule.getExactValue());
                            if (StringUtil.isNotEmpty(rule.getUnit())) {
                                attrValStr.append("(");
                                attrValStr.append(rule.getUnit());
                                attrValStr.append(")");
                                attrValStr.append("]");
                            }
                        }
                    }
                } else if (rule.getLabelTypeId().intValue() == 5) {
                    if (StringUtil.isNotEmpty(rule.getAttrVal())) {
                        attrValStr.append("[");
                        attrValStr.append(rule.getColumnCnName());
                        attrValStr.append("：");
                        attrValStr.append(rule.getAttrName());
                        attrValStr.append("]");
                    }
                } else if (rule.getLabelTypeId().intValue() == 6) {
                    if ("1".equals(rule.getQueryWay())) {
                        if (StringUtil.isNotEmpty(rule.getStartTime()) || StringUtil.isNotEmpty(rule.getEndTime())) {
                            attrValStr.append("[");
                            attrValStr.append(rule.getColumnCnName());
                            attrValStr.append("：");
                            if (StringUtil.isNotEmpty(rule.getStartTime())) {
                                if (rule.getLeftZoneSign().equals(">=")) {
                                    attrValStr.append("大于等于");
                                }

                                if (rule.getLeftZoneSign().equals(">")) {
                                    attrValStr.append("大于");
                                }

                                attrValStr.append(rule.getStartTime());
                            }

                            if (StringUtil.isNotEmpty(rule.getEndTime())) {
                                if (rule.getRightZoneSign().equals("<=")) {
                                    attrValStr.append("小于等于");
                                }

                                if (rule.getRightZoneSign().equals("<")) {
                                    attrValStr.append("小于");
                                }

                                attrValStr.append(rule.getEndTime());
                            }

                            if (rule.getIsNeedOffset().intValue() == 1) {
                                attrValStr.append("（动态偏移更新）");
                            }

                            attrValStr.append("]");
                        }
                    } else if ("2".equals(rule.getQueryWay()) && StringUtil.isNotEmpty(rule.getExactValue())) {
                        attrValStr.append("[");
                        attrValStr.append(rule.getColumnCnName());
                        attrValStr.append("：");
                        String[] exactStr = rule.getExactValue().split(",");
                        if (StringUtil.isNotEmpty(exactStr) && !exactStr[0].equals("-1")) {
                            attrValStr.append(exactStr[0]);
                            attrValStr.append("年");
                        }

                        if (StringUtil.isNotEmpty(exactStr) && !exactStr[1].equals("-1")) {
                            attrValStr.append(exactStr[1]);
                            attrValStr.append("月");
                        }

                        if (StringUtil.isNotEmpty(exactStr) && !exactStr[2].equals("-1")) {
                            attrValStr.append(exactStr[2]);
                            attrValStr.append("日");
                        }

                        attrValStr.append("]");
                    }
                } else if (rule.getLabelTypeId().intValue() == 7) {
                    if ("1".equals(rule.getQueryWay())) {
                        if (StringUtil.isNotEmpty(rule.getDarkValue())) {
                            attrValStr.append("[");
                            attrValStr.append(rule.getColumnCnName());
                            attrValStr.append("：");
                            attrValStr.append(rule.getDarkValue());
                            attrValStr.append("]");
                        }
                    } else if ("2".equals(rule.getQueryWay()) && StringUtil.isNotEmpty(rule.getExactValue())) {
                        attrValStr.append("[");
                        attrValStr.append(rule.getColumnCnName());
                        attrValStr.append("：");
                        attrValStr.append(rule.getExactValue());
                        attrValStr.append("]");
                    }
                }
            }
        }

        return attrValStr.toString();
    }

    public String queryOtherSysCustomGroupId(String customGroupId, String sysId) {
        this.log.info("===================》》根据系统ID和客户群ID查询外部系统对应的客户群ID，SYSID：" + sysId);
        return this.customersJdbcDao.selectOtherSysCustomGroupId(customGroupId, sysId);
    }
}
