package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiUserReadInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;

public interface ICiUserReadInfoService {
    void deleteCiUserReadInfo(CiUserReadInfo var1) throws CIServiceException;

    void addCiUserReadInfo(CiUserReadInfo var1) throws CIServiceException;

    void delete(List<String> var1, String var2) throws CIServiceException;
}
