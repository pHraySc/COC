package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiTaskServerCityRelHDao;
import com.ailk.biapp.ci.entity.CiTaskServerCityRel;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ICiTaskServerCityRelService;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiTaskServerCityRelServiceImpl implements ICiTaskServerCityRelService {
    private final Logger log = Logger.getLogger(CiTaskServerCityRelServiceImpl.class);
    @Autowired
    private ICiTaskServerCityRelHDao ciTaskServerCityRelHDao;

    public CiTaskServerCityRelServiceImpl() {
    }

    public List<CiTaskServerCityRel> queryTaskServerCityRelListByServerId(String serverId) throws CIServiceException {
        List list = null;

        try {
            list = this.ciTaskServerCityRelHDao.selectListByServerId(serverId);
            return list;
        } catch (Exception var5) {
            String message = "根据是服务器ID查询服务地市列表错误";
            this.log.error(message, var5);
            throw new CIServiceException(message, var5);
        }
    }

    public List<CiTaskServerCityRel> queryAllTaskServerCityRelList() throws CIServiceException {
        List list = null;

        try {
            list = this.ciTaskServerCityRelHDao.selectAllList();
            return list;
        } catch (Exception var4) {
            String message = "查询全部服务地市列表错误";
            this.log.error(message, var4);
            throw new CIServiceException(message, var4);
        }
    }
}
