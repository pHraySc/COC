package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiCustomCampsegRelHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiCustomCampsegRel;
import com.ailk.biapp.ci.entity.CiCustomCampsegRelId;
import org.springframework.stereotype.Repository;

@Repository
public class CiCustomCampsegRelHDaoImpl extends HibernateBaseDao<CiCustomCampsegRel, CiCustomCampsegRelId> implements ICiCustomCampsegRelHDao {
    public CiCustomCampsegRelHDaoImpl() {
    }

    public void insertCiCustomCampsegRel(CiCustomCampsegRel ciCustomCampsegRel) {
        super.save(ciCustomCampsegRel);
    }

    public void deleteByListTableName(String listTableName) {
        this.batchExecute("delete from CiCustomCampsegRel where relId.customGroupId=?", new Object[]{listTableName});
    }
}
