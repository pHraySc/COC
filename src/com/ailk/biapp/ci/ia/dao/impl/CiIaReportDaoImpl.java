package com.ailk.biapp.ci.ia.dao.impl;

import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.ia.dao.ICiIaReportDao;
import com.ailk.biapp.ci.ia.entity.CiIaReport;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CiIaReportDaoImpl extends HibernateBaseDao<CiIaReport, Integer> implements ICiIaReportDao {
    public CiIaReportDaoImpl() {
    }

    public void insertCiIaReport(CiIaReport ciIaReport) {
        this.save(ciIaReport);
    }

    public void updateCiIaReport(CiIaReport ciIaReport) {
        this.save(ciIaReport);
    }

    public void updateStatus(CiIaReport ciIaReport, Integer status) {
        String hql = "update CiIaReport t set t.status=? where t.reportId=?";
        Query query = this.getSession().createQuery(hql);
        query.setParameter(0, ciIaReport.getReportId());
        query.setParameter(1, status);
        query.executeUpdate();
    }

    public void deleteCiIaReport(Integer Id) {
        this.delete(Id);
    }

    public void deleteCiIaReport(CiIaReport ciIaReport) {
        this.delete(ciIaReport);
    }

    public List<CiIaReport> selectByCreateUserId(String createUserId) {
        List results = this.find("from CiIaReport t where t.createUserId=:createUserId", new Object[]{createUserId});
        return results;
    }
}
