package com.ailk.biapp.ci.dataservice.service.impl;

import com.ailk.biapp.ci.dataservice.dao.ICiNewestLabelDateForMongoJDao;
import com.ailk.biapp.ci.dataservice.dao.ILabelAndIndexType2MongoJDao;
import com.ailk.biapp.ci.dataservice.dao.ILabelsAndIndexes2MongoJDao;
import com.ailk.biapp.ci.dataservice.dao.IPeopleLabel2MongoJDao;
import com.ailk.biapp.ci.dataservice.dao.IPersonIndex2MongoJDao;
import com.ailk.biapp.ci.dataservice.service.ILabelAndIndex2MongoService;
import com.ailk.biapp.ci.entity.CiNewestLabelDate;
import com.ailk.biapp.ci.entity.CiPersonIndexInfo;
import com.ailk.biapp.ci.entity.DimCrmLabelType;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.PeopleLabelInfo;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LabelAndIndex2MongoServiceImpl implements ILabelAndIndex2MongoService {
    private Logger log = Logger.getLogger(LabelAndIndex2MongoServiceImpl.class);
    @Autowired
    private ILabelAndIndexType2MongoJDao labelAndIndexType2MongoJDao;
    @Autowired
    private IPeopleLabel2MongoJDao peopleLabel2MongoJDao;
    @Autowired
    private IPersonIndex2MongoJDao personIndex2MongoJDao;
    @Autowired
    private ICiNewestLabelDateForMongoJDao ciNewestLabelDateForMongoJDao;
    @Autowired
    private ILabelsAndIndexes2MongoJDao labelsAndIndexes2MongoJDao;

    public LabelAndIndex2MongoServiceImpl() {
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public int queryLabelsAndIndexesCount(String dataDate) throws CIServiceException {
        boolean count = false;

        try {
            int count1 = this.labelsAndIndexes2MongoJDao.selectLabelsAndIndexesCount(dataDate);
            return count1;
        } catch (Exception var5) {
            String message = "查询个人标签及指标总数错误";
            this.log.error(message, var5);
            throw new CIServiceException(message, var5);
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public void execBigSelectSql(String sql, RowCallbackHandler rowCallbackHandler) throws Exception {
        this.labelsAndIndexes2MongoJDao.execBigSelectSql(sql, rowCallbackHandler);
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<Map<String, Object>> queryLabelsAndIndexes(int currPage, int pageSize, String dataDate) throws CIServiceException {
        List list = null;

        try {
            list = this.labelsAndIndexes2MongoJDao.selectLabelsAndIndexes(currPage, pageSize, dataDate);
            return list;
        } catch (Exception var7) {
            String message = "查询个人标签及指标分页列表错误";
            this.log.error(message, var7);
            throw new CIServiceException(message, var7);
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<DimCrmLabelType> queryDimCrmLabelTypeList() throws CIServiceException {
        List list = null;

        try {
            list = this.labelAndIndexType2MongoJDao.selectDimCrmLabelTypeList();
            return list;
        } catch (Exception var4) {
            String message = "查询客户群对应营销活动错误";
            this.log.error(message, var4);
            throw new CIServiceException(message, var4);
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<PeopleLabelInfo> queryPeopleLabelList() throws CIServiceException {
        List list = null;

        try {
            list = this.peopleLabel2MongoJDao.selectPeopleLabelList();
            return list;
        } catch (Exception var4) {
            String message = "查询客户群对应营销活动错误";
            this.log.error(message, var4);
            throw new CIServiceException(message, var4);
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiPersonIndexInfo> queryPersonIndexList() throws CIServiceException {
        List list = null;

        try {
            list = this.personIndex2MongoJDao.selectPersonIndexList();
            return list;
        } catch (Exception var4) {
            String message = "查询客户群对应营销活动错误";
            this.log.error(message, var4);
            throw new CIServiceException(message, var4);
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiNewestLabelDate> queryNewestLabelDate() throws CIServiceException {
        List list = null;

        try {
            list = this.ciNewestLabelDateForMongoJDao.selectNewestLabelDate();
            return list;
        } catch (Exception var4) {
            String message = "查询标签最新月错误";
            this.log.error(message, var4);
            throw new CIServiceException(message, var4);
        }
    }

    public boolean collectAllTag(String baseMonth, String baseDate) {
        return this.peopleLabel2MongoJDao.getUnionTableOfLebelsAndIndex(baseMonth, baseDate);
    }
}
