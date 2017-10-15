package com.ailk.biapp.ci.ia.dao.impl;

import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.ia.dao.ICiIaFilterHDao;
import com.ailk.biapp.ci.ia.entity.CiIaFilter;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiIaFilterHDaoImpl extends HibernateBaseDao<CiIaFilter, Integer> implements ICiIaFilterHDao {
    public CiIaFilterHDaoImpl() {
    }

    public List<CiIaFilter> selectBySheetChartRelId(int relId) {
        String hql = "from CiIaFilter where worksheetChartRelId = ?";
        return this.find(hql, new Object[]{Integer.valueOf(relId)});
    }

    public void deleteBySheetChartRelId(int relId) {
        String hql = "delete from CiIaFilter where worksheetChartRelId = ?";
        this.batchExecute(hql, new Object[]{Integer.valueOf(relId)});
    }

    public void insert(CiIaFilter ciIaFilter) {
        this.save(ciIaFilter);
    }

    public void insert(List<CiIaFilter> ciIaFilterList) {
        Iterator i$ = ciIaFilterList.iterator();

        while(i$.hasNext()) {
            CiIaFilter filter = (CiIaFilter)i$.next();
            this.save(filter);
        }

    }
}
