package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiLabelCountDao;
import com.ailk.biapp.ci.dao.impl.CiLabelCountDaoImpl;
import com.ailk.biapp.ci.entity.CilabelCount;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiLabelCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by admin on 2017/4/24.
 */
@Service("ciLabelCountService")
@Transactional
public class CiLabelCountServiceImpl implements ICiLabelCountService {

    @Autowired
    private ICiLabelCountDao ciLabelCountDao;

    public CiLabelCountServiceImpl() {
    }

    @Override
    public List<CilabelCount> getLabelCount(Pager pager) {
        return ciLabelCountDao.getLabelCount(pager);
    }

    @Override
    public int getLabelCountNum() {
        return ciLabelCountDao.getLabelCountNum();
    }
}
