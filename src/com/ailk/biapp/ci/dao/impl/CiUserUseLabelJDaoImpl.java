package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.ICiUserUseLabelJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.model.LabelDetailInfo;
import com.ailk.biapp.ci.model.LabelTrendInfo;
import com.ailk.biapp.ci.model.LabelUseLogInfo;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository("ciUserUseLabelJDao")
public class CiUserUseLabelJDaoImpl extends JdbcBaseDao implements ICiUserUseLabelJDao {
    private Logger log = Logger.getLogger(this.getClass());

    public CiUserUseLabelJDaoImpl() {
    }

    public long getLabelUseTimesByID(String labelId) {
        String sql = " SELECT COUNT(1) FROM CI_USER_USE_LABEL WHERE LABEL_ID = ? ";
        long count = this.getSimpleJdbcTemplate().queryForLong(sql, new Object[]{labelId});
        return count;
    }

    public List<LabelDetailInfo> getSysRecommendLabel(int currPage, int pageSize, String dataScope) {
        String sql = "";
        if(dataScope.equals("all")) {
            sql = " SELECT e.MAX_VAL,e.MIN_VAL,l.UPDATE_CYCLE,l.LABEL_ID,l.LABEL_NAME,l.LABEL_TYPE_ID,t.USE_TIMES,l.BUSI_CALIBER,l.PARENT_ID,l.IS_SYS_RECOM,e.CUSTOM_NUM,e.ATTR_VAL, CASE WHEN T3.SUMCC IS NULL THEN \'false\' ELSE \'true\' END ISATTENTION FROM  CI_LABEL_INFO l,(SELECT LABEL_ID,COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL GROUP BY LABEL_ID) t,CI_LABEL_EXT_INFO e  LEFT JOIN ( SELECT T1.LABEL_ID, COUNT(T1.LABEL_ID) SUMCC FROM  CI_USER_ATTENTION_LABEL T1 WHERE T1.USER_ID = \'" + PrivilegeServiceUtil.getUserId() + "\' GROUP BY T1.LABEL_ID) T3 ON e.LABEL_ID = T3.LABEL_ID  " + " WHERE l.LABEL_ID = t.LABEL_ID AND l.LABEL_ID=e.LABEL_ID AND e.IS_STAT_USER_NUM=1 AND l.DATA_STATUS_ID=2  " + " ORDER BY t.USE_TIMES DESC ";
        } else {
            Date sqlPag = new Date();
            String results = "";
            String endTime = "";
            String today;
            String startDay;
            if(dataScope.equals("oneDay")) {
                today = DateUtil.date2String(sqlPag, "yyyy-MM-dd");
                startDay = DateUtil.getFrontDay(1, today, "yyyy-MM-dd");
                results = this.getDataBaseAdapter().getTimeStamp(startDay, "00", "00", "00");
                endTime = this.getDataBaseAdapter().getTimeStamp(today, "23", "59", "59");
            }

            if(dataScope.equals("oneWeek")) {
                today = DateUtil.date2String(sqlPag, "yyyy-MM-dd");
                startDay = DateUtil.getFrontDay(6, today, "yyyy-MM-dd");
                results = this.getDataBaseAdapter().getTimeStamp(startDay, "00", "00", "00");
                endTime = this.getDataBaseAdapter().getTimeStamp(today, "23", "59", "59");
            }

            if(dataScope.equals("oneMonth")) {
                today = DateUtil.date2String(sqlPag, "yyyy-MM-dd");
                startDay = DateUtil.getFrontDay(30, today, "yyyy-MM-dd");
                results = this.getDataBaseAdapter().getTimeStamp(startDay, "00", "00", "00");
                endTime = this.getDataBaseAdapter().getTimeStamp(today, "23", "59", "59");
            }

            if(dataScope.equals("threeMonth")) {
                today = DateUtil.date2String(sqlPag, "yyyy-MM-dd");
                startDay = DateUtil.getFrontDay(90, today, "yyyy-MM-dd");
                results = this.getDataBaseAdapter().getTimeStamp(startDay, "00", "00", "00");
                endTime = this.getDataBaseAdapter().getTimeStamp(today, "23", "59", "59");
            }

            sql = " SELECT e.MAX_VAL,e.MIN_VAL,l.UPDATE_CYCLE,l.LABEL_ID,l.LABEL_NAME,l.LABEL_TYPE_ID,t.USE_TIMES,l.BUSI_CALIBER,l.PARENT_ID,l.IS_SYS_RECOM,e.CUSTOM_NUM,e.ATTR_VAL FROM  CI_LABEL_INFO l,(SELECT LABEL_ID,COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL  WHERE USE_TIME>=" + results + " AND USE_TIME<=" + endTime + " GROUP BY LABEL_ID) t,CI_LABEL_EXT_INFO e " + " LEFT JOIN ( SELECT T1.LABEL_ID, COUNT(T1.LABEL_ID) SUMCC FROM  CI_USER_ATTENTION_LABEL T1 WHERE T1.USER_ID = \'" + PrivilegeServiceUtil.getUserId() + "\' GROUP BY T1.LABEL_ID) T3 ON e.LABEL_ID = T3.LABEL_ID  " + " WHERE l.LABEL_ID = t.LABEL_ID AND l.LABEL_ID=e.LABEL_ID AND e.IS_STAT_USER_NUM=1 AND l.DATA_STATUS_ID=2  " + " ORDER BY t.USE_TIMES DESC ";
        }

        String sqlPag1 = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        this.log.debug(sqlPag1);
        List results1 = this.getSimpleJdbcTemplate().query(sqlPag1, ParameterizedBeanPropertyRowMapper.newInstance(LabelDetailInfo.class), new Object[0]);
        return results1;
    }

