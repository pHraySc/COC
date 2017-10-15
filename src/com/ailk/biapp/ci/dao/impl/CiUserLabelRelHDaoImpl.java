package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiUserLabelRelHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiUserLabelRel;
import com.ailk.biapp.ci.entity.CiUserLabelRelId;
import org.springframework.stereotype.Repository;

@Repository("ciUserLabelRelHDao")
public class CiUserLabelRelHDaoImpl extends HibernateBaseDao<CiUserLabelRel, CiUserLabelRelId> implements ICiUserLabelRelHDao {
    public CiUserLabelRelHDaoImpl() {
    }

    public void deleteCiUserLabelRel(CiUserLabelRelId id) {
        CiUserLabelRel entity = (CiUserLabelRel)super.get(id);
        if(null != entity) {
            entity.setStatus(Integer.valueOf(0));
            super.save(entity);
        }

    }

    public void insertCiUserLabelRel(CiUserLabelRel entity) {
        super.save(entity);
    }
}
