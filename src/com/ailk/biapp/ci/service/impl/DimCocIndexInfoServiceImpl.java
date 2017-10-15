package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.IDimCocIndexInfoJDao;
import com.ailk.biapp.ci.entity.DimCocIndexInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.IDimCocIndexInfoService;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("dimCocIndexInfoServiceImpl")
@Transactional
public class DimCocIndexInfoServiceImpl implements IDimCocIndexInfoService {
    private static Logger log = Logger.getLogger(DimCocIndexInfoServiceImpl.class);
    @Autowired
    private IDimCocIndexInfoJDao dimCocIndexInfoJDao;

    public DimCocIndexInfoServiceImpl() {
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public int queryDimCocIndexInfoCount(HashMap<String, Object> bean) throws CIServiceException {
        boolean count = false;

        try {
            int count1 = this.dimCocIndexInfoJDao.getDimCocIndexInfoCount(bean);
            return count1;
        } catch (Exception var4) {
            log.error("查询指标信息配置信息总数失败", var4);
            throw new CIServiceException("查询指标信息配置信息总数失败");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<DimCocIndexInfo> queryDimCocIndexInfoList(int currPage, int pageSize, HashMap<String, Object> bean) throws CIServiceException {
        List dimIndexInfoList = null;

        try {
            dimIndexInfoList = this.dimCocIndexInfoJDao.getDimCocIndexInfoList(currPage, pageSize, bean);
            return dimIndexInfoList;
        } catch (Exception var6) {
            log.error("查询指标信息配置信息分页列表失败," + currPage + "," + pageSize + "," + bean, var6);
            throw new CIServiceException("查询指标信息配置信息分页列表失败");
        }
    }
}
