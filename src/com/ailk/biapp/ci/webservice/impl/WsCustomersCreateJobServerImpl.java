package com.ailk.biapp.ci.webservice.impl;

import com.ailk.biapp.ci.task.DailyCacheRefreshJob;
import com.ailk.biapp.ci.task.MonthlyCacheRefreshJob;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.ailk.biapp.ci.webservice.IWsCustomersCreateJobServer;
import com.asiainfo.biframe.utils.string.StringUtil;
import javax.jws.WebService;
import org.apache.cxf.feature.Features;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@WebService(
    endpointInterface = "com.ailk.biapp.ci.webservice.IWsCustomersCreateJobServer"
)
@Features(
    features = {"org.apache.cxf.feature.LoggingFeature"}
)
public class WsCustomersCreateJobServerImpl implements IWsCustomersCreateJobServer {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private DailyCacheRefreshJob dailyCacheRefreshJob;
    @Autowired
    private MonthlyCacheRefreshJob monthlyCacheRefreshJob;

    public WsCustomersCreateJobServerImpl() {
    }

    public void notifyExecuteServerJob(int isExeTask, int customCycleType, String cityIds) {
        this.log.info("Enter WsCustomersCreateJobServerImpl.notifyExecuteServerJob.");
        String errorMsg = null;
        if(StringUtil.isEmpty(Integer.valueOf(isExeTask))) {
            errorMsg = "缺失是否执行清单参数";
            this.log.error(errorMsg);
        } else if(StringUtil.isEmpty(Integer.valueOf(customCycleType))) {
            errorMsg = "缺失客户群周期性参数";
            this.log.error(errorMsg);
        } else {
            CacheBase cache = CacheBase.getInstance();

            try {
                cache.init(false);
                if(isExeTask == 1) {
                    if(customCycleType == 3) {
                        try {
                            this.dailyCacheRefreshJob.createCustomers(cityIds);
                        } catch (Exception var8) {
                            this.log.error("日周期客户群报错", var8);
                        }
                    } else if(customCycleType == 2) {
                        try {
                            this.monthlyCacheRefreshJob.createCustomers(cityIds);
                        } catch (Exception var7) {
                            this.log.error("月周期客户群报错", var7);
                        }
                    }
                }
            } catch (Exception var9) {
                errorMsg = "周期性客户群报错";
                this.log.error(errorMsg, var9);
                return;
            }

            this.log.debug("周期性客户群webservice执行完成，Exit WsCustomersCreateJobServerImpl.notifyExecuteServerJob.");
        }
    }
}
