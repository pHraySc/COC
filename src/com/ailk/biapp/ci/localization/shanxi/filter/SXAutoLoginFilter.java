package com.ailk.biapp.ci.localization.shanxi.filter;

import com.ailk.biapp.ci.localization.shanxi.listener.SXSessionListener;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.DES;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SXAutoLoginFilter implements Filter {
    private static Log log = LogFactory.getLog(SXAutoLoginFilter.class);
    protected boolean autoLogin = false;
    private static String[] skips = null;
    private int sumSSO = 0;

    public SXAutoLoginFilter() {
    }

    public void init(FilterConfig arg0) throws ServletException {
        String value = arg0.getInitParameter("autoLogin");
        if(value != null && (value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true"))) {
            this.autoLogin = true;
        }

        String skip = arg0.getInitParameter("skip");
        if(skip != null) {
            skips = skip.split(";");
        } else {
            skips = new String[0];
        }

    }

    private boolean needSkipUserLoginCheck(HttpServletRequest r) {
        boolean result = false;
        String uri = r.getRequestURI();
        System.out.println("URI->" + uri);
        if(!uri.equals(r.getContextPath()) && !uri.equals(r.getContextPath() + "/")) {
            String[] arr$ = skips;
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String s = arr$[i$];
                if(uri.indexOf(s) > -1) {
                    result = true;
                    break;
                }
            }

            return result;
        } else {
            return true;
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hrequest = (HttpServletRequest)request;
        HttpServletResponse hresponse = (HttpServletResponse)response;
        System.out.println("��ǰ·��Ϊ-----------" + hrequest.getRequestURL() + (hrequest.getQueryString() == null?"":"?" + hrequest.getQueryString()));
        System.out.println("�ж��Ƿ�Ź���·��???????");
        if(this.needSkipUserLoginCheck(hrequest)) {
            System.out.println("�Ź���·��");
            chain.doFilter(request, response);
        } else {
            System.out.println("�ж��Ƿ��Զ���½???");
            String url;
            if(!this.autoLogin) {
                System.out.println("�����Զ���½����������������������");
                if(hrequest.getSession(false) != null && hrequest.getSession().getAttribute("biplatform_user") != null) {
                    System.out.println("����ֱ�ӵ�½");
                    chain.doFilter(request, response);
                } else {
                    System.out.println("session��ʱ��,��ת��ϵͳ��¼ҳ��");
                    url = "<script language=\'javascript\'>alert(\'ϵͳ�ỰʧЧ���������µ�¼��\');top.location.href=\'" + hrequest.getContextPath() + "/index.jsp\'</script>";
                    url = new String(url.getBytes("gbk"), "iso8859-1");
                    hresponse.getOutputStream().print(url);
                }
            } else {
                System.out.println("���Զ���½");
                url = hrequest.getRequestURL() + (hrequest.getQueryString() == null?"":"?" + hrequest.getQueryString());
                System.out.println(url);
                System.out.println(hrequest.getQueryString());
                if(url.contains("mpm.jsp")) {
                    ++this.sumSSO;
                }

                String userId = hrequest.getParameter("userId");
                String uuid = hrequest.getParameter("uuid");
                String mainAccount = hrequest.getParameter("mainAccount");
                System.out.println("�õ��Ĳ���ΪuserID=" + userId + ",uuid=" + uuid + ",mainAccount=" + mainAccount);
                if(uuid != null && !"".equals(uuid)) {
                    System.out.println("uuID��Ϊ��ʱ���򵥵����UUIDȥ����userID");
                    if(this.sumSSO < 2) {
                        HttpClient e = new HttpClient();
                        System.out.println("COC���ֵ�����������ļ���bi_sso_ip=" + Configure.getInstance().getProperty("bi_sso_ip") + ",bi_sso_port=" + Configure.getInstance().getProperty("bi_sso_port") + ",bi_sso_verify_path=" + Configure.getInstance().getProperty("bi_sso_verify_path"));
                        e.getHostConfiguration().setHost(Configure.getInstance().getProperty("bi_sso_ip"), Integer.parseInt(Configure.getInstance().getProperty("bi_sso_port")));
                        PostMethod post = new PostMethod(Configure.getInstance().getProperty("bi_sso_verify_path"));
                        System.out.println("���͵�UUID=" + uuid);
                        NameValuePair uuidPair = new NameValuePair("uuid", uuid);
                        post.setRequestBody(new NameValuePair[]{uuidPair});
                        e.executeMethod(post);
                        String responseStr = new String(post.getResponseBodyAsString().getBytes("GBK"));
                        System.out.println("�ж��Ƿ�����õ����㷵�ص�userID???");
                        if(responseStr != null && !"".equals(responseStr)) {
                            System.out.println("���Ե��õ�userIDΪ" + responseStr);
                            userId = responseStr;
                        }

                        System.out.println("���յ�userID�������#####");
                        log.debug("get userid from sso:" + userId);
                        post.releaseConnection();
                    } else {
                        this.sumSSO = 0;
                    }
                }

                System.out.println("���ж�userID�ǲ��ǿգ���������");
                if(userId != null && userId.length() != 0) {
                    System.out.println("�ܵ�����˵���ǴӾ���ϵͳ����������");
                    log.error("----------------------------------- remote ip:" + request.getRemoteAddr());

                    try {
                        log.debug("before decrypt:" + userId);
                        userId = DES.decrypt(userId);
                        if(mainAccount != null) {
                            mainAccount = DES.decrypt(mainAccount);
                        }

                        System.out.println("���ܺ��userID=" + userId + ",mainAccount=" + mainAccount);
                        PrivilegeServiceUtil.setCityIdSession(hrequest.getSession(), userId);
                        PrivilegeServiceUtil.removeShopCarSession(hrequest.getSession());
                        if(hrequest.getSession(false) == null) {
                            log.info(">>Session has been invalidated!...create a new session!");
                            System.out.println("�Ӿ��ֲഫ����userID��" + userId + ",���˺�:" + mainAccount);
                            System.out.println("��ʼ�Լ�����Session........");
                            hrequest.getSession().setAttribute("aibi_component_privilege_sessionlistener", SXSessionListener.login(hrequest, userId, mainAccount));
                            System.out.println("����session��ϣ�׼����½��ת����������");
                        } else {
                            System.out.println("���ϵͳ�Ѿ���session�ˣ�ֱ����ԭ����");
                            SXSessionListener var16 = (SXSessionListener)hrequest.getSession().getAttribute("aibi_component_privilege_sessionlistener");
                            if(var16 == null) {
                                System.out.println("����Ѿ����ڵ�session�ֿ���");
                                log.info(">>New user [" + userId + "] from" + hrequest.getRemoteAddr() + " login in...");
                                hrequest.getSession().setAttribute("aibi_component_privilege_sessionlistener", SXSessionListener.login(hrequest, userId, mainAccount));
                            } else if(var16 != null && !var16.getUserID().equalsIgnoreCase(userId)) {
                                System.out.println("��ϵͳ���session�봫������userID=" + userId + "��ͬ���������µ�session");
                                log.info(">>Request has a session, but a new user [" + userId + "] from" + hrequest.getRemoteAddr() + " login in...");
                                hrequest.getSession().setAttribute("aibi_component_privilege_sessionlistener", SXSessionListener.login(hrequest, userId, mainAccount));
                            }
                        }
                    } catch (Exception var14) {
                        var14.printStackTrace();
                    }

                    System.out.println("������������������������������");
                    System.out.println(hrequest.getRequestURI());
                    chain.doFilter(hrequest, hresponse);
                    System.out.println("ִ������ת");
                } else {
                    System.out.println("�������UserIDΪ���ˣ������Ǹ�����û��ϵ������");
                    if(hrequest.getSession(false) != null && hrequest.getSession().getAttribute("biplatform_user") != null) {
                        chain.doFilter(request, response);
                    } else {
                        String var15 = "<script language=\'javascript\'>alert(\'ϵͳ�ỰʧЧ���������µ�¼��\');top.location.href=\'" + hrequest.getContextPath() + "/login.jsp\'</script>";
                        var15 = new String(var15.getBytes("gbk"), "iso8859-1");
                        hresponse.getOutputStream().print(var15);
                    }
                }
            }

        }
    }

    public void destroy() {
        this.autoLogin = false;
    }
}
