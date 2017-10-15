package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiApproveStatus;
import java.util.List;

public interface ICiApproveStatusJDao {
    int getCountBySql(CiApproveStatus var1) throws Exception;

    List<CiApproveStatus> getPageListBySql(int var1, int var2, CiApproveStatus var3) throws Exception;
}
