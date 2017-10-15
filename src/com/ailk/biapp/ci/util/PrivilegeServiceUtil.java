package com.ailk.biapp.ci.util;

import com.ailk.biapp.ci.constant.CommonConstants;
import com.ailk.biapp.ci.localization.hubei.service.ICiHbUserCityService;
import com.asiainfo.biframe.exception.NotLoginException;
import com.asiainfo.biframe.privilege.ICity;
import com.asiainfo.biframe.privilege.IMenuItem;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.privilege.IUserCompany;
import com.asiainfo.biframe.privilege.IUserPrivilegeService;
import com.asiainfo.biframe.privilege.IUserSession;
import com.asiainfo.biframe.privilege.webosimpl.UserPrivilegeServiceImpl;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.i18n.LocaleUtil;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.opensymphony.xwork2.ActionContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class PrivilegeServiceUtil {
    private static Logger log = Logger.getLogger(PrivilegeServiceUtil.class);

    public PrivilegeServiceUtil() {
    }

    public static String getUserId() {
        try {
            HttpSession e = getSession();
            IUserSession mysession = (IUserSession)e.getAttribute("biplatform_user");
            return "nj".equalsIgnoreCase(CommonConstants.base)?mysession.getUserID().toLowerCase():mysession.getUserID();
        } catch (Exception var2) {
            log.error(LocaleUtil.getLocaleMessage("core", "core.java.getUserFail"), var2);
            throw new NotLoginException(LocaleUtil.getLocaleMessage("core", "core.java.getOperatorFail"), var2);
        }
    }

    public static IUserSession getUserSession() {
        HttpSession session = getSession();
        IUserSession mysession = null;
        if(session != null) {
            mysession = (IUserSession)session.getAttribute("biplatform_user");
        }

        return mysession;
    }

    public static HttpSession getSession() {
        ActionContext ctx = ActionContext.getContext();
        if(ctx == null) {
            log.info("ctx is null");
            return null;
        } else {
            HttpServletRequest request = (HttpServletRequest)ctx.get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
            if(request == null) {
                log.info("request is null");
                return null;
            } else {
                HttpSession session = request.getSession();
                if(session == null) {
                    log.info("session is null" + session);
                }

                return session;
            }
        }
    }

    public static HttpServletRequest getHttpServletRequest() {
        ActionContext ctx = ActionContext.getContext();
        if(ctx == null) {
            return null;
        } else {
            HttpServletRequest request = (HttpServletRequest)ctx.get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
            return request == null?null:request;
        }
    }

    public static String getUserGroupId() throws NotLoginException {
        try {
            IUserSession e = (IUserSession)getSession().getAttribute("biplatform_user");
            return e.getGroupId();
        } catch (Exception var1) {
            log.error(LocaleUtil.getLocaleMessage("core", "core.java.getUserGroupFail"), var1);
            throw new NotLoginException(LocaleUtil.getLocaleMessage("core", "core.java.getOperatorGroupFail"), var1);
        }
    }

    public static String getUserCityId() throws NotLoginException {
        try {
            IUserSession e = (IUserSession)getSession().getAttribute("biplatform_user");
            return e.getUserCityID();
        } catch (Exception var1) {
            log.error(LocaleUtil.getLocaleMessage("core", "core.java.getOperatorFail"), var1);
            throw new NotLoginException(LocaleUtil.getLocaleMessage("core", "core.java.getOperatorFail"), var1);
        }
    }

    public static IUser getUser() throws NotLoginException {
        try {
            IUserSession e = (IUserSession)getSession().getAttribute("biplatform_user");
            return e.getUser();
        } catch (Exception var1) {
            log.error(LocaleUtil.getLocaleMessage("core", "core.java.getUserObjectFail"), var1);
            throw new NotLoginException(LocaleUtil.getLocaleMessage("core", "core.java.getUserObjectFail"), var1);
        }
    }

    public static IUser getUserById(String userId) throws Exception {
        IUserPrivilegeService userPrivilegeService = getUserPrivilegeService();
        return userPrivilegeService.getUser(userId);
    }

    public static boolean isAdminUser(String userId) throws Exception {
        if(null == userId) {
            return false;
        } else {
            IUserPrivilegeService userPrivilegeService = getUserPrivilegeService();
            HttpSession session = getSession();
            Boolean isAdmin = null;
            if(session != null) {
                isAdmin = (Boolean)session.getAttribute("isAdminUser_" + userId);
                if(null == isAdmin) {
                    isAdmin = Boolean.valueOf(userPrivilegeService.isAdminUser(userId));
                    getSession().setAttribute("isAdminUser_" + userId, isAdmin);
                }
            } else {
                isAdmin = Boolean.valueOf(userPrivilegeService.isAdminUser(userId));
            }

            return isAdmin.booleanValue();
        }
    }

    public static IUserPrivilegeService getUserPrivilegeService() throws Exception {
        return (IUserPrivilegeService)("bj".equalsIgnoreCase(CommonConstants.base)?(IUserPrivilegeService)SystemServiceLocator.getInstance().getService("userPrivilegeService"):new UserPrivilegeServiceImpl());
    }

    public static String getUserDept(String userId) throws Exception {
        IUserPrivilegeService userPrivilegeService = getUserPrivilegeService();
        return userPrivilegeService.getUserDept(userId).getTitle();
    }

    public static Integer getUserDeptId(String userId) throws Exception {
        IUserPrivilegeService userPrivilegeService = getUserPrivilegeService();
        return userPrivilegeService.getUserDept(userId).getDeptid();
    }

    public static List<ICity> getUserCitys(String userId) throws Exception {
        IUserPrivilegeService userPrivilegeService = getUserPrivilegeService();
        return userPrivilegeService.getCityByUser(userId);
    }

    public static List<ICity> getUserCitysForLabelTree(String userId) throws Exception {
        ICiHbUserCityService userCityService = (ICiHbUserCityService)SystemServiceLocator.getInstance().getService("ICiHbUserCityService");
        return userCityService.getCityByUserForLabelTree(userId);
    }

    public static List<String> getUserCityIds(String userId) throws Exception {
        List cityList = getUserCitys(userId);
        ArrayList cityIdList = new ArrayList();
        HashSet allCitys = new HashSet();
        Iterator noCity = cityList.iterator();

        while(noCity.hasNext()) {
            ICity centerCity = (ICity)noCity.next();
            allCitys.add(centerCity.getCityId());
        }

        String noCity1 = Configure.getInstance().getProperty("NO_CITY");
        String centerCity1 = Configure.getInstance().getProperty("CENTER_CITYID");
        Iterator i$ = cityList.iterator();

        while(i$.hasNext()) {
            ICity city = (ICity)i$.next();
            if(centerCity1.equals(city.getDmCityId()) || centerCity1.equals(city.getCityId())) {
                cityIdList.clear();
                cityIdList.add(String.valueOf(-1));
                return cityIdList;
            }

            if(!allCitys.contains(city.getParentId())) {
                if(noCity1.equals(city.getDmDeptId())) {
                    if(noCity1.equals(city.getDmCountyId())) {
                        cityIdList.add(city.getDmCityId());
                    } else {
                        cityIdList.add(city.getDmCountyId());
                    }
                } else {
                    cityIdList.add(city.getDmDeptId());
                }
            }
        }

        return cityIdList;
    }

    public static List<String> getAllUserCityIds(String userId) throws Exception {
        IUserPrivilegeService userPrivilegeService = getUserPrivilegeService();
        List cityList = userPrivilegeService.getCityByUser(userId);
        ArrayList cityIdList = new ArrayList();
        String noCity = Configure.getInstance().getProperty("NO_CITY");
        String centerCity = Configure.getInstance().getProperty("CENTER_CITYID");
        Iterator i$ = cityList.iterator();

        while(true) {
            while(true) {
                while(true) {
                    while(i$.hasNext()) {
                        ICity city = (ICity)i$.next();
                        if(noCity.equals(city.getDmDeptId())) {
                            List subCities;
                            Iterator i$1;
                            ICity subCity;
                            if(noCity.equals(city.getDmCountyId())) {
                                cityIdList.add(city.getDmCityId());
                                if(!noCity.equals(city.getDmCityId()) && !centerCity.equals(city.getDmCityId())) {
                                    subCities = getSubCitys(city.getCityId());
                                    i$1 = subCities.iterator();

                                    while(i$1.hasNext()) {
                                        subCity = (ICity)i$1.next();
                                        cityIdList.add(subCity.getDmCountyId());
                                    }
                                } else {
                                    subCities = getSubCitys(city.getCityId());
                                    i$1 = subCities.iterator();

                                    while(i$1.hasNext()) {
                                        subCity = (ICity)i$1.next();
                                        cityIdList.add(subCity.getDmCityId());
                                    }
                                }
                            } else {
                                cityIdList.add(city.getDmCountyId());
                                subCities = getSubCitys(city.getCityId());
                                i$1 = subCities.iterator();

                                while(i$1.hasNext()) {
                                    subCity = (ICity)i$1.next();
                                    cityIdList.add(subCity.getDmDeptId());
                                }
                            }
                        } else {
                            cityIdList.add(city.getDmDeptId());
                        }
                    }

                    return cityIdList;
                }
            }
        }
    }

    public static ICity getCityByCityId(String cityId) throws Exception {
        IUserPrivilegeService userPrivilegeService = getUserPrivilegeService();
        return userPrivilegeService.getCityByCityID(cityId);
    }

    public static List<String> getCityIdsForFormAnalysis(String userId) throws Exception {
        String cityLevel = Configure.getInstance().getProperty("CITY_LEVEL");
        IUserPrivilegeService userPrivilegeService = getUserPrivilegeService();
        List cityList = userPrivilegeService.getCityByUser(userId);
        ArrayList cityIdList = new ArrayList();
        HashMap dmCityIdMap = new HashMap();
        HashMap dmCountyIdMap = new HashMap();
        String noCity = Configure.getInstance().getProperty("NO_CITY");
        String centerCity = Configure.getInstance().getProperty("CENTER_CITYID");
        Iterator iterator = cityList.iterator();

        List subCities;
        Iterator i$;
        ICity subCity;
        label76:
        while(iterator.hasNext()) {
            ICity dmCityId = (ICity)iterator.next();
            if(noCity.equals(dmCityId.getDmCityId()) || centerCity.equals(dmCityId.getDmCityId())) {
                subCities = getSubCitys(dmCityId.getCityId());
                i$ = subCities.iterator();

                while(true) {
                    if(!i$.hasNext()) {
                        break label76;
                    }

                    subCity = (ICity)i$.next();
                    cityIdList.add(subCity.getDmCityId());
                }
            }

            if(noCity.equals(dmCityId.getDmCountyId())) {
                dmCityIdMap.put(dmCityId.getDmCityId(), dmCityId.getCityId());
            } else if(!noCity.equals(dmCityId.getDmCountyId())) {
                dmCountyIdMap.put(dmCityId.getDmCountyId(), dmCityId.getCityId());
            }
        }

        if(dmCityIdMap.size() > 1) {
            cityIdList.addAll(dmCityIdMap.keySet());
            return cityIdList;
        } else {
            if(dmCityIdMap.size() == 1) {
                if("true".equals(cityLevel)) {
                    cityIdList.addAll(dmCityIdMap.keySet());
                } else {
                    iterator = dmCityIdMap.keySet().iterator();

                    while(true) {
                        do {
                            do {
                                if(!iterator.hasNext()) {
                                    if(cityIdList.size() < 1) {
                                        cityIdList.addAll(dmCityIdMap.keySet());
                                    }

                                    return cityIdList;
                                }

                                String dmCityId1 = (String)iterator.next();
                                subCities = getSubCitys((String)dmCityIdMap.get(dmCityId1));
                            } while(null == subCities);
                        } while(subCities.size() <= 0);

                        i$ = subCities.iterator();

                        while(i$.hasNext()) {
                            subCity = (ICity)i$.next();
                            cityIdList.add(subCity.getDmCountyId());
                        }
                    }
                }
            } else if(dmCityIdMap.size() == 0 && dmCountyIdMap.size() > 0) {
                cityIdList.addAll(dmCountyIdMap.keySet());
            }

            return cityIdList;
        }
    }

    public static List<ICity> getSubCitys(String cityId) throws Exception {
        IUserPrivilegeService userPrivilegeService = getUserPrivilegeService();
        return userPrivilegeService.getSubCitysById(cityId);
    }

    public static List<IUserCompany> getAllUserCompany() throws Exception {
        IUserPrivilegeService userPrivilegeService = getUserPrivilegeService();
        return userPrivilegeService.getAllUserCompany();
    }

    public List<IMenuItem> getMenuByUser(String userId) throws Exception {
        IUserPrivilegeService userPrivilegeService = getUserPrivilegeService();
        ArrayList menuList = new ArrayList();
        List IMenuList = userPrivilegeService.getAllMenuItem(userId);
        isAdminUser(userId);
        Iterator i$ = IMenuList.iterator();

        while(i$.hasNext()) {
            IMenuItem iMenu = (IMenuItem)i$.next();
            if(iMenu != null && iMenu.getMenuItemId().toString().startsWith("93")) {
                menuList.add(iMenu);
            }
        }

        Collections.sort(menuList, new Comparator() {
        	
            public int compare(IMenuItem o1, IMenuItem o2) {
                return o1 != null && o1.getSortNum() != null?(o2 != null && o2.getSortNum() != null?o1.getSortNum().intValue() - o2.getSortNum().intValue():1):-1;
            }

			@Override
			public int compare(Object o1, Object o2) {
				// TODO Auto-generated method stub
				return 0;
			}
        });
        return menuList;
    }

    public static String getCityId(String userId) throws Exception {
        IUserPrivilegeService userPrivilegeService = getUserPrivilegeService();
        IUser user = userPrivilegeService.getUser(userId);
        String cityIdOne = user.getCityid();
        String root = Configure.getInstance().getProperty("CENTER_CITYID");
        String noCity = Configure.getInstance().getProperty("NO_CITY");
        return getOneLevCityId(cityIdOne, root, noCity);
    }

    private static String getOneLevCityId(String cityId, String root, String noCity) {
        try {
            IUserPrivilegeService e = getUserPrivilegeService();
            if(!cityId.equals(root) && cityId != root && cityId != noCity && !cityId.equals(noCity)) {
                ICity city = e.getCityByCityID(cityId);
                String parentId = city.getParentId();
                return parentId != null && !parentId.equals(root) && parentId != root && parentId != noCity && !parentId.equals(noCity)?getOneLevCityId(city.getParentId(), root, noCity):city.getDmCityId();
            } else {
                return e.getCityByCityID(cityId).getDmCityId();
            }
        } catch (Exception var6) {
            log.error("获取地市ID失败", var6);
            return null;
        }
    }

    public static String getCityIdFromSession() throws NotLoginException {
        try {
            HttpSession e = getSession();
            String cityId = (String)e.getAttribute("cocUserCityId");
            return cityId;
        } catch (Exception var2) {
            log.error("获取用户一级地市或者省会失败", var2);
            throw new NotLoginException(LocaleUtil.getLocaleMessage("core", "core.java.getOperatorFail"), var2);
        }
    }

    public static void setCityIdSession(HttpSession session, String userId) throws NotLoginException {
        try {
            log.debug("session=" + session);
            String e = getCityId(userId);
            if(StringUtil.isEmpty(e)) {
                throw new Exception("cityId为空或者NULL");
            }

            session.setAttribute("cocUserCityId", e);
        } catch (Exception var3) {
            log.error("获取用户一级地市或者省会失败", var3);
        }

    }

    public static void removeCityIdSession(HttpSession session) {
        try {
            session.removeAttribute("cocUserCityId");
        } catch (Exception var2) {
            log.error("移除用户一级地市或者省会seesion值失败", var2);
        }

    }

    public static boolean isProvinceUser(String userId) {
        String root = Configure.getInstance().getProperty("CENTER_CITYID");
        String cityId = getCityIdFromSession();
        return cityId.equals(root) || cityId == root;
    }

    public static void removeShopCarSession(HttpSession session) {
        try {
            session.removeAttribute("sessionModelList");
            session.removeAttribute("calcElementNum");
        } catch (Exception var2) {
            log.error("移除购物车seesion值失败", var2);
        }

    }
}
