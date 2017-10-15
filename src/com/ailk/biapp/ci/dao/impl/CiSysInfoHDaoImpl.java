package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiSysInfoHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiSysInfo;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiSysInfoHDaoImpl extends HibernateBaseDao<CiSysInfo, String> implements ICiSysInfoHDao {
    public CiSysInfoHDaoImpl() {
    }

    public CiSysInfo selectByName(String sysName) {
        String hql = "from CiSysInfo where sysName = ?";
        return (CiSysInfo)super.findUnique(hql, new Object[]{sysName});
    }

    public CiSysInfo selectById(String sysId) {
        return (CiSysInfo)super.get(sysId);
    }

    public void insertCiSysInfo(CiSysInfo ciSysInfo) {
        super.save(ciSysInfo);
    }

    public List<CiSysInfo> selectByShowOrNOt(int showOrNot) {
        String hql = "from CiSysInfo where showInPage = ?";
        return super.find(hql, new Object[]{Integer.valueOf(showOrNot)});
    }
}
