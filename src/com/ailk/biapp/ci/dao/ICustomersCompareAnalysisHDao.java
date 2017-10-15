package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiUserCustomContrast;
import com.ailk.biapp.ci.entity.CiUserCustomContrastId;

public interface ICustomersCompareAnalysisHDao {
    void insertCiUserCustomContrast(CiUserCustomContrast var1) throws Exception;

    void deleteCiUserCustomContrast(CiUserCustomContrastId var1) throws Exception;

    void updateCiUserCustomContrast(String var1, String var2) throws Exception;
}
