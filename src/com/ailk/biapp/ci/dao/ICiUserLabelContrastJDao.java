package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.model.LabelShortInfo;
import java.util.List;

public interface ICiUserLabelContrastJDao {
    List<LabelShortInfo> getLabelContrastRecommend(int var1, int var2, String var3);

    List<LabelShortInfo> getLabelContrastRecommend(int var1, int var2, String var3, String var4);

    List<LabelShortInfo> getCiUserLabelContrastList(String var1, String var2);

    int getCountUserLabelContrast(String var1, String var2);

    List<LabelShortInfo> getCiUserLabelContrastList(String var1, String var2, String var3);
}