    public List<LabelDetailInfo> getSysRecommendLabelTask() {
        String today = DateUtil.date2String(new Date(), "yyyy-MM-dd");
        String startDay = DateUtil.getFrontDay(90, today, "yyyy-MM-dd");
        String startTime = this.getDataBaseAdapter().getTimeStamp(startDay, "00", "00", "00");
        String endTime = this.getDataBaseAdapter().getTimeStamp(today, "23", "59", "59");
        String sql = " SELECT e.MAX_VAL,e.MIN_VAL,l.UPDATE_CYCLE,l.LABEL_ID,l.LABEL_NAME,l.LABEL_TYPE_ID,t.USE_TIMES,l.BUSI_CALIBER,l.PARENT_ID,l.IS_SYS_RECOM,e.CUSTOM_NUM,e.ATTR_VAL " +
                "FROM  CI_LABEL_INFO l,(SELECT LABEL_ID,COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL WHERE USE_TIME>=" + startTime + " AND USE_TIME<=" + endTime + " GROUP BY LABEL_ID) t,CI_LABEL_EXT_INFO e " +
                " WHERE l.LABEL_ID = t.LABEL_ID AND l.LABEL_ID=e.LABEL_ID AND e.IS_STAT_USER_NUM=1 AND l.DATA_STATUS_ID=2  " +
                " ORDER BY t.USE_TIMES DESC ";
        String sqlPag = this.getDataBaseAdapter().getPagedSql(sql, 1, ServiceConstants.SHOW_RECOMMEND_LABEL_NUM);
        this.log.debug(sqlPag);
        List results = this.getSimpleJdbcTemplate().query(sqlPag, ParameterizedBeanPropertyRowMapper.newInstance(LabelDetailInfo.class), new Object[0]);
        return results;
    }

    public Long getSysRecommendLabelNum() {
        String sql = " SELECT count(*) FROM  CI_LABEL_INFO l,(SELECT LABEL_ID,COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL GROUP BY LABEL_ID) t,CI_LABEL_EXT_INFO e  WHERE l.LABEL_ID = t.LABEL_ID AND l.LABEL_ID=e.LABEL_ID AND e.IS_STAT_USER_NUM=1 AND l.DATA_STATUS_ID=2   ORDER BY t.USE_TIMES DESC ";
        long count = this.getSimpleJdbcTemplate().queryForLong(sql, new Object[0]);
        this.log.debug(sql);
        return Long.valueOf(count);
    }

    public void deleteCiLabelUseStat(String dataDate) {
        String sql = "DELETE FROM CI_LABEL_USE_STAT WHERE DATA_DATE=?";
        this.getSimpleJdbcTemplate().update(sql, new Object[]{dataDate});
    }

