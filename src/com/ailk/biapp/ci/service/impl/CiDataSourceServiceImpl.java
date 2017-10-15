package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICidataSourceDao;
import com.ailk.biapp.ci.entity.CiDataSource;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiDataSourceService;
import freemarker.template.SimpleDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.java2d.pipe.SpanShapeRenderer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/4/24.
 */
@Service("ciDataSourceService")
@Transactional
public class CiDataSourceServiceImpl implements ICiDataSourceService {

    @Autowired
    private ICidataSourceDao cidataSourceDao;

    public CiDataSourceServiceImpl() {
    }

    @Override
    public List<CiDataSource> getDataSource(Pager pager) {
        return cidataSourceDao.getDataSource(pager);
    }


    @Override
    public int getDataCount() {
        return cidataSourceDao.getDataCount();
    }

    @Override
    public int getDataCountByDataSrcCode(CiDataSource dataSource) {
        return cidataSourceDao.getDataCountByDataSrcCode(dataSource);
    }

    @Override
    public List selectInfluencedLabelByDataSrcCode(Pager pager, CiDataSource dataSource) {
        return cidataSourceDao.selectInfluencedLabelByDataSrcCode(pager, dataSource);
    }

    @Override
    public List getDataSourceIncludeDate() {
        return cidataSourceDao.getDataSourceIncludeDate();
    }


    @Override
    public String getTheNewestDataDate(String labelId) throws ParseException {
        String dateString = cidataSourceDao.getTheNewestDataDate(labelId);
        if(dateString.length() == 6) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
            Date str = simpleDateFormat.parse(dateString);
            simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            return simpleDateFormat.format(str);
        }else if(dateString.length() == 8){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            Date str = simpleDateFormat.parse(dateString);
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.format(str);
        }else {
            return null;
        }
    }
}
