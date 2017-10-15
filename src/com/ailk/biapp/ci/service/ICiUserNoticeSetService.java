package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiUserNoticeSet;
import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;

public interface ICiUserNoticeSetService {
    void addUserNoticeSet(List<CiUserNoticeSet> var1) throws CIServiceException;

    void addUserNoticeSet(CiUserNoticeSet var1) throws CIServiceException;

    List<CiUserNoticeSet> queryUserNoticeSet(String var1) throws CIServiceException;

    List<CiUserNoticeSet> queryUserNoticeSet(String var1, String var2, String var3) throws CIServiceException;

    List<CiUserNoticeSet> queryUserNoticeSetInfo(String var1, String var2, int var3) throws CIServiceException;

    List<CiUserNoticeSet> queryUserNoticeSetInit(String var1, String var2) throws CIServiceException;
}
