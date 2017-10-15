package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiOperLogTempHDao;
import com.ailk.biapp.ci.dao.ICiOperLoginTempHDao;
import com.ailk.biapp.ci.dao.ILogTransferJDao;
import com.ailk.biapp.ci.entity.CiOperLogTemp;
import com.ailk.biapp.ci.entity.CiOperLoginTemp;
import com.ailk.biapp.ci.service.ILogTransferService;
import com.asiainfo.biframe.utils.config.Configure;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LogTransferServiceImpl implements ILogTransferService {
    private Logger log = Logger.getLogger(LogTransferServiceImpl.class);
    @Autowired
    private ILogTransferJDao logTransferJDao;
    @Autowired
    private ICiOperLogTempHDao ciOperLogTempHDao;
    @Autowired
    private ICiOperLoginTempHDao ciOperLoginTempHDao;

    public LogTransferServiceImpl() {
    }

    public void logStatistics(String date) {
        this.logTransferJDao.exportCustomerCreateDetail2LogOverview(date);
        this.logTransferJDao.exportCustomerCreateTotal2LogOverview(date);
        this.logTransferJDao.exportCustomerCreateDetail2LogUser(date);
        this.logTransferJDao.exportLabelAnalysisDetail2LogOverview(date);
        this.logTransferJDao.exportLabelAnalysisTotal2LogOverview(date);
        this.logTransferJDao.exportLabelAnalysisDetail2LogUser(date);
        this.logTransferJDao.exportCustomerAnalysisDetail2LogOverview(date);
        this.logTransferJDao.exportCustomerAnalysisTotal2LogOverview(date);
        this.logTransferJDao.exportCustomerAnalysisDetail2LogUser(date);
        this.logTransferJDao.exportDateExplore2LogOverview(date);
        this.logTransferJDao.exportDateExplore2LogUser(date);
        this.logTransferJDao.exportMarketing2LogOverview(date);
        this.logTransferJDao.exportMarketing2LogUser(date);
        this.logTransferJDao.exportLoginData2LogOverview(date);
        this.logTransferJDao.exportLoginData2LogUser(date);
    }

    @Transactional(
        propagation = Propagation.REQUIRES_NEW
    )
    public synchronized void transportLogFromWebOS(String date) {
        this.transportOperLogData(date);
        this.transportLoginLogData(date);
    }

    private void transportOperLogData(String date) {
        String logTableName = "LKG_OPER_LOG_" + date;
        List webOsLogList = this.logTransferJDao.selectLgkOperLog(logTableName);
        CiOperLogTemp entity = null;
        Iterator i$ = webOsLogList.iterator();

        while(i$.hasNext()) {
            Object item = i$.next();
            Map o = (Map)item;
            entity = this.generateOperLogTempEntity(o);
            this.ciOperLogTempHDao.save(entity);
        }

    }

    private void transportLoginLogData(String date) {
        String logTableName = "LKG_HITRATE_LOG_" + date;
        this.log.debug("login data transfering ...");
        List webOsLogList = this.logTransferJDao.selectLgkLogin(logTableName);
        CiOperLoginTemp entity = null;
        Iterator i$ = webOsLogList.iterator();

        while(i$.hasNext()) {
            Object item = i$.next();
            Map o = (Map)item;
            entity = this.generateOperLoginTempEntity(o);
            this.ciOperLoginTempHDao.save(entity);
        }

    }

    private CiOperLogTemp generateOperLogTempEntity(Map o) {
        CiOperLogTemp entity = new CiOperLogTemp();
        entity.setOperateDate((String)o.get("operatedate"));
        entity.setSessionId((String)o.get("sessionid"));
        entity.setOperatorId((String)o.get("operatorid"));
        entity.setOperatorName((String)o.get("staff_name"));
        entity.setBassId((String)o.get("bassid"));
        entity.setOperaterType((String)o.get("operatertype"));
        entity.setOperaterName((String)o.get("operatertype"));
        entity.setAppId((String)o.get("appid"));
        entity.setAppName((String)o.get("appname"));
        entity.setResourceId((String)o.get("resourceid"));
        entity.setExtResourceId((String)o.get("extresourceid"));
        entity.setResourceName((String)o.get("resourcename"));
        entity.setResourceType((String)o.get("resourcetype"));
        entity.setResourcePath((String)o.get("resourcepath"));
        entity.setMsg((String)o.get("msg"));
        entity.setOperationResult((String)o.get("operationresult"));
        entity.setLogLevel((String)o.get("loglevel"));
        entity.setTabHeader((String)o.get("tabheader"));
        entity.setClientAddress((String)o.get("clientaddress"));
        entity.setServerAddress((String)o.get("serveraddress"));
        entity.setThreadName((String)o.get("threadname"));
        entity.setTraffic(Integer.valueOf(String.valueOf(o.get("traffic"))).intValue());
        entity.setLogServerIp((String)o.get("logserverip"));
        entity.setLogServerPort((String)o.get("logserverport"));
        entity.setThirdDeptId((String)o.get("third_dept_id"));
        entity.setThirdDeptName((String)o.get("third_dept_name"));
        entity.setSecondDeptId((String)o.get("second_dept_id"));
        entity.setSecondDeptName((String)o.get("second_dept_name"));
        return entity;
    }

    private CiOperLoginTemp generateOperLoginTempEntity(Map o) {
        CiOperLoginTemp entity = new CiOperLoginTemp();
        entity.setHitTime((String)o.get("hittime"));
        entity.setSessionId((String)o.get("sessionid"));
        entity.setUserId((String)o.get("userid"));
        entity.setUsername((String)o.get("staff_name"));
        entity.setBassId((String)o.get("bassid"));
        entity.setOperaterType((String)o.get("operatertype"));
        entity.setOperaterName((String)o.get("operatertype"));
        entity.setAppId((String)o.get("appid"));
        entity.setAppName((String)o.get("appname"));
        entity.setResourceId((String)o.get("resourceid"));
        entity.setExtResourceId((String)o.get("extresourceid"));
        entity.setResourceName((String)o.get("resourcename"));
        entity.setResourceType((String)o.get("resourcetype"));
        entity.setResourcePath((String)o.get("resourcepath"));
        entity.setMsg((String)o.get("msg"));
        entity.setOperationResult((String)o.get("operationresult"));
        entity.setLogLevel((String)o.get("loglevel"));
        entity.setTabHeader((String)o.get("tabheader"));
        entity.setClientAddress((String)o.get("clientaddress"));
        entity.setServerAddress((String)o.get("serveraddress"));
        entity.setThreadName((String)o.get("threadname"));
        entity.setTraffic(Integer.valueOf(String.valueOf(o.get("traffic"))).intValue());
        entity.setLogServerIp((String)o.get("logserverip"));
        entity.setLogServerPort((String)o.get("logserverport"));
        entity.setThirdDeptId((String)o.get("third_dept_id"));
        entity.setThirdDeptName((String)o.get("third_dept_name"));
        entity.setSecondDeptId((String)o.get("second_dept_id"));
        entity.setSecondDeptName((String)o.get("second_dept_name"));
        return entity;
    }

    public boolean logTableIfExists(String date) {
        String dbType = Configure.getInstance().getProperty("CI_DBTYPE");
        String jndi = Configure.getInstance().getProperty("JNDI_WEBOS_CI");
        String loginTableName = "LKG_HITRATE_LOG_" + date;
        String logTableName = "LKG_OPER_LOG_" + date;
        return this.logTransferJDao.tableExists(loginTableName, dbType, jndi) && this.logTransferJDao.tableExists(logTableName, dbType, jndi);
    }

    public void removeDirtyData(String dataDate) {
        this.logTransferJDao.deleteDirtyData(dataDate);
    }
}
