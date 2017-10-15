package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiLabelExtInfoHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiLabelExtInfo;
import org.springframework.stereotype.Repository;

@Repository
public class CiLabelExtInfoHDaoImpl extends HibernateBaseDao<CiLabelExtInfo, Integer> implements ICiLabelExtInfoHDao {
    public CiLabelExtInfoHDaoImpl() {
    }

    public void insertCiLabelExtInfo(CiLabelExtInfo bean) {
        this.save(bean);
    }

    public void updateCiLabelExtInfo(CiLabelExtInfo bean) {
        this.save(bean);
    }

    public CiLabelExtInfo getById(Integer labelId) {
        return (CiLabelExtInfo)this.get(labelId);
    }

    public void updateCiLabelExtInfoByLabelIds(String labelIds, Integer isLeaf) {
        StringBuffer hql = new StringBuffer();
        hql.append("update CiLabelExtInfo c set c.isLeaf=? where c.labelId in ( ").append(labelIds).append(" )");
        this.batchExecute(hql.toString(), new Object[]{isLeaf});
    }
}
