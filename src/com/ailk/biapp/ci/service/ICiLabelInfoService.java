package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiApproveHistory;
import com.ailk.biapp.ci.entity.CiApproveUserInfo;
import com.ailk.biapp.ci.entity.CiEnumCategoryInfo;
import com.ailk.biapp.ci.entity.CiLabelExtInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiLabelSceneRel;
import com.ailk.biapp.ci.entity.CiLabelVerticalColumnRel;
import com.ailk.biapp.ci.entity.CiMdaSysTable;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.entity.CiUserAttentionLabel;
import com.ailk.biapp.ci.entity.DimApproveStatus;
import com.ailk.biapp.ci.entity.DimCocIndexInfo;
import com.ailk.biapp.ci.entity.DimCocIndexTableInfo;
import com.ailk.biapp.ci.entity.DimCocLabelCountRules;
import com.ailk.biapp.ci.entity.DimLabelDataStatus;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.CiConfigLabeInfo;
import com.ailk.biapp.ci.model.CiLabelInfoTree;
import com.ailk.biapp.ci.model.LabelDetailInfo;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.model.TreeNode;
import com.asiainfo.biframe.dimtable.model.DimTableDefine;
import java.io.File;
import java.util.List;
import java.util.Map;

public interface ICiLabelInfoService {
    List<CiLabelInfoTree> queryAllLabelCategory() throws CIServiceException;

    CiLabelInfoTree getCategoryTree(CiLabelInfo var1, List<CiLabelInfo> var2) throws CIServiceException;

    List<CiLabelInfo> queryThirdLabelList(String var1, int var2) throws CIServiceException;

    List<CiLabelInfo> queryThirdLabelList(String var1, List<CiLabelInfo> var2) throws CIServiceException;

    List<CiLabelInfoTree> queryMapPageObj(int var1, int var2, String var3, int var4) throws CIServiceException;

    int queryTotalPageCount(CiLabelInfo var1) throws CIServiceException;

    List<CiLabelInfo> queryPageList(int var1, int var2, CiLabelInfo var3) throws CIServiceException;

    void add(CiLabelInfo var1) throws CIServiceException;

    CiLabelInfo queryCiLabelInfoById(Integer var1) throws CIServiceException;

    CiLabelInfo queryCiLabelInfoByIdFullLoad(Integer var1) throws CIServiceException;

    int querySecondLabelCountByFirstLabel(Integer var1) throws CIServiceException;

    List<DimLabelDataStatus> queryDimLabelDataStatus() throws CIServiceException;

    List<DimApproveStatus> queryDimApproveStatus() throws CIServiceException;

    Boolean hasSameLabel(CiLabelInfo var1);

    List<CiLabelInfo> querySimilarityLabelNameList(CiLabelInfo var1);

    void modify(CiLabelInfo var1) throws CIServiceException;

    void delete(List<Integer> var1) throws CIServiceException;

    void stopUseAndOffLineLabel(CiLabelInfo var1, Integer var2) throws CIServiceException;

    void onLineLabel(CiLabelInfo var1) throws CIServiceException;

    List<CiApproveUserInfo> queryApproveUserInfoList(String var1) throws CIServiceException;

    List<CiApproveUserInfo> queryInfoRoleApproveUserInfoList(String var1) throws CIServiceException;

    void userAttentionLabelRecord(Integer var1, String var2) throws CIServiceException;

    void deleteUserAttentionLabelRecord(Integer var1, String var2) throws CIServiceException;

    CiUserAttentionLabel queryUserAttentionLabelRecord(Integer var1, String var2) throws CIServiceException;

    List<CiLabelInfo> queryAllEffectiveLabel() throws CIServiceException;

    List<TreeNode> queryLabelTree() throws CIServiceException;

    Integer queryIdByNameForImport(String var1) throws CIServiceException;

    List<CiLabelInfo> queryLabelName(String var1, Integer var2) throws CIServiceException;

