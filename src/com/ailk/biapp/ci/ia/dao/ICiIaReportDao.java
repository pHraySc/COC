package com.ailk.biapp.ci.ia.dao;

import com.ailk.biapp.ci.ia.entity.CiIaReport;
import java.util.List;

public interface ICiIaReportDao {
    void insertCiIaReport(CiIaReport var1);

    void updateCiIaReport(CiIaReport var1);

    void updateStatus(CiIaReport var1, Integer var2);

    void deleteCiIaReport(Integer var1);

    void deleteCiIaReport(CiIaReport var1);

    List<CiIaReport> selectByCreateUserId(String var1);
}
