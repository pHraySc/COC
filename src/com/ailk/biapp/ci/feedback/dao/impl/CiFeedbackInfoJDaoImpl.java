package com.ailk.biapp.ci.feedback.dao.impl;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.feedback.dao.ICiFeedbackInfoJDao;
import com.ailk.biapp.ci.feedback.entity.CiFeedbackInfo;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiFeedbackInfoJDaoImpl extends JdbcBaseDao implements ICiFeedbackInfoJDao {
    protected Logger log = Logger.getLogger(CiFeedbackInfoJDaoImpl.class);

    public CiFeedbackInfoJDaoImpl() {
    }

    public int getFeedbackCountBySql(CiFeedbackInfo ciFeedbackInfo) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        CiFeedbackInfo clone = new CiFeedbackInfo();
        clone.setFeedbackInfoId(ciFeedbackInfo.getFeedbackInfoId());
        clone.setUserId(ciFeedbackInfo.getUserId());
        clone.setLabelCustomId(ciFeedbackInfo.getLabelCustomId());
        clone.setFeedbackTime(ciFeedbackInfo.getFeedbackTime());
        clone.setCanManageDeclare(ciFeedbackInfo.getCanManageDeclare());
        clone.setFeedbackType(ciFeedbackInfo.getFeedbackType());
        clone.setStartDate(ciFeedbackInfo.getStartDate());
        clone.setEndDate(ciFeedbackInfo.getEndDate());
        if(StringUtil.isNotEmpty(ciFeedbackInfo.getFeedbackTitle())) {
            clone.setFeedbackTitle("%" + this.replaceSpecialCharacter(ciFeedbackInfo.getFeedbackTitle()).toLowerCase() + "%");
        }

        clone.setFeedbackInfo(ciFeedbackInfo.getFeedbackInfo());
        clone.setReplyUserId(ciFeedbackInfo.getReplyUserId());
        clone.setDealStatusId(ciFeedbackInfo.getDealStatusId());
        clone.setStatus(ciFeedbackInfo.getStatus());
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from CI_FEEDBACK_INFO a where a.deal_status_id <> 4 and a.deal_status_id <> 5 and a.status = 1 ");
        if(!ciFeedbackInfo.getCanManageDeclare()) {
            if(StringUtil.isNotEmpty(ciFeedbackInfo.getUserId())) {
                if(ServiceConstants.FEEDBACK_SIGN_CREDATE == ciFeedbackInfo.getSign()) {
                    sql.append(" and a.user_id= :userId ");
                } else if(ServiceConstants.FEEDBACK_SIGN_DEAL == ciFeedbackInfo.getSign()) {
                    sql.append(" and a.reply_user_id= :userId ");
                } else {
                    sql.append(" and (a.user_id= :userId or a.reply_user_id= :userId) ");
                }
            }
        } else {
            sql.append(" and a.reply_user_id= \'" + Configure.getInstance().getProperty("ADMIN_ID") + "\' ");
        }

        if(StringUtil.isNotEmpty(ciFeedbackInfo.getFeedbackType())) {
            sql.append(" and a.feedback_type= :feedbackType ");
        }

        if(StringUtil.isNotEmpty(ciFeedbackInfo.getFeedbackTitle())) {
            sql.append(" and LOWER(a.feedback_title) like :feedbackTitle  escape \'|\' ");
        }

        if(StringUtil.isNotEmpty(ciFeedbackInfo.getDealStatusId())) {
            sql.append("and a.deal_status_id= :dealStatusId ");
        }

        String endStr;
        if(StringUtil.isNotEmpty(ciFeedbackInfo.getStartDate())) {
            endStr = format.format(ciFeedbackInfo.getStartDate());
            sql.append(" and a.feedback_time >= ").append(this.getDataBaseAdapter().getTimeStamp(endStr, "00", "00", "00"));
        }

        if(StringUtil.isNotEmpty(ciFeedbackInfo.getEndDate())) {
            endStr = format.format(ciFeedbackInfo.getEndDate());
            sql.append(" and a.feedback_time <= ").append(this.getDataBaseAdapter().getTimeStamp(endStr, "23", "59", "59"));
        }

        this.log.debug("反馈信息总数量查询：" + sql);
        return this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new BeanPropertySqlParameterSource(clone));
    }

    public List<CiFeedbackInfo> getFeedbackPageListBySql(int currPage, int pageSize, CiFeedbackInfo ciFeedbackInfo) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        CiFeedbackInfo clone = new CiFeedbackInfo();
        clone.setFeedbackInfoId(ciFeedbackInfo.getFeedbackInfoId());
        clone.setUserId(ciFeedbackInfo.getUserId());
        clone.setLabelCustomId(ciFeedbackInfo.getLabelCustomId());
        clone.setFeedbackTime(ciFeedbackInfo.getFeedbackTime());
        clone.setFeedbackType(ciFeedbackInfo.getFeedbackType());
        clone.setStartDate(ciFeedbackInfo.getStartDate());
        clone.setEndDate(ciFeedbackInfo.getEndDate());
        if(StringUtil.isNotEmpty(ciFeedbackInfo.getFeedbackTitle())) {
            clone.setFeedbackTitle("%" + this.replaceSpecialCharacter(ciFeedbackInfo.getFeedbackTitle()).toLowerCase() + "%");
        }

        clone.setFeedbackInfo(ciFeedbackInfo.getFeedbackInfo());
        clone.setReplyUserId(ciFeedbackInfo.getReplyUserId());
        clone.setDealStatusId(ciFeedbackInfo.getDealStatusId());
        clone.setStatus(ciFeedbackInfo.getStatus());
        StringBuilder sql = new StringBuilder();
        sql.append("select a.feedback_info_id,a.user_id,a.reply_user_id,a.feedback_time,a.feedback_type,a.feedback_title,a.feedback_info,b.deal_status_id, ").append("c.label_name,d.custom_group_name ").append("from CI_FEEDBACK_INFO a inner join DIM_DECLARE_DEAL_STATUS b on a.deal_status_id=b.deal_status_id ").append(" left join CI_LABEL_INFO c on a.label_custom_id= ").append(this.getDataBaseAdapter().getIntToChar("c.label_id ")).append("and a.feedback_type=" + ServiceConstants.FEEDBACK_TYPE_LABEL).append(" left join ci_custom_group_info d on a.label_custom_id=d.custom_group_id and a.feedback_type=" + ServiceConstants.FEEDBACK_TYPE_CUSTOM).append(" where a.deal_status_id <> 4 and a.deal_status_id <> 5 ").append(" and a.status = 1 ");
        if(!ciFeedbackInfo.getCanManageDeclare()) {
            if(StringUtil.isNotEmpty(ciFeedbackInfo.getUserId())) {
                if(ServiceConstants.FEEDBACK_SIGN_CREDATE == ciFeedbackInfo.getSign()) {
                    sql.append(" and a.user_id= :userId ");
                } else if(ServiceConstants.FEEDBACK_SIGN_DEAL == ciFeedbackInfo.getSign()) {
                    sql.append(" and a.reply_user_id= :userId ");
                } else {
                    sql.append(" and (a.user_id= :userId or a.reply_user_id= :userId) ");
                }
            }
        } else {
            sql.append(" and a.reply_user_id= \'" + Configure.getInstance().getProperty("ADMIN_ID") + "\' ");
        }

        if(StringUtil.isNotEmpty(ciFeedbackInfo.getFeedbackType())) {
            sql.append("and a.feedback_type= :feedbackType ");
        }

        if(StringUtil.isNotEmpty(ciFeedbackInfo.getFeedbackTitle())) {
            sql.append(" and LOWER(a.feedback_title) like :feedbackTitle  escape \'|\' ");
        }

        if(StringUtil.isNotEmpty(ciFeedbackInfo.getDealStatusId())) {
            sql.append("and a.deal_status_id= :dealStatusId ");
        }

        String sqlPage;
        if(StringUtil.isNotEmpty(ciFeedbackInfo.getStartDate())) {
            sqlPage = format.format(ciFeedbackInfo.getStartDate());
            sql.append(" and a.feedback_time >= ").append(this.getDataBaseAdapter().getTimeStamp(sqlPage, "00", "00", "00"));
        }

        if(StringUtil.isNotEmpty(ciFeedbackInfo.getEndDate())) {
            sqlPage = format.format(ciFeedbackInfo.getEndDate());
            sql.append(" and a.feedback_time <= ").append(this.getDataBaseAdapter().getTimeStamp(sqlPage, "23", "59", "59"));
        }

        sql.append("order by a.feedback_time desc");
        this.log.debug("反馈信息总数量查询：" + sql);
        sqlPage = this.getDataBaseAdapter().getPagedSql(sql.toString(), currPage, pageSize);
        List list = this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(CiFeedbackInfo.class), new BeanPropertySqlParameterSource(clone));
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

    public int getFeedbackHistoryCountBySql(CiFeedbackInfo ciFeedbackInfo) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        CiFeedbackInfo clone = new CiFeedbackInfo();
        clone.setFeedbackInfoId(ciFeedbackInfo.getFeedbackInfoId());
        clone.setUserId(ciFeedbackInfo.getUserId());
        clone.setLabelCustomId(ciFeedbackInfo.getLabelCustomId());
        clone.setFeedbackTime(ciFeedbackInfo.getFeedbackTime());
        clone.setCanManageDeclare(ciFeedbackInfo.getCanManageDeclare());
        clone.setFeedbackType(ciFeedbackInfo.getFeedbackType());
        clone.setStartDate(ciFeedbackInfo.getStartDate());
        clone.setEndDate(ciFeedbackInfo.getEndDate());
        if(StringUtil.isNotEmpty(ciFeedbackInfo.getFeedbackTitle())) {
            clone.setFeedbackTitle("%" + this.replaceSpecialCharacter(ciFeedbackInfo.getFeedbackTitle()).toLowerCase() + "%");
        }

        clone.setFeedbackInfo(ciFeedbackInfo.getFeedbackInfo());
        clone.setReplyUserId(ciFeedbackInfo.getReplyUserId());
        clone.setDealStatusId(ciFeedbackInfo.getDealStatusId());
        clone.setStatus(ciFeedbackInfo.getStatus());
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from CI_FEEDBACK_INFO a where a.status = 1 and (a.deal_status_id=4 or a.deal_status_id=5)  ");
        if(!ciFeedbackInfo.getCanManageDeclare()) {
            if(StringUtil.isNotEmpty(ciFeedbackInfo.getUserId())) {
                if(ServiceConstants.FEEDBACK_SIGN_CREDATE == ciFeedbackInfo.getSign()) {
                    sql.append(" and a.user_id= :userId ");
                } else if(ServiceConstants.FEEDBACK_SIGN_DEAL == ciFeedbackInfo.getSign()) {
                    sql.append(" and a.reply_user_id= :userId ");
                } else {
                    sql.append(" and (a.user_id= :userId or a.reply_user_id= :userId) ");
                }
            }
        } else {
            sql.append(" and a.reply_user_id= \'" + Configure.getInstance().getProperty("ADMIN_ID") + "\' ");
        }

        if(StringUtil.isNotEmpty(ciFeedbackInfo.getFeedbackType())) {
            sql.append("and a.feedback_type= :feedbackType ");
        }

        if(StringUtil.isNotEmpty(ciFeedbackInfo.getFeedbackTitle())) {
            sql.append(" and LOWER(a.feedback_title) like :feedbackTitle  escape \'|\' ");
        }

        if(StringUtil.isNotEmpty(ciFeedbackInfo.getDealStatusId())) {
            sql.append("and a.deal_status_id= :dealStatusId ");
        }

        String endStr;
        if(StringUtil.isNotEmpty(ciFeedbackInfo.getStartDate())) {
            endStr = format.format(ciFeedbackInfo.getStartDate());
            sql.append(" and a.feedback_time >= ").append(this.getDataBaseAdapter().getTimeStamp(endStr, "00", "00", "00"));
        }

        if(StringUtil.isNotEmpty(ciFeedbackInfo.getEndDate())) {
            endStr = format.format(ciFeedbackInfo.getEndDate());
            sql.append(" and a.feedback_time <= ").append(this.getDataBaseAdapter().getTimeStamp(endStr, "23", "59", "59"));
        }

        this.log.debug("历史反馈信息总数量查询：" + sql);
        return this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new BeanPropertySqlParameterSource(clone));
    }

    public List<CiFeedbackInfo> getFeedbackHistorytListBySql(int currPage, int pageSize, CiFeedbackInfo ciFeedbackInfo) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        CiFeedbackInfo clone = new CiFeedbackInfo();
        clone.setFeedbackInfoId(ciFeedbackInfo.getFeedbackInfoId());
        clone.setUserId(ciFeedbackInfo.getUserId());
        clone.setLabelCustomId(ciFeedbackInfo.getLabelCustomId());
        clone.setFeedbackTime(ciFeedbackInfo.getFeedbackTime());
        clone.setFeedbackType(ciFeedbackInfo.getFeedbackType());
        clone.setStartDate(ciFeedbackInfo.getStartDate());
        clone.setEndDate(ciFeedbackInfo.getEndDate());
        if(StringUtil.isNotEmpty(ciFeedbackInfo.getFeedbackTitle())) {
            clone.setFeedbackTitle("%" + this.replaceSpecialCharacter(ciFeedbackInfo.getFeedbackTitle()).toLowerCase() + "%");
        }

        clone.setFeedbackInfo(ciFeedbackInfo.getFeedbackInfo());
        clone.setReplyUserId(ciFeedbackInfo.getReplyUserId());
        clone.setDealStatusId(ciFeedbackInfo.getDealStatusId());
        clone.setStatus(ciFeedbackInfo.getStatus());
        StringBuilder sql = new StringBuilder();
        sql.append("select a.feedback_info_id,a.user_id,a.reply_user_id,a.feedback_time,a.feedback_type,a.feedback_title,a.feedback_info,b.deal_status_id, ").append("c.label_name,d.custom_group_name ").append("from CI_FEEDBACK_INFO a inner join DIM_DECLARE_DEAL_STATUS b on a.deal_status_id=b.deal_status_id ").append(" left join CI_LABEL_INFO c on a.label_custom_id= ").append(this.getDataBaseAdapter().getIntToChar("c.label_id ")).append("and a.feedback_type=" + ServiceConstants.FEEDBACK_TYPE_LABEL).append(" left join ci_custom_group_info d on a.label_custom_id=d.custom_group_id and a.feedback_type=" + ServiceConstants.FEEDBACK_TYPE_CUSTOM).append(" where a.status = 1 and (a.deal_status_id=4 or a.deal_status_id=5) ");
        if(!ciFeedbackInfo.getCanManageDeclare()) {
            if(StringUtil.isNotEmpty(ciFeedbackInfo.getUserId())) {
                if(ServiceConstants.FEEDBACK_SIGN_CREDATE == ciFeedbackInfo.getSign()) {
                    sql.append(" and a.user_id= :userId ");
                } else if(ServiceConstants.FEEDBACK_SIGN_DEAL == ciFeedbackInfo.getSign()) {
                    sql.append(" and a.reply_user_id= :userId ");
                } else {
                    sql.append(" and (a.user_id= :userId or a.reply_user_id= :userId) ");
                }
            }
        } else {
            sql.append(" and a.reply_user_id= \'" + Configure.getInstance().getProperty("ADMIN_ID") + "\' ");
        }

        if(StringUtil.isNotEmpty(ciFeedbackInfo.getFeedbackType())) {
            sql.append("and a.feedback_type= :feedbackType ");
        }

        if(StringUtil.isNotEmpty(ciFeedbackInfo.getFeedbackTitle())) {
            sql.append(" and LOWER(a.feedback_title) like :feedbackTitle  escape \'|\' ");
        }

        if(StringUtil.isNotEmpty(ciFeedbackInfo.getDealStatusId())) {
            sql.append("and a.deal_status_id= :dealStatusId ");
        }

        String sqlPage;
        if(StringUtil.isNotEmpty(ciFeedbackInfo.getStartDate())) {
            sqlPage = format.format(ciFeedbackInfo.getStartDate());
            sql.append(" and a.feedback_time >= ").append(this.getDataBaseAdapter().getTimeStamp(sqlPage, "00", "00", "00"));
        }

        if(StringUtil.isNotEmpty(ciFeedbackInfo.getEndDate())) {
            sqlPage = format.format(ciFeedbackInfo.getEndDate());
            sql.append(" and a.feedback_time <= ").append(this.getDataBaseAdapter().getTimeStamp(sqlPage, "23", "59", "59"));
        }

        sql.append("order by a.feedback_time desc");
        this.log.debug("历史反馈信息总数量查询：" + sql);
        sqlPage = this.getDataBaseAdapter().getPagedSql(sql.toString(), currPage, pageSize);
        List list = this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(CiFeedbackInfo.class), new BeanPropertySqlParameterSource(clone));
        return list;
    }
}
