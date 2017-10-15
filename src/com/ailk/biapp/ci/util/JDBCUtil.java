package com.ailk.biapp.ci.util;

import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class JDBCUtil {
    private static Logger logger = Logger.getLogger(JDBCUtil.class.getName());
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal();

    public JDBCUtil() {
    }

    public static Connection getConnection() {
        Connection conn = (Connection)threadLocal.get();
        if(conn != null && !"".equals(conn)) {
            try {
                logger.debug("conn is closed==" + conn.isClosed());
            } catch (SQLException var7) {
                var7.printStackTrace();
            }
        }

        if(conn == null) {
            try {
                String e = "oracle.jdbc.driver.OracleDriver";
                String configDriver = Configure.getInstance().getProperty("CI_BACK_DATABASE_DRIVER");
                if(StringUtil.isNotEmpty(configDriver)) {
                    e = configDriver;
                }

                String url = Configure.getInstance().getProperty("CI_BACK_DATABASE_URL");
                String user = Configure.getInstance().getProperty("CI_BACK_DATABASE_USERNAME");
                String password = Configure.getInstance().getProperty("CI_BACK_DATABASE_PASSWORD");
                logger.debug("driver:" + e + " url:" + url + " user:" + user + " password:" + password);
                Class.forName(e);
                conn = DriverManager.getConnection(url, user, password);
                logger.debug("conn in getConn= ==  " + conn);
                logger.debug("get new connection successful");
            } catch (Exception var6) {
                logger.error("get new connection encounter error", var6);
            }

            threadLocal.set(conn);
        } else {
            logger.debug("get connection from threadLocal successful");
        }

        return conn;
    }

    public static Connection getConnection(String url, String user, String password) {
        logger.debug(" start JDBCUtil getConnection ");
        Connection conn = (Connection)threadLocal.get();
        if(conn != null && !"".equals(conn)) {
            try {
                logger.debug("conn is closed==" + conn.isClosed());
            } catch (SQLException var7) {
                var7.printStackTrace();
            }
        }

        if(conn == null) {
            try {
                String e = "oracle.jdbc.driver.OracleDriver";
                String configDriver = Configure.getInstance().getProperty("CI_BACK_DATABASE_DRIVER");
                if(StringUtil.isNotEmpty(configDriver)) {
                    e = configDriver;
                }

                if(StringUtil.isEmpty(url)) {
                    url = Configure.getInstance().getProperty("CI_BACK_DATABASE_URL");
                }

                if(StringUtil.isEmpty(user)) {
                    user = Configure.getInstance().getProperty("CI_BACK_DATABASE_USERNAME");
                }

                if(StringUtil.isEmpty(password)) {
                    password = Configure.getInstance().getProperty("CI_BACK_DATABASE_PASSWORD");
                }

                logger.debug("driver:" + e + " url:" + url + " user:" + user + " password:" + password);
                Class.forName(e);
                conn = DriverManager.getConnection(url, user, password);
                logger.debug("conn in getConn= ==  " + conn);
                logger.debug("get new connection successful");
            } catch (Exception var6) {
                logger.error("get new connection encounter error", var6);
            }

            threadLocal.set(conn);
        } else {
            logger.debug("get connection from threadLocal successful");
        }

        logger.debug(" end JDBCUtil getConnection ");
        return conn;
    }

    public static void closeConnection() {
        Connection conn = (Connection)threadLocal.get();
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException var6) {
                logger.error("close connection is failure", var6);
            } finally {
                threadLocal.remove();
            }
        }

    }

    public static void close(PreparedStatement ps, ResultSet rs) {
        if(rs != null) {
            try {
                rs.close();
                logger.debug("close ResultSet is successful");
            } catch (SQLException var4) {
                logger.error("close ResultSet encounter error", var4);
            }
        }

        if(ps != null) {
            try {
                ps.close();
                logger.debug("close PreparedStatement is successful");
            } catch (SQLException var3) {
                logger.error("close PreparedStatement encounter error", var3);
            }
        }

    }
}
