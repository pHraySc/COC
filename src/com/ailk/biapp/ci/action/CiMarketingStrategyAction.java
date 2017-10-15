package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiCustomProductTacticsRel;
import com.ailk.biapp.ci.entity.CiCustomSourceRel;
import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiMarketTactics;
import com.ailk.biapp.ci.entity.CiProductInfo;
import com.ailk.biapp.ci.entity.CiTemplateInfo;
import com.ailk.biapp.ci.entity.CustomProductMatch;
import com.ailk.biapp.ci.entity.DimBrand;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiMarketTacticsService;
import com.ailk.biapp.ci.service.ICiMarketingStrategyService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.task.CiProductMatchThread;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.GroupCalcSqlPaser;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.ThreadPool;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;

import edu.emory.mathcs.backport.java.util.Collections;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiMarketingStrategyAction extends CIBaseAction {
    private Logger log = Logger.getLogger(this.getClass());
    private Pager pager;
    private String witchPage;
    private CiCustomGroupInfo customGroup;
    private CiMarketTactics marketTactics;
    private List<CiMarketTactics> marketTacticsList;
    private CiCustomProductTacticsRel customProductRel;
    private List<CiProductInfo> productInfoList;
    private List<CustomProductMatch> productMatchList;
    private List<CustomProductMatch> sysProductMatchList;
    @Autowired
    private ICustomersManagerService customersService;
    @Autowired
    private ICustomersManagerService customersManagerService;
    @Autowired
    private ICiMarketTacticsService marketTacticsService;
    @Autowired
    private ICiMarketingStrategyService marketingStrategyService;

    public CiMarketingStrategyAction() {
    }

    public void sysMatch() {
        boolean success = false;
        CiCustomGroupInfo customGroupInfo = null;
        String msg = "";
        HashMap returnMsg = new HashMap();
        if(this.customGroup != null) {
            String response = this.customGroup.getListTableName();

            try {
                customGroupInfo = this.customersService.queryCiCustomGroupInfo(this.customGroup.getCustomGroupId());
                if(customGroupInfo.getProductAutoMacthFlag() == null) {
                    customGroupInfo.setProductAutoMacthFlag(Integer.valueOf(0));
                }

                customGroupInfo.setProductAutoMacthFlag(Integer.valueOf(customGroupInfo.getProductAutoMacthFlag().intValue() + 2));
                this.customersService.saveCiCustomGroupInfo(customGroupInfo);
                CiProductMatchThread e = (CiProductMatchThread)SystemServiceLocator.getInstance().getService("ciProductMatchThread");
                CiCustomListInfo e1 = this.customersManagerService.queryCiCustomListInfoById(response);
                e.setCiCustomListInfo(e1);
                ThreadPool.getInstance().execute(e, true);
                success = true;
                returnMsg.put("msg", msg);
                returnMsg.put("success", Boolean.valueOf(success));
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MATCH_ADD", customGroupInfo.getCustomGroupId(), customGroupInfo.getCustomGroupName(), "系统自动匹配【" + customGroupInfo.getCustomGroupName() + "】开始进行匹配", OperResultEnum.Success, LogLevelEnum.Medium);
            } catch (Exception var9) {
                this.log.error(var9);
                success = false;
                msg = "客户群清单系统匹配失败";
                returnMsg.put("msg", msg);
                returnMsg.put("success", Boolean.valueOf(success));
                if(customGroupInfo != null) {
                    CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MATCH_ADD", customGroupInfo.getCustomGroupId(), "-1", "系统自动匹配【" + customGroupInfo.getCustomGroupName() + "】匹配失败", OperResultEnum.Failure, LogLevelEnum.Medium);
                }
            }
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
        } catch (Exception var8) {
            this.log.error("发送json串异常", var8);
            throw new CIServiceException(var8);
        }
    }

    public String marketingStrategy() {
        String fromPageFlag = this.getRequest().getParameter("fromPageFlag");
        String COC_CUS_MARKETING_MATCH_LINK = "";
        if("1".equals(fromPageFlag)) {
            COC_CUS_MARKETING_MATCH_LINK = "COC_CUS_MARKETING_MATCH_LINK_INDEX";
        } else {
            COC_CUS_MARKETING_MATCH_LINK = "COC_CUS_MARKETING_MATCH_LINK";
        }

        String customGroupId = null;
        String dataDate = null;
        String listTableName = null;
        String productsStr = null;
        if(this.customGroup != null) {
            customGroupId = this.customGroup.getCustomGroupId();
            dataDate = this.customGroup.getDataDate();
            listTableName = this.customGroup.getListTableName();
            productsStr = this.customGroup.getProductsStr();
            this.customGroup.setReturnPage("marketingStrategy");
            this.productMatchList = new ArrayList();
            CacheBase cache = CacheBase.getInstance();
            if(StringUtils.isBlank(dataDate)) {
                this.customGroup.setDataDate(CacheBase.getInstance().getNewLabelMonth());
            }

            this.customGroup.setProductDate(CacheBase.getInstance().getNewLabelMonth());
            if(StringUtils.isNotBlank(productsStr)) {
                String[] customGroupInfo = productsStr.split(",");
                String[] customListInfo = customGroupInfo;
                int len$ = customGroupInfo.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    String productId = customListInfo[i$];
                    CiProductInfo productInfo = cache.getEffectiveProduct(productId);
                    CustomProductMatch productMatch = new CustomProductMatch();
                    productMatch.setDataDate(dataDate);
                    productMatch.setListTableName(listTableName);
                    productMatch.setProductId(productInfo.getProductId());
                    productMatch.setProductName(productInfo.getProductName());
                    this.productMatchList.add(productMatch);
                }
            }

            if(StringUtils.isNotBlank(listTableName) && StringUtils.isNotBlank(customGroupId)) {
                this.sysProductMatchList = this.marketingStrategyService.querySysProductMatch(this.customGroup);
                CiCustomGroupInfo var15 = this.customersService.queryCiCustomGroupInfo(customGroupId);
                CiCustomListInfo var16 = this.customersManagerService.queryCiCustomListInfoById(listTableName);
                this.customGroup.setCustomGroupName(var15.getCustomGroupName());
                this.customGroup.setCustomNum(var16.getCustomNum());
            }

            CILogServiceUtil.getLogServiceInstance().log(COC_CUS_MARKETING_MATCH_LINK, this.customGroup.getCustomGroupId(), this.customGroup.getCustomGroupName(), "客户群营销策略匹配首页【客户群ID：" + this.customGroup.getCustomGroupId() + "名称：" + this.customGroup.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        }

        return "marketingStrategy";
    }

    public String customProductView() {
        if(this.customGroup != null) {
            CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(this.customGroup.getCustomGroupId());
            String customGroupId = this.customGroup.getCustomGroupId();
            String productsStr = this.customGroup.getProductsStr();
            String listTableName = this.customGroup.getListTableName();
            String dataDate = this.customGroup.getProductDate();
            Long customNum = this.customGroup.getCustomNum();
            String userId = this.getUserId();
            this.customGroup.setReturnPage("customProductView");
            if(StringUtils.isNotBlank(listTableName)) {
                if(StringUtils.isNotBlank(productsStr)) {
                    GroupCalcSqlPaser sf = new GroupCalcSqlPaser(Configure.getInstance().getProperty("CI_BACK_DBTYPE"));
                    String[] productIds = productsStr.split(",");
                    this.marketingStrategyService.saveCustomProductRel(customGroupId, productIds, userId);
                    String selectSql = null;
                    this.productMatchList = new ArrayList();
                    CacheBase cache = CacheBase.getInstance();
                    String[] arr$ = productIds;
                    int len$ = productIds.length;

                    for(int i$ = 0; i$ < len$; ++i$) {
                        String productId = arr$[i$];
                        CiProductInfo productInfo = cache.getEffectiveProduct(productId);
                        List ciLabelRuleList = this.getCiLabelRuleList(productId);
                        selectSql = this.customersManagerService.getWithColumnSqlStr(ciLabelRuleList, dataDate, "", this.getUserId(), (List)null, (Integer)null, (Integer)null);
                        CustomProductMatch productMatch = this.marketingStrategyService.getProductMatch(this.customGroup, selectSql, sf, Integer.valueOf(productId));
                        BigDecimal a = new BigDecimal(customNum.longValue());
                        BigDecimal b = (new BigDecimal(productMatch.getMatchCustomNum().intValue())).multiply(new BigDecimal(100));
                        double c = 0.0D;
                        if(a.intValue() > 0) {
                            c = b.divide(a, 2, RoundingMode.HALF_UP).doubleValue();
                        }

                        productMatch.setMatchPropotion(Double.valueOf(c));
                        if(productInfo != null) {
                            productMatch.setProductName(productInfo.getProductName());
                        }

                        this.productMatchList.add(productMatch);
                    }

                    if(this.productMatchList.size() > 0) {
                        Collections.sort(this.productMatchList, new Comparator() {
                            public int compare(CustomProductMatch o1, CustomProductMatch o2) {
                                return o2.getMatchCustomNum().compareTo(o1.getMatchCustomNum());
                            }

							@Override
							public int compare(Object o1, Object o2) {
								// TODO Auto-generated method stub
								return 0;
							}
                        });
                    }

                    if(this.productMatchList.size() > 10) {
                        this.productMatchList = this.productMatchList.subList(0, 10);
                    }
                }

                if(StringUtils.isBlank(this.customGroup.getDataDate())) {
                    SimpleDateFormat var23 = new SimpleDateFormat("yyyyMM");
                    this.customGroup.setDataDate(var23.format(new Date()));
                }

                this.sysProductMatchList = this.marketingStrategyService.querySysProductMatch(this.customGroup);
                if(this.sysProductMatchList.size() > 0) {
                    Collections.sort(this.sysProductMatchList, new Comparator() {
                        public int compare(CustomProductMatch o1, CustomProductMatch o2) {
                            return o2.getMatchCustomNum().compareTo(o1.getMatchCustomNum());
                        }

						@Override
						public int compare(Object o1, Object o2) {
							// TODO Auto-generated method stub
							return 0;
						}
                    });
                }

                if(this.sysProductMatchList.size() == 0) {
                    this.sysProductMatchList = null;
                }
            }

            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MATCH_ADD", customGroupInfo.getCustomGroupId(), customGroupInfo.getCustomGroupName(), "手动策略匹配【客户群:" + customGroupInfo.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        }

        return "customProductView";
    }

    public void isCustomProductMatch() {
        boolean success = false;
        boolean matchIng = false;
        String msg = "";
        HashMap returnMsg = new HashMap();

        try {
            if(this.customGroup != null) {
                this.newLabelMonth = CacheBase.getInstance().getNewLabelMonth();
                this.customGroup.setDataDate(this.newLabelMonth);
                if(this.marketingStrategyService.isCustomProductMatch(this.customGroup)) {
                    success = true;
                } else {
                    CiCustomGroupInfo response = this.customersService.queryCiCustomGroupInfo(this.customGroup.getCustomGroupId());
                    if(response.getProductAutoMacthFlag() != null && response.getProductAutoMacthFlag().intValue() > 1) {
                        matchIng = true;
                    }
                }
            }

            returnMsg.put("msg", msg);
            returnMsg.put("success", Boolean.valueOf(success));
            returnMsg.put("matchIng", Boolean.valueOf(matchIng));
        } catch (Exception var8) {
            this.log.error(var8);
            success = false;
            msg = "判断客户群是否进行系统匹配失败";
            returnMsg.put("msg", msg);
            returnMsg.put("success", Boolean.valueOf(success));
            returnMsg.put("matchIng", Boolean.valueOf(matchIng));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException(var7);
        }
    }

    public String initSaveMarketing() {
        CacheBase cache = CacheBase.getInstance();
        boolean precisionMarketing = true;
        String forword = "initSaveMarketing";
        if(this.customGroup != null && StringUtils.isNotBlank(this.customGroup.getProductsIdsStr())) {
            this.customProductRel = new CiCustomProductTacticsRel();
            this.productInfoList = new ArrayList();
            precisionMarketing = this.customGroup.isPrecisionMarketing();
            String[] productIds = this.customGroup.getProductsIdsStr().split(",");
            String[] arr$ = productIds;
            int len$ = productIds.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String productId = arr$[i$];
                CiProductInfo productInfo = cache.getEffectiveProduct(productId);
                String brandIds = productInfo.getBrandId();
                String brandNames = this.getBrandNamesById(brandIds);
                StringBuffer brandNameBuf = new StringBuffer();
                if(StringUtils.isNotBlank(brandNames)) {
                    String[] brandNameArr = brandNames.split(",");
                    String[] arr$1 = brandNameArr;
                    int len$1 = brandNameArr.length;

                    for(int i$1 = 0; i$1 < len$1; ++i$1) {
                        String brandName = arr$1[i$1];
                        brandNameBuf.append(brandName).append("|");
                    }

                    brandNames = brandNameBuf.substring(0, brandNameBuf.length() - 1);
                }

                productInfo.setBrandNames(brandNames);
                this.productInfoList.add(productInfo);
            }
        }

        if(precisionMarketing) {
            forword = "initSavePrecisionMarketing";
        }

        return forword;
    }

    public void saveMarketingStategy() {
        HashMap returnMap = new HashMap();
        if(this.marketTactics != null && this.customGroup != null) {
            String customGroupId = this.customGroup.getCustomGroupId();
            String productIdsStrs = this.customGroup.getProductsIdsStr();
            String listTableName = this.customGroup.getListTableName();
            String dataDate = this.customGroup.getDataDate();
            boolean isPrecisionMarketing = this.customGroup.isPrecisionMarketing();
            CacheBase cache = CacheBase.getInstance();

            String e;
            try {
                if(StringUtils.isNotBlank(productIdsStrs) && StringUtils.isNotBlank(listTableName)) {
                    String[] response = productIdsStrs.split(",");
                    e = this.getUserId();
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
                    String date = sf.format(new Date());
                    this.marketTactics.setCreateUserId(e);
                    this.marketTactics.setCreateTime(new Timestamp((new Date()).getTime()));
                    this.marketTactics.setStatus(Short.valueOf(ServiceConstants.MARKET_TACTICS_STATUS_VAL));
                    ArrayList customProductTacticsRelList = new ArrayList();
                    String tacticNames;
                    String var35;
                    if(!isPrecisionMarketing) {
                        this.marketTactics.setTacticName(URLDecoder.decode(URLDecoder.decode(this.marketTactics.getTacticName(), "UTF-8"), "UTF-8"));
                        String[] var32 = response;
                        int var33 = response.length;

                        for(int var34 = 0; var34 < var33; ++var34) {
                            var35 = var32[var34];
                            CiCustomProductTacticsRel var36 = new CiCustomProductTacticsRel();
                            var36.setCustomGroupId(customGroupId);
                            var36.setProductId(Integer.valueOf(var35));
                            var36.setUserId(e);
                            var36.setStatus(ServiceConstants.CUSTOM_PRODUCT_TACTICS_REL_VAL);
                            var36.setCreateTime(date);
                            customProductTacticsRelList.add(var36);
                        }

                        this.marketingStrategyService.saveCustomProductTacticsRel(this.marketTactics, customProductTacticsRelList);
                        tacticNames = "创建成功！";
                        returnMap.put("cmsg", tacticNames);
                        returnMap.put("success", Boolean.valueOf(true));
                        CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MARKETING_ADD", this.marketTactics.getTacticId(), this.marketTactics.getTacticName(), "全量保存营销策略【" + this.marketTactics.getTacticName() + "】成功", OperResultEnum.Success, LogLevelEnum.Medium);
                    } else {
                        tacticNames = this.marketTactics.getTacticNames();
                        tacticNames = URLDecoder.decode(URLDecoder.decode(tacticNames, "UTF-8"), "UTF-8");
                        String[] tacticNameArr = tacticNames.split(",");
                        String[] productIdsStrsArr = productIdsStrs.split(",");
                        int msg = 0;

                        while(true) {
                            if(msg >= tacticNameArr.length) {
                                var35 = "创建成功！";
                                returnMap.put("msg", var35);
                                returnMap.put("success", Boolean.valueOf(true));
                                break;
                            }

                            String productId = productIdsStrsArr[msg];
                            String tacticName = tacticNameArr[msg];
                            CiCustomGroupInfo customGroupInfo = new CiCustomGroupInfo();
                            customGroupInfo.setCustomGroupName(tacticName);
                            customGroupInfo.setDataDate(dataDate);
                            customGroupInfo.setCreateTypeId(Integer.valueOf(9));
                            customGroupInfo.setUpdateCycle(Integer.valueOf(1));
                            customGroupInfo.setStatus(Integer.valueOf(1));
                            List labelRuleList = this.getCiLabelRuleList(productId);
                            ArrayList ciCustomSourceRelList = new ArrayList();
                            CiCustomSourceRel customSourceRel = new CiCustomSourceRel();
                            customSourceRel.setElementType(Integer.valueOf(1));
                            customSourceRel.setCalcuElement(listTableName);
                            CiCustomSourceRel customSourceRelAnd = new CiCustomSourceRel();
                            customSourceRelAnd.setElementType(Integer.valueOf(0));
                            customSourceRelAnd.setCalcuElement("AND");
                            ciCustomSourceRelList.add(customSourceRel);
                            ciCustomSourceRelList.add(customSourceRelAnd);
                            CiProductInfo productInfo = cache.getEffectiveProduct(productId);
                            CiCustomGroupInfo ciCustomGroupInfo = this.customersManagerService.queryCiCustomGroupInfo(customGroupId);
                            String customGroupInfoId;
                            if(productInfo != null && ciCustomGroupInfo != null) {
                                customGroupInfoId = ciCustomGroupInfo.getCustomGroupName() + " 且 " + productInfo.getProductName();
                                customGroupInfo.setProdOptRuleShow(customGroupInfoId);
                            } else {
                                customGroupInfo.setProdOptRuleShow("");
                            }

                            customGroupInfo.setProductAutoMacthFlag(Integer.valueOf(0));
                            this.customersService.addCiCustomGroupInfo(customGroupInfo, labelRuleList, ciCustomSourceRelList, (CiTemplateInfo)null, e, false, (List)null);
                            customGroupInfoId = customGroupInfo.getCustomGroupId();
                            if(StringUtils.isNotBlank(customGroupInfoId)) {
                                CiMarketTactics marketTactic = new CiMarketTactics();
                                marketTactic.setTacticName(tacticName);
                                marketTactic.setCreateUserId(e);
                                marketTactic.setCreateTime(this.marketTactics.getCreateTime());
                                marketTactic.setStatus(Short.valueOf(ServiceConstants.MARKET_TACTICS_STATUS_VAL));
                                CiCustomProductTacticsRel customProductTacticsRel = new CiCustomProductTacticsRel();
                                customProductTacticsRel.setCustomGroupId(customGroupInfoId);
                                customProductTacticsRel.setProductId(Integer.valueOf(productId));
                                customProductTacticsRel.setUserId(e);
                                customProductTacticsRel.setStatus(ServiceConstants.CUSTOM_PRODUCT_TACTICS_REL_VAL);
                                customProductTacticsRel.setCreateTime(date);
                                customProductTacticsRelList.add(customProductTacticsRel);
                                this.marketingStrategyService.saveCustomProductTacticsRel(marketTactic, customProductTacticsRelList);
                                customProductTacticsRelList.clear();
                                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MARKETING_ADD", marketTactic.getTacticId(), marketTactic.getTacticName(), "精确保存营销策略【" + marketTactic.getTacticName() + "】成功", OperResultEnum.Success, LogLevelEnum.Medium);
                            }

                            ++msg;
                        }
                    }
                }
            } catch (Exception var30) {
                this.log.error("保存营销策略", var30);
                e = "保存营销策略";
                returnMap.put("cmsg", e + "失败");
                returnMap.put("success", Boolean.valueOf(false));
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MARKETING_ADD", "-1", "-1", "保存营销策略失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            }

            HttpServletResponse var31 = this.getResponse();

            try {
                this.sendJson(var31, JsonUtil.toJson(returnMap));
            } catch (Exception var29) {
                this.log.error("发送json串异常", var29);
                throw new CIServiceException(var29);
            }
        }

    }

    public String marketingStategyList() throws Exception {
        boolean totalPage = false;
        if(this.marketTactics == null) {
            this.marketTactics = new CiMarketTactics();
        }

        String userId = this.getUserId();
        this.marketTactics.setCreateUserId(userId);
        if(this.pager == null) {
            this.pager = new Pager(this.marketTacticsService.queryCiMarketTacticsCount(this.marketTactics));
            byte currentPage = 10;
            this.pager.setTotalPage((int)Math.ceil((double)this.pager.getTotalSize() / (double)currentPage));
        }

        int currentPage1 = Integer.valueOf(this.getRequest().getParameter("currentPage") == null?"1":this.getRequest().getParameter("currentPage")).intValue();
        int totalPage1 = this.pager.getTotalPage();
        if(currentPage1 <= totalPage1) {
            byte pageSize = 5;
            this.marketTacticsList = this.marketTacticsService.queryCiMarketTacticsList(currentPage1, pageSize, this.marketTactics);
            this.pager.setPageSize(pageSize);
            this.pager.setTotalPage(totalPage1);
        }

        return "marketingStategyList";
    }

    public void findMarketTacticsTotalNum() throws Exception {
        if(this.marketTactics == null) {
            this.marketTactics = new CiMarketTactics();
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        boolean success = false;
        String userId = this.getUserId();
        HashMap result = new HashMap();
        String msg = "";
        int totalPage = 0;
        this.marketTactics.setCreateUserId(userId);

        try {
            byte response = 5;
            long e = this.marketTacticsService.queryCiMarketTacticsCount(this.marketTactics);
            totalPage = (int)Math.ceil((double)e / (double)response);
            this.pager.setTotalPage(totalPage);
            this.pager.setPageSize(response);
            success = true;
            String name = this.marketTactics.getTacticName();
            String dateType = this.marketTactics.getDateType();
            CILogServiceUtil.getLogServiceInstance().log("COC_HOME_MARKETING_SELECT", "", "", "首页爱营销查询成功，查询条件【名称：" + (StringUtil.isEmpty(name)?"无":"营销活动名称：" + name) + "，时间类型：" + (StringUtil.isEmpty(dateType)?"无":dateType) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var12) {
            msg = "查询营销策略列表总页数错误";
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
    }

    public void createLogForMCD() throws Exception {
        boolean success = false;
        HashMap result = new HashMap();
        String msg = "";

        try {
            if(this.witchPage.equals("MCD_URL_CAMP")) {
                CILogServiceUtil.getLogServiceInstance().log("COC_MARKETING_CAMPAIGN_LINK", "", "", "营销活动：打开MCD营销活动策划页面", OperResultEnum.Success, LogLevelEnum.Medium);
            } else if(this.witchPage.equals("MCD_URL_PLAN")) {
                CILogServiceUtil.getLogServiceInstance().log("COC_MARKETING_STRATEGY_LINK", "", "", "营销策略：打开MCD营销案策划页面", OperResultEnum.Success, LogLevelEnum.Medium);
            }

            success = true;
        } catch (Exception var7) {
            msg = "记录营销策略日志错误";
            this.log.error(msg, var7);
            success = false;
        }

        result.put("success", Boolean.valueOf(success));
        if(!success) {
            result.put("msg", msg);
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void isNameExit() throws Exception {
        boolean success = false;

        try {
            if(this.marketTactics != null && StringUtils.isNotBlank(this.marketTactics.getTacticName())) {
                String response = this.marketTactics.getTacticName();
                success = this.marketTacticsService.isNameExit(response);
            }
        } catch (Exception var5) {
            String e = "判断营销策略名称是否重复失败";
            this.log.error(e, var5);
            success = false;
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(Boolean.valueOf(success)));
        } catch (Exception var4) {
            this.log.error("发送json串异常", var4);
            throw new CIServiceException(var4);
        }
    }

    private List<CiLabelRule> getCiLabelRuleList(String productId) {
        ArrayList ciLabelRuleList = new ArrayList();
        CiLabelRule rule = new CiLabelRule();
        rule.setCalcuElement(String.valueOf(productId));
        rule.setElementType(Integer.valueOf(4));
        rule.setLabelFlag(Integer.valueOf(1));
        rule.setMaxVal(Double.valueOf(1.0D));
        rule.setMinVal(Double.valueOf(0.0D));
        rule.setSortNum(Long.valueOf(0L));
        rule.setLabelFlag(Integer.valueOf(1));
        ciLabelRuleList.add(rule);
        return ciLabelRuleList;
    }

    private String getBrandNamesById(String ids) {
        String[] idsArray = new String[0];
        StringBuffer brandNames = new StringBuffer();
        CacheBase cache = CacheBase.getInstance();
        CopyOnWriteArrayList dimBrand = cache.getObjectList("DIM_BRAND");
        if(StringUtil.isNotEmpty(ids)) {
            idsArray = ids.split(",");
        }

        String[] arr$ = idsArray;
        int len$ = idsArray.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String id = arr$[i$];

            for(int i = 0; i < dimBrand.size(); ++i) {
                if(id.equals(((DimBrand)dimBrand.get(i)).getBrandId().toString())) {
                    brandNames.append(((DimBrand)dimBrand.get(i)).getBrandName().toString() + ",");
                }
            }
        }

        if(brandNames.length() > 0) {
            return brandNames.toString().substring(0, brandNames.toString().length() - 1);
        } else {
            return "";
        }
    }

    public List<CustomProductMatch> getProductMatchList() {
        return this.productMatchList;
    }

    public void setProductMatchList(List<CustomProductMatch> productMatchList) {
        this.productMatchList = productMatchList;
    }

    public List<CustomProductMatch> getSysProductMatchList() {
        return this.sysProductMatchList;
    }

    public void setSysProductMatchList(List<CustomProductMatch> sysProductMatchList) {
        this.sysProductMatchList = sysProductMatchList;
    }

    public CiCustomGroupInfo getCustomGroup() {
        return this.customGroup;
    }

    public void setCustomGroup(CiCustomGroupInfo customGroup) {
        this.customGroup = customGroup;
    }

    public CiCustomProductTacticsRel getCustomProductRel() {
        return this.customProductRel;
    }

    public void setCustomProductRel(CiCustomProductTacticsRel customProductRel) {
        this.customProductRel = customProductRel;
    }

    public List<CiProductInfo> getProductInfoList() {
        return this.productInfoList;
    }

    public void setProductInfoList(List<CiProductInfo> productInfoList) {
        this.productInfoList = productInfoList;
    }

    public CiMarketTactics getMarketTactics() {
        return this.marketTactics;
    }

    public void setMarketTactics(CiMarketTactics marketTactics) {
        this.marketTactics = marketTactics;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public List<CiMarketTactics> getMarketTacticsList() {
        return this.marketTacticsList;
    }

    public void setMarketTacticsList(List<CiMarketTactics> marketTacticsList) {
        this.marketTacticsList = marketTacticsList;
    }

    public String getWitchPage() {
        return this.witchPage;
    }

    public void setWitchPage(String witchPage) {
        this.witchPage = witchPage;
    }
}
