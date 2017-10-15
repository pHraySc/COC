package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiDimCocIndexInfoHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.DimCocIndexInfo;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

@Repository
public class CiDimCocIndexInfoHDaoImpl extends HibernateBaseDao<DimCocIndexInfo, String> implements ICiDimCocIndexInfoHDao {
    public CiDimCocIndexInfoHDaoImpl() {
    }

    public DimCocIndexInfo selectDimCocIndexInfobyId(String id) throws Exception {
        String hql = "from DimCocIndexInfo dimCocIndexInfo where dimCocIndexInfo.indexCode = ?";
        List list = this.find(hql, new Object[]{id});
        return CollectionUtils.isEmpty(list)?null:(DimCocIndexInfo)list.get(0);
    }
}
