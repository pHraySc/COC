package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiLabelVerticalColumnRel;
import java.util.List;

public interface ICiLabelVerticalColumnRelJDao {
    List<CiLabelVerticalColumnRel> selectCiVerticalColumnRelByLabelId(Integer var1);
}
