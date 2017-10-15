package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiCustomModifyHistory;
import java.util.List;

public interface ICiCustomModifyHistoryHDao {
    void insertCiCustomModifyHistory(CiCustomModifyHistory var1);

    List<CiCustomModifyHistory> selectCustomModifyHistory(String var1);
}
