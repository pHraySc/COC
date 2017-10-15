package com.ailk.biapp.ci.webservice.impl;

import com.ailk.biapp.ci.entity.CiCustomCampsegRel;
import com.ailk.biapp.ci.entity.CiCustomCampsegRelId;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.webservice.ICustomerMarketCampaignWsServer;
import java.io.StringReader;
import javax.jws.WebService;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.feature.Features;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@WebService(
    endpointInterface = "com.ailk.biapp.ci.webservice.ICustomerMarketCampaignWsServer",
    targetNamespace = "http://webservice.ci.biapp.ailk.com/"
)
@Features(
    features = {"org.apache.cxf.feature.LoggingFeature"}
)
public class CustomerMarketCampaignWsServerImpl implements ICustomerMarketCampaignWsServer {
    @Autowired
    private ICustomersManagerService customersService;
    private static Logger log = Logger.getLogger(CustomerMarketCampaignWsServerImpl.class);

    public CustomerMarketCampaignWsServerImpl() {
    }

    public String sendCustomerMarketCampaignInfo(String infoXml) {
        log.info("Enter webservice CustomerMarketCampaignWsServerImpl.sendCustomerMarketCampaignInfo");
        log.debug("infoXml:" + infoXml);
        String campId = "";
        String campName = "";
        String customGroupId = "";
        String elementName = "";
        String campsegCreateUser = "";
        String approveEndTime = "";

        try {
            SAXReader listInfo = new SAXReader();
            Document gourpInfo = listInfo.read(new StringReader(infoXml));
            Element relId = gourpInfo.getRootElement();
            relId = relId.element("CreateCampaign");
            elementName = "CampsegId";
            Element rel = relId.element(elementName);
            campId = rel.getText();
            elementName = "CampsegName";
            Element campsegNameElement = relId.element(elementName);
            campName = campsegNameElement.getText();
            elementName = "CustgroupId";
            Element customGroupIdElement = relId.element(elementName);
            customGroupId = customGroupIdElement.getText();
            elementName = "CampsegCreateUser";
            Element campsegCreateUserElement = relId.element(elementName);
            campsegCreateUser = campsegCreateUserElement.getText();
            elementName = "ApproveEndTime";
            Element approveEndTimeElement = relId.element(elementName);
            approveEndTime = approveEndTimeElement.getText();
        } catch (Exception var16) {
            log.error("read xml Error,elementName:" + elementName + ",infoXml: " + infoXml, var16);
            return "0";
        }

        if(StringUtils.isEmpty(customGroupId)) {
            log.error("客户群id为空，" + customGroupId);
            return "-1";
        } else {
            CiCustomListInfo listInfo1 = this.customersService.queryCiCustomListInfoById(customGroupId);
            if(listInfo1 == null) {
                log.error("清单id对应的清单信息不存在，" + customGroupId);
                return "-2";
            } else {
                CiCustomGroupInfo gourpInfo1 = this.customersService.queryCiCustomGroupInfo(listInfo1.getCustomGroupId());
                if(gourpInfo1 == null) {
                    log.error("客户群不存在，" + listInfo1.getCustomGroupId());
                    return "-3";
                } else if(StringUtils.isEmpty(campId)) {
                    log.error("营销活动id为空，" + campId);
                    return "-4";
                } else if(StringUtils.isEmpty(campName)) {
                    log.error("营销活动名称为空，" + campName);
                    return "-5";
                } else if(StringUtils.isEmpty(campsegCreateUser)) {
                    log.error("营销活动创建人id为空，" + campsegCreateUser);
                    return "-6";
                } else {
                    CiCustomCampsegRelId relId1 = new CiCustomCampsegRelId();
                    CiCustomCampsegRel rel1 = new CiCustomCampsegRel();
                    relId1.setCampsegId(campId);
                    relId1.setCustomGroupId(customGroupId);
                    rel1.setRelId(relId1);
                    rel1.setCampsegCreateUser(gourpInfo1.getCreateUserId());
                    rel1.setCampsegName(campName);
                    if(StringUtils.isNotEmpty(approveEndTime)) {
                        rel1.setApproveEndTime(DateUtil.string2Date(approveEndTime, "yyyy-MM-dd HH:mm:ss"));
                    }

                    this.customersService.saveCiCustomCampsegRel(rel1);
                    log.info("Exit webservice CustomerMarketCampaignWsServerImpl.sendCustomerMarketCampaignInfo success(1)");
                    return "1";
                }
            }
        }
    }

    public static void main(String[] args) {
    }
}
