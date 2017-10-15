package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiUserReadInfoHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiUserReadInfo;
import com.ailk.biapp.ci.entity.CiUserReadInfoId;
import org.springframework.stereotype.Repository;

@Repository
public class CiUserReadInfoHDaoImpl extends HibernateBaseDao<CiUserReadInfo, CiUserReadInfoId> implements ICiUserReadInfoHDao {
    public CiUserReadInfoHDaoImpl() {
    }

    public void insertCiUserReadInfo(CiUserReadInfo ciUserReadInfo) {
        this.save(ciUserReadInfo);
    }

    public void deleteCiUserReadInfo(CiUserReadInfo ciUserReadInfo) {
        CiUserReadInfo obj = this.selectCiUserReadInfoById(ciUserReadInfo.getId());
        obj.setStatus(Integer.valueOf(0));
        this.save(obj);
    }

    public CiUserReadInfo selectCiUserReadInfoById(CiUserReadInfoId id) {
        CiUserReadInfo ciUserReadInfo = (CiUserReadInfo)this.get(id);
        return ciUserReadInfo;
    }
}
