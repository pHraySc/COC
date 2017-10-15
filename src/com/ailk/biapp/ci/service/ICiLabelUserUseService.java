package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.model.LabelUseLogInfo;
import java.util.List;

public interface ICiLabelUserUseService {
    void addLabelUserUseLog(String var1, int var2);

    long getLabelUseTimesByID(String var1);

    List<LabelUseLogInfo> queryLabelUseLogInfos(int var1, int var2, String var3);

    String queryLastLabelUseLogDesc(String var1);

    void insertCiLabelMonthUseStat(String var1);

    void insertCiLabelDayUseStat(String var1);
}
