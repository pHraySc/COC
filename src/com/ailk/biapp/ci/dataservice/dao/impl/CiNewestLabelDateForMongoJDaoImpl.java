package com.ailk.biapp.ci.dataservice.dao.impl;

import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.dataservice.dao.ICiNewestLabelDateForMongoJDao;
import com.ailk.biapp.ci.entity.CiNewestLabelDate;
import com.asiainfo.biframe.utils.config.Configure;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CiNewestLabelDateForMongoJDaoImpl
	implements ICiNewestLabelDateForMongoJDao
{

	private final Logger log = Logger.getLogger(CiNewestLabelDateForMongoJDaoImpl.class);
	private JdbcBaseDao jdbcBaseDao;
	private SimpleJdbcTemplate simpleJdbcTemplate;

	public CiNewestLabelDateForMongoJDaoImpl()
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

	public List selectNewestLabelDate()
		throws Exception
	{
		init();
		String sql = "select DAY_NEWEST_DATE, MONTH_NEWEST_DATE from CI_NEWEST_LABEL_DATE ";
		List newDateTemp = simpleJdbcTemplate.query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiNewestLabelDate.class), new Object[0]);
		return newDateTemp;
	}
}
