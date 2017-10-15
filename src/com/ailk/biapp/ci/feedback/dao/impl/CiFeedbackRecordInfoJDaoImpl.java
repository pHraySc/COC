package com.ailk.biapp.ci.feedback.dao.impl;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.feedback.dao.ICiFeedbackRecordInfoJDao;
import com.ailk.biapp.ci.feedback.dao.impl.CiFeedbackInfoJDaoImpl;
import com.ailk.biapp.ci.feedback.entity.CiFeedbackRecordInfo;
import com.ailk.biapp.ci.util.DateUtil;
import com.asiainfo.biframe.utils.config.Configure;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiFeedbackRecordInfoJDaoImpl extends JdbcBaseDao implements ICiFeedbackRecordInfoJDao {
    protected Logger log = Logger.getLogger(CiFeedbackInfoJDaoImpl.class);

    public CiFeedbackRecordInfoJDaoImpl() {
    }

    public List<CiFeedbackRecordInfo> getFeedbackRecordInfo() throws Exception {
        List list = null;
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct * from CI_FEEDBACK_RECORD_INFO a inner join CI_FEEDBACK_INFO b on a.FEEDBACK_INFO_ID = b.FEEDBACK_INFO_ID");
        sql.append(" where b.DEAL_STATUS_ID = " + ServiceConstants.FEEDBACK_DEAL_STATUS_FINISH + " and a.REPLY_TIME <? ");
        Date date = new Date();
        Date expirationDate = DateUtil.addDays(date, -Integer.valueOf(Configure.getInstance().getProperty("EXPIRATION_DATE")).intValue());
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiFeedbackRecordInfo.class), new Object[]{expirationDate});
        return list;
    }

    public List<CiFeedbackRecordInfo> getCancelOrCloseFeedbackList() throws Exception {
        List list = null;
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct * from CI_FEEDBACK_RECORD_INFO a inner join CI_FEEDBACK_INFO b on a.FEEDBACK_INFO_ID = b.FEEDBACK_INFO_ID");
        sql.append(" where  a.REPLY_TIME <? and(b.DEAL_STATUS_ID = " + ServiceConstants.FEEDBACK_DEAL_STATUS_CLOSE + " or b.DEAL_STATUS_ID = " + ServiceConstants.FEEDBACK_DEAL_STATUS_CANCEL + ")");
        Date date = new Date();
        Date expirationDate = DateUtil.addDays(date, -Integer.valueOf(Configure.getInstance().getProperty("EXPIRATION_DATE")).intValue());
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiFeedbackRecordInfo.class), new Object[]{expirationDate});
        return list;
    }
}
