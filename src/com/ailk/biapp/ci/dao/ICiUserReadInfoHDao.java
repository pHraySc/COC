package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiUserReadInfo;
import com.ailk.biapp.ci.entity.CiUserReadInfoId;

public interface ICiUserReadInfoHDao {
    void insertCiUserReadInfo(CiUserReadInfo var1);

    void deleteCiUserReadInfo(CiUserReadInfo var1);

    CiUserReadInfo selectCiUserReadInfoById(CiUserReadInfoId var1);
}
