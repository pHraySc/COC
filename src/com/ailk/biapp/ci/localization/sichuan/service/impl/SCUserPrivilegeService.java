// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SCUserPrivilegeService.java

package com.ailk.biapp.ci.localization.sichuan.service.impl;

import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.localization.sichuan.model.*;
import com.asiainfo.biframe.exception.ServiceException;
import com.asiainfo.biframe.privilege.*;
import com.asiainfo.biframe.utils.config.Configure;
import java.util.*;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SCUserPrivilegeService extends JdbcBaseDao
	implements IUserPrivilegeService
{

	public SCUserPrivilegeService()
	{
	}

	public List getCityByUser(String loginNo)
		throws ServiceException
	{
		List cityList = new ArrayList();
		String sqlCounty = "select login_no,user_name,group_id,password,city_id,dept_id from power_user_info where login_no=?";
		List rs = null;
		rs = getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_Local")).queryForList(sqlCounty, new Object[] {
			loginNo
		});
		String group_id = "";
		for (Iterator i$ = rs.iterator(); i$.hasNext();)
		{
			Map m = (Map)i$.next();
			group_id = m.get("group_id").toString();
		}

		StringBuffer sql = new StringBuffer();
		rs = null;
		if ("1".equals(group_id))
		{
			sql.append("select -1 as city_id,'四川省' as city_name,'@' as parent_id,-1 as dept_id,-1 as county_id  from power_user_info where login_no=?");
			rs = getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_Local")).queryForList(sql.toString(), new Object[] {
				loginNo
			});
		} else
		{
			sql.append("select a.city_id,b.group_name as city_name,-1 as parent_id,-1 as dept_id,-1 as county_id from power_user_info a ").append("left join (select * from power_areainfo where lvl_id=2) b on a.city_id=b.group_id ").append("where a.login_no=?");
			rs = getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_Local")).queryForList(sql.toString(), new Object[] {
				loginNo
			});
		}
		CityUser cityUser;
		for (Iterator i$ = rs.iterator(); i$.hasNext(); cityList.add(cityUser))
		{
			Map m = (Map)i$.next();
			cityUser = new CityUser();
			cityUser.setCityId(m.get("city_id").toString());
			cityUser.setParentId("@".equals(m.get("parent_id")) ? null : m.get("parent_id").toString());
			cityUser.setDmCityId(m.get("city_id").toString());
			cityUser.setDmDeptId(m.get("dept_id").toString());
			cityUser.setDmCountyId(m.get("county_id").toString());
			cityUser.setCityName(m.get("city_name").toString());
			cityUser.setSortNum(0);
			cityUser.setDmTypeId(null);
			cityUser.setDmTypeCode(null);
		}

		return cityList;
	}

	public List getSubCitysById(String parentCityId)
		throws ServiceException
	{
		List subcityList = new ArrayList();
		List rs = null;
		if (parentCityId.equals("-1"))
		{
			String sql = "select distinct b.group_id as city_id,b.group_name as city_name,-1 as parent_id,-1 as dept_id,-1 as county_id from power_areainfo  b where  b.lvl_id=2  order by 1";
			rs = getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_Local")).queryForList(sql, new Object[0]);
			CityUser cityUser;
			for (Iterator i$ = rs.iterator(); i$.hasNext(); subcityList.add(cityUser))
			{
				Map m = (Map)i$.next();
				cityUser = new CityUser();
				cityUser.setCityId(m.get("city_id").toString());
				cityUser.setParentId(m.get("parent_id").toString());
				cityUser.setDmCityId(m.get("city_id").toString());
				cityUser.setDmDeptId(m.get("dept_id").toString());
				cityUser.setDmCountyId(m.get("county_id").toString());
				cityUser.setCityName(m.get("city_name").toString());
				cityUser.setSortNum(0);
				cityUser.setDmTypeId(null);
				cityUser.setDmTypeCode(null);
			}

		}
		return subcityList;
	}

	public ICity getCityByCityID(String cityId)
		throws ServiceException
	{
		CityUser cityUser = null;
		List rs = null;
		String sql = " ";
		if ("@".equals(cityId) || "-1".equals(cityId) || "*".equals(cityId))
		{
			sql = "select -1 as city_id,'@' as parent_id,'四川省' as city_name from power_areainfo where group_id= '1'";
			rs = getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_Local")).queryForList(sql, new Object[0]);
		} else
		{
			sql = "select group_id as city_id,-1 as parent_id,group_name as city_name from power_areainfo where lvl_id=2 and group_id=?";
			rs = getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_Local")).queryForList(sql, new Object[] {
				cityId
			});
		}
		if (rs.size() > 0)
		{
			Map m = (Map)rs.get(0);
			cityUser = new CityUser();
			cityUser.setCityId(m.get("city_id").toString());
			cityUser.setParentId(!"@".equals(m.get("parent_id")) && !"*".equals(m.get("parent_id")) ? m.get("parent_id").toString() : null);
			cityUser.setDmCityId(m.get("city_id").toString());
			cityUser.setDmDeptId("-1");
			cityUser.setDmCountyId("-1");
			cityUser.setCityName(m.get("city_name").toString());
			cityUser.setSortNum(0);
			cityUser.setDmTypeId(null);
			cityUser.setDmTypeCode(null);
		}
		return cityUser;
	}

	public IUser getUser(String loginNo)
		throws ServiceException
	{
		User user = null;
		List rs = null;
		String sql = "select login_no,user_name,group_id,password,email,mobile_phone,city_id,dept_id from power_user_info where login_no=?";
		rs = getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_Local")).queryForList(sql, new Object[] {
			loginNo
		});
		if (rs.size() > 0)
		{
			Map m = (Map)rs.get(0);
			user = new User();
			user.setUserid(m.get("login_no").toString());
			user.setUsername(m.get("user_name").toString());
			user.setCityid(m.get("city_id").toString());
			user.setDepartmentid(Integer.parseInt(m.get("dept_id").toString()));
			user.setGroupId(m.get("group_id").toString());
			user.setEmail(m.get("email").toString());
			user.setMobilePhone(m.get("mobile_phone").toString());
			user.setLoginId(m.get("login_no").toString());
			user.setDomainType(null);
		}
		return user;
	}

	public boolean isAdminUser(String loginNo)
		throws ServiceException
	{
		StringBuffer sqlBuff = (new StringBuffer()).append("select count(1)").append(" from  (select login_no from POWER_USER_INFO where login_no= ?) pu,").append("(select login_no from POWER_ROLE").append(" where model_id = ").append(Configure.getInstance().getProperty("MODEL_ID")).append(" ) pr").append(" where  pu.login_no = pr.login_no");
		int count = 0;
		count = getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_Local")).queryForInt(sqlBuff.toString(), new Object[] {
			loginNo
		});
		return count > 0;
	}

	public IUserCompany getUserDept(String loginNo)
		throws ServiceException
	{
		UserCompany company = null;
		List rs = null;
		String sql = "select a.dept_id,b.department_name from POWER_USER_INFO a left join POWER_DEPARTMENT b on a.dept_id=b.dept_id where a.login_no=?";
		rs = getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_Local")).queryForList(sql, new Object[] {
			loginNo
		});
		if (rs.size() > 0)
		{
			Map m = (Map)rs.get(0);
			company = new UserCompany();
			company.setDeptid(Integer.valueOf(Integer.parseInt(m.get("dept_id").toString())));
			company.setTitle(m.get("department_name").toString());
			company.setServiceCode(null);
			company.setParentid(Integer.valueOf(0));
			company.setStatus(null);
		}
		return company;
	}

	public boolean addPublishMenu(int arg0, String arg1, String arg2, String s, String s1, String s2, String as[])
	{
		return false;
	}

	public String decryption(String arg0)
	{
		return null;
	}

	public boolean deletePublishMenu(String arg0, String arg1)
	{
		return false;
	}

	public void doAffirmApply(String s, String s1)
		throws ServiceException
	{
	}

	public String doCreateApply(String arg0, String arg1, String arg2, String s, List list)
		throws ServiceException
	{
		return null;
	}

	public String encryption(String arg0)
	{
		return null;
	}

	public Collection getAllApplications()
		throws ServiceException
	{
		return null;
	}

	public List getAllCity()
		throws ServiceException
	{
		return null;
	}

	public String getAllCityID(String arg0, boolean arg1)
		throws ServiceException
	{
		return null;
	}

	public List getAllMenuItem(String arg0)
		throws ServiceException
	{
		return null;
	}

	public List getAllRoles(String arg0, int arg1, int arg2)
		throws ServiceException
	{
		return null;
	}

	public List getAllSubGroupsByUserId(String arg0)
		throws ServiceException
	{
		return null;
	}

	public List getAllSubUsersByUserId(String arg0)
		throws ServiceException
	{
		return null;
	}

	public List getAllUserCompany()
		throws ServiceException
	{
		return null;
	}

	public List getAllUserGroup()
		throws ServiceException
	{
		return null;
	}

	public List getAllUserRole()
		throws ServiceException
	{
		return null;
	}

	public List getAllUsers()
		throws ServiceException
	{
		return null;
	}

	public IUserRightApply getApplyById(String arg0)
		throws ServiceException
	{
		return null;
	}

	public ICity getCityByDmCity(String arg0, String arg1)
		throws ServiceException
	{
		return null;
	}

	public String getDmCity(String arg0, String arg1, String arg2, String s)
		throws ServiceException
	{
		return null;
	}

	public IUserGroup getGroupObject(String arg0)
		throws ServiceException
	{
		return null;
	}

	public List getGroupRoleMapList()
		throws ServiceException
	{
		return null;
	}

	public List getMapUsers(String arg0, int arg1, String arg2)
		throws ServiceException
	{
		return null;
	}

	public List getMenuItemList(String arg0, Map arg1)
		throws ServiceException
	{
		return null;
	}

	public IMenuItem getMeunItemById(Integer arg0)
		throws ServiceException
	{
		return null;
	}

	public List getPreferDescListByPreferType(String arg0, String arg1)
		throws ServiceException
	{
		return null;
	}

	public List getPrivacyRights(String arg0)
	{
		return null;
	}

	public String getResourceName(int arg0, int arg1, String arg2)
		throws ServiceException
	{
		return null;
	}

	public ISysResourceType getResourceTypeByRoleRes(int arg0, int arg1)
		throws ServiceException
	{
		return null;
	}

	public List getResourceTypeByRoleType(int arg0)
		throws ServiceException
	{
		return null;
	}

	public List getRight(String arg0, int arg1)
		throws ServiceException
	{
		return null;
	}

	public List getRight(String arg0, int arg1, int arg2)
		throws ServiceException
	{
		return null;
	}

	public List getRightByOperation(String arg0, int arg1, int arg2, String s)
		throws ServiceException
	{
		return null;
	}

	public List getRoleType()
		throws ServiceException
	{
		return null;
	}

	public String getShowDmCity(String arg0, String arg1, String arg2, String s)
		throws ServiceException
	{
		return null;
	}

	public List getShowTreeRight(String arg0, int arg1)
	{
		return null;
	}

	public List getSubCompanyById(String arg0)
		throws ServiceException
	{
		return null;
	}

	public List getSubMenuItemById(Integer arg0, String arg1, boolean arg2)
	{
		return null;
	}

	public Collection getTempRightsByApplyId(String arg0)
		throws ServiceException
	{
		return null;
	}

	public long getUserAmount()
	{
		return 0L;
	}

	public IUserCompany getUserCompanyById(String arg0)
		throws ServiceException
	{
		return null;
	}

	public List getUserCompanyByName(String arg0)
		throws ServiceException
	{
		return null;
	}

	public String getUserCurrentCity(String arg0)
		throws ServiceException
	{
		return null;
	}

	public String getUserDmCity(String arg0, String arg1)
		throws ServiceException
	{
		return null;
	}

	public IUserDuty getUserDutyById(String arg0)
		throws ServiceException
	{
		return null;
	}

	public IUserDuty getUserDutyByUser(IUser arg0)
		throws ServiceException
	{
		return null;
	}

	public IUserExt getUserExt(String arg0)
		throws ServiceException
	{
		return null;
	}

	public List getUserExtInfo(String arg0)
		throws Exception
	{
		return null;
	}

	public IUserGroup getUserGroupById(String arg0)
		throws ServiceException
	{
		return null;
	}

	public List getUserGroupByUserId(List arg0)
		throws ServiceException
	{
		return null;
	}

	public List getUserList(List arg0)
		throws ServiceException
	{
		return null;
	}

	public List getUserPreferList(String arg0)
		throws ServiceException
	{
		return null;
	}

	public List getUserRole(String arg0, String arg1)
		throws ServiceException
	{
		return null;
	}

	public IUserRole getUserRoleById(String arg0)
		throws ServiceException
	{
		return null;
	}

	public String getUserSensitiveLevel(String arg0)
		throws ServiceException
	{
		return null;
	}

	public List getUsersByGroupId(String arg0)
		throws ServiceException
	{
		return null;
	}

	public List getUsersByPage(String arg0, String arg1)
	{
		return null;
	}

	public List getUsersOfDepartment(int arg0)
		throws ServiceException
	{
		return null;
	}

	public List getValidChildGroups(String arg0)
		throws ServiceException
	{
		return null;
	}

	public boolean haveOperRight(String arg0, String arg1, String arg2)
	{
		return false;
	}

	public boolean haveOperateRight(String arg0, int arg1, String arg2, String s)
		throws ServiceException
	{
		return false;
	}

	public boolean haveRightByUserId(String arg0, IUserRight arg1)
		throws ServiceException
	{
		return false;
	}

	public boolean haveRightByUserId(String arg0, int arg1, String arg2)
		throws ServiceException
	{
		return false;
	}

	public boolean haveRightByUserId(String arg0, int arg1, int arg2, String s)
		throws ServiceException
	{
		return false;
	}

	public boolean isPwdChangedByOthers(String arg0)
	{
		return false;
	}

	public boolean isPwdNeedChange(String arg0)
	{
		return false;
	}

	public boolean isUserLegal(String arg0, String arg1)
	{
		return false;
	}

	public boolean modPublishMenu(int arg0, String arg1, String arg2, String s, String s1, String s2, String as[])
	{
		return false;
	}

	public String queryFolderList(int arg0)
	{
		return null;
	}

	public void saveRight(String s, int i, int j, String s1, int k, String s2)
		throws ServiceException
	{
	}

	public void saveUserPrefer(String s, String s1, String s2)
		throws ServiceException
	{
	}
}
