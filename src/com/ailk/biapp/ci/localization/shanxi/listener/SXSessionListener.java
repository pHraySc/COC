package com.ailk.biapp.ci.localization.shanxi.listener;

import com.ailk.biapp.ci.localization.shanxi.dao.SXBIConnectionEx;
import com.ailk.biapp.ci.localization.shanxi.dao.SqlcaSX;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.privilege.IUserPrivilegeService;
import com.asiainfo.biframe.privilege.base.constants.UserManager;
import com.asiainfo.biframe.privilege.base.vo.PrivilegeUserSession;
import com.asiainfo.biframe.privilege.sysmanage.service.IUserAdminService;
import com.asiainfo.biframe.utils.i18n.LocaleUtil;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.UUIDHexGenerator;

public class SXSessionListener implements HttpSessionBindingListener, Serializable {
    private static final long serialVersionUID = 501911809540566762L;
    private static Log log = LogFactory.getLog(SXSessionListener.class);
    private IUser m_User = null;
    private String m_strLoginOAUser = "";
    private String m_strSessionID = null;
    private String m_strClientIP = null;
    private String m_strServerAddress = "";
    private String m_browser_version = "";
    public static final String LOGOUTTYPE_ZHUDONG = "1";
    public static final String LOGOUTTYPE_BEIDONG = "2";

    protected static final IUserAdminService getUserAdminService() throws Exception {
        return (IUserAdminService)SystemServiceLocator.getInstance().getService("right_userAdminService");
    }

    protected SXSessionListener() {
    }

    public static String getIpAddr(HttpServletRequest request) {
        Enumeration xx = request.getHeaderNames();
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        String[] ips = null;
        if(StringUtil.isNotEmpty(ip)) {
            ips = ip.split(",");
            return ips[0];
        } else {
            return ip;
        }
    }

    public static SXSessionListener login(HttpServletRequest request, String strUserID, String strOaUser) throws Exception {
        SXSessionListener session = new SXSessionListener();
        session.m_strClientIP = getIpAddr(request);
        session.m_strSessionID = request.getSession().getId();
        session.m_strLoginOAUser = strOaUser;
        session.m_browser_version = getBrowserVersion(request.getHeader("user-agent"));
        log.info("[SessionListener] login sessionid=====" + session.m_strSessionID);

        try {
            session.m_strServerAddress = UserManager.getHostAddress();
        } catch (Exception var13) {
            session.m_strServerAddress = "127.0.0.1";
        }

        Object sqlca = null;

        try {
            System.out.println("开始执行获取用户");
            IUserPrivilegeService excep = (IUserPrivilegeService)SystemServiceLocator.getInstance().getService("sxUserPrivilegeService");
            IUser sxUser = excep.getUser(strUserID);
            System.out.println("获取用户->" + sxUser.getUsername());
            session.m_User = sxUser;
        } catch (Exception var12) {
            var12.printStackTrace();
            throw var12;
        } finally {
            if(null != sqlca) {
                ((SqlcaSX)sqlca).closeAll();
            }

        }

        return session;
    }

    public void valueBound(HttpSessionBindingEvent event) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        SqlcaSX sqlca = null;

