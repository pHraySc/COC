package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiProductInfoHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiProductInfo;
import java.util.HashMap;
import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Repository;

@Repository
public class CiProductInfoHDaoImpl extends HibernateBaseDao<CiProductInfo, Integer> implements ICiProductInfoHDao {
    public CiProductInfoHDaoImpl() {
    }

    public List<CiProductInfo> selectChildrenCiProductInfo(Integer parentId) {
        HashMap propertyNameValues = new HashMap();
        propertyNameValues.put("parentId", parentId);
        propertyNameValues.put("status", Integer.valueOf(2));
        Criterion criterion = Restrictions.allEq(propertyNameValues);
        List list = this.find(new Criterion[]{criterion});
        return list;
    }

    public List<CiProductInfo> selectCategoryCiProductInfo(Integer categoryId, Integer topProductParentId) {
        HashMap propertyNameValues = new HashMap();
        propertyNameValues.put("parentId", topProductParentId);
        propertyNameValues.put("categoryId", categoryId);
        propertyNameValues.put("status", Integer.valueOf(2));
        Criterion criterion = Restrictions.allEq(propertyNameValues);
        List list = this.find(new Criterion[]{criterion});
        return list;
    }

    public List<CiProductInfo> selectEffectiveCiProductInfo() {
        SimpleExpression criterion = Restrictions.eq("status", Integer.valueOf(2));
        List list = this.find(new Criterion[]{criterion});
        return list;
    }
}
