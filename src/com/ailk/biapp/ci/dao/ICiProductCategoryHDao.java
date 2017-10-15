package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiProductCategory;
import java.util.List;

public interface ICiProductCategoryHDao {
    List<CiProductCategory> selectProductFirstCategoryList();

    List<CiProductCategory> selectProductSecondCategory(Integer var1);
}
