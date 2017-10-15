package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.model.ProductDetailInfo;
import java.util.List;

public interface ICiProductInfoJDao {
    List<ProductDetailInfo> getEffectiveProduct(int var1, int var2, String var3, String var4, Integer var5, String var6);

    long getEffectiveProductNum(String var1, Integer var2, String var3);
}
