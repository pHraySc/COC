package com.ailk.biapp.ci.webservice.impl;

import com.ailk.biapp.ci.constant.CommonConstants;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.ICiLabelInfoJDao;
import com.ailk.biapp.ci.dao.IMcdInterfaceJDao;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.exception.*;
import com.ailk.biapp.ci.model.TargetCustomersAttr;
import com.ailk.biapp.ci.model.TargetCustomersModel;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.ThreadPool;
import com.ailk.biapp.ci.webservice.IWsMcdInterfaceServer;
import com.ailk.biapp.ci.webservice.util.SendCustGroupContentThread;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.feature.Features;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@WebService(
    endpointInterface = "com.ailk.biapp.ci.webservice.IWsMcdInterfaceServer",
    targetNamespace = "http://webservice.cm.biapp.ailk.com/"
)
@Features(
    features = {"org.apache.cxf.feature.LoggingFeature"}
)
public class WsMcdInterfaceServerImpl implements IWsMcdInterfaceServer {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private IMcdInterfaceJDao iMcdInterfaceJDao;
    @Autowired
    private ICiLabelInfoJDao ciLabelInfoJDao;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public WsMcdInterfaceServerImpl() {
    }

    public String getCustomersList(String userId, String sysId) {
        this.log.info("mcd webservice invoke method getCustomersList start");
        if("nj".equalsIgnoreCase(CommonConstants.base)) {
            userId = userId.toLowerCase();
        }

        this.log.debug("param is : " + userId);
        List list = this.iMcdInterfaceJDao.getCustomerByUserId(userId, sysId);
        String str = "";

        try {
            if(list != null && list.size() != 0) {
                str = JsonUtil.toJson(list, this.dateFormat);
            } else {
                this.log.info("没有找到用户Id为：" + userId + "的客户群！");
            }
        } catch (IOException var6) {
            this.log.error("转换成JSON格式错误！", var6);
        }

        this.log.info("mcd webservice invoke method getCustomersList end" + str);
        return str;
    }

    public String getTargetCustomersObj(String id) {
        this.log.info("mcd webservice invoke method getTargetCustomersObj start");
        this.log.debug("param is : " + id);
        TargetCustomersModel targetCustomersModel = this.iMcdInterfaceJDao.getCustomerById(id);
        if(targetCustomersModel != null) {
            List str = this.iMcdInterfaceJDao.getTargetCustomersAttr(id);
            Object e = new ArrayList();
            if((str == null || str.size() != 1 || str.get(0) == null || ((TargetCustomersAttr)str.get(0)).getStatus() == null || ((TargetCustomersAttr)str.get(0)).getStatus().intValue() != 2) && str != null) {
                e = str;
            }

            targetCustomersModel.setTarget_customers_attr((List)e);
        }

        String str1 = "";

        try {
            str1 = JsonUtil.toJson(targetCustomersModel, this.dateFormat);
        } catch (IOException var5) {
            this.log.error("转换成JSON格式错误！", var5);
        }

        this.log.info("mcd webservice invoke method getTargetCustomersObj end" + str1);
        return str1;
    }

    public String getTargetCustomersCycleObj(String id, String date) {
        this.log.info("mcd webservice invoke method getTargetCustomersObj start");
        this.log.debug("param is : id=" + id + ",date=" + date);
        TargetCustomersModel targetCustomersModel = null;
        if(StringUtil.isNotEmpty(date)) {
            targetCustomersModel = this.iMcdInterfaceJDao.getCustomerById(id, date);
        }

        if(targetCustomersModel != null && StringUtil.isNotEmpty(targetCustomersModel.getTarget_customers_tab_name())) {
            Boolean str = Boolean.valueOf(this.iMcdInterfaceJDao.getTableExists(targetCustomersModel.getTarget_customers_tab_name()));
            if(!str.booleanValue()) {
                targetCustomersModel = null;
            }
        }

        if(targetCustomersModel == null || StringUtil.isEmpty(targetCustomersModel.getTarget_customers_tab_name())) {
            this.log.debug("param is : id=" + id + ",清单表为空");
            targetCustomersModel = this.iMcdInterfaceJDao.getCustomerById(id);
        }

        if(targetCustomersModel != null) {
            if(StringUtil.isEmpty(targetCustomersModel.getTarget_customers_base_month())) {
                targetCustomersModel.setTarget_customers_base_month("");
            }

            if(StringUtil.isEmpty(targetCustomersModel.getTarget_customers_tab_name())) {
                targetCustomersModel.setTarget_customers_tab_name("");
            }

            targetCustomersModel.setTarget_customers_attr(this.iMcdInterfaceJDao.getTargetCustomersAttr(id));
        }

        String str1 = "";

        try {
            str1 = JsonUtil.toJson(targetCustomersModel, this.dateFormat);
        } catch (IOException var6) {
            this.log.error("转换成JSON格式错误！", var6);
        }

        this.log.info("mcd webservice invoke method getTargetCustomersCycleObj end" + str1);
        return str1;
    }

    public IMcdInterfaceJDao getiMcdInterfaceJDao() {
        return this.iMcdInterfaceJDao;
    }

    public void setiMcdInterfaceJDao(IMcdInterfaceJDao iMcdInterfaceJDao) {
        this.iMcdInterfaceJDao = iMcdInterfaceJDao;
    }

