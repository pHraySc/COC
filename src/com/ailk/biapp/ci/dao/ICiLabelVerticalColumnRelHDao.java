package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiLabelVerticalColumnRel;
import com.ailk.biapp.ci.entity.CiLabelVerticalColumnRelId;

public interface ICiLabelVerticalColumnRelHDao {
    void insertCiLabelVerticalColumnRel(CiLabelVerticalColumnRel var1);

    void deleteCiLabelVerticalColumnRel(CiLabelVerticalColumnRelId var1);

    void deleteCiLabelVerticalColumnRelByLabelId(Integer var1) throws Exception;
}
