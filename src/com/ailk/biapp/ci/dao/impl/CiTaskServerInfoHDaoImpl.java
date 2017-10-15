package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiTaskServerInfoHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiTaskServerInfo;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiTaskServerInfoHDaoImpl extends HibernateBaseDao<CiTaskServerInfo, String> implements ICiTaskServerInfoHDao {
    public CiTaskServerInfoHDaoImpl() {
    }

    public CiTaskServerInfo selectById(String serverId) {
        CiTaskServerInfo ciTaskServerInfo = (CiTaskServerInfo)this.get(serverId);
        return ciTaskServerInfo;
    }

    public List<CiTaskServerInfo> select() {
        List ciTaskServerInfo = this.getAll();
        return ciTaskServerInfo;
    }

    public List<CiTaskServerInfo> selectByIsExeTask(int isExeTask) {
        List list = this.find("from CiTaskServerInfo u where u.isExeTask = ?", new Object[]{Integer.valueOf(isExeTask)});
        return list;
    }
}
