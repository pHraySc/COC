package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiCustomSourceRel;
import java.util.List;

public interface ICiCustomSourceRelHDao {
    List<CiCustomSourceRel> selectByCustomGroupId(String var1);

    void deleteByCustomGroupId(String var1);

    void insert(CiCustomSourceRel var1);

    void insertCiCustomSourceRelList(List<CiCustomSourceRel> var1);
}
