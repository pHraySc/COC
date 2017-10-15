package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.CommonConstants;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.DimOpLogType;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.CiLogStatAnalysisModel;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.model.TreeNode;
import com.ailk.biapp.ci.service.ICiLogStatAnalysisService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.FileUtil;
import com.ailk.biapp.ci.util.IdToName;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.privilege.IUserCompany;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiLogStatAnalysisAction extends CIBaseAction {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ICiLogStatAnalysisService ciLogStatAnalysisService;
    private CiLogStatAnalysisModel ciLogStatAnalysisModel = new CiLogStatAnalysisModel();
    private Pager pager;
    private List<DimOpLogType> opLogTypeList;
    private List<DimOpLogType> headList;
    private List<CiLogStatAnalysisModel> multDeptOptypesChartList;
    private List<CiLogStatAnalysisModel> multDeptOpTypesTableList;
    private List<Map<String, Object>> oneDeptOpTypesTableList;
    private List<Map<String, Object>> deptMultOpTypesChartList = new ArrayList();
    private String isClick;

    public CiLogStatAnalysisAction() {
    }

    public String logAnalysisIndex() {
        try {
            String e = DateUtil.getFrontDate(1, DateUtil.getCurrentDayYYYYMMDD(), 1);
            String beginDate = DateUtil.getFrontDate(31, DateUtil.getCurrentDayYYYYMMDD(), 1);
            String min = DateUtil.getFrontDate(91, DateUtil.getCurrentDayYYYYMMDD(), 1);
            this.getRequest().setAttribute("endDate", e);
            this.getRequest().setAttribute("beginDate", beginDate);
            this.getRequest().setAttribute("minDate", min);
        } catch (Exception var4) {
            this.log.error("初始化统计页面错误", var4);
            var4.printStackTrace();
        }

        return "logAnalysisIndex";
    }

    public String loadTitle() {
        try {
            this.findAnaOpTypeId();
            this.getRequest().setAttribute("opTypeId", this.ciLogStatAnalysisModel.getOpTypeId());
        } catch (Exception var2) {
            this.log.error("加载趋势图错误", var2);
            var2.printStackTrace();
        }

        return "logIndexLoadTitle";
    }

    public void changeDate() {
        String frontOrNext = this.getRequest().getParameter("frontOrNext");
        String endDate = "";
        HashMap result = new HashMap();
        if("front".equals(frontOrNext)) {
            endDate = DateUtil.getFrontDate(1, this.ciLogStatAnalysisModel.getEndDate(), 1);
        } else if("next".equals(frontOrNext)) {
            endDate = DateUtil.getFrontDate(-1, this.ciLogStatAnalysisModel.getEndDate(), 1);
        }

        endDate = endDate.substring(0, 4) + "-" + endDate.substring(4, 6) + "-" + endDate.substring(6, 8);
        result.put("endDate", endDate);
        result.put("success", Boolean.valueOf(true));
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void exportIndexTable() {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        if(this.pager.getPageSize() < 1) {
            this.pager.setPageSize(10);
        }

        HttpServletResponse response = this.getResponse();
        HttpServletRequest request = this.getRequest();
        String fname = "各部门系统使用情况总览表格.xls";
        FileUtil.downLoadFile(response, fname);
        List headList = null;
        List resultList = null;
        WritableWorkbook wwb = null;
        ServletOutputStream os = null;
        int row = 1;
        byte col = 0;
        int k = 2;

        try {
            request.setCharacterEncoding("UTF-8");
            os = response.getOutputStream();
            headList = this.ciLogStatAnalysisService.findOpTypeListByParentId("-1");
            String e = "";

            for(int ws1 = 0; ws1 < headList.size(); ++ws1) {
                e = e + ((DimOpLogType)headList.get(ws1)).getOpTypeId();
                if(ws1 != headList.size() - 1) {
                    e = e + ",";
                }
            }

            this.ciLogStatAnalysisModel.setOpTypeId(e);
            resultList = this.ciLogStatAnalysisService.findAllLogViewDataInThePeriod(this.pager, this.ciLogStatAnalysisModel);
            wwb = Workbook.createWorkbook(os);
            WritableSheet var27 = wwb.createSheet("sheet1", 0);
            var27.addCell(new Label(0, 0, "二级部门名称"));
            var27.addCell(new Label(1, 0, "三级部门名称"));

            Iterator it;
            for(it = headList.iterator(); it.hasNext(); ++k) {
                DimOpLogType model = (DimOpLogType)it.next();
                var27.addCell(new Label(k, 0, model.getOpTypeName()));
            }

            for(it = resultList.iterator(); it.hasNext(); col = 0) {
                CiLogStatAnalysisModel var28 = (CiLogStatAnalysisModel)it.next();
                int var26 = col + 1;
                var27.addCell(new Label(col, row, var28.getSecondDeptName()));
                var27.addCell(new Label(var26++, row, var28.getThirdDeptName()));
                var27.addCell(new Label(var26++, row, var28.getOpTimes1()));
                var27.addCell(new Label(var26++, row, var28.getOpTimes2()));
                var27.addCell(new Label(var26++, row, var28.getOpTimes3()));
                var27.addCell(new Label(var26++, row, var28.getOpTimes4()));
                var27.addCell(new Label(var26++, row, var28.getOpTimes5()));
                var27.addCell(new Label(var26++, row, var28.getOpTimes6()));
                var27.addCell(new Label(var26++, row, var28.getOpTimes7()));
                var27.addCell(new Label(var26++, row, var28.getOpTimes8()));
                var27.addCell(new Label(var26++, row, var28.getOpTimes9()));
                var27.addCell(new Label(var26++, row, var28.getOpTimes10()));
                ++row;
            }

            wwb.write();
            CILogServiceUtil.getLogServiceInstance().log("COC_LOG_DOWNLOAD", "", "", "日志下载成功,【部门整体概况统计列表】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var24) {
            this.log.error("导出日志统计错误", var24);
            CILogServiceUtil.getLogServiceInstance().log("COC_LOG_DOWNLOAD", "-1", "", "日志下载失败,【部门整体概况统计列表】", OperResultEnum.Failure, LogLevelEnum.Medium);
            var24.printStackTrace();
        } finally {
            try {
                wwb.close();
                os.close();
            } catch (IOException var23) {
                this.log.error("导出日志统计关闭流错误", var23);
                var23.printStackTrace();
            }

        }

    }

    public void initDeptTree() {
        ArrayList treeList = null;
        boolean success = false;

        try {
            List e = PrivilegeServiceUtil.getAllUserCompany();
            treeList = new ArrayList();
            HashMap result = new HashMap();
            success = true;
            if("bj".equalsIgnoreCase(CommonConstants.base)) {
                Iterator var14 = e.iterator();

                while(var14.hasNext()) {
                    IUserCompany var16 = (IUserCompany)var14.next();
                    TreeNode var17 = new TreeNode();
                    var17.setId(var16.getDeptid().toString());
                    var17.setpId(var16.getParentid().toString());
                    if("-1".equals(var17.getpId())) {
                        var17.setIsParent(Boolean.valueOf(true));
                    } else {
                        var17.setIsParent(Boolean.valueOf(false));
                    }

                    var17.setName(var16.getTitle());
                    var17.setTip(var16.getTitle());
                    treeList.add(var17);
                }
            } else {
                ArrayList response = new ArrayList();
                Iterator e1 = e.iterator();

                label84:
                while(true) {
                    IUserCompany i;
                    do {
                        if(!e1.hasNext()) {
                            e1 = e.iterator();

                            while(true) {
                                do {
                                    do {
                                        if(!e1.hasNext()) {
                                            break label84;
                                        }

                                        i = (IUserCompany)e1.next();
                                    } while("0".equals(i.getParentid()));
                                } while(0 == i.getParentid().intValue());

                                TreeNode t = new TreeNode();
                                t.setId(i.getDeptid().toString());
                                t.setpId(i.getParentid().toString());
                                boolean isSecondDept = false;

                                for(int k = 0; k < response.size(); ++k) {
                                    Integer fristDept = (Integer)response.get(k);
                                    if(fristDept.equals(t.getpId())) {
                                        isSecondDept = true;
                                    }
                                }

                                if(isSecondDept) {
                                    t.setIsParent(Boolean.valueOf(true));
                                } else {
                                    t.setIsParent(Boolean.valueOf(false));
                                }

                                t.setName(i.getTitle());
                                t.setTip(i.getTitle());
                                treeList.add(t);
                            }
                        }

                        i = (IUserCompany)e1.next();
                    } while(!"0".equals(i.getParentid()) && 0 != i.getParentid().intValue());

                    response.add(i.getDeptid());
                }
            }

            result.put("treeList", treeList);
            result.put("success", Boolean.valueOf(success));
            HttpServletResponse var15 = this.getResponse();

            try {
                this.sendJson(var15, JsonUtil.toJson(result));
            } catch (Exception var12) {
                this.log.error("发送json串异常", var12);
                throw new CIServiceException(var12);
            }
        } catch (Exception var13) {
            this.log.error("部门tree查询错误", var13);
            var13.printStackTrace();
        }

    }

    public void findAnaOpTypeJson() {
        HashMap result = new HashMap();
        List opTypeModelList = null;

        try {
            opTypeModelList = this.ciLogStatAnalysisService.findOpTypeListByParentId("-1");
        } catch (Exception var6) {
            this.log.error("获取操作类型列表错误...", var6);
            var6.printStackTrace();
        }

        result.put("opTypeModelList", opTypeModelList);
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var5) {
            this.log.error("发送json串异常", var5);
            throw new CIServiceException(var5);
        }
    }

    private List<CiLogStatAnalysisModel> findAnaOpTypeId() {
        List headList = null;

        try {
            headList = this.ciLogStatAnalysisService.findAnaOpTypeInThePeriod(this.ciLogStatAnalysisModel);
            this.getRequest().setAttribute("resultList", headList);
        } catch (Exception var3) {
            this.log.error("获取操作类型表头失败！", var3);
            var3.printStackTrace();
        }

        return headList;
    }

    public String findAllLogViewData() {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        if(this.pager.getPageSize() < 1) {
            this.pager.setPageSize(10);
        }

        List headList = null;

        try {
            headList = this.ciLogStatAnalysisService.findOpTypeListByParentId("-1");
        } catch (Exception var5) {
            this.log.error("获取操作类型列表错误...", var5);
            var5.printStackTrace();
        }

        String opTypeId = "";
        if(headList != null && headList.size() > 0) {
            for(int resultList = 0; resultList < headList.size(); ++resultList) {
                if(headList.get(resultList) != null) {
                    opTypeId = opTypeId + ((DimOpLogType)headList.get(resultList)).getOpTypeId();
                    if(resultList != headList.size() - 1) {
                        opTypeId = opTypeId + ",";
                    }
                }
            }
        }

        this.ciLogStatAnalysisModel.setOpTypeId(opTypeId);
        List var7 = null;

        try {
            if(this.ciLogStatAnalysisModel != null) {
                var7 = this.ciLogStatAnalysisService.findAllLogViewDataInThePeriod(this.pager, this.ciLogStatAnalysisModel);
                this.pager = this.pager.pagerFlip();
                this.getRequest().setAttribute("resultList", var7);
                this.getRequest().setAttribute("headList", headList);
                if(StringUtil.isNotEmpty(this.isClick) && this.isClick.equalsIgnoreCase("true")) {
                    CILogServiceUtil.getLogServiceInstance().log("COC_LOG_DEPT_OVERALL_STATISTICS", "", "", "日志部门概况统计表查询成功,【开始时间：" + (this.ciLogStatAnalysisModel.getBeginDate() == null?"无":this.ciLogStatAnalysisModel.getBeginDate()) + ",结束时间：" + (this.ciLogStatAnalysisModel.getEndDate() == null?"无":this.ciLogStatAnalysisModel.getEndDate()) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                }
            }
        } catch (Exception var6) {
            this.log.error("获取选择时间段内各部门的操作日志统计信息失败！", var6);
            if(StringUtil.isNotEmpty(this.isClick) && this.isClick.equalsIgnoreCase("true")) {
                CILogServiceUtil.getLogServiceInstance().log("COC_LOG_DEPT_OVERALL_STATISTICS", "-1", "", "日志部门概况统计表查询失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            }

            var6.printStackTrace();
        }

        return "logAnalysisIndexTable";
    }

    public String findOpTypeChar() {
        List resultList = null;
        String str = "";

        try {
            List e = this.findAnaOpTypeId();
            Iterator i$ = e.iterator();

            while(i$.hasNext()) {
                CiLogStatAnalysisModel model = (CiLogStatAnalysisModel)i$.next();
                if(this.ciLogStatAnalysisModel.getOpTypeId().equals(model.getOpTypeId())) {
                    str = model.getOpTypeName();
                }
            }

            resultList = this.ciLogStatAnalysisService.findOpTypeChar(this.ciLogStatAnalysisModel);
            this.getRequest().setAttribute("resultListChar", resultList);
            this.getRequest().setAttribute("typeName", str);
            CILogServiceUtil.getLogServiceInstance().log("COC_LOG_OVERALL_STATISTICS", "", "", "日志整体概况统计查询成功,【开始时间：" + (this.ciLogStatAnalysisModel.getBeginDate() == null?"无":this.ciLogStatAnalysisModel.getBeginDate()) + ",结束时间：" + (this.ciLogStatAnalysisModel.getEndDate() == null?"无":this.ciLogStatAnalysisModel.getEndDate()) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var6) {
            this.log.error("日志统计，整体概要趋势图查询失败！", var6);
            CILogServiceUtil.getLogServiceInstance().log("COC_LOG_OVERALL_STATISTICS", "-1", "", "日志整体概况统计查询失败", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        return "logIndexTrendChart";
    }

    public String findOneDeptOpLogTrend() {
        String[] headStrs = null;

        try {
            if(this.ciLogStatAnalysisModel != null) {
                if(StringUtil.isNotEmpty(this.ciLogStatAnalysisModel.getOpTypeId())) {
                    this.deptMultOpTypesChartList = this.ciLogStatAnalysisService.findOneDeptOpLogTrend(this.ciLogStatAnalysisModel);
                    headStrs = this.ciLogStatAnalysisModel.getOpTypeId().split(",");
                }

                this.getRequest().setAttribute("headStrs", headStrs);
            }
        } catch (Exception var3) {
            this.log.error("查询选择时间段内单个部门的操作日志指标数据失败！", var3);
            var3.printStackTrace();
        }

        return "logADeptOpTypesTrendChart";
    }

    public String findOneDeptOpLogMain() {
        try {
            this.headList = this.ciLogStatAnalysisService.findOpTypeListByParentId("-1");
            if(this.headList != null && this.headList.size() > 0) {
                this.ciLogStatAnalysisModel.setOpTypeId(((DimOpLogType)this.headList.get(0)).getOpTypeId());
            }

            this.findLogUserDetilList();
            CILogServiceUtil.getLogServiceInstance().log("COC_LOG_SINGLE_DEPT_ANALYSIS", "", "", "日志单部门分析成功,【部门id：" + (StringUtil.isEmpty(this.ciLogStatAnalysisModel.getSecondDeptId())?"":this.ciLogStatAnalysisModel.getSecondDeptId()) + (StringUtil.isEmpty(this.ciLogStatAnalysisModel.getThirdDeptId())?"":this.ciLogStatAnalysisModel.getThirdDeptId()) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var2) {
            this.log.error("获取单部门多操作类型数据失败", var2);
            CILogServiceUtil.getLogServiceInstance().log("COC_LOG_SINGLE_DEPT_ANALYSIS", "-1", "", "日志单部门分析失败,【部门id：" + (StringUtil.isNotEmpty(this.ciLogStatAnalysisModel.getSecondDeptId())?"":this.ciLogStatAnalysisModel.getSecondDeptId()) + (StringUtil.isNotEmpty(this.ciLogStatAnalysisModel.getThirdDeptId())?"":this.ciLogStatAnalysisModel.getThirdDeptId()) + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
            var2.printStackTrace();
        }

        return "logADeptOpTypesMain";
    }

    public String findOneDeptOpLogUserDetail() {
        this.findLogUserDetilList();
        return "logADeptOpTypesList";
    }

    private void findLogUserDetilList() {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        if(this.pager.getPageSize() < 1) {
            this.pager.setPageSize(10);
        }

        String[] headStrs = null;

        try {
            if(this.ciLogStatAnalysisModel != null) {
                if(StringUtil.isNotEmpty(this.ciLogStatAnalysisModel.getOpTypeId())) {
                    Pager e = new Pager((long)this.ciLogStatAnalysisService.findLogUserDetilListCount(this.ciLogStatAnalysisModel));
                    this.pager.setTotalPage(e.getTotalPage());
                    this.pager.setTotalSize(e.getTotalSize());
                    this.pager = this.pager.pagerFlip();
                    this.oneDeptOpTypesTableList = this.ciLogStatAnalysisService.findLogUserDetilList(this.pager, this.ciLogStatAnalysisModel);
                    this.pager.setResult(this.oneDeptOpTypesTableList);
                    headStrs = this.ciLogStatAnalysisModel.getOpTypeId().split(",");
                }

                this.getRequest().setAttribute("headStrs", headStrs);
            }
        } catch (Exception var3) {
            this.log.error("查询单个操作日志指标的具体操作人失败！", var3);
            var3.printStackTrace();
        }

    }

    public String findOneOpLogTypeSpread() {
        try {
            if(this.ciLogStatAnalysisModel != null) {
                this.multDeptOptypesChartList = this.ciLogStatAnalysisService.findOneOpLogTypeSpread(this.ciLogStatAnalysisModel);
            }
        } catch (Exception var2) {
            this.log.error("查询单个操作在各部门的使用情况失败！", var2);
            var2.printStackTrace();
        }

        return "1".equals(this.ciLogStatAnalysisModel.getThirdDeptId())?"logAnaChart":"logThirdAnaChart";
    }

    public String findMultOpTypeAllLogViewData() {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        if(this.pager.getPageSize() < 1) {
            this.pager.setPageSize(10);
        }

        String[] headStrs = null;

        try {
            this.headList = this.ciLogStatAnalysisService.findOpTypeListByParentId(this.ciLogStatAnalysisModel.getParentId());
        } catch (Exception var5) {
            this.log.error("获取操作类型列表错误...", var5);
            var5.printStackTrace();
        }

        String opTypeId = "";

        for(int e = 0; e < this.headList.size(); ++e) {
            opTypeId = opTypeId + ((DimOpLogType)this.headList.get(e)).getOpTypeId();
            if(e != this.headList.size() - 1) {
                opTypeId = opTypeId + ",";
            }
        }

        if(StringUtil.isEmpty(opTypeId)) {
            opTypeId = this.ciLogStatAnalysisModel.getParentId();
        }

        if(StringUtil.isNotEmpty(opTypeId)) {
            this.ciLogStatAnalysisModel.setOpTypeId(opTypeId);
            headStrs = this.ciLogStatAnalysisModel.getOpTypeId().split(",");
        }

        this.getRequest().setAttribute("headStrs", headStrs);

        try {
            if(this.ciLogStatAnalysisModel != null) {
                Pager var6 = new Pager((long)this.ciLogStatAnalysisService.findAllSecondLogViewDataInThePeriodCount(this.ciLogStatAnalysisModel));
                this.pager.setTotalPage(var6.getTotalPage());
                this.pager.setTotalSize(var6.getTotalSize());
                this.pager = this.pager.pagerFlip();
                this.multDeptOpTypesTableList = this.ciLogStatAnalysisService.findAllSecondLogViewDataInThePeriod(this.pager, this.ciLogStatAnalysisModel);
                this.pager.setResult(this.multDeptOpTypesTableList);
            }
        } catch (Exception var4) {
            this.log.error("获取选择时间段内各部门的操作日志统计信息失败！", var4);
            var4.printStackTrace();
        }

        return "logAanMultList";
    }

    public void findDeptTree() {
        List treeList = null;
        boolean success = false;
        HashMap result = new HashMap();

        try {
            treeList = this.ciLogStatAnalysisService.queryDeptTree();
            success = true;
            result.put("treeList", treeList);
            result.put("success", Boolean.valueOf(success));
        } catch (CIServiceException var7) {
            String e = "获得标签树失败";
            this.log.error(e, var7);
            success = false;
            result.put("msg", e);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    private void oneOpLogTypeUserSpreadList() {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        if(this.pager.getPageSize() < 1) {
            this.pager.setPageSize(10);
        }

        String[] headStrs = null;

        try {
            this.headList = this.ciLogStatAnalysisService.findOpTypeListByParentId(this.ciLogStatAnalysisModel.getParentId());
        } catch (Exception var5) {
            this.log.error("获取操作类型列表错误...", var5);
            var5.printStackTrace();
        }

        String opTypeId = "";
        if(this.headList.size() > 0) {
            headStrs = new String[this.headList.size()];

            for(int e = 0; e < this.headList.size(); ++e) {
                opTypeId = opTypeId + ((DimOpLogType)this.headList.get(e)).getOpTypeId();
                headStrs[e] = ((DimOpLogType)this.headList.get(e)).getOpTypeId();
                if(e != this.headList.size() - 1) {
                    opTypeId = opTypeId + ",";
                }
            }
        }

        if(StringUtil.isEmpty(opTypeId)) {
            opTypeId = this.ciLogStatAnalysisModel.getParentId();
            headStrs = new String[]{opTypeId};
        }

        if(StringUtil.isNotEmpty(opTypeId)) {
            this.ciLogStatAnalysisModel.setOpTypeId(opTypeId);
        }

        this.getRequest().setAttribute("headStrs", headStrs);

        try {
            if(this.ciLogStatAnalysisModel != null && StringUtil.isNotEmpty(this.ciLogStatAnalysisModel.getOpTypeId())) {
                Pager var6 = new Pager((long)this.ciLogStatAnalysisService.findLogUserDetilListCount(this.ciLogStatAnalysisModel));
                this.pager.setTotalPage(var6.getTotalPage());
                this.pager.setTotalSize(var6.getTotalSize());
                this.pager = this.pager.pagerFlip();
                this.oneDeptOpTypesTableList = this.ciLogStatAnalysisService.findLogUserDetilList(this.pager, this.ciLogStatAnalysisModel);
                this.pager.setResult(this.oneDeptOpTypesTableList);
            }
        } catch (Exception var4) {
            this.log.error("查询单个操作日志指标的具体操作人失败！", var4);
            var4.printStackTrace();
        }

    }

    public String findOneOpLogTypeUserSpread() {
        this.oneOpLogTypeUserSpreadList();
        return "logMultDeptOpTypesUserList";
    }

    public void export() {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        this.pager.setPageSize(2147483647);
        List resultList = null;
        String[] headList = null;
        StringBuilder headerList = new StringBuilder();
        headerList = headerList.append("操作时间").append(",").append("操作人").append(",").append("二级部门名称").append(",").append("三级部门名称").append(",");

        try {
            String e = this.ciLogStatAnalysisModel.getOpTypeId();
            headList = e.split(",");

            for(int header = 0; header < headList.length; ++header) {
                String contents = IdToName.getName("DIM_OP_LOG_TYPE", headList[header]);
                headerList = headerList.append(contents);
                if(header != headList.length - 1) {
                    e = e + ",";
                    headerList = headerList.append(",");
                }
            }

            resultList = this.ciLogStatAnalysisService.findLogUserDetilList(this.pager, this.ciLogStatAnalysisModel);
            String var14 = headerList.toString();
            StringBuffer var15 = new StringBuffer();
            String content = "";
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat df_now = new SimpleDateFormat("yyyyMMdd");
            if(resultList.size() > 0) {
                Iterator response = resultList.iterator();

                while(true) {
                    if(!response.hasNext()) {
                        content = var15.substring(0, var15.length() - 2);
                        break;
                    }

                    Map request = (Map)response.next();
                    var15 = var15.append(request.get("data_date") == null?"":df.format(df_now.parse(request.get("data_date").toString()))).append("&&").append(request.get("USER_NAME") == null?"":request.get("USER_NAME").toString()).append("&&").append(request.get("SECOND_DEPT_NAME") == null?"":request.get("SECOND_DEPT_NAME").toString()).append("&&").append(request.get("THIRD_DEPT_NAME") == null?"":request.get("THIRD_DEPT_NAME").toString()).append("&&");

                    for(int fileName = 1; fileName <= headList.length; ++fileName) {
                        var15.append(request.get("op_TIMES_" + fileName) == null?"":request.get("op_TIMES_" + fileName).toString());
                        if(fileName < headList.length) {
                            var15 = var15.append("&&");
                        }
                    }

                    var15 = var15.append("||");
                }
            }

            HttpServletResponse var16 = this.getResponse();
            HttpServletRequest var17 = this.getRequest();
            var17.setCharacterEncoding("UTF-8");
            String var18 = "部门操作日志.xls";
            FileUtil.downLoadFile(var16, var18);
            FileUtil.export(var17, var16, var14, content);
            CILogServiceUtil.getLogServiceInstance().log("COC_LOG_DOWNLOAD", "", "", "日志下载成功,【单部门操作日志统计列表】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var13) {
            this.log.error("获取用户操作信息失败", var13);
            CILogServiceUtil.getLogServiceInstance().log("COC_LOG_DOWNLOAD", "-1", "", "日志下载失败,【单部门操作日志统计列表】", OperResultEnum.Failure, LogLevelEnum.Medium);
            var13.printStackTrace();
        }

    }

    public String anaChart() {
        try {
            this.opLogTypeList = this.ciLogStatAnalysisService.findOpTypeListByParentId(this.ciLogStatAnalysisModel.getParentId());
            if(this.opLogTypeList.size() > 0) {
                this.ciLogStatAnalysisModel.setOpTypeId(((DimOpLogType)this.opLogTypeList.get(0)).getOpTypeId());
            } else {
                this.ciLogStatAnalysisModel.setOpTypeId(" ");
                this.ciLogStatAnalysisModel.setOpTypeId(this.ciLogStatAnalysisModel.getParentId());
            }
        } catch (Exception var2) {
            this.log.error("多部门对比标签操作趋势图初始化失败", var2);
        }

        return "initAnaChart";
    }

    public String logMultDeptCompareInit() {
        try {
            this.headList = this.ciLogStatAnalysisService.findOpTypeListByParentId("-1");
            if((null == this.ciLogStatAnalysisModel.getParentId() || "".equals(this.ciLogStatAnalysisModel.getParentId())) && this.headList.size() > 0) {
                this.ciLogStatAnalysisModel.setParentId(((DimOpLogType)this.headList.get(0)).getOpTypeId());
            }

            this.opLogTypeList = this.ciLogStatAnalysisService.findOpTypeListByParentId(this.ciLogStatAnalysisModel.getParentId());
            if(this.opLogTypeList.size() > 0) {
                this.ciLogStatAnalysisModel.setOpTypeId(((DimOpLogType)this.opLogTypeList.get(0)).getOpTypeId());
            } else {
                this.ciLogStatAnalysisModel.setOpTypeId(" ");
                this.ciLogStatAnalysisModel.setOpTypeId(this.ciLogStatAnalysisModel.getParentId());
            }

            CILogServiceUtil.getLogServiceInstance().log("COC_LOG_MULTI_DEPT_ANALYSIS", "", "", "日志多部门分析成功", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var2) {
            this.log.error("日志多部门分析错误", var2);
            CILogServiceUtil.getLogServiceInstance().log("COC_LOG_MULTI_DEPT_ANALYSIS", "-1", "", "日志多部门分析失败", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        return "logMultDeptCompareInit";
    }

    public String toMultOpTyPesExcel() {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        if(this.pager.getPageSize() < 1) {
            this.pager.setPageSize(10);
        }

        HttpServletResponse response = this.getResponse();
        HttpServletRequest request = this.getRequest();
        String fname = "多部门操作日志情况.xls";
        FileUtil.downLoadFile(response, fname);
        Object headList = null;
        List resultList = null;
        WritableWorkbook wwb = null;
        ServletOutputStream os = null;
        int row = 1;
        byte col = 0;
        int k = 3;

        try {
            request.setCharacterEncoding("UTF-8");
            os = response.getOutputStream();
            headList = this.ciLogStatAnalysisService.findOpTypeListByParentId(this.ciLogStatAnalysisModel.getParentId());
            String e = "";

            for(int ws1 = 0; ws1 < ((List)headList).size(); ++ws1) {
                e = e + ((DimOpLogType)((List)headList).get(ws1)).getOpTypeId();
                if(ws1 != ((List)headList).size() - 1) {
                    e = e + ",";
                }
            }

            if(StringUtil.isEmpty(e)) {
                e = this.ciLogStatAnalysisModel.getParentId();
                CopyOnWriteArrayList var27 = CacheBase.getInstance().getObjectList("DIM_OP_LOG_TYPE");
                if(var27 != null && var27.size() > 0) {
                    headList = new ArrayList();

                    for(int it = 0; it < var27.size(); ++it) {
                        String model = ((DimOpLogType)var27.get(it)).getOpTypeId();
                        model = StringUtil.isEmpty(model)?"":model.trim();
                        if(e.equalsIgnoreCase(model)) {
                            ((List)headList).add(var27.get(it));
                        }
                    }
                }
            }

            this.ciLogStatAnalysisModel.setOpTypeId(e);
            resultList = this.ciLogStatAnalysisService.findAllSecondLogViewDataInThePeriod(this.ciLogStatAnalysisModel);
            wwb = Workbook.createWorkbook(os);
            WritableSheet var28 = wwb.createSheet("sheet1", 0);
            var28.addCell(new Label(0, 0, "操作时间"));
            var28.addCell(new Label(1, 0, "二级部门名称"));
            var28.addCell(new Label(2, 0, "三级部门名称"));

            Iterator var29;
            for(var29 = ((List)headList).iterator(); var29.hasNext(); ++k) {
                DimOpLogType var30 = (DimOpLogType)var29.next();
                var28.addCell(new Label(k, 0, var30.getOpTypeName()));
            }

            for(var29 = resultList.iterator(); var29.hasNext(); col = 0) {
                CiLogStatAnalysisModel var31 = (CiLogStatAnalysisModel)var29.next();
                int var26 = col + 1;
                var28.addCell(new Label(col, row, var31.getDataDate()));
                var28.addCell(new Label(var26++, row, var31.getSecondDeptName()));
                var28.addCell(new Label(var26++, row, var31.getThirdDeptName()));
                var28.addCell(new Label(var26++, row, var31.getOpTimes1()));
                var28.addCell(new Label(var26++, row, var31.getOpTimes2()));
                var28.addCell(new Label(var26++, row, var31.getOpTimes3()));
                var28.addCell(new Label(var26++, row, var31.getOpTimes4()));
                var28.addCell(new Label(var26++, row, var31.getOpTimes5()));
                var28.addCell(new Label(var26++, row, var31.getOpTimes6()));
                var28.addCell(new Label(var26++, row, var31.getOpTimes7()));
                var28.addCell(new Label(var26++, row, var31.getOpTimes8()));
                var28.addCell(new Label(var26++, row, var31.getOpTimes9()));
                var28.addCell(new Label(var26++, row, var31.getOpTimes10()));
                ++row;
            }

            wwb.write();
            CILogServiceUtil.getLogServiceInstance().log("COC_LOG_DOWNLOAD", "", "", "日志下载成功,【多部门操作日志列表】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var24) {
            this.log.error("多部门标签操作信息导出失败", var24);
            CILogServiceUtil.getLogServiceInstance().log("COC_LOG_DOWNLOAD", "-1", "", "日志下载失败,【多部门操作日志列表】", OperResultEnum.Failure, LogLevelEnum.Medium);
        } finally {
            try {
                if(wwb != null) {
                    wwb.close();
                }

                if(os != null) {
                    os.close();
                }
            } catch (IOException var23) {
                this.log.error("多部门标签操作信息导出失败,文件流关闭失败", var23);
            }

        }

        return null;
    }

    public String toMultOpTyPesUserExcel() {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        if(this.pager.getPageSize() < 1) {
            this.pager.setPageSize(10);
        }

        HttpServletResponse response = this.getResponse();
        HttpServletRequest request = this.getRequest();
        String fname = "多部门操作人操作日志明细.xls";
        FileUtil.downLoadFile(response, fname);
        Object headList = null;
        List resultList = null;
        WritableWorkbook wwb = null;
        ServletOutputStream os = null;
        int row = 1;
        byte col = 0;
        int k = 3;

        try {
            request.setCharacterEncoding("UTF-8");
            os = response.getOutputStream();
            headList = this.ciLogStatAnalysisService.findOpTypeListByParentId(this.ciLogStatAnalysisModel.getParentId());
            String e = "";

            for(int ws1 = 0; ws1 < ((List)headList).size(); ++ws1) {
                e = e + ((DimOpLogType)((List)headList).get(ws1)).getOpTypeId();
                if(ws1 != ((List)headList).size() - 1) {
                    e = e + ",";
                }
            }

            if(StringUtil.isEmpty(e)) {
                e = this.ciLogStatAnalysisModel.getParentId();
                CopyOnWriteArrayList var30 = CacheBase.getInstance().getObjectList("DIM_OP_LOG_TYPE");
                if(var30 != null && var30.size() > 0) {
                    headList = new ArrayList();

                    for(int it = 0; it < var30.size(); ++it) {
                        String model = ((DimOpLogType)var30.get(it)).getOpTypeId();
                        model = StringUtil.isEmpty(model)?"":model.trim();
                        if(e.equalsIgnoreCase(model)) {
                            ((List)headList).add(var30.get(it));
                        }
                    }
                }
            }

            this.ciLogStatAnalysisModel.setOpTypeId(e);
            resultList = this.ciLogStatAnalysisService.findLogUserDetilList(this.ciLogStatAnalysisModel);
            wwb = Workbook.createWorkbook(os);
            WritableSheet var31 = wwb.createSheet("sheet1", 0);
            var31.addCell(new Label(0, 0, "操作时间"));
            var31.addCell(new Label(1, 0, "操作人"));
            var31.addCell(new Label(2, 0, "三级部门名称"));

            Iterator var32;
            for(var32 = ((List)headList).iterator(); var32.hasNext(); ++k) {
                DimOpLogType var33 = (DimOpLogType)var32.next();
                var31.addCell(new Label(k, 0, var33.getOpTypeName()));
            }

            for(var32 = resultList.iterator(); var32.hasNext(); col = 0) {
                Map var34 = (Map)var32.next();
                int var29 = col + 1;
                var31.addCell(new Label(col, row, var34.get("data_date").toString()));
                var31.addCell(new Label(var29++, row, var34.get("user_name").toString()));
                var31.addCell(new Label(var29++, row, var34.get("third_dept_Name").toString()));

                for(int i = 0; i < ((List)headList).size(); ++i) {
                    int index = i + 1;
                    String opTimes = "op_times_" + index;
                    if(null != var34.get(opTimes) && !"".equals(var34.get(opTimes))) {
                        var31.addCell(new Label(var29++, row, var34.get(opTimes).toString()));
                    }
                }

                ++row;
            }

            wwb.write();
            CILogServiceUtil.getLogServiceInstance().log("COC_LOG_DOWNLOAD", "", "", "日志下载成功,【多部门操作人操作日志统计列表】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var27) {
            this.log.error("多部门多操作人标签操作信息导出失败", var27);
            CILogServiceUtil.getLogServiceInstance().log("COC_LOG_DOWNLOAD", "-1", "", "日志下载失败,【多部门操作人操作日志统计列表】", OperResultEnum.Failure, LogLevelEnum.Medium);
        } finally {
            try {
                if(wwb != null) {
                    wwb.close();
                }

                if(os != null) {
                    os.close();
                }
            } catch (IOException var26) {
                this.log.error("多部门多操作人标签操作信息导出失败，文件流关闭失败", var26);
            }

        }

        return null;
    }

    public CiLogStatAnalysisModel getCiLogStatAnalysisModel() {
        return this.ciLogStatAnalysisModel;
    }

    public void setCiLogStatAnalysisModel(CiLogStatAnalysisModel ciLogStatAnalysisModel) {
        this.ciLogStatAnalysisModel = ciLogStatAnalysisModel;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public List<DimOpLogType> getOpLogTypeList() {
        return this.opLogTypeList;
    }

    public void setOpLogTypeList(List<DimOpLogType> opLogTypeList) {
        this.opLogTypeList = opLogTypeList;
    }

    public List<DimOpLogType> getHeadList() {
        return this.headList;
    }

    public void setHeadList(List<DimOpLogType> headList) {
        this.headList = headList;
    }

    public List<CiLogStatAnalysisModel> getMultDeptOptypesChartList() {
        return this.multDeptOptypesChartList;
    }

    public void setMultDeptOptypesChartList(List<CiLogStatAnalysisModel> multDeptOptypesChartList) {
        this.multDeptOptypesChartList = multDeptOptypesChartList;
    }

    public List<CiLogStatAnalysisModel> getMultDeptOpTypesTableList() {
        return this.multDeptOpTypesTableList;
    }

    public void setMultDeptOpTypesTableList(List<CiLogStatAnalysisModel> multDeptOpTypesTableList) {
        this.multDeptOpTypesTableList = multDeptOpTypesTableList;
    }

    public List<Map<String, Object>> getOneDeptOpTypesTableList() {
        return this.oneDeptOpTypesTableList;
    }

    public void setOneDeptOpTypesTableList(List<Map<String, Object>> oneDeptOpTypesTableList) {
        this.oneDeptOpTypesTableList = oneDeptOpTypesTableList;
    }

    public List<Map<String, Object>> getDeptMultOpTypesChartList() {
        return this.deptMultOpTypesChartList;
    }

    public void setDeptMultOpTypesChartList(List<Map<String, Object>> deptMultOpTypesChartList) {
        this.deptMultOpTypesChartList = deptMultOpTypesChartList;
    }

    public String getIsClick() {
        return this.isClick;
    }

    public void setIsClick(String isClick) {
        this.isClick = isClick;
    }
}
