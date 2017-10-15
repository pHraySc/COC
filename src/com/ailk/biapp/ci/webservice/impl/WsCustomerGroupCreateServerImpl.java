package com.ailk.biapp.ci.webservice.impl;

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
import com.ailk.biapp.ci.util.DataBaseAdapter;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.ThreadPool;
import com.ailk.biapp.ci.webservice.IWsCustomerGroupCreateServer;
import com.ailk.biapp.ci.webservice.util.CustomerGroupCreateThread;
import com.asiainfo.biframe.dimtable.model.DimTableDefine;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;

import java.io.StringReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.feature.Features;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@WebService(
    endpointInterface = "com.ailk.biapp.ci.webservice.IWsCustomerGroupCreateServer",
    targetNamespace = "http://webservice.cm.biapp.ailk.com"
)
@Features(
    features = {"org.apache.cxf.feature.LoggingFeature"}
)
public class WsCustomerGroupCreateServerImpl implements IWsCustomerGroupCreateServer {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ICustomersManagerService customersService;
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;

    public WsCustomerGroupCreateServerImpl() {
    }

    public String createCustomerGroup(String xmlBody) {
        Boolean result = Boolean.valueOf(true);
        CustomerGroupCreateThread thread = null;
        CiCustomGroupInfo ciCustomGroupInfo = new CiCustomGroupInfo();
        ArrayList attrRel = new ArrayList();

        try {
            this.log.info("从政企接收XML数据并解析");
            SAXReader customGroupName = new SAXReader();
            Document list = customGroupName.read(new StringReader(xmlBody));
            Element e = list.getRootElement();
            Element data = e.element("data");
            ciCustomGroupInfo.setCustomGroupId(data.elementTextTrim("customGroupId"));
            ciCustomGroupInfo.setCustomGroupName(data.elementTextTrim("customGroupName"));
            ciCustomGroupInfo.setDataDate(data.elementTextTrim("dataDate"));
            String startDate = data.elementTextTrim("effective_time");
            if(StringUtil.isNotEmpty(startDate)) {
                ciCustomGroupInfo.setStartDate(DateUtil.string2Date(startDate, "yyyy-MM-dd"));
            }

            String endDate = data.elementTextTrim("fail_time");
            if(StringUtil.isNotEmpty(endDate)) {
                ciCustomGroupInfo.setEndDate(DateUtil.string2Date(endDate, "yyyy-MM-dd"));
            }

            ciCustomGroupInfo.setCreateCityId(data.elementTextTrim("createCityId"));
            ciCustomGroupInfo.setCreateUserId(data.elementTextTrim("userId"));
            ciCustomGroupInfo.setListTableName(data.elementTextTrim("uploadFileName"));
            ciCustomGroupInfo.setCustomGroupDesc(data.elementTextTrim("customGroupDesc"));
            Iterator cols = data.elementIterator("columns");
            if(cols.hasNext()) {
                Element columnsEle = (Element)cols.next();
                Iterator col = columnsEle.elementIterator("column");

                while(col.hasNext()) {
                    Element columnEle = (Element)col.next();
                    String columnName = columnEle.elementTextTrim("columnName");
                    String columnCnName = columnEle.elementTextTrim("columnCnName");
                    String columnType = columnEle.elementTextTrim("columnDataType");
                    CiGroupAttrRel ciGroupAttrRel = new CiGroupAttrRel();
                    CiGroupAttrRelId ciGroupAttrRelId = new CiGroupAttrRelId();
                    ciGroupAttrRelId.setAttrCol(columnName);
                    ciGroupAttrRel.setId(ciGroupAttrRelId);
                    ciGroupAttrRel.setAttrColName(columnCnName);
                    ciGroupAttrRel.setAttrColType(columnType);
                    attrRel.add(ciGroupAttrRel);
                }
            }
        } catch (Exception var22) {
            result = Boolean.valueOf(false);
            this.log.error("xml转换异常", var22);
        }

        String customGroupName1 = ciCustomGroupInfo.getCustomGroupName() + DateUtil.date2String(new Date(), "yyyy-MM-dd");
        List list1 = this.customersService.queryCiCustomGroupInfoListByName(customGroupName1, ciCustomGroupInfo.getCreateUserId(), ciCustomGroupInfo.getCreateCityId());
        if(list1 != null && list1.size() > 0) {
            result = Boolean.valueOf(false);
        }

        if(result.booleanValue()) {
            try {
                thread = (CustomerGroupCreateThread)SystemServiceLocator.getInstance().getService("customerGroupCreateThread");
                thread.setPushCiCustomGroupInfo(ciCustomGroupInfo);
                thread.setPushCiGroupAttrRelList(attrRel);
                thread.setPushCustGroupId(ciCustomGroupInfo.getCustomGroupId());
                ThreadPool.getInstance().execute(thread);
            } catch (Exception var21) {
                result = Boolean.valueOf(false);
                this.log.error("创建客户群线程发生错误", var21);
            }
        }

        this.log.info("sicuan webservice invoke method createCustomerGroup end");
        return result.toString();
    }

