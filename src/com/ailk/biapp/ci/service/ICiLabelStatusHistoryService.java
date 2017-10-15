package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiLabelStatusHistory;
import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;

public interface ICiLabelStatusHistoryService {
    void addCiLabelStatusHistory(CiLabelStatusHistory var1) throws CIServiceException;

    void modifyCiLabelStatusHistory(CiLabelStatusHistory var1) throws CIServiceException;

    List<CiLabelStatusHistory> queryCiLabelStatusHistoryList() throws CIServiceException;

    void addSysAnnouncemnetByLabelIds();

    int modifyBatchCiLabelStatusHistory(String var1) throws CIServiceException;
}
