package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomGroupPushCycle;
import java.util.Date;
import java.util.List;

public interface ICiCustomGroupInfoHDao {
    void deleteCustomGroup(CiCustomGroupInfo var1);

    CiCustomGroupInfo selectCustomGroupById(String var1);

    void insertCustomGroup(CiCustomGroupInfo var1);

    List<CiCustomGroupInfo> selectCiCustomGroupInfoListByName(String var1, String var2, String var3);

    List<CiCustomGroupInfo> selectCiCustomGroupInfo4CycleCreate(int var1, Date var2, String var3);

    List<CiCustomGroupInfo> selectCiCustomGroupInfoListByCustomGroupInfo(CiCustomGroupInfo var1, String var2, int var3);

    int resetProductAutoMacthFlag();

    void insertCustomGroupPushCycle(CiCustomGroupPushCycle var1);

    void deleteCiCustomGroupPushCycleByCustomGroupId(String var1);

    void deleteCiCustomGroupPushCycle(String var1, Integer var2);

    List<CiCustomGroupPushCycle> selectushCycleByGroupId(String var1);

    List<CiCustomGroupPushCycle> selectPushCycleByGroupId(String var1);

    List<CiCustomGroupInfo> selectCiCustomGroupInfoListByShareName(String var1);

    List<CiCustomGroupInfo> selectDelCustomGroup(String var1);

    List<CiCustomGroupInfo> selectCiCustomGroupInfoByIsFirstFailed(Integer var1);

    void updateAllCiCustomGroupIsFirstFailed(Integer var1);

    void updateCiCustomGroupIsFirstFailed(String var1, Integer var2);

    List<CiCustomGroupInfo> selectPublicCustomGroup(String var1);
}
