package com.ailk.biapp.ci.entity;

import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import net.sf.json.JSONObject;

public class CiCustomGroupInfo
  implements Serializable, Cloneable
{
  private static final long serialVersionUID = 1L;
  private static final int RULE_MAX_LENGTH = 220;
  private String customGroupId;
  private String customGroupName;
  private String customGroupDesc;
  private String parentCustomId;
  private Integer updateCycle;
  private Integer createTypeId;
  private String createUserId;
  private Date createTime;
  private String prodOptRuleShow;
  private String labelOptRuleShow;
  private String customOptRuleShow;
  private String kpiDiffRule;
  private Integer status;
  private Integer dataStatus;
  private Date startDate;
  private Date endDate;
  private Long customNum;
  private Date dataTime;
  private String dataDate;
  private String templateId;
  private Integer productAutoMacthFlag;
  private Integer isPrivate;
  private String createCityId;
  private Date newModifyTime;
  private Integer isLabelOffline;
  private String thumbnailData;
  private Integer isSysRecom;
  private Integer isContainLocalList;
  private String monthLabelDate;
  private String dayLabelDate;
  private String tacticsId;
  private Integer isFirstFailed;
  private Long duplicateNum;
  private Integer isHasList;
  private String listCreateTime;
  private Integer customListCreateFailed;
  private String serverId;
  private String offsetDate;
  private Integer listMaxNum;
  private String sysId;
  private String ruleExpress;
  private String sceneId;
  private String customSceneNames;
  private String createTimeView;
  private String labelName;
  private String templateName;
  private String dateType;
  private String createType;
  private String createUserName;
  private String updateCycleStr;
  private String dataStatusStr;
  private String year;
  private String month;
  private String day;
  private String listTableName;
  private Integer sysMatchStatus;
  private boolean isAlarm;
  private Date modifyTime;
  private String customMatch;
  private String productsStr;
  private String productDate;
  private String productsIdsStr;
  private boolean precisionMarketing;
  private boolean saveTemplate;
  private String returnPage;
  private List<CiCustomListInfo> successList;
  private int failedListCount;
  private String customerAnalysisMenu;
  private Integer oldIsHasList;
  private String isSysRecommendTime;
  private String simpleRule;
  private boolean isOverlength;
  private int isPush;
  private String isAttention = "false";
  private String startDateStr;
  private String endDateStr;
  private Date modifyStartDate;
  private Date modifyEndDate;
  private Long minCustomNum;
  private Long maxCustomNum;
  private String isHotCustom = "false";
  private String tabelName;
  private String whereSql;
  private List<CiCustomSceneRel> sceneList;
  private int useTimes;
  private String cityId;
  private String createCityName;
  private String isMyCustom = "false";
  private String userId;
  private Integer lastDataStatus;
  private String dayDataDate;
  private String dataDateStr;
  private Double avgScore;

  public Integer getLastDataStatus()
  {
    return this.lastDataStatus;
  }

  public void setLastDataStatus(Integer lastDataStatus) {
    this.lastDataStatus = lastDataStatus;
  }

  public int getIsPush() {
    return this.isPush;
  }

  public void setIsPush(int isPush) {
    this.isPush = isPush;
  }

  public CiCustomGroupInfo()
  {
  }

  public CiCustomGroupInfo(String parentCustomId)
  {
    this.parentCustomId = parentCustomId;
  }

  public CiCustomGroupInfo(String customGroupId, String customGroupName, String customGroupDesc, String parentCustomId, Integer updateCycle, Integer createTypeId, String createUserId, Date createTime, String prodOptRuleShow, String labelOptRuleShow, String customOptRuleShow, String kpiDiffRule, Integer status, Integer dataStatus, Date startDate, Date endDate, Long customNum, Date dataTime, String dataDate, String templateId, Integer productAutoMacthFlag, Integer isPrivate, String createCityId, Date newModifyTime, Integer isLabelOffline, Integer isSysRecom, Integer isContainLocalList)
  {
    this.customGroupId = customGroupId;
    this.customGroupName = customGroupName;
    this.customGroupDesc = customGroupDesc;
    this.parentCustomId = parentCustomId;
    this.updateCycle = updateCycle;
    this.createTypeId = createTypeId;
    this.createUserId = createUserId;
    this.createTime = createTime;
    this.prodOptRuleShow = prodOptRuleShow;
    this.labelOptRuleShow = labelOptRuleShow;
    this.customOptRuleShow = customOptRuleShow;
    this.kpiDiffRule = kpiDiffRule;
    this.status = status;
    this.dataStatus = dataStatus;
    this.startDate = startDate;
    this.endDate = endDate;
    this.customNum = customNum;
    this.dataTime = dataTime;
    this.dataDate = dataDate;
    this.templateId = templateId;
    this.productAutoMacthFlag = productAutoMacthFlag;
    this.isPrivate = isPrivate;
    this.createCityId = createCityId;
    this.newModifyTime = newModifyTime;
    this.isLabelOffline = isLabelOffline;
    this.isSysRecom = isSysRecom;
    this.isContainLocalList = isContainLocalList;
  }

  public String getCustomGroupId()
  {
    return this.customGroupId;
  }

  public String getDateType() {
    return this.dateType;
  }

  public void setDateType(String dateType) {
    this.dateType = dateType;
  }

  public void setCustomGroupId(String customGroupId) {
    this.customGroupId = customGroupId;
  }

  public String getCustomGroupName() {
    return this.customGroupName;
  }

  public void setCustomGroupName(String customGroupName) {
    this.customGroupName = customGroupName;
  }

  public String getCustomGroupDesc() {
    return this.customGroupDesc;
  }

  public void setCustomGroupDesc(String customGroupDesc) {
    this.customGroupDesc = customGroupDesc;
  }

  public String getParentCustomId() {
    return this.parentCustomId;
  }

  public void setParentCustomId(String parentCustomId) {
    this.parentCustomId = parentCustomId;
  }

  public Integer getUpdateCycle() {
    return this.updateCycle;
  }

  public void setUpdateCycle(Integer updateCycle) {
    this.updateCycle = updateCycle;
  }

  public Integer getCreateTypeId() {
    return this.createTypeId;
  }

  public void setCreateTypeId(Integer createTypeId) {
    this.createTypeId = createTypeId;
  }

  public String getCreateUserId() {
    return this.createUserId;
  }

  public void setCreateUserId(String createUserId) {
    this.createUserId = createUserId;
  }

  public Date getCreateTime() {
    return this.createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getLabelOptRuleShow() {
    return this.labelOptRuleShow;
  }

  public void setLabelOptRuleShow(String labelOptRuleShow) {
    this.labelOptRuleShow = labelOptRuleShow;
    if ((StringUtil.isNotEmpty(labelOptRuleShow)) && (labelOptRuleShow.length() > 220)) {
      setSimpleRule(labelOptRuleShow.substring(0, 220));
      setIsOverlength(Boolean.TRUE.booleanValue());
    } else {
      setSimpleRule(labelOptRuleShow);
      setIsOverlength(Boolean.FALSE.booleanValue());
    }
  }

  public String getCustomOptRuleShow() {
    return this.customOptRuleShow;
  }

  public void setCustomOptRuleShow(String customOptRuleShow) {
    this.customOptRuleShow = customOptRuleShow;
  }

  public String getKpiDiffRule() {
    return this.kpiDiffRule;
  }

  public void setKpiDiffRule(String kpiDiffRule) {
    this.kpiDiffRule = kpiDiffRule;
  }

  public Integer getStatus() {
    return this.status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getDataStatus() {
    return this.dataStatus;
  }

  public void setDataStatus(Integer dataStatus) {
    this.dataStatus = dataStatus;
  }

  public Date getStartDate() {
    return this.startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return this.endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Long getCustomNum() {
    return this.customNum;
  }

  public void setCustomNum(Long customNum) {
    this.customNum = customNum;
  }

  public Date getDataTime() {
    return this.dataTime;
  }

  public void setDataTime(Date dataTime) {
    this.dataTime = dataTime;
  }

  public String getDataDate() {
    return this.dataDate;
  }

  public void setDataDate(String dataDate) {
    this.dataDate = dataDate;
  }

  public String getTemplateId() {
    return this.templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public String getTemplateName() {
    return this.templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public String getLabelName() {
    return this.labelName;
  }

  public void setLabelName(String labelName) {
    this.labelName = labelName;
  }

  public String getCreateTimeView() {
    return this.createTimeView;
  }

  public void setCreateTimeView(String createTimeView) {
    this.createTimeView = createTimeView;
  }

  public String getProdOptRuleShow() {
    return this.prodOptRuleShow;
  }

  public void setProdOptRuleShow(String prodOptRuleShow) {
    this.prodOptRuleShow = prodOptRuleShow;
  }

  public Integer getProductAutoMacthFlag() {
    return this.productAutoMacthFlag;
  }

  public void setProductAutoMacthFlag(Integer productAutoMacthFlag) {
    this.productAutoMacthFlag = productAutoMacthFlag;
  }

  public String toString()
  {
    return "CiCustomGroupInfo [customGroupId=" + this.customGroupId + ", customGroupName=" + this.customGroupName + ", customGroupDesc=" + this.customGroupDesc + ", parentCustomId=" + this.parentCustomId + ", updateCycle=" + this.updateCycle + ", createTypeId=" + this.createTypeId + ", createUserId=" + this.createUserId + ", createTime=" + this.createTime + ", prodOptRuleShow=" + this.prodOptRuleShow + ", labelOptRuleShow=" + this.labelOptRuleShow + ", customOptRuleShow=" + this.customOptRuleShow + ", kpiDiffRule=" + this.kpiDiffRule + ", status=" + this.status + ", dataStatus=" + this.dataStatus + ", startDate=" + this.startDate + ", endDate=" + this.endDate + ", customNum=" + this.customNum + ", dataTime=" + this.dataTime + ", dataDate=" + this.dataDate + ", templateId=" + this.templateId + ", productAutoMacthFlag=" + this.productAutoMacthFlag + "]";
  }

  public String getJson()
  {
    try
    {
      if (this.successList == null)
        initCiCustomListInfo();

      JSONObject jsonObject = JSONObject.fromObject(this);
      String json = URLEncoder.encode(jsonObject.toString(), "UTF-8").replaceAll("\\+", "%20");
      return json; } catch (Exception e) {
    }
    return null;
  }

  private void initCiCustomListInfo()
  {
    CacheBase cache = CacheBase.getInstance();
    List list = (List)cache.get("CI_CUSTOM_LIST_INFO_MAP", getCustomGroupId(), List.class);

    if (list != null)
      for (Iterator i$ = list.iterator(); i$.hasNext(); ) { CiCustomListInfo listInfo = (CiCustomListInfo)i$.next();
        if (listInfo.getDataStatus().intValue() == 3)
          addSuccessList(listInfo);
        else if (listInfo.getDataStatus().intValue() == 0) {
          this.failedListCount += 1;
        }
        else
          cache.put("CI_CUSTOM_LIST_INFO_MAP", getCustomGroupId(), null);
      }
  }

  public boolean getSaveTemplate()
  {
    return this.saveTemplate;
  }

  public void setSaveTemplate(boolean isSaveTemplate) {
    this.saveTemplate = isSaveTemplate;
  }

  public String getCreateType() {
    return this.createType;
  }

  public void setCreateType(String createType) {
    this.createType = createType;
  }

  public String getUpdateCycleStr() {
    return this.updateCycleStr;
  }

  public void setUpdateCycleStr(String updateCycleStr) {
    this.updateCycleStr = updateCycleStr;
  }

  public String getDataStatusStr() {
    return this.dataStatusStr;
  }

  public void setDataStatusStr(String dataStatusStr) {
    this.dataStatusStr = dataStatusStr;
  }

  public String getYear() {
    return this.year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getMonth() {
    return this.month;
  }

  public void setMonth(String month) {
    this.month = month;
  }

  public String getDay() {
    return this.day;
  }

  public void setDay(String day) {
    this.day = day;
  }

  public String getListTableName() {
    return this.listTableName;
  }

  public void setListTableName(String listTableName) {
    this.listTableName = listTableName;
  }

  public List<CiCustomListInfo> getSuccessList() {
    return this.successList;
  }

  public void setSuccessList(List<CiCustomListInfo> successList) {
    this.successList = successList;
  }

  public void addSuccessList(CiCustomListInfo successList) {
    if (this.successList == null)
      this.successList = new ArrayList();

    this.successList.add(successList);
  }

  public String getProductsStr() {
    return this.productsStr;
  }

  public void setProductsStr(String productsStr) {
    this.productsStr = productsStr;
  }

  public String getCustomMatch() {
    return this.customMatch;
  }

  public void setCustomMatch(String customMatch) {
    this.customMatch = customMatch;
  }

  public String getStartDateStr() {
    return this.startDateStr;
  }

  public void setStartDateStr(String startDateStr) {
    this.startDateStr = startDateStr;
  }

  public String getEndDateStr() {
    return this.endDateStr;
  }

  public void setEndDateStr(String endDateStr) {
    this.endDateStr = endDateStr;
  }

  public int getFailedListCount() {
    if (this.successList == null)
      initCiCustomListInfo();

    return this.failedListCount;
  }

  public void setFailedListCount(int failedListCount) {
    this.failedListCount = failedListCount;
  }

  public boolean isPrecisionMarketing() {
    return this.precisionMarketing;
  }

  public void setPrecisionMarketing(boolean precisionMarketing) {
    this.precisionMarketing = precisionMarketing;
  }

  public Date getModifyTime() {
    return this.modifyTime;
  }

  public void setModifyTime(Date modifyTime) {
    this.modifyTime = modifyTime;
  }

  public String getCreateUserName() {
    return this.createUserName;
  }

  public void setCreateUserName(String createUserName) {
    this.createUserName = createUserName;
  }

  public boolean getIsAlarm() {
    return this.isAlarm;
  }

  public void setAlarm(boolean isAlarm) {
    this.isAlarm = isAlarm;
  }

  public String getProductsIdsStr() {
    return this.productsIdsStr;
  }

  public void setProductsIdsStr(String productsIdsStr) {
    this.productsIdsStr = productsIdsStr;
  }

  public String getReturnPage() {
    return this.returnPage;
  }

  public void setReturnPage(String returnPage) {
    this.returnPage = returnPage;
  }

  public String getProductDate() {
    return this.productDate;
  }

  public void setProductDate(String productDate) {
    this.productDate = productDate;
  }

  public Integer getSysMatchStatus() {
    return this.sysMatchStatus;
  }

  public void setSysMatchStatus(Integer sysMatchStatus) {
    this.sysMatchStatus = sysMatchStatus;
  }

  public String getCustomerAnalysisMenu() {
    return this.customerAnalysisMenu;
  }

  public void setCustomerAnalysisMenu(String customerAnalysisMenu) {
    this.customerAnalysisMenu = customerAnalysisMenu;
  }

  public CiCustomGroupInfo clone() throws CloneNotSupportedException {
    CiCustomGroupInfo ciCustomGroupInfo = (CiCustomGroupInfo)super.clone();
    return ciCustomGroupInfo;
  }

  public Integer getIsPrivate() {
    return this.isPrivate;
  }

  public void setIsPrivate(Integer isPrivate) {
    this.isPrivate = isPrivate;
  }

  public String getCreateCityId() {
    return this.createCityId;
  }

  public void setCreateCityId(String createCityId) {
    this.createCityId = createCityId;
  }

  public Date getNewModifyTime() {
    return this.newModifyTime;
  }

  public void setNewModifyTime(Date newModifyTime) {
    this.newModifyTime = newModifyTime;
  }

  public Integer getIsLabelOffline() {
    return this.isLabelOffline;
  }

  public void setIsLabelOffline(Integer isLabelOffline) {
    this.isLabelOffline = isLabelOffline; }

  public Date getModifyStartDate() {
    return this.modifyStartDate;
  }

  public void setModifyStartDate(Date modifyStartDate) {
    this.modifyStartDate = modifyStartDate;
  }

  public Date getModifyEndDate() {
    return this.modifyEndDate;
  }

  public void setModifyEndDate(Date modifyEndDate) {
    this.modifyEndDate = modifyEndDate;
  }

  public Long getMinCustomNum() {
    return this.minCustomNum;
  }

  public void setMinCustomNum(Long minCustomNum) {
    this.minCustomNum = minCustomNum;
  }

  public Long getMaxCustomNum() {
    return this.maxCustomNum;
  }

  public void setMaxCustomNum(Long maxCustomNum) {
    this.maxCustomNum = maxCustomNum;
  }

  public String getSceneId() {
    return this.sceneId;
  }

  public void setSceneId(String sceneId) {
    this.sceneId = sceneId;
  }

  public String getCustomSceneNames() {
    return this.customSceneNames;
  }

  public void setCustomSceneNames(String customSceneNames) {
    this.customSceneNames = customSceneNames;
  }

  public String getThumbnailData() {
    return this.thumbnailData;
  }

  public void setThumbnailData(String thumbnailData) {
    this.thumbnailData = thumbnailData;
  }

  public String getTabelName() {
    return this.tabelName;
  }

  public void setTabelName(String tabelName) {
    this.tabelName = tabelName;
  }

  public String getWhereSql() {
    return this.whereSql;
  }

  public void setWhereSql(String whereSql) {
    this.whereSql = whereSql;
  }

  public String getIsAttention() {
    return this.isAttention;
  }

  public void setIsAttention(String isAttention) {
    this.isAttention = isAttention;
  }

  public List<CiCustomSceneRel> getSceneList() {
    return this.sceneList;
  }

  public void setSceneList(List<CiCustomSceneRel> sceneList) {
    this.sceneList = sceneList; }

  public Integer getIsSysRecom() {
    return this.isSysRecom;
  }

  public void setIsSysRecom(Integer isSysRecom) {
    this.isSysRecom = isSysRecom;
  }

  public Integer getIsContainLocalList() {
    return this.isContainLocalList;
  }

  public void setIsContainLocalList(Integer isContainLocalList) {
    this.isContainLocalList = isContainLocalList;
  }

  public String getIsHotCustom() {
    return this.isHotCustom;
  }

  public void setIsHotCustom(String isHotCustom) {
    this.isHotCustom = isHotCustom;
  }

  public int getUseTimes() {
    return this.useTimes;
  }

  public void setUseTimes(int useTimes) {
    this.useTimes = useTimes;
  }

  public String getCityId() {
    return this.cityId;
  }

  public void setCityId(String cityId) {
    this.cityId = cityId;
  }

  public String getCreateCityName() {
    return this.createCityName;
  }

  public void setCreateCityName(String createCityName) {
    this.createCityName = createCityName;
  }

  public String getIsMyCustom() {
    return this.isMyCustom;
  }

  public void setIsMyCustom(String isMyCustom) {
    this.isMyCustom = isMyCustom;
  }

  public String getUserId() {
    return this.userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getMonthLabelDate() {
    return this.monthLabelDate;
  }

  public void setMonthLabelDate(String monthLabelDate) {
    this.monthLabelDate = monthLabelDate;
  }

  public String getDayLabelDate() {
    return this.dayLabelDate;
  }

  public void setDayLabelDate(String dayLabelDate) {
    this.dayLabelDate = dayLabelDate;
  }

  public String getTacticsId() {
    return this.tacticsId;
  }

  public void setTacticsId(String tacticsId) {
    this.tacticsId = tacticsId;
  }

  public Integer getIsFirstFailed() {
    return this.isFirstFailed;
  }

  public void setIsFirstFailed(Integer isFirstFailed) {
    this.isFirstFailed = isFirstFailed;
  }

  public Long getDuplicateNum() {
    return this.duplicateNum;
  }

  public void setDuplicateNum(Long duplicateNum) {
    this.duplicateNum = duplicateNum;
  }

  public Integer getIsHasList() {
    return this.isHasList;
  }

  public void setIsHasList(Integer isHasList) {
    this.isHasList = isHasList;
  }

  public String getListCreateTime() {
    return this.listCreateTime;
  }

  public void setListCreateTime(String listCreateTime) {
    this.listCreateTime = listCreateTime;
  }

  public Integer getCustomListCreateFailed() {
    return this.customListCreateFailed;
  }

  public void setCustomListCreateFailed(Integer customListCreateFailed) {
    this.customListCreateFailed = customListCreateFailed;
  }

  public Integer getOldIsHasList() {
    return this.oldIsHasList;
  }

  public void setOldIsHasList(Integer oldIsHasList) {
    this.oldIsHasList = oldIsHasList;
  }

  public void setIsSysRecommendTime(String isSysRecommendTime) {
    this.isSysRecommendTime = isSysRecommendTime;
  }

  public String getIsSysRecommendTime() {
    return this.isSysRecommendTime;
  }

  public String getServerId() {
    return this.serverId;
  }

  public void setServerId(String serverId) {
    this.serverId = serverId;
  }

  public String getOffsetDate() {
    return this.offsetDate;
  }

  public void setOffsetDate(String offsetDate) {
    this.offsetDate = offsetDate;
  }

  public String getDayDataDate() {
    return this.dayDataDate;
  }

  public void setDayDataDate(String dayDataDate) {
    this.dayDataDate = dayDataDate;
  }

  public Integer getListMaxNum() {
    return this.listMaxNum;
  }

  public void setListMaxNum(Integer listMaxNum) {
    this.listMaxNum = listMaxNum;
  }

  public String getSimpleRule() {
    return this.simpleRule;
  }

  public void setSimpleRule(String simpleRule) {
    this.simpleRule = simpleRule;
  }

  public boolean getIsOverlength() {
    return this.isOverlength;
  }

  public void setIsOverlength(boolean isOverlength) {
    this.isOverlength = isOverlength;
  }

  public String getDataDateStr() {
    return this.dataDateStr;
  }

  public void setDataDateStr(String dataDateStr) {
    this.dataDateStr = dataDateStr;
  }

  public Double getAvgScore() {
    return this.avgScore;
  }

  public void setAvgScore(Double avgScore) {
    this.avgScore = avgScore;
  }

  public String getSysId() {
    return this.sysId;
  }

  public void setSysId(String sysId) {
    this.sysId = sysId;
  }

  public String getRuleExpress() {
    return this.ruleExpress;
  }

  public void setRuleExpress(String ruleExpress) {
    this.ruleExpress = ruleExpress;
  }
}