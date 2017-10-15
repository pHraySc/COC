package com.ailk.biapp.ci.localization.beijing.nonstandardimpl;

import com.ailk.biapp.ci.dao.ICiGroupAttrRelJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiCustomPushReq;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.entity.CiSysInfo;
import com.ailk.biapp.ci.localization.nonstandard.ICustomerPush;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.DataBaseAdapter;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("bJCustomerPushImpl")
@Scope("prototype")
@Transactional
public class BJCustomerPushImpl implements ICustomerPush {
    private static Logger log = Logger.getLogger(BJCustomerPushImpl.class);
    private String cmGroupId;
    private String currentSql;
    private String cmDataTableName;
    private int ciUpDateCycle;
    private static String QUERY_GROUP = "SELECT CUSTOM_GROUP_ID,CUSTOM_GROUP_NAME,UPDATE_CYCLE FROM CM_GROUP_INFO WHERE LAST_CUSTOM_GROUP_ID = ? AND CUSTOM_SOURCE_ID = \'COC\' AND CREATE_USER_ID = ? AND STATUS = 1";
    private static String QUERY_GROUP_LIST = "SELECT LIST_TABLE_NAME,DATA_DATE FROM CM_CUSTOM_LIST_INFO WHERE CUSTOM_GROUP_ID = ? AND DATA_DATE = ? ORDER BY DATA_DATE DESC";
    private static String INSERT_CM_LIST = "INSERT INTO CM_CUSTOM_LIST_INFO (LIST_TABLE_NAME,DATA_DATE,CUSTOM_GROUP_ID,CUSTOM_NUM,DATA_STATUS) VALUES (?,?,?,?,3)";
    private static String UPDATE_GROUP_NUM = "UPDATE CM_GROUP_INFO SET CUSTOM_NUM = ? WHERE CUSTOM_GROUP_ID = ?";
    private static String UPDATE_CM_LIST = "UPDATE CM_CUSTOM_LIST_INFO SET LIST_TABLE_NAME = ?, CUSTOM_NUM=? WHERE CUSTOM_GROUP_ID = ? AND DATA_DATE = ? ";
    private static String UPDATE_GROUP_INFO = "UPDATE CM_GROUP_INFO SET CUSTOM_GROUP_NAME = :groupName , CUSTOM_GROUP_DESC = :customGroupDesc , RULE_DESC = :ruleDesc WHERE CUSTOM_GROUP_ID = :groupInfoId";
    private static String INSERT_ATTR = "INSERT INTO CM_GROUP_ATTR_REL (LIST_TABLE_NAME,ATTR_COL,CUSTOM_GROUP_ID,CUSTOM_SOURCE_ID,ATTR_COL_NAME,ATTR_COL_TYPE) VALUES (?,?,?,?,?,?)";
    private CiSysInfo sysInfo;
    private CiCustomGroupInfo ciCustomGroupInfo;
    private CiCustomListInfo ciCustomListInfo;
    private CiCustomPushReq ciCustomPushReq;
    private JdbcBaseDao jdbcBaseDao;
    private SimpleJdbcTemplate cmFront;
    private SimpleJdbcTemplate cmBack;
    private DataBaseAdapter cmBackDBAdapter;
    @Autowired
    private ICustomersManagerService customersManagerService;
    @Autowired
    private ICiGroupAttrRelJDao ciGroupAttrRelJDao;

    public BJCustomerPushImpl() {
    }

