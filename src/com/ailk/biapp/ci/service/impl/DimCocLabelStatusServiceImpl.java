package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.IDimCocLabelStatusJDao;
import com.ailk.biapp.ci.entity.DimCocLabelStatus;
import com.ailk.biapp.ci.service.IDimCocLabelStatusService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DimCocLabelStatusServiceImpl implements IDimCocLabelStatusService {
    private Logger log = Logger.getLogger(DimCocLabelStatusServiceImpl.class);
    @Autowired
    private IDimCocLabelStatusJDao dimCocLabelStatusJDao;

    public DimCocLabelStatusServiceImpl() {
    }

    public List<DimCocLabelStatus> queryDimCocLabelStatusList(String dataDate) {
        this.log.debug("标签更新统计周期是：" + dataDate);
        List list = this.dimCocLabelStatusJDao.selectDimCocLabelStatusList(dataDate);
        return list;
    }
}
