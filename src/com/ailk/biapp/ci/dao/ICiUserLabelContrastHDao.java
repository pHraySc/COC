package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiUserLabelContrast;
import com.ailk.biapp.ci.entity.CiUserLabelContrastId;

public interface ICiUserLabelContrastHDao {
    void insertCiUserLabelContrast(CiUserLabelContrast var1);

    void deleteCiUserLabelContrast(CiUserLabelContrastId var1);

    void deleteCiUserLabelContrast(String var1, String var2);
}
