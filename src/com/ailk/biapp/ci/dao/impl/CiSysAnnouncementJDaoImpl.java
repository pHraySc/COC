package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiSysAnnouncementJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiSysAnnouncement;
import com.ailk.biapp.ci.util.CiUtil;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiSysAnnouncementJDaoImpl extends JdbcBaseDao implements ICiSysAnnouncementJDao {
    private Logger log = Logger.getLogger(CiSysAnnouncementJDaoImpl.class);

    public CiSysAnnouncementJDaoImpl() {
    }

    public int selectSysAnnouncementCount(CiSysAnnouncement sysAnnouncement) throws Exception {
        CiSysAnnouncement searchBean = (CiSysAnnouncement)sysAnnouncement.clone();
        String sql = "select count(*) " + this.getFromSql(searchBean);
        this.log.debug("count sql is : " + sql);
        int count = this.getSimpleJdbcTemplate().queryForInt(sql, new BeanPropertySqlParameterSource(searchBean));
        return count;
    }

    public List<CiSysAnnouncement> selectSysAnnouncementList(int currPage, int pageSize, CiSysAnnouncement sysAnnouncement) throws Exception {
        CiSysAnnouncement searchBean = (CiSysAnnouncement)sysAnnouncement.clone();
        String sql = "select * " + this.getFromSql(searchBean) + " order by PRIORITY_ID,RELEASE_DATE desc";
        this.log.debug("select sql is : " + sql);
        String sqlPage = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        List list = this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(CiSysAnnouncement.class), new BeanPropertySqlParameterSource(searchBean));
        return list;
    }

    private String getFromSql(CiSysAnnouncement searchBean) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("from CI_SYS_ANNOUNCEMENT where STATUS <> 0");
        String name = searchBean.getAnnouncementName() == null?"":searchBean.getAnnouncementName().trim();
        if(StringUtils.isNotEmpty(name)) {
            name = StringUtils.lowerCase(name);
            searchBean.setAnnouncementName("%" + CiUtil.escapeSQLLike(name) + "%");
            sql.append(" and LOWER(ANNOUNCEMENT_NAME) like :announcementName escape \'|\'");
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(null != searchBean.getTypeId()) {
            sql.append(" and TYPE_ID = :typeId");
        }

        if(null != searchBean.getPriorityId()) {
            sql.append(" and PRIORITY_ID = :priorityId");
        }

        if(null != searchBean.getStatus()) {
            sql.append(" and STATUS = :status");
        }

        String dbType = this.getDataBaseAdapter().getDbType();
        if(null != searchBean.getEffectiveTime()) {
            if(dbType.indexOf("ORACLE") >= 0) {
                sql.append(" and EFFECTIVE_TIME>=to_date(\'" + formatter.format(new Date(searchBean.getEffectiveTime().getTime())) + "\',\'yyyy-mm-dd hh24:mi:ss\')");
            } else if(dbType.indexOf("DB2") >= 0) {
                sql.append(" and EFFECTIVE_TIME>=:effectiveTime");
            }
        }

        if(null != searchBean.getReleaseDate()) {
            if(dbType.indexOf("ORACLE") >= 0) {
                sql.append(" and RELEASE_Date>=to_date(\'" + formatter.format(new Date(searchBean.getReleaseDate().getTime())) + "\',\'yyyy-mm-dd hh24:mi:ss\')");
            } else if(dbType.indexOf("DB2") >= 0) {
                sql.append(" and RELEASE_Date>=:releaseDate");
            }
        }

        return sql.toString();
    }

    public int selectSysAnnouncementCountByUserId(String userId, CiSysAnnouncement sysAnnouncement) throws Exception {
        CiSysAnnouncement searchBean = (CiSysAnnouncement)sysAnnouncement.clone();
        String sql = "select count(*) " + this.getUserAnnouncementFromSql(userId, searchBean);
        this.log.debug("count sql is : " + sql);
        int count = this.getSimpleJdbcTemplate().queryForInt(sql, new BeanPropertySqlParameterSource(searchBean));
        return count;
    }

    public List<CiSysAnnouncement> selectSysAnnouncementListByUserId(String userId, CiSysAnnouncement sysAnnouncement, int currPage, int pageSize) throws Exception {
        CiSysAnnouncement searchBean = (CiSysAnnouncement)sysAnnouncement.clone();
        String sql = "select * " + this.getUserAnnouncementFromSql(userId, searchBean) + " order by PRIORITY_ID,RELEASE_DATE desc";
        this.log.debug("select sql is : " + sql);
        String sqlPage = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        List list = this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(CiSysAnnouncement.class), new BeanPropertySqlParameterSource(searchBean));
        return list;
    }

    private String getUserAnnouncementFromSql(String userId, CiSysAnnouncement searchBean) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("  from  CI_SYS_ANNOUNCEMENT  a  ");
        sb.append(" where not exists ");
        sb.append(" (select  b.ANNOUNCEMENT_ID from CI_USER_READ_INFO b ");
        sb.append(" where a.ANNOUNCEMENT_ID = b.ANNOUNCEMENT_ID  and b.user_id =\'" + userId + "\')");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(null != searchBean.getTypeId()) {
            sb.append(" and a.TYPE_ID = :typeId");
        }

        if(null != searchBean.getPriorityId()) {
            sb.append(" and a.PRIORITY_ID = :priorityId");
        }

        if(null != searchBean.getStatus()) {
            sb.append(" and a.STATUS = :status");
        }

        String dbType = this.getDataBaseAdapter().getDbType();
        if(null != searchBean.getEffectiveTime()) {
            if(dbType.indexOf("ORACLE") >= 0) {
                sb.append(" and a.EFFECTIVE_TIME>=to_date(\'" + formatter.format(new Date(searchBean.getEffectiveTime().getTime())) + "\',\'yyyy-mm-dd hh24:mi:ss\')");
            } else if(dbType.indexOf("DB2") >= 0) {
                sb.append(" and a.EFFECTIVE_TIME>=:effectiveTime");
            }
        }

        if(null != searchBean.getReleaseDate()) {
            if(dbType.indexOf("ORACLE") >= 0) {
                sb.append(" and a.RELEASE_Date>=to_date(\'" + formatter.format(new Date(searchBean.getReleaseDate().getTime())) + "\',\'yyyy-mm-dd hh24:mi:ss\')");
            } else if(dbType.indexOf("DB2") >= 0) {
                sb.append(" and a.RELEASE_Date>=:releaseDate");
            }
        }

        return sb.toString();
    }

    public List<CiSysAnnouncement> selectCiUserReadInfoListByUserId(String userId, CiSysAnnouncement searchBean, int currPage, int pageSize) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append(" select b.STATUS deleteStatus,b.USER_ID userId,b.READ_TIME readTime, ");
        sb.append(" case when b.ANNOUNCEMENT_ID IS NULL then 0 else 1 end readStatus, ");
        sb.append(" a.*  from CI_SYS_ANNOUNCEMENT a ");
        sb.append(" LEFT JOIN CI_USER_READ_INFO b ON a.ANNOUNCEMENT_ID = b.ANNOUNCEMENT_ID ");
        if(null != searchBean.getStatus()) {
            sb.append(" AND ( b.STATUS = 1").append(" or b.STATUS = 0").append(" ) ");
        }

        sb.append(" AND b.USER_ID =\'" + userId + "\'");
        sb.append(" where 1=1 ");
        sb.append(" and( b.status is null or b.status != 0)");
        if(null != searchBean.getReadStatus()) {
            if(searchBean.getReadStatus().intValue() == 0) {
                sb.append(" AND ( b.status IS NULL or b.status =2  )");
            }

            if(searchBean.getReadStatus().intValue() == 1) {
                sb.append(" AND b.status =1");
            }
        }

        if(null == searchBean.getStatus()) {
            sb.append(" AND b.status IS   NULL");
        }

        sb.append(" order by a.RELEASE_DATE desc ");
        String sql = sb.toString();
        this.log.debug("sql=" + sql);
        this.log.debug("select sql is : " + sql);
        String sqlPage = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        List list = this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(CiSysAnnouncement.class), new Object[0]);
        return list;
    }

    public int selectCiUserReadInfoCountByUserId(String userId, CiSysAnnouncement searchBean) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append(" select count(1) ");
        sb.append("   from CI_SYS_ANNOUNCEMENT a ");
        sb.append(" LEFT JOIN CI_USER_READ_INFO b ON a.ANNOUNCEMENT_ID = b.ANNOUNCEMENT_ID ");
        sb.append(" AND b.USER_ID =\'" + userId + "\'");
        sb.append(" where 1=1 ");
        sb.append(" and( b.status is null or b.status != 0)");
        if(null != searchBean.getReadStatus()) {
            if(searchBean.getReadStatus().intValue() == 0) {
                sb.append(" AND ( b.status IS NULL or b.status =2  )");
            }

            if(searchBean.getReadStatus().intValue() == 1) {
                sb.append(" AND b.status =1");
            }
        }

        if(null == searchBean.getStatus()) {
            sb.append(" AND b.status IS   NULL");
        }

        String sql = sb.toString();
        this.log.debug("count sql is : " + sql);
        int count = this.getSimpleJdbcTemplate().queryForInt(sql, new Object[0]);
        return count;
    }

    public List<CiSysAnnouncement> selectCiUserNotReadInfoListByUserId(String userId) throws Exception {
        return null;
    }
}
