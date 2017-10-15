package com.ailk.biapp.ci.dataservice.dao.impl;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.dataservice.dao.ICiLabelsAndIndexesJDao;
import com.ailk.biapp.ci.model.CrmTypeModel;
import com.ailk.biapp.ci.model.LabelTelTableInfo;
import com.ailk.biapp.ci.util.MongoDB;
import com.asiainfo.biframe.utils.config.Configure;
import com.mongodb.*;
import java.util.*;
import org.apache.log4j.Logger;
import org.bson.BSONObject;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CiLabelsAndIndexesJDaoImpl extends JdbcBaseDao
	implements ICiLabelsAndIndexesJDao
{

	private Logger log;
	DB db;

	public CiLabelsAndIndexesJDaoImpl()
	{
		log = Logger.getLogger(CiLabelsAndIndexesJDaoImpl.class);
	}

	public String getLabelsAndIndexes(String mobile)
	{
		String result = "";
		result = getLabelsAndIndexesByMobile(mobile).toString();
		return result;
	}

	public BSONObject getLabelsAndIndexesByMobile(String mobile)
	{
		return MongoDB.getInstance().findOne("labelsAndIndexes", "mobile", mobile);
	}

	public List getIndexes(List sub)
	{
		db = MongoDB.getInstance().getDB();
		DBCollection collection = db.getCollection("indexes");
		List list = new ArrayList();
		BasicDBList dbList = new BasicDBList();
		dbList.addAll(sub);
		DBObject inObj = new BasicDBObject("$in", dbList);
		DBCursor cursor = collection.find(new BasicDBObject("indexId", inObj));
		DBObject dbObj = null;
		for (; cursor.hasNext(); list.add(dbObj))
			dbObj = cursor.next();

		return list;
	}

	public List getLabels(List sub)
	{
		db = MongoDB.getInstance().getDB();
		DBCollection collection = db.getCollection("labels");
		List list = new ArrayList();
		BasicDBList dbList = new BasicDBList();
		dbList.addAll(sub);
		DBObject inObj = new BasicDBObject("$in", dbList);
		DBCursor cursor = collection.find(new BasicDBObject("labelId", inObj));
		DBObject dbObj = null;
		for (; cursor.hasNext(); list.add(dbObj))
			dbObj = cursor.next();

		return list;
	}

	public List selectAllLabelInfo()
	{
		log.info("dao-->select label table info!");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.TABLE_NAME, C.COLUMN_NAME, L.UPDATE_CYCLE,LT.TYPE_DESC, LT.ICON, ");
		sql.append("L.LABEL_NAME,L.LABEL_ID,L.BUSI_CALIBER,LT.TYPE_NAME,LT.TYPE_ID ");
		sql.append("FROM CI_PEOPLE_ORIENTED_LABEL_INFO PL, ");
		sql.append("DIM_CRM_LABEL_TYPE            LT, ");
		sql.append("CI_MDA_SYS_TABLE_COLUMN       C, ");
		sql.append("CI_MDA_SYS_TABLE              B, ");
		sql.append("CI_LABEL_EXT_INFO             E, ");
		sql.append("CI_LABEL_INFO                 L ");
		sql.append("WHERE PL.LABEL_ID = C.COLUMN_NAME ");
		sql.append("AND C.COLUMN_ID = E.COLUMN_ID ");
		sql.append("AND E.LABEL_ID = L.LABEL_ID ");
		sql.append("AND C.TABLE_ID = B.TABLE_ID ");
		sql.append("AND PL.TYPE_ID = LT.TYPE_ID ");
		sql.append("ORDER BY B.TABLE_NAME, C.COLUMN_NAME ");
		log.info((new StringBuilder()).append("select label table info:").append(sql).toString());
		return getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(LabelTelTableInfo.class), new Object[0]);
	}

	public Map selectBySQL(String sql)
	{
		log.info((new StringBuilder()).append("label horizontal table:").append(sql).toString());
		return getBackSimpleJdbcTemplate().queryForMap(sql, new Object[0]);
	}

	public Integer selectPhoneNum(String tableName, String keyWord)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*) FROM ").append(tableName).append(" WHERE ").append(Configure.getInstance().getProperty("RELATED_COLUMN")).append("=?");
		log.info((new StringBuilder()).append("selectPhoneNum:").append(sql.toString()).toString());
		return Integer.valueOf(getBackSimpleJdbcTemplate().queryForInt(sql.toString(), new Object[] {
			keyWord
		}));
	}

	public List selectTableName(String sql)
	{
		log.info((new StringBuilder()).append("selectTableName:").append(sql).toString());
		return getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(LabelTelTableInfo.class), new Object[0]);
	}

	public List selectLabelTypeInfo()
	{
		String sql = "select type_id typeid,type_name typename,type_desc typedesc,icon from DIM_CRM_LABEL_TYPE";
		log.info((new StringBuilder()).append("selectLabelTypeInfo:").append(sql).toString());
		return getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CrmTypeModel.class), new Object[0]);
	}
}
