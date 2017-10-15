package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiApproveHistory;
import com.ailk.biapp.ci.entity.CiApproveStatus;
import com.ailk.biapp.ci.entity.CiApproveUserInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;

public interface ICiApproveService {
    int queryTotolPageCount(CiApproveStatus var1) throws CIServiceException;

    List<CiApproveStatus> queryPageList(int var1, int var2, CiApproveStatus var3) throws CIServiceException;

    int queryApprovedTotolPageCount(CiApproveHistory var1) throws CIServiceException;

    List<CiApproveHistory> queryApprovedPageList(int var1, int var2, CiApproveHistory var3) throws CIServiceException;

    String approvePass(CiApproveStatus var1, String var2, String var3) throws CIServiceException;

    void approveNotPass(CiApproveStatus var1, String var2, String var3) throws CIServiceException;

    boolean judgeCurrentUserIsApprover(String var1) throws CIServiceException;

    CiApproveStatus queryApproveStatusByStatusId(String var1) throws CIServiceException;

    void insertOrSubmitApprove(CiApproveStatus var1, int var2) throws CIServiceException;

    List<CiApproveUserInfo> queryApproveUserInfoList(String var1) throws CIServiceException;

    CiApproveStatus queryApproveStatusByResourceIdAndProcessId(String var1, String var2) throws CIServiceException;

    List<CiApproveHistory> queryApproveHistoryByResourceIdAndProcessId(String var1, String var2) throws CIServiceException;

    List<CiApproveUserInfo> queryApproveUserInfoByUserId(String var1) throws CIServiceException;

    List<CiApproveUserInfo> queryApproveUserInfoByStatusIdAndDeptId(String var1, String var2) throws CIServiceException;
}
