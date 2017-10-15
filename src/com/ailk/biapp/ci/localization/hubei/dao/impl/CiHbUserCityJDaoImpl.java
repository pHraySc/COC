package com.ailk.biapp.ci.localization.hubei.dao.impl;

import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.localization.chongqing.model.CityUser;
import com.ailk.biapp.ci.localization.hubei.dao.ICiHbUserCityJDao;
import com.asiainfo.biframe.privilege.ICity;
import com.asiainfo.biframe.utils.config.Configure;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class CiHbUserCityJDaoImpl extends JdbcBaseDao implements ICiHbUserCityJDao {
    public CiHbUserCityJDaoImpl() {
    }

    public List<ICity> getCityByUserForLabelTree(String loginName) {
        ArrayList cityList = new ArrayList();
        String sqlCounty = "select userid,cityid from user_user where userid=?";
        List rs = null;
        rs = (new JdbcBaseDao()).getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_CI")).queryForList(sqlCounty, new Object[]{loginName});
        String cityId = "";

        Map i$;
        for(Iterator sql = rs.iterator(); sql.hasNext(); cityId = i$.get("cityid").toString()) {
            i$ = (Map)sql.next();
        }

        StringBuffer sql1 = new StringBuffer();
        rs = null;
        if("0".equals(cityId)) {
            sql1.append("select 0 as realCityId,-1 as cityid,\'ºþ±±Ê¡\' as cityname,\'null\' as parentid,-1 as deptid,-1 as countyid  from user_user where userid=?");
            rs = (new JdbcBaseDao()).getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_CI")).queryForList(sql1.toString(), new Object[]{loginName});
        } else {
            sql1.append("select (case when f.dm_county_id=\'-1\' then f.cityid else null end) as realCityId,f.cityid as cityid,cityname,\'-1\' as parentid,\'-1\' as countyid,\'-1\' as deptid from user_group_map a,group_role_map c,").append("user_role d,user_right e,user_city f where a.group_id=c.group_id and ").append("c.role_id=d.role_id and d.role_id=e.operatorid and e.resourceid=f.cityid ").append("and e.operatortype=0 and d.role_type=0 and d.resourcetype=5 and a.userid=?");
            rs = (new JdbcBaseDao()).getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_CI")).queryForList(sql1.toString(), new Object[]{loginName});
        }

        Iterator i$2 = rs.iterator();

        while(true) {
            Map m;
            do {
                do {
                    if(!i$2.hasNext()) {
                        return cityList;
                    }

                    m = (Map)i$2.next();
                } while(null == m.get("realCityId"));
            } while("null".equals(m.get("realCityId").toString()));

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
            sql1.append("select county_code,county_name from bt_area_all where area_id=?");
            List rsCounty = (new JdbcBaseDao()).getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_CI")).queryForList(sql1.toString(), new Object[]{Integer.valueOf(Integer.parseInt(m.get("realCityId").toString()))});
            Iterator rsCounty_style2 = rsCounty.iterator();

            while(rsCounty_style2.hasNext()) {
                Map rsDept = (Map)rsCounty_style2.next();
                if(null != rsDept.get("county_code") && !"null".equals(rsDept.get("county_code").toString())) {
                    cityUser = new CityUser();
                    cityUser.setCityId(rsDept.get("county_code").toString());
                    cityUser.setParentId(m.get("realCityId").toString());
                    cityUser.setDmCityId(m.get("realCityId").toString());
                    cityUser.setDmDeptId("-1");
                    cityUser.setDmCountyId(rsDept.get("county_code").toString());
                    cityUser.setCityName(rsDept.get("county_name").toString());
                    cityUser.setSortNum(0);
                    cityUser.setDmTypeId((String)null);
                    cityUser.setDmTypeCode((String)null);
                    cityList.add(cityUser);
                }
            }

            sql1.append("select id,name from bureau_tree_view where area_id=?");
            List rsCounty_style21 = (new JdbcBaseDao()).getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_CI")).queryForList(sql1.toString(), new Object[]{Integer.valueOf(Integer.parseInt(m.get("realCityId").toString()))});
            Iterator rsDept1 = rsCounty_style21.iterator();

            while(rsDept1.hasNext()) {
                Map i$1 = (Map)rsDept1.next();
                if(null != i$1.get("id") && !"null".equals(i$1.get("id").toString())) {
                    cityUser = new CityUser();
                    cityUser.setCityId(i$1.get("id").toString());
                    cityUser.setParentId(m.get("realCityId").toString());
                    cityUser.setDmCityId(m.get("realCityId").toString());
                    cityUser.setDmDeptId("-1");
                    cityUser.setDmCountyId(i$1.get("id").toString());
                    cityUser.setCityName(i$1.get("name").toString());
                    cityUser.setSortNum(0);
                    cityUser.setDmTypeId((String)null);
                    cityUser.setDmTypeCode((String)null);
                    cityList.add(cityUser);
                }
            }

            sql1.append("select id,name,pid from bureau_tree_view where area_id=?");
            List rsDept2 = (new JdbcBaseDao()).getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_CI")).queryForList(sql1.toString(), new Object[]{Integer.valueOf(Integer.parseInt(m.get("realCityId").toString()))});
            Iterator i$3 = rsDept2.iterator();

            while(i$3.hasNext()) {
                Map dept = (Map)i$3.next();
                if(null != dept.get("id") && !"null".equals(dept.get("id").toString())) {
                    cityUser = new CityUser();
                    cityUser.setCityId(dept.get("id").toString());
                    cityUser.setParentId(m.get("realCityId").toString());
                    cityUser.setDmCityId(m.get("realCityId").toString());
                    cityUser.setDmDeptId(dept.get("id").toString());
                    cityUser.setDmCountyId(dept.get("pid").toString());
                    cityUser.setCityName(dept.get("name").toString());
                    cityUser.setSortNum(0);
                    cityUser.setDmTypeId((String)null);
                    cityUser.setDmTypeCode((String)null);
                    cityList.add(cityUser);
                }
            }
        }
    }
}
