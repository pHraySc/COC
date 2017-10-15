package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiGroupAttrRelHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.entity.CiGroupAttrRelId;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiGroupAttrRelHDaoImpl extends HibernateBaseDao<CiGroupAttrRel, CiGroupAttrRelId> implements ICiGroupAttrRelHDao {
    public CiGroupAttrRelHDaoImpl() {
    }

    public void insertCiGroupAttrRelHDao(CiGroupAttrRel ciGroupAttrRel) throws Exception {
        this.save(ciGroupAttrRel);
    }

    public List<CiGroupAttrRel> selectCiGroupAttrRelListBySourceId(String groupInfoId, String customSourceId) throws Exception {
        String hql = "from CiGroupAttrRel where id.customGroupId = ? and attrSource = ?";
        return this.find(hql, new Object[]{groupInfoId, customSourceId});
    }

    public void deleteCiGroupAttrRelListBySourceId(String groupInfoId) throws Exception {
        String hql = "delete CiGroupAttrRel where id.customGroupId = ?";
        this.batchExecute(hql, new Object[]{groupInfoId});
    }
}
