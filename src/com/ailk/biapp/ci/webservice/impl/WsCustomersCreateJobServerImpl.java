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
            errorMsg = "ȱʧ�Ƿ�ִ���嵥����";
            this.log.error(errorMsg);
        } else if(StringUtil.isEmpty(Integer.valueOf(customCycleType))) {
            errorMsg = "ȱʧ�ͻ�Ⱥ�����Բ���";
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
                            this.log.error("�����ڿͻ�Ⱥ����", var8);
                        }
                    } else if(customCycleType == 2) {
                        try {
                            this.monthlyCacheRefreshJob.createCustomers(cityIds);
                        } catch (Exception var7) {
                            this.log.error("�����ڿͻ�Ⱥ����", var7);
                        }
                    }
                }
            } catch (Exception var9) {
                errorMsg = "�����Կͻ�Ⱥ����";
                this.log.error(errorMsg, var9);
                return;
            }

            this.log.debug("�����Կͻ�Ⱥwebserviceִ����ɣ�Exit WsCustomersCreateJobServerImpl.notifyExecuteServerJob.");
        }
    }
}
