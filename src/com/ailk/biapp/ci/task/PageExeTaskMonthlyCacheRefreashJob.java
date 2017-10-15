package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.service.ICustomersListInfoService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.service.IDimCocLabelStatusService;
import com.ailk.biapp.ci.service.ILabelStatService;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by admin on 2017/7/28.
 */

@Service
public class PageExeTaskMonthlyCacheRefreashJob  extends  AbstractJob {

    private Logger logger = Logger.getLogger(PageExeTaskMonthlyCacheRefreashJob.class);
    @Autowired
    private IDimCocLabelStatusService dimCocLabelStatusService;
    @Autowired
    private ILabelStatService labelStatService;
    @Autowired
    private ICustomersManagerService customersService;
    @Autowired
    private ICustomersListInfoService customersListInfoService;


    @Override
    public void work() {
        {
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

                    //���¹���Ա�ͻ���
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


                }
            }

        }
    }
}
