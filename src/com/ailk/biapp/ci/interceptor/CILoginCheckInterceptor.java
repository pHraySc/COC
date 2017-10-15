package com.ailk.biapp.ci.interceptor;

import com.asiainfo.biframe.privilege.IUserSession;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class CILoginCheckInterceptor extends AbstractInterceptor {
    private static final long serialVersionUID = 1L;
    private static final String LOGIN_PAGE = "noauth";
    public static final String XML_HTTPREQUEST = "XMLHttpRequest";

    public CILoginCheckInterceptor() {
    }

    public String intercept(ActionInvocation invocation) throws Exception {
        String result = "noauth";
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest)ctx.get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
        HttpServletResponse response = (HttpServletResponse)ctx.get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");
        HttpSession session = request.getSession();
        IUserSession mysession = (IUserSession)session.getAttribute("biplatform_user");
        String requestType = request.getHeader("x-requested-with");
        String userId;
        if(requestType != null && requestType.equals("XMLHttpRequest")) {
            if(mysession != null) {
                userId = mysession.getUserID();
                if(StringUtil.isNotEmpty(userId)) {
                    result = invocation.invoke();
                } else {
                    response.sendError(403);
                    result = null;
                }
            } else {
                response.sendError(403);
                result = null;
            }
        } else if(mysession != null) {
            userId = mysession.getUserID();
            if(StringUtil.isNotEmpty(userId)) {
                result = invocation.invoke();
            }
        }

        return result;
    }
}
