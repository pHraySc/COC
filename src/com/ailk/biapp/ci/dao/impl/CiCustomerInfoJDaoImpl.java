package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiCustomerInfoJDao;
import com.ailk.biapp.ci.util.MongoDB;
import org.springframework.stereotype.Repository;

@Repository
public class CiCustomerInfoJDaoImpl implements ICiCustomerInfoJDao {
    public CiCustomerInfoJDaoImpl() {
    }

    public String getCustomerInfoJson(String mobile) {
        return MongoDB.getInstance().findOne("customer", "mobile", mobile).toString();
    }
}
