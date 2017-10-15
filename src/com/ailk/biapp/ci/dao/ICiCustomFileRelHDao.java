package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiCustomFileRel;

public interface ICiCustomFileRelHDao {
    void insertCustomFileRel(CiCustomFileRel var1) throws Exception;

    CiCustomFileRel selectCustomFile(String var1) throws Exception;

    CiCustomFileRel selectCustomFileByCustomGroupId(String var1);
}
