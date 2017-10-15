package com.ailk.biapp.ci.market.service.impl;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.ICiCustomGroupInfoHDao;
import com.ailk.biapp.ci.dao.ICiLabelInfoJDao;
import com.ailk.biapp.ci.dao.ICustomGroupInfoJDao;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.market.dao.ICiCustomLabelSceneRelHDao;
import com.ailk.biapp.ci.market.dao.ICiCustomLabelSceneRelJDao;
import com.ailk.biapp.ci.market.dao.ICiMarketSceneHDao;
import com.ailk.biapp.ci.market.dao.ICiMarketTaskHDao;
import com.ailk.biapp.ci.market.entity.CiCustomLabelSceneRel;
import com.ailk.biapp.ci.market.entity.CiMarketScene;
import com.ailk.biapp.ci.market.entity.CiMarketTask;
import com.ailk.biapp.ci.market.service.ICiMarketService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.service.impl.CiProductServiceImpl;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiMarketServiceImpl implements ICiMarketService {
    private Logger log = Logger.getLogger(CiProductServiceImpl.class);
    @Autowired
    private ICiMarketTaskHDao marketTaskHDao;
    @Autowired
    private ICiMarketSceneHDao marketSceneHDao;
    @Autowired
    private ICiCustomLabelSceneRelHDao customLabelSceneRelHDao;
    @Autowired
    private ICiLabelInfoJDao ciLabelInfoJDao;
    @Autowired
    private ICustomGroupInfoJDao groupInfoJDao;
    @Autowired
    private ICiCustomLabelSceneRelJDao sceneRelJDao;
    @Autowired
    private ICiCustomGroupInfoHDao ciCustomGroupInfoHDao;
    @Autowired
    private ICustomersManagerService customersManagerService;

    public CiMarketServiceImpl() {
    }

    public List<CiMarketTask> findFirstLevelTasks() throws CIServiceException {
        new ArrayList();

        try {
            List ciMarketTasks = this.marketTaskHDao.getFirstLevelTasks();
            return ciMarketTasks;
        } catch (Exception var3) {
            this.log.error("获得一级营销任务出错", var3);
            throw new CIServiceException("获得一级营销任务出错");
        }
    }

    public List<CiMarketScene> findFirstLevelScenes() throws CIServiceException {
        new ArrayList();

        try {
            List ciMarketScenes = this.marketSceneHDao.queryFirstLevelScenes();
            return ciMarketScenes;
        } catch (Exception var3) {
            this.log.error("获得一级场景分类出错", var3);
            throw new CIServiceException("获得一级场景分类出错");
        }
    }

    public List<CiMarketTask> findSecondTasksByTaskId(String taskId) throws CIServiceException {
        new ArrayList();

        try {
            List secondLevelTasks = this.marketTaskHDao.querySecondTasksByTaskId(taskId);
            return secondLevelTasks;
        } catch (Exception var4) {
            this.log.error("根据一级营销任务查询二级营销任务出错", var4);
            throw new CIServiceException("根据一级营销任务查询二级营销任务出错");
        }
    }

    public void updateLabelScoreAndTimesAndTime() throws CIServiceException {
        try {
            List e = this.ciLabelInfoJDao.findRecommendLabelInfoList();
            this.sceneRelJDao.modifyLabelSceneRel(e);
        } catch (Exception var2) {
            this.log.error("批量更新标签评分、最新数据日期及使用次数失败", var2);
            throw new CIServiceException("批量更新标签评分、最新数据日期及使用次数失败");
        }
    }

    public void updateCustomScoreAndTimesAndTime() throws CIServiceException {
        try {
            List e = this.groupInfoJDao.findRecommendCustomInfoList();
            this.sceneRelJDao.modifyCustomSceneRel(e);
        } catch (Exception var2) {
            this.log.error("批量更新客户群评分、最新数据日期及使用次数失败", var2);
            throw new CIServiceException("批量更新客户群评分、最新数据日期及使用次数失败");
        }
    }

    public List<CiMarketScene> findSecondScenesBySceneId(String sceneId) throws CIServiceException {
        new ArrayList();

        try {
            List secondLevelScenes = this.marketSceneHDao.querySecondScenesBySceneId(sceneId);
            return secondLevelScenes;
        } catch (Exception var4) {
            this.log.error("根据一级场景分类查询二级场景分类出错", var4);
            throw new CIServiceException("根据一级场景分类查询二级场景分类出错");
        }
    }

    public void saveCustomOrLabelSceneRel(CiCustomLabelSceneRel customLabelSceneRel) throws CIServiceException {
        try {
            this.customLabelSceneRelHDao.insertCustomLabelRel(customLabelSceneRel);
        } catch (Exception var4) {
            String message = "保存客户群标签场景关系失败";
            this.log.error(message, var4);
            throw new CIServiceException(message, var4);
        }
    }

    public boolean isRepeatScene(CiCustomLabelSceneRel customLabelSceneRel) throws CIServiceException {
        boolean flag = false;
        List list = this.customLabelSceneRelHDao.queryCustomLabelSceneRels(customLabelSceneRel);
        if(list == null || list.size() == 0) {
            flag = true;
        }

        return flag;
    }

    public void deleteCustomOrLabelSceneRel(CiCustomLabelSceneRel customLabelSceneRel) throws CIServiceException {
        try {
            this.customLabelSceneRelHDao.deleteCustomLabelRel(customLabelSceneRel);
        } catch (Exception var3) {
            this.log.error("deleteCustomOrLabelSceneRel " + customLabelSceneRel, var3);
            throw new CIServiceException("删除客户群标签场景关系实体失败", var3);
        }
    }

    public boolean isExistOtherSceneRel(CiCustomLabelSceneRel customLabelSceneRel) throws CIServiceException {
        boolean isExist = false;

        try {
            if(this.customLabelSceneRelHDao.queryCustomLabelSceneRelsById(customLabelSceneRel).size() > 0) {
                isExist = true;
            }

            return isExist;
        } catch (Exception var4) {
            this.log.error("是否还存在其他标签客户群关系 " + customLabelSceneRel, var4);
            throw new CIServiceException("查询是否还存在其他标签客户群关系失败", var4);
        }
    }

    public List<CiCustomLabelSceneRel> queryMarketIndexList(int start, int pageSize, CiCustomLabelSceneRel ciCustomLabelSceneRel) throws CIServiceException {
        List result = null;

        try {
            result = this.sceneRelJDao.selectCiCustomLabelSceneRel(start, pageSize, ciCustomLabelSceneRel);
            if(result != null) {
                for(int e = 0; e < result.size(); ++e) {
                    CiCustomLabelSceneRel rel = (CiCustomLabelSceneRel)result.get(e);
                    if(StringUtils.isEmpty(rel.getNewestTime())) {
                        rel.setNewestTime("");
                    }

                    if(rel.getAvgScore() == null) {
                        rel.setAvgScore(Double.valueOf(0.0D));
                    }

                    String dataDate = this.getCustomLabelDataDate(rel);
                    rel.setDataDate(dataDate);
                }
            }

            return result;
        } catch (Exception var8) {
            this.log.error("queryMarketIndexList", var8);
            throw new CIServiceException("查询营销导航也列表异常", var8);
        }
    }

    private String getCustomLabelDataDate(CiCustomLabelSceneRel rel) {
        String fmtDataDate = "-";
        if(rel.getShowType().intValue() == ServiceConstants.RECOM_SHOW_TYPE_LABEL) {
            CiLabelInfo customGroupInfo = CacheBase.getInstance().getEffectiveLabel(String.valueOf(rel.getLabelId()));
            rel.setUpdateCycle(customGroupInfo.getUpdateCycle());
            fmtDataDate = this.queryLabelDataDate(customGroupInfo);
        } else if(rel.getShowType().intValue() == ServiceConstants.RECOM_SHOW_TYPE_CUSTOM) {
            CiCustomGroupInfo customGroupInfo1 = this.ciCustomGroupInfoHDao.selectCustomGroupById(rel.getCustomGroupId());
            rel.setUpdateCycle(customGroupInfo1.getUpdateCycle());
            fmtDataDate = this.queryCustomDataDate(customGroupInfo1);
        }

        return fmtDataDate;
    }

    public String queryCustomDataDate(CiCustomGroupInfo customGroupInfo) {
        String customDataDate = customGroupInfo.getDataDate();
        String fmtDataDate = "-";
        if(StringUtil.isNotEmpty(customDataDate)) {
            if(customGroupInfo.getUpdateCycle().intValue() == 3) {
                fmtDataDate = DateUtil.string2StringFormat(customDataDate, "yyyyMMdd", "yyyy-MM-dd");
            } else if(customGroupInfo.getUpdateCycle().intValue() == 2) {
                fmtDataDate = DateUtil.string2StringFormat(customDataDate, "yyyyMM", "yyyy-MM");
            } else if(customGroupInfo.getUpdateCycle().intValue() == 1) {
                if(customDataDate.length() == 6) {
                    fmtDataDate = DateUtil.string2StringFormat(customDataDate, "yyyyMM", "yyyy-MM");
                } else if(customDataDate.length() == 8) {
                    fmtDataDate = DateUtil.string2StringFormat(customDataDate, "yyyyMMdd", "yyyy-MM-dd");
                }
            } else if(customGroupInfo.getUpdateCycle().intValue() == 4) {
                List rules = this.customersManagerService.queryCiLabelRuleList(customGroupInfo.getCustomGroupId(), Integer.valueOf(1));
                if(rules != null) {
                    String rulesDataDate = this.queryMinRulesDataDate(rules);
                    if(rulesDataDate.length() == 6) {
                        fmtDataDate = DateUtil.string2StringFormat(rulesDataDate, "yyyyMM", "yyyy-MM");
                    } else if(rulesDataDate.length() == 8) {
                        fmtDataDate = DateUtil.string2StringFormat(rulesDataDate, "yyyyMMdd", "yyyy-MM-dd");
                    }
                }
            }
        }

        return fmtDataDate;
    }

    public String queryLabelDataDate(CiLabelInfo labelInfo) {
        String labelDataDate = labelInfo.getDataDate();
        String fmtDataDate = "-";
        if(StringUtil.isNotEmpty(labelDataDate)) {
            if(labelInfo.getUpdateCycle().intValue() == 1) {
                fmtDataDate = DateUtil.string2StringFormat(labelDataDate, "yyyyMMdd", "yyyy-MM-dd");
            } else if(labelInfo.getUpdateCycle().intValue() == 2) {
                fmtDataDate = DateUtil.string2StringFormat(labelDataDate, "yyyyMM", "yyyy-MM");
            }
        }

        return fmtDataDate;
    }

    public int queryMarketIndexListCount(CiCustomLabelSceneRel customLabelSceneRel) {
        return this.sceneRelJDao.selectCiCustomLabelSceneRelCount(customLabelSceneRel);
    }

    public List<CiMarketTask> queryFirstLevelMarketTask() {
        return this.sceneRelJDao.selectFistLevelMarketTask();
    }

    public List<CiMarketTask> queryMarketTaskByParentId(String parentId) {
        return this.sceneRelJDao.selectMarketTaskByParentId(parentId);
    }

    public List<CiMarketScene> queryMarketScene() {
        return this.customLabelSceneRelHDao.selectMarketScene();
    }

    public List<CiCustomLabelSceneRel> queryCustomLabelSceneRelsByType(String customOrLabelId, Integer showType) throws CIServiceException {
        List result = null;

        try {
            result = this.customLabelSceneRelHDao.queryCustomLabelSceneRelsByType(customOrLabelId, showType);
            return result;
        } catch (Exception var5) {
            this.log.error("queryCustomLabelSceneRelsByType ", var5);
            throw new CIServiceException("查询营销导航列表异常", var5);
        }
    }

    public CiMarketTask queryMarketTaskById(String marketTaskId) {
        return this.marketTaskHDao.selectMarketTaskById(marketTaskId);
    }

    private String queryMinRulesDataDate(List<CiLabelRule> rules) {
        ArrayList dataDateList = new ArrayList();
        int temp;
        if(rules != null) {
            for(int dayDataDateList = 0; dayDataDateList < rules.size(); ++dayDataDateList) {
                CiLabelRule result = (CiLabelRule)rules.get(dayDataDateList);
                temp = result.getElementType().intValue();
                if(temp == 5) {
                    String i$ = result.getCalcuElement();
                    CiCustomListInfo dataDate = this.customersManagerService.queryCiCustomListInfoById(i$);
                    dataDateList.add(dataDate.getDataDate());
                } else if(temp == 2) {
                    CiLabelInfo var13 = this.ciLabelInfoJDao.selectCiLabelInfoById(Integer.valueOf(result.getCalcuElement()));
                    if(var13 != null && StringUtil.isNotEmpty(var13.getDataDate())) {
                        dataDateList.add(var13.getDataDate());
                    }
                }
            }
        }

        ArrayList var9 = new ArrayList();
        Iterator var10 = dataDateList.iterator();

        while(var10.hasNext()) {
            String var12 = (String)var10.next();
            if(var12.length() == 8) {
                var9.add(var12);
            }
        }

        String var11 = "-";
        temp = 99999999;
        if(var9.size() > 0) {
            Iterator var14 = var9.iterator();

            while(var14.hasNext()) {
                String var15 = (String)var14.next();
                int dateDateNum = Integer.valueOf(var15).intValue();
                if(dateDateNum < temp) {
                    temp = dateDateNum;
                    var11 = String.valueOf(dateDateNum);
                }
            }

            if(temp == 99999999) {
                var11 = "-";
            }
        } else if(dataDateList.size() > 0) {
            var11 = (String)dataDateList.get(0);
        }

        return var11;
    }
}
