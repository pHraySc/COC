package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.LabelShortInfo;
import java.util.List;

public interface ICiLabelContrastAnalysisService {
    void addCiUserLabelContrast(String var1, String var2, String var3) throws CIServiceException;

    void deleteUserLabelContrast(String var1, String var2, String var3) throws CIServiceException;

    void deleteCiUserLabelContrast(String var1, String var2) throws CIServiceException;

    LabelShortInfo queryLabelBrandUserNum(String var1, String var2) throws CIServiceException;

    String queryLabelBrandUserNumById(String var1, String var2) throws CIServiceException;

    int queryCountUserLabelContrast(String var1, String var2) throws CIServiceException;

    List<LabelShortInfo> queryCiUserLabelContrastList(String var1, String var2, String var3) throws CIServiceException;

    List<LabelShortInfo> queryCiUserLabelContrastListNoData(String var1, String var2) throws CIServiceException;
}
