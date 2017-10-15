package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiCustomPushReqHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiCustomPushReq;
import com.ailk.biapp.ci.util.CiUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.springframework.stereotype.Repository;

@Repository
public class CiCustomPushReqHDaoImpl extends HibernateBaseDao<CiCustomPushReq, String> implements ICiCustomPushReqHDao {
    public CiCustomPushReqHDaoImpl() {
    }

    public void insertCiCustomPushReq(CiCustomPushReq req) {
        if(req.getReqId() == null) {
            req.setReqId("COC" + CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L));
        }

        super.save(req);
    }

    public List<CiCustomPushReq> select(CiCustomPushReq ciCustomPushReq) {
        Criteria c = this.getSession().createCriteria(CiCustomPushReq.class);
        Example example = Example.create(ciCustomPushReq);
        c.add(example);
        return c.list();
    }

    public CiCustomPushReq selectCiCustomPushReqById(String reqId) {
        CiCustomPushReq ciCustomPushReq = (CiCustomPushReq)this.get(reqId);
        return ciCustomPushReq;
    }
}
