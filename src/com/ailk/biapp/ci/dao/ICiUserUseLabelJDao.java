package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.model.LabelDetailInfo;
import com.ailk.biapp.ci.model.LabelTrendInfo;
import com.ailk.biapp.ci.model.LabelUseLogInfo;
import java.util.List;

public interface ICiUserUseLabelJDao {
    long getLabelUseTimesByID(String var1);

    Long getSysRecommendLabelNum();

    List<LabelDetailInfo> getSysRecommendLabel(int var1, int var2, String var3);

    List<LabelDetailInfo> getSysRecommendLabelTask();

    void insertCiLabelDayUseStat(String var1);

    void insertCiLabelMonthUseStat(String var1);

    void deleteCiLabelUseStat(String var1);

    List<LabelTrendInfo> getLabelUseTimesTrendInfo(int var1, int var2, String var3);

    List<LabelTrendInfo> getLabelUseTimesTrendInfo(String var1, String var2, String var3);

    List<LabelUseLogInfo> getLabelUseLogInfos(int var1, int var2, String var3);

    LabelUseLogInfo getLastLabelUseLog(String var1);

    List<LabelDetailInfo> getUserUsedLabel(int var1, int var2, String var3, String var4, String var5);

    long getUserUsedLabelNum(String var1, String var2);
}
