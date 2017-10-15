package com.ailk.biapp.ci.ia.dao.impl;

import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.ia.dao.ICiIaDimIndexGroupHDao;
import com.ailk.biapp.ci.ia.entity.CiIaDimIndexGroup;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiIaDimIndexGroupHDaoImpl extends HibernateBaseDao<CiIaDimIndexGroup, Integer> implements ICiIaDimIndexGroupHDao {
    public CiIaDimIndexGroupHDaoImpl() {
    }

    public List<CiIaDimIndexGroup> selectBySheetChartRelId(int relId) {
        String hql = "from CiIaDimIndexGroup where worksheetChartRelId = ?";
        return this.find(hql, new Object[]{Integer.valueOf(relId)});
    }

    public void deleteBySheetChartRelId(int relId) {
        String hql = "delete from CiIaDimIndexGroup where worksheetChartRelId = ?";
        this.batchExecute(hql, new Object[]{Integer.valueOf(relId)});
    }

    public void insert(CiIaDimIndexGroup ciIaDimIndexGroup) {
        this.save(ciIaDimIndexGroup);
    }

    public void insert(List<CiIaDimIndexGroup> ciIaDimIndexGroupList) {
        Iterator i$ = ciIaDimIndexGroupList.iterator();

        while(i$.hasNext()) {
            CiIaDimIndexGroup group = (CiIaDimIndexGroup)i$.next();
            this.save(group);
        }

    }
}
