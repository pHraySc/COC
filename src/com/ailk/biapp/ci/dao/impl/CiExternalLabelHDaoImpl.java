package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiExternalLabelHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiExternalSysLabelRel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiExternalLabelHDaoImpl extends HibernateBaseDao<CiCustomListInfo, String> implements ICiExternalLabelHDao {
    public CiExternalLabelHDaoImpl() {
    }

    public List<CiExternalSysLabelRel> selectExternalLabelRelList() throws Exception {
        String hql = "from CiExternalSysLabelRel";
        List ciExternalSysLabelRelList = this.find(hql, new Object[0]);
        return ciExternalSysLabelRelList;
    }
}