    public void insertCiLabelDayUseStat(String dataDate) {
        Date date = DateUtil.string2Date(dataDate, "yyyyMMdd");
        String dateStr = DateUtil.date2String(date, "yyyy-MM-dd");
        String startTime = this.getDataBaseAdapter().getTimeStamp(dateStr, "00", "00", "00");
        String endTime = this.getDataBaseAdapter().getTimeStamp(dateStr, "23", "59", "59");
        String sql = "INSERT INTO CI_LABEL_USE_STAT(LABEL_ID,LABEL_USE_TYPE_ID,DATA_DATE,USE_TIMES)  SELECT LABEL_ID,-1 AS LABEL_USE_TYPE_ID,\'" + dataDate + "\' AS DATE_DATE,COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL  " + " WHERE USE_TIME >= " + startTime + " AND USE_TIME<=" + endTime + " AND LABEL_ID IN ( SELECT l.LABEL_ID FROM CI_LABEL_INFO l WHERE l.UPDATE_CYCLE=1 ) " + " GROUP BY LABEL_ID ";
        this.log.debug(sql);
        this.getSimpleJdbcTemplate().update(sql, new Object[0]);
    }

    public void insertCiLabelMonthUseStat(String dataDate) {
        Date date = DateUtil.string2Date(dataDate, "yyyyMM");
        String dateStr = DateUtil.date2String(date, "yyyy-MM");
        String firstDay = dateStr + "-01";
        String lastDay = "";

        try {
            Date startTime = (new SimpleDateFormat("yyyy-MM-dd")).parse(firstDay);
            startTime = DateUtil.lastDayOfMonth(date);
            lastDay = (new SimpleDateFormat("yyyy-MM-dd")).format(startTime);
        } catch (ParseException var9) {
            this.log.error("日期解析有误！", var9);
        }

        this.log.debug("LastDay Of Month:" + lastDay);
        String startTime1 = this.getDataBaseAdapter().getTimeStamp(firstDay, "00", "00", "00");
        String endTime = this.getDataBaseAdapter().getTimeStamp(lastDay, "23", "59", "59");
        String sql = "INSERT INTO CI_LABEL_USE_STAT(LABEL_ID,LABEL_USE_TYPE_ID,DATA_DATE,USE_TIMES)  SELECT LABEL_ID,-1 AS LABEL_USE_TYPE_ID,\'" + dataDate + "\' AS DATE_DATE,COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL  " + " WHERE USE_TIME >= " + startTime1 + " AND USE_TIME<=" + endTime + " AND LABEL_ID IN ( SELECT l.LABEL_ID FROM CI_LABEL_INFO l WHERE l.UPDATE_CYCLE=2 ) " + " GROUP BY LABEL_ID ";
        this.log.debug(sql);
        this.getSimpleJdbcTemplate().update(sql, new Object[0]);
    }

    public List<LabelTrendInfo> getLabelUseTimesTrendInfo(int currPage, int pageSize, String labelId) {
        String sql = " SELECT LABEL_ID,DATA_DATE,USE_TIMES AS VALUE FROM CI_LABEL_USE_STAT WHERE LABEL_USE_TYPE_ID=-1 AND LABEL_ID=?  ORDER BY DATA_DATE DESC ";
        String sqlPag = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        List labelTrendList = this.getSimpleJdbcTemplate().query(sqlPag, ParameterizedBeanPropertyRowMapper.newInstance(LabelTrendInfo.class), new Object[]{labelId});
        return labelTrendList;
    }

