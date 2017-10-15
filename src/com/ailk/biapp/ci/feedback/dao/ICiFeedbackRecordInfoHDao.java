package com.ailk.biapp.ci.feedback.dao;

import com.ailk.biapp.ci.feedback.entity.CiFeedbackRecordInfo;
import java.util.List;

public interface ICiFeedbackRecordInfoHDao {
    void insertCiFeedbackRecordInfo(CiFeedbackRecordInfo var1);

    List<CiFeedbackRecordInfo> selectByFeedbackInfoId(Integer var1);
}
