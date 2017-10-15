package com.ailk.biapp.ci.localization.hubei.service.impl;

import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.asiainfo.biframe.privilege.model.User_Group;
import com.asiainfo.biframe.privilege.sysmanage.exception.SysmanageException;
import com.asiainfo.biframe.privilege.sysmanage.service.impl.UserAdminServiceImpl;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.i18n.LocaleUtil;
import java.util.List;
import java.util.Map;

public class CiHbUserAdminServiceImpl extends UserAdminServiceImpl {
    public CiHbUserAdminServiceImpl() {
    }

    public User_Group getGroupObject(String userid) {
        try {
            List e = null;
            String sql = "select ug.group_id,ug.group_name from user_group_map ugm,user_group ug where ugm.group_id=ug.group_id and ugm.userid =?";
            e = (new JdbcBaseDao()).getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_Local")).queryForList(sql, new Object[]{userid});
            if(null == e || e.size() < 1) {
                sql = "select ug.group_id,ug.group_name from user_group_map ugm,user_group ug where ugm.group_id=ug.group_id";
                e = (new JdbcBaseDao()).getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_Local")).queryForList(sql, new Object[0]);
            }

            if(null != e && e.size() > 0) {
                Map m = (Map)e.get(0);
                User_Group ug = new User_Group();
                ug.setGroupid(m.get("group_id").toString());
                ug.setGroupname(m.get("group_name").toString());
                return ug;
            } else {
                return null;
            }
        } catch (Exception var6) {
            var6.printStackTrace();
            throw new SysmanageException("" + LocaleUtil.getLocaleMessage("privilegeService", "privilegeService.java.getUserGroupFail"));
        }
    }
}