    public String sendCustGroupContentReq(String custGroupId, String contentType, String custGroupDate, String labelInfoCategoryId) {
        Boolean result = Boolean.valueOf(true);
        SendCustGroupContentThread thread = null;

        try {
            thread = (SendCustGroupContentThread)SystemServiceLocator.getInstance().getService("sendCustGroupContentThread");
            thread.setCustGroupId(custGroupId);
            thread.setContentType(contentType);
            thread.setCustGroupDate(custGroupDate);
            thread.setLabelInfoCategoryId(labelInfoCategoryId);
            ThreadPool.getInstance().execute(thread);
        } catch (Exception var8) {
            result = Boolean.valueOf(false);
            this.log.error("线程池异常", var8);
        }

        this.log.info("mcd webservice invoke method sendCustGroupContentReq end");
        return result.toString();
    }

    public String get(String custGroupId, String contentType, String custGroupDate) {
        this.log.info("custGroupId===" + custGroupId + "custGroupDate===" + custGroupDate);
        TargetCustomersModel targetCustomersModel = null;
        if(StringUtil.isNotEmpty(custGroupDate)) {
            targetCustomersModel = this.iMcdInterfaceJDao.getCustomerById(custGroupId, custGroupDate);
        }

        if(targetCustomersModel != null && StringUtil.isNotEmpty(targetCustomersModel.getTarget_customers_tab_name())) {
            Boolean result = Boolean.valueOf(this.iMcdInterfaceJDao.getTableExists(targetCustomersModel.getTarget_customers_tab_name()));
            if(!result.booleanValue()) {
                targetCustomersModel = null;
            }
        }

        if(targetCustomersModel == null || StringUtil.isEmpty(targetCustomersModel.getTarget_customers_tab_name())) {
            this.log.debug("param is : id=" + custGroupId + ",清单表为空");
            targetCustomersModel = this.iMcdInterfaceJDao.getCustomerById(custGroupId);
        }

        if(targetCustomersModel != null) {
            if(StringUtil.isEmpty(targetCustomersModel.getFlag())) {
                targetCustomersModel.setFlag(ServiceConstants.ISSTRENGTHEN_ON.toString());
            }

            List result1 = this.iMcdInterfaceJDao.getTargetCustomersAttr(custGroupId);
            Object e = new ArrayList();
            if((result1 == null || result1.size() != 1 || result1.get(0) == null || ((TargetCustomersAttr)result1.get(0)).getStatus() == null || ((TargetCustomersAttr)result1.get(0)).getStatus().intValue() != 2) && result1 != null) {
                e = result1;
            }

            TargetCustomersAttr targetCustomersAttrLabelName = new TargetCustomersAttr();
            targetCustomersAttrLabelName.setName("偏好标签");
            targetCustomersAttrLabelName.setCode("labelName");
            targetCustomersAttrLabelName.setType("String");
            targetCustomersAttrLabelName.setLength("128");
            ((List)e).add(targetCustomersAttrLabelName);
            TargetCustomersAttr targetCustomersAttr = new TargetCustomersAttr();
            targetCustomersAttr.setName("内容偏好编码");
            targetCustomersAttr.setCode("contentId");
            targetCustomersAttr.setType("String");
            targetCustomersAttr.setLength("32");
            ((List)e).add(targetCustomersAttr);
            targetCustomersModel.setTarget_customers_attr((List)e);
        } else {
            targetCustomersModel = new TargetCustomersModel();
            targetCustomersModel.setFlag(ServiceConstants.ISSTRENGTHEN_ON.toString());
            targetCustomersModel.setTarget_customers_id(custGroupId);
            targetCustomersModel.setCustomers_desc("根据条件没有查询出符合要求的客户群");
            this.log.info("=================根据条件没有查询出符合要求的客户群");
        }

        String result2 = "";

        try {
            result2 = JsonUtil.toJson(targetCustomersModel, this.dateFormat);
            this.log.info("查询结果:" + result2);
        } catch (IOException var9) {
            this.log.error("转换成JSON格式错误！", var9);
        }

        this.log.info("mcd webservice invoke method get end" + result2);
        return result2;
    }

    public String getCiLabelInfoCategoryList() {
        String json = "";
        new ArrayList();
        CiLabelInfo searchConditionInfo = new CiLabelInfo();
        String parentIds = Configure.getInstance().getProperty("LABEL_PATENTIDS");
        String labelIdLevelDesc = "";

        List list;
        try {
            if(StringUtil.isNotEmpty(parentIds)) {
                CiLabelInfo e = this.ciLabelInfoJDao.selectCiLabelInfoByLabelId(Integer.valueOf(Integer.parseInt(parentIds)));
                if(e != null) {
                    labelIdLevelDesc = e.getLabelIdLevelDesc() + parentIds + "/";
                }
            }

            searchConditionInfo.setIsStatUserNum(String.valueOf(0));
            searchConditionInfo.setDataStatusId(Integer.valueOf(2));
            searchConditionInfo.setLabelIdLevelDesc(labelIdLevelDesc);
            list = this.ciLabelInfoJDao.getPageListBySql(1, 10000, searchConditionInfo, "1");
        } catch (Exception var8) {
            this.log.error("查询分页记录失败", var8);
            throw new CIServiceException("查询分页记录失败");
        }

        try {
            json = JsonUtil.toJson(list, this.dateFormat);
        } catch (IOException var7) {
            this.log.error("转换成JSON格式错误！", var7);
        }

        return json;
    }

    public static void main(String[] args) throws Exception {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient("http://10.1.48.22:8080/coc/services/wsMcdInterfaceServer?wsdl");
        Object[] obj = client.invoke("sendCustGroupContentReq", new Object[]{"KHQ000002701", "", "201307", "3777"});
        System.out.println(obj[0]);
    }
}
