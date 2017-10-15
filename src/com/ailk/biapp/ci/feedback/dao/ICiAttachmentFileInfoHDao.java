package com.ailk.biapp.ci.feedback.dao;

import com.ailk.biapp.ci.feedback.entity.CiAttachmentFileInfo;
import java.util.List;

public interface ICiAttachmentFileInfoHDao {
    void insertCiAttachmentFileInfo(CiAttachmentFileInfo var1);

    List<CiAttachmentFileInfo> selectByRecordId(Integer var1);

    CiAttachmentFileInfo selectByFileId(Integer var1);
}
