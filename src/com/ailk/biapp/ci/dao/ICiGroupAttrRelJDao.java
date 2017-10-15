package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import java.util.Date;
import java.util.List;

public interface ICiGroupAttrRelJDao {
    List<CiGroupAttrRel> selectCiGroupAttrRelList(String var1, Date var2) throws Exception;

    List<CiGroupAttrRel> selectNewestCiGroupAttrRelList(String var1) throws Exception;

    void updateCiGroupAttrRelListModifyTime(String var1, Date var2) throws Exception;

    void updateCiGroupAttrRelStatusByGroupInfoId(String var1) throws Exception;

    void deleteGroupAttrRelList(String var1, Date var2);

    List<CiGroupAttrRel> selectNewestCiGroupAttrRelBySort(String var1) throws Exception;
}
