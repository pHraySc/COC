package com.ailk.biapp.ci.dataservice.dao.impl;

import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.dataservice.dao.ILabelsAndIndexes2MongoJDao;
import com.ailk.biapp.ci.util.DataBaseAdapter;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LabelsAndIndexes2MongoJDaoImpl
	implements ILabelsAndIndexes2MongoJDao
{

	private final Logger log = Logger.getLogger(LabelsAndIndexes2MongoJDaoImpl.class);
	private JdbcBaseDao jdbcBaseDao;
	private SimpleJdbcTemplate simpleJdbcTemplate;
	public static final String DBMS_DB2 = "DB2";

	public LabelsAndIndexes2MongoJDaoImpl()
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

	public int selectLabelsAndIndexesCount(String dataDate)
		throws Exception
	{
		init();
		String sql = (new StringBuilder()).append("select count(1) from CI_PERSON_LABEL_AND_INDEX_").append(dataDate).toString();
		log.debug((new StringBuilder()).append("count sql is : ").append(sql).toString());
		int count = simpleJdbcTemplate.queryForInt(sql, new Object[0]);
		return count;
	}

	public List selectLabelsAndIndexes(int currPage, int pageSize, String dataDate)
		throws Exception
	{
		init();
		String tableName = (new StringBuilder()).append("CI_PERSON_LABEL_AND_INDEX_").append(dataDate).toString();
		StringBuffer sql = new StringBuffer("select * from ");
		String dbType = Configure.getInstance().getProperty("CI", "CI_BACK_DBTYPE");
		if (StringUtil.isEmpty(dbType))
			dbType = "DB2";
		DataBaseAdapter adapter = new DataBaseAdapter(dbType);
		String sqlPage = adapter.getPagedSql(sql.toString(), currPage, pageSize);
		log.debug((new StringBuilder()).append("select sql is : ").append(sqlPage).toString());
		List list = simpleJdbcTemplate.queryForList(sqlPage, new Object[0]);
		return list;
	}

	public void execBigSelectSql(String sql, RowCallbackHandler rowCallbackHandler)
		throws Exception
	{
		init();
		String jndi = "java:comp/env/jdbc/CI";
		try
		{
			jndi = Configure.getInstance().getProperty("CI", "JNDI_CI");
		}
		catch (Exception e)
		{
			log.error("get jndi error", e);
		}
		JdbcTemplate jt = jdbcBaseDao.getJdbcTemplate(jndi);
		jt.query(sql, rowCallbackHandler);
	}
}
