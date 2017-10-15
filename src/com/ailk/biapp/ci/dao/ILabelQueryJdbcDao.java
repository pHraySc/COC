package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiUserAttentionLabelId;
import com.ailk.biapp.ci.model.LabelDetailInfo;
import com.ailk.biapp.ci.model.LabelShortInfo;
import com.ailk.biapp.ci.model.LabelTrendInfo;
import com.ailk.biapp.ci.model.LabelTrendTableInfo;
import com.ailk.biapp.ci.model.Pager;
import java.util.List;

public interface ILabelQueryJdbcDao {
    List<LabelShortInfo> getUserLabelByUserId(String var1);

    List<LabelShortInfo> getUserLabelByUserId(String var1, String var2);

    List<LabelShortInfo> getLabelByDeptId(Integer var1);

    List<LabelShortInfo> getLabelByDeptId(Integer var1, String var2);

    List<LabelShortInfo> getUserAttentionLabel(String var1);

    List<CiUserAttentionLabelId> queryRecordByLabelId(String var1);

    List<LabelShortInfo> getUserAttentionLabel(String var1, String var2);

    List<LabelDetailInfo> getUserAttentionLabel(int var1, int var2, String var3, String var4, String var5);

    long getUserAttentionLabelNum(String var1, String var2);

    List<LabelDetailInfo> getNewPublishLabel(int var1, int var2, String var3, String var4);

    long getNewPublishLabelNum(String var1);

    LabelDetailInfo getLabelDetailInfo(String var1);

    long getLabelUseTimesByID(String var1);

    List<LabelTrendInfo> getLabelUserNumsTrendInfo(int var1, int var2, String var3);

    List<LabelTrendInfo> getLabelUseTimesTrendInfo(int var1, int var2, String var3);

    List<LabelTrendTableInfo> getLabelTrendTableInfo(int var1, int var2, String var3);

    boolean isNewLabel(String var1);

    List<LabelDetailInfo> getEffectiveLabel(Pager var1, String var2, String var3, Integer var4, String var5, CiLabelInfo var6) throws Exception;

    long getUserEffectiveLabelNum(String var1, Integer var2, String var3, CiLabelInfo var4) throws Exception;

    List<LabelDetailInfo> getSysRecommendLabelInfoList(CiLabelInfo var1) throws Exception;

    LabelDetailInfo getSysRecommendLabelInfo(Integer var1) throws Exception;

    List<LabelDetailInfo> getEffectiveLabelByLabelId(Integer var1) throws Exception;
}
