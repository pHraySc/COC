package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.LabelTrendInfo;
import com.ailk.biapp.ci.model.LabelTrendTableInfo;
import java.util.List;

public interface ICiLabelTrendAnalysisService {
    List<LabelTrendInfo> queryLabelUserNumsTrendInfo(int var1, int var2, String var3) throws CIServiceException;

    List<LabelTrendTableInfo> queryLabelTrendTableInfo(int var1, int var2, String var3) throws CIServiceException;

    String queryLabelBrandUserNumById(String var1, String var2) throws CIServiceException;

    List<LabelTrendInfo> queryLabelUseTimesTrendInfo(int var1, int var2, String var3) throws CIServiceException;

    List<LabelTrendInfo> queryLabelUserNumsTrendInfo(String var1, String var2, String var3) throws CIServiceException;

    List<LabelTrendTableInfo> queryLabelTrendTableInfo(int var1, int var2, String var3, String var4, String var5) throws CIServiceException;

    List<LabelTrendInfo> queryLabelUseTimesTrendInfo(String var1, String var2, String var3) throws CIServiceException;

    long queryLabelTrednTableInfoNums(String var1, String var2, String var3) throws CIServiceException;
}
