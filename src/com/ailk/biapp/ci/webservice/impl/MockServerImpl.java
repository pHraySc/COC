package com.ailk.biapp.ci.webservice.impl;

import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.webservice.MockServer;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import javax.jws.WebService;
import org.apache.cxf.feature.Features;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@WebService(
    endpointInterface = "com.ailk.biapp.ci.webservice.MockServer"
)
@Features(
    features = {"org.apache.cxf.feature.LoggingFeature"}
)
public class MockServerImpl implements MockServer {
    private Logger log = Logger.getLogger(MockServerImpl.class);

    public MockServerImpl() {
    }

    public String getDataFromCOC(String xmlBody) {
        this.log.info("从COC接收XML数据");
        this.log.info("webservice传递的参数XML ： " + xmlBody);
        String interfaceAddress = null;
        String sendTime = null;
        String syncTaskId = null;
        String returnCode = null;
        String errorMsg = null;

        try {
            SAXReader returnMap = new SAXReader();
            Document json = returnMap.read(new StringReader(xmlBody));
            Element e = json.getRootElement();
            Element title = e.element("Title");
            interfaceAddress = title.elementTextTrim("InterfaceAddress");
            sendTime = title.elementTextTrim("SendTime");
            Element data = e.element("Data");
            syncTaskId = data.elementTextTrim("SyncTaskId");
            returnCode = data.elementTextTrim("ReturnCode");
            errorMsg = data.elementTextTrim("ErrorMsg");
        } catch (DocumentException var13) {
            this.log.error("解析XML错误", var13);
            returnCode = "0";
            var13.printStackTrace();
        }

        returnCode = "1";
        HashMap returnMap1 = new HashMap();
        returnMap1.put("interfaceAddress", interfaceAddress);
        returnMap1.put("sendTime", sendTime);
        returnMap1.put("syncTaskId", syncTaskId);
        returnMap1.put("returnCode", returnCode);
        returnMap1.put("errorMsg", errorMsg);
        String json1 = "";

        try {
            json1 = JsonUtil.toJson(returnMap1);
        } catch (IOException var12) {
            this.log.error("json解析错误", var12);
            returnCode = "0";
            var12.printStackTrace();
        }

        System.out.println(json1);
        return json1;
    }
}
