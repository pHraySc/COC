package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.ILabelQueryJdbcDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiUserAttentionLabelId;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.*;
import com.ailk.biapp.ci.util.DataBaseAdapter;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository("labelQueryJdbcDao")
public class LabelQueryJdbcDaoImpl extends JdbcBaseDao implements ILabelQueryJdbcDao {
    private static Logger log = Logger.getLogger(LabelQueryJdbcDaoImpl.class);

    public LabelQueryJdbcDaoImpl() {
    }

    public List<LabelShortInfo> getLabelByDeptId(Integer deptId) {
        String sql = " SELECT  l.LABEL_ID,l.UPDATE_CYCLE,l.LABEL_NAME FROM  CI_LABEL_INFO l,(SELECT DISTINCT LABEL_ID FROM CI_USER_USE_LABEL WHERE DEPT_ID=?) a  WHERE l.LABEL_ID=a.LABEL_ID AND l.DATA_STATUS_ID=2  ";
        List labelInfoList = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(LabelShortInfo.class), new Object[]{deptId});
        return labelInfoList;
    }

    public List<LabelShortInfo> getLabelByDeptId(Integer deptId, String labelName) {
        String sql = " SELECT  l.LABEL_ID,l.UPDATE_CYCLE,l.LABEL_NAME FROM  CI_LABEL_INFO l,(SELECT DISTINCT LABEL_ID FROM CI_USER_USE_LABEL WHERE DEPT_ID=:deptId) a  WHERE l.LABEL_ID=a.LABEL_ID AND l.DATA_STATUS_ID=2 AND l.LABEL_NAME like :labelName ";
        HashMap queryParam = new HashMap();
        queryParam.put("deptId", deptId);
        queryParam.put("labelName", "%" + labelName + "%");
        List labelInfoList = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(LabelShortInfo.class), queryParam);
        return labelInfoList;
    }

    public List<LabelShortInfo> getUserAttentionLabel(String userId) {
        String sql = " SELECT  l.LABEL_ID,l.UPDATE_CYCLE,l.LABEL_NAME FROM  CI_LABEL_INFO l,CI_USER_ATTENTION_LABEL a  WHERE l.LABEL_ID=a.LABEL_ID AND l.DATA_STATUS_ID=2 AND a.USER_ID=? ";
        List labelInfoList = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(LabelShortInfo.class), new Object[]{userId});
        return labelInfoList;
    }

    public List<LabelShortInfo> getUserAttentionLabel(String userId, String labelName) {
        String sql = " SELECT  l.LABEL_ID,l.UPDATE_CYCLE,l.LABEL_NAME FROM  CI_LABEL_INFO l,CI_USER_ATTENTION_LABEL a  WHERE l.LABEL_ID=a.LABEL_ID AND l.DATA_STATUS_ID=2 AND a.USER_ID=:userId AND l.LABEL_NAME like :labelName ";
        HashMap queryParam = new HashMap();
        queryParam.put("userId", userId);
        queryParam.put("labelName", "%" + labelName + "%");
        List labelInfoList = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(LabelShortInfo.class), queryParam);
        return labelInfoList;
    }

    public List<LabelShortInfo> getUserLabelByUserId(String userId) {
        String sql = "SELECT t.LABEL_ID,t.UPDATE_CYCLE,t.LABEL_NAME  FROM CI_LABEL_INFO t WHERE t.DATA_STATUS_ID=2 AND t.CREATE_USER_ID=?";
        List labelInfoList = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(LabelShortInfo.class), new Object[]{userId});
        return labelInfoList;
    }

    public List<LabelShortInfo> getUserLabelByUserId(String userId, String labelName) {
        String sql = "SELECT t.LABEL_ID,t.UPDATE_CYCLE,t.LABEL_NAME  FROM CI_LABEL_INFO t WHERE t.DATA_STATUS_ID=2 AND t.CREATE_USER_ID=:userId AND t.LABEL_NAME like :labelName escape \'|\' ";
        HashMap queryParam = new HashMap();
        queryParam.put("userId", userId);
        queryParam.put("labelName", this.replaceSpecialCharacter("%" + labelName + "%"));
        List labelInfoList = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(LabelShortInfo.class), queryParam);
        return labelInfoList;
    }

    public List<CiUserAttentionLabelId> queryRecordByLabelId(String labelId) {
        String sql = " SELECT user_id,label_id FROM  CI_USER_ATTENTION_LABEL  WHERE LABEL_ID=:labelId ";
        HashMap queryParam = new HashMap();
        queryParam.put("labelId", labelId);
        List recordList = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiUserAttentionLabelId.class), queryParam);
        return recordList;
    }

    public LabelDetailInfo getLabelDetailInfo(String labelId) {
        String sql = " SELECT l.LABEL_ID,l.LABEL_NAME,l.EFFEC_TIME,l.FAIL_TIME,l.CREATE_USER_ID,l.PUBLISH_USER_ID,l.IS_SYS_RECOM,e.TECH_CALIBER,d.DATA_STATUS_NAME,  l.UPDATE_CYCLE,l.CREATE_TIME,l.PUBLISH_TIME,l.BUSI_CALIBER,e.CUSTOM_NUM,e.ATTR_VAL,l.DATA_SOURCE,e.MAX_VAL,e.MIN_VAL,l.LABEL_TYPE_ID,  l.busi_legend,l.apply_suggest FROM  CI_LABEL_INFO l,CI_LABEL_EXT_INFO e,DIM_LABEL_DATA_STATUS d  WHERE l.LABEL_ID=e.LABEL_ID AND e.IS_STAT_USER_NUM=1 AND l.DATA_STATUS_ID=d.DATA_STATUS_ID AND l.LABEL_ID=?";
        LabelDetailInfo labelDetailInfo = (LabelDetailInfo)this.getSimpleJdbcTemplate().queryForObject(sql, new LabelDetailInfoRowMapper(), new Object[]{labelId});
        return labelDetailInfo;
    }

    public long getLabelUseTimesByID(String labelId) {
        String sql = "SELECT COUNT(1) FROM CI_USER_USE_LABEL WHERE LABEL_ID = ? ";
        long count = this.getSimpleJdbcTemplate().queryForLong(sql, new Object[]{labelId});
        return count;
    }

    public List<LabelTrendInfo> getLabelUseTimesTrendInfo(int currPage, int pageSize, String labelId) {
        String sql = " SELECT LABEL_ID,DATA_DATE,USE_TIMES AS VALUE FROM CI_LABEL_USE_STAT WHERE LABEL_USE_TYPE_ID=-1 AND LABEL_ID=?  ORDER BY DATA_DATE DESC ";
        String sqlPag = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        List labelTrendList = this.getSimpleJdbcTemplate().query(sqlPag, ParameterizedBeanPropertyRowMapper.newInstance(LabelTrendInfo.class), new Object[]{labelId});
        return labelTrendList;
    }

    public List<LabelTrendInfo> getLabelUserNumsTrendInfo(int currPage, int pageSize, String labelId) {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(labelId.toString()).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        String sql = " SELECT LABEL_ID,DATA_DATE,CUSTOM_NUM AS VALUE FROM " + tableName + " WHERE CITY_ID=-1 AND VIP_LEVEL_ID=-1 AND BRAND_ID=-1 AND LABEL_ID=? " + " ORDER BY DATA_DATE DESC ";
        String sqlPag = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        List labelTrendList = this.getSimpleJdbcTemplate().query(sqlPag, ParameterizedBeanPropertyRowMapper.newInstance(LabelTrendInfo.class), new Object[]{labelId});
        return labelTrendList;
    }

    public List<LabelTrendTableInfo> getLabelTrendTableInfo(int currPage, int pageSize, String labelId) {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(labelId.toString()).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        String sql = " SELECT n.LABEL_ID,n.DATA_DATE,n.CUSTOM_NUM,s.USE_TIMES FROM " + tableName + " n  " + " LEFT JOIN CI_LABEL_USE_STAT s ON n.DATA_DATE = s.DATA_DATE " + " AND n.CITY_ID=-1 AND n.VIP_LEVEL_ID=-1 AND n.BRAND_ID=-1 AND s.LABEL_USE_TYPE_ID=-1 AND n.LABEL_ID=?" + " ORDER BY n.DATA_DATE DESC ";
        String sqlPag = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        List labelTrendTableList = this.getSimpleJdbcTemplate().query(sqlPag, ParameterizedBeanPropertyRowMapper.newInstance(LabelTrendTableInfo.class), new Object[]{labelId});
        return labelTrendTableList;
    }

    public boolean isNewLabel(String labelId) {
        boolean isNew = false;
        Date date = new Date();
        String today = DateUtil.date2String(date, "yyyy-MM-dd");
        String startDay = DateUtil.getFrontDay(31, today, "yyyy-MM-dd");
        String startTime = this.getDataBaseAdapter().getTimeStamp(startDay, "00", "00", "00");
        String endTime = this.getDataBaseAdapter().getTimeStamp(today, "23", "59", "59");
        String sql = " SELECT COUNT(*) FROM CI_LABEL_INFO WHERE PUBLISH_TIME >=" + startTime + " AND PUBLISH_TIME <=" + endTime + " AND LABEL_ID=?";
        log.debug("Sql->" + sql);
        int count = this.getSimpleJdbcTemplate().queryForInt(sql, new Object[]{labelId});
        if(count == 1) {
            isNew = true;
        }

        return isNew;
    }

    public List<LabelDetailInfo> getUserAttentionLabel(int currPage, int pageSize, String userId, String orderType, String dataScope) {
        String publishTime = this.getDataBaseAdapter().getFullDate("t11.PUBLISH_TIME");
        String useTimes = this.getDataBaseAdapter().getNvl("t22.USE_TIMES", "0");
        String sql = "";
        if(dataScope.equals("all")) {
            sql = " SELECT t11.UPDATE_CYCLE,t11.MAX_VAL,t11.MIN_VAL,t11.LABEL_ID,t11.LABEL_NAME,t11.LABEL_TYPE_ID,t11.BUSI_CALIBER,t11.PARENT_ID,t11.IS_SYS_RECOM,t11.CUSTOM_NUM,t11.DATA_DATE,t11.ATTR_VAL,t11.ATTENTION_TIME," + publishTime + " AS PUBLISH_TIME ," + useTimes + " AS USE_TIMES ," + "  CASE WHEN T3.SUMCC IS NULL THEN \'false\' ELSE \'true\' END ISATTENTION FROM  " + " (SELECT l.DATA_DATE, l.UPDATE_CYCLE,e.MAX_VAL,e.MIN_VAL,l.LABEL_ID,l.LABEL_NAME,l.LABEL_TYPE_ID,l.BUSI_CALIBER,l.PARENT_ID,l.IS_SYS_RECOM,e.CUSTOM_NUM,e.ATTR_VAL,t1.ATTENTION_TIME,l.PUBLISH_TIME FROM " + "  CI_LABEL_INFO l,CI_LABEL_EXT_INFO e,(SELECT LABEL_ID,ATTENTION_TIME FROM CI_USER_ATTENTION_LABEL WHERE USER_ID=?) t1 " + "  WHERE l.LABEL_ID=e.LABEL_ID AND e.IS_STAT_USER_NUM=1 AND l.LABEL_ID=t1.LABEL_ID  AND l.DATA_STATUS_ID=2 ) t11 LEFT JOIN  " + " (SELECT LABEL_ID,COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL GROUP BY LABEL_ID) t22 ON t11.LABEL_ID=t22.LABEL_ID " + " LEFT JOIN ( SELECT T1.LABEL_ID, COUNT(T1.LABEL_ID) SUMCC FROM " + " CI_USER_ATTENTION_LABEL T1 WHERE T1.USER_ID = \'" + PrivilegeServiceUtil.getUserId() + "\' GROUP BY T1.LABEL_ID) T3" + " ON t11.LABEL_ID = T3.LABEL_ID ";
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

            sql = " SELECT t11.UPDATE_CYCLE,t11.MAX_VAL,t11.MIN_VAL,t11.LABEL_ID,t11.LABEL_NAME,t11.DATA_DATE,t11.LABEL_TYPE_ID,t11.BUSI_CALIBER,t11.PARENT_ID,t11.IS_SYS_RECOM,t11.CUSTOM_NUM,t11.ATTR_VAL,t11.ATTENTION_TIME," + publishTime + " AS PUBLISH_TIME ," + useTimes + " AS USE_TIMES ," + "  CASE WHEN T3.SUMCC IS NULL THEN \'false\' ELSE \'true\' END ISATTENTION FROM  " + " (SELECT l.DATA_DATE, l.UPDATE_CYCLE,e.MAX_VAL,e.MIN_VAL,l.LABEL_ID,l.LABEL_NAME,l.LABEL_TYPE_ID,l.BUSI_CALIBER,l.PARENT_ID,l.IS_SYS_RECOM,e.CUSTOM_NUM,e.ATTR_VAL,t1.ATTENTION_TIME, l.PUBLISH_TIME FROM " + "  CI_LABEL_INFO l,CI_LABEL_EXT_INFO e,(SELECT LABEL_ID,ATTENTION_TIME FROM CI_USER_ATTENTION_LABEL WHERE USER_ID=?) t1 " + "  WHERE t1.ATTENTION_TIME>=" + results + " AND t1.ATTENTION_TIME<=" + endTime + "  AND l.LABEL_ID=e.LABEL_ID AND e.IS_STAT_USER_NUM=1 AND l.LABEL_ID=t1.LABEL_ID AND l.DATA_STATUS_ID=2 ) t11 LEFT JOIN  " + " (SELECT LABEL_ID,COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL GROUP BY LABEL_ID) t22 ON t11.LABEL_ID=t22.LABEL_ID " + " LEFT JOIN ( SELECT T1.LABEL_ID, COUNT(T1.LABEL_ID) SUMCC FROM " + " CI_USER_ATTENTION_LABEL T1 WHERE T1.USER_ID = \'" + PrivilegeServiceUtil.getUserId() + "\' GROUP BY T1.LABEL_ID) T3" + " ON t11.LABEL_ID = T3.LABEL_ID ";
        }

        if(orderType.equals("time")) {
            sql = sql + " ORDER BY USE_TIMES DESC ";
        }

        if(orderType.equals("date")) {
            sql = sql + " ORDER BY t11.ATTENTION_TIME DESC ";
        }

        String sqlPag1 = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        log.debug("我关注的标签sql： " + sqlPag1);
        List results1 = this.getSimpleJdbcTemplate().query(sqlPag1, ParameterizedBeanPropertyRowMapper.newInstance(LabelDetailInfo.class), new Object[]{userId});
        return results1;
    }

    public List<LabelDetailInfo> getNewPublishLabel(int currPage, int pageSize, String orderType, String dataScope) {
        String publishTime = this.getDataBaseAdapter().getFullDate("t11.PUBLISH_TIME");
        String useTimes = this.getDataBaseAdapter().getNvl("t22.USE_TIMES", "0");
        String sql = "";
        if(dataScope.equals("all")) {
            sql = " SELECT t11.MAX_VAL,t11.MIN_VAL,t11.UPDATE_CYCLE,t11.LABEL_ID,t11.LABEL_NAME,t11.LABEL_TYPE_ID,t11.BUSI_CALIBER,t11.PARENT_ID,t11.IS_SYS_RECOM,t11.CUSTOM_NUM,t11.ATTR_VAL, " + publishTime + " AS PUBLISH_TIME , " + useTimes + " AS USE_TIMES ," + "  CASE WHEN T3.SUMCC IS NULL THEN \'false\' ELSE \'true\' END ISATTENTION FROM  " + " (SELECT e.MAX_VAL,e.MIN_VAL,l.UPDATE_CYCLE,l.LABEL_ID,l.LABEL_NAME,l.LABEL_TYPE_ID,l.BUSI_CALIBER,l.PARENT_ID,l.IS_SYS_RECOM,e.CUSTOM_NUM,e.ATTR_VAL,l.PUBLISH_TIME FROM" + "  CI_LABEL_INFO l,CI_LABEL_EXT_INFO e" + "  WHERE l.LABEL_ID=e.LABEL_ID AND e.IS_STAT_USER_NUM=1 AND l.DATA_STATUS_ID=2 AND e.LABEL_LEVEL>2 " + ") t11 LEFT JOIN " + " (SELECT LABEL_ID,COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL GROUP BY LABEL_ID) t22 ON t11.LABEL_ID=t22.LABEL_ID" + " LEFT JOIN ( SELECT T1.LABEL_ID, COUNT(T1.LABEL_ID) SUMCC FROM " + " CI_USER_ATTENTION_LABEL T1 WHERE T1.USER_ID = \'" + PrivilegeServiceUtil.getUserId() + "\' GROUP BY T1.LABEL_ID) T3" + " ON t11.LABEL_ID = T3.LABEL_ID ";
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

            sql = " SELECT t11.MAX_VAL,t11.MIN_VAL,t11.UPDATE_CYCLE,t11.LABEL_ID,t11.LABEL_NAME,t11.LABEL_TYPE_ID,t11.BUSI_CALIBER,t11.PARENT_ID,t11.IS_SYS_RECOM,t11.CUSTOM_NUM,t11.ATTR_VAL,t11.PUBLISH_TIME, " + useTimes + " AS USE_TIMES FROM  " + " (SELECT e.MAX_VAL,e.MIN_VAL,l.UPDATE_CYCLE,l.LABEL_ID,l.LABEL_NAME,l.LABEL_TYPE_ID,l.BUSI_CALIBER,l.PARENT_ID,l.IS_SYS_RECOM,e.CUSTOM_NUM,e.ATTR_VAL,l.PUBLISH_TIME FROM" + "  CI_LABEL_INFO l,CI_LABEL_EXT_INFO e" + "  WHERE l.LABEL_ID=e.LABEL_ID AND e.IS_STAT_USER_NUM=1 AND l.DATA_STATUS_ID=2 AND e.LABEL_LEVEL>2 AND PUBLISH_TIME >=" + results + " AND PUBLISH_TIME<=" + endTime + " ) t11 LEFT JOIN " + " (SELECT LABEL_ID,COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL GROUP BY LABEL_ID) t22 ON t11.LABEL_ID=t22.LABEL_ID";
        }

        if(orderType.equals("time")) {
            sql = sql + " ORDER BY USE_TIMES DESC ";
        }

        if(orderType.equals("date")) {
            sql = sql + " ORDER BY t11.PUBLISH_TIME DESC ";
        }

        String sqlPag1 = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        log.debug("sqlPag->" + sqlPag1);
        List results1 = this.getSimpleJdbcTemplate().query(sqlPag1, ParameterizedBeanPropertyRowMapper.newInstance(LabelDetailInfo.class), new Object[0]);
        return results1;
    }

    public long getNewPublishLabelNum(String dataScope) {
        String useTimes = this.getDataBaseAdapter().getNvl("t22.USE_TIMES", "0");
        String sql = "";
        if(dataScope.equals("all")) {
            sql = " SELECT COUNT(*) FROM ( SELECT t11.LABEL_ID,t11.LABEL_NAME,t11.BUSI_CALIBER,t11.CUSTOM_NUM,t11.PUBLISH_TIME,t11.IS_SYS_RECOM," + useTimes + " AS USE_TIMES FROM  " + " (SELECT l.LABEL_ID,l.LABEL_NAME,l.BUSI_CALIBER,e.CUSTOM_NUM,l.PUBLISH_TIME,l.IS_SYS_RECOM FROM" + "  CI_LABEL_INFO l,CI_LABEL_EXT_INFO e" + "  WHERE l.LABEL_ID=e.LABEL_ID AND e.IS_STAT_USER_NUM=1 AND l.DATA_STATUS_ID=2 AND e.LABEL_LEVEL > 2 " + " ) t11 LEFT JOIN " + " (SELECT LABEL_ID,COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL GROUP BY LABEL_ID) t22 ON t11.LABEL_ID=t22.LABEL_ID" + " ) abc ";
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

            sql = " SELECT COUNT(*) FROM ( SELECT t11.LABEL_ID,t11.LABEL_NAME,t11.BUSI_CALIBER,t11.CUSTOM_NUM,t11.PUBLISH_TIME,t11.IS_SYS_RECOM," + useTimes + " AS USE_TIMES FROM  " + " (SELECT l.LABEL_ID,l.LABEL_NAME,l.BUSI_CALIBER,e.CUSTOM_NUM,l.PUBLISH_TIME,l.IS_SYS_RECOM FROM" + "  CI_LABEL_INFO l,CI_LABEL_EXT_INFO e" + "  WHERE l.LABEL_ID=e.LABEL_ID AND e.IS_STAT_USER_NUM=1 AND l.DATA_STATUS_ID=2 AND e.LABEL_LEVEL > 2 AND PUBLISH_TIME >=" + startTime + " AND PUBLISH_TIME<=" + endTime + " ) t11 LEFT JOIN " + " (SELECT LABEL_ID,COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL GROUP BY LABEL_ID) t22 ON t11.LABEL_ID=t22.LABEL_ID" + " ) abc ";
        }

        long count1 = this.getSimpleJdbcTemplate().queryForLong(sql, new Object[0]);
        return count1;
    }

    public long getUserAttentionLabelNum(String userId, String dataScope) {
        String useTimes = this.getDataBaseAdapter().getNvl("t22.USE_TIMES", "0");
        String sql = "";
        if(dataScope.equals("all")) {
            sql = " SELECT COUNT(*) FROM (  SELECT t11.LABEL_ID,t11.LABEL_NAME,t11.BUSI_CALIBER,t11.IS_SYS_RECOM,t11.CUSTOM_NUM,t11.ATTENTION_TIME," + useTimes + " AS USE_TIMES FROM  " + " (SELECT l.LABEL_ID,l.LABEL_NAME,l.BUSI_CALIBER,l.IS_SYS_RECOM,e.CUSTOM_NUM,t1.ATTENTION_TIME FROM " + "  CI_LABEL_INFO l,CI_LABEL_EXT_INFO e,(SELECT LABEL_ID,ATTENTION_TIME FROM CI_USER_ATTENTION_LABEL WHERE USER_ID=?) t1 " + "  WHERE l.LABEL_ID=e.LABEL_ID AND e.IS_STAT_USER_NUM=1 AND l.LABEL_ID=t1.LABEL_ID  AND l.DATA_STATUS_ID=2 ) t11 LEFT JOIN  " + " (SELECT LABEL_ID,COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL GROUP BY LABEL_ID) t22 ON t11.LABEL_ID=t22.LABEL_ID " + " ) abc";
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

            sql = " SELECT COUNT(*) FROM ( SELECT t11.LABEL_ID,t11.LABEL_NAME,t11.BUSI_CALIBER,t11.IS_SYS_RECOM,t11.CUSTOM_NUM,t11.ATTENTION_TIME," + useTimes + " AS USE_TIMES FROM  " + " (SELECT l.LABEL_ID,l.LABEL_NAME,l.BUSI_CALIBER,l.IS_SYS_RECOM,e.CUSTOM_NUM,t1.ATTENTION_TIME FROM " + "  CI_LABEL_INFO l,CI_LABEL_EXT_INFO e,(SELECT LABEL_ID,ATTENTION_TIME FROM CI_USER_ATTENTION_LABEL WHERE USER_ID=?) t1 " + "  WHERE t1.ATTENTION_TIME>=" + startTime + " AND t1.ATTENTION_TIME<=" + endTime + "  AND l.LABEL_ID=e.LABEL_ID AND e.IS_STAT_USER_NUM=1 AND l.LABEL_ID=t1.LABEL_ID  AND l.DATA_STATUS_ID=2 ) t11 LEFT JOIN  " + " (SELECT LABEL_ID,COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL GROUP BY LABEL_ID) t22 ON t11.LABEL_ID=t22.LABEL_ID " + " ) abc";
        }

        /*-----------------------------------------过滤标签 start------------------------------------------------------*/
        Map<String,String> hiddenMap=CacheBase.getInstance().getHiddenLabelMap();
        Iterator<String> iterator=hiddenMap.keySet().iterator();
        StringBuilder sqlStr=new StringBuilder();
        boolean first=true;
        while (iterator.hasNext()){
            String hLabelId=hiddenMap.get(String.valueOf(iterator.next()));
            if (first){
                sqlStr.append(" WHERE abc.LABEL_ID!="+hLabelId);
                first=false;
            }else {
                sqlStr.append(" AND abc.LABEL_ID!="+hLabelId);
            }
        }
        sql=sql+new String(sqlStr);
        System.out.println("查询收藏数量："+sql);
        /*-----------------------------------------过滤标签 end--------------------------------------------------------*/

        long count1 = this.getSimpleJdbcTemplate().queryForLong(sql, new Object[]{userId});
        return count1;
    }

    public List<LabelDetailInfo> getEffectiveLabel(Pager pager, String orderType, String labelKeyWord, Integer topLabelId, String dataScope, CiLabelInfo searchBean) throws Exception {
        if(StringUtil.isNotEmpty(labelKeyWord)) {
            labelKeyWord = labelKeyWord.toUpperCase();
        }

        labelKeyWord = this.replaceSpecialCharacter(labelKeyWord);
        String useTimes = this.getDataBaseAdapter().getNvl("t22.USE_TIMES", "0");
        String dbType = Configure.getInstance().getProperty("CI_DBTYPE");
        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(dbType);
        String publishTime = dataBaseAdapter.getFullDate("t11.PUBLISH_TIME");
        String labelIdSqlStr = "";
        Object labelIdList = new ArrayList();
        if(topLabelId != null) {
            this.getLabelIds(topLabelId, (List)labelIdList);

            String date;
            while(((List)labelIdList).size() >= 1000) {
                date = ((List)labelIdList).subList(0, 1000).toString();
                date = date.substring(1, date.length() - 1);
                labelIdSqlStr = labelIdSqlStr + " e.LABEL_ID IN (" + date + ") OR";
                labelIdList = ((List)labelIdList).subList(1000, ((List)labelIdList).size());
            }

            date = labelIdList.toString();
            date = date.substring(1, date.length() - 1);
            labelIdSqlStr = labelIdSqlStr + " e.LABEL_ID IN (" + date + ")";
            labelIdSqlStr = "(" + labelIdSqlStr + ")";
        }

        Date var25 = new Date();
        String startTime = "";
        String endTime = "";
        String timeSql = "";
        String sql;
        String sceneIds;
        if(dataScope.equals("oneDay")) {
            sql = DateUtil.date2String(var25, "yyyy-MM-dd");
            sceneIds = DateUtil.getFrontDay(1, sql, "yyyy-MM-dd");
            startTime = this.getDataBaseAdapter().getTimeStamp(sceneIds, "00", "00", "00");
            endTime = this.getDataBaseAdapter().getTimeStamp(sql, "23", "59", "59");
            timeSql = timeSql + " and l.PUBLISH_TIME>=" + startTime + " AND l.PUBLISH_TIME<=" + endTime;
        }

        if(dataScope.equals("oneMonth")) {
            sql = DateUtil.date2String(var25, "yyyy-MM-dd");
            sceneIds = DateUtil.getFrontDay(30, sql, "yyyy-MM-dd");
            startTime = this.getDataBaseAdapter().getTimeStamp(sceneIds, "00", "00", "00");
            endTime = this.getDataBaseAdapter().getTimeStamp(sql, "23", "59", "59");
            timeSql = timeSql + " and l.PUBLISH_TIME>=" + startTime + " AND l.PUBLISH_TIME<=" + endTime;
        }

        if(dataScope.equals("threeMonth")) {
            sql = DateUtil.date2String(var25, "yyyy-MM-dd");
            sceneIds = DateUtil.getFrontDay(90, sql, "yyyy-MM-dd");
            startTime = this.getDataBaseAdapter().getTimeStamp(sceneIds, "00", "00", "00");
            endTime = this.getDataBaseAdapter().getTimeStamp(sql, "23", "59", "59");
            timeSql = timeSql + " and l.PUBLISH_TIME>=" + startTime + " AND l.PUBLISH_TIME<=" + endTime;
        }

        sql = "SELECT  t11.UPDATE_CYCLE,t11.LABEL_ID,t11.LABEL_NAME, t11.BUSI_CALIBER,t11.CUSTOM_NUM," + publishTime + " AS PUBLISH_TIME ,t11.DATA_DATE," + "t11.EFFEC_TIME, t11.MAX_VAL, t11.MIN_VAL, t11.LABEL_TYPE_ID, t11.ATTR_VAL, t11.PARENT_ID, t11.IS_SYS_RECOM,t11.CREATE_DESC," + useTimes + " AS USE_TIMES,t11.LABEL_ID_LEVEL_DESC,t11.AVG_SCORE";
        if(pager != null) {
            sql = sql + ",  CASE WHEN T3.SUMCC IS NULL THEN \'false\' ELSE \'true\' END ISATTENTION";
        }

        sql = sql + " FROM " + "(" + " SELECT " + "l.DATA_DATE, l.UPDATE_CYCLE,l.LABEL_ID,l.LABEL_NAME,l.BUSI_CALIBER,l.PUBLISH_TIME,l.EFFEC_TIME,l.IS_SYS_RECOM,l.CREATE_DESC,l.LABEL_ID_LEVEL_DESC," + "e.MAX_VAL, e.MIN_VAL, l.LABEL_TYPE_ID, l.PARENT_ID, e.CUSTOM_NUM, e.ATTR_VAL, l.AVG_SCORE " + " FROM" + " CI_LABEL_INFO l," + " CI_LABEL_EXT_INFO e" + " WHERE" + " (upper(l.label_name) like ? escape \'|\' " + " OR upper(l.create_desc) like ? escape \'|\' " + " OR upper(l.busi_caliber) like ? escape \'|\') " + " AND l.LABEL_ID=e.LABEL_ID AND e.IS_STAT_USER_NUM=1" + " AND l.DATA_STATUS_ID=2 ";
        if(StringUtil.isNotEmpty(labelIdSqlStr)) {
            sql = sql + " and " + labelIdSqlStr;
        }

        if(StringUtil.isNotEmpty(timeSql)) {
            sql = sql + timeSql;
        }

        sceneIds = searchBean.getSceneId();
        String sqlSceneIds = "";
        String[] sqlPag;
        String[] results;
        if(null != sceneIds && sceneIds.length() >= 1) {
            sceneIds = sceneIds.substring(0, sceneIds.length() - 1);
            sqlPag = sceneIds.split(",");
            results = sqlPag;
            int sf = sqlPag.length;

            for(int i$ = 0; i$ < sf; ++i$) {
                String sceneId = results[i$];
                sqlSceneIds = sqlSceneIds + " or senceT.scene_id=\'" + sceneId + "\' ";
            }
        }

        if(StringUtil.isNotEmpty(searchBean.getSceneId())) {
            sql = sql + " AND l.LABEL_ID IN  ( select  senceT.label_id from ci_label_scene_rel  senceT  where  senceT.scene_id = \'";
            sql = sql + ServiceConstants.ALL_SCENE_ID + "\'";
            sql = sql + sqlSceneIds;
            sql = sql + ")";
        }

        if(StringUtil.isNotEmpty(searchBean.getStartDate()) || StringUtil.isNotEmpty(searchBean.getEndDate())) {
            SimpleDateFormat var26 = new SimpleDateFormat("yyyy-MM-dd");
            String var28;
            String var29;
            if(StringUtil.isNotEmpty(searchBean.getStartDate())) {
                var28 = var26.format(searchBean.getStartDate()) + " 00:00:00";
                var29 = " AND l.PUBLISH_TIME > to_date(\'" + var28 + "\',\'yyyy-MM-dd HH24:mi:ss\')";
                sql = sql + var29;
            }

            if(StringUtil.isNotEmpty(searchBean.getEndDate())) {
                var28 = var26.format(searchBean.getEndDate()) + " 23:59:59";
                var29 = " AND l.PUBLISH_TIME <= to_date(\'" + var28 + "\',\'yyyy-MM-dd HH24:mi:ss\')";
                sql = sql + var29;
            }
        }

        if(searchBean.getUpdateCycle() != null && searchBean.getUpdateCycle().intValue() != 0) {
            sql = sql + " AND l.UPDATE_CYCLE = ";
            sql = sql + searchBean.getUpdateCycle();
        }

        String var27;
        if(StringUtil.isNotEmpty(searchBean.getLabelIdLevelDesc())) {
            var27 = " AND   l.LABEL_ID_LEVEL_DESC like \'%/" + searchBean.getLabelIdLevelDesc() + "/%\' ";
            sql = sql + var27;
        }

        sql = sql + ")  t11 LEFT JOIN( SELECT LABEL_ID, COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL GROUP BY LABEL_ID) t22 ON t11.LABEL_ID=t22.LABEL_ID";
        if(pager != null) {
            sql = sql + " LEFT JOIN ( SELECT T1.LABEL_ID, COUNT(T1.LABEL_ID) SUMCC FROM " + " CI_USER_ATTENTION_LABEL T1 WHERE T1.USER_ID = \'" + PrivilegeServiceUtil.getUserId() + "\' GROUP BY T1.LABEL_ID) T3" + " ON t11.LABEL_ID = T3.LABEL_ID ";
        }

        if(pager != null && StringUtil.isNotEmpty(pager.getOrderBy())) {
            sqlPag = pager.getOrderBy().split(",");
            results = pager.getOrder().split(",");
            StringBuffer var31 = new StringBuffer("");
            if(sqlPag.length != results.length) {
                throw new CIServiceException("排序字段和排序方式个数不匹配！");
            }

            if(sqlPag[0].equals("USE_TIMES")) {
                var31.append("IS_SYS_RECOM " + results[0] + ",");
                var31.append(sqlPag[0] + " " + results[0]);
            } else if(sqlPag[0].equals("PUBLISH_TIME")) {
                var31.append(sqlPag[0] + " " + results[0]);
            }

            sql = sql + " order by  " + var31.toString();
        }

        var27 = sql;
        if(pager != null) {
            var27 = this.getDataBaseAdapter().getPagedSql(sql, pager.getPageNum(), pager.getPageSize());
        }

        log.debug("sqlPag->" + var27);
        List var30 = this.getSimpleJdbcTemplate().query(var27, ParameterizedBeanPropertyRowMapper.newInstance(LabelDetailInfo.class), new Object[]{"%" + labelKeyWord + "%", "%" + labelKeyWord + "%", "%" + labelKeyWord + "%"});
        return var30;
    }

    public long getUserEffectiveLabelNum(String labelKeyWord, Integer topLabelId, String dataScope, CiLabelInfo searchBean) throws Exception {
        Object labelIdList = new ArrayList();
        String labelIdSqlStr = "";
        String sql;
        if(topLabelId != null) {
            this.getLabelIds(topLabelId, (List)labelIdList);

            while(((List)labelIdList).size() >= 1000) {
                sql = ((List)labelIdList).subList(0, 1000).toString();
                sql = sql.substring(1, sql.length() - 1);
                labelIdSqlStr = labelIdSqlStr + " e.LABEL_ID IN (" + sql + ") OR";
                labelIdList = ((List)labelIdList).subList(1000, ((List)labelIdList).size());
            }

            sql = labelIdList.toString();
            sql = sql.substring(1, sql.length() - 1);
            labelIdSqlStr = labelIdSqlStr + " e.LABEL_ID IN (" + sql + ")";
            labelIdSqlStr = "(" + labelIdSqlStr + ")";
        }

        if(StringUtil.isNotEmpty(labelKeyWord)) {
            labelKeyWord = labelKeyWord.toUpperCase();
        }

        labelKeyWord = this.replaceSpecialCharacter(labelKeyWord);
        sql = " SELECT COUNT(*) FROM CI_LABEL_INFO t,CI_LABEL_EXT_INFO e WHERE t.LABEL_ID=e.LABEL_ID  and e.IS_STAT_USER_NUM=1 and t.DATA_STATUS_ID = 2 and (upper(t.label_name) like ? escape \'|\'  OR upper(t.create_desc) like ? escape \'|\'  OR upper(t.busi_caliber) like ? escape \'|\') ";
        if(StringUtil.isNotEmpty(labelIdSqlStr)) {
            sql = sql + " and " + labelIdSqlStr;
        }

        Date date = new Date();
        String startTime = "";
        String endTime = "";
        String sceneIds;
        String sqlSceneIds;
        if(dataScope.equals("oneDay")) {
            sceneIds = DateUtil.date2String(date, "yyyy-MM-dd");
            sqlSceneIds = DateUtil.getFrontDay(1, sceneIds, "yyyy-MM-dd");
            startTime = this.getDataBaseAdapter().getTimeStamp(sqlSceneIds, "00", "00", "00");
            endTime = this.getDataBaseAdapter().getTimeStamp(sceneIds, "23", "59", "59");
            sql = sql + " and t.PUBLISH_TIME>=" + startTime + " AND t.PUBLISH_TIME<=" + endTime;
        }

        if(dataScope.equals("oneMonth")) {
            sceneIds = DateUtil.date2String(date, "yyyy-MM-dd");
            sqlSceneIds = DateUtil.getFrontDay(30, sceneIds, "yyyy-MM-dd");
            startTime = this.getDataBaseAdapter().getTimeStamp(sqlSceneIds, "00", "00", "00");
            endTime = this.getDataBaseAdapter().getTimeStamp(sceneIds, "23", "59", "59");
            sql = sql + " and t.PUBLISH_TIME>=" + startTime + " AND t.PUBLISH_TIME<=" + endTime;
        }

        if(dataScope.equals("threeMonth")) {
            sceneIds = DateUtil.date2String(date, "yyyy-MM-dd");
            sqlSceneIds = DateUtil.getFrontDay(90, sceneIds, "yyyy-MM-dd");
            startTime = this.getDataBaseAdapter().getTimeStamp(sqlSceneIds, "00", "00", "00");
            endTime = this.getDataBaseAdapter().getTimeStamp(sceneIds, "23", "59", "59");
            sql = sql + " and t.PUBLISH_TIME>=" + startTime + " AND t.PUBLISH_TIME<=" + endTime;
        }

        if(StringUtil.isNotEmpty(searchBean.getStartDate()) || StringUtil.isNotEmpty(searchBean.getEndDate())) {
            SimpleDateFormat var18 = new SimpleDateFormat("yyyy-MM-dd");
            String count;
            if(StringUtil.isNotEmpty(searchBean.getStartDate())) {
                sqlSceneIds = var18.format(searchBean.getStartDate()) + " 00:00:00";
                count = " AND t.PUBLISH_TIME > to_date(\'" + sqlSceneIds + "\',\'yyyy-MM-dd HH24:mi:ss\')";
                sql = sql + count;
            }

            if(StringUtil.isNotEmpty(searchBean.getEndDate())) {
                sqlSceneIds = var18.format(searchBean.getEndDate()) + " 23:59:59";
                count = " AND t.PUBLISH_TIME <= to_date(\'" + sqlSceneIds + "\',\'yyyy-MM-dd HH24:mi:ss\')";
                sql = sql + count;
            }
        }

        if(searchBean.getUpdateCycle() != null && searchBean.getUpdateCycle().intValue() != 0) {
            sql = sql + " AND t.UPDATE_CYCLE =";
            sql = sql + searchBean.getUpdateCycle();
        }

        if(StringUtil.isNotEmpty(searchBean.getLabelIdLevelDesc())) {
            sceneIds = " AND   t.LABEL_ID_LEVEL_DESC like \'%/" + searchBean.getLabelIdLevelDesc() + "/%\' ";
            sql = sql + sceneIds;
        }

        sceneIds = searchBean.getSceneId();
        sqlSceneIds = "";
        if(null != sceneIds && sceneIds.length() >= 1) {
            sceneIds = sceneIds.substring(0, sceneIds.length() - 1);
            String[] var19 = sceneIds.split(",");
            String[] arr$ = var19;
            int len$ = var19.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String sceneId = arr$[i$];
                sqlSceneIds = sqlSceneIds + " or senceT.scene_id=\'" + sceneId + "\' ";
            }
        }

        if(StringUtil.isNotEmpty(searchBean.getSceneId())) {
            sql = sql + " AND t.LABEL_ID IN  ( select  senceT.label_id from ci_label_scene_rel  senceT  where  senceT.scene_id = \'";
            sql = sql + ServiceConstants.ALL_SCENE_ID + "\'";
            sql = sql + sqlSceneIds;
            sql = sql + ")";
        }

        log.debug("sqlCount->" + sql);
        long var20 = this.getSimpleJdbcTemplate().queryForLong(sql, new Object[]{"%" + labelKeyWord + "%", "%" + labelKeyWord + "%", "%" + labelKeyWord + "%"});
        return var20;
    }

    private String replaceSpecialCharacter(String labelName) {
        if(StringUtil.isNotEmpty(labelName)) {
            labelName = labelName.trim();
            return labelName.replace("|", "||").replace("%", "|%").replace("_", "|_");
        } else {
            return "";
        }
    }

    private void getLabelIds(Integer parentId, List<String> labelIdList) {
        CacheBase cache = CacheBase.getInstance();
        CopyOnWriteArrayList labelIds = cache.getKeyList("ALL_EFFECTIVE_LABEL_MAP");
        Iterator i$ = labelIds.iterator();

        while(i$.hasNext()) {
            String idStr = (String)i$.next();
            CiLabelInfo ciLabelInfo = cache.getEffectiveLabel(idStr);
            if(ciLabelInfo.getParentId().compareTo(parentId) == 0) {
                labelIdList.add(idStr);
                this.getLabelIds(Integer.valueOf(idStr), labelIdList);
            }
        }

    }

    public List<LabelDetailInfo> getSysRecommendLabelInfoList(CiLabelInfo searchBean) throws Exception {
        String useTimes = this.getDataBaseAdapter().getNvl("t22.USE_TIMES", "0");
        String sql = "SELECT  t11.UPDATE_CYCLE,t11.LABEL_ID,t11.LABEL_NAME, t11.BUSI_CALIBER,t11.CUSTOM_NUM,t11.PUBLISH_TIME,t11.EFFEC_TIME, t11.MAX_VAL, t11.MIN_VAL, t11.LABEL_TYPE_ID, t11.ATTR_VAL, t11.PARENT_ID, t11.IS_SYS_RECOM," + useTimes + " AS USE_TIMES ,CASE WHEN T3.SUMCC IS NULL THEN \'false\' ELSE \'true\' END ISATTENTION  ";
        sql = sql + " FROM ( SELECT  l.UPDATE_CYCLE,l.LABEL_ID,l.LABEL_NAME,l.BUSI_CALIBER,l.PUBLISH_TIME,l.EFFEC_TIME,l.IS_SYS_RECOM,e.MAX_VAL, e.MIN_VAL, l.LABEL_TYPE_ID, l.PARENT_ID, e.CUSTOM_NUM,e.ATTR_VAL FROM CI_LABEL_INFO l, CI_LABEL_EXT_INFO e";
        if(searchBean.getIsAttention().equals("true")) {
            sql = sql + ", (SELECT LABEL_ID, ATTENTION_TIME FROM CI_USER_ATTENTION_LABEL WHERE  USER_ID=\'" + PrivilegeServiceUtil.getUserId() + "\' ) t1";
        }

        sql = sql + " WHERE l.LABEL_ID=e.LABEL_ID AND e.IS_STAT_USER_NUM=1";
        Date date = new Date();
        String today = DateUtil.date2String(date, "yyyy-MM-dd");
        String startDay = DateUtil.getFrontDay(90, today, "yyyy-MM-dd");
        String startTime = this.getDataBaseAdapter().getTimeStamp(startDay, "00", "00", "00");
        String endTime = this.getDataBaseAdapter().getTimeStamp(today, "23", "59", "59");
        sql = sql + " AND PUBLISH_TIME>=" + startTime + " AND PUBLISH_TIME<=" + endTime;
        if(searchBean.getIsAttention().equals("true")) {
            sql = sql + " AND l.LABEL_ID=t1.LABEL_ID";
        }

        sql = sql + " AND l.DATA_STATUS_ID=2 ";
        if(StringUtil.isNotEmpty(searchBean.getSceneId())) {
            sql = sql + " AND l.LABEL_ID IN  ( select  senceT.label_id from ci_label_scene_rel  senceT  where  senceT.scene_id = \'";
            sql = sql + searchBean.getSceneId();
            sql = sql + "\' or senceT.scene_id =\'";
            sql = sql + ServiceConstants.ALL_SCENE_ID;
            sql = sql + "\' )";
        }

        if(StringUtil.isNotEmpty(searchBean.getLabelId())) {
            sql = sql + " AND l.LABEL_ID=" + searchBean.getLabelId();
        }

        sql = sql + ")  t11 LEFT JOIN( SELECT LABEL_ID, COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL GROUP BY LABEL_ID) t22 ON t11.LABEL_ID=t22.LABEL_ID ";
        sql = sql + " LEFT JOIN ( SELECT t33.LABEL_ID, COUNT(t33.LABEL_ID) SUMCC FROM  CI_USER_ATTENTION_LABEL t33 WHERE t33.USER_ID = \'" + PrivilegeServiceUtil.getUserId() + "\' GROUP BY t33.LABEL_ID) T3" + " ON t11.LABEL_ID = T3.LABEL_ID ";
        sql = sql + " order by IS_SYS_RECOM desc, USE_TIMES desc ";
        String sqlPag = this.getDataBaseAdapter().getPagedSql(sql, 1, ServiceConstants.SHOW_SYS_RECOMMEND_LABEL);
        log.debug("sqlPag->" + sqlPag);
        List results = this.getSimpleJdbcTemplate().query(sqlPag, ParameterizedBeanPropertyRowMapper.newInstance(LabelDetailInfo.class), new BeanPropertySqlParameterSource(searchBean));
        return results;
    }

    public LabelDetailInfo getSysRecommendLabelInfo(Integer labelId) throws Exception {
        String useTimes = this.getDataBaseAdapter().getNvl("t22.USE_TIMES", "0");
        String sql = "SELECT  t11.UPDATE_CYCLE,t11.LABEL_ID,t11.LABEL_NAME, t11.BUSI_CALIBER,t11.CUSTOM_NUM,t11.PUBLISH_TIME,t11.EFFEC_TIME, t11.MAX_VAL, t11.MIN_VAL, t11.LABEL_TYPE_ID, t11.ATTR_VAL, t11.PARENT_ID, t11.IS_SYS_RECOM," + useTimes + " AS USE_TIMES ,CASE WHEN T3.SUMCC IS NULL THEN \'false\' ELSE \'true\' END ISATTENTION  ";
        sql = sql + " FROM ( SELECT  l.UPDATE_CYCLE,l.LABEL_ID,l.LABEL_NAME,l.BUSI_CALIBER,l.PUBLISH_TIME,l.EFFEC_TIME,l.IS_SYS_RECOM,e.MAX_VAL, e.MIN_VAL, l.LABEL_TYPE_ID, l.PARENT_ID, e.CUSTOM_NUM,e.ATTR_VAL FROM CI_LABEL_INFO l left join CI_LABEL_EXT_INFO e on l.label_id=e.label_id left join (SELECT LABEL_ID, ATTENTION_TIME FROM CI_USER_ATTENTION_LABEL WHERE  USER_ID=\'" + PrivilegeServiceUtil.getUserId() + "\' ) t1 on t1.label_id=e.label_id";
        sql = sql + " WHERE  e.IS_STAT_USER_NUM=1";
        sql = sql + " AND l.DATA_STATUS_ID=2 ";
        sql = sql + " AND l.LABEL_ID=" + labelId;
        sql = sql + ")  t11 LEFT JOIN( SELECT LABEL_ID, COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL GROUP BY LABEL_ID) t22 ON t11.LABEL_ID=t22.LABEL_ID ";
        sql = sql + " LEFT JOIN ( SELECT t33.LABEL_ID, COUNT(t33.LABEL_ID) SUMCC FROM  CI_USER_ATTENTION_LABEL t33 WHERE t33.USER_ID = \'" + PrivilegeServiceUtil.getUserId() + "\' GROUP BY t33.LABEL_ID) T3" + " ON t11.LABEL_ID = T3.LABEL_ID ";
        sql = sql + " order by IS_SYS_RECOM desc, USE_TIMES desc ";
        log.debug("sqlPag->" + sql);
        log.debug("labelId->" + labelId);
        LabelDetailInfo result = (LabelDetailInfo)this.getSimpleJdbcTemplate().queryForObject(sql, ParameterizedBeanPropertyRowMapper.newInstance(LabelDetailInfo.class), new Object[0]);
        return result;
    }

    public List<LabelDetailInfo> getEffectiveLabelByLabelId(Integer labelId) throws Exception {
        String useTimes = this.getDataBaseAdapter().getNvl("t22.USE_TIMES", "0");
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append(" t11.UPDATE_CYCLE,t11.LABEL_ID,t11.LABEL_NAME, t11.BUSI_CALIBER,t11.CUSTOM_NUM,t11.PUBLISH_TIME,t11.DATA_DATE,").append(" t11.EFFEC_TIME, t11.MAX_VAL, t11.MIN_VAL, t11.LABEL_TYPE_ID, t11.ATTR_VAL, t11.PARENT_ID, t11.IS_SYS_RECOM,t11.CREATE_DESC,").append(useTimes).append(" AS USE_TIMES ");
        sql.append(" FROM ( SELECT ").append(" l.DATA_DATE, l.UPDATE_CYCLE,l.LABEL_ID,l.LABEL_NAME,l.BUSI_CALIBER,l.PUBLISH_TIME,l.EFFEC_TIME,l.IS_SYS_RECOM,l.CREATE_DESC,").append(" e.MAX_VAL, e.MIN_VAL, l.LABEL_TYPE_ID, l.PARENT_ID, e.CUSTOM_NUM,e.ATTR_VAL").append(" FROM CI_LABEL_INFO l, CI_LABEL_EXT_INFO e WHERE l.LABEL_ID=e.LABEL_ID AND e.IS_STAT_USER_NUM=1");
        if(labelId != null) {
            sql.append(" AND  e.LABEL_ID = ").append(labelId);
        }

        sql.append(" )  t11 LEFT JOIN");
        sql.append(" ( SELECT LABEL_ID, COUNT(*) AS USE_TIMES FROM CI_USER_USE_LABEL GROUP BY LABEL_ID ) ");
        sql.append(" t22 ON t11.LABEL_ID=t22.LABEL_ID");
        String sqlPag = sql.toString();
        log.debug("sqlPag->" + sqlPag);
        List results = this.getSimpleJdbcTemplate().query(sqlPag, ParameterizedBeanPropertyRowMapper.newInstance(LabelDetailInfo.class), new Object[0]);
        return results;
    }
}
