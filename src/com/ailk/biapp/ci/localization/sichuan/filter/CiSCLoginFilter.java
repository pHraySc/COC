package com.ailk.biapp.ci.localization.sichuan.filter;

import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.localization.sichuan.model.CiSCUserInfo;
import com.ailk.biapp.ci.localization.sichuan.model.CiSCUserSession;
import com.ailk.biapp.ci.localization.sichuan.service.ICiSCUserRoleService;
import com.ailk.biapp.ci.localization.sichuan.util.PasswordUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.asiainfo.biframe.exception.NotLoginException;
import com.asiainfo.biframe.log.LogInfo;
import com.asiainfo.biframe.privilege.IUserSession;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.LogRecord;

public class CiSCLoginFilter implements Filter {
    private Logger log = Logger.getLogger(this.getClass());

    public CiSCLoginFilter() {
    }

    /**
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     * 过滤器过滤：*.html/*.jsp/*.ai2do(action)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hrequest = (HttpServletRequest)request;
        HttpServletResponse hresponse = (HttpServletResponse)response;
        IUserSession iUserSession = (IUserSession)hrequest.getSession().getAttribute("biplatform_user");
        this.log.info("iUserSession:" + iUserSession);
        String param = request.getParameter("param");
        this.log.info("param:" + param);
        if(param == null) {
            if(iUserSession != null) {
                LogInfo.setClientAddress(iUserSession.getClientIP());
                LogInfo.setOperatorID(iUserSession.getUserID());
                LogInfo.setOperatorName(iUserSession.getUserName());
                LogInfo.setSessionID(iUserSession.getSessionID());
                LogInfo.setHostAddress(Configure.getInstance().getProperty("HOST_ADDRESS"));
                chain.doFilter(hrequest, hresponse);
            } else {
                throw new NotLoginException();
            }
        } else {
            try {
                String e = PasswordUtil.decrypt(param);
                this.log.info("deParam:" + e);
                String loginNo = "";
                String pwd = "";
                String op_time = "";
                if(StringUtils.isNotBlank(e)) {
                    String[] sql = e.split("&");
                    String[] userInfoList = sql;
                    this.log.info("sql[]" + sql);
                    int flag = sql.length;
                    for(int ciSCUserRoleService = 0; ciSCUserRoleService < flag; ++ciSCUserRoleService) {
                        String timestampdiff = userInfoList[ciSCUserRoleService];
                        if(timestampdiff.startsWith("loginno=")) {
                            loginNo = timestampdiff.replace("loginno=", "");
                        } else if(timestampdiff.startsWith("pwd=")) {
                            pwd = timestampdiff.replace("pwd=", "").toUpperCase();
                        } else if(timestampdiff.startsWith("op_time=")) {
                            op_time = timestampdiff.replace("op_time=", "");
                        }
                    }
                }

                this.log.info("参数："+loginNo+"--"+pwd+"--"+op_time);
                this.log.info("lewis 跳过登录验证修改此处");
                String var21 = "select timestampdiff(2,char(current timestamp - timestamp(\'" + op_time + "\'))) from sysibm.sysdummy1";
                System.out.println(var21);
                boolean var22 = true;
                int var23 = (new JdbcBaseDao()).getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_CI")).queryForInt(var21, new Object[0]);
//                if(var23 > 30) { lewis 跳过登录验证修改
                if(false){
                    throw new NotLoginException();
                } else {
                    ICiSCUserRoleService var24 = (ICiSCUserRoleService)SystemServiceLocator.getInstance().getService("ciSCUserRoleServiceImpl");
                    boolean var25 = true;
                    if(!"true".equals(Configure.getInstance().getProperty("DEBUG"))) {
                        var25 = var24.verifyLoginUser(loginNo, pwd);
                    }
                    if(var25) {
                        List var26 = var24.getLoginUserInfo(loginNo);
                        if(var26 != null && var26.size() > 0) {
                            CiSCUserInfo ciSCUserInfo = (CiSCUserInfo)var26.get(0);
                            CiSCUserSession userSession = new CiSCUserSession();
                            userSession.setClientIp(hrequest.getRemoteAddr());
                            userSession.setGroupId((String)null);
                            userSession.setLoginTime(new Timestamp(System.currentTimeMillis()));
                            userSession.setOaUserId((String)null);
                            userSession.setSessionId(hrequest.getSession().getId());
                            userSession.setServerIp(Configure.getInstance().getProperty("HOST_ADDRESS"));
                            userSession.setUser(ciSCUserInfo);
                            userSession.setUserID(ciSCUserInfo.getLoginNo());
                            hrequest.getSession().setAttribute("biplatform_user", userSession);
                            PrivilegeServiceUtil.setCityIdSession(hrequest.getSession(), userSession.getUserID());
                            this.log.info("CityIdFromSession=========" + hrequest.getSession().getAttribute("cocUserCityId"));
                            PrivilegeServiceUtil.removeShopCarSession(hrequest.getSession());
                            boolean isAdminUser = false;
                            isAdminUser = var24.verifyIsAdmin(loginNo);
                            hrequest.getSession().setAttribute("isAdminUser_" + loginNo, Boolean.valueOf(isAdminUser));
                            LogInfo.setClientAddress(userSession.getClientIP());
                            LogInfo.setOperatorID(userSession.getUserID());
                            LogInfo.setOperatorName(userSession.getUserName());
                            LogInfo.setSessionID(userSession.getSessionID());
                            LogInfo.setHostAddress(userSession.getServerIp());
                            chain.doFilter(hrequest, hresponse);
                        } else {
                            throw new NotLoginException();
                        }
                    } else {
                        throw new NotLoginException();
                    }
                }
            } catch (Exception var20) {
                var20.printStackTrace();
                throw new NotLoginException();
            }
        }
    }

    public boolean isLoggable(LogRecord record) {
        return false;
    }

    public void destroy() {
    }

    public void init(FilterConfig arg0) throws ServletException {
    }
}
