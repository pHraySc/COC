package com.ailk.biapp.ci.dataservice.service.impl;

import com.ailk.biapp.ci.dataservice.dao.ICiConfigJDao;
import com.ailk.biapp.ci.dataservice.dao.ICiLabelsAndIndexesJDao;
import com.ailk.biapp.ci.dataservice.service.ICiLabelsAndIndexesService;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.CrmLabelOrIndexModel;
import com.ailk.biapp.ci.model.CrmTypeModel;
import com.ailk.biapp.ci.model.LabelTelTableInfo;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;
import com.mongodb.DBObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.bson.BSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("LabelsAndIndexesService")
public class CiLabelsAndIndexesServiceImpl implements ICiLabelsAndIndexesService {
    private final Logger log = Logger.getLogger(CiLabelsAndIndexesServiceImpl.class);
    @Autowired
    private ICiLabelsAndIndexesJDao ciLabelsAndIndexesJDao;
    @Autowired
    private ICiConfigJDao ciConfigJDao;

    public CiLabelsAndIndexesServiceImpl() {
    }

    public List<CrmTypeModel> getCrmTypeModelByMobile(String mobile) throws CIServiceException {
        ArrayList crmTypeModelList = new ArrayList();
        BSONObject labelsAndIndexesByMobile = this.getLabelsAndIndexesByMobile(mobile);
        if(labelsAndIndexesByMobile != null) {
            List labels = (List)labelsAndIndexesByMobile.get("labels");
            List labelList = this.ciLabelsAndIndexesJDao.getLabels(labels);
            HashMap idToCrmLabelOrIndexModelMap = new HashMap();
            Iterator indexs = labelList.iterator();

            while(indexs.hasNext()) {
                DBObject indexIds = (DBObject)indexs.next();
                CrmLabelOrIndexModel values = new CrmLabelOrIndexModel();
                String indexList = (String)indexIds.get("labelId");
                values.setId(indexList);
                values.setName((String)indexIds.get("labelName"));
                values.setLabel(true);
                idToCrmLabelOrIndexModelMap.put(indexList, values);
            }

            List var18 = (List)labelsAndIndexesByMobile.get("indexes");
            ArrayList var19 = new ArrayList();
            HashMap var20 = new HashMap();
            Iterator var21 = var18.iterator();

            while(var21.hasNext()) {
                DBObject configList = (DBObject)var21.next();
                String idx = (String)configList.get("id");
                var19.add(idx);
                var20.put(idx, (String)configList.get("value"));
            }

            List var22 = this.ciLabelsAndIndexesJDao.getIndexes(var19);
            Iterator var23 = var22.iterator();

            while(var23.hasNext()) {
                DBObject var25 = (DBObject)var23.next();
                CrmLabelOrIndexModel crmTypeModel = new CrmLabelOrIndexModel();
                String config = (String)var25.get("indexId");
                crmTypeModel.setId(config);
                crmTypeModel.setName((String)var25.get("indexName"));
                crmTypeModel.setValue(((String)var20.get(config)).replace(",", "&nbsp;,&nbsp;"));
                crmTypeModel.setLabel(false);
                idToCrmLabelOrIndexModelMap.put(config, crmTypeModel);
            }

            List var24 = this.ciConfigJDao.getConifg();

            for(int var26 = 0; var26 < var24.size(); ++var26) {
                CrmTypeModel var27 = new CrmTypeModel();
                var27.setTypeId("class" + var26);
                DBObject var28 = (DBObject)var24.get(var26);
                var27.setTypeName((String)var28.get("class_name"));
                List elementIdList = (List)var28.get("element_ids");
                var27.setElementIdList(elementIdList);
                Iterator i$ = elementIdList.iterator();

                while(i$.hasNext()) {
                    String elementId = (String)i$.next();
                    if(idToCrmLabelOrIndexModelMap.containsKey(elementId)) {
                        var27.addDisplayLabelOrIndex((CrmLabelOrIndexModel)idToCrmLabelOrIndexModelMap.get(elementId));
                    }
                }

                crmTypeModelList.add(var27);
            }
        }

        return crmTypeModelList;
    }

    public List<CrmTypeModel> getCrmTypeModelByMobile(String sysCode, String channelId, String accNbr, String staffCode, String acpId) throws CIServiceException {
        this.log.info("[crm url input]:" + sysCode + "," + channelId + "," + accNbr + "," + staffCode + "," + acpId);
        return this.getCrmTypeModelByMobile(accNbr);
    }

    public BSONObject getLabelsAndIndexesByMobile(String mobile) throws CIServiceException {
        BSONObject obj = this.ciLabelsAndIndexesJDao.getLabelsAndIndexesByMobile(mobile);
        return obj;
    }

