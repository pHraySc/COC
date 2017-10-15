package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiListExeSqlAllHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiListExeSqlAll;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiListExeSqlAllHDaoImpl extends HibernateBaseDao<CiListExeSqlAll, Integer> implements ICiListExeSqlAllHDao {
    public CiListExeSqlAllHDaoImpl() {
    }

    public void insertCiListExeSqlAll(CiListExeSqlAll ciListExeSqlAll) {
        this.save(ciListExeSqlAll);
    }

    public List<CiListExeSqlAll> selectCiListExeSqlAllByExeInfoId(String exeInfoId) {
        List list = this.find(" from CiListExeSqlAll l where l.exeInfoId = ? order by l.sortNum", new Object[]{exeInfoId});
        return list;
    }
}
