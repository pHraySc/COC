package com.ailk.biapp.ci.ia.dao;

import com.ailk.biapp.ci.ia.entity.CiIaWorksheetChartRel;
import java.util.List;

public interface ICiIaWorksheetChartRelDao {
    void insertCiIaWorksheetChartRel(CiIaWorksheetChartRel var1);

    void updateCiIaWorksheetChartRel(CiIaWorksheetChartRel var1);

    CiIaWorksheetChartRel selectById(Integer var1);

    List<CiIaWorksheetChartRel> selectByWorksheetId(Integer var1);

    List<CiIaWorksheetChartRel> selectAllCiIaWorksheetChartRel();

    void deleteCiIaWorksheetChartRel(Integer var1);

    void deleteCiIaWorksheetChartRel(CiIaWorksheetChartRel var1);
}
