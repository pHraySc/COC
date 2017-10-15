package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiCustomFileRelHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiCustomFileRel;
import org.springframework.stereotype.Repository;

@Repository
public class CiCustomFileRelHDaoImpl extends HibernateBaseDao<CiCustomFileRel, String> implements ICiCustomFileRelHDao {
    public CiCustomFileRelHDaoImpl() {
    }

    public void insertCustomFileRel(CiCustomFileRel customFileRel) throws Exception {
        this.save(customFileRel);
    }

    public CiCustomFileRel selectCustomFile(String fileId) throws Exception {
        return (CiCustomFileRel)this.get(fileId);
    }

    public CiCustomFileRel selectCustomFileByCustomGroupId(String customGroupId) {
        CiCustomFileRel ciCustomFileRel = null;
        ciCustomFileRel = (CiCustomFileRel)this.findUniqueBy("customGroupId", customGroupId);
        return ciCustomFileRel;
    }
}
