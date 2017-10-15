package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiTaskServerCityRel;
import java.util.List;

public interface ICiTaskServerCityRelHDao {
    List<CiTaskServerCityRel> selectListByServerId(String var1);

    List<CiTaskServerCityRel> selectAllList();

    CiTaskServerCityRel selectById(Integer var1);
}
