package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiProductCategoryHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiProductCategory;
import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Repository;

@Repository
public class CiProductCategoryHDaoImpl extends HibernateBaseDao<CiProductCategory, Integer> implements ICiProductCategoryHDao {
    public CiProductCategoryHDaoImpl() {
    }

    public List<CiProductCategory> selectProductFirstCategoryList() {
        SimpleExpression criterion = Restrictions.eq("parentId", Integer.valueOf(-1));
        List list = this.find(new Criterion[]{criterion});
        return list;
    }

    public List<CiProductCategory> selectProductSecondCategory(Integer categoryId) {
        SimpleExpression criterion = Restrictions.eq("parentId", categoryId);
        List list = this.find(new Criterion[]{criterion});
        return list;
    }
}
