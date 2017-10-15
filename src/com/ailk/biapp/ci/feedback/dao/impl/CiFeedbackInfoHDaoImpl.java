package com.ailk.biapp.ci.feedback.dao.impl;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.feedback.dao.ICiFeedbackInfoHDao;
import com.ailk.biapp.ci.feedback.entity.CiFeedbackInfo;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiFeedbackInfoHDaoImpl extends HibernateBaseDao<CiFeedbackInfo, Integer> implements ICiFeedbackInfoHDao {
    public CiFeedbackInfoHDaoImpl() {
    }

    public void insertOrUpdateCiFeedbackInfo(CiFeedbackInfo ciFeedbackInfo) {
        this.save(ciFeedbackInfo);
    }

    public List<CiFeedbackInfo> selectFeedbackInfoByUserId(String userId) {
        List feedbackInfolist = this.findBy("userId", userId);
        return feedbackInfolist;
    }

    public CiFeedbackInfo selectFeedbackInfoById(Integer feedbackInfoId) {
        CiFeedbackInfo feedbackInfo = (CiFeedbackInfo)this.get(feedbackInfoId);
        return feedbackInfo;
    }

    public List<CiFeedbackInfo> selectByUserIdAndLabelCustomId(CiFeedbackInfo ciFeedbackInfo) {
        List list = null;
        list = this.find("from \tCiFeedbackInfo c where c.userId = ? and c.labelCustomId = ? and c.dealStatusId <> ? and c.dealStatusId <>?", new Object[]{ciFeedbackInfo.getUserId(), ciFeedbackInfo.getLabelCustomId(), Integer.valueOf(ServiceConstants.FEEDBACK_DEAL_STATUS_CANCEL), Integer.valueOf(ServiceConstants.FEEDBACK_DEAL_STATUS_CLOSE)});
        return list;
    }

    public void deleteFeedbackInfo(CiFeedbackInfo ciFeedbackInfo) {
        this.delete(ciFeedbackInfo);
    }
}
