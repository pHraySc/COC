package com.ailk.biapp.ci.ia.dao.impl;

import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.ia.dao.ICiIaChartIndexRuleHDao;
import com.ailk.biapp.ci.ia.entity.CiIaChartIndexRule;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiIaChartIndexRuleHDaoImpl extends HibernateBaseDao<CiIaChartIndexRule, Integer> implements ICiIaChartIndexRuleHDao {
    public CiIaChartIndexRuleHDaoImpl() {
    }

    public List<CiIaChartIndexRule> selectBySheetChartRelId(int relId) {
        String hql = "from CiIaChartIndexRule where worksheetChartRelId = ?";
        return this.find(hql, new Object[]{Integer.valueOf(relId)});
    }

    public void deleteBySheetChartRelId(int relId) {
        String hql = "delete from CiIaChartIndexRule where worksheetChartRelId = ?";
        this.batchExecute(hql, new Object[]{Integer.valueOf(relId)});
    }

    public void insert(CiIaChartIndexRule ciIaChartIndexRule) {
        this.save(ciIaChartIndexRule);
    }

    public void inset(List<CiIaChartIndexRule> ciIaChartIndexRulelist) {
        Iterator i$ = ciIaChartIndexRulelist.iterator();

        while(i$.hasNext()) {
            CiIaChartIndexRule rule = (CiIaChartIndexRule)i$.next();
            this.save(rule);
        }

    }
}
