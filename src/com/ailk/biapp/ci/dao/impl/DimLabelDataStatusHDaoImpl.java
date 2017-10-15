package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.IDimLabelDataStatusHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.DimLabelDataStatus;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DimLabelDataStatusHDaoImpl extends HibernateBaseDao<DimLabelDataStatus, String> implements IDimLabelDataStatusHDao {
    public DimLabelDataStatusHDaoImpl() {
    }

    public DimLabelDataStatus selectById(String tableId) {
        DimLabelDataStatus dimLabelDataStatus = (DimLabelDataStatus)this.get(tableId);
        return dimLabelDataStatus;
    }

    public List<DimLabelDataStatus> select() {
        List dimLabelDataStatusList = this.getAll();
        return dimLabelDataStatusList;
    }
}
