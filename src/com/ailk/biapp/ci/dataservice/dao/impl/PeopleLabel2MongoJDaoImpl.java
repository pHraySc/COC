package com.ailk.biapp.ci.dataservice.dao.impl;

import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.dataservice.dao.IPeopleLabel2MongoJDao;
import com.ailk.biapp.ci.model.CiMdaSysTableColumn;
import com.ailk.biapp.ci.model.PeopleLabelInfo;
import com.asiainfo.biframe.utils.config.Configure;
import java.util.*;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PeopleLabel2MongoJDaoImpl
	implements IPeopleLabel2MongoJDao
{

	private final Logger log = Logger.getLogger(PeopleLabel2MongoJDaoImpl.class);
	private JdbcBaseDao jdbcBaseDao;
	private SimpleJdbcTemplate simpleJdbcTemplate;

	public PeopleLabel2MongoJDaoImpl()
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

	public List selectPeopleLabelList()
		throws Exception
	{
		init();
		String sql = "SELECT PL.LABEL_ID,L.LABEL_NAME,PL.TYPE_ID,PL.IS_IMPORTENT FROM CI_PEOPLE_ORIENTED_LABEL_INFO PL, CI_MDA_SYS_TABLE_COLUMN C, CI_LABEL_EXT_INFO E, CI_LABEL_INFO L WHERE PL.LABEL_ID = C.COLUMN_NAME AND C.COLUMN_ID = E.COLUMN_ID AND E.LABEL_ID = L.LABEL_ID";
		log.debug((new StringBuilder()).append("SQL is : ").append(sql).toString());
		List list = simpleJdbcTemplate.query(sql, ParameterizedBeanPropertyRowMapper.newInstance(PeopleLabelInfo.class), new Object[0]);
		return list;
	}

	public boolean getUnionTableOfLebelsAndIndex(String baseMonth, String baseDate)
	{
		boolean flag = true;
		init();
		String sql = "SELECT B.TABLE_NAME, C.COLUMN_NAME,L.update_cycle FROM CI_PEOPLE_ORIENTED_LABEL_INFO PL, CI_MDA_SYS_TABLE_COLUMN C, CI_MDA_SYS_TABLE B, CI_LABEL_EXT_INFO E, CI_LABEL_INFO L WHERE PL.LABEL_ID = C.COLUMN_NAME AND C.COLUMN_ID = E.COLUMN_ID AND E.LABEL_ID = L.LABEL_ID AND C.TABLE_ID = B.TABLE_ID ORDER BY B.TABLE_NAME, C.COLUMN_NAME";
		log.debug((new StringBuilder()).append("sql:").append(sql).toString());
		List columns = simpleJdbcTemplate.query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiMdaSysTableColumn.class), new Object[0]);
		StringBuffer selectLabelSql = new StringBuffer(" select ");
		StringBuffer fromLabelSql = new StringBuffer(" from ");
		String schema = "";
		String tableSpace = "";
		String indexSpace = "";
		String primyKey = "phone_no";
		try
		{
			schema = Configure.getInstance().getProperty("CI", "CI_SCHEMA");
			tableSpace = Configure.getInstance().getProperty("CI", "CI_TABLESPACE");
			indexSpace = Configure.getInstance().getProperty("CI", "CI_INDEX_TABLESPACE");
			primyKey = Configure.getInstance().getProperty("CI", "RELATED_COLUMN");
		}
		catch (Exception e)
		{
			log.error("get properties error", e);
		}
		String tempLeableTableName = (new StringBuilder()).append(schema).append(".TEMP_CI_ALL_LEABLE_").append(baseMonth).toString();
		String tempIndexTableName = (new StringBuilder()).append(schema).append(".TEMP_CI_ALL_INDEX_").append(baseMonth).toString();
		String leableAndIndexTableName = (new StringBuilder()).append(schema).append(".CI_PERSON_LABEL_AND_INDEX_").append(baseMonth).toString();
		StringBuffer createLeableSql = (new StringBuffer(" create table ")).append(tempLeableTableName).append("(").append(primyKey).append(" varchar(100),");
		StringBuffer createLabelAndIndexSql = (new StringBuffer(" create table ")).append(leableAndIndexTableName).append("(").append(primyKey).append(" varchar(100),");
		int i = 0;
		String firstTable = "";
		List tableNames = new ArrayList();
		Map tableColumns = new HashMap();
		for (Iterator i$ = columns.iterator(); i$.hasNext(); i++)
		{
			CiMdaSysTableColumn col = (CiMdaSysTableColumn)i$.next();
			String dataDate = baseMonth;
			if (col.getUpdateCycle().intValue() == 1)
				dataDate = baseDate;
			if (tableColumns.containsKey((new StringBuilder()).append(col.getTableName()).append("_").append(dataDate).toString()))
			{
				((List)tableColumns.get((new StringBuilder()).append(col.getTableName()).append("_").append(dataDate).toString())).add(col.getColumnName());
			} else
			{
				List cols = new ArrayList();
				cols.add(col.getColumnName());
				tableColumns.put((new StringBuilder()).append(col.getTableName()).append("_").append(dataDate).toString(), cols);
			}
			if (i == 0)
			{
				selectLabelSql.append(col.getTableName()).append('.').append(primyKey).append(",");
				selectLabelSql.append(col.getTableName()).append('.').append(col.getColumnName());
				fromLabelSql.append(getSmallTableName((new StringBuilder()).append(col.getTableName()).append("_").append(dataDate).toString(), schema)).append(' ').append(col.getTableName());
				firstTable = col.getTableName();
				createLeableSql.append(col.getColumnName()).append(" smallint");
				createLabelAndIndexSql.append(col.getColumnName()).append(" smallint");
				tableNames.add(col.getTableName());
				continue;
			}
			createLeableSql.append(",").append(col.getColumnName()).append(" smallint");
			createLabelAndIndexSql.append(",").append(col.getColumnName()).append(" smallint");
			selectLabelSql.append(",").append(col.getTableName()).append('.').append(col.getColumnName());
			if (!tableNames.contains(col.getTableName()))
			{
				fromLabelSql.append(" left join ").append(getSmallTableName((new StringBuilder()).append(col.getTableName()).append("_").append(dataDate).toString(), schema)).append(" ").append(col.getTableName()).append(" on ").append(firstTable).append('.').append(primyKey).append("=").append(col.getTableName()).append('.').append(primyKey);
				tableNames.add(col.getTableName());
			}
		}

		genSmallTable(schema, tableSpace, indexSpace, primyKey, tableColumns);
		createLeableSql.append(")  NOT LOGGED INITIALLY DATA CAPTURE NONE IN ").append(tableSpace).append(" INDEX IN ").append(indexSpace).append("  PARTITIONING KEY ( ").append(primyKey).append(" ) USING HASHING");
		createLabelAndIndexSql.append(",I001 VARCHAR(200), I002 VARCHAR(200), I003 VARCHAR(200), I004 DECIMAL(16,3), I005 VARCHAR(200), I006 VARCHAR(200), I007 VARCHAR(200), I008 VARCHAR(200), I009 VARCHAR(200) ,I010 VARCHAR(50),I011 VARCHAR(50),I012 VARCHAR(50),I013 VARCHAR(50),I014 VARCHAR(50),I015 VARCHAR(50)").append(")  NOT LOGGED INITIALLY DATA CAPTURE NONE IN ").append(tableSpace).append(" INDEX IN ").append(indexSpace).append("  PARTITIONING KEY ( ").append(primyKey).append(" ) USING HASHING");
		selectLabelSql.append(fromLabelSql);
		StringBuffer insertLeableSql = (new StringBuffer("insert into ")).append(tempLeableTableName).append(selectLabelSql);
		StringBuffer createIndexSql = new StringBuffer();
		createIndexSql.append(" CREATE TABLE ").append(tempIndexTableName).append('(').append(primyKey).append(" varchar(100),I001 VARCHAR(200), I002 VARCHAR(200), I003 VARCHAR(200), I004 DECIMAL(16,3), I005 VARCHAR(200), I006 VARCHAR(200), I007 VARCHAR(200), I008 VARCHAR(200), I009 VARCHAR(200),I010 VARCHAR(50),I011 VARCHAR(50),I012 VARCHAR(50),I013 VARCHAR(50),I014 VARCHAR(50),I015 VARCHAR(50) ");
		createIndexSql.append(")  NOT LOGGED INITIALLY DATA CAPTURE NONE IN ").append(tableSpace).append(" INDEX IN ").append(indexSpace).append("  PARTITIONING KEY ( ").append(primyKey).append(" ) USING HASHING");
		StringBuffer insertIndexSql = (new StringBuffer("insert into ")).append(tempIndexTableName).append(" SELECT ").append(primyKey).append(", TRIM(REPLACE(strip(REPLACE(CHAR(DEC(arpu,10,2)), '0.', '#'),B,'0'),'#','0.'))||','||TRIM(REPLACE( strip(REPLACE(CHAR( DEC(last1_month_arpu,10,2)), '0.', '#'),B,'0'),'#','0.'))||','||TRIM(REPLACE( strip(REPLACE(CHAR(DEC( last2_month_arpu,10,2)), '0.', '#'),B,'0'),'#','0.'))||','||TRIM(REPLACE( strip(REPLACE(CHAR(DEC( last3_month_arpu,10,2)), '0.', '#'),B,'0'),'#','0.'))||','||TRIM(REPLACE( strip(REPLACE(CHAR(DEC( last4_month_arpu,10,2)), '0.', '#'),B,'0'),'#','0.'))||','||TRIM(REPLACE( strip(REPLACE(CHAR(DEC( last5_month_arpu,10,2)), '0.', '#'),B,'0'),'#','0.')) I001, TRIM(REPLACE( strip(REPLACE(CHAR(DEC(newbusi_fee,10,2)), '0.', '#'),B,'0'),'#','0.'))||','||TRIM(REPLACE(strip( REPLACE(CHAR(DEC(last1_newbusi_fee,10,2)), '0.', '#'),B,'0'),'#','0.'))||','||TRIM(REPLACE(strip( REPLACE(CHAR( DEC(last2_newbusi_fee,10,2)), '0.', '#'),B,'0'),'#','0.')) I002, trim(CHAR( per_sms_count))||','||trim(CHAR(last1_per_sms_count))||','||trim(CHAR( last2_per_sms_count))||','|| trim(CHAR(last3_per_sms_count))||','||trim(CHAR(last4_per_sms_count ))||','||trim(CHAR( last5_per_sms_count))||';平均'||trim(CHAR((per_sms_count+last1_per_sms_count+ last2_per_sms_count+ last3_per_sms_count+last4_per_sms_count+last5_per_sms_count)/6)) I003, arpu+last1_month_arpu+ last2_month_arpu+last3_month_arpu+last4_month_arpu+last5_month_arpu+ last6_month_arpu+ last7_month_arpu+last8_month_arpu+last9_month_arpu+last10_month_arpu+ last11_month_arpu I004, trim( CHAR(exchangable_balance)) I005, trim(CHAR(total_flow))||','||trim(CHAR(last1_total_flow))||','|| trim(CHAR(last2_total_flow))|| ','||trim(CHAR(last3_total_flow))||','||trim(CHAR(last4_total_flow)) ||','||trim(CHAR( last5_total_flow))||';平均'||trim(CHAR((total_flow+last1_total_flow+ last2_total_flow+ last3_total_flow+last4_total_flow+last5_total_flow)/6)) I006, trim(CHAR( wlan_duration_m))||','||trim(CHAR(last1_wlan_duration_m))||','||trim(CHAR( last2_wlan_duration_m)) ||','||trim(CHAR(last3_wlan_duration_m))||','||trim(CHAR( last4_wlan_duration_m))||','||trim(CHAR( last5_wlan_duration_m)) I007, trim(CHAR(hallcount))||','||trim(CHAR(last1_hallcount))||','||trim( CHAR(last2_hallcount)) I008, CASE WHEN innet_month<12 THEN '1年以内' ELSE trim(CHAR(innet_month/12)) || '年以上' END I009,termbrand_name AS I010, termtype_name AS I011, os_name AS I012, '' AS I013, CASE WHEN is_inteligent = 1 THEN '智能机' ELSE '非智能机' END AS I014, termscreen_name AS I015 FROM DW_COC_PRODUCT_CRM_").append(baseMonth);
		StringBuffer insertLeableAndIndexSql = (new StringBuffer("insert into ")).append(leableAndIndexTableName).append(" select a.*,b.I001, b.I002, b.I003, b.I004, b.I005, b.I006, b.I007, b.I008, b.I009,b.I010,b.I011,b.I012,b.I013,b.I014,b.I015 from ").append(tempLeableTableName).append(" a left join ").append(tempIndexTableName).append(" b on a.").append(primyKey).append(" = b.").append(primyKey);
		flag = createAndInsert(tempLeableTableName, createLeableSql.toString(), insertLeableSql.toString());
		if (!flag)
			return flag;
		flag = createAndInsert(tempIndexTableName, createIndexSql.toString(), insertIndexSql.toString());
		if (!flag)
		{
			return flag;
		} else
		{
			createAndInsert(leableAndIndexTableName, createLabelAndIndexSql.toString(), insertLeableAndIndexSql.toString());
			dorpSmallTable(tableColumns, schema);
			return flag;
		}
	}

	private void dorpSmallTable(Map tableColumns, String schema)
	{
		String smallName;
		for (Iterator i$ = tableColumns.keySet().iterator(); i$.hasNext(); dropTable(smallName))
		{
			String tableName = (String)i$.next();
			smallName = getSmallTableName(tableName, schema);
		}

	}

	private void genSmallTable(String schema, String tableSpace, String indexSpace, String primyKey, Map tableColumns)
	{
		String smallName;
		StringBuffer createSmallTableSql;
		StringBuffer insertSmallTableSql;
		for (Iterator i$ = tableColumns.keySet().iterator(); i$.hasNext(); createAndInsert(smallName, createSmallTableSql.toString(), insertSmallTableSql.toString()))
		{
			String tableName = (String)i$.next();
			smallName = getSmallTableName(tableName, schema);
			createSmallTableSql = (new StringBuffer("create table ")).append(smallName).append('(').append(primyKey).append(" varchar(100)");
			insertSmallTableSql = (new StringBuffer("insert into ")).append(smallName).append(" select ").append(primyKey);
			String col;
			for (Iterator i = ((List)tableColumns.get(tableName)).iterator(); i.hasNext(); insertSmallTableSql.append(",").append(col))
			{
				col = (String)i.next();
				createSmallTableSql.append(",").append(col).append(" smallint");
			}

			createSmallTableSql.append(")  NOT LOGGED INITIALLY DATA CAPTURE NONE IN ").append(tableSpace).append(" INDEX IN ").append(indexSpace).append("  PARTITIONING KEY ( ").append(primyKey).append(" ) USING HASHING");
			insertSmallTableSql.append(" from ").append(tableName);
		}

	}

	private boolean createAndInsert(String tableName, String createSql, String insertSql)
	{
		boolean flag = true;
		long start = System.currentTimeMillis();
		dropTable(tableName);
		String alterSql = (new StringBuilder()).append("ALTER TABLE ").append(tableName).append(" ACTIVATE NOT LOGGED INITIALLY").toString();
		try
		{
			log.debug((new StringBuilder()).append("createSql:").append(createSql).toString());
			simpleJdbcTemplate.getJdbcOperations().execute(createSql.toString());
		}
		catch (Exception e)
		{
			flag = false;
			log.error((new StringBuilder()).append("create table failed:").append(createSql).toString(), e);
		}
		if (!flag)
			return flag;
		try
		{
			log.debug((new StringBuilder()).append("alterSql:").append(alterSql).toString());
			simpleJdbcTemplate.getJdbcOperations().execute(alterSql);
		}
		catch (Exception e)
		{
			log.error((new StringBuilder()).append(tableName).append(" may not exists,").append(e.getMessage()).toString());
		}
		try
		{
			log.debug((new StringBuilder()).append("insertSql:").append(insertSql).toString());
			simpleJdbcTemplate.getJdbcOperations().execute(insertSql.toString());
		}
		catch (Exception e)
		{
			flag = false;
			log.error((new StringBuilder()).append("insert table failed:").append(insertSql).toString(), e);
		}
		log.info((new StringBuilder()).append(tableName).append(" , cost:").append(System.currentTimeMillis() - start).toString());
		return flag;
	}

	private void dropTable(String tableName)
	{
		String alterSql = (new StringBuilder()).append("ALTER TABLE ").append(tableName).append(" ACTIVATE NOT LOGGED INITIALLY").toString();
		try
		{
			log.debug((new StringBuilder()).append("alterSql:").append(alterSql).toString());
			simpleJdbcTemplate.getJdbcOperations().execute(alterSql);
		}
		catch (Exception e)
		{
			log.error((new StringBuilder()).append(tableName).append(" may not exists,").append(e.getMessage()).toString());
		}
		try
		{
			log.debug((new StringBuilder()).append("dropSql: drop table ").append(tableName).toString());
			simpleJdbcTemplate.getJdbcOperations().execute((new StringBuilder()).append(" drop table ").append(tableName).toString());
		}
		catch (Exception e)
		{
			log.error((new StringBuilder()).append("drop failed table my not exits").append(tableName).toString());
		}
		try
		{
			log.debug((new StringBuilder()).append("dropSql: drop view ").append(tableName).toString());
			simpleJdbcTemplate.getJdbcOperations().execute((new StringBuilder()).append(" drop view ").append(tableName).toString());
		}
		catch (Exception e)
		{
			log.error((new StringBuilder()).append("drop failed table my not exits").append(tableName).toString());
		}
	}

	private String getSmallTableName(String tableName, String schema)
	{
		return (new StringBuilder()).append(schema).append(".").append(tableName).append("_PART").toString();
	}
}
