package com.ailk.biapp.ci.core.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.LabelDetailInfo;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiRankingListAction extends CIBaseAction {
    private static Logger log = Logger.getLogger(CiRankingListAction.class);
    private int rankingListType = 1;
    private Pager pager;
    private CiCustomGroupInfo ciCustomGroupInfo;
    private List<LabelDetailInfo> labelDetailList;
    private List<CiCustomGroupInfo> ciCustomGroupInfoList;
    private Integer havePushFun;
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;
    @Autowired
    private ICustomersManagerService customersService;

    public CiRankingListAction() {
    }

    public String findSysHotRankLabelIndex() {
        System.out.println("=================================================");
        return "sysHotLabelRankingIndex";
    }

    public String findSysHotRankLabel() {
        this.rankingListType = 2;
        String dataScope = this.getRequest().getParameter("dataScope");
        if(StringUtil.isEmpty(dataScope)) {
            dataScope = "all";
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        try {
            this.labelDetailList = this.ciLabelInfoService.querySysRecommendLabel(1, ServiceConstants.SHOW_RECOMMEND_LABEL_NUM, dataScope);
            this.pager.setResult(this.labelDetailList);
            return "sysHotLabelRankingList";
        } catch (Exception var4) {
            String msg = "系统热门标签排行查询错误";
            log.error(msg, var4);
            throw new CIServiceException(var4);
        }
    }

    public String findlastPublishRankLabel() {
        this.rankingListType = 1;

        try {
            String e = "date";
            this.labelDetailList = this.ciLabelInfoService.queryNewPublishLabel(1, ServiceConstants.SHOW_RECOMMEND_LABEL_NUM, e, "all");
        } catch (Exception var3) {
            String msg = "最新发布标签排行查询发生异常";
            log.error(msg, var3);
            throw new CIServiceException(var3);
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        this.pager.setResult(this.labelDetailList);
        return "lastPublishRankLabel";
    }

    public String sysHotCustomRankingListIndex() throws Exception {
        return "sysHotCustomRankingIndex";
    }

    public String sysHotCustomRankingList() throws Exception {
        this.rankingListType = 2;
        if(this.ciCustomGroupInfo == null) {
            this.ciCustomGroupInfo = new CiCustomGroupInfo();
        }

        if(StringUtils.isBlank(this.ciCustomGroupInfo.getDataDate())) {
            this.newLabelMonth = this.getNewLabelMonth();
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

        this.ciCustomGroupInfo.setIsPrivate(Integer.valueOf(0));
        if(this.pager == null) {
            this.pager = new Pager(1L);
        }

        this.pager.setPageNum(1);
        this.pager.setPageSize(ServiceConstants.SHOW_RECOMMEND_CUSTOMS_NUM);
        this.pager.setTotalPage(1);
        this.pager.setOrder("desc");
        this.pager.setOrderBy("USECOUNT_HOT");
        this.ciCustomGroupInfoList = this.customersService.queryCustomersList(this.pager, this.newLabelMonth, this.ciCustomGroupInfo);
        this.pager.setResult(this.ciCustomGroupInfoList);
        return "sysHotCustomRankingList";
    }

    public String latestCustomerList() throws Exception {
        try {
            this.rankingListType = 1;
            List e = this.customersService.queryCiSysInfo(1);
            if(e != null && e.size() > 0) {
                this.havePushFun = Integer.valueOf(1);
            } else {
                this.havePushFun = Integer.valueOf(0);
            }

            if(this.ciCustomGroupInfo == null) {
                this.ciCustomGroupInfo = new CiCustomGroupInfo();
            }

            if(StringUtils.isBlank(this.ciCustomGroupInfo.getDataDate())) {
                this.newLabelMonth = this.getNewLabelMonth();
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

            this.ciCustomGroupInfo.setIsPrivate(Integer.valueOf(0));
            if(this.pager == null) {
                this.pager = new Pager((long)ServiceConstants.SHOW_RECOMMEND_CUSTOMS_NUM);
            }

            this.pager.setPageNum(1);
            this.pager.setPageSize(ServiceConstants.SHOW_RECOMMEND_CUSTOMS_NUM);
            this.pager.setTotalPage(1);
            this.ciCustomGroupInfoList = this.customersService.queryCustomersList(this.pager, this.newLabelMonth, this.ciCustomGroupInfo);
            String customerAnalysisMenu = Configure.getInstance().getProperty("CUSTOMER_ANALYSIS");
            this.ciCustomGroupInfo.setCustomerAnalysisMenu(customerAnalysisMenu);
            log.info("Exit CustomersManagerAction.search() method");
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_SELECT", "", StringUtil.isEmpty(this.ciCustomGroupInfo.getCustomGroupName())?"":this.ciCustomGroupInfo.getCustomGroupName(), "查询最新用户群名称中含有【" + (StringUtil.isEmpty(this.ciCustomGroupInfo.getCustomGroupName())?"":this.ciCustomGroupInfo.getCustomGroupName()) + "】的记录成功", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var6) {
            log.error("用户群列表查询出错：" + var6);
            throw new CIServiceException(var6);
        }

        this.pager.setResult(this.ciCustomGroupInfoList);
        return "lastUsedCuntom";
    }

    public int getRankingListType() {
        return this.rankingListType;
    }

    public void setRankingListType(int rankingListType) {
        this.rankingListType = rankingListType;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public CiCustomGroupInfo getCiCustomGroupInfo() {
        return this.ciCustomGroupInfo;
    }

    public void setCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo) {
        this.ciCustomGroupInfo = ciCustomGroupInfo;
    }

    public List<LabelDetailInfo> getLabelDetailList() {
        return this.labelDetailList;
    }

    public void setLabelDetailList(List<LabelDetailInfo> labelDetailList) {
        this.labelDetailList = labelDetailList;
    }

    public List<CiCustomGroupInfo> getCiCustomGroupInfoList() {
        return this.ciCustomGroupInfoList;
    }

    public void setCiCustomGroupInfoList(List<CiCustomGroupInfo> ciCustomGroupInfoList) {
        this.ciCustomGroupInfoList = ciCustomGroupInfoList;
    }

    public ICiLabelInfoService getCiLabelInfoService() {
        return this.ciLabelInfoService;
    }

    public void setCiLabelInfoService(ICiLabelInfoService ciLabelInfoService) {
        this.ciLabelInfoService = ciLabelInfoService;
    }

    public ICustomersManagerService getCustomersService() {
        return this.customersService;
    }

    public void setCustomersService(ICustomersManagerService customersService) {
        this.customersService = customersService;
    }

    public Integer getHavePushFun() {
        return this.havePushFun;
    }

    public void setHavePushFun(Integer havePushFun) {
        this.havePushFun = havePushFun;
    }
}
