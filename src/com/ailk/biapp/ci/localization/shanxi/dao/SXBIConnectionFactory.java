package com.ailk.biapp.ci.localization.shanxi.dao;

import com.asiainfo.biframe.utils.config.Configure;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class SXBIConnectionFactory {
    private static Hashtable dss = new Hashtable();
    private DataSource ds;

    public static SXBIConnectionFactory getInstance() {
        String mysql_jndiname = Configure.getInstance().getProperty("JDBC_SXBI");
        if(mysql_jndiname.indexOf("java:comp/env/") >= 0) {
            mysql_jndiname = mysql_jndiname.substring((new String("java:comp/env/")).length());
        }

        return getInstance(mysql_jndiname);
    }

    public static SXBIConnectionFactory getInstance(String jndi) {
        if(!dss.containsKey(jndi)) {
            addDataSource(jndi);
        }

        return (SXBIConnectionFactory)dss.get(jndi);
    }

    public SXBIConnectionFactory(DataSource ds) {
        this.ds = ds;
    }

    private static void addDataSource(String jndi) {
        try {
            InitialContext e = new InitialContext();
            String app_server_type = Configure.getInstance().getProperty("APP_SERVER_TYPE");
            Object envContext = null;
            if(app_server_type == null || !"weblogic".equalsIgnoreCase(app_server_type) && !"websphere".equalsIgnoreCase(app_server_type)) {
                envContext = (Context)e.lookup("java:comp/env");
            } else {
                envContext = e;
            }

            if(envContext == null) {
                throw new Exception("Boom - No Context");
            }

            if(jndi != null) {
                String strDbJndi = jndi;
                if(jndi.startsWith("java:comp/env/")) {
                    strDbJndi = jndi.substring(14);
                }

                DataSource ds = (DataSource)((Context)envContext).lookup(strDbJndi);
                if(ds == null) {
                    throw new Exception("can\'t find new datasource!");
                }

                dss.put(jndi, new SXBIConnectionFactory(ds));
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public Connection getConnection() {
        try {
            if(this.ds != null) {
                Connection ex = this.ds.getConnection();
                if(ex != null) {
                    return ex;
                }
            }
        } catch (SQLException var2) {
            var2.printStackTrace();
        }

        return null;
    }

    public void destory() {
        dss.clear();
    }
}