    List<CiLabelInfo> queryChildrenById(Integer var1) throws CIServiceException;

    List<CiLabelInfo> queryChildrenById(Integer var1, List<CiLabelInfo> var2) throws CIServiceException;

    List<CiLabelInfoTree> queryMapPageObject(int var1, int var2, int var3) throws CIServiceException;

    CiLabelInfoTree getLabelTree(CiLabelInfo var1, List<CiLabelInfo> var2) throws CIServiceException;

    List<LabelDetailInfo> querySysRecommendLabel(int var1, int var2, String var3) throws CIServiceException;

    Long querySysRecommendLabelNum() throws CIServiceException;

    long queryUserUsedLabelNum(String var1, String var2) throws CIServiceException;

    List<LabelDetailInfo> queryUserUsedLabel(int var1, int var2, String var3, String var4, String var5) throws CIServiceException;

    List<LabelDetailInfo> queryUserAttentionLabel(int var1, int var2, String var3, String var4, String var5) throws CIServiceException;

    long queryUserAttentionLabelNum(String var1, String var2) throws CIServiceException;

    List<LabelDetailInfo> queryNewPublishLabel(int var1, int var2, String var3, String var4) throws CIServiceException;

    long queryNewPublishLabelNum(String var1) throws CIServiceException;

    List<TreeNode> queryUserLabelTree(String var1);

    List<CiApproveHistory> queryHistoryByLabelId(Integer var1);

    String getApproveStatusByLabelId(Integer var1);

    List<TreeNode> queryLabelTreeByName(String var1);

    List<TreeNode> queryUserLabelTreeByName(String var1, String var2);

    List<LabelDetailInfo> queryEffectiveLabel(Pager var1, String var2, String var3, Integer var4, String var5, CiLabelInfo var6) throws CIServiceException;

    long queryEffectiveLabelNum(String var1, Integer var2, String var3, CiLabelInfo var4) throws CIServiceException;

    boolean judgeCurrentUserIsApprover(String var1) throws CIServiceException;

    List<TreeNode> queryDimTree(String var1, String var2) throws CIServiceException;

    Long getCustomNum(String var1, Integer var2, Integer var3, Integer var4, Long var5, String var6) throws CIServiceException;

    void addCiLabelHistoryInfo(CiLabelInfo var1, Integer var2) throws CIServiceException;

    List<Map<String, Object>> queryLabelDimValue(Integer var1, Pager var2, String var3, String var4, String var5) throws Exception;

    List<Map<String, Object>> queryLabelAllDimValue(Integer var1) throws Exception;

    List<Map<String, Object>> queryAllDimDataByDimTableDefine(DimTableDefine var1) throws Exception;

    List<Map<String, Object>> queryLabelDimValueByColumnId(Integer var1, Pager var2, String var3, String var4, String var5) throws Exception;

    long queryLabelDimValueCount(String var1, Integer var2, String var3, String var4) throws Exception;

    List<CiLabelVerticalColumnRel> queryCiLabelVerticalColumnRelByLabelId(Integer var1);

    long queryLabelDimValueCountByColumnId(String var1, Integer var2, String var3, String var4) throws Exception;

    List<LabelDetailInfo> getSysRecommendLabelInfos(CiLabelInfo var1);

    List<LabelDetailInfo> querySysRecommendLabelTask();

    List<Map<String, Object>> queryVerticalValueByImport(String var1, Integer var2, File var3, String var4) throws Exception;

    List<CiLabelInfo> queryChildrenByIdAndName(Integer var1, String var2) throws CIServiceException;

    String queryLikeLabelValueByImport(File var1, String var2) throws Exception;

    List<CiLabelInfo> queryVertAndEnumLabelListFromCache() throws CIServiceException;

    List<CiLabelInfo> queryEnumLabelOrColumnListByLabelIdAndType(String var1);

    List<CiEnumCategoryInfo> queryEnumCategoryInfoList(String var1, Integer var2);

