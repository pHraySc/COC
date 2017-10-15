package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.DateUtil;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentCustomerJob {
    private Logger logger = Logger.getLogger(AppointmentCustomerJob.class);
    @Autowired
    private ICustomersManagerService customersService;

    public AppointmentCustomerJob() {
    }

    public void creatCustomerList() {
        this.logger.debug("AppointmentCustomerJob-start ");
        Date nowDate = new Date();
        List customGroupInfoList = this.customersService.findCiCustomGroupInfo4TimingCreate(nowDate);
        String ciCustomExeTime = DateUtil.date2String(nowDate, "yyyy-MM-dd HH:mm:ss");
        this.customersService.updateCiCustomExeTime(ciCustomExeTime);
        this.logger.info("当前时段要创建清单的的客户群数" + customGroupInfoList.size());
        if(customGroupInfoList != null && customGroupInfoList.size() > 0) {
            this.logger.debug("customGroupInfoList size:" + customGroupInfoList.size());
            Iterator i$ = customGroupInfoList.iterator();

            while(i$.hasNext()) {
                CiCustomGroupInfo ciCustomGroupInfo = (CiCustomGroupInfo)i$.next();

                try {
                    this.logger.debug("开始跑定时执行的客户群，客户群ID为:" + ciCustomGroupInfo.getCustomGroupId());
                    this.customersService.generateNewTimingData(ciCustomGroupInfo);
                    Thread.sleep(5000L);
                } catch (Exception var7) {
                    this.logger.error(var7);
                }
            }
        }

        this.logger.debug("AppointmentCustomerJob-exit ");
    }
}
