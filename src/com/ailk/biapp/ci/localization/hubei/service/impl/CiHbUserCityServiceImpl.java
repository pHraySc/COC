package com.ailk.biapp.ci.localization.hubei.service.impl;

import com.ailk.biapp.ci.localization.hubei.dao.ICiHbUserCityJDao;
import com.ailk.biapp.ci.localization.hubei.service.ICiHbUserCityService;
import com.asiainfo.biframe.privilege.ICity;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("ICiHbUserCityService")
@Transactional
public class CiHbUserCityServiceImpl implements ICiHbUserCityService {
    @Autowired
    private ICiHbUserCityJDao userCityDao;

    public CiHbUserCityServiceImpl() {
    }

    public List<ICity> getCityByUserForLabelTree(String loginName) {
        List userCitys = this.userCityDao.getCityByUserForLabelTree(loginName);
        return userCitys;
    }
}
