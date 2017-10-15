package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.CiCustomFileRel;
import com.ailk.biapp.ci.entity.CiCustomGroupForm;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.service.ICiCustomFileRelService;
import com.ailk.biapp.ci.service.ICustomersAnalysisService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CustomersAnalysisAction extends CIBaseAction {
    private Logger log = Logger.getLogger(CustomersAnalysisAction.class);
    private String tab;
    private String customMatch;
    private String dataDate;
    private String tabName;
    private String analysisType;
    private String ciLableId;
    private CiCustomGroupInfo customGroup;
    private CiCustomFileRel customFileRel;
    private List<String> ciLableIds;
    @Autowired
    private ICustomersManagerService customersService;
    @Autowired
    private ICiCustomFileRelService customFileRelService;
    @Autowired
    private ICustomersAnalysisService customersAnalysisService;

    public CustomersAnalysisAction() {
    }

    public String customersAnalysisIndex() throws Exception {
        CILogServiceUtil.getLogServiceInstance().log("COC_CUS_ANALYSIS_MAIN_LINK", "-1", "客户群分析首页", "客户群分析首页链接跳转", OperResultEnum.Success, LogLevelEnum.Normal);
        return "customersAnalysisIndex";
    }

    public String customersAnalysis() throws Exception {
        String customGroupId = this.customGroup.getCustomGroupId();
        if(StringUtil.isNotEmpty(customGroupId)) {
            this.customGroup = this.customersService.queryCiCustomGroupInfo(customGroupId);
            CacheBase cache = CacheBase.getInstance();
            String createType = cache.getNameByKey("DIM_CUSTOM_CREATE_TYPE", this.customGroup.getCreateTypeId());
            this.customGroup.setCreateType(createType);
            String iuser;
            String list;
            String year;
            String month;
            if(this.customGroup.getUpdateCycle().intValue() == 3) {
                this.customGroup.setUpdateCycleStr("日周期");
                iuser = DateUtil.date2String(new Date(), "yyyyMMdd");
                list = DateUtil.getFrontDay(2, iuser);
                year = list.substring(0, 4);
                month = list.substring(4, 6);
                String day = list.substring(6);
                this.customGroup.setDataDate(list);
                this.customGroup.setYear(year);
                this.customGroup.setMonth(month);
                this.customGroup.setDay(day);
            } else if(this.customGroup.getUpdateCycle().intValue() == 2) {
                this.customGroup.setUpdateCycleStr("月周期");
                iuser = this.customGroup.getDataDate();
                list = iuser.substring(0, 4);
                year = iuser.substring(4, 6);
                CiCustomListInfo month1 = this.customersService.queryCiCustomListInfoByCGroupIdAndDataDate(customGroupId, iuser);
                if(month1 != null) {
                    this.customGroup.setListTableName(month1.getListTableName());
                }

                this.customGroup.setDataDate(iuser);
                this.customGroup.setYear(list);
                this.customGroup.setMonth(year);
            } else {
                this.customGroup.setUpdateCycleStr("一次性");
                iuser = this.customGroup.getDataDate();
                List list1 = this.customersService.queryCiCustomListInfoByCGroupId(customGroupId);
                if(list1 != null && list1.size() > 0) {
                    CiCustomListInfo year1 = (CiCustomListInfo)list1.get(0);
                    this.customGroup.setListTableName(year1.getListTableName());
                }

                year = iuser.substring(0, 4);
                month = iuser.substring(4, 6);
                this.customGroup.setDataDate(iuser);
                this.customGroup.setYear(year);
                this.customGroup.setMonth(month);
            }

            if(this.customGroup.getDataStatus() != null) {
                this.customGroup.setDataStatusStr(cache.getNameByKey("DIM_CUSTOM_DATA_STATUS", this.customGroup.getDataStatus()));
            }

            if(this.customGroup.getCustomNum() == null) {
                this.customGroup.setCustomNum(Long.valueOf(0L));
            }

            this.customFileRel = this.customFileRelService.queryCustomFileByCustomGroupId(customGroupId);
            IUser iuser1 = PrivilegeServiceUtil.getUserById(this.customGroup.getCreateUserId());
            this.customGroup.setCreateUserName(iuser1.getUsername());
            CILogServiceUtil.getLogServiceInstance().log("COC_CUS_ANALYSIS_LINK", this.customGroup.getCustomGroupId(), this.customGroup.getCustomGroupName(), "客户群分析页", OperResultEnum.Success, LogLevelEnum.Normal);
        }

        return "customersAnalysis";
    }

    public String customersAssociationAnalysis() {
        CiLabelRule labelRule = new CiLabelRule();
        labelRule.setCalcuElement(this.ciLableId);
        labelRule.setMaxVal(Double.valueOf(1.0D));
        labelRule.setMinVal(Double.valueOf(0.0D));
        ArrayList ciLabelRuleList = new ArrayList();
        ciLabelRuleList.add(labelRule);
        String lastDate = this.getNewLabelDay();
        String fromSql = this.customersService.getWithColumnSqlStr(ciLabelRuleList, this.dataDate, lastDate, this.getUserId(), (List)null, (Integer)null, (Integer)null);
        int analysisResult = this.customersAnalysisService.queryAssociationAnalysis(fromSql, this.dataDate);
        this.log.debug("查询客户群与标签的关联分析结果：" + analysisResult);
        return null;
    }

    public String customersComponentAnalysis() {
        String customGroupId = this.customGroup.getCreateUserId();
        if(StringUtil.isNotEmpty(customGroupId)) {
            CiCustomGroupForm ciCustomGroupForm = new CiCustomGroupForm();
            ciCustomGroupForm.setCustomGroupId(customGroupId);
            ciCustomGroupForm.setDataDate(this.dataDate);
            List brandList = this.customersAnalysisService.queryBrandFormChartData(ciCustomGroupForm);
            List subBrandList = this.customersAnalysisService.querySubBrandFormChartData(ciCustomGroupForm);
            List cityList = this.customersAnalysisService.queryCityFormChartData(ciCustomGroupForm);
            List vipList = this.customersAnalysisService.queryVipFormChartData(ciCustomGroupForm);
            this.log.debug("客户群品牌构成分析 数据" + brandList);
            this.log.debug("客户群子品牌构成分析 数据" + subBrandList);
            this.log.debug("客户群地域构成分析 数据" + cityList);
            this.log.debug("客户群VIP等级构成分析 数据" + vipList);
        }

        return null;
    }

    public String customerAnalysisMain() {
        String COC_CUS_LINK = "";
        String descStr = "";
        if(this.customGroup != null) {
            CiCustomGroupInfo customGroupInfo = null;
            String customGroupId = this.customGroup.getCustomGroupId();
            if(StringUtil.isNotEmpty(customGroupId)) {
                customGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
                this.customGroup.setCustomGroupName(customGroupInfo.getCustomGroupName());
                this.customGroup.setDataDate(this.dataDate);
            }
        } else {
            CILogServiceUtil.getLogServiceInstance().log(COC_CUS_LINK, "", "", "转入" + descStr, OperResultEnum.Success, LogLevelEnum.Normal);
        }

        this.getRequest().setAttribute("fromPageFlag", this.getRequest().getParameter("fromPageFlag"));
        return "customerAnalysisMain";
    }

    public String getDataDate() {
        return this.dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public String getTabName() {
        return this.tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getCiLableId() {
        return this.ciLableId;
    }

    public void setCiLableId(String ciLableId) {
        this.ciLableId = ciLableId;
    }

    public List<String> getCiLableIds() {
        return this.ciLableIds;
    }

    public void setCiLableIds(List<String> ciLableIds) {
        this.ciLableIds = ciLableIds;
    }

    public String getAnalysisType() {
        return this.analysisType;
    }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }

    public CiCustomGroupInfo getCustomGroup() {
        return this.customGroup;
    }

    public void setCustomGroup(CiCustomGroupInfo customGroup) {
        this.customGroup = customGroup;
    }

    public String getTab() {
        return this.tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getCustomMatch() {
        return this.customMatch;
    }

    public void setCustomMatch(String customMatch) {
        this.customMatch = customMatch;
    }

    public CiCustomFileRel getCustomFileRel() {
        return this.customFileRel;
    }

    public void setCustomFileRel(CiCustomFileRel customFileRel) {
        this.customFileRel = customFileRel;
    }
}
