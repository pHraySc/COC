package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiTaskServerCityRel;
import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;

public interface ICiTaskServerCityRelService {
    List<CiTaskServerCityRel> queryTaskServerCityRelListByServerId(String var1) throws CIServiceException;

    List<CiTaskServerCityRel> queryAllTaskServerCityRelList() throws CIServiceException;
}
