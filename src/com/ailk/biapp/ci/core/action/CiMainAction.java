package com.ailk.biapp.ci.core.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.core.model.ShopCartRule;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiExploreLogRecord;
import com.ailk.biapp.ci.entity.CiExploreSqlAll;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiLabelVerticalColumnRel;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.market.entity.CiMarketTask;
import com.ailk.biapp.ci.market.service.ICiMarketService;
import com.ailk.biapp.ci.model.LabelDetailInfo;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiCustomUserUseService;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.service.ICiLabelUserUseService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.IdToName;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CiMainAction extends CIBaseAction {
    private static Logger log = Logger.getLogger(CiMainAction.class);
    @Autowired
    private ICustomersManagerService customersService;
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;
    @Autowired
    private ICiLabelUserUseService ciLabelUserUseService;
    @Autowired
    private ICiCustomUserUseService ciCustomUserUseService;
    @Autowired
    private ICiMarketService ciMarketService;
    private String labelOrCustomId;
    private CiCustomGroupInfo ciCustomGroupInfo;
    private LabelDetailInfo labelDetailInfo;
    private List<CiLabelInfo> firstCategoryList = new ArrayList();
    private List<CiMarketTask> marketTaskTree;
    private Pager pager;
    private String dimName;
    private String columnId;
    private String enumCategoryId;
    private Integer elementType;
    private Integer source;
    private CiLabelRule ciLabelRule;
    private List<CiLabelRule> ciLabelRuleList;
    private List<CiCustomListInfo> ciCustomListInfoList;
    private List<CiLabelRule> shopCartRules;
    private List<CiLabelVerticalColumnRel> ciLabelVerticalColumnRelList;
    private Integer sort;
    private String operator;
    private String forwardNewPageFlag = "false";
    private String seachTypeId;
    private String seachKeyWord;
    private String marketTaskId;
    private String indexLinkType;
    private String newLabelMonthFormat;
    private String newLabelDayFormat;
    private List<ShopCartRule> shopCartRuleList;
    private HashMap<String, Object> pojo = new HashMap();

    public CiMainAction() {
    }

    public String findMarketMain() {
        this.firstCategoryList = this.ciLabelInfoService.queryCiLabelInfoList(Integer.valueOf(0), Integer.valueOf(1));

        try {
            CacheBase e = CacheBase.getInstance();
            this.marketTaskTree = e.getCiMarketTree();
            if(this.marketTaskTree != null) {
                Iterator i$ = this.marketTaskTree.iterator();

                while(i$.hasNext()) {
                    CiMarketTask ciMarketTask = (CiMarketTask)i$.next();
                    List children = ciMarketTask.getChildCiMarketTaskList();
                    if(CollectionUtils.isNotEmpty(children)) {
                        ciMarketTask.setChildrenJson(JsonUtil.toJson(children));
                    }
                }
            }
        } catch (Exception var5) {
            log.error("营销任务异常", var5);
        }

        return "marketMain";
    }

    public String openElementSettingDialog() {
        Integer labelTypeId = this.ciLabelRule.getLabelTypeId();
        String resultPage = "";
        if(this.elementType != null) {
            String customId;
            if(2 == this.elementType.intValue()) {
                if(1 == labelTypeId.intValue()) {
                    resultPage = "signLabelSettingDialog";
                } else if(4 == labelTypeId.intValue()) {
                    resultPage = "kpiLabelSettingDialog";
                } else if(5 == labelTypeId.intValue()) {
                    resultPage = "enumLabelSettingDialog";
                } else if(6 == labelTypeId.intValue()) {
                    resultPage = "dateLabelSettingDialog";
                } else if(7 == labelTypeId.intValue()) {
                    resultPage = "textLabelSettingDialog";
                } else if(8 == labelTypeId.intValue()) {
                    customId = this.ciLabelRule.getCalcuElement();
                    this.ciLabelVerticalColumnRelList = this.ciLabelInfoService.queryCiLabelVerticalColumnRelByLabelId(Integer.valueOf(customId));
                    resultPage = "vertLabelSettingDialog";
                }
            } else if(5 == this.elementType.intValue()) {
                if(!StringUtil.isNotEmpty(this.ciLabelRule.getCustomId())) {
                    log.error("用户群id为null");
                    throw new CIServiceException("用户群id为null");
                }

                customId = this.ciLabelRule.getCustomId();
                this.ciCustomListInfoList = this.customersService.querySuccessCiCustomListInfoByCGroupId(customId);
                this.ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(customId);
                if(this.ciCustomGroupInfo.getIsLabelOffline() == null || this.ciCustomGroupInfo.getIsLabelOffline().intValue() != 1) {
                    this.ciLabelRuleList = this.customersService.queryCiLabelRuleList(customId, Integer.valueOf(1));
                }

                this.setRulesOffsetDate(this.ciLabelRuleList);
                if(this.ciCustomGroupInfo.getUpdateCycle().intValue() == 3 && this.ciCustomListInfoList.size() > 8) {
                    this.ciCustomListInfoList = this.ciCustomListInfoList.subList(0, 8);
                }

                resultPage = "customerSettingDialog";
            } else if(6 == this.elementType.intValue()) {
                if(!StringUtil.isNotEmpty(this.ciLabelRule.getCustomId())) {
                    log.error("用户群id为null");
                    throw new CIServiceException("用户群id为null");
                }

                customId = this.ciLabelRule.getCustomId();
                this.ciCustomListInfoList = this.customersService.querySuccessCiCustomListInfoByCGroupId(customId);
                this.ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(customId);
                if(this.ciCustomGroupInfo.getUpdateCycle().intValue() == 3 && this.ciCustomListInfoList.size() > 8) {
                    this.ciCustomListInfoList = this.ciCustomListInfoList.subList(0, 8);
                }

                if(this.sort == null) {
                    log.error("sort为null");
                    throw new CIServiceException("sort为null");
                }

                HttpSession session = this.getSession();
                List rules = (List)session.getAttribute("sessionModelList");
                CiLabelRule custRules = null;
                Iterator i$ = rules.iterator();

                while(i$.hasNext()) {
                    CiLabelRule rule = (CiLabelRule)i$.next();
                    int sortNum = rule.getSortNum().intValue();
                    if(sortNum == this.sort.intValue()) {
                        custRules = rule;
                        break;
                    }
                }

                this.ciLabelRuleList = custRules.getChildCiLabelRuleList();
                if(this.ciCustomGroupInfo != null) {
                    this.ciCustomGroupInfo.setLabelOptRuleShow((String)null);
                }

                if(this.ciLabelRule.getIsHasList().intValue() == 1) {
                    resultPage = "customerSettingDialog";
                } else {
                    resultPage = "customerRuleSettingDialog";
                }
            }
        }

        return resultPage;
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
        if(this.ciCustomGroupInfo.getUpdateCycle().intValue() == 2 || this.ciCustomGroupInfo.getUpdateCycle().intValue() == 1) {
            fmt = "yyyyMM";
            dataDate = cache.getNewLabelMonth();
        }

        int offset = DateUtil.dateInterval(offsetDate, dataDate, fmt, this.ciCustomGroupInfo.getUpdateCycle().intValue());
        String currentStartDate = DateUtil.calculateOffsetDate(startDate, offset, "yyyy-MM-dd", this.ciCustomGroupInfo.getUpdateCycle().intValue());
        String currentEndDate = DateUtil.calculateOffsetDate(endDate, offset, "yyyy-MM-dd", this.ciCustomGroupInfo.getUpdateCycle().intValue());
        rule.setStartTime(currentStartDate);
        rule.setEndTime(currentEndDate);
    }

    public String findEnumLabelDimValue() {
        try {
            String e = this.ciLabelRule.getCalcuElement();
            if(!StringUtil.isEmpty(e)) {
                if(this.pager == null) {
                    this.pager = new Pager();
                }

                String userId = this.getUserId();
                this.pager.setTotalSize(this.ciLabelInfoService.queryLabelDimValueCount(userId, Integer.valueOf(e), this.dimName, this.enumCategoryId));
                int totalpage = (int)Math.ceil((double)this.pager.getTotalSize() / (double)this.pager.getPageSize());
                this.pager.setTotalPage(totalpage);
                this.pager = this.pager.pagerFlip();
                List list = this.ciLabelInfoService.queryLabelDimValue(Integer.valueOf(e), this.pager, this.dimName, userId, this.enumCategoryId);
                this.pager.setResult(list);
            }
        } catch (Exception var5) {
            log.error("查询枚举标签异常", var5);
        }

        return "enumLabelSettingList";
    }

    public String findCustomGroupDetail() {
        try {
            this.ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(this.labelOrCustomId);
            IUser e = PrivilegeServiceUtil.getUserById(this.ciCustomGroupInfo.getCreateUserId());
            if(e != null) {
                this.ciCustomGroupInfo.setCreateUserName(e.getUsername());
            }

            if(this.ciCustomGroupInfo.getCreateTypeId() != null) {
                this.ciCustomGroupInfo.setCreateType(IdToName.getName("DIM_CUSTOM_CREATE_TYPE", this.ciCustomGroupInfo.getCreateTypeId()));
            }

            if(this.ciCustomGroupInfo.getCreateCityId() != null) {
                this.ciCustomGroupInfo.setCreateCityName(IdToName.getName("DIM_CITY", Integer.valueOf(this.ciCustomGroupInfo.getCreateCityId())));
            }

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
                } else {
                    this.ciCustomGroupInfo.setDataDateStr(this.ciMarketService.queryCustomDataDate(this.ciCustomGroupInfo));
                }
            }
        } catch (Exception var3) {
            log.error("查询用户群详细信息异常！", var3);
        }

        return "customGroupDetailTip";
    }

    public String findLabelDetail() {
        try {
            this.labelDetailInfo = (LabelDetailInfo)this.ciLabelInfoService.queryEffectiveLabelByLabelId(Integer.valueOf(this.labelOrCustomId)).get(0);
        } catch (Exception var2) {
            log.error("查询标签详细信息异常！", var2);
        }

        return "labelDetailTip";
    }

    public String findShopCart() {
        HttpSession session = this.getSession();
        List rules = (List)session.getAttribute("sessionModelList");
        this.shopCartRules = new ArrayList();
        if(rules != null) {
            Iterator i$ = rules.iterator();

            label40:
            while(true) {
                CiLabelRule rule;
                do {
                    if(!i$.hasNext()) {
                        break label40;
                    }

                    rule = (CiLabelRule)i$.next();
                    if(rule.getElementType().intValue() == 1 || rule.getElementType().intValue() == 2 || rule.getElementType().intValue() == 5 || rule.getElementType().intValue() == 6) {
                        this.shopCartRules.add(rule);
                    }
                } while(rule.getElementType().intValue() != 5 && rule.getElementType().intValue() != 6);

                if(StringUtil.isNotEmpty(rule.getCustomId())) {
                    List list = this.customersService.querySuccessCiCustomListInfoByCGroupId(rule.getCustomId());
                    rule.setIsHasList(Integer.valueOf(list.size() > 0?1:0));
                }
            }
        }

        this.newLabelMonth = this.getNewLabelMonth();
        this.newLabelDay = this.getNewLabelDay();
        return "shopCart";
    }

    public String shopCartRule() throws Exception {
        String date = this.getNewLabelMonth();
        String newLablDay = this.getNewLabelDay();
        this.newLabelMonthFormat = DateUtil.string2StringFormat(date, "yyyyMM", "yyyy-MM");
        this.newLabelDayFormat = DateUtil.string2StringFormat(newLablDay, "yyyyMMdd", "yyyy-MM-dd");

        try {
            HttpSession e = this.getSession();
            List ciLabelRuleListTemp = (List)e.getAttribute("sessionModelList");
            this.shopCartRuleList = new ArrayList();
            this.showShopCartRuleList(ciLabelRuleListTemp, this.shopCartRuleList);
        } catch (Exception var5) {
            log.error("查询session里用户群规则异常", var5);
        }

        return "shopCartRuleTable";
    }

    private List<ShopCartRule> showShopCartRuleList(List<CiLabelRule> ciLabelRuleListTemp, List<ShopCartRule> shopCartRuleList) {
        if(ciLabelRuleListTemp != null && ciLabelRuleListTemp.size() > 0) {
            for(int i = 0; i < ciLabelRuleListTemp.size(); ++i) {
                ShopCartRule shopCartRule1;
                if(((CiLabelRule)ciLabelRuleListTemp.get(i)).getElementType().intValue() == 3) {
                    shopCartRule1 = new ShopCartRule();
                    shopCartRule1.setOperatorElement(((CiLabelRule)ciLabelRuleListTemp.get(i)).getCalcuElement());
                    shopCartRuleList.add(shopCartRule1);
                } else if(((CiLabelRule)ciLabelRuleListTemp.get(i)).getElementType().intValue() == 1) {
                    shopCartRule1 = new ShopCartRule();
                    String var13 = "";
                    if("and".equals(((CiLabelRule)ciLabelRuleListTemp.get(i)).getCalcuElement())) {
                        var13 = "且";
                    } else if("or".equals(((CiLabelRule)ciLabelRuleListTemp.get(i)).getCalcuElement())) {
                        var13 = "或";
                    } else {
                        var13 = "剔除";
                    }

                    shopCartRule1.setOperatorElement(var13);
                    CiLabelRule var14 = (CiLabelRule)ciLabelRuleListTemp.get(i + 1);
                    ShopCartRule shopCartRule11;
                    if(var14.getElementType().intValue() == 3) {
                        shopCartRule11 = new ShopCartRule();
                        shopCartRule11.setOperatorElement(var14.getCalcuElement());
                        shopCartRuleList.add(shopCartRule1);
                        shopCartRuleList.add(shopCartRule11);
                    } else {
                        StringBuffer var15;
                        if(var14.getElementType().intValue() == 2) {
                            var15 = new StringBuffer();
                            if(var14.getLabelTypeId().intValue() == 1) {
                                if(var14.getLabelFlag().intValue() != 1) {
                                    var15.append("非");
                                } else {
                                    var15.append("是");
                                }
                            } else if(var14.getLabelFlag().intValue() != 1) {
                                var15.append("(非)");
                            }

                            var15.append(this.customersService.ruleAttrVal(var14));
                            shopCartRule1.setAttrValue(var15.toString());
                            shopCartRule1.setLableOrCustomName(var14.getCustomOrLabelName());
                            if((var14.getLabelTypeId().intValue() == 4 || var14.getLabelTypeId().intValue() == 5 || var14.getLabelTypeId().intValue() == 7 || var14.getLabelTypeId().intValue() == 8 || var14.getLabelTypeId().intValue() == 6) && StringUtil.isEmpty(shopCartRule1.getAttrValue())) {
                                shopCartRule1.setIsSetValue(Boolean.valueOf(false));
                            }

                            shopCartRuleList.add(shopCartRule1);
                        } else if(var14.getElementType().intValue() == 5) {
                            var15 = new StringBuffer();
                            if(StringUtil.isNotEmpty(var14.getAttrVal())) {
                                var15.append("已选清单：");
                                var15.append(var14.getAttrVal());
                            }

                            shopCartRule1.setAttrValue(var15.toString());
                            shopCartRule1.setLableOrCustomName(var14.getCustomOrLabelName());
                            if(StringUtil.isEmpty(shopCartRule1.getAttrValue()) && StringUtil.isNotEmpty(shopCartRule1.getLableOrCustomName())) {
                                shopCartRule1.setIsSetValue(Boolean.valueOf(false));
                            }

                            shopCartRuleList.add(shopCartRule1);
                        } else if(var14.getElementType().intValue() == 6) {
                            if(StringUtil.isEmpty(shopCartRule1.getAttrValue()) && StringUtil.isNotEmpty(shopCartRule1.getLableOrCustomName())) {
                                shopCartRule1.setIsSetValue(Boolean.valueOf(false));
                            }

                            shopCartRuleList.add(shopCartRule1);
                            shopCartRule11 = new ShopCartRule();
                            shopCartRule11.setOperatorElement("(");
                            shopCartRuleList.add(shopCartRule11);
                            List list1 = var14.getChildCiLabelRuleList();
                            this.showShopCartRuleList(list1, shopCartRuleList);
                            ShopCartRule shopCartRule21 = new ShopCartRule();
                            shopCartRule21.setOperatorElement(")");
                            shopCartRuleList.add(shopCartRule21);
                        }
                    }

                    ++i;
                } else if(((CiLabelRule)ciLabelRuleListTemp.get(i)).getElementType().intValue() == 2) {
                    StringBuffer var10 = new StringBuffer();
                    if(((CiLabelRule)ciLabelRuleListTemp.get(i)).getLabelTypeId().intValue() == 1) {
                        if(((CiLabelRule)ciLabelRuleListTemp.get(i)).getLabelFlag().intValue() != 1) {
                            var10.append("非");
                        } else {
                            var10.append("是");
                        }
                    } else if(((CiLabelRule)ciLabelRuleListTemp.get(i)).getLabelFlag().intValue() != 1) {
                        var10.append("(非)");
                    }

                    var10.append(this.customersService.ruleAttrVal((CiLabelRule)ciLabelRuleListTemp.get(i)));
                    ShopCartRule var12 = new ShopCartRule();
                    var12.setAttrValue(var10.toString());
                    var12.setLableOrCustomName(((CiLabelRule)ciLabelRuleListTemp.get(i)).getCustomOrLabelName());
                    if((((CiLabelRule)ciLabelRuleListTemp.get(i)).getLabelTypeId().intValue() == 4 || ((CiLabelRule)ciLabelRuleListTemp.get(i)).getLabelTypeId().intValue() == 5 || ((CiLabelRule)ciLabelRuleListTemp.get(i)).getLabelTypeId().intValue() == 7 || ((CiLabelRule)ciLabelRuleListTemp.get(i)).getLabelTypeId().intValue() == 8 || ((CiLabelRule)ciLabelRuleListTemp.get(i)).getLabelTypeId().intValue() == 6) && StringUtil.isEmpty(var12.getAttrValue())) {
                        var12.setIsSetValue(Boolean.valueOf(false));
                    }

                    shopCartRuleList.add(var12);
                } else if(((CiLabelRule)ciLabelRuleListTemp.get(i)).getElementType().intValue() == 5) {
                    shopCartRule1 = new ShopCartRule();
                    StringBuffer list = new StringBuffer();
                    if(StringUtil.isNotEmpty(((CiLabelRule)ciLabelRuleListTemp.get(i)).getAttrVal())) {
                        list.append("已选清单：");
                        list.append(((CiLabelRule)ciLabelRuleListTemp.get(i)).getAttrVal());
                    }

                    shopCartRule1.setAttrValue(list.toString());
                    shopCartRule1.setLableOrCustomName(((CiLabelRule)ciLabelRuleListTemp.get(i)).getCustomOrLabelName());
                    if(StringUtil.isEmpty(shopCartRule1.getAttrValue())) {
                        shopCartRule1.setIsSetValue(Boolean.valueOf(false));
                    }

                    shopCartRuleList.add(shopCartRule1);
                } else if(((CiLabelRule)ciLabelRuleListTemp.get(i)).getElementType().intValue() == 6) {
                    shopCartRule1 = new ShopCartRule();
                    shopCartRule1.setOperatorElement("(");
                    shopCartRuleList.add(shopCartRule1);
                    List var11 = ((CiLabelRule)ciLabelRuleListTemp.get(i)).getChildCiLabelRuleList();
                    this.showShopCartRuleList(var11, shopCartRuleList);
                    ShopCartRule shopCartRule2 = new ShopCartRule();
                    shopCartRule2.setOperatorElement(")");
                    shopCartRuleList.add(shopCartRule2);
                }
            }
        }

        return shopCartRuleList;
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
                    CiLabelRule i = (CiLabelRule)this.ciLabelRuleList.get(shopCarItemIndex.intValue());
                    removeList.add(i);
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

                for(int var15 = 0; var15 < this.ciLabelRuleList.size(); ++var15) {
                    ((CiLabelRule)this.ciLabelRuleList.get(var15)).setSortNum(Long.valueOf((long)var15 + 1L));
                }

                if(numValue == 0) {
                    this.ciLabelRuleList = null;
                }

                response.setAttribute("sessionModelList", this.ciLabelRuleList);
                response.setAttribute("calcElementNum", String.valueOf(numValue));
            } else {
                success = false;
                msg = "标签用户群模板sort不存在,移出购物车失败!";
            }
        } catch (Exception var13) {
            msg = "移出购物车失败!";
            log.error(msg, var13);
        }

        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);
        result.put("numValue", Integer.valueOf(numValue));
        HttpServletResponse var14 = this.getResponse();

        try {
            this.sendJson(var14, JsonUtil.toJson(result));
        } catch (Exception var12) {
            log.error("发送json串异常", var12);
            throw new CIServiceException(var12);
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

    public void editShopCartOperator() {
        try {
            HttpSession e = this.getSession();
            List rules = (List)e.getAttribute("sessionModelList");
            CiLabelRule rule = (CiLabelRule)rules.get(this.sort.intValue() - 1);
            rule.setCalcuElement(this.operator.toLowerCase());
        } catch (Exception var4) {
            log.error("切换运算符同步session异常！", var4);
        }

    }

    public void updateSession() {
        try {
            HttpSession e = this.getSession();
            List rules = (List)e.getAttribute("sessionModelList");
            ArrayList updateRules = new ArrayList();
            Iterator i$ = rules.iterator();

            while(i$.hasNext()) {
                CiLabelRule rule = (CiLabelRule)i$.next();
                int sortNum = rule.getSortNum().intValue();
                if(sortNum == this.sort.intValue()) {
                    updateRules.addAll(this.ciLabelRuleList);
                } else {
                    updateRules.add(rule);
                }
            }

            e.setAttribute("sessionModelList", updateRules);
            this.findLabelOrCustomNum(updateRules);
        } catch (Exception var7) {
            log.error("设置属性更新session异常", var7);
        }

    }

    public void saveShopSession() {
        HashMap result = new HashMap();
        boolean success = false;
        String msg = "失败";
        int numValue = 0;

        try {
            HttpSession response = this.getSession();
            CacheBase e = CacheBase.getInstance();
            String isEditCustomFlag = "";
            if(StringUtil.isEmpty((String)this.pojo.get("isEditCustomFlag"))) {
                isEditCustomFlag = "0";
            } else {
                isEditCustomFlag = this.pojo.get("isEditCustomFlag").toString();
            }

            CiCustomGroupInfo rules;
            String calculationsId;
            String defaultOp;
            String typeId;
            if(StringUtil.isNotEmpty((String)this.pojo.get("calculationsId"))) {
                if(!ServiceConstants.CUSTOM_GROUP_INFO_CALCULATIONS_TYPEID.equals(this.pojo.get("typeId").toString()) && this.pojo.get("typeId").toString() != ServiceConstants.CUSTOM_GROUP_INFO_CALCULATIONS_TYPEID) {
                    success = true;
                } else {
                    success = true;
                    rules = this.customersService.queryCiCustomGroupInfo((String)this.pojo.get("calculationsId"));
                    calculationsId = Configure.getInstance().getProperty("CENTER_CITYID");
                    String sort = rules.getCreateUserId();
                    String cityId = PrivilegeServiceUtil.getCityIdFromSession();
                    defaultOp = this.getUserId();
                    typeId = rules.getCreateCityId();
                    Integer rule = rules.getDataStatus();
                    if(rules.getUpdateCycle().intValue() == 4 && rules.getIsLabelOffline() != null && rules.getIsLabelOffline().intValue() == 1) {
                        success = false;
                        msg = "抱歉，该用户群无规则、无清单可用，不能添加到收纳篮！";
                    } else {
                        if(rules.getIsPrivate().intValue() == 0) {
                            if(!typeId.equals(calculationsId) && !typeId.equals(cityId) && rules.getIsContainLocalList().intValue() != 0) {
                                success = false;
                                msg = "抱歉，您无权限添加到收纳篮！";
                            } else if((rules.getCreateTypeId().intValue() != 7 || rule.intValue() == 3) && (rules.getCreateTypeId().intValue() != 12 || rule.intValue() == 3)) {
                                if(rule.intValue() == 0 && rules.getCustomNum() == null) {
                                    success = false;
                                    msg = "抱歉，该用户群无规则、无清单可用，不能添加到收纳篮！";
                                }
                            } else {
                                success = false;
                                msg = "抱歉，该用户群无规则、无清单可用，不能添加到收纳篮！";
                            }
                        }

                        if(rules.getIsPrivate().intValue() == 1) {
                            if(!defaultOp.equals(sort)) {
                                success = false;
                                msg = "抱歉，您无权限添加到收纳篮！";
                            } else if((rules.getCreateTypeId().intValue() != 7 || rule.intValue() == 3) && (rules.getCreateTypeId().intValue() != 12 || rule.intValue() == 3)) {
                                if(rule.intValue() == 0 && rules.getCustomNum() == null) {
                                    success = false;
                                    msg = "抱歉，该用户群无规则、无清单可用，不能添加到收纳篮！";
                                }
                            } else {
                                success = false;
                                msg = "抱歉，该用户群无规则、无清单可用，不能添加到收纳篮！";
                            }
                        }
                    }
                }
            }

            if(success) {
                rules = null;
                calculationsId = this.pojo.get("calculationsId").toString();
                msg = "加入购物车成功";
                if(StringUtil.isNotEmpty((String)this.pojo.get("typeId"))) {
                    if(ServiceConstants.EDIT_CUSTOM_FLAG.equals(isEditCustomFlag) || isEditCustomFlag == ServiceConstants.EDIT_CUSTOM_FLAG) {
                        response.removeAttribute("sessionModelList");
                    }

                    Object var22 = (List)response.getAttribute("sessionModelList");
                    if(null == var22 || ((List)var22).size() <= 0) {
                        var22 = new ArrayList();
                    }

                    long var23 = 0L;
                    defaultOp = "and";
                    if(StringUtil.isNotEmpty(this.pojo.get("defaultOp"))) {
                        defaultOp = this.pojo.get("defaultOp").toString();
                    }

                    if(((List)var22).size() > 0) {
                        var23 = ((CiLabelRule)((List)var22).get(((List)var22).size() - 1)).getSortNum().longValue();
                        ++var23;
                        ((List)var22).add(this.generateRule(defaultOp, 1, var23));
                    }

                    ++var23;
                    typeId = this.pojo.get("typeId").toString();
                    CiLabelRule var24 = new CiLabelRule();
                    if(ServiceConstants.LABEL_INFO_CALCULATIONS_TYPEID.equals(typeId) || typeId == ServiceConstants.LABEL_INFO_CALCULATIONS_TYPEID) {
                        CiLabelInfo ciCustomGroupInfo = e.getEffectiveLabel(calculationsId);
                        if(ciCustomGroupInfo != null) {
                            Date dataCycle = ciCustomGroupInfo.getEffecTime();
                            int isTemplate = ciCustomGroupInfo.getUpdateCycle().intValue();
                            String effectDate;
                            if(isTemplate == 1) {
                                effectDate = (new SimpleDateFormat("yyyyMMdd")).format(dataCycle);
                                var24.setEffectDate(DateUtil.getOffsetDateByDate(effectDate, -1, 1));
                            } else if(isTemplate == 2) {
                                effectDate = (new SimpleDateFormat("yyyyMM")).format(dataCycle);
                                var24.setEffectDate(DateUtil.getOffsetDateByDate(effectDate, -1, 0));
                            }

                            var24.setCalcuElement(String.valueOf(ciCustomGroupInfo.getLabelId()));
                            var24.setCustomOrLabelName(ciCustomGroupInfo.getLabelName());
                            var24.setMaxVal(new Double(1.0D));
                            var24.setMinVal(new Double(0.0D));
                            var24.setLabelFlag(Integer.valueOf(1));
                            var24.setSortNum(Long.valueOf(var23));
                            var24.setLabelTypeId(ciCustomGroupInfo.getLabelTypeId());
                            var24.setElementType(Integer.valueOf(2));
                            var24.setUnit(ciCustomGroupInfo.getCiLabelExtInfo().getCiMdaSysTableColumn().getUnit());
                            var24.setDataDate(ciCustomGroupInfo.getDataDate());
                            var24.setUpdateCycle(ciCustomGroupInfo.getUpdateCycle());
                            ((List)var22).add(var24);
                            this.ciLabelUserUseService.addLabelUserUseLog(String.valueOf(ciCustomGroupInfo.getLabelId()), 2);
                        }
                    }

                    if(ServiceConstants.CUSTOM_GROUP_INFO_CALCULATIONS_TYPEID.equals(typeId) || typeId == ServiceConstants.CUSTOM_GROUP_INFO_CALCULATIONS_TYPEID) {
                        CiCustomGroupInfo var25 = this.customersService.queryCiCustomGroupInfo(calculationsId);
                        var24.setCustomCreateTypeId(var25.getCreateTypeId());
                        var24.setCustomId(var25.getCustomGroupId());
                        var24.setCustomOrLabelName(var25.getCustomGroupName());
                        var24.setSortNum(Long.valueOf(var23));
                        var24.setElementType(Integer.valueOf(5));
                        var24.setCustomStatus(var25.getStatus());
                        int var26 = this.customersService.getCustomGroupInfoDataCycle(var25.getCreateTypeId(), var25.getUpdateCycle(), var25.getDataDate()).intValue();
                        if(var26 != -1) {
                            var24.setLabelFlag(Integer.valueOf(var26));
                        }

                        List var27;
                        if((var25.getCreateTypeId().intValue() == 1 || var25.getCreateTypeId().intValue() == 4) && (var25.getIsLabelOffline() == null || var25.getIsLabelOffline().intValue() != 1)) {
                            var27 = this.customersService.queryCiLabelRuleList(var25.getCustomGroupId(), Integer.valueOf(1));
                            var24.setChildCiLabelRuleList(var27);
                            var24.setElementType(Integer.valueOf(6));
                        } else {
                            var27 = this.customersService.queryCiCustomListInfoByCGroupId(var25.getCustomGroupId());
                            if(var27 != null && var27.size() > 0) {
                                var24.setCalcuElement(((CiCustomListInfo)var27.get(0)).getListTableName());
                                var24.setAttrVal(((CiCustomListInfo)var27.get(0)).getDataDate());
                            }
                        }

                        ((List)var22).add(var24);
                        Integer var28 = Integer.valueOf(0);
                        if(var25.getUpdateCycle().intValue() == 4) {
                            var28 = Integer.valueOf(1);
                        }

                        this.ciCustomUserUseService.addCustomUserUseLog(var25.getCustomGroupId(), 2, var28.intValue());
                    }

                    response.setAttribute("sessionModelList", var22);
                    numValue = this.findLabelOrCustomNum((List)var22).intValue();
                    response.setAttribute("calcElementNum", String.valueOf(numValue));
                    this.editSessionRules((List)var22);
                } else {
                    success = false;
                    msg = "标签用户群模板类型未添加,加入购物车失败!";
                }
            }
        } catch (Exception var20) {
            success = false;
            msg = "加入购物车失败!";
            log.error(msg, var20);
        }

        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);
        result.put("numValue", Integer.valueOf(numValue));
        HttpServletResponse var21 = this.getResponse();

        try {
            this.sendJson(var21, JsonUtil.toJson(result));
        } catch (Exception var19) {
            log.error("发送json串异常", var19);
            throw new CIServiceException(var19);
        }
    }

    public void validateSql() {
        String jsonObj = null;
        HashMap result = new HashMap();
        String validateSql = "";
        boolean flag = false;
        this.ciLabelRuleList = this.resolveCustomRule();
        String e;
        if(this.haveCustomOrVerticalLabel(this.ciLabelRuleList)) {
            try {
                Set response = this.findInvalidCustomListInfoIds(this.ciLabelRuleList);
                if(response != null && response.size() > 0) {
                    result.put("msg", "您选择的用户群已失效！");
                    result.put("ids", response);
                    flag = false;
                } else {
                    validateSql = this.customersService.getSelectSqlByCustomersRels((List)null, this.ciLabelRuleList, this.ciCustomGroupInfo.getMonthLabelDate(), this.ciCustomGroupInfo.getDayLabelDate(), this.ciCustomGroupInfo.getDataDate(), this.getUserId(), (Integer)null, (Integer)null, true);
                    StringBuffer e1 = new StringBuffer();
                    e1.append("select 1 from (").append(validateSql).append(") where 1=2");
                    validateSql = e1.toString();
                    log.info("COC_SQL >>>>>>> " + validateSql);
                    flag = true;
                }
            } catch (Exception var10) {
                e = "SQL拼接异常";
                log.error(e, var10);
                result.put("msg", e + "，请联系管理员！");
                flag = false;
            }
        } else {
            try {
                List response1 = this.customersService.getNewCiLabelRuleList(this.ciLabelRuleList);
                validateSql = this.customersService.getValidateSqlStr(response1, this.ciCustomGroupInfo.getMonthLabelDate(), this.ciCustomGroupInfo.getDayLabelDate(), this.getUserId());
                log.debug("validate SQL : " + validateSql);
                flag = true;
            } catch (Exception var9) {
                e = "SQL拼接异常";
                log.error(e, var9);
                result.put("msg", e + "，请联系管理员！");
                flag = false;
            }
        }

        if(flag) {
            flag = this.customersService.queryValidateSql(validateSql);
            if(!flag) {
                result.put("msg", "您拖动的条件验证未通过，请检查所选条件是否正确！");
            } else {
                result.put("msg", "ok");
            }
        }

        result.put("success", Boolean.valueOf(flag));

        try {
            jsonObj = JsonUtil.toJson(result);
        } catch (IOException var8) {
            log.error("json转换异常", var8);
            throw new CIServiceException("json转换异常", var8);
        }

        HttpServletResponse response2 = this.getResponse();

        try {
            this.sendJson(response2, jsonObj);
        } catch (Exception var7) {
            log.error("发送json串异常", var7);
            throw new CIServiceException("发送json串异常");
        }
    }

    private boolean haveCustomOrVerticalLabel(List<CiLabelRule> rules) {
        boolean result = false;
        if(rules != null) {
            Iterator i$ = rules.iterator();

            while(i$.hasNext()) {
                CiLabelRule rule = (CiLabelRule)i$.next();
                if(rule.getElementType().intValue() == 6 || rule.getElementType().intValue() == 5) {
                    result = true;
                    break;
                }

                if(rule.getElementType().intValue() == 2 && rule.getLabelTypeId().intValue() == 8) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    public void explore() throws Exception {
        HashMap result = new HashMap();
        this.ciLabelRuleList = this.resolveCustomRule();
        String LOG_DATA_EXPLORE = "COC_INDEX_LABEL_DATA_EXPLORE";
        String msg;
        String countSql;
        if(this.haveCustomOrVerticalLabel(this.ciLabelRuleList)) {
            msg = null;

            try {
                Date num = new Date();
                countSql = this.customersService.getSelectSqlByCustomersRels((List)null, this.ciLabelRuleList, this.ciCustomGroupInfo.getMonthLabelDate(), this.ciCustomGroupInfo.getDayLabelDate(), this.ciCustomGroupInfo.getDataDate(), this.getUserId(), (Integer)null, (Integer)null, false);
                StringBuffer success = new StringBuffer();
                success.append("select count(1) from (").append(countSql).append(") abc");
                msg = success.toString();
                log.info("COC_SQL >>>>>>> " + msg);
                int e = this.customersService.queryCount(msg);
                this.logExploreRecord(msg, num, this.ciLabelRuleList);
                result.put("success", Boolean.valueOf(true));
                result.put("msg", Integer.valueOf(e));
                CILogServiceUtil.getLogServiceInstance().log(LOG_DATA_EXPLORE, "", "数据探索", "数据探索成功，用户数为：" + e, OperResultEnum.Success, LogLevelEnum.Medium);
            } catch (Exception var12) {
                result.put("success", Boolean.valueOf(false));
                result.put("msg", var12.getMessage());
                CILogServiceUtil.getLogServiceInstance().log(LOG_DATA_EXPLORE, "-1", "数据探索", "数据探索失败，执行SQL : " + msg, OperResultEnum.Failure, LogLevelEnum.Medium);
                log.error("数据探索出错 in countOfCustomerCalc");
            }

            try {
                HttpServletResponse num1 = this.getResponse();
                this.sendJson(num1, JsonUtil.toJson(result));
            } catch (Exception var11) {
                log.error("发送json串异常", var11);
                throw new CIServiceException(var11);
            }
        } else {
            msg = "";
            boolean num2 = false;
            countSql = null;
            boolean success1 = false;

            try {
                Date e1 = new Date();
                List newList = this.customersService.getNewCiLabelRuleList(this.ciLabelRuleList);
                countSql = this.customersService.getCountSqlStr(newList, this.ciCustomGroupInfo.getMonthLabelDate(), this.ciCustomGroupInfo.getDayLabelDate(), this.getUserId());
                log.info("count SQL : " + countSql);
                int num3 = this.customersService.queryCount(countSql);
                this.logExploreRecord(countSql, e1, this.ciLabelRuleList);
                success1 = true;
                result.put("success", Boolean.valueOf(success1));
                result.put("msg", Integer.valueOf(num3));
                CILogServiceUtil.getLogServiceInstance().log(LOG_DATA_EXPLORE, "", "数据探索", "数据探索成功，用户数为：" + num3, OperResultEnum.Success, LogLevelEnum.Medium);
            } catch (Exception var10) {
                msg = "查询用户数量失败";
                log.error(msg + "EXCUTE SQL IS : " + countSql, var10);
                success1 = false;
                result.put("success", Boolean.valueOf(success1));
                result.put("msg", msg);
                CILogServiceUtil.getLogServiceInstance().log(LOG_DATA_EXPLORE, "-1", "数据探索", "数据探索失败，执行SQL : " + countSql, OperResultEnum.Failure, LogLevelEnum.Medium);
            }

            try {
                HttpServletResponse e2 = this.getResponse();
                this.sendJson(e2, JsonUtil.toJson(result));
            } catch (Exception var9) {
                log.error("发送json串异常", var9);
                throw new CIServiceException(var9);
            }
        }

    }

    private List<CiLabelRule> resolveCustomRule() {
        HttpSession session = super.getSession();
        List rules = (List)session.getAttribute("sessionModelList");
        ArrayList newRules = new ArrayList();
        if(rules != null) {
            int i;
            for(i = 0; i < rules.size(); ++i) {
                CiLabelRule rule = (CiLabelRule)rules.get(i);
                CiLabelRule clone = null;

                try {
                    clone = rule.clone();
                } catch (CloneNotSupportedException var9) {
                    var9.printStackTrace();
                }

                int elementType = rule.getElementType().intValue();
                if(elementType == 6) {
                    List children = rule.getChildCiLabelRuleList();
                    newRules.add(this.generateRule("(", 3, 0L));
                    newRules.addAll(children);
                    newRules.add(this.generateRule(")", 3, 0L));
                } else {
                    newRules.add(clone);
                }
            }

            for(i = 0; i < newRules.size(); ++i) {
                ((CiLabelRule)newRules.get(i)).setSortNum(Long.valueOf((long)(i + 1)));
            }
        }

        return newRules;
    }

    private Set<String> findInvalidCustomListInfoIds(List<CiLabelRule> ciLabelRuleList) {
        String userId = this.getUserId();
        HashSet ids = new HashSet();

        try {
            Iterator e = ciLabelRuleList.iterator();

            while(e.hasNext()) {
                CiLabelRule r = (CiLabelRule)e.next();
                int elementType = r.getElementType().intValue();
                if(elementType == 5) {
                    String listInfoId = r.getCalcuElement();
                    CiCustomGroupInfo ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfoByListInfoId(listInfoId);
                    if(1 == ciCustomGroupInfo.getStatus().intValue()) {
                        if(1 == ciCustomGroupInfo.getIsPrivate().intValue() && !userId.equals(ciCustomGroupInfo.getCreateUserId())) {
                            ids.add(listInfoId);
                        }
                    } else {
                        ids.add(listInfoId);
                    }
                }
            }
        } catch (Exception var9) {
            log.error("查询无效的用户群清单id集合错误", var9);
        }

        return ids;
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

    private CiLabelRule generateRule(String element, int type, long sortNum) {
        CiLabelRule result = new CiLabelRule();
        result.setCalcuElement(element);
        result.setElementType(Integer.valueOf(type));
        result.setSortNum(Long.valueOf(sortNum));
        return result;
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

    private void logExploreRecord(String sql, Date startDate, List<CiLabelRule> ciLabelRuleList) {
        log.debug("exe sql:" + sql);
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
        logRecord.setUserId(this.getUserId());

        try {
            this.customersService.saveCiExploreLogRecord(logRecord);
        } catch (Exception var17) {
            var17.printStackTrace();
        }

        List ciListExeSqlAllList1 = this.generateCiExploreSqlAllList(logRecord, sql);
        this.customersService.saveCiExploreSqlAllList(ciListExeSqlAllList1);
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

    public List<CiLabelInfo> getFirstCategoryList() {
        return this.firstCategoryList;
    }

    public void setFirstCategoryList(List<CiLabelInfo> firstCategoryList) {
        this.firstCategoryList = firstCategoryList;
    }

    public String getLabelOrCustomId() {
        return this.labelOrCustomId;
    }

    public void setLabelOrCustomId(String labelOrCustomId) {
        this.labelOrCustomId = labelOrCustomId;
    }

    public LabelDetailInfo getLabelDetailInfo() {
        return this.labelDetailInfo;
    }

    public void setLabelDetailInfo(LabelDetailInfo labelDetailInfo) {
        this.labelDetailInfo = labelDetailInfo;
    }

    public CiCustomGroupInfo getCiCustomGroupInfo() {
        return this.ciCustomGroupInfo;
    }

    public void setCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo) {
        this.ciCustomGroupInfo = ciCustomGroupInfo;
    }

    public List<CiMarketTask> getMarketTaskTree() {
        return this.marketTaskTree;
    }

    public void setMarketTaskTree(List<CiMarketTask> marketTaskTree) {
        this.marketTaskTree = marketTaskTree;
    }

    public List<CiLabelRule> getShopCartRules() {
        return this.shopCartRules;
    }

    public void setShopCartRules(List<CiLabelRule> shopCartRules) {
        this.shopCartRules = shopCartRules;
    }

    public Integer getSort() {
        return this.sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getElementType() {
        return this.elementType;
    }

    public void setElementType(Integer elementType) {
        this.elementType = elementType;
    }

    public Integer getSource() {
        return this.source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public String getDimName() {
        return this.dimName;
    }

    public void setDimName(String dimName) {
        this.dimName = dimName;
    }

    public String getColumnId() {
        return this.columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getEnumCategoryId() {
        return this.enumCategoryId;
    }

    public void setEnumCategoryId(String enumCategoryId) {
        this.enumCategoryId = enumCategoryId;
    }

    public CiLabelRule getCiLabelRule() {
        return this.ciLabelRule;
    }

    public void setCiLabelRule(CiLabelRule ciLabelRule) {
        this.ciLabelRule = ciLabelRule;
    }

    public List<CiLabelRule> getCiLabelRuleList() {
        return this.ciLabelRuleList;
    }

    public void setCiLabelRuleList(List<CiLabelRule> ciLabelRuleList) {
        this.ciLabelRuleList = ciLabelRuleList;
    }

    public List<CiCustomListInfo> getCiCustomListInfoList() {
        return this.ciCustomListInfoList;
    }

    public void setCiCustomListInfoList(List<CiCustomListInfo> ciCustomListInfoList) {
        this.ciCustomListInfoList = ciCustomListInfoList;
    }

    public List<CiLabelVerticalColumnRel> getCiLabelVerticalColumnRelList() {
        return this.ciLabelVerticalColumnRelList;
    }

    public void setCiLabelVerticalColumnRelList(List<CiLabelVerticalColumnRel> ciLabelVerticalColumnRelList) {
        this.ciLabelVerticalColumnRelList = ciLabelVerticalColumnRelList;
    }

    public String getForwardNewPageFlag() {
        return this.forwardNewPageFlag;
    }

    public void setForwardNewPageFlag(String forwardNewPageFlag) {
        this.forwardNewPageFlag = forwardNewPageFlag;
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

    public HashMap<String, Object> getPojo() {
        return this.pojo;
    }

    public void setPojo(HashMap<String, Object> pojo) {
        this.pojo = pojo;
    }

    public String getMarketTaskId() {
        return this.marketTaskId;
    }

    public void setMarketTaskId(String marketTaskId) {
        this.marketTaskId = marketTaskId;
    }

    public String getIndexLinkType() {
        return this.indexLinkType;
    }

    public void setIndexLinkType(String indexLinkType) {
        this.indexLinkType = indexLinkType;
    }

    public String getNewLabelMonthFormat() {
        return this.newLabelMonthFormat;
    }

    public void setNewLabelMonthFormat(String newLabelMonthFormat) {
        this.newLabelMonthFormat = newLabelMonthFormat;
    }

    public String getNewLabelDayFormat() {
        return this.newLabelDayFormat;
    }

    public void setNewLabelDayFormat(String newLabelDayFormat) {
        this.newLabelDayFormat = newLabelDayFormat;
    }

    public List<ShopCartRule> getShopCartRuleList() {
        return this.shopCartRuleList;
    }

    public void setShopCartRuleList(List<ShopCartRule> shopCartRuleList) {
        this.shopCartRuleList = shopCartRuleList;
    }
}
