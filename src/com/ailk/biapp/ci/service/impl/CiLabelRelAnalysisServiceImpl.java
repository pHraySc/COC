package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiLabelRelAnalysisJDao;
import com.ailk.biapp.ci.dao.ICiUserLabelRelHDao;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiTemplateInfo;
import com.ailk.biapp.ci.entity.CiUserLabelRel;
import com.ailk.biapp.ci.entity.CiUserLabelRelId;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.CiLabelRelModel;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.service.ICiLabelRelAnalysisService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.IdToName;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiLabelRelAnalysisServiceImpl implements ICiLabelRelAnalysisService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ICiLabelRelAnalysisJDao ciLabelRelAnalysisJDao;
    @Autowired
    private ICustomersManagerService customersManagerService;
    @Autowired
    private ICiUserLabelRelHDao ciUserLabelRelHDao;
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;

    public CiLabelRelAnalysisServiceImpl() {
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public void queryMainLabelUserNum(CiLabelRelModel ciLabelRelModel) throws CIServiceException {
        try {
            this.ciLabelRelAnalysisJDao.selectMainLabelUserNum(ciLabelRelModel);
        } catch (Exception var3) {
            var3.printStackTrace();
            this.log.error("查询主标签客户数数据失败", var3);
            throw new CIServiceException("查询主标签客户数数据失败");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public void queryRelLabelData(CiLabelRelModel ciLabelRelModel) throws CIServiceException {
        try {
            List e = this.getLabelRulesForRel(ciLabelRelModel);
            String userId = PrivilegeServiceUtil.getUserId();
            String countSql = this.customersManagerService.getCountSqlStr(e, ciLabelRelModel.getDataDate(), (String)null, userId);
            int overlapUserNum = this.customersManagerService.queryCount(countSql);
            ciLabelRelModel.setOverlapUserNum(Long.valueOf((long)overlapUserNum));
            long mainLabelUserNum = this.ciLabelInfoService.getCustomNum(ciLabelRelModel.getDataDate(), Integer.valueOf(ciLabelRelModel.getMainLabelId().intValue()), Integer.valueOf(-1), Integer.valueOf(-1), (Long)null, (String)null).longValue();
            ciLabelRelModel.setMainLabelUserNum(Long.valueOf(mainLabelUserNum));
        } catch (Exception var8) {
            var8.printStackTrace();
            this.log.error("查询关联标签客户数、交集、占比数据失败", var8);
            throw new CIServiceException("查询关联标签客户数、交集、占比数据失败");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiLabelRelModel> queryRelLabelByMainLabel(String userId, CiLabelRelModel ciLabelRelModel) throws CIServiceException {
        new ArrayList();
        List relLabelList = null;

        try {
            relLabelList = this.ciLabelRelAnalysisJDao.selectRelLabelByMainLabel(userId, ciLabelRelModel);
            List ciLabelRelModelList = this.selectRelLabelData(ciLabelRelModel, relLabelList);
            return ciLabelRelModelList;
        } catch (Exception var6) {
            var6.printStackTrace();
            this.log.error("查询用户指定主标签关联标签数据失败", var6);
            throw new CIServiceException("查询用户指定主标签关联标签数据失败");
        }
    }

    public void addUserRelLabel(String userId, CiLabelRelModel ciLabelRelModel) throws CIServiceException {
        try {
            String[] e = ciLabelRelModel.getRelLabelIds().split(",");
            if(null != e && e.length > 0) {
                this.ciLabelRelAnalysisJDao.deleteUserLabelRel(userId, ciLabelRelModel.getMainLabelId(), ciLabelRelModel.getRelLabelIds());

                for(int i = 0; i < e.length; ++i) {
                    CiUserLabelRel ciUserLabelRel = new CiUserLabelRel();
                    CiUserLabelRelId ciUserLabelRelId = new CiUserLabelRelId();
                    ciUserLabelRelId.setMainLabelId(ciLabelRelModel.getMainLabelId());
                    ciUserLabelRelId.setUserId(userId);
                    ciUserLabelRelId.setAssociLabelId(Integer.valueOf(e[i]));
                    ciUserLabelRel.setId(ciUserLabelRelId);
                    ciUserLabelRel.setCreateTime(DateUtil.date2String(new Date()));
                    ciUserLabelRel.setStatus(Integer.valueOf(1));
                    this.ciUserLabelRelHDao.insertCiUserLabelRel(ciUserLabelRel);
                }
            }

        } catch (Exception var7) {
            var7.printStackTrace();
            this.log.error("保存用户指定标签与关联标签的关联关系失败", var7);
            throw new CIServiceException("保存用户指定标签与关联标签的关联关系失败");
        }
    }

    public void deleteUserRelLabel(String userId, CiLabelRelModel ciLabelRelModel) throws CIServiceException {
        try {
            CiUserLabelRel e = new CiUserLabelRel();
            e.getId().setMainLabelId(ciLabelRelModel.getMainLabelId());
            e.getId().setUserId(userId);
            e.getId().setAssociLabelId(ciLabelRelModel.getRelLabelId());
            e.setDelTime(new Timestamp((new Date()).getTime()));
            e.setStatus(Integer.valueOf(0));
            this.ciUserLabelRelHDao.insertCiUserLabelRel(e);
        } catch (Exception var4) {
            var4.printStackTrace();
            this.log.error("保存用户指定标签与关联标签的关联关系失败", var4);
            throw new CIServiceException("保存用户指定标签与关联标签的关联关系失败");
        }
    }

    public void addRelLabelAsCustomGroup(String userId, CiLabelRelModel ciLabelRelModel, CiCustomGroupInfo ciCustomGroupInfo) throws CIServiceException {
        try {
            ciCustomGroupInfo.setUpdateCycle(Integer.valueOf(1));
            ciCustomGroupInfo.setCreateTypeId(Integer.valueOf(3));
            ciCustomGroupInfo.setCreateUserId(userId);
            ciCustomGroupInfo.setStatus(Integer.valueOf(1));
            ciCustomGroupInfo.setDataStatus(Integer.valueOf(1));
            List e = this.getLabelRulesForRel(ciLabelRelModel);
            ciCustomGroupInfo.setLabelOptRuleShow(IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", ciLabelRelModel.getMainLabelId()) + " 和 " + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", ciLabelRelModel.getRelLabelId()));
            this.customersManagerService.addCiCustomGroupInfo(ciCustomGroupInfo, e, (List)null, (CiTemplateInfo)null, userId, false, (List)null);
        } catch (Exception var5) {
            var5.printStackTrace();
            this.log.error("将主标签与关联标签的关系保存为客户群失败", var5);
            throw new CIServiceException("将主标签与关联标签的关系保存为客户群失败");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public Long queryCustomNumByLabelId(Integer mainLabelId, String dataDate) throws CIServiceException {
        try {
            Long customNum = this.ciLabelRelAnalysisJDao.getCustomNumByLabelId(mainLabelId, dataDate);
            return customNum;
        } catch (Exception var5) {
            var5.printStackTrace();
            this.log.error("根据标签ID和查询日期获取客户数失败", var5);
            throw new CIServiceException("根据标签ID和查询日期获取客户数失败");
        }
    }

    public List<CiLabelRelModel> queryRelLabelByDate(CiLabelRelModel ciLabelRelModel) throws CIServiceException {
        Object ciLabelRelModelList = new ArrayList();
        ArrayList relLabelList = new ArrayList();

        try {
            if(!StringUtil.isEmpty(ciLabelRelModel.getRelLabelIds())) {
                String[] e = ciLabelRelModel.getRelLabelIds().split(",");

                CiLabelRelModel model;
                for(int i$ = 0; i$ < e.length; ++i$) {
                    model = new CiLabelRelModel();
                    model.setRelLabelId(Integer.valueOf(e[i$]));
                    relLabelList.add(model);
                }

                if(null != relLabelList && relLabelList.size() > 0) {
                    Iterator var8 = relLabelList.iterator();

                    while(var8.hasNext()) {
                        model = (CiLabelRelModel)var8.next();
                        model.setRelLabelName(IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", model.getRelLabelId()));
                    }
                }

                ciLabelRelModelList = this.selectRelLabelData(ciLabelRelModel, relLabelList);
            }

            return (List)ciLabelRelModelList;
        } catch (Exception var7) {
            var7.printStackTrace();
            this.log.error("查询用户指定主标签关联标签数据失败", var7);
            throw new CIServiceException("查询用户指定主标签关联标签数据失败");
        }
    }

    public List<CiLabelRelModel> queryLabelRel(String userId, CiLabelRelModel ciLabelRelModel) throws CIServiceException {
        List relLabelList = null;

        try {
            relLabelList = this.ciLabelRelAnalysisJDao.selectRelLabelByMainLabel(userId, ciLabelRelModel);
            if(null != relLabelList && relLabelList.size() > 0) {
                Iterator e = relLabelList.iterator();

                while(e.hasNext()) {
                    CiLabelRelModel model = (CiLabelRelModel)e.next();
                    model.setRelLabelName(IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", model.getRelLabelId()));
                }
            }

            return relLabelList;
        } catch (Exception var6) {
            var6.printStackTrace();
            this.log.error("查询用户指定主标签关联标签数据失败", var6);
            throw new CIServiceException("查询用户指定主标签关联标签数据失败");
        }
    }

    private List<CiLabelRelModel> selectRelLabelData(CiLabelRelModel ciLabelRelModel, List<CiLabelRelModel> relLabelList) throws Exception {
        ArrayList list = new ArrayList();
        String userId = PrivilegeServiceUtil.getUserId();

        for(int i = 0; i < relLabelList.size(); ++i) {
            CiLabelRelModel labelRelModel = (CiLabelRelModel)relLabelList.get(i);
            labelRelModel.setMainLabelId(ciLabelRelModel.getMainLabelId());
            List ruleList = this.getLabelRulesForRel(labelRelModel);
            String sql = this.customersManagerService.getCountSqlStr(ruleList, ciLabelRelModel.getDataDate(), (String)null, userId);
            this.log.debug("countRelLabelCustomNum relLabelId=" + ((CiLabelRelModel)relLabelList.get(i)).getRelLabelId() + " sql: " + sql);
            long customNum = (long)this.customersManagerService.queryCount(sql);
            CiLabelRelModel model = new CiLabelRelModel();
            model.setMainLabelId(ciLabelRelModel.getMainLabelId());
            model.setMainLabelUserNum(ciLabelRelModel.getMainLabelUserNum());
            model.setDataDate(ciLabelRelModel.getDataDate());
            model.setRelLabelId(((CiLabelRelModel)relLabelList.get(i)).getRelLabelId());
            model.setRelLabelName(IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", model.getRelLabelId()));
            model.setOverlapUserNum(Long.valueOf(customNum));
            list.add(model);
        }

        return list;
    }

    private List<CiLabelRule> getLabelRulesForRel(CiLabelRelModel ciLabelRelModel) {
        CiLabelInfo ciLabelInfoMain = CacheBase.getInstance().getEffectiveLabel(String.valueOf(ciLabelRelModel.getMainLabelId()));
        CiLabelInfo ciLabelInfoRel = CacheBase.getInstance().getEffectiveLabel(String.valueOf(ciLabelRelModel.getRelLabelId()));
        ArrayList ciLabelRuleList = new ArrayList();
        CiLabelRule rule1 = new CiLabelRule();
        CiLabelRule rule2 = new CiLabelRule();
        CiLabelRule rule3 = new CiLabelRule();
        rule1.setCalcuElement(String.valueOf(ciLabelRelModel.getMainLabelId()));
        rule1.setMaxVal(ciLabelInfoMain.getCiLabelExtInfo().getMaxVal());
        rule1.setMinVal(ciLabelInfoMain.getCiLabelExtInfo().getMinVal());
        rule1.setSortNum(Long.valueOf(1L));
        rule1.setElementType(Integer.valueOf(2));
        rule1.setLabelFlag(Integer.valueOf(1));
        rule1.setAttrVal(ciLabelInfoMain.getCiLabelExtInfo().getAttrVal());
        rule2.setCalcuElement("and");
        rule2.setSortNum(Long.valueOf(2L));
        rule2.setElementType(Integer.valueOf(1));
        rule3.setCalcuElement(String.valueOf(ciLabelRelModel.getRelLabelId()));
        rule3.setMaxVal(ciLabelInfoRel.getCiLabelExtInfo().getMaxVal());
        rule3.setMinVal(ciLabelInfoRel.getCiLabelExtInfo().getMinVal());
        rule3.setSortNum(Long.valueOf(3L));
        rule3.setElementType(Integer.valueOf(2));
        rule3.setLabelFlag(Integer.valueOf(1));
        rule3.setAttrVal(ciLabelInfoRel.getCiLabelExtInfo().getAttrVal());
        ciLabelRuleList.add(rule1);
        ciLabelRuleList.add(rule2);
        ciLabelRuleList.add(rule3);
        return ciLabelRuleList;
    }
}
