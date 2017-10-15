package com.ailk.biapp.ci.localization.hubei.service.impl;

import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.localization.chongqing.model.CityUser;
import com.asiainfo.biframe.exception.ServiceException;
import com.asiainfo.biframe.privilege.ICity;
import com.asiainfo.biframe.privilege.model.User_User;
import com.asiainfo.biframe.privilege.sysmanage.publish.PrivilegeServiceImpl;
import com.asiainfo.biframe.utils.config.Configure;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CiHbUserRoleServiceImpl extends PrivilegeServiceImpl {
    public CiHbUserRoleServiceImpl() {
    }

    public List<ICity> getCityByUser(String loginName) {
        ArrayList cityList = new ArrayList();
        String sqlCounty = "select userid,cityid from user_user where userid=?";
        List rs = null;
        rs = (new JdbcBaseDao()).getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_Local")).queryForList(sqlCounty, new Object[]{loginName});
        String cityId = "";

        Map i$;
        for(Iterator sql = rs.iterator(); sql.hasNext(); cityId = i$.get("cityid").toString()) {
            i$ = (Map)sql.next();
        }

        StringBuffer sql1 = new StringBuffer();
        rs = null;
        if("0".equals(cityId)) {
            sql1.append("select 0 as realCityId,-1 as cityid,\'湖北省\' as cityname,\'null\' as parentid,-1 as deptid,-1 as countyid  from user_user where userid=?");
            rs = (new JdbcBaseDao()).getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_Local")).queryForList(sql1.toString(), new Object[]{loginName});
        } else {
            sql1.append("select (case when f.dm_county_id=\'-1\' then f.cityid else null end) as realCityId,f.cityid as cityid,cityname,\'-1\' as parentid,\'-1\' as countyid,\'-1\' as deptid from user_group_map a,group_role_map c,").append("user_role d,user_right e,user_city f where a.group_id=c.group_id and ").append("c.role_id=d.role_id and d.role_id=e.operatorid and e.resourceid=f.cityid ").append("and e.operatortype=0 and d.role_type=0 and d.resourcetype=5 and a.userid=?");
            rs = (new JdbcBaseDao()).getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_Local")).queryForList(sql1.toString(), new Object[]{loginName});
        }

        Iterator i$1 = rs.iterator();

        while(i$1.hasNext()) {
            Map m = (Map)i$1.next();
            if(null != m.get("realCityId") && !"null".equals(m.get("realCityId").toString())) {
                CityUser cityUser = new CityUser();
                cityUser.setCityId(m.get("realCityId").toString());
                cityUser.setParentId(m.get("parentid").toString().equals("null")?null:m.get("parentid").toString());
                cityUser.setDmCityId(m.get("cityid").toString());
                cityUser.setDmDeptId(m.get("deptid").toString());
                cityUser.setDmCountyId(m.get("countyid").toString());
                cityUser.setCityName(m.get("cityname").toString());
                cityUser.setSortNum(0);
                cityUser.setDmTypeId((String)null);
                cityUser.setDmTypeCode((String)null);
                cityList.add(cityUser);
            }
        }

        return cityList;
    }

    public List<ICity> getSubCitysById(String parentCityId) throws ServiceException {
        ArrayList subcityList = new ArrayList();
        List rs = null;
        if(parentCityId.equals("-1") || "0".equals(parentCityId)) {
            String i$ = "select area_id as realCityId,area_id as cityid,area_name as cityname,\'-1\' as parentid,\'-1\' as deptid,\'-1\' as countyid from bt_area order by area_id";
            rs = (new JdbcBaseDao()).getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_CI")).queryForList(i$, new Object[0]);
        }

        if(null != rs) {
            Iterator i$1 = rs.iterator();

            while(i$1.hasNext()) {
                Map m = (Map)i$1.next();
                CityUser cityUser = new CityUser();
                cityUser.setCityId(m.get("realCityId").toString());
                cityUser.setParentId(m.get("parentid").toString().equals("null")?null:m.get("parentid").toString());
                cityUser.setDmCityId(m.get("cityid").toString());
                cityUser.setDmDeptId(m.get("deptid").toString());
                cityUser.setDmCountyId(m.get("countyid").toString());
                cityUser.setCityName(m.get("cityname").toString());
                cityUser.setSortNum(0);
                cityUser.setDmTypeId((String)null);
                cityUser.setDmTypeCode((String)null);
                subcityList.add(cityUser);
            }
        }

        return subcityList;
    }

    public ICity getCityByCityID(String cityId) throws ServiceException {
        CityUser cityUser = null;
        List rs = null;
        String sql = " ";
        if(!"-1".equals(cityId) && !"0".equals(cityId)) {
            sql = "select area_id as realCityId,area_id as cityid,\'-1\' as parentid,area_name as cityname,\'-1\' as countyid from bt_area where area_code=?";
            rs = (new JdbcBaseDao()).getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_CI")).queryForList(sql, new Object[]{cityId});
        } else {
            sql = "select \'0\' as realCityId,\'-1\' as cityid,\'null\' as parentid,\'湖北省\' as cityname,\'-1\' as countyid from bt_area_all";
            rs = (new JdbcBaseDao()).getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_CI")).queryForList(sql, new Object[0]);
        }

        if(rs.size() > 0) {
            Map m = (Map)rs.get(0);
            cityUser = new CityUser();
            cityUser.setCityId(m.get("realCityId").toString());
            cityUser.setParentId(m.get("parentid").toString().equals("null")?null:m.get("parentid").toString());
            cityUser.setDmCityId(m.get("cityid").toString());
            cityUser.setDmDeptId("-1");
            cityUser.setDmCountyId(m.get("countyid").toString());
            cityUser.setCityName(m.get("cityname").toString());
            cityUser.setSortNum(0);
            cityUser.setDmTypeId((String)null);
            cityUser.setDmTypeCode((String)null);
        }

        return cityUser;
    }

    public User_User getUser(String loginNo) throws ServiceException {
        User_User user = null;
        List rs = null;
        String sql = "select userid as userid,username as username,cityid as cityid,departmentid as departmentid from user_user where userid=?";
        rs = (new JdbcBaseDao()).getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_Local")).queryForList(sql, new Object[]{loginNo});
        if(rs.size() > 0) {
            Map m = (Map)rs.get(0);
            user = new User_User();
            user.setUserid(m.get("userid").toString());
            user.setUsername(m.get("username").toString());
            user.setCityid(m.get("cityid").toString());
            user.setDepartmentid(Integer.parseInt(m.get("departmentid").toString()));
            user.setGroupId((String)null);
            user.setEmail((String)null);
            user.setMobilePhone((String)null);
            user.setDomainType((String)null);
        }

        return user;
    }
}
