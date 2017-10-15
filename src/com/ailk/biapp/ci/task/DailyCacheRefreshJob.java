package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.service.IDimCocLabelStatusService;
import com.ailk.biapp.ci.service.ILabelStatService;
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
public class DailyCacheRefreshJob {
    private Logger logger = Logger.getLogger(DailyCacheRefreshJob.class);
    @Autowired
    private IDimCocLabelStatusService dimCocLabelStatusService;
    @Autowired
    private ILabelStatService labelStatService;
    @Autowired
    private ICustomersManagerService customersService;

    public DailyCacheRefreshJob() {
    }

    public void createCustomers(String cityIds) {
        String dataDate = CacheBase.getInstance().getNewLabelDay();
        this.logger.debug("DailyCustomersJob-start " + dataDate);
        Date newLabelDate = DateUtil.string2Date(dataDate, "yyyyMMdd");
        List ciCustomGroupInfos = this.customersService.findCiCustomGroupInfo4CycleCreate(3, newLabelDate, cityIds);
        this.logger.info("�����ڵĿͻ�Ⱥ" + dataDate + ",��Ҫ�����Ŀͻ�Ⱥ��" + ciCustomGroupInfos.size());
        this.logger.info("����id:" + cityIds);
        this.logger.info("ciCustomGroupInfos��" + ciCustomGroupInfos.toString());
        if(ciCustomGroupInfos != null && ciCustomGroupInfos.size() > 0) {
            Iterator i$ = ciCustomGroupInfos.iterator();

            while(i$.hasNext()) {
                CiCustomGroupInfo ciCustomGroupInfo = (CiCustomGroupInfo)i$.next();

                try {
                    this.logger.debug("��ʼ�������ڿͻ�Ⱥ���ͻ�ȺIDΪ:" + ciCustomGroupInfo.getCustomGroupId());
                    if(ciCustomGroupInfo.getIsFirstFailed() != null && ciCustomGroupInfo.getIsFirstFailed().intValue() == 1) {
                        this.customersService.genCusomerList(ciCustomGroupInfo);
                    } else if(StringUtil.isNotEmpty(ciCustomGroupInfo.getListCreateTime())) {
                        this.customersService.generateNewPeriodData(ciCustomGroupInfo, dataDate);
                    } else {
                        this.customersService.generateNewPeriodData(ciCustomGroupInfo, dataDate);
                        this.customersService.delCustomListTableAndListInfoBefore(ciCustomGroupInfo, dataDate);
                    }

                    Thread.sleep(5000L);
                } catch (Exception var8) {
                    this.logger.error(var8);
                }
            }
        }

        this.logger.debug("DailyCustomersJob-exit " + dataDate);
    }

    public void refreshLabelNumAndCreateCustomList() {
        String cycleCustomWebservice = Configure.getInstance().getProperty("CYCLE_CUSTOM_WEBSERVICE");
        if(StringUtil.isEmpty(cycleCustomWebservice) || "false".equalsIgnoreCase(cycleCustomWebservice)) {
            this.logger.debug("DailyCacheRefreshJob   refreshLabelNumAndCreateCustomList-start ");
            CacheBase cache = CacheBase.getInstance();
            cache.refreshNewestDayAndMonth();
            String dataDate = cache.getNewLabelDay();
            byte updateCycle = 1;
            int status = cache.getNewLabelDayStatus().intValue();
            String LabelUpdateInBatch = Configure.getInstance().getProperty("LABEL_UPDATE_IN_BATCHES");
            boolean ifLabelUpdateInBatch = false;
            if(StringUtil.isNotEmpty(LabelUpdateInBatch) && "true".equalsIgnoreCase(LabelUpdateInBatch)) {
                ifLabelUpdateInBatch = true;
            }

            if(status == 0) {
                //����CI_NEWEST_LABEL_DATE��״̬
                this.labelStatService.updateLabelStatDateStatus(updateCycle);

                //����CI_LABEL_EXT_INFO��Ŀ��ͻ���
                this.labelStatService.updateLabelTotalCustomNum(dataDate, updateCycle);

                //ifLabelUpdateInBatch  ���ظò���Ϊtrue
                if(ifLabelUpdateInBatch) {
                    try {
                        //��ѯDIM_COC_LABEL_STATUS����״̬Ϊ1�ı�ǩ
                        List list = this.dimCocLabelStatusService.queryDimCocLabelStatusList(dataDate);

                        //����CI_LABEL_INFO�еı�ǩ����
                        int row = this.labelStatService.updateCiLabelInfoDataDate(list, dataDate);

                        this.logger.debug("���±�ǩ�����ռ�¼����" + row);
                    } catch (Exception e) {
                        this.logger.error("���±�ǩ������ͳ�������쳣", e);
                        e.printStackTrace();
                    }
                }

                //ˢ�»���
                cache.initAllEffectiveLabel();

                this.createCustomers((String)null);
            }

            this.logger.debug("DailyCacheRefreshJob   refreshLabelNumAndCreateCustomList-end ");
        }

    }
}
