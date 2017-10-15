package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.service.ICiLabelStatusHistoryService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class LabelSysAnnouncementJob {
    private Logger log = Logger.getLogger(LabelSysAnnouncementJob.class);
    @Autowired
    private ICiLabelStatusHistoryService ciLabelStatusHistoryService;

    public LabelSysAnnouncementJob() {
    }

    public void refreshLabelSysAnnouncement() {
        this.log.info("LabelSysAnnouncementJob- start");
        this.ciLabelStatusHistoryService.addSysAnnouncemnetByLabelIds();
        this.log.info("LabelSysAnnouncementJob- end");
    }
}
