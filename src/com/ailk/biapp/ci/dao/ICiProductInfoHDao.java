package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiProductInfo;
import java.util.List;

public interface ICiProductInfoHDao {
    List<CiProductInfo> selectChildrenCiProductInfo(Integer var1);

    List<CiProductInfo> selectCategoryCiProductInfo(Integer var1, Integer var2);

    List<CiProductInfo> selectEffectiveCiProductInfo();
}
