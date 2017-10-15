package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiApproveHistoryHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiApproveHistory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiApproveHistoryHDaoImpl extends HibernateBaseDao<CiApproveHistory, String> implements ICiApproveHistoryHDao {
    public CiApproveHistoryHDaoImpl() {
    }

    public void insertCiApproveHistory(CiApproveHistory bean) {
        this.save(bean);
    }

    public List<CiApproveHistory> selectByResourceIdAndProcessId(String resourceId, String processId) {
        List list = null;
        list = this.find("from \tCiApproveHistory c where c.resourceId = ? and c.processId = ? order by approveTime", new Object[]{resourceId, processId});
        return list;
    }

    /** @deprecated */
    @Deprecated
    public List<Integer> getLabelIdsByApprUserId(String approveUserId) {
        ArrayList labelIds = new ArrayList();
        List ciApproveHistoryList = this.findBy("approveUserId", approveUserId);
        Iterator iter = ciApproveHistoryList.iterator();

        while(iter.hasNext()) {
        }

        return labelIds;
    }

    public List<CiApproveHistory> getHistoryByLabelId(Integer labelId) {
        List ciApproveHistoryList = this.findBy("labelId", labelId);
        return ciApproveHistoryList;
    }
}
