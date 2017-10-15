package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiCustomProductTacticsRelJDao;
import com.ailk.biapp.ci.dao.ICiMarketTacticsJDao;
import com.ailk.biapp.ci.entity.CiCustomProductTacticsRel;
import com.ailk.biapp.ci.entity.CiMarketTactics;
import com.ailk.biapp.ci.entity.CiProductInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ICiMarketTacticsService;
import com.ailk.biapp.ci.util.cache.CacheBase;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiMarketTacticsServiceImpl implements ICiMarketTacticsService {
    private Logger log = Logger.getLogger(CiMarketTacticsServiceImpl.class);
    @Autowired
    private ICiMarketTacticsJDao marketTacticsJDao;
    @Autowired
    private ICiCustomProductTacticsRelJDao productTacticsRelJDao;

    public CiMarketTacticsServiceImpl() {
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public boolean isNameExit(String marketName) throws CIServiceException {
        boolean nameExit = false;

        try {
            nameExit = this.marketTacticsJDao.isNameExit(marketName);
            return nameExit;
        } catch (Exception var4) {
            this.log.error(var4);
            throw new CIServiceException("判断营销策略名称是否重复失败");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiMarketTactics> queryCiMarketTacticsName(String keyWord, String classId, String userId) throws Exception {
        List list = null;

        try {
            list = this.marketTacticsJDao.selectCiMarketTacticsName(keyWord, classId, userId);
            return list;
        } catch (Exception var6) {
            this.log.error(var6);
            throw new CIServiceException("查询营销策略根据名称失败");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public long queryCiMarketTacticsCount(CiMarketTactics marketTactics) throws CIServiceException {
        boolean count = false;

        int count1;
        try {
            count1 = this.marketTacticsJDao.selectCiMarketTacticsCount(marketTactics);
        } catch (Exception var4) {
            this.log.error(var4);
            throw new CIServiceException("查询营销策略列表失败");
        }

        return (long)count1;
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiMarketTactics> queryCiMarketTacticsList(int pageNum, int pageSize, CiMarketTactics marketTactics) throws Exception {
        List list = null;

        try {
            list = this.marketTacticsJDao.selectCiMarketTacticsList(pageNum, pageSize, marketTactics);
            CacheBase e = CacheBase.getInstance();
            Iterator i$ = list.iterator();

            while(i$.hasNext()) {
                CiMarketTactics marketTactic = (CiMarketTactics)i$.next();
                HashSet customGroupNameSet = new HashSet();
                ArrayList productNames = new ArrayList();
                marketTactic.setProductNames(productNames);
                String tacticId = marketTactic.getTacticId();
                List relList = this.productTacticsRelJDao.selectCustomProductTacticsRelList(tacticId);
                Iterator customGroupNameList = relList.iterator();

                while(customGroupNameList.hasNext()) {
                    CiCustomProductTacticsRel customProductTacticsRel = (CiCustomProductTacticsRel)customGroupNameList.next();
                    String customGroupName = customProductTacticsRel.getCustomGroupName();
                    Integer productId = customProductTacticsRel.getProductId();
                    CiProductInfo productInfo = e.getEffectiveProduct(String.valueOf(productId));
                    customGroupNameSet.add(customGroupName);
                    marketTactic.getProductNames().add(productInfo.getProductName());
                }

                ArrayList customGroupNameList1 = new ArrayList(customGroupNameSet);
                marketTactic.setCustomGroupNames(customGroupNameList1);
            }

            return list;
        } catch (Exception var17) {
            this.log.error(var17);
            throw new CIServiceException("查询营销策略集合失败");
        }
    }
}
