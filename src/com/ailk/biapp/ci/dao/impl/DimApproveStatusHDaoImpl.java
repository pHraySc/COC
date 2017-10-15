package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.IDimApproveStatusHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.DimApproveStatus;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DimApproveStatusHDaoImpl extends HibernateBaseDao<DimApproveStatus, String> implements IDimApproveStatusHDao {
    public DimApproveStatusHDaoImpl() {
    }

    public List<DimApproveStatus> select() {
        List dimApproveStatus = this.find("from DimApproveStatus c order by c.approveStatusId", new Object[0]);
        return dimApproveStatus;
    }

    public DimApproveStatus selectDraftStatus() {
        long sortNum = 0L;
        DimApproveStatus dimApproveStatus = (DimApproveStatus)this.findUniqueBy("sortNum", Long.valueOf(sortNum));
        return dimApproveStatus;
    }

    public String getApproveStatuByApproveRole(String approveRoleId, Integer flag) {
        if(approveRoleId == null) {
            return null;
        } else {
            List dimApproveStatusList = this.find("from DimApproveStatus c where c.approveRoleId = ? order by c.sortNum", new Object[]{approveRoleId});
            return flag.intValue() == 1?((DimApproveStatus)dimApproveStatusList.get(0)).getApproveStatusId():(flag.intValue() == 2?((DimApproveStatus)dimApproveStatusList.get(1)).getApproveStatusId():null);
        }
    }

    public DimApproveStatus selectByApproveStatusId(String approveStatusId) {
        if(approveStatusId == null) {
            return null;
        } else {
            List dimApproveStatusList = this.find("from DimApproveStatus c where c.approveStatusId = ?", new Object[]{approveStatusId});
            return (DimApproveStatus)dimApproveStatusList.get(0);
        }
    }

    public void insertApproveStatus(DimApproveStatus dimApproveStatus) {
        this.save(dimApproveStatus);
    }

    public void updateApproveStatus(DimApproveStatus dimApproveStatus) {
        this.save(dimApproveStatus);
    }

    public void deleteApproveStatus(String approveStatusId) {
        this.delete(approveStatusId);
    }
}
