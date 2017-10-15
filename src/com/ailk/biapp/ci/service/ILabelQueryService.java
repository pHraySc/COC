package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiTemplateInfo;
import com.ailk.biapp.ci.entity.CiUserAttentionLabelId;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.LabelDetailInfo;
import com.ailk.biapp.ci.model.LabelShortInfo;
import com.ailk.biapp.ci.model.TreeNode;
import java.util.List;

public interface ILabelQueryService {
    List<LabelShortInfo> queryUserLabelByUserId(String var1) throws CIServiceException;

    List<LabelShortInfo> queryLabelByDeptId(String var1) throws CIServiceException;

    List<LabelShortInfo> queryUserAttentionLabel(String var1) throws CIServiceException;

    List<CiUserAttentionLabelId> queryAttentionRecordByLabelId(String var1) throws CIServiceException;

    LabelDetailInfo queryLabelDetailInfo(String var1) throws CIServiceException;

    List<TreeNode> queryMyLabelTree(String var1, String var2);

    List<TreeNode> queryMyTemplateTree(String var1, String var2);

    List<TreeNode> queryContrastSysRecommend(String var1, String var2);

    List<TreeNode> queryRelSysRecommend(String var1, String var2);

    TreeNode getTreeNode(CiTemplateInfo var1, String var2);
}
