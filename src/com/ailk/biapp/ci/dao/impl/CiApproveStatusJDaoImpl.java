package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiApproveStatusJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiApproveStatus;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiApproveStatusJDaoImpl extends JdbcBaseDao implements ICiApproveStatusJDao {
    protected Logger log = Logger.getLogger(CiApproveStatusJDaoImpl.class);

    public CiApproveStatusJDaoImpl() {
    }

    public int getCountBySql(CiApproveStatus ciApproveStatus) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        CiApproveStatus ciApproveStatusCopy = new CiApproveStatus();
        ciApproveStatusCopy.setUserId(ciApproveStatus.getUserId());
        if(StringUtil.isNotEmpty(ciApproveStatus.getResourceName())) {
            ciApproveStatusCopy.setResourceName("%" + this.replaceSpecialCharacter(ciApproveStatus.getResourceName()).toLowerCase() + "%");
        }

        ciApproveStatusCopy.setEndDate(ciApproveStatus.getEndDate());
        ciApproveStatusCopy.setStartDate(ciApproveStatus.getStartDate());
        ciApproveStatusCopy.setResourceTypeId(ciApproveStatus.getResourceTypeId());
        String approveRoleIdsOfNeed = null;
        String approveRoleIdsOfNoNeed = null;
        Set needRoleIds = (Set)ciApproveStatus.getApproveRoleIdContainer().get("1");
        Set noneRoleIds = (Set)ciApproveStatus.getApproveRoleIdContainer().get("0");
        Iterator sql = needRoleIds.iterator();

        String endStr;
        while(sql.hasNext()) {
            endStr = (String)sql.next();
            if(StringUtil.isNotEmpty(approveRoleIdsOfNeed)) {
                approveRoleIdsOfNeed = approveRoleIdsOfNeed + "," + "\'" + endStr + "\'";
            } else {
                approveRoleIdsOfNeed = "\'" + endStr + "\'";
            }
        }

        sql = noneRoleIds.iterator();

        while(sql.hasNext()) {
            endStr = (String)sql.next();
            if(StringUtil.isNotEmpty(approveRoleIdsOfNoNeed)) {
                approveRoleIdsOfNoNeed = approveRoleIdsOfNoNeed + "," + "\'" + endStr + "\'";
            } else {
                approveRoleIdsOfNoNeed = "\'" + endStr + "\'";
            }
        }

        StringBuilder sql1 = new StringBuilder();
        sql1.append("select count(*) from (");
        if(StringUtil.isNotEmpty(approveRoleIdsOfNeed)) {
            sql1.append("select c.resource_id ").append(" from CI_APPROVE_USER_INFO a,DIM_APPROVE_STATUS b,CI_APPROVE_STATUS c,DIM_APPROVE_RESOURCE_TYPE d,DIM_APPROVE_LEVEL e ").append(" where b.APPROVE_STATUS_ID=c.CURR_APPROVE_STATUS_ID ").append(" and a.APPROVE_ROLE_ID=b.APPROVE_ROLE_ID ").append(" and a.DEPT_ID=c.DEPT_ID ").append(" and c.resource_type_id = d.resource_type_id ").append(" and b.sort_num = 1 ").append(" and a.approve_role_id in (").append(approveRoleIdsOfNeed).append(") ").append("and a.approve_role_id = e.approve_role_id ").append("and e.approve_role_type = 1 ");
            if(StringUtil.isNotEmpty(ciApproveStatus.getUserId())) {
                sql1.append(" and a.approve_user_id= :userId ");
            }

            if(StringUtil.isNotEmpty(ciApproveStatus.getResourceName())) {
                sql1.append(" and LOWER(c.resource_name) like :resourceName  escape \'|\' ");
            }

            if(StringUtil.isNotEmpty(ciApproveStatus.getResourceTypeId()) && !"-1".equals(ciApproveStatus.getResourceTypeId())) {
                sql1.append(" and c.resource_type_id= :resourceTypeId ");
            }

            if(StringUtil.isNotEmpty(ciApproveStatus.getStartDate())) {
                endStr = format.format(ciApproveStatus.getStartDate());
                sql1.append(" and c.last_approve_time >= ").append(this.getDataBaseAdapter().getTimeStamp(endStr, "00", "00", "00"));
            }

            if(StringUtil.isNotEmpty(ciApproveStatus.getEndDate())) {
                endStr = format.format(ciApproveStatus.getEndDate());
                sql1.append(" and c.last_approve_time <= ").append(this.getDataBaseAdapter().getTimeStamp(endStr, "23", "59", "59"));
            }
        }

        if(StringUtil.isNotEmpty(approveRoleIdsOfNoNeed)) {
            if(StringUtil.isNotEmpty(approveRoleIdsOfNeed)) {
                sql1.append("union ");
            }

            sql1.append(" select c.resource_id ").append(" from CI_APPROVE_USER_INFO a,DIM_APPROVE_STATUS b,CI_APPROVE_STATUS c,DIM_APPROVE_RESOURCE_TYPE d,DIM_APPROVE_LEVEL e  ").append(" where b.APPROVE_STATUS_ID=c.CURR_APPROVE_STATUS_ID ").append(" and a.APPROVE_ROLE_ID=b.APPROVE_ROLE_ID ").append(" and c.resource_type_id = d.resource_type_id ").append(" and b.sort_num = 1 ").append(" and a.approve_role_id in (").append(approveRoleIdsOfNoNeed).append(") ").append("and a.approve_role_id = e.approve_role_id ").append("and e.approve_role_type = 1 ");
            if(StringUtil.isNotEmpty(ciApproveStatus.getUserId())) {
                sql1.append(" and a.approve_user_id= :userId ");
            }

            if(StringUtil.isNotEmpty(ciApproveStatus.getResourceName())) {
                sql1.append(" and LOWER(c.resource_name) like :resourceName  escape \'|\' ");
            }

            if(StringUtil.isNotEmpty(ciApproveStatus.getResourceTypeId()) && !"-1".equals(ciApproveStatus.getResourceTypeId())) {
                sql1.append(" and c.resource_type_id= :resourceTypeId ");
            }

            if(StringUtil.isNotEmpty(ciApproveStatus.getStartDate())) {
                endStr = format.format(ciApproveStatus.getStartDate());
                sql1.append(" and c.last_approve_time >= ").append(this.getDataBaseAdapter().getTimeStamp(endStr, "00", "00", "00"));
            }

            if(StringUtil.isNotEmpty(ciApproveStatus.getEndDate())) {
                endStr = format.format(ciApproveStatus.getEndDate());
                sql1.append(" and c.last_approve_time <= ").append(this.getDataBaseAdapter().getTimeStamp(endStr, "23", "59", "59"));
            }
        }

        sql1.append(")");
        this.log.debug("待审批的资源总记录数查询：" + sql1);
        return this.getSimpleJdbcTemplate().queryForInt(sql1.toString(), new BeanPropertySqlParameterSource(ciApproveStatusCopy));
    }

    public List<CiApproveStatus> getPageListBySql(int currPage, int pageSize, CiApproveStatus ciApproveStatus) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        CiApproveStatus ciApproveStatusCopy = new CiApproveStatus();
        ciApproveStatusCopy.setUserId(ciApproveStatus.getUserId());
        if(StringUtil.isNotEmpty(ciApproveStatus.getResourceName())) {
            ciApproveStatusCopy.setResourceName("%" + this.replaceSpecialCharacter(ciApproveStatus.getResourceName()).toLowerCase() + "%");
        }

        ciApproveStatusCopy.setEndDate(ciApproveStatus.getEndDate());
        ciApproveStatusCopy.setStartDate(ciApproveStatus.getStartDate());
        ciApproveStatusCopy.setResourceTypeId(ciApproveStatus.getResourceTypeId());
        String approveRoleIdsOfNeed = null;
        String approveRoleIdsOfNoNeed = null;
        Set needRoleIds = (Set)ciApproveStatus.getApproveRoleIdContainer().get("1");
        Set noneRoleIds = (Set)ciApproveStatus.getApproveRoleIdContainer().get("0");
        Iterator sql = needRoleIds.iterator();

        String sqlPage;
        while(sql.hasNext()) {
            sqlPage = (String)sql.next();
            if(StringUtil.isNotEmpty(approveRoleIdsOfNeed)) {
                approveRoleIdsOfNeed = approveRoleIdsOfNeed + "," + "\'" + sqlPage + "\'";
            } else {
                approveRoleIdsOfNeed = "\'" + sqlPage + "\'";
            }
        }

        sql = noneRoleIds.iterator();

        while(sql.hasNext()) {
            sqlPage = (String)sql.next();
            if(StringUtil.isNotEmpty(approveRoleIdsOfNoNeed)) {
                approveRoleIdsOfNoNeed = approveRoleIdsOfNoNeed + "," + "\'" + sqlPage + "\'";
            } else {
                approveRoleIdsOfNoNeed = "\'" + sqlPage + "\'";
            }
        }

        StringBuilder sql1 = new StringBuilder();
        if(StringUtil.isNotEmpty(approveRoleIdsOfNeed)) {
            sql1.append("select c.status_id, c.resource_create_user_id, c.resource_id, c.resource_name, c.resource_type_id, d.resource_type_name, d.resource_detail_link, c.last_approve_user_id, c.last_approve_time, c.last_approve_opinion, b.approve_status_name, b.sort_num ").append("from CI_APPROVE_USER_INFO a,DIM_APPROVE_STATUS b,CI_APPROVE_STATUS c,DIM_APPROVE_RESOURCE_TYPE d, DIM_APPROVE_LEVEL e ").append("where b.APPROVE_STATUS_ID=c.CURR_APPROVE_STATUS_ID ").append("and a.APPROVE_ROLE_ID=b.APPROVE_ROLE_ID ").append("and a.DEPT_ID=c.DEPT_ID ").append("and c.resource_type_id = d.resource_type_id ").append(" and a.approve_role_id in (").append(approveRoleIdsOfNeed).append(") ").append("and b.sort_num = 1 ").append("and a.approve_role_id = e.approve_role_id ").append("and e.approve_role_type = 1 ");
            if(StringUtil.isNotEmpty(ciApproveStatusCopy.getUserId())) {
                sql1.append("and a.approve_user_id= :userId ");
            }

            if(StringUtil.isNotEmpty(ciApproveStatusCopy.getResourceName())) {
                sql1.append("and LOWER(c.resource_name) like :resourceName  escape \'|\' ");
            }

            if(StringUtil.isNotEmpty(ciApproveStatusCopy.getResourceTypeId()) && !"-1".equals(ciApproveStatusCopy.getResourceTypeId())) {
                sql1.append("and c.resource_type_id= :resourceTypeId ");
            }

            if(StringUtil.isNotEmpty(ciApproveStatusCopy.getStartDate())) {
                sqlPage = format.format(ciApproveStatusCopy.getStartDate());
                sql1.append(" and c.last_approve_time >= ").append(this.getDataBaseAdapter().getTimeStamp(sqlPage, "00", "00", "00"));
            }

            if(StringUtil.isNotEmpty(ciApproveStatusCopy.getEndDate())) {
                sqlPage = format.format(ciApproveStatusCopy.getEndDate());
                sql1.append(" and c.last_approve_time <= ").append(this.getDataBaseAdapter().getTimeStamp(sqlPage, "23", "59", "59"));
            }
        }

        if(StringUtil.isNotEmpty(approveRoleIdsOfNoNeed)) {
            if(StringUtil.isNotEmpty(approveRoleIdsOfNeed)) {
                sql1.append("union ");
            }

            sql1.append("select c.status_id, c.resource_create_user_id, c.resource_id, c.resource_name, c.resource_type_id, d.resource_type_name, d.resource_detail_link, c.last_approve_user_id, c.last_approve_time, c.last_approve_opinion, b.approve_status_name, b.sort_num ").append("from CI_APPROVE_USER_INFO a,DIM_APPROVE_STATUS b,CI_APPROVE_STATUS c,DIM_APPROVE_RESOURCE_TYPE d,DIM_APPROVE_LEVEL e ").append("where b.APPROVE_STATUS_ID=c.CURR_APPROVE_STATUS_ID ").append("and a.APPROVE_ROLE_ID=b.APPROVE_ROLE_ID ").append("and c.resource_type_id = d.resource_type_id ").append(" and a.approve_role_id in (").append(approveRoleIdsOfNoNeed).append(") ").append("and b.sort_num = 1 ").append("and a.approve_role_id = e.approve_role_id ").append("and e.approve_role_type = 1 ");
            if(StringUtil.isNotEmpty(ciApproveStatusCopy.getUserId())) {
                sql1.append("and a.approve_user_id= :userId ");
            }

            if(StringUtil.isNotEmpty(ciApproveStatusCopy.getResourceName())) {
                sql1.append("and LOWER(c.resource_name) like :resourceName  escape \'|\' ");
            }

            if(StringUtil.isNotEmpty(ciApproveStatusCopy.getResourceTypeId()) && !"-1".equals(ciApproveStatusCopy.getResourceTypeId())) {
                sql1.append("and c.resource_type_id= :resourceTypeId ");
            }

            if(StringUtil.isNotEmpty(ciApproveStatusCopy.getStartDate())) {
                sqlPage = format.format(ciApproveStatusCopy.getStartDate());
                sql1.append(" and c.last_approve_time >= ").append(this.getDataBaseAdapter().getTimeStamp(sqlPage, "00", "00", "00"));
            }

            if(StringUtil.isNotEmpty(ciApproveStatusCopy.getEndDate())) {
                sqlPage = format.format(ciApproveStatusCopy.getEndDate());
                sql1.append(" and c.last_approve_time <= ").append(this.getDataBaseAdapter().getTimeStamp(sqlPage, "23", "59", "59"));
            }
        }

        sql1.append("order by last_approve_time desc ");
        this.log.debug("待审批的资源分页记录查询：" + sql1);
        sqlPage = this.getDataBaseAdapter().getPagedSql(sql1.toString(), currPage, pageSize);
        List list = this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(CiApproveStatus.class), new BeanPropertySqlParameterSource(ciApproveStatusCopy));
        return list;
    }

    private String replaceSpecialCharacter(String labelName) {
        if(StringUtil.isNotEmpty(labelName)) {
            labelName = labelName.trim();
            return labelName.replace("|", "||").replace("%", "|%").replace("_", "|_");
        } else {
            return "";
        }
    }
}
