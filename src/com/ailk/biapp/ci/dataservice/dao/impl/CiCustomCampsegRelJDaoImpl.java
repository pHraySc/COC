package com.ailk.biapp.ci.dataservice.dao.impl;

import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.dataservice.dao.ICiCustomCampsegRelJDao;
import com.ailk.biapp.ci.model.CiCustomCampsegRel;
import com.asiainfo.biframe.utils.config.Configure;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CiCustomCampsegRelJDaoImpl
	implements ICiCustomCampsegRelJDao
{

	private final Logger log = Logger.getLogger(CiCustomCampsegRelJDaoImpl.class);
	private JdbcBaseDao jdbcBaseDao;
	private SimpleJdbcTemplate simpleJdbcTemplate;

	public CiCustomCampsegRelJDaoImpl()
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

	public List selectCiCustomCampsegRelList()
		throws Exception
	{
		init();
		String sql = "SELECT * FROM CI_CUSTOM_CAMPSEG_REL";
		log.debug((new StringBuilder()).append("SQL is : ").append(sql).toString());
		List list = simpleJdbcTemplate.query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiCustomCampsegRel.class), new Object[0]);
		return list;
	}
}
