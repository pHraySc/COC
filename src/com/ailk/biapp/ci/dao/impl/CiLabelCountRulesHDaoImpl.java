package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiLabelCountRulesHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.DimCocLabelCountRules;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CiLabelCountRulesHDaoImpl extends HibernateBaseDao<DimCocLabelCountRules, String> implements ICiLabelCountRulesHDao {
    public CiLabelCountRulesHDaoImpl() {
    }

    public void insertDimCocLabelCountRule(DimCocLabelCountRules lableCountRules) throws Exception {
        this.save(lableCountRules);
    }

    public DimCocLabelCountRules selectDimCocLabelCountRule(String countRulesCode) throws Exception {
        return (DimCocLabelCountRules)this.get(countRulesCode);
    }

    public String selectMaxLabelCountRule() throws Exception {
        String sql = "SELECT MAX(countRulesCode) FROM DimCocLabelCountRules";
        Query query = this.createQuery(sql, new Object[0]);
        Object object = query.uniqueResult();
        return null == object?"":object.toString();
    }
}
