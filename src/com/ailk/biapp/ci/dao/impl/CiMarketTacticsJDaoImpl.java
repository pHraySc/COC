package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiMarketTacticsJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiMarketTactics;
import com.ailk.biapp.ci.util.DateUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiMarketTacticsJDaoImpl extends JdbcBaseDao implements ICiMarketTacticsJDao {
    public CiMarketTacticsJDaoImpl() {
    }

    public boolean isNameExit(String marketName) throws Exception {
        boolean nameExit = true;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(*) FROM CI_MARKET_TACTICS WHERE TACTIC_NAME = \'").append(marketName).append("\'");
        if(this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new Object[0]) > 0) {
            nameExit = false;
        }

        return nameExit;
    }

    public List<CiMarketTactics> selectCiMarketTacticsName(String keyWord, String classId, String userId) throws Exception {
        ArrayList marketTacticsList = new ArrayList();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT TACTIC_NAME").append(" FROM CI_MARKET_TACTICS").append(" WHERE LOWER(TACTIC_NAME) LIKE").append(" LOWER(\'%" + keyWord + "%\')");
        if(StringUtils.isNotBlank(userId)) {
            sql.append(" AND CREATE_USER_ID = \'").append(userId).append("\'");
        }

        String sqlPage = this.getDataBaseAdapter().getPagedSql(sql.toString(), 1, 9);
        List mapList = this.getSimpleJdbcTemplate().queryForList(sqlPage, new Object[0]);
        Iterator i$ = mapList.iterator();

        while(i$.hasNext()) {
            Map map = (Map)i$.next();
            CiMarketTactics marketTactics = new CiMarketTactics();
            marketTactics.setTacticName((String)map.get("TACTIC_NAME"));
            marketTacticsList.add(marketTactics);
        }

        return marketTacticsList;
    }

    public int selectCiMarketTacticsCount(CiMarketTactics marketTactics) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(*) FROM CI_MARKET_TACTICS WHERE 1=1");
        if(StringUtils.isNotBlank(marketTactics.getTacticName())) {
            sql.append(" AND LOWER(TACTIC_NAME) LIKE LOWER(\'%").append(marketTactics.getTacticName()).append("%\')");
        }

        if(StringUtils.isNotBlank(marketTactics.getCreateUserId())) {
            sql.append(" AND CREATE_USER_ID = \'").append(marketTactics.getCreateUserId()).append("\'");
        }

        if("1".equals(marketTactics.getDateType())) {
            String sdf = DateUtil.getCurrentDay();
            sql.append(" AND CREATE_TIME > \'").append(sdf).append("\'");
        }

        String startMonth;
        String startDay;
        SimpleDateFormat sdf1;
        if("2".equals(marketTactics.getDateType())) {
            sdf1 = new SimpleDateFormat("yyyyMM");
            startMonth = DateUtil.getFrontMonth(1, DateUtil.date2String(new Date(), "yyyyMM"));
            startDay = DateUtil.date2String(DateUtil.firstDayOfMonth(sdf1.parse(startMonth)), "yyyy-MM-dd HH:mm:ss");
            System.out.println(startDay);
            sql.append(" AND CREATE_TIME > \'").append(startDay).append("\'");
        }

        if("3".equals(marketTactics.getDateType())) {
            sdf1 = new SimpleDateFormat("yyyyMM");
            startMonth = DateUtil.getFrontMonth(3, DateUtil.date2String(new Date(), "yyyyMM"));
            startDay = DateUtil.date2String(DateUtil.firstDayOfMonth(sdf1.parse(startMonth)), "yyyy-MM-dd HH:mm:ss");
            sql.append(" AND CREATE_TIME > \'").append(startDay).append("\'");
        }

        return this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new BeanPropertySqlParameterSource(marketTactics));
    }

    public List<CiMarketTactics> selectCiMarketTacticsList(int pageNum, int pageSize, CiMarketTactics marketTactics) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM CI_MARKET_TACTICS WHERE 1=1");
        if(StringUtils.isNotBlank(marketTactics.getTacticName())) {
            sql.append(" AND LOWER(TACTIC_NAME) LIKE LOWER(\'%").append(marketTactics.getTacticName()).append("%\')");
        }

        if(StringUtils.isNotBlank(marketTactics.getCreateUserId())) {
            sql.append(" AND CREATE_USER_ID = \'").append(marketTactics.getCreateUserId()).append("\'");
        }

        String sqlPage;
        if("1".equals(marketTactics.getDateType())) {
            sqlPage = DateUtil.getCurrentDay();
            sql.append(" AND CREATE_TIME > \'").append(sqlPage).append("\'");
        }

        String startMonth;
        String startDay;
        SimpleDateFormat sqlPage1;
        if("2".equals(marketTactics.getDateType())) {
            sqlPage1 = new SimpleDateFormat("yyyyMM");
            startMonth = DateUtil.getFrontMonth(1, DateUtil.date2String(new Date(), "yyyyMM"));
            startDay = DateUtil.date2String(DateUtil.firstDayOfMonth(sqlPage1.parse(startMonth)), "yyyy-MM-dd HH:mm:ss");
            System.out.println(startDay);
            sql.append(" AND CREATE_TIME > \'").append(startDay).append("\'");
        }

        if("3".equals(marketTactics.getDateType())) {
            sqlPage1 = new SimpleDateFormat("yyyyMM");
            startMonth = DateUtil.getFrontMonth(3, DateUtil.date2String(new Date(), "yyyyMM"));
            startDay = DateUtil.date2String(DateUtil.firstDayOfMonth(sqlPage1.parse(startMonth)), "yyyy-MM-dd HH:mm:ss");
            sql.append(" AND CREATE_TIME > \'").append(startDay).append("\'");
        }

        sql.append(" ORDER BY CREATE_TIME DESC");
        sqlPage = this.getDataBaseAdapter().getPagedSql(sql.toString(), pageNum, pageSize);
        return this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(CiMarketTactics.class), new BeanPropertySqlParameterSource(marketTactics));
    }
}
