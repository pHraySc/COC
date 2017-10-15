package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.service.ILabelDataTransferService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LabelDataInitJob {
    private Logger logger = Logger.getLogger(LabelDataInitJob.class);
    @Autowired
    private ILabelDataTransferService labelDataTransferService;

    public LabelDataInitJob() {
    }

    public void work() {
        this.logger.info("LabelDataInitJob- start");
        this.labelDataTransferService.importLabelInfo();
        this.logger.info("LabelDataInitJob- exit");
    }
}
