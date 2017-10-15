package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.DimApproveStatus;
import com.ailk.biapp.ci.entity.DimLabelDataStatus;
import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;

public interface ICiLabelInfoApproveService {
    int queryTotolPageCount(CiLabelInfo var1, int var2) throws CIServiceException;

    List<CiLabelInfo> queryPageList(int var1, int var2, CiLabelInfo var3, int var4) throws CIServiceException;

    List<DimLabelDataStatus> queryDimLabelDataStatus() throws CIServiceException;

    List<DimApproveStatus> queryDimApproveStatus() throws CIServiceException;

    String approvePass(List<Integer> var1, String var2, String var3) throws CIServiceException;

    void approveNotPass(List<Integer> var1, String var2, String var3) throws CIServiceException;
}
