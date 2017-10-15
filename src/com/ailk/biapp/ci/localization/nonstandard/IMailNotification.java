package com.ailk.biapp.ci.localization.nonstandard;

import com.ailk.biapp.ci.entity.CiPersonNotice;
import com.ailk.biapp.ci.entity.CiSysAnnouncement;
import java.util.List;

public interface IMailNotification {
    boolean personNoticeMail(CiPersonNotice var1) throws Exception;

    boolean sysAnnouncementMail(CiSysAnnouncement var1, List<String> var2) throws Exception;
}
