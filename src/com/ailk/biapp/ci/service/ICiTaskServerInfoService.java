package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiTaskServerInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;

public interface ICiTaskServerInfoService {
    List<CiTaskServerInfo> queryAllTaskServerList() throws CIServiceException;

    List<CiTaskServerInfo> queryTaskServerListByIsExeTask(int var1) throws CIServiceException;
}
