package com.ailk.biapp.ci.localization.sichuan.dao.impl;

import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.localization.sichuan.dao.ICiSCUserRoleJDao;
import com.ailk.biapp.ci.localization.sichuan.model.CiSCUserInfo;
import com.asiainfo.biframe.utils.config.Configure;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CiSCUserRoleJDaoImpl extends JdbcBaseDao implements ICiSCUserRoleJDao {
    private Logger log = Logger.getLogger(this.getClass());

    public CiSCUserRoleJDaoImpl() {
    }

    public boolean isAdmin(String loginNo) throws CIServiceException {
        StringBuffer sqlBuff = (new StringBuffer()).append("select count(1)").append(" from  (select login_no from POWER_USER_INFO where login_no= ? and dept_id = \'").append(Configure.getInstance().getProperty("DEPT_ID")).append("\') pu,").append("(select login_no from POWER_ROLE").append(" where model_id = ").append(Configure.getInstance().getProperty("MODEL_ID")).append(" ) pr").append(" where  pu.login_no = pr.login_no");
        this.log.debug("SQL->" + sqlBuff.toString());
        boolean count = false;
        int count1 = this.getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_Local")).queryForInt(sqlBuff.toString(), new Object[]{loginNo});
        return count1 > 0;
    }

    public List<CiSCUserInfo> selectLocalUserInfo(String loginNo) throws CIServiceException {
        StringBuffer sbf = (new StringBuffer()).append("select p.login_no,p.user_name,p.group_id,p.city_id,a.group_name,").append("p.county_id,p.section_id,p.dept_id,d.department_name,").append("p.user_status,p.mobile_phone,p.email,p.password,p.user_type").append(" from power_user_info p,(select  case when group_id =\'1\' then \'*\' else group_id end as group_id, group_name,parent_id,lvl_id from power_areainfo where lvl_id=1 or lvl_id=2) a,power_department d").append(" where login_no = ?").append(" and p.city_id = a.group_id and p.dept_id = d.dept_id");
        this.log.debug("SQL->" + sbf.toString());
        List scUserInfoList = this.getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_Local")).query(sbf.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiSCUserInfo.class), new Object[]{loginNo});
        return scUserInfoList;
}

    public boolean verifyUserInfo(String loginNo, String password) throws CIServiceException {
        StringBuffer sqlBuff = (new StringBuffer()).append("select count(1) from power_user_info").append(" where  login_no = ? and password = ?");
        this.log.debug("SQL->" + sqlBuff.toString());
        boolean count = false;
        int count1 = this.getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_Local")).queryForInt(sqlBuff.toString(), new Object[]{loginNo, password});
        return count1 > 0;
    }
}
