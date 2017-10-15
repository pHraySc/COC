package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICustomersCompareAnalysisHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiUserCustomContrast;
import com.ailk.biapp.ci.entity.CiUserCustomContrastId;
import com.ailk.biapp.ci.util.DateUtil;
import java.sql.Timestamp;
import java.util.Date;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository("customersCompareAnalysisHDao")
public class CustomersCompareAnalysisHDaoImpl extends HibernateBaseDao<CiUserCustomContrast, CiUserCustomContrastId> implements ICustomersCompareAnalysisHDao {
    public CustomersCompareAnalysisHDaoImpl() {
    }

    public void insertCiUserCustomContrast(CiUserCustomContrast ciUserCustomContrast) throws Exception {
        try {
            Date e = new Date();
            ciUserCustomContrast.setCreateTime(DateUtil.date2String(e));
            this.save(ciUserCustomContrast);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public void deleteCiUserCustomContrast(CiUserCustomContrastId id) throws Exception {
        CiUserCustomContrast ciUserCustomContrast = (CiUserCustomContrast)this.get(id);
        if(ciUserCustomContrast != null) {
            Date date = new Date();
            ciUserCustomContrast.setDelTime(new Timestamp(date.getTime()));
            ciUserCustomContrast.setStatus(Integer.valueOf(0));
            this.save(ciUserCustomContrast);
        }

    }

    public void updateCiUserCustomContrast(String userId, String customGroupId) throws Exception {
        String hql = "UPDATE CiUserCustomContrast c SET c.status=0,c.delTime=:TIME WHERE c.id.userId = :userId AND c.id.customGroupId = :customGroupId AND c.status=1 ";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setString("userId", userId);
        query.setString("customGroupId", customGroupId);
        query.setTimestamp("TIME", new Date());
        query.executeUpdate();
    }
}
