package com.ailk.biapp.ci.webservice.impl;

import com.ailk.biapp.ci.webservice.IWsCustomersCreateJobClient;
import com.asiainfo.biframe.utils.string.StringUtil;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceClient;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@WebServiceClient
public class WsCustomersCreateJobClientImpl implements IWsCustomersCreateJobClient {
    private Logger log = Logger.getLogger(this.getClass());

    public WsCustomersCreateJobClientImpl() {
    }

    public void notifyCustomerClientJob(String wsdl, String targetNamespace, String methodName, int isExeTask, int customCycleType, String cityIds) {
        this.log.info("Enter CustomersCreateJobClientImpl.notifyCustomerClientJob()");
        if(StringUtil.isEmpty(wsdl)) {
            this.log.error("webservice的WebserviceWSDL配置为空");
        } else if(StringUtil.isEmpty(methodName)) {
            this.log.error("webservice的WebserviceMethod配置为空");
        } else {
            try {
                this.callWebService(wsdl, targetNamespace, methodName, isExeTask, customCycleType, cityIds);
            } catch (Exception var8) {
                this.log.debug("webservice超时");
            }

            this.log.info("Exit CustomersCreateJobClientImpl.notifyCustomerClientJob()");
        }
    }

    private void callWebService(String wsdl, String targetNamespace, String methodName, int isExeTask, int customCycleType, String cityIds) {
        try {
            JaxWsDynamicClientFactory e = JaxWsDynamicClientFactory.newInstance();
            Client client = e.createClient(wsdl);
            if(targetNamespace == null) {
                client.invoke(methodName, new Object[]{Integer.valueOf(isExeTask), Integer.valueOf(customCycleType), cityIds});
            } else {
                client.invoke(new QName(targetNamespace, methodName), new Object[]{Integer.valueOf(isExeTask), Integer.valueOf(customCycleType), cityIds});
            }
        } catch (Exception var9) {
            this.log.debug("响应超时");
        }

    }
}
