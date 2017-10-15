package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiProductCategory;
import com.ailk.biapp.ci.entity.CiProductInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.ProductDetailInfo;
import com.ailk.biapp.ci.model.TreeNode;
import java.util.List;
import java.util.Map;

public interface ICiProductService {
    List<CiProductCategory> queryProductFirstCategoryList() throws CIServiceException;

    List<CiProductCategory> queryProductSecondCategory(Integer var1) throws CIServiceException;

    int queryProductSecondCategoryCount(Integer var1) throws CIServiceException;

    List queryProductPage(int var1, int var2, int var3) throws CIServiceException;

    List<CiProductInfo> queryEffectiveCiProductInfo() throws CIServiceException;

    List<ProductDetailInfo> queryEffectiveCiProductInfo(int var1, int var2, String var3, String var4, Integer var5, String var6) throws CIServiceException;

    long queryEffectiveCiProductInfoNum(String var1, Integer var2, String var3) throws CIServiceException;

    List<TreeNode> queryProductTree() throws CIServiceException;

    List<TreeNode> queryProductTreeByName(String var1) throws CIServiceException;

    Map querySecondCategoryProductPage(int var1) throws CIServiceException;

    List<CiProductInfo> queryProductName(String var1, Integer var2) throws CIServiceException;
}
