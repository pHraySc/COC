package com.ailk.biapp.ci.filter;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.privilege.IUserPrivilegeService;
import com.asiainfo.biframe.privilege.IUserSession;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class CiSessionFilter implements Filter {
    private Logger log = Logger.getLogger(CiSessionFilter.class);

    public CiSessionFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hrequest = (HttpServletRequest)request;
        HttpServletResponse hresponse = (HttpServletResponse)response;

        try {
            HttpSession e = hrequest.getSession();
            if(e == null) {
                this.log.warn("HttpSession is null !");
                chain.doFilter(hrequest, hresponse);
                return;
            }

            IUserSession userSession = (IUserSession)e.getAttribute("biplatform_user");
            if(userSession == null) {
                chain.doFilter(hrequest, hresponse);
                return;
            }

            String userId = userSession.getUserID();
            if(StringUtil.isEmpty(userId)) {
                chain.doFilter(hrequest, hresponse);
                return;
            }

            String cityId = (String)e.getAttribute("cocUserCityId");
            if(StringUtil.isEmpty(cityId)) {
                PrivilegeServiceUtil.setCityIdSession(hrequest.getSession(), userId);
            }

            Boolean isAdmin = null;
            if(e != null) {
                isAdmin = (Boolean)e.getAttribute("isAdminUser_" + userId);
            }

            if(isAdmin == null) {
                boolean showVersionFlag = false;
                IUserPrivilegeService myCookies = PrivilegeServiceUtil.getUserPrivilegeService();
                showVersionFlag = myCookies.isAdminUser(userId);
                hrequest.getSession().setAttribute("isAdminUser_" + userId, Boolean.valueOf(showVersionFlag));
                Cookie cacheBase = new Cookie(ServiceConstants.COOKIE_NAME_FOR_VERSION + "_" + userId, ServiceConstants.CHANGE_VERSION_NO);
                cacheBase.setMaxAge(31536000);
                cacheBase.setPath("/");
                hresponse.addCookie(cacheBase);
            }

            String showVersionFlag1 = ServiceConstants.CHANGE_VERSION_SHOW_NO;
            Cookie[] myCookies1 = hrequest.getCookies();
            CacheBase cacheBase1 = CacheBase.getInstance();
            List list = cacheBase1.getZjUsers();
            if(list != null && list.size() > 0) {
                Iterator cookieVersionShow = list.iterator();

                while(cookieVersionShow.hasNext()) {
                    String u = (String)cookieVersionShow.next();
                    if(u.equalsIgnoreCase(userId)) {
                        showVersionFlag1 = ServiceConstants.CHANGE_VERSION_SHOW_YES;
                        break;
                    }
                }
            }

            Cookie cookieVersionShow1 = new Cookie(ServiceConstants.COOKIE_NAME_FOR_SHOW_VERSION + "_" + userId, showVersionFlag1);
            cookieVersionShow1.setMaxAge(31536000);
            cookieVersionShow1.setPath("/");
            hresponse.addCookie(cookieVersionShow1);
        } catch (Exception var17) {
            this.log.error(var17);
        }

        chain.doFilter(hrequest, hresponse);
    }

    public void destroy() {
    }
}
