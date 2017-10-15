package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.service.ICiLabelUserUseService;
import com.ailk.biapp.ci.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LabelUseStatJob {
    private Logger logger = Logger.getLogger(LabelUseStatJob.class);
    @Autowired
    public ICiLabelUserUseService ciLabelUserUseService;

    public LabelUseStatJob() {
    }

    public ICiLabelUserUseService getCiLabelUserUseService() {
        return this.ciLabelUserUseService;
    }

    public void setCiLabelUserUseService(ICiLabelUserUseService ciLabelUserUseService) {
        this.ciLabelUserUseService = ciLabelUserUseService;
    }

    public void monthCount() {
        this.logger.info("LabelUseStatJob.monthCount() Method Start:");
        String dataDate = DateUtil.getLastMonthStr();
        this.logger.info("LabelUseStatJob Month Count DataDate=" + dataDate);
        this.ciLabelUserUseService.insertCiLabelMonthUseStat(dataDate);
        this.logger.info("LabelUseStatJob.monthCount() Method End");
    }

    public void dayCount() {
        this.logger.info("LabelUseStatJob.dayCount() Method Start:");
        String dataDate = DateUtil.getLastDate();
        this.logger.info("LabelUseStatJob Day Count DataDate=" + dataDate);
        this.ciLabelUserUseService.insertCiLabelDayUseStat(dataDate);
        this.logger.info("LabelUseStatJob.dayCount() Method End");
    }
}
