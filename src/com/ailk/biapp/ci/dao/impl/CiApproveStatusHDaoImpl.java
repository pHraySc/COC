package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiApproveStatusHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiApproveStatus;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiApproveStatusHDaoImpl extends HibernateBaseDao<CiApproveStatus, Integer> implements ICiApproveStatusHDao {
    public CiApproveStatusHDaoImpl() {
    }

    public CiApproveStatus selectById(Integer tableId) {
        CiApproveStatus ciApproveStatus = (CiApproveStatus)this.get(tableId);
        return ciApproveStatus;
    }

    public List<CiApproveStatus> select() {
        List ciApproveStatus = this.getAll();
        return ciApproveStatus;
    }

    public void insertOrUpdateCiApproveStatus(CiApproveStatus bean) {
        this.save(bean);
    }

    /** @deprecated */
    @Deprecated
    public List<Integer> getLabelIdsByApprStaId(String approveStatusId) {
        ArrayList labelIds = new ArrayList();
        List ciApproveStatusList = this.findBy("currApproveStatusId", approveStatusId);
        Iterator iter = ciApproveStatusList.iterator();

        while(iter.hasNext()) {
            ;
        }

        return labelIds;
    }

    public CiApproveStatus selectByStatusId(Integer statusId) {
        CiApproveStatus ciApproveStatus = (CiApproveStatus)this.get(statusId);
        return ciApproveStatus;
    }

    public List<CiApproveStatus> selectByResourceIdAndProcessId(String resourceId, String processId) {
        List list = null;
        list = this.find("from \tCiApproveStatus c where c.resourceId = ? and c.processId = ?", new Object[]{resourceId, processId});
        return list;
    }
}
