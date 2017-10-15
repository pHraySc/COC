package com.ailk.biapp.ci.ia.dao;

import com.ailk.biapp.ci.ia.entity.CiIaWorksheet;
import java.util.List;

public interface ICiIaWorksheetDao {
    void insertCiIaWorksheet(CiIaWorksheet var1);

    void updateCiIaWorksheet(CiIaWorksheet var1);

    void updateStatus(CiIaWorksheet var1, Integer var2);

    void deleteCiaWorksheet(Integer var1);

    void deleteCiIaWorksheet(CiIaWorksheet var1);

    List<CiIaWorksheet> selectByReportId(Integer var1);
}