    public boolean push(CiCustomGroupInfo ciCustomGroupInfo, CiCustomListInfo ciCustomListInfo, CiSysInfo sysInfo) {
        boolean flag = false;
        long t1 = System.currentTimeMillis();
        String exportSQL = Configure.getInstance().getProperty("CM_EXPORT_SQL");
        exportSQL = exportSQL.replace("${listTableName}", ciCustomListInfo.getListTableName());
        log.debug("publishToCm ==>exportSQL: " + exportSQL);
        this.ciCustomGroupInfo = ciCustomGroupInfo;
        this.ciCustomListInfo = ciCustomListInfo;
        this.sysInfo = sysInfo;
        this.ciCustomPushReq = this.customersManagerService.queryCiCustomPushReqById(sysInfo.getReqId());

        boolean attrList;
        try {
            this.jdbcBaseDao = (JdbcBaseDao)SystemServiceLocator.getInstance().getService("jdbcBaseDao");
            this.cmFront = this.jdbcBaseDao.getSimpleJdbcTemplate(Configure.getInstance().getProperty("CM_JNDI"));
            this.cmBack = this.jdbcBaseDao.getSimpleJdbcTemplate(Configure.getInstance().getProperty("CM_BACK_JNDI"));
            this.cmBackDBAdapter = this.jdbcBaseDao.getDataBaseAdapter(Configure.getInstance().getProperty("CM_BACK_DBTYPE"));
            List e = this.cmFront.queryForList(QUERY_GROUP, new Object[]{ciCustomGroupInfo.getCustomGroupId(), ciCustomGroupInfo.getCreateUserId()});
            List attrList1 = null;

            try {
                attrList1 = this.ciGroupAttrRelJDao.selectCiGroupAttrRelList(ciCustomGroupInfo.getCustomGroupId(), ciCustomListInfo.getDataTime());
            } catch (Exception var27) {
                log.error("查询客户群属性表错误！", var27);
            }

            ArrayList resultAttrs = new ArrayList();
            if(attrList1 != null && attrList1.size() > 0 && (attrList1.size() != 1 || attrList1.get(0) == null || ((CiGroupAttrRel)attrList1.get(0)).getStatus() == null || ((CiGroupAttrRel)attrList1.get(0)).getStatus().intValue() != 2)) {
                Iterator attrColumnNames = attrList1.iterator();

                while(attrColumnNames.hasNext()) {
                    CiGroupAttrRel attrColumns = (CiGroupAttrRel)attrColumnNames.next();
                    resultAttrs.add(attrColumns);
                }
            }

            StringBuffer attrColumnNames1 = new StringBuffer();
            StringBuffer attrColumns1 = new StringBuffer();
            if(!CollectionUtils.isEmpty(resultAttrs)) {
                Iterator resultMap = resultAttrs.iterator();

                while(resultMap.hasNext()) {
                    CiGroupAttrRel cmGroupListList = (CiGroupAttrRel)resultMap.next();
                    attrColumnNames1.append(",").append(cmGroupListList.getId().getAttrCol());
                    attrColumns1.append(",").append("?");
                }
            }

            exportSQL = exportSQL.replace("$(attrColumnNames)", attrColumnNames1.toString());
            if(e != null && e.size() != 0) {
                Map resultMap1 = (Map)e.get(0);
                this.cmGroupId = (String)resultMap1.get("CUSTOM_GROUP_ID");
                this.ciUpDateCycle = ((Integer)resultMap1.get("UPDATE_CYCLE")).intValue();
                List cmGroupListList1 = this.cmFront.queryForList(QUERY_GROUP_LIST, new Object[]{this.cmGroupId, ciCustomListInfo.getDataDate()});
                String param;
                if(this.ciUpDateCycle != 2 && this.ciUpDateCycle != 3) {
                    if(this.ciUpDateCycle == 1 && cmGroupListList1 != null && cmGroupListList1.size() > 0) {
                        param = (String)((Map)cmGroupListList1.get(0)).get("LIST_TABLE_NAME");
                        this.cmDataTableName = this.exportListData(exportSQL, attrColumnNames1.toString(), attrColumns1.toString(), resultAttrs);
                        this.cmFront.update(UPDATE_CM_LIST, new Object[]{this.cmDataTableName, ciCustomListInfo.getCustomNum(), this.cmGroupId, ciCustomListInfo.getDataDate()});
                        this.cmFront.update(UPDATE_GROUP_NUM, new Object[]{ciCustomListInfo.getCustomNum(), this.cmGroupId});
                        flag = true;
                        flag = this.insertAttrList(resultAttrs, this.cmGroupId, this.cmDataTableName);
                        if(flag) {
                            flag = this.deleteOldAttrList(param);
                            if(flag) {
                                flag = this.dropOldListTabel(param);
                            }
                        }
                    }
                } else if(cmGroupListList1 != null && cmGroupListList1.size() != 0) {
                    param = (String)((Map)cmGroupListList1.get(0)).get("LIST_TABLE_NAME");
                    this.cmDataTableName = this.exportListData(exportSQL, attrColumnNames1.toString(), attrColumns1.toString(), resultAttrs);
                    this.cmFront.update(UPDATE_CM_LIST, new Object[]{this.cmDataTableName, ciCustomListInfo.getCustomNum(), this.cmGroupId, ciCustomListInfo.getDataDate()});
                    this.cmFront.update(UPDATE_GROUP_NUM, new Object[]{ciCustomListInfo.getCustomNum(), this.cmGroupId});
                    flag = true;
                    flag = this.insertAttrList(resultAttrs, this.cmGroupId, this.cmDataTableName);
                    if(flag) {
                        flag = this.deleteOldAttrList(param);
                        if(flag) {
                            flag = this.dropOldListTabel(param);
                        }
                    }
                } else {
                    this.cmDataTableName = this.exportListData(exportSQL, attrColumnNames1.toString(), attrColumns1.toString(), resultAttrs);
                    this.cmFront.update(INSERT_CM_LIST, new Object[]{this.cmDataTableName, ciCustomListInfo.getDataDate(), this.cmGroupId, ciCustomListInfo.getCustomNum()});
                    this.cmFront.update(UPDATE_GROUP_NUM, new Object[]{ciCustomListInfo.getCustomNum(), this.cmGroupId});
                    flag = true;
                    flag = this.insertAttrList(resultAttrs, this.cmGroupId, this.cmDataTableName);
                }

                HashMap param1 = new HashMap();
                param1.put("groupName", ciCustomGroupInfo.getCustomGroupName());
                param1.put("groupInfoId", this.cmGroupId);
                StringBuffer desc = new StringBuffer();
                String customRules = (StringUtil.isEmpty(ciCustomGroupInfo.getCustomOptRuleShow())?"":ciCustomGroupInfo.getCustomOptRuleShow()) + (StringUtil.isEmpty(ciCustomGroupInfo.getKpiDiffRule())?"":ciCustomGroupInfo.getKpiDiffRule()) + (StringUtil.isEmpty(ciCustomGroupInfo.getProdOptRuleShow())?"":ciCustomGroupInfo.getProdOptRuleShow()) + (StringUtil.isEmpty(ciCustomGroupInfo.getLabelOptRuleShow())?"":ciCustomGroupInfo.getLabelOptRuleShow());
                desc.append(customRules);
                String _desc = desc.toString();

                try {
                    if(_desc.getBytes("UTF-8").length > 1000) {
                        _desc = CiUtil.subStringByByte(_desc, 1000, "UTF-8");
                    }
                } catch (UnsupportedEncodingException var26) {
                    log.error("截取异常");
                }

                param1.put("ruleDesc", _desc);
                param1.put("customGroupDesc", ciCustomGroupInfo.getCustomGroupDesc());
                this.cmFront.update(UPDATE_GROUP_INFO, param1);
                if(!flag) {
                    this.ciCustomPushReq.setExeInfo("已经推送过的客户群清单");
                    this.delNormal(3);
                    flag = true;
                }
            } else {
                this.cmDataTableName = this.exportListData(exportSQL, attrColumnNames1.toString(), attrColumns1.toString(), resultAttrs);
                this.insertCmGroupInfo(resultAttrs);
                flag = true;
            }

            boolean resultMap2 = flag;
            return resultMap2;
        } catch (Exception var28) {
            log.error("publishToCm( ) error:", var28);
            this.delException(this.currentSql, var28);
            attrList = false;
        } finally {
            log.info("The cost of publishToCm is :  " + (System.currentTimeMillis() - t1) + "ms");
        }

        return attrList;
    }

