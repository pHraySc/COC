package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiTaskServerInfoHDao;
import com.ailk.biapp.ci.entity.CiTaskServerInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ICiTaskServerInfoService;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiTaskServerInfoServiceImpl implements ICiTaskServerInfoService {
    private final Logger log = Logger.getLogger(CiTaskServerInfoServiceImpl.class);
    @Autowired
    private ICiTaskServerInfoHDao ciTaskServerInfoHDao;

    public CiTaskServerInfoServiceImpl() {
    }

    public List<CiTaskServerInfo> queryAllTaskServerList() throws CIServiceException {
        List list = null;

        try {
            list = this.ciTaskServerInfoHDao.select();
            return list;
        } catch (Exception var4) {
            String message = "查询服务器信息错误";
            this.log.error(message, var4);
            throw new CIServiceException(message, var4);
        }
    }

    public List<CiTaskServerInfo> queryTaskServerListByIsExeTask(int isExeTask) throws CIServiceException {
        List list = null;

        try {
            list = this.ciTaskServerInfoHDao.selectByIsExeTask(isExeTask);
            return list;
        } catch (Exception var5) {
            String message = "根据是否执行清单状态查询服务器信息错误";
            this.log.error(message, var5);
            throw new CIServiceException(message, var5);
        }
    }
}