    private Boolean addCiCustomGroupInfo(CiCustomGroupInfo pushCiCustomGroupInfo, List<CiGroupAttrRel> pushCiGroupAttrRelList) {
        Boolean result = Boolean.valueOf(true);
        String labelId = Configure.getInstance().getProperty("CMTARGET_ENUM_LABEL_ID");
        String sceneIds = Configure.getInstance().getProperty("CMTARGET_CICUSTOM_SCENE_IDS");
        String zqScheme = Configure.getInstance().getProperty("ZQCI_BACK_SCHEMA");
        String zqColumn = Configure.getInstance().getProperty("ZQCI_KEYCOLUMN");
        String dbType = Configure.getInstance().getProperty("CI_BACK_DBTYPE");
        ArrayList ciLabelRuleList = new ArrayList();
        ArrayList attrList = new ArrayList();
        CiLabelInfo ciLabelInfo = this.ciLabelInfoService.queryCiLabelInfoByIdFullLoad(Integer.valueOf(Integer.parseInt(labelId)));
        new CiCustomGroupInfo();
        CiCustomGroupInfo ciCustomGroupInfo = this.createCiCustomGroupInfo(pushCiCustomGroupInfo, sceneIds);
        StringBuffer listTableInfoStr = new StringBuffer();
        listTableInfoStr.append(ciCustomGroupInfo.getListTableName());
        byte columnNameIndex = 0;
        CiGroupAttrRel labelItem = this.genGroupAttrRel(columnNameIndex, ciLabelInfo.getLabelName(), 2, (String)null);
        labelItem.setLabelOrCustomId(ciLabelInfo.getLabelId().toString());
        int var22 = columnNameIndex + 1;
        attrList.add(labelItem);
        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(dbType);
        CiGroupAttrRel labelItemKey = this.genGroupAttrRel(var22, ciLabelInfo.getLabelName() + "编码", 0, dataBaseAdapter.getStrColumnType(32));
        ++var22;
        listTableInfoStr.append(",").append(zqColumn);
        attrList.add(labelItemKey);
        if(pushCiGroupAttrRelList != null && pushCiGroupAttrRelList.size() > 0) {
            for(int ciLabelRule = 0; ciLabelRule < pushCiGroupAttrRelList.size(); ++ciLabelRule) {
                CiGroupAttrRel arra = (CiGroupAttrRel)pushCiGroupAttrRelList.get(ciLabelRule);
                if(!StringUtil.isEmpty(arra.getId().getAttrCol())) {
                    listTableInfoStr.append(",").append(arra.getId().getAttrCol());
                    CiGroupAttrRel e = this.genGroupAttrRel(var22, arra.getAttrColName(), 0, arra.getAttrColType());
                    ++var22;
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
        CiLabelRule var23 = new CiLabelRule();
        var23.setCalcuElement(ciLabelInfo.getLabelId().toString());
        var23.setCustomOrLabelName(ciLabelInfo.getLabelName());
        String[] var24 = this.getDimStr(zqScheme + "." + pushCiCustomGroupInfo.getListTableName(), zqColumn, ciLabelInfo.getCiLabelExtInfo().getCiMdaSysTableColumn().getColumnId());
        var23.setAttrVal(var24[0]);
        var23.setElementType(Integer.valueOf(2));
        var23.setLabelTypeId(Integer.valueOf(5));
        var23.setLabelFlag(Integer.valueOf(1));
        var23.setUpdateCycle(ciLabelInfo.getUpdateCycle());
        var23.setCustomType(Integer.valueOf(1));
        var23.setSortNum(Long.valueOf("0"));
        ciLabelRuleList.add(var23);

        try {
            this.log.info("创建客户群准备数据完成！");
            result = Boolean.valueOf(this.customersService.addCiCustomGroupInfo(ciCustomGroupInfo, ciLabelRuleList, (List)null, (CiTemplateInfo)null, ciCustomGroupInfo.getCreateUserId(), false, attrList));
        } catch (CIServiceException var21) {
            result = Boolean.valueOf(false);
            this.log.error("创建客户群失败", var21);
        }

        return result;
    }

    private CiCustomGroupInfo createCiCustomGroupInfo(CiCustomGroupInfo pushCiCustomGroupInfo, String sceneIds) {
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

    private String[] getDimStr(String tableName, String columnName, Integer columnId) {
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

    public static void main(String[] args) {
        try {
            JaxWsDynamicClientFactory e = JaxWsDynamicClientFactory.newInstance();
            Client client = e.createClient("http://localhost:8080/coc/services/wsCustomerGroupCreateServer?wsdl");
            Object[] obj = client.invoke("createCustomerGroup", new Object[]{"KHQ000002542", "", ""});
            System.out.println(obj[0]);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }
}
