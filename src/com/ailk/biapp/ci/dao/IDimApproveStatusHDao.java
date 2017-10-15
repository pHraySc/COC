package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.DimApproveStatus;
import java.util.List;

public interface IDimApproveStatusHDao {
    List<DimApproveStatus> select();

    DimApproveStatus selectDraftStatus();

    String getApproveStatuByApproveRole(String var1, Integer var2);

    void insertApproveStatus(DimApproveStatus var1);

    void updateApproveStatus(DimApproveStatus var1);

    void deleteApproveStatus(String var1);

    DimApproveStatus selectByApproveStatusId(String var1);
}
