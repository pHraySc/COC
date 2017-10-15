package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiPersonNoticeJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiPersonNotice;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiPersonNoticeJDaoImpl extends JdbcBaseDao implements ICiPersonNoticeJDao {
    private Logger log = Logger.getLogger(this.getClass());

    public CiPersonNoticeJDaoImpl() {
    }

    public int getCountOfPersonNotice(CiPersonNotice personNotice) throws Exception {
        String sql = "select count(*) " + this.getFromSql(personNotice);
        this.log.debug("select sql is : " + sql);
        int count = this.getSimpleJdbcTemplate().queryForInt(sql, new BeanPropertySqlParameterSource(personNotice));
        return count;
    }

    public List<CiPersonNotice> queryPagePersonNoticeList(int currPage, int pageSize, CiPersonNotice searchBean) throws Exception {
        String sql = "select T.* " + this.getFromSql(searchBean) + " order by T.NOTICE_SEND_TIME desc";
        this.log.debug("select sql is : " + sql);
        String sqlPage = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        List list = this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(CiPersonNotice.class), new BeanPropertySqlParameterSource(searchBean));
        return list;
    }

    public List<CiPersonNotice> queryPersonNoticeList(CiPersonNotice searchBean) throws Exception {
        String sql = "select T.NOTICE_ID,T.NOTICE_NAME,T.NOTICE_DETAIL,T.NOTICE_TYPE_ID,T.NOTICE_SEND_TIME,T.LABEL_ID,T.STATUS,T.RELEASE_USER_ID,T.CUSTOM_GROUP_ID,T.IS_SUCCESS ,T.RECEIVE_USER_ID,T.READ_STATUS,T.IS_SHOW_TIP " + this.getFromSql(searchBean) + " order by T.NOTICE_SEND_TIME desc";
        this.log.debug("select sql is : " + sql);
        List list = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiPersonNotice.class), new BeanPropertySqlParameterSource(searchBean));
        return list;
    }

    private String getFromSql(CiPersonNotice searchBean) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("from CI_PERSON_NOTICE T where 1=1 ");
        if(StringUtil.isNotEmpty(searchBean.getNoticeId())) {
            sql.append(" and T.NOTICE_ID = :noticeId");
        }

        if(StringUtil.isNotEmpty(searchBean.getNoticeName())) {
            searchBean.setNoticeName("%" + searchBean.getNoticeName() + "%");
            sql.append(" and T.NOTICE_NAME like :noticeName");
        }

        if(null != searchBean.getNoticeTypeId()) {
            sql.append(" and T.notice_type_id like :noticeTypeId");
        }

        if(StringUtil.isNotEmpty(searchBean.getStartDate())) {
            sql.append(" and T.notice_send_time >= :startDate");
        }

        if(StringUtil.isNotEmpty(searchBean.getEndDate())) {
            sql.append(" and T.ADD_TIME <= :endDate");
        }

        if(null != searchBean.getLabelId()) {
            sql.append(" and T.LABEL_ID = :labelId");
        }

        if(StringUtil.isNotEmpty(searchBean.getCustomerGroupId())) {
            sql.append(" and T.CUSTOM_GROUP_ID = :customerGroupId");
        }

        if(null != searchBean.getStatus()) {
            sql.append(" and T.STATUS = :status");
        }

        if(StringUtil.isNotEmpty(searchBean.getReleaseUserId())) {
            sql.append(" and T.RELEASE_USER_ID = :releaseUserId");
        }

        if(null != searchBean.getIsSuccess()) {
            sql.append(" and T.IS_SUCCESS = :isSuccess");
        }

        if(StringUtil.isNotEmpty(searchBean.getReceiveUserId())) {
            sql.append(" and T.RECEIVE_USER_ID = :receiveUserId");
        }

        if(null != searchBean.getReadStatus()) {
            sql.append(" and T.READ_STATUS = :readStatus");
        }

        if(null != searchBean.getIsShowTip()) {
            sql.append(" and T.IS_SHOW_TIP = :isShowTip");
        }

        return sql.toString();
    }
}
