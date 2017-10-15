package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiMdaTableHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiMdaSysTable;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

@Repository
public class CiMdaTableHDaoImpl extends HibernateBaseDao<CiMdaSysTable, Integer> implements ICiMdaTableHDao {
    public CiMdaTableHDaoImpl() {
    }

    public void insertTableInfo(CiMdaSysTable table) throws Exception {
        this.save(table);
    }

    public List<CiMdaSysTable> selectAllCiMdaSysTable() throws Exception {
        String hql = "from CiMdaSysTable";
        List list = this.find(hql, new Object[0]);
        return list;
    }

    public CiMdaSysTable selectTableInfoByName(String name) throws Exception {
        List tableList = this.findBy("tableName", name);
        return CollectionUtils.isNotEmpty(tableList)?(CiMdaSysTable)tableList.get(0):null;
    }

    public CiMdaSysTable selectTableInfoById(Integer tableId) throws Exception {
        return (CiMdaSysTable)this.get(tableId);
    }
}
