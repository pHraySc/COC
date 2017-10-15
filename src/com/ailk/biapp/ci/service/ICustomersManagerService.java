package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiCustomCampsegRel;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomGroupPushCycle;
import com.ailk.biapp.ci.entity.CiCustomListExeInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiCustomModifyHistory;
import com.ailk.biapp.ci.entity.CiCustomPushReq;
import com.ailk.biapp.ci.entity.CiCustomSceneRel;
import com.ailk.biapp.ci.entity.CiCustomSourceRel;
import com.ailk.biapp.ci.entity.CiExploreLogRecord;
import com.ailk.biapp.ci.entity.CiExploreSqlAll;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiListExeSqlAll;
import com.ailk.biapp.ci.entity.CiSysInfo;
import com.ailk.biapp.ci.entity.CiTemplateInfo;
import com.ailk.biapp.ci.entity.VCiLabelCustomList;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.Column;
import com.ailk.biapp.ci.model.CrmTypeModel;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.model.ReturnMsgModel;
import com.ailk.biapp.ci.model.TreeNode;
import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ICustomersManagerService {
    int queryCustomersAutoMacthFlag(String var1) throws CIServiceException;

    CiCustomListInfo queryCustomListInfo(CiCustomGroupInfo var1) throws CIServiceException;

    int queryCustomersCount(CiCustomGroupInfo var1) throws CIServiceException;

    List<CiCustomGroupInfo> queryCustomersList(Pager var1, String var2, CiCustomGroupInfo var3);

    List<CiCustomGroupInfo> queryCustomersListByCustomGroupInfo(CiCustomGroupInfo var1, String var2, int var3);

    void deleteCiCustomGroupInfo(CiCustomGroupInfo var1) throws CIServiceException;

    CiCustomGroupInfo queryCiCustomGroupInfo(String var1);

    CiCustomGroupInfo queryCiCustomGroupInfoForCache(String var1);

    List<CiCustomListInfo> queryCiCustomListInfoAll();

    int queryCustomerListCount(CiCustomListInfo var1) throws CIServiceException;

    List<CiCustomListInfo> queryCiCustomListInfoByCGroupId(String var1) throws CIServiceException;

    List<CiCustomListInfo> querySuccessCiCustomListInfoByCGroupId(String var1) throws CIServiceException;

    List<CiCustomListInfo> queryCiCustomListInfoByCustomListInfo(CiCustomListInfo var1) throws CIServiceException;

    CiCustomListInfo queryCiCustomListInfoByCGroupIdAndDataDate(String var1, String var2) throws CIServiceException;

    String getTabelExistSqlStr(String var1) throws CIServiceException;

    List<CiLabelRule> getNewCiLabelRuleList(List<CiLabelRule> var1) throws CIServiceException;

    int queryForExist(String labelIds) throws Exception;

    int queryForPowerMatch(String userId, String labelIds);

    String getWithColumnSqlStr(List<CiLabelRule> var1, String var2, String var3, String var4, List<CiGroupAttrRel> var5, Integer var6, Integer var7) throws CIServiceException;

    String getCountSqlStr(List<CiLabelRule> var1, String var2, String var3, String var4) throws CIServiceException;

    String validateLabelDataDate(CiCustomGroupInfo var1, List<CiLabelRule> var2, List<CiGroupAttrRel> var3, String var4, String var5);

    String validateLabelDataDate(CiCustomGroupInfo var1, List<CiGroupAttrRel> var2, String var3, String var4);

    String getValidateSqlStr(List<CiLabelRule> var1, String var2, String var3, String var4) throws CIServiceException;

    String getLastDateStr(List<CiLabelRule> var1) throws CIServiceException;

    int queryCount(String var1) throws CIServiceException;

    boolean queryValidateSql(String var1) throws CIServiceException;

    boolean addCiCustomGroupInfo(CiCustomGroupInfo var1, List<CiLabelRule> var2, List<CiCustomSourceRel> var3, CiTemplateInfo var4, String var5, boolean var6, List<CiGroupAttrRel> var7) throws CIServiceException;

    boolean modifyCiCustomGroupInfo(CiCustomGroupInfo var1, List<CiLabelRule> var2, CiTemplateInfo var3, String var4, boolean var5, List<CiGroupAttrRel> var6) throws CIServiceException;

    boolean modifyCiCustomGroupInfoSimple(CiCustomGroupInfo var1) throws CIServiceException;

    int queryCiCustomGroupCalcCount(String var1, Map<String, String> var2, List<String> var3, CiCustomGroupInfo var4) throws CIServiceException;

    CiCustomListInfo queryCiCustomListInfoById(String var1) throws CIServiceException;

    void saveCiCustomSourceRel(List<CiCustomSourceRel> var1) throws CIServiceException;

    int queryCustomersListInfoCount(CiCustomListInfo var1) throws CIServiceException;

    List<CiCustomListInfo> queryCustomersListInfo(int var1, int var2, CiCustomListInfo var3) throws CIServiceException;

    List<CiLabelRule> queryCiLabelRuleList(String var1, Integer var2) throws CIServiceException;

    List<CiCustomGroupInfo> queryCiCustomGroupInfoListByName(String var1, String var2, String var3) throws CIServiceException;

    String getIntersectSqlOfCustomersAndLabel(List<CiLabelRule> var1, String var2, String var3, CiCustomListInfo var4, String var5) throws CIServiceException;

    List<CiCustomGroupInfo> indexQueryCustomersName(CiCustomGroupInfo var1, Integer var2) throws CIServiceException;

    Map<String, Object> isNameExist(CiCustomGroupInfo var1, String var2) throws CIServiceException;

    CiCustomGroupInfo reGenerate(CiCustomGroupInfo var1, String var2, String var3);

    void generateNewPeriodData(CiCustomGroupInfo var1, String var2);

    List<CiCustomGroupInfo> findCiCustomGroupInfo4CycleCreate(int var1, Date var2, String var3);

    void generateNewTimingData(CiCustomGroupInfo var1);

    List<CiCustomGroupInfo> findCiCustomGroupInfo4TimingCreate(Date var1);

    void updateCiCustomExeTime(String var1);

    List<TreeNode> queryUserCustomerGroupTree(String var1);

    List<TreeNode> queryCustomGroupTree(String var1) throws CIServiceException;

    void saveCiCustomListInfo(CiCustomListInfo var1);

    void syncUpdateCiCustomListInfo(CiCustomListInfo var1);

    void genCusomerList(CiCustomGroupInfo var1);

    void pushCustomers(List<CiCustomPushReq> var1, String var2);

    void pushCustomersAfterSave(CiCustomPushReq var1, String var2, CiCustomGroupInfo var3);

    boolean pushSaCustomers(CiCustomPushReq var1, String var2);

    List<CiCustomPushReq> queryCiCustomPushReq(CiCustomPushReq var1);

    CiCustomPushReq queryCiCustomPushReqById(String var1) throws CIServiceException;

    void saveCiCustomPushReq(CiCustomPushReq var1);

    List<CiSysInfo> queryCiSysInfo(int var1);

    void saveCiCustomGroupInfo(CiCustomGroupInfo var1);

    void syncUpdateCiCustomGroupInfo(CiCustomGroupInfo var1);

    void saveCiCustomCampsegRel(CiCustomCampsegRel var1);

    void executeInBackDataBase(String var1);

    void executeInFrontDataBase(String var1);

    int addInBackDataBase(String var1) throws Exception;

    int addInBackDataBase(String var1, String var2) throws Exception;

    int queryCustomersTotalCount(CiCustomListInfo var1);

    void saveCiCustomListExeInfo(CiCustomListExeInfo var1);

    List<CiCustomSourceRel> queryCiCustomSourceRelByCustomGroupId(String var1);

    int queryCountInBackDB(String var1);

    Map<String, Object> query4Map(String var1);

    void resetProductAutoMacthFlag4CiCustomGroupInfo();

    String getSqlCreateAsTable(String var1, String var2, String var3);

    Collection<CrmTypeModel> queryAllLabelsOfUser(String var1);

    List<CiSysInfo> queryCiSysInfo(CiCustomGroupInfo var1);

    List<CiCustomGroupPushCycle> queryPushCycleByGroupId(String var1);

    List<CiCustomGroupPushCycle> queryAllPushCycleByGroupId(String var1);

    void saveCiCustomGroupPushCycle(CiCustomGroupPushCycle var1);

    void deleteCiCustomGroupPushCycleByCustomGroupId(String var1);

    boolean importCustomListFile(File var1, String var2, String var3, CiCustomGroupInfo var4, List<Column> var5, Integer var6) throws Exception;

    List<Map<String, Object>> queryCustomerPhoneNumList(int var1, int var2, CiCustomGroupInfo var3);

    Map<String, Object> isShareNameExist(CiCustomGroupInfo var1, String var2, String var3) throws CIServiceException;

    boolean modifyCiCustomGroupInfo(CiCustomGroupInfo var1, String var2) throws CIServiceException;

    List<CiLabelInfo> queryCiLabelInfoList4SaveCustom();

    List<CiLabelInfo> queryCiLabelInfoList4SaveCustom(String var1, String var2, Integer var3);

    List<CiGroupAttrRel> queryCustomGroupAttrRelList(List<CiLabelRule> var1);

    List<CiGroupAttrRel> queryCustomGroupAttrRelList(List<CiLabelRule> var1, String var2);

    ReturnMsgModel deleteCustomGroupById(String var1, String var2);

    ReturnMsgModel modifyPushCustomerEndTime(String var1, String var2, Date var3, String var4);

    String getFromSqlStrForVerticalLabel(CiLabelRule var1, String var2, String var3, boolean var4, String var5, Integer var6, Integer var7, boolean var8) throws Exception;

    void translateListTableCol(List<String> var1);

    String getSelectSqlByCustomersRels(List<CiGroupAttrRel> var1, List<CiLabelRule> var2, String var3, String var4, String var5, String var6, Integer var7, Integer var8, boolean var9);

    CiCustomGroupInfo queryCiCustomGroupInfoByListInfoId(String var1);

    void userAttentionCustom(String var1, String var2) throws CIServiceException;

    void deleteUserAttentionCustom(String var1, String var2) throws CIServiceException;

    Long getUserAttentionCustomserCount(CiCustomGroupInfo var1) throws CIServiceException;

    List<CiCustomGroupInfo> queryUserAttentionCustomersList(Pager var1, String var2, CiCustomGroupInfo var3) throws CIServiceException;

    void deleteCiCustomSceneRel(CiCustomSceneRel var1) throws CIServiceException;

    List<CiCustomGroupInfo> queryCustomersListTask(Pager var1, String var2, CiCustomGroupInfo var3);

    List<CiCustomGroupInfo> getSysRecommendCustomInfos(CiCustomGroupInfo var1);

    void dropTable(String var1);

    List<CiGroupAttrRel> queryAttrRelByCustomerID(String var1, String var2);

    boolean checkIfDailyCustomGroupCanCreate(String var1);

    void delCustomListTableAndListInfoBefore(CiCustomGroupInfo var1, String var2);

    Map<String, Object> isUsingCustom(String var1) throws CIServiceException;

    List<CiCustomGroupInfo> queryDelCustomGroup(String var1);

    int getDailyCustomersTotolCount(CiCustomGroupInfo var1);

    List<CiCustomGroupInfo> queryCiCustomGroupInfoListByShareName(String var1) throws CIServiceException;

    CiCustomGroupInfo queryCiCustomGroupInfoAndAttention(CiCustomGroupInfo var1);

    List<Map<String, Object>> queryCustomerLists(int var1, int var2, CiCustomGroupInfo var3);

    List<Map<String, Object>> queryListExeInfosByTableName(Pager var1, String var2);

    int queryListExeInfoNumByTableName(String var1) throws CIServiceException;

    List<CiLabelInfo> queryCiLabelInfoListBySys(String var1);

    List<CiCustomGroupInfo> queryCiCustomGroupInfoByIsFirstFailed(Integer var1) throws CIServiceException;

    void modifyAllCustomGroupIsFirstFailed(Integer var1);

    void modifyCustomGroupIsFirstFailed(String var1, Integer var2);

    List<VCiLabelCustomList> queryLastLabelCustomAttentionList(Pager var1);

    List<CiLabelInfo> queryCiLabelInfoTreeList(String var1);

    String getLastListTableName(String var1, String var2);

    int queryDistinctListNumByTableName(String var1);

    List<CiLabelInfo> queryLabelInfoByNameOrLabelIdsOrParentId(String var1, String var2, String var3, Integer var4);

    void batchUpdateValueList(List<String> var1, String var2, String var3) throws CIServiceException;

    void saveCiListExeSqlAllList(List<CiListExeSqlAll> var1);

    int selectEndedCustomersListInfoCount(CiCustomListInfo var1);

    boolean isEarlierThanCustomExeTime(CiCustomGroupInfo var1);

    boolean isConstraintCreateCustom(List<CiLabelRule> var1);

    String queryCustomerOffsetStr(CiCustomGroupInfo var1);

    List<CiGroupAttrRel> queryCustomGroupAttrRelList(List<CiLabelRule> var1, String var2, String var3, String var4);

    int addExactValueToTable(String var1, String var2);

    String createValueTableName();

    String queryAllValueStrByValueTable(String var1);

    Integer getCustomGroupInfoDataCycle(Integer var1, Integer var2, String var3);

    String queryListMaxNumSql(String var1, int var2, String var3);

    void saveCiExploreLogRecord(CiExploreLogRecord var1);

    void saveCiExploreSqlAllList(List<CiExploreSqlAll> var1);

    int queryCustomListCount(CiCustomListInfo var1);

    List<CiCustomListInfo> queryCustomList(int var1, int var2, CiCustomListInfo var3) throws Exception;

    CiCustomModifyHistory queryNewestCustomModifyHistory(String var1);

    void updateCustomListInfo(CiCustomListInfo var1);

    void deleteCustomSceneRels(String var1, Integer var2);

    StringBuffer shopCartRule(List<CiLabelRule> var1, StringBuffer var2);

    String ruleAttrVal(CiLabelRule var1);

    String queryOtherSysCustomGroupId(String var1, String var2);
}
