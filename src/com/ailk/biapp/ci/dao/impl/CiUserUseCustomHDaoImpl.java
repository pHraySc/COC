package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiUserUseCustomHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiUserUseCustom;
import org.springframework.stereotype.Repository;

@Repository
public class CiUserUseCustomHDaoImpl extends HibernateBaseDao<CiUserUseCustom, String> implements ICiUserUseCustomHDao {
    public CiUserUseCustomHDaoImpl() {
    }

    public void insertOrUpdateCiUserUseCustom(CiUserUseCustom bean) {
        this.save(bean);
    }
}
