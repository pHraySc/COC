package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.entity.CiProductInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiProductService;
import com.ailk.biapp.ci.util.JsonUtil;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ProductManagerAction extends CIBaseAction {
    private Logger log = Logger.getLogger(ProductManagerAction.class);
    @Autowired
    private ICiProductService ciProductService;
    private List<CiProductInfo> ciProductInfoList;
    private String productName;
    private Pager pager;

    public ProductManagerAction() {
    }

    public void findProductTree() throws Exception {
        List treeList = null;
        boolean success = false;
        HashMap result = new HashMap();

        try {
            if(StringUtil.isNotEmpty(this.productName)) {
                treeList = this.ciProductService.queryProductTreeByName(this.productName);
            } else {
                treeList = this.ciProductService.queryProductTree();
            }

            success = true;
            result.put("treeList", treeList);
            result.put("success", Boolean.valueOf(success));
        } catch (CIServiceException var7) {
            String e = "获得产品树失败";
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

    public void findProductTreeByName() throws Exception {
        List treeList = null;
        boolean success = false;
        HashMap result = new HashMap();

        try {
            treeList = this.ciProductService.queryProductTreeByName(this.productName);
            success = true;
            result.put("treeList", treeList);
            result.put("success", Boolean.valueOf(success));
        } catch (CIServiceException var7) {
            String e = "模糊查询获得产品树失败";
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

    public List<CiProductInfo> getCiProductInfoList() {
        return this.ciProductInfoList;
    }

    public void setCiProductInfoList(List<CiProductInfo> ciProductInfoList) {
        this.ciProductInfoList = ciProductInfoList;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }
}
