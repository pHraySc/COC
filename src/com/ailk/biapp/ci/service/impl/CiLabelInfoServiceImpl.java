package com.ailk.biapp.ci.service.impl;

import au.com.bytecode.opencsv.CSVReader;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.core.model.CiLabelCategories;
import com.ailk.biapp.ci.dao.*;
import com.ailk.biapp.ci.entity.*;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.market.entity.CiCustomLabelSceneRel;
import com.ailk.biapp.ci.market.service.ICiMarketService;
import com.ailk.biapp.ci.model.*;
import com.ailk.biapp.ci.search.service.ISearchService;
import com.ailk.biapp.ci.service.ICiApproveService;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.service.ICiPersonNoticeService;
import com.ailk.biapp.ci.util.*;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.dimtable.model.DimTableDefine;
import com.asiainfo.biframe.dimtable.service.impl.DimTableServiceImpl;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class CiLabelInfoServiceImpl implements ICiLabelInfoService {
    private Logger log = Logger.getLogger(CiLabelInfoServiceImpl.class);
    private static Integer count = Integer.valueOf(0);
    @Autowired
    private ICiLabelInfoJDao ciLabelInfoJDao;
    @Autowired
    private ICiLabelInfoHDao ciLabelInfoHDao;
    @Autowired
    private ICiApproveStatusHDao ciApproveStatusHDao;
    @Autowired
    private IDimLabelDataStatusHDao dimLabelDataStatusHDao;
    @Autowired
    private IDimApproveStatusHDao dimApproveStatusHDao;
    @Autowired
    private ICiUserUseLabelJDao ciUserUseLabelJDao;
    @Autowired
    private ICiUserAttentionLabelHDao ciUserAttentionLabelHDao;
    @Autowired
    ICiApproveUserInfoHDao ciApproveUserInfoHDao;
    @Autowired
    ILabelQueryJdbcDao labelQueryJdbcDao;
    @Autowired
    private ICiApproveHistoryHDao ciApproveHistoryHDao;
    @Autowired
    private ICiLabelStatJDao ciLabelStatJDao;
    @Autowired
    private DimTableServiceImpl dimTableServiceImpl;
    @Autowired
    private ICiApproveService ciApproveService;
    @Autowired
    private ICiLabelSceneRelHDao ciLabelSceneRelHDao;
    @Autowired
    private ICiLabelRuleHDao ciLabelRuleHDao;
    @Autowired
    private ICiTemplateInfoHDao ciTemplateInfoHDao;
    @Autowired
    private ICiCustomGroupInfoHDao ciCustomGroupInfoHDao;
    @Autowired
    private ICiTemplateModifyInfoHDao ciTemplateModifyInfoHDao;
    @Autowired
    private ICiCustomModifyHistoryHDao ciCustomModifyHistoryHDao;
    @Autowired
    private ICiLabelHistoryInfoHDao ciLabelHistoryInfoHDao;
    @Autowired
    private ICiLabelVerticalColumnRelJDao ciLabelVerticalColumnRelJDao;
    @Autowired
    private ICiMdaColumnHDao ciMdaColumnHDao;
    @Autowired
    private ICiEnumCategoryInfoHDao categoryInfoHDao;
    @Autowired
    private ICiEnumCategoryInfoJDao categoryInfoJDao;
    @Autowired
    private ICiEnumConditionInfoJDao enumConditionInfoJDao;
    @Autowired
    private ICiEnumConditionInfoHDao ciEnumConditionInfoHDao;
    @Autowired
    private ICiLabelCountRulesHDao ciLabelCountRulesHDao;
    @Autowired
    private ICiMdaTableHDao ciMdaTableHDao;
    @Autowired
    private ICiLabelExtInfoHDao ciLabelExtInfoHDao;
    @Autowired
    private ICiDimCocIndexTableInfoHDao ciDimCocIndexTableInfoHDao;
    @Autowired
    private ICiDimCocIndexInfoHDao ciDimCocIndexInfoHDao;
    @Autowired
    private ICiLabelVerticalColumnRelHDao ciLabelVerticalColumnRelHDao;
    @Autowired
    private ICiMdaSysTableColumnJDao ciMdaSysTableColumnJDao;
    @Autowired
    private ICiPersonNoticeService personNoticeService;
    @Autowired
    private ISearchService searchService;
    @Autowired
    private ICiMarketService marketService;

    public CiLabelInfoServiceImpl() {
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiLabelInfoTree> queryAllLabelCategory() throws CIServiceException {
        ArrayList categoryList = new ArrayList();
        CacheBase cache = CacheBase.getInstance();
        CopyOnWriteArrayList allEffectiveLabelList = cache.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
        //取出缓存中所有的在有效期的标签
        Iterator i$ = allEffectiveLabelList.iterator();

        while (i$.hasNext()) {
            CiLabelInfo ciLabelInfo = (CiLabelInfo) i$.next();
            if (ciLabelInfo.getCiLabelExtInfo().getLabelLevel().intValue() == 1) {   //一级分类
                CiLabelInfoTree ciLabelInfoTree = this.getCategoryTree(ciLabelInfo, allEffectiveLabelList);
                categoryList.add(ciLabelInfoTree);
            }
        }

        return categoryList;
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public CiLabelInfoTree getCategoryTree(CiLabelInfo ciLabelInfo, List<CiLabelInfo> allEffectiveLabelList) throws CIServiceException {
        /*
            每颗树有自己的树枝(ciLabelInfo),也有自己的子树枝（树叶）ciLabelInfoTree
         */
        CiLabelInfoTree tree = new CiLabelInfoTree();
        tree.setCiLabelInfo(ciLabelInfo);//将1级标签放入
        ArrayList childTreeList = new ArrayList();
        Integer labelId = ciLabelInfo.getLabelId();
        List childrenLabels = this.queryChildrenById(labelId, allEffectiveLabelList);//查询一级分类下的二级分类

        int childTreeDepth;
        CiLabelInfoTree childTree;
        for (childTreeDepth = 0; childTreeDepth < childrenLabels.size(); ++childTreeDepth) {
            CiLabelInfo i$ = (CiLabelInfo) childrenLabels.get(childTreeDepth);
            childTree = null;
            int levelType = Integer.valueOf(Configure.getInstance().getProperty("LABEL_CATEGORY_TYPE")).intValue();
            this.log.info("levelType" + " " + levelType);
            //从ci-sichuan配置中取出为2
            if (i$.getCiLabelExtInfo().getLabelLevel().intValue() <= levelType) {
                childTree = this.getCategoryTree(i$, allEffectiveLabelList);
                //递归直到3级标签停止
            }

            if (childTree != null) {
                childTreeList.add(childTree);
            }
        }
        //childTreeList中有父标签
        if (childTreeList != null && !childTreeList.isEmpty()) {
            tree.setCiLabelInfoTree(childTreeList);
            childTreeDepth = 0;
            Iterator var11 = childTreeList.iterator();

            while (var11.hasNext()) {
                childTree = (CiLabelInfoTree) var11.next();
                if (childTreeDepth < childTree.getDepth()) {
                    childTreeDepth = childTree.getDepth();
                }
            }

            ++childTreeDepth;
            tree.setDepth(childTreeDepth);
        } else {
            tree.isLeaf = Boolean.valueOf(true);
            tree.depth = 1;
        }

        return tree;
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiLabelInfo> queryThirdLabelList(String labelId, int labelType) throws CIServiceException {
        Object labelList = new ArrayList();

        try {
            int e = Integer.valueOf(Configure.getInstance().getProperty("LABEL_CATEGORY_TYPE")).intValue();
            CacheBase cache = CacheBase.getInstance();
            CopyOnWriteArrayList allEffectiveLabel = cache.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
            List i$;
            Iterator cilabelInfo;
            CiLabelInfo ciLabelInfo;
            if (labelType == 1) {
                i$ = this.queryChildrenById(Integer.valueOf(labelId), allEffectiveLabel);
                if (e == 2) {
                    labelList = i$;
                } else {
                    cilabelInfo = i$.iterator();
                    while (cilabelInfo.hasNext()) {
                        ciLabelInfo = (CiLabelInfo) cilabelInfo.next();
                        List thirdInfos = this.queryChildrenById(ciLabelInfo.getLabelId(), allEffectiveLabel);
                        Iterator i$1 = thirdInfos.iterator();

                        while (i$1.hasNext()) {
                            CiLabelInfo ciLabelInfo2 = (CiLabelInfo) i$1.next();
                            ((List) labelList).add(ciLabelInfo2);
                        }
                    }
                }
            } else {
                Iterator i$2;
                CiLabelInfo cilabelInfo1;
                if (labelType == 2) {
                    if (e == 2) {
                        if (StringUtil.isEmpty(labelId)) {
                            i$2 = allEffectiveLabel.iterator();

                            while (i$2.hasNext()) {
                                cilabelInfo1 = (CiLabelInfo) i$2.next();
                                if (cilabelInfo1.getCiLabelExtInfo().getLabelLevel().intValue() == e) {
                                    ((List) labelList).add(cilabelInfo1);
                                }
                            }
                        } else {
                            ((List) labelList).add(cache.getEffectiveLabel(labelId));
                        }
                    } else {
                        i$ = this.queryChildrenById(Integer.valueOf(labelId), allEffectiveLabel);
                        cilabelInfo = i$.iterator();

                        while (cilabelInfo.hasNext()) {
                            ciLabelInfo = (CiLabelInfo) cilabelInfo.next();
                            ((List) labelList).add(ciLabelInfo);
                        }
                    }
                } else if (StringUtil.isEmpty(labelId)) {
                    i$2 = allEffectiveLabel.iterator();

                    while (i$2.hasNext()) {
                        cilabelInfo1 = (CiLabelInfo) i$2.next();
                        if (cilabelInfo1.getCiLabelExtInfo().getLabelLevel().intValue() == e) {
                            ((List) labelList).add(cilabelInfo1);
                        }
                    }
                } else {
                    ((List) labelList).add(cache.getEffectiveLabel(labelId));
                }
            }

            return (List) labelList;
        } catch (Exception var13) {
            this.log.error("根据标签Id查询标签失败", var13);
            throw new CIServiceException("根据标签Id查询标签失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiLabelInfo> queryThirdLabelList(String labelId, List<CiLabelInfo> allEffectiveLabelList) throws CIServiceException {
        ArrayList labelList = new ArrayList();

        try {
            CacheBase e = CacheBase.getInstance();
            if (StringUtil.isEmpty(labelId)) {
                Iterator i$ = allEffectiveLabelList.iterator();

                while (i$.hasNext()) {
                    CiLabelInfo cilabelInfo = (CiLabelInfo) i$.next();
                    if (cilabelInfo.getCiLabelExtInfo().getLabelLevel().intValue() == 3) {
                        labelList.add(cilabelInfo);
                    }
                }
            } else {
                labelList.add(e.getEffectiveLabel(labelId));
            }

            return labelList;
        } catch (Exception var7) {
            this.log.error("根据标签Id查询标签失败", var7);
            throw new CIServiceException("根据标签Id查询标签失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiLabelInfoTree> queryMapPageObj(int currPage, int pageSize, String labelId, int labelType) throws CIServiceException {
        ArrayList thirdLevelList = new ArrayList();
        try {
            CacheBase e = CacheBase.getInstance();
            CopyOnWriteArrayList allEffectiveLabel = e.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
            this.log.info("页面显示的总标签数" + allEffectiveLabel.size());
            Object thirdLevelLabels = new ArrayList();
            int labelCategoryType = Integer.valueOf(Configure.getInstance().getProperty("LABEL_CATEGORY_TYPE")).intValue();
            if (labelType != labelCategoryType) {
                ((List) thirdLevelLabels).addAll(this.queryThirdLabelList(labelId, labelType));
            } else {
                thirdLevelLabels = this.queryThirdLabelList(labelId, allEffectiveLabel);
            }

            ArrayList thirdLabelListB = new ArrayList();
            Iterator begin = ((List) thirdLevelLabels).iterator();

            while (begin.hasNext()) {
                CiLabelInfo end = (CiLabelInfo) begin.next();
                if (this.queryChildrenById(end.getLabelId()).size() > 0) {
                    thirdLabelListB.add(end);
                }
            }

            int var18 = (currPage - 1) * pageSize;
            int var19 = var18 + pageSize;
            if (var19 > thirdLabelListB.size()) {
                var19 = thirdLabelListB.size();
            }

            List currPageList = thirdLabelListB.subList(var18, var19);

            for (int i = 0; i < currPageList.size(); ++i) {
                CiLabelInfo ciLabelInfo = (CiLabelInfo) currPageList.get(i);
                CiLabelInfoTree thirdLevelLabelTree = this.getLabelTree(ciLabelInfo, allEffectiveLabel);
                if (thirdLevelLabelTree != null) {
                    thirdLevelList.add(thirdLevelLabelTree);
                }
            }

            return thirdLevelList;
        } catch (Exception var17) {
            this.log.error("查询标签地图分页记录失败", var17);
            throw new CIServiceException("查询标签地图分页记录失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiLabelInfo> queryPageList(int currPage, int pageSize, CiLabelInfo ciLabelInfo) throws CIServiceException {
        List pageList = null;

        try {
            pageList = this.ciLabelInfoJDao.getPageListBySql(currPage, pageSize, ciLabelInfo, "1");
            Iterator e = pageList.iterator();

            while (e.hasNext()) {
                CiLabelInfo label = (CiLabelInfo) e.next();
                if (StringUtil.isNotEmpty(label.getCreateUserId())) {
                    label.setCreateUserName(PrivilegeServiceUtil.getUserById(label.getCreateUserId()).getUsername());
                }
            }

            return pageList;
        } catch (Exception var7) {
            this.log.error("查询分页记录失败", var7);
            throw new CIServiceException("查询分页记录失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiLabelInfoTree> queryMapPageObject(int currPage, int pageSize, int labelId) throws CIServiceException {
        ArrayList secondLList = new ArrayList();

        try {
            CacheBase e = CacheBase.getInstance();
            CopyOnWriteArrayList allEffectiveLabel = e.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
            List secondLLabels = this.queryChildrenById(Integer.valueOf(labelId), allEffectiveLabel);
            int begin = (currPage - 1) * pageSize;
            int end = begin + pageSize;
            if (end > secondLLabels.size()) {
                end = secondLLabels.size();
            }

            List currPageList = secondLLabels.subList(begin, end);

            for (int i = 0; i < currPageList.size(); ++i) {
                CiLabelInfo ciLabelInfo = (CiLabelInfo) currPageList.get(i);
                CiLabelInfoTree secondLLabelTree = this.getLabelTree(ciLabelInfo, allEffectiveLabel);
                if (secondLLabelTree != null) {
                    secondLList.add(secondLLabelTree);
                }
            }

            return secondLList;
        } catch (Exception var14) {
            this.log.error("查询分页记录失败", var14);
            throw new CIServiceException("查询分页记录失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public CiLabelInfoTree getLabelTree(CiLabelInfo ciLabelInfo, List<CiLabelInfo> allEffectiveLabelList) {
        CacheBase cache = CacheBase.getInstance();
        CiLabelInfoTree tree = new CiLabelInfoTree();
        tree.setCiLabelInfo(ciLabelInfo);
        ArrayList childTreeList = new ArrayList();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMM");
        Integer labelId = ciLabelInfo.getLabelId();
        List childrenLabels = this.queryChildrenById(labelId, allEffectiveLabelList);

        int info;
        for (info = 0; info < childrenLabels.size(); ++info) {
            CiLabelInfo i$ = (CiLabelInfo) childrenLabels.get(info);
            Date childTree = i$.getEffecTime();
            if (null != childTree) {
                String labelSceneNames = sdFormat.format(childTree);
                i$.setEffectDate(DateUtil.getOffsetDateByDate(labelSceneNames, -1, 0));
            }

            StringBuilder var19 = new StringBuilder();
            Set ciLabelSceneRels = i$.getCiLabelExtInfo().getCiLabelSceneRels();
            Iterator it = ciLabelSceneRels.iterator();

            while (it.hasNext()) {
                CiLabelSceneRel mytree = (CiLabelSceneRel) it.next();
                var19.append(cache.getDimScene(mytree.getId().getSceneId()));
                if (it.hasNext()) {
                    var19.append("&nbsp;&nbsp;");
                }
            }

            i$.setLabelSceneNames(var19.toString());
            CiLabelInfoTree var20 = this.getLabelTree(i$, allEffectiveLabelList);
            if (var20 != null) {
                childTreeList.add(var20);
            }
        }

        if (childTreeList != null && !childTreeList.isEmpty()) {
            tree.setCiLabelInfoTree(childTreeList);
            info = 0;
            Iterator var16 = childTreeList.iterator();

            while (var16.hasNext()) {
                CiLabelInfoTree var18 = (CiLabelInfoTree) var16.next();
                if (info < var18.getDepth()) {
                    info = var18.getDepth();
                }
            }

            ++info;
            tree.setDepth(info);
        } else {
            tree.isLeaf = Boolean.valueOf(true);
            tree.depth = 1;
        }

        CiLabelInfo var17 = tree.getCiLabelInfo();
        if (var17.getUpdateCycle().intValue() == 1) {
            var17.setDataDateStr(DateUtil.string2StringFormat(var17.getDataDate(), "yyyyMMdd", "yyyy-MM-dd"));
        } else {
            var17.setDataDateStr(DateUtil.string2StringFormat(var17.getDataDate(), "yyyyMM", "yyyy-MM"));
        }

        return tree;
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public int queryTotalPageCount(CiLabelInfo ciLabelInfo) throws CIServiceException {
        boolean count = false;

        try {
            int count1 = this.ciLabelInfoJDao.getCountBySql(ciLabelInfo, "1");
            return count1;
        } catch (Exception var4) {
            this.log.error("查询总记录数失败", var4);
            throw new CIServiceException("查询总记录数失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public int querySecondLabelCountByFirstLabel(Integer labelId) throws CIServiceException {
        boolean count = false;

        try {
            List e = this.queryChildrenById(labelId);
            int count1 = e.size();
            return count1;
        } catch (Exception var4) {
            this.log.error("查询总记录数失败", var4);
            throw new CIServiceException("查询总记录数失败");
        }
    }

    public void add(CiLabelInfo label) throws CIServiceException {
        try {
            Integer e = label.getLabelId();
            this.ciLabelInfoHDao.insertCiLabelInfo(label);
            if (null != e) {
                this.addCiLabelHistoryInfo(label, Integer.valueOf(2));
            } else {
                this.addCiLabelHistoryInfo(label, Integer.valueOf(1));
            }

            this.ciLabelSceneRelHDao.deleteLabelSceneByLabelId(String.valueOf(label.getLabelId()));
            List scenes = label.getSceneList();
            if (scenes != null) {
                Iterator i$ = scenes.iterator();

                while (i$.hasNext()) {
                    CiLabelSceneRel item = (CiLabelSceneRel) i$.next();
                    item.getId().setLabelId(label.getLabelId());
                    this.ciLabelSceneRelHDao.insertCiLabelSceneRel(item);
                }
            }

        } catch (Exception var6) {
            this.log.error("新增标签失败", var6);
            throw new CIServiceException("新增标签失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public CiLabelInfo queryCiLabelInfoById(Integer labelId) throws CIServiceException {
        CiLabelInfo ciLabelInfo = null;

        try {
            ciLabelInfo = this.ciLabelInfoJDao.selectCiLabelInfoById(labelId);
            return ciLabelInfo;
        } catch (Exception var4) {
            this.log.error("根据标签Id查询标签失败", var4);
            throw new CIServiceException("根据标签Id查询标签失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public CiLabelInfo queryCiLabelInfoByIdFullLoad(Integer labelId) throws CIServiceException {
        CiLabelInfo ciLabelInfo = null;

        try {
            ciLabelInfo = this.ciLabelInfoHDao.selectCiLabelInfoById(labelId);
            return ciLabelInfo;
        } catch (Exception var4) {
            this.log.error("根据标签Id查询标签失败", var4);
            throw new CIServiceException("根据标签Id查询标签失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiLabelInfo> queryChildrenById(Integer labelId) throws CIServiceException {
        ArrayList labelList = new ArrayList();

        try {
            CacheBase e = CacheBase.getInstance();
            CopyOnWriteArrayList allEffectiveLabel = e.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
            String lableNum = Configure.getInstance().getProperty("LABEL_NUMBER");
            Iterator i$ = allEffectiveLabel.iterator();

            while (i$.hasNext()) {
                CiLabelInfo cilabelInfo = (CiLabelInfo) i$.next();
                if (cilabelInfo.getParentId().equals(labelId)) {
                    if ("true".equals(lableNum)) {
                        this.count(cilabelInfo.getLabelId().intValue(), allEffectiveLabel);
                        cilabelInfo.setLabelNum(count);
                    }

                    labelList.add(cilabelInfo);
                    count = Integer.valueOf(0);
                }
            }

            return labelList;
        } catch (Exception var8) {
            this.log.error("根据标签Id查询标签失败", var8);
            throw new CIServiceException("根据标签Id查询标签失败");
        }
    }

    public void count(int labelId, List<CiLabelInfo> list) {
        Iterator i$ = list.iterator();

        while (i$.hasNext()) {
            CiLabelInfo ciLabel = (CiLabelInfo) i$.next();
            if (labelId == ciLabel.getParentId().intValue()) {
                Integer var5 = count;
                count = Integer.valueOf(count.intValue() + 1);
                this.count(ciLabel.getLabelId().intValue(), list);
            }
        }

    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiLabelInfo> queryChildrenById(Integer labelId, List<CiLabelInfo> allEffectiveLabelList) throws CIServiceException {
        ArrayList labelList = new ArrayList();

        try {
            Iterator e = allEffectiveLabelList.iterator();

            while (e.hasNext()) {
                CiLabelInfo cilabelInfo = (CiLabelInfo) e.next();
                if (cilabelInfo.getParentId().equals(labelId)) {
                    labelList.add(cilabelInfo);
                }
            }

            return labelList;
        } catch (Exception var6) {
            this.log.error("根据标签Id查询标签失败", var6);
            throw new CIServiceException("根据标签Id查询标签失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<DimLabelDataStatus> queryDimLabelDataStatus() throws CIServiceException {
        List dimLabelDataStatusList = null;

        try {
            dimLabelDataStatusList = this.dimLabelDataStatusHDao.select();
            return dimLabelDataStatusList;
        } catch (Exception var3) {
            this.log.error("查询标签数据状态错误", var3);
            throw new CIServiceException("查询数据状态错误");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<DimApproveStatus> queryDimApproveStatus() throws CIServiceException {
        List dimApproveStatus = null;

        try {
            dimApproveStatus = this.dimApproveStatusHDao.select();
            return dimApproveStatus;
        } catch (Exception var3) {
            this.log.error("查询审批状态维度错误", var3);
            throw new CIServiceException("查询审批状态维表错误");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public Boolean hasSameLabel(CiLabelInfo ciLabelInfo) {
        try {
            List e = this.ciLabelInfoJDao.selectSimilarityLabelNameList(ciLabelInfo);
            ArrayList labelList = new ArrayList();
            Iterator labelId = e.iterator();

            while (labelId.hasNext()) {
                CiLabelInfo tempLabelInfo = (CiLabelInfo) labelId.next();
                if (ciLabelInfo.getLabelName().equalsIgnoreCase(tempLabelInfo.getLabelName().trim())) {
                    labelList.add(tempLabelInfo);
                }
            }

            if (labelList != null && !labelList.isEmpty()) {
                Integer labelId1 = ciLabelInfo.getLabelId();
                if (labelId1 != null && labelList.size() == 1 && ((CiLabelInfo) labelList.get(0)).getLabelId().equals(labelId1)) {
                    return Boolean.valueOf(false);//修改并发布，只能有一个，多余一个就有重复标签
                } else {
                    return Boolean.valueOf(true);
                    //labelId1==null则是新加标签，新加标签还能查出label_name说明有重名
                    //如果labelId1!=null，说明是修改标签，库中应该只有一个当前标签名，超过一个，则重名（理论上size不可能超过1）
                }
            } else {
                return Boolean.valueOf(false);//增加并发布
            }
        } catch (Exception var6) {
            this.log.error("查找标签名重名错误", var6);
            throw new CIServiceException("查找标签名重名错误");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiLabelInfo> querySimilarityLabelNameList(CiLabelInfo ciLabelInfo) {
        List labelList = this.ciLabelInfoJDao.selectSimilarityLabelNameList(ciLabelInfo);
        return labelList;
    }

    public void modify(CiLabelInfo ciLabelInfo) throws CIServiceException {
        try {
            this.ciLabelInfoHDao.updateCiLabelInfo(ciLabelInfo);
            this.addCiLabelHistoryInfo(ciLabelInfo, Integer.valueOf(2));
        } catch (Exception var3) {
            this.log.error("修改标签失败", var3);
            throw new CIServiceException("修改标签失败");
        }
    }

    public void delete(List<Integer> labelIds) throws CIServiceException {
        try {
            CiLabelInfo e = null;
            Iterator iter = labelIds.iterator();

            while (iter.hasNext()) {
                Integer labelId = (Integer) iter.next();
                e = this.ciLabelInfoJDao.selectCiLabelInfoById(labelId);
                CacheBase cache = CacheBase.getInstance();
                if (cache.getEffectiveLabel(labelId + "") != null) {
                    cache.removeEffectiveLabel(e.getLabelId().toString());
                }

                e.setDataStatusId(Integer.valueOf(6));
                this.ciLabelInfoHDao.updateCiLabelInfo(e);
                this.addCiLabelHistoryInfo(e, Integer.valueOf(3));
            }

        } catch (Exception var6) {
            this.log.error("删除标签失败", var6);
            throw new CIServiceException("删除标签失败");
        }
    }

    public void stopUseAndOffLineLabel(CiLabelInfo ciLabelInfo, Integer statusId) throws CIServiceException {
        try {
            ciLabelInfo.setDataStatusId(statusId);
            this.ciLabelInfoHDao.updateCiLabelInfo(ciLabelInfo);
            this.modifyCiTemplateInfoAndAddHistory(ciLabelInfo.getLabelId().toString(), 2);
            this.modifyCiCustomGropInfoAndAddHistory(ciLabelInfo.getLabelId().toString(), 1);
            if (statusId.intValue() == 4) {
                this.addCiLabelHistoryInfo(ciLabelInfo, Integer.valueOf(6));
            } else if (statusId.intValue() == 5) {
                this.addCiLabelHistoryInfo(ciLabelInfo, Integer.valueOf(7));
            }

            CacheBase.getInstance().removeEffectiveLabel(ciLabelInfo.getLabelId().toString());
            String e = Configure.getInstance().getProperty("SEARCH_BY_DB_FLAG");
            if (StringUtil.isNotEmpty(e) && "false".equalsIgnoreCase(e)) {
                this.searchService.deleteLabelIndexByLabelId(ciLabelInfo.getLabelId());
            }
        } catch (Exception var4) {
            if (statusId.intValue() == 4) {
                this.log.error("停用标签失败", var4);
                throw new CIServiceException("停用标签失败");
            }

            if (statusId.intValue() == 5) {
                this.log.error("标签下线失败", var4);
                throw new CIServiceException("标签下线失败");
            }
        }

    }

    public void onLineLabel(CiLabelInfo ciLabelInfo) throws CIServiceException {
        try {
            ciLabelInfo.setDataStatusId(Integer.valueOf(2));
            this.ciLabelInfoHDao.updateCiLabelInfo(ciLabelInfo);
            this.addCiLabelHistoryInfo(ciLabelInfo, Integer.valueOf(8));
            this.modifyCiCustomGropInfoAndAddHistory(ciLabelInfo.getLabelId().toString(), 0);
            this.modifyCiTemplateInfoAndAddHistory(ciLabelInfo.getLabelId().toString(), 1);
            CacheBase.getInstance().addEffectiveLabel(ciLabelInfo.getLabelId().toString(), ciLabelInfo);
            String e = Configure.getInstance().getProperty("SEARCH_BY_DB_FLAG");
            if (StringUtil.isNotEmpty(e) && "false".equalsIgnoreCase(e)) {
                this.searchService.addLabelIndexByLabelId(ciLabelInfo.getLabelId());
            }

        } catch (Exception var3) {
            this.log.error("标签启用失败", var3);
            throw new CIServiceException("标签启用失败");
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiApproveUserInfo> queryApproveUserInfoList(String approveRoleId) throws CIServiceException {
        List approveUserInfoList = null;

        try {
            String e = PrivilegeServiceUtil.getUserId();
            Integer deptId = PrivilegeServiceUtil.getUserDeptId(e);
            approveUserInfoList = this.ciApproveUserInfoHDao.select(approveRoleId, String.valueOf(deptId));
            return approveUserInfoList;
        } catch (Exception var5) {
            this.log.error("查询标签审批人员信息", var5);
            throw new CIServiceException("标签提交审核失败");
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiApproveUserInfo> queryInfoRoleApproveUserInfoList(String approveRoleId) throws CIServiceException {
        List approveUserInfoList = null;

        try {
            approveUserInfoList = this.ciApproveUserInfoHDao.selectByApproveRoleId(approveRoleId);
            return approveUserInfoList;
        } catch (Exception var4) {
            this.log.error("查询标签审批人员信息", var4);
            throw new CIServiceException("标签提交审核失败");
        }
    }

    public String getApproveStatusByLabelId(Integer labelId) throws CIServiceException {
        return this.ciApproveStatusHDao.selectById(labelId).getCurrApproveStatusId();
    }

    public void userAttentionLabelRecord(Integer labelId, String userId) throws CIServiceException {
        try {
            Date e = new Date();
            CiUserAttentionLabel ciUserAttentionLabel = new CiUserAttentionLabel();
            CiUserAttentionLabelId ciUserAttentionLabelId = new CiUserAttentionLabelId();
            ciUserAttentionLabelId.setUserId(userId);
            ciUserAttentionLabelId.setLabelId(labelId);
            ciUserAttentionLabel.setId(ciUserAttentionLabelId);
            ciUserAttentionLabel.setAttentionTime(e);
            this.ciUserAttentionLabelHDao.insertOrUpdateCiUserAttentionLabel(ciUserAttentionLabel);
        } catch (Exception var6) {
            this.log.error("增加标签关注记录失败", var6);
            throw new CIServiceException("增加标签关注记录失败");
        }
    }

    public void deleteUserAttentionLabelRecord(Integer labelId, String userId) throws CIServiceException {
        try {
            CiUserAttentionLabel e = new CiUserAttentionLabel();
            CiUserAttentionLabelId ciUserAttentionLabelId = new CiUserAttentionLabelId();
            ciUserAttentionLabelId.setUserId(userId);
            ciUserAttentionLabelId.setLabelId(labelId);
            e.setId(ciUserAttentionLabelId);
            this.ciUserAttentionLabelHDao.deleteCiUserAttentionLabel(e);
        } catch (Exception var5) {
            this.log.error("删除标签关注记录失败", var5);
            throw new CIServiceException("删除标签关注记录失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiLabelInfo> queryAllEffectiveLabel() throws CIServiceException {
        List list = null;
        try {
            list = this.ciLabelInfoHDao.selectEffectiveCiLabelInfo();
//            this.log.info("开始隐藏标签："+list.size());
//            List removeList=new ArrayList();
//            Map <String,String> hiddenLabel=CacheBase.getInstance().getHiddenLabelMap();
//            if (hiddenLabel==null){
//                CacheBase.getInstance().initHiddenLabelMap();
//            }
//            this.log.info("需要隐藏的标签："+hiddenLabel.toString());
//            int k=0;
//            for (CiLabelInfo ciLabelInfo:(List<CiLabelInfo>)list){
//                String hiddenId=hiddenLabel.get(String.valueOf(ciLabelInfo.getLabelId()));
//                if (hiddenId!=null){
//                    this.log.info("过滤标签："+ciLabelInfo.getLabelId()+"--"+ciLabelInfo.getLabelName());
//                    removeList.add(k);
//                }
//                k++;
//            }
//            for (int j=0;j<removeList.size();j++){
//                int index=(Integer) removeList.get(j);
//                list.remove(index);
//            }
//            this.log.info("结束隐藏标签："+list.size());
            return list;
        } catch (Exception var4) {
            String message = "查询所有有效的标签错误";
            this.log.error(message, var4);
            throw new CIServiceException(message, var4);
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public CiUserAttentionLabel queryUserAttentionLabelRecord(Integer labelId, String userId) throws CIServiceException {
        CiUserAttentionLabel ciUserAttentionLabel = null;

        try {
            CiUserAttentionLabelId e = new CiUserAttentionLabelId();
            e.setLabelId(labelId);
            e.setUserId(userId);
            ciUserAttentionLabel = this.ciUserAttentionLabelHDao.selectById(e);
            return ciUserAttentionLabel;
        } catch (Exception var5) {
            this.log.error("删除标签关注记录失败", var5);
            throw new CIServiceException("删除标签关注记录失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<TreeNode> queryLabelTree() throws CIServiceException {
        ArrayList treeList = new ArrayList();

        try {
            CacheBase e = CacheBase.getInstance();
            boolean message1 = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
            boolean hasAuthority = false;
            String userId = null;
            if (message1) {
                userId = PrivilegeServiceUtil.getUserId();
                hasAuthority = message1 && !PrivilegeServiceUtil.isAdminUser(userId);
            }

            CopyOnWriteArrayList labelIds = e.getKeyList("ALL_EFFECTIVE_LABEL_MAP");
            String level = Configure.getInstance().getProperty("LABEL_TREE_START_LEVEL").trim();
            int startLevel = Integer.valueOf(level).intValue();
            Iterator i$ = labelIds.iterator();

            while (true) {
                CiLabelInfo ciLabelInfo;
                CiLabelExtInfo ext;
                do {
                    do {
                        if (!i$.hasNext()) {
                            return treeList;
                        }

                        String idStr = (String) i$.next();
                        ciLabelInfo = null;
                        if (hasAuthority) {
                            ciLabelInfo = e.getEffectiveLabelByUser(idStr, userId);
                        } else {
                            ciLabelInfo = e.getEffectiveLabel(idStr);
                        }

                        ext = ciLabelInfo.getCiLabelExtInfo();
                    } while (ext == null);
                } while (startLevel != 1 && ext.getLabelLevel().intValue() < startLevel);

                TreeNode treeNode = this.newTreeNode(ciLabelInfo, ext);
                treeList.add(treeNode);
            }
        } catch (Exception var14) {
            String message = "标签树查询错误";
            this.log.error(message, var14);
            throw new CIServiceException(message, var14);
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public Integer queryIdByNameForImport(String labelName) throws CIServiceException {
        Integer labelId = null;

        try {
            CiLabelInfo e = new CiLabelInfo();
            e.setLabelName(labelName.replace(" ", ""));
            List similarLabelList = this.ciLabelInfoJDao.selectSimilarityLabelNameList(e);
            Iterator i$ = similarLabelList.iterator();

            while (i$.hasNext()) {
                CiLabelInfo tempLabelInfo = (CiLabelInfo) i$.next();
                if (e.getLabelName().equalsIgnoreCase(tempLabelInfo.getLabelName().trim())) {
                    labelId = tempLabelInfo.getLabelId();
                }
            }

            return labelId;
        } catch (Exception var7) {
            this.log.error("根据标签名称查询标签id失败", var7);
            throw new CIServiceException("根据标签名称查询标签id失败");
        }
    }

    private TreeNode newTreeNode(CiLabelInfo ciLabelInfo, CiLabelExtInfo ext) {
        TreeNode treeNode = new TreeNode();
        treeNode.setId(ciLabelInfo.getLabelId() + "");
        treeNode.setName(ciLabelInfo.getLabelName());
        String level = Configure.getInstance().getProperty("LABEL_TREE_START_LEVEL").trim();
        int startLevel = Integer.valueOf(level).intValue();
        if (ext.getLabelLevel().intValue() == startLevel) {
            treeNode.setpId("0");
            treeNode.setOpen(Boolean.FALSE);
            treeNode.setIsParent(Boolean.TRUE);
            treeNode.setTip(ciLabelInfo.getLabelName());
        } else {
            treeNode.setpId(ciLabelInfo.getParentId() + "");
            treeNode.setIsParent(Boolean.FALSE);
            treeNode.setOpen(Boolean.FALSE);
            if (ciLabelInfo.getUpdateCycle().intValue() == 2) {
                treeNode.setTip("月周期");
            } else {
                treeNode.setTip("日周期");
            }

            ActionContext ctx = ActionContext.getContext();
            HttpServletRequest request = (HttpServletRequest) ctx.get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
            String contextPath = request.getContextPath();
            String attrIconPath;
            if (ciLabelInfo.getLabelTypeId().intValue() == 1) {
                attrIconPath = contextPath + Configure.getInstance().getProperty("SIGN_LABEL_FOR_TREE");
                treeNode.setIcon(attrIconPath);
            } else if (ciLabelInfo.getLabelTypeId().intValue() == 2) {
                attrIconPath = contextPath + Configure.getInstance().getProperty("SCORE_LABEL_FOR_TREE");
                treeNode.setIcon(attrIconPath);
            } else if (ciLabelInfo.getLabelTypeId().intValue() == 3) {
                attrIconPath = contextPath + Configure.getInstance().getProperty("ATTR_LABEL_FOR_TREE");
                treeNode.setIcon(attrIconPath);
            }
        }

        if (ext != null && ext.getLabelId() != null) {
            if (ext.getIsStatUserNum().intValue() == 0) {
                treeNode.setClick(Boolean.FALSE);
            }

            treeNode.setParam(this.newLabelTreeModel(ciLabelInfo, ext));
        }

        return treeNode;
    }

    private LabelInfoTreeModel newLabelTreeModel(CiLabelInfo ciLabelInfo, CiLabelExtInfo ext) {
        LabelInfoTreeModel model = new LabelInfoTreeModel();
        model.setUpdateCycle(ciLabelInfo.getUpdateCycle());
        model.setLabelTypeId(ciLabelInfo.getLabelTypeId());
        model.setLabelLevel(ext.getLabelLevel());
        model.setCtrlTypeId(ext.getCtrlTypeId());
        model.setMinVal(ext.getMinVal());
        model.setMaxVal(ext.getMaxVal());
        model.setIsLeaf(ext.getIsLeaf());
        model.setIsStatUserNum(ext.getIsStatUserNum());
        model.setCustomNum(ext.getCustomNum());
        model.setAttrVal(ext.getAttrVal());
        if (ext.getIsStatUserNum() != null && ext.getIsStatUserNum().intValue() == 1) {
            model.setIsCanDrag(Integer.valueOf(1));
        } else {
            model.setIsCanDrag(Integer.valueOf(0));
        }

        Date d = ciLabelInfo.getEffecTime();
        if (null != d) {
            String e = (new SimpleDateFormat("yyyyMM")).format(d);
            model.setEffectDate(DateUtil.getOffsetDateByDate(e, -1, 0));

            try {
                model.setEffectTime((new SimpleDateFormat("yyyy-MM-dd")).format(d));
            } catch (Exception var8) {
                this.log.error("model.setEffectTime error", var8);
            }
        }

        try {
            if (null == ciLabelInfo.getFailTime()) {
                ciLabelInfo.setFailTime(DateUtil.string2Date("2099-06-30", "yyyy-MM-dd"));
            }

            model.setFailTime((new SimpleDateFormat("yyyy-MM-dd")).format(ciLabelInfo.getFailTime()));
        } catch (Exception var7) {
            this.log.error("model.setFailTime error", var7);
        }

        return model;
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiLabelInfo> queryLabelName(String labelName, Integer parentId) throws CIServiceException {
        int count = 0;
        ArrayList resultList = new ArrayList();
        ArrayList ciLabelInfoList = new ArrayList();
        if (parentId != null) {
            this.getLabel((Integer) parentId, ciLabelInfoList);
            Iterator i$ = ciLabelInfoList.iterator();

            while (i$.hasNext()) {
                CiLabelInfo ciLabelInfo = (CiLabelInfo) i$.next();
                if (count >= 9) {
                    break;
                }

                CiLabelExtInfo ext = ciLabelInfo.getCiLabelExtInfo();
                if (ext != null && ext.getLabelLevel().intValue() >= 3 && 0 != ext.getIsStatUserNum().intValue() && ciLabelInfo.getLabelName().toLowerCase().contains(labelName.toLowerCase())) {
                    resultList.add(ciLabelInfo);
                    ++count;
                }
            }
        } else {
            this.getLabel((String) labelName, resultList);
        }

        return resultList;
    }

    private void getLabel(Integer parentId, List<CiLabelInfo> ciLabelInfoList) {
        CacheBase cache = CacheBase.getInstance();

        try {
            boolean e = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
            boolean hasAuthority = false;
            String userId = null;
            if (e) {
                userId = PrivilegeServiceUtil.getUserId();
                hasAuthority = e && !PrivilegeServiceUtil.isAdminUser(userId);
            }

            CopyOnWriteArrayList labelIds = cache.getKeyList("ALL_EFFECTIVE_LABEL_MAP");
            Iterator i$ = labelIds.iterator();

            while (i$.hasNext()) {
                String idStr = (String) i$.next();
                CiLabelInfo ciLabelInfo = null;
                if (hasAuthority) {
                    ciLabelInfo = cache.getEffectiveLabelByUser(idStr, userId);
                } else {
                    ciLabelInfo = cache.getEffectiveLabel(idStr);
                }

                if (ciLabelInfo.getParentId().compareTo(parentId) == 0) {
                    ciLabelInfoList.add(ciLabelInfo);
                    this.getLabel(Integer.valueOf(idStr), ciLabelInfoList);
                }
            }
        } catch (Exception var11) {
            this.log.error("根据父Id获得标签错误", var11);
            var11.printStackTrace();
        }

    }

    private void getLabel(String labelName, List<CiLabelInfo> resultList) {
        int count = 0;
        CacheBase cache = CacheBase.getInstance();

        try {
            boolean e = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
            boolean hasAuthority = false;
            String userId = null;
            if (e) {
                userId = PrivilegeServiceUtil.getUserId();
                hasAuthority = e && !PrivilegeServiceUtil.isAdminUser(userId);
            }

            CopyOnWriteArrayList labelIds = cache.getKeyList("ALL_EFFECTIVE_LABEL_MAP");
            Iterator i$ = labelIds.iterator();

            while (i$.hasNext()) {
                String idStr = (String) i$.next();
                if (count > 9) {
                    break;
                }

                CiLabelInfo ciLabelInfo = null;
                if (hasAuthority) {
                    ciLabelInfo = cache.getEffectiveLabelByUser(idStr, userId);
                } else {
                    ciLabelInfo = cache.getEffectiveLabel(idStr);
                }

                CiLabelExtInfo ext = ciLabelInfo.getCiLabelExtInfo();
                if (ext != null && ext.getLabelLevel().intValue() >= 3 && 0 != ext.getIsStatUserNum().intValue() && ciLabelInfo.getLabelName().toLowerCase().contains(labelName.toLowerCase())) {
                    resultList.add(ciLabelInfo);
                    ++count;
                }
            }
        } catch (Exception var13) {
            this.log.error("根据标签名称获得标签错误", var13);
            var13.printStackTrace();
        }

    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<LabelDetailInfo> querySysRecommendLabel(int currPage, int pageSize, String dataScope) {
        List results = null;

        try {
            CacheBase e = CacheBase.getInstance();
            CopyOnWriteArrayList allEffectiveLabel = e.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
            results = this.ciUserUseLabelJDao.getSysRecommendLabel(currPage, pageSize, dataScope);
            Iterator i$ = results.iterator();

            while (i$.hasNext()) {
                LabelDetailInfo info = (LabelDetailInfo) i$.next();
                info.setHasAlarm(this.hasAlarm(info));
                String dimCtiy = "";
                Set dimCitySet = null;
                String labelId = info.getLabelId();

                try {
                    CiLabelInfo currentLabelPath = CacheBase.getInstance().getEffectiveLabel(info.getLabelId());
                    CiLabelExtInfo ciLabelInfo = currentLabelPath.getCiLabelExtInfo();
                    CiMdaSysTableColumn labelSceneNames = ciLabelInfo.getCiMdaSysTableColumn();
                    dimCtiy = labelSceneNames.getDimTransId();
                    dimCitySet = CacheBase.getDimCitySet();
                } catch (Exception var18) {
                    this.log.error("地市标签查询查询出对应的地市区县表失败,标签ID为：--" + labelId + "--", var18);
                }

                String currentLabelPath1;
                if (StringUtil.isNotEmpty(dimCtiy) && dimCitySet != null && dimCitySet.size() > 0 && dimCitySet.contains(dimCtiy.toUpperCase())) {
                    currentLabelPath1 = PrivilegeServiceUtil.getUserId();
                    List ciLabelInfo1 = PrivilegeServiceUtil.getAllUserCityIds(currentLabelPath1);
                    HashSet labelSceneNames1 = new HashSet(ciLabelInfo1);
                    if (labelSceneNames1 != null && labelSceneNames1.size() > 0 && StringUtil.isNotEmpty(info.getAttrVal()) && !labelSceneNames1.contains(info.getAttrVal())) {
                        info.setIsInPrilabel(0);
                    }
                }

                currentLabelPath1 = this.getCurrentLabelPath(info, allEffectiveLabel);
                info.setCurrentLabelPath(currentLabelPath1);
                CiLabelInfo ciLabelInfo2 = e.getEffectiveLabel(Integer.valueOf(info.getLabelId()) + "");
                StringBuilder labelSceneNames2 = new StringBuilder();
                Set ciLabelSceneRels = ciLabelInfo2.getCiLabelExtInfo().getCiLabelSceneRels();
                Iterator it = ciLabelSceneRels.iterator();

                while (it.hasNext()) {
                    CiLabelSceneRel labelInfo = (CiLabelSceneRel) it.next();
                    labelSceneNames2.append(e.getDimScene(labelInfo.getId().getSceneId()));
                    if (it.hasNext()) {
                        labelSceneNames2.append("&nbsp;&nbsp;");
                    }
                }

                info.setLabelSceneNames(labelSceneNames2.toString());
                LabelDetailInfo labelInfo1 = e.getHotLabelByKey("HOT_LABELS", info.getLabelId());
                if (labelInfo1 != null) {
                    info.setIsHot("true");
                }
            }

            return results;
        } catch (Exception var19) {
            this.log.error("查询系统推荐标签失败", var19);
            throw new CIServiceException("查询系统推荐标签失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<LabelDetailInfo> querySysRecommendLabelTask() {
        List results = null;

        try {
            results = this.ciUserUseLabelJDao.getSysRecommendLabelTask();
            return results;
        } catch (Exception var3) {
            this.log.error("后台查询系统推荐标签失败", var3);
            throw new CIServiceException("后台查询系统推荐标签失败");
        }
    }

    public Long querySysRecommendLabelNum() {
        Long results = null;

        try {
            results = this.ciUserUseLabelJDao.getSysRecommendLabelNum();
            if (results.longValue() >= (long) ServiceConstants.SHOW_RECOMMEND_LABEL_NUM) {
                results = Long.valueOf((long) ServiceConstants.SHOW_RECOMMEND_LABEL_NUM);
            }

            return results;
        } catch (Exception var3) {
            this.log.error("查询系统推荐标签数失败", var3);
            throw new CIServiceException("查询系统推荐标签数失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<LabelDetailInfo> queryUserUsedLabel(int currPage, int pageSize, String userId, String orderType, String dataScope) throws CIServiceException {
        List results = null;

        try {
            CacheBase e = CacheBase.getInstance();
            CopyOnWriteArrayList allEffectiveLabel = e.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
            results = this.ciUserUseLabelJDao.getUserUsedLabel(currPage, pageSize, userId, orderType, dataScope);
            Iterator i$ = results.iterator();

            while (i$.hasNext()) {
                LabelDetailInfo info = (LabelDetailInfo) i$.next();
                info.setHasAlarm(this.hasAlarm(info));
                Long customNum = this.getCustomNum(info.getDataDate(), Integer.valueOf(info.getLabelId()), Integer.valueOf(-1), Integer.valueOf(-1), Long.valueOf(info.getCustomNum() == null ? 0L : Long.valueOf(info.getCustomNum()).longValue()), (String) null);
                info.setCustomNum(customNum.toString());
                String dimCtiy = "";
                Set dimCitySet = null;
                String labelId = info.getLabelId();

                try {
                    CiLabelInfo currentLabelPath = CacheBase.getInstance().getEffectiveLabel(info.getLabelId());
                    CiLabelExtInfo AllUserCityIdsSet = currentLabelPath.getCiLabelExtInfo();
                    CiMdaSysTableColumn ciMdaSysTableColumn = AllUserCityIdsSet.getCiMdaSysTableColumn();
                    dimCtiy = ciMdaSysTableColumn.getDimTransId();
                    dimCitySet = CacheBase.getDimCitySet();
                } catch (Exception var18) {
                    this.log.error("地市标签查询查询出对应的地市区县表失败,标签ID为：--" + labelId + "--", var18);
                }

                if (StringUtil.isNotEmpty(dimCtiy) && dimCitySet != null && dimCitySet.size() > 0 && dimCitySet.contains(dimCtiy.toUpperCase())) {
                    if (StringUtil.isEmpty(userId)) {
                        userId = PrivilegeServiceUtil.getUserId();
                    }

                    List currentLabelPath1 = PrivilegeServiceUtil.getAllUserCityIds(userId);
                    HashSet AllUserCityIdsSet1 = new HashSet(currentLabelPath1);
                    if (AllUserCityIdsSet1 != null && AllUserCityIdsSet1.size() > 0 && StringUtil.isNotEmpty(info.getAttrVal()) && !AllUserCityIdsSet1.contains(info.getAttrVal())) {
                        info.setIsInPrilabel(0);
                    }
                }

                String currentLabelPath2 = this.getCurrentLabelPath(info, allEffectiveLabel);
                info.setCurrentLabelPath(currentLabelPath2);
            }

            return results;
        } catch (Exception var19) {
            this.log.error("查询用户使用标签列表失败", var19);
            throw new CIServiceException("查询用户使用标签列表失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public long queryUserUsedLabelNum(String userId, String dataScope) throws CIServiceException {
        try {
            long count = this.ciUserUseLabelJDao.getUserUsedLabelNum(userId, dataScope);
            return count;
        } catch (Exception var6) {
            this.log.error("查询用户使用标签列表失败", var6);
            throw new CIServiceException("查询用户使用标签列表失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<LabelDetailInfo> queryNewPublishLabel(int currPage, int pageSize, String orderType, String dataScope) throws CIServiceException {
        List results = null;

        try {
            CacheBase e = CacheBase.getInstance();
            CopyOnWriteArrayList allEffectiveLabel = e.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
            results = this.labelQueryJdbcDao.getNewPublishLabel(currPage, pageSize, orderType, dataScope);
            Iterator i$ = results.iterator();

            while (i$.hasNext()) {
                LabelDetailInfo info = (LabelDetailInfo) i$.next();
                info.setHasAlarm(this.hasAlarm(info));
                String dimCtiy = "";
                Set dimCitySet = null;
                String labelId = info.getLabelId();

                try {
                    CiLabelInfo currentLabelPath = CacheBase.getInstance().getEffectiveLabel(info.getLabelId());
                    CiLabelExtInfo ciLabelInfo = currentLabelPath.getCiLabelExtInfo();
                    CiMdaSysTableColumn labelSceneNames = ciLabelInfo.getCiMdaSysTableColumn();
                    dimCtiy = labelSceneNames.getDimTransId();
                    dimCitySet = CacheBase.getDimCitySet();
                } catch (Exception var19) {
                    this.log.error("地市标签查询查询出对应的地市区县表失败,标签ID为：--" + labelId + "--", var19);
                }

                String currentLabelPath1;
                if (StringUtil.isNotEmpty(dimCtiy) && dimCitySet != null && dimCitySet.size() > 0 && dimCitySet.contains(dimCtiy.toUpperCase())) {
                    currentLabelPath1 = PrivilegeServiceUtil.getUserId();
                    List ciLabelInfo1 = PrivilegeServiceUtil.getAllUserCityIds(currentLabelPath1);
                    HashSet labelSceneNames1 = new HashSet(ciLabelInfo1);
                    if (labelSceneNames1 != null && labelSceneNames1.size() > 0 && StringUtil.isNotEmpty(info.getAttrVal()) && !labelSceneNames1.contains(info.getAttrVal())) {
                        info.setIsInPrilabel(0);
                    }
                }

                currentLabelPath1 = this.getCurrentLabelPath(info, allEffectiveLabel);
                info.setCurrentLabelPath(currentLabelPath1);
                CiLabelInfo ciLabelInfo2 = this.ciLabelInfoHDao.selectCiLabelInfoById(Integer.valueOf(info.getLabelId()));
                StringBuilder labelSceneNames2 = new StringBuilder();
                Set ciLabelSceneRels = ciLabelInfo2.getCiLabelExtInfo().getCiLabelSceneRels();
                Iterator it = ciLabelSceneRels.iterator();

                while (it.hasNext()) {
                    CiLabelSceneRel detailInfo = (CiLabelSceneRel) it.next();
                    labelSceneNames2.append(e.getDimScene(detailInfo.getId().getSceneId()));
                    if (it.hasNext()) {
                        labelSceneNames2.append("&nbsp;&nbsp;");
                    }
                }

                info.setLabelSceneNames(labelSceneNames2.toString());
                LabelDetailInfo detailInfo1 = e.getHotLabelByKey("HOT_LABELS", info.getLabelId());
                if (detailInfo1 != null) {
                    info.setIsHot("true");
                }
            }

            return results;
        } catch (Exception var20) {
            this.log.error("查询新发布标签列表失败", var20);
            throw new CIServiceException("查询新发布标签列表失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public long queryNewPublishLabelNum(String dataScope) throws CIServiceException {
        try {
            long count = this.labelQueryJdbcDao.getNewPublishLabelNum(dataScope);
            return count;
        } catch (Exception var5) {
            this.log.error("查询新发布标签列表失败", var5);
            throw new CIServiceException("查询新发布标签列表失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<LabelDetailInfo> queryUserAttentionLabel(int currPage, int pageSize, String userId, String orderType, String dataScope) throws CIServiceException {
        List results = null;

        try {
            CacheBase e = CacheBase.getInstance();
            CopyOnWriteArrayList allEffectiveLabel = e.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
            results = this.labelQueryJdbcDao.getUserAttentionLabel(currPage, pageSize, userId, orderType, dataScope);
            /*---------------------------------------过滤标签 start----------------------------------------------------*/
            Map<String, String> hidden = CacheBase.getInstance().getHiddenLabelMap();
            List<CiLabelInfo> ciLabelInfos = new ArrayList<CiLabelInfo>();
            for (CiLabelInfo ciLabelInfo : (List<CiLabelInfo>) results) {
                if (hidden.get(Integer.valueOf(ciLabelInfo.getLabelId())) != null) {
                    this.log.info("隐藏标签：" + ciLabelInfo.getLabelId() + "-" + ciLabelInfo.getLabelName());
                } else {
                    ciLabelInfos.add(ciLabelInfo);
                }
            }
            results = ciLabelInfos;
            /*---------------------------------------过滤标签 end------------------------------------------------------*/
            Iterator i$ = results.iterator();

            while (i$.hasNext()) {
                LabelDetailInfo info = (LabelDetailInfo) i$.next();
                if (info.getUpdateCycle() == "1") {
                    info.setDataDateStr(DateUtil.string2StringFormat(info.getDataDate(), "yyyyMMdd", "yyyy-MM-dd"));
                } else {
                    info.setDataDateStr(DateUtil.string2StringFormat(info.getDataDate(), "yyyyMM", "yyyy-MM"));
                }

                info.setHasAlarm(this.hasAlarm(info));
                Long customNum = this.getCustomNum(info.getDataDate(), Integer.valueOf(info.getLabelId()), Integer.valueOf(-1), Integer.valueOf(-1), Long.valueOf(info.getCustomNum() == null ? 0L : Long.valueOf(info.getCustomNum()).longValue()), (String) null);
                info.setCustomNum(customNum.toString());
                String dimCtiy = "";
                Set dimCitySet = null;
                String labelId = info.getLabelId();

                CiLabelInfo ciLabelInfo;
                try {
                    ciLabelInfo = CacheBase.getInstance().getEffectiveLabel(info.getLabelId());
                    CiLabelExtInfo labelSceneNames = ciLabelInfo.getCiLabelExtInfo();
                    CiMdaSysTableColumn ciLabelSceneRels = labelSceneNames.getCiMdaSysTableColumn();
                    dimCtiy = ciLabelSceneRels.getDimTransId();
                    dimCitySet = CacheBase.getDimCitySet();
                } catch (Exception var21) {
                    this.log.error("地市标签查询查询出对应的地市区县表失败,标签ID为：--" + labelId + "--", var21);
                }

                if (StringUtil.isNotEmpty(dimCtiy) && dimCitySet != null && dimCitySet.size() > 0 && dimCitySet.contains(dimCtiy.toUpperCase())) {
                    if (StringUtil.isEmpty(userId)) {
                        userId = PrivilegeServiceUtil.getUserId();
                    }

                    List ciLabelInfo1 = PrivilegeServiceUtil.getAllUserCityIds(userId);
                    HashSet labelSceneNames1 = new HashSet(ciLabelInfo1);
                    if (labelSceneNames1 != null && labelSceneNames1.size() > 0 && StringUtil.isNotEmpty(info.getAttrVal()) && !labelSceneNames1.contains(info.getAttrVal())) {
                        info.setIsInPrilabel(0);
                    }
                }

                ciLabelInfo = e.getEffectiveLabel(Integer.valueOf(info.getLabelId()) + "");
                StringBuilder labelSceneNames2 = new StringBuilder();
                Set ciLabelSceneRels1 = ciLabelInfo.getCiLabelExtInfo().getCiLabelSceneRels();
                Iterator it = ciLabelSceneRels1.iterator();

                while (it.hasNext()) {
                    CiLabelSceneRel labelDetailInfo = (CiLabelSceneRel) it.next();
                    labelSceneNames2.append(e.getDimScene(labelDetailInfo.getId().getSceneId()));
                    if (it.hasNext()) {
                        labelSceneNames2.append("&nbsp;&nbsp;");
                    }
                }

                info.setLabelSceneNames(labelSceneNames2.toString());
                LabelDetailInfo labelDetailInfo1 = e.getHotLabelByKey("HOT_LABELS", info.getLabelId());
                if (labelDetailInfo1 != null) {
                    info.setIsHot("true");
                }

                String currentLabelPath = this.getCurrentLabelPath(info, allEffectiveLabel);
                info.setCurrentLabelPath(currentLabelPath);
            }

            return results;
        } catch (Exception var22) {
            this.log.error("查询用户关注标签列表失败", var22);
            throw new CIServiceException("查询用户关注标签列表失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public long queryUserAttentionLabelNum(String userId, String dataScope) throws CIServiceException {
        try {
            long count = this.labelQueryJdbcDao.getUserAttentionLabelNum(userId, dataScope);
            return count;
        } catch (Exception var6) {
            this.log.error("查询用户关注标签列表失败", var6);
            throw new CIServiceException("查询用户关注标签列表失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<TreeNode> queryUserLabelTree(String userId) {
        ArrayList treeList = new ArrayList();

        try {
            CacheBase e = CacheBase.getInstance();
            boolean message1 = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
            boolean hasAuthority = false;
            if (message1) {
                userId = PrivilegeServiceUtil.getUserId();
                hasAuthority = message1 && !PrivilegeServiceUtil.isAdminUser(userId);
            }

            CopyOnWriteArrayList labelIds = e.getKeyList("ALL_EFFECTIVE_LABEL_MAP");
            Iterator i$ = labelIds.iterator();

            while (true) {
                CiLabelInfo ciLabelInfo;
                CiLabelExtInfo ext;
                do {
                    do {
                        if (!i$.hasNext()) {
                            return treeList;
                        }

                        String idStr = (String) i$.next();
                        ciLabelInfo = null;
                        if (hasAuthority) {
                            ciLabelInfo = e.getEffectiveLabelByUser(idStr, userId);
                        } else {
                            ciLabelInfo = e.getEffectiveLabel(idStr);
                        }

                        ext = ciLabelInfo.getCiLabelExtInfo();
                    } while (ext == null);
                } while (ext != null && ext.getLabelLevel().intValue() < 2);

                if (userId != null && userId.equals(ciLabelInfo.getCreateUserId())) {
                    TreeNode treeNode = this.newTreeNode(ciLabelInfo, ext);
                    treeList.add(treeNode);
                }
            }
        } catch (Exception var12) {
            String message = "我的标签库标签树查询错误";
            this.log.error(message, var12);
            throw new CIServiceException(message, var12);
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiApproveHistory> queryHistoryByLabelId(Integer labelId) {
        List approveHistoryList = this.ciApproveHistoryHDao.getHistoryByLabelId(labelId);
        return approveHistoryList;
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<TreeNode> queryLabelTreeByName(String labelName) {
        ArrayList treeList = new ArrayList();
        CacheBase cache = CacheBase.getInstance();
        String level = Configure.getInstance().getProperty("LABEL_TREE_START_LEVEL").trim();
        int startLevel = Integer.valueOf(level).intValue();
        HashSet set = new HashSet();
        CopyOnWriteArrayList labelIds = cache.getKeyList("ALL_EFFECTIVE_LABEL_MAP");
        Iterator i$ = labelIds.iterator();

        while (i$.hasNext()) {
            String idStr = (String) i$.next();

            try {
                CiLabelInfo e = cache.getEffectiveLabelByUser(idStr, (String) null);
                CiLabelExtInfo message1 = e.getCiLabelExtInfo();
                if (message1 != null && (startLevel == 1 || message1.getLabelLevel().intValue() >= startLevel) && e.getLabelName() != null && (e.getLabelName() == null || e.getLabelName().contains(labelName))) {
                    if (!set.contains(e.getParentId())) {
                        set.add(e.getLabelId());
                        this.searchParentNode(treeList, e, labelName, set);
                    }

                    TreeNode treeNode = this.newTreeNode(e, message1);
                    treeList.add(treeNode);
                }
            } catch (Exception var13) {
                String message = "标签树模糊查询错误";
                this.log.error(message, var13);
                throw new CIServiceException(message, var13);
            }
        }

        return treeList;
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<TreeNode> queryUserLabelTreeByName(String labelName, String userId) {
        ArrayList treeList = new ArrayList();

        try {
            CacheBase e = CacheBase.getInstance();
            boolean message1 = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
            boolean hasAuthority = false;
            if (message1) {
                userId = PrivilegeServiceUtil.getUserId();
                hasAuthority = message1 && !PrivilegeServiceUtil.isAdminUser(userId);
            }

            HashSet set = new HashSet();
            CopyOnWriteArrayList labelIds = e.getKeyList("ALL_EFFECTIVE_LABEL_MAP");
            Iterator i$ = labelIds.iterator();

            while (true) {
                CiLabelInfo ciLabelInfo;
                CiLabelExtInfo ext;
                do {
                    do {
                        do {
                            do {
                                do {
                                    do {
                                        if (!i$.hasNext()) {
                                            return treeList;
                                        }

                                        String idStr = (String) i$.next();
                                        ciLabelInfo = null;
                                        if (hasAuthority) {
                                            ciLabelInfo = e.getEffectiveLabelByUser(idStr, userId);
                                        } else {
                                            ciLabelInfo = e.getEffectiveLabel(idStr);
                                        }

                                        ext = ciLabelInfo.getCiLabelExtInfo();
                                    } while (ext == null);
                                } while (ext != null && ext.getLabelLevel().intValue() < 2);
                            } while (userId == null);
                        } while (!userId.equals(ciLabelInfo.getCreateUserId()));
                    } while (ciLabelInfo.getLabelName() == null);
                } while (ciLabelInfo.getLabelName() != null && !ciLabelInfo.getLabelName().contains(labelName));

                if (!set.contains(ciLabelInfo.getParentId())) {
                    set.add(ciLabelInfo.getLabelId());
                    this.searchParentNode(treeList, ciLabelInfo, labelName, set);
                }

                TreeNode treeNode = this.newTreeNode(ciLabelInfo, ext);
                treeList.add(treeNode);
            }
        } catch (Exception var14) {
            String message = "我的标签树模糊查询错误";
            this.log.error(message, var14);
            throw new CIServiceException(message, var14);
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public void searchParentNode(List<TreeNode> treeList, CiLabelInfo ciLabelInfo, String labelName, Set<Integer> set) {
        CiLabelExtInfo ext = ciLabelInfo.getCiLabelExtInfo();

        try {
            String e = Configure.getInstance().getProperty("LABEL_TREE_START_LEVEL").trim();
            int startLevel = Integer.valueOf(e).intValue();
            if (ext != null && ext.getLabelLevel().intValue() >= startLevel && ciLabelInfo.getParentId().intValue() > 0) {
                boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
                boolean hasAuthority = false;
                String userId = null;
                if (needAuthority) {
                    userId = PrivilegeServiceUtil.getUserId();
                    hasAuthority = needAuthority && !PrivilegeServiceUtil.isAdminUser(userId);
                }

                CiLabelInfo parentInfo = null;
                CacheBase cache = CacheBase.getInstance();
                if (hasAuthority) {
                    parentInfo = cache.getEffectiveLabelByUser(String.valueOf(ciLabelInfo.getParentId()), userId);
                } else {
                    parentInfo = cache.getEffectiveLabel(String.valueOf(ciLabelInfo.getParentId()));
                }

                CiLabelExtInfo parent_ext = parentInfo.getCiLabelExtInfo();
                if (parent_ext != null && parent_ext.getLabelLevel().intValue() >= startLevel && !parentInfo.getLabelName().contains(labelName) && !set.contains(parentInfo.getLabelId())) {
                    set.add(parentInfo.getLabelId());
                    TreeNode treeNode = this.newTreeNode(parentInfo, parent_ext);
                    treeList.add(treeNode);
                    this.searchParentNode(treeList, parentInfo, labelName, set);
                }
            }
        } catch (Exception var15) {
            this.log.error("查询父节点错误", var15);
            var15.printStackTrace();
        }

    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<LabelDetailInfo> queryEffectiveLabel(Pager pager, String orderType, String labelKeyWord, Integer topLabelId, String dataScope, CiLabelInfo searchBean) throws CIServiceException {
        List results = null;

        try {
            CacheBase e = CacheBase.getInstance();
            CopyOnWriteArrayList allEffectiveLabel = e.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
            SimpleDateFormat sdMonFormat = new SimpleDateFormat("yyyyMM");
            SimpleDateFormat sdDayFormat = new SimpleDateFormat("yyyy-MM-dd");
            results = this.labelQueryJdbcDao.getEffectiveLabel(pager, orderType, labelKeyWord, topLabelId, dataScope, searchBean);

            Iterator i$ = results.iterator();
            while (i$.hasNext()) {
//                Map<String,String> map=CacheBase.getInstance().getHiddenLabelMap();
                LabelDetailInfo info = (LabelDetailInfo) i$.next();
//                if (map.get(String.valueOf(info.getLabelId()))!=null){
//                    this.log.info("标签详情过滤"+info.getLabelId()+"--"+info.getLabelName());
//                    continue;
//                }
                String effectTime = info.getEffecTime();
                Date d = sdDayFormat.parse(effectTime);
                String effectDate = sdMonFormat.format(d);
                info.setEffectDate(DateUtil.getOffsetDateByDate(effectDate, -1, 0));
                String currentLabelPath;
                if (pager != null) {
                    currentLabelPath = PrivilegeServiceUtil.getUserId();
                    info.setHasAlarm(this.hasAlarm(info));
                    Long ciLabelInfo = this.getCustomNum(info.getDataDate(), Integer.valueOf(info.getLabelId()), Integer.valueOf(-1), Integer.valueOf(-1), Long.valueOf(info.getCustomNum() == null ? 0L : Long.valueOf(info.getCustomNum()).longValue()), (String) null);
                    info.setCustomNum(ciLabelInfo.toString());
                    String labelSceneNames = "";
                    Set labelSceneIds = null;
                    String ciLabelSceneRels = info.getLabelId();

                    try {
                        CiLabelInfo it = CacheBase.getInstance().getEffectiveLabel(info.getLabelId());
                        CiLabelExtInfo ciLabelSceneRel = it.getCiLabelExtInfo();
                        CiMdaSysTableColumn ciMdaSysTableColumn = ciLabelSceneRel.getCiMdaSysTableColumn();
                        labelSceneNames = ciMdaSysTableColumn.getDimTransId();
                        labelSceneIds = CacheBase.getDimCitySet();
                    } catch (Exception var25) {
                        this.log.error("地市标签查询查询出对应的地市区县表失败,标签ID为：--" + ciLabelSceneRels + "--", var25);
                    }

                    if (StringUtil.isNotEmpty(labelSceneNames) && labelSceneIds != null && labelSceneIds.size() > 0 && labelSceneIds.contains(labelSceneNames.toUpperCase())) {
                        List it1 = PrivilegeServiceUtil.getAllUserCityIds(currentLabelPath);
                        HashSet ciLabelSceneRel1 = new HashSet(it1);
                        if (ciLabelSceneRel1 != null && ciLabelSceneRel1.size() > 0 && StringUtil.isNotEmpty(info.getAttrVal()) && !ciLabelSceneRel1.contains(info.getAttrVal())) {
                            info.setIsInPrilabel(0);
                        }
                    }

                    LabelDetailInfo it2 = e.getHotLabelByKey("HOT_LABELS", info.getLabelId());
                    if (it2 != null) {
                        info.setIsHot("true");
                    }
                } else {
                    Long currentLabelPath1 = Long.valueOf(info.getCustomNum() == null ? 0L : Long.valueOf(info.getCustomNum()).longValue());
                    info.setCustomNum(currentLabelPath1.toString());
                }

                currentLabelPath = this.getCurrentLabelPath(info, allEffectiveLabel);
                info.setCurrentLabelPath(currentLabelPath);
                CiLabelInfo ciLabelInfo1 = e.getEffectiveLabel(Integer.valueOf(info.getLabelId()) + "");
                StringBuilder labelSceneNames1 = new StringBuilder();
                StringBuilder labelSceneIds1 = new StringBuilder();
                Set ciLabelSceneRels1 = ciLabelInfo1.getCiLabelExtInfo().getCiLabelSceneRels();
                Iterator it3 = ciLabelSceneRels1.iterator();

                while (it3.hasNext()) {
                    CiLabelSceneRel ciLabelSceneRel2 = (CiLabelSceneRel) it3.next();
                    labelSceneNames1.append(e.getDimScene(ciLabelSceneRel2.getId().getSceneId()));
                    labelSceneIds1.append(ciLabelSceneRel2.getId().getSceneId());
                    if (it3.hasNext()) {
                        labelSceneNames1.append("&nbsp;&nbsp;");
                        labelSceneIds1.append(" ");
                    }
                }

                info.setLabelSceneIds(labelSceneIds1.toString());
                info.setLabelSceneNames(labelSceneNames1.toString());
            }

            return results;
        } catch (Exception var26) {
            this.log.error("查询首页标签列表失败", var26);
            throw new CIServiceException("查询首页标签列表失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public long queryEffectiveLabelNum(String labelKeyWord, Integer topLabelId, String dataScope, CiLabelInfo searchBean) throws CIServiceException {
        try {
            long count = this.labelQueryJdbcDao.getUserEffectiveLabelNum(labelKeyWord, topLabelId, dataScope, searchBean);
            return count;
        } catch (Exception var8) {
            this.log.error("查询首页标签列表失败", var8);
            throw new CIServiceException("查询首页标签列表失败");
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public boolean judgeCurrentUserIsApprover(String userId) throws CIServiceException {
        CiApproveUserInfo ciApproveUserInfo = this.ciApproveUserInfoHDao.selectById(userId);
        return ciApproveUserInfo != null;
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public boolean hasAlarm(LabelDetailInfo labelDetailInfo) {
        String dataDate = CacheBase.getInstance().getNewLabelMonth();
        String updateCycle = labelDetailInfo.getUpdateCycle();
        if (updateCycle == "1") {
            dataDate = CacheBase.getInstance().getNewLabelDay();
        }

        String labelId = labelDetailInfo.getLabelId();
        String userId = PrivilegeServiceUtil.getUserId();
        if (StringUtil.isNotEmpty(labelId) && StringUtil.isNotEmpty(dataDate)) {
            try {
                boolean e = CIAlarmServiceUtil.haveAlarmRecords(dataDate, userId, "LabelAlarm", labelId);
                return e;
            } catch (Exception var7) {
                this.log.error("", var7);
            }
        }

        return false;
    }

    /**
     * @deprecated
     */
    @Deprecated
    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<TreeNode> queryDimTree(String labelId, String dimName) throws CIServiceException {
        ArrayList treeList = new ArrayList();
        CiLabelInfo label = CacheBase.getInstance().getEffectiveLabel(labelId);
        String dimId = label.getCiLabelExtInfo().getCiMdaSysTableColumn().getDimTransId();
        ArrayList list = new ArrayList();
        this.queryDimTableDefine(dimId, list);
        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        String userId = null;
        if (needAuthority) {
            userId = PrivilegeServiceUtil.getUserId();
        }

        int i = 0;
        HashSet set = null;

        for (Iterator i$ = list.iterator(); i$.hasNext(); ++i) {
            DimTableDefine define = (DimTableDefine) i$.next();
            int j = 0;
            HashSet tempSet = new HashSet();
            List dimValList = this.dimTableServiceImpl.getAllDimData(define.getDimId(), define.getDimPCodeCol(), (String) null, (String) null, userId, (String) null, false);
            Iterator i$1 = dimValList.iterator();

            while (true) {
                while (i$1.hasNext()) {
                    Map map = (Map) i$1.next();
                    String id = map.get(define.getDimCodeCol()).toString();
                    String name = map.get(define.getDimValueCol()).toString();
                    String pid = "-1";
                    if (StringUtil.isNotEmpty(define.getDimPCodeCol())) {
                        pid = map.get(define.getDimPCodeCol()).toString();
                    }

                    TreeNode node = new TreeNode();
                    node.setId(id);
                    node.setName(name);
                    node.setpId(pid);
                    if (StringUtil.isEmpty(dimName)) {
                        if (i != 0) {
                            node.setNocheck(Boolean.valueOf(true));
                            if (j == 0) {
                                node.setOpen(Boolean.valueOf(true));
                            }

                            ++j;
                        }

                        treeList.add(node);
                    } else if (i == 0 && StringUtil.isNotEmpty(name) && name.contains(dimName)) {
                        if (!pid.equals("-1")) {
                            tempSet.add(pid);
                        }

                        treeList.add(node);
                    } else if (i != 0 && set.contains(id)) {
                        if (!pid.equals("-1")) {
                            tempSet.add(pid);
                        }

                        if (j == 0) {
                            node.setOpen(Boolean.valueOf(true));
                        }

                        ++j;
                        node.setNocheck(Boolean.valueOf(true));
                        treeList.add(node);
                    }
                }

                set = new HashSet();
                set.addAll(tempSet);
                break;
            }
        }

        return treeList;
    }

    private void queryDimTableDefine(String dimId, List<DimTableDefine> list) {
        DimTableDefine define = this.dimTableServiceImpl.findDefineById(dimId);
        list.add(define);
        String pDimId = define.getParentDimId();
        if (StringUtil.isNotEmpty(pDimId)) {
            this.queryDimTableDefine(pDimId, list);
        }

    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public Long getCustomNum(String dataDate, Integer labelId, Integer vipLevelId, Integer brandId, Long customNum, String userId) throws CIServiceException {
        Long retCustomNum = customNum;

        try {
            if (StringUtil.isEmpty(userId)) {
                userId = PrivilegeServiceUtil.getUserId();
            }

            boolean e = PrivilegeServiceUtil.isAdminUser(userId);
            boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
            boolean isProvinceUser = PrivilegeServiceUtil.isProvinceUser(userId);
            int updateCycle;
            if (needAuthority && !e && !isProvinceUser) {
                updateCycle = CacheBase.getInstance().getEffectiveLabel(labelId.toString()).getUpdateCycle().intValue();
                List retCustomNumIntValue1 = PrivilegeServiceUtil.getUserCityIds(userId);
                if (StringUtil.isEmpty(dataDate)) {
                    if (1 == updateCycle) {
                        dataDate = CacheBase.getInstance().getNewLabelDay();
                    } else if (2 == updateCycle) {
                        dataDate = CacheBase.getInstance().getNewLabelMonth();
                    } else {
                        dataDate = CacheBase.getInstance().getNewLabelMonth();
                    }
                } else if (1 == updateCycle) {
                    dataDate = CacheBase.getInstance().getNewLabelDay();
                }

                retCustomNum = this.ciLabelStatJDao.getCustomNum(dataDate, labelId, retCustomNumIntValue1, vipLevelId, brandId);
            } else if (customNum == null) {
                updateCycle = CacheBase.getInstance().getEffectiveLabel(labelId.toString()).getUpdateCycle().intValue();
                if (StringUtil.isEmpty(dataDate)) {
                    if (1 == updateCycle) {
                        dataDate = CacheBase.getInstance().getNewLabelDay();
                        Integer retCustomNumIntValue = CacheBase.getInstance().getEffectiveLabel(labelId.toString()).getCiLabelExtInfo().getCustomNum();
                        if (retCustomNumIntValue != null) {
                            retCustomNum = Long.valueOf(retCustomNumIntValue.longValue());
                        } else {
                            this.log.debug("缓存中该日标签的客户数为null");
                            retCustomNum = Long.valueOf(0L);
                        }
                    } else if (2 == updateCycle) {
                        dataDate = CacheBase.getInstance().getNewLabelMonth();
                        retCustomNum = this.ciLabelStatJDao.getCustomNum(dataDate, labelId, (List) null, vipLevelId, brandId);
                    } else {
                        dataDate = CacheBase.getInstance().getNewLabelMonth();
                        retCustomNum = this.ciLabelStatJDao.getCustomNum(dataDate, labelId, (List) null, vipLevelId, brandId);
                    }
                } else if (1 == updateCycle) {
                    retCustomNum = Long.valueOf(CacheBase.getInstance().getEffectiveLabel(labelId.toString()).getCiLabelExtInfo().getCustomNum().longValue());
                } else if (2 == updateCycle) {
                    retCustomNum = this.ciLabelStatJDao.getCustomNum(dataDate, labelId, (List) null, vipLevelId, brandId);
                }
            }

            return retCustomNum;
        } catch (Exception var13) {
            this.log.error("获取用户拥有权限的地市某标签客户数失败", var13);
            throw new CIServiceException("获取用户拥有权限的地市某标签客户数失败");
        }
    }

    public void addCiLabelHistoryInfo(CiLabelInfo ciLabelInfo, Integer opType) throws CIServiceException {
        try {
            String e = PrivilegeServiceUtil.getUserId();
            CiLabelHistoryInfo ciLabelHistoryInfo = new CiLabelHistoryInfo();
            ciLabelHistoryInfo.setLabelId(ciLabelInfo.getLabelId());
            ciLabelHistoryInfo.setLabelName(ciLabelInfo.getLabelName());
            ciLabelHistoryInfo.setUpdateCycle(ciLabelInfo.getUpdateCycle());
            ciLabelHistoryInfo.setParentId(ciLabelInfo.getParentId());
            ciLabelHistoryInfo.setEffecTime(ciLabelInfo.getEffecTime());
            ciLabelHistoryInfo.setFailTime(ciLabelInfo.getFailTime());
            ciLabelHistoryInfo.setModifyUserId(e);
            ciLabelHistoryInfo.setModifyTime(new Date());
            ciLabelHistoryInfo.setModifyDesc(ciLabelInfo.getCreateDesc());
            ciLabelHistoryInfo.setDeptId(String.valueOf(PrivilegeServiceUtil.getUserDeptId(e)));
            ciLabelHistoryInfo.setDataStatusId(ciLabelInfo.getDataStatusId());
            ciLabelHistoryInfo.setLabelTypeId(ciLabelInfo.getLabelTypeId());
            ciLabelHistoryInfo.setBusiCaliber(ciLabelInfo.getBusiCaliber());
            ciLabelHistoryInfo.setDataSource(ciLabelInfo.getDataSource());
            ciLabelHistoryInfo.setBusiLegend(ciLabelInfo.getBusiLegend());
            ciLabelHistoryInfo.setApplySuggest(ciLabelInfo.getApplySuggest());
            ciLabelHistoryInfo.setSortNum(ciLabelInfo.getSortNum());
            ciLabelHistoryInfo.setOpType(opType);
            ciLabelHistoryInfo.setIsSysRecom(ciLabelInfo.getIsSysRecom());
            this.ciLabelHistoryInfoHDao.insertCiLabelHistoryInfo(ciLabelHistoryInfo);
        } catch (Exception var5) {
            this.log.error("标签历史信息表中插入标签管理操作记录失败", var5);
            throw new CIServiceException("标签历史信息表中插入标签管理操作记录失败");
        }
    }

    private String getCurrentLabelPath(LabelDetailInfo detailInfo, List<CiLabelInfo> allEffectiveLabel) {
        Integer parentId = Integer.valueOf(detailInfo.getParentId());
        ArrayList labelNameList = new ArrayList();
        labelNameList.add(detailInfo.getLabelName());
        this.getLabelNameList(labelNameList, allEffectiveLabel, parentId);
        StringBuilder retLablePath = new StringBuilder();

        for (int i = labelNameList.size() - 1; i >= 0; --i) {
            retLablePath.append((String) labelNameList.get(i)).append(i > 0 ? "&nbsp;&nbsp;&raquo;&nbsp;&nbsp;" : "");
        }

        return retLablePath.toString();
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<Map<String, Object>> queryLabelDimValue(Integer labelId, Pager pager, String dimName, String userId, String enumCategoryId) throws Exception {
        List result = null;
        CacheBase cache = CacheBase.getInstance();
        CiLabelInfo ciLabelInfo = cache.getEffectiveLabel(String.valueOf(labelId));
        CiMdaSysTableColumn column = ciLabelInfo.getCiLabelExtInfo().getCiMdaSysTableColumn();
        DimTableServiceImpl service = (DimTableServiceImpl) SystemServiceLocator.getInstance().getService("dimTable_serviceTarget");
        String transDimId = this.ciLabelInfoJDao.selectDimTransIdByLabelId(labelId);
        DimTableDefine define = service.findDefineById(transDimId);
        boolean isNeedAuthority = StringUtil.isNotEmpty(column.getIsNeedAuthority()) && column.getIsNeedAuthority().intValue() == 1;
        String cityAuthorityWhereStr = this.getCityAuthoritySql(userId, isNeedAuthority, define.getDimTablename());
        if (StringUtil.isEmpty(enumCategoryId)) {
            int searchBean = ciLabelInfo.getLabelTypeId().intValue();
            if (5 == searchBean && !StringUtil.isEmpty(transDimId)) {
                result = this.ciLabelInfoJDao.getAllDimDataByDimTableDefine(define, pager.getPageNum(), pager.getPageSize(), dimName, cityAuthorityWhereStr);
            }
        } else {
            CiEnumConditionInfo searchBean1 = new CiEnumConditionInfo();
            searchBean1.setEnumCategoryId(enumCategoryId);
            searchBean1.setEnumName(dimName);
            result = this.enumConditionInfoJDao.selectCiEnumConditionInfoList(pager.getPageNum(), pager.getPageSize(), searchBean1, cityAuthorityWhereStr, define, column);
        }

        return result;
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<Map<String, Object>> queryLabelAllDimValue(Integer columnId) throws Exception {
        CiMdaSysTableColumn column = this.ciMdaColumnHDao.selectCiMdaSysTableColumnByColumnId(columnId);
        DimTableServiceImpl service = (DimTableServiceImpl) SystemServiceLocator.getInstance().getService("dimTable_serviceTarget");
        String transDimId = column.getDimTransId();
        DimTableDefine define = service.findDefineById(transDimId);
        return this.ciLabelInfoJDao.getAllDimDataByDimTableDefine(define);
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<Map<String, Object>> queryAllDimDataByDimTableDefine(DimTableDefine define) throws Exception {
        return this.ciLabelInfoJDao.getAllDimDataByDimTableDefine(define);
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<Map<String, Object>> queryLabelDimValueByColumnId(Integer columnId, Pager pager, String dimName, String userId, String enumCategoryId) throws Exception {
        List result = null;
        CacheBase cache = CacheBase.getInstance();
        CiMdaSysTableColumn column = cache.getEffectiveColumn(columnId + "");
        DimTableServiceImpl service = (DimTableServiceImpl) SystemServiceLocator.getInstance().getService("dimTable_serviceTarget");
        String transDimId = column.getDimTransId();
        DimTableDefine define = service.findDefineById(transDimId);
        boolean isNeedAuthority = StringUtil.isNotEmpty(column.getIsNeedAuthority()) && column.getIsNeedAuthority().intValue() == 1;
        String cityAuthorityWhereStr = this.getCityAuthoritySql(userId, isNeedAuthority, define.getDimTablename());
        if (StringUtil.isEmpty(enumCategoryId)) {
            if (!StringUtil.isEmpty(transDimId) && define != null) {
                result = this.ciLabelInfoJDao.getAllDimDataByDimTableDefine(define, pager.getPageNum(), pager.getPageSize(), dimName, cityAuthorityWhereStr);
            }
        } else {
            CiEnumConditionInfo searchBean = new CiEnumConditionInfo();
            searchBean.setEnumCategoryId(enumCategoryId);
            searchBean.setEnumName(dimName);
            result = this.enumConditionInfoJDao.selectCiEnumConditionInfoList(pager.getPageNum(), pager.getPageSize(), searchBean, cityAuthorityWhereStr, define, column);
        }

        return result;
    }

    private String getCityAuthoritySql(String userId, boolean isNeedAuthority, String tabelAlias) {
        StringBuffer result = new StringBuffer();
        if (StringUtil.isEmpty(userId)) {
            this.log.error("用户ID为空，无法判断权限");
            throw new CIServiceException("用户ID为空，无法判断权限");
        } else {
            try {
                String e = PrivilegeServiceUtil.getCityId(userId);
                String centerCity = Configure.getInstance().getProperty("CENTER_CITYID");
                boolean isCenterCityUser = e.equals(centerCity);
                boolean hasAuthority = isNeedAuthority && !isCenterCityUser;
                List icities = null;
                if (hasAuthority) {
                    String cityColumn = Configure.getInstance().getProperty("CITY_COLUMN").toUpperCase();
                    String cityColumnType = Configure.getInstance().getProperty("CITY_COLUMN_TYPE").toLowerCase();
                    String countyColumn = Configure.getInstance().getProperty("COUNTY_COLUMN").toUpperCase();
                    String countyColumnType = Configure.getInstance().getProperty("COUNTY_COLUMN_TYPE").toLowerCase();
                    CacheBase cache = CacheBase.getInstance();
                    icities = PrivilegeServiceUtil.getUserCityIds(userId);
                    CopyOnWriteArrayList onlyCityIds = cache.getKeyList("CITY_IDS_LIST");

                    for (int i = 0; i < icities.size(); ++i) {
                        StringBuffer sb = new StringBuffer();
                        String cityId = (String) icities.get(i);
                        if (cityId.equals(centerCity)) {
                            return null;
                        }

                        if (onlyCityIds.contains(cityId)) {
                            if ("char".equals(cityColumnType)) {
                                sb.append(tabelAlias).append(".").append(cityColumn).append(" = \'").append(cityId).append("\' or ");
                                sb.append(tabelAlias).append(".").append(cityColumn).append(" = \'").append(centerCity).append("\' ");
                            } else if ("number".equals(cityColumnType)) {
                                sb.append(tabelAlias).append(".").append(cityColumn).append(" = ").append(cityId).append(" or ");
                                sb.append(tabelAlias).append(".").append(cityColumn).append(" = ").append(centerCity).append(" ");
                            }
                        } else {
                            String superCityId = PrivilegeServiceUtil.getCityByCityId(cityId).getParentId();
                            if ("char".equals(countyColumnType)) {
                                sb.append(" ( ").append(tabelAlias).append(".").append(countyColumn);
                                sb.append(" = \'").append(cityId).append("\' and ");
                                if ("char".equals(cityColumnType)) {
                                    sb.append(tabelAlias).append(".").append(cityColumn);
                                    sb.append(" = \'").append(superCityId).append("\' ) or ");
                                } else if ("number".equals(cityColumnType)) {
                                    sb.append(tabelAlias).append(".").append(cityColumn);
                                    sb.append(" = ").append(superCityId).append(" ) or ");
                                }

                                sb.append(" ( ").append(tabelAlias).append(".").append(countyColumn);
                                sb.append("  is null and ");
                                if ("char".equals(cityColumnType)) {
                                    sb.append(tabelAlias).append(".").append(cityColumn);
                                    sb.append(" = \'").append(superCityId).append("\' ) or ");
                                } else if ("number".equals(cityColumnType)) {
                                    sb.append(tabelAlias).append(".").append(cityColumn);
                                    sb.append(" = ").append(superCityId).append(" ) or ");
                                }

                                if ("char".equals(cityColumnType)) {
                                    sb.append(tabelAlias).append(".").append(cityColumn).append(" = \'");
                                    sb.append(centerCity).append("\' ");
                                } else if ("number".equals(cityColumnType)) {
                                    sb.append(tabelAlias).append(".").append(cityColumn).append(" = ");
                                    sb.append(centerCity).append(" ");
                                }
                            } else if ("number".equals(countyColumnType)) {
                                sb.append(" ( ").append(tabelAlias).append(".").append(countyColumn);
                                sb.append(" = ").append(cityId).append(" and ");
                                if ("char".equals(cityColumnType)) {
                                    sb.append(tabelAlias).append(".").append(cityColumn);
                                    sb.append(" = \'").append(superCityId).append("\' ) or ");
                                } else if ("number".equals(cityColumnType)) {
                                    sb.append(tabelAlias).append(".").append(cityColumn);
                                    sb.append(" = ").append(superCityId).append(" ) or ");
                                }

                                sb.append(" ( ").append(tabelAlias).append(".").append(countyColumn);
                                sb.append("  is null and ");
                                if ("char".equals(cityColumnType)) {
                                    sb.append(tabelAlias).append(".").append(cityColumn);
                                    sb.append(" = \'").append(superCityId).append("\' ) or ");
                                } else if ("number".equals(cityColumnType)) {
                                    sb.append(tabelAlias).append(".").append(cityColumn);
                                    sb.append(" = ").append(superCityId).append(" ) or ");
                                }

                                if ("char".equals(cityColumnType)) {
                                    sb.append(tabelAlias).append(".").append(cityColumn).append(" = \'");
                                    sb.append(centerCity).append("\' ");
                                } else if ("number".equals(cityColumnType)) {
                                    sb.append(tabelAlias).append(".").append(cityColumn).append(" = ");
                                    sb.append(centerCity).append(" ");
                                }
                            }
                        }

                        if (icities.size() > 1 && i != icities.size() - 1) {
                            sb.append(" or ");
                        }

                        result.append(sb.toString());
                    }
                }
            } catch (Exception var20) {
                var20.printStackTrace();
            }

            return result.length() > 0 ? " ( " + result.toString() + " ) " : null;
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public long queryLabelDimValueCount(String userId, Integer labelId, String dimName, String enumCategoryId) throws Exception {
        long result = 0L;
        CiLabelInfo ciLabelInfo = CacheBase.getInstance().getEffectiveLabel(labelId + "");
        if (ciLabelInfo != null && ciLabelInfo.getLabelTypeId() != null) {
            DimTableServiceImpl service = (DimTableServiceImpl) SystemServiceLocator.getInstance().getService("dimTable_serviceTarget");
            CiMdaSysTableColumn column = ciLabelInfo.getCiLabelExtInfo().getCiMdaSysTableColumn();
            String transDimId = ciLabelInfo.getCiLabelExtInfo().getCiMdaSysTableColumn().getDimTransId();
            DimTableDefine define = service.findDefineById(transDimId);
            boolean isNeedAuthority = StringUtil.isNotEmpty(column.getIsNeedAuthority()) && column.getIsNeedAuthority().intValue() == 1;
            String cityAuthorityWhereStr = this.getCityAuthoritySql(userId, isNeedAuthority, define.getDimTablename());
            if (StringUtil.isEmpty(enumCategoryId)) {
                int searchBean = ciLabelInfo.getLabelTypeId().intValue();
                if (5 == searchBean && !StringUtil.isEmpty(transDimId)) {
                    result = this.ciLabelInfoJDao.getAllDimDataCountByDimTableDefine(define, dimName, cityAuthorityWhereStr);
                }
            } else {
                CiEnumConditionInfo searchBean1 = new CiEnumConditionInfo();
                searchBean1.setEnumCategoryId(enumCategoryId);
                searchBean1.setEnumName(dimName);
                result = this.enumConditionInfoJDao.selectCiEnumConditionInfoCount(searchBean1, cityAuthorityWhereStr, define, column);
            }

            return result;
        } else {
            this.log.error("unum label is null !");
            throw new CIServiceException("unum label is null !");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public long queryLabelDimValueCountByColumnId(String userId, Integer columnId, String dimName, String enumCategoryId) throws Exception {
        long result = 0L;
        CacheBase cache = CacheBase.getInstance();
        CiMdaSysTableColumn column = cache.getEffectiveColumn(columnId + "");
        DimTableServiceImpl service = (DimTableServiceImpl) SystemServiceLocator.getInstance().getService("dimTable_serviceTarget");
        String transDimId = column.getDimTransId();
        DimTableDefine define = service.findDefineById(transDimId);
        boolean isNeedAuthority = StringUtil.isNotEmpty(column.getIsNeedAuthority()) && column.getIsNeedAuthority().intValue() == 1;
        String cityAuthorityWhereStr = this.getCityAuthoritySql(userId, isNeedAuthority, define.getDimTablename());
        if (StringUtil.isEmpty(enumCategoryId)) {
            if (!StringUtil.isEmpty(transDimId) && define != null) {
                result = this.ciLabelInfoJDao.getAllDimDataCountByDimTableDefine(define, dimName, cityAuthorityWhereStr);
            }
        } else {
            CiEnumConditionInfo searchBean = new CiEnumConditionInfo();
            searchBean.setEnumCategoryId(enumCategoryId);
            searchBean.setEnumName(dimName);
            result = this.enumConditionInfoJDao.selectCiEnumConditionInfoCount(searchBean, cityAuthorityWhereStr, define, column);
        }

        return result;
    }

    public List<CiLabelVerticalColumnRel> queryCiLabelVerticalColumnRelByLabelId(Integer labelId) {
        return this.ciLabelVerticalColumnRelJDao.selectCiVerticalColumnRelByLabelId(labelId);
    }

    private void getLabelNameList(List<String> labelNameList, List<CiLabelInfo> allEffectiveLabel, Integer parentId) {
        Iterator i$ = allEffectiveLabel.iterator();

        while (i$.hasNext()) {
            CiLabelInfo info = (CiLabelInfo) i$.next();
            if (info.getLabelId().compareTo(parentId) == 0) {
                if (parentId.intValue() == -1) {
                    break;
                }

                labelNameList.add(info.getLabelName());
                this.getLabelNameList(labelNameList, allEffectiveLabel, info.getParentId());
            }
        }

    }

    private void modifyCiTemplateInfoAndAddHistory(String labelId, int customGropIsOffLine) {
        String userId = PrivilegeServiceUtil.getUserId();
        List ciLabelRuleList = this.ciLabelRuleHDao.selectCiLabelRuleListByLabelId(labelId, Integer.valueOf(2));
        Iterator i$ = ciLabelRuleList.iterator();

        while (true) {
            CiTemplateInfo ciTemplateInfo;
            boolean flag;
            do {
                if (!i$.hasNext()) {
                    return;
                }

                CiLabelRule ciLabelRule = (CiLabelRule) i$.next();
                ciTemplateInfo = this.ciTemplateInfoHDao.selectTemplateInfoById(ciLabelRule.getCustomId());
                flag = true;
            } while (null == ciTemplateInfo);

            if (customGropIsOffLine == 1) {
                List history = this.ciLabelRuleHDao.selectCiLabelRuleList(ciTemplateInfo.getTemplateId(), Integer.valueOf(2));
                Iterator i$1 = history.iterator();

                while (i$1.hasNext()) {
                    CiLabelRule ciLabelRule2 = (CiLabelRule) i$1.next();
                    if (ciLabelRule2.getElementType().intValue() == 2) {
                        CacheBase cache = CacheBase.getInstance();
                        CiLabelInfo ciLabelInfo = cache.getEffectiveLabel(ciLabelRule2.getCalcuElement());
                        if (ciLabelInfo == null && !ciLabelRule2.getCalcuElement().equals(labelId)) {
                            flag = false;
                            break;
                        }
                    }
                }
            }

            if (flag) {
                ciTemplateInfo.setStatus(Integer.valueOf(customGropIsOffLine));
                ciTemplateInfo.setNewModifyTime(new Date());
            }

            this.ciTemplateInfoHDao.insertTemplateInfo(ciTemplateInfo);
            CiTemplateModifyInfo history1 = this.addCiTemplateModifyInfo(ciTemplateInfo, userId);
            this.ciTemplateModifyInfoHDao.insertCiTemplateModifyInfo(history1);
        }
    }

    private void modifyCiCustomGropInfoAndAddHistory(String labelId, int customGropIsOffLine) {
        String userId = PrivilegeServiceUtil.getUserId();
        List ciLabelRuleList = this.ciLabelRuleHDao.selectCiLabelRuleListByLabelId(labelId, Integer.valueOf(1));
        Iterator i$ = ciLabelRuleList.iterator();

        while (true) {
            CiCustomGroupInfo ciCustomGroupInfo;
            do {
                if (!i$.hasNext()) {
                    return;
                }

                CiLabelRule ciLabelRule = (CiLabelRule) i$.next();
                ciCustomGroupInfo = this.ciCustomGroupInfoHDao.selectCustomGroupById(ciLabelRule.getCustomId());
            } while (null == ciCustomGroupInfo);

            boolean flag = true;
            List history;
            Iterator i$1;
            if (customGropIsOffLine == 0) {
                history = this.ciLabelRuleHDao.selectCiLabelRuleList(ciCustomGroupInfo.getCustomGroupId(), Integer.valueOf(1));
                i$1 = history.iterator();

                while (i$1.hasNext()) {
                    CiLabelRule ciCustomLabelSceneRel = (CiLabelRule) i$1.next();
                    if (ciCustomLabelSceneRel.getElementType().intValue() == 2) {
                        CacheBase cache = CacheBase.getInstance();
                        CiLabelInfo ciLabelInfo = cache.getEffectiveLabel(ciCustomLabelSceneRel.getCalcuElement());
                        if (ciLabelInfo == null && !ciCustomLabelSceneRel.getCalcuElement().equals(labelId)) {
                            flag = false;
                            break;
                        }
                    }
                }
            }

            if (flag) {
                ciCustomGroupInfo.setIsLabelOffline(Integer.valueOf(customGropIsOffLine));
                ciCustomGroupInfo.setNewModifyTime(new Date());
                if (ciCustomGroupInfo.getUpdateCycle().intValue() == 4) {
                    history = this.marketService.queryCustomLabelSceneRelsByType(ciCustomGroupInfo.getCustomGroupId() + "", Integer.valueOf(ServiceConstants.RECOM_SHOW_TYPE_CUSTOM));
                    i$1 = history.iterator();

                    while (i$1.hasNext()) {
                        CiCustomLabelSceneRel ciCustomLabelSceneRel1 = (CiCustomLabelSceneRel) i$1.next();
                        ciCustomLabelSceneRel1.setStatus(Integer.valueOf(ServiceConstants.RECOM_STATUS_INVALIDATE));
                        this.marketService.saveCustomOrLabelSceneRel(ciCustomLabelSceneRel1);
                    }
                }
            }

            this.ciCustomGroupInfoHDao.insertCustomGroup(ciCustomGroupInfo);
            CiCustomModifyHistory history1 = this.addCiCustomModifyInfo(ciCustomGroupInfo, userId);
            this.ciCustomModifyHistoryHDao.insertCiCustomModifyHistory(history1);
        }
    }

    private CiTemplateModifyInfo addCiTemplateModifyInfo(CiTemplateInfo ciTemplateInfo, String userId) throws CIServiceException {
        CiTemplateModifyInfo history = new CiTemplateModifyInfo();
        history.setTemplateId(ciTemplateInfo.getTemplateId());
        history.setTemplateName(ciTemplateInfo.getTemplateName());
        history.setTemplateDesc(ciTemplateInfo.getTemplateDesc());
        history.setModifyUserId(userId);
        history.setLabelOptRuleShow(ciTemplateInfo.getLabelOptRuleShow());
        history.setModifyTime(new Date());
        return history;
    }

    private CiCustomModifyHistory addCiCustomModifyInfo(CiCustomGroupInfo ciCustomGroupInfo, String userId) throws CIServiceException {
        CiCustomModifyHistory ciCustomModifyHistory = new CiCustomModifyHistory();
        ciCustomModifyHistory.setCustomGroupId(ciCustomGroupInfo.getCustomGroupId());
        ciCustomModifyHistory.setCustomGroupDesc(ciCustomGroupInfo.getCustomGroupDesc());
        ciCustomModifyHistory.setCustomGroupName(ciCustomGroupInfo.getCreateUserName());
        ciCustomModifyHistory.setCustomOptRuleShow(ciCustomGroupInfo.getCustomOptRuleShow());
        ciCustomModifyHistory.setStartDate(ciCustomGroupInfo.getStartDate());
        ciCustomModifyHistory.setEndDate(ciCustomGroupInfo.getEndDate());
        ciCustomModifyHistory.setModifyUserId(userId);
        ciCustomModifyHistory.setModifyTime(new Date());
        ciCustomModifyHistory.setIsPrivate(ciCustomGroupInfo.getIsPrivate());
        ciCustomModifyHistory.setUpdateCycle(ciCustomGroupInfo.getUpdateCycle());
        ciCustomModifyHistory.setProdOptRuleShow(ciCustomGroupInfo.getProdOptRuleShow());
        ciCustomModifyHistory.setLabelOptRuleShow(ciCustomGroupInfo.getLabelOptRuleShow());
        ciCustomModifyHistory.setParentCustomId(ciCustomGroupInfo.getParentCustomId());
        ciCustomModifyHistory.setKpiDiffRule(ciCustomGroupInfo.getKpiDiffRule());
        ciCustomModifyHistory.setCreateCityId(ciCustomGroupInfo.getCreateCityId());
        ciCustomModifyHistory.setTemplateId(ciCustomGroupInfo.getTemplateId());
        return ciCustomModifyHistory;
    }

    public List<LabelDetailInfo> getSysRecommendLabelInfos(CiLabelInfo searchBean) {
        List sysRecommendLabelInfos = null;

        try {
            CacheBase e = CacheBase.getInstance();
            sysRecommendLabelInfos = this.labelQueryJdbcDao.getSysRecommendLabelInfoList(searchBean);
            Iterator i$ = sysRecommendLabelInfos.iterator();

            while (i$.hasNext()) {
                LabelDetailInfo labelDetailInfo = (LabelDetailInfo) i$.next();
                LabelDetailInfo info = e.getHotLabelByKey("HOT_LABELS", labelDetailInfo.getLabelId());
                if (info != null) {
                    labelDetailInfo.setIsHot("true");
                }
            }

            return sysRecommendLabelInfos;
        } catch (Exception var7) {
            this.log.error("查询系统推荐标签列表失败", var7);
            throw new CIServiceException("查询系统推荐标签列表失败");
        }
    }

    public LabelDetailInfo getSysRecommendLabelInfo(Integer labelId) {
        LabelDetailInfo sysRecommendLabelInfo = null;

        try {
            CacheBase e = CacheBase.getInstance();
            sysRecommendLabelInfo = this.labelQueryJdbcDao.getSysRecommendLabelInfo(labelId);
            LabelDetailInfo info = e.getHotLabelByKey("HOT_LABELS", sysRecommendLabelInfo.getLabelId());
            if (info != null) {
                sysRecommendLabelInfo.setIsHot("true");
            }

            return sysRecommendLabelInfo;
        } catch (Exception var5) {
            this.log.error("查询系统推荐标签列表失败", var5);
            throw new CIServiceException("查询系统推荐标签列表失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<Map<String, Object>> queryVerticalValueByImport(String userId, Integer columnId, File file, String fileFileName) throws Exception {
        List result = null;
        if (file != null) {
            String filePath = null;
            BufferedReader reader = null;
            String lineStr = null;
            StringBuffer ids = new StringBuffer();

            try {
                filePath = CiUtil.uploadTargetUserFile(file, fileFileName);
                if ("-1".equals(filePath)) {
                    throw new Exception("上传文件目录不存在");
                }
            } catch (Exception var19) {
                this.log.error("获取文件目录信息报错", var19);
            }

            CiMdaSysTableColumn column = this.ciMdaColumnHDao.selectCiMdaSysTableColumnByColumnId(columnId);
            DimTableServiceImpl service = (DimTableServiceImpl) SystemServiceLocator.getInstance().getService("dimTable_serviceTarget");
            String transDimId = column.getDimTransId();
            Integer idDataType = column.getColumnDataTypeId();
            String code = CiUtil.getTxtType(new File(filePath));
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), code));
            int i = 0;

            while ((lineStr = reader.readLine()) != null) {
                if (i == 0 && code.equalsIgnoreCase("UTF-8")) {
                    byte[] define = lineStr.getBytes("UTF-8");
                    lineStr = new String(define, 3, define.length - 3, "UTF-8");
                }

                ++i;
                String var20 = lineStr.split(",")[0].trim();
                if (!var20.startsWith("#")) {
                    if (2 == idDataType.intValue()) {
                        ids.append("\'").append(var20).append("\',");
                    } else if (1 == idDataType.intValue() && this.isNumeric(var20)) {
                        ids.append(var20).append(",");
                    }
                }
            }

            if (ids.length() > 0) {
                ids.deleteCharAt(ids.length() - 1);
            }

            DimTableDefine var21 = service.findDefineById(transDimId);
            if (!StringUtil.isEmpty(transDimId) && var21 != null) {
                boolean isNeedAuthority = StringUtil.isNotEmpty(column.getIsNeedAuthority()) && column.getIsNeedAuthority().intValue() == 1;
                String cityAuthorityWhereStr = this.getCityAuthoritySql(userId, isNeedAuthority, var21.getDimTablename());
                result = this.ciLabelInfoJDao.selectDimValueByImport(ids.toString(), var21, cityAuthorityWhereStr);
            }
        }

        return result;
    }

    public List<CiLabelInfo> queryChildrenByIdAndName(Integer parentId, String labelName) throws CIServiceException {
        ArrayList labelList = new ArrayList();

        try {
            CacheBase e = CacheBase.getInstance();
            CopyOnWriteArrayList allEffectiveLabel = e.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
            Iterator i$ = allEffectiveLabel.iterator();

            while (i$.hasNext()) {
                CiLabelInfo cilabelInfo = (CiLabelInfo) i$.next();
                if (cilabelInfo.getParentId().equals(parentId)) {
                    if (StringUtil.isNotEmpty(labelName)) {
                        if (cilabelInfo.getLabelName().toLowerCase().contains(labelName.toLowerCase())) {
                            labelList.add(cilabelInfo);
                        }
                    } else {
                        labelList.add(cilabelInfo);
                    }
                }
            }

            return labelList;
        } catch (Exception var8) {
            this.log.error("根据标签Id查询标签失败", var8);
            throw new CIServiceException("根据标签Id查询标签失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public String queryLikeLabelValueByImport(File file, String fileFileName) throws Exception {
        StringBuffer ids = new StringBuffer();
        if (file != null) {
            String filePath = null;
            BufferedReader reader = null;
            String lineStr = null;

            try {
                filePath = CiUtil.uploadTargetUserFile(file, fileFileName);
                if ("-1".equals(filePath)) {
                    throw new Exception("上传文件目录不存在");
                }
            } catch (Exception var10) {
                this.log.error("获取文件目录信息报错", var10);
            }

            String code = CiUtil.getTxtType(new File(filePath));
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), code));
            int i = 0;

            while ((lineStr = reader.readLine()) != null) {
                if (i == 0 && code.equalsIgnoreCase("UTF-8")) {
                    byte[] b = lineStr.getBytes("UTF-8");
                    lineStr = new String(b, 3, b.length - 3, "UTF-8");
                }

                ++i;
                ids.append(lineStr).append(",");
            }

            if (ids.length() > 0) {
                ids.deleteCharAt(ids.length() - 1);
            }
        }

        return ids.toString();
    }

    private boolean isNumeric(String str) {
        if (StringUtil.isNotEmpty(str)) {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(str);
            return isNum.matches();
        } else {
            return false;
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiLabelInfo> queryVertAndEnumLabelListFromCache() throws CIServiceException {
        ArrayList categoryList = new ArrayList();

        try {
            CacheBase e = CacheBase.getInstance();
            CopyOnWriteArrayList allEffectiveLabelList = e.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
            int labelCategoryType = Integer.valueOf(Configure.getInstance().getProperty("LABEL_CATEGORY_TYPE")).intValue();
            Iterator i$;
            CiLabelInfo ciLabelInfo;
            CiLabelExtInfo ciLabelExtInfo;
            Set columnRel;
            boolean _flag;
            Iterator i$1;
            CiLabelVerticalColumnRel ciLabelVerticalColumnRel;
            if (labelCategoryType == 2) {
                i$ = allEffectiveLabelList.iterator();

                while (true) {
                    do {
                        do {
                            do {
                                if (!i$.hasNext()) {
                                    return categoryList;
                                }

                                ciLabelInfo = (CiLabelInfo) i$.next();
                            } while (ciLabelInfo.getCiLabelExtInfo().getLabelLevel().intValue() < 3);
                        } while (null == ciLabelInfo.getLabelTypeId());

                        if (ciLabelInfo.getLabelTypeId().intValue() == 5) {
                            categoryList.add(ciLabelInfo);
                        }
                    } while (ciLabelInfo.getLabelTypeId().intValue() != 8);

                    ciLabelExtInfo = ciLabelInfo.getCiLabelExtInfo();
                    columnRel = ciLabelExtInfo.getCiLabelVerticalColumnRels();
                    _flag = false;
                    i$1 = columnRel.iterator();

                    while (i$1.hasNext()) {
                        ciLabelVerticalColumnRel = (CiLabelVerticalColumnRel) i$1.next();
                        if (ciLabelVerticalColumnRel.getLabelTypeId().intValue() == 5) {
                            _flag = true;
                            break;
                        }
                    }

                    if (_flag) {
                        categoryList.add(ciLabelInfo);
                    }
                }
            } else if (labelCategoryType == 3) {
                i$ = allEffectiveLabelList.iterator();

                while (true) {
                    do {
                        do {
                            do {
                                if (!i$.hasNext()) {
                                    return categoryList;
                                }

                                ciLabelInfo = (CiLabelInfo) i$.next();
                            } while (ciLabelInfo.getCiLabelExtInfo().getLabelLevel().intValue() < 4);
                        } while (null == ciLabelInfo.getLabelTypeId());

                        if (ciLabelInfo.getLabelTypeId().intValue() == 5) {
                            categoryList.add(ciLabelInfo);
                        }
                    } while (ciLabelInfo.getLabelTypeId().intValue() != 8);

                    ciLabelExtInfo = ciLabelInfo.getCiLabelExtInfo();
                    columnRel = ciLabelExtInfo.getCiLabelVerticalColumnRels();
                    _flag = false;
                    i$1 = columnRel.iterator();

                    while (i$1.hasNext()) {
                        ciLabelVerticalColumnRel = (CiLabelVerticalColumnRel) i$1.next();
                        if (ciLabelVerticalColumnRel.getLabelTypeId().intValue() == 5) {
                            _flag = true;
                            break;
                        }
                    }

                    if (_flag) {
                        categoryList.add(ciLabelInfo);
                    }
                }
            } else {
                return categoryList;
            }
        } catch (Exception var12) {
            this.log.error("查询所有的枚举与带有枚举标签的纵表标签列表失败", var12);
            throw new CIServiceException("查询所有的枚举与带有枚举标签的纵表标签列表失败");
        }
    }

    public List<CiLabelInfo> queryEnumLabelOrColumnListByLabelIdAndType(String labelId) {
        ArrayList categoryList = new ArrayList();
        CacheBase cacheBase = CacheBase.getInstance();
        CiLabelInfo ciLabelInfo = cacheBase.getEffectiveLabel(labelId);
        int labelTypeId = ciLabelInfo.getLabelTypeId().intValue();
        CiLabelExtInfo ciLabelExtInfo;
        if (labelTypeId == 5) {
            ciLabelExtInfo = ciLabelInfo.getCiLabelExtInfo();
            CiMdaSysTableColumn columnRel = ciLabelExtInfo.getCiMdaSysTableColumn();
            ciLabelInfo.setColumnId(columnRel.getColumnId().toString());
            ciLabelInfo.setColumnCnName(columnRel.getColumnCnName());
            categoryList.add(ciLabelInfo);
        }

        if (labelTypeId == 8) {
            ciLabelExtInfo = ciLabelInfo.getCiLabelExtInfo();
            Set columnRel1 = ciLabelExtInfo.getCiLabelVerticalColumnRels();
            Iterator i$ = columnRel1.iterator();

            while (i$.hasNext()) {
                CiLabelVerticalColumnRel ciLabelVerticalColumnRel = (CiLabelVerticalColumnRel) i$.next();
                if (ciLabelVerticalColumnRel.getLabelTypeId().intValue() == 5) {
                    try {
                        CiLabelInfo e = ciLabelInfo.clone();
                        CiMdaSysTableColumn column = ciLabelVerticalColumnRel.getCiMdaSysTableColumn();
                        e.setColumnCnName(column.getColumnCnName());
                        e.setColumnId(column.getColumnId().toString());
                        categoryList.add(e);
                    } catch (CloneNotSupportedException var12) {
                        this.log.error("复制标签信息失败", var12);
                    }
                }
            }
        }

        return categoryList;
    }

    public List<CiEnumCategoryInfo> queryEnumCategoryInfoList(String cityId, Integer columnId) {
        Object list = new ArrayList();

        try {
            list = this.categoryInfoHDao.selectCiEnumCategoryInfoList(cityId, columnId);
        } catch (Exception var5) {
            this.log.error("根据标签列ID查询分类列表失败", var5);
        }

        return (List) list;
    }

    public int queryVertAndEnumLabelCount(CiLabelInfo ciLabelInfo) {
        int count = 0;

        try {
            count = this.ciLabelInfoJDao.selectVertAndEnumLabelCount(ciLabelInfo);
        } catch (Exception var4) {
            this.log.error("查询所有的枚举与带有枚举标签的纵表标签总数失败", var4);
        }

        return count;
    }

    public List<CiLabelInfo> queryVertAndEnumLabelList(Pager pager, CiLabelInfo ciLabelInfo) {
        Object list = new ArrayList();

        try {
            list = this.ciLabelInfoJDao.selectVertAndEnumLabelList(pager, ciLabelInfo);
        } catch (Exception var5) {
            this.log.error("查询所有的枚举与带有枚举标签的纵表标签列表失败", var5);
        }

        return (List) list;
    }

    public int queryEnumCategoryInfoTotalCountByColumnId(String userId, String cityId, Integer columnId) {
        int count = 0;

        try {
            count = this.categoryInfoJDao.selectEnumCategoryInfoTotalCountByColumnId(userId, cityId, columnId);
        } catch (Exception var6) {
            this.log.error("查询所有的分类列表总数失败", var6);
        }

        return count;
    }

    public List<CiEnumCategoryInfo> queryEnumCategoryInfoPagerListByColumnId(Pager pager, String userId, String cityId, Integer columnId) {
        Object list = new ArrayList();

        try {
            list = this.categoryInfoJDao.selectEnumCategoryInfoPagerListByColumnId(pager, userId, cityId, columnId);
        } catch (Exception var7) {
            this.log.error("查询所有的分类列表失败", var7);
        }

        return (List) list;
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public Map<String, Object> isCategoryInfoNameExist(CiEnumCategoryInfo categoryInfo) throws CIServiceException {
        boolean success = false;
        HashMap returnMap = new HashMap();
        String msg = "";

        try {
            String e = categoryInfo.getEnumCategoryName();
            String cityId = categoryInfo.getCityId();
            Integer columnId = categoryInfo.getColumnId();
            if (StringUtil.isEmpty(e)) {
                msg = "分类名称不能为空！";
            } else if (StringUtil.isEmpty(cityId)) {
                msg = "地市ID不能为空";
            } else if (null == columnId) {
                msg = "标签columnId不能为空";
            } else {
                success = true;
            }

            if (success) {
                List list = this.queryEnumCategoryInfoListByCityIdAndColumnId(cityId, columnId, e);
                if (list != null && list.size() > 0) {
                    msg = "输入的分类名称重名，请重新输入！";
                    success = false;
                }
            }

            returnMap.put("success", Boolean.valueOf(success));
            returnMap.put("msg", msg);
        } catch (Exception var9) {
            msg = "分类重名验证错误！";
            this.log.error(msg, var9);
            success = false;
            returnMap.put("success", Boolean.valueOf(success));
            returnMap.put("msg", msg);
        }

        return returnMap;
    }

    private List<CiEnumCategoryInfo> queryEnumCategoryInfoListByCityIdAndColumnId(String cityId, Integer columnId, String enumCategoryName) {
        Object list = new ArrayList();

        try {
            list = this.categoryInfoJDao.queryEnumCategoryInfoListByCityIdAndColumnId(cityId, columnId, enumCategoryName);
        } catch (Exception var6) {
            this.log.error("根据地市ID与columnID查询分类列表失败", var6);
        }

        return (List) list;
    }

    public void addEnumCategoryInfo(CiEnumCategoryInfo ciEnumCategoryInfo) {
        try {
            this.categoryInfoHDao.insertCiEnumCategoryInfo(ciEnumCategoryInfo);
        } catch (Exception var3) {
            this.log.error("保存枚举维值分类失败", var3);
            throw new CIServiceException("保存枚举维值分类失败");
        }
    }

    public void deleteCategoryInfo(List<String> enumCategoryIds) throws CIServiceException {
        try {
            CiEnumCategoryInfo e = null;
            Iterator iter = enumCategoryIds.iterator();

            while (iter.hasNext()) {
                String enumCategoryId = (String) iter.next();
                e = this.categoryInfoHDao.selectById(enumCategoryId);
                e.setStatus(Integer.valueOf(ServiceConstants.STATUS_DEL));
                this.categoryInfoHDao.insertCiEnumCategoryInfo(e);
                this.ciEnumConditionInfoHDao.deleteCiEnumConditionInfoListByenumCategoryId(enumCategoryId);
            }

        } catch (Exception var5) {
            this.log.error("删除分类失败", var5);
            throw new CIServiceException("删除分类失败");
        }
    }

    public Map<String, Object> importEnumListFile(String userId, Integer columnId, String enumCategoryId, File file, String fileName) {
        boolean success = false;
        new ArrayList();
        HashMap returnMap = new HashMap();
        String msg = "";
        ArrayList conditionInfoList = new ArrayList();

        try {
            this.ciEnumConditionInfoHDao.deleteCiEnumConditionInfoListByenumCategoryId(enumCategoryId);
            List map = this.queryVerticalValueByImport(userId, columnId, file, fileName);
            int e = 0;
            if (map != null) {
                for (Iterator categoryInfo = map.iterator(); categoryInfo.hasNext(); ++e) {
                    Map enumConditionInfoMap = (Map) categoryInfo.next();
                    CiEnumConditionInfo conditionInfo = new CiEnumConditionInfo();
                    conditionInfo.setEnumId(enumConditionInfoMap.get("V_KEY").toString());
                    conditionInfo.setEnumName(enumConditionInfoMap.get("V_NAME").toString());
                    conditionInfo.setEnumCategoryId(enumCategoryId);
                    conditionInfo.setSortNum(Integer.valueOf(e));
                    conditionInfoList.add(conditionInfo);
                    this.ciEnumConditionInfoHDao.insertCiEnumConditionInfo(conditionInfo);
                }
            }

            if (e > 0) {
                CiEnumCategoryInfo var16 = this.categoryInfoHDao.selectById(enumCategoryId);
                var16.setEnumNum(Integer.valueOf(e));
                this.categoryInfoHDao.insertCiEnumCategoryInfo(var16);
            }

            success = true;
            msg = "导入成功";
        } catch (Exception var15) {
            success = false;
            msg = "导入失败";
            this.log.error("上传文件解析异常", var15);
        }

        returnMap.put("success", Boolean.valueOf(success));
        returnMap.put("msg", msg);
        return returnMap;
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public DimCocLabelCountRules queryLabelCountRules(String countRulesCode) throws CIServiceException {
        DimCocLabelCountRules labelCountRules = null;

        try {
            if (StringUtil.isNotEmpty(countRulesCode)) {
                labelCountRules = this.ciLabelCountRulesHDao.selectDimCocLabelCountRule(countRulesCode);
            }

            return labelCountRules;
        } catch (Exception var4) {
            this.log.error("根据规则编码查询标签规则表对象失败", var4);
            throw new CIServiceException("根据规则编码保存标签规则表对象失败");
        }
    }

    public List<TreeNode> queryLabelTree(Integer minLevel, Integer maxLevel) throws CIServiceException {
        ArrayList treeList = new ArrayList();

        try {
            CacheBase e = CacheBase.getInstance();
            boolean message1 = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
            boolean hasAuthority = false;
            String userId = null;
            if (message1) {
                userId = PrivilegeServiceUtil.getUserId();
                hasAuthority = message1 && !PrivilegeServiceUtil.isAdminUser(userId);
            }

            CopyOnWriteArrayList labelIds = e.getKeyList("ALL_EFFECTIVE_LABEL_MAP");
            Iterator i$ = labelIds.iterator();

            while (i$.hasNext()) {
                String idStr = (String) i$.next();
                CiLabelInfo ciLabelInfo = null;
                if (hasAuthority) {
                    ciLabelInfo = e.getEffectiveLabelByUser(idStr, userId);
                } else {
                    ciLabelInfo = e.getEffectiveLabel(idStr);
                }

                CiLabelExtInfo ext = ciLabelInfo.getCiLabelExtInfo();
                if (ext != null && ext.getLabelLevel().intValue() <= maxLevel.intValue() && ext.getLabelLevel().intValue() >= minLevel.intValue()) {
                    TreeNode treeNode = this.generateTreeNode(ciLabelInfo, ext);
                    treeList.add(treeNode);
                }
            }

            return treeList;
        } catch (Exception var14) {
            String message = "标签树查询错误";
            this.log.error(message, var14);
            throw new CIServiceException(message, var14);
        }
    }

    private TreeNode generateTreeNode(CiLabelInfo ciLabelInfo, CiLabelExtInfo ext) {
        TreeNode treeNode = new TreeNode();
        treeNode.setId(String.valueOf(ciLabelInfo.getLabelId()));
        treeNode.setName(ciLabelInfo.getLabelName());
        treeNode.setpId(ciLabelInfo.getParentId() + "");
        treeNode.setTip(ciLabelInfo.getLabelName());
        if (ext != null && ext.getLabelId() != null) {
            if (ext.getIsStatUserNum().intValue() == 0) {
                treeNode.setClick(Boolean.FALSE);
            }

            treeNode.setParam(this.newLabelTreeModel(ciLabelInfo, ext));
        }

        return treeNode;
    }

    private void getLabelDesc(Integer labelId, CiLabelInfo bean) {
        Integer id = null;
        if (StringUtil.isNotEmpty(bean.getLabelLevelDesc())) {
            CiLabelInfo labelInfo = this.ciLabelInfoJDao.selectCiLabelInfoByLabelId(labelId);
            if (labelInfo == null) {
                return;
            }

            bean.setLabelLevelDesc(labelInfo.getLabelName() + "/" + bean.getLabelLevelDesc());
            id = labelInfo.getParentId();
        } else {
            bean.setLabelLevelDesc(bean.getLabelName());
            id = bean.getParentId();
        }

        this.getLabelDesc(id, bean);
    }

    public void adminAdd(CiLabelInfo label, String userId, Integer labelCreateType) throws CIServiceException {
        try {
            this.ciLabelInfoHDao.insertCiLabelInfo(label);
            this.insertOrSubmitApprove(label, 3);
            if (labelCreateType.intValue() == 1) {
                if (null != label.getLabelId()) {
                    this.addCiLabelHistoryInfo(label, Integer.valueOf(2));
                } else {
                    this.addCiLabelHistoryInfo(label, Integer.valueOf(1));
                }
            }

        } catch (Exception var5) {
            this.log.error("新增标签失败", var5);
            throw new CIServiceException("新增标签失败");
        }
    }

    public void addLabelInfoByConfig(CiLabelInfo createLabelInfo) {
        this.ciLabelInfoHDao.insertCiLabelInfo(createLabelInfo);
        this.addCiLabelHistoryInfo(createLabelInfo, Integer.valueOf(1));
        this.insertOrSubmitApprove(createLabelInfo, 3);
        List scenes = createLabelInfo.getSceneList();
        if (scenes != null) {
            Iterator i$ = scenes.iterator();

            while (i$.hasNext()) {
                CiLabelSceneRel item = (CiLabelSceneRel) i$.next();
                item.getId().setLabelId(createLabelInfo.getLabelId());
                this.ciLabelSceneRelHDao.insertCiLabelSceneRel(item);
            }
        }

    }

    public void editLabelInfoByConfig(CiLabelInfo label) {
        CiLabelInfo preLabel = this.queryCiLabelInfoByLabelId(label.getLabelId());
        preLabel.setLabelName(label.getLabelName());
        preLabel.setBusiCaliber(label.getBusiCaliber());
        preLabel.setApplySuggest(label.getApplySuggest());
        preLabel.setBusiLegend(label.getBusiLegend());
        preLabel.setCreateDesc(label.getCreateDesc());
        this.ciLabelInfoHDao.updateCiLabelInfo(preLabel);
        this.addCiLabelHistoryInfo(label, Integer.valueOf(2));
        this.insertOrSubmitApprove(label, 3);
        this.ciLabelSceneRelHDao.deleteLabelSceneByLabelId(String.valueOf(label.getLabelId()));
        List scenes = label.getSceneList();
        if (scenes != null) {
            Iterator i$ = scenes.iterator();

            while (i$.hasNext()) {
                CiLabelSceneRel item = (CiLabelSceneRel) i$.next();
                item.getId().setLabelId(label.getLabelId());
                this.ciLabelSceneRelHDao.insertCiLabelSceneRel(item);
            }
        }

    }

    public List<CiLabelSceneRel> queryLabelSceneByLabelId(String labelId) {
        return this.ciLabelSceneRelHDao.selectLabelSceneByLabelId(labelId);
    }

    private void saveLabelInfo(CiConfigLabeInfo configLabelInfo) throws Exception {
        CiLabelInfo labelInfo = null;
        if (configLabelInfo.getLabelId() != null) {
            labelInfo = this.ciLabelInfoJDao.selectCiLabelInfoByLabelId(configLabelInfo.getLabelId());
        } else {
            labelInfo = new CiLabelInfo();
            labelInfo.setLabelName(configLabelInfo.getLabelName());
            labelInfo.setCreateTime(configLabelInfo.getCreateTime());
            labelInfo.setEffecTime(configLabelInfo.getEffecTime());
            labelInfo.setDeptId(configLabelInfo.getDeptId().toString());
            labelInfo.setCreateUserId(configLabelInfo.getUserId());
            labelInfo.setFailTime(configLabelInfo.getFailTime());
            labelInfo.setBusiCaliber(configLabelInfo.getBusiCaliber());
        }

        labelInfo.setParentId(configLabelInfo.getParentId());
        labelInfo.setLabelTypeId(configLabelInfo.getLabelTypeId());
        labelInfo.setDataStatusId(Integer.valueOf(1));
        labelInfo.setUpdateCycle(configLabelInfo.getUpdateCycle());
        labelInfo.setEffecTime(configLabelInfo.getEffecTime());
        labelInfo.setFailTime(configLabelInfo.getFailTime());
        this.getLabelDesc(configLabelInfo.getLabelId(), labelInfo);
        this.adminAdd(labelInfo, configLabelInfo.getUserId(), configLabelInfo.getLabelCreateType());
        configLabelInfo.setLabelId(labelInfo.getLabelId());
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public CiMdaSysTable queryMdaSysTable(Integer tableId) throws CIServiceException {
        CiMdaSysTable sysTable = null;

        try {
            sysTable = this.ciMdaTableHDao.selectTableInfoById(tableId);
            return sysTable;
        } catch (Exception var4) {
            this.log.error("查询系统配置表对象失败", var4);
            throw new CIServiceException("查询系统配置表对象失败");
        }
    }

    private String addOne(String codeStr) {
        String[] strs = codeStr.split("[^0-9]");
        String numStr = strs[strs.length - 1];
        if (numStr != null && numStr.length() > 0) {
            int n = numStr.length();
            int num = Integer.parseInt(numStr) + 1;
            String added = String.valueOf(num);
            n = Math.min(n, added.length());
            return codeStr.subSequence(0, codeStr.length() - n) + added;
        } else {
            throw new NumberFormatException();
        }
    }

    private String getOldCode(String pColumnName) {
        this.log.info("getOldCode pColumnName :" + pColumnName);
        CacheBase cach = CacheBase.getInstance();
        String lastColumn = "";
        String labelColumn = "";
        Map map = cach.getAllTableColumnNameMap();
        Iterator i$ = map.keySet().iterator();

        while (true) {
            String lastColumnName;
            do {
                String columnName;
                String parentColumnName;
                do {
                    do {
                        if (!i$.hasNext()) {
                            if (StringUtil.isNotEmpty(lastColumn)) {
                                labelColumn = this.addOne(pColumnName + "_" + lastColumn);
                            } else if (pColumnName.split("_").length > 3) {
                                labelColumn = this.addOne(pColumnName + "_000");
                            } else {
                                labelColumn = this.addOne(pColumnName + "_00");
                            }

                            return labelColumn;
                        }

                        Object key = i$.next();
                        columnName = (String) map.get(key);
                    } while (!columnName.contains("_"));

                    parentColumnName = columnName.substring(0, columnName.lastIndexOf("_"));
                } while (!pColumnName.equals(parentColumnName));

                lastColumnName = columnName.substring(columnName.lastIndexOf("_") + 1, columnName.length());
            } while (!StringUtil.isEmpty(lastColumn) && lastColumnName.compareTo(lastColumn) <= 0);

            lastColumn = lastColumnName;
        }
    }

    public void addMdaColumn(CiMdaSysTableColumn mdaSysTableColumn) throws CIServiceException {
        try {
            this.ciMdaColumnHDao.insertColumnInfo(mdaSysTableColumn);
        } catch (Exception var3) {
            this.log.error("保存系统配置表列对象失败", var3);
            throw new CIServiceException("保存系统配置表列对象失败");
        }
    }

    private void saveLabelColumn(CiConfigLabeInfo configLabelInfo) throws Exception {
        CiMdaSysTable mdaSysTable = null;
        if (configLabelInfo.getCreateTableType().equals("2")) {
            mdaSysTable = this.ciMdaTableHDao.selectTableInfoByName(configLabelInfo.getTableName());
            if (null == mdaSysTable) {
                mdaSysTable = new CiMdaSysTable();
                mdaSysTable.setTableName(configLabelInfo.getTableName());
                mdaSysTable.setUpdateCycle(configLabelInfo.getUpdateCycle());
                if (8 == configLabelInfo.getLabelTypeId().intValue()) {
                    mdaSysTable.setTableType(Integer.valueOf(8));
                } else {
                    mdaSysTable.setTableType(Integer.valueOf(1));
                }

                this.ciMdaTableHDao.insertTableInfo(mdaSysTable);
                CacheBase.getInstance().initMdaSysTable();
            }

            configLabelInfo.setTableId(mdaSysTable.getTableId());
        } else {
            mdaSysTable = this.queryMdaSysTable(configLabelInfo.getTableId());
        }

        if (mdaSysTable == null) {
            this.log.error("系统配置表对象为空");
            throw new CIServiceException("系统配置表对象为空");
        } else {
            CiMdaSysTableColumn tableColumn = new CiMdaSysTableColumn();
            if (configLabelInfo.getColumnId() != null) {
                tableColumn = this.ciMdaColumnHDao.selectCiMdaSysTableColumnByColumnId(configLabelInfo.getColumnId());
            }

            tableColumn.setCiMdaSysTable(mdaSysTable);
            tableColumn.setColumnName("L" + configLabelInfo.getLabelId());
            if (configLabelInfo.getLabelTypeId().intValue() == 5) {
                tableColumn.setDimTransId(configLabelInfo.getDimTransId());
            }

            if (StringUtil.isNotEmpty(configLabelInfo.getDataType())) {
                if (configLabelInfo.getDataType().toLowerCase().contains("char")) {
                    tableColumn.setColumnDataTypeId(Integer.valueOf(2));
                } else {
                    tableColumn.setColumnDataTypeId(Integer.valueOf(1));
                }
            }

            tableColumn.setDataType(configLabelInfo.getDataType());
            tableColumn.setIsNeedAuthority(configLabelInfo.getIsNeedAuthority());
            this.addMdaColumn(tableColumn);
            configLabelInfo.setColumnId(tableColumn.getColumnId());
        }
    }

    public void addLabelCountRules(DimCocLabelCountRules labelCountRules) throws CIServiceException {
        try {
            this.ciLabelCountRulesHDao.insertDimCocLabelCountRule(labelCountRules);
        } catch (Exception var3) {
            this.log.error("保存标签规则表对象失败", var3);
            throw new CIServiceException("保存标签规则表对象失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public String queryMaxCountRules() throws CIServiceException {
        String countRules = null;

        try {
            countRules = this.ciLabelCountRulesHDao.selectMaxLabelCountRule();
            return countRules;
        } catch (Exception var3) {
            this.log.error("查询标签规则最大id失败", var3);
            throw new CIServiceException("查询标签规则最大id失败");
        }
    }

    private void saveLabelCountRules(CiConfigLabeInfo configLabelInfo) throws Exception {
        if (configLabelInfo.getLabelTypeId().intValue() != 8) {
            DimCocLabelCountRules labelCountRules = new DimCocLabelCountRules();
            BeanUtils.copyProperties(configLabelInfo, labelCountRules);
            labelCountRules.setEffectiveTime(configLabelInfo.getEffecTime());
            labelCountRules.setInvalidTime(configLabelInfo.getFailTime());
            labelCountRules.setLabelTableConf("1");
            if (5 == configLabelInfo.getLabelTypeId().intValue()) {
                labelCountRules.setCountRules(configLabelInfo.getDependIndex());
            }

            if (StringUtil.isNotEmpty(labelCountRules.getCountRulesCode())) {
                this.addLabelCountRules(labelCountRules);
            } else {
                String maxCountRules = this.queryMaxCountRules();
                if (StringUtil.isNotEmpty(maxCountRules)) {
                    labelCountRules.setCountRulesCode(this.addOne(maxCountRules));
                } else {
                    labelCountRules.setCountRulesCode("R_00001");
                }

                configLabelInfo.setCountRulesCode(labelCountRules.getCountRulesCode());
                this.addLabelCountRules(labelCountRules);
            }
        }

    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public CiLabelExtInfo queryCiLabelExtInfoById(Integer labelId) {
        return this.ciLabelExtInfoHDao.getById(labelId);
    }

    public void addLabelExtInfo(CiLabelExtInfo labelExtInfo) throws CIServiceException {
        try {
            this.ciLabelExtInfoHDao.insertCiLabelExtInfo(labelExtInfo);
        } catch (Exception var3) {
            this.log.error("保存标签扩展表对象失败", var3);
            throw new CIServiceException("保存标签扩展表对象失败");
        }
    }

    private void saveLabelExtInfo(CiConfigLabeInfo configLabelInfo) throws Exception {
        CiLabelExtInfo pLabelExtInfo = this.queryCiLabelExtInfoById(configLabelInfo.getParentId());
        CiLabelExtInfo labelExtInfo = new CiLabelExtInfo();
        labelExtInfo.setLabelId(configLabelInfo.getLabelId());
        labelExtInfo.setLabelLevel(Integer.valueOf(pLabelExtInfo.getLabelLevel().intValue() + 1));
        labelExtInfo.setIsLeaf(Integer.valueOf(1));
        labelExtInfo.setMinVal(Double.valueOf(0.0D));
        labelExtInfo.setMaxVal(Double.valueOf(1.0D));
        labelExtInfo.setIsStatUserNum(Integer.valueOf(1));
        new CiMdaSysTableColumn();
        CiMdaSysTableColumn tableColumn = this.ciMdaColumnHDao.selectCiMdaSysTableColumnByColumnId(configLabelInfo.getColumnId());
        labelExtInfo.setCiMdaSysTableColumn(tableColumn);
        labelExtInfo.setCountRulesCode(configLabelInfo.getCountRulesCode());
        this.addLabelExtInfo(labelExtInfo);
    }

    public void saveConfigLabelInfo(CiConfigLabeInfo configLabelInfo, List<CiMdaSysTableColumn> ciMdaSysTableColumnList) throws CIServiceException {
        try {
            this.saveLabelInfo(configLabelInfo);
            this.saveLabelColumn(configLabelInfo);
            this.saveLabelCountRules(configLabelInfo);
            this.saveLabelExtInfo(configLabelInfo);
            this.saveVertLabelColumn(configLabelInfo, ciMdaSysTableColumnList);
        } catch (Exception var4) {
            this.log.error("保存标签配置信息失败", var4);
            throw new CIServiceException("保存标签配置信息失败");
        }
    }

    private void saveVertLabelColumn(CiConfigLabeInfo configLabelInfo, List<CiMdaSysTableColumn> ciMdaSysTableColumnList) throws Exception {
        if (null != ciMdaSysTableColumnList && ciMdaSysTableColumnList.size() > 0) {
            CiMdaSysTable mdaSysTable = this.queryMdaSysTable(configLabelInfo.getTableId());
            if (mdaSysTable == null) {
                this.log.error("系统配置表对象为空");
                throw new CIServiceException("系统配置表对象为空");
            }

            int sortNum = 0;
            CiMdaSysTableColumn tableColumn;
            if (8 == configLabelInfo.getLabelTypeId().intValue()) {
                List i$ = this.ciMdaSysTableColumnJDao.selectCiMdaSysTableColumnList(configLabelInfo.getLabelId());
                Iterator ciMdaSysTableColumn = i$.iterator();

                while (ciMdaSysTableColumn.hasNext()) {
                    tableColumn = (CiMdaSysTableColumn) ciMdaSysTableColumn.next();
                    this.ciMdaColumnHDao.deleteCiMdaSysTableColumnByColumnId(tableColumn.getColumnId());
                }

                this.ciLabelVerticalColumnRelHDao.deleteCiLabelVerticalColumnRelByLabelId(configLabelInfo.getLabelId());
            }

            Iterator var10 = ciMdaSysTableColumnList.iterator();

            while (var10.hasNext()) {
                CiMdaSysTableColumn var11 = (CiMdaSysTableColumn) var10.next();
                if (null != var11) {
                    tableColumn = new CiMdaSysTableColumn();
                    if (var11.getColumnId() != null) {
                        tableColumn = this.ciMdaColumnHDao.selectCiMdaSysTableColumnByColumnId(configLabelInfo.getColumnId());
                    }

                    tableColumn.setCiMdaSysTable(mdaSysTable);
                    if (StringUtil.isNotEmpty(var11.getColumnName())) {
                        tableColumn.setColumnName(var11.getColumnName());
                    } else {
                        tableColumn.setColumnName("L" + configLabelInfo.getLabelId());
                    }

                    if (var11.getVertLabelTypeId().intValue() == 5) {
                        tableColumn.setDimTransId(var11.getDimTransId());
                    }

                    if (StringUtil.isNotEmpty(var11.getDataType())) {
                        if (var11.getDataType().toLowerCase().contains("char")) {
                            tableColumn.setColumnDataTypeId(Integer.valueOf(2));
                        } else {
                            tableColumn.setColumnDataTypeId(Integer.valueOf(1));
                        }
                    }

                    tableColumn.setColumnCnName(var11.getColumnCnName());
                    tableColumn.setDataType(var11.getDataType());
                    tableColumn.setIsNeedAuthority(var11.getIsNeedAuthority());
                    this.addMdaColumn(tableColumn);
                    CiLabelVerticalColumnRel columnRel = new CiLabelVerticalColumnRel();
                    CiLabelVerticalColumnRelId id = new CiLabelVerticalColumnRelId(configLabelInfo.getLabelId(), tableColumn.getColumnId());
                    columnRel.setId(id);
                    columnRel.setLabelTypeId(var11.getVertLabelTypeId());
                    columnRel.setIsMustColumn(var11.getIsMustColumn());
                    columnRel.setSortNum(Integer.valueOf(sortNum));
                    ++sortNum;
                    this.ciLabelVerticalColumnRelHDao.insertCiLabelVerticalColumnRel(columnRel);
                }
            }
        }

    }

    public DimCocIndexInfo queryDimCocIndexInfoById(String id) throws CIServiceException {
        DimCocIndexInfo dimCocIndexInfo = null;

        try {
            if (StringUtil.isNotEmpty(id)) {
                dimCocIndexInfo = this.ciDimCocIndexInfoHDao.selectDimCocIndexInfobyId(id);
            }

            return dimCocIndexInfo;
        } catch (Exception var4) {
            this.log.error("查询指标信息对象失败", var4);
            throw new CIServiceException("查询指标信息对象失败");
        }
    }

    public DimCocIndexTableInfo queryDimCocIndexTableInfoById(String id) throws CIServiceException {
        DimCocIndexTableInfo dimCocIndexTableInfo = null;

        try {
            if (StringUtil.isNotEmpty(id)) {
                dimCocIndexTableInfo = this.ciDimCocIndexTableInfoHDao.selectDimCocIndexTableInfoById(id);
            }

            return dimCocIndexTableInfo;
        } catch (Exception var4) {
            this.log.error("查询指标宽表信息失败", var4);
            throw new CIServiceException("查询指标宽表信息失败");
        }
    }

    public void publishLabel(CiConfigLabeInfo configLabel, String userId) throws CIServiceException {
        try {
            CiLabelInfo e = this.ciLabelInfoHDao.selectCiLabelInfoById(configLabel.getLabelId());
            CiLabelExtInfo extInfo = this.ciLabelExtInfoHDao.getById(e.getLabelId());
            if (e.getLabelTypeId().intValue() == 3) {
                CiMdaSysTableColumn tableColumn = extInfo.getCiMdaSysTableColumn();
                DimTableDefine define = configLabel.getDefine();
                List listMap = configLabel.getDimTransList();
                Iterator i$ = listMap.iterator();

                while (i$.hasNext()) {
                    Map map = (Map) i$.next();
                    String dimId = map.get(define.getDimCodeCol()).toString();
                    String dimName = map.get(define.getDimValueCol()).toString();
                    CiLabelInfo attrLabelInfo = new CiLabelInfo();
                    attrLabelInfo.setLabelName(dimName);
                    attrLabelInfo.setCreateTime(new Date());
                    attrLabelInfo.setUpdateCycle(e.getUpdateCycle());
                    attrLabelInfo.setEffecTime(e.getEffecTime());
                    attrLabelInfo.setFailTime(e.getFailTime());
                    attrLabelInfo.setParentId(e.getLabelId());
                    attrLabelInfo.setLabelTypeId(e.getLabelTypeId());
                    attrLabelInfo.setCreateUserId(e.getCreateUserId());
                    attrLabelInfo.setDeptId(e.getDeptId());
                    attrLabelInfo.setPublishUserId(e.getCreateUserId());
                    attrLabelInfo.setDataStatusId(Integer.valueOf(1));
                    this.adminAdd(attrLabelInfo, userId, Integer.valueOf(1));
                    CiLabelExtInfo attrExtInfo = new CiLabelExtInfo();
                    attrExtInfo.setLabelId(attrLabelInfo.getLabelId());
                    attrExtInfo.setIsStatUserNum(Integer.valueOf(1));
                    attrExtInfo.setIsLeaf(Integer.valueOf(1));
                    attrExtInfo.setMinVal(Double.valueOf(0.0D));
                    attrExtInfo.setMaxVal(Double.valueOf(1.0D));
                    attrExtInfo.setAttrVal(dimId);
                    attrExtInfo.setLabelLevel(Integer.valueOf(extInfo.getLabelLevel().intValue() + 1));
                    attrExtInfo.setCiMdaSysTableColumn(tableColumn);
                    this.addLabelExtInfo(attrExtInfo);
                    this.insertOrSubmitApprove(attrLabelInfo, 4);
                }
            }

            this.insertOrSubmitApprove(e, 4);
        } catch (Exception var14) {
            this.log.error("发布标签失败", var14);
            throw new CIServiceException("发布标签失败");
        }
    }

    public void saveLabelClassify(CiLabelInfo ciLabelInfo) throws CIServiceException {
        try {
            CiLabelExtInfo e = null;
            CiMdaSysTableColumn tableColumn = null;
            CacheBase cach = CacheBase.getInstance();
            Integer labelId = ciLabelInfo.getLabelId();
            String createUserId = PrivilegeServiceUtil.getUserId();
            Integer deptId = PrivilegeServiceUtil.getUserDeptId(createUserId);
            String deptIdStr = "";
            if (deptId != null) {
                deptIdStr = Integer.toString(deptId.intValue());
            }

            if (null != labelId) {
                CiLabelInfo tableName = this.queryCiLabelInfoById(labelId);
                tableName.setLabelName(ciLabelInfo.getLabelName());
                tableName.setParentId(ciLabelInfo.getParentId());
                tableName.setCreateDesc(ciLabelInfo.getCreateDesc());
                this.modify(tableName);
                ciLabelInfo = tableName;
                this.addCiLabelHistoryInfo(tableName, Integer.valueOf(2));
                e = this.ciLabelExtInfoHDao.getById(labelId);
                tableColumn = e.getCiMdaSysTableColumn();
            } else {
                ciLabelInfo.setCreateTime(new Date());
                ciLabelInfo.setCreateUserId(createUserId);
                ciLabelInfo.setDeptId(deptIdStr);
                ciLabelInfo.setUpdateCycle(Integer.valueOf(2));
                ciLabelInfo.setLabelTypeId(Integer.valueOf(1));
                ciLabelInfo.setEffecTime(new Date());
                ciLabelInfo.setFailTime(DateUtil.string2Date("2099-06-30", "yyyy-MM-dd"));
                ciLabelInfo.setBusiCaliber("");
                this.addLabelInfo(ciLabelInfo);
                this.addCiLabelHistoryInfo(ciLabelInfo, Integer.valueOf(1));
                e = new CiLabelExtInfo();
                tableColumn = new CiMdaSysTableColumn();
            }

            String tableName1 = "first_second_label_no_table";
            CiMdaSysTable table = this.ciMdaTableHDao.selectTableInfoByName(tableName1);
            if (null == table) {
                table = new CiMdaSysTable();
                table.setTableName(tableName1);
                this.ciMdaTableHDao.insertTableInfo(table);
            }

            tableColumn.setColumnName("L" + ciLabelInfo.getLabelId());
            tableColumn.setCiMdaSysTable(table);
            this.addMdaColumn(tableColumn);
            e.setLabelId(ciLabelInfo.getLabelId());
            CiLabelExtInfo extInfo = this.queryCiLabelExtInfoById(ciLabelInfo.getParentId());
            if (null != extInfo) {
                e.setLabelLevel(Integer.valueOf(extInfo.getLabelLevel().intValue() + 1));
            } else {
                e.setLabelLevel(Integer.valueOf(1));
            }

            e.setCiMdaSysTableColumn(tableColumn);
            e.setIsLeaf(Integer.valueOf(0));
            e.setIsStatUserNum(Integer.valueOf(0));
            this.addLabelExtInfo(e);
            ciLabelInfo.setCiLabelExtInfo(e);
            CiLabelInfo cacheLabel = cach.getEffectiveLabel(ciLabelInfo.getLabelId().toString());
            if (null != cacheLabel && ciLabelInfo.getDataStatusId() != null && ciLabelInfo.getDataStatusId().intValue() != 2) {
                cach.removeEffectiveLabel(ciLabelInfo.getLabelId().toString());
                cach.addEffectiveLabel(ciLabelInfo.getLabelId().toString(), ciLabelInfo);
            }

        } catch (Exception var13) {
            this.log.error("保存标签分类失败", var13);
            throw new CIServiceException("保存标签分类失败");
        }
    }

    public void addLabelInfo(CiLabelInfo ciLabelInfo) {
        try {
            this.ciLabelInfoHDao.insertCiLabelInfo(ciLabelInfo);
            this.insertOrSubmitApprove(ciLabelInfo, 4);
        } catch (Exception var3) {
            this.log.error("新增标签分类失败", var3);
            throw new CIServiceException("新增标签分类失败");
        }
    }

    public Boolean validEidtLabel(CiLabelInfo ciLabelInfo) {
        Boolean validEidtLabelFlag = Boolean.valueOf(true);
        Integer labelId = ciLabelInfo.getLabelId();
        Integer parentId = ciLabelInfo.getParentId();
        CiLabelExtInfo oldExtInfo = this.queryCiLabelExtInfoById(labelId);
        CiLabelExtInfo parentExtInfo = this.queryCiLabelExtInfoById(parentId);
        List childrenLabelInfo = this.queryToEffectChildrenLabelsById(labelId);
        if (childrenLabelInfo.size() != 0) {
            if (parentExtInfo != null) {
                if (oldExtInfo.getLabelLevel().intValue() != parentExtInfo.getLabelLevel().intValue() + 1) {
                    validEidtLabelFlag = Boolean.valueOf(false);
                }
            } else if (parentId.intValue() != -1) {
                validEidtLabelFlag = Boolean.valueOf(false);
            }
        }

        return validEidtLabelFlag;
    }

    public void onLineClassesLabel(CiLabelInfo ciLabelInfo) throws CIServiceException {
        try {
            ciLabelInfo.setDataStatusId(Integer.valueOf(2));
            this.ciLabelInfoHDao.updateCiLabelInfo(ciLabelInfo);
            CacheBase.getInstance().addEffectiveLabel(ciLabelInfo.getLabelId().toString(), ciLabelInfo);
        } catch (Exception var3) {
            this.log.error("上线标签分类失败", var3);
            throw new CIServiceException("上线标签分类失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public int queryTotalLabelInfoCount(CiLabelInfo ciLabelInfo) throws CIServiceException {
        boolean count = false;

        try {
            int count1 = this.ciLabelInfoJDao.getLabelInfoListCountBySql(ciLabelInfo, "1");
            return count1;
        } catch (Exception var4) {
            this.log.error("查询总记录数失败", var4);
            throw new CIServiceException("查询总记录数失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiLabelInfo> queryPageLabelInfoList(int currPage, int pageSize, CiLabelInfo ciLabelInfo) throws CIServiceException {
        List pageList = null;

        try {
            pageList = this.ciLabelInfoJDao.getPageLabelInfoListBySql(currPage, pageSize, ciLabelInfo, "1");
            Iterator e = pageList.iterator();

            while (e.hasNext()) {
                CiLabelInfo label = (CiLabelInfo) e.next();
                if (StringUtil.isNotEmpty(label.getCreateUserId())) {
                    label.setCreateUserName(PrivilegeServiceUtil.getUserById(label.getCreateUserId()).getUsername());
                }
            }

            return pageList;
        } catch (Exception var7) {
            this.log.error("查询分页记录失败", var7);
            throw new CIServiceException("查询分页记录失败");
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<CiLabelInfo> queryToEffectChildrenLabelsById(Integer labelId) throws CIServiceException {
        new ArrayList();

        try {
            List labelList = this.ciLabelInfoHDao.selectToEffectChildrenLabelsById(labelId);
            return labelList;
        } catch (Exception var5) {
            String message = "查看标签所有未生效、已生效、冷冻期的子标签";
            this.log.error(message, var5);
            throw new CIServiceException(message, var5);
        }
    }

    public List<TreeNode> queryAllLabelTree(Integer minLevel, Integer maxLevel) throws CIServiceException {
        ArrayList treeList = new ArrayList();

        try {
            List e = this.ciLabelInfoJDao.selectEffectAndPublAndNoEffectLabel();
            Iterator message1 = e.iterator();

            while (message1.hasNext()) {
                CiLabelInfo ciLabelInfo = (CiLabelInfo) message1.next();
                CiLabelExtInfo ext = ciLabelInfo.getCiLabelExtInfo();
                if (ext != null && ext.getLabelLevel().intValue() <= maxLevel.intValue() && ext.getLabelLevel().intValue() >= minLevel.intValue()) {
                    TreeNode treeNode = this.generateTreeNode(ciLabelInfo, ext);
                    treeList.add(treeNode);
                }
            }

            return treeList;
        } catch (Exception var9) {
            String message = "标签树查询错误";
            this.log.error(message, var9);
            throw new CIServiceException(message, var9);
        }
    }

    public Map<String, Object> saveImportLabel(File file) throws CIServiceException {
        HashMap resultMap = new HashMap();
        Charset charset = CiUtil.getFileCharset(file);
        boolean success = true;
        int labelNum = 0;
        int labelCount = 0;
        int successCount = 0;
        String error = "";
        String userId = PrivilegeServiceUtil.getUserId();

        try {
            CSVReader e = new CSVReader(new InputStreamReader(new FileInputStream(file), charset));
            HashSet set = new HashSet();

            label445:
            while (true) {
                String[] nextLine;
                CiConfigLabeInfo ciConfiglabelInfo;
                String labelName;
                do {
                    if ((nextLine = e.readNext()) == null) {
                        break label445;
                    }

                    ciConfiglabelInfo = new CiConfigLabeInfo();
                    this.log.info("charset:" + charset.toString());
                    this.log.info("content:" + nextLine);
                    ++labelNum;
                    labelName = nextLine[0];
                } while (labelName.contains("#"));

                ++labelCount;
                if (StringUtil.isEmpty(labelName)) {
                    error = "第" + labelNum + "行标签名称不能为空！";
                    resultMap.put("error", error);
                    success = false;
                    new CIServiceException(error);
                }

                set.add(labelName);
                if (set.size() < labelCount) {
                    error = "第" + labelNum + "行表格中标签名称重复！";
                    success = false;
                    resultMap.put("error", error);
                    break;
                }

                CiLabelInfo bean = new CiLabelInfo();
                bean.setLabelName(labelName);
                Boolean hasSameLabel = this.hasSameLabel(bean);
                if (hasSameLabel.booleanValue()) {
                    error = "第" + labelNum + "行标签名称已存在！";
                    success = false;
                    resultMap.put("error", error);
                    break;
                }

                ciConfiglabelInfo.setLabelName(labelName);
                String labelType = nextLine[1];
                if (StringUtil.isEmpty(labelType)) {
                    error = "第" + labelNum + "行标签类型不能为空！";
                    success = false;
                    resultMap.put("error", error);
                    break;
                }

                if (!labelType.equals("标识型") && !labelType.equals("指标型") && !labelType.equals("枚举型") && !labelType.equals("日期型") && !labelType.equals("文本型") && !labelType.equals("组合型")) {
                    error = "第" + labelCount + "行标签类型必须为[标识型,指标型,枚举型,日期型,文本型,组合型]中的其中一种!";
                    resultMap.put("error", error);
                    success = false;
                    break;
                }

                if ("标识型".equals(labelType)) {
                    ciConfiglabelInfo.setLabelTypeId(Integer.valueOf(1));
                } else if ("指标型".equals(labelType)) {
                    ciConfiglabelInfo.setLabelTypeId(Integer.valueOf(4));
                } else if ("枚举型".equals(labelType)) {
                    ciConfiglabelInfo.setLabelTypeId(Integer.valueOf(5));
                } else if ("日期型".equals(labelType)) {
                    ciConfiglabelInfo.setLabelTypeId(Integer.valueOf(6));
                } else if ("文本型".equals(labelType)) {
                    ciConfiglabelInfo.setLabelTypeId(Integer.valueOf(7));
                } else if ("组合型".equals(labelType)) {
                    ciConfiglabelInfo.setLabelTypeId(Integer.valueOf(8));
                }

                String updateType = nextLine[2];
                if (StringUtil.isEmpty(updateType)) {
                    success = false;
                    error = "第" + labelNum + "行更新周期不能为空！";
                    resultMap.put("error", error);
                    break;
                }

                if (!updateType.equals("日周期") && !updateType.equals("月周期")) {
                    success = false;
                    error = "第" + labelNum + "行更新周期必须为日周期或者月周期";
                    resultMap.put("error", error);
                    break;
                }

                if ("月周期".equals(updateType.toString())) {
                    ciConfiglabelInfo.setUpdateCycle(Integer.valueOf(2));
                } else {
                    ciConfiglabelInfo.setUpdateCycle(Integer.valueOf(1));
                }

                String businessCaliber = nextLine[3];
                if (StringUtil.isEmpty(businessCaliber)) {
                    error = "第" + labelCount + "行业务口径不能为空！";
                    success = false;
                    resultMap.put("error", error);
                    break;
                }

                ciConfiglabelInfo.setBusiCaliber(businessCaliber);

                String dependIndex = nextLine[4];
                if (StringUtil.isEmpty(dependIndex)) {
                    success = false;
                    error = "第" + labelNum + "行依赖指标不能为空！";
                    resultMap.put("error", error);
                    break;
                }

                ciConfiglabelInfo.setDependIndex(String.valueOf(dependIndex));
                if (5 == ciConfiglabelInfo.getLabelTypeId().intValue() && dependIndex.contains(",")) {
                    success = false;
                    error = "第" + labelNum + "行数据，枚举型指标只能选择一个！";
                    resultMap.put("error", error);
                    break;
                }

                if (6 == ciConfiglabelInfo.getLabelTypeId().intValue() && dependIndex.contains(",")) {
                    success = false;
                    error = "第" + labelNum + "行数据，日期型指标只能选择一个！";
                    resultMap.put("error", error);
                    break;
                }

                String countRules = nextLine[5];
                String countRulesDesc = nextLine[6];
                if (StringUtil.isEmpty(countRules)) {
                    error = "第" + labelNum + "行具体规则不能为空！";
                    resultMap.put("error", error);
                    success = false;
                    break;
                }

                if (countRulesDesc == null) {
                    error = "第" + labelNum + "行规则描述不能为空!";
                    resultMap.put("error", error);
                    success = false;
                    break;
                }

                ciConfiglabelInfo.setCountRules(String.valueOf(countRules));
                ciConfiglabelInfo.setCountRulesDesc(String.valueOf(countRulesDesc));
                String parentLabelName = nextLine[7];
                if (StringUtil.isEmpty(parentLabelName)) {
                    error = "第" + labelNum + "行父标签不能为空!";
                    resultMap.put("error", error);
                    success = false;
                    break;
                }

                Integer parentId = this.queryIdByNameForImport(parentLabelName);
                if (parentId == null) {
                    error = "第" + labelNum + "行找不到父标签!";
                    resultMap.put("error", error);
                    success = false;
                    break;
                }

                ciConfiglabelInfo.setParentId(parentId);
                CiLabelInfo labelInfo = this.queryCiLabelInfoByIdFullLoad(parentId);
                CiLabelExtInfo extInfo = labelInfo.getCiLabelExtInfo();
                ciConfiglabelInfo.setParentIdIsLeaf(Integer.valueOf(0));
                if (null != extInfo.getIsLeaf() && 1 == extInfo.getIsLeaf().intValue()) {
                    ciConfiglabelInfo.setParentIdIsLeaf(Integer.valueOf(1));
                }

                Integer labelTypeLevel = Integer.valueOf(Configure.getInstance().getProperty("LABEL_CATEGORY_TYPE"));
                if (extInfo.getLabelLevel().intValue() < labelTypeLevel.intValue() - 1) {
                    error = "第" + labelNum + "行数据，父标签级别不能小于" + (labelTypeLevel.intValue() - 1) + "级!";
                    resultMap.put("error", error);
                    success = false;
                    break;
                }

                if (extInfo.getLabelLevel().intValue() == labelTypeLevel.intValue() + 3) {
                    error = "第" + labelNum + "行数据，父标签级别不能为" + (labelTypeLevel.intValue() + 3) + "级!";
                    resultMap.put("error", error);
                    success = false;
                    break;
                }

                String dimTransId = nextLine[8];
                if (ciConfiglabelInfo.getLabelTypeId().intValue() != 5 && !StringUtil.isEmpty(dimTransId)) {
                    error = "第" + labelNum + "行数据，类型不为枚举型时维表应为空!";
                    resultMap.put("error", error);
                    success = false;
                    break;
                }

                String dataType = nextLine[10];
                if (StringUtil.isEmpty(dataType)) {
                    error = "第" + labelNum + "行数据，数据类型不能为空！";
                    resultMap.put("error", error);
                    success = false;
                    break;
                }

                ciConfiglabelInfo.setDataType(dataType);
                String needAuthority = nextLine[11];
                if (ciConfiglabelInfo.getLabelTypeId().intValue() == 5) {
                    if (dimTransId == null) {
                        error = "第" + labelNum + "行数据，类型为枚举型时，对应维表不能为空！";
                        resultMap.put("error", error);
                        success = false;
                        break;
                    }

                    if (!CacheBase.getInstance().isExistTable(dimTransId.toString())) {
                        error = "第" + labelNum + "行没找到对应维表";
                        resultMap.put("error", error);
                        success = false;
                        break;
                    }

                    if (StringUtil.isEmpty(needAuthority)) {
                        error = "第" + labelNum + "行数据，类型为枚举型时，过滤权限不能为空！";
                        resultMap.put("error", error);
                        success = false;
                        break;
                    }

                    if (!"是".equals(needAuthority) && !"否".equals(needAuthority)) {
                        error = "第" + labelNum + "行数据，过滤权限内容不正确！";
                        resultMap.put("error", error);
                        success = false;
                        break;
                    }

                    if ("是".equals(needAuthority)) {
                        ciConfiglabelInfo.setIsNeedAuthority(Integer.valueOf(1));
                    } else {
                        ciConfiglabelInfo.setIsNeedAuthority(Integer.valueOf(0));
                    }

                    ciConfiglabelInfo.setDimTransId(dimTransId.toString());
                    DimTableDefine tableName = this.dimTableServiceImpl.findDefineById(ciConfiglabelInfo.getDimTransId());
                    String tableId = tableName.getDimCodeColType();
                    if (tableId.equalsIgnoreCase("char") && !dataType.contains(tableId)) {
                        error = "第" + labelNum + "行数据，数据类型内容不正确！";
                        resultMap.put("error", error);
                        success = false;
                        break;
                    }

                    List sceneIds = this.dimTableServiceImpl.getAllDimData(tableName.getDimId(), tableName.getDimPCodeCol(), (String) null, (String) null, (String) null, (String) null, false);
                    ciConfiglabelInfo.setDefine(tableName);
                    ciConfiglabelInfo.setDimTransList(sceneIds);
                }

                String var44 = nextLine[9];
                if (StringUtil.isEmpty(var44)) {
                    error = "第" + labelNum + "行宽表不能为空！";
                    resultMap.put("error", error);
                    success = false;
                    break;
                }

                Integer var45 = CacheBase.getInstance().getLabelTableId(String.valueOf(var44));
                if (var45 == null) {
                    error = "第" + labelNum + "行数据，没找到对应宽表!";
                    resultMap.put("error", error);
                    success = false;
                    break;
                }

                String var46 = nextLine[12];
                ArrayList labelSceneList = new ArrayList();
                if (StringUtil.isNotEmpty(var46)) {
                    String[] sceneIdsArr = var46.split(",");

                    for (int i = 0; i < sceneIdsArr.length; ++i) {
                        if (StringUtil.isNotEmpty(sceneIdsArr[i].trim())) {
                            String sceneValue = CacheBase.getInstance().getDimScene(sceneIdsArr[i].trim());
                            if (StringUtil.isEmpty(sceneValue)) {
                                error = "第" + labelNum + "行数据，没有找到对应的场景分类!";
                                resultMap.put("error", error);
                                success = false;
                                break;
                            }

                            CiLabelSceneRel item = new CiLabelSceneRel();
                            item.setId(new CiLabelSceneRelId());
                            item.getId().setSceneId(sceneIdsArr[i].trim());
                            labelSceneList.add(item);
                        }
                    }
                }

                resultMap.put("success", Boolean.valueOf(success));
                ciConfiglabelInfo.setLabelSceneList(labelSceneList);
                ciConfiglabelInfo.setBusiCaliber(businessCaliber);
                ciConfiglabelInfo.setTableId(var45);
                ciConfiglabelInfo.setUserId(userId);
                ciConfiglabelInfo.setDeptId(PrivilegeServiceUtil.getUserDeptId(userId).toString());
                ciConfiglabelInfo.setLabelCreateType(Integer.valueOf(2));
                ciConfiglabelInfo.setCreateTime(new Date());
                ciConfiglabelInfo.setEffecTime(DateUtil.string2Date("2013-06-30", "yyyy-MM-dd"));
                ciConfiglabelInfo.setFailTime(DateUtil.string2Date("2099-06-30", "yyyy-MM-dd"));
                ciConfiglabelInfo.setLabelPriority(Integer.valueOf(2));
                ciConfiglabelInfo.setCreateTableType("1");
                this.saveConfigLabelInfo(ciConfiglabelInfo, (List) null);
                this.publishLabel(ciConfiglabelInfo, userId);
                this.addLabelSceneList(ciConfiglabelInfo);
                ++successCount;
                if (1 == ciConfiglabelInfo.getParentIdIsLeaf().intValue()) {
                    this.modifyLabelLeaf(ciConfiglabelInfo.getParentId().toString(), Integer.valueOf(0));
                }
            }

            if (!success) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        } catch (Exception var42) {
            success = false;
            this.log.error("导入标签报错", var42);
        } finally {
            resultMap.put("success", Boolean.valueOf(success));
        }

        return resultMap;
    }

    public void saveImportVertConfigLabelInfo(CiConfigLabeInfo configLabelInfo, List<CiMdaSysTableColumn> ciMdaSysTableColumnList) throws CIServiceException {
        try {
            String e = PrivilegeServiceUtil.getUserId();
            Integer deptId = PrivilegeServiceUtil.getUserDeptId(e);
            configLabelInfo.setUserId(e);
            configLabelInfo.setDeptId(deptId.toString());
            configLabelInfo.setLabelCreateType(Integer.valueOf(2));
            configLabelInfo.setCreateTime(new Date());
            configLabelInfo.setEffecTime(DateUtil.string2Date("2013-06-30", "yyyy-MM-dd"));
            configLabelInfo.setFailTime(DateUtil.string2Date("2099-06-30", "yyyy-MM-dd"));
            configLabelInfo.setCreateTableType("1");
            this.saveConfigLabelInfo(configLabelInfo, ciMdaSysTableColumnList);
            this.publishLabel(configLabelInfo, e);
            this.addLabelSceneList(configLabelInfo);
        } catch (Exception var5) {
            this.log.error("导入组合标签信息失败", var5);
            throw new CIServiceException("导入组合标签信息失败");
        }
    }

    public void modifyLabelLeaf(String labelIds, Integer isLeaf) throws CIServiceException {
        try {
            this.ciLabelExtInfoHDao.updateCiLabelExtInfoByLabelIds(labelIds, isLeaf);
        } catch (Exception var4) {
            this.log.error("批量更新标签叶子属性失败", var4);
            throw new CIServiceException("批量更新标签叶子属性失败");
        }
    }

    public Map<String, Object> saveImportLabelClass(File file) throws CIServiceException {
        HashMap resultMap = new HashMap();
        boolean success = true;
        String error = "";
        int labelNum = 0;
        int labelCount = 0;
        int successCount = 0;

        try {
            Charset e = CiUtil.getFileCharset(file);
            CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file), e));
            HashSet set = new HashSet();

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                this.log.info("charset:" + e.toString());
                this.log.info("content:" + nextLine);
                ++labelNum;
                String labelName = nextLine[0];
                if (!labelName.contains("#")) {
                    ++labelCount;
                    CiLabelInfo labelInfo = new CiLabelInfo();
                    if (StringUtil.isEmpty(labelName)) {
                        error = "第" + labelNum + "行分类名称不能为空!";
                        resultMap.put("error", error);
                        success = false;
                        break;
                    }

                    if (labelName.length() > 128) {
                        error = "第" + labelNum + "行分类名称长度超长!";
                        resultMap.put("error", error);
                        success = false;
                        break;
                    }

                    set.add(labelName);
                    if (set.size() < labelCount) {
                        error = "第" + labelNum + "行分类名称重复!";
                        resultMap.put("error", error);
                        success = false;
                        break;
                    }

                    CiLabelInfo bean = new CiLabelInfo();
                    bean.setLabelName(labelName);
                    Boolean hasSameLabel = this.hasSameLabel(bean);
                    if (hasSameLabel.booleanValue()) {
                        error = "第" + labelNum + "行分类名称已存在!";
                        resultMap.put("error", error);
                        success = false;
                        break;
                    }

                    labelInfo.setLabelName(labelName);
                    if (nextLine.length < 2) {
                        error = "第" + labelNum + "行父分类列不存在!";
                        resultMap.put("error", error);
                        success = false;
                        break;
                    }

                    String parentLabelName = nextLine[1];
                    if (StringUtil.isEmpty(parentLabelName)) {
                        error = "第" + labelNum + "行分类不能为空!";
                        resultMap.put("error", error);
                        success = false;
                        break;
                    }

                    if ("根节点".equals(parentLabelName)) {
                        labelInfo.setParentId(Integer.valueOf(-1));
                    } else {
                        Integer parentId = this.queryIdByNameForImport(parentLabelName);
                        if (parentId == null) {
                            error = "第" + labelNum + "行找不到父分类!";
                            resultMap.put("error", error);
                            success = false;
                            break;
                        }

                        Integer labelLevel = this.queryCiLabelExtInfoById(parentId).getLabelLevel();
                        Integer labelClassNum = Integer.valueOf(Configure.getInstance().getProperty("LABEL_CATEGORY_TYPE"));
                        if (labelLevel.intValue() >= labelClassNum.intValue()) {
                            error = "第" + labelNum + "行父分类等级不能大于" + (labelClassNum.intValue() - 1) + "级!";
                            resultMap.put("error", error);
                            success = false;
                            break;
                        }

                        labelInfo.setParentId(parentId);
                    }

                    labelInfo.setDataStatusId(Integer.valueOf(2));
                    this.saveLabelClassify(labelInfo);
                    ++successCount;
                }
            }

            resultMap.put("success", Boolean.valueOf(success));
            resultMap.put("msg", error);
            if (!success) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        } catch (Exception var20) {
            this.log.error("导入标签分类文件报错", var20);
        }

        return resultMap;
    }

    private void insertOrSubmitApprove(CiLabelInfo ciLabelInfo, int flag) {
        try {
            CiApproveStatus e = new CiApproveStatus();
            e.setResourceCreateUserId(ciLabelInfo.getCreateUserId());
            e.setDeptId(ciLabelInfo.getDeptId());
            e.setProcessId("1");
            e.setResourceId(Integer.toString(ciLabelInfo.getLabelId().intValue()));
            e.setResourceName(ciLabelInfo.getLabelName());
            this.ciApproveService.insertOrSubmitApprove(e, flag);
        } catch (Exception var4) {
            this.log.error("保存或提交审批时异常", var4);
        }

    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public List<LabelDetailInfo> queryEffectiveLabelByLabelId(Integer labelId) throws CIServiceException {
        List results = null;

        try {
            CacheBase e = CacheBase.getInstance();
            CopyOnWriteArrayList allEffectiveLabel = e.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
            SimpleDateFormat sdMonFormat = new SimpleDateFormat("yyyyMM");
            SimpleDateFormat sdDayFormat = new SimpleDateFormat("yyyy-MM-dd");
            results = this.labelQueryJdbcDao.getEffectiveLabelByLabelId(labelId);
            Iterator i$ = results.iterator();

            while (i$.hasNext()) {
                LabelDetailInfo info = (LabelDetailInfo) i$.next();
                String effectTime = info.getEffecTime();
                Date d = sdDayFormat.parse(effectTime);
                String effectDate = sdMonFormat.format(d);
                info.setEffectDate(DateUtil.getOffsetDateByDate(effectDate, -1, 0));
                Long customNum = Long.valueOf(info.getCustomNum() == null ? 0L : Long.valueOf(info.getCustomNum()).longValue());
                info.setCustomNum(customNum.toString());
                String currentLabelPath = this.getCurrentLabelPath(info, allEffectiveLabel);
                info.setCurrentLabelPath(currentLabelPath);
                CiLabelInfo ciLabelInfo = this.ciLabelInfoHDao.selectCiLabelInfoById(Integer.valueOf(info.getLabelId()));
                StringBuilder labelSceneNames = new StringBuilder();
                StringBuilder labelSceneIds = new StringBuilder();
                Set ciLabelSceneRels = ciLabelInfo.getCiLabelExtInfo().getCiLabelSceneRels();
                Iterator it = ciLabelSceneRels.iterator();

                while (it.hasNext()) {
                    CiLabelSceneRel ciLabelSceneRel = (CiLabelSceneRel) it.next();
                    labelSceneNames.append(e.getDimScene(ciLabelSceneRel.getId().getSceneId()));
                    labelSceneIds.append(ciLabelSceneRel.getId().getSceneId());
                    if (it.hasNext()) {
                        labelSceneNames.append("&nbsp;&nbsp;");
                        labelSceneIds.append(" ");
                    }
                }

                info.setLabelSceneIds(labelSceneIds.toString());
                info.setLabelSceneNames(labelSceneNames.toString());
            }

            return results;
        } catch (Exception var20) {
            this.log.error("根据标签Id查询标签信息失败", var20);
            throw new CIServiceException("根据标签Id查询标签信息失败", var20);
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED,
            readOnly = true
    )
    public CiLabelInfo queryCiLabelInfoByLabelId(Integer labelId) throws CIServiceException {
        CiLabelInfo ciLabelInfo = null;

        try {
            ciLabelInfo = this.ciLabelInfoJDao.selectCiLabelInfoByLabelId(labelId);
            return ciLabelInfo;
        } catch (Exception var4) {
            this.log.error("根据标签Id查询标签失败", var4);
            throw new CIServiceException("根据标签Id查询标签失败");
        }
    }

    private void addLabelSceneList(CiConfigLabeInfo configLabelInfo) {
        try {
            if (null != configLabelInfo.getLabelSceneList() && configLabelInfo.getLabelSceneList().size() > 0) {
                this.ciLabelSceneRelHDao.deleteLabelSceneByLabelId(String.valueOf(configLabelInfo.getLabelId()));
                List e = configLabelInfo.getLabelSceneList();
                Iterator i$ = e.iterator();

                while (i$.hasNext()) {
                    CiLabelSceneRel item = (CiLabelSceneRel) i$.next();
                    item.getId().setLabelId(configLabelInfo.getLabelId());
                    this.ciLabelSceneRelHDao.insertCiLabelSceneRel(item);
                }
            }

        } catch (Exception var5) {
            this.log.error("保存标签场景分类失败", var5);
            throw new CIServiceException("保存标签场景分类失败");
        }
    }

    public void saveLabelLevel(Integer parentId, CiLabelInfo bean) {
        try {
            CiLabelInfo e = CacheBase.getInstance().getEffectiveLabel(String.valueOf(parentId));
            if (e == null) {
                if (StringUtil.isNotEmpty(bean.getLabelIdLevelDesc())) {
                    bean.setLabelIdLevelDesc("/" + bean.getLabelIdLevelDesc());
                } else {
                    bean.setLabelIdLevelDesc("/");
                }

                return;
            }

            if (StringUtil.isNotEmpty(bean.getLabelIdLevelDesc())) {
                bean.setLabelIdLevelDesc(e.getLabelId() + "/" + bean.getLabelIdLevelDesc());
            } else {
                bean.setLabelIdLevelDesc(e.getLabelId() + "/");
            }

            this.saveLabelLevel(e.getParentId(), bean);
        } catch (Exception var4) {
            this.log.error("保存标签ID层级结构失败 ", var4);
        }

    }

    public void batchUpdateLabelIdLevel() {
        CacheBase cache = CacheBase.getInstance();
        CopyOnWriteArrayList allEffectiveLabelList = cache.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
        Iterator i$ = allEffectiveLabelList.iterator();

        while (i$.hasNext()) {
            CiLabelInfo ciLabelInfo = (CiLabelInfo) i$.next();
            ciLabelInfo.setLabelIdLevelDesc((String) null);
            this.saveLabelLevel(ciLabelInfo.getParentId(), ciLabelInfo);
            this.ciLabelInfoHDao.updateCiLabelInfo(ciLabelInfo);
        }

    }

    public List<CiLabelInfo> queryCiLabelInfoList(Integer minLevel, Integer maxLevel) {
        ArrayList ciLabelInfoList = new ArrayList();
        CacheBase cache = CacheBase.getInstance();
        CopyOnWriteArrayList allEffectiveLabelList = cache.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
        Iterator i$ = allEffectiveLabelList.iterator();

        while (i$.hasNext()) {
            CiLabelInfo ciLabelInfo = (CiLabelInfo) i$.next();
            CiLabelExtInfo ext = ciLabelInfo.getCiLabelExtInfo();
            if (ext != null && ext.getLabelLevel().intValue() >= minLevel.intValue() && ext.getLabelLevel().intValue() <= maxLevel.intValue()) {
                List childrenList = this.queryLabelCategory(ciLabelInfo.getLabelId(), allEffectiveLabelList);
                String categoriesString = "";

                try {
                    categoriesString = JsonUtil.toJson(childrenList);
                } catch (Exception var12) {
                    this.log.error("JSON转换失败", var12);
                }

                ciLabelInfo.setLabelCategories(categoriesString);
                ciLabelInfoList.add(ciLabelInfo);
            }
        }

        return ciLabelInfoList;
    }

    private List<CiLabelCategories> queryLabelCategory(Integer parentId, List<CiLabelInfo> allEffectiveLabelList) throws CIServiceException {
        ArrayList categoryList = new ArrayList();
        int levelType = Integer.valueOf(Configure.getInstance().getProperty("LABEL_CATEGORY_TYPE")).intValue();
        List childrenLabels = this.queryChildrenById(parentId, allEffectiveLabelList);
        Iterator i$ = childrenLabels.iterator();

        while (i$.hasNext()) {
            CiLabelInfo ciLabelInfo = (CiLabelInfo) i$.next();
            if (ciLabelInfo.getCiLabelExtInfo().getLabelLevel().intValue() <= levelType) {
                CiLabelCategories categories = new CiLabelCategories(ciLabelInfo.getLabelId(), ciLabelInfo.getLabelName(), ciLabelInfo.getParentId(), ciLabelInfo.getCiLabelExtInfo().getLabelLevel(), ciLabelInfo.getLabelIdLevelDesc(), ciLabelInfo.getLabelLevelDesc());
                List children = this.queryLabelCategory(ciLabelInfo.getLabelId(), allEffectiveLabelList);
                categories.setChildren(children);
                categoryList.add(categories);
            }
        }

        return categoryList;
    }

    public List<CiLabelInfo> findAllChildByParentId(Integer labelId) {
        ArrayList result = new ArrayList();
        this.findChildByParentId(labelId, result);
        return result;
    }

    private void findChildByParentId(Integer labelId, List<CiLabelInfo> result) {
        List childList = this.selectChildByParentId(labelId);
        result.addAll(childList);
        Iterator i$ = childList.iterator();

        while (i$.hasNext()) {
            CiLabelInfo child = (CiLabelInfo) i$.next();
            this.findChildByParentId(child.getLabelId(), result);
        }

    }

    private List<CiLabelInfo> selectChildByParentId(Integer labelId) {
        ArrayList childList = new ArrayList();
        return childList;
    }

    public List<TreeNode> queryAllEffectLabelTree(Integer minLevel, Integer maxLevel) throws CIServiceException {
        ArrayList treeList = new ArrayList();

        try {
            List e = this.ciLabelInfoJDao.selectEffectLabel();
            Iterator message1 = e.iterator();

            while (message1.hasNext()) {
                CiLabelInfo ciLabelInfo = (CiLabelInfo) message1.next();
                CiLabelExtInfo ext = ciLabelInfo.getCiLabelExtInfo();
                if (ext != null && ext.getLabelLevel().intValue() <= maxLevel.intValue() && ext.getLabelLevel().intValue() >= minLevel.intValue()) {
                    TreeNode treeNode = this.generateTreeNode(ciLabelInfo, ext);
                    treeList.add(treeNode);
                }
            }

            return treeList;
        } catch (Exception var9) {
            String message = "标签树查询错误";
            this.log.error(message, var9);
            throw new CIServiceException(message, var9);
        }
    }
}
