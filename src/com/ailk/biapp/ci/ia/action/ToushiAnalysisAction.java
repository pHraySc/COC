package com.ailk.biapp.ci.ia.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.ia.model.AttributeModel;
import com.ailk.biapp.ci.ia.model.WorkBookModel;
import com.ailk.biapp.ci.ia.model.WorkSheetModel;
import com.ailk.biapp.ci.ia.service.IAnalysisAttrService;
import com.ailk.biapp.ci.ia.service.IAnalysisReportService;
import com.ailk.biapp.ci.ia.service.IToushiAnalysisService;
import com.ailk.biapp.ci.util.JsonUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ToushiAnalysisAction extends CIBaseAction {
    private Logger log = Logger.getLogger(ToushiAnalysisAction.class);
    @Autowired
    private IToushiAnalysisService toushiAnalysisService;
    @Autowired
    private IAnalysisAttrService analysisAttrService;
    @Autowired
    private IAnalysisReportService analysisReportService;
    private String groupId;
    private String listTableName;
    private String customGroupId;
    private String workSheetString;

    public ToushiAnalysisAction() {
    }

    public String getCustomGroupId() {
        return this.customGroupId;
    }

    public void setCustomGroupId(String customGroupId) {
        this.customGroupId = customGroupId;
    }

    public String forwardMain() {
        this.getRequest().setAttribute("customGroupId", this.customGroupId);
        return "main";
    }

    public void queryPublicAttr() {
        List attrList = this.analysisAttrService.getPublicAttr();
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(attrList));
        } catch (Exception var4) {
            this.log.error("发送json串异常", var4);
            throw new CIServiceException(var4);
        }
    }

    public void queryCustomAttr() {
        if(this.listTableName == null) {
            throw new CIServiceException("查询客户群自带属性,清单表名为空!");
        } else {
            List attrList = this.analysisAttrService.getCustomAttr(this.listTableName);
            HttpServletResponse response = this.getResponse();

            try {
                this.sendJson(response, JsonUtil.toJson(attrList));
            } catch (Exception var4) {
                this.log.error("发送json串异常", var4);
                throw new CIServiceException(var4);
            }
        }
    }

    public void getCustomGroupList() {
        List list = this.toushiAnalysisService.getCustomGroupList(this.groupId);
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(list));
        } catch (Exception var4) {
            this.log.error("发送json串异常", var4);
            throw new CIServiceException(var4);
        }
    }

    public void generateChart() {
        this.log.debug("workSheetString=" + this.workSheetString);

        try {
            WorkSheetModel e = (WorkSheetModel)JsonUtil.json2Bean(this.workSheetString, WorkSheetModel.class);
            Map map = this.toushiAnalysisService.generateChart(e);
            HttpServletResponse response = this.getResponse();
            this.sendJson(response, JsonUtil.toJson(map));
        } catch (Exception var4) {
            this.log.error("发送json串异常", var4);
            throw new CIServiceException(var4);
        }
    }

    public void generateReport() {
        WorkBookModel model = new WorkBookModel();
        model.setGroupId("test");
        model.setGroupName("test");
        model.setReportDesc("test");
        model.setReportName("test");
        ArrayList wroksheet = new ArrayList();
        WorkSheetModel sheet = new WorkSheetModel();
        sheet.setWorkSheetId("sheet-test");
        sheet.setWorkSheetName("sheet-test");
        sheet.setListTableName("CI_CUSER_20140701142802632");
        sheet.setDataDate("201412");
        sheet.setChartType("line");
        sheet.setChartData("");
        ArrayList row = new ArrayList();
        AttributeModel attr = new AttributeModel();
        attr.setAttrId("1");
        attr.setAttrName("attr-test");
        attr.setAttrType(Integer.valueOf(1));
        row.add(attr);
        sheet.setRow(row);
        wroksheet.add(sheet);
        model.setWroksheet(wroksheet);
        this.analysisReportService.generateReport(model);
    }

    public String getListTableName() {
        return this.listTableName;
    }

    public void setListTableName(String listTableName) {
        this.listTableName = listTableName;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getWorkSheetString() {
        return this.workSheetString;
    }

    public void setWorkSheetString(String workSheetString) {
        this.workSheetString = workSheetString;
    }
}
