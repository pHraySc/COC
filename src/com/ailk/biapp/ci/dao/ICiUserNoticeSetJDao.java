package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiSysAnnouncement;
import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;

public interface ICiUserNoticeSetJDao {
    List<String> selectUserNoticeSetForUserId(CiSysAnnouncement var1) throws CIServiceException;
}
