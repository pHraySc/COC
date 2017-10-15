package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiLabelSceneRelHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiLabelSceneRel;
import com.ailk.biapp.ci.entity.CiLabelSceneRelId;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiLabelSceneRelHDaoImpl extends HibernateBaseDao<CiLabelSceneRel, CiLabelSceneRelId> implements ICiLabelSceneRelHDao {
    public CiLabelSceneRelHDaoImpl() {
    }

    public void insertCiLabelSceneRel(CiLabelSceneRel ciLabelSceneRel) {
        this.save(ciLabelSceneRel);
    }

    public void deleteLabelSceneByLabelId(String labelId) {
        this.batchExecute("delete from CiLabelSceneRel scene where scene.id.labelId = ? ", new Object[]{Integer.valueOf(labelId)});
    }

    public List<CiLabelSceneRel> selectLabelSceneByLabelId(String labelId) {
        String hql = "from CiLabelSceneRel scene where scene.id.labelId=?";
        List list = this.find(hql, new Object[]{Integer.valueOf(labelId)});
        return list;
    }
}
