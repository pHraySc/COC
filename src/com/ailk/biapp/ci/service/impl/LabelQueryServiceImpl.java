package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiApproveStatusHDao;
import com.ailk.biapp.ci.dao.ICiLabelRuleHDao;
import com.ailk.biapp.ci.dao.ICiTemplateInfoJDao;
import com.ailk.biapp.ci.dao.ICiUserAttentionLabelHDao;
import com.ailk.biapp.ci.dao.ICiUserLabelContrastJDao;
import com.ailk.biapp.ci.dao.ICiUserLabelRelJDao;
import com.ailk.biapp.ci.dao.ICiUserUseLabelJDao;
import com.ailk.biapp.ci.dao.ILabelQueryJdbcDao;
import com.ailk.biapp.ci.entity.CiApproveStatus;
import com.ailk.biapp.ci.entity.CiLabelExtInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiTemplateInfo;
import com.ailk.biapp.ci.entity.CiUserAttentionLabel;
import com.ailk.biapp.ci.entity.CiUserAttentionLabelId;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.LabelDetailInfo;
import com.ailk.biapp.ci.model.LabelInfoTreeModel;
import com.ailk.biapp.ci.model.LabelShortInfo;
import com.ailk.biapp.ci.model.TreeNode;
import com.ailk.biapp.ci.service.ILabelQueryService;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("labelQueryService")
@Transactional
public class LabelQueryServiceImpl implements ILabelQueryService {
    private Logger log = Logger.getLogger(LabelQueryServiceImpl.class);
    @Autowired
    private ILabelQueryJdbcDao labelQueryJdbcDao;
    @Autowired
    private ICiApproveStatusHDao ciApproveStatusHDao;
    @Autowired
    private ICiUserAttentionLabelHDao ciUserAttentionLabelHDao;
    @Autowired
    private ICiUserUseLabelJDao ciUserUseLabelJDao;
    @Autowired
    private ICiTemplateInfoJDao ciTemplateInfoJDao;
    @Autowired
    private ICiLabelRuleHDao ciLabelRuleHDao;
    @Autowired
    private ICiUserLabelContrastJDao ciUserLabelContrastJDao;
    @Autowired
    private ICiUserLabelRelJDao ciUserLabelRelJDao;
    public CacheBase cache = CacheBase.getInstance();

    public LabelQueryServiceImpl() {
    }

    public ICiUserLabelRelJDao getCiUserLabelRelJDao() {
        return this.ciUserLabelRelJDao;
    }

    public void setCiUserLabelRelJDao(ICiUserLabelRelJDao ciUserLabelRelJDao) {
        this.ciUserLabelRelJDao = ciUserLabelRelJDao;
    }

    public ICiUserLabelContrastJDao getCiUserLabelContrastJDao() {
        return this.ciUserLabelContrastJDao;
    }

    public void setCiUserLabelContrastJDao(ICiUserLabelContrastJDao ciUserLabelContrastJDao) {
        this.ciUserLabelContrastJDao = ciUserLabelContrastJDao;
    }

    public ICiApproveStatusHDao getCiApproveStatusHDao() {
        return this.ciApproveStatusHDao;
    }

    public void setCiApproveStatusHDao(ICiApproveStatusHDao ciApproveStatusHDao) {
        this.ciApproveStatusHDao = ciApproveStatusHDao;
    }

    public ILabelQueryJdbcDao getLabelQueryJdbcDao() {
        return this.labelQueryJdbcDao;
    }

    public void setLabelQueryJdbcDao(ILabelQueryJdbcDao labelQueryJdbcDao) {
        this.labelQueryJdbcDao = labelQueryJdbcDao;
    }

    public ICiUserAttentionLabelHDao getCiUserAttentionLabelHDao() {
        return this.ciUserAttentionLabelHDao;
    }

    public void setCiUserAttentionLabelHDao(ICiUserAttentionLabelHDao ciUserAttentionLabelHDao) {
        this.ciUserAttentionLabelHDao = ciUserAttentionLabelHDao;
    }

    public ICiUserUseLabelJDao getCiUserUseLabelJDao() {
        return this.ciUserUseLabelJDao;
    }

