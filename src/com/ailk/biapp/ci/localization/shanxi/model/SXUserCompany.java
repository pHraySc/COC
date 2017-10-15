package com.ailk.biapp.ci.localization.shanxi.model;

import com.ailk.biapp.ci.localization.shanxi.dao.SqlcaSX;
import com.asiainfo.biframe.privilege.IUserCompany;

public class SXUserCompany
	implements IUserCompany
{

	private SqlcaSX sqlCaSX;
	private String userId;

	public SqlcaSX getSqlCaSX()
	{
		return sqlCaSX;
	}

	public void setSqlCaSX(SqlcaSX sqlCaSX)
	{
		this.sqlCaSX = sqlCaSX;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public SXUserCompany(String userId)
	{
		try
		{
			this.userId = userId;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Integer getDeptid()
	{
		return new Integer("0");
	}

	public Integer getParentid()
	{
		return null;
	}

	public String getServiceCode()
	{
		return null;
	}

	public String getStatus()
	{
		return null;
	}

	public String getTitle()
	{
		return "ÉÂÎ÷ÒÆ¶¯";
	}
}
