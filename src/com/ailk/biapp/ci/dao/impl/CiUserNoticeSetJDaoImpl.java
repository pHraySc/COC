package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiUserNoticeSetJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiSysAnnouncement;
import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class CiUserNoticeSetJDaoImpl extends JdbcBaseDao implements ICiUserNoticeSetJDao {
    public CiUserNoticeSetJDaoImpl() {
    }

    public List<String> selectUserNoticeSetForUserId(CiSysAnnouncement sysAnnouncement) throws CIServiceException {
        StringBuffer sql = new StringBuffer();
        ArrayList userIdList = new ArrayList();
        sql.append("SELECT DISTINCT(USER_ID) USER_ID FROM CI_USER_NOTICE_SET WHERE NOTICE_ID = \'").append(sysAnnouncement.getTypeId()).append("\' AND NOTICE_TYPE = 1 AND IS_SUCCESS = 1 AND IS_RECEIVE = 1");
        List mapList = this.getSimpleJdbcTemplate().queryForList(sql.toString(), new Object[0]);
        Iterator i$ = mapList.iterator();

        while(i$.hasNext()) {
            Map map = (Map)i$.next();
            userIdList.add((String)map.get("USER_ID"));
        }

        return userIdList;
    }
}
