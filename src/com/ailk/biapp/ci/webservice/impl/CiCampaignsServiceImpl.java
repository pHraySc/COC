package com.ailk.biapp.ci.webservice.impl;

import com.ailk.biapp.ci.dataservice.dao.ICiCampaignsJDao;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.webservice.ICiCampaignsService;
import com.mongodb.BasicDBObject;
import java.util.ArrayList;
import java.util.Iterator;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.apache.log4j.Logger;
import org.bson.BSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@WebService(
    endpointInterface = "com.ailk.biapp.ci.webservice.ICiCampaignsService",
    name = "ws_cus_mkt_act",
    serviceName = "ws_cus_mkt_act"
)
public class CiCampaignsServiceImpl implements ICiCampaignsService {
    private final Logger log = Logger.getLogger(CiCampaignsServiceImpl.class);
    @Autowired
    private ICiCampaignsJDao ciCampaignsJDao;

    public CiCampaignsServiceImpl() {
    }

    @WebMethod(
        operationName = "getCampaigns"
    )
    public String getCampaigns(@WebParam(name = "sysCode") String sysCode, @WebParam(name = "channelId") String channelId, @WebParam(name = "accNbr") String accNbr, @WebParam(name = "staffCode") String staffCode, @WebParam(name = "acpId") String acpId) throws CIServiceException {
        this.log.info("[crm webservice input]:" + sysCode + "," + channelId + "," + accNbr + "," + staffCode + "," + acpId);
        BasicDBObject result = new BasicDBObject();

        try {
            result.append("accNbr", accNbr);
            BSONObject e = this.ciCampaignsJDao.getCampaignsByMobile(accNbr);
            if(e == null) {
                result.append("result", "1");
                result.append("resultMsg", "客户不存在");
                this.log.info("[crm webservice output]:" + result.toString());
                return result.toString();
            }

            ArrayList list = (ArrayList)e.get("campaigns");
            ArrayList campaignList = new ArrayList();
            Iterator i$ = list.iterator();

            while(i$.hasNext()) {
                String o = (String)i$.next();
                BasicDBObject basicDbObj = new BasicDBObject();
                basicDbObj.append("campaign_id", o);
                basicDbObj.append("recomm_reason", "");
                campaignList.add(basicDbObj);
            }

            result.append("campaigns", campaignList);
            result.append("result", "0");
            result.append("resultMsg", "获取客户相关营销活动成功");
        } catch (Exception var13) {
            var13.printStackTrace();
            result.append("result", "2");
            result.append("resultMsg", "获取客户相关营销活动错误");
        }

        this.log.info("[crm webservice output]:" + result.toString());
        return result.toString();
    }
}
