package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiCustomModifyHistoryHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiCustomModifyHistory;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiCustomModifyHistoryHDaoImpl extends HibernateBaseDao<CiCustomModifyHistory, Integer> implements ICiCustomModifyHistoryHDao {
    public CiCustomModifyHistoryHDaoImpl() {
    }

    public void insertCiCustomModifyHistory(CiCustomModifyHistory ciCustomModifyHistory) {
        this.save(ciCustomModifyHistory);
    }

    public List<CiCustomModifyHistory> selectCustomModifyHistory(String customGroupId) {
        return this.find("from CiCustomModifyHistory ch where ch.customGroupId=? order by ch.modifyTime desc", new Object[]{customGroupId});
    }
}
