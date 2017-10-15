package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.IMcdInterfaceJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiMdaSysTable;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.model.CiLabelCategoryRel;
import com.ailk.biapp.ci.model.LabelColumnTable;
import com.ailk.biapp.ci.model.TargetCustomersAttr;
import com.ailk.biapp.ci.model.TargetCustomersModel;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository("iMcdInterfaceJDao")
public class McdInterfaceJDaoImpl extends JdbcBaseDao implements IMcdInterfaceJDao {
    private Logger log = Logger.getLogger(McdInterfaceJDaoImpl.class);

    public McdInterfaceJDaoImpl() {
    }

    public List<TargetCustomersModel> getCustomerByUserId(String userId, String sysId) {
        HashMap paramMap = new HashMap();
        paramMap.put("userId", userId);
        String sql = " SELECT a.CUSTOM_GROUP_ID target_customers_id, a.CUSTOM_GROUP_NAME target_customers_name, a.CUSTOM_NUM target_customers_num,b.data_date target_customers_base_moth,a.UPDATE_CYCLE target_customers_cycle,b.LIST_TABLE_NAME  target_customers_tab_name FROM CI_CUSTOM_GROUP_INFO a LEFT JOIN (  SELECT b1.data_date,b1.LIST_TABLE_NAME,b1.CUSTOM_GROUP_ID FROM ci_custom_list_info b1, ( SELECT MAX(data_date) data_date, CUSTOM_GROUP_ID FROM ci_custom_list_info GROUP BY CUSTOM_GROUP_ID ) b2  WHERE b1.CUSTOM_GROUP_ID = b2.CUSTOM_GROUP_ID AND b1.data_date = b2.data_date ) b ON a.CUSTOM_GROUP_ID = b.CUSTOM_GROUP_ID WHERE a.STATUS=1 and a.DATA_STATUS=3 and ( a.CREATE_USER_ID=:userId or a.is_private = 0)";
        sql = sql + " order by a.CUSTOM_GROUP_NAME";
        this.log.debug("getCustomeryUserId--" + sql);
        Object list = new ArrayList();

        try {
            list = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(TargetCustomersModel.class), paramMap);
        } catch (Exception var7) {
            this.log.error("mcd调用接口getCustomerByUserId报错", var7);
        }

