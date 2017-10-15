package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiApproveUserInfo;
import java.util.List;

public interface ICiApproveUserInfoHDao {
    /** @deprecated */
    @Deprecated
    CiApproveUserInfo selectById(String var1);

    List<CiApproveUserInfo> selectByUserId(String var1);

    List<CiApproveUserInfo> selectByApproveRoleId(String var1);

    List<CiApproveUserInfo> select(String var1, String var2);
}
