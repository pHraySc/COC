package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiLabelStatusHistory;
import java.util.List;

public interface ICiLabelStatusHistoryHDao {
    void insertCiLabelStatusHistory(CiLabelStatusHistory var1);

    void updateCiLabelStatusHistory(CiLabelStatusHistory var1);

    CiLabelStatusHistory selectCiLabelStatusHistoryById(Integer var1);

    List<CiLabelStatusHistory> selectCiLabelStatusHistoryListByNotice();
}
