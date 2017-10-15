// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Campaign2MongoJDaoImpl.java

package com.ailk.biapp.ci.dataservice.dao.impl;

import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.dataservice.dao.ICampaign2MongoJDao;
import com.ailk.biapp.ci.entity.CiPersonCampaigns;
import com.ailk.biapp.ci.util.DataBaseAdapter;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class Campaign2MongoJDaoImpl
	implements ICampaign2MongoJDao
{

	private final Logger log = Logger.getLogger(Campaign2MongoJDaoImpl.class);
	private JdbcBaseDao jdbcBaseDao;
	private SimpleJdbcTemplate simpleJdbcTemplate;
	public static final String DBMS_DB2 = "DB2";

	public Campaign2MongoJDaoImpl()
	{
		jdbcBaseDao = new JdbcBaseDao();
		simpleJdbcTemplate = null;
	}

	private void init()
	{
		if (simpleJdbcTemplate == null)
		{
			String jndi = "java:comp/env/jdbc/CI";
			try
			{
				jndi = Configure.getInstance().getProperty("CI", "JNDI_CI");
			}
			catch (Exception e)
			{
				log.error("get jndi error", e);
			}
			simpleJdbcTemplate = jdbcBaseDao.getSimpleJdbcTemplate(jndi);
		}
	}

	public int selectCiPersonCampaignsCount()
		throws Exception
	{
		init();
		String sql = "select count(1) from CI_PERSON_CAMPAIGNS";
		log.debug((new StringBuilder()).append("count sql is : ").append(sql).toString());
		int count = simpleJdbcTemplate.queryForInt(sql, new Object[0]);
		return count;
	}

	public List selectCiPersonCampaignsList(int currPage, int pageSize)
		throws Exception
	{
		init();
		String sql = "select * from CI_PERSON_CAMPAIGNS";
		String dbType = Configure.getInstance().getProperty("CI", "CI_BACK_DBTYPE");
		if (StringUtil.isEmpty(dbType))
			dbType = "DB2";
		DataBaseAdapter adapter = new DataBaseAdapter(dbType);
		String sqlPage = adapter.getPagedSql(sql, currPage, pageSize);
		log.debug((new StringBuilder()).append("select sql is : ").append(sqlPage).toString());
		List list = simpleJdbcTemplate.query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(CiPersonCampaigns.class), new Object[0]);
		return list;
	}
}