    private boolean dropOldListTabel(String oldListTabelName) {
        boolean result = false;
        String dropSql = "DROP TABLE " + oldListTabelName;
        StringBuffer existsSqlBuf = new StringBuffer();
        existsSqlBuf.append("SELECT COUNT(1) FROM ").append(oldListTabelName);
        boolean flag = true;

        try {
            this.cmFront.getJdbcOperations().execute(existsSqlBuf.toString());
        } catch (Exception var7) {
            log.warn("table " + oldListTabelName + " is not exists!");
            flag = false;
        }

        if(flag) {
            this.cmFront.getJdbcOperations().execute(dropSql);
        }

        result = true;
        return result;
    }

    private boolean deleteOldAttrList(String oldListTabelName) {
        boolean result = false;
        String delSql = "DELETE FROM CM_GROUP_ATTR_REL WHERE LIST_TABLE_NAME = ?";

        try {
            this.cmFront.getJdbcOperations().update(delSql, new Object[]{oldListTabelName});
            result = true;
        } catch (Exception var5) {
            log.error("删除旧清单属性错误", var5);
        }

        return result;
    }

    private void insertCmGroupInfo(List<CiGroupAttrRel> attrList) {
        String insetCusomerSql = Configure.getInstance().getProperty("CM_INSERT_GROUPINFO_SQL");
        StringBuffer desc = new StringBuffer();
        String customRules = (StringUtil.isEmpty(this.ciCustomGroupInfo.getCustomOptRuleShow())?"":this.ciCustomGroupInfo.getCustomOptRuleShow()) + (StringUtil.isEmpty(this.ciCustomGroupInfo.getKpiDiffRule())?"":this.ciCustomGroupInfo.getKpiDiffRule()) + (StringUtil.isEmpty(this.ciCustomGroupInfo.getProdOptRuleShow())?"":this.ciCustomGroupInfo.getProdOptRuleShow()) + (StringUtil.isEmpty(this.ciCustomGroupInfo.getLabelOptRuleShow())?"":this.ciCustomGroupInfo.getLabelOptRuleShow());
        desc.append(customRules);
        String _desc = desc.toString();

        try {
            if(_desc.getBytes("UTF-8").length > 1000) {
                _desc = CiUtil.subStringByByte(_desc, 1000, "UTF-8");
            }
        } catch (UnsupportedEncodingException var8) {
            log.error("截取异常");
        }

        log.debug("publishToCm insetCusomerSql:" + insetCusomerSql);
        String groupId = CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L);

