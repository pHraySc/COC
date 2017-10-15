package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiSysAnnouncement;
import java.util.List;

public interface ICiSysAnnouncementHDao {
    void insertCiSysAnnouncement(CiSysAnnouncement var1);

    void updateCiSysAnnouncement(CiSysAnnouncement var1);

    void deleteCiSysAnnouncement(CiSysAnnouncement var1);

    List<CiSysAnnouncement> selectCiSysAnnouncementList(CiSysAnnouncement var1);

    CiSysAnnouncement selectCiSysAnnouncementById(String var1);
}
