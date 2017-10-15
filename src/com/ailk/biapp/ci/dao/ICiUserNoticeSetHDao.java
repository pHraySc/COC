package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiUserNoticeSet;
import com.ailk.biapp.ci.entity.DimAnnouncementType;
import com.ailk.biapp.ci.entity.DimNoticeSendMode;
import com.ailk.biapp.ci.entity.DimNoticeType;
import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;

public interface ICiUserNoticeSetHDao {
    void insertUserNoticeSet(List<CiUserNoticeSet> var1) throws CIServiceException;

    void addUserNoticeSet(CiUserNoticeSet var1) throws CIServiceException;

    List<CiUserNoticeSet> selectUserNoticeSet(String var1) throws CIServiceException;

    List<CiUserNoticeSet> selectUserNoticeSet(String var1, String var2) throws CIServiceException;

    List<CiUserNoticeSet> selectUserNoticeOpenClose(String var1, String var2, Integer var3, Integer var4, String var5) throws CIServiceException;

    List<DimAnnouncementType> selectDimAnnouncementType() throws CIServiceException;

    List<DimNoticeType> selectDimNoticeType() throws CIServiceException;

    List<CiUserNoticeSet> selectUserNoticeSetInfo(String var1, String var2, int var3) throws CIServiceException;

    List<DimNoticeSendMode> selectdimNoticeSendMode() throws CIServiceException;
}