    public List<LabelTrendInfo> getLabelUseTimesTrendInfo(String labelId, String dataDate, String labelType) {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(labelId).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        HashMap queryParam = new HashMap();
        queryParam.put("labelId", labelId);
        String user_times = this.getDataBaseAdapter().getNvl("s.USE_TIMES", "0");
        StringBuffer sqlBuf = new StringBuffer("SELECT t.DATA_DATE as dataDate , " + user_times + " AS value FROM( ");
        sqlBuf.append("SELECT * FROM ").append(tableName).append(" n WHERE 1 = 1  AND n.CITY_ID = -1  AND n.VIP_LEVEL_ID=-1 AND n.BRAND_ID=-1 AND n.LABEL_ID=:labelId ");
        String labelTrendList;
        if(labelType.equals("1")) {
            labelTrendList = DateUtil.getFrontDay(31, dataDate);
            this.log.debug("startDay->" + labelTrendList + ",endDay->" + dataDate);
            sqlBuf.append(" AND DATA_DATE>=\'" + labelTrendList + "\' AND DATA_DATE<=\'" + dataDate + "\' ORDER BY DATA_DATE ASC ");
        } else {
            labelTrendList = dataDate.substring(0, 6);
            String startMonth = DateUtil.getFrontMonth(12, labelTrendList);
            this.log.debug("startMonth->" + startMonth + ",endMonth->" + labelTrendList);
            sqlBuf.append(" AND DATA_DATE>=\'" + startMonth + "\' AND DATA_DATE<=\'" + labelTrendList + "\' ORDER BY DATA_DATE ASC ");
        }

        sqlBuf.append(" ) t ");
        sqlBuf.append("LEFT JOIN( SELECT * FROM CI_LABEL_USE_STAT u WHERE u.LABEL_ID=:labelId ORDER BY DATA_DATE ASC ) s ON t.DATA_DATE = s.DATA_DATE ");
        this.log.debug("SQL->" + sqlBuf.toString());
        List labelTrendList1 = this.getSimpleJdbcTemplate().query(sqlBuf.toString(), ParameterizedBeanPropertyRowMapper.newInstance(LabelTrendInfo.class), queryParam);
        return labelTrendList1;
    }

    public List<LabelUseLogInfo> getLabelUseLogInfos(int currPage, int pageSize, String labelId) {
        String sql = "SELECT u.user_id,u.dept_id,u.label_id,l.LABEL_NAME,u.use_time,u.label_use_type_id,d.label_use_type_name,d.label_use_type_desc  FROM CI_USER_USE_LABEL  u,DIM_LABEL_USE_TYPE d,CI_LABEL_INFO l  WHERE u.label_id=l.label_id and u.label_use_type_id=d.label_use_type_id and u.label_id=? ORDER BY u.USE_TIME DESC ";
        String sqlPag = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        this.log.debug("sqlPag->" + sqlPag);
        List useLogList = this.getSimpleJdbcTemplate().query(sqlPag, ParameterizedBeanPropertyRowMapper.newInstance(LabelUseLogInfo.class), new Object[]{labelId});
        return useLogList;
    }

    public LabelUseLogInfo getLastLabelUseLog(String labelId) {
        String sql = " SELECT LABEL_ID,USER_ID,USE_TIME FROM CI_USER_USE_LABEL WHERE LABEL_ID=?  ORDER BY USE_TIME DESC ";
        String sqlPag = this.getDataBaseAdapter().getPagedSql(sql, 1, 1);
        this.log.debug("sqlPag->" + sqlPag);
        List useLogList = this.getSimpleJdbcTemplate().query(sqlPag, ParameterizedBeanPropertyRowMapper.newInstance(LabelUseLogInfo.class), new Object[]{labelId});
        LabelUseLogInfo useLog = null;
        if(null != useLogList && useLogList.size() > 0) {
            useLog = (LabelUseLogInfo)useLogList.get(0);
        }

        return useLog;
    }

