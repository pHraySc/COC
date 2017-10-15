package com.ailk.biapp.ci.feedback.dao.impl;

import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.feedback.dao.ICiAttachmentFileInfoHDao;
import com.ailk.biapp.ci.feedback.entity.CiAttachmentFileInfo;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiAttachmentFileInfoHDaoImpl extends HibernateBaseDao<CiAttachmentFileInfo, Integer> implements ICiAttachmentFileInfoHDao {
    public CiAttachmentFileInfoHDaoImpl() {
    }

    public void insertCiAttachmentFileInfo(CiAttachmentFileInfo ciAttachmentFileInfo) {
        this.save(ciAttachmentFileInfo);
    }

    public List<CiAttachmentFileInfo> selectByRecordId(Integer recordId) {
        List ciAttachmentFileInfoList = this.findBy("recordId", recordId);
        return ciAttachmentFileInfoList;
    }

    public CiAttachmentFileInfo selectByFileId(Integer fileId) {
        new CiAttachmentFileInfo();
        CiAttachmentFileInfo ciAttachmentFileInfo = (CiAttachmentFileInfo)this.get(fileId);
        return ciAttachmentFileInfo;
    }
}
