package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.model.Pager;
import java.util.Date;
import java.util.List;

public interface ICustomGroupInfoJDao {
    int getCustomersAutoMacthFlag(String var1) throws Exception;

    int getCustomersTotolCount(CiCustomGroupInfo var1) throws Exception;

    List<CiCustomGroupInfo> getCustomsersList(Pager var1, CiCustomGroupInfo var2) throws Exception;

    List<CiCustomGroupInfo> getCustomsersList(Pager var1, CiCustomGroupInfo var2, String var3) throws Exception;

    List<CiCustomGroupInfo> getUserUseCustomsersList(Pager var1, CiCustomGroupInfo var2, String var3) throws Exception;

    Long getUserAttentionCustomserCount(CiCustomGroupInfo var1) throws Exception;

    String getLableNameById(String var1) throws Exception;

    int selectCount(String var1) throws Exception;

    void selectValidate(String var1) throws Exception;

    List<CiCustomGroupInfo> indexQueryCustomersName(CiCustomGroupInfo var1, Integer var2) throws Exception;

    int selectCustomerGroupIsPush(String var1) throws Exception;

    void batchUpdateSql(List<String> var1);

    CiCustomGroupInfo queryCiCustomGroupInfoByListInfoId(String var1) throws Exception;

    List<CiCustomGroupInfo> getSysRecommendCustomInfoList(CiCustomGroupInfo var1) throws Exception;

    int getDailyCustomersTotolCount(CiCustomGroupInfo var1) throws Exception;

    CiCustomGroupInfo selectCustomGroupAndAttention(CiCustomGroupInfo var1);

    String getCiCustomExeTime() throws Exception;

    void updateCiCustomExeTime(String var1) throws Exception;

    List<CiCustomGroupInfo> findCiCustomGroupInfo4TimingCreate(String var1, Date var2) throws Exception;

    int selectCountByJdbc(String var1, String var2, String var3, String var4) throws Exception;

    List<CiCustomGroupInfo> findRecommendCustomInfoList();

    String selectOtherSysCustomGroupId(String var1, String var2);

    int queryForExist(String labelIds);

    int queryForPowerMatch(String userId, String labelIds);
}
