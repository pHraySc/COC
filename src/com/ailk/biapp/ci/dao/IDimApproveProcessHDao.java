package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.DimApproveProcess;
import java.util.List;

public interface IDimApproveProcessHDao {
    DimApproveProcess selectById(String var1);

    List<DimApproveProcess> select();

    String findbyProperty(Integer var1);
}
