package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.CiLabelRelModel;
import java.util.List;

public interface ICiLabelRelAnalysisService {
    void queryMainLabelUserNum(CiLabelRelModel var1) throws CIServiceException;

    void queryRelLabelData(CiLabelRelModel var1) throws CIServiceException;

    List<CiLabelRelModel> queryRelLabelByMainLabel(String var1, CiLabelRelModel var2) throws CIServiceException;

    void addUserRelLabel(String var1, CiLabelRelModel var2) throws CIServiceException;

    void deleteUserRelLabel(String var1, CiLabelRelModel var2) throws CIServiceException;

    void addRelLabelAsCustomGroup(String var1, CiLabelRelModel var2, CiCustomGroupInfo var3) throws CIServiceException;

    Long queryCustomNumByLabelId(Integer var1, String var2) throws CIServiceException;

    List<CiLabelRelModel> queryRelLabelByDate(CiLabelRelModel var1) throws CIServiceException;

    List<CiLabelRelModel> queryLabelRel(String var1, CiLabelRelModel var2) throws CIServiceException;
}
