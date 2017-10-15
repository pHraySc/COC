package com.ailk.biapp.ci.webservice.util;

import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomSceneRel;
import com.ailk.biapp.ci.entity.CiCustomSceneRelId;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.entity.CiGroupAttrRelId;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiTemplateInfo;
import com.ailk.biapp.ci.exception.*;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.asiainfo.biframe.dimtable.model.DimTableDefine;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class CustomerGroupCreateThread extends Thread {
    private Logger log = Logger.getLogger(CustomerGroupCreateThread.class);
    @Autowired
    private ICustomersManagerService customersService;
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;
    private CiCustomGroupInfo pushCiCustomGroupInfo;
    private List<CiGroupAttrRel> pushCiGroupAttrRelList;
    private String pushCustGroupId;

    public CustomerGroupCreateThread() {
    }

    public String getPushCustGroupId() {
        return this.pushCustGroupId;
    }

    public void setPushCustGroupId(String pushCustGroupId) {
        this.pushCustGroupId = pushCustGroupId;
    }

    public CiCustomGroupInfo getPushCiCustomGroupInfo() {
        return this.pushCiCustomGroupInfo;
    }

    public void setPushCiCustomGroupInfo(CiCustomGroupInfo pushCiCustomGroupInfo) {
        this.pushCiCustomGroupInfo = pushCiCustomGroupInfo;
    }

    public List<CiGroupAttrRel> getPushCiGroupAttrRelList() {
        return this.pushCiGroupAttrRelList;
    }

    public void setPushCiGroupAttrRelList(List<CiGroupAttrRel> pushCiGroupAttrRelList) {
        this.pushCiGroupAttrRelList = pushCiGroupAttrRelList;
    }

    public void run() {
        String labelId = Configure.getInstance().getProperty("CMTARGET_ENUM_LABEL_ID");
        String sceneIds = Configure.getInstance().getProperty("CMTARGET_CICUSTOM_SCENE_IDS");
        String zqScheme = Configure.getInstance().getProperty("ZQCI_BACK_SCHEMA");
        String zqColumn = Configure.getInstance().getProperty("ZQCI_KEYCOLUMN");
        ArrayList ciLabelRuleList = new ArrayList();
        ArrayList attrList = new ArrayList();
        CiLabelInfo ciLabelInfo = this.ciLabelInfoService.queryCiLabelInfoByIdFullLoad(Integer.valueOf(Integer.parseInt(labelId)));
        new CiCustomGroupInfo();
        CiCustomGroupInfo ciCustomGroupInfo = this.createCiCustomGroupInfo(this.pushCiCustomGroupInfo, sceneIds);
        StringBuffer listTableInfoStr = new StringBuffer();
        listTableInfoStr.append(ciCustomGroupInfo.getListTableName());
        byte columnNameIndex = 0;
        CiGroupAttrRel labelItem = this.genGroupAttrRel(columnNameIndex, ciLabelInfo.getLabelName(), 2, (String)null);
        labelItem.setLabelOrCustomId(ciLabelInfo.getLabelId().toString());
        int var16 = columnNameIndex + 1;
        attrList.add(labelItem);
        if(this.pushCiGroupAttrRelList != null && this.pushCiGroupAttrRelList.size() > 0) {
            for(int ciLabelRule = 0; ciLabelRule < this.pushCiGroupAttrRelList.size(); ++ciLabelRule) {
                CiGroupAttrRel arra = (CiGroupAttrRel)this.pushCiGroupAttrRelList.get(ciLabelRule);
                if(!StringUtil.isEmpty(arra.getId().getAttrCol())) {
                    listTableInfoStr.append(",").append(arra.getId().getAttrCol());
                    CiGroupAttrRel e = this.genGroupAttrRel(var16, arra.getAttrColName(), 0, arra.getAttrColType());
                    ++var16;
                    attrList.add(e);
                }
            }
        }

        ciCustomGroupInfo.setListTableName(listTableInfoStr.toString());
        if(1 == ciLabelInfo.getUpdateCycle().intValue()) {
            ciCustomGroupInfo.setDayLabelDate(ciLabelInfo.getDataDate());
        } else {
            ciCustomGroupInfo.setMonthLabelDate(ciLabelInfo.getDataDate());
        }

        ciCustomGroupInfo.setDataDate(ciLabelInfo.getDataDate());
        CiLabelRule var17 = new CiLabelRule();
        var17.setCalcuElement(ciLabelInfo.getLabelId().toString());
        var17.setCustomOrLabelName(ciLabelInfo.getLabelName());
        String[] var18 = this.getDimStr(zqScheme + "." + this.pushCiCustomGroupInfo.getListTableName(), zqColumn, ciLabelInfo.getCiLabelExtInfo().getCiMdaSysTableColumn().getColumnId());
        var17.setAttrVal(var18[0]);
        var17.setElementType(Integer.valueOf(2));
        var17.setLabelTypeId(Integer.valueOf(5));
        var17.setLabelFlag(Integer.valueOf(1));
        var17.setUpdateCycle(ciLabelInfo.getUpdateCycle());
        var17.setCustomType(Integer.valueOf(1));
        var17.setSortNum(Long.valueOf("0"));
        ciLabelRuleList.add(var17);

        try {
            this.log.info("创建客户群准备数据完成！");
            this.customersService.addCiCustomGroupInfo(ciCustomGroupInfo, ciLabelRuleList, (List)null, (CiTemplateInfo)null, ciCustomGroupInfo.getCreateUserId(), false, attrList);
        } catch (CIServiceException var15) {
            this.log.error("创建客户群失败", var15);
        }

    }

    public CiCustomGroupInfo createCiCustomGroupInfo(CiCustomGroupInfo pushCiCustomGroupInfo, String sceneIds) {
        CiCustomGroupInfo ciCustomGroupInfo = new CiCustomGroupInfo();
        ArrayList sceneList = new ArrayList();
        ciCustomGroupInfo.setCustomGroupName(pushCiCustomGroupInfo.getCustomGroupName() + DateUtil.date2String(new Date(), "yyyy-MM-dd"));
        if(StringUtil.isNotEmpty(sceneIds)) {
            String[] sceneIdsArr = sceneIds.split(",");

            for(int i = 0; i < sceneIdsArr.length; ++i) {
                if(StringUtil.isNotEmpty(sceneIdsArr[i].trim())) {
                    CiCustomSceneRel item = new CiCustomSceneRel();
                    item.setId(new CiCustomSceneRelId());
                    item.getId().setSceneId(sceneIdsArr[i].trim());
                    item.getId().setUserId(pushCiCustomGroupInfo.getCreateUserId());
                    item.setStatus(1);
                    sceneList.add(item);
                }
            }

            ciCustomGroupInfo.setSceneList(sceneList);
        }

        ciCustomGroupInfo.setDataDate(pushCiCustomGroupInfo.getDataDate());
        ciCustomGroupInfo.setStartDate(pushCiCustomGroupInfo.getStartDate());
        ciCustomGroupInfo.setEndDate(pushCiCustomGroupInfo.getEndDate());
        ciCustomGroupInfo.setCreateCityId(pushCiCustomGroupInfo.getCreateCityId());
        ciCustomGroupInfo.setCreateUserId(pushCiCustomGroupInfo.getCreateUserId());
        ciCustomGroupInfo.setListTableName(pushCiCustomGroupInfo.getListTableName());
        ciCustomGroupInfo.setCustomGroupDesc(pushCiCustomGroupInfo.getCustomGroupDesc());
        ciCustomGroupInfo.setIsPrivate(Integer.valueOf(1));
        ciCustomGroupInfo.setUpdateCycle(Integer.valueOf(1));
        ciCustomGroupInfo.setCreateTypeId(Integer.valueOf(13));
        ciCustomGroupInfo.setIsHasList(Integer.valueOf(1));
        ciCustomGroupInfo.setCreateTime(new Timestamp((new Date()).getTime()));
        ciCustomGroupInfo.setStatus(Integer.valueOf(1));
        ciCustomGroupInfo.setDataStatus(Integer.valueOf(1));
        ciCustomGroupInfo.setCustomNum((Long)null);
        ciCustomGroupInfo.setTacticsId("2");
        return ciCustomGroupInfo;
    }

    public String[] getDimStr(String tableName, String columnName, Integer columnId) {
        String[] array = new String[]{"", ""};
        StringBuffer ids = new StringBuffer();
        StringBuffer names = new StringBuffer();

        try {
            DimTableDefine e = new DimTableDefine();
            e.setDimTablename(tableName);
            e.setDimCodeCol(columnName);
            e.setDimValueCol(columnName);
            this.log.info("开始查询维值tableName=" + tableName + "列columnName=" + columnName + "time=" + DateUtil.getCurrentDay());
            List mapList = this.ciLabelInfoService.queryAllDimDataByDimTableDefine(e);
            this.log.info("timeEnd=" + DateUtil.getCurrentDay());
            Iterator var10 = mapList.iterator();

            while(var10.hasNext()) {
                Map map = (Map)var10.next();
                String id = map.get("V_KEY").toString();
                ids.append(id).append(",");
            }

            if(ids.length() > 0) {
                ids.delete(ids.length() - 1, ids.length());
            }

            array[0] = ids.toString();
            array[1] = names.toString();
            this.log.info("查询出的政企清单数据array[0] ===========" + array[0]);
            return array;
        } catch (Exception var12) {
            this.log.error("查出清单表tableName=" + tableName + "列columnName=" + columnName + "报错", var12);
            return array;
        }
    }

    private CiGroupAttrRel genGroupAttrRel(int i, String attrColName, int attrSource, String attrColType) {
        CiGroupAttrRelId ciGroupAttrRelId = new CiGroupAttrRelId();
        CiGroupAttrRel ciGroupAttrRel = new CiGroupAttrRel();
        ciGroupAttrRelId.setAttrCol(CiUtil.genCustomAttrColumnName(i));
        ciGroupAttrRel.setAttrColName(attrColName);
        ciGroupAttrRel.setId(ciGroupAttrRelId);
        String dbType = Configure.getInstance().getProperty("CI_DBTYPE");
        if(StringUtil.isNotEmpty(attrColType)) {
            ciGroupAttrRel.setAttrColType(attrColType);
        } else if("ORACLE".equalsIgnoreCase(dbType)) {
            ciGroupAttrRel.setAttrColType("VARCHAR2(512)");
        } else {
            ciGroupAttrRel.setAttrColType("VARCHAR(512)");
        }

        ciGroupAttrRel.setAttrSource(attrSource);
        ciGroupAttrRel.setIsVerticalAttr(0);
        ciGroupAttrRel.setStatus(Integer.valueOf(0));
        ciGroupAttrRel.setSortNum(Integer.valueOf(i));
        return ciGroupAttrRel;
    }
}
