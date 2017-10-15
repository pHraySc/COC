package com.ailk.biapp.ci.localization.chongqing.dao.impl;

import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.localization.chongqing.dao.ICiChqUserRoleJDao;
import com.ailk.biapp.ci.localization.chongqing.model.CiLocalUserInfo;
import com.asiainfo.biframe.utils.config.Configure;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiChqUserRoleJDaoImpl extends JdbcBaseDao implements ICiChqUserRoleJDao {
    private Logger log = Logger.getLogger(this.getClass());

    public CiChqUserRoleJDaoImpl() {
    }

    public List<CiLocalUserInfo> selectLocalUserInfo(String loginName) throws CIServiceException {
        String sql = "select u.userID,u.name,u.login_name,u.password,u.email,u.city_id,c.city_name,u.duty,u.status,u.depart_id,d.depart_name from d_city c,Portal_Users u,d_depart d where u.login_name=? and u.city_id=c.city_id and u.depart_id=d.depart_id";
        this.log.debug("SQL->" + sql.toString());
        List localUserInfoList = this.getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_Local")).query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLocalUserInfo.class), new Object[]{loginName});
        return localUserInfoList;
    }

    public boolean verifyUserInfo(String loginName, int flag, String random_code) throws CIServiceException {
        String sql = "";
        if(flag == 1) {
            sql = "select count(1) from cqcrm.COC_LOGIN_RANDOMCODE where LOGIN_ID=\'" + loginName + "\' and INVALID_FLAG=0 and ENTER_FLAG=1 AND APP_FLAG=1 AND RANDOM_CODE=\'" + random_code + "\'";
        } else if(flag == 2) {
            sql = "select count(1) from cqcrm.COC_LOGIN_RANDOMCODE where LOGIN_ID=\'" + loginName + "\' and INVALID_FLAG=0 and ENTER_FLAG=2 AND APP_FLAG=2";
        }

        this.log.debug("SQL->" + sql.toString());
        boolean count = false;
        int count1 = this.getSimpleJdbcTemplate().queryForInt(sql, new Object[0]);
        if(count1 > 0) {
            if(flag == 1) {
                sql = "update cqcrm.COC_LOGIN_RANDOMCODE set INVALID_FLAG=1 where LOGIN_ID=? and INVALID_FLAG=0 and ENTER_FLAG=1 AND APP_FLAG=1 AND RANDOM_CODE=\'" + random_code + "\'";
                this.getSimpleJdbcTemplate().update(sql, new Object[]{loginName});
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean isAdmin(String loginName) throws CIServiceException {
        String sql = "select count(1) from portal_users u,portal_userRoles ur, portal_roles r where u.login_name=? and u.userid=ur.userid and ur.roleid=r.roleid and r.rolename=\'" + Configure.getInstance().getProperty("ADMIN_ROLE_NAME") + "\'";
        this.log.debug("SQL->" + sql.toString());
        boolean count = false;
        int count1 = this.getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_Local")).queryForInt(sql, new Object[]{loginName});
        return count1 > 0;
    }
}
