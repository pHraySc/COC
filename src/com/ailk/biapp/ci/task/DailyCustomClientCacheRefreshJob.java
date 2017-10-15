package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.entity.CiTaskServerCityRel;
import com.ailk.biapp.ci.entity.CiTaskServerInfo;
import com.ailk.biapp.ci.service.ICiTaskServerCityRelService;
import com.ailk.biapp.ci.service.ICiTaskServerInfoService;
import com.ailk.biapp.ci.service.IDimCocLabelStatusService;
import com.ailk.biapp.ci.service.ILabelStatService;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.ailk.biapp.ci.webservice.IWsCustomersCreateJobClient;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class DailyCustomClientCacheRefreshJob {
    private Logger logger = Logger.getLogger(DailyCustomClientCacheRefreshJob.class);
    @Autowired
    private IDimCocLabelStatusService dimCocLabelStatusService;
    @Autowired
    private ILabelStatService labelStatService;
    @Autowired
    private IWsCustomersCreateJobClient customersCreateJobClient;
    @Autowired
    private ICiTaskServerInfoService ciTaskServerInfoService;
    @Autowired
    private ICiTaskServerCityRelService ciTaskServerCityRelService;

    public DailyCustomClientCacheRefreshJob() {
    }

    public void refreshLabelNumAndCreateCustomList() {
        String cycleCustomWebservice = Configure.getInstance().getProperty("CYCLE_CUSTOM_WEBSERVICE");
        if(StringUtil.isNotEmpty(cycleCustomWebservice) && "true".equalsIgnoreCase(cycleCustomWebservice)) {
            this.logger.debug("Enter DailyCustomCliendCacheRefreshJob   refreshLabelNumAndCreateCustomList ");
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
                this.labelStatService.updateLabelStatDateStatus(updateCycle);
                this.labelStatService.updateLabelTotalCustomNum(dataDate, updateCycle);
                if(ifLabelUpdateInBatch) {
                    try {
                        List e = this.dimCocLabelStatusService.queryDimCocLabelStatusList(dataDate);
                        int row = this.labelStatService.updateCiLabelInfoDataDate(e, dataDate);
                        this.logger.debug("更新标签最新日记录数：" + row);
                    } catch (Exception var10) {
                        this.logger.error("更新标签最新日统计周期异常", var10);
                        var10.printStackTrace();
                    }
                }

                cache.initAllEffectiveLabel();
                this.webServiceClient(3);
            }

            this.logger.debug("Exit DailyCustomCliendCacheRefreshJob   refreshLabelNumAndCreateCustomList ");
        }

    }

    private void webServiceClient(int customerType) {
        this.logger.debug("Enter DailyCustomCliendCacheRefreshJob   webServiceClient ");
        List serverInfos = this.ciTaskServerInfoService.queryAllTaskServerList();
        List serverCityRels = this.ciTaskServerCityRelService.queryAllTaskServerCityRelList();
        Iterator i$ = serverInfos.iterator();

        while(i$.hasNext()) {
            CiTaskServerInfo ciTaskServerInfo = (CiTaskServerInfo)i$.next();
            String serverId = ciTaskServerInfo.getServerId();
            if(StringUtil.isEmpty(serverId)) {
                this.logger.error("服务器ID为空");
                return;
            }

            String wsdl = ciTaskServerInfo.getWsdlAddr();
            int isExeTask = ciTaskServerInfo.getIsExeTask().intValue();
            String cityIds = "";
            int _index = 0;
            Iterator targetNamespace = serverCityRels.iterator();

            while(targetNamespace.hasNext()) {
                CiTaskServerCityRel methodName = (CiTaskServerCityRel)targetNamespace.next();
                if(serverId.equals(methodName.getServerId()) && null != methodName.getCityId()) {
                    if(_index == 0) {
                        cityIds = cityIds + methodName.getCityId();
                    } else {
                        cityIds = cityIds + "," + methodName.getCityId();
                    }

                    ++_index;
                }
            }

            String var15 = "http://webservice.ci.biapp.ailk.com/";
            String var16 = "notifyExecuteServerJob";

            try {
                this.logger.info("wsdl:" + wsdl);
                this.customersCreateJobClient.notifyCustomerClientJob(wsdl, var15, var16, isExeTask, customerType, cityIds);
            } catch (Exception var14) {
                ;
            }
        }

        this.logger.debug("Exit DailyCustomCliendCacheRefreshJob   webServiceClient ");
    }
}
