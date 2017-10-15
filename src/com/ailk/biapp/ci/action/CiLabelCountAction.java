package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.entity.CilabelCount;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiLabelCountService;
import com.ailk.biapp.ci.util.FileUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 2017/4/24.
 */
@Controller("ciLabelCountAction")
@Scope("prototype")
public class CiLabelCountAction extends CIBaseAction{

    private Logger log = Logger.getLogger(CiLabelCountAction.class);
    private Pager pager;
    private HashMap result = new HashMap();
    @Autowired
    private ICiLabelCountService ciLabelCountService;

    public CiLabelCountAction() {
    }

    public String getLabelCount() {
        this.log.info("start to execute @Controller");
        if(this.pager == null){
            this.pager = new Pager();
        }
        Pager p = new Pager((long)this.ciLabelCountService.getLabelCountNum(),4);
        this.pager.setTotalPage(p.getTotalPage());
        this.pager.setTotalSize(p.getTotalSize());
        this.pager.setPageSize(2);
        this.log.info("this.pageSize" + this.pager.getPageSize());
        this.pager = this.pager.pagerFlip();
        this.pager.setResult(this.ciLabelCountService.getLabelCount(this.pager));
        this.log.info("Exit CiLabelCountAction.getLabelCount() method");
        return "labelCount";
    }

    public void exportLabelCountToExcel() {
        this.log.info("Sc In");
        String fileName = "标签统计（总）.xls";
        HttpServletRequest request = this.getRequest();
        HttpServletResponse response = this.getResponse();
        try {
            request.setCharacterEncoding("UTF-8");
            this.pager = new Pager();
            this.pager.setPageSize(0);
            this.log.info("Sc In 1");
            List labelCountList = ciLabelCountService.getLabelCount(this.pager);
            this.log.info("Sc In 2");
            FileUtil.downLoadFile(response, fileName);
            this.log.info("Sc In 3");
            FileUtil.exportLabelCountToExcel(request, response, labelCountList);
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

    public HashMap getResult() {
        return result;
    }

    public void setResult(HashMap result) {
        this.result = result;
    }

    public ICiLabelCountService getCiLabelCountService() {
        return ciLabelCountService;
    }

    public void setCiLabelCountService(ICiLabelCountService ciLabelCountService) {
        this.ciLabelCountService = ciLabelCountService;
    }
}
