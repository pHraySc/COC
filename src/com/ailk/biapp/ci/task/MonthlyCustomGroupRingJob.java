package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.service.ICustomGroupStatService;
import com.ailk.biapp.ci.task.AbstractJob;
import com.ailk.biapp.ci.util.cache.CacheBase;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonthlyCustomGroupRingJob extends AbstractJob {
    private Logger logger = Logger.getLogger(MonthlyCustomGroupRingJob.class);
    @Autowired
    private ICustomGroupStatService customGroupStatService;

    public MonthlyCustomGroupRingJob() {
    }

    public void work() {
        this.logger.info("MonthlyCustomGroupStatJob- start");
        String dataDate = CacheBase.getInstance().getNewLabelMonth();
        if(!StringUtils.isEmpty(super.getDataTime())) {
            dataDate = super.getDataTime();
        }

        byte updateCycle = 2;
        this.customGroupStatService.statCustomGroupRingNum(dataDate, updateCycle);
        this.logger.info("MonthlyCustomGroupStatJob- exit");
    }
}
