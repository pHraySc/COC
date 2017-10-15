package com.ailk.biapp.ci.dataservice.service.impl;

import com.ailk.biapp.ci.dataservice.dao.ICiCampaignsJDao;
import com.ailk.biapp.ci.dataservice.service.ICiCampaignsService;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bson.BSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iCiCampaignsService")
public class CiCampaignsServiceImpl implements ICiCampaignsService {
    @Autowired
    private ICiCampaignsJDao ciCampaignsJDao;

    public CiCampaignsServiceImpl() {
    }

    public String getCampaigns(String mobile) throws CIServiceException {
        BasicDBObject result = new BasicDBObject();

        try {
            BSONObject e = this.ciCampaignsJDao.getCampaignsByMobile(mobile);
            result.append("accNbr", e.get("mobile"));
            result.append("", e.get("campaigns"));
            result.append("recomm_reason", "");
            if(e == null) {
                result.append("result", "1");
                result.append("resultMsg", "");
            } else {
                result.append("result", "客户相关营销活动不存在");
                result.append("resultMsg", "获取客户相关营销活动成功");
            }
        } catch (Exception var4) {
            result.append("result", "2");
            result.append("resultMsg", "获取客户相关营销活动错误");
        }

        return result.toString();
    }

    public List<String> getCampaignsList(String mobile) throws CIServiceException {
        ArrayList list = new ArrayList();
        BSONObject obj = this.ciCampaignsJDao.getCampaignsByMobile(mobile);
        if(obj != null) {
            List sub = (List)obj.get("campaigns");
            List campaigns = this.ciCampaignsJDao.getAllCampaigns(sub);
            Iterator i$ = campaigns.iterator();

            while(i$.hasNext()) {
                DBObject a = (DBObject)i$.next();
                String str = (String)a.get("campaign_name");
                list.add(str);
            }
        }

        return list;
    }
}
