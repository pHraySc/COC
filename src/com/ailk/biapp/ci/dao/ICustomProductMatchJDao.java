package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CustomProductMatch;
import java.util.List;

public interface ICustomProductMatchJDao {
    boolean isCustomProductMatch(CiCustomGroupInfo var1) throws Exception;

    List<CustomProductMatch> selectSysProductMatch(CiCustomGroupInfo var1) throws Exception;
}