        try {
            sqlca = new SqlcaSX(new SXBIConnectionEx());
            sqlca.setAutoCommit(false);
            String e = "insert into LKG_LOG_VISIT(LOG_ID,STAFF_ID,FUNC_ID,VISIT_IP,VISIT_TIME,BROWSER_VERSION,MAIN_ACCOUNT,ROUND_AUDIT,OPER_TYPE,OPER_SUB_TYPE,SESSION_ID) values(\'" + (String)(new UUIDHexGenerator()).generate((SessionImplementor)null, (Object)null) + "\',\'" + this.m_User.getUserid() + "\',\'COC_LOGIN\',\'" + this.m_strClientIP + "\',\'" + currentTimestamp + "\',\'" + this.m_browser_version + "\',\'" + this.m_strLoginOAUser + "\',\'\',\'\',\'\',\'" + this.m_strSessionID + "\')";
            log.info("[SessionListener] --valueBound insert sql:" + e);
            System.out.println(this.m_strSessionID);
            sqlca.execute(e);
            sqlca.commit();
            PrivilegeUserSession sessionUser = new PrivilegeUserSession();
            sessionUser.setUser(this.m_User);
            sessionUser.setClientIp(this.m_strClientIP);
            sessionUser.setSessionId(this.m_strSessionID);
            sessionUser.setOaUserId(this.m_strLoginOAUser);
            sessionUser.setGroupId(this.m_User.getGroupId());
            event.getSession().setAttribute("biplatform_user", sessionUser);
            log.info("[SessionListener] --valueBound put PrivilegeUserSession into \"biplatform_user\"");
        } catch (Exception var10) {
            var10.printStackTrace();
        } finally {
            if(null != sqlca) {
                sqlca.closeAll();
            }

        }

    }

    public void valueUnbound(HttpSessionBindingEvent event) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        SqlcaSX sqlca = null;

        try {
            sqlca = new SqlcaSX(new SXBIConnectionEx());
            sqlca.setAutoCommit(false);
            String e = "insert into LKG_LOG_VISIT(LOG_ID,STAFF_ID,FUNC_ID,VISIT_IP,VISIT_TIME,BROWSER_VERSION,MAIN_ACCOUNT,ROUND_AUDIT,OPER_TYPE,OPER_SUB_TYPE,SESSION_ID) values(\'" + (String)(new UUIDHexGenerator()).generate((SessionImplementor)null, (Object)null) + "\',\'" + this.m_User.getUserid() + "\',\'COC_LOGOUT\',\'" + this.m_strClientIP + "\',\'" + currentTimestamp + "\',\'" + this.m_browser_version + "\',\'" + this.m_strLoginOAUser + "\',\'\',\'\',\'\',\'" + this.m_strSessionID + "\')";
            log.info("[SessionListener] --valueUnbound  sql:" + e);
            sqlca.execute(e);
            sqlca.commit();
        } catch (Exception var9) {
            var9.printStackTrace();
        } finally {
            if(null != sqlca) {
                sqlca.closeAll();
            }

        }

    }

    public String getUserID() throws Exception {
        return this.m_User.getUserid();
    }

    public String getUserName() throws Exception {
        return this.m_User.getUsername();
    }

    public String getOAUserID() throws Exception {
        return this.m_strLoginOAUser;
    }

    public String getUserRoles() throws Exception {
        IUserAdminService userService = (IUserAdminService)SystemServiceLocator.getInstance().getService("right_userAdminService");
        return userService.getGroup(this.m_User.getUserid());
    }

    public String getUserCityID() throws Exception {
        return this.m_User.getCityid();
    }

    public String getClientIP() {
        return this.m_strClientIP;
    }

    public String getLoginOAUser() {
        return this.m_strLoginOAUser;
    }

    public String getSessionID() {
        return this.m_strSessionID;
    }

    private static String getBrowserVersion(String user_agent) {
        return user_agent.indexOf("MSIE 6.0") > 0?"IE 6.0":(user_agent.indexOf("MSIE 8.0") > 0?"IE 8.0":(user_agent.indexOf("MSIE 7.0") > 0?"IE 7.0":(user_agent.indexOf("MSIE 5.5") > 0?"IE 5.5":(user_agent.indexOf("MSIE 5.01") > 0?"IE 5.01":(user_agent.indexOf("MSIE 5.0") > 0?"IE 5.0":(user_agent.indexOf("MSIE 4.0") > 0?"IE 4.0":(user_agent.indexOf("Firefox") > 0?"Firefox":(user_agent.indexOf("Netscape") > 0?"Netscape":(user_agent.indexOf("Opera") > 0?"Opera":"" + LocaleUtil.getLocaleMessage("privilegeService", "privilegeService.java.other") + "")))))))));
    }

    public IUser getUser() {
        return this.m_User;
    }
}
