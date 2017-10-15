package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.model.LabelShortInfo;
import java.util.List;

public interface ICiUserLabelRelJDao {
    List<LabelShortInfo> getCiUserLabelRelList(String var1, Integer var2);

    List<LabelShortInfo> getLabelRelRecommend(int var1, int var2, Integer var3);

    List<LabelShortInfo> getLabelRelRecommend(int var1, int var2, Integer var3, String var4);
}