    public List<LabelDetailInfo> getUserUsedLabel(int currPage, int pageSize, String userId, String orderType, String dataScope) {
        String sql = "";
        if(dataScope.equals("all")) {
            sql = " SELECT t11.UPDATE_CYCLE,t11.MAX_VAL,t11.MIN_VAL,t11.LABEL_ID,t11.LABEL_NAME,t11.LABEL_TYPE_ID,t11.BUSI_CALIBER,t11.PARENT_ID,t11.IS_SYS_RECOM,t11.CUSTOM_NUM,t11.ATTR_VAL,t22.USE_TIMES,t11.USE_TIME, CASE WHEN T3.SUMCC IS NULL THEN \'false\' ELSE \'true\' END ISATTENTION FROM  (SELECT l.UPDATE_CYCLE,e.MAX_VAL,e.MIN_VAL,l.LABEL_ID,l.LABEL_NAME,l.LABEL_TYPE_ID,l.BUSI_CALIBER,l.PARENT_ID,l.IS_SYS_RECOM,e.CUSTOM_NUM,e.ATTR_VAL,t1.USE_TIME FROM  CI_LABEL_INFO l,CI_LABEL_EXT_INFO e,(SELECT u.LABEL_ID,MAX(u.USE_TIME) AS USE_TIME FROM CI_USER_USE_LABEL u WHERE USER_ID=? GROUP BY LABEL_ID) t1  WHERE l.LABEL_ID=e.LABEL_ID AND e.IS_STAT_USER_NUM=1 AND l.LABEL_ID=t1.LABEL_ID  AND l.DATA_STATUS_ID=2 ) t11 LEFT JOIN   (SELECT LABEL_ID,COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL GROUP BY LABEL_ID) t22 ON t11.LABEL_ID=t22.LABEL_ID  LEFT JOIN ( SELECT T1.LABEL_ID, COUNT(T1.LABEL_ID) SUMCC FROM  CI_USER_ATTENTION_LABEL T1 WHERE T1.USER_ID = \'" + PrivilegeServiceUtil.getUserId() + "\' GROUP BY T1.LABEL_ID) T3 ON t11.LABEL_ID = T3.LABEL_ID  ";
        } else {
            Date sqlPag = new Date();
            String results = "";
            String endTime = "";
            String today;
            String startDay;
            if(dataScope.equals("oneDay")) {
                today = DateUtil.date2String(sqlPag, "yyyy-MM-dd");
                startDay = DateUtil.getFrontDay(1, today, "yyyy-MM-dd");
                results = this.getDataBaseAdapter().getTimeStamp(startDay, "00", "00", "00");
                endTime = this.getDataBaseAdapter().getTimeStamp(today, "23", "59", "59");
            }

            if(dataScope.equals("oneMonth")) {
                today = DateUtil.date2String(sqlPag, "yyyy-MM-dd");
                startDay = DateUtil.getFrontDay(30, today, "yyyy-MM-dd");
                results = this.getDataBaseAdapter().getTimeStamp(startDay, "00", "00", "00");
                endTime = this.getDataBaseAdapter().getTimeStamp(today, "23", "59", "59");
            }

            if(dataScope.equals("threeMonth")) {
                today = DateUtil.date2String(sqlPag, "yyyy-MM-dd");
                startDay = DateUtil.getFrontDay(90, today, "yyyy-MM-dd");
                results = this.getDataBaseAdapter().getTimeStamp(startDay, "00", "00", "00");
                endTime = this.getDataBaseAdapter().getTimeStamp(today, "23", "59", "59");
            }

            sql = " SELECT t11.UPDATE_CYCLE,t11.MAX_VAL,t11.MIN_VAL,t11.LABEL_ID,t11.LABEL_NAME,t11.LABEL_TYPE_ID,t11.BUSI_CALIBER,t11.PARENT_ID,t11.IS_SYS_RECOM,t11.CUSTOM_NUM,t11.ATTR_VAL,t22.USE_TIMES,t11.USE_TIME, CASE WHEN T3.SUMCC IS NULL THEN \'false\' ELSE \'true\' END ISATTENTION FROM  (SELECT l.UPDATE_CYCLE,e.MAX_VAL,e.MIN_VAL,l.LABEL_ID,l.LABEL_NAME,l.LABEL_TYPE_ID,l.BUSI_CALIBER,l.PARENT_ID,l.IS_SYS_RECOM,e.CUSTOM_NUM,e.ATTR_VAL,t1.USE_TIME FROM  CI_LABEL_INFO l,CI_LABEL_EXT_INFO e,(SELECT u.LABEL_ID,MAX(u.USE_TIME) AS USE_TIME FROM CI_USER_USE_LABEL u WHERE USER_ID=? GROUP BY LABEL_ID) t1  WHERE t1.USE_TIME>=" + results + " AND t1.USE_TIME<=" + endTime + "  AND l.LABEL_ID=e.LABEL_ID AND e.IS_STAT_USER_NUM=1 AND l.LABEL_ID=t1.LABEL_ID  AND l.DATA_STATUS_ID=2 ) t11 LEFT JOIN " + "  (SELECT LABEL_ID,COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL GROUP BY LABEL_ID) t22 ON t11.LABEL_ID=t22.LABEL_ID " + " LEFT JOIN ( SELECT T1.LABEL_ID, COUNT(T1.LABEL_ID) SUMCC FROM  CI_USER_ATTENTION_LABEL T1 WHERE T1.USER_ID = \'" + PrivilegeServiceUtil.getUserId() + "\' GROUP BY T1.LABEL_ID) T3 ON t11.LABEL_ID = T3.LABEL_ID  ";
        }

        if(orderType.equals("time")) {
            sql = sql + " ORDER BY USE_TIMES DESC ";
        }

        if(orderType.equals("date")) {
            sql = sql + " ORDER BY t11.USE_TIME DESC ";
        }

        String sqlPag1 = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        this.log.debug("我使用的标签sql: " + sqlPag1);
        List results1 = this.getSimpleJdbcTemplate().query(sqlPag1, ParameterizedBeanPropertyRowMapper.newInstance(LabelDetailInfo.class), new Object[]{userId});
        return results1;
    }

