package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiLabelStatusHistoryJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.asiainfo.biframe.utils.string.StringUtil;
import org.springframework.stereotype.Repository;

@Repository
public class CiLabelStatusHistoryJDaoImpl extends JdbcBaseDao implements ICiLabelStatusHistoryJDao {
    public CiLabelStatusHistoryJDaoImpl() {
    }

    public int updateCiLabelStatusHistoryHasSendNotice(String labelIds) {
        if(StringUtil.isEmpty(labelIds)) {
            return 0;
        } else {
            StringBuffer sql = new StringBuffer();
            sql.append("update CI_LABEL_STATUS_HISTORY set HAS_SEND_NOTICE = ").append(1).append(" where LABEL_ID in (").append(labelIds).append(")").append(" and HAS_SEND_NOTICE = ").append(0);
            return this.getSimpleJdbcTemplate().getJdbcOperations().update(sql.toString());
        }
    }
}
