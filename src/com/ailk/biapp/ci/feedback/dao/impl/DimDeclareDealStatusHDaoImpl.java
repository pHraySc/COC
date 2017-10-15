package com.ailk.biapp.ci.feedback.dao.impl;

import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.feedback.dao.IDimDeclareDealStatusHDao;
import com.ailk.biapp.ci.feedback.entity.DimDeclareDealStatus;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DimDeclareDealStatusHDaoImpl extends HibernateBaseDao<DimDeclareDealStatus, Integer> implements IDimDeclareDealStatusHDao {
    public DimDeclareDealStatusHDaoImpl() {
    }

    public List<DimDeclareDealStatus> selectAllStatus() {
        List list = this.getAll();
        return list;
    }
}
