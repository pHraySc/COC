package com.ailk.biapp.ci.feedback.dao;

import com.ailk.biapp.ci.feedback.entity.CiFeedbackRecordInfo;
import java.util.List;

public interface ICiFeedbackRecordInfoJDao {
    List<CiFeedbackRecordInfo> getFeedbackRecordInfo() throws Exception;

    List<CiFeedbackRecordInfo> getCancelOrCloseFeedbackList() throws Exception;
}
