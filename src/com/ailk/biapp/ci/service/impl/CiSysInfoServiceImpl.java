package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiSysInfoHDao;
import com.ailk.biapp.ci.entity.CiSysInfo;
import com.ailk.biapp.ci.service.ICiSysInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiSysInfoServiceImpl implements ICiSysInfoService {
    @Autowired
    private ICiSysInfoHDao ciSysInfoHDao;

    public CiSysInfoServiceImpl() {
    }

    public CiSysInfo queryById(String sysId) {
        return this.ciSysInfoHDao.selectById(sysId);
    }
}
