package com.ailk.biapp.ci.ia.service.impl;

import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.ia.dao.ICiIaAnalyseAttrHDao;
import com.ailk.biapp.ci.ia.dao.ICiIaChartIndexRuleHDao;
import com.ailk.biapp.ci.ia.dao.ICiIaDimIndexGroupHDao;
import com.ailk.biapp.ci.ia.dao.ICiIaFilterHDao;
import com.ailk.biapp.ci.ia.dao.ICiIaReportDao;
import com.ailk.biapp.ci.ia.dao.ICiIaWorksheetChartRelDao;
import com.ailk.biapp.ci.ia.dao.ICiIaWorksheetDao;
import com.ailk.biapp.ci.ia.entity.CiIaAnalyseAttr;
import com.ailk.biapp.ci.ia.entity.CiIaChartIndexRule;
import com.ailk.biapp.ci.ia.entity.CiIaDimIndexGroup;
import com.ailk.biapp.ci.ia.entity.CiIaReport;
import com.ailk.biapp.ci.ia.entity.CiIaWorksheet;
import com.ailk.biapp.ci.ia.entity.CiIaWorksheetChartRel;
import com.ailk.biapp.ci.ia.model.AttributeModel;
import com.ailk.biapp.ci.ia.model.WorkBookModel;
import com.ailk.biapp.ci.ia.model.WorkSheetModel;
import com.ailk.biapp.ci.ia.service.IAnalysisReportService;
import com.ailk.biapp.ci.ia.utils.PdfUtil;
import com.ailk.biapp.ci.ia.utils.SysConstants;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Table;
import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AnalysisReportServiceImpl implements IAnalysisReportService {
    private Logger log = Logger.getLogger(AnalysisReportServiceImpl.class);
    @Autowired
    private ICiIaReportDao ciIaReportDao;
    @Autowired
    private ICiIaWorksheetDao ciIaWorksheetDao;
    @Autowired
    private ICiIaWorksheetChartRelDao ciIaWorksheetChartRelDao;
    @Autowired
    private ICiIaChartIndexRuleHDao ciIaChartIndexRuleHDao;
    @Autowired
    private ICiIaDimIndexGroupHDao ciIaDimIndexGroupHDao;
    @Autowired
    private ICiIaFilterHDao ciIaFilterHDao;
    @Autowired
    private ICiIaAnalyseAttrHDao ciIaAnalyseAttrHDao;

    public AnalysisReportServiceImpl() {
    }

    public String generateReport(WorkBookModel model) {
        String fileName = null;

        try {
            fileName = this.createPdfReport(model);
        } catch (Exception var4) {
            this.log.error("生成pdf文件失败", var4);
        }

        this.log.debug("生成pdf文件成功[" + fileName + "]");
        this.saveAnalysisResult(model, fileName);
        return null;
    }

    private String createPdfReport(WorkBookModel model) throws Exception {
        List sheets = model.getWroksheet();
        if(sheets != null && sheets.size() != 0) {
            String reportName = model.getReportName();
            String createUserId = PrivilegeServiceUtil.getUserId();
            String createTime = DateUtil.date2String(new Date(), "yyyyMMddHHmmss");
            String pdfName = reportName + "_" + createUserId + "_" + createTime + ".pdf";
            Document doc = PdfUtil.createPdf(new File(SysConstants.ANALYSIS_REPORT_DIR + "/" + pdfName));
            Table pdfTable = PdfUtil.createTable(2);
            pdfTable.setWidth(100.0F);
            int sheetIndex = 0;
            int row = 0;
            Iterator i$ = sheets.iterator();

            while(i$.hasNext()) {
                WorkSheetModel sheet = (WorkSheetModel)i$.next();
                StringBuilder info = new StringBuilder();
                List dims = sheet.getRow();
                ArrayList dimArray = new ArrayList();
                Iterator dimStr = dims.iterator();

                while(dimStr.hasNext()) {
                    AttributeModel guidelines = (AttributeModel)dimStr.next();
                    dimArray.add(guidelines.getAttrName());
                }

                String var24 = StringUtil.list2String(dimArray, "、", false);
                info.append("维度:").append(var24).append("\r\n");
                List var25 = sheet.getColumn();
                ArrayList guidelineArray = new ArrayList();
                info.append("指标:");
                Iterator guidelineStr = var25.iterator();

                while(guidelineStr.hasNext()) {
                    AttributeModel chartData = (AttributeModel)guidelineStr.next();
                    guidelineArray.add(chartData.getAttrName());
                }

                String var26 = StringUtil.list2String(guidelineArray, "、", false);
                info.append("指标:").append(var26).append("\r\n");
                String var27 = sheet.getChartData();
                byte[] imgBuffer = Base64.decodeBase64(var27.getBytes("UTF-8"));
                Image image = Image.getInstance(imgBuffer);
                int col = sheetIndex % 2;
                pdfTable.addCell(PdfUtil.createCellText(info.toString()), row, col);
                pdfTable.addCell(PdfUtil.createCellObject(image), row + 1, col);
                ++sheetIndex;
                if(col == 1) {
                    row += 2;
                }
            }

            doc.add(pdfTable);
            doc.close();
            return pdfName;
        } else {
            throw new CIServiceException("没有要保存的工作表");
        }
    }

    private boolean saveAnalysisResult(WorkBookModel model, String fileName) {
        List sheets = model.getWroksheet();
        if(sheets != null && sheets.size() != 0) {
            WorkSheetModel sheet0 = (WorkSheetModel)sheets.get(0);
            String reportName = model.getReportName();
            String reportDesc = model.getReportDesc();
            String createUserId = PrivilegeServiceUtil.getUserId();
            Timestamp createTime = new Timestamp(System.currentTimeMillis());
            String listTableName = sheet0.getListTableName();
            String dataDate = sheet0.getDataDate();
            String customGroupId = model.getGroupId();
            String customGroupName = model.getGroupName();
            byte status = 1;
            CiIaReport ciIaReport = new CiIaReport(reportName, reportDesc, createUserId, createTime, listTableName, dataDate, customGroupId, customGroupName, Integer.valueOf(status), fileName);
            this.ciIaReportDao.insertCiIaReport(ciIaReport);
            Iterator i$ = sheets.iterator();

            while(true) {
                CiIaWorksheetChartRel ciIaWorksheetChartRel;
                List symbols2;
                label52:
                do {
                    if(!i$.hasNext()) {
                        return true;
                    }

                    WorkSheetModel sheet = (WorkSheetModel)i$.next();
                    CiIaWorksheet ciIaWorksheet = new CiIaWorksheet(sheet.getWorkSheetName(), ciIaReport.getReportId(), Integer.valueOf(status));
                    this.ciIaWorksheetDao.insertCiIaWorksheet(ciIaWorksheet);
                    Integer worksheetId = ciIaWorksheet.getWorksheetId();
                    String chartId = sheet.getChartType();
                    String chartDataFileName = sheet.getWorkSheetId() + ".json";
                    String chartRuleDesc = "";
                    ciIaWorksheetChartRel = new CiIaWorksheetChartRel(worksheetId, chartId, chartDataFileName, chartRuleDesc);
                    this.ciIaWorksheetChartRelDao.insertCiIaWorksheetChartRel(ciIaWorksheetChartRel);
                    List guidelines = sheet.getColumn();
                    Iterator dims = guidelines.iterator();

                    while(dims.hasNext()) {
                        AttributeModel symbols = (AttributeModel)dims.next();
                        this.saveChartIndexRule(symbols, Integer.valueOf(1), ciIaWorksheetChartRel.getWorksheetChartRelId());
                    }

                    List dims1 = sheet.getRow();
                    Iterator symbols1 = dims1.iterator();

                    while(true) {
                        Integer symbol;
                        Integer attrId;
                        List ciIaDimIndexGroupList;
                        do {
                            if(!symbols1.hasNext()) {
                                symbols2 = sheet.getSymbol();
                                continue label52;
                            }

                            AttributeModel i$1 = (AttributeModel)symbols1.next();
                            symbol = ciIaWorksheetChartRel.getWorksheetChartRelId();
                            attrId = this.saveChartIndexRule(i$1, Integer.valueOf(2), ciIaWorksheetChartRel.getWorksheetChartRelId());
                            ciIaDimIndexGroupList = i$1.getGroup();
                        } while(ciIaDimIndexGroupList == null);

                        Iterator i$2 = ciIaDimIndexGroupList.iterator();

                        while(i$2.hasNext()) {
                            CiIaDimIndexGroup group = (CiIaDimIndexGroup)i$2.next();
                            group.setWorksheetChartRelId(symbol);
                            group.setAttrId(attrId);
                        }

                        this.ciIaDimIndexGroupHDao.insert(ciIaDimIndexGroupList);
                    }
                } while(symbols2 == null);

                Iterator i$3 = symbols2.iterator();

                while(i$3.hasNext()) {
                    AttributeModel symbol1 = (AttributeModel)i$3.next();
                    this.saveChartIndexRule(symbol1, Integer.valueOf(3), ciIaWorksheetChartRel.getWorksheetChartRelId());
                }
            }
        } else {
            throw new CIServiceException("没有要保存的工作表");
        }
    }

    private Integer saveChartIndexRule(AttributeModel model, Integer type, Integer worksheetChartRelId) {
        Integer attrId = Integer.valueOf(0);
        if(model.getAttrSource().intValue() != 1) {
            attrId = this.saveAnalyseAttr(model);
        } else {
            attrId = Integer.valueOf(Integer.parseInt(model.getAttrId()));
        }

        Integer aggregationId = model.getAggregationId();
        String unit = model.getGuidelineUnit();
        Integer isShowUnit = Integer.valueOf(0);
        if(StringUtil.isNotEmpty(unit)) {
            isShowUnit = Integer.valueOf(1);
        }

        String dataFormat = model.getGuidelineMask();
        Integer sortType = Integer.valueOf(1);
        CiIaChartIndexRule ciIaChartIndexRule = new CiIaChartIndexRule(attrId, type, aggregationId, worksheetChartRelId, isShowUnit, unit, dataFormat, sortType);
        this.ciIaChartIndexRuleHDao.insert(ciIaChartIndexRule);
        return attrId;
    }

    private Integer saveAnalyseAttr(AttributeModel model) {
        CiIaAnalyseAttr attr = new CiIaAnalyseAttr();
        attr.setAttrName(model.getAttrName());
        attr.setAttrSource(model.getAttrSource());
        attr.setAttrType(model.getAttrType());
        attr.setCustomAttrRule(model.getCustomSystemAttr());
        attr.setGuidelineMask(model.getGuidelineMask());
        attr.setGuidelineTitle(model.getGuidelineTitle());
        attr.setGuidelineUnit(model.getGuidelineUnit());
        attr.setIsCalculate(Integer.valueOf(2));
        attr.setStatus(Integer.valueOf(1));
        this.ciIaAnalyseAttrHDao.insert(attr);
        return attr.getAttrId();
    }
}