        return (List)list;
    }

    public TargetCustomersModel getCustomerById(String id) {
        HashMap paramMap = new HashMap();
        paramMap.put("id", id);
        String sql = " SELECT A.CUSTOM_GROUP_ID TARGET_CUSTOMERS_ID,A.CREATE_TIME CREATE_TIME,A.CUSTOM_GROUP_DESC CUSTOMERS_DESC, A.CUSTOM_GROUP_NAME TARGET_CUSTOMERS_NAME, A.CUSTOM_NUM TARGET_CUSTOMERS_NUM,B.DATA_DATE TARGET_CUSTOMERS_BASE_MONTH,A.UPDATE_CYCLE TARGET_CUSTOMERS_CYCLE,B.LIST_TABLE_NAME TARGET_CUSTOMERS_TAB_NAME,A.CREATE_USER_ID CREATE_USER_ID, A.IS_PRIVATE IS_PRIVATE,B.ISSTRENGTHEN FLAG FROM CI_CUSTOM_GROUP_INFO A LEFT JOIN (  SELECT B1.DATA_DATE,B1.LIST_TABLE_NAME,B1.CUSTOM_GROUP_ID,B1.ISSTRENGTHEN FROM CI_CUSTOM_LIST_INFO B1, ( SELECT MAX(DATA_DATE) DATA_DATE, CUSTOM_GROUP_ID FROM CI_CUSTOM_LIST_INFO GROUP BY CUSTOM_GROUP_ID ) B2  WHERE B1.CUSTOM_GROUP_ID = B2.CUSTOM_GROUP_ID AND B1.DATA_DATE = B2.DATA_DATE ) B ON A.CUSTOM_GROUP_ID = B.CUSTOM_GROUP_ID WHERE A.CUSTOM_GROUP_ID =:id";
        this.log.debug("getCustomerById--" + sql);
        List list = null;
        TargetCustomersModel targetCustomersModel = null;

        try {
            list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(TargetCustomersModel.class), paramMap);
            if(list != null && list.size() > 0) {
                targetCustomersModel = (TargetCustomersModel)list.get(0);
            }
        } catch (Exception var7) {
            this.log.error("mcd调用接口getCustomerById报错", var7);
        }

        return targetCustomersModel;
    }

    public TargetCustomersModel getCustomerById(String id, String date) {
        HashMap paramMap = new HashMap();
        paramMap.put("date", date);
        paramMap.put("id", id);
        StringBuffer sql = new StringBuffer("SELECT A.CUSTOM_GROUP_ID TARGET_CUSTOMERS_ID,A.CREATE_TIME CREATE_TIME,A.CUSTOM_GROUP_DESC CUSTOMERS_DESC, A.CUSTOM_GROUP_NAME TARGET_CUSTOMERS_NAME, A.CUSTOM_NUM TARGET_CUSTOMERS_NUM,B.DATA_DATE TARGET_CUSTOMERS_BASE_MONTH,A.UPDATE_CYCLE TARGET_CUSTOMERS_CYCLE,B.LIST_TABLE_NAME TARGET_CUSTOMERS_TAB_NAME,A.CREATE_USER_ID CREATE_USER_ID, A.IS_PRIVATE IS_PRIVATE,B.ISSTRENGTHEN FLAG FROM CI_CUSTOM_GROUP_INFO A LEFT JOIN ( ");
        sql.append(" SELECT B1.DATA_DATE,B1.LIST_TABLE_NAME,B1.CUSTOM_GROUP_ID,B1.ISSTRENGTHEN FROM CI_CUSTOM_LIST_INFO B1, ( SELECT MAX(DATA_DATE) DATA_DATE, CUSTOM_GROUP_ID FROM CI_CUSTOM_LIST_INFO ");
        sql.append(" WHERE DATA_DATE = :date ");
        sql.append(" GROUP BY CUSTOM_GROUP_ID ) B2 ");
        sql.append(" WHERE B1.CUSTOM_GROUP_ID = B2.CUSTOM_GROUP_ID AND B1.DATA_DATE = B2.DATA_DATE ) B ON A.CUSTOM_GROUP_ID = B.CUSTOM_GROUP_ID WHERE A.CUSTOM_GROUP_ID =:id");
        this.log.debug("getCustomerById--" + sql.toString());
        List list = null;
        TargetCustomersModel targetCustomersModel = null;

        try {
            list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(TargetCustomersModel.class), paramMap);
            if(list != null && list.size() > 0) {
                targetCustomersModel = (TargetCustomersModel)list.get(0);
            }
        } catch (Exception var8) {
            this.log.error("mcd调用接口getCustomerById报错", var8);
        }

        return targetCustomersModel;
    }

    public List<TargetCustomersAttr> getTargetCustomersAttr(String id) {
        HashMap paramMap = new HashMap();
        paramMap.put("id", id);
        String sql = " select ATTR_COL code, ATTR_COL_NAME name,ATTR_COL_TYPE type,STATUS status from CI_GROUP_ATTR_REL where CUSTOM_GROUP_ID=:id ";
        this.log.debug("getTargetCustomersAttr--" + sql);
        List list = null;

        try {
            list = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(TargetCustomersAttr.class), paramMap);
        } catch (Exception var6) {
            this.log.error("mcd调用接口getTargetCustomersAttr报错", var6);
        }

        return list;
    }

    public List<CiLabelCategoryRel> getLabelIdList(String categoryId) {
        HashMap paramMap = new HashMap();
        paramMap.put("categoryId", categoryId);
        String sql = "SELECT LABEL_ID FROM CI_LABEL_CATEGORY_REL WHERE CONTENT_CATEGORY_ID = :categoryId";
        this.log.debug("getCustomeryUserId--" + sql);
        Object list = new ArrayList();

        try {
            list = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiLabelCategoryRel.class), paramMap);
        } catch (Exception var6) {
            this.log.error("mcd调用接口getCustomerByUserId报错", var6);
        }

        return (List)list;
    }

    public List<CiLabelCategoryRel> getCiLabelCategoryRelByLabelId(Integer labelId) {
        HashMap paramMap = new HashMap();
        paramMap.put("labelId", labelId);
        String sql = "SELECT * FROM CI_LABEL_CATEGORY_REL WHERE LABEL_ID = :labelId";
        this.log.debug("getCustomeryUserId--" + sql);
        new ArrayList();
        List list = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiLabelCategoryRel.class), paramMap);
        return list;
    }

    public CiMdaSysTable getMdaSysTable(String labelId) {
        HashMap paramMap = new HashMap();
        paramMap.put("labelId", labelId);
        String sql = "select * from ci_mda_sys_table where table_id  in(  select table_id from ci_mda_sys_table_column  where column_id in (select column_id from ci_label_ext_info where label_id = :labelId))";
        Object list = new ArrayList();

        try {
            list = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiMdaSysTable.class), paramMap);
        } catch (Exception var6) {
            this.log.error("mcd调用接口getCustomerByUserId报错", var6);
        }

        return ((List)list).size() > 0?(CiMdaSysTable)((List)list).get(0):null;
    }

    public List<LabelColumnTable> getLabelColumnTableList(Map<String, Object> paramMap) throws Exception {
        StringBuffer sql = (new StringBuffer("SELECT C.LABEL_ID AS LABELID,D.LABEL_NAME AS LABELNAME,B.COLUMN_NAME AS COLUMNNAME,")).append(" CONCAT(A.TABLE_NAME,CONCAT(\'_\',D.DATA_DATE)) AS TABLENAME").append(" FROM CI_LABEL_INFO D").append(" LEFT JOIN CI_LABEL_EXT_INFO C ON D.LABEL_ID = C.LABEL_ID ").append(" LEFT JOIN CI_MDA_SYS_TABLE_COLUMN B ON B.COLUMN_ID = C.COLUMN_ID ").append(" LEFT JOIN CI_MDA_SYS_TABLE A ON A.TABLE_ID = B.TABLE_ID").append(" WHERE D.LABEL_TYPE_ID =").append(1).append(" AND D.DATA_STATUS_ID=").append(2).append(" AND C.IS_STAT_USER_NUM=").append(1);
        if(paramMap != null && paramMap.get("labelIdLevelDescs") != null) {
            sql.append(" and (");
            int list = 0;

            for(Iterator var5 = ((List)paramMap.get("labelIdLevelDescs")).iterator(); var5.hasNext(); ++list) {
                CiLabelInfo ciLabelInfo = (CiLabelInfo)var5.next();
                if(list == 0) {
                    sql.append(" D.LABEL_ID_LEVEL_DESC LIKE \'").append(ciLabelInfo.getLabelIdLevelDesc()).append("%\'");
                } else {
                    String str = "OR D.LABEL_ID_LEVEL_DESC LIKE \'" + ciLabelInfo.getLabelIdLevelDesc() + "%\'";
                    if(sql.indexOf(str) < 0) {
                        sql.append(" " + str);
                    }
                }
            }

            sql.append(")");
        }

        sql.append(" order by C.LABEL_ID");
        new ArrayList();
        this.log.info("根据labelIds查询宽表表明和列名，sql============" + sql.toString());
        List var7 = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(LabelColumnTable.class), new Object[0]);
        return var7;
    }

    public CiMdaSysTableColumn getMdaSysTableColumn(String labelId) {
        HashMap paramMap = new HashMap();
        paramMap.put("labelId", labelId);
        String sql = "select * from ci_mda_sys_table_column  where column_id in (select column_id from ci_label_ext_info where label_id = :labelId)";
        Object list = new ArrayList();

        try {
            list = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiMdaSysTableColumn.class), paramMap);
        } catch (Exception var6) {
            this.log.error("mcd调用接口getCustomerByUserId报错", var6);
        }

        return ((List)list).size() > 0?(CiMdaSysTableColumn)((List)list).get(0):null;
    }

    public List<String> getUserList(String wideTable, String wideColumn, String customListTable, String customListColumn) {
        HashMap paramMap = new HashMap();
        paramMap.put("flag", "1");
        String sql = "SELECT USER_ID AS CONTENT_CATEGORY_ID FROM " + wideTable + " WHERE USER_ID IN(SELECT PRODUCT_NO FROM " + customListTable + ") AND " + wideColumn + " = :flag";
        Object list = new ArrayList();

        try {
            list = this.getBackSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(String.class), paramMap);
        } catch (Exception var9) {
            this.log.error("mcd调用接口getCustomerByUserId报错", var9);
        }

        return (List)list;
    }

    public List<CiLabelInfo> getCiLabelInfoList(String parentIds) {
        HashMap paramMap = new HashMap();
        String sql = "SELECT * FROM CI_LABEL_INFO WHERE 1=1 ";
        if(StringUtil.isNotEmpty(parentIds)) {
            sql = sql + " AND PARENT_ID IN (" + parentIds + ")";
        }

        this.log.info("根据标签所属的父id查询标签信息sql:" + sql);
        new ArrayList();
        List list = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiLabelInfo.class), paramMap);
        return list;
    }

    public void updateUserList(String customListTable, String contentColumn, String val, String productNo) {
        HashMap paramMap = new HashMap();
        paramMap.put("contentVal", val);
        paramMap.put("productNo", productNo);
        String sql = "UPDATE " + customListTable + " SET " + contentColumn + " = :contentVal " + " WHERE  PRODUCT_NO = :productNo";

        try {
            this.getBackSimpleJdbcTemplate().update(sql, paramMap);
        } catch (Exception var8) {
            this.log.error("mcd调用接口getCustomerByUserId报错", var8);
        }

    }

    public List<Map<String, Object>> getExcuteSqlResultList(String sqlStr) {
        this.log.info("查询清单在宽表中的值sql========" + sqlStr);
        List list = this.getBackSimpleJdbcTemplate().getJdbcOperations().queryForList(sqlStr);
        return list;
    }

    public boolean getTableExists(String tableName) {
        boolean flag = true;

        try {
            StringBuffer e = new StringBuffer();
            e.append("SELECT COUNT(1) FROM ").append(tableName);
            this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(e.toString());
        } catch (Exception var4) {
            this.log.warn("table " + tableName + " is not exists!");
            flag = false;
        }

        return flag;
    }
}
