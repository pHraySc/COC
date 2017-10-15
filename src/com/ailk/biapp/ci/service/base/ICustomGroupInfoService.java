package com.ailk.biapp.ci.service.base;

import com.ailk.biapp.ci.entity.base.CustomGroupInfo;
import com.ailk.biapp.ci.entity.base.CustomListInfo;
import com.ailk.biapp.ci.entity.base.SysInfo;
import java.util.List;

public interface ICustomGroupInfoService {
    List<CustomGroupInfo> queryCustomGroupInfoList(SysInfo var1, Integer var2, Integer var3) throws Exception;

    CustomGroupInfo queryCustomGroupInfoById(String var1, SysInfo var2) throws Exception;

    List<CustomListInfo> queryCustomListInfo(SysInfo var1, String var2) throws Exception;

    int queryCustomNum(String var1, SysInfo var2) throws Exception;

    String addCustomGroupInfo(String var1, SysInfo var2, CustomGroupInfo var3) throws Exception;

    int updateCustomGroupInfo(String var1, SysInfo var2, CustomGroupInfo var3) throws Exception;

    int deleteCustomGroupInfo(String var1, SysInfo var2) throws Exception;

    int pushCustomGroupInfo(String var1, SysInfo var2, String var3) throws Exception;

    List<SysInfo> querySysInfo(SysInfo var1) throws Exception;
}
