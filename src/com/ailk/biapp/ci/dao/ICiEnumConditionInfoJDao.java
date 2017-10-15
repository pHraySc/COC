package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiEnumConditionInfo;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.asiainfo.biframe.dimtable.model.DimTableDefine;
import java.util.List;
import java.util.Map;

public interface ICiEnumConditionInfoJDao {
    long selectCiEnumConditionInfoCount(CiEnumConditionInfo var1, String var2, DimTableDefine var3, CiMdaSysTableColumn var4) throws Exception;

    List<Map<String, Object>> selectCiEnumConditionInfoList(int var1, int var2, CiEnumConditionInfo var3, String var4, DimTableDefine var5, CiMdaSysTableColumn var6) throws Exception;
}
