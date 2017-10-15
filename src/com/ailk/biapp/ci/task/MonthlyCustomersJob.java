package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.service.ICustomersListInfoService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.task.AbstractJob;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;
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
public class MonthlyCustomersJob extends AbstractJob {
    private Logger logger = Logger.getLogger(MonthlyCustomersJob.class);
    @Autowired
    private ICustomersManagerService customersService;
    @Autowired
    private ICustomersListInfoService customersListInfoService;

    public MonthlyCustomersJob() {
    }

    public void work() {
        this.logger.info("MonthlyCustomersJob- start " + super.getDataTime());
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        c.add(2, -1);
        List ciCustomGroupInfos = this.customersService.findCiCustomGroupInfo4CycleCreate(2, now, (String)null);
        if(StringUtils.isEmpty(super.getDataTime())) {
            super.setDataTime(CacheBase.getInstance().getNewLabelMonth());
        }

        this.logger.info("月周期的客户群" + super.getDataTime() + ",需要创建的客户群数" + ciCustomGroupInfos.size());
        if(ciCustomGroupInfos != null && ciCustomGroupInfos.size() > 0) {
            this.logger.debug("ciCustomGroupInfos size:" + ciCustomGroupInfos.size());
            Iterator e = ciCustomGroupInfos.iterator();

            while(e.hasNext()) {
                CiCustomGroupInfo ciCustomGroupInfo = (CiCustomGroupInfo)e.next();
                this.customersService.generateNewPeriodData(ciCustomGroupInfo, super.getDataTime());
            }
        }

        try {
            int e1 = Integer.valueOf(Configure.getInstance().getProperty("CI_LIST_TABLE_EXPIRATION_TIME")).intValue();
            this.customersListInfoService.removeCustomerListBeforeMonth(e1);
            CacheBase.getInstance().init(false);
        } catch (Exception var6) {
            this.logger.warn("invalid CI_LIST_TABLE_EXPIRATION_TIME :" + var6.getMessage());
        }

        this.logger.info("MonthlyCustomersJob- exit " + super.getDataTime());
    }
}
