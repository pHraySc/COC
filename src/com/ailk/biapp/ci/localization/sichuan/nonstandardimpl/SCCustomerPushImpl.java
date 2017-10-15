package com.ailk.biapp.ci.localization.sichuan.nonstandardimpl;

import com.ailk.biapp.ci.dao.ICiGroupAttrRelJDao;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiCustomPushReq;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.entity.CiSysInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.localization.nonstandard.ICustomerPush;
import com.ailk.biapp.ci.model.StandardPushXmlBean;
import com.ailk.biapp.ci.model.StandardPushXmlBean.Data;
import com.ailk.biapp.ci.model.StandardPushXmlBean.Title;
import com.ailk.biapp.ci.model.StandardPushXmlBean.Data.Column;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.Bean2XMLUtils;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("scCustomerPushImpl")
@Scope("prototype")
@Transactional
public class SCCustomerPushImpl implements ICustomerPush {
    private static Logger log = Logger.getLogger(SCCustomerPushImpl.class);
    private CiSysInfo sysInfo;
    private CiCustomGroupInfo ciCustomGroupInfo;
    private CiCustomListInfo ciCustomListInfo;
    private CiCustomPushReq ciCustomPushReq;
    @Autowired
    private ICustomersManagerService customersManagerService;
    @Autowired
    private ICiGroupAttrRelJDao ciGroupAttrRelJDao;

    public SCCustomerPushImpl() {
    }

    public boolean push(CiCustomGroupInfo ciCustomGroupInfo, CiCustomListInfo ciCustomListInfo, CiSysInfo sysInfo) {
        boolean flag = false;
        long t1 = System.currentTimeMillis();
        this.ciCustomGroupInfo = ciCustomGroupInfo;
        this.ciCustomListInfo = ciCustomListInfo;
        this.sysInfo = sysInfo;
        this.ciCustomPushReq = this.customersManagerService.queryCiCustomPushReqById(sysInfo.getReqId());

        boolean resultAttrs;
        try {
            List e = null;

            try {
                e = this.ciGroupAttrRelJDao.selectCiGroupAttrRelList(ciCustomGroupInfo.getCustomGroupId(), ciCustomListInfo.getDataTime());
            } catch (Exception var15) {
                log.error("查询客户群属性表错误！", var15);
            }

            ArrayList resultAttrs1 = new ArrayList();
            if(e != null && e.size() > 0 && (e.size() != 1 || e.get(0) == null || ((CiGroupAttrRel)e.get(0)).getStatus() == null || ((CiGroupAttrRel)e.get(0)).getStatus().intValue() != 2)) {
                Iterator i$ = e.iterator();

                while(i$.hasNext()) {
                    CiGroupAttrRel rel = (CiGroupAttrRel)i$.next();
                    resultAttrs1.add(rel);
                }
            }

            ciCustomGroupInfo.setListTableName(ciCustomListInfo.getListTableName());
            flag = this.createCustomerGroup(ciCustomGroupInfo.getCreateUserId(), this.ciCustomPushReq, resultAttrs1).booleanValue();
            if(flag) {
                this.ciCustomPushReq.setExeInfo("推送成功");
                this.delNormal(3);
            } else {
                this.ciCustomPushReq.setExeInfo("推送失败,已经推送过客户群【" + ciCustomGroupInfo.getCustomGroupName() + "】。");
                this.delNormal(0);
            }

            boolean i$1 = flag;
            return i$1;
        } catch (Exception var16) {
            log.error("推送失败异常 error:", var16);
            this.delException("推送失败异常", var16);
            resultAttrs = false;
        } finally {
            log.info("The cost of publishToCm is :  " + (System.currentTimeMillis() - t1) + "ms");
        }

        return resultAttrs;
    }

    private void delNormal(int status) {
        this.ciCustomPushReq.setStatus(Integer.valueOf(status));
        this.customersManagerService.saveCiCustomPushReq(this.ciCustomPushReq);
    }

    private void delException(String msg, Exception e) {
        this.ciCustomPushReq.setStatus(Integer.valueOf(0));
        String expctionInfo = "";

        try {
            StringWriter e2 = new StringWriter();
            PrintWriter pw = new PrintWriter(e2);
            e.printStackTrace(pw);
            expctionInfo = e2.toString();
        } catch (Exception var6) {
            log.error("异常情况后处理错误", var6);
        }

        expctionInfo = msg + expctionInfo;
        if(expctionInfo.length() > 2048) {
            expctionInfo = expctionInfo.substring(0, 1024);
        }

        this.ciCustomPushReq.setExeInfo(expctionInfo);
        this.customersManagerService.saveCiCustomPushReq(this.ciCustomPushReq);
    }

    public boolean isExistsSame() {
        CiCustomPushReq queryParam = new CiCustomPushReq();
        queryParam.setUserId(this.ciCustomGroupInfo.getCreateUserId());
        queryParam.setSysId(this.sysInfo.getSysId());
        queryParam.setListTableName(this.ciCustomListInfo.getListTableName());
        queryParam.setStatus(Integer.valueOf(3));
        List pushreq = this.customersManagerService.queryCiCustomPushReq(queryParam);
        return pushreq != null && pushreq.size() > 0;
    }

