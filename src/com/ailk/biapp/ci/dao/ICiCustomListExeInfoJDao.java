package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiCustomListExeInfo;
import com.ailk.biapp.ci.model.Pager;
import java.util.List;

public interface ICiCustomListExeInfoJDao {
    int getListExeInfoNumByTableName(String var1);

    List<CiCustomListExeInfo> queryListExeInfosByTableName(Pager var1, String var2);
}
