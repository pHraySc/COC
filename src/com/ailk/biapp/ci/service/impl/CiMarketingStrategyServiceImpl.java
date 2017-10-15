package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiCustomListInfoJDao;
import com.ailk.biapp.ci.dao.ICiCustomProductRelJDao;
import com.ailk.biapp.ci.dao.ICiCustomProductTacticsRelHDao;
import com.ailk.biapp.ci.dao.ICiMarketTacticsHDao;
import com.ailk.biapp.ci.dao.ICustomProductMatchHDao;
import com.ailk.biapp.ci.dao.ICustomProductMatchJDao;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomProductTacticsRel;
import com.ailk.biapp.ci.entity.CiMarketTactics;
import com.ailk.biapp.ci.entity.CustomProductMatch;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ICiMarketingStrategyService;
import com.ailk.biapp.ci.util.GroupCalcSqlPaser;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiMarketingStrategyServiceImpl implements ICiMarketingStrategyService {
    private Logger log = Logger.getLogger(CiMarketingStrategyServiceImpl.class);
    private CiMarketTactics marketTactics;
    @Autowired
    private ICiCustomListInfoJDao ciCustomListInfoJDao;
    @Autowired
    private ICiCustomProductRelJDao ciCustomProductRelJDao;
    @Autowired
    private ICustomProductMatchJDao customProductMatchDao;
    @Autowired
    private ICustomProductMatchHDao customProductMatchHDao;
    @Autowired
    private ICiMarketTacticsHDao marketTacticsHDao;
    @Autowired
    private ICiCustomProductTacticsRelHDao customProductTacticsRelHDao;

    public CiMarketingStrategyServiceImpl() {
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public boolean isCustomProductMatch(CiCustomGroupInfo bean) throws CIServiceException {
        boolean isCustomProductMatch = false;

        try {
            isCustomProductMatch = this.customProductMatchDao.isCustomProductMatch(bean);
            return isCustomProductMatch;
        } catch (Exception var4) {
            this.log.error("判断客户群是否进行过系统匹配错误", var4);
            throw new CIServiceException("判断客户群是否进行过系统匹配错误");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CustomProductMatch> querySysProductMatch(CiCustomGroupInfo bean) throws CIServiceException {
        List list = null;

        try {
            list = this.customProductMatchDao.selectSysProductMatch(bean);
            if(list.size() > 10) {
                list = list.subList(0, 10);
            }

            return list;
        } catch (Exception var4) {
            this.log.error("查询系统匹配的产品集合", var4);
            throw new CIServiceException("查询系统匹配的产品集合错误");
        }
    }

    public CustomProductMatch getProductMatch(CiCustomGroupInfo bean, String selectSql, GroupCalcSqlPaser parser, Integer productId) throws CIServiceException {
        CustomProductMatch match = new CustomProductMatch();
        match.setDataDate(bean.getDataDate());
        match.setListTableName(bean.getListTableName());
        match.setProductId(productId);

        try {
            List intersectSql = this.customProductMatchHDao.selectByCustomProductMatch(match);
            if(intersectSql != null && intersectSql.size() > 0) {
                return (CustomProductMatch)intersectSql.get(0);
            }
        } catch (Exception var8) {
            this.log.error("查询系统匹配的产品集合", var8);
        }

        String intersectSql1 = parser.getIntersectOfTable(bean.getListTableName(), selectSql);
        int intersectCount = this.ciCustomListInfoJDao.selectBackCountBySql("select count(1) from(" + intersectSql1 + ")");
        match.setMatchCustomNum(Integer.valueOf(intersectCount));
        return match;
    }

    public void saveCustomProductRel(String customGroupId, String[] productIds, String userId) throws CIServiceException {
        try {
            this.ciCustomProductRelJDao.insertCustomProductRel(customGroupId, productIds, userId);
        } catch (Exception var5) {
            this.log.error("批量保存客户群与产品的匹配关系错误", var5);
            throw new CIServiceException("批量保存客户群与产品的匹配关系错误");
        }
    }

    public void saveCustomProductTacticsRel(CiMarketTactics marketTactics, List<CiCustomProductTacticsRel> customProductTacticsRelList) throws CIServiceException {
        try {
            String e = this.marketTacticsHDao.saveMarketTactics(marketTactics);
            this.customProductTacticsRelHDao.saveCustomProductTacticsRel(customProductTacticsRelList, e);
        } catch (Exception var4) {
            this.log.error("保存营销策略及客户群与产品的关系错误", var4);
            throw new CIServiceException("保存营销策略及客户群与产品的关系错误");
        }
    }

    public String marketTacticsList() {
        if(this.marketTactics == null) {
            this.marketTactics = new CiMarketTactics();
        }

        return "marketTacticsList";
    }

    public CiMarketTactics getMarketTactics() {
        return this.marketTactics;
    }

    public void setMarketTactics(CiMarketTactics marketTactics) {
        this.marketTactics = marketTactics;
    }

    public ICiCustomListInfoJDao getCiCustomListInfoJDao() {
        return this.ciCustomListInfoJDao;
    }

    public void setCiCustomListInfoJDao(ICiCustomListInfoJDao ciCustomListInfoJDao) {
        this.ciCustomListInfoJDao = ciCustomListInfoJDao;
    }

    public ICiCustomProductRelJDao getCiCustomProductRelJDao() {
        return this.ciCustomProductRelJDao;
    }

    public void setCiCustomProductRelJDao(ICiCustomProductRelJDao ciCustomProductRelJDao) {
        this.ciCustomProductRelJDao = ciCustomProductRelJDao;
    }

    public ICustomProductMatchJDao getCustomProductMatchDao() {
        return this.customProductMatchDao;
    }

    public void setCustomProductMatchDao(ICustomProductMatchJDao customProductMatchDao) {
        this.customProductMatchDao = customProductMatchDao;
    }

    public ICiMarketTacticsHDao getMarketTacticsHDao() {
        return this.marketTacticsHDao;
    }

    public void setMarketTacticsHDao(ICiMarketTacticsHDao marketTacticsHDao) {
        this.marketTacticsHDao = marketTacticsHDao;
    }

    public ICiCustomProductTacticsRelHDao getCustomProductTacticsRelHDao() {
        return this.customProductTacticsRelHDao;
    }

    public void setCustomProductTacticsRelHDao(ICiCustomProductTacticsRelHDao customProductTacticsRelHDao) {
        this.customProductTacticsRelHDao = customProductTacticsRelHDao;
    }

    public void removeCustomProductMatchByListTableName(String listTableName) {
        this.customProductMatchHDao.deleteByListTableName(listTableName);
    }

    public void saveCustomProductMatch(CustomProductMatch customProductMatch) {
        this.customProductMatchHDao.insertOrUpdate(customProductMatch);
    }
}
