package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiLabelStatusHistoryHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiLabelStatusHistory;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiLabelStatusHistoryHDaoImpl extends HibernateBaseDao<CiLabelStatusHistory, Integer> implements ICiLabelStatusHistoryHDao {
    public CiLabelStatusHistoryHDaoImpl() {
    }

    public void insertCiLabelStatusHistory(CiLabelStatusHistory bean) {
        this.save(bean);
    }

    public CiLabelStatusHistory selectCiLabelStatusHistoryById(Integer labelId) {
        CiLabelStatusHistory ciLabelStatusHistory = (CiLabelStatusHistory)this.get(labelId);
        return ciLabelStatusHistory;
    }

    public void updateCiLabelStatusHistory(CiLabelStatusHistory bean) {
        this.save(bean);
    }

    public List<CiLabelStatusHistory> selectCiLabelStatusHistoryListByNotice() {
        String hql = "from CiLabelStatusHistory t where t.hasSendNotice = 0";
        List list = this.find(hql, new Object[0]);
        return list;
    }
}
