package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiCustomListExeInfoHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiCustomListExeInfo;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiCustomListExeInfoHDaoImpl extends HibernateBaseDao<CiCustomListExeInfo, String> implements ICiCustomListExeInfoHDao {
    public CiCustomListExeInfoHDaoImpl() {
    }

    public CiCustomListExeInfo selectCiCustomListExeInfoById(String exeInfoId) {
        return (CiCustomListExeInfo)this.get(exeInfoId);
    }

    public void insertCiCustomListExeInfo(CiCustomListExeInfo ciCustomListExeInfo) {
        this.save(ciCustomListExeInfo);
    }

    public void deleteByListTableName(String listTableName) {
        this.batchExecute(" delete from CiCustomListExeInfo where listTableName= ? ", new Object[]{listTableName});
    }

    public List<CiCustomListExeInfo> queryListExeInfosByTableName(String listTableName) {
        List list = this.find(" from CiCustomListExeInfo l where l.listTableName = ?", new Object[]{listTableName});
        return list;
    }
}
