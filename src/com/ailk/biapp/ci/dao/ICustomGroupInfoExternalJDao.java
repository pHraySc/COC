package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.base.CustomGroupInfo;
import com.ailk.biapp.ci.entity.base.CustomListInfo;
import com.ailk.biapp.ci.entity.base.SysInfo;
import com.ailk.biapp.ci.model.Pager;
import java.util.List;

public interface ICustomGroupInfoExternalJDao {
    List<CustomGroupInfo> getCustomsersList(SysInfo var1, Pager var2) throws Exception;

    int getCustomsersListCount(SysInfo var1) throws Exception;

    CustomGroupInfo selectCustomGroupById(String var1) throws Exception;

    List<CustomListInfo> queryCustomListByCustomId(String var1) throws Exception;

    List<SysInfo> querySysInfos() throws Exception;
}
