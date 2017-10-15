package com.ailk.biapp.ci.localization.beijing.listener;

import com.ailk.biapp.ci.constant.CommonConstants;
import com.ailk.biapp.ci.localization.beijing.listener.CiUserSession;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.asiainfo.biframe.log.impl.LogLevelEnum;
import com.asiainfo.biframe.log.impl.OperResultEnum;
import com.asiainfo.biframe.privilege.IUser;
import com.linkage.bi3.loggingComponent.client.imp.LogService;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CiSessionListener implements HttpSessionBindingListener, Serializable {
    private static final long serialVersionUID = 1L;
    private static Logger log = Logger.getLogger(CiSessionListener.class);
    private IUser m_User = null;
    private String m_strSessionID = null;
    private String m_strClientIP = null;

    protected CiSessionListener() {
    }

    public static String getIpAddr(HttpServletRequest request) {
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
        if(StringUtils.isNotEmpty(ip)) {
            ips = ip.split(",");
            return ips[0];
        } else {
            return ip;
        }
    }

    public static CiSessionListener login(HttpServletRequest request, String strUserID) throws Exception {
        CiSessionListener session = new CiSessionListener();
        session.m_strClientIP = getIpAddr(request);
        session.m_strSessionID = request.getSession().getId();
        session.m_User = PrivilegeServiceUtil.getUserById(strUserID);
        return session;
    }

    public void valueBound(HttpSessionBindingEvent event) {
        try {
            CiUserSession e = new CiUserSession(this.m_strClientIP, (String)null, this.m_strSessionID, this.m_User, (String)null);
            event.getSession().setAttribute("biplatform_user", e);
            log.debug("[CiSessionListener] --valueBound put IUserSession into \"biplatform_user\"");
            (new LogService()).login(this.m_strSessionID, this.m_User.getUserid(), this.m_User.getUsername(), CommonConstants.loginResourceType, OperResultEnum.Success, LogLevelEnum.Normal, this.m_strClientIP);
        } catch (Exception var3) {
            log.error("ÓÃ»§µÇÂ¼Ê§°Ü", var3);
            throw new RuntimeException(var3);
        }
    }

    public void valueUnbound(HttpSessionBindingEvent event) {
        (new LogService()).logout(this.m_strSessionID, this.m_User.getUserid(), this.m_User.getUsername(), CommonConstants.logoutResourceType, OperResultEnum.Success, LogLevelEnum.Normal, this.m_strClientIP);
    }
}
