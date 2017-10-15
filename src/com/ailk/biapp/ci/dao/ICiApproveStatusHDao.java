package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiApproveStatus;
import java.util.List;

public interface ICiApproveStatusHDao {
    CiApproveStatus selectById(Integer var1);

    List<CiApproveStatus> select();

    void insertOrUpdateCiApproveStatus(CiApproveStatus var1);

    /** @deprecated */
    @Deprecated
    List<Integer> getLabelIdsByApprStaId(String var1);

    CiApproveStatus selectByStatusId(Integer var1);

    List<CiApproveStatus> selectByResourceIdAndProcessId(String var1, String var2);
}