    public void setCiUserUseLabelJDao(ICiUserUseLabelJDao ciUserUseLabelJDao) {
        this.ciUserUseLabelJDao = ciUserUseLabelJDao;
    }

    public ICiTemplateInfoJDao getCiTemplateInfoJDao() {
        return this.ciTemplateInfoJDao;
    }

    public void setCiTemplateInfoJDao(ICiTemplateInfoJDao ciTemplateInfoJDao) {
        this.ciTemplateInfoJDao = ciTemplateInfoJDao;
    }

    public ICiLabelRuleHDao getCiLabelRuleHDao() {
        return this.ciLabelRuleHDao;
    }

    public void setCiLabelRuleHDao(ICiLabelRuleHDao ciLabelRuleHDao) {
        this.ciLabelRuleHDao = ciLabelRuleHDao;
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<LabelShortInfo> queryLabelByDeptId(String deptId) throws CIServiceException {
        new ArrayList();

        try {
            List labelShortInfoList = this.labelQueryJdbcDao.getLabelByDeptId(Integer.valueOf(deptId));
            return labelShortInfoList;
        } catch (Exception var4) {
            this.log.error("根据部门ID查询部门常用标签列表异常", var4);
            throw new CIServiceException("根据部门ID查询部门常用标签列表异常");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<LabelShortInfo> queryUserAttentionLabel(String userId) throws CIServiceException {
        new ArrayList();

        try {
            List labelShortInfoList = this.labelQueryJdbcDao.getUserAttentionLabel(userId);
            return labelShortInfoList;
        } catch (Exception var4) {
            this.log.error("根据用户ID查询用户关注标签列表异常", var4);
            throw new CIServiceException("根据用户ID查询用户关注标签列表异常");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiUserAttentionLabelId> queryAttentionRecordByLabelId(String labelId) throws CIServiceException {
        new ArrayList();

        try {
            List attentionRecordList = this.labelQueryJdbcDao.queryRecordByLabelId(labelId);
            return attentionRecordList;
        } catch (Exception var4) {
            this.log.error("根据标签ID查询用户关注标签列表异常", var4);
            throw new CIServiceException("根据标签ID查询用户关注标签列表异常");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<LabelShortInfo> queryUserLabelByUserId(String userId) throws CIServiceException {
        new ArrayList();

        try {
            List labelShortInfoList = this.labelQueryJdbcDao.getUserLabelByUserId(userId);
            return labelShortInfoList;
        } catch (Exception var4) {
            this.log.error("根据用户ID查询用户创建标签列表异常", var4);
            throw new CIServiceException("根据用户ID查询用户创建标签列表异常");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public LabelDetailInfo queryLabelDetailInfo(String labelId) throws CIServiceException {
        try {
            LabelDetailInfo labelDetailInfo = this.labelQueryJdbcDao.getLabelDetailInfo(labelId);
            CiApproveStatus labelApproveStatus = this.ciApproveStatusHDao.selectById(new Integer(labelId));
            if(null != labelDetailInfo) {
                String e = labelDetailInfo.getCreateUserId();
                String publishUserId = labelDetailInfo.getPublishUserId();
                IUser num;
                if(StringUtil.isNotEmpty(e)) {
                    num = PrivilegeServiceUtil.getUserById(e);
                    if(null != num) {
                        labelDetailInfo.setCreateUser(num.getUsername());
                    }
                }

                if(StringUtil.isNotEmpty(publishUserId)) {
                    num = PrivilegeServiceUtil.getUserById(publishUserId);
                    if(null != num) {
                        labelDetailInfo.setPublishUser(num.getUsername());
                    }
                }

                long num1 = this.labelQueryJdbcDao.getLabelUseTimesByID(labelId);
                labelDetailInfo.setUseTimes((new Long(num1)).toString());
                String updateCycle = labelDetailInfo.getUpdateCycle();
                if(updateCycle.equals("1")) {
                    labelDetailInfo.setUpdateCycleName("日");
                } else {
                    labelDetailInfo.setUpdateCycleName("月");
                }

                String userId;
                if(null != labelApproveStatus) {
                    userId = labelApproveStatus.getCurrApproveStatusId();
                    String attentionId = CacheBase.getInstance().getNameByKey("DIM_APPROVE_STATUS", userId);
                    labelDetailInfo.setCurrApproveStatus(attentionId);
                }

                userId = PrivilegeServiceUtil.getUserId();
                CiUserAttentionLabelId attentionId1 = new CiUserAttentionLabelId();
                attentionId1.setLabelId(new Integer(labelId));
                attentionId1.setUserId(userId);
                CiUserAttentionLabel ciUserAttentionLabel = this.ciUserAttentionLabelHDao.selectById(attentionId1);
                if(null != ciUserAttentionLabel) {
                    labelDetailInfo.setIsAttention("true");
                } else {
                    labelDetailInfo.setIsAttention("false");
                }

                boolean isNew = this.labelQueryJdbcDao.isNewLabel(labelId);
                if(isNew) {
                    labelDetailInfo.setIsNew("true");
                } else {
                    labelDetailInfo.setIsNew("false");
                }

                List recommedLabels = this.ciUserUseLabelJDao.getSysRecommendLabel(1, 10, "all");
                if(null != recommedLabels && recommedLabels.size() > 0) {
                    Iterator effectTime = recommedLabels.iterator();

                    while(effectTime.hasNext()) {
                        LabelDetailInfo d = (LabelDetailInfo)effectTime.next();
                        if(d.getLabelId().equals(labelId)) {
                            labelDetailInfo.setIsHot("true");
                            break;
                        }
                    }
                } else {
                    labelDetailInfo.setIsHot("false");
                }

                String effectTime1 = labelDetailInfo.getEffecTime();
                Date d1 = (new SimpleDateFormat("yyyy-MM-dd")).parse(effectTime1);
                String effectDate = (new SimpleDateFormat("yyyyMM")).format(d1);
                labelDetailInfo.setEffectDate(DateUtil.getOffsetDateByDate(effectDate, -1, 0));
            }

            return labelDetailInfo;
        } catch (Exception var17) {
            this.log.error("根据标签 ID 查询标签详细信息异常", var17);
            throw new CIServiceException("根据标签 ID 查询标签详细信息异常");
        }
    }

    public List<TreeNode> queryMyLabelTree(String userId, String labelName) {
        ArrayList treeNodes = new ArrayList();
        TreeNode myCreateLabelNode = new TreeNode();
        TreeNode deptLabelNode = new TreeNode();
        TreeNode myAttentionLabelNode = new TreeNode();

        try {
            boolean e = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
            boolean hasAuthority = false;
            if(e) {
                hasAuthority = e && !PrivilegeServiceUtil.isAdminUser(userId);
            }

            myCreateLabelNode.setId("1-1");
            myCreateLabelNode.setpId("0");
            myCreateLabelNode.setName("我创建的标签");
            myCreateLabelNode.setIsParent(Boolean.valueOf(true));
            myCreateLabelNode.setOpen(Boolean.valueOf(false));
            myCreateLabelNode.setTip("我创建的标签");
            List myCreateLabelList = null;
            if(StringUtil.isNotEmpty(labelName)) {
                myCreateLabelList = this.labelQueryJdbcDao.getUserLabelByUserId(userId, labelName);
            } else {
                myCreateLabelList = this.labelQueryJdbcDao.getUserLabelByUserId(userId);
            }

            LabelShortInfo deptUseLabelList;
            String myAttentionLabelList;
            TreeNode label;
            if(null != myCreateLabelList && myCreateLabelList.size() > 0) {
                treeNodes.add(myCreateLabelNode);
                Iterator deptId = myCreateLabelList.iterator();

                label73:
                while(true) {
                    CiLabelInfo i$;
                    do {
                        if(!deptId.hasNext()) {
                            break label73;
                        }

                        deptUseLabelList = (LabelShortInfo)deptId.next();
                        myAttentionLabelList = deptUseLabelList.getLabelId();
                        i$ = this.cache.getEffectiveLabel(myAttentionLabelList);
                    } while(i$.getCiLabelExtInfo() != null && i$.getCiLabelExtInfo().getIsStatUserNum().intValue() == 0);

                    label = this.getTreeNode(deptUseLabelList, "1-1", hasAuthority, userId);
                    treeNodes.add(label);
                }
            }

            deptLabelNode.setId("1-2");
            deptLabelNode.setpId("0");
            deptLabelNode.setName("部门常用标签");
            deptLabelNode.setIsParent(Boolean.valueOf(true));
            deptLabelNode.setOpen(Boolean.valueOf(false));
            deptLabelNode.setTip("部门常用标签");
            Integer deptId1 = PrivilegeServiceUtil.getUserDeptId(userId);
            deptUseLabelList = null;
            List deptUseLabelList1;
            if(StringUtil.isNotEmpty(labelName)) {
                deptUseLabelList1 = this.labelQueryJdbcDao.getLabelByDeptId(deptId1, labelName);
            } else {
                deptUseLabelList1 = this.labelQueryJdbcDao.getLabelByDeptId(deptId1);
            }

            if(null != deptUseLabelList1 && deptUseLabelList1.size() > 0) {
                treeNodes.add(deptLabelNode);
                Iterator myAttentionLabelList1 = deptUseLabelList1.iterator();

                while(myAttentionLabelList1.hasNext()) {
                    LabelShortInfo i$1 = (LabelShortInfo)myAttentionLabelList1.next();
                    label = this.getTreeNode(i$1, "1-2", hasAuthority, userId);
                    treeNodes.add(label);
                }
            }

            myAttentionLabelNode.setId("1-3");
            myAttentionLabelNode.setpId("0");
            myAttentionLabelNode.setName("我关注的标签");
            myAttentionLabelNode.setIsParent(Boolean.valueOf(true));
            myAttentionLabelNode.setOpen(Boolean.valueOf(false));
            myAttentionLabelNode.setTip("我关注的标签");
            myAttentionLabelList = null;
            List myAttentionLabelList2;
            if(StringUtil.isNotEmpty(labelName)) {
                myAttentionLabelList2 = this.labelQueryJdbcDao.getUserAttentionLabel(userId, labelName);
            } else {
                myAttentionLabelList2 = this.labelQueryJdbcDao.getUserAttentionLabel(userId);
            }

            if(null != myAttentionLabelList2 && myAttentionLabelList2.size() > 0) {
                treeNodes.add(myAttentionLabelNode);
                Iterator i$2 = myAttentionLabelList2.iterator();

                while(i$2.hasNext()) {
                    LabelShortInfo label1 = (LabelShortInfo)i$2.next();
                    TreeNode tempNode = this.getTreeNode(label1, "1-3", hasAuthority, userId);
                    treeNodes.add(tempNode);
                }
            }

            return treeNodes;
        } catch (Exception var16) {
            this.log.error("我的标签库查询异常", var16);
            throw new CIServiceException("我的标签库查询异常");
        }
    }

    public List<TreeNode> queryMyTemplateTree(String userId, String templateName) {
        ArrayList treeNodes = new ArrayList();
        TreeNode myTemplateNode = new TreeNode();
        myTemplateNode.setId("1-4");
        myTemplateNode.setpId("0");
        myTemplateNode.setName("我的模板");
        myTemplateNode.setIsParent(Boolean.valueOf(true));
        myTemplateNode.setOpen(Boolean.valueOf(false));
        myTemplateNode.setTip("我的模板");

        try {
            List e = null;
            if(StringUtil.isEmpty(templateName)) {
                e = this.ciTemplateInfoJDao.selectTemplateInfoList(userId);
            } else {
                e = this.ciTemplateInfoJDao.selectTemplateInfoList(userId, templateName);
            }

            if(null != e && e.size() > 0) {
                treeNodes.add(myTemplateNode);
                Iterator i$ = e.iterator();

                while(i$.hasNext()) {
                    CiTemplateInfo templateInfo = (CiTemplateInfo)i$.next();
                    TreeNode treeNode = this.getTreeNode(templateInfo, "1-4");
                    treeNodes.add(treeNode);
                }
            }

            return treeNodes;
        } catch (Exception var9) {
            this.log.error("我的标签库查询异常", var9);
            throw new CIServiceException("我的标签库查询异常");
        }
    }

    public List<TreeNode> queryContrastSysRecommend(String mainLabelId, String queryLabelName) {
        ArrayList treeNodes = new ArrayList();
        TreeNode sysRecommendNode = new TreeNode();
        sysRecommendNode.setId("1-5");
        sysRecommendNode.setpId("0");
        sysRecommendNode.setName("系统推荐标签");
        sysRecommendNode.setIsParent(Boolean.valueOf(true));
        sysRecommendNode.setOpen(Boolean.valueOf(false));
        sysRecommendNode.setTip("标签对比系统推荐标签");

        try {
            List e = null;
            if(StringUtil.isNotEmpty(queryLabelName)) {
                e = this.ciUserLabelContrastJDao.getLabelContrastRecommend(1, 5, mainLabelId, queryLabelName);
            } else {
                e = this.ciUserLabelContrastJDao.getLabelContrastRecommend(1, 5, mainLabelId);
            }

            if(null != e && e.size() > 0) {
                treeNodes.add(sysRecommendNode);
                boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
                boolean hasAuthority = false;
                String userId = null;
                if(needAuthority) {
                    userId = PrivilegeServiceUtil.getUserId();
                    hasAuthority = needAuthority && !PrivilegeServiceUtil.isAdminUser(userId);
                }

                Iterator i$ = e.iterator();

                while(i$.hasNext()) {
                    LabelShortInfo label = (LabelShortInfo)i$.next();
                    TreeNode tempNode = this.getTreeNode(label, "1-5", hasAuthority, userId);
                    treeNodes.add(tempNode);
                }
            }

            return treeNodes;
        } catch (Exception var12) {
            this.log.error("标签对比系统推荐标签查询异常", var12);
            throw new CIServiceException("标签对比系统推荐标签查询异常");
        }
    }

    public List<TreeNode> queryRelSysRecommend(String mainLabelId, String queryLabelName) {
        ArrayList treeNodes = new ArrayList();
        TreeNode sysRecommendNode = new TreeNode();
        sysRecommendNode.setId("1-6");
        sysRecommendNode.setpId("0");
        sysRecommendNode.setName("系统推荐标签");
        sysRecommendNode.setIsParent(Boolean.valueOf(true));
        sysRecommendNode.setOpen(Boolean.valueOf(false));
        sysRecommendNode.setTip("标签关联系统推荐标签");

        try {
            List e = null;
            if(StringUtil.isNotEmpty(queryLabelName)) {
                e = this.ciUserLabelRelJDao.getLabelRelRecommend(1, 5, new Integer(mainLabelId), queryLabelName);
            } else {
                e = this.ciUserLabelRelJDao.getLabelRelRecommend(1, 5, new Integer(mainLabelId));
            }

            if(null != e && e.size() > 0) {
                treeNodes.add(sysRecommendNode);
                boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
                boolean hasAuthority = false;
                String userId = null;
                if(needAuthority) {
                    userId = PrivilegeServiceUtil.getUserId();
                    hasAuthority = needAuthority && !PrivilegeServiceUtil.isAdminUser(userId);
                }

                Iterator i$ = e.iterator();

                while(i$.hasNext()) {
                    LabelShortInfo label = (LabelShortInfo)i$.next();
                    TreeNode tempNode = this.getTreeNode(label, "1-6", hasAuthority, userId);
                    treeNodes.add(tempNode);
                }
            }

            return treeNodes;
        } catch (Exception var12) {
            this.log.error("标签分析系统推荐标签查询异常", var12);
            throw new CIServiceException("标签分析系统推荐标签查询异常");
        }
    }

    public TreeNode getTreeNode(CiTemplateInfo template, String pId) {
        TreeNode treeNode = new TreeNode();
        treeNode.setId(template.getTemplateId());
        treeNode.setIsParent(Boolean.valueOf(false));
        treeNode.setName(template.getTemplateName());
        treeNode.setOpen(Boolean.valueOf(false));
        treeNode.setpId(pId);
        treeNode.setTip(template.getTemplateName());
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest)ctx.get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
        String contextPath = request.getContextPath();
        String tempIconPath = contextPath + Configure.getInstance().getProperty("TEMPLATE_FOR_TREE");
        treeNode.setIcon(tempIconPath);
        List ruleList = this.ciLabelRuleHDao.selectCiLabelRuleList(template.getTemplateId(), Integer.valueOf(2));
        if(null != ruleList) {
            Iterator i$ = ruleList.iterator();

            while(i$.hasNext()) {
                CiLabelRule rule = (CiLabelRule)i$.next();
                int elementType = rule.getElementType().intValue();
                if(elementType == 2) {
                    String labelId = rule.getCalcuElement();
                    CiLabelInfo ciLabelInfo = CacheBase.getInstance().getEffectiveLabel(labelId);
                    rule.setUpdateCycle(ciLabelInfo.getUpdateCycle());
                    if(ciLabelInfo.getUpdateCycle().intValue() == 2) {
                        Date effectTime = ciLabelInfo.getEffecTime();
                        String effectTimeStr = DateUtil.date2String(effectTime, "yyyyMM");
                        String realEffectTime = DateUtil.getOffsetDateByDate(effectTimeStr, -1, 0);
                        rule.setEffectDate(realEffectTime);
                    }

                    rule.setLabelName(ciLabelInfo.getLabelName());
                    rule.setCustomOrLabelName(ciLabelInfo.getLabelName());
                    rule.setLabelTypeId(ciLabelInfo.getLabelTypeId());
                }
            }
        }

        if(null != ruleList) {
            treeNode.setParam(ruleList);
        }

        return treeNode;
    }

    private TreeNode getTreeNode(LabelShortInfo label, String pId, boolean hasAuthority, String userId) {
        String labelId = label.getLabelId();
        TreeNode treeNode = null;

        try {
            CacheBase e = CacheBase.getInstance();
            CiLabelInfo ciLabelInfo = null;
            if(hasAuthority) {
                ciLabelInfo = e.getEffectiveLabelByUser(labelId, userId);
            } else {
                ciLabelInfo = e.getEffectiveLabel(labelId);
            }

            CiLabelExtInfo ext = ciLabelInfo.getCiLabelExtInfo();
            treeNode = new TreeNode();
            treeNode.setId(label.getLabelId());
            treeNode.setIsParent(Boolean.valueOf(false));
            treeNode.setName(label.getLabelName());
            treeNode.setOpen(Boolean.valueOf(false));
            treeNode.setpId(pId);
            if(ciLabelInfo.getUpdateCycle().intValue() == 2) {
                treeNode.setTip("月周期");
            } else {
                treeNode.setTip("日周期");
            }

            ActionContext ctx = ActionContext.getContext();
            HttpServletRequest request = (HttpServletRequest)ctx.get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
            String contextPath = request.getContextPath();
            String attrIconPath;
            if(ciLabelInfo.getLabelTypeId().intValue() == 1) {
                attrIconPath = contextPath + Configure.getInstance().getProperty("SIGN_LABEL_FOR_TREE");
                treeNode.setIcon(attrIconPath);
            } else if(ciLabelInfo.getLabelTypeId().intValue() == 2) {
                attrIconPath = contextPath + Configure.getInstance().getProperty("SCORE_LABEL_FOR_TREE");
                treeNode.setIcon(attrIconPath);
            } else if(ciLabelInfo.getLabelTypeId().intValue() == 3) {
                attrIconPath = contextPath + Configure.getInstance().getProperty("ATTR_LABEL_FOR_TREE");
                treeNode.setIcon(attrIconPath);
            }

            if(ext != null && ext.getLabelId() != null) {
                treeNode.setParam(this.newLabelTreeModel(ciLabelInfo, ext));
            }
        } catch (Exception var14) {
            this.log.error("构建标签树错误", var14);
            var14.printStackTrace();
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
        if(ext.getIsStatUserNum() != null && ext.getIsStatUserNum().intValue() == 1) {
            model.setIsCanDrag(Integer.valueOf(1));
        } else {
            model.setIsCanDrag(Integer.valueOf(0));
        }

        Date d = ciLabelInfo.getEffecTime();
        String effectDate = (new SimpleDateFormat("yyyyMM")).format(d);
        model.setEffectDate(DateUtil.getOffsetDateByDate(effectDate, -1, 0));
        return model;
    }
}
