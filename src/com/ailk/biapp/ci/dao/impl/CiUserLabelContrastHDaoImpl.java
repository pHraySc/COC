package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiUserLabelContrastHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiUserLabelContrast;
import com.ailk.biapp.ci.entity.CiUserLabelContrastId;
import com.ailk.biapp.ci.util.DateUtil;
import java.sql.Timestamp;
import java.util.Date;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository("ciUserLabelContrastHDao")
public class CiUserLabelContrastHDaoImpl extends HibernateBaseDao<CiUserLabelContrast, CiUserLabelContrastId> implements ICiUserLabelContrastHDao {
    public CiUserLabelContrastHDaoImpl() {
    }

    public void deleteCiUserLabelContrast(CiUserLabelContrastId id) {
        CiUserLabelContrast entity = (CiUserLabelContrast)this.get(id);
        if(null != entity) {
            Date date = new Date();
            entity.setDelTime(new Timestamp(date.getTime()));
            entity.setStatus(Integer.valueOf(0));
            this.save(entity);
        }

    }

    public void insertCiUserLabelContrast(CiUserLabelContrast entity) {
        Date date = new Date();
        entity.setCreateTime(DateUtil.date2String(date));
        this.save(entity);
    }

    public void deleteCiUserLabelContrast(String userId, String labelId) {
        String hql = "UPDATE CiUserLabelContrast c SET c.status=0,c.delTime=:TIME WHERE c.id.userId = :userId AND c.id.labelId = :labelId AND c.status=1 ";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setString("userId", userId);
        query.setString("labelId", labelId);
        query.setTimestamp("TIME", new Date());
        query.executeUpdate();
    }
}
