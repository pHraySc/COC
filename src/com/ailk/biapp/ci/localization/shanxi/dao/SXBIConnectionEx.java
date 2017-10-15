package com.ailk.biapp.ci.localization.shanxi.dao;

import COM.ibm.db2.jdbc.app.DB2Driver;
import com.ailk.biapp.ci.localization.shanxi.dao.SXBIConnectionFactory;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.database.jdbc.DbConnectionBroker;
import com.mysql.jdbc.Driver;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Hashtable;
import java.util.Properties;
import oracle.jdbc.driver.OracleDriver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SXBIConnectionEx {
    private static Log log = LogFactory.getLog(SXBIConnectionEx.class);
    public static final String DBMS_ORACLE = "ORACLE";
    public static final String DBMS_ODBC = "ODBC";
    public static final String DBMS_ACESS = "ACESS";
    public static final String DBMS_MYSQL = "MYSQL";
    public static final String DBMS_DB2 = "DB2";
    public static final String DBMS_SQLSERVER = "SQLSERVER";
    public static final String DBMS_TERA = "TERA";
    public static final String DBMS_SYBASE = "SYBASE";
    protected Connection m_Connection = null;
    private String strConnectURL = "jdbc:odbc:BrioAccess";
    private String strDriver = "com.mysql.jdbc.Driver";
    private String strDBMSType = "odbc";
    private String strUser = "";
    private String strPassword = "";
    protected static Hashtable hashPool = new Hashtable();
    protected boolean bInitFromPool = true;

    public Connection getConnection() {
        return this.m_Connection;
    }

    public static SXBIConnectionEx getConnection(String jndiName) throws Exception {
        return new SXBIConnectionEx(jndiName);
    }

    public SXBIConnectionEx(String strDBType, String user, String strPwd, String strUrl, boolean bPool) throws Exception {
        this.bInitFromPool = bPool;
        this.strConnectURL = strUrl;
        this.strDBMSType = strDBType;
        this.strUser = user;
        this.strPassword = strPwd;
        this.connect();
    }

    public SXBIConnectionEx(SXBIConnectionEx connectEx) {
        this.m_Connection = connectEx.m_Connection;
    }

    public SXBIConnectionEx(Connection connect) {
        this.m_Connection = connect;
    }

    public SXBIConnectionEx() throws Exception {
        Connection conn = null;

        try {
            conn = SXBIConnectionFactory.getInstance().getConnection();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        synchronized(this) {
            this.m_Connection = conn;
        }
    }

    public SXBIConnectionEx(String strJDBC_NAME) throws Exception {
        String strDataSourceName = Configure.getInstance().getProperty(strJDBC_NAME);
        if(strDataSourceName != null && strDataSourceName.length() >= 1) {
            Connection conn = null;

            try {
                conn = SXBIConnectionFactory.getInstance(strDataSourceName).getConnection();
            } catch (Exception var7) {
                var7.printStackTrace();
            }

            synchronized(this) {
                this.m_Connection = conn;
            }
        } else {
            throw new Exception("JDBC Connection Configuration: \'" + strJDBC_NAME + " \' does not exist, or configuration errors");
        }
    }

    public SXBIConnectionEx(String moduleName, String path, String strJDBC_NAME) throws Exception {
        Configure.getInstance().addConfFileName(moduleName, Thread.currentThread().getContextClassLoader().getResource(path).getFile());
        String strDataSourceName = Configure.getInstance().getProperty(moduleName, strJDBC_NAME);
        if(strDataSourceName != null && strDataSourceName.length() >= 1) {
            Connection conn = null;

            try {
                conn = SXBIConnectionFactory.getInstance(strDataSourceName).getConnection();
            } catch (Exception var9) {
                var9.printStackTrace();
            }

            synchronized(this) {
                this.m_Connection = conn;
            }
        } else {
            throw new Exception("JDBC Connection Configuration: \'" + strJDBC_NAME + " \' does not exist, or configuration errors");
        }
    }

    public static SXBIConnectionEx getReportServerConnection() throws Exception {
        String strType = Configure.getInstance().getProperty("REPORT_DBTYPE");
        String strUrl = Configure.getInstance().getProperty("REPORT_URL");
        String strUser = Configure.getInstance().getProperty("REPORT_USERNAME");
        String strPwd = Configure.getInstance().getProperty("REPORT_PASSWORD");
        if(null != strType && strType.trim().length() >= 1) {
            return new SXBIConnectionEx(strType, strUser, strPwd, strUrl, true);
        } else {
            throw new Exception("--------err not found the database configuration parameter:[REPORT_DBTYPE]");
        }
    }

    protected int connect() {
        if(this.bInitFromPool) {
            String excep = this.strDBMSType + this.strConnectURL + this.strUser + this.strPassword;
            excep = excep.toUpperCase();
            DbConnectionBroker myBroker = null;
            synchronized(this) {
                myBroker = (DbConnectionBroker)hashPool.get(excep);
            }

            if(null == myBroker) {
                try {
                    if(this.strDBMSType.equalsIgnoreCase("ORACLE")) {
                        this.strDriver = "oracle.jdbc.driver.OracleDriver";
                    } else if(this.strDBMSType.equalsIgnoreCase("DB2")) {
                        this.strDriver = "COM.ibm.db2.jdbc.app.DB2Driver";
                    } else if(this.strDBMSType.equalsIgnoreCase("MYSQL")) {
                        this.strDriver = "com.mysql.jdbc.Driver";
                    } else if(this.strDBMSType.equalsIgnoreCase("SYBASE")) {
                        this.strDriver = "com.sybase.jdbc2.jdbc.SybDriver";
                    }

                    log.debug("Connecting to database : " + this.strConnectURL + "[" + this.strDBMSType + "]" + this.strDriver);
                    myBroker = new DbConnectionBroker(this.strDriver, this.strConnectURL, this.strUser, this.strPassword, 3, 10, 1.0D);
                    hashPool.put(excep, myBroker);
                } catch (Exception var5) {
                    var5.printStackTrace();
                    hashPool.remove(excep);
                    return 1;
                }
            }

            this.m_Connection = myBroker.getConnection();
            return 0;
        } else {
            try {
                if(this.strDBMSType.equalsIgnoreCase("MYSQL")) {
                    return this.connectMySQL();
                }

                if(this.strDBMSType.equalsIgnoreCase("ORACLE")) {
                    return this.connectOracle();
                }

                if(this.strDBMSType.equalsIgnoreCase("DB2")) {
                    return this.connectDB2();
                }
            } catch (Exception var8) {
                var8.printStackTrace();
            }

            try {
                return -1;
            } catch (Exception var7) {
                var7.printStackTrace();
                this.m_Connection = null;
                return -1;
            }
        }
    }

    public void commit() throws Exception {
        if(null != this.m_Connection) {
            this.m_Connection.commit();
        }

    }

    protected int connectMySQL() throws Exception {
        if(!this.strDBMSType.equalsIgnoreCase("MYSQL")) {
            return -1;
        } else {
            Properties info = new Properties();
            info.put("USER", this.strUser);
            info.put("PASSWORD", this.strPassword);
            InetAddress ina = InetAddress.getLocalHost();
            String hostname = ina.getHostName();
            info.put("HOSTNAME", hostname);
            info.put("APPLICATIONNAME", "kpi");
            this.strDriver = "com.mysql.jdbc.Driver";
            Driver mydriver = (Driver)Class.forName(this.strDriver).newInstance();
            DriverManager.registerDriver(mydriver);
            this.m_Connection = DriverManager.getConnection(this.strConnectURL, info);
            return 0;
        }
    }

    protected int connectDB2() throws Exception {
        this.strDriver = "COM.ibm.db2.jdbc.app.DB2Driver";
        DB2Driver mydriver = (DB2Driver)Class.forName(this.strDriver).newInstance();
        DriverManager.registerDriver(mydriver);
        this.m_Connection = DriverManager.getConnection(this.strConnectURL, this.strUser, this.strPassword);
        return 0;
    }

    protected int connectAccess() throws Exception {
        throw new Exception("drive type not supported ");
    }

    protected int connectOracle() throws Exception {
        this.strDriver = "oracle.jdbc.driver.OracleDriver";
        OracleDriver mydriver = (OracleDriver)Class.forName(this.strDriver).newInstance();
        DriverManager.registerDriver(mydriver);
        this.m_Connection = DriverManager.getConnection(this.strConnectURL, this.strUser, this.strPassword);
        return 0;
    }

    public void rollback() throws Exception {
        if(null != this.m_Connection) {
            this.m_Connection.rollback();
        }

    }

    public void close() {
        try {
            if(null != this.m_Connection) {
                this.m_Connection.close();
                this.m_Connection = null;
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public boolean isClosed() throws Exception {
        return null == this.m_Connection?true:this.m_Connection.isClosed();
    }
}
