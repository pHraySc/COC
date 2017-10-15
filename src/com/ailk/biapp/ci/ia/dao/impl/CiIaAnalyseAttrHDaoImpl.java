package com.ailk.biapp.ci.ia.dao.impl;

import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.ia.dao.ICiIaAnalyseAttrHDao;
import com.ailk.biapp.ci.ia.entity.CiIaAnalyseAttr;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiIaAnalyseAttrHDaoImpl extends HibernateBaseDao<CiIaAnalyseAttr, Integer> implements ICiIaAnalyseAttrHDao {
    public CiIaAnalyseAttrHDaoImpl() {
    }

    public List<CiIaAnalyseAttr> selectEffectiveAttrBySource(int source) {
        String hql = "from CiIaAnalyseAttr where status = 1 and attrSource = ? order by dispOrder";
        return this.find(hql, new Object[]{Integer.valueOf(source)});
    }

    public void insert(CiIaAnalyseAttr attr) {
        this.save(attr);
    }
}
