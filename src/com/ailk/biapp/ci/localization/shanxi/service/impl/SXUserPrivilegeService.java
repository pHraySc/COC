package com.ailk.biapp.ci.localization.shanxi.service.impl;

import com.ailk.biapp.ci.localization.shanxi.dao.SXBIConnectionEx;
import com.ailk.biapp.ci.localization.shanxi.dao.SqlcaSX;
import com.ailk.biapp.ci.localization.shanxi.model.SXCityInfo;
import com.ailk.biapp.ci.localization.shanxi.model.SXUser;
import com.ailk.biapp.ci.localization.shanxi.model.SXUserCompany;
import com.asiainfo.biframe.exception.ServiceException;
import com.asiainfo.biframe.privilege.ICity;
import com.asiainfo.biframe.privilege.IGroupRoleMap;
import com.asiainfo.biframe.privilege.IMenuItem;
import com.asiainfo.biframe.privilege.ISysResourceType;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.privilege.IUserApplication;
import com.asiainfo.biframe.privilege.IUserCompany;
import com.asiainfo.biframe.privilege.IUserDuty;
import com.asiainfo.biframe.privilege.IUserExt;
import com.asiainfo.biframe.privilege.IUserExtInfo;
import com.asiainfo.biframe.privilege.IUserGroup;
import com.asiainfo.biframe.privilege.IUserPreferForm;
import com.asiainfo.biframe.privilege.IUserPrivilegeService;
import com.asiainfo.biframe.privilege.IUserRight;
import com.asiainfo.biframe.privilege.IUserRightApply;
import com.asiainfo.biframe.privilege.IUserRole;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.springframework.stereotype.Service;

@Service("sxUserPrivilegeService")
public class SXUserPrivilegeService implements IUserPrivilegeService {
    private static final String CENTER_CITY_ID = "290";
    private static Logger log = Logger.getLogger(SXUserPrivilegeService.class);

    public SXUserPrivilegeService() {
    }

