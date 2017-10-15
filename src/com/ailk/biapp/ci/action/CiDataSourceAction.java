package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.entity.CiDataSource;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiDataSourceService;
import com.ailk.biapp.ci.util.FileUtil;
import com.opensymphony.xwork2.ActionContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/24.
 */
@Controller("ciDataSourceAction")
@Scope("prototype")
public class CiDataSourceAction extends CIBaseAction {

    private Logger log = Logger.getLogger(CiDataSourceAction.class);
    private Pager pager;
    private CiDataSource ciDataSource;
    private List<CiDataSource> dataSource;
    private ActionContext ac;
    private List<Map<String, Object>> InfluencedLabelList;

    public List<Map<String, Object>> getInfluencedLabelList() {
        return InfluencedLabelList;
    }

    public void setInfluencedLabelList(List<Map<String, Object>> influencedLabelList) {
        InfluencedLabelList = influencedLabelList;
    }

    public ActionContext getAc() {
        return ac;
    }

    public void setAc(ActionContext ac) {
        this.ac = ac;
    }

    HashMap result = new HashMap();

    @Autowired
    private ICiDataSourceService ciDataSourceService;

    public CiDataSourceAction() {
    }

    public String getDataSource() {
        if (this.pager == null) {
            this.pager = new Pager();
        }
        Pager p = new Pager((long) this.ciDataSourceService.getDataCount());
        this.pager.setTotalPage(p.getTotalPage());
        this.pager.setTotalSize(p.getTotalSize());
        this.pager.setPageSize(7);
        this.pager = this.pager.pagerFlip();
        this.log.info("pager exit");
        this.pager.setResult(this.ciDataSourceService.getDataSource(this.pager));
        this.log.info("Exit CiDataSourceAction.getDataSource() method");
        return "dSource";
    }


    public String getTheNewestDataDate() throws ParseException {
        String dataDate = ciDataSourceService.getTheNewestDataDate(this.getCiDataSource().getLabelId());
        this.log.info("dataDate = " + dataDate);
        this.getContext().put("dataDate", dataDate);
        return "theNewestDataDate";
    }

    public String selectInfluencedLabelByDataSrcCode() {
        if (this.pager == null) {
            this.pager = new Pager();
        }
        this.log.info("dataSource" + this.getCiDataSource());
        this.log.info("dataSrcCode" + ciDataSource.getDataSrcCode());
        Pager p = new Pager((long) this.ciDataSourceService.getDataCountByDataSrcCode(ciDataSource));
        this.pager.setTotalPage(p.getTotalPage());
        this.pager.setTotalSize(p.getTotalSize());
        this.pager.setPageSize(3);
        this.pager = this.pager.pagerFlip();
        this.log.info("BurNIng");
        this.InfluencedLabelList = ciDataSourceService.selectInfluencedLabelByDataSrcCode(this.pager, ciDataSource);
        this.log.info("influencedLabelList" + this.InfluencedLabelList);
        this.pager.setResult(this.InfluencedLabelList);
        return "InfluencedLabel";
    }

    public void exportDataSourceToExcel() {
        this.log.info("Sc In");
        String fileName = "数据源导出表.xls";
        HttpServletRequest request = this.getRequest();
        HttpServletResponse response = this.getResponse();
        try {
            request.setCharacterEncoding("UTF-8");
            this.pager = new Pager();
            this.pager.setPageSize(0);
            this.log.info("Sc In 1");
            List dataSourceList = ciDataSourceService.getDataSourceIncludeDate();
            this.log.info("Sc In 2");
            FileUtil.downLoadFile(response, fileName);
            this.log.info("Sc In 3");
            FileUtil.exportDataSourceToExcel(request, response, dataSourceList);
            this.log.info("Sc out");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public CiDataSource getCiDataSource() {
        return ciDataSource;
    }

    public void setCiDataSource(CiDataSource ciDataSource) {
        this.ciDataSource = ciDataSource;
    }

    public void setDataSource(List<CiDataSource> dataSource) {
        this.dataSource = dataSource;
    }

    public HashMap getResult() {
        return result;
    }

    public void setResult(HashMap result) {
        this.result = result;
    }

    public ICiDataSourceService getCiDataSourceService() {
        return ciDataSourceService;
    }

    public void setCiDataSourceService(ICiDataSourceService ciDataSourceService) {
        this.ciDataSourceService = ciDataSourceService;
    }
    public static void main(String args[]) {

    }
}
