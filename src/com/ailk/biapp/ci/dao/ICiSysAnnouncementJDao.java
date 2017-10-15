package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiSysAnnouncement;
import java.util.List;

public interface ICiSysAnnouncementJDao {
    int selectSysAnnouncementCount(CiSysAnnouncement var1) throws Exception;

    List<CiSysAnnouncement> selectSysAnnouncementList(int var1, int var2, CiSysAnnouncement var3) throws Exception;

    int selectSysAnnouncementCountByUserId(String var1, CiSysAnnouncement var2) throws Exception;

    List<CiSysAnnouncement> selectSysAnnouncementListByUserId(String var1, CiSysAnnouncement var2, int var3, int var4) throws Exception;

    List<CiSysAnnouncement> selectCiUserReadInfoListByUserId(String var1, CiSysAnnouncement var2, int var3, int var4) throws Exception;

    int selectCiUserReadInfoCountByUserId(String var1, CiSysAnnouncement var2) throws Exception;

    List<CiSysAnnouncement> selectCiUserNotReadInfoListByUserId(String var1) throws Exception;
}