    public boolean isAdminUser(String userId) throws ServiceException {
        boolean isAdmin = false;
        SqlcaSX sqlCaSX = null;

        try {
            sqlCaSX = new SqlcaSX(new SXBIConnectionEx());
            StringBuffer e = new StringBuffer();
            e.append("SELECT JOB_ID FROM LKG_STAFF_JOB WHERE STAFF_ID=\'").append(userId).append("\'");
            sqlCaSX.execute(e.toString());

            while(true) {
                String jobId;
                do {
                    do {
                        if(!sqlCaSX.next()) {
                            return isAdmin;
                        }

                        jobId = sqlCaSX.getString("JOB_ID");
                    } while(null == jobId);
                } while(!jobId.equals("10001") && !jobId.equals("1003") && !jobId.equals("1004") && !jobId.equals("2002") && !jobId.equals("2002A"));

                isAdmin = true;
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        } finally {
            if(null != sqlCaSX) {
                sqlCaSX.closeAll();
            }

        }

        return isAdmin;
    }

    public IUser getUser(String userId) throws ServiceException {
        SXUser sxUser = new SXUser(userId);
        return sxUser;
    }

    public IUserCompany getUserDept(String userId) throws ServiceException {
        SXUserCompany company = new SXUserCompany(userId);
        return company;
    }

    public List<ICity> getCityByUser(String userId) throws ServiceException {
        log.debug("Jump into method getCityByUser");
        log.debug("getCityByUser\'s parameter userId:" + userId);
        Object list = null;
        SXUser user = (SXUser)this.getUser(userId);
        String cityId = user.getCityid();
        String countryId = user.getCountryid();
        if(!"0000".equals(countryId)) {
            cityId = countryId;
        }

        log.debug("User\'s city id is :" + cityId);
        if("290".equals(cityId)) {
            list = this.getSubCitysById(cityId);
            ((List)list).add((new SXCityInfo()).buildCenterCity());
        } else if(cityId.length() == 2) {
            list = this.getSubCitysById(cityId);
            ((List)list).add(this.getCityByCityID(cityId));
        } else {
            list = new ArrayList();
            ((List)list).add(this.getCityByCityID(cityId));
        }

        log.debug("Method getCityByUser\'s result size:" + ((List)list).size());
        return (List)list;
    }

    public List<ICity> getSubCitysById(String cityId) throws ServiceException {
        log.debug("getSubCitysById\'s parameter city:" + cityId);
        String sql = null;
        ArrayList result = new ArrayList();
        if("290".equals(cityId)) {
            sql = "SELECT EPARCHY_ID\t\tAS cityId, EPARCHY_NAME \tAS cityName, \'290\'\tAS parentId, EPARCHY_ID \t\tAS dmCityId, \'-1\'\t\t \tAS dmCountyId FROM TD_EPARCHY_CITY ";
        } else {
            if(cityId.length() != 2) {
                return new ArrayList();
            }

            sql = "SELECT  CITY_CODE\tAS cityId, CITY_NAME  \tAS cityName, EPARCHY_ID \tAS parentId, EPARCHY_ID \tAS dmCityId, CITY_CODE\tAS dmCountyId FROM TD_CITYCODE WHERE EPARCHY_ID=\'" + cityId + "\'";
        }

        log.info("Method getSubCitysById sql:" + sql);
        SqlcaSX sqlCaSX = null;

        try {
            sqlCaSX = new SqlcaSX(new SXBIConnectionEx());
            sqlCaSX.execute(sql);

            while(sqlCaSX.next()) {
                SXCityInfo e = new SXCityInfo();
                e.setCityId(sqlCaSX.getString("cityid"));
                e.setCityName(sqlCaSX.getString("cityName"));
                e.setParentId(sqlCaSX.getString("parentId"));
                e.setDmCityId(sqlCaSX.getString("dmCityId"));
                e.setDmCountyId(sqlCaSX.getString("dmCountyId"));
                e.setDmDeptId("-1");
                result.add(e);
                log.debug("Method getSubCitysById result =========>>>>>cityid:" + sqlCaSX.getString("cityid") + ";cityName:" + sqlCaSX.getString("cityName") + ";parentId:" + sqlCaSX.getString("parentId") + ";dmCityId:" + sqlCaSX.getString("dmCityId") + ";dmCountyId:" + sqlCaSX.getString("dmCountyId"));
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        } finally {
            if(null != sqlCaSX) {
                sqlCaSX.closeAll();
            }

        }

        return result;
    }

    public ICity getCityByCityID(String cityId) throws ServiceException {
        String sql = null;
        log.debug("getCityByCityID\'s parameter city:" + cityId);
        if("290".equals(cityId)) {
            sql = "SELECT \'" + cityId + "\' \t\tAS \tcityId, " + "\'Ê¡ÖÐÐÄ \' \tAS\tcityName, " + "\'" + "290" + "\'\t\tAS \tparentId, " + "\'" + "290" + "\'\t\tAS \tdmCityId, " + "\'-1\'\t\tAS\tdmCountyId " + "FROM SYSIBM.SYSDUMMY1";
        } else if(cityId.length() == 2) {
            sql = "SELECT EPARCHY_ID\tAS\tcityId, EPARCHY_NAME AS cityName, \'290\' \t\tAS\tparentId, EPARCHY_ID\tAS \tdmCityId, \'-1\' \tAS\tdmCountyId FROM TD_EPARCHY_CITY WHERE EPARCHY_ID=\'" + cityId + "\'";
        } else {
            sql = "SELECT  CITY_CODE \tAS cityId , CITY_NAME\tAS cityName,  EPARCHY_ID\tAS parentId, EPARCHY_ID\tAS dmCityId, CITY_CODE\tAS dmCountyId FROM TD_CITYCODE WHERE CITY_CODE=\'" + cityId + "\'";
        }

        log.info("Method getCityByCityID\'s sql:" + sql);
        SqlcaSX sqlCaSX = null;
        SXCityInfo city = new SXCityInfo();

        try {
            sqlCaSX = new SqlcaSX(new SXBIConnectionEx());
            sqlCaSX.execute(sql);

            while(sqlCaSX.next()) {
                city.setCityId(sqlCaSX.getString("cityid"));
                city.setCityName(sqlCaSX.getString("cityName"));
                city.setParentId(sqlCaSX.getString("parentId"));
                city.setDmCityId(sqlCaSX.getString("dmCityId"));
                city.setDmCountyId(sqlCaSX.getString("dmCountyId"));
                city.setDmDeptId("-1");
                log.debug("Method getCityByCityID result =========>>>>>cityid:" + sqlCaSX.getString("cityid") + ";cityName:" + sqlCaSX.getString("cityName") + ";parentId:" + sqlCaSX.getString("parentId") + ";dmCityId:" + sqlCaSX.getString("dmCityId") + ";dmCountyId:" + sqlCaSX.getString("dmCountyId"));
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        } finally {
            if(null != sqlCaSX) {
                sqlCaSX.closeAll();
            }

        }

        return city;
    }

    public boolean addPublishMenu(int arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String[] arg6) {
        return false;
    }

    public String decryption(String arg0) {
        return null;
    }

    public boolean deletePublishMenu(String arg0, String arg1) {
        return false;
    }

    public void doAffirmApply(String arg0, String arg1) throws ServiceException {
    }

    public String doCreateApply(String arg0, String arg1, String arg2, String arg3, List<IUserRight> arg4) throws ServiceException {
        return null;
    }

    public String encryption(String arg0) {
        return null;
    }

    public Collection<IUserApplication> getAllApplications() throws ServiceException {
        return null;
    }

    public List<ICity> getAllCity() throws ServiceException {
        return null;
    }

    public String getAllCityID(String arg0, boolean arg1) throws ServiceException {
        return null;
    }

    public List<IMenuItem> getAllMenuItem(String arg0) throws ServiceException {
        return null;
    }

    public List<IUserRole> getAllRoles(String arg0, int arg1, int arg2) throws ServiceException {
        return null;
    }

    public List<IUserGroup> getAllSubGroupsByUserId(String arg0) throws ServiceException {
        return null;
    }

    public List<IUser> getAllSubUsersByUserId(String arg0) throws ServiceException {
        return null;
    }

    public List<IUserCompany> getAllUserCompany() throws ServiceException {
        return null;
    }

    public List<IUserGroup> getAllUserGroup() throws ServiceException {
        return null;
    }

    public List<IUserRole> getAllUserRole() throws ServiceException {
        return null;
    }

    public List<IUser> getAllUsers() throws ServiceException {
        return null;
    }

    public IUserRightApply getApplyById(String arg0) throws ServiceException {
        return null;
    }

    public ICity getCityByDmCity(String arg0, String arg1) throws ServiceException {
        return null;
    }

    public String getDmCity(String arg0, String arg1, String arg2, String arg3) throws ServiceException {
        return null;
    }

    public IUserGroup getGroupObject(String arg0) throws ServiceException {
        return null;
    }

    public List<IGroupRoleMap> getGroupRoleMapList() throws ServiceException {
        return null;
    }

    public List<IUser> getMapUsers(String arg0, int arg1, String arg2) throws ServiceException {
        return null;
    }

    public List<IMenuItem> getMenuItemList(String arg0, Map<String, String> arg1) throws ServiceException {
        return null;
    }

    public IMenuItem getMeunItemById(Integer arg0) throws ServiceException {
        return null;
    }

    public List<String> getPreferDescListByPreferType(String arg0, String arg1) throws ServiceException {
        return null;
    }

    public List<IUserRight> getPrivacyRights(String arg0) {
        return null;
    }

    public String getResourceName(int arg0, int arg1, String arg2) throws ServiceException {
        return null;
    }

    public ISysResourceType getResourceTypeByRoleRes(int arg0, int arg1) throws ServiceException {
        return null;
    }

    public List<LabelValueBean> getResourceTypeByRoleType(int arg0) throws ServiceException {
        return null;
    }

    public List<IUserRight> getRight(String arg0, int arg1) throws ServiceException {
        return null;
    }

    public List<IUserRight> getRight(String arg0, int arg1, int arg2) throws ServiceException {
        return null;
    }

    public List<IUserRight> getRightByOperation(String arg0, int arg1, int arg2, String arg3) throws ServiceException {
        return null;
    }

    public List<LabelValueBean> getRoleType() throws ServiceException {
        return null;
    }

    public String getShowDmCity(String arg0, String arg1, String arg2, String arg3) throws ServiceException {
        return null;
    }

    public List<IUserRight> getShowTreeRight(String arg0, int arg1) {
        return null;
    }

    public List<IUserCompany> getSubCompanyById(String arg0) throws ServiceException {
        return null;
    }

    public List<IMenuItem> getSubMenuItemById(Integer arg0, String arg1, boolean arg2) {
        return null;
    }

    public Collection<IUserRight> getTempRightsByApplyId(String arg0) throws ServiceException {
        return null;
    }

    public long getUserAmount() {
        return 0L;
    }

    public IUserCompany getUserCompanyById(String arg0) throws ServiceException {
        return null;
    }

    public List<IUserCompany> getUserCompanyByName(String arg0) throws ServiceException {
        return null;
    }

    public String getUserCurrentCity(String arg0) throws ServiceException {
        return null;
    }

    public String getUserDmCity(String arg0, String arg1) throws ServiceException {
        return null;
    }

    public IUserDuty getUserDutyById(String arg0) throws ServiceException {
        return null;
    }

    public IUserDuty getUserDutyByUser(IUser arg0) throws ServiceException {
        return null;
    }

    public IUserExt getUserExt(String arg0) throws ServiceException {
        return null;
    }

    public List<IUserExtInfo> getUserExtInfo(String arg0) throws Exception {
        return null;
    }

    public IUserGroup getUserGroupById(String arg0) throws ServiceException {
        return null;
    }

    public List<IUserGroup> getUserGroupByUserId(List<String> arg0) throws ServiceException {
        return null;
    }

    public List<IUser> getUserList(List<String> arg0) throws ServiceException {
        return null;
    }

    public List<IUserPreferForm> getUserPreferList(String arg0) throws ServiceException {
        return null;
    }

    public List<IUserRole> getUserRole(String arg0, String arg1) throws ServiceException {
        return null;
    }

    public IUserRole getUserRoleById(String arg0) throws ServiceException {
        return null;
    }

    public String getUserSensitiveLevel(String arg0) throws ServiceException {
        return null;
    }

    public List<IUser> getUsersByGroupId(String arg0) throws ServiceException {
        return null;
    }

    public List<IUser> getUsersByPage(String arg0, String arg1) {
        return null;
    }

    public List<IUser> getUsersOfDepartment(int arg0) throws ServiceException {
        return null;
    }

    public List<IUserGroup> getValidChildGroups(String arg0) throws ServiceException {
        return null;
    }

    public boolean haveOperRight(String arg0, String arg1, String arg2) {
        return false;
    }

    public boolean haveOperateRight(String arg0, int arg1, String arg2, String arg3) throws ServiceException {
        return false;
    }

    public boolean haveRightByUserId(String arg0, IUserRight arg1) throws ServiceException {
        return false;
    }

    public boolean haveRightByUserId(String arg0, int arg1, String arg2) throws ServiceException {
        return false;
    }

    public boolean haveRightByUserId(String arg0, int arg1, int arg2, String arg3) throws ServiceException {
        return false;
    }

    public boolean isPwdChangedByOthers(String arg0) {
        return false;
    }

    public boolean isPwdNeedChange(String arg0) {
        return false;
    }

    public boolean isUserLegal(String arg0, String arg1) {
        return false;
    }

    public boolean modPublishMenu(int arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String[] arg6) {
        return false;
    }

    public String queryFolderList(int arg0) {
        return null;
    }

    public void saveRight(String arg0, int arg1, int arg2, String arg3, int arg4, String arg5) throws ServiceException {
    }

    public void saveUserPrefer(String arg0, String arg1, String arg2) throws ServiceException {
    }
}
