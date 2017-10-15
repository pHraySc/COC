package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiDimCocIndexTableInfoHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.DimCocIndexTableInfo;
import org.springframework.stereotype.Repository;

@Repository
public class CiDimCocIndexTableInfoHDaoImpl extends HibernateBaseDao<DimCocIndexTableInfo, String> implements ICiDimCocIndexTableInfoHDao {
    public CiDimCocIndexTableInfoHDaoImpl() {
    }

    public DimCocIndexTableInfo selectDimCocIndexTableInfoById(String id) throws Exception {
        return (DimCocIndexTableInfo)this.get(id);
    }
}