    public List<CrmTypeModel> getCrmTypeModelByMobile4OtherProvince(String keyWord) throws CIServiceException {
        new ArrayList();
        HashMap labelTableMap = new HashMap();
        ArrayList labelValues = new ArrayList();
        List crmTypeModelList = this.ciLabelsAndIndexesJDao.selectLabelTypeInfo();
        List telTableList = this.ciLabelsAndIndexesJDao.selectAllLabelInfo();
        Iterator labelTableMapSet = telTableList.iterator();

        String i;
        while(labelTableMapSet.hasNext()) {
            LabelTelTableInfo keepLabelTelTableInfo = (LabelTelTableInfo)labelTableMapSet.next();
            i = keepLabelTelTableInfo.getTable_name();
            if(keepLabelTelTableInfo.getUpdate_cycle() == 1) {
                i = i + "_" + CacheBase.getInstance().getNewLabelDay();
            } else if(keepLabelTelTableInfo.getUpdate_cycle() == 2) {
                i = i + "_" + CacheBase.getInstance().getNewLabelMonth();
            }

            Object item = (List)labelTableMap.get(i);
            if(item == null) {
                item = new ArrayList();
            }

            ((List)item).add(keepLabelTelTableInfo.getColumn_name());
            labelTableMap.put(i, item);
        }

        Set var17 = labelTableMap.keySet();
        Iterator var18 = var17.iterator();

        String i$;
        String lTableName;
        while(var18.hasNext()) {
            i = (String)var18.next();
            List var21 = (List)labelTableMap.get(i);
            i$ = this.initSelectHorizontalSql(keyWord, i, var21);
            Map ctm = this.ciLabelsAndIndexesJDao.selectBySQL(i$);
            Set model = ctm.keySet();
            Iterator l = model.iterator();

            while(l.hasNext()) {
                lTableName = (String)l.next();
                Integer lColumnName = Integer.valueOf((new BigDecimal(String.valueOf(ctm.get(lTableName)))).intValue());
                if(lColumnName.intValue() == 1) {
                    LabelTelTableInfo labelTelTableInfo = new LabelTelTableInfo();
                    labelTelTableInfo.setTable_name(i);
                    labelTelTableInfo.setColumn_name(lTableName);
                    labelValues.add(labelTelTableInfo);
                }
            }
        }

        ArrayList var19 = new ArrayList();

        int var20;
        LabelTelTableInfo var22;
        for(var20 = 0; var20 < telTableList.size(); ++var20) {
            var22 = (LabelTelTableInfo)telTableList.get(var20);
            i$ = var22.getTable_name();
            String var23 = var22.getColumn_name();
            if(var22.getUpdate_cycle() == 1) {
                i$ = i$ + "_" + CacheBase.getInstance().getNewLabelDay();
            } else if(var22.getUpdate_cycle() == 2) {
                i$ = i$ + "_" + CacheBase.getInstance().getNewLabelMonth();
            }

            Iterator var26 = labelValues.iterator();

            while(var26.hasNext()) {
                LabelTelTableInfo var28 = (LabelTelTableInfo)var26.next();
                lTableName = var28.getTable_name();
                String var29 = var28.getColumn_name();
                if(i$.equals(lTableName) && var23.equals(var29)) {
                    var19.add(var22);
                    break;
                }
            }
        }

        for(var20 = 0; var20 < var19.size(); ++var20) {
            var22 = (LabelTelTableInfo)var19.get(var20);
            Iterator var24 = crmTypeModelList.iterator();

            while(var24.hasNext()) {
                CrmTypeModel var25 = (CrmTypeModel)var24.next();
                if(String.valueOf(var22.getType_id()).equals(var25.getTypeId())) {
                    CrmLabelOrIndexModel var27 = new CrmLabelOrIndexModel();
                    var27.setId(String.valueOf(var22.getLabel_id()));
                    var27.setName(var22.getLabel_name());
                    var27.setLabel(true);
                    var27.setBusiCaliber(var22.getBusi_caliber());
                    if(var25.getDisplayLabelOrIndexList() == null) {
                        var25.setDisplayLabelOrIndexList(new ArrayList());
                    }

                    var25.getDisplayLabelOrIndexList().add(var27);
                }
            }
        }

        return crmTypeModelList;
    }

    private String initSelectHorizontalSql(String phoneNo, String tableName, List<String> columns) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");

        for(int i = 0; i < columns.size(); ++i) {
            sql.append((String)columns.get(i));
            if(i < columns.size() - 1) {
                sql.append(",");
            }
        }

        sql.append(" FROM ").append(tableName);
        sql.append(" WHERE ").append(Configure.getInstance().getProperty("RELATED_COLUMN"));
        sql.append(" =\'").append(phoneNo).append("\'");
        return sql.toString();
    }

    public Integer queryPhoneNum(String keyWord) {
        byte result = 0;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT B.TABLE_NAME, L.UPDATE_CYCLE ");
        sql.append("FROM  ");
        sql.append("CI_MDA_SYS_TABLE_COLUMN       C,  ");
        sql.append("CI_MDA_SYS_TABLE              B,  ");
        sql.append("CI_LABEL_EXT_INFO             E,  ");
        sql.append("CI_LABEL_INFO                 L  ");
        sql.append("WHERE   C.COLUMN_ID = E.COLUMN_ID  ");
        sql.append("AND E.LABEL_ID = L.LABEL_ID  ");
        sql.append("AND C.TABLE_ID = B.TABLE_ID  ");
        sql.append("ORDER BY B.TABLE_NAME, C.COLUMN_NAME");
        List telTable = this.ciLabelsAndIndexesJDao.selectTableName(sql.toString());
        if(telTable != null && telTable.size() > 0) {
            LabelTelTableInfo item = (LabelTelTableInfo)telTable.get(0);
            String tableName = item.getTable_name();
            if(item.getUpdate_cycle() == 1) {
                tableName = tableName + "_" + CacheBase.getInstance().getNewLabelDay();
            } else if(item.getUpdate_cycle() == 2) {
                tableName = tableName + "_" + CacheBase.getInstance().getNewLabelMonth();
            }

            int i = this.ciLabelsAndIndexesJDao.selectPhoneNum(tableName, keyWord).intValue();
            if(i > 0) {
                result = 1;
            }
        }

        return Integer.valueOf(result);
    }

    public static void main(String[] args) {
        CrmTypeModel ctm = new CrmTypeModel();
        ArrayList list = new ArrayList();
        list.add(ctm);
        list.add(ctm);
        System.out.println(list);
    }
}
