package com.ailk.biapp.ci.ia.dao.impl;

import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.ia.dao.ICiIaWorksheetDao;
import com.ailk.biapp.ci.ia.entity.CiIaWorksheet;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CiIaWorksheetDaoImpl extends HibernateBaseDao<CiIaWorksheet, Integer> implements ICiIaWorksheetDao {
    public CiIaWorksheetDaoImpl() {
    }

    public void insertCiIaWorksheet(CiIaWorksheet ciIaWorksheet) {
        this.save(ciIaWorksheet);
    }

    public void updateCiIaWorksheet(CiIaWorksheet ciIaWorksheet) {
        this.save(ciIaWorksheet);
    }

    public void updateStatus(CiIaWorksheet ciIaWorksheet, Integer status) {
        Integer worksheetId = ciIaWorksheet.getWorksheetId();
        String hql = "update CiIaWorksheet t set t.status=? where t.worksheetId=?";
        Query query = this.getSession().createQuery(hql);
        query.setParameter(0, worksheetId);
        query.setParameter(1, status);
        query.executeUpdate();
    }

    public void deleteCiaWorksheet(Integer Id) {
        this.delete(Id);
    }

    public void deleteCiIaWorksheet(CiIaWorksheet ciIaWorksheet) {
        this.delete(ciIaWorksheet);
    }

    public List<CiIaWorksheet> selectByReportId(Integer Id) {
        List results = this.find(" from CiIaWorksheet t where t.reportId=? ", new Object[]{Id});
        return results;
    }
}
