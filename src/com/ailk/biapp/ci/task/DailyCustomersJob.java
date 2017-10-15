package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.task.AbstractJob;
import com.ailk.biapp.ci.util.cache.CacheBase;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class DailyCustomersJob extends AbstractJob {
    private Logger logger = Logger.getLogger(DailyCustomersJob.class);
    @Autowired
    private ICustomersManagerService customersService;

    public DailyCustomersJob() {
    }

    public void work() {
        this.logger.info("DailyCustomersJob-start " + super.getDataTime());
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        List ciCustomGroupInfos = this.customersService.findCiCustomGroupInfo4CycleCreate(3, now, (String)null);
        if(StringUtils.isEmpty(super.getDataTime())) {
            super.setDataTime(CacheBase.getInstance().getNewLabelDay());
        }

        this.logger.info("日周期的客户群" + super.getDataTime() + ",需要创建的客户群数" + ciCustomGroupInfos.size());
        if(ciCustomGroupInfos != null && ciCustomGroupInfos.size() > 0) {
            this.logger.debug("ciCustomGroupInfos size:" + ciCustomGroupInfos.size());
            Iterator i$ = ciCustomGroupInfos.iterator();

            while(i$.hasNext()) {
                CiCustomGroupInfo ciCustomGroupInfo = (CiCustomGroupInfo)i$.next();
                this.customersService.generateNewPeriodData(ciCustomGroupInfo, super.getDataTime());
            }
        }

        this.logger.info("DailyCustomersJob-exit " + super.getDataTime());
    }
}
