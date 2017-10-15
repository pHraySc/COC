package com.ailk.biapp.ci.feedback.dao;

import com.ailk.biapp.ci.feedback.entity.CiFeedbackInfo;
import java.util.List;

public interface ICiFeedbackInfoJDao {
    int getFeedbackCountBySql(CiFeedbackInfo var1) throws Exception;

    int getFeedbackHistoryCountBySql(CiFeedbackInfo var1) throws Exception;

    List<CiFeedbackInfo> getFeedbackPageListBySql(int var1, int var2, CiFeedbackInfo var3) throws Exception;

    List<CiFeedbackInfo> getFeedbackHistorytListBySql(int var1, int var2, CiFeedbackInfo var3) throws Exception;
}
