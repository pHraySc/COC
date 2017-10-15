package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiSysInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.LabelTrendInfo;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICustomersAnalysisService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.asiainfo.biframe.chart.AIChart;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CustomersTrendAnalysisAction extends CIBaseAction {
    private Logger log = Logger.getLogger(CustomersTrendAnalysisAction.class);
    private Pager pager;
    private Integer havePushFun;
    private String dataDate;
    private AIChart customersTrendChart;
    private List<LabelTrendInfo> trendAnalysisList;
    private CiCustomGroupInfo customGroupInfo;
    private Map<String, Object> resultMap;
    @Autowired
    private ICustomersManagerService customersService;
    @Autowired
    private ICustomersAnalysisService customersAnalysisService;

    public CustomersTrendAnalysisAction() {
    }

    public String initCustomersTrendAnalysis() {
        this.log.debug("CustomGroupInfo ID->" + this.customGroupInfo.getCustomGroupId());
        return "initCustomersTrendAnalysis";
    }

    public String customersTrendAnalysis() throws CIServiceException {
        String customGroupId = this.customGroupInfo.getCustomGroupId();
        if(StringUtil.isNotEmpty(customGroupId)) {
            CiCustomGroupInfo ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
            int cycle = ciCustomGroupInfo.getUpdateCycle().intValue();
            this.trendAnalysisList = this.customersAnalysisService.queryTrendAnalysis(customGroupId, this.dataDate, cycle);
            CILogServiceUtil.getLogServiceInstance().log("COC_CUS_NUM_TREND_CHART_VIEW", ciCustomGroupInfo.getCustomGroupId(), ciCustomGroupInfo.getCustomGroupName(), "客户群用户数趋势图查看【客户群ID：" + ciCustomGroupInfo.getCustomGroupId() + "名称：" + ciCustomGroupInfo.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        }

        return "customersTrendAnalysis";
    }

    public void customersTrendChartData() throws CIServiceException {
        String customGroupId = this.getRequest().getParameter("customGroupId");
        CiCustomGroupInfo ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
        int cycle = ciCustomGroupInfo.getUpdateCycle().intValue();
        this.trendAnalysisList = this.customersAnalysisService.queryTrendAnalysis(customGroupId, this.dataDate, cycle);
        String customersName = ciCustomGroupInfo.getCustomGroupName();
        StringBuffer strBuf = new StringBuffer("<chart palette=\'2\' caption=\'" + customersName + "客户群用户数趋势分析\' baseFontSize=\'12\' ");
        strBuf.append(" yAxisName=\'用户数︵人︶\' shownames=\'1\' showvalues=\'0\' useRoundEdges=\'1\' legendBorderAlpha=\'0\' bgColor=\'ffffff\' yAxisNameWidth=\'16\' ");
        strBuf.append(" showBorder=\'0\' baseFont=\'Microsoft YaHei\' outCnvBaseFont=\'Microsoft YaHei\' rotateYAxisName=\'0\' ");
        strBuf.append(" canvasBorderColor=\'#cccccc\' formatNumberScale=\'0\'  showShadow=\'0\' anchorBgColor=\'#6BCF39\'  anchorBorderColor=\'F8F8F8\' anchorRadius=\'5\' anchorBorderThickness=\'3\' lineThickness=\'2\' labelDisplay=\'ROTATE\' slantLabels=\'1\' lineColor=\'#6BCF39\'> ");
        if(null != this.trendAnalysisList && this.trendAnalysisList.size() > 0) {
            strBuf.append(" <set/> ");
            Iterator e = this.trendAnalysisList.iterator();

            while(e.hasNext()) {
                LabelTrendInfo info = (LabelTrendInfo)e.next();
                String dateTime = info.getDataDate().substring(0, 4) + "-" + info.getDataDate().substring(4, 6);
                String out = "<set label=\'" + dateTime + "\' value=\'" + info.getValue() + "\'/>";
                strBuf.append(out);
            }
        }

        strBuf.append(" <set/> ");
        strBuf.append(" <styles> ");
        strBuf.append(" <definition> ");
        strBuf.append(" <style name=\'CaptionFont\' type=\'font\' font=\'Microsoft YaHei\' size=\'14\' color=\'666666\' bold=\'1\' /> ");
        strBuf.append(" <style name=\'AxisNameFont\' type=\'font\' font=\'Microsoft YaHei\' size=\'12\' color=\'666666\' italic=\'0\'/> ");
        strBuf.append("  </definition> ");
        strBuf.append(" <application> ");
        strBuf.append(" <apply toObject=\'Caption\' styles=\'CaptionFont\' /> ");
        strBuf.append(" <apply toObject=\'yAxisName\' styles=\'AxisNameFont\' /> ");
        strBuf.append(" <apply toObject=\'XAxisName\' styles=\'AxisNameFont\' /> ");
        strBuf.append(" </application> ");
        strBuf.append(" </styles> ");
        strBuf.append(" </chart> ");

        try {
            super.sendXML(this.getResponse(), strBuf.toString());
        } catch (Exception var10) {
            this.log.error("发送XML异常", var10);
            throw new CIServiceException(var10);
        }
    }

    public String customersTrendChartData2() {
        String customGroupId = this.getRequest().getParameter("customGroupId");
        CiCustomGroupInfo ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
        int cycle = ciCustomGroupInfo.getUpdateCycle().intValue();
        this.trendAnalysisList = this.customersAnalysisService.queryTrendAnalysis(customGroupId, ciCustomGroupInfo.getDataDate(), cycle);
        this.customGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
        if(this.customGroupInfo.getUpdateCycle().intValue() == 3 && this.trendAnalysisList.size() > 8) {
            Collections.reverse(this.trendAnalysisList);
            this.trendAnalysisList = this.trendAnalysisList.subList(0, 8);
            Collections.reverse(this.trendAnalysisList);
        }

        String customGroupName = "";
        if(this.customGroupInfo != null) {
            customGroupName = this.customGroupInfo.getCustomGroupName();
        }

        CILogServiceUtil.getLogServiceInstance().log("COC_CUS_TREND_LIST_VIEW", customGroupId, customGroupName, "查询客户群用户数趋势数据成功【客户群ID:" + customGroupId + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "customersTrendChartData";
    }

    public String findCustomerTrendTable() {
        List ciSysInfoRightList = this.customersService.queryCiSysInfo(1);
        if(ciSysInfoRightList != null && ciSysInfoRightList.size() > 0) {
            this.havePushFun = Integer.valueOf(1);
        } else {
            this.havePushFun = Integer.valueOf(0);
        }

        Object ciSysInfoList = this.customersService.queryCiSysInfo(1);
        String province = Configure.getInstance().getProperty("PROVINCE");
        String customGroupId;
        if("zhejiang".equals(province)) {
            customGroupId = Configure.getInstance().getProperty("SYS_FLAG");
            String ciCustomGroupInfo = (String)this.getSession().getAttribute("SYS_FLAG");
            ArrayList cycle = new ArrayList();
            if(customGroupId.equals(ciCustomGroupInfo)) {
                Iterator currentPageSize = ((List)ciSysInfoList).iterator();

                while(currentPageSize.hasNext()) {
                    CiSysInfo size = (CiSysInfo)currentPageSize.next();
                    String i = size.getSysId();
                    if(i.contains(customGroupId)) {
                        cycle.add(size);
                    }
                }

                ciSysInfoList = cycle;
            }
        }

        this.getSession().setAttribute("ciSysInfoList", ciSysInfoList);
        customGroupId = this.getRequest().getParameter("customGroupId");
        CiCustomGroupInfo var11 = this.customersService.queryCiCustomGroupInfo(customGroupId);
        int var12 = var11.getUpdateCycle().intValue();
        if(this.pager == null) {
            this.pager = new Pager();
        }

        byte var13 = 6;
        this.pager.setPageSize(var13);
        if(this.pager.getTotalSize() == 0L) {
            this.pager.setTotalSize((long)this.customersAnalysisService.selectCustomersTotalByTrendAnalysisCount(customGroupId, var11.getDataDate(), var12));
        }

        this.pager = this.pager.pagerFlip();
        this.trendAnalysisList = this.customersAnalysisService.queryTrendAnalysis(customGroupId, var11.getDataDate(), var12, this.pager.getPageNum(), this.pager.getPageSize());
        int var14;
        if(var12 == 3) {
            if(this.pager.getTotalSize() > 8L) {
                this.pager.setTotalSize(8L);
            }

            if(this.pager.getPageNum() > 1 && this.trendAnalysisList.size() > 0) {
                var14 = 2;
                if(this.trendAnalysisList.size() < var14) {
                    var14 = this.trendAnalysisList.size();
                }

                this.trendAnalysisList = this.trendAnalysisList.subList(0, var14);
            }
        }

        var14 = var13 - this.trendAnalysisList.size();
        if(var14 < var13) {
            for(int var15 = 0; var15 < var14; ++var15) {
                LabelTrendInfo l = new LabelTrendInfo();
                this.trendAnalysisList.add(l);
            }
        }

        this.pager.setResult(this.trendAnalysisList);
        CILogServiceUtil.getLogServiceInstance().log("COC_CUS_TREND_LIST_VIEW", var11.getCustomGroupId(), var11.getCustomGroupName(), "首页群乐享-客户群趋势表格查看【客户群ID：" + var11.getCustomGroupId() + "名称：" + var11.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "customersTrendTable";
    }

    public String initCustomersTrendDetail() {
        String customGroupId = this.getRequest().getParameter("customGroupId");
        boolean success = false;
        String msg = "";

        try {
            this.customGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
            String e = this.customersService.queryCustomerOffsetStr(this.customGroupInfo);
            this.customGroupInfo.setKpiDiffRule((this.customGroupInfo.getKpiDiffRule() == null?"":this.customGroupInfo.getKpiDiffRule()) + e);
            success = true;
            Date today = new Date();
            String month = DateUtil.date2String(today, "yyyyMM");
            this.dataDate = DateUtil.getFrontMonth(1, month);
            if(success && 0 == this.customGroupInfo.getStatus().intValue()) {
                success = false;
                msg = "该客户群已被删除！";
            }
        } catch (Exception var7) {
            success = false;
            msg = "客户群清单趋势查询出错";
            this.log.error(msg, var7);
            throw new CIServiceException(var7);
        }

        this.resultMap = new HashMap();
        this.resultMap.put("success", Boolean.valueOf(success));
        this.resultMap.put("msg", msg);
        return "customersTrendDetail";
    }

    public String customersTrendAnalysisTable() throws CIServiceException {
        String customGroupId = this.customGroupInfo.getCustomGroupId();
        CiCustomGroupInfo ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
        int cycle = ciCustomGroupInfo.getUpdateCycle().intValue();
        if(this.pager == null) {
            this.pager = new Pager();
        }

        int totalSize = this.customersAnalysisService.selectCustomersTotalByTrendAnalysisCount(customGroupId, ciCustomGroupInfo.getDataDate(), cycle);
        this.pager.setPageSize(6);
        this.pager.setTotalSize((long)totalSize);
        this.pager.setTotalPage((int)Math.ceil((double)this.pager.getTotalSize() / (double)this.pager.getPageSize()));
        this.pager = this.pager.pagerFlip();
        this.pager.setResult(this.trendAnalysisList = this.customersAnalysisService.queryTrendAnalysis(customGroupId, ciCustomGroupInfo.getDataDate(), cycle, this.pager.getPageNum(), this.pager.getPageSize()));
        CILogServiceUtil.getLogServiceInstance().log("COC_CUS_NUM_TREND_TABEL_VIEW", ciCustomGroupInfo.getCustomGroupId(), ciCustomGroupInfo.getCustomGroupName(), "客户群分析-趋势分析表格查看【客户群ID：" + ciCustomGroupInfo.getCustomGroupId() + "名称：" + ciCustomGroupInfo.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "customersTrendAnalysisTable";
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public String getDataDate() {
        return this.dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public AIChart getCustomersTrendChart() {
        return this.customersTrendChart;
    }

    public void setCustomersTrendChart(AIChart customersTrendChart) {
        this.customersTrendChart = customersTrendChart;
    }

    public List<LabelTrendInfo> getTrendAnalysisList() {
        return this.trendAnalysisList;
    }

    public void setTrendAnalysisList(List<LabelTrendInfo> trendAnalysisList) {
        this.trendAnalysisList = trendAnalysisList;
    }

    public CiCustomGroupInfo getCustomGroupInfo() {
        return this.customGroupInfo;
    }

    public void setCustomGroupInfo(CiCustomGroupInfo customGroupInfo) {
        this.customGroupInfo = customGroupInfo;
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
}
