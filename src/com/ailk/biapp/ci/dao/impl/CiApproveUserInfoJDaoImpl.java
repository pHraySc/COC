package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiApproveUserInfoJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class CiApproveUserInfoJDaoImpl extends JdbcBaseDao implements ICiApproveUserInfoJDao {
    private Logger log = Logger.getLogger(CiApproveUserInfoJDaoImpl.class);

    public CiApproveUserInfoJDaoImpl() {
    }

    public int select(String approveRoleId) {
        String sql = "SELECT COUNT(*) FROM CI_APPROVE_USER_INFO WHERE APPROVE_USER_ID =?";
        this.log.debug(sql);
        return this.getSimpleJdbcTemplate().queryForInt(sql, new Object[]{approveRoleId});
    }
}
