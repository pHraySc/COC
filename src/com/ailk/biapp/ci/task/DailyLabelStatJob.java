package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.service.ILabelStatService;
import com.ailk.biapp.ci.task.AbstractJob;
import com.ailk.biapp.ci.util.cache.CacheBase;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DailyLabelStatJob extends AbstractJob {
    private Logger logger = Logger.getLogger(DailyLabelStatJob.class);
    @Autowired
    private ILabelStatService labelStatService;

    public DailyLabelStatJob() {
    }

    public void work() {
        this.logger.info("DailyLabelStatJob- start");
        String dataDate = CacheBase.getInstance().getNewLabelDay();
        if(!StringUtils.isEmpty(super.getDataTime())) {
            dataDate = super.getDataTime();
        }

        byte updateCycle = 1;
        int dataDateNum = this.labelStatService.dataDateExist(dataDate);
        if(dataDateNum <= 0) {
            this.labelStatService.statLabelCustomNum(dataDate, updateCycle);
            this.labelStatService.updateLabelTotalCustomNum(dataDate, updateCycle);
            this.labelStatService.statLabelRingNum(dataDate, updateCycle);
            this.labelStatService.statLabelProportion(dataDate, updateCycle);
        }

        try {
            CacheBase.getInstance().init(false);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        this.logger.info("DailyLabelStatJob- exit");
    }
}
