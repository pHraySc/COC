package com.ailk.biapp.ci.feedback.dao;

import com.ailk.biapp.ci.feedback.entity.CiFeedbackInfo;
import java.util.List;

public interface ICiFeedbackInfoHDao {
    void insertOrUpdateCiFeedbackInfo(CiFeedbackInfo var1);

    List<CiFeedbackInfo> selectFeedbackInfoByUserId(String var1);

    CiFeedbackInfo selectFeedbackInfoById(Integer var1);

    List<CiFeedbackInfo> selectByUserIdAndLabelCustomId(CiFeedbackInfo var1);

    void deleteFeedbackInfo(CiFeedbackInfo var1);
}
