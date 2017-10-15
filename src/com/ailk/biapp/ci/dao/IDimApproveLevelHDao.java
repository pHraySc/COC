package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.DimApproveLevel;
import java.util.List;

public interface IDimApproveLevelHDao {
    DimApproveLevel selectById(String var1);

    String selectByProcessIdAndApproveRoleType(String var1, Integer var2);

    List<DimApproveLevel> selectByRoleIdAndRoleType(String var1, Integer var2);

    List<DimApproveLevel> selectListByProperty(String var1, Object var2);

    void insertApproveLevel(DimApproveLevel var1);

    void updateApproveLevel(DimApproveLevel var1);

    void deleteApproveLevel(String var1);

    DimApproveLevel selectByApproveRoleId(String var1);
}
