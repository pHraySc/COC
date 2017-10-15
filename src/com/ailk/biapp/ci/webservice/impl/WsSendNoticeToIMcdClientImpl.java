package com.ailk.biapp.ci.webservice.impl;

import com.ailk.biapp.ci.webservice.IWsSendNoticeToIMcdClient;
import com.asiainfo.biframe.utils.string.StringUtil;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceClient;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@WebServiceClient
public class WsSendNoticeToIMcdClientImpl implements IWsSendNoticeToIMcdClient {
    private Logger log = Logger.getLogger(this.getClass());

    public WsSendNoticeToIMcdClientImpl() {
    }

    public void sendNotice(String wsdl, String targetNamespace, String methodName, String systemId, String custGroupId, String dataDate) {
        this.log.info("=============>>>> ��imcdϵͳ��֪ͨ��ʼ");
        this.log.info("systemId:" + systemId + "custGroupId:" + custGroupId);
        if(StringUtil.isEmpty(wsdl)) {
            this.log.error("webservice��WebserviceWSDL����Ϊ��");
        } else if(StringUtil.isEmpty(methodName)) {
            this.log.error("webservice��WebserviceMethod����Ϊ��");
        } else {
            try {
                this.callWebService(wsdl, targetNamespace, methodName, systemId, custGroupId, dataDate);
            } catch (Exception var8) {
                this.log.debug("webservice��ʱ");
            }

            this.log.info("=============>>>> ��imcdϵͳ��֪ͨ����");
        }
    }

    private void callWebService(String wsdl, String targetNamespace, String methodName, String systemId, String custGroupId, String dataDate) {
        try {
            JaxWsDynamicClientFactory e = JaxWsDynamicClientFactory.newInstance();
            Client client = e.createClient(wsdl);
            if(targetNamespace == null) {
                client.invoke(methodName, new Object[]{systemId, custGroupId, dataDate});
            } else {
                client.invoke(new QName(targetNamespace, methodName), new Object[]{systemId, custGroupId, dataDate});
            }
        } catch (Exception var9) {
            this.log.debug("��Ӧ��ʱ");
        }

    }
}
