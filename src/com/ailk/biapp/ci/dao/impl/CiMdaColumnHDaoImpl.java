package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiMdaColumnHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import org.springframework.stereotype.Repository;

@Repository
public class CiMdaColumnHDaoImpl extends HibernateBaseDao<CiMdaSysTableColumn, Integer> implements ICiMdaColumnHDao {
    public CiMdaColumnHDaoImpl() {
    }

    public void insertColumnInfo(CiMdaSysTableColumn column) throws Exception {
        this.save(column);
    }

    public CiMdaSysTableColumn selectCiMdaSysTableColumnByColumnId(Integer columnId) {
        return (CiMdaSysTableColumn)this.get(columnId);
    }

    public void deleteCiMdaSysTableColumnByColumnId(Integer columnId) {
        this.delete(columnId);
    }
}