    public long getUserUsedLabelNum(String userId, String dataScope) {
        String sql = "";
        if(dataScope.equals("all")) {
            sql = " SELECT COUNT(*) FROM ( SELECT t11.LABEL_ID,t11.LABEL_NAME,t11.BUSI_CALIBER,t11.CUSTOM_NUM,t22.USE_TIMES,t11.USE_TIME FROM  (SELECT l.LABEL_ID,l.LABEL_NAME,l.BUSI_CALIBER,e.CUSTOM_NUM,t1.USE_TIME FROM  CI_LABEL_INFO l,CI_LABEL_EXT_INFO e,(SELECT u.LABEL_ID,MAX(u.USE_TIME) AS USE_TIME FROM CI_USER_USE_LABEL u WHERE USER_ID=? GROUP BY LABEL_ID) t1  WHERE l.LABEL_ID=e.LABEL_ID AND e.IS_STAT_USER_NUM=1 AND l.LABEL_ID=t1.LABEL_ID  AND l.DATA_STATUS_ID=2 ) t11 LEFT JOIN   (SELECT LABEL_ID,COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL GROUP BY LABEL_ID) t22 ON t11.LABEL_ID=t22.LABEL_ID  ) abc  ";
        } else {
            Date count = new Date();
            String startTime = "";
            String endTime = "";
            String today;
            String startDay;
            if(dataScope.equals("oneDay")) {
                today = DateUtil.date2String(count, "yyyy-MM-dd");
                startDay = DateUtil.getFrontDay(1, today, "yyyy-MM-dd");
                startTime = this.getDataBaseAdapter().getTimeStamp(startDay, "00", "00", "00");
                endTime = this.getDataBaseAdapter().getTimeStamp(today, "23", "59", "59");
            }

            if(dataScope.equals("oneMonth")) {
                today = DateUtil.date2String(count, "yyyy-MM-dd");
                startDay = DateUtil.getFrontDay(30, today, "yyyy-MM-dd");
                startTime = this.getDataBaseAdapter().getTimeStamp(startDay, "00", "00", "00");
                endTime = this.getDataBaseAdapter().getTimeStamp(today, "23", "59", "59");
            }

            if(dataScope.equals("threeMonth")) {
                today = DateUtil.date2String(count, "yyyy-MM-dd");
                startDay = DateUtil.getFrontDay(90, today, "yyyy-MM-dd");
                startTime = this.getDataBaseAdapter().getTimeStamp(startDay, "00", "00", "00");
                endTime = this.getDataBaseAdapter().getTimeStamp(today, "23", "59", "59");
            }

            sql = " SELECT COUNT(*) FROM ( SELECT t11.LABEL_ID,t11.LABEL_NAME,t11.BUSI_CALIBER,t11.CUSTOM_NUM,t22.USE_TIMES,t11.USE_TIME FROM  (SELECT l.LABEL_ID,l.LABEL_NAME,l.BUSI_CALIBER,e.CUSTOM_NUM,t1.USE_TIME FROM  CI_LABEL_INFO l,CI_LABEL_EXT_INFO e,(SELECT u.LABEL_ID,MAX(u.USE_TIME) AS USE_TIME FROM CI_USER_USE_LABEL u WHERE USER_ID=? GROUP BY LABEL_ID) t1  WHERE t1.USE_TIME>=" + startTime + " AND t1.USE_TIME<=" + endTime + "  AND l.LABEL_ID=e.LABEL_ID AND e.IS_STAT_USER_NUM=1 AND l.LABEL_ID=t1.LABEL_ID  AND l.DATA_STATUS_ID=2 ) t11 LEFT JOIN " + "  (SELECT LABEL_ID,COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL GROUP BY LABEL_ID) t22 ON t11.LABEL_ID=t22.LABEL_ID " + " ) abc ";
        }

        long count1 = this.getSimpleJdbcTemplate().queryForLong(sql, new Object[]{userId});
        return count1;
    }
}
