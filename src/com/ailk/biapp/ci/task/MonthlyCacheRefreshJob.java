package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.service.ICustomersListInfoService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.service.IDimCocLabelStatusService;
import com.ailk.biapp.ci.service.ILabelStatService;
import com.ailk.biapp.ci.task.MonthlyCustomersJob;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonthlyCacheRefreshJob {
    private Logger logger = Logger.getLogger(MonthlyCustomersJob.class);
    @Autowired
    private IDimCocLabelStatusService dimCocLabelStatusService;
    @Autowired
    private ILabelStatService labelStatService;
    @Autowired
    private ICustomersManagerService customersService;
    @Autowired
    private ICustomersListInfoService customersListInfoService;

    public MonthlyCacheRefreshJob() {
    }

    public void createCustomers(String cityIds) {
        String dataDate = CacheBase.getInstance().getNewLabelMonth();
        this.logger.info("MonthlyCustomersJob- start " + dataDate);
        Date newLabelDate = DateUtil.string2Date(dataDate, "yyyyMM");
        List ciCustomGroupInfos = this.customersService.findCiCustomGroupInfo4CycleCreate(2, newLabelDate, cityIds);
        this.logger.info("月周期的客户群" + dataDate + ",需要创建的客户群数" + ciCustomGroupInfos.size());
        if(ciCustomGroupInfos != null && ciCustomGroupInfos.size() > 0) {
            this.logger.debug("ciCustomGroupInfos size:" + ciCustomGroupInfos.size());
            Iterator i$ = ciCustomGroupInfos.iterator();

            while(i$.hasNext()) {
                CiCustomGroupInfo ciCustomGroupInfo = (CiCustomGroupInfo)i$.next();

                try {
                    this.logger.info("月周期的客户群开始生成，ID为：" + ciCustomGroupInfo.getCustomGroupId());
                    this.customersService.generateNewPeriodData(ciCustomGroupInfo, dataDate);
                    Thread.sleep(5000L);
                } catch (Exception var8) {
                    this.logger.error(var8);
                }
            }
        }

        this.logger.info("MonthlyCustomersJob- exit " + dataDate);
    }

    public void refreshLabelNumAndCreateCustomList() {
        String cycleCustomWebservice = Configure.getInstance().getProperty("CYCLE_CUSTOM_WEBSERVICE");
        if(StringUtil.isEmpty(cycleCustomWebservice) || "false".equalsIgnoreCase(cycleCustomWebservice)) {
            CacheBase cache = CacheBase.getInstance();

            //刷新CI_NEWEST_LABEL_DATE到缓存
            cache.refreshNewestDayAndMonth();

            String dataDate = cache.getNewLabelMonth();
            byte updateCycle = 2;

            int status = cache.getNewLabelMonthStatus().intValue();

            String LabelUpdateInBatch = Configure.getInstance().getProperty("LABEL_UPDATE_IN_BATCHES");
            boolean ifLabelUpdateInBatch = false;
            if(StringUtil.isNotEmpty(LabelUpdateInBatch) && "true".equalsIgnoreCase(LabelUpdateInBatch)) {
                ifLabelUpdateInBatch = true;
            }

            if(status == 0) {


                //CI_NEWEST_LABEL_DATE 更新状态为1
                this.labelStatService.updateLabelStatDateStatus(updateCycle);

                //更新管理员所以看到的客户数（ext表中的customNum）
              this.labelStatService.updateLabelTotalCustomNum(dataDate, updateCycle);

                if(ifLabelUpdateInBatch) {
                    try {

                        //更新标签日期
                        List e = this.dimCocLabelStatusService.queryDimCocLabelStatusList(dataDate);
                        int row = this.labelStatService.updateCiLabelInfoDataDate(e, dataDate);

                        this.logger.debug("更新标签最新月记录数：" + row);
                    } catch (Exception var11) {
                        this.logger.error("更新标签最新月统计周期异常", var11);
                        var11.printStackTrace();
                    }
                }


                //更新前台缓存和刷新客户群
                cache.initAllEffectiveLabel();
                this.createCustomers((String)null);


                try {
                    //CI_LIST_TABLE_EXPIRATION_TIME配置为-1
                    int e1 = Integer.valueOf(Configure.getInstance().getProperty("CI_LIST_TABLE_EXPIRATION_TIME")).intValue();
                    this.customersListInfoService.removeCustomerListBeforeMonth(e1);
                } catch (Exception var10) {
                    this.logger.warn("invalid CI_LIST_TABLE_EXPIRATION_TIME :" + var10.getMessage());
                }
            }
        }

    }
}
