package com.ailk.biapp.ci.feedback.dao.impl;

import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.feedback.dao.ICiFeedbackRecordInfoHDao;
import com.ailk.biapp.ci.feedback.entity.CiFeedbackRecordInfo;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiFeedbackRecordInfoHDaoImpl extends HibernateBaseDao<CiFeedbackRecordInfo, Integer> implements ICiFeedbackRecordInfoHDao {
    public CiFeedbackRecordInfoHDaoImpl() {
    }

    public void insertCiFeedbackRecordInfo(CiFeedbackRecordInfo ciFeedbackRecordInfo) {
        this.save(ciFeedbackRecordInfo);
    }

    public List<CiFeedbackRecordInfo> selectByFeedbackInfoId(Integer feedbackInfoId) {
        List ciFeedbackRecordInfoList = this.find("from CiFeedbackRecordInfo c where c.feedbackInfoId = ? and c.replyType != null order by replyTime", new Object[]{feedbackInfoId});
        return ciFeedbackRecordInfoList;
    }
}
