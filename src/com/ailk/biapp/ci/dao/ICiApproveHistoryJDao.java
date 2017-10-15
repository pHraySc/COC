package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiApproveHistory;
import java.util.List;

public interface ICiApproveHistoryJDao {
    int getApprovedCountBySql(CiApproveHistory var1) throws Exception;

    List<CiApproveHistory> getApprovedPageListBySql(int var1, int var2, CiApproveHistory var3) throws Exception;
}
