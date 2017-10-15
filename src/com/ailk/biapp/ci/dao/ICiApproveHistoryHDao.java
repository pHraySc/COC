package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiApproveHistory;
import java.util.List;

public interface ICiApproveHistoryHDao {
    void insertCiApproveHistory(CiApproveHistory var1);

    /** @deprecated */
    @Deprecated
    List<Integer> getLabelIdsByApprUserId(String var1);

    /** @deprecated */
    @Deprecated
    List<CiApproveHistory> getHistoryByLabelId(Integer var1);

    List<CiApproveHistory> selectByResourceIdAndProcessId(String var1, String var2);
}
