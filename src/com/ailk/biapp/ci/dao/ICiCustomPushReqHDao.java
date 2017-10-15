package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiCustomPushReq;
import java.util.List;

public interface ICiCustomPushReqHDao {
    void insertCiCustomPushReq(CiCustomPushReq var1);

    List<CiCustomPushReq> select(CiCustomPushReq var1);

    CiCustomPushReq selectCiCustomPushReqById(String var1);
}
