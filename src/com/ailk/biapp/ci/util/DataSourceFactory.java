package com.ailk.biapp.ci.util;

import com.ailk.biapp.ci.exception.CIServiceException;
import com.asiainfo.biframe.utils.config.Configure;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

public class DataSourceFactory {
    private static Logger logger = Logger.getLogger(DataSourceFactory.class);
    private static Hashtable<String, DataSource> dss = new Hashtable();

    public DataSourceFactory() {
    }

    public static DataSource getDataSource(String jndi) {
        if(!dss.containsKey(jndi)) {
            addDataSource(jndi);
        }

        return (DataSource)dss.get(jndi);
    }

    private static void addDataSource(String jndi) {
        try {
            InitialContext e = new InitialContext();
            String appServerType = null;

            try {
                appServerType = Configure.getInstance().getProperty("APP_SERVER_TYPE");
            } catch (Exception var6) {
                logger.error(var6);
                var6.printStackTrace();
            }

            Object envContext = null;
            if(appServerType == null || !"weblogic".equalsIgnoreCase(appServerType) && !"websphere".equalsIgnoreCase(appServerType)) {
                envContext = (Context)e.lookup("java:comp/env");
            } else {
                envContext = e;
            }

            if(envContext == null) {
                logger.error("Boom - No Context");
                throw new Exception("Boom - No Context");
            }

            if(jndi != null) {
                String strDbJndi = jndi;
                if(jndi.startsWith("java:comp/env/")) {
                    strDbJndi = jndi.substring(14);
                }

                DataSource ds = (DataSource)((Context)envContext).lookup(strDbJndi);
                if(ds == null) {
                    logger.error("can\'t find new datasource!");
                    throw new CIServiceException("can\'t find new datasource!");
                }

                dss.put(jndi, ds);
            }
        } catch (Exception var7) {
            logger.error(var7);
            var7.printStackTrace();
        }

    }

    public void destory() {
        dss.clear();
    }
}