    int queryVertAndEnumLabelCount(CiLabelInfo var1);

    List<CiLabelInfo> queryVertAndEnumLabelList(Pager var1, CiLabelInfo var2);

    int queryEnumCategoryInfoTotalCountByColumnId(String var1, String var2, Integer var3);

    List<CiEnumCategoryInfo> queryEnumCategoryInfoPagerListByColumnId(Pager var1, String var2, String var3, Integer var4);

    Map<String, Object> isCategoryInfoNameExist(CiEnumCategoryInfo var1);

    void addEnumCategoryInfo(CiEnumCategoryInfo var1);

    void deleteCategoryInfo(List<String> var1) throws CIServiceException;

    Map<String, Object> importEnumListFile(String var1, Integer var2, String var3, File var4, String var5);

    DimCocLabelCountRules queryLabelCountRules(String var1) throws CIServiceException;

    List<TreeNode> queryLabelTree(Integer var1, Integer var2) throws CIServiceException;

    void saveConfigLabelInfo(CiConfigLabeInfo var1, List<CiMdaSysTableColumn> var2) throws CIServiceException;

    CiMdaSysTable queryMdaSysTable(Integer var1) throws CIServiceException;

    void addMdaColumn(CiMdaSysTableColumn var1) throws CIServiceException;

    void addLabelCountRules(DimCocLabelCountRules var1) throws CIServiceException;

    String queryMaxCountRules() throws CIServiceException;

    CiLabelExtInfo queryCiLabelExtInfoById(Integer var1);

    void addLabelExtInfo(CiLabelExtInfo var1) throws CIServiceException;

    void adminAdd(CiLabelInfo var1, String var2, Integer var3) throws CIServiceException;

    void addLabelInfoByConfig(CiLabelInfo var1);

    void editLabelInfoByConfig(CiLabelInfo var1);

    DimCocIndexInfo queryDimCocIndexInfoById(String var1) throws CIServiceException;

    DimCocIndexTableInfo queryDimCocIndexTableInfoById(String var1) throws CIServiceException;

    void publishLabel(CiConfigLabeInfo var1, String var2) throws CIServiceException;

    void saveLabelClassify(CiLabelInfo var1) throws CIServiceException;

    Boolean validEidtLabel(CiLabelInfo var1);

    void onLineClassesLabel(CiLabelInfo var1) throws CIServiceException;

    int queryTotalLabelInfoCount(CiLabelInfo var1) throws CIServiceException;

    List<CiLabelInfo> queryPageLabelInfoList(int var1, int var2, CiLabelInfo var3) throws CIServiceException;

    List<CiLabelInfo> queryToEffectChildrenLabelsById(Integer var1) throws CIServiceException;

    List<TreeNode> queryAllLabelTree(Integer var1, Integer var2) throws CIServiceException;

    Map<String, Object> saveImportLabel(File var1) throws CIServiceException;

    void saveImportVertConfigLabelInfo(CiConfigLabeInfo var1, List<CiMdaSysTableColumn> var2) throws CIServiceException;

    void modifyLabelLeaf(String var1, Integer var2) throws CIServiceException;

    Map<String, Object> saveImportLabelClass(File var1);

    List<LabelDetailInfo> queryEffectiveLabelByLabelId(Integer var1) throws CIServiceException;

    List<CiLabelSceneRel> queryLabelSceneByLabelId(String var1);

    CiLabelInfo queryCiLabelInfoByLabelId(Integer var1) throws CIServiceException;

    void saveLabelLevel(Integer var1, CiLabelInfo var2);

    void batchUpdateLabelIdLevel();

    List<CiLabelInfo> queryCiLabelInfoList(Integer var1, Integer var2);

    LabelDetailInfo getSysRecommendLabelInfo(Integer var1);

    List<TreeNode> queryAllEffectLabelTree(Integer var1, Integer var2) throws CIServiceException;
}
