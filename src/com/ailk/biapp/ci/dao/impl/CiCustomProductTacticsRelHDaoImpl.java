package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiCustomProductTacticsRelHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiCustomProductTacticsRel;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiCustomProductTacticsRelHDaoImpl extends HibernateBaseDao<CiCustomProductTacticsRel, String> implements ICiCustomProductTacticsRelHDao {
    public CiCustomProductTacticsRelHDaoImpl() {
    }

    public void saveCustomProductTacticsRel(List<CiCustomProductTacticsRel> customProductTacticsRelList, String tacticeId) throws Exception {
        Iterator i$ = customProductTacticsRelList.iterator();

        while(i$.hasNext()) {
            CiCustomProductTacticsRel customProductTacticsRel = (CiCustomProductTacticsRel)i$.next();
            customProductTacticsRel.setTacticId(tacticeId);
            this.save(customProductTacticsRel);
        }

    }
}