    public Boolean createCustomerGroup(String userId, CiCustomPushReq ciCustomPushReq, List<CiGroupAttrRel> resultAttrs) {
        Boolean result = Boolean.valueOf(false);

        try {
            Object[] e = new Object[1];
            StandardPushXmlBean bean = this.createStandardPushXmlBean(userId, ciCustomPushReq, resultAttrs);
            String pushXmlBody = Bean2XMLUtils.bean2XmlString(bean);
            log.debug("StandardPushXml : " + pushXmlBody);
            e[0] = pushXmlBody;
            Object[] res = callWebService("http://localhost:8080/coc/services/wsCustomerGroupCreateServer?wsdl", this.sysInfo.getWebserviceTargetNamespace(), this.sysInfo.getWebserviceMethod(), e);
            result = Boolean.valueOf(res[0].toString());
            return result;
        } catch (Exception var9) {
            log.error("调用四川推送webservice接口错误============", var9);
            this.delException("调用四川推送webservice接口错误：", var9);
            return Boolean.valueOf(false);
        }
    }

    private static Object[] callWebService(String wsdl, String targetNamespace, String methodName, Object[] args) {
        try {
            JaxWsDynamicClientFactory e = JaxWsDynamicClientFactory.newInstance();
            Client client = e.createClient(wsdl);
            Object[] res = null;
            if(targetNamespace == null) {
                res = client.invoke(methodName, args);
            } else {
                res = client.invoke(new QName(targetNamespace, methodName), args);
            }

            return res;
        } catch (Exception var7) {
            var7.printStackTrace();
            log.error("动态调用webservice 失败,wsdl:" + wsdl + ",targetNamespace:" + targetNamespace + ",methodName:" + methodName + ",args:" + args, var7);
            throw new CIServiceException("动态调用webservice 失败");
        }
    }

    private StandardPushXmlBean createStandardPushXmlBean(String userId, CiCustomPushReq ciCustomPushReq, List<CiGroupAttrRel> resultAttrs) {
        StandardPushXmlBean bean = new StandardPushXmlBean();
        Title title = bean.getTitle();
        title.setTaskDesc(this.sysInfo.getSysName() + "推送任务");
        title.setSendTime(new Date());
        Data data = bean.getData();
        data.setReqId(ciCustomPushReq.getReqId());
        data.setPlatformCode("COC");
        data.setUserId(userId);
        data.setUploadFileName(ciCustomPushReq.getListTableName());
        data.setCreateCityId(this.ciCustomGroupInfo.getCreateCityId());
        String crtPersnName = this.ciCustomGroupInfo.getCreateUserName();
        data.setCrtPersnName(crtPersnName == null?"":crtPersnName);
        Date crtTime = this.ciCustomGroupInfo.getCreateTime();
        data.setCrtTime(crtTime);
        Date effective_time = this.ciCustomGroupInfo.getStartDate();
        data.setEffective_time(effective_time);
        Date fail_time = this.ciCustomGroupInfo.getEndDate();
        data.setFail_time(fail_time);
        Long rowNumber = this.ciCustomGroupInfo.getCustomNum();
        data.setRowNumber(rowNumber);
        int dataCycle = this.ciCustomGroupInfo.getUpdateCycle().intValue();
        data.setDataCycle(Integer.valueOf(dataCycle));
        String dataCycleDesc = "";
        if(1 == dataCycle) {
            dataCycleDesc = "一次性";
        } else if(2 == dataCycle) {
            dataCycleDesc = "月周期";
        } else if(3 == dataCycle) {
            dataCycleDesc = "日周期";
        }

        data.setDataCycleDesc(dataCycleDesc);
        data.setCustomGroupId(this.ciCustomGroupInfo.getCustomGroupId());
        data.setCustomGroupName(this.ciCustomGroupInfo.getCustomGroupName());
        data.setCustomGroupDesc(this.ciCustomGroupInfo.getCustomGroupDesc());
        String dataDateStr = this.ciCustomGroupInfo.getDataDate();
        if(this.ciCustomGroupInfo.getUpdateCycle().intValue() == 1 || this.ciCustomGroupInfo.getUpdateCycle().intValue() == 3) {
            if(StringUtil.isNotEmpty(this.ciCustomGroupInfo.getDayLabelDate())) {
                dataDateStr = this.ciCustomGroupInfo.getDayLabelDate();
            } else {
                dataDateStr = CacheBase.getInstance().getNewLabelDay();
            }
        }

        if(this.ciCustomGroupInfo.getUpdateCycle().intValue() == 2) {
            if(StringUtil.isNotEmpty(this.ciCustomGroupInfo.getMonthLabelDate())) {
                dataDateStr = this.ciCustomGroupInfo.getMonthLabelDate();
            } else {
                dataDateStr = CacheBase.getInstance().getNewLabelMonth();
            }
        }

        data.setDataDate(dataDateStr);
        ArrayList columns = new ArrayList();
        Iterator i$ = resultAttrs.iterator();

        while(i$.hasNext()) {
            CiGroupAttrRel rel = (CiGroupAttrRel)i$.next();
            Column column = data.newColumn();
            column.setColumnName(rel.getId().getAttrCol());
            column.setColumnCnName(rel.getAttrColName());
            column.setColumnDataType(rel.getAttrColType());
            column.setIsPrimaryKey(Integer.valueOf(0));
            columns.add(column);
        }

        data.setColumns(columns);
        return bean;
    }
}
