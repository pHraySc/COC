package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.DimLabelDataStatus;
import java.util.List;

public interface IDimLabelDataStatusHDao {
    DimLabelDataStatus selectById(String var1);

    List<DimLabelDataStatus> select();
}
