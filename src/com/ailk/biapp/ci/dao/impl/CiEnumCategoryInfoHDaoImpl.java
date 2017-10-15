package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.ICiEnumCategoryInfoHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiEnumCategoryInfo;
import com.asiainfo.biframe.utils.config.Configure;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiEnumCategoryInfoHDaoImpl extends HibernateBaseDao<CiEnumCategoryInfo, String> implements ICiEnumCategoryInfoHDao {
    public CiEnumCategoryInfoHDaoImpl() {
    }

    public void insertCiEnumCategoryInfo(CiEnumCategoryInfo ciEnumCategoryInfo) {
        this.save(ciEnumCategoryInfo);
    }

    public List<CiEnumCategoryInfo> selectCiEnumCategoryInfoList(String cityId, Integer columnId) {
        StringBuffer hql = new StringBuffer("from CiEnumCategoryInfo c where c.columnId = ?  and c.enumNum > 0 and c.status = " + ServiceConstants.PUBLIC_STATUS_VAL);
        new ArrayList();
        String centerCityId = Configure.getInstance().getProperty("CENTER_CITYID");
        if(!cityId.equals(centerCityId)) {
            hql.append(" and (c.cityId = ? or c.cityId = \'" + centerCityId + "\') ");
        } else {
            hql.append(" and c.cityId = ? ");
        }

        List ciEnumCategoryInfoList = this.find(hql.toString(), new Object[]{columnId, cityId});
        return ciEnumCategoryInfoList;
    }

    public CiEnumCategoryInfo selectById(String enumCategoryId) {
        CiEnumCategoryInfo ciEnumCategoryInfo = (CiEnumCategoryInfo)this.get(enumCategoryId);
        return ciEnumCategoryInfo;
    }
}
