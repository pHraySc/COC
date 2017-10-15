package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiUserUseCustomJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.asiainfo.biframe.utils.date.DateUtil;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository("ciUserUseCustomJDao")
public class CiUserUseCustomJDaoImpl extends JdbcBaseDao implements ICiUserUseCustomJDao {
    private Logger log = Logger.getLogger(this.getClass());

    public CiUserUseCustomJDaoImpl() {
    }

    public Long getCustomUseTimesById(String customId, int isTemplate) {
        String sql = " SELECT COUNT(1) FROM CI_USER_USE_CUSTOM CUUC WHERE CUUC.CUSTOM_ID = :customId and IS_TEMPLATE = :isTemplate ";
        HashMap map = new HashMap();
        map.put("customId", customId);
        map.put("isTemplate", Integer.valueOf(isTemplate));
        long count = this.getSimpleJdbcTemplate().queryForLong(sql, map);
        return Long.valueOf(count);
    }

    public void insertCiCustomUseStat(String dataDate) {
        String startTime = "";

        try {
            Date endTime = (new SimpleDateFormat("yyyyMMdd")).parse(dataDate + "01");
            startTime = (new SimpleDateFormat("yyyy-MM-dd 00:00:00")).format(endTime);
        } catch (ParseException var6) {
            this.log.error("按月统计客户群使用次数时日期格式转换错误", var6);
            throw new RuntimeException(var6);
        }

        String endTime1 = DateUtil.getLastDateOfMonth(dataDate + "01") + " 23:59:59";
        StringBuffer sql = new StringBuffer();
        sql.append(" insert into CI_CUSTOM_USE_STAT(CUSTOM_ID,USE_TYPE_ID,DATA_DATE,USE_TIMES) ");
        sql.append(" select CUSTOM_ID,USE_TYPE_ID," + dataDate + ",count(CUSTOM_ID) from CI_USER_USE_CUSTOM ");
        sql.append(" where USE_TIME >= :startTime and  USE_TIME <= :endTime and IS_TEMPLATE=0 ");
        sql.append(" group by CUSTOM_ID,USE_TYPE_ID ");
        this.log.debug(sql.toString());
        HashMap map = new HashMap();
        map.put("startTime", Timestamp.valueOf(startTime));
        map.put("endTime", Timestamp.valueOf(endTime1));
        this.getSimpleJdbcTemplate().update(sql.toString(), map);
    }

    public void deleteCiCustomUseStat(String dataDate) {
        String sql = " delete from CI_CUSTOM_USE_STAT ccus where ccus.DATA_DATE = :dataDate ";
        HashMap map = new HashMap();
        map.put("dataDate", dataDate);
        this.getSimpleJdbcTemplate().update(sql, map);
    }
}
