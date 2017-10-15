package com.ailk.biapp.ci.ia.dao.impl;

import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.ia.dao.ICiIaWorksheetChartRelDao;
import com.ailk.biapp.ci.ia.entity.CiIaWorksheetChartRel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiIaWorksheetChartRelDaoImpl extends HibernateBaseDao<CiIaWorksheetChartRel, Integer> implements ICiIaWorksheetChartRelDao {
    public CiIaWorksheetChartRelDaoImpl() {
    }

    public void insertCiIaWorksheetChartRel(CiIaWorksheetChartRel ciIaWorksheetChartRel) {
        this.save(ciIaWorksheetChartRel);
    }

    public void updateCiIaWorksheetChartRel(CiIaWorksheetChartRel ciIaWorksheetChartRel) {
        this.save(ciIaWorksheetChartRel);
    }

    public CiIaWorksheetChartRel selectById(Integer id) {
        CiIaWorksheetChartRel ciIaWorksheetChartRel = (CiIaWorksheetChartRel)this.findUniqueBy("worksheetChartRelId", id);
        return ciIaWorksheetChartRel;
    }

    public List<CiIaWorksheetChartRel> selectByWorksheetId(Integer Id) {
        List results = this.findBy("worksheetId", Id);
        return results;
    }

    public List<CiIaWorksheetChartRel> selectAllCiIaWorksheetChartRel() {
        List results = this.getSession().createQuery(" from CiIaWorksheetChartRel ").list();
        return results;
    }

    public void deleteCiIaWorksheetChartRel(Integer Id) {
        this.delete(Id);
    }

    public void deleteCiIaWorksheetChartRel(CiIaWorksheetChartRel ciIaWorksheetChartRel) {
        this.delete(ciIaWorksheetChartRel);
    }
}
