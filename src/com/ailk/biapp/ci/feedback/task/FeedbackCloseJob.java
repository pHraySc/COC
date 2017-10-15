package com.ailk.biapp.ci.feedback.task;

import com.ailk.biapp.ci.feedback.service.ICiFeedbackService;
import com.asiainfo.biframe.utils.config.Configure;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackCloseJob {
    private static Logger log = Logger.getLogger(FeedbackCloseJob.class);
    @Autowired
    private ICiFeedbackService ciFeedbackService;

    public FeedbackCloseJob() {
    }

    public void closeCiFeedbackInfo() {
        if(Boolean.valueOf(Configure.getInstance().getProperty("FEEDBACK_MENU")).booleanValue()) {
            this.ciFeedbackService.modifyFeedbackByFeedbackRecord();
            this.ciFeedbackService.modifyFeedbackStatus();
            this.ciFeedbackService.cleanInvalidAttachement();
        }

    }
}
