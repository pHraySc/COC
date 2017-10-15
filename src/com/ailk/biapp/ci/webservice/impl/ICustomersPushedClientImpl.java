package com.ailk.biapp.ci.webservice.impl;

import com.ailk.biapp.ci.dao.ICiSysInfoHDao;
import com.ailk.biapp.ci.entity.CiSysInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.ReturnMessageModel;
import com.ailk.biapp.ci.webservice.ICustomersPushedClient;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceClient;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@WebServiceClient
public class ICustomersPushedClientImpl implements ICustomersPushedClient {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ICiSysInfoHDao ciSysInfoHDao;

    public ICustomersPushedClientImpl() {
    }

    public String sendDataToSA(String xmlBody) {
        this.log.info("Enter ICustomersPushedClientImpl.sendDataToSA()");
        String sysId = Configure.getInstance().getProperty("SA_FOR_MCD");
        if(StringUtil.isEmpty(sysId)) {
            this.log.error("webservice系统id为空");
            return null;
        } else {
            CiSysInfo ciSysInfo = this.ciSysInfoHDao.selectById(sysId);
            String wsdl = ciSysInfo.getWebserviceWSDL();
            String targetNamespace = ciSysInfo.getWebserviceTargetNamespace();
            String methodName = ciSysInfo.getWebserviceMethod();
            if(StringUtil.isEmpty(wsdl)) {
                this.log.error("webservice的WebserviceWSDL配置为空");
                return null;
            } else if(StringUtil.isEmpty(targetNamespace)) {
                this.log.error("webservice的WebserviceTargetNamespace配置为空");
                return null;
            } else if(StringUtil.isEmpty(methodName)) {
                this.log.error("webservice的WebserviceMethod配置为空");
                return null;
            } else {
                Object[] args = new Object[]{xmlBody};
                Object[] res = this.callWebService(wsdl, targetNamespace, methodName, args);
                this.log.info("Exit WsIndexDifferentialClientImpl.sendDataToSA()");
                return (String)res[0];
            }
        }
    }

    private Object[] callWebService(String wsdl, String targetNamespace, String methodName, Object[] args) {
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
        } catch (Exception var8) {
            this.log.error("动态调用webservice 失败,wsdl:" + wsdl + ",targetNamespace:" + targetNamespace + ",methodName:" + methodName + ",args:" + args, var8);
            throw new CIServiceException("动态调用webservice 失败");
        }
    }

    public static String createXmlBody(ReturnMessageModel returnMessageModel) {
        String xmlBody = null;
        StringBuffer sb = new StringBuffer();
        if(returnMessageModel != null && StringUtil.isNotEmpty(returnMessageModel.getSyncTaskId())) {
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            sb.append("<BODY>");
            sb.append("<Title>");
            sb.append("<InterfaceAddress>");
            sb.append(returnMessageModel.getInterfaceAddress());
            sb.append("</InterfaceAddress>");
            sb.append("<SendTime>");
            sb.append(returnMessageModel.getSendTime());
            sb.append("</SendTime>");
            sb.append("</Title>");
            sb.append("<Data>");
            sb.append("<SyncTaskId>");
            sb.append(returnMessageModel.getSyncTaskId());
            sb.append("</SyncTaskId>");
            sb.append("<ReturnCode>");
            sb.append(returnMessageModel.getReturnCode());
            sb.append("</ReturnCode>");
            sb.append("<ErrorMsg>");
            sb.append(returnMessageModel.getErrorMsg());
            sb.append("</ErrorMsg>");
            sb.append("</Data>");
            sb.append("</BODY>");
        }

        xmlBody = sb.toString();
        System.out.println("发送的消息体为：" + xmlBody);
        return xmlBody;
    }

    public static void main(String[] args) {
        String syncTaskId = "SYNC1348207541730";
        String returnCode = "1";
        String errorMsg = "no error";
        ReturnMessageModel returnMessageModel = new ReturnMessageModel();
        returnMessageModel.setSyncTaskId(syncTaskId);
        String interfaceAddressForSA = "http://1.1.1.1:8088/GetSyncResultService";
        returnMessageModel.setInterfaceAddress(interfaceAddressForSA);
        returnMessageModel.setErrorMsg(errorMsg);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        returnMessageModel.setReturnCode(returnCode);
        returnMessageModel.setErrorMsg("");
        String sendTime = sf.format(new Date());
        returnMessageModel.setSendTime(sendTime);
        String xmlBody = createXmlBody(returnMessageModel);
        ICustomersPushedClientImpl client = new ICustomersPushedClientImpl();
        String res = client.sendDataToSA(xmlBody);
        System.out.println(res);
    }
}
