package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiLabelBrandUserNumHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiLabelBrandUserNum;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.springframework.stereotype.Repository;

@Repository("labelBrandUserNumHDao")
public class CiLabelBrandUserNumHDaoImpl extends HibernateBaseDao<CiLabelBrandUserNum, String> implements ICiLabelBrandUserNumHDao {
    public CiLabelBrandUserNumHDaoImpl() {
    }

    public List<CiLabelBrandUserNum> getLabelBrandUserNumList(CiLabelBrandUserNum labelBrandUserNum) {
        Criteria c = this.getSession().createCriteria(CiLabelBrandUserNum.class, "t");
        Example example = Example.create(labelBrandUserNum);
        c.add(example);
        Integer brandId = labelBrandUserNum.getId().getBrandId();
        if(null != brandId) {
            c.add(Expression.eq("t.id.brandId", brandId));
        }

        Integer cityId = labelBrandUserNum.getId().getCityId();
        if(null != cityId) {
            c.add(Expression.eq("t.id.cityId", cityId));
        }

        Integer vipLevelId = labelBrandUserNum.getId().getVipLevelId();
        if(null != vipLevelId) {
            c.add(Expression.eq("t.id.vipLevelId", vipLevelId));
        }

        Integer labelId = labelBrandUserNum.getId().getLabelId();
        if(null != labelId) {
            c.add(Expression.eq("t.id.labelId", labelId));
        }

        String dataDate = labelBrandUserNum.getId().getDataDate();
        if(StringUtils.isNotEmpty(dataDate)) {
            c.add(Expression.eq("t.id.dataDate", dataDate));
        }

        return c.list();
    }
}
