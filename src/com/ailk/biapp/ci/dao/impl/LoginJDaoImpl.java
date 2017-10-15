package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ILoginJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoginJDaoImpl implements ILoginJDao {
    private Logger log = Logger.getLogger(LoginJDaoImpl.class);
    private static final String IE10 = "MSIE 10.0";
    private static final String IE9 = "MSIE 9.0";
    private static final String IE8 = "MSIE 8.0";
    private static final String IE7 = "MSIE 7.0";
    private static final String IE6 = "MSIE 6.0";
    private static final String MAXTHON = "Maxthon";
    private static final String QQ = "QQBrowser";
    private static final String GREEN = "GreenBrowser";
    private static final String SE360 = "360SE";
    private static final String FIREFOX = "Firefox";
    private static final String OPERA = "Opera";
    private static final String CHROME = "Chrome";
    private static final String SAFARI = "Safari";
    private static final String NETSCAPE = "NETSCAPE";
    private static final String OTHER = "Other";

    public LoginJDaoImpl() {
    }

    public String checkBrowse(String userAgent) {
        return this.regex("MSIE 10.0", userAgent)?"MSIE 10.0":(this.regex("MSIE 9.0", userAgent)?"MSIE 9.0":(this.regex("MSIE 8.0", userAgent)?"MSIE 8.0":(this.regex("MSIE 7.0", userAgent)?"MSIE 7.0":(this.regex("MSIE 6.0", userAgent)?"MSIE 6.0":(this.regex("Opera", userAgent)?"Opera":(this.regex("Chrome", userAgent)?"Chrome":(this.regex("Firefox", userAgent)?"Firefox":(this.regex("Safari", userAgent)?"Safari":(this.regex("360SE", userAgent)?"360SE":(this.regex("GreenBrowser", userAgent)?"GreenBrowser":(this.regex("QQBrowser", userAgent)?"QQBrowser":(this.regex("Maxthon", userAgent)?"Maxthon":(this.regex("NETSCAPE", userAgent)?"NETSCAPE":"Other")))))))))))));
    }

    private boolean regex(String regex, String str) {
        Pattern p = Pattern.compile(regex, 8);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public void saveOrUpdateLoginHistory(final String sessionId, final String userId, final String clientAddress, final String serverAddress, final String browserVersion, final String productId) throws Exception {
        String JDBC_AIOMNI = Configure.getInstance().getProperty("JDBC_AIOMNI");
        JdbcBaseDao jdbcBaseDao = (JdbcBaseDao)SystemServiceLocator.getInstance().getService("jdbcBaseDao");
        SimpleJdbcTemplate suiteJdbcTemplate = jdbcBaseDao.getSimpleJdbcTemplate(JDBC_AIOMNI);

        try {
            String ex = "update user_login_history set logouttime=null where sessionid=? and userid=?";
            int rows = suiteJdbcTemplate.getJdbcOperations().update(ex, new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, sessionId);
                    ps.setString(2, userId);
                }
            });
            if(rows < 1) {
                String insertSql = "insert into user_login_history(sessionid,userid,oauser,clientaddress,logintime,serveraddress,browserversion,productid) values(?,?,?,?,?,?,?,?)";
                suiteJdbcTemplate.getJdbcOperations().update(insertSql, new PreparedStatementSetter() {
                    public void setValues(PreparedStatement ps) throws SQLException {
                        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                        byte index = 1;
                        int var4 = index + 1;
                        ps.setString(index, sessionId);
                        ps.setString(var4++, userId);
                        ps.setString(var4++, (String)null);
                        ps.setString(var4++, clientAddress);
                        ps.setTimestamp(var4++, currentTimestamp);
                        ps.setString(var4++, serverAddress);
                        ps.setString(var4++, browserVersion);
                        ps.setString(var4++, productId);
                    }
                });
            }

        } catch (Exception var13) {
            this.log.error("±£´æµÇÂ¼ÈÕÖ¾´íÎó", var13);
            throw new RuntimeException(var13);
        }
    }
}
