package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiCustomSceneHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiCustomSceneRel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiCustomSceneHDaoImpl extends HibernateBaseDao<CiCustomSceneRel, String> implements ICiCustomSceneHDao {
    public CiCustomSceneHDaoImpl() {
    }

    public List<CiCustomSceneRel> getCustomScenesByCustomId(String customGroupId) {
        String hql = "from CiCustomSceneRel where id.customGroupId = ? and status = 1";
        List list = this.find(hql, new Object[]{customGroupId});
        return list;
    }

    public void insertCiCustomSceneRel(CiCustomSceneRel ciCustomSceneRel) {
        this.save(ciCustomSceneRel);
    }
}
