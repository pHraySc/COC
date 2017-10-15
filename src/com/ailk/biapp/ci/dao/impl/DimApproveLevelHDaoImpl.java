package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.IDimApproveLevelHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.DimApproveLevel;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import sun.rmi.runtime.Log;

@Repository
public class DimApproveLevelHDaoImpl extends HibernateBaseDao<DimApproveLevel, String> implements IDimApproveLevelHDao {
    private Logger log = Logger.getLogger(DimApproveLevelHDaoImpl.class);

    public DimApproveLevelHDaoImpl() {
    }

    public DimApproveLevel selectById(String tableId) {
        DimApproveLevel dimApproveLevel = (DimApproveLevel)this.get(tableId);
        return dimApproveLevel;
    }

    public String selectByProcessIdAndApproveRoleType(String processId, Integer approveRoleType) {
        if(processId != null && approveRoleType != null) {
            List dimApproveLevelList = this.find("from DimApproveLevel c where c.processId = ? and c.approveRoleType=? order by c.approveLevel", new Object[]{processId, approveRoleType});
            this.log.info("dimApproveLevelList's size" + dimApproveLevelList.size());
            return dimApproveLevelList != null?((DimApproveLevel)dimApproveLevelList.get(0)).getApproveRoleId():null;
        } else {
            return null;
        }
    }

    public List<DimApproveLevel> selectByRoleIdAndRoleType(String approveRoleId, Integer approveRoleType) {
        if(approveRoleId != null && approveRoleType != null) {
            List dimApproveLevelList = this.find("from DimApproveLevel c where c.approveRoleId = ? and c.approveRoleType=? order by c.approveLevel", new Object[]{approveRoleId, approveRoleType});
            return dimApproveLevelList;
        } else {
            return null;
        }
    }

    public List<DimApproveLevel> selectListByProperty(String propertyName, Object propertyValue) {
        List dimApproveLevelList = this.findBy(propertyName, propertyValue);
        return dimApproveLevelList;
    }

    public void insertApproveLevel(DimApproveLevel dimApproveLevel) {
        this.save(dimApproveLevel);
    }

    public void updateApproveLevel(DimApproveLevel dimApproveLevel) {
        this.save(dimApproveLevel);
    }

    public void deleteApproveLevel(String approveRoleId) {
        this.delete(approveRoleId);
    }

    public DimApproveLevel selectByApproveRoleId(String approveRoleId) {
        DimApproveLevel dimApproveLevel = (DimApproveLevel)this.get(approveRoleId);
        return dimApproveLevel;
    }
}
