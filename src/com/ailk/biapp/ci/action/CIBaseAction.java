package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.exception.NotLoginException;
import com.asiainfo.biframe.privilege.IMenuItem;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.privilege.IUserPrivilegeService;
import com.asiainfo.biframe.privilege.IUserSession;
import com.asiainfo.biframe.utils.http.ResponseEncodingUtil;
import com.asiainfo.biframe.utils.i18n.LocaleUtil;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
@Controller
public class CIBaseAction {
    protected boolean isAdmin;
    protected String userId;
    protected String userName;
    protected String menuId = null;
    protected String menuName = null;
    protected String noDataToDisplay;
    protected String newLabelDay;
    protected String newLabelMonth;
    protected String cityId;
    protected Logger log = Logger.getLogger(CIBaseAction.class);

    public CIBaseAction() {
    }

    protected ActionContext getContext() {
        ActionContext ctx = ActionContext.getContext();
        return ctx;
    }

    protected HttpServletRequest getRequest() {
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest)ctx.get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
        return request;
    }

    protected HttpServletResponse getResponse() {
        ActionContext ctx = ActionContext.getContext();
        HttpServletResponse response = (HttpServletResponse)ctx.get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");
        return response;
    }

    protected HttpSession getSession() {
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest)ctx.get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
        HttpSession session = request.getSession();
        return session;
    }

    public String getUserId() {
        return PrivilegeServiceUtil.getUserId();
    }

    public String getUserName() {
        try {
            IUserSession e = (IUserSession)this.getSession().getAttribute("biplatform_user");
            this.userName = e.getUserName();
            return this.userName;
        } catch (Exception var2) {
            this.log.error(LocaleUtil.getLocaleMessage("core", "core.java.getUserFail"), var2);
            throw new NotLoginException(LocaleUtil.getLocaleMessage("core", "core.java.getOperatorFail"), var2);
        }
    }

    protected final String getUserGroupId() throws NotLoginException {
        try {
            IUserSession e = (IUserSession)this.getSession().getAttribute("biplatform_user");
            return e.getGroupId();
        } catch (Exception var2) {
            this.log.error(LocaleUtil.getLocaleMessage("core", "core.java.getUserGroupFail"), var2);
            throw new NotLoginException(LocaleUtil.getLocaleMessage("core", "core.java.getOperatorGroupFail"), var2);
        }
    }

    protected final String getUserCityId() throws NotLoginException {
        try {
            IUserSession e = (IUserSession)this.getSession().getAttribute("biplatform_user");
            return e.getUserCityID();
        } catch (Exception var2) {
            this.log.error(LocaleUtil.getLocaleMessage("core", "core.java.getOperatorFail"), var2);
            throw new NotLoginException(LocaleUtil.getLocaleMessage("core", "core.java.getOperatorFail"), var2);
        }
    }

    protected IUser getUser() throws NotLoginException {
        try {
            IUserSession e = (IUserSession)this.getSession().getAttribute("biplatform_user");
            return e.getUser();
        } catch (Exception var2) {
            this.log.error(LocaleUtil.getLocaleMessage("core", "core.java.getUserObjectFail"), var2);
            throw new NotLoginException(LocaleUtil.getLocaleMessage("core", "core.java.getUserObjectFail"), var2);
        }
    }

    protected List<IMenuItem> getMenuByUser() {
        IUserPrivilegeService src;
        try {
            src = (IUserPrivilegeService)SystemServiceLocator.getInstance().getService("right_privilegeService");
        } catch (Exception var3) {
            this.log.error("获取用户菜单权限失败", var3);
            throw new RuntimeException(var3);
        }

        List menuItemList = src.getAllMenuItem(this.getUserId());
        this.getSession().setAttribute("USER_MENU_ITEM", menuItemList);
        return menuItemList;
    }

    public void sendJson(HttpServletResponse response, String text) throws Exception {
        response.setContentType("text/json; charset=UTF-8");
        //设置MIME类型回传数据
        response.setHeader("progma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter out = response.getWriter();
        out.print(text);
        out.flush();
        out.close();
    }

    public void sendXML(HttpServletResponse response, String text) throws Exception {
        response.setContentType("text/xml; charset=GBK");
        response.setHeader("progma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        text = new String(text.getBytes("GBK"), "GBK");
        PrintWriter out = response.getWriter();
        out.print(text);
        out.flush();
        out.close();
    }

    protected final Object getBean(String name) throws Exception {
        return SystemServiceLocator.getInstance().getService(name);
    }

    protected String getExceptionMessage(Exception e) {
        StackTraceElement[] ele = e.getStackTrace();
        String str = "";

        for(int i = 0; i < ele.length; ++i) {
            str = str + ele[i] + "\n";
        }

        return str;
    }

    public String getMenuId() {
        if(this.menuId == null) {
            this.menuId = this.getRequest().getParameter("menuId");
        }

        return this.menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        if(StringUtil.isEmpty(this.menuId)) {
            return null;
        } else {
            List menuItemList = null;
            if(this.getSession().getAttribute("USER_MENU_ITEM") != null) {
                menuItemList = (List)this.getSession().getAttribute("USER_MENU_ITEM");
            } else {
                menuItemList = this.getMenuByUser();
            }

            Iterator i$ = menuItemList.iterator();

            IMenuItem iMenuItem;
            do {
                if(!i$.hasNext()) {
                    return this.menuName;
                }

                iMenuItem = (IMenuItem)i$.next();
            } while(!this.menuId.equals(iMenuItem.getMenuItemId().toString()));

            return iMenuItem.getMenuItemTitle();
        }
    }

    public void downLoadFile(String filePath) {
        ServletOutputStream out = null;
        FileInputStream fileInputStream = null;

        try {
            HttpServletResponse e = this.getResponse();
            HttpServletRequest request = this.getRequest();
            File file = new File(filePath);
            if(file.exists() || file.mkdirs()) {
                String clientLanguage = request.getHeader("Accept-Language");
                String guessCharset = ResponseEncodingUtil.getGuessCharset(clientLanguage);
                String fileNameEncode = ResponseEncodingUtil.encodingFileName(file.getName(), guessCharset);
                e.addHeader("Content-Disposition", "attachment; filename=" + fileNameEncode);
                this.log.debug("offline download from web server================the file path is: " + filePath);
                e.setContentType("application/octet-stream;charset=" + guessCharset);
                out = e.getOutputStream();
                fileInputStream = new FileInputStream(filePath);
                boolean bytesRead = false;
                byte[] buffer = new byte[1024];

                int bytesRead1;
                while((bytesRead1 = fileInputStream.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, bytesRead1);
                }
                return;
            }
        } catch (IOException var26) {
            this.log.error(var26);
            return;
        } finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException var25) {}
            }
            if(fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException var24) {}
            }
        }
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getNoDataToDisplay() {
        this.noDataToDisplay = "暂时没有数据！";
        return this.noDataToDisplay;
    }

    public String getNewLabelMonth() {
        this.newLabelMonth = CacheBase.getInstance().getNewLabelMonth();
        return this.newLabelMonth;
    }

    public String getNewLabelDay() {
        this.newLabelDay = CacheBase.getInstance().getNewLabelDay();
        return this.newLabelDay;
    }

    public boolean getIsAdmin() {
        try {
            String e = this.getUserId();
            this.isAdmin = PrivilegeServiceUtil.isAdminUser(e);
        } catch (Exception var2) {
            this.isAdmin = false;
        }

        return this.isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getCityId() {
        this.cityId = PrivilegeServiceUtil.getCityIdFromSession();
        return this.cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
