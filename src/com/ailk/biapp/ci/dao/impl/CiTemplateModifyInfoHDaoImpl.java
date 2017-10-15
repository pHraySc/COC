package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiTemplateModifyInfoHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiTemplateModifyInfo;
import org.springframework.stereotype.Repository;

@Repository
public class CiTemplateModifyInfoHDaoImpl extends HibernateBaseDao<CiTemplateModifyInfo, Integer> implements ICiTemplateModifyInfoHDao {
    public CiTemplateModifyInfoHDaoImpl() {
    }

    public void insertCiTemplateModifyInfo(CiTemplateModifyInfo ciTemplateModifyInfo) {
        this.save(ciTemplateModifyInfo);
    }
}
