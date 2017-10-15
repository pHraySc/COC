package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiSysAnnouncement;
import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;

public interface ICiSysAnnouncementService {
    int querySysAnnouncementCount(CiSysAnnouncement var1) throws CIServiceException;

    List<CiSysAnnouncement> querySysAnnouncementList(int var1, int var2, CiSysAnnouncement var3) throws CIServiceException;

    boolean addSysAnnouncement(CiSysAnnouncement var1) throws CIServiceException;

    boolean modifySysAnnouncement(CiSysAnnouncement var1) throws CIServiceException;

    boolean deleteSysAnnouncement(CiSysAnnouncement var1) throws CIServiceException;

    boolean batchDeleteSysAnnouncementList(List<String> var1) throws CIServiceException;

    List<CiSysAnnouncement> querySysAnnouncementListBySysAnnouncement(CiSysAnnouncement var1) throws CIServiceException;

    CiSysAnnouncement selectCiSysAnnouncementById(String var1) throws CIServiceException;

    int querySysAnnouncementCountByUserId(String var1, CiSysAnnouncement var2) throws CIServiceException;

    List<CiSysAnnouncement> querySysAnnouncementListByUserId(String var1, CiSysAnnouncement var2, int var3, int var4) throws CIServiceException;

    List<CiSysAnnouncement> queryCiUserReadInfoListByUserId(String var1, CiSysAnnouncement var2, int var3, int var4) throws CIServiceException;

    int queryCiUserReadInfoCountByUserId(String var1, CiSysAnnouncement var2) throws CIServiceException;
}
