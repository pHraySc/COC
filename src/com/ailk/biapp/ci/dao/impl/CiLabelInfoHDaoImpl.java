package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiLabelInfoHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Repository
public class CiLabelInfoHDaoImpl extends HibernateBaseDao<CiLabelInfo, Integer> implements ICiLabelInfoHDao {
    public CiLabelInfoHDaoImpl() {
    }

    public void insertCiLabelInfo(CiLabelInfo bean) {
        this.save(bean);
    }

    public CiLabelInfo selectCiLabelInfoById(Integer labelId) {
        CiLabelInfo ciLabelInfo = (CiLabelInfo)this.get(labelId);
        return ciLabelInfo;
    }

    public void updateCiLabelInfo(CiLabelInfo bean) {
        this.save(bean);
    }

    public void delete(List<Integer> labelIds) {
        Iterator iter = labelIds.iterator();

        while(iter.hasNext()) {
            Integer labelId = (Integer)iter.next();
            this.delete(labelId);
        }

    }

    public List<CiLabelInfo> selectEffectiveCiLabelInfo() {
        SimpleExpression criterion = Restrictions.eq("dataStatusId", Integer.valueOf(2));
        Criteria criteria = this.createCriteria(new Criterion[]{criterion});
        criteria.addOrder(Order.asc("sortNum"));
        List list = criteria.list();
        return list;
    }

    public List<CiLabelInfo> selectEffectiveCiLabelInfoByParentId(Integer parentLabelId) {
        HashMap propertyNameValues = new HashMap();
        propertyNameValues.put("parentId", parentLabelId);
        propertyNameValues.put("dataStatusId", Integer.valueOf(2));
        Criterion criterion = Restrictions.allEq(propertyNameValues);
        List list = this.find(new Criterion[]{criterion});
        return list;
    }

    public List<CiLabelInfo> selectEffectiveFirstLevelLabel() {
        HashMap propertyNameValues = new HashMap();
        propertyNameValues.put("parentId", Integer.valueOf(-1));
        propertyNameValues.put("dataStatusId", Integer.valueOf(2));
        Criterion criterion = Restrictions.allEq(propertyNameValues);
        List list = this.find(new Criterion[]{criterion});
        return list;
    }

    public List<CiLabelInfo> selectToEffectChildrenLabelsById(Integer labelId) {
        List list = this.find("from CiLabelInfo cli where (cli.dataStatusId = 1 or cli.dataStatusId = 2 or cli.dataStatusId = 4) and cli.parentId = " + labelId, new Object[0]);
        return list;
    }
}
