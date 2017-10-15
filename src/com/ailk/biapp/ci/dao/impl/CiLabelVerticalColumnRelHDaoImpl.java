package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiLabelVerticalColumnRelHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiLabelVerticalColumnRel;
import com.ailk.biapp.ci.entity.CiLabelVerticalColumnRelId;
import org.springframework.stereotype.Repository;

@Repository
public class CiLabelVerticalColumnRelHDaoImpl extends HibernateBaseDao<CiLabelVerticalColumnRel, CiLabelVerticalColumnRelId> implements ICiLabelVerticalColumnRelHDao {
    public CiLabelVerticalColumnRelHDaoImpl() {
    }

    public void insertCiLabelVerticalColumnRel(CiLabelVerticalColumnRel ciLabelVerticalColumnRel) {
        this.save(ciLabelVerticalColumnRel);
    }

    public void deleteCiLabelVerticalColumnRel(CiLabelVerticalColumnRelId id) {
        this.delete(id);
    }

    public void deleteCiLabelVerticalColumnRelByLabelId(Integer labelId) {
        String hql = "delete CiLabelVerticalColumnRel where id.labelId = ?";
        this.batchExecute(hql, new Object[]{labelId});
    }
}
