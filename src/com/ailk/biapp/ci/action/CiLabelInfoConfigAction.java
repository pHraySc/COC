package com.ailk.biapp.ci.action;

import au.com.bytecode.opencsv.CSVReader;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.entity.*;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.CiConfigLabeInfo;
import com.ailk.biapp.ci.model.LabelOperManager;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.model.TreeNode;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.service.ICiPersonNoticeService;
import com.ailk.biapp.ci.service.ICiSysAnnouncementService;
import com.ailk.biapp.ci.service.IDimCocIndexInfoService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.dimtable.model.DimTableDefine;
import com.asiainfo.biframe.dimtable.service.impl.DimTableServiceImpl;
import com.asiainfo.biframe.dimtable.service.impl.DimTableServiceImpl2;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.http.ResponseEncodingUtil;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class CiLabelInfoConfigAction extends CIBaseAction {
    private Logger log = Logger.getLogger(CiLabelInfoConfigAction.class);
    boolean approver = false;
    boolean importLabel;
    private File file;
    private String fileFileName;
    private String fileContentType;
    private CiLabelInfo createLabelInfo;
    private CiLabelInfo searchConditionInfo;
    private Pager pager;
    private CiConfigLabeInfo configLabelInfo;
    private CiLabelInfo ciLabelInfo;
    private List<LabelOperManager> labelOperManagerList;
    private List<DimLabelDataStatus> dimLabelDataStatusList;
    private List<DimApproveStatus> dimApproveStatusList;
    private List<DimScene> dimScenes;
    @Autowired
    private DimTableServiceImpl dimTableService;
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;
    @Autowired
    private ICiPersonNoticeService personNoticeService;
    @Autowired
    private ICiSysAnnouncementService ciSysAnnouncementService;
    private String labelId;
    private String error;
    private String sceneIds;
    private HashMap<String, Object> pojo = new HashMap();
    private List<DimLabelType> dimLabelTypeList;
    private List<DimLabelType> dimLabelTypeNotContainVertList = new ArrayList();
    private List<CiMdaSysTable> ciMdaSysTableList;
    private List<DimTableDefine> dimTableDefineList;
    private List<CiMdaSysTableColumn> ciMdaSysTableColumnList;
    @Autowired
    private IDimCocIndexInfoService cocIndexInfoService;

    public CiLabelInfoConfigAction() {
    }

    public String init() throws Exception {
        if(!PrivilegeServiceUtil.isAdminUser(this.getUserId())) {
            throw new Exception();
        } else {
            CacheBase cacheBase = CacheBase.getInstance();
            this.dimLabelDataStatusList =(List<DimLabelDataStatus>) cacheBase.getObjectList("DIM_LABEL_DATA_STATUS");
            CopyOnWriteArrayList list = cacheBase.getObjectList("DIM_APPROVE_STATUS");
            ArrayList approveStatusList = new ArrayList();
            Iterator i$ = list.iterator();

            while(true) {
                DimApproveStatus dimApproveStatus;
                do {
                    if(!i$.hasNext()) {
                        this.dimApproveStatusList = approveStatusList;
                        if(this.pager == null) {
                            this.pager = new Pager();
                        }

                        if(StringUtil.isNotEmpty(this.pojo.get("classes")) && "true".equalsIgnoreCase(this.pojo.get("classes").toString())) {
                            return "classesIndex";
                        }

                        return "index";
                    }

                    dimApproveStatus = (DimApproveStatus)i$.next();
                } while(!"1".equals(dimApproveStatus.getProcessId()) && dimApproveStatus.getSortNum().longValue() != 0L);

                approveStatusList.add(dimApproveStatus);
            }
        }
    }

    public String query() throws Exception {
        if(!PrivilegeServiceUtil.isAdminUser(this.getUserId())) {
            throw new Exception();
        } else {
            if(this.searchConditionInfo == null) {
                this.searchConditionInfo = new CiLabelInfo();
            }

            if(this.pager == null) {
                this.pager = new Pager();
            }

            this.searchConditionInfo.setIsStatUserNum(String.valueOf(1));
            this.userId = PrivilegeServiceUtil.getUserId();
            Pager p2 = new Pager((long)this.ciLabelInfoService.queryTotalLabelInfoCount(this.searchConditionInfo));
            this.pager.setTotalPage(p2.getTotalPage());
            this.pager.setTotalSize(p2.getTotalSize());
            this.pager = this.pager.pagerFlip();
            List ciLabelInfoList = this.ciLabelInfoService.queryPageLabelInfoList(this.pager.getPageNum(), this.pager.getPageSize(), this.searchConditionInfo);
            String labelStopOnOffLine = Configure.getInstance().getProperty("LABEL_STOPONOFFLINE");
            if("true".equals(labelStopOnOffLine)) {
                this.labelOperManagerList = new ArrayList();

                LabelOperManager lom;
                for(Iterator i$ = ciLabelInfoList.iterator(); i$.hasNext(); this.labelOperManagerList.add(lom)) {
                    CiLabelInfo ci = (CiLabelInfo)i$.next();
                    lom = new LabelOperManager();
                    int statusId = ci.getDataStatusId().intValue();
                    if(statusId == 2 || statusId == 4 || statusId == 3) {
                        List toEffectChildrenLabels = this.ciLabelInfoService.queryToEffectChildrenLabelsById(ci.getLabelId());
                        if(toEffectChildrenLabels.size() == 0) {
                            if(statusId == 2) {
                                lom.setStopFlag(Integer.valueOf(1));
                            }

                            if(statusId == 4) {
                                lom.setOffLineFlag(Integer.valueOf(1));
                            }
                        }

                        if(statusId == 4 && null != ci.getParentId() && ci.getParentId().intValue() != -1) {
                            CiLabelInfo parentCiLabelInfo = this.ciLabelInfoService.queryCiLabelInfoById(ci.getParentId());
                            if(parentCiLabelInfo.getDataStatusId().intValue() == 2) {
                                lom.setOnLineFlag(Integer.valueOf(1));
                                lom.setModifyFlag(Integer.valueOf(1));
                            }
                        }
                    }
                }
            }

            this.pager.setResult(ciLabelInfoList);
            return "query";
        }
    }

    public String queryClasses() throws Exception {
        if(!PrivilegeServiceUtil.isAdminUser(this.getUserId())) {
            throw new Exception();
        } else {
            if(this.searchConditionInfo == null) {
                this.searchConditionInfo = new CiLabelInfo();
            }

            if(this.pager == null) {
                this.pager = new Pager();
            }

            this.userId = PrivilegeServiceUtil.getUserId();
            this.searchConditionInfo.setIsStatUserNum(String.valueOf(0));
            Pager p2 = new Pager((long)this.ciLabelInfoService.queryTotalPageCount(this.searchConditionInfo));
            this.pager.setTotalPage(p2.getTotalPage());
            this.pager.setTotalSize(p2.getTotalSize());
            this.pager = this.pager.pagerFlip();
            List ciLabelInfoList = this.ciLabelInfoService.queryPageList(this.pager.getPageNum(), this.pager.getPageSize(), this.searchConditionInfo);
            String labelStopOnOffLine = Configure.getInstance().getProperty("LABEL_STOPONOFFLINE");
            if("true".equals(labelStopOnOffLine)) {
                this.labelOperManagerList = new ArrayList();

                LabelOperManager lom;
                for(Iterator i$ = ciLabelInfoList.iterator(); i$.hasNext(); this.labelOperManagerList.add(lom)) {
                    CiLabelInfo ci = (CiLabelInfo)i$.next();
                    lom = new LabelOperManager();
                    int statusId = ci.getDataStatusId().intValue();
                    List toEffectChildrenLabels = this.ciLabelInfoService.queryToEffectChildrenLabelsById(ci.getLabelId());
                    if(toEffectChildrenLabels.size() == 0) {
                        lom.setDeleteFlag(Integer.valueOf(1));
                    }

                    if(toEffectChildrenLabels.size() == 0 && statusId == 2) {
                        lom.setOffLineFlag(Integer.valueOf(1));
                    }

                    if(null != ci.getParentId() && ci.getParentId().intValue() != -1) {
                        CiLabelInfo parentCiLabelInfo = this.ciLabelInfoService.queryCiLabelInfoById(ci.getParentId());
                        if(ci.getDataStatusId().intValue() == 1 && parentCiLabelInfo.getDataStatusId().intValue() == 2) {
                            lom.setOnLineFlag(Integer.valueOf(1));
                        }
                    }

                    if(null != ci.getParentId() && ci.getParentId().intValue() == -1 && ci.getDataStatusId().intValue() == 1) {
                        lom.setOnLineFlag(Integer.valueOf(1));
                    }
                }
            }

            this.pager.setResult(ciLabelInfoList);
            return "queryClasses";
        }
    }

    public String queryIndexCodeList() throws Exception {
        if(!PrivilegeServiceUtil.isAdminUser(this.getUserId())) {
            throw new Exception();
        } else {
            if(this.pojo == null) {
                this.pojo = new HashMap();
            }

            if(this.pager == null) {
                this.pager = new Pager();
            }

            this.userId = PrivilegeServiceUtil.getUserId();
            Pager p2 = new Pager((long)this.cocIndexInfoService.queryDimCocIndexInfoCount(this.pojo));
            this.pager.setTotalPage(p2.getTotalPage());
            this.pager.setTotalSize(p2.getTotalSize());
            this.pager = this.pager.pagerFlip();
            List dimCocIndexInfoList = this.cocIndexInfoService.queryDimCocIndexInfoList(this.pager.getPageNum(), this.pager.getPageSize(), this.pojo);
            this.pager.setResult(dimCocIndexInfoList);
            return "queryIndexCode";
        }
    }

    public String configLabelIndex() {
        new CiLabelInfo();

        try {
            if(this.configLabelInfo == null) {
                this.log.info("标签对象为空");
                throw new CIServiceException("标签对象为空");
            }

            if(this.configLabelInfo.getLabelId() == null) {
                this.log.info("标签ID为空");
                throw new CIServiceException("标签ID为空");
            }

            int e = 0;
            CiLabelInfo labelInfo = this.ciLabelInfoService.queryCiLabelInfoByIdFullLoad(this.configLabelInfo.getLabelId());
            CacheBase cacheBase = CacheBase.getInstance();
            this.dimLabelTypeList = (List<DimLabelType>)cacheBase.getObjectList("DIM_LABEL_TYPE");
            Iterator tableServiceImpl = this.dimLabelTypeList.iterator();

            while(tableServiceImpl.hasNext()) {
                DimLabelType updateCycle = (DimLabelType)tableServiceImpl.next();
                if(8 != updateCycle.getLabelTypeId().intValue() && 1 != updateCycle.getLabelTypeId().intValue()) {
                    this.dimLabelTypeNotContainVertList.add(updateCycle);
                }
            }

            this.ciMdaSysTableList =(List<CiMdaSysTable>) cacheBase.getObjectList("CI_SYS_TABLE_MAP");
            DimTableServiceImpl2 var14 = (DimTableServiceImpl2)SystemServiceLocator.getInstance().getService("dimTable_newService");
            this.dimTableDefineList = var14.findDimTableDefineByJndiId(Configure.getInstance().getProperty("JNDI_ID"));
            this.configLabelInfo.setLabelTypeId(labelInfo.getLabelTypeId());
            if(labelInfo.getParentId() != null) {
                CiLabelInfo var15 = this.ciLabelInfoService.queryCiLabelInfoByLabelId(labelInfo.getParentId());
                this.configLabelInfo.setParentId(labelInfo.getParentId());
                if(var15 != null) {
                    this.configLabelInfo.setParentName(var15.getLabelName());
                }
            }

            Integer var16 = labelInfo.getUpdateCycle();
            this.configLabelInfo.setFailTime(labelInfo.getFailTime());
            CiLabelExtInfo labelExtInfo = labelInfo.getCiLabelExtInfo();
            if(labelExtInfo != null) {
                DimCocLabelCountRules labelCountRules = this.ciLabelInfoService.queryLabelCountRules(labelExtInfo.getCountRulesCode());
                if(labelCountRules != null) {
                    BeanUtils.copyProperties(labelCountRules, this.configLabelInfo);
                }

                CiMdaSysTableColumn tableColumn = labelExtInfo.getCiMdaSysTableColumn();
                if(tableColumn != null) {
                    BeanUtils.copyProperties(tableColumn, this.configLabelInfo);
                    CiMdaSysTable columnRel = tableColumn.getCiMdaSysTable();
                    BeanUtils.copyProperties(columnRel, this.configLabelInfo);
                }

                if(8 == this.configLabelInfo.getLabelTypeId().intValue()) {
                    if(null == this.ciMdaSysTableColumnList) {
                        this.ciMdaSysTableColumnList = new ArrayList();
                    }

                    Set var17 = labelExtInfo.getCiLabelVerticalColumnRels();

                    for(Iterator i$ = var17.iterator(); i$.hasNext(); ++e) {
                        CiLabelVerticalColumnRel ciLabelVerticalColumnRel = (CiLabelVerticalColumnRel)i$.next();
                        CiMdaSysTableColumn ciMdaSysTableColumn = ciLabelVerticalColumnRel.getCiMdaSysTableColumn();
                        ciMdaSysTableColumn.setIsMustColumn(ciLabelVerticalColumnRel.getIsMustColumn());
                        ciMdaSysTableColumn.setVertLabelTypeId(ciLabelVerticalColumnRel.getLabelTypeId());
                        this.ciMdaSysTableColumnList.add(ciMdaSysTableColumn);
                    }
                }
            }

            this.pojo.put("columnListSize", Integer.valueOf(e));
            this.configLabelInfo.setUpdateCycle(var16);
        } catch (Exception var13) {
            this.log.error("标签配置信息获取失败", var13);
        }

        return "configLabel";
    }

    public void queryCiSysMdaTable() {
        String msg = "查询系统宽表成功";
        String failMsg = "查询系统宽表失败";
        HashMap returnMap = new HashMap();
        HttpServletResponse response = this.getResponse();

        try {
            if(this.pojo == null) {
                this.log.info("参数对象为空");
                throw new CIServiceException("参数对象为空");
            }

            if(StringUtil.isEmpty(this.pojo.get("updateCycle"))) {
                this.log.info("标签周期性为空");
                throw new CIServiceException("标签周期性为空");
            }

            if(StringUtil.isEmpty(this.pojo.get("labelTypeId"))) {
                this.log.info("标签类型为空");
                throw new CIServiceException("标签类型为空");
            }

            ArrayList e = new ArrayList();
            CacheBase cacheBase = CacheBase.getInstance();
            CopyOnWriteArrayList mdaSysTableList = cacheBase.getObjectList("CI_SYS_TABLE_MAP");
            int updateCycle = Integer.parseInt(this.pojo.get("updateCycle").toString());
            int labelTypeId = Integer.parseInt(this.pojo.get("labelTypeId").toString());
            Iterator i$ = mdaSysTableList.iterator();

            while(i$.hasNext()) {
                CiMdaSysTable ciMdaSysTable = (CiMdaSysTable)i$.next();
                if(null != ciMdaSysTable.getUpdateCycle() && null != ciMdaSysTable.getTableType() && updateCycle == ciMdaSysTable.getUpdateCycle().intValue() && labelTypeId == ciMdaSysTable.getTableType().intValue()) {
                    e.add(ciMdaSysTable);
                }
            }

            returnMap.put("resultTableList", e);
            returnMap.put("success", Boolean.valueOf(true));
            returnMap.put("msg", msg);
        } catch (Exception var13) {
            returnMap.put("success", Boolean.valueOf(false));
            returnMap.put("msg", failMsg);
            this.log.error(failMsg, var13);
        }

        try {
            this.sendJson(response, JsonUtil.toJson(returnMap));
        } catch (Exception var12) {
            this.log.error("发送json串异常", var12);
            throw new CIServiceException(var12);
        }
    }

    public void queryLabelTree() throws Exception {
        new ArrayList();
        int levelType = Integer.valueOf(Configure.getInstance().getProperty("LABEL_CATEGORY_TYPE")).intValue();
        int maxLevel = levelType + 2;
        List treeNodeList = this.ciLabelInfoService.queryAllEffectLabelTree(Integer.valueOf(levelType), Integer.valueOf(maxLevel));

        try {
            this.sendJson(this.getResponse(), JsonUtil.toJson(treeNodeList));
        } catch (Exception var5) {
            this.log.error("获取标签分类json串异常", var5);
            throw new CIServiceException(var5);
        }
    }

    public String saveLabelInfoInit() throws Exception {
        CacheBase cache = CacheBase.getInstance();
        this.dimScenes =(List<DimScene>) cache.getObjectList("DIM_SCENE");
        if(this.labelId != null) {
            List sceneList = this.ciLabelInfoService.queryLabelSceneByLabelId(this.labelId);
            StringBuffer sceneSb = new StringBuffer();

            for(int i = 0; i < sceneList.size(); ++i) {
                CiLabelSceneRel item = (CiLabelSceneRel)sceneList.get(i);
                sceneSb.append(item.getId().getSceneId());
                if(i < sceneList.size() - 1) {
                    sceneSb.append(",");//每个字符串后加个del最后一个不加

                }
            }

            this.sceneIds = sceneSb.toString();
        }

        return "addConfigLabel";
    }

    public void saveConfigLabelInfo() throws Exception {
        String msg = "保存标签配置成功";
        String failMsg = "保存标签配置失败";
        HashMap returnMap = new HashMap();
        HttpServletResponse response = this.getResponse();

        try {
            if(this.configLabelInfo == null) {
                this.log.info("标签对象为空");
                throw new CIServiceException("标签对象为空");
            }

            if(this.configLabelInfo.getLabelId() == null) {
                this.log.info("标签ID为空");
                throw new CIServiceException("标签ID为空");
            }

            this.configLabelInfo.setLabelCreateType(Integer.valueOf(1));
            this.configLabelInfo.setUserId(PrivilegeServiceUtil.getUserId());
            if(8 != this.configLabelInfo.getLabelTypeId().intValue()) {
                this.ciMdaSysTableColumnList = null;
            }

            this.ciLabelInfoService.saveConfigLabelInfo(this.configLabelInfo, this.ciMdaSysTableColumnList);
            CiLabelExtInfo e = this.ciLabelInfoService.queryCiLabelExtInfoById(this.configLabelInfo.getParentId());
            if(null != e && 1 == e.getIsLeaf().intValue()) {
                this.ciLabelInfoService.modifyLabelLeaf(e.getLabelId().toString(), Integer.valueOf(0));
            }

            returnMap.put("success", Boolean.valueOf(true));
            returnMap.put("labelId", this.configLabelInfo.getLabelId());
            returnMap.put("dimTransId", this.configLabelInfo.getDimTransId());
            returnMap.put("cmsg", msg);
            returnMap.put("labelId", this.configLabelInfo.getLabelId());
        } catch (Exception var7) {
            returnMap.put("success", Boolean.valueOf(false));
            returnMap.put("cmsg", failMsg);
            this.log.error(failMsg, var7);
        }

        try {
            this.sendJson(response, JsonUtil.toJson(returnMap));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void save() throws Exception {
        String msg = "";
        HashMap returnMsg = new HashMap();
        HttpServletResponse response = this.getResponse();

        try {
            String e = PrivilegeServiceUtil.getUserId();
            Integer deptId = PrivilegeServiceUtil.getUserDeptId(e);
            String deptIdStr = "";
            if(deptId != null) {
                deptIdStr = Integer.toString(deptId.intValue());
            }

            this.createLabelInfo.setCreateTime(new Date());
            this.createLabelInfo.setCreateUserId(e);
            this.createLabelInfo.setDeptId(deptIdStr);
            this.createLabelInfo.setIsSysRecom(Integer.valueOf(ServiceConstants.IS_NOT_SYS_RECOM));
            if(this.createLabelInfo.getDataStatusId() == null) {
                this.createLabelInfo.setDataStatusId(Integer.valueOf(1));
            }//如果没有状态ID就设置为了，对应未生效

            Boolean hasSameLabel = this.ciLabelInfoService.hasSameLabel(this.createLabelInfo);
            if(hasSameLabel.booleanValue()) {
                msg = "标签有重名";
                returnMsg.put("msgType", "sameLabel");
            } else {
                if(this.createLabelInfo.getLabelId() != null) {
                    msg = "标签修改成功";
                } else {
                    msg = "标签创建成功";
                }

                ArrayList sceneList = new ArrayList();
                if(StringUtil.isNotEmpty(this.sceneIds)) {
                    String[] sceneIdsArr = this.sceneIds.split(",");

                    for(int i = 0; i < sceneIdsArr.length; ++i) {
                        if(StringUtil.isNotEmpty(sceneIdsArr[i].trim())) {
                            CiLabelSceneRel item = new CiLabelSceneRel();
                            item.setId(new CiLabelSceneRelId());
                            item.getId().setSceneId(sceneIdsArr[i].trim());
                            sceneList.add(item);
                        }
                    }

                    this.createLabelInfo.setSceneList(sceneList);
                }

                this.createLabelInfo.setCreateUserId(e);
                /*
                    存在labelId则编辑修改该标签
                    不存在labelId则新增该标签
                 */
                if(null != this.createLabelInfo.getLabelId()) {
                    this.ciLabelInfoService.editLabelInfoByConfig(this.createLabelInfo);
                } else {
                    this.ciLabelInfoService.addLabelInfoByConfig(this.createLabelInfo);
                }

                returnMsg.put("msgType", "successSave");
                returnMsg.put("labelId", this.createLabelInfo.getLabelId());
                returnMsg.put("labelName", this.createLabelInfo.getLabelName());
            }

            returnMsg.put("msgContent", msg);
            returnMsg.put("labelId", this.createLabelInfo.getLabelId());
        } catch (Exception var13) {
            this.log.error("标签保存出错", var13);
            throw new CIServiceException(var13);
        }

        try {
            this.sendJson(response, JsonUtil.toJson(returnMsg));
        } catch (Exception var12) {
            this.log.error("发送json串异常", var12);
            throw new CIServiceException(var12);
        }
    }

    private String getVerifySql(Integer updateCycle, String dependIndex, String countRules, Integer labelTypeId, String vertColumns, String vertTableName) {
        StringBuffer sqlBuf = new StringBuffer();
        if(8 == labelTypeId.intValue()) {
            sqlBuf.append(" SELECT ");
            sqlBuf.append(vertColumns);
            sqlBuf.append(" FROM ");
            sqlBuf.append(vertTableName.toUpperCase()).append("_");
            if(updateCycle.intValue() == 2) {
                sqlBuf.append(CacheBase.getInstance().getNewLabelMonth());
            } else if(updateCycle.intValue() == 1) {
                sqlBuf.append(CacheBase.getInstance().getNewLabelDay());
            }
        } else {
            if(1 == labelTypeId.intValue()) {
                sqlBuf.append("SELECT CASE WHEN ");
                sqlBuf.append(countRules);
                sqlBuf.append(" THEN 1 END  FROM ");
            } else {
                sqlBuf.append(" SELECT ");
                sqlBuf.append(countRules);
                sqlBuf.append(" FROM ");
            }

            String[] dependIndexArr = dependIndex.split(",");
            int i = 1;
            ArrayList indexTabNames = new ArrayList();
            String[] arr$ = dependIndexArr;
            int len$ = dependIndexArr.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String index = arr$[i$];
                DimCocIndexInfo indexInfo = this.ciLabelInfoService.queryDimCocIndexInfoById(index);
                if(indexInfo == null) {
                    sqlBuf.append(" {not found Index table name }");
                } else {
                    DimCocIndexTableInfo indexTableInfo = this.ciLabelInfoService.queryDimCocIndexTableInfoById(indexInfo.getDataSrcCode());
                    if(indexTableInfo == null) {
                        sqlBuf.append(" {not found Index table name }");
                    } else if(!indexTabNames.contains(indexTableInfo.getDataSrcTabName().toUpperCase())) {
                        if(i == 1) {
                            sqlBuf.append(indexTableInfo.getDataSrcTabName().toUpperCase());
                            if(updateCycle.intValue() == 2) {
                                sqlBuf.append(CacheBase.getInstance().getNewLabelMonth());
                            } else if(updateCycle.intValue() == 1) {
                                sqlBuf.append(CacheBase.getInstance().getNewLabelDay());
                            }

                            sqlBuf.append(" N");
                        } else {
                            sqlBuf.append(" LEFT JOIN ").append(indexTableInfo.getDataSrcTabName().toUpperCase());
                            if(updateCycle.intValue() == 2) {
                                sqlBuf.append(CacheBase.getInstance().getNewLabelMonth());
                            } else if(updateCycle.intValue() == 1) {
                                sqlBuf.append(CacheBase.getInstance().getNewLabelDay());
                            }

                            sqlBuf.append(" N").append(i).append(" ON  N.PRODUCT_NO = N").append(i).append(".PRODUCT_NO");
                        }

                        ++i;
                        indexTabNames.add(indexTableInfo.getDataSrcTabName().toUpperCase());
                    }
                }
            }
        }

        return sqlBuf.toString();
    }

    public String getVerifySql() {
        String sql = "";
        new CiLabelInfo();

        try {
            CiLabelInfo labelInfo = this.ciLabelInfoService.queryCiLabelInfoByIdFullLoad(this.configLabelInfo.getLabelId());
            CiLabelExtInfo e = labelInfo.getCiLabelExtInfo();
            if(8 != labelInfo.getLabelTypeId().intValue()) {
                DimCocLabelCountRules var11 = this.ciLabelInfoService.queryLabelCountRules(e.getCountRulesCode());
                sql = this.getVerifySql(labelInfo.getUpdateCycle(), var11.getDependIndex(), var11.getCountRules(), labelInfo.getLabelTypeId(), "", "");
            } else {
                StringBuffer labelCountRules = new StringBuffer();
                Set columnRel = e.getCiLabelVerticalColumnRels();
                String vertTableName = "";
                int i = 0;

                for(Iterator i$ = columnRel.iterator(); i$.hasNext(); ++i) {
                    CiLabelVerticalColumnRel ciLabelVerticalColumnRel = (CiLabelVerticalColumnRel)i$.next();
                    if(i > 0) {
                        labelCountRules.append(" , ");
                    } else {
                        vertTableName = ciLabelVerticalColumnRel.getCiMdaSysTableColumn().getCiMdaSysTable().getTableName();
                    }

                    labelCountRules.append(ciLabelVerticalColumnRel.getCiMdaSysTableColumn().getColumnName());
                }

                sql = this.getVerifySql(labelInfo.getUpdateCycle(), "", "", labelInfo.getLabelTypeId(), labelCountRules.toString(), vertTableName);
            }

            this.pojo.put("sql", sql);
    } catch (Exception var10) {
        this.log.error("查询标签校验SQL失败", var10);
    }

        return "publishLabel";
    }

    public void publishLabel() {
        String msg = "发布标签成功";
        String failMsg = "发布标签失败";
        HashMap returnMap = new HashMap();
        HttpServletResponse response = this.getResponse();
        new CiLabelInfo();
        String publishTime = null;

        try {
            String e = PrivilegeServiceUtil.getUserId();
            CiLabelInfo labelInfo = this.ciLabelInfoService.queryCiLabelInfoById(this.configLabelInfo.getLabelId());
            if(labelInfo.getLabelTypeId().intValue() == 3) {
                DimTableDefine date = this.dimTableService.findDefineById(this.configLabelInfo.getDimTransId());
                List ft = this.dimTableService.getAllDimData(date.getDimId(), date.getDimPCodeCol(), (String)null, (String)null, (String)null, (String)null, false);
                this.configLabelInfo.setDefine(date);
                this.configLabelInfo.setDimTransList(ft);
            }

            this.ciLabelInfoService.publishLabel(this.configLabelInfo, e);
            labelInfo.setPublishDesc(this.configLabelInfo.getPublishDesc());
            labelInfo.setPublishUserId(e);
            labelInfo.setPublishTime(new Date());
            this.ciLabelInfoService.modify(labelInfo);
            Date date1 = new Date();
            SimpleDateFormat ft1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            publishTime = ft1.format(date1);
            CiPersonNotice personNotice = new CiPersonNotice();
            personNotice.setStatus(Integer.valueOf(1));
            personNotice.setLabelId(this.configLabelInfo.getLabelId());
            personNotice.setNoticeName(labelInfo.getLabelName() + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_LABEL_PUBLISH);
            personNotice.setNoticeDetail("您好 " + PrivilegeServiceUtil.getUserDept(this.getUserId()) + "的" + this.getUserName() + "于" + publishTime + "发布名称为“" + labelInfo.getLabelName() + "”的标签");
            personNotice.setNoticeSendTime(new Date());
            personNotice.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_LABEL_PUBLISH));
            personNotice.setReadStatus(Integer.valueOf(1));
            personNotice.setIsSuccess(Integer.valueOf(1));
            String createUserId = labelInfo.getCreateUserId();
            personNotice.setReleaseUserId(createUserId);
            personNotice.setReceiveUserId(createUserId);
            this.personNoticeService.addPersonNotice(personNotice);
            CiSysAnnouncement ciSysAnnouncement = new CiSysAnnouncement();
            ciSysAnnouncement.setAnnouncementName(labelInfo.getLabelName() + " " + "标签发布公告");
            ciSysAnnouncement.setAnnouncementDetail("您好 " + PrivilegeServiceUtil.getUserDept(this.getUserId()) + "的" + this.getUserName() + "于" + publishTime + "发布名称为“" + labelInfo.getLabelName() + "”的标签");
            ciSysAnnouncement.setTypeId(Integer.valueOf(1));
            ciSysAnnouncement.setPriorityId(Integer.valueOf(0));
            ciSysAnnouncement.setEffectiveTime(Timestamp.valueOf("2099-06-30 00:00:00"));
            ciSysAnnouncement.setReleaseDate(Timestamp.valueOf(publishTime));
            ciSysAnnouncement.setStatus(Integer.valueOf(1));
            ciSysAnnouncement.setUserId(e);
            this.ciSysAnnouncementService.addSysAnnouncement(ciSysAnnouncement);
            returnMap.put("success", Boolean.valueOf(true));
            returnMap.put("cmsg", msg);
        } catch (Exception var14) {
            returnMap.put("success", Boolean.valueOf(false));
            returnMap.put("cmsg", failMsg);
            this.log.error(failMsg, var14);
        }

        try {
            this.sendJson(response, JsonUtil.toJson(returnMap));
        } catch (Exception var13) {
            this.log.error("发送json串异常", var13);
            throw new CIServiceException(var13);
        }
    }

    public String loadLabelInfoFile() {
        ActionContext ctx = ActionContext.getContext();
        String forward = "importSuccess";
        boolean success = false;
        String msg = "";
        new HashMap();

        try {
            Map resultMap = this.ciLabelInfoService.saveImportLabel(this.file);
            success = ((Boolean)resultMap.get("success")).booleanValue();
            msg = (String)resultMap.get("error");
        } catch (Exception var11) {
            this.log.error("导入失败", var11);
            forward = "importFail";
            success = false;
        } finally {
            if(!success) {
                forward = "importFail";
            }

        }

        ctx.put("success", Boolean.valueOf(success));
        ctx.put("error", msg);
        return forward;
    }

    public void saveLabelClassify() throws Exception {
        String msg = "";
        HashMap returnMsg = new HashMap();

        try {
            Boolean response = this.ciLabelInfoService.hasSameLabel(this.ciLabelInfo);
            Boolean e = Boolean.valueOf(true);
            if(null != this.ciLabelInfo.getLabelId()) {
                e = this.ciLabelInfoService.validEidtLabel(this.ciLabelInfo);
            }

            if(response.booleanValue()) {
                msg = "标签分类有重名";
                returnMsg.put("msgType", "sameLabel");
            } else if(null != this.ciLabelInfo.getLabelId() && this.ciLabelInfo.getLabelId().equals(this.ciLabelInfo.getParentId())) {
                msg = "标签分类不能选择标签自己，请选择其他标签分类";
                returnMsg.put("msgType", "sameClassify");
            } else if(null != this.ciLabelInfo.getLabelId() && !e.booleanValue()) {
                msg = "分类标签含有子标签，不能修改为其他级别的标签！";
                returnMsg.put("msgType", "noEditLabel");
            } else {
                if(null != this.ciLabelInfo.getLabelId()) {
                    msg = "标签分类修改成功";
                    returnMsg.put("msgType", "successEdit");
                } else {
                    msg = "标签分类创建成功";
                    returnMsg.put("msgType", "successSave");
                }

                this.ciLabelInfo.setDataStatusId(Integer.valueOf(1));
                this.ciLabelInfoService.saveLabelClassify(this.ciLabelInfo);
                returnMsg.put("labelId", this.ciLabelInfo.getLabelId());
                returnMsg.put("labelName", this.ciLabelInfo.getLabelName());
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_MANAGE_ADD", this.ciLabelInfo.getLabelId().toString(), this.ciLabelInfo.getLabelName(), msg + ",【标签名称 :" + this.ciLabelInfo.getLabelName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            }

            returnMsg.put("msgContent", msg);
        } catch (Exception var6) {
            this.log.error("标签分类保存出错", var6);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_MANAGE_ADD", "-1", "标签创建出错", "标签创建失败,【标签名称 :" + this.ciLabelInfo.getLabelName() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
            throw new CIServiceException(var6);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
        } catch (Exception var5) {
            this.log.error("发送json串异常", var5);
            throw new CIServiceException(var5);
        }
    }

    public void getLabelClassify() throws Exception {
        HttpServletResponse response = this.getResponse();
        String parentName = "";
        new CiLabelInfo();

        CiLabelInfo ciLabelInfo;
        try {
            ciLabelInfo = this.ciLabelInfoService.queryCiLabelInfoById(Integer.valueOf(this.labelId));
            if(ciLabelInfo.getParentId().intValue() != -1) {
                CiLabelInfo returnLabel = this.ciLabelInfoService.queryCiLabelInfoById(ciLabelInfo.getParentId());
                parentName = returnLabel.getLabelName();
            } else {
                parentName = "根节点";
            }
        } catch (Exception var7) {
            this.log.error("CiLabelInfoAction.getLabelInfo() method 发生异常", var7);
            throw new CIServiceException(var7);
        }

        HashMap returnLabel1 = new HashMap();
        returnLabel1.put("labelInfo", ciLabelInfo);
        returnLabel1.put("parentName", parentName);

        try {
            this.sendJson(response, JsonUtil.toJson(returnLabel1));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void getLabelTreeByLevel() throws Exception {
        TreeNode tNode = new TreeNode();
        tNode.setId("-1");
        tNode.setIsParent(Boolean.valueOf(true));
        tNode.setName("根节点");
        tNode.setOpen(Boolean.valueOf(true));
        tNode.setTip("根节点");
        tNode.setpId("0");
        byte levelNum = 2;
        int labelCategoryType = Integer.valueOf(Configure.getInstance().getProperty("LABEL_CATEGORY_TYPE")).intValue();
        if(labelCategoryType == 2) {
            levelNum = 1;
        }

        List treeNodeList = this.ciLabelInfoService.queryAllEffectLabelTree(Integer.valueOf(1), Integer.valueOf(levelNum));
        treeNodeList.add(tNode);

        try {
            this.sendJson(this.getResponse(), JsonUtil.toJson(treeNodeList));
        } catch (Exception var6) {
            this.log.error("获取标签分类json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void downImportTempletFile() {
        ServletOutputStream out = null;
        FileInputStream fileInputStream = null;
        boolean isVertLabel = false;
        String fileName = "";
        if(StringUtil.isNotEmpty(this.pojo.get("isVertLabel"))) {
            if("1".equals(this.pojo.get("isVertLabel"))) {
                isVertLabel = true;
            } else if("0".equals(this.pojo.get("isVertLabel"))) {
                isVertLabel = false;
            }
        }

        if(isVertLabel) {
            fileName = Configure.getInstance().getProperty("SYS_COMMON_VERT_LABEL_DOWN_NAME");
        } else {
            fileName = Configure.getInstance().getProperty("SYS_COMMON_LABEL_DOWN_NAME");
        }

        String localFilePath = "";

        try {
            HttpServletResponse e = this.getResponse();
            HttpServletRequest request = this.getRequest();
            String mpmPath = Configure.getInstance().getProperty("SYS_COMMON_DOWN_PATH");
            if(!mpmPath.endsWith(File.separator)) {
                mpmPath = mpmPath + File.separator;
            }

            mpmPath = request.getSession().getServletContext().getRealPath("/") + File.separator + mpmPath;
            File pathFile = new File(mpmPath);
            if(pathFile.exists() || pathFile.mkdirs()) {
                localFilePath = mpmPath + fileName;
                String clientLanguage = request.getHeader("Accept-Language");
                String guessCharset = ResponseEncodingUtil.getGuessCharset(clientLanguage);
                String fileNameEncode = ResponseEncodingUtil.encodingFileName(fileName, guessCharset);
                e.addHeader("Content-Disposition", "attachment; filename=" + fileNameEncode);
                this.log.debug("offline download from web server================the file path is: " + localFilePath);
                e.setContentType("application/octet-stream;charset=" + guessCharset);
                out = e.getOutputStream();
                fileInputStream = new FileInputStream(localFilePath);
                boolean bytesRead = false;
                byte[] buffer = new byte[1024];

                int bytesRead1;
                while((bytesRead1 = fileInputStream.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, bytesRead1);
                }

                return;
            }
        } catch (IOException var30) {
            this.log.error(var30);
            return;
        } finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException var29) {
                    ;
                }
            }

            if(fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException var28) {
                    ;
                }
            }

        }

    }

    public String loadVertLabelInfoFile() {
        ActionContext ctx = ActionContext.getContext();
        String forward = "importSuccess";
        this.importLabel = true;
        StringBuffer parentLabelIds = new StringBuffer();

        try {
            ArrayList e = new ArrayList();
            Charset charset = CiUtil.getFileCharset(this.file);
            CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(this.file), charset));
            int labelCount = 0;
            int totalCount = 0;
            int columnCount = 0;
            int lineNum = 0;
            HashSet nameSet = new HashSet();
            HashSet columnNameSet = new HashSet();
            HashSet columnCnNameSet = new HashSet();
            CiConfigLabeInfo ciConfiglabelInfo = new CiConfigLabeInfo();

            label572:
            while(true) {
                String[] nextLine;
                String labelName;
                do {
                    if((nextLine = reader.readNext()) == null) {
                        break label572;
                    }

                    ++lineNum;
                    this.log.info("charset:" + charset.toString());
                    this.log.info("content:" + nextLine);
                    labelName = nextLine[0];
                } while(labelName.startsWith("#"));

                if(StringUtil.isEmpty(labelName)) {
                    ctx.put("error", "第" + lineNum + "行" + "标签名称不能为空！");
                    forward = "importFail";
                    this.importLabel = false;
                    break;
                }

                String vertColumn = nextLine[9];
                boolean vertColumnNum = false;
                if(StringUtil.isEmpty(vertColumn)) {
                    ctx.put("error", "第" + lineNum + "行是否组合标签列不能为空！");
                    forward = "importFail";
                    this.importLabel = false;
                    break;
                }

                if(!"是".equals(vertColumn) && !"否".equals(vertColumn)) {
                    ctx.put("error", "第" + lineNum + "行数据，是否组合标签列内容不正确！");
                    forward = "importFail";
                    this.importLabel = false;
                    break;
                }

                if("是".equals(vertColumn)) {
                    vertColumnNum = true;
                    ++columnCount;
                } else if("否".equals(vertColumn)) {
                    vertColumnNum = false;
                    ++labelCount;
                }

                String vertColumnType;
                String updateType;
                String businessCaliber;
                String parentLabelName;
                if(vertColumnNum) {
                    if(totalCount < 1) {
                        ctx.put("error", "第" + lineNum + "行数据，组合标签应该在列信息前面！");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    CiMdaSysTableColumn bean = new CiMdaSysTableColumn();
                    columnCnNameSet.add(labelName);
                    if(columnCnNameSet.size() < columnCount) {
                        ctx.put("error", "第" + lineNum + "行表格中列中文名称重复！");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    bean.setColumnCnName(labelName);
                    String hasSameLabel = nextLine[1];
                    if(StringUtil.isEmpty(hasSameLabel)) {
                        ctx.put("error", "第" + lineNum + "行标签类型不能为空！");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    if(!hasSameLabel.equals("指标型") && !hasSameLabel.equals("枚举型") && !hasSameLabel.equals("日期型") && !hasSameLabel.equals("文本型")) {
                        ctx.put("error", "第" + lineNum + "行标签类型必须为[指标型,枚举型,日期型,文本型]中的其中一种!");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    if("指标型".equals(hasSameLabel)) {
                        bean.setVertLabelTypeId(Integer.valueOf(4));
                    } else if("枚举型".equals(hasSameLabel)) {
                        bean.setVertLabelTypeId(Integer.valueOf(5));
                    } else if("日期型".equals(hasSameLabel)) {
                        bean.setVertLabelTypeId(Integer.valueOf(6));
                    } else if("文本型".equals(hasSameLabel)) {
                        bean.setVertLabelTypeId(Integer.valueOf(7));
                    }

                    if(columnCount > 1) {
                        bean.setIsMustColumn(Integer.valueOf(0));
                    } else {
                        bean.setIsMustColumn(Integer.valueOf(1));
                        if(!hasSameLabel.equals("枚举型")) {
                            ctx.put("error", "第" + lineNum + "行是必选项，标签类型必须为枚举型!");
                            forward = "importFail";
                            this.importLabel = false;
                            break;
                        }
                    }

                    vertColumnType = nextLine[4].trim();
                    if(StringUtil.isEmpty(vertColumnType)) {
                        ctx.put("error", "第" + lineNum + "行列所属纵表标签不能为空!");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    if(!vertColumnType.equals(ciConfiglabelInfo.getLabelName())) {
                        ctx.put("error", "第" + lineNum + "行列所属纵表标签内容不正确!");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    updateType = nextLine[5];
                    if(bean.getVertLabelTypeId().intValue() != 5 && !StringUtil.isEmpty(updateType)) {
                        ctx.put("error", "第" + lineNum + "行数据，类型不为枚举型时维表应为空!");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    businessCaliber = nextLine[7];
                    if(StringUtil.isEmpty(businessCaliber)) {
                        ctx.put("error", "第" + lineNum + "行数据，数据类型不能为空！");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    bean.setDataType(businessCaliber);
                    parentLabelName = nextLine[8];
                    if(bean.getVertLabelTypeId().intValue() == 5) {
                        if(updateType == null) {
                            ctx.put("error", "第" + lineNum + "行数据，类型为枚举型时，对应维表不能为空！");
                            forward = "importFail";
                            this.importLabel = false;
                            break;
                        }

                        if(!CacheBase.getInstance().isExistTable(updateType.toString())) {
                            ctx.put("error", "第" + lineNum + "行没找到对应维表");
                            forward = "importFail";
                            this.importLabel = false;
                            break;
                        }

                        if(StringUtil.isEmpty(parentLabelName)) {
                            ctx.put("error", "第" + lineNum + "行数据，类型为枚举型时，过滤权限不能为空！");
                            forward = "importFail";
                            this.importLabel = false;
                            break;
                        }

                        if(!"是".equals(parentLabelName) && !"否".equals(parentLabelName)) {
                            ctx.put("error", "第" + lineNum + "行数据，过滤权限内容不正确！");
                            forward = "importFail";
                            this.importLabel = false;
                            break;
                        }

                        if("是".equals(parentLabelName)) {
                            bean.setIsNeedAuthority(Integer.valueOf(1));
                        } else {
                            bean.setIsNeedAuthority(Integer.valueOf(0));
                        }

                        bean.setDimTransId(updateType.toString());
                        DimTableDefine parentId = this.dimTableService.findDefineById(bean.getDimTransId());
                        String labelInfo = parentId.getDimCodeColType();
                        if(labelInfo.equalsIgnoreCase("char") && !businessCaliber.contains(labelInfo)) {
                            ctx.put("error", "第" + lineNum + "行数据，数据类型内容不正确！");
                            forward = "importFail";
                            this.importLabel = false;
                            break;
                        }
                    }

                    String var46 = nextLine[10];
                    if(StringUtil.isEmpty(var46)) {
                        ctx.put("error", "第" + lineNum + "行数据，列字段名不能为空！");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    columnNameSet.add(var46);
                    if(columnNameSet.size() < columnCount) {
                        ctx.put("error", "第" + lineNum + "行表格中列字段名重复！");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    bean.setColumnName(var46);
                    e.add(bean);
                    if(e.size() > 6) {
                        ctx.put("error", "第" + lineNum + "行数据，组合标签列信息不能超过6个！");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }
                } else {
                    if(totalCount > 0) {
                        this.ciLabelInfoService.saveImportVertConfigLabelInfo(ciConfiglabelInfo, e);
                        if(1 == ciConfiglabelInfo.getParentIdIsLeaf().intValue()) {
                            if(StringUtil.isEmpty(parentLabelIds.toString())) {
                                parentLabelIds.append(ciConfiglabelInfo.getParentId());
                            } else {
                                parentLabelIds.append(", ").append(ciConfiglabelInfo.getParentId());
                            }
                        }

                        e.clear();
                        columnNameSet.clear();
                        columnCnNameSet.clear();
                        ciConfiglabelInfo = new CiConfigLabeInfo();
                        columnCount = 0;
                    }

                    nameSet.add(labelName);
                    if(nameSet.size() < labelCount) {
                        ctx.put("error", "第" + lineNum + "行表格中标签名称重复！");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    CiLabelInfo var44 = new CiLabelInfo();
                    var44.setLabelName(labelName);
                    Boolean var45 = this.ciLabelInfoService.hasSameLabel(var44);
                    if(var45.booleanValue()) {
                        ctx.put("error", "第" + lineNum + "行标签名称已存在！");
                        this.error = "第" + lineNum + "行" + "标签名称已存在！";
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    ciConfiglabelInfo.setLabelName(labelName);
                    vertColumnType = nextLine[1];
                    if(StringUtil.isEmpty(vertColumnType)) {
                        ctx.put("error", "第" + lineNum + "行标签类型不能为空！");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    if(!vertColumnType.equals("组合型")) {
                        ctx.put("error", "第" + lineNum + "行标签类型必须为组合型!");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    if("组合型".equals(vertColumnType)) {
                        ciConfiglabelInfo.setLabelTypeId(Integer.valueOf(8));
                    }

                    updateType = nextLine[2];
                    if(StringUtil.isEmpty(updateType)) {
                        ctx.put("error", "第" + lineNum + "行更新周期不能为空！");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    if(!updateType.equals("日周期") && !updateType.equals("月周期")) {
                        ctx.put("error", "第" + lineNum + "行更新周期必须为日周期或者月周期");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    if("月周期".equals(updateType.toString())) {
                        ciConfiglabelInfo.setUpdateCycle(Integer.valueOf(2));
                    } else {
                        ciConfiglabelInfo.setUpdateCycle(Integer.valueOf(1));
                    }

                    businessCaliber = nextLine[3];
                    if(StringUtil.isEmpty(businessCaliber)) {
                        ctx.put("error", "第" + lineNum + "行业务口径不能为空！");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    ciConfiglabelInfo.setBusiCaliber(businessCaliber);
                    parentLabelName = nextLine[4];
                    if(StringUtil.isEmpty(parentLabelName)) {
                        ctx.put("error", "第" + lineNum + "行父标签不能为空!");
                        forward = "importFail";
                        this.importLabel = false;
                    }

                    Integer var47 = this.ciLabelInfoService.queryIdByNameForImport(parentLabelName);
                    if(var47 == null) {
                        ctx.put("error", "第" + lineNum + "行找不到父标签!");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    ciConfiglabelInfo.setParentId(var47);
                    CiLabelInfo var48 = this.ciLabelInfoService.queryCiLabelInfoByIdFullLoad(var47);
                    CiLabelExtInfo extInfo = var48.getCiLabelExtInfo();
                    ciConfiglabelInfo.setParentIdIsLeaf(Integer.valueOf(0));
                    if(null != extInfo.getIsLeaf() && 1 == extInfo.getIsLeaf().intValue()) {
                        ciConfiglabelInfo.setParentIdIsLeaf(Integer.valueOf(1));
                    }

                    Integer labelTypeLevel = Integer.valueOf(Configure.getInstance().getProperty("LABEL_CATEGORY_TYPE"));
                    if(extInfo.getLabelLevel().intValue() < labelTypeLevel.intValue() - 1) {
                        ctx.put("error", "第" + lineNum + "行数据，父标签级别不能小于" + (labelTypeLevel.intValue() - 1) + "级!");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    if(extInfo.getLabelLevel().intValue() == labelTypeLevel.intValue() + 3) {
                        ctx.put("error", "第" + lineNum + "行数据，父标签级别不能为" + (labelTypeLevel.intValue() + 3) + "级!");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    String tableName = nextLine[6];
                    if(StringUtil.isEmpty(tableName)) {
                        ctx.put("error", "第" + lineNum + "行宽表不能为空！");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    Integer tableId = CacheBase.getInstance().getLabelTableId(tableName);
                    if(tableId == null) {
                        ctx.put("error", "第" + lineNum + "行数据，没找到对应宽表!");
                        forward = "importFail";
                        this.importLabel = false;
                        break;
                    }

                    ciConfiglabelInfo.setTableId(tableId);
                    String sceneIds = nextLine[11];
                    ArrayList labelSceneList = new ArrayList();
                    if(StringUtil.isNotEmpty(sceneIds)) {
                        String[] sceneIdsArr = sceneIds.split(",");

                        for(int i = 0; i < sceneIdsArr.length; ++i) {
                            if(StringUtil.isNotEmpty(sceneIdsArr[i].trim())) {
                                String sceneValue = CacheBase.getInstance().getDimScene(sceneIdsArr[i].trim());
                                if(StringUtil.isEmpty(sceneValue)) {
                                    ctx.put("error", "第" + lineNum + "行数据，没有找到对应的场景分类!");
                                    forward = "importFail";
                                    this.importLabel = false;
                                    break;
                                }

                                CiLabelSceneRel item = new CiLabelSceneRel();
                                item.setId(new CiLabelSceneRelId());
                                item.getId().setSceneId(sceneIdsArr[i].trim());
                                labelSceneList.add(item);
                            }
                        }
                    }

                    ciConfiglabelInfo.setLabelSceneList(labelSceneList);
                }

                ++totalCount;
            }

            if(this.importLabel && e.size() > 0 && null != ciConfiglabelInfo) {
                this.ciLabelInfoService.saveImportVertConfigLabelInfo(ciConfiglabelInfo, e);
                if(1 == ciConfiglabelInfo.getParentIdIsLeaf().intValue()) {
                    if(StringUtil.isEmpty(parentLabelIds.toString())) {
                        parentLabelIds.append(ciConfiglabelInfo.getParentId());
                    } else {
                        parentLabelIds.append(", ").append(ciConfiglabelInfo.getParentId());
                    }
                }

                e.clear();
                columnNameSet.clear();
                columnCnNameSet.clear();
                boolean var43 = false;
            }
        } catch (Exception var41) {
            this.log.error("导入失败", var41);
            this.importLabel = false;
            forward = "importFail";
        } finally {
            if(StringUtil.isNotEmpty(parentLabelIds.toString())) {
                this.ciLabelInfoService.modifyLabelLeaf(parentLabelIds.toString(), Integer.valueOf(0));
            }

        }

        return forward;
    }

    public void downLabelClassFile() {
        try {
            String e = Configure.getInstance().getProperty("LABEL_CLASS_FILE");
            String path = Configure.getInstance().getProperty("SYS_COMMON_DOWN_PATH");
            path = this.getSession().getServletContext().getRealPath("/") + File.separator + path + File.separator + e;
            this.downLoadFile(path);
        } catch (Exception var3) {
            this.log.error("下载标签导入模版文件", var3);
        }

    }

    public String loadLabelClassFile() {
        ActionContext ctx = ActionContext.getContext();
        String forward = "importSuccess";
        boolean success = false;
        String msg = "";

        try {
            Map e = this.ciLabelInfoService.saveImportLabelClass(this.file);
            success = ((Boolean)e.get("success")).booleanValue();
            if(!success) {
                msg = (String)e.get("msg");
                forward = "importFail";
            }
        } catch (Exception var6) {
            this.log.error("导入失败", var6);
            forward = "importFail";
            success = false;
        }

        ctx.put("success", Boolean.valueOf(success));
        ctx.put("error", msg);
        return forward;
    }

    public boolean isImportLabel() {
        return this.importLabel;
    }

    public void setImportLabel(boolean importLabel) {
        this.importLabel = importLabel;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileFileName() {
        return this.fileFileName;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public String getFileContentType() {
        return this.fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public boolean isApprover() {
        return this.approver;
    }

    public void setApprover(boolean approver) {
        this.approver = approver;
    }

    public CiLabelInfo getCreateLabelInfo() {
        return this.createLabelInfo;
    }

    public void setCreateLabelInfo(CiLabelInfo createLabelInfo) {
        this.createLabelInfo = createLabelInfo;
    }

    public CiLabelInfo getSearchConditionInfo() {
        return this.searchConditionInfo;
    }

    public void setSearchConditionInfo(CiLabelInfo searchConditionInfo) {
        this.searchConditionInfo = searchConditionInfo;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public CiConfigLabeInfo getConfigLabelInfo() {
        return this.configLabelInfo;
    }

    public void setConfigLabelInfo(CiConfigLabeInfo configLabelInfo) {
        this.configLabelInfo = configLabelInfo;
    }

    public List<LabelOperManager> getLabelOperManagerList() {
        return this.labelOperManagerList;
    }

    public void setLabelOperManagerList(List<LabelOperManager> labelOperManagerList) {
        this.labelOperManagerList = labelOperManagerList;
    }

    public CiLabelInfo getCiLabelInfo() {
        return this.ciLabelInfo;
    }

    public void setCiLabelInfo(CiLabelInfo ciLabelInfo) {
        this.ciLabelInfo = ciLabelInfo;
    }

    public String getLabelId() {
        return this.labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<DimLabelDataStatus> getDimLabelDataStatusList() {
        return this.dimLabelDataStatusList;
    }

    public void setDimLabelDataStatusList(List<DimLabelDataStatus> dimLabelDataStatusList) {
        this.dimLabelDataStatusList = dimLabelDataStatusList;
    }

    public List<DimApproveStatus> getDimApproveStatusList() {
        return this.dimApproveStatusList;
    }

    public void setDimApproveStatusList(List<DimApproveStatus> dimApproveStatusList) {
        this.dimApproveStatusList = dimApproveStatusList;
    }

    public HashMap<String, Object> getPojo() {
        return this.pojo;
    }

    public void setPojo(HashMap<String, Object> pojo) {
        this.pojo = pojo;
    }

    public List<DimLabelType> getDimLabelTypeList() {
        return this.dimLabelTypeList;
    }

    public void setDimLabelTypeList(List<DimLabelType> dimLabelTypeList) {
        this.dimLabelTypeList = dimLabelTypeList;
    }

    public List<CiMdaSysTable> getCiMdaSysTableList() {
        return this.ciMdaSysTableList;
    }

    public void setCiMdaSysTableList(List<CiMdaSysTable> ciMdaSysTableList) {
        this.ciMdaSysTableList = ciMdaSysTableList;
    }

    public List<DimTableDefine> getDimTableDefineList() {
        return this.dimTableDefineList;
    }

    public void setDimTableDefineList(List<DimTableDefine> dimTableDefineList) {
        this.dimTableDefineList = dimTableDefineList;
    }

    public List<DimLabelType> getDimLabelTypeNotContainVertList() {
        return this.dimLabelTypeNotContainVertList;
    }

    public void setDimLabelTypeNotContainVertList(List<DimLabelType> dimLabelTypeNotContainVertList) {
        this.dimLabelTypeNotContainVertList = dimLabelTypeNotContainVertList;
    }

    public List<CiMdaSysTableColumn> getCiMdaSysTableColumnList() {
        return this.ciMdaSysTableColumnList;
    }

    public void setCiMdaSysTableColumnList(List<CiMdaSysTableColumn> ciMdaSysTableColumnList) {
        this.ciMdaSysTableColumnList = ciMdaSysTableColumnList;
    }

    public List<DimScene> getDimScenes() {
        return this.dimScenes;
    }

    public void setDimScenes(List<DimScene> dimScenes) {
        this.dimScenes = dimScenes;
    }

    public String getSceneIds() {
        return this.sceneIds;
    }

    public void setSceneIds(String sceneIds) {
        this.sceneIds = sceneIds;
    }
}
