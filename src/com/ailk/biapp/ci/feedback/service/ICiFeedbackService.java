package com.ailk.biapp.ci.feedback.service;

import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.feedback.entity.CiAttachmentFileInfo;
import com.ailk.biapp.ci.feedback.entity.CiFeedbackInfo;
import com.ailk.biapp.ci.feedback.entity.CiFeedbackRecordInfo;
import com.ailk.biapp.ci.feedback.entity.DimDeclareDealStatus;
import com.ailk.biapp.ci.model.TreeNode;
import java.io.File;
import java.util.List;

public interface ICiFeedbackService {
    CiFeedbackInfo queryByFeedbackInfoById(Integer var1) throws CIServiceException;

    void modifyFeedbackByFeedbackRecord() throws CIServiceException;

    void modifyFeedbackStatus() throws CIServiceException;

    int queryTotolPageCount(CiFeedbackInfo var1) throws CIServiceException;

    int queryHistoryTotalPageCount(CiFeedbackInfo var1) throws CIServiceException;

    List<CiFeedbackInfo> queryPageList(int var1, int var2, CiFeedbackInfo var3) throws CIServiceException;

    List<CiFeedbackInfo> queryHistoryPageList(int var1, int var2, CiFeedbackInfo var3) throws CIServiceException;

    List<CiFeedbackRecordInfo> queryFeedbackRecord(Integer var1) throws CIServiceException;

    void insertFeedbackInfo(CiFeedbackInfo var1) throws CIServiceException;

    void insertRecordInfo(CiFeedbackRecordInfo var1) throws CIServiceException;

    List<DimDeclareDealStatus> queryDeclareDealStatus() throws CIServiceException;

    void statusAddToReply(Integer var1) throws CIServiceException;

    void statusAddToCancel(Integer var1) throws CIServiceException;

    void statusReplyToFinish(Integer var1) throws CIServiceException;

    void statusFinishToClose(Integer var1) throws CIServiceException;

    boolean hasSameFeedback(CiFeedbackInfo var1) throws CIServiceException;

    String uploadFile(File var1, String var2) throws CIServiceException;

    CiAttachmentFileInfo queryAttachmentFileById(Integer var1) throws CIServiceException;

    List<TreeNode> queryCustomerGroupTree(String var1) throws CIServiceException;

    void cleanInvalidAttachement() throws CIServiceException;

    void deleteFeedbackInfo(CiFeedbackInfo var1) throws CIServiceException;
}
