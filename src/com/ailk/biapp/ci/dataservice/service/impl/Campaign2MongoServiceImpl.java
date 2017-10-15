package com.ailk.biapp.ci.dataservice.service.impl;

import com.ailk.biapp.ci.dataservice.dao.ICampaign2MongoJDao;
import com.ailk.biapp.ci.dataservice.dao.ICiCustomCampsegRelJDao;
import com.ailk.biapp.ci.dataservice.service.ICampaign2MongoService;
import com.ailk.biapp.ci.entity.CiPersonCampaigns;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.CiCustomCampsegRel;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Campaign2MongoServiceImpl implements ICampaign2MongoService {
    private Logger log = Logger.getLogger(Campaign2MongoServiceImpl.class);
    @Autowired
    private ICiCustomCampsegRelJDao ciCustomCampsegRelJDao;
    @Autowired
    private ICampaign2MongoJDao campaign2MongoJDao;

    public Campaign2MongoServiceImpl() {
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiPersonCampaigns> queryPersonCampaigns(int currPage, int pageSize) throws CIServiceException {
        List list = null;

        try {
            list = this.campaign2MongoJDao.selectCiPersonCampaignsList(currPage, pageSize);
            return list;
        } catch (Exception var6) {
            String message = "查询个人营销活动分页列表错误";
            this.log.error(message, var6);
            throw new CIServiceException(message, var6);
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiCustomCampsegRel> queryCiCustomCampsegRelList() throws CIServiceException {
        List list = null;

        try {
            list = this.ciCustomCampsegRelJDao.selectCiCustomCampsegRelList();
            return list;
        } catch (Exception var4) {
            String message = "查询客户群对应营销活动错误";
            this.log.error(message, var4);
            throw new CIServiceException(message, var4);
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public int queryPersonCampaignsCount() throws CIServiceException {
        boolean count = false;

        try {
            int count1 = this.campaign2MongoJDao.selectCiPersonCampaignsCount();
            return count1;
        } catch (Exception var4) {
            String message = "查询个人营销活动总数错误";
            this.log.error(message, var4);
            throw new CIServiceException(message, var4);
        }
    }
}
