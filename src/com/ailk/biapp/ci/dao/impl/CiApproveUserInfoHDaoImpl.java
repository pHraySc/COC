package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiApproveUserInfoHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiApproveUserInfo;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiApproveUserInfoHDaoImpl extends HibernateBaseDao<CiApproveUserInfo, String> implements ICiApproveUserInfoHDao {
    public CiApproveUserInfoHDaoImpl() {
    }

    public CiApproveUserInfo selectById(String approveUserId) {
        CiApproveUserInfo ciApproveUserInfo = (CiApproveUserInfo)this.get(approveUserId);
        return ciApproveUserInfo;
    }

    public List<CiApproveUserInfo> selectByApproveRoleId(String approveRoleId) {
        List list = this.find("from CiApproveUserInfo u where u.approveRoleId = ?", new Object[]{approveRoleId});
        return list;
    }

    public List<CiApproveUserInfo> select(String approveRoleId, String deptId) {
        List list = this.find("from CiApproveUserInfo u where u.approveRoleId = ? and (u.deptId = ? or u.deptId = \'all\')", new Object[]{approveRoleId, deptId});
        return list;
    }

    public List<CiApproveUserInfo> selectByUserId(String approveUserId) {
        List list = this.find("from CiApproveUserInfo u where u.approveUserId = ?", new Object[]{approveUserId});
        return list;
    }
}
