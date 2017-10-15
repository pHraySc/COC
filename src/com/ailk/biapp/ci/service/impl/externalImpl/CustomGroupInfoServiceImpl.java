package com.ailk.biapp.ci.service.impl.externalImpl;

import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.core.model.ShopCartRule;
import com.ailk.biapp.ci.dao.ICiCustomGroupInfoHDao;
import com.ailk.biapp.ci.dao.ICiCustomListInfoHDao;
import com.ailk.biapp.ci.dao.ICiCustomModifyHistoryHDao;
import com.ailk.biapp.ci.dao.ICiCustomSourceRelHDao;
import com.ailk.biapp.ci.dao.ICiGroupAttrRelHDao;
import com.ailk.biapp.ci.dao.ICiLabelInfoJDao;
import com.ailk.biapp.ci.dao.ICiLabelRuleHDao;
import com.ailk.biapp.ci.dao.ICiTaskServerCityRelHDao;
import com.ailk.biapp.ci.dao.ICiUserUseLabelHDao;
import com.ailk.biapp.ci.dao.ICustomGroupInfoExternalJDao;
import com.ailk.biapp.ci.dao.ICustomGroupInfoJDao;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiCustomModifyHistory;
import com.ailk.biapp.ci.entity.CiCustomPushReq;
import com.ailk.biapp.ci.entity.CiCustomSceneRel;
import com.ailk.biapp.ci.entity.CiCustomSourceRel;
import com.ailk.biapp.ci.entity.CiExploreLogRecord;
import com.ailk.biapp.ci.entity.CiExploreSqlAll;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.entity.CiGroupAttrRelId;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiTaskServerCityRel;
import com.ailk.biapp.ci.entity.CiTemplateInfo;
import com.ailk.biapp.ci.entity.CiUserUseLabel;
import com.ailk.biapp.ci.entity.CiUserUseLabelId;
import com.ailk.biapp.ci.entity.base.CustomGroupInfo;
import com.ailk.biapp.ci.entity.base.CustomListInfo;
import com.ailk.biapp.ci.entity.base.SysInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.localization.nonstandard.IImportTableService;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiTemplateInfoService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.service.base.ICustomGroupInfoService;
import com.ailk.biapp.ci.service.impl.ReviseDateModel;
import com.ailk.biapp.ci.task.CustomerListCreaterThread;
import com.ailk.biapp.ci.util.CIAlarmServiceUtil;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.CiCustomGroupInfoIdGenUseSequence;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.ThreadPool;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.dimtable.model.DimTableDefine;
import com.asiainfo.biframe.dimtable.service.impl.DimTableServiceImpl;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomGroupInfoServiceImpl implements ICustomGroupInfoService {
    private final Logger log = Logger.getLogger(CustomGroupInfoServiceImpl.class);
    @Autowired
    private ICustomGroupInfoJDao customersJdbcDao;
    @Autowired
    private ICiCustomGroupInfoHDao ciCustomGroupInfoHDao;
    @Autowired
    private ICustomGroupInfoExternalJDao customersExternalJDao;
    @Autowired
    private ICustomersManagerService customGroupInfoService;
    @Autowired
    private ICiCustomListInfoHDao ciCustomListInfoHDao;
    @Autowired
    private CiCustomGroupInfoIdGenUseSequence customGroupIdGen;
    @Autowired
    private ICiTaskServerCityRelHDao ciTaskServerCityRelHDao;
    @Autowired
    private ICiTemplateInfoService ciTemplateInfoService;
    @Autowired
    private ICiUserUseLabelHDao ciUserUseLabelHDao;
    @Autowired
    private ICiGroupAttrRelHDao ciGroupAttrRelHDao;
    @Autowired
    private ICiCustomModifyHistoryHDao ciCustomModifyHistoryHDao;
    @Autowired
    private ICiLabelRuleHDao ciLabelRuleHDao;
    @Autowired
    private ICiCustomSourceRelHDao ciCustomSourceRelHDao;
    @Autowired
    private ICiLabelInfoJDao ciLabelInfoJDao;
    public static Lock customLock = new ReentrantLock();
    public static Lock listLock = new ReentrantLock();
    public static Lock labelRuleLock = new ReentrantLock();

    public CustomGroupInfoServiceImpl() {
    }

    public List<CustomGroupInfo> queryCustomGroupInfoList(SysInfo sysInfo, Integer currPage, Integer pageSize) throws Exception {
        this.log.info("=============>>>>  invoke method queryCustomGroupInfoList start");
        this.log.info(JsonUtil.toJson(sysInfo));
        this.log.info("currPage：" + currPage + ",pageSize：" + pageSize);
        boolean totalSize = false;
        new ArrayList();

        int totalSize1;
        try {
            totalSize1 = this.customersExternalJDao.getCustomsersListCount(sysInfo);
        } catch (Exception var12) {
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_GROUP", "", "查询客户群列表", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "分页查询客户群列表失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            this.log.error("获得客户群总数失败");
            this.log.error(var12);
            throw new CIServiceException("获得客户群总数失败");
        }

        Pager pager = new Pager((long)totalSize1);
        pager.setPageSize(pageSize.intValue());
        pager.setPageNum(currPage.intValue());

        List infos;
        try {
            infos = this.customersExternalJDao.getCustomsersList(sysInfo, pager);
        } catch (Exception var11) {
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_GROUP", "", "查询客户群列表", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "分页查询客户群列表失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            this.log.error("获得客户群列表失败--------currPage：" + currPage + ",pageSize:" + pageSize);
            this.log.error(var11);
            throw new CIServiceException("获得客户群列表失败--------currPage：" + currPage + ",pageSize:" + pageSize);
        }

        if(infos != null) {
            Iterator i$ = infos.iterator();

            while(i$.hasNext()) {
                CustomGroupInfo customGroupInfo = (CustomGroupInfo)i$.next();

                try {
                    this.assignmentCustomGroupIsPush(customGroupInfo);
                } catch (Exception var10) {
                    CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_GROUP", "", "查询客户群列表", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "分页查询客户群列表失败", OperResultEnum.Failure, LogLevelEnum.Medium);
                    this.log.error("获得客户群" + customGroupInfo.getCustomGroupId() + "是否推送信息失败");
                    this.log.error(var10);
                    throw new CIServiceException("获得客户群" + customGroupInfo.getCustomGroupId() + "是否推送信息失败");
                }
            }
        }

        CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_GROUP", "", "查询客户群列表", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "分页查询客户群列表成功", OperResultEnum.Success, LogLevelEnum.Medium);
        this.log.info("分页查询所有客户群信息成功---------------currPage：" + currPage + ",pageSize:" + pageSize);
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method queryCustomGroupInfoList end");
        return infos;
    }

    public CustomGroupInfo queryCustomGroupInfoById(String customGroupId, SysInfo sysInfo) throws Exception {
        this.log.info(" =============>>>> invoke method queryCustomGroupInfoList start");
        this.log.info("customGroupId:" + customGroupId);
        this.log.info(JsonUtil.toJson(sysInfo));
        CustomGroupInfo customGroupInfo = null;

        try {
            customGroupInfo = this.customersExternalJDao.selectCustomGroupById(customGroupId);
            this.assignmentCustomGroupIsPush(customGroupInfo);
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_GROUP", "", "查询客户群", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "根据客户群ID" + customGroupId + "获得客户群成功", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var5) {
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_GROUP", "", "查询客户群", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "根据客户群ID" + customGroupId + "获得客户群失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            this.log.error("根据客户群id获得客户群信息失败---------customGroupId:" + customGroupId);
            this.log.error(var5);
            throw new CIServiceException("根据客户群id获得客户群信息失败---------customGroupId:" + customGroupId);
        }

        this.log.info("根据客户群id获得客户群信息成功,customGroupName:" + customGroupInfo.getCustomGroupName() + "---------customGroupId:" + customGroupId);
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method queryCustomGroupInfoList end");
        return customGroupInfo;
    }

    public List<CustomListInfo> queryCustomListInfo(SysInfo sysInfo, String customGroupId) throws Exception {
        this.log.info("=============>>>> invoke method queryCustomGroupInfoList start");
        this.log.info("customGroupId:" + customGroupId);
        this.log.info(JsonUtil.toJson(sysInfo));
        new ArrayList();

        List lists;
        try {
            lists = this.customersExternalJDao.queryCustomListByCustomId(customGroupId);
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_GROUP_LIST", "", "查询客户群清单", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "根据客户群ID" + customGroupId + "获得客户群清单列表成功", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var5) {
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_GROUP_LIST", "", "查询客户群清单", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "根据客户群ID" + customGroupId + "获得客户群清单列表失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            this.log.error("根据客户群id获得客户群清单列表信息失败------------customGroupId:" + customGroupId);
            this.log.error(var5);
            throw new CIServiceException("根据客户群id获得客户群清单列表信息失败------------customGroupId:" + customGroupId);
        }

        this.log.info("根据客户群id获得客户群清单列表信息成功,size:" + lists.size() + "---------customGroupId:" + customGroupId);
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method queryCustomGroupInfoList end");
        return lists;
    }

    public int queryCustomNum(String ruleStr, SysInfo sysInfo) throws Exception {
        this.log.info("=============>>>>  invoke method queryCustomNum start");
        this.log.info("ruleStr:" + ruleStr);
        this.log.info(JsonUtil.toJson(sysInfo));
        ruleStr = ruleStr.replaceAll(" ", "");
        String msg = "";
        boolean num = false;
        String countSql = null;
        boolean returnCode = true;

        int num1;
        int returnCode1;
        try {
            Date se = new Date();
            ArrayList ciLabelRuleList = new ArrayList();
            returnCode1 = this.analysisRuleStrToRuleList(ruleStr, ciLabelRuleList, (String)null);
            if(returnCode1 == 0) {
                this.log.error("解析规则字符串失败");
                throw new CIServiceException("解析规则字符串失败");
            }

            this.sortLabelRuleListBySortNum(ciLabelRuleList);
            CiCustomGroupInfo custom = new CiCustomGroupInfo();
            this.reviseCustomDataDate(custom, ciLabelRuleList);
            this.validateLabelRuleListSql(ciLabelRuleList, sysInfo, custom);
            String querySql = this.customGroupInfoService.getSelectSqlByCustomersRels((List)null, ciLabelRuleList, custom.getMonthLabelDate(), custom.getDayLabelDate(), (String)null, sysInfo.getUserId(), (Integer)null, (Integer)null, false);
            countSql = "select count(1) from (" + querySql + ") abc";
            this.log.info("COC_SQL >>>>>>> " + countSql);
            num1 = this.queryCount(countSql, sysInfo.getUserId());
            this.logExploreRecord(countSql, se, ciLabelRuleList, sysInfo.getUserId());
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_DATA_EXPLORE", "", "数据探索", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "数据探索成功，用户数为：" + num1, OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var11) {
            msg = "查询用户数量失败";
            this.log.error(msg + "EXCUTE SQL IS : " + countSql, var11);
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_DATA_EXPLORE", "", "数据探索", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "数据探索失败，执行SQL : " + countSql, OperResultEnum.Failure, LogLevelEnum.Medium);
            throw new CIServiceException("数据探索失败");
        }

        this.log.info("数据探索成功,num:" + num1);
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method queryCustomNum end");
        return returnCode1 == 0?0:num1;
    }

    private void sortLabelRuleListBySortNum(List<CiLabelRule> ciLabelRuleList) {
        Comparator comparator = new Comparator() {
            public int compare(CiLabelRule s1, CiLabelRule s2) {
                return (int)(s1.getSortNum().longValue() - s2.getSortNum().longValue());
            }

			@Override
			public int compare(Object o1, Object o2) {
				// TODO Auto-generated method stub
				return 0;
			}
        };
        Collections.sort(ciLabelRuleList, comparator);
    }

    public String addCustomGroupInfo(String ruleStr, SysInfo sysInfo, CustomGroupInfo groupInfo) throws Exception {
        this.log.info("=============>>>>  invoke method addCustomGroupInfo start");
        this.log.info("invoke method addCustomGroupInfo start");
        this.log.info(JsonUtil.toJson(sysInfo));
        this.log.info(JsonUtil.toJson(groupInfo));
        this.log.info("ruleStr：" + ruleStr);
        boolean returnCode = true;
        CiCustomGroupInfo ciCustomGroupInfo = new CiCustomGroupInfo();

        int returnCode1;
        try {
            CacheBase e = CacheBase.getInstance();
            String userId = sysInfo.getUserId();
            ciCustomGroupInfo.setTacticsId("2");
            if(StringUtil.isEmpty(groupInfo.getCustomGroupName())) {
                this.log.error("客户群名称不能为空！");
                throw new CIServiceException("客户群名称不能为空！");
            }

            ciCustomGroupInfo.setCustomGroupName(groupInfo.getCustomGroupName());
            if(groupInfo.getUpdateCycle() == null) {
                ciCustomGroupInfo.setUpdateCycle(Integer.valueOf(1));
            } else {
                ciCustomGroupInfo.setUpdateCycle(groupInfo.getUpdateCycle());
            }

            if((ciCustomGroupInfo.getUpdateCycle().intValue() == 2 || ciCustomGroupInfo.getUpdateCycle().intValue() == 3) && StringUtil.isEmpty(groupInfo.getEndDate())) {
                this.log.error("客户群有效期的结束时间不能为空！");
                throw new CIServiceException("客户群有效期的结束时间不能为空！");
            }

            Map returnMap = this.customGroupInfoService.isNameExist(ciCustomGroupInfo, sysInfo.getUserId());
            String customGroupId;
            if(!((Boolean)returnMap.get("success")).booleanValue()) {
                returnCode = false;
                customGroupId = (String)returnMap.get("cmsg");
                this.log.error(customGroupId);
                throw new CIServiceException(customGroupId);
            }

            customGroupId = this.customGroupIdGen.getId(userId);
            this.log.debug("新建客户群生成的客户群ID：===" + customGroupId);
            ciCustomGroupInfo.setCustomGroupId(customGroupId);
            ciCustomGroupInfo.setSysId(sysInfo.getSysId());
            ciCustomGroupInfo.setRuleExpress(ruleStr);
            ciCustomGroupInfo.setIsFirstFailed(Integer.valueOf(0));
            ciCustomGroupInfo.setStatus(Integer.valueOf(1));
            ciCustomGroupInfo.setCreateUserId(userId);
            ciCustomGroupInfo.setCustomGroupDesc(groupInfo.getCustomGroupDesc());
            ciCustomGroupInfo.setLabelOptRuleShow(groupInfo.getLabelOptRuleShow());
            ciCustomGroupInfo.setIsSysRecom(Integer.valueOf(ServiceConstants.IS_NOT_SYS_RECOM));
            ciCustomGroupInfo.setIsPrivate(Integer.valueOf(1));
            Date date = new Date();
            ciCustomGroupInfo.setCreateTime(date);
            ciCustomGroupInfo.setNewModifyTime(date);
            IUser iuser = PrivilegeServiceUtil.getUserById(userId);
            String createCityId = iuser.getCityid();
            ciCustomGroupInfo.setCreateCityId(createCityId);
            ciCustomGroupInfo.setCustomNum((Long)null);
            ciCustomGroupInfo.setDataTime((Date)null);
            ciCustomGroupInfo.setIsHasList(Integer.valueOf(1));
            ciCustomGroupInfo.setCreateTypeId(Integer.valueOf(1));
            if(StringUtil.isEmpty(groupInfo.getMonthLabelDate())) {
                ciCustomGroupInfo.setMonthLabelDate(e.getNewLabelMonth());
            } else {
                ciCustomGroupInfo.setMonthLabelDate(groupInfo.getMonthLabelDate());
            }

            if(StringUtil.isEmpty(groupInfo.getDayLabelDate())) {
                ciCustomGroupInfo.setDayLabelDate(e.getNewLabelDay());
            } else {
                ciCustomGroupInfo.setDayLabelDate(groupInfo.getDayLabelDate());
            }

            ArrayList ciLabelRuleList = new ArrayList();
            returnCode1 = this.analysisRuleStrToRuleList(ruleStr, ciLabelRuleList, customGroupId);
            if(returnCode1 == 0) {
                this.log.error("解析规则字符串失败");
                throw new CIServiceException("解析规则字符串失败");
            }

            this.sortLabelRuleListBySortNum(ciLabelRuleList);
            this.reviseCustomDataDate(ciCustomGroupInfo, ciLabelRuleList);
            this.validateLabelRuleListSql(ciLabelRuleList, sysInfo, ciCustomGroupInfo);
            if(ciCustomGroupInfo.getUpdateCycle().intValue() == 3) {
                ciCustomGroupInfo.setDataDate(ciCustomGroupInfo.getDayLabelDate());
            } else if(ciCustomGroupInfo.getUpdateCycle().intValue() == 2) {
                ciCustomGroupInfo.setDataDate(ciCustomGroupInfo.getMonthLabelDate());
            } else if(ciCustomGroupInfo.getMonthLabelDate() == null) {
                ciCustomGroupInfo.setDataDate(ciCustomGroupInfo.getDayLabelDate());
            } else {
                ciCustomGroupInfo.setDataDate(ciCustomGroupInfo.getMonthLabelDate());
            }

            if(groupInfo.getStartDate() != null && groupInfo.getEndDate() != null) {
                ciCustomGroupInfo.setStartDate(groupInfo.getStartDate());
                ciCustomGroupInfo.setEndDate(groupInfo.getEndDate());
            } else {
                String shopCartRuleList;
                Date labelOptRuleShow;
                String i$;
                Date shopCartRule;
                if(ciCustomGroupInfo.getUpdateCycle().intValue() == 2) {
                    shopCartRuleList = DateUtil.string2StringFormat(ciCustomGroupInfo.getMonthLabelDate(), "yyyyMM", "yyyy-MM-dd HH:mm:ss");
                    labelOptRuleShow = DateUtil.string2Date(shopCartRuleList, "yyyy-MM-dd HH:mm:ss");
                    ciCustomGroupInfo.setStartDate(labelOptRuleShow);
                    i$ = DateUtil.string2StringFormat("20991231", "yyyyMMdd", "yyyy-MM-dd HH:mm:ss");
                    shopCartRule = DateUtil.string2Date(i$, "yyyy-MM-dd HH:mm:ss");
                    ciCustomGroupInfo.setEndDate(shopCartRule);
                }

                if(ciCustomGroupInfo.getUpdateCycle().intValue() == 3) {
                    shopCartRuleList = DateUtil.string2StringFormat(ciCustomGroupInfo.getDayLabelDate(), "yyyyMM", "yyyy-MM-dd HH:mm:ss");
                    labelOptRuleShow = DateUtil.string2Date(shopCartRuleList, "yyyy-MM-dd HH:mm:ss");
                    ciCustomGroupInfo.setStartDate(labelOptRuleShow);
                    i$ = DateUtil.string2StringFormat("20991231", "yyyyMMdd", "yyyy-MM-dd HH:mm:ss");
                    shopCartRule = DateUtil.string2Date(i$, "yyyy-MM-dd HH:mm:ss");
                    ciCustomGroupInfo.setEndDate(shopCartRule);
                }

                if(ciCustomGroupInfo.getUpdateCycle().intValue() == 1) {
                    shopCartRuleList = DateUtil.string2StringFormat("20991231", "yyyyMMdd", "yyyy-MM-dd HH:mm:ss");
                    labelOptRuleShow = DateUtil.string2Date(shopCartRuleList, "yyyy-MM-dd HH:mm:ss");
                    ciCustomGroupInfo.setStartDate(new Date());
                    ciCustomGroupInfo.setEndDate(labelOptRuleShow);
                }
            }

            ArrayList shopCartRuleList1 = new ArrayList();
            this.showShopCartRuleList(ciLabelRuleList, shopCartRuleList1);
            StringBuffer labelOptRuleShow1 = new StringBuffer();
            Iterator i$1 = shopCartRuleList1.iterator();

            while(i$1.hasNext()) {
                ShopCartRule shopCartRule1 = (ShopCartRule)i$1.next();
                String attrValue = shopCartRule1.getAttrValue();
                String lableOrCustomName = shopCartRule1.getLableOrCustomName();
                String operatorElement = shopCartRule1.getOperatorElement();
                if(StringUtil.isNotEmpty(operatorElement)) {
                    labelOptRuleShow1.append(operatorElement);
                }

                if(shopCartRule1.getIsSetValue() != null && !shopCartRule1.getIsSetValue().booleanValue()) {
                    labelOptRuleShow1.append("(").append(attrValue).append(")").append(lableOrCustomName);
                } else {
                    if(StringUtil.isNotEmpty(lableOrCustomName)) {
                        labelOptRuleShow1.append(lableOrCustomName);
                    }

                    if(StringUtil.isNotEmpty(attrValue)) {
                        labelOptRuleShow1.append("[");
                        labelOptRuleShow1.append(attrValue);
                        labelOptRuleShow1.append("]");
                    }
                }
            }

            ciCustomGroupInfo.setLabelOptRuleShow(labelOptRuleShow1.toString());
            this.addCiCustomGroupInfo(ciCustomGroupInfo, ciLabelRuleList, (List)null, (CiTemplateInfo)null, userId, false, (List)null);
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_CREATE_GROUP", "", "创建客户群", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "创建客户群成功", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var21) {
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_CREATE_GROUP", "", "创建客户群", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "创建客户群失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            this.log.error("保存客户群失败");
            this.log.error(var21);
            throw new CIServiceException("保存客户群失败");
        }

        this.log.info("保存客户群客户群成功，customGroupId：" + ciCustomGroupInfo.getCustomGroupId());
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method addCustomGroupInfo end");
        return 1 == returnCode1?ciCustomGroupInfo.getCustomGroupId():String.valueOf(returnCode1);
    }

    public int updateCustomGroupInfo(String ruleStr, SysInfo sysInfo, CustomGroupInfo groupInfo) throws Exception {
        this.log.info("=============>>>> invoke method updateCustomGroupInfo start");
        this.log.info(JsonUtil.toJson(sysInfo));
        this.log.info(JsonUtil.toJson(groupInfo));
        boolean returnCode = true;
        ArrayList ciLabelRuleList = new ArrayList();

        int returnCode1;
        try {
            CiCustomGroupInfo e = this.customGroupInfoService.queryCiCustomGroupInfo(groupInfo.getCustomGroupId());
            CacheBase cache = CacheBase.getInstance();
            this.log.info("customGroupId---------------------" + groupInfo.getCustomGroupId());
            this.log.info("是否删除验证..................");
            if(e == null) {
                this.log.error("该客户群已被删除！");
                throw new CIServiceException("该客户群已被删除！");
            }

            this.log.info("客户群名称非空验证..................");
            if(StringUtil.isEmpty(groupInfo.getCustomGroupName())) {
                this.log.error("客户群名称不能为空！");
                throw new CIServiceException("客户群名称不能为空！");
            }

            this.log.info("客户群Id非空验证..................");
            if(StringUtil.isEmpty(groupInfo.getCustomGroupId())) {
                this.log.error("客户群ID不能为空！");
                throw new CIServiceException("客户群ID不能为空！");
            }

            e.setTacticsId("2");
            e.setCustomGroupName(groupInfo.getCustomGroupName());
            if(groupInfo.getUpdateCycle() == null) {
                e.setUpdateCycle(Integer.valueOf(1));
            } else {
                e.setUpdateCycle(groupInfo.getUpdateCycle());
            }

            e.setUpdateCycle(groupInfo.getUpdateCycle());
            if((groupInfo.getUpdateCycle().intValue() == 2 || groupInfo.getUpdateCycle().intValue() == 3) && (StringUtil.isEmpty(groupInfo.getEndDate()) || StringUtil.isEmpty(groupInfo.getStartDate()))) {
                this.log.error("客户群有效期的结束时间不能为空！");
                throw new CIServiceException("客户群有效期的结束时间不能为空！");
            }

            Date date;
            if(groupInfo.getUpdateCycle().intValue() == 1) {
                String returnMap = DateUtil.string2StringFormat("20991231", "yyyyMMdd", "yyyy-MM-dd HH:mm:ss");
                date = DateUtil.string2Date(returnMap, "yyyy-MM-dd HH:mm:ss");
                e.setStartDate(new Date());
                e.setEndDate(date);
            } else {
                e.setStartDate(groupInfo.getStartDate());
                e.setEndDate(groupInfo.getEndDate());
            }

            this.log.info("客户群重名验证..................");
            Map returnMap1 = this.customGroupInfoService.isNameExist(e, sysInfo.getUserId());
            if(!((Boolean)returnMap1.get("success")).booleanValue()) {
                returnCode = false;
                String date1 = (String)returnMap1.get("cmsg");
                this.log.error(date1);
                throw new CIServiceException(date1);
            }

            e.setSysId(sysInfo.getSysId());
            e.setRuleExpress(ruleStr);
            e.setOldIsHasList(Integer.valueOf(1));
            e.setIsHasList(Integer.valueOf(1));
            e.setStatus(Integer.valueOf(1));
            e.setCustomGroupDesc(groupInfo.getCustomGroupDesc());
            e.setLabelOptRuleShow(groupInfo.getLabelOptRuleShow());
            if(StringUtil.isEmpty(groupInfo.getMonthLabelDate())) {
                e.setMonthLabelDate(cache.getNewLabelMonth());
            } else {
                e.setMonthLabelDate(groupInfo.getMonthLabelDate());
            }

            if(StringUtil.isEmpty(groupInfo.getDayLabelDate())) {
                e.setDayLabelDate(cache.getNewLabelDay());
            } else {
                e.setDayLabelDate(groupInfo.getDayLabelDate());
            }

            date = new Date();
            e.setNewModifyTime(date);
            returnCode1 = this.analysisRuleStrToRuleList(ruleStr, ciLabelRuleList, groupInfo.getCustomGroupId());
            this.sortLabelRuleListBySortNum(ciLabelRuleList);
            this.reviseCustomDataDate(e, ciLabelRuleList);
            this.validateLabelRuleListSql(ciLabelRuleList, sysInfo, e);
            if(e.getUpdateCycle().intValue() == 3) {
                e.setDataDate(e.getDayLabelDate());
            } else if(e.getUpdateCycle().intValue() == 2) {
                e.setDataDate(e.getMonthLabelDate());
            } else if(e.getMonthLabelDate() == null) {
                e.setDataDate(e.getDayLabelDate());
            } else {
                e.setDataDate(e.getMonthLabelDate());
            }

            if(groupInfo.getStartDate() != null && groupInfo.getEndDate() != null) {
                e.setStartDate(groupInfo.getStartDate());
                e.setEndDate(groupInfo.getEndDate());
            } else {
                String shopCartRuleList;
                Date labelOptRuleShow;
                String i$;
                Date shopCartRule;
                if(e.getUpdateCycle().intValue() == 2) {
                    shopCartRuleList = DateUtil.string2StringFormat(e.getMonthLabelDate(), "yyyyMM", "yyyy-MM-dd HH:mm:ss");
                    labelOptRuleShow = DateUtil.string2Date(shopCartRuleList, "yyyy-MM-dd HH:mm:ss");
                    e.setStartDate(labelOptRuleShow);
                    i$ = DateUtil.string2StringFormat("20991231", "yyyyMMdd", "yyyy-MM-dd HH:mm:ss");
                    shopCartRule = DateUtil.string2Date(i$, "yyyy-MM-dd HH:mm:ss");
                    e.setEndDate(shopCartRule);
                }

                if(e.getUpdateCycle().intValue() == 3) {
                    shopCartRuleList = DateUtil.string2StringFormat(e.getDayLabelDate(), "yyyyMM", "yyyy-MM-dd HH:mm:ss");
                    labelOptRuleShow = DateUtil.string2Date(shopCartRuleList, "yyyy-MM-dd HH:mm:ss");
                    e.setStartDate(labelOptRuleShow);
                    i$ = DateUtil.string2StringFormat("20991231", "yyyyMMdd", "yyyy-MM-dd HH:mm:ss");
                    shopCartRule = DateUtil.string2Date(i$, "yyyy-MM-dd HH:mm:ss");
                    e.setEndDate(shopCartRule);
                }

                if(e.getUpdateCycle().intValue() == 1) {
                    shopCartRuleList = DateUtil.string2StringFormat("20991231", "yyyyMMdd", "yyyy-MM-dd HH:mm:ss");
                    labelOptRuleShow = DateUtil.string2Date(shopCartRuleList, "yyyy-MM-dd HH:mm:ss");
                    e.setStartDate(new Date());
                    e.setEndDate(labelOptRuleShow);
                }
            }

            ArrayList shopCartRuleList1 = new ArrayList();
            this.showShopCartRuleList(ciLabelRuleList, shopCartRuleList1);
            StringBuffer labelOptRuleShow1 = new StringBuffer();
            Iterator i$1 = shopCartRuleList1.iterator();

            while(true) {
                if(!i$1.hasNext()) {
                    e.setLabelOptRuleShow(labelOptRuleShow1.toString());
                    this.customGroupInfoService.modifyCiCustomGroupInfo(e, ciLabelRuleList, (CiTemplateInfo)null, sysInfo.getUserId(), false, (List)null);
                    CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_UPDATE_GROUP", "", "修改客户群", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "修改客户群成功", OperResultEnum.Success, LogLevelEnum.Medium);
                    this.log.info("修改客户群成功，customGroupId：" + e.getCustomGroupId());
                    break;
                }

                ShopCartRule shopCartRule1 = (ShopCartRule)i$1.next();
                String operatorElement = shopCartRule1.getOperatorElement();
                if(StringUtil.isNotEmpty(operatorElement)) {
                    labelOptRuleShow1.append(operatorElement);
                }

                String lableOrCustomName = shopCartRule1.getLableOrCustomName();
                if(StringUtil.isNotEmpty(lableOrCustomName)) {
                    labelOptRuleShow1.append(lableOrCustomName);
                }

                String attrValue = shopCartRule1.getAttrValue();
                if(StringUtil.isNotEmpty(attrValue)) {
                    labelOptRuleShow1.append("[");
                    labelOptRuleShow1.append(attrValue);
                    labelOptRuleShow1.append("]");
                }
            }
        } catch (Exception var17) {
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_UPDATE_GROUP", "", "修改客户群", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "修改客户群成功", OperResultEnum.Failure, LogLevelEnum.Medium);
            this.log.error("修改客户群失败");
            this.log.error(var17);
            throw new CIServiceException("修改客户群失败");
        }

        this.log.info("returnCode：" + returnCode1);
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method updateCustomGroupInfo end");
        return returnCode1;
    }

    public int deleteCustomGroupInfo(String customGroupId, SysInfo sysInfo) throws Exception {
        this.log.info("=============>>>>  invoke method deleteCustomGroupInfo start");
        this.log.info("customGroupId:" + customGroupId);
        this.log.info(JsonUtil.toJson(sysInfo));
        byte returnCode = 1;

        try {
            CiCustomGroupInfo e = this.customGroupInfoService.queryCiCustomGroupInfo(customGroupId);
            String userId = sysInfo.getUserId();
            if(e == null || !e.getCreateUserId().equals(userId) && !PrivilegeServiceUtil.isAdminUser(userId)) {
                returnCode = 0;
                this.log.info("删除失败，客户群id\'" + customGroupId + "\'不存在。");
                CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_DELETE_GROUP", "", "删除客户群", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "删除客户群失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            } else {
                this.customGroupInfoService.deleteUserAttentionCustom(customGroupId, userId);
                this.customGroupInfoService.deleteCiCustomGroupInfo(e);
                CIAlarmServiceUtil.deleteAlarmThresholdByBusiId("CustomersAlarm", customGroupId);
                CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_DELETE_GROUP", "", "删除客户群", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "删除客户群成功", OperResultEnum.Success, LogLevelEnum.Medium);
            }
        } catch (Exception var6) {
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_DELETE_GROUP", "", "删除客户群", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "删除客户群失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            this.log.error("删除客户群失败，customGroupId = " + customGroupId);
            this.log.error(var6);
            throw new CIServiceException("删除客户群失败，customGroupId = " + customGroupId);
        }

        this.log.info("删除客户群成功，customGroupId：" + customGroupId);
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method deleteCustomGroupInfo end");
        return returnCode;
    }

    public int pushCustomGroupInfo(String customGroupId, SysInfo sysInfo, String targetSysId) throws Exception {
        this.log.info(" =============>>>>  invoke method pushCustomGroupInfo start");
        this.log.info(JsonUtil.toJson(sysInfo));
        this.log.info("customGroupId:" + customGroupId);
        this.log.info("targetSysId:" + targetSysId);
        byte returnCode = 1;
        ArrayList sysIds = new ArrayList();
        sysIds.add(targetSysId);
        new CiCustomPushReq();

        CiCustomPushReq ciCustomPushReq;
        try {
            ciCustomPushReq = this.genCiCustomPushReq(customGroupId, sysInfo.getUserId(), sysIds);
        } catch (Exception var10) {
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_PUSH_GROUP", "", "推送设置客户群", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "推送设置客户群失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            this.log.error("构造客户群推送对象失败-----------customGroupId:" + customGroupId);
            this.log.error(var10);
            throw new CIServiceException("构造客户群推送对象失败-----------customGroupId:" + customGroupId);
        }

        CiCustomGroupInfo groupInfo = new CiCustomGroupInfo();
        groupInfo.setCustomGroupId(customGroupId);

        try {
            this.customGroupInfoService.pushCustomersAfterSave(ciCustomPushReq, String.valueOf(1), groupInfo);
        } catch (Exception var9) {
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_PUSH_GROUP", "", "推送设置客户群", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "推送设置客户群失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            this.log.error("推送设置客户群失败-----------customGroupId:" + customGroupId);
            this.log.error(var9);
            throw new CIServiceException("推送设置客户群失败-----------customGroupId:" + customGroupId);
        }

        CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_PUSH_GROUP", "", "推送设置客户群", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "推送设置客户群成功", OperResultEnum.Success, LogLevelEnum.Medium);
        this.log.info("向" + targetSysId + "推送设置客户群成功-----------customGroupId:" + customGroupId);
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method pushCustomGroupInfo end");
        return returnCode;
    }

    public List<SysInfo> querySysInfo(SysInfo sysInfo) throws Exception {
        this.log.info("=============>>>>   invoke method querySysInfo start");
        this.log.info(JsonUtil.toJson(sysInfo));
        new ArrayList();

        List infos;
        try {
            infos = this.customersExternalJDao.querySysInfos();
        } catch (Exception var4) {
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_SYSINFO_LIST", "", "获得所有外部系统列表", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "获得所有系统列表失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            this.log.error("获得外部系统列表失败");
            this.log.error(var4);
            throw new CIServiceException("获得外部系统列表失败");
        }

        CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_SYSINFO_LIST", "", "获得所有外部系统列表", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "获得所有系统列表成功", OperResultEnum.Success, LogLevelEnum.Medium);
        this.log.info("获取所有外部系统列表成功，size：" + infos.size());
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method querySysInfo end");
        return infos;
    }

    private void assignmentCustomGroupIsPush(CustomGroupInfo customGroupInfo) throws Exception {
        int isPush = this.customersJdbcDao.selectCustomerGroupIsPush(customGroupInfo.getCustomGroupId());
        customGroupInfo.setIsPush(isPush);
    }

    private void validateLabelRuleListSql(List<CiLabelRule> ciLabelRuleList, SysInfo sysInfo, CiCustomGroupInfo customGroupInfo) {
        String validateSql = "";
        boolean flag = false;

        String message;
        try {
            Set e = this.findInvalidCustomListInfoIds(ciLabelRuleList, sysInfo);
            if(e != null && e.size() > 0) {
                message = "";

                String listId;
                for(Iterator i$ = e.iterator(); i$.hasNext(); message = message + listId) {
                    listId = (String)i$.next();
                    message = message + ",";
                }

                this.log.error("您选择的客户群已失效" + message);
                flag = false;
                throw new CIServiceException("您选择的客户群已失效" + message);
            }

            validateSql = this.customGroupInfoService.getSelectSqlByCustomersRels((List)null, ciLabelRuleList, customGroupInfo.getMonthLabelDate(), customGroupInfo.getDayLabelDate(), (String)null, sysInfo.getUserId(), (Integer)null, (Integer)null, true);
            validateSql = "select 1 from (" + validateSql + ")ss where 1=2";
            this.log.info("COC_SQL >>>>>>> " + validateSql);
            flag = true;
        } catch (Exception var10) {
            message = "验证ciLabelRuleList时，SQL拼接异常";
            this.log.error(message, var10);
            throw new CIServiceException("验证ciLabelRuleList时，SQL拼接异常");
        }

        if(flag) {
            flag = this.customGroupInfoService.queryValidateSql(validateSql);
            if(!flag) {
                this.log.error("验证ciLabelRuleList有误，请检查字符串规则是否有误！");
                throw new CIServiceException("验证ciLabelRuleList有误，请检查字符串规则是否有误！");
            }

            this.log.info("验证ciLabelRuleList成功");
        }

    }

    private Set<String> findInvalidCustomListInfoIds(List<CiLabelRule> ciLabelRuleList, SysInfo sysInfo) {
        this.log.info("查询数据探索或者保存客户群的规则中所有无效的客户群清单集合 start");
        String userId = sysInfo.getUserId();
        HashSet ids = new HashSet();

        try {
            Iterator e = ciLabelRuleList.iterator();

            while(e.hasNext()) {
                CiLabelRule r = (CiLabelRule)e.next();
                int elementType = r.getElementType().intValue();
                if(elementType == 5) {
                    String listInfoId = r.getCalcuElement();
                    CiCustomGroupInfo ciCustomGroupInfo = this.customGroupInfoService.queryCiCustomGroupInfoByListInfoId(listInfoId);
                    if(1 == ciCustomGroupInfo.getStatus().intValue()) {
                        if(1 == ciCustomGroupInfo.getIsPrivate().intValue() && !userId.equals(ciCustomGroupInfo.getCreateUserId())) {
                            ids.add(listInfoId);
                        }
                    } else {
                        ids.add(listInfoId);
                    }
                }
            }
        } catch (Exception var10) {
            this.log.error("查询无效的客户群清单id集合错误", var10);
            throw new CIServiceException("查询无效的客户群清单id集合错误");
        }

        this.log.info("查询数据探索或者保存客户群的规则中所有无效的客户群清单集合 end");
        return ids;
    }

    private CiCustomPushReq genCiCustomPushReq(String customGroupId, String userId, List<String> sysIds) {
        this.log.info("构造推送对象 start");
        CiCustomPushReq req = new CiCustomPushReq();
        req.setUserId(userId);
        req.setReqTime(new Date());
        req.setStatus(Integer.valueOf(1));
        CiCustomGroupInfo ciCustomGroupInfo = this.customGroupInfoService.queryCiCustomGroupInfo(customGroupId);
        List customListList = this.customGroupInfoService.queryCiCustomListInfoByCGroupId(ciCustomGroupInfo.getCustomGroupId());
        if(customListList == null) {
            return null;
        } else {
            CiCustomListInfo customList = (CiCustomListInfo)customListList.get(0);
            if(customList == null) {
                return null;
            } else {
                req.setListTableName(customList.getListTableName());
                req.setSysIds(sysIds);
                req.setReqId("COC" + CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L));
                this.log.info("构造推送对象 end");
                return req;
            }
        }
    }

    public List<String> getLabelIds(String rule) {
        String labelIdRegex = "#[\\d]+";
        return this.getMatchResult(rule, labelIdRegex);
    }

    public List<String> getCustom(String rule) {
        String labelIdRegex = "\\$[a-zA-Z0-9_]+";
        return this.getMatchResult(rule, labelIdRegex);
    }

    public List<String> getMatchResult(String src, String regex) {
        ArrayList result = new ArrayList();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(src);

        while(matcher.find()) {
            result.add(matcher.group());
        }

        return result;
    }

    private List<String> analysisRuleStrToList(String ruleStr) {
        ArrayList arry1 = new ArrayList();
        ArrayList arry2 = new ArrayList();
        ArrayList arry3 = new ArrayList();

        try {
            String[] e = ruleStr.toLowerCase().split("and");
            int i = 0;
            String[] i$ = e;
            int str4 = e.length;

            for(int arr3 = 0; arr3 < str4; ++arr3) {
                String j = i$[arr3];
                ++i;
                arry1.add(j);
                if(i < e.length) {
                    arry1.add("and");
                }
            }

            Iterator var16 = arry1.iterator();

            String[] arr$;
            int len$;
            int i$1;
            String str5;
            String var17;
            String[] var18;
            int var19;
            while(var16.hasNext()) {
                var17 = (String)var16.next();
                var18 = var17.toLowerCase().split("or");
                var19 = 0;
                arr$ = var18;
                len$ = var18.length;

                for(i$1 = 0; i$1 < len$; ++i$1) {
                    str5 = arr$[i$1];
                    ++var19;
                    arry2.add(str5);
                    if(var19 < var18.length) {
                        arry2.add("or");
                    }
                }
            }

            var16 = arry2.iterator();

            while(var16.hasNext()) {
                var17 = (String)var16.next();
                var18 = var17.toLowerCase().split("REMOVE".toLowerCase());
                var19 = 0;
                arr$ = var18;
                len$ = var18.length;

                for(i$1 = 0; i$1 < len$; ++i$1) {
                    str5 = arr$[i$1];
                    ++var19;
                    arry3.add(str5);
                    if(var19 < var18.length) {
                        arry3.add("-");
                    }
                }
            }

            return arry3;
        } catch (Exception var15) {
            this.log.error("根据规则解析成数组失败");
            this.log.error(var15);
            throw new CIServiceException("根据规则解析成数组失败");
        }
    }

    private int analysisRuleStrToRuleList(String ruleStr, List<CiLabelRule> rules, String customGroupId) {
        this.log.info("解析规则字符串封装成List<CiLabelRule> ---------- start");
        ruleStr = ruleStr.replace(" ", "");
        this.log.info("去掉空格之后的伪SQL" + ruleStr);
        this.log.info("把伪SQL封装成List---------   start");
        byte resultCode = 1;
        Object arry = new ArrayList();
        new ArrayList();
        String[] arr = ruleStr.split("\'");
        String str;
        if(arr.length > 1) {
            for(int sortNum = 0; sortNum < arr.length; ++sortNum) {
                List arry1;
                if(sortNum == arr.length - 1) {
                    arry1 = this.analysisRuleStrToList(arr[sortNum]);
                    ((List)arry).addAll(arry1);
                } else if((sortNum + 1) % 2 != 0) {
                    arry1 = this.analysisRuleStrToList(arr[sortNum]);
                    int i$ = arry1.size() - 1;
                    str = (String)arry1.get(i$) + arr[sortNum + 1];
                    arry1.set(arry1.size() - 1, str);
                    ((List)arry).addAll(arry1);
                }
            }
        } else {
            arry = this.analysisRuleStrToList(ruleStr);
        }

        this.log.info("List size:" + ((List)arry).size());
        this.log.info("把伪SQL封装成List-----------------------   end");
        this.log.info("把List封装成List<CiLabelRule>-------------------   start");
        Long var20 = Long.valueOf(0L);
        Iterator var21 = ((List)arry).iterator();
        while(true) {
            do {
                if(!var21.hasNext()) {
                    this.log.info("把List封装成List<CiLabelRule>   end");
                    this.log.info("解析规则字符串封装成List<CiLabelRule> end");
                    return resultCode;
                }

                str = (String)var21.next();
            } while(StringUtil.isEmpty(str));

            this.log.info("遍历List-------------------" + str);
            var20 = Long.valueOf(var20.longValue() + 1L);
            CiLabelRule labelRule = new CiLabelRule();
            labelRule.setCustomGroupId(customGroupId);
            labelRule.setSortNum(var20);
            labelRule.setCustomType(Integer.valueOf(1));
            if(str.startsWith("and")) {
                labelRule.setElementType(Integer.valueOf(1));
                labelRule.setCalcuElement("and");
            } else if(str.startsWith("or")) {
                labelRule.setElementType(Integer.valueOf(1));
                labelRule.setCalcuElement("or");
            } else if(str.startsWith("-")) {
                labelRule.setElementType(Integer.valueOf(1));
                labelRule.setCalcuElement("-");
            } else {
                int labelR;
                int var22;
                int var25;
                String var27;
                if(str.startsWith("#")) {
                    var22 = 0;
                    labelR = str.indexOf(41);
                    var25 = str.indexOf(40);
                    if(var25 != -1) {
                        var22 = str.length() - labelR - 1;
                    } else if(labelR == -1 && var25 == -1) {
                        var22 = 0;
                    } else if(labelR != -1) {
                        var22 = str.length() - labelR;
                    }

                    if(var22 > 0) {
                        for(int var28 = 0; var28 < var22; ++var28) {
                            var20 = Long.valueOf(var20.longValue() + 1L);
                            CiLabelRule var29 = new CiLabelRule();
                            var29.setSortNum(var20);
                            var29.setCustomGroupId(customGroupId);
                            var29.setCalcuElement(String.valueOf(')'));
                            var29.setCustomType(Integer.valueOf(1));
                            var29.setElementType(Integer.valueOf(3));
                            rules.add(var29);
                        }
                    }

                    var27 = str.substring(0, str.length() - var22);
                    labelRule.setElementType(Integer.valueOf(2));
                    this.ruleStrToLabelRule(labelRule, var27, resultCode);
                } else {
                    CiCustomListInfo customListInfo;
                    String var24;
                    if(str.startsWith("$")) {
                        var22 = 0;
                        labelR = str.indexOf(41);
                        if(labelR != -1) {
                            var22 = str.length() - labelR;
                        }

                        if(var22 > 0) {
                            for(var25 = 0; var25 < var22; ++var25) {
                                var20 = Long.valueOf(var20.longValue() + 1L);
                                CiLabelRule var26 = new CiLabelRule();
                                var26.setSortNum(var20);
                                var26.setCustomGroupId(customGroupId);
                                var26.setCalcuElement(String.valueOf(')'));
                                var26.setCustomType(Integer.valueOf(1));
                                var26.setElementType(Integer.valueOf(3));
                                rules.add(var26);
                            }
                        }

                        var24 = str.substring(0, str.length() - var22);
                        labelRule.setElementType(Integer.valueOf(5));
                        var27 = var24.substring(1).toUpperCase();
                        labelRule.setCalcuElement(var27);
                        customListInfo = this.ciCustomListInfoHDao.selectById(var27);
                        if(customListInfo == null) {
                            throw new CIServiceException("规则字符中清单表名不存在！");
                        }

                        labelRule.setAttrVal(customListInfo.getDataDate());
                        CiCustomGroupInfo e = this.ciCustomGroupInfoHDao.selectCustomGroupById(customListInfo.getCustomGroupId());
                        labelRule.setCustomOrLabelName(e.getCustomGroupName());
                        labelRule.setLabelFlag(e.getUpdateCycle());
                    } else if(str.startsWith(String.valueOf('('))) {
                        boolean num = false;
                        if(str.contains(String.valueOf(')'))) {
                            var22 = str.indexOf(35);
                        } else {
                            labelR = str.lastIndexOf(40);
                            var22 = labelR + 1;
                        }

                        if(var22 > 0) {
                            for(labelR = 0; labelR < var22; ++labelR) {
                                if(labelR == 0) {
                                    labelRule.setCustomGroupId(customGroupId);
                                    labelRule.setCalcuElement(String.valueOf('('));
                                    labelRule.setCustomType(Integer.valueOf(1));
                                    labelRule.setElementType(Integer.valueOf(3));
                                } else {
                                    var20 = Long.valueOf(var20.longValue() + 1L);
                                    CiLabelRule customListTabelName = new CiLabelRule();
                                    customListTabelName.setSortNum(var20);
                                    customListTabelName.setCustomGroupId(customGroupId);
                                    customListTabelName.setCalcuElement(String.valueOf('('));
                                    customListTabelName.setCustomType(Integer.valueOf(1));
                                    customListTabelName.setElementType(Integer.valueOf(3));
                                    rules.add(customListTabelName);
                                }
                            }

                            var20 = Long.valueOf(var20.longValue() + 1L);
                            str = str.substring(var22, str.length());
                        }

                        CiLabelRule var23;
                        if(str.startsWith("#")) {
                            var23 = new CiLabelRule();
                            var23.setSortNum(var20);
                            var23.setElementType(Integer.valueOf(2));
                            this.ruleStrToLabelRule(var23, str, resultCode);
                            rules.add(var23);
                        } else if(str.startsWith("$")) {
                            var23 = new CiLabelRule();
                            var23.setElementType(Integer.valueOf(5));
                            var24 = str.substring(1);
                            CiCustomGroupInfo info = null;
                            customListInfo = this.ciCustomListInfoHDao.selectById(var24);
                            if(customListInfo == null) {
                                throw new CIServiceException("规则字符中清单表名不存在！");
                            }

                            var23.setAttrVal(customListInfo.getDataDate());

                            try {
                                info = this.ciCustomGroupInfoHDao.selectCustomGroupById(customListInfo.getCustomGroupId());
                                var23.setLabelFlag(info.getUpdateCycle());
                                var23.setCalcuElement(var24.toUpperCase());
                                var23.setElementType(Integer.valueOf(5));
                                var23.setCustomOrLabelName(info.getCustomGroupName());
                                rules.add(var23);
                            } catch (Exception var18) {
                                boolean var19 = false;
                                this.log.error("根据清单表名" + var24 + "获得客户群失败");
                                this.log.error(var18);
                                throw new CIServiceException("根据清单表名" + var24 + "获得客户群失败");
                            }
                        }
                    }
                }
            }

            rules.add(labelRule);
        }
    }

    private void ruleStrToLabelRule(CiLabelRule labelRule, String str, int resultCode) {
        this.log.info("根据标签的不同类型 封装CiLabelRule对象 start");
        this.log.info("要封装对象的字符串---------" + str);
        labelRule.setLabelFlag(Integer.valueOf(1));
        List labelIdStrs = this.getLabelIds(str);
        if(labelIdStrs != null && labelIdStrs.size() != 0) {
            String labelId = ((String)labelIdStrs.get(0)).substring(1);
            CacheBase cache = CacheBase.getInstance();
            CiLabelInfo labelInfo = cache.getEffectiveLabel(labelId);
            labelRule.setLabelTypeId(labelInfo.getLabelTypeId());
            labelRule.setCustomOrLabelName(labelInfo.getLabelName());
            if(labelInfo != null) {
                this.log.info("labelName:" + labelInfo.getLabelName() + "labelId:" + labelInfo.getLabelId());
                labelRule.setCalcuElement(labelInfo.getLabelId().toString());
                String gtStr = ">";
                String geStr = ">=";
                String ltStr = "<";
                String leStr = "<=";
                String eqStr = "=";
                int gtIndex = str.indexOf(">");
                int ltIndex = str.indexOf("<");
                int geIndex = str.indexOf(">=");
                int leIndex = str.indexOf("<=");
                int eqIndex = str.indexOf("=");
                int labelTypeId = labelInfo.getLabelTypeId().intValue();
                int valStr;
                if(labelTypeId == 1) {
                    this.log.info("标识型标签");
                    labelRule.setMinVal(Double.valueOf(0.0D));
                    valStr = str.indexOf("=") + 1;
                    String arry = str.substring(valStr, str.length());
                    labelRule.setMaxVal(Double.valueOf(arry));
                    labelRule.setLabelFlag(Integer.valueOf(arry));
                }

                int rightNum;
                if(labelTypeId == 5) {
                    this.log.info("枚举型标签");
                    valStr = str.indexOf(40);
                    int var34 = str.indexOf(41);
                    rightNum = str.toUpperCase().indexOf("NOT");
                    if(rightNum != -1 && var34 > rightNum) {
                        labelRule.setLabelFlag(Integer.valueOf(0));
                    } else {
                        labelRule.setLabelFlag(Integer.valueOf(1));
                    }

                    String notNum = str.substring(valStr + 1, var34);
                    labelRule.setAttrVal(notNum);
                    List dimTableVal = null;
                    DimTableDefine define = null;

                    try {
                        DimTableServiceImpl idStrs = (DimTableServiceImpl)SystemServiceLocator.getInstance().getService("dimTable_serviceTarget");
                        String enumNames = this.ciLabelInfoJDao.selectDimTransIdByLabelId(labelInfo.getLabelId());
                        define = idStrs.findDefineById(enumNames);
                    } catch (Exception var31) {
                        this.log.error("获得枚举标签的枚举值错误");
                        this.log.error(var31);
                        throw new CIServiceException("获得枚举标签的枚举值错误");
                    }

                    String[] var39 = notNum.split(",");
                    StringBuffer var40 = new StringBuffer();
                    String[] enumNameStr = var39;
                    int len$ = var39.length;

                    for(int i$ = 0; i$ < len$; ++i$) {
                        String id = enumNameStr[i$];
                        dimTableVal = this.ciLabelInfoJDao.getAllDimDataByDefine(define, id);
                        var40.append(((Map)dimTableVal.get(0)).get("V_NAME"));
                        var40.append(",");
                    }

                    String var41 = var40.toString();
                    var41 = var41.substring(0, var41.length() - 1);
                    labelRule.setAttrName(var41);
                }

                String var33;
                if(labelTypeId == 7) {
                    this.log.info("文本型标签 ");
                    var33 = "";
                    String[] var35 = str.split("like");
                    if(str.indexOf("%") != -1) {
                        String[] var37 = str.split("%");
                        var33 = var37[1];
                    } else {
                        var33 = var35[1];
                    }

                    rightNum = str.indexOf("like");
                    int var38 = str.toUpperCase().indexOf("NOT");
                    if(var38 != -1 && rightNum > var38) {
                        labelRule.setLabelFlag(Integer.valueOf(0));
                    } else {
                        labelRule.setLabelFlag(Integer.valueOf(1));
                    }

                    labelRule.setDarkValue(var33);
                }

                if(labelTypeId == 6) {
                    this.log.info("日期型标签 ");
                    String[] var36;
                    if(gtIndex != -1 && ltIndex == -1 && leIndex == -1 && geIndex == -1) {
                        var36 = str.split(">");
                        labelRule.setStartTime(var36[1]);
                        labelRule.setLeftZoneSign(gtStr);
                    }

                    if(geIndex != -1 && ltIndex == -1 && leIndex == -1) {
                        var36 = str.split(">=");
                        labelRule.setStartTime(var36[1]);
                        labelRule.setLeftZoneSign(geStr);
                    }

                    if(ltIndex != -1 && gtIndex == -1 && geIndex == -1) {
                        var36 = str.split("<");
                        labelRule.setEndTime(var36[1]);
                        labelRule.setRightZoneSign(ltStr);
                    }

                    if(leIndex != -1 && gtIndex == -1 && geIndex == -1) {
                        var36 = str.split("<=");
                        labelRule.setEndTime(var36[1]);
                        labelRule.setRightZoneSign(leStr);
                    }

                    if(eqIndex != -1) {
                        var36 = str.split("=");
                        labelRule.setAttrVal(var36[1]);
                    }
                }

                if(labelTypeId == 4) {
                    this.log.info("连续型标签 ");
                    if(gtIndex != -1 && ltIndex == -1 && leIndex == -1 && geIndex == -1) {
                        var33 = str.substring(gtIndex + 1, str.length());
                        labelRule.setContiueMinVal(var33);
                        labelRule.setLeftZoneSign(gtStr);
                    }

                    if(geIndex != -1 && ltIndex == -1 && leIndex == -1) {
                        var33 = str.substring(geIndex + 2, str.length());
                        labelRule.setContiueMinVal(var33);
                        labelRule.setLeftZoneSign(geStr);
                    }

                    if(ltIndex != -1 && gtIndex == -1 && geIndex == -1) {
                        var33 = str.substring(ltIndex + 1, str.length());
                        labelRule.setContiueMaxVal(var33);
                        labelRule.setRightZoneSign(ltStr);
                    }

                    if(leIndex != -1 && gtIndex == -1 && geIndex == -1) {
                        var33 = str.substring(leIndex + 2, str.length());
                        labelRule.setContiueMaxVal(var33);
                        labelRule.setRightZoneSign(leStr);
                    }

                    if(eqIndex != -1 && leIndex == -1 && geIndex == -1) {
                        var33 = str.split(eqStr)[1];
                        labelRule.setExactValue(var33);
                    }
                }

                this.log.info("根据标签的不同类型 封装CiLabelRule对象 end");
            } else {
                boolean var32 = false;
                this.log.info("所拼标签" + labelId + "不存在---------");
                throw new CIServiceException("所拼标签" + labelId + "不存在---------");
            }
        } else {
            throw new CIServiceException("根据标签的不同类型 封装CiLabelRule对象错误，所得到的标签ID为空");
        }
    }

    private void logExploreRecord(String sql, Date startDate, List<CiLabelRule> ciLabelRuleList, String userId) {
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
        Iterator useElementsStr = ciLabelRuleList.iterator();

        while(useElementsStr.hasNext()) {
            CiLabelRule ciListExeSqlAllList = (CiLabelRule)useElementsStr.next();
            int elementType = ciListExeSqlAllList.getElementType().intValue();
            if(elementType == 2) {
                String labelId = ciListExeSqlAllList.getCalcuElement();
                CiLabelInfo labelInfo = cache.getEffectiveLabel(labelId);
                if(labelInfo != null) {
                    useElements.append(labelInfo.getLabelName() + ",");
                }
            } else if(elementType == 5) {
                useElements.append(ciListExeSqlAllList.getCalcuElement() + ",");
            }
        }

        String useElementsStr1 = useElements.toString();
        if(StringUtil.isNotEmpty(useElementsStr1)) {
            useElementsStr1 = useElementsStr1.substring(0, useElementsStr1.lastIndexOf(","));
        }

        logRecord.setUseLabels(useElementsStr1);
        logRecord.setUserId(userId);

        try {
            this.customGroupInfoService.saveCiExploreLogRecord(logRecord);
        } catch (Exception var18) {
            var18.printStackTrace();
        }

        List ciListExeSqlAllList1 = this.generateCiExploreSqlAllList(logRecord, sql);
        this.customGroupInfoService.saveCiExploreSqlAllList(ciListExeSqlAllList1);
    }

    private List<CiExploreSqlAll> generateCiExploreSqlAllList(CiExploreLogRecord ciExploreLogRecord, String sql) {
        if(StringUtil.isEmpty(sql)) {
            return null;
        } else {
            ArrayList result = new ArrayList();
            String temp = sql;

            String s;
            for(int row = 0; temp.length() > 0; temp = temp.substring(s.length(), temp.length())) {
                s = CiUtil.subStringByByte(temp, 4000, "UTF-8");
                ++row;
                CiExploreSqlAll exploreSqlAll = new CiExploreSqlAll((Integer)null, ciExploreLogRecord.getRecordId(), s, Integer.valueOf(row));
                result.add(exploreSqlAll);
            }

            return result;
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    private int queryCount(String countSql, String userId) throws CIServiceException {
        boolean num = false;

        try {
            String e = Configure.getInstance().getProperty("CYCLE_CUSTOM_WEBSERVICE");
            IUser iuser = PrivilegeServiceUtil.getUserById(userId);
            String cityId = iuser.getCityid();
            int num1;
            if(e != null && StringUtil.isNotEmpty(e) && e.equalsIgnoreCase("true") && StringUtil.isNotEmpty(cityId)) {
                CiTaskServerCityRel cityRel = this.ciTaskServerCityRelHDao.selectById(Integer.valueOf(cityId));
                String databaseUrl = "";
                if(null != cityRel && StringUtil.isNotEmpty(cityRel.getDatabseUrl())) {
                    databaseUrl = cityRel.getDatabseUrl();
                }

                String username = "";
                if(null != cityRel && StringUtil.isNotEmpty(cityRel.getUsername())) {
                    username = cityRel.getUsername();
                }

                String password = "";
                if(null != cityRel && StringUtil.isNotEmpty(cityRel.getPassword())) {
                    password = cityRel.getPassword();
                }

                num1 = this.customersJdbcDao.selectCountByJdbc(countSql, databaseUrl, username, password);
            } else {
                num1 = this.customersJdbcDao.selectCountByJdbc(countSql, "", "", "");
            }

            return num1;
        } catch (Exception var11) {
            this.log.error("查询总数异常", var11);
            throw new CIServiceException("查询总数异常", var11);
        }
    }

    private boolean addCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo, List<CiLabelRule> ciLabelRuleList, List<CiCustomSourceRel> ciCustomSourceRelList, CiTemplateInfo ciTemplateInfo, String userId, boolean isSaveTemplate, List<CiGroupAttrRel> ciGroupAttrRelList) throws CIServiceException {
        boolean flag = false;
        String customGroupId = ciCustomGroupInfo.getCustomGroupId();

        try {
            ciCustomGroupInfo.setIsFirstFailed(Integer.valueOf(0));
            if(StringUtil.isNotEmpty(ciCustomGroupInfo.getListCreateTime())) {
                ciCustomGroupInfo.setCustomListCreateFailed(Integer.valueOf(0));
            }

            ciCustomGroupInfo.setCreateUserId(userId);
            Date e = new Date();
            ciCustomGroupInfo.setCreateTime(e);
            ciCustomGroupInfo.setNewModifyTime(e);
            IUser message1 = PrivilegeServiceUtil.getUserById(userId);
            String createCityId = message1.getCityid();
            ciCustomGroupInfo.setCreateCityId(createCityId);
            StringBuffer sceneIdsBuf = new StringBuffer("");
            String sceneIds = "";
            if(ciCustomGroupInfo.getSceneList() != null && ciCustomGroupInfo.getSceneList().size() > 0) {
                List hasLocalList = ciCustomGroupInfo.getSceneList();
                Iterator centerCityId = hasLocalList.iterator();

                while(centerCityId.hasNext()) {
                    CiCustomSceneRel history = (CiCustomSceneRel)centerCityId.next();
                    history.setModifyTime(e);
                    history.getId().setCustomGroupId(customGroupId);
                    if(StringUtil.isNotEmpty(history.getId().getSceneId())) {
                        String dayFlag = history.getId().getSceneId();
                        sceneIdsBuf.append(dayFlag).append(",");
                    }
                }
            }

            if(StringUtil.isNotEmpty(sceneIdsBuf)) {
                int hasLocalList1 = sceneIdsBuf.lastIndexOf(",");
                sceneIds = sceneIdsBuf.substring(0, hasLocalList1);
            }

            boolean hasLocalList2 = false;
            String centerCityId1 = Configure.getInstance().getProperty("CENTER_CITYID");
            if(!centerCityId1.equals(createCityId) && ciLabelRuleList != null && ciLabelRuleList.size() != 0) {
                Iterator history1 = ciLabelRuleList.iterator();

                while(history1.hasNext()) {
                    CiLabelRule dayFlag1 = (CiLabelRule)history1.next();
                    if(5 == dayFlag1.getElementType().intValue()) {
                        CiCustomGroupInfo monthFlag = this.customersJdbcDao.queryCiCustomGroupInfoByListInfoId(dayFlag1.getCalcuElement());
                        if(createCityId.equals(monthFlag.getCreateCityId())) {
                            hasLocalList2 = true;
                            break;
                        }
                    }
                }
            }

            if(hasLocalList2) {
                ciCustomGroupInfo.setIsContainLocalList(Integer.valueOf(1));
            } else {
                ciCustomGroupInfo.setIsContainLocalList(Integer.valueOf(0));
            }

            ciCustomGroupInfo.setIsSysRecom(Integer.valueOf(ServiceConstants.IS_NOT_SYS_RECOM));
            CiCustomModifyHistory history2 = this.addCiCustomModifyHistory(ciCustomGroupInfo, userId);
            history2.setModifyTime(e);
            history2.setSceneIds(sceneIds);
            boolean dayFlag2 = false;
            boolean monthFlag1 = false;
            boolean dateLabelFlag = false;
            List customListInfoList;
            if(ciLabelRuleList != null && ciLabelRuleList.size() > 0) {
                CacheBase serverId = CacheBase.getInstance();
                Iterator ciCustomListInfoList = ciLabelRuleList.iterator();

                label182:
                while(true) {
                    while(true) {
                        CiLabelRule ciCustomListInfo;
                        CiLabelInfo customListInfo1;
                        do {
                            while(true) {
                                if(!ciCustomListInfoList.hasNext()) {
                                    break label182;
                                }

                                ciCustomListInfo = (CiLabelRule)ciCustomListInfoList.next();
                                if(2 == ciCustomListInfo.getElementType().intValue()) {
                                    customListInfo1 = serverId.getEffectiveLabel(ciCustomListInfo.getCalcuElement());
                                    break;
                                }

                                if(5 == ciCustomListInfo.getElementType().intValue()) {
                                    Integer customListInfo = ciCustomListInfo.getLabelFlag();
                                    if(customListInfo != null) {
                                        if(customListInfo.intValue() == 2) {
                                            dayFlag2 = true;
                                        }

                                        if(customListInfo.intValue() == 1) {
                                            monthFlag1 = true;
                                        }
                                    }
                                }
                            }
                        } while(customListInfo1 == null);

                        if(customListInfo1.getUpdateCycle().intValue() == 1) {
                            dayFlag2 = true;
                        }

                        if(customListInfo1.getUpdateCycle().intValue() == 2) {
                            monthFlag1 = true;
                        }

                        if(6 == customListInfo1.getLabelTypeId().intValue() && ciCustomListInfo.getIsNeedOffset() != null && ciCustomListInfo.getIsNeedOffset().intValue() == ServiceConstants.IS_NEED_OFFSET_YES) {
                            dateLabelFlag = true;
                        } else if(8 == customListInfo1.getLabelTypeId().intValue()) {
                            customListInfoList = ciCustomListInfo.getChildCiLabelRuleList();
                            if(customListInfoList != null) {
                                Iterator implClassName = customListInfoList.iterator();

                                while(implClassName.hasNext()) {
                                    CiLabelRule tableName = (CiLabelRule)implClassName.next();
                                    if(6 == tableName.getLabelTypeId().intValue() && tableName.getIsNeedOffset().intValue() == ServiceConstants.IS_NEED_OFFSET_YES) {
                                        dateLabelFlag = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if(!dayFlag2) {
                ciCustomGroupInfo.setDayLabelDate((String)null);
            }

            if(!monthFlag1) {
                ciCustomGroupInfo.setMonthLabelDate((String)null);
            }

            if(dateLabelFlag) {
                ciCustomGroupInfo.setOffsetDate(ciCustomGroupInfo.getDataDate());
            }

            switch(ciCustomGroupInfo.getCreateTypeId().intValue()) {
            case 1:
            case 2:
            case 3:
            case 8:
                if(ciCustomGroupInfo.getIsHasList().intValue() == 0) {
                    ciCustomGroupInfo.setDataStatus(Integer.valueOf(3));
                    ciCustomGroupInfo.setDayLabelDate((String)null);
                    ciCustomGroupInfo.setMonthLabelDate((String)null);
                } else {
                    ciCustomGroupInfo.setDataStatus(Integer.valueOf(1));
                }

                String serverId1 = Configure.getInstance().getProperty("CURRENT_SERVER_ID");
                if(StringUtil.isNotEmpty(serverId1)) {
                    ciCustomGroupInfo.setServerId(serverId1);
                }

                ciCustomGroupInfo.setStatus(Integer.valueOf(1));
                ciCustomGroupInfo.setCustomNum((Long)null);
                ciCustomGroupInfo.setDataTime((Date)null);
                if(isSaveTemplate) {
                    this.saveTemplate(ciLabelRuleList, ciCustomGroupInfo, ciTemplateInfo, userId);
                }

                List ciCustomListInfoList1 = this.newCiCustomListInfoList(ciCustomGroupInfo, ciLabelRuleList, (List)ciGroupAttrRelList);
                if(ciGroupAttrRelList == null || ((List)ciGroupAttrRelList).size() == 0) {
                    if(ciGroupAttrRelList == null) {
                        ciGroupAttrRelList = new ArrayList();
                    }

                    CiGroupAttrRel ciCustomListInfo2 = new CiGroupAttrRel();
                    CiGroupAttrRelId customListInfo3 = new CiGroupAttrRelId();
                    customListInfo3.setCustomGroupId(ciCustomGroupInfo.getCustomGroupId());
                    customListInfo3.setAttrCol("ATTR_COL");
                    customListInfo3.setModifyTime(new Date());
                    ciCustomListInfo2.setId(customListInfo3);
                    ciCustomListInfo2.setStatus(Integer.valueOf(2));
                    ((List)ciGroupAttrRelList).add(ciCustomListInfo2);
                }

                flag = this.addOrModifyCiCustomGroupInfoByOnlyLabel(ciCustomGroupInfo, history2, ciCustomListInfoList1, ciLabelRuleList, (List)ciGroupAttrRelList, false);
                break;
            case 4:
                CiCustomListInfo ciCustomListInfo1 = this.newCiCustomListInfo(ciCustomGroupInfo, ciLabelRuleList, (List)ciGroupAttrRelList);
                flag = this.addCiCustomGroupInfoWithCustomer(ciCustomGroupInfo, history2, ciLabelRuleList, (List)null, ciCustomListInfo1);
                break;
            case 5:
            case 6:
            case 9:
                CiCustomListInfo customListInfo2 = this.newCiCustomListInfo(ciCustomGroupInfo, ciLabelRuleList, (List)ciGroupAttrRelList);
                flag = this.addCiCustomGroupInfoWithCustomer(ciCustomGroupInfo, history2, ciLabelRuleList, ciCustomSourceRelList, customListInfo2);
                break;
            case 7:
            case 10:
            case 11:
                ciCustomGroupInfo.setDataStatus(Integer.valueOf(1));
                ciCustomGroupInfo.setStatus(Integer.valueOf(1));
                ciCustomGroupInfo.setCustomNum((Long)null);
                ciCustomGroupInfo.setDataTime((Date)null);
                ciCustomGroupInfo.setUpdateCycle(Integer.valueOf(1));
                if(!centerCityId1.equals(createCityId)) {
                    ciCustomGroupInfo.setIsContainLocalList(Integer.valueOf(1));
                }

                history2.setUpdateCycle(Integer.valueOf(1));
                customListInfoList = this.newCiCustomListInfoList(ciCustomGroupInfo, (List)null, (List)null);
                flag = this.addOrModifyCiCustomGroupInfoByOnlyLabel(ciCustomGroupInfo, history2, customListInfoList, (List)null, (List)ciGroupAttrRelList, false);
                break;
            case 12:
                ciCustomGroupInfo.setDataStatus(Integer.valueOf(1));
                ciCustomGroupInfo.setStatus(Integer.valueOf(1));
                ciCustomGroupInfo.setCustomNum((Long)null);
                ciCustomGroupInfo.setDataTime((Date)null);
                ciCustomGroupInfo.setIsHasList(Integer.valueOf(1));
                if(!centerCityId1.equals(createCityId)) {
                    ciCustomGroupInfo.setIsContainLocalList(Integer.valueOf(1));
                }

                history2.setUpdateCycle(ciCustomGroupInfo.getUpdateCycle());
                String implClassName1 = Configure.getInstance().getProperty("IMPORT_BY_TABLE_CLASS_NAME");
                String tableName1 = ciCustomGroupInfo.getTabelName();
                String errorMsg;
                if(!StringUtil.isNotEmpty(implClassName1) || !StringUtil.isNotEmpty(tableName1)) {
                    errorMsg = "";
                    if(StringUtil.isEmpty(implClassName1)) {
                        errorMsg = "导入表创建客户群service实现类名称未配置！";
                    } else if(StringUtil.isEmpty(tableName1)) {
                        errorMsg = "数据来源表为空！";
                    }

                    this.log.error(errorMsg);
                    throw new CIServiceException(errorMsg);
                }

                errorMsg = ciCustomGroupInfo.getWhereSql();
                this.log.debug("表导入创建客户群的whereSql：" + errorMsg);
                flag = this.addOrModifyCiCustomGroupInfoByOnlyLabel(ciCustomGroupInfo, history2, (List)null, (List)null, (List)ciGroupAttrRelList, false);
                if(flag) {
                    IImportTableService importService = (IImportTableService)SystemServiceLocator.getInstance().getService(implClassName1);
                    flag = importService.importTable(ciCustomGroupInfo, tableName1, errorMsg, (List)ciGroupAttrRelList, userId);
                }
            }

            return flag;
        } catch (Exception var30) {
            String message = "创建客户群错误";
            this.log.error(message, var30);
            throw new CIServiceException(message, var30);
        }
    }

    private CiCustomModifyHistory addCiCustomModifyHistory(CiCustomGroupInfo ciCustomGroupInfo, String userId) throws CIServiceException {
        CiCustomModifyHistory ciCustomModifyHistory = null;

        try {
            ciCustomModifyHistory = new CiCustomModifyHistory();
            String e = ciCustomGroupInfo.getCustomGroupId();
            if(StringUtil.isNotEmpty(e)) {
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

    private void saveTemplate(List<CiLabelRule> ciLabelRuleList, CiCustomGroupInfo ciCustomGroupInfo, CiTemplateInfo ciTemplateInfo, String userId) throws Exception {
        ArrayList newLabelRuleList = new ArrayList();
        Iterator i$ = ciLabelRuleList.iterator();

        while(i$.hasNext()) {
            CiLabelRule rule = (CiLabelRule)i$.next();
            CiLabelRule newRule = rule.clone();
            newLabelRuleList.add(newRule);
        }

        ciTemplateInfo.setTemplateDesc(ciCustomGroupInfo.getCustomGroupDesc());
        ciTemplateInfo.setLabelOptRuleShow(ciCustomGroupInfo.getLabelOptRuleShow());
        this.ciTemplateInfoService.addTemplateInfo(ciTemplateInfo, newLabelRuleList, userId);
    }

    private List<CiCustomListInfo> newCiCustomListInfoList(CiCustomGroupInfo ciCustomGroupInfo, List<CiLabelRule> ciLabelRuleList, List<CiGroupAttrRel> ciGroupAttrRelList) {
        CacheBase cache = CacheBase.getInstance();
        ArrayList list = new ArrayList();
        if(1 == ciCustomGroupInfo.getUpdateCycle().intValue()) {
            CiCustomListInfo startDate = this.newCiCustomListInfo(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList, ciCustomGroupInfo.getDataDate(), ciCustomGroupInfo.getMonthLabelDate(), ciCustomGroupInfo.getDayLabelDate());
            if("2".equals(ciCustomGroupInfo.getTacticsId())) {
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
            if(2 == ciCustomGroupInfo.getUpdateCycle().intValue()) {
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

                if(startDateStr.equals(newestDate)) {
                    i = this.newCiCustomListInfo(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList, startDateStr, ciCustomGroupInfo.getMonthLabelDate(), ciCustomGroupInfo.getDayLabelDate());
                    list.add(i);
                } else if(var17.before(newest)) {
                    var18 = 0;
                    j = 0;

                    while(!startDateStr.equals(newestDate) && StringUtils.isNotEmpty(newestDate)) {
                        newestDate = DateUtil.getOffsetDateByDate(newestDate, var18, 0);
                        result = this.customGroupInfoService.validateLabelDataDate(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList, newestDate, ciCustomGroupInfo.getDayLabelDate());
                        if("2".equals(result)) {
                            ciCustomListInfo = this.newCiCustomListInfo(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList, newestDate, newestDate, ciCustomGroupInfo.getDayLabelDate());
                            list.add(ciCustomListInfo);
                        }

                        var18 = -1;
                        ++j;
                        if(j > 100) {
                            break;
                        }
                    }
                }
            } else if(3 == ciCustomGroupInfo.getUpdateCycle().intValue()) {
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

                if(startDateStr.equals(newestDate)) {
                    i = this.newCiCustomListInfo(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList, startDateStr, ciCustomGroupInfo.getMonthLabelDate(), ciCustomGroupInfo.getDayLabelDate());
                    list.add(i);
                } else if(var17.before(newest)) {
                    var18 = 0;
                    j = 0;

                    while(!startDateStr.equals(newestDate) && StringUtils.isNotEmpty(newestDate)) {
                        newestDate = DateUtil.getOffsetDateByDate(newestDate, var18, 1);
                        result = this.customGroupInfoService.validateLabelDataDate(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList, ciCustomGroupInfo.getMonthLabelDate(), newestDate);
                        if("2".equals(result)) {
                            ciCustomListInfo = this.newCiCustomListInfo(ciCustomGroupInfo, ciLabelRuleList, ciGroupAttrRelList, newestDate, ciCustomGroupInfo.getMonthLabelDate(), newestDate);
                            list.add(ciCustomListInfo);
                        }

                        var18 = -1;
                        ++j;
                        if(j > 1000) {
                            break;
                        }
                    }
                }
            }
        }

        this.log.debug("list.size():=====" + list.size());
        return list;
    }

    private boolean addCiCustomGroupInfoWithCustomer(CiCustomGroupInfo ciCustomGroupInfo, CiCustomModifyHistory ciCustomModifyHistory, List<CiLabelRule> ciLabelRuleList, List<CiCustomSourceRel> ciCustomSourceRelList, CiCustomListInfo ciCustomListInfo) throws CIServiceException {
        boolean flag = false;

        String message;
        try {
            ciCustomGroupInfo.setDataStatus(Integer.valueOf(1));
            this.ciCustomGroupInfoHDao.insertCustomGroup(ciCustomGroupInfo);
            String e = ciCustomGroupInfo.getCustomGroupId();
            if(ciCustomModifyHistory != null && StringUtil.isNotEmpty(ciCustomModifyHistory.getCustomGroupName())) {
                ciCustomModifyHistory.setCustomGroupId(e);
                this.ciCustomModifyHistoryHDao.insertCiCustomModifyHistory(ciCustomModifyHistory);
            }

            if(ciCustomListInfo != null && StringUtil.isNotEmpty(ciCustomListInfo.getListTableName())) {
                ciCustomListInfo.setCustomGroupId(e);
                this.ciCustomListInfoHDao.insertCiCustomListInfo(ciCustomListInfo);
            }

            if(ciLabelRuleList != null && ciLabelRuleList.size() != 0) {
                message = ciCustomGroupInfo.getCreateUserId();
                Integer ciCustomSourceRel = PrivilegeServiceUtil.getUserDeptId(message);
                Iterator i$ = ciLabelRuleList.iterator();

                while(i$.hasNext()) {
                    CiLabelRule rule = (CiLabelRule)i$.next();
                    rule.setRuleId((String)null);
                    rule.setCustomId(e);
                    rule.setCustomType(Integer.valueOf(1));
                    this.ciLabelRuleHDao.insertCiLabelRule(rule);
                    if(2 == rule.getElementType().intValue()) {
                        CiUserUseLabel ciUserUseLabel = new CiUserUseLabel();
                        CiUserUseLabelId ciUserUseLabelId = new CiUserUseLabelId();
                        ciUserUseLabelId.setUserId(message);
                        ciUserUseLabelId.setDeptId(ciCustomSourceRel + "");
                        ciUserUseLabelId.setLabelId(new Integer(rule.getCalcuElement()));
                        ciUserUseLabelId.setUseTime(new Date());
                        ciUserUseLabel.setId(ciUserUseLabelId);
                        ciUserUseLabel.setLabelUseTypeId(Integer.valueOf(2));
                        this.ciUserUseLabelHDao.insertOrUpdateCiUserUseLabel(ciUserUseLabel);
                    }
                }
            }

            if(ciCustomSourceRelList != null && ciCustomSourceRelList.size() != 0) {
                Iterator message1 = ciCustomSourceRelList.iterator();

                while(message1.hasNext()) {
                    CiCustomSourceRel ciCustomSourceRel1 = (CiCustomSourceRel)message1.next();
                    ciCustomSourceRel1.setIndexId((String)null);
                    ciCustomSourceRel1.setCustomGroupId(e);
                }

                this.ciCustomSourceRelHDao.insertCiCustomSourceRelList(ciCustomSourceRelList);
            }

            this.customGroupInfoService.genCusomerList(ciCustomGroupInfo);
            flag = true;
            return flag;
        } catch (Exception var14) {
            message = "保存客户群相关信息错误";
            this.log.error(message, var14);
            throw new CIServiceException(message, var14);
        }
    }

    private boolean addOrModifyCiCustomGroupInfoByOnlyLabel(CiCustomGroupInfo ciCustomGroupInfo, CiCustomModifyHistory ciCustomModifyHistory, List<CiCustomListInfo> ciCustomListInfoList, List<CiLabelRule> ciLabelRuleList, List<CiGroupAttrRel> ciGroupAttrRelList, boolean isEdit) throws CIServiceException {
        boolean flag = false;

        try {
            labelRuleLock.lock();
            this.ciCustomGroupInfoHDao.insertCustomGroup(ciCustomGroupInfo);
            String toHasList = ciCustomGroupInfo.getCustomGroupId();
            if(ciCustomModifyHistory != null && StringUtil.isNotEmpty(ciCustomModifyHistory.getCustomGroupName())) {
                ciCustomModifyHistory.setCustomGroupId(toHasList);
                this.ciCustomModifyHistoryHDao.insertCiCustomModifyHistory(ciCustomModifyHistory);
            }

            if(ciCustomListInfoList != null && ciCustomListInfoList.size() > 0) {
                Iterator message1 = ciCustomListInfoList.iterator();

                while(message1.hasNext()) {
                    CiCustomListInfo i$ = (CiCustomListInfo)message1.next();
                    i$.setCustomGroupId(toHasList);
                    this.ciCustomListInfoHDao.insertCiCustomListInfo(i$);
                }
            }

            if(ciLabelRuleList != null && ciLabelRuleList.size() != 0) {
                ArrayList message2 = new ArrayList();
                CacheBase i$2 = CacheBase.getInstance();

                Iterator ciGroupAttrRel;
                CiLabelRule e;
                for(ciGroupAttrRel = ciLabelRuleList.iterator(); ciGroupAttrRel.hasNext(); this.ciLabelRuleHDao.insertCiLabelRule(e)) {
                    e = (CiLabelRule)ciGroupAttrRel.next();
                    e.setRuleId((String)null);
                    e.setCustomId(toHasList);
                    e.setCustomType(Integer.valueOf(1));
                    int parentRuleId = e.getElementType().intValue();
                    if(parentRuleId == 2) {
                        String childRules = e.getCalcuElement();
                        CiLabelInfo i$1 = i$2.getEffectiveLabel(childRules);
                        int childRule = i$1.getLabelTypeId().intValue();
                        if(childRule == 8) {
                            message2.add(e);
                        }

                        String labelTypeId;
                        String[] exactValue;
                        String exactValueArr;
                        if(childRule == 5) {
                            labelTypeId = e.getAttrVal();
                            if(StringUtils.isNotEmpty(labelTypeId)) {
                                exactValue = labelTypeId.split(",");
                                if(exactValue.length > 20) {
                                    try {
                                        exactValueArr = this.customGroupInfoService.createValueTableName();
                                        e.setTableName(exactValueArr);
                                        this.customGroupInfoService.addExactValueToTable(labelTypeId, exactValueArr);
                                        e.setAttrVal((String)null);
                                    } catch (Exception var34) {
                                        this.log.error("枚举值存于数据库表中失败", var34);
                                    }
                                }
                            }
                        }

                        if(childRule == 7) {
                            labelTypeId = e.getExactValue();
                            if(StringUtils.isNotEmpty(labelTypeId)) {
                                exactValue = labelTypeId.split(",");
                                if(exactValue.length > 20) {
                                    try {
                                        exactValueArr = this.customGroupInfoService.createValueTableName();
                                        e.setTableName(exactValueArr);
                                        this.customGroupInfoService.addExactValueToTable(labelTypeId, exactValueArr);
                                        e.setExactValue((String)null);
                                    } catch (Exception var33) {
                                        this.log.error("精确值存于数据库表中失败", var33);
                                    }
                                }
                            }
                        }
                    }
                }

                ciGroupAttrRel = message2.iterator();

                while(ciGroupAttrRel.hasNext()) {
                    e = (CiLabelRule)ciGroupAttrRel.next();
                    String parentRuleId1 = e.getRuleId();
                    List childRules1 = e.getChildCiLabelRuleList();

                    CiLabelRule childRule1;
                    for(Iterator i$4 = childRules1.iterator(); i$4.hasNext(); this.ciLabelRuleHDao.insertCiLabelRule(childRule1)) {
                        childRule1 = (CiLabelRule)i$4.next();
                        childRule1.setRuleId((String)null);
                        childRule1.setParentId(parentRuleId1);
                        childRule1.setCustomId(toHasList);
                        childRule1.setCustomType(Integer.valueOf(1));
                        int labelTypeId1 = childRule1.getLabelTypeId().intValue();
                        String e1;
                        String exactValue1;
                        String[] exactValueArr1;
                        if(labelTypeId1 == 5) {
                            exactValue1 = childRule1.getAttrVal();
                            if(StringUtils.isNotEmpty(exactValue1)) {
                                exactValueArr1 = exactValue1.split(",");
                                if(exactValueArr1.length > 20) {
                                    try {
                                        e1 = this.customGroupInfoService.createValueTableName();
                                        childRule1.setTableName(e1);
                                        this.customGroupInfoService.addExactValueToTable(exactValue1, e1);
                                        childRule1.setAttrVal((String)null);
                                    } catch (Exception var32) {
                                        this.log.error("枚举值存于数据库表中失败", var32);
                                    }
                                }
                            }
                        }

                        if(labelTypeId1 == 7) {
                            exactValue1 = childRule1.getExactValue();
                            if(StringUtils.isNotEmpty(exactValue1)) {
                                exactValueArr1 = exactValue1.split(",");
                                if(exactValueArr1.length > 20) {
                                    try {
                                        e1 = this.customGroupInfoService.createValueTableName();
                                        childRule1.setTableName(e1);
                                        this.customGroupInfoService.addExactValueToTable(exactValue1, e1);
                                        childRule1.setExactValue((String)null);
                                    } catch (Exception var31) {
                                        this.log.error("精确值存于数据库表中失败", var31);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Date message3 = new Date();
            if(ciGroupAttrRelList != null && ciGroupAttrRelList.size() > 0) {
                Iterator i$3 = ciGroupAttrRelList.iterator();

                while(i$3.hasNext()) {
                    CiGroupAttrRel ciGroupAttrRel1 = (CiGroupAttrRel)i$3.next();

                    try {
                        ciGroupAttrRel1.getId().setCustomGroupId(toHasList);
                        ciGroupAttrRel1.getId().setModifyTime(message3);
                        this.ciGroupAttrRelHDao.insertCiGroupAttrRelHDao(ciGroupAttrRel1);
                    } catch (Exception var30) {
                        this.log.error("保存客户群属性错误", var30);
                    }
                }
            }

            flag = true;
        } catch (Exception var35) {
            String message = "保存客户群相关信息错误";
            this.log.error(message, var35);
            throw new CIServiceException(message, var35);
        } finally {
            labelRuleLock.unlock();
        }

        boolean toHasList1 = false;
        if(ciCustomGroupInfo.getOldIsHasList() != null && ciCustomGroupInfo.getOldIsHasList().intValue() == 0 && ciCustomGroupInfo.getIsHasList() != ciCustomGroupInfo.getOldIsHasList()) {
            toHasList1 = true;
        }

        if((ciCustomGroupInfo.getIsHasList() != null && ciCustomGroupInfo.getIsHasList().intValue() == 1 && !isEdit || toHasList1) && StringUtil.isEmpty(ciCustomGroupInfo.getListCreateTime())) {
            this.customGroupInfoService.genCusomerList(ciCustomGroupInfo);
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
        if(reviseDateModel != null && "2".equals(reviseDateModel.getReviseType())) {
            ciCustomListInfo.setMonthLabelDate(reviseDateModel.getReviseMonth());
        } else if(reviseDateModel != null && "4".equals(reviseDateModel.getReviseType())) {
            ciCustomListInfo.setMonthLabelDate(ciCustomGroupInfo.getMonthLabelDate());
        }

        return ciCustomListInfo;
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
        if(reviseDateModel != null && "2".equals(reviseDateModel.getReviseType())) {
            ciCustomListInfo.setMonthLabelDate(reviseDateModel.getReviseMonth());
        } else if(reviseDateModel != null && "4".equals(reviseDateModel.getReviseType())) {
            ciCustomListInfo.setMonthLabelDate(month);
        }

        return ciCustomListInfo;
    }

    private void reviseDayLabelDate(CiCustomListInfo ciCustomListInfo, List<CiLabelRule> ciLabelRuleList, List<CiGroupAttrRel> ciGroupAttrRelList) {
        CacheBase cache = CacheBase.getInstance();
        String day = ciCustomListInfo.getDayLabelDate();
        int dayNum = 0;
        if(StringUtil.isNotEmpty(day)) {
            dayNum = Integer.valueOf(day).intValue();
        }

        int reviseDay = 2147483647;
        this.log.debug("所选日期 dayNum:" + dayNum);
        Iterator i$;
        int attrSource;
        String labelIdStr;
        CiLabelInfo ciLabelInfo;
        int updateCycle;
        String labelDataDate;
        int tempNum;
        if(ciLabelRuleList != null && ciLabelRuleList.size() > 0) {
            i$ = ciLabelRuleList.iterator();

            while(i$.hasNext()) {
                CiLabelRule rule = (CiLabelRule)i$.next();
                if(!StringUtil.isNotEmpty(rule.getParentId())) {
                    attrSource = rule.getElementType().intValue();
                    if(attrSource == 2) {
                        labelIdStr = rule.getCalcuElement();
                        ciLabelInfo = cache.getEffectiveLabel(labelIdStr);
                        updateCycle = ciLabelInfo.getUpdateCycle().intValue();
                        labelDataDate = ciLabelInfo.getDataDate();
                        if(1 == updateCycle && StringUtil.isNotEmpty(labelDataDate) && dayNum != 0) {
                            tempNum = Integer.valueOf(labelDataDate).intValue();
                            if(tempNum < dayNum && reviseDay > tempNum) {
                                reviseDay = tempNum;
                            }
                        }
                    }
                }
            }
        }

        if(ciGroupAttrRelList != null && ciGroupAttrRelList.size() > 0) {
            i$ = ciGroupAttrRelList.iterator();

            while(i$.hasNext()) {
                CiGroupAttrRel rule1 = (CiGroupAttrRel)i$.next();
                attrSource = rule1.getAttrSource();
                if(attrSource == 2) {
                    labelIdStr = rule1.getLabelOrCustomId();
                    ciLabelInfo = cache.getEffectiveLabel(labelIdStr);
                    updateCycle = ciLabelInfo.getUpdateCycle().intValue();
                    labelDataDate = ciLabelInfo.getDataDate();
                    if(1 == updateCycle && StringUtil.isNotEmpty(labelDataDate) && dayNum != 0) {
                        tempNum = Integer.valueOf(labelDataDate).intValue();
                        if(tempNum < dayNum && reviseDay > tempNum) {
                            reviseDay = tempNum;
                        }
                    }
                }
            }
        }

        if(reviseDay != 2147483647) {
            ciCustomListInfo.setDayLabelDate(reviseDay + "");
        }

    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public void genCusomerList(CiCustomGroupInfo ciCustomGroupInfo, Boolean isPeriodCreateListTable) {
        CustomerListCreaterThread creator = null;

        try {
            creator = (CustomerListCreaterThread)SystemServiceLocator.getInstance().getService("customerListCreaterThread");
            CiCustomGroupInfo e = ciCustomGroupInfo.clone();
            creator.setCiCustomGroupInfo(e);
            creator.setIsPeriodCreateListTable(isPeriodCreateListTable);
            creator.setNeedRecreateListFile(this.isNeedRecreateListFile(ciCustomGroupInfo));
            ThreadPool.getInstance().execute(creator, false, e.getCreateCityId());
        } catch (Exception var5) {
            this.log.error("线程池异常", var5);
        }

    }

    private ReviseDateModel queryReviseDate(CiCustomGroupInfo ciCustomGroupInfo, List<CiLabelRule> ciLabelRuleList, List<CiGroupAttrRel> ciGroupAttrRelList, String month) {
        ReviseDateModel reviseDateModel = new ReviseDateModel();
        String reviseType = null;
        CacheBase cache = CacheBase.getInstance();
        boolean satisfyMonth = true;
        int monthNum = 0;
        if(StringUtil.isNotEmpty(month)) {
            monthNum = Integer.valueOf(month).intValue();
        }

        int reviseMonth = 2147483647;
        this.log.debug("所选月分 monthNum:" + monthNum);
        Iterator updateCycle;
        int attrSource;
        String labelIdStr;
        CiLabelInfo ciLabelInfo;
        int updateCycle1;
        String dataDate;
        int tempNum;
        if(ciLabelRuleList != null && ciLabelRuleList.size() > 0) {
            updateCycle = ciLabelRuleList.iterator();

            while(updateCycle.hasNext()) {
                CiLabelRule rule = (CiLabelRule)updateCycle.next();
                if(!StringUtil.isNotEmpty(rule.getParentId())) {
                    attrSource = rule.getElementType().intValue();
                    if(attrSource == 2) {
                        labelIdStr = rule.getCalcuElement();
                        ciLabelInfo = cache.getEffectiveLabel(labelIdStr);
                        updateCycle1 = ciLabelInfo.getUpdateCycle().intValue();
                        dataDate = ciLabelInfo.getDataDate();
                        if(2 == updateCycle1 && StringUtil.isNotEmpty(dataDate) && monthNum != 0) {
                            tempNum = Integer.valueOf(dataDate).intValue();
                            if(tempNum < monthNum) {
                                satisfyMonth = false;
                                if(reviseMonth > tempNum) {
                                    reviseMonth = tempNum;
                                }
                            }
                        }
                    }
                }
            }
        }

        if(ciGroupAttrRelList != null && ciGroupAttrRelList.size() > 0) {
            updateCycle = ciGroupAttrRelList.iterator();

            while(updateCycle.hasNext()) {
                CiGroupAttrRel rule1 = (CiGroupAttrRel)updateCycle.next();
                attrSource = rule1.getAttrSource();
                if(attrSource == 2) {
                    labelIdStr = rule1.getLabelOrCustomId();
                    ciLabelInfo = cache.getEffectiveLabel(labelIdStr);
                    updateCycle1 = ciLabelInfo.getUpdateCycle().intValue();
                    dataDate = ciLabelInfo.getDataDate();
                    if(2 == updateCycle1 && StringUtil.isNotEmpty(dataDate) && monthNum != 0) {
                        tempNum = Integer.valueOf(dataDate).intValue();
                        if(tempNum < monthNum) {
                            satisfyMonth = false;
                            if(reviseMonth > tempNum) {
                                reviseMonth = tempNum;
                            }
                        }
                    }
                }
            }
        }

        int updateCycle2 = ciCustomGroupInfo.getUpdateCycle().intValue();
        if(1 == updateCycle2) {
            if(!satisfyMonth) {
                reviseType = "2";
            } else {
                reviseType = "4";
            }
        } else if(2 == updateCycle2) {
            reviseType = "4";
        } else if(3 == updateCycle2) {
            if(!satisfyMonth) {
                reviseType = "2";
            } else {
                reviseType = "4";
            }
        }

        reviseDateModel.setReviseType(reviseType);
        if(reviseMonth != 2147483647) {
            reviseDateModel.setReviseMonth(reviseMonth + "");
        }

        this.log.debug("return reviseDateModel: type:" + reviseDateModel.getReviseType() + " ReviseMonth:" + reviseDateModel.getReviseMonth());
        return reviseDateModel;
    }

    private boolean isNeedRecreateListFile(CiCustomGroupInfo ciCustomGroupInfo) {
        boolean result = false;
        Integer updateCycle = ciCustomGroupInfo.getUpdateCycle();
        if(1 == updateCycle.intValue()) {
            List listInfos = this.ciCustomListInfoHDao.selectByCustomGroupId(ciCustomGroupInfo.getCustomGroupId());
            Iterator i$ = listInfos.iterator();

            while(i$.hasNext()) {
                CiCustomListInfo info = (CiCustomListInfo)i$.next();
                if(info.getFileCreateStatus() != null && 3 == info.getFileCreateStatus().intValue()) {
                    result = true;
                    info.setFileCreateStatus(Integer.valueOf(2));
                    this.ciCustomListInfoHDao.updateCiCustomListInfo(info);
                }
            }
        }

        return result;
    }

    private List<ShopCartRule> showShopCartRuleList(List<CiLabelRule> ciLabelRuleListTemp, List<ShopCartRule> shopCartRuleList) {
        if(ciLabelRuleListTemp != null && ciLabelRuleListTemp.size() > 0) {
            for(int i = 0; i < ciLabelRuleListTemp.size(); ++i) {
                CiLabelRule labelRule = (CiLabelRule)ciLabelRuleListTemp.get(i);
                labelRule.setQueryWay("1");
                int elementType = labelRule.getElementType().intValue();
                String calcuElement = labelRule.getCalcuElement();
                ShopCartRule shopCartRule1;
                if(elementType == 3) {
                    shopCartRule1 = new ShopCartRule();
                    shopCartRule1.setOperatorElement(calcuElement);
                    shopCartRuleList.add(shopCartRule1);
                } else if(elementType == 1) {
                    shopCartRule1 = new ShopCartRule();
                    String var16 = "";
                    if("and".equals(calcuElement)) {
                        var16 = "且";
                    } else if("or".equals(calcuElement)) {
                        var16 = "或";
                    } else {
                        var16 = "剔除";
                    }

                    shopCartRule1.setOperatorElement(var16);
                    CiLabelRule var17 = (CiLabelRule)ciLabelRuleListTemp.get(i + 1);
                    var17.setQueryWay("1");
                    ShopCartRule shopCartRule11;
                    if(var17.getElementType().intValue() == 3) {
                        shopCartRule11 = new ShopCartRule();
                        shopCartRule11.setOperatorElement(var17.getCalcuElement());
                        shopCartRuleList.add(shopCartRule1);
                        shopCartRuleList.add(shopCartRule11);
                    } else {
                        StringBuffer var18;
                        if(var17.getElementType().intValue() == 2) {
                            var18 = new StringBuffer();
                            if(var17.getLabelTypeId().intValue() == 1) {
                                shopCartRule1.setIsSetValue(Boolean.valueOf(false));
                                if(var17.getLabelFlag().intValue() != 1) {
                                    var18.append("非");
                                } else {
                                    var18.append("是");
                                }
                            } else if(var17.getLabelFlag().intValue() != 1) {
                                var18.append("(非)");
                            }

                            var18.append(this.customGroupInfoService.ruleAttrVal(var17));
                            shopCartRule1.setAttrValue(var18.toString());
                            shopCartRule1.setLableOrCustomName(var17.getCustomOrLabelName());
                            if((var17.getLabelTypeId().intValue() == 4 || var17.getLabelTypeId().intValue() == 5 || var17.getLabelTypeId().intValue() == 7 || var17.getLabelTypeId().intValue() == 8 || var17.getLabelTypeId().intValue() == 6) && StringUtil.isEmpty(shopCartRule1.getAttrValue())) {
                                shopCartRule1.setIsSetValue(Boolean.valueOf(false));
                            }

                            shopCartRuleList.add(shopCartRule1);
                        } else if(var17.getElementType().intValue() == 5) {
                            var18 = new StringBuffer();
                            if(StringUtil.isNotEmpty(var17.getAttrVal())) {
                                var18.append("已选清单：");
                                var18.append(var17.getAttrVal());
                            }

                            shopCartRule1.setAttrValue(var18.toString());
                            shopCartRule1.setLableOrCustomName(var17.getCustomOrLabelName());
                            if(StringUtil.isEmpty(shopCartRule1.getAttrValue()) && StringUtil.isNotEmpty(shopCartRule1.getLableOrCustomName())) {
                                shopCartRule1.setIsSetValue(Boolean.valueOf(false));
                            }

                            shopCartRuleList.add(shopCartRule1);
                        } else if(var17.getElementType().intValue() == 6) {
                            if(StringUtil.isEmpty(shopCartRule1.getAttrValue()) && StringUtil.isNotEmpty(shopCartRule1.getLableOrCustomName())) {
                                shopCartRule1.setIsSetValue(Boolean.valueOf(false));
                            }

                            shopCartRuleList.add(shopCartRule1);
                            shopCartRule11 = new ShopCartRule();
                            shopCartRule11.setOperatorElement("(");
                            shopCartRuleList.add(shopCartRule11);
                            List list1 = var17.getChildCiLabelRuleList();
                            this.showShopCartRuleList(list1, shopCartRuleList);
                            ShopCartRule shopCartRule21 = new ShopCartRule();
                            shopCartRule21.setOperatorElement(")");
                            shopCartRuleList.add(shopCartRule21);
                        }
                    }

                    ++i;
                } else if(elementType == 2) {
                    StringBuffer var13 = new StringBuffer();
                    if(labelRule.getLabelTypeId().intValue() == 1) {
                        if(((CiLabelRule)ciLabelRuleListTemp.get(i)).getLabelFlag().intValue() != 1) {
                            var13.append("非");
                        } else {
                            var13.append("是");
                        }
                    } else if(((CiLabelRule)ciLabelRuleListTemp.get(i)).getLabelFlag().intValue() != 1) {
                        var13.append("(非)");
                    }

                    var13.append(this.customGroupInfoService.ruleAttrVal(labelRule));
                    ShopCartRule var15 = new ShopCartRule();
                    if(labelRule.getLabelTypeId().intValue() == 1) {
                        var15.setIsSetValue(Boolean.valueOf(false));
                    }

                    var15.setAttrValue(var13.toString());
                    var15.setLableOrCustomName(labelRule.getCustomOrLabelName());
                    if((labelRule.getLabelTypeId().intValue() == 4 || labelRule.getLabelTypeId().intValue() == 5 || labelRule.getLabelTypeId().intValue() == 7 || labelRule.getLabelTypeId().intValue() == 8 || labelRule.getLabelTypeId().intValue() == 6) && StringUtil.isEmpty(var15.getAttrValue())) {
                        var15.setIsSetValue(Boolean.valueOf(false));
                    }

                    shopCartRuleList.add(var15);
                } else if(elementType == 5) {
                    shopCartRule1 = new ShopCartRule();
                    StringBuffer list = new StringBuffer();
                    if(StringUtil.isNotEmpty(((CiLabelRule)ciLabelRuleListTemp.get(i)).getAttrVal())) {
                        list.append("已选清单：");
                        list.append(((CiLabelRule)ciLabelRuleListTemp.get(i)).getAttrVal());
                    }

                    shopCartRule1.setAttrValue(list.toString());
                    shopCartRule1.setLableOrCustomName(labelRule.getCustomOrLabelName());
                    if(StringUtil.isEmpty(shopCartRule1.getAttrValue())) {
                        shopCartRule1.setIsSetValue(Boolean.valueOf(false));
                    }

                    shopCartRuleList.add(shopCartRule1);
                } else if(elementType == 6) {
                    shopCartRule1 = new ShopCartRule();
                    shopCartRule1.setOperatorElement("(");
                    shopCartRuleList.add(shopCartRule1);
                    List var14 = ((CiLabelRule)ciLabelRuleListTemp.get(i)).getChildCiLabelRuleList();
                    this.showShopCartRuleList(var14, shopCartRuleList);
                    ShopCartRule shopCartRule2 = new ShopCartRule();
                    shopCartRule2.setOperatorElement(")");
                    shopCartRuleList.add(shopCartRule2);
                }
            }
        }

        return shopCartRuleList;
    }

    private void reviseCustomDataDate(CiCustomGroupInfo groupInfo, List<CiLabelRule> ciLabelRuleList) {
        this.log.info("解析标签日期 start");
        Integer dayLabelDate = Integer.valueOf(2147483647);
        Integer monthLabelDate = Integer.valueOf(2147483647);
        if(ciLabelRuleList != null) {
            for(int i = 0; i < ciLabelRuleList.size(); ++i) {
                CiLabelRule rule = (CiLabelRule)ciLabelRuleList.get(i);
                if(!StringUtil.isNotEmpty(rule.getParentId()) && rule.getElementType().intValue() == 2) {
                    CiLabelInfo ciLabelInfo = CacheBase.getInstance().getEffectiveLabel(rule.getCalcuElement());
                    Integer updateCycle = ciLabelInfo.getUpdateCycle();
                    String dataDate = ciLabelInfo.getDataDate();
                    this.log.info("获取标签：" + ciLabelInfo.getLabelName() + ";标签数据日期：" + dataDate);
                    if(updateCycle.intValue() == 1) {
                        if(dayLabelDate.intValue() >= Integer.valueOf(dataDate).intValue()) {
                            dayLabelDate = Integer.valueOf(dataDate);
                        }
                    } else if(updateCycle.intValue() == 2 && monthLabelDate.intValue() >= Integer.valueOf(dataDate).intValue()) {
                        monthLabelDate = Integer.valueOf(dataDate);
                    }
                }
            }
        }

        if(dayLabelDate.intValue() == 2147483647) {
            groupInfo.setDayLabelDate((String)null);
        } else {
            groupInfo.setDayLabelDate(String.valueOf(dayLabelDate));
        }

        if(monthLabelDate.intValue() == 2147483647) {
            groupInfo.setMonthLabelDate((String)null);
        } else {
            groupInfo.setMonthLabelDate(String.valueOf(monthLabelDate));
        }

        this.log.info("解析完毕：客户群日标签数据日期：" + groupInfo.getDayLabelDate() + "；客户群月标签数据日期：" + groupInfo.getMonthLabelDate());
        this.log.info("解析标签日期 end");
    }

    public static void main(String[] args) {
        CiCustomGroupInfo c = new CiCustomGroupInfo();
        CiLabelRule c1 = new CiLabelRule();
        c1.setElementType(Integer.valueOf(2));
        c1.setUpdateCycle(Integer.valueOf(1));
        c1.setDataDate("20150526");
        CiLabelRule c2 = new CiLabelRule();
        c2.setElementType(Integer.valueOf(2));
        c2.setUpdateCycle(Integer.valueOf(1));
        c2.setDataDate("20050524");
        CiLabelRule c3 = new CiLabelRule();
        c3.setElementType(Integer.valueOf(2));
        c3.setUpdateCycle(Integer.valueOf(1));
        c3.setDataDate("20150527");
        CiLabelRule c4 = new CiLabelRule();
        c4.setElementType(Integer.valueOf(2));
        c4.setUpdateCycle(Integer.valueOf(2));
        c4.setDataDate("201501");
        CiLabelRule c5 = new CiLabelRule();
        c5.setElementType(Integer.valueOf(2));
        c5.setUpdateCycle(Integer.valueOf(2));
        c5.setDataDate("201502");
        CiLabelRule c6 = new CiLabelRule();
        c6.setElementType(Integer.valueOf(2));
        c6.setUpdateCycle(Integer.valueOf(2));
        c6.setDataDate("201505");
        CiLabelRule c7 = new CiLabelRule();
        c7.setElementType(Integer.valueOf(2));
        c7.setUpdateCycle(Integer.valueOf(2));
        c7.setDataDate("201505");
        CiLabelRule c8 = new CiLabelRule();
        c8.setElementType(Integer.valueOf(2));
        c8.setUpdateCycle(Integer.valueOf(2));
        c8.setDataDate("201105");
        CustomGroupInfoServiceImpl i = new CustomGroupInfoServiceImpl();
        ArrayList l = new ArrayList();
        l.add(c1);
        l.add(c2);
        l.add(c3);
        l.add(c4);
        l.add(c5);
        l.add(c6);
        l.add(c7);
        l.add(c8);
        i.reviseCustomDataDate(c, l);
        System.out.println(c.getDayLabelDate() + "-" + c.getMonthLabelDate());
    }
}
