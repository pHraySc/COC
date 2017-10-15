package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.ICiEnumConditionInfoHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiEnumConditionInfo;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiEnumConditionInfoHDaoImpl extends HibernateBaseDao<CiEnumConditionInfo, String> implements ICiEnumConditionInfoHDao {
    public CiEnumConditionInfoHDaoImpl() {
    }

    public void insertCiEnumConditionInfo(CiEnumConditionInfo ciEnumConditionInfo) {
        this.save(ciEnumConditionInfo);
    }

    public List<CiEnumConditionInfo> selectCiEnumConditionInfoList(String enumCategoryId) {
        List ciEnumCategoryInfoList = null;
        String hql = "from CiEnumConditionInfo  where enumCategoryId = ?  and status = " + ServiceConstants.STATUS_VAL;
        ciEnumCategoryInfoList = this.find(hql, new Object[]{enumCategoryId});
        return ciEnumCategoryInfoList;
    }

    public CiEnumConditionInfo selectById(String primaryId) {
        CiEnumConditionInfo ciEnumConditionInfo = (CiEnumConditionInfo)this.get(primaryId);
        return ciEnumConditionInfo;
    }

    public void deleteCiEnumConditionInfoListByenumCategoryId(String enumCategoryId) {
        this.batchExecute("delete from CiEnumConditionInfo where enumCategoryId = ?", new Object[]{enumCategoryId});
    }
}
