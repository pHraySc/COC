package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiApproveHistoryJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiApproveHistory;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiApproveHistoryJDaoImpl extends JdbcBaseDao implements ICiApproveHistoryJDao {
    protected Logger log = Logger.getLogger(CiApproveHistoryJDaoImpl.class);

    public CiApproveHistoryJDaoImpl() {
    }

    public int getApprovedCountBySql(CiApproveHistory ciApproveHistory) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        CiApproveHistory ciApproveHistoryCopy = new CiApproveHistory();
        ciApproveHistoryCopy.setApproveUserId(ciApproveHistory.getUserId());
        if(StringUtil.isNotEmpty(ciApproveHistory.getResourceName())) {
            ciApproveHistoryCopy.setResourceName("%" + this.replaceSpecialCharacter(ciApproveHistory.getResourceName()).toLowerCase() + "%");
        }

        ciApproveHistoryCopy.setEndDate(ciApproveHistory.getEndDate());
        ciApproveHistoryCopy.setStartDate(ciApproveHistory.getStartDate());
        ciApproveHistoryCopy.setResourceTypeId(ciApproveHistory.getResourceTypeId());
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from ( ").append("select distinct resource_id from CI_APPROVE_HISTORY t,  dim_approve_resource_type b ").append("where t.resource_type_id=b.resource_type_id and ");
        if(StringUtil.isNotEmpty(ciApproveHistory.getUserId())) {
            sql.append("t.approve_user_id= :approveUserId ");
        }

        if(StringUtil.isNotEmpty(ciApproveHistory.getResourceName())) {
            sql.append("and LOWER(t.resource_name) like :resourceName  escape \'|\' ");
        }

        if(StringUtil.isNotEmpty(ciApproveHistory.getResourceTypeId())) {
            sql.append("and t.resource_type_id= :resourceTypeId ");
        }

        String endStr;
        if(StringUtil.isNotEmpty(ciApproveHistory.getStartDate())) {
            endStr = format.format(ciApproveHistory.getStartDate());
            sql.append(" and t.approve_time >= ").append(this.getDataBaseAdapter().getTimeStamp(endStr, "00", "00", "00"));
        }

        if(StringUtil.isNotEmpty(ciApproveHistory.getEndDate())) {
            endStr = format.format(ciApproveHistory.getEndDate());
            sql.append(" and t.approve_time <= ").append(this.getDataBaseAdapter().getTimeStamp(endStr, "23", "59", "59"));
        }

        sql.append(")");
        this.log.debug("已审批过的资源总记录数查询：" + sql);
        return this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new BeanPropertySqlParameterSource(ciApproveHistoryCopy));
    }

    public List<CiApproveHistory> getApprovedPageListBySql(int currPage, int pageSize, CiApproveHistory ciApproveHistory) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        CiApproveHistory ciApproveHistoryCopy = new CiApproveHistory();
        ciApproveHistoryCopy.setApproveUserId(ciApproveHistory.getUserId());
        if(StringUtil.isNotEmpty(ciApproveHistory.getResourceName())) {
            ciApproveHistoryCopy.setResourceName("%" + this.replaceSpecialCharacter(ciApproveHistory.getResourceName()).toLowerCase() + "%");
        }

        ciApproveHistoryCopy.setEndDate(ciApproveHistory.getEndDate());
        ciApproveHistoryCopy.setStartDate(ciApproveHistory.getStartDate());
        ciApproveHistoryCopy.setResourceTypeId(ciApproveHistory.getResourceTypeId());
        StringBuilder sql = new StringBuilder();
        sql.append("select a.resource_id, a.resource_name, a.resource_create_user_id, a.resource_type_id, b.resource_type_name, b.resource_detail_link, c.approve_time,  a.approve_result ").append("from ci_approve_history a left join dim_approve_resource_type b ").append("on ( a.resource_type_id = b.resource_type_id ) ").append("inner JOIN ( select resource_id , max(approve_time) approve_time from ci_approve_history ");
        if(StringUtil.isNotEmpty(ciApproveHistory.getUserId())) {
            sql.append("where approve_user_id= :approveUserId ");
        }

        if(StringUtil.isNotEmpty(ciApproveHistory.getResourceName())) {
            sql.append("and LOWER(resource_name) like :resourceName  escape \'|\' ");
        }

        if(StringUtil.isNotEmpty(ciApproveHistory.getResourceTypeId()) && !"-1".equals(ciApproveHistory.getResourceTypeId())) {
            sql.append("and resource_type_id= :resourceTypeId ");
        }

        String sqlPage;
        if(StringUtil.isNotEmpty(ciApproveHistory.getStartDate())) {
            sqlPage = format.format(ciApproveHistory.getStartDate());
            sql.append(" and approve_time >= ").append(this.getDataBaseAdapter().getTimeStamp(sqlPage, "00", "00", "00"));
        }

        if(StringUtil.isNotEmpty(ciApproveHistory.getEndDate())) {
            sqlPage = format.format(ciApproveHistory.getEndDate());
            sql.append(" and approve_time <= ").append(this.getDataBaseAdapter().getTimeStamp(sqlPage, "23", "59", "59"));
        }

        sql.append(" group by resource_id) c ").append("on (a.resource_id=c.resource_id and a.approve_time=c.approve_time) ").append(" order by a.approve_time desc ");
        this.log.debug("已审批过的资源分页查询：" + sql);
        sqlPage = this.getDataBaseAdapter().getPagedSql(sql.toString(), currPage, pageSize);
        List list = this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(CiApproveHistory.class), new BeanPropertySqlParameterSource(ciApproveHistoryCopy));
        return list;
    }

    private String replaceSpecialCharacter(String resourceName) {
        if(StringUtil.isNotEmpty(resourceName)) {
            resourceName = resourceName.trim();
            return resourceName.replace("|", "||").replace("%", "|%").replace("_", "|_");
        } else {
            return "";
        }
    }
}