        try {
            this.cmFront.update(insetCusomerSql, new Object[]{groupId, this.ciCustomGroupInfo.getCustomGroupName(), this.ciCustomGroupInfo.getCustomGroupDesc(), this.ciCustomGroupInfo.getCreateUserId(), new Date(), _desc, "COC", this.ciCustomListInfo.getCustomNum(), Integer.valueOf(3), this.ciCustomGroupInfo.getUpdateCycle(), Integer.valueOf(1), this.ciCustomGroupInfo.getCustomGroupId()});
            String e = "INSERT INTO CM_CUSTOM_LIST_INFO (LIST_TABLE_NAME,DATA_DATE,CUSTOM_GROUP_ID,CUSTOM_NUM,DATA_STATUS) VALUES (?,?,?,?,3)";
            this.cmFront.update(e, new Object[]{this.cmDataTableName, this.ciCustomListInfo.getDataDate(), groupId, this.ciCustomListInfo.getCustomNum()});
            this.insertAttrList(attrList, groupId, this.cmDataTableName);
        } catch (Exception var9) {
            log.error("publishToCm error:", var9);
            if(this.isExistsSame()) {
                this.ciCustomPushReq.setExeInfo("已经推送过的客户群清单");
                this.delNormal(0);
            } else {
                this.delException(this.currentSql, var9);
            }
        }

    }

    private boolean insertAttrList(List<CiGroupAttrRel> attrList, String groupId, String listTabelName) {
        boolean result = false;

        try {
            if(attrList == null || attrList.size() == 0) {
                result = true;
                return result;
            }

            ArrayList e = new ArrayList();
            Iterator i$ = attrList.iterator();

            while(i$.hasNext()) {
                CiGroupAttrRel attr = (CiGroupAttrRel)i$.next();
                Object[] values = new Object[]{listTabelName, attr.getId().getAttrCol(), groupId, "COC", attr.getAttrColName(), attr.getAttrColType()};
                e.add(values);
            }

            this.cmFront.batchUpdate(INSERT_ATTR, e);
            result = true;
        } catch (Exception var9) {
            log.error("保存属性错误", var9);
        }

        return result;
    }

    private String exportListData(String exportSQL, String attrColumnNames, String attrColumns, List<CiGroupAttrRel> ciGroupAttrRels) throws Exception {
        String cmTemplateTable = Configure.getInstance().getProperty("CM_TMP_TABLE");
        String cmDataTableName = cmTemplateTable.substring(0, cmTemplateTable.lastIndexOf("_")) + "_" + CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L);
        this.cmDataTableName = cmDataTableName;
        String nameWithSchema = cmDataTableName.toLowerCase();
        String cmSchema = Configure.getInstance().getProperty("CM_BACK_SCHEMA");
        if(StringUtil.isNotEmpty(cmSchema)) {
            nameWithSchema = cmSchema + "." + nameWithSchema;
        }

        String createsql = this.cmBackDBAdapter.getCreateAsTableSql(nameWithSchema, cmTemplateTable, Configure.getInstance().getProperty("CM_TABLESPACE_BACK"));
        log.debug("publishToCm createsql:" + createsql);
        this.currentSql = createsql;
        this.cmBack.getJdbcOperations().execute(createsql);
        String cmKeyCoulmn = Configure.getInstance().getProperty("CM_KEYCOLUMN");
        final String ciKeyCoulmn = Configure.getInstance().getProperty("MAIN_COLUMN");
        StringBuffer insertDateSqlBuf = new StringBuffer();
        insertDateSqlBuf.append("insert into ").append(nameWithSchema).append(" (").append(cmKeyCoulmn).append(attrColumnNames).append(") values(?").append(attrColumns).append(")");
        String insertDataSql = insertDateSqlBuf.toString();
        log.debug("publishToCm insertDataSql:" + insertDataSql);
        this.currentSql = insertDataSql;
        final String[] attrColumnNamesArr;
        if(!attrColumnNames.equals("") && attrColumnNames.length() > 0) {
            attrColumnNamesArr = attrColumnNames.substring(1, attrColumnNames.length()).split(",");
            String start = Configure.getInstance().getProperty("CM_BACK_DBTYPE");

            StringBuffer pagedSql;
            for(Iterator pageSize = ciGroupAttrRels.iterator(); pageSize.hasNext(); this.cmBack.getJdbcOperations().execute(pagedSql.toString())) {
                CiGroupAttrRel batchCount = (CiGroupAttrRel)pageSize.next();
                pagedSql = (new StringBuffer("ALTER TABLE ")).append(nameWithSchema).append(" ADD ");
                if("ORACLE".equalsIgnoreCase(start)) {
                    pagedSql.append(batchCount.getId().getAttrCol()).append(" ");
                    pagedSql.append(batchCount.getAttrColType());
                } else {
                    pagedSql.append("COLUMN ").append(batchCount.getId().getAttrCol()).append(" ");
                    pagedSql.append(batchCount.getAttrColType());
                }
            }
        } else {
            attrColumnNamesArr = new String[0];
        }

        int var21 = 1;
        int var22 = 100000;
        String var23 = Configure.getInstance().getProperty("BATCH_COUNT");
        if(StringUtil.isNotEmpty(var23)) {
            try {
                var22 = Integer.valueOf(var23).intValue();
            } catch (Exception var20) {
                String datas = "字符串->数字转换错误";
                log.error(datas, var20);
                throw new Exception(datas, var20);
            }
        }

        while(true) {
            String var24 = this.jdbcBaseDao.getBackDataBaseAdapter().getPagedSql(exportSQL, var21, var22);
            log.debug("publishToCm ==>pagedSQL: " + var24);
            final List var25 = this.jdbcBaseDao.getBackSimpleJdbcTemplate().queryForList(var24, new Object[0]);
            if(var25 == null) {
                break;
            }

            this.cmBack.getJdbcOperations().batchUpdate(insertDataSql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    String tempArr = (String)((Map)var25.get(i)).get(ciKeyCoulmn.toLowerCase());
                    ps.setString(1, tempArr);

                    for(int j = 0; j < attrColumnNamesArr.length; ++j) {
                        Object tempArrs = ((Map)var25.get(i)).get(attrColumnNamesArr[j]);
                        ps.setObject(j + 2, tempArrs);
                    }

                }

                public int getBatchSize() {
                    return var25.size();
                }
            });
            if(var25.size() < var22) {
                break;
            }

            ++var21;
        }

        return cmDataTableName;
    }

    private void delNormal(int status) {
        this.ciCustomPushReq.setStatus(Integer.valueOf(status));
        this.customersManagerService.saveCiCustomPushReq(this.ciCustomPushReq);
    }

    private void delException(String msg, Exception e) {
        this.dropOldListTabel(this.cmDataTableName);
        this.ciCustomPushReq.setStatus(Integer.valueOf(0));
        String expctionInfo = "";

        try {
            StringWriter e2 = new StringWriter();
            PrintWriter pw = new PrintWriter(e2);
            e.printStackTrace(pw);
            expctionInfo = e2.toString();
        } catch (Exception var6) {
            log.error("异常情况后处理错误", var6);
        }

        expctionInfo = msg + expctionInfo;
        if(expctionInfo.length() > 2048) {
            expctionInfo = expctionInfo.substring(0, 1024);
        }

        this.ciCustomPushReq.setExeInfo(expctionInfo);
        this.customersManagerService.saveCiCustomPushReq(this.ciCustomPushReq);
    }

    public boolean isExistsSame() {
        CiCustomPushReq queryParam = new CiCustomPushReq();
        queryParam.setUserId(this.ciCustomGroupInfo.getCreateUserId());
        queryParam.setSysId(this.sysInfo.getSysId());
        queryParam.setListTableName(this.ciCustomListInfo.getListTableName());
        queryParam.setStatus(Integer.valueOf(3));
        List pushreq = this.customersManagerService.queryCiCustomPushReq(queryParam);
        return pushreq != null && pushreq.size() > 0;
    }
}
