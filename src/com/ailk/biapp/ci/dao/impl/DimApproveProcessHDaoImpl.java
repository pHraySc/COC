package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.IDimApproveProcessHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.DimApproveProcess;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DimApproveProcessHDaoImpl extends HibernateBaseDao<DimApproveProcess, String> implements IDimApproveProcessHDao {
    public DimApproveProcessHDaoImpl() {
    }

    public DimApproveProcess selectById(String tableId) {
        DimApproveProcess dimApproveProcess = (DimApproveProcess)this.get(tableId);
        return dimApproveProcess;
    }

    public List<DimApproveProcess> select() {
        List dimApproveProcess = this.getAll();
        return dimApproveProcess;
    }

    public String findbyProperty(Integer status) {
        if(status == null) {
            return null;
        } else {
            List dimApproveProcessList = this.find("from DimApproveProcess c where c.status = ?", new Object[]{status});
            return dimApproveProcessList != null?((DimApproveProcess)dimApproveProcessList.get(0)).getProcessId():null;
        }
    }
}
