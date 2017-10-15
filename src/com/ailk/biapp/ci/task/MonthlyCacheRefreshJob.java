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
        this.logger.info("�����ڵĿͻ�Ⱥ" + dataDate + ",��Ҫ�����Ŀͻ�Ⱥ��" + ciCustomGroupInfos.size());
        if(ciCustomGroupInfos != null && ciCustomGroupInfos.size() > 0) {
            this.logger.debug("ciCustomGroupInfos size:" + ciCustomGroupInfos.size());
            Iterator i$ = ciCustomGroupInfos.iterator();

            while(i$.hasNext()) {
                CiCustomGroupInfo ciCustomGroupInfo = (CiCustomGroupInfo)i$.next();

                try {
                    this.logger.info("�����ڵĿͻ�Ⱥ��ʼ���ɣ�IDΪ��" + ciCustomGroupInfo.getCustomGroupId());
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

            //ˢ��CI_NEWEST_LABEL_DATE������
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


                //CI_NEWEST_LABEL_DATE ����״̬Ϊ1
                this.labelStatService.updateLabelStatDateStatus(updateCycle);

                //���¹���Ա���Կ����Ŀͻ�����ext���е�customNum��
              this.labelStatService.updateLabelTotalCustomNum(dataDate, updateCycle);

                if(ifLabelUpdateInBatch) {
                    try {

                        //���±�ǩ����
                        List e = this.dimCocLabelStatusService.queryDimCocLabelStatusList(dataDate);
                        int row = this.labelStatService.updateCiLabelInfoDataDate(e, dataDate);

                        this.logger.debug("���±�ǩ�����¼�¼����" + row);
                    } catch (Exception var11) {
                        this.logger.error("���±�ǩ������ͳ�������쳣", var11);
                        var11.printStackTrace();
                    }
                }


                //����ǰ̨�����ˢ�¿ͻ�Ⱥ
                cache.initAllEffectiveLabel();
                this.createCustomers((String)null);


                try {
                    //CI_LIST_TABLE_EXPIRATION_TIME����Ϊ-1
                    int e1 = Integer.valueOf(Configure.getInstance().getProperty("CI_LIST_TABLE_EXPIRATION_TIME")).intValue();
                    this.customersListInfoService.removeCustomerListBeforeMonth(e1);
                } catch (Exception var10) {
                    this.logger.warn("invalid CI_LIST_TABLE_EXPIRATION_TIME :" + var10.getMessage());
                }
            }
        }

    }
}
