package com.ailk.biapp.ci.localization.nonstandard;

import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiSysInfo;

public interface ICustomerPush {
    boolean push(CiCustomGroupInfo var1, CiCustomListInfo var2, CiSysInfo var3);
}
