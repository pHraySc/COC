package com.ailk.biapp.ci.ia.dao;

import com.ailk.biapp.ci.ia.entity.CiIaDimIndexGroup;
import java.util.List;

public interface ICiIaDimIndexGroupHDao {
    List<CiIaDimIndexGroup> selectBySheetChartRelId(int var1);

    void deleteBySheetChartRelId(int var1);

    void insert(CiIaDimIndexGroup var1);

    void insert(List<CiIaDimIndexGroup> var1);
}
