package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiLabelBrandUserNum;
import com.ailk.biapp.ci.model.LabelShortInfo;
import com.ailk.biapp.ci.model.LabelTrendInfo;
import com.ailk.biapp.ci.model.LabelTrendTableInfo;
import java.util.List;

public interface ICiLabelBrandUserNumJDao {
    String getLabelBrandUserNumById(String var1, String var2);

    LabelShortInfo getLabelBrandUserNum(String var1, String var2);

    List<LabelTrendInfo> getLabelUserNumsTrendInfo(int var1, int var2, String var3);

    List<LabelTrendInfo> getLabelUserNumsTrendInfo(String var1, String var2, String var3);

    List<LabelTrendTableInfo> getLabelTrendTableInfo(int var1, int var2, String var3);

    List<LabelTrendTableInfo> getLabelTrendTableInfo(int var1, int var2, String var3, String var4, String var5);

    long getLabelTrednTableInfoNums(String var1, String var2, String var3);

    List<CiLabelBrandUserNum> getLabelBrandUserNumListByDataDate(String var1);

    List<CiLabelBrandUserNum> getAllCityLabelBrandUserNumListByDataDate(String var1);

    List<CiLabelBrandUserNum> getLabelBrandUserNumList(CiLabelBrandUserNum var1);
}
