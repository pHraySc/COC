package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiApproveStatusHDao;
import com.ailk.biapp.ci.dao.ICiLabelExtInfoHDao;
import com.ailk.biapp.ci.dao.ICiLabelInfoHDao;
import com.ailk.biapp.ci.dao.ICiMdaColumnHDao;
import com.ailk.biapp.ci.dao.ICiMdaTableHDao;
import com.ailk.biapp.ci.dao.ILabelDataTransferJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiApproveStatus;
import com.ailk.biapp.ci.entity.CiLabelExtInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiLabelSceneRel;
import com.ailk.biapp.ci.entity.CiLabelSceneRelId;
import com.ailk.biapp.ci.entity.CiLabelVerticalColumnRel;
import com.ailk.biapp.ci.entity.CiLabelVerticalColumnRelId;
import com.ailk.biapp.ci.entity.CiMdaSysTable;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.entity.DimCocLabelColumnInfo;
import com.ailk.biapp.ci.model.DimCocLabelInfo;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.util.DateUtil;
import com.asiainfo.biframe.dimtable.model.DimTableDefine;
import com.asiainfo.biframe.dimtable.service.impl.DimTableServiceImpl;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LabelDataTransferJDaoImpl extends JdbcBaseDao implements ILabelDataTransferJDao {
    private Logger log = Logger.getLogger(LabelDataTransferJDaoImpl.class);
    private static final String TABLE_DIM_COC_LABEL_INFO = "DIM_COC_LABEL_INFO";
    private static final String TABLE_DIM_COC_LABEL_MODEL_TABLE = "DIM_COC_LABEL_MODEL_TABLE";
    private static final String TABLE_DIM_COC_LABEL_TABLE = "DIM_COC_LABEL_TABLE";
    private static final String TABLE_DIM_COC_LABEL_COLUMN_INFO = "DIM_COC_LABEL_COLUMN_INFO";
    private static final String TABLE_DIM_LABEL_SCENE_REL = "DIM_LABEL_SCENE_REL";
    private static final String TABLE_CI_LABEL_INFO = "CI_LABEL_INFO";
    private static final String TABLE_CI_LABEL_EXT_INFO = "CI_LABEL_EXT_INFO";
    private static final String TABLE_MDA_COLUMN = "CI_MDA_SYS_TABLE_COLUMN";
    private static final String TABLE_MDA_TABLE = "CI_MDA_SYS_TABLE";
    private static String APPROVE_STATUS = "";
    @Autowired
    private ICiLabelInfoHDao ciLabelInfoHDao;
    @Autowired
    private ICiLabelExtInfoHDao ciLabelExtInfoHDao;
    @Autowired
    private ICiMdaTableHDao ciMdaTableHDao;
    @Autowired
    private ICiMdaColumnHDao ciMdaColumnHDao;
    @Autowired
    private ICiApproveStatusHDao ciApproveStatusHDao;
    @Autowired
    private DimTableServiceImpl dimTableService;
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;

    public LabelDataTransferJDaoImpl() {
    }

    private SimpleJdbcTemplate getJdbcTemplate() {
        return this.getSimpleJdbcTemplate();
    }

    public String[] importLabelInfo() throws Exception {
        HashMap labelIds = new HashMap();
        this.initFirstLevelLabel(labelIds);
        this.initSecondLevelLabel(labelIds);
        int labelCategoryType = Integer.valueOf(Configure.getInstance().getProperty("LABEL_CATEGORY_TYPE")).intValue();
        if(labelCategoryType == 3) {
            this.initThirdLevelLabel(labelIds);
        }

        String[] message = this.initOtherlevelLabel(labelIds);
        return message;
    }

    private String[] initOtherlevelLabel(Map<String, Integer> labelIds) throws Exception {
        String[] message = new String[]{"1", "导入成功"};
        String dbType = Configure.getInstance().getProperty("CI_DBTYPE");
        List list = null;
        HashSet labelCodeSet = new HashSet();
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from ").append("DIM_COC_LABEL_INFO").append(" order by label_code");
        list = this.getJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(DimCocLabelInfo.class), new Object[0]);
        StringBuffer attrSql = new StringBuffer();
        attrSql.append(" SELECT LABEL_TYPE, LABEL_DIM FROM ").append("DIM_COC_LABEL_MODEL_TABLE").append(" WHERE LABEL_CODE = :labelCode ");
        List labelTypeList = null;
        Iterator labelIdStr = list.iterator();

        while(labelIdStr.hasNext()) {
            DimCocLabelInfo labelType = (DimCocLabelInfo)labelIdStr.next();
            labelCodeSet.add(labelType.getLabelCode());
        }

        String[] labelIdStr1 = new String[0];
        long labelType1 = 1L;
        String labelDim = "";
        HashMap param = new HashMap();
        Iterator i$ = list.iterator();

        while(i$.hasNext()) {
            DimCocLabelInfo dimCocLabelInfo = (DimCocLabelInfo)i$.next();

            try {
                labelIdStr1 = dimCocLabelInfo.getLabelCode().split("_");
                param.put("labelCode", dimCocLabelInfo.getLabelCode());
                labelTypeList = this.getJdbcTemplate().queryForList(attrSql.toString(), param);
                if(labelTypeList != null && labelTypeList.size() > 0) {
                    if("ORACLE".equalsIgnoreCase(dbType)) {
                        BigDecimal e = (BigDecimal)((Map)labelTypeList.get(0)).get("LABEL_TYPE");
                        labelType1 = (long)e.intValue();
                    } else {
                        labelType1 = (long)((Integer)((Map)labelTypeList.get(0)).get("LABEL_TYPE")).intValue();
                    }

                    labelDim = (String)((Map)labelTypeList.get(0)).get("LABEL_DIM");
                }

                if(3L == labelType1) {
                    this.insertAttrLabelInfo(dimCocLabelInfo, labelIds, labelCodeSet, Integer.valueOf(labelIdStr1.length), labelDim);
                } else if(!labelIds.containsKey(dimCocLabelInfo.getLabelCode())) {
                    this.insertOtherLevelLabelInfo(dimCocLabelInfo, labelIds, labelCodeSet, labelIdStr1);
                }
            } catch (Exception var17) {
                this.log.error("初始化标签失败" + var17.getMessage(), var17);
                var17.printStackTrace();
                message[0] = "0";
                message[1] = var17.getMessage();
            }
        }

        return message;
    }

    private void insertAttrParentLabel(DimCocLabelInfo dimCocLabelInfo, Map<String, Integer> labelIds, Set<String> labelCodeSet, Integer labelLevel) {
        String dbType = Configure.getInstance().getProperty("CI_DBTYPE");
        CiLabelInfo ciLabelInfo = new CiLabelInfo();
        String parentCode = "";
        String labelCode = dimCocLabelInfo.getLabelCode();
        if(labelLevel.intValue() == 3) {
            ciLabelInfo.setLabelName(null == dimCocLabelInfo.getThirdClassDesc()?"":dimCocLabelInfo.getThirdClassDesc().trim().replace("\r", "").replace("\n", ""));
            parentCode = dimCocLabelInfo.getFirstClassCode() + "_" + dimCocLabelInfo.getSecondClassCode();
        }

        if(labelLevel.intValue() == 4) {
            ciLabelInfo.setLabelName(null == dimCocLabelInfo.getFourthClassDesc()?"":dimCocLabelInfo.getFourthClassDesc().trim().replace("\r", "").replace("\n", ""));
            parentCode = dimCocLabelInfo.getFirstClassCode() + "_" + dimCocLabelInfo.getSecondClassCode() + "_" + dimCocLabelInfo.getThirdClassCode();
        }

        ciLabelInfo.setCreateTime(dimCocLabelInfo.getEffectiveTime());
        ciLabelInfo.setEffecTime(dimCocLabelInfo.getEffectiveTime());
        ciLabelInfo.setPublishTime(dimCocLabelInfo.getEffectiveTime());
        ciLabelInfo.setFailTime(dimCocLabelInfo.getInvalidTime());
        ciLabelInfo.setLabelTypeId(Integer.valueOf(3));
        ciLabelInfo.setParentId((Integer)labelIds.get(parentCode));
        if(null == ciLabelInfo.getParentId()) {
            ciLabelInfo.setDataStatusId(Integer.valueOf(1));
        } else {
            ciLabelInfo.setDataStatusId(Integer.valueOf(2));
        }

        ciLabelInfo.setBusiLegend(dimCocLabelInfo.getBusiDesc());
        ciLabelInfo.setApplySuggest(dimCocLabelInfo.getAppSuggest());
        ciLabelInfo.setBusiCaliber(dimCocLabelInfo.getBusiRulesDesc());
        ciLabelInfo.setCreateUserId(dimCocLabelInfo.getCreator());
        ciLabelInfo.setPublishUserId(dimCocLabelInfo.getCreator());
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT T2.LABEL_TABLE_NAME TABLE_NAME,T2.TABLE_DATA_CYCLE DATA_CYCLE FROM ").append("DIM_COC_LABEL_MODEL_TABLE").append(" T1 LEFT JOIN ").append("DIM_COC_LABEL_TABLE").append(" T2 ON (T1.TARGET_TABLE_CODE = T2.TARGET_TABLE_CODE) ").append(" WHERE 1=1 ").append(" AND T1.LABEL_CODE = \'").append(labelCode).append("\' ");
        List tableNameList = this.getJdbcTemplate().queryForList(sql.toString(), new Object[0]);
        String tableName = "";
        long dataCycle = 1L;
        if(tableNameList != null && tableNameList.size() > 0) {
            tableName = (String)((Map)tableNameList.get(0)).get("TABLE_NAME");
            if("ORACLE".equalsIgnoreCase(dbType)) {
                BigDecimal ciLabelExtInfo = (BigDecimal)((Map)tableNameList.get(0)).get("DATA_CYCLE");
                dataCycle = (long)ciLabelExtInfo.intValue();
            } else {
                dataCycle = (long)((Integer)((Map)tableNameList.get(0)).get("DATA_CYCLE")).intValue();
            }

            if(tableName.endsWith("_")) {
                tableName = tableName.substring(0, tableName.length() - 1);
            }
        } else {
            tableName = "no_table";
            dataCycle = 1L;
        }

        if(dataCycle == 1L) {
            ciLabelInfo.setUpdateCycle(Integer.valueOf(2));
        } else {
            ciLabelInfo.setUpdateCycle(Integer.valueOf(1));
        }

        this.ciLabelInfoHDao.insertCiLabelInfo(ciLabelInfo);
        ciLabelInfo.setSortNum(ciLabelInfo.getLabelId());
        this.ciLabelInfoHDao.updateCiLabelInfo(ciLabelInfo);
        CiLabelExtInfo ciLabelExtInfo1 = new CiLabelExtInfo();
        ciLabelExtInfo1.setLabelId(ciLabelInfo.getLabelId());
        ciLabelExtInfo1.setIsLeaf(Integer.valueOf(0));
        ciLabelExtInfo1.setIsStatUserNum(Integer.valueOf(0));
        ciLabelExtInfo1.setLabelLevel(labelLevel);
        CiMdaSysTable table = new CiMdaSysTable();
        table.setTableName(tableName);
        if(!labelIds.containsKey(tableName)) {
            try {
                this.ciMdaTableHDao.insertTableInfo(table);
            } catch (Exception var19) {
                this.log.error(var19.getMessage());
                var19.printStackTrace();
            }

            labelIds.put(tableName, table.getTableId());
        } else {
            table.setTableId((Integer)labelIds.get(tableName));
        }

        CiMdaSysTableColumn column = new CiMdaSysTableColumn();
        column.setColumnName(labelCode);
        column.setCiMdaSysTable(table);

        try {
            this.ciMdaColumnHDao.insertColumnInfo(column);
        } catch (Exception var18) {
            this.log.error(var18.getMessage());
            var18.printStackTrace();
        }

        ciLabelExtInfo1.setCiMdaSysTableColumn(column);
        ciLabelExtInfo1.setMaxVal(Double.valueOf(1.0D));
        ciLabelExtInfo1.setMinVal(Double.valueOf(0.0D));
        this.ciLabelExtInfoHDao.insertCiLabelExtInfo(ciLabelExtInfo1);
        labelIds.put(labelCode, ciLabelInfo.getLabelId());
        CiApproveStatus ciApproveStatus = this.newCiApproveStatus(ciLabelInfo.getLabelId() + "", ciLabelInfo.getLabelName(), ciLabelInfo.getCreateUserId());
        this.initLabelApproveStatus(ciLabelInfo.getLabelId(), ciApproveStatus);
    }

    private void insertAttrLabelInfo(DimCocLabelInfo dimCocLabelInfo, Map<String, Integer> labelIds, Set<String> labelCodeSet, Integer labelLevel, String labelDim) throws Exception {
        String dbType = Configure.getInstance().getProperty("CI_DBTYPE");
        String province = Configure.getInstance().getProperty("PROVINCE");
        String cityLabelCode = Configure.getInstance().getProperty("CITY_LABEL_CODE");
        String attrLabelNameShow = Configure.getInstance().getProperty("ATTR_LABEL_NAME_SHOW");
        DimTableDefine define = this.dimTableService.findDefineById(labelDim);
        String labelCode = dimCocLabelInfo.getLabelCode();
        List dimValList = this.dimTableService.getAllDimData(define.getDimId(), define.getDimPCodeCol(), (String)null, (String)null, (String)null, (String)null, false);
        HashSet dimValueSet = new HashSet();
        Iterator attrLabelCodeIdList = dimValList.iterator();

        String attrVal;
        while(attrLabelCodeIdList.hasNext()) {
            Map labelSql = (Map)attrLabelCodeIdList.next();
            attrVal = labelSql.get(define.getDimCodeCol()).toString();
            String labelId = labelSql.get(define.getDimValueCol()).toString();
            String iterator2 = "";
            if(StringUtil.isNotEmpty(define.getDimPCodeCol())) {
                iterator2 = labelSql.get(define.getDimPCodeCol()).toString();
            }

            dimValueSet.add(attrVal);
            if(!labelIds.containsKey(labelCode + attrVal)) {
                CiLabelInfo map = new CiLabelInfo();
                map.setLabelName(null == labelId?"":labelId.trim().replace("\r", "").replace("\n", ""));
                map.setCreateTime(dimCocLabelInfo.getEffectiveTime());
                map.setEffecTime(dimCocLabelInfo.getEffectiveTime());
                map.setPublishTime(dimCocLabelInfo.getEffectiveTime());
                map.setFailTime(dimCocLabelInfo.getInvalidTime());
                map.setLabelTypeId(Integer.valueOf(3));
                String ciLabelInfo = "";
                if("BEIJING".equalsIgnoreCase(province)) {
                    if(labelLevel.intValue() == 4) {
                        ciLabelInfo = cityLabelCode;
                    }

                    if(labelLevel.intValue() == 5) {
                        ciLabelInfo = dimCocLabelInfo.getFirstClassCode() + "_" + dimCocLabelInfo.getSecondClassCode() + "_" + dimCocLabelInfo.getThirdClassCode() + "_" + dimCocLabelInfo.getFourthClassCode();
                    }

                    map.setParentId((Integer)labelIds.get(ciLabelInfo + iterator2));
                } else {
                    if("true".equalsIgnoreCase(attrLabelNameShow)) {
                        if(labelLevel.intValue() < 5) {
                            ciLabelInfo = labelCode;
                        } else {
                            ciLabelInfo = dimCocLabelInfo.getFirstClassCode() + "_" + dimCocLabelInfo.getSecondClassCode() + "_" + dimCocLabelInfo.getThirdClassCode() + "_" + dimCocLabelInfo.getFourthClassCode();
                        }
                    } else {
                        if(labelLevel.intValue() == 3) {
                            ciLabelInfo = dimCocLabelInfo.getFirstClassCode() + "_" + dimCocLabelInfo.getSecondClassCode();
                        }

                        if(labelLevel.intValue() == 4) {
                            ciLabelInfo = dimCocLabelInfo.getFirstClassCode() + "_" + dimCocLabelInfo.getSecondClassCode() + "_" + dimCocLabelInfo.getThirdClassCode();
                        }

                        if(labelLevel.intValue() == 5) {
                            ciLabelInfo = dimCocLabelInfo.getFirstClassCode() + "_" + dimCocLabelInfo.getSecondClassCode() + "_" + dimCocLabelInfo.getThirdClassCode() + "_" + dimCocLabelInfo.getFourthClassCode();
                        }
                    }

                    if("".equals(iterator2)) {
                        if("true".equalsIgnoreCase(attrLabelNameShow) && !labelIds.containsKey(ciLabelInfo) && labelLevel.intValue() < 5) {
                            this.insertAttrParentLabel(dimCocLabelInfo, labelIds, labelCodeSet, labelLevel);
                        }

                        map.setParentId((Integer)labelIds.get(ciLabelInfo));
                    } else if(!labelIds.containsKey(ciLabelInfo + iterator2)) {
                        map.setParentId((Integer)labelIds.get(ciLabelInfo));
                    } else {
                        map.setParentId((Integer)labelIds.get(ciLabelInfo + iterator2));
                    }
                }

                if(null == map.getParentId()) {
                    map.setDataStatusId(Integer.valueOf(1));
                    this.log.error(map.getLabelName() + "标签的父ID不存在");
                    throw new Exception(map.getLabelName() + "标签的父ID不存在");
                }

                map.setDataStatusId(Integer.valueOf(2));
                map.setBusiLegend(dimCocLabelInfo.getBusiDesc());
                map.setApplySuggest(dimCocLabelInfo.getAppSuggest());
                map.setBusiCaliber(dimCocLabelInfo.getBusiRulesDesc());
                map.setCreateUserId(dimCocLabelInfo.getCreator());
                map.setPublishUserId(dimCocLabelInfo.getCreator());
                StringBuffer sql = new StringBuffer();
                sql.append(" SELECT T2.LABEL_TABLE_NAME TABLE_NAME,T2.TABLE_DATA_CYCLE DATA_CYCLE,T1.DATA_TYPE FROM ").append("DIM_COC_LABEL_MODEL_TABLE").append(" T1 LEFT JOIN ").append("DIM_COC_LABEL_TABLE").append(" T2 ON (T1.TARGET_TABLE_CODE = T2.TARGET_TABLE_CODE) ").append(" WHERE 1=1 ").append(" AND T1.LABEL_CODE = \'").append(labelCode).append("\' ");
                List tableNameList = this.getJdbcTemplate().queryForList(sql.toString(), new Object[0]);
                String tableName = "";
                String dataType = "";
                long dataCycle = 1L;
                if(tableNameList != null && tableNameList.size() > 0) {
                    tableName = (String)((Map)tableNameList.get(0)).get("TABLE_NAME");
                    dataType = (String)((Map)tableNameList.get(0)).get("DATA_TYPE");
                    if("ORACLE".equalsIgnoreCase(dbType)) {
                        BigDecimal ciLabelExtInfo = (BigDecimal)((Map)tableNameList.get(0)).get("DATA_CYCLE");
                        dataCycle = (long)ciLabelExtInfo.intValue();
                    } else {
                        dataCycle = (long)((Integer)((Map)tableNameList.get(0)).get("DATA_CYCLE")).intValue();
                    }

                    if(tableName.endsWith("_")) {
                        tableName = tableName.substring(0, tableName.length() - 1);
                    }
                } else {
                    tableName = "no_table";
                    dataCycle = 1L;
                    dataType = "INTEGER";
                    this.log.error(labelCode + "对应的表名不存在");
                }

                if(dataCycle == 1L) {
                    map.setUpdateCycle(Integer.valueOf(2));
                } else {
                    map.setUpdateCycle(Integer.valueOf(1));
                }

                this.ciLabelInfoHDao.insertCiLabelInfo(map);
                map.setSortNum(map.getLabelId());
                this.ciLabelInfoHDao.updateCiLabelInfo(map);
                CiLabelExtInfo ciLabelExtInfo1 = new CiLabelExtInfo();
                ciLabelExtInfo1.setLabelId(map.getLabelId());
                ciLabelExtInfo1.setAttrVal(attrVal);
                ciLabelExtInfo1.setIsLeaf(Integer.valueOf(1));
                ciLabelExtInfo1.setIsStatUserNum(Integer.valueOf(1));
                ciLabelExtInfo1.setLabelLevel(labelLevel);
                CiMdaSysTable table = new CiMdaSysTable();
                table.setTableName(tableName);
                if(!labelIds.containsKey(tableName)) {
                    try {
                        this.ciMdaTableHDao.insertTableInfo(table);
                    } catch (Exception var32) {
                        this.log.error(var32.getMessage());
                        var32.printStackTrace();
                    }

                    labelIds.put(tableName, table.getTableId());
                } else {
                    table.setTableId((Integer)labelIds.get(tableName));
                }

                CiMdaSysTableColumn column = new CiMdaSysTableColumn();
                column.setColumnName(labelCode);
                column.setColumnCnName(map.getLabelName());
                column.setDimTransId(labelDim);
                column.setCiMdaSysTable(table);
                dataType = dataType.toUpperCase();
                if(dataType.contains("VARCHAR")) {
                    column.setColumnDataTypeId(Integer.valueOf(2));
                } else {
                    column.setColumnDataTypeId(Integer.valueOf(1));
                }

                try {
                    this.ciMdaColumnHDao.insertColumnInfo(column);
                } catch (Exception var31) {
                    this.log.error(var31.getMessage());
                    var31.printStackTrace();
                }

                ciLabelExtInfo1.setCiMdaSysTableColumn(column);
                ciLabelExtInfo1.setMaxVal(Double.valueOf(1.0D));
                ciLabelExtInfo1.setMinVal(Double.valueOf(0.0D));
                this.ciLabelExtInfoHDao.insertCiLabelExtInfo(ciLabelExtInfo1);
                labelIds.put(labelCode + attrVal, map.getLabelId());
                CiApproveStatus ciApproveStatus = this.newCiApproveStatus(map.getLabelId() + "", map.getLabelName(), map.getCreateUserId());
                this.initLabelApproveStatus(map.getLabelId(), ciApproveStatus);
            }
        }

        attrLabelCodeIdList = null;
        StringBuffer labelSql1 = new StringBuffer();
        labelSql1.append(" SELECT T3.COLUMN_NAME AS LABEL_CODE, T1.LABEL_ID, T2.ATTR_VAL FROM ").append("CI_LABEL_INFO").append("  T1 LEFT JOIN ").append("CI_LABEL_EXT_INFO").append(" T2 ON(T1.LABEL_ID = T2.LABEL_ID) ").append(" LEFT JOIN ").append("CI_MDA_SYS_TABLE_COLUMN").append(" T3 ON(T2.COLUMN_ID = T3.COLUMN_ID) WHERE T3.COLUMN_NAME = \'").append(labelCode).append("\'").append(" AND T2.ATTR_VAL IS NOT NULL");
        List attrLabelCodeIdList1 = this.getJdbcTemplate().queryForList(labelSql1.toString(), new Object[0]);
        attrVal = "";
        Integer labelId1 = Integer.valueOf(0);
        if(null != attrLabelCodeIdList1 && attrLabelCodeIdList1.size() > 0) {
            Iterator iterator21 = attrLabelCodeIdList1.iterator();

            while(iterator21.hasNext()) {
                Map map1 = (Map)iterator21.next();
                attrVal = (String)map1.get("ATTR_VAL");
                if("ORACLE".equalsIgnoreCase(dbType)) {
                    labelId1 = Integer.valueOf(((BigDecimal)map1.get("LABEL_ID")).intValue());
                } else {
                    labelId1 = (Integer)map1.get("LABEL_ID");
                }

                CiLabelInfo ciLabelInfo1 = this.ciLabelInfoHDao.selectCiLabelInfoById(labelId1);
                if(dimValueSet.contains(attrVal)) {
                    ciLabelInfo1.setDataStatusId(Integer.valueOf(2));
                    this.ciLabelInfoHDao.updateCiLabelInfo(ciLabelInfo1);
                } else {
                    ciLabelInfo1.setDataStatusId(Integer.valueOf(6));
                    this.ciLabelInfoHDao.updateCiLabelInfo(ciLabelInfo1);
                }
            }
        }

    }

    private void insertOtherLevelLabelInfo(DimCocLabelInfo dimCocLabelInfo, Map<String, Integer> labelIds, Set<String> labelCodeSet, String[] labelIdStr) throws Exception {
        String labelCode = "";
        int labelLevel = labelIdStr.length;

        for(int parentCode = 0; parentCode < labelLevel; ++parentCode) {
            labelCode = labelCode + labelIdStr[parentCode];
            if(parentCode != labelLevel - 1) {
                labelCode = labelCode + "_";
            }
        }

        String var41 = "ROOT";
        if(labelCode.contains("_")) {
            var41 = labelCode.substring(0, labelCode.lastIndexOf("_"));
        }

        if(!labelIds.containsKey(var41)) {
            this.insertOtherLevelLabelInfo(dimCocLabelInfo, labelIds, labelCodeSet, var41.split("_"));
        }

        String dbType = Configure.getInstance().getProperty("CI_DBTYPE");
        CiLabelInfo ciLabelInfo = new CiLabelInfo();
        String labelName = "";
        switch(labelLevel) {
        case 1:
            labelName = dimCocLabelInfo.getFirstClassDesc();
            break;
        case 2:
            labelName = dimCocLabelInfo.getSecondClassDesc();
            break;
        case 3:
            labelName = dimCocLabelInfo.getThirdClassDesc();
            break;
        case 4:
            labelName = dimCocLabelInfo.getFourthClassDesc();
            break;
        case 5:
            labelName = dimCocLabelInfo.getFifthClassDesc();
            break;
        case 6:
            labelName = dimCocLabelInfo.getSixthClassDesc();
        }

        ciLabelInfo.setLabelName(null == labelName?"":labelName.trim().replace("\r", "").replace("\n", ""));
        ciLabelInfo.setCreateTime(dimCocLabelInfo.getEffectiveTime());
        ciLabelInfo.setEffecTime(dimCocLabelInfo.getEffectiveTime());
        ciLabelInfo.setPublishTime(dimCocLabelInfo.getEffectiveTime());
        ciLabelInfo.setFailTime(dimCocLabelInfo.getInvalidTime());
        ciLabelInfo.setIsSysRecom(Integer.valueOf(0));
        ciLabelInfo.setParentId((Integer)labelIds.get(var41));
        if(null == ciLabelInfo.getParentId()) {
            ciLabelInfo.setDataStatusId(Integer.valueOf(1));
            this.log.error(var41 + "标签不存在");
            throw new Exception(var41 + "标签不存在");
        } else {
            ciLabelInfo.setDataStatusId(Integer.valueOf(2));
            ciLabelInfo.setBusiLegend(null == dimCocLabelInfo.getBusiDesc()?"":dimCocLabelInfo.getBusiDesc().trim().replace("\r", "").replace("\n", ""));
            ciLabelInfo.setApplySuggest(null == dimCocLabelInfo.getAppSuggest()?"":dimCocLabelInfo.getAppSuggest().trim().replace("\r", "").replace("\n", ""));
            ciLabelInfo.setBusiCaliber(null == dimCocLabelInfo.getBusiRulesDesc()?"":dimCocLabelInfo.getBusiRulesDesc().trim().replace("\r", "").replace("\n", ""));
            ciLabelInfo.setCreateUserId(dimCocLabelInfo.getCreator());
            ciLabelInfo.setPublishUserId(dimCocLabelInfo.getCreator());
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT T2.LABEL_TABLE_NAME TABLE_NAME,T2.TABLE_DATA_CYCLE DATA_CYCLE, T2.TARGET_TABLE_TYPE TABLE_TYPE,T1.DATA_TYPE,T2.TARGET_TABLE_CODE, T1.LABEL_TYPE, T1.LABEL_DIM,T1.UNIT,T1.COUNT_RULES_CODE,T1.IS_NEED_AUTHORITY FROM ").append("DIM_COC_LABEL_MODEL_TABLE").append(" T1 LEFT JOIN ").append("DIM_COC_LABEL_TABLE").append(" T2 ON (T1.TARGET_TABLE_CODE = T2.TARGET_TABLE_CODE) ").append(" WHERE 1=1 ").append(" AND T1.LABEL_CODE = \'").append(labelCode).append("\' ");
            List tableNameList = this.getJdbcTemplate().queryForList(sql.toString(), new Object[0]);
            String tableName = "";
            String labelDim = "";
            String dataType = "";
            String unit = "";
            String countRulesCode = "";
            String targetTableCode = "";
            long dataCycle = 1L;
            boolean isStatUserNum = false;
            Integer labelType = Integer.valueOf(0);
            Integer tableType = Integer.valueOf(1);
            Integer isNeedAuthority = Integer.valueOf(0);
            BigDecimal sceneRelList;
            byte var42;
            if(tableNameList != null && tableNameList.size() > 0) {
                tableName = (String)((Map)tableNameList.get(0)).get("TABLE_NAME");
                labelDim = (String)((Map)tableNameList.get(0)).get("LABEL_DIM");
                dataType = (String)((Map)tableNameList.get(0)).get("DATA_TYPE");
                unit = (String)((Map)tableNameList.get(0)).get("UNIT");
                countRulesCode = (String)((Map)tableNameList.get(0)).get("COUNT_RULES_CODE");
                targetTableCode = (String)((Map)tableNameList.get(0)).get("TARGET_TABLE_CODE");
                if("ORACLE".equalsIgnoreCase(dbType)) {
                    BigDecimal ciLabelExtInfo = (BigDecimal)((Map)tableNameList.get(0)).get("DATA_CYCLE");
                    dataCycle = (long)ciLabelExtInfo.intValue();
                    BigDecimal table = (BigDecimal)((Map)tableNameList.get(0)).get("LABEL_TYPE");
                    labelType = Integer.valueOf(table.intValue());
                    BigDecimal column = (BigDecimal)((Map)tableNameList.get(0)).get("TABLE_TYPE");
                    tableType = Integer.valueOf(column.intValue());
                    sceneRelList = (BigDecimal)((Map)tableNameList.get(0)).get("IS_NEED_AUTHORITY");
                    isNeedAuthority = Integer.valueOf(sceneRelList.intValue());
                } else {
                    dataCycle = (long)((Integer)((Map)tableNameList.get(0)).get("DATA_CYCLE")).intValue();
                    labelType = (Integer)((Map)tableNameList.get(0)).get("LABEL_TYPE");
                    tableType = (Integer)((Map)tableNameList.get(0)).get("TABLE_TYPE");
                    isNeedAuthority = (Integer)((Map)tableNameList.get(0)).get("IS_NEED_AUTHORITY");
                }

                if(tableName.endsWith("_")) {
                    tableName = tableName.substring(0, tableName.length() - 1);
                }

                var42 = 1;
            } else {
                dataCycle = 1L;
                var42 = 0;
                if(labelLevel <= 3) {
                    tableName = "first_second_label_no_table";
                } else {
                    tableName = "no_table";
                    this.log.error(labelCode + "对应的表名不存在");
                }
            }

            ciLabelInfo.setUpdateCycle(Integer.valueOf((int)dataCycle));
            ciLabelInfo.setLabelTypeId(labelType);
            this.ciLabelInfoHDao.insertCiLabelInfo(ciLabelInfo);
            ciLabelInfo.setLabelIdLevelDesc((String)null);
            this.ciLabelInfoService.saveLabelLevel(ciLabelInfo.getParentId(), ciLabelInfo);
            ciLabelInfo.setSortNum(ciLabelInfo.getLabelId());
            this.ciLabelInfoHDao.updateCiLabelInfo(ciLabelInfo);
            CiLabelExtInfo var43 = new CiLabelExtInfo();
            var43.setLabelId(ciLabelInfo.getLabelId());
            if(labelLevel > 3 && labelCodeSet.contains(labelCode)) {
                var43.setIsLeaf(Integer.valueOf(1));
            } else {
                var43.setIsLeaf(Integer.valueOf(0));
            }

            var43.setIsStatUserNum(Integer.valueOf(var42));
            var43.setLabelLevel(Integer.valueOf(labelLevel));
            var43.setCountRulesCode(countRulesCode);
            CiMdaSysTable var44 = new CiMdaSysTable();
            var44.setTableName(tableName);
            var44.setTableType(tableType);
            var44.setUpdateCycle(Integer.valueOf((int)dataCycle));
            var44.setTargetTableCode(targetTableCode);
            if(!labelIds.containsKey(tableName + dataCycle)) {
                try {
                    this.ciMdaTableHDao.insertTableInfo(var44);
                } catch (Exception var40) {
                    this.log.error(tableName + "插入元数据表报错" + var40.getMessage());
                    throw new Exception("插入元数据表报错" + tableName);
                }

                labelIds.put(tableName + dataCycle, var44.getTableId());
            } else {
                var44.setTableId((Integer)labelIds.get(tableName + dataCycle));
            }

            CiMdaSysTableColumn var45 = new CiMdaSysTableColumn();
            var45.setColumnName(labelCode);
            var45.setColumnCnName(ciLabelInfo.getLabelName());
            var45.setCiMdaSysTable(var44);
            var45.setDimTransId(labelDim);
            var45.setUnit(unit);
            var45.setIsNeedAuthority(isNeedAuthority);
            dataType = dataType.toUpperCase();
            if(dataType.contains("VARCHAR")) {
                var45.setColumnDataTypeId(Integer.valueOf(2));
            } else {
                var45.setColumnDataTypeId(Integer.valueOf(1));
            }

            var45.setDataType(dataType);

            try {
                this.ciMdaColumnHDao.insertColumnInfo(var45);
            } catch (Exception var39) {
                this.log.error(labelCode + "插入元数据列报错" + var39.getMessage());
                throw new Exception(labelCode + "插入元数据列报错");
            }

            var43.setCiMdaSysTableColumn(var45);
            List var46;
            if(8 == labelType.intValue()) {
                sceneRelList = null;
                StringBuffer ciApproveStatus = new StringBuffer();
                ciApproveStatus.append("SELECT * FROM ").append("DIM_COC_LABEL_COLUMN_INFO").append(" WHERE LABEL_CODE = \'").append(labelCode).append("\'");
                var46 = this.getJdbcTemplate().query(ciApproveStatus.toString(), ParameterizedBeanPropertyRowMapper.newInstance(DimCocLabelColumnInfo.class), new Object[0]);
                HashSet i$ = new HashSet();
                String item = "";
                if(null != var46 && var46.size() > 0) {
                    Iterator rel = var46.iterator();

                    while(rel.hasNext()) {
                        DimCocLabelColumnInfo id = (DimCocLabelColumnInfo)rel.next();
                        item = id.getColumnDataType();
                        CiLabelVerticalColumnRel columnRel = new CiLabelVerticalColumnRel();
                        CiLabelVerticalColumnRelId coumnRelId = new CiLabelVerticalColumnRelId();
                        CiMdaSysTableColumn vertColumn = new CiMdaSysTableColumn();
                        vertColumn.setUnit(id.getUnit());
                        vertColumn.setColumnName(id.getColumnName());
                        vertColumn.setColumnCnName(id.getColumnCnName());
                        vertColumn.setCiMdaSysTable(var44);
                        vertColumn.setDimTransId(id.getDimId());
                        vertColumn.setIsNeedAuthority(id.getIsNeedAuthority());
                        item = item.toUpperCase();
                        if(item.contains("VARCHAR")) {
                            vertColumn.setColumnDataTypeId(Integer.valueOf(2));
                        } else {
                            vertColumn.setColumnDataTypeId(Integer.valueOf(1));
                        }

                        vertColumn.setDataType(item);

                        try {
                            this.ciMdaColumnHDao.insertColumnInfo(vertColumn);
                        } catch (Exception var38) {
                            this.log.error(id.getColumnName() + "插入元数据列报错" + var38.getMessage());
                            throw new Exception(id.getColumnName() + "插入元数据列报错");
                        }

                        coumnRelId.setLabelId(ciLabelInfo.getLabelId());
                        coumnRelId.setColumnId(vertColumn.getColumnId());
                        columnRel.setCiMdaSysTableColumn(vertColumn);
                        columnRel.setId(coumnRelId);
                        columnRel.setIsMustColumn(id.getIsMust());
                        columnRel.setLabelTypeId(id.getColumnOpType());
                        columnRel.setSortNum(id.getSortNum());
                        i$.add(columnRel);
                    }
                }

                var43.setCiLabelVerticalColumnRels(i$);
            }

            var46 = this.selectCiLabelSceneRelByLabelCode(labelCode);
            if(var46 != null && var46.size() > 0) {
                HashSet var47 = new HashSet();
                Iterator var49 = var46.iterator();

                while(var49.hasNext()) {
                    Map var50 = (Map)var49.next();
                    CiLabelSceneRel var51 = new CiLabelSceneRel();
                    CiLabelSceneRelId var52 = new CiLabelSceneRelId();
                    var52.setLabelId(ciLabelInfo.getLabelId());
                    var52.setSceneId((String)var50.get("SCENE_ID"));
                    var51.setId(var52);
                    var47.add(var51);
                }

                var43.setCiLabelSceneRels(var47);
            }

            var43.setMaxVal(Double.valueOf(1.0D));
            var43.setMinVal(Double.valueOf(0.0D));
            this.ciLabelExtInfoHDao.insertCiLabelExtInfo(var43);
            labelIds.put(labelCode, ciLabelInfo.getLabelId());
            CiApproveStatus var48 = this.newCiApproveStatus(ciLabelInfo.getLabelId() + "", ciLabelInfo.getLabelName(), ciLabelInfo.getCreateUserId());
            this.initLabelApproveStatus(ciLabelInfo.getLabelId(), var48);
        }
    }

    private List<Map<String, Object>> selectCiLabelSceneRelByLabelCode(String labelCode) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT LABEL_CODE,SCENE_ID FROM ").append("DIM_LABEL_SCENE_REL").append(" WHERE LABEL_CODE=?");
        this.log.debug(sql);
        return this.getSimpleJdbcTemplate().queryForList(sql.toString(), new Object[]{labelCode});
    }

    /** @deprecated */
    @Deprecated
    private void insertLevel4LabelInfo(DimCocLabelInfo dimCocLabelInfo, Map<String, Integer> labelIds, Set<String> labelCodeSet) throws Exception {
        String dbType = Configure.getInstance().getProperty("CI_DBTYPE");
        CiLabelInfo ciLabelInfo = new CiLabelInfo();
        ciLabelInfo.setLabelName(null == dimCocLabelInfo.getThirdClassDesc()?"":dimCocLabelInfo.getThirdClassDesc().trim().replace("\r", "").replace("\n", ""));
        ciLabelInfo.setCreateTime(dimCocLabelInfo.getEffectiveTime());
        ciLabelInfo.setEffecTime(dimCocLabelInfo.getEffectiveTime());
        ciLabelInfo.setPublishTime(dimCocLabelInfo.getEffectiveTime());
        ciLabelInfo.setFailTime(dimCocLabelInfo.getInvalidTime());
        ciLabelInfo.setLabelTypeId(Integer.valueOf(1));
        ciLabelInfo.setParentId((Integer)labelIds.get(dimCocLabelInfo.getFirstClassCode() + "_" + dimCocLabelInfo.getSecondClassCode()));
        if(null == ciLabelInfo.getParentId()) {
            ciLabelInfo.setDataStatusId(Integer.valueOf(1));
        } else {
            ciLabelInfo.setDataStatusId(Integer.valueOf(2));
        }

        ciLabelInfo.setBusiLegend(dimCocLabelInfo.getBusiDesc());
        ciLabelInfo.setApplySuggest(dimCocLabelInfo.getAppSuggest());
        ciLabelInfo.setBusiCaliber(dimCocLabelInfo.getBusiRulesDesc());
        ciLabelInfo.setCreateUserId(dimCocLabelInfo.getCreator());
        ciLabelInfo.setPublishUserId(dimCocLabelInfo.getCreator());
        String labelCode = dimCocLabelInfo.getFirstClassCode() + "_" + dimCocLabelInfo.getSecondClassCode() + "_" + dimCocLabelInfo.getThirdClassCode();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT T2.LABEL_TABLE_NAME TABLE_NAME,T2.TABLE_DATA_CYCLE DATA_CYCLE,T1.DATA_TYPE FROM ").append("DIM_COC_LABEL_MODEL_TABLE").append(" T1 LEFT JOIN ").append("DIM_COC_LABEL_TABLE").append(" T2 ON (T1.TARGET_TABLE_CODE = T2.TARGET_TABLE_CODE) ").append(" WHERE 1=1 ").append(" AND T1.LABEL_CODE = \'").append(labelCode).append("\' ");
        List tableNameList = this.getJdbcTemplate().queryForList(sql.toString(), new Object[0]);
        String tableName = "";
        long dataCycle = 1L;
        boolean isStatUserNum = false;
        if(tableNameList != null && tableNameList.size() > 0) {
            tableName = (String)((Map)tableNameList.get(0)).get("TABLE_NAME");
            if("ORACLE".equalsIgnoreCase(dbType)) {
                BigDecimal ciLabelExtInfo = (BigDecimal)((Map)tableNameList.get(0)).get("DATA_CYCLE");
                dataCycle = (long)ciLabelExtInfo.intValue();
            } else {
                dataCycle = (long)((Integer)((Map)tableNameList.get(0)).get("DATA_CYCLE")).intValue();
            }

            if(tableName.endsWith("_")) {
                tableName = tableName.substring(0, tableName.length() - 1);
            }

            byte isStatUserNum1 = 1;
            if(dataCycle == 1L) {
                ciLabelInfo.setUpdateCycle(Integer.valueOf(2));
            } else {
                ciLabelInfo.setUpdateCycle(Integer.valueOf(1));
            }

            this.ciLabelInfoHDao.insertCiLabelInfo(ciLabelInfo);
            ciLabelInfo.setSortNum(ciLabelInfo.getLabelId());
            this.ciLabelInfoHDao.updateCiLabelInfo(ciLabelInfo);
            CiLabelExtInfo ciLabelExtInfo1 = new CiLabelExtInfo();
            ciLabelExtInfo1.setLabelId(ciLabelInfo.getLabelId());
            ciLabelExtInfo1.setIsLeaf(Integer.valueOf(isStatUserNum1));
            ciLabelExtInfo1.setIsStatUserNum(Integer.valueOf(isStatUserNum1));
            ciLabelExtInfo1.setLabelLevel(Integer.valueOf(3));
            CiMdaSysTable table = new CiMdaSysTable();
            table.setTableName(tableName);
            if(!labelIds.containsKey(tableName)) {
                try {
                    this.ciMdaTableHDao.insertTableInfo(table);
                } catch (Exception var18) {
                    var18.printStackTrace();
                }

                labelIds.put(tableName, table.getTableId());
            } else {
                table.setTableId((Integer)labelIds.get(tableName));
            }

            CiMdaSysTableColumn column = new CiMdaSysTableColumn();
            column.setColumnName(labelCode);
            column.setColumnCnName(ciLabelInfo.getLabelName());
            column.setCiMdaSysTable(table);
            column.setColumnDataTypeId(Integer.valueOf(1));

            try {
                this.ciMdaColumnHDao.insertColumnInfo(column);
            } catch (Exception var17) {
                var17.printStackTrace();
            }

            ciLabelExtInfo1.setCiMdaSysTableColumn(column);
            ciLabelExtInfo1.setMaxVal(Double.valueOf(1.0D));
            ciLabelExtInfo1.setMinVal(Double.valueOf(0.0D));
            this.ciLabelExtInfoHDao.insertCiLabelExtInfo(ciLabelExtInfo1);
            labelIds.put(labelCode, ciLabelInfo.getLabelId());
            CiApproveStatus ciApproveStatus = this.newCiApproveStatus(ciLabelInfo.getLabelId() + "", ciLabelInfo.getLabelName(), ciLabelInfo.getCreateUserId());
            this.initLabelApproveStatus(ciLabelInfo.getLabelId(), ciApproveStatus);
        } else {
            tableName = "no_table";
            dataCycle = 1L;
            isStatUserNum = false;
            this.log.error(labelCode + "对应的表名不存在");
            throw new Exception(labelCode + "对应的表名不存在");
        }
    }

    /** @deprecated */
    @Deprecated
    private void insertLevel5LabelInfo(DimCocLabelInfo dimCocLabelInfo, Map<String, Integer> labelIds, Set<String> labelCodeSet) throws Exception {
        String dbType = Configure.getInstance().getProperty("CI_DBTYPE");
        CiLabelInfo ciLabelInfo = new CiLabelInfo();
        ciLabelInfo.setLabelName(null == dimCocLabelInfo.getFourthClassDesc()?"":dimCocLabelInfo.getFourthClassDesc().trim().replace("\r", "").replace("\n", ""));
        ciLabelInfo.setCreateTime(dimCocLabelInfo.getEffectiveTime());
        ciLabelInfo.setEffecTime(dimCocLabelInfo.getEffectiveTime());
        ciLabelInfo.setPublishTime(dimCocLabelInfo.getEffectiveTime());
        ciLabelInfo.setFailTime(dimCocLabelInfo.getInvalidTime());
        ciLabelInfo.setLabelTypeId(Integer.valueOf(1));
        ciLabelInfo.setParentId((Integer)labelIds.get(dimCocLabelInfo.getFirstClassCode() + "_" + dimCocLabelInfo.getSecondClassCode() + "_" + dimCocLabelInfo.getThirdClassCode()));
        if(null == ciLabelInfo.getParentId()) {
            ciLabelInfo.setDataStatusId(Integer.valueOf(1));
        } else {
            ciLabelInfo.setDataStatusId(Integer.valueOf(2));
        }

        ciLabelInfo.setBusiLegend(dimCocLabelInfo.getBusiDesc());
        ciLabelInfo.setApplySuggest(dimCocLabelInfo.getAppSuggest());
        ciLabelInfo.setBusiCaliber(dimCocLabelInfo.getBusiRulesDesc());
        ciLabelInfo.setCreateUserId(dimCocLabelInfo.getCreator());
        ciLabelInfo.setPublishUserId(dimCocLabelInfo.getCreator());
        String labelCode = dimCocLabelInfo.getFirstClassCode() + "_" + dimCocLabelInfo.getSecondClassCode() + "_" + dimCocLabelInfo.getThirdClassCode() + "_" + dimCocLabelInfo.getFourthClassCode();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT T2.LABEL_TABLE_NAME TABLE_NAME,T2.TABLE_DATA_CYCLE DATA_CYCLE FROM ").append("DIM_COC_LABEL_MODEL_TABLE").append(" T1 LEFT JOIN ").append("DIM_COC_LABEL_TABLE").append(" T2 ON (T1.TARGET_TABLE_CODE = T2.TARGET_TABLE_CODE) ").append(" WHERE 1=1 ").append(" AND T1.LABEL_CODE = \'").append(labelCode).append("\' ");
        List tableNameList = this.getJdbcTemplate().queryForList(sql.toString(), new Object[0]);
        String tableName = "";
        long dataCycle = 1L;
        boolean isStatUserNum = false;
        byte isStatUserNum1;
        if(tableNameList != null && tableNameList.size() > 0) {
            tableName = (String)((Map)tableNameList.get(0)).get("TABLE_NAME");
            if("ORACLE".equalsIgnoreCase(dbType)) {
                BigDecimal ciLabelExtInfo = (BigDecimal)((Map)tableNameList.get(0)).get("DATA_CYCLE");
                dataCycle = (long)ciLabelExtInfo.intValue();
            } else {
                dataCycle = (long)((Integer)((Map)tableNameList.get(0)).get("DATA_CYCLE")).intValue();
            }

            if(tableName.endsWith("_")) {
                tableName = tableName.substring(0, tableName.length() - 1);
            }

            isStatUserNum1 = 1;
        } else {
            tableName = "no_table";
            dataCycle = 1L;
            isStatUserNum1 = 0;
            this.log.error(labelCode + "对应的表名不存在");
        }

        if(dataCycle == 1L) {
            ciLabelInfo.setUpdateCycle(Integer.valueOf(2));
        } else {
            ciLabelInfo.setUpdateCycle(Integer.valueOf(1));
        }

        this.ciLabelInfoHDao.insertCiLabelInfo(ciLabelInfo);
        ciLabelInfo.setSortNum(ciLabelInfo.getLabelId());
        this.ciLabelInfoHDao.updateCiLabelInfo(ciLabelInfo);
        CiLabelExtInfo ciLabelExtInfo1 = new CiLabelExtInfo();
        ciLabelExtInfo1.setLabelId(ciLabelInfo.getLabelId());
        ciLabelExtInfo1.setIsLeaf(Integer.valueOf(isStatUserNum1));
        ciLabelExtInfo1.setIsStatUserNum(Integer.valueOf(isStatUserNum1));
        ciLabelExtInfo1.setLabelLevel(Integer.valueOf(4));
        CiMdaSysTable table = new CiMdaSysTable();
        table.setTableName(tableName);
        if(!labelIds.containsKey(tableName)) {
            try {
                this.ciMdaTableHDao.insertTableInfo(table);
            } catch (Exception var18) {
                var18.printStackTrace();
            }

            labelIds.put(tableName, table.getTableId());
        } else {
            table.setTableId((Integer)labelIds.get(tableName));
        }

        CiMdaSysTableColumn column = new CiMdaSysTableColumn();
        column.setColumnName(labelCode);
        column.setColumnCnName(ciLabelInfo.getLabelName());
        column.setCiMdaSysTable(table);
        column.setColumnDataTypeId(Integer.valueOf(1));

        try {
            this.ciMdaColumnHDao.insertColumnInfo(column);
        } catch (Exception var17) {
            var17.printStackTrace();
        }

        ciLabelExtInfo1.setCiMdaSysTableColumn(column);
        ciLabelExtInfo1.setMaxVal(Double.valueOf(1.0D));
        ciLabelExtInfo1.setMinVal(Double.valueOf(0.0D));
        this.ciLabelExtInfoHDao.insertCiLabelExtInfo(ciLabelExtInfo1);
        labelIds.put(labelCode, ciLabelInfo.getLabelId());
        CiApproveStatus ciApproveStatus = this.newCiApproveStatus(ciLabelInfo.getLabelId() + "", ciLabelInfo.getLabelName(), ciLabelInfo.getCreateUserId());
        this.initLabelApproveStatus(ciLabelInfo.getLabelId(), ciApproveStatus);
    }

    /** @deprecated */
    @Deprecated
    private void insertLevel6LabelInfo(DimCocLabelInfo dimCocLabelInfo, Map<String, Integer> labelIds, Set<String> labelCodeSet) throws Exception {
        String dbType = Configure.getInstance().getProperty("CI_DBTYPE");
        CiLabelInfo ciLabelInfo = new CiLabelInfo();
        ciLabelInfo.setLabelName(null == dimCocLabelInfo.getFifthClassDesc()?"":dimCocLabelInfo.getFifthClassDesc().trim().replace("\r", "").replace("\n", ""));
        ciLabelInfo.setCreateTime(dimCocLabelInfo.getEffectiveTime());
        ciLabelInfo.setEffecTime(dimCocLabelInfo.getEffectiveTime());
        ciLabelInfo.setPublishTime(dimCocLabelInfo.getEffectiveTime());
        ciLabelInfo.setFailTime(dimCocLabelInfo.getInvalidTime());
        ciLabelInfo.setLabelTypeId(Integer.valueOf(1));
        ciLabelInfo.setParentId((Integer)labelIds.get(dimCocLabelInfo.getFirstClassCode() + "_" + dimCocLabelInfo.getSecondClassCode() + "_" + dimCocLabelInfo.getThirdClassCode() + "_" + dimCocLabelInfo.getFourthClassCode()));
        if(null == ciLabelInfo.getParentId()) {
            ciLabelInfo.setDataStatusId(Integer.valueOf(1));
        } else {
            ciLabelInfo.setDataStatusId(Integer.valueOf(2));
        }

        ciLabelInfo.setBusiLegend(dimCocLabelInfo.getBusiDesc());
        ciLabelInfo.setApplySuggest(dimCocLabelInfo.getAppSuggest());
        ciLabelInfo.setBusiCaliber(dimCocLabelInfo.getBusiRulesDesc());
        ciLabelInfo.setCreateUserId(dimCocLabelInfo.getCreator());
        ciLabelInfo.setPublishUserId(dimCocLabelInfo.getCreator());
        String labelCode = dimCocLabelInfo.getLabelCode();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT T2.LABEL_TABLE_NAME TABLE_NAME,T2.TABLE_DATA_CYCLE DATA_CYCLE FROM ").append("DIM_COC_LABEL_MODEL_TABLE").append(" T1 LEFT JOIN ").append("DIM_COC_LABEL_TABLE").append(" T2 ON (T1.TARGET_TABLE_CODE = T2.TARGET_TABLE_CODE) ").append(" WHERE 1=1 ").append(" AND T1.LABEL_CODE = \'").append(labelCode).append("\' ");
        List tableNameList = this.getJdbcTemplate().queryForList(sql.toString(), new Object[0]);
        String tableName = "";
        long dataCycle = 1L;
        boolean isStatUserNum = false;
        byte isStatUserNum1;
        if(tableNameList != null && tableNameList.size() > 0) {
            tableName = (String)((Map)tableNameList.get(0)).get("TABLE_NAME");
            if("ORACLE".equalsIgnoreCase(dbType)) {
                BigDecimal ciLabelExtInfo = (BigDecimal)((Map)tableNameList.get(0)).get("DATA_CYCLE");
                dataCycle = (long)ciLabelExtInfo.intValue();
            } else {
                dataCycle = (long)((Integer)((Map)tableNameList.get(0)).get("DATA_CYCLE")).intValue();
            }

            if(tableName.endsWith("_")) {
                tableName = tableName.substring(0, tableName.length() - 1);
            }

            isStatUserNum1 = 1;
        } else {
            tableName = "no_table";
            dataCycle = 1L;
            isStatUserNum1 = 0;
            this.log.error(labelCode + "对应的表名不存在");
        }

        if(dataCycle == 1L) {
            ciLabelInfo.setUpdateCycle(Integer.valueOf(2));
        } else {
            ciLabelInfo.setUpdateCycle(Integer.valueOf(1));
        }

        this.ciLabelInfoHDao.insertCiLabelInfo(ciLabelInfo);
        ciLabelInfo.setSortNum(ciLabelInfo.getLabelId());
        this.ciLabelInfoHDao.updateCiLabelInfo(ciLabelInfo);
        CiLabelExtInfo ciLabelExtInfo1 = new CiLabelExtInfo();
        ciLabelExtInfo1.setLabelId(ciLabelInfo.getLabelId());
        ciLabelExtInfo1.setIsLeaf(Integer.valueOf(isStatUserNum1));
        ciLabelExtInfo1.setIsStatUserNum(Integer.valueOf(isStatUserNum1));
        ciLabelExtInfo1.setLabelLevel(Integer.valueOf(5));
        CiMdaSysTable table = new CiMdaSysTable();
        table.setTableName(tableName);
        if(!labelIds.containsKey(tableName)) {
            try {
                this.ciMdaTableHDao.insertTableInfo(table);
            } catch (Exception var18) {
                var18.printStackTrace();
            }

            labelIds.put(tableName, table.getTableId());
        } else {
            table.setTableId((Integer)labelIds.get(tableName));
        }

        CiMdaSysTableColumn column = new CiMdaSysTableColumn();
        column.setColumnName(labelCode);
        column.setColumnCnName(ciLabelInfo.getLabelName());
        column.setCiMdaSysTable(table);
        column.setColumnDataTypeId(Integer.valueOf(1));

        try {
            this.ciMdaColumnHDao.insertColumnInfo(column);
        } catch (Exception var17) {
            var17.printStackTrace();
        }

        ciLabelExtInfo1.setCiMdaSysTableColumn(column);
        ciLabelExtInfo1.setMaxVal(Double.valueOf(1.0D));
        ciLabelExtInfo1.setMinVal(Double.valueOf(0.0D));
        this.ciLabelExtInfoHDao.insertCiLabelExtInfo(ciLabelExtInfo1);
        labelIds.put(labelCode, ciLabelInfo.getLabelId());
        CiApproveStatus ciApproveStatus = this.newCiApproveStatus(ciLabelInfo.getLabelId() + "", ciLabelInfo.getLabelName(), ciLabelInfo.getCreateUserId());
        this.initLabelApproveStatus(ciLabelInfo.getLabelId(), ciApproveStatus);
    }

    private void initFirstLevelLabel(Map<String, Integer> labelIds) {
        labelIds.put("ROOT", Integer.valueOf(-1));
        List labelInfoList = null;
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT FIRST_CLASS_CODE,FIRST_CLASS_DESC FROM ").append("DIM_COC_LABEL_INFO").append(" GROUP BY FIRST_CLASS_CODE,FIRST_CLASS_DESC ");
        this.log.debug("initFirstLevelLabelNew sql : " + sql);
        labelInfoList = this.getJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(DimCocLabelInfo.class), new Object[0]);
        Iterator i$ = labelInfoList.iterator();

        while(i$.hasNext()) {
            DimCocLabelInfo dimCocLabelInfo = (DimCocLabelInfo)i$.next();
            CiLabelInfo ciLabelInfo = new CiLabelInfo();
            ciLabelInfo.setLabelName(dimCocLabelInfo.getFirstClassDesc().trim().replace("\r", "").replace("\n", ""));
            ciLabelInfo.setUpdateCycle(Integer.valueOf(2));
            ciLabelInfo.setCreateUserId("admin");
            ciLabelInfo.setEffecTime(DateUtil.string2Date("2014-05-31", "yyyy-MM-dd"));
            ciLabelInfo.setFailTime(DateUtil.string2Date("2099-12-31", "yyyy-MM-dd"));
            ciLabelInfo.setPublishTime(DateUtil.string2Date("2014-05-31", "yyyy-MM-dd"));
            ciLabelInfo.setLabelTypeId(Integer.valueOf(1));
            ciLabelInfo.setParentId(Integer.valueOf(-1));
            ciLabelInfo.setDataStatusId(Integer.valueOf(2));
            this.ciLabelInfoHDao.insertCiLabelInfo(ciLabelInfo);
            ciLabelInfo.setSortNum(ciLabelInfo.getLabelId());
            this.ciLabelInfoHDao.updateCiLabelInfo(ciLabelInfo);
            CiLabelExtInfo ciLabelExtInfo = new CiLabelExtInfo();
            ciLabelExtInfo.setLabelId(ciLabelInfo.getLabelId());
            ciLabelExtInfo.setIsStatUserNum(Integer.valueOf(0));
            ciLabelExtInfo.setIsLeaf(Integer.valueOf(0));
            ciLabelExtInfo.setLabelLevel(Integer.valueOf(1));
            String labelCode = dimCocLabelInfo.getFirstClassCode();
            String tableName = "first_second_label_no_table";
            CiMdaSysTable table = new CiMdaSysTable();
            table.setTableName(tableName);
            if(!labelIds.containsKey(tableName)) {
                try {
                    this.ciMdaTableHDao.insertTableInfo(table);
                } catch (Exception var13) {
                    var13.printStackTrace();
                }

                labelIds.put(tableName, table.getTableId());
            } else {
                table.setTableId((Integer)labelIds.get(tableName));
            }

            CiMdaSysTableColumn column = new CiMdaSysTableColumn();
            column.setColumnCnName(ciLabelInfo.getLabelName());
            column.setColumnName(labelCode);
            column.setCiMdaSysTable(table);

            try {
                this.ciMdaColumnHDao.insertColumnInfo(column);
            } catch (Exception var14) {
                var14.printStackTrace();
            }

            ciLabelExtInfo.setCiMdaSysTableColumn(column);
            this.ciLabelExtInfoHDao.insertCiLabelExtInfo(ciLabelExtInfo);
            labelIds.put(labelCode, ciLabelInfo.getLabelId());
            CiApproveStatus ciApproveStatus = this.newCiApproveStatus(ciLabelInfo.getLabelId() + "", ciLabelInfo.getLabelName(), ciLabelInfo.getCreateUserId());
            this.initLabelApproveStatus(ciLabelInfo.getLabelId(), ciApproveStatus);
        }

    }

    private void initSecondLevelLabel(Map<String, Integer> labelIds) {
        List labelInfoList = null;
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT * FROM ").append("DIM_COC_LABEL_INFO").append(" WHERE LABEL_CODE IN ( ").append("SELECT MAX(LABEL_CODE) AS LABEL_CODE FROM ").append("DIM_COC_LABEL_INFO").append(" GROUP BY FIRST_CLASS_CODE,SECOND_CLASS_CODE ) ");
        this.log.debug("initSecondLevelLabel sql : " + sql);
        labelInfoList = this.getJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(DimCocLabelInfo.class), new Object[0]);
        Iterator i$ = labelInfoList.iterator();

        while(i$.hasNext()) {
            DimCocLabelInfo dimCocLabelInfo = (DimCocLabelInfo)i$.next();
            CiLabelInfo ciLabelInfo = new CiLabelInfo();
            ciLabelInfo.setLabelName(dimCocLabelInfo.getSecondClassDesc().trim().replace("\r", "").replace("\n", ""));
            ciLabelInfo.setUpdateCycle(Integer.valueOf(2));
            ciLabelInfo.setCreateUserId("admin");
            ciLabelInfo.setEffecTime(DateUtil.string2Date("2014-05-31", "yyyy-MM-dd"));
            ciLabelInfo.setFailTime(DateUtil.string2Date("2099-12-31", "yyyy-MM-dd"));
            ciLabelInfo.setPublishTime(DateUtil.string2Date("2014-05-31", "yyyy-MM-dd"));
            ciLabelInfo.setLabelTypeId(Integer.valueOf(1));
            ciLabelInfo.setParentId((Integer)labelIds.get(dimCocLabelInfo.getFirstClassCode()));
            ciLabelInfo.setDataStatusId(Integer.valueOf(2));
            this.ciLabelInfoHDao.insertCiLabelInfo(ciLabelInfo);
            ciLabelInfo.setSortNum(ciLabelInfo.getLabelId());
            this.ciLabelInfoHDao.updateCiLabelInfo(ciLabelInfo);
            CiLabelExtInfo ciLabelExtInfo = new CiLabelExtInfo();
            ciLabelExtInfo.setLabelId(ciLabelInfo.getLabelId());
            ciLabelExtInfo.setIsStatUserNum(Integer.valueOf(0));
            ciLabelExtInfo.setIsLeaf(Integer.valueOf(0));
            ciLabelExtInfo.setLabelLevel(Integer.valueOf(2));
            String labelCode = dimCocLabelInfo.getFirstClassCode() + "_" + dimCocLabelInfo.getSecondClassCode();
            String tableName = "first_second_label_no_table";
            CiMdaSysTable table = new CiMdaSysTable();
            table.setTableName(tableName);
            if(!labelIds.containsKey(tableName)) {
                try {
                    this.ciMdaTableHDao.insertTableInfo(table);
                } catch (Exception var13) {
                    var13.printStackTrace();
                }

                labelIds.put(tableName, table.getTableId());
            } else {
                table.setTableId((Integer)labelIds.get(tableName));
            }

            CiMdaSysTableColumn column = new CiMdaSysTableColumn();
            column.setColumnCnName(ciLabelInfo.getLabelName());
            column.setColumnName(labelCode);
            column.setCiMdaSysTable(table);

            try {
                this.ciMdaColumnHDao.insertColumnInfo(column);
            } catch (Exception var14) {
                var14.printStackTrace();
            }

            ciLabelExtInfo.setCiMdaSysTableColumn(column);
            this.ciLabelExtInfoHDao.insertCiLabelExtInfo(ciLabelExtInfo);
            labelIds.put(labelCode, ciLabelInfo.getLabelId());
            CiApproveStatus ciApproveStatus = this.newCiApproveStatus(ciLabelInfo.getLabelId() + "", ciLabelInfo.getLabelName(), ciLabelInfo.getCreateUserId());
            this.initLabelApproveStatus(ciLabelInfo.getLabelId(), ciApproveStatus);
        }

    }

    private void initThirdLevelLabel(Map<String, Integer> labelIds) {
        List labelInfoList = null;
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT * FROM ").append("DIM_COC_LABEL_INFO").append(" WHERE LABEL_CODE IN ( ").append("SELECT MAX(LABEL_CODE) AS LABEL_CODE FROM ").append("DIM_COC_LABEL_INFO").append(" GROUP BY FIRST_CLASS_CODE,SECOND_CLASS_CODE,THIRD_CLASS_CODE ) ");
        this.log.debug("initThirdLevelLabel sql : " + sql);
        labelInfoList = this.getJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(DimCocLabelInfo.class), new Object[0]);
        Iterator i$ = labelInfoList.iterator();

        while(i$.hasNext()) {
            DimCocLabelInfo dimCocLabelInfo = (DimCocLabelInfo)i$.next();
            CiLabelInfo ciLabelInfo = new CiLabelInfo();
            ciLabelInfo.setLabelName(dimCocLabelInfo.getThirdClassDesc().trim().replace("\r", "").replace("\n", ""));
            ciLabelInfo.setUpdateCycle(Integer.valueOf(2));
            ciLabelInfo.setCreateUserId("admin");
            ciLabelInfo.setEffecTime(DateUtil.string2Date("2014-05-31", "yyyy-MM-dd"));
            ciLabelInfo.setFailTime(DateUtil.string2Date("2099-12-31", "yyyy-MM-dd"));
            ciLabelInfo.setPublishTime(DateUtil.string2Date("2014-05-31", "yyyy-MM-dd"));
            ciLabelInfo.setLabelTypeId(Integer.valueOf(1));
            ciLabelInfo.setParentId((Integer)labelIds.get(dimCocLabelInfo.getFirstClassCode() + "_" + dimCocLabelInfo.getSecondClassCode()));
            ciLabelInfo.setDataStatusId(Integer.valueOf(2));
            this.ciLabelInfoHDao.insertCiLabelInfo(ciLabelInfo);
            ciLabelInfo.setSortNum(ciLabelInfo.getLabelId());
            this.ciLabelInfoHDao.updateCiLabelInfo(ciLabelInfo);
            CiLabelExtInfo ciLabelExtInfo = new CiLabelExtInfo();
            ciLabelExtInfo.setLabelId(ciLabelInfo.getLabelId());
            ciLabelExtInfo.setIsStatUserNum(Integer.valueOf(0));
            ciLabelExtInfo.setIsLeaf(Integer.valueOf(0));
            ciLabelExtInfo.setLabelLevel(Integer.valueOf(3));
            String labelCode = dimCocLabelInfo.getFirstClassCode() + "_" + dimCocLabelInfo.getSecondClassCode() + "_" + dimCocLabelInfo.getThirdClassCode();
            String tableName = "first_second_label_no_table";
            CiMdaSysTable table = new CiMdaSysTable();
            table.setTableName(tableName);
            if(!labelIds.containsKey(tableName)) {
                try {
                    this.ciMdaTableHDao.insertTableInfo(table);
                } catch (Exception var13) {
                    var13.printStackTrace();
                }

                labelIds.put(tableName, table.getTableId());
            } else {
                table.setTableId((Integer)labelIds.get(tableName));
            }

            CiMdaSysTableColumn column = new CiMdaSysTableColumn();
            column.setColumnName(labelCode);
            column.setColumnCnName(ciLabelInfo.getLabelName());
            column.setCiMdaSysTable(table);

            try {
                this.ciMdaColumnHDao.insertColumnInfo(column);
            } catch (Exception var14) {
                var14.printStackTrace();
            }

            ciLabelExtInfo.setCiMdaSysTableColumn(column);
            this.ciLabelExtInfoHDao.insertCiLabelExtInfo(ciLabelExtInfo);
            labelIds.put(labelCode, ciLabelInfo.getLabelId());
            CiApproveStatus ciApproveStatus = this.newCiApproveStatus(ciLabelInfo.getLabelId() + "", ciLabelInfo.getLabelName(), ciLabelInfo.getCreateUserId());
            this.initLabelApproveStatus(ciLabelInfo.getLabelId(), ciApproveStatus);
        }

    }

    private CiApproveStatus newCiApproveStatus(String labelId, String labelName, String createUserId) {
        CiApproveStatus ciApproveStatus = new CiApproveStatus();
        ciApproveStatus.setResourceId(labelId);
        ciApproveStatus.setResourceName(labelName);
        ciApproveStatus.setResourceCreateUserId(createUserId);
        ciApproveStatus.setLastApproveTime(new Date());
        ciApproveStatus.setResourceTypeId("1");
        return ciApproveStatus;
    }

    private void initLabelApproveStatus(Integer labelId, CiApproveStatus ciApproveStatus) {
        if(StringUtil.isEmpty(APPROVE_STATUS)) {
            StringBuffer sql = new StringBuffer();
            sql.append("select a.approve_status_id STATUS_ID from DIM_APPROVE_STATUS a ,DIM_APPROVE_LEVEL b ").append(" where a.approve_role_id = b.approve_role_id and a.sort_num=").append(2).append(" and b.approve_role_type=").append(2).append(" and b.process_id = \'").append("1").append("\'");
            Map map = this.getSimpleJdbcTemplate().queryForMap(sql.toString(), new Object[0]);
            if(map != null && StringUtil.isNotEmpty(map.get("STATUS_ID"))) {
                APPROVE_STATUS = map.get("STATUS_ID").toString();
            }
        }

        ciApproveStatus.setCurrApproveStatusId(APPROVE_STATUS);
        this.ciApproveStatusHDao.insertOrUpdateCiApproveStatus(ciApproveStatus);
    }

    public String[] increaseLabelInfo() throws Exception {
        String dbType = Configure.getInstance().getProperty("CI_DBTYPE");
        String province = Configure.getInstance().getProperty("PROVINCE");
        String cityDimTable = Configure.getInstance().getProperty("CITY_DIM_TABLE");
        String cityLabelCode = Configure.getInstance().getProperty("CITY_LABEL_CODE");
        List labelCodeIdList = null;
        List attrLabelCodeIdList = null;
        List tableList = null;
        HashMap labelIds = new HashMap();
        StringBuffer sql = new StringBuffer();
        StringBuffer attrsql = new StringBuffer();
        sql.append(" SELECT T3.COLUMN_NAME AS LABEL_CODE, T1.LABEL_ID FROM ").append("CI_LABEL_INFO").append("  T1 LEFT JOIN ").append("CI_LABEL_EXT_INFO").append(" T2 ON(T1.LABEL_ID = T2.LABEL_ID) ").append(" LEFT JOIN ").append("CI_MDA_SYS_TABLE_COLUMN").append(" T3 ON(T2.COLUMN_ID = T3.COLUMN_ID) WHERE T1.LABEL_TYPE_ID != ").append(3);
        attrsql.append(" SELECT T3.COLUMN_NAME AS LABEL_CODE, T1.LABEL_ID, T2.ATTR_VAL FROM ").append("CI_LABEL_INFO").append("  T1 LEFT JOIN ").append("CI_LABEL_EXT_INFO").append(" T2 ON(T1.LABEL_ID = T2.LABEL_ID) ").append(" LEFT JOIN ").append("CI_MDA_SYS_TABLE_COLUMN").append(" T3 ON(T2.COLUMN_ID = T3.COLUMN_ID) WHERE T1.LABEL_TYPE_ID = ").append(3);
        StringBuffer tableSql = new StringBuffer();
        tableSql.append(" SELECT TABLE_NAME,TABLE_ID,UPDATE_CYCLE FROM ").append("CI_MDA_SYS_TABLE");
        labelCodeIdList = this.getSimpleJdbcTemplate().queryForList(sql.toString(), new Object[0]);
        attrLabelCodeIdList = this.getSimpleJdbcTemplate().queryForList(attrsql.toString(), new Object[0]);
        tableList = this.getSimpleJdbcTemplate().queryForList(tableSql.toString(), new Object[0]);
        Iterator attrVal;
        if(null != labelCodeIdList && labelCodeIdList.size() > 0) {
            attrVal = labelCodeIdList.iterator();

            while(attrVal.hasNext()) {
                Map message = (Map)attrVal.next();
                if("ORACLE".equalsIgnoreCase(dbType)) {
                    BigDecimal cityLabelList = (BigDecimal)message.get("LABEL_ID");
                    labelIds.put((String)message.get("LABEL_CODE"), Integer.valueOf(cityLabelList.intValue()));
                } else {
                    labelIds.put((String)message.get("LABEL_CODE"), (Integer)message.get("LABEL_ID"));
                }
            }
        }

        attrVal = null;
        BigDecimal labelId;
        String attrVal1;
        Iterator message1;
        Map cityLabelList1;
        if(null != attrLabelCodeIdList && attrLabelCodeIdList.size() > 0) {
            message1 = attrLabelCodeIdList.iterator();

            while(message1.hasNext()) {
                cityLabelList1 = (Map)message1.next();
                attrVal1 = (String)cityLabelList1.get("ATTR_VAL");
                if(null == attrVal1) {
                    if("ORACLE".equalsIgnoreCase(dbType)) {
                        labelId = (BigDecimal)cityLabelList1.get("LABEL_ID");
                        labelIds.put((String)cityLabelList1.get("LABEL_CODE"), Integer.valueOf(labelId.intValue()));
                    } else {
                        labelIds.put((String)cityLabelList1.get("LABEL_CODE"), (Integer)cityLabelList1.get("LABEL_ID"));
                    }
                } else if("ORACLE".equalsIgnoreCase(dbType)) {
                    labelId = (BigDecimal)cityLabelList1.get("LABEL_ID");
                    labelIds.put((String)cityLabelList1.get("LABEL_CODE") + (String)cityLabelList1.get("ATTR_VAL"), Integer.valueOf(labelId.intValue()));
                } else {
                    labelIds.put((String)cityLabelList1.get("LABEL_CODE") + (String)cityLabelList1.get("ATTR_VAL"), (Integer)cityLabelList1.get("LABEL_ID"));
                }
            }
        }

        message1 = tableList.iterator();

        while(message1.hasNext()) {
            cityLabelList1 = (Map)message1.next();
            if("ORACLE".equalsIgnoreCase(dbType)) {
                labelId = (BigDecimal)cityLabelList1.get("TABLE_ID");
                BigDecimal iterator2 = (BigDecimal)cityLabelList1.get("UPDATE_CYCLE");
                labelIds.put((String)cityLabelList1.get("TABLE_NAME") + iterator2, Integer.valueOf(labelId.intValue()));
            } else {
                labelIds.put((String)cityLabelList1.get("TABLE_NAME") + (Integer)cityLabelList1.get("UPDATE_CYCLE"), (Integer)cityLabelList1.get("TABLE_ID"));
            }
        }

        if("BEIJING".equalsIgnoreCase(province)) {
            StringBuffer message2 = new StringBuffer();
            message2.append(" SELECT T3.COLUMN_NAME AS LABEL_CODE, T1.LABEL_ID, T2.ATTR_VAL FROM ").append("CI_LABEL_INFO").append("  T1 LEFT JOIN ").append("CI_LABEL_EXT_INFO").append(" T2 ON(T1.LABEL_ID = T2.LABEL_ID) ").append(" LEFT JOIN ").append("CI_MDA_SYS_TABLE_COLUMN").append(" T3 ON(T2.COLUMN_ID = T3.COLUMN_ID) WHERE T3.DIM_TRANS_ID = \'").append(cityDimTable).append("\'").append(" AND T2.ATTR_VAL IS NOT NULL");
            List cityLabelList2 = this.getSimpleJdbcTemplate().queryForList(message2.toString(), new Object[0]);
            Integer labelId1 = Integer.valueOf(0);
            if(null != cityLabelList2) {
                Iterator iterator21 = cityLabelList2.iterator();

                while(iterator21.hasNext()) {
                    Map map2 = (Map)iterator21.next();
                    attrVal1 = (String)map2.get("ATTR_VAL");
                    labelId1 = (Integer)map2.get("LABEL_ID");
                    labelIds.put(cityLabelCode + attrVal1, labelId1);
                }
            }
        }

        labelIds.put("ROOT", Integer.valueOf(-1));
        String[] message3 = this.initOtherlevelLabel(labelIds);
        return message3;
    }
}
