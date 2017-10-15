package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import java.util.List;

public interface ICiGroupAttrRelHDao {
    void insertCiGroupAttrRelHDao(CiGroupAttrRel var1) throws Exception;

    List<CiGroupAttrRel> selectCiGroupAttrRelListBySourceId(String var1, String var2) throws Exception;

    void deleteCiGroupAttrRelListBySourceId(String var1) throws Exception;
}
