package com.ailk.biapp.ci.webservice.impl;

import com.ailk.biapp.ci.webservice.IWsSendNoticeToVgopClient;
import com.asiainfo.biframe.utils.string.StringUtil;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceClient;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@WebServiceClient
public class WsSendNoticeToVgopClientImpl implements IWsSendNoticeToVgopClient {
    private Logger log = Logger.getLogger(this.getClass());

    public WsSendNoticeToVgopClientImpl() {
    }

    public void sendNotice(String wsdl, String targetNamespace, String methodName, String customGroupId, String customNum, String status) {
        this.log.info("=============>>>> ��vgopϵͳ��֪ͨ��ʼ");
        this.log.info("customGroupId:" + customGroupId + "customNum:" + customNum + "status:" + status);
        if(StringUtil.isEmpty(wsdl)) {
            this.log.error("webservice��WebserviceWSDL����Ϊ��");
        } else if(StringUtil.isEmpty(methodName)) {
            this.log.error("webservice��WebserviceMethod����Ϊ��");
        } else {
            try {
                this.callNoticeWebService(wsdl, targetNamespace, methodName, customGroupId, customNum, status);
            } catch (Exception var8) {
                this.log.debug("webservice��ʱ");
            }

            this.log.info("=============>>>> ��vgopϵͳ��֪ͨ����");
        }
    }

    public void sendPushNotice(String wsdl, String targetNamespace, String methodName, String customGroupId, String clwxCustomGroupId, String customNum, String status) {
        this.log.info("=============>>>> ���������vgopϵͳ��֪ͨ��ʼ");
        this.log.info("COC customGroupId:" + customGroupId + ";CLWX customGroupId:" + clwxCustomGroupId + ";custom number:" + customNum + ";status:" + status);
        if(StringUtil.isEmpty(wsdl)) {
            this.log.error("webservice��WebserviceWSDL����Ϊ��");
        } else if(StringUtil.isEmpty(methodName)) {
            this.log.error("webservice��WebserviceMethod����Ϊ��");
        } else {
            try {
                this.callPushNoticeWebService(wsdl, targetNamespace, methodName, customGroupId, clwxCustomGroupId, customNum, status);
            } catch (Exception var9) {
                this.log.debug("webservice��ʱ");
            }

            this.log.info("=============>>>> ���������vgopϵͳ��֪ͨ����");
        }
    }

    private void callNoticeWebService(String wsdl, String targetNamespace, String methodName, String customGroupId, String customNum, String status) {
        try {
            JaxWsDynamicClientFactory e = JaxWsDynamicClientFactory.newInstance();
            Client client = e.createClient(wsdl);
            if(targetNamespace == null) {
                client.invoke(methodName, new Object[]{customGroupId, customNum, status});
            } else {
                client.invoke(new QName(targetNamespace, methodName), new Object[]{customGroupId, customNum, status});
            }
        } catch (Exception var9) {
            this.log.debug("��Ӧ��ʱ");
        }

    }

    private void callPushNoticeWebService(String wsdl, String targetNamespace, String methodName, String customGroupId, String clwxCustomGroupId, String customNum, String status) {
        try {
            JaxWsDynamicClientFactory e = JaxWsDynamicClientFactory.newInstance();
            Client client = e.createClient(wsdl);
            if(targetNamespace == null) {
                client.invoke(methodName, new Object[]{customGroupId, clwxCustomGroupId, customNum, status});
            } else {
                client.invoke(new QName(targetNamespace, methodName), new Object[]{customGroupId, clwxCustomGroupId, customNum, status});
            }
        } catch (Exception var10) {
            this.log.debug("����֪ͨ��Ӧ��ʱ");
        }

    }
}
