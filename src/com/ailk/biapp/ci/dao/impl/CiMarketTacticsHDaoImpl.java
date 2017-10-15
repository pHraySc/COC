package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiMarketTacticsHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiMarketTactics;
import org.springframework.stereotype.Repository;

@Repository
public class CiMarketTacticsHDaoImpl extends HibernateBaseDao<CiMarketTactics, String> implements ICiMarketTacticsHDao {
    public CiMarketTacticsHDaoImpl() {
    }

    public String saveMarketTactics(CiMarketTactics marketTactics) throws Exception {
        this.save(marketTactics);
        return marketTactics.getTacticId();
    }
}
