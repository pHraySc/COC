package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiLabelBrandUserNum;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICiLabelBrandUserNumService {
    List<CiLabelBrandUserNum> getLabelBrandUserNumListByDataDate(String var1);

    List<CiLabelBrandUserNum> getAllCityLabelBrandUserNumListByDataDate(String var1);

    Map<String, Map<String, CiLabelBrandUserNum>> getUserIdAndlabelIdToLabelStatsMap(List<CiLabelBrandUserNum> var1, Set<String> var2);

    List<CiLabelBrandUserNum> getLabelBrandUserNumList(CiLabelBrandUserNum var1);
}
