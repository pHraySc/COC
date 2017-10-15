package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.ICustomGroupInfoJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.JDBCUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CustomGroupInfoJDaoImpl extends JdbcBaseDao implements ICustomGroupInfoJDao {
    private Logger log = Logger.getLogger(CustomGroupInfoJDaoImpl.class);

    public CustomGroupInfoJDaoImpl() {
    }

    public int getCustomersAutoMacthFlag(String customGroupId) throws Exception {
        boolean customersAutoMacthFlag = false;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT PRODUCT_AUTO_MACTH_FLAG FROM CI_CUSTOM_GROUP_INFO WHERE CUSTOM_GROUP_ID = \'").append(customGroupId).append("\'");
        int customersAutoMacthFlag1 = this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new Object[0]);
        return customersAutoMacthFlag1;
    }

    public int getCustomersTotolCount(CiCustomGroupInfo bean) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(CUSTOM_GROUP_ID)").append(" FROM CI_CUSTOM_GROUP_INFO").append(" WHERE 1=1 ").append(" AND STATUS =").append(1);
        if(StringUtil.isNotEmpty(bean.getIsMyCustom()) && bean.getIsMyCustom().equals("true")) {
            sql.append(" AND CREATE_USER_ID = :userId");
        }

        if(StringUtil.isNotEmpty(bean.getCityId())) {
            sql.append(" AND  CREATE_CITY_ID = :cityId");
        }

        if(StringUtil.isNotEmpty(bean.getModifyStartDate())) {
            sql.append(" AND NEW_MODIFY_TIME>= :modifyStartDate");
        }

        if(StringUtil.isNotEmpty(bean.getModifyEndDate())) {
            sql.append(" AND NEW_MODIFY_TIME<= :modifyEndDate");
        }

        if(StringUtil.isNotEmpty(bean.getMinCustomNum())) {
            sql.append(" AND CUSTOM_NUM>= :minCustomNum");
        }

        if(StringUtil.isNotEmpty(bean.getMaxCustomNum())) {
            sql.append(" AND CUSTOM_NUM<= :maxCustomNum");
        }

        String dbType = Configure.getInstance().getProperty("CI_DBTYPE");
        if(StringUtil.isNotEmpty(bean.getCustomGroupName())) {
            sql.append(" AND (").append(" (LOWER(CUSTOM_GROUP_NAME) LIKE LOWER(\'%\'||").append(":customGroupName").append("||\'%\'))").append(" OR (LOWER(CUSTOM_GROUP_DESC) LIKE LOWER(\'%\'||").append(":customGroupName").append("||\'%\'))").append(" OR (LOWER(PROD_OPT_RULE_SHOW) LIKE LOWER(\'%\'||").append(":customGroupName").append("||\'%\'))");
            if("DB2".equalsIgnoreCase(dbType)) {
                sql.append(" OR (PROD_OPT_RULE_SHOW LIKE \'%\'||").append(":customGroupName").append("||\'%\')");
            } else {
                sql.append(" OR (LOWER(LABEL_OPT_RULE_SHOW) LIKE LOWER(\'%\'||").append(":customGroupName").append("||\'%\'))");
            }

            sql.append(" OR (LOWER(CUSTOM_OPT_RULE_SHOW) LIKE LOWER(\'%\'||").append(":customGroupName").append("||\'%\'))").append(" OR (LOWER(KPI_DIFF_RULE) LIKE LOWER(\'%\'||").append(":customGroupName").append("||\'%\'))").append(" ) ");
        }

        if(StringUtil.isNotEmpty(bean.getCreateTime())) {
            sql.append(" AND CREATE_TIME = :createTime");
        }

        sql.append(" AND (UPDATE_CYCLE != ").append(4).append(" or (IS_LABEL_OFFLINE is null or IS_LABEL_OFFLINE = ").append(0).append(") )");
        String templateMenu = Configure.getInstance().getProperty("TEMPLATE_MENU");
        String sceneIds;
        if(templateMenu.equals("false")) {
            if(StringUtil.isNotEmpty(bean.getCreateUserId())) {
                sql.append(" AND (IS_PRIVATE = ").append(0).append("  or ( IS_PRIVATE = ").append(1).append("  AND  CREATE_USER_ID = :createUserId ) ) ");
            } else if(StringUtil.isNotEmpty(bean.getCreateCityId())) {
                sql.append(" AND (IS_PRIVATE = ").append(0).append("  or ( IS_PRIVATE = ").append(1).append("  AND  CREATE_CITY_ID = :createCityId ) ) ");
            }
        } else {
            sceneIds = Configure.getInstance().getProperty("CENTER_CITYID");
            if(StringUtil.isNotEmpty(bean.getCreateUserId())) {
                sql.append(" AND ( CREATE_USER_ID = :createUserId or ( IS_PRIVATE = ").append(0).append("  AND  ( CREATE_CITY_ID = :createCityId or  CREATE_CITY_ID = ").append(sceneIds).append(" ) ) ) ");
            } else if(StringUtil.isNotEmpty(bean.getCreateCityId())) {
                sql.append(" AND ( CREATE_CITY_ID = :createCityId or ( IS_PRIVATE = ").append(0).append("  AND   CREATE_CITY_ID = ").append(sceneIds).append("  ) ) ");
            }
        }

        if(StringUtil.isNotEmpty(bean.getIsPrivate())) {
            sql.append(" AND IS_PRIVATE = :isPrivate");
        }

        if(StringUtil.isNotEmpty(bean.getUpdateCycle())) {
            sql.append(" AND UPDATE_CYCLE = :updateCycle");
        }

        sceneIds = bean.getSceneId();
        String sqlSceneIds = "";
        if(null != sceneIds && sceneIds.length() >= 1) {
            sceneIds = sceneIds.substring(0, sceneIds.length() - 1);
            String[] sdf = sceneIds.split(",");
            String[] startMonth = sdf;
            int startDay = sdf.length;

            for(int endDate = 0; endDate < startDay; ++endDate) {
                String sceneId = startMonth[endDate];
                sqlSceneIds = sqlSceneIds + " OR SENCET.SCENE_ID=\'" + sceneId + "\' ";
            }
        }

        if(StringUtil.isNotEmpty(bean.getSceneId())) {
            sql.append(" AND CUSTOM_GROUP_ID IN  ( SELECT  SENCET.CUSTOM_GROUP_ID FROM CI_CUSTOM_SCENE_REL SENCET  WHERE SENCET.STATUS=1 ");
            sql.append(" AND (SENCET.SCENE_ID=\'");
            sql.append(ServiceConstants.ALL_SCENE_ID + "\'");
            sql.append(sqlSceneIds);
            sql.append("))");
        }

        SimpleDateFormat var12;
        String var16;
        if(StringUtil.isNotEmpty(bean.getStartDate()) || StringUtil.isNotEmpty(bean.getEndDate())) {
            var12 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat var14 = new SimpleDateFormat("yyyy-MM-dd");
            Date var17;
            if(StringUtil.isNotEmpty(bean.getStartDate())) {
                var16 = var14.format(bean.getStartDate());
                var17 = var12.parse(var16 + " 00:00:00");
                bean.setStartDate(var17);
                sql.append(" AND CREATE_TIME >= :startDate");
            }

            if(StringUtil.isNotEmpty(bean.getEndDate())) {
                var16 = var14.format(bean.getEndDate());
                var17 = var12.parse(var16 + " 23:59:59");
                bean.setEndDate(var17);
                sql.append(" AND CREATE_TIME <= :endDate");
            }
        }

        if("1".equals(bean.getDateType())) {
            String var13 = DateUtil.getCurrentDay();
            sql.append(" AND CREATE_TIME > to_date(\'").append(var13).append("\',\'yyyy-MM-dd HH24:mi:ss\')");
        }

        String var15;
        if("2".equals(bean.getDateType())) {
            var12 = new SimpleDateFormat("yyyyMM");
            var15 = DateUtil.getFrontMonth(0, DateUtil.date2String(new Date(), "yyyyMM"));
            var16 = DateUtil.date2String(DateUtil.firstDayOfMonth(var12.parse(var15)), "yyyy-MM-dd HH:mm:ss");
            System.out.println(var16);
            sql.append(" AND CREATE_TIME > to_date(\'").append(var16).append("\',\'yyyy-MM-dd HH24:mi:ss\')");
        }

        if("3".equals(bean.getDateType())) {
            var12 = new SimpleDateFormat("yyyyMM");
            var15 = DateUtil.getFrontMonth(2, DateUtil.date2String(new Date(), "yyyyMM"));
            var16 = DateUtil.date2String(DateUtil.firstDayOfMonth(var12.parse(var15)), "yyyy-MM-dd HH:mm:ss");
            sql.append(" AND CREATE_TIME > to_date(\'").append(var16).append("\',\'yyyy-MM-dd HH24:mi:ss\')");
        }

        if(bean.getCreateTypeId() != null && bean.getCreateTypeId().intValue() != 0) {
            sql.append(" AND CREATE_TYPE_ID = :createTypeId");
        }

        if(StringUtil.isNotEmpty(bean.getLabelName())) {
            sql.append(" AND CUSTOM_GROUP_ID IN ").append("( ").append(" SELECT R.CUSTOM_ID ").append(" FROM CI_LABEL_INFO L,CI_LABEL_RULE R ").append(" WHERE R.ELEMENT_TYPE = 2 ").append(" AND R.CALCU_ELEMENT = ").append(this.getDataBaseAdapter().getIntToChar("L.LABEL_ID")).append(" AND L.LABEL_NAME LIKE \'%").append(bean.getLabelName()).append("%\')");
        }

        if(StringUtil.isNotEmpty(bean.getTemplateName())) {
            sql.append(" AND TEMPLATE_ID IN ").append("( ").append(" SELECT TEMPLATE_ID").append(" FROM CI_TEMPLATE_INFO").append(" WHERE TEMPLATE_NAME LIKE \'%").append(bean.getTemplateName()).append("%\')");
        }

        if(bean.getDataStatus() != null) {
            sql.append(" AND DATA_STATUS = :dataStatus");
        }

        this.log.debug("sql=" + sql.toString());
        return this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new BeanPropertySqlParameterSource(bean));
    }

    public List<CiCustomGroupInfo> getCustomsersList(Pager pager, CiCustomGroupInfo bean, String queryType) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT CCGI.*, T4.ATTENTION_TIME ,CASE WHEN T3.SUMCC IS NULL THEN 0 ELSE T3.SUMCC END USE_TIMES");
        if(queryType == null || !"callByBackStage".equals(queryType)) {
            sql.append(", CASE WHEN T4.SUMCC IS NULL THEN \'false\' ELSE \'true\' END ISATTENTION ");
        }

        sql.append(" FROM CI_CUSTOM_GROUP_INFO CCGI LEFT JOIN (SELECT  T1.CUSTOM_ID,COUNT(T1.CUSTOM_ID) SUMCC FROM CI_USER_USE_CUSTOM T1 WHERE T1.IS_TEMPLATE = 0 GROUP BY T1.CUSTOM_ID) T3 ON CCGI.CUSTOM_GROUP_ID = T3.CUSTOM_ID");
        if(queryType == null || !"callByBackStage".equals(queryType)) {
            sql.append(" LEFT JOIN (SELECT  T11.USER_ID,T11.ATTENTION_TIME,T11.CUSTOM_GROUP_ID,COUNT(T11.CUSTOM_GROUP_ID) SUMCC FROM CI_USER_ATTENTION_CUSTOM T11 WHERE T11.USER_ID = \'" + PrivilegeServiceUtil.getUserId() + "\' GROUP BY T11.CUSTOM_GROUP_ID,T11.USER_ID,T11.ATTENTION_TIME) T4 ON CCGI.CUSTOM_GROUP_ID = T4.CUSTOM_GROUP_ID");
        }

        sql.append(" WHERE 1=1").append(" AND STATUS = ").append(1);
        if(StringUtil.isNotEmpty(bean.getIsMyCustom()) && bean.getIsMyCustom().equals("true")) {
            sql.append(" AND CREATE_USER_ID = :userId");
        }

        if(StringUtil.isNotEmpty(bean.getModifyStartDate())) {
            sql.append(" AND NEW_MODIFY_TIME>= :modifyStartDate");
        }

        if(StringUtil.isNotEmpty(bean.getModifyEndDate())) {
            sql.append(" AND NEW_MODIFY_TIME<= :modifyEndDate");
        }

        String dbType = Configure.getInstance().getProperty("CI_DBTYPE");
        if(StringUtil.isNotEmpty(bean.getCustomGroupName())) {
            sql.append(" AND (").append(" (LOWER(CUSTOM_GROUP_NAME) LIKE LOWER(\'%\'||").append(":customGroupName").append("||\'%\'))").append(" OR (LOWER(CUSTOM_GROUP_DESC) LIKE LOWER(\'%\'||").append(":customGroupName").append("||\'%\'))").append(" OR (LOWER(PROD_OPT_RULE_SHOW) LIKE LOWER(\'%\'||").append(":customGroupName").append("||\'%\'))");
            if("DB2".equalsIgnoreCase(dbType)) {
                sql.append(" OR (PROD_OPT_RULE_SHOW LIKE \'%\'||").append(":customGroupName").append("||\'%\')");
            } else {
                sql.append(" OR (LOWER(LABEL_OPT_RULE_SHOW) LIKE LOWER(\'%\'||").append(":customGroupName").append("||\'%\'))");
            }

            sql.append(" OR (LOWER(CUSTOM_OPT_RULE_SHOW) LIKE LOWER(\'%\'||").append(":customGroupName").append("||\'%\'))").append(" OR (LOWER(KPI_DIFF_RULE) LIKE LOWER(\'%\'||").append(":customGroupName").append("||\'%\'))").append(" ) ");
        }

        if(StringUtil.isNotEmpty(bean.getCreateTime())) {
            sql.append(" AND CREATE_TIME = :createTime");
        }

        if(bean.getDataStatus() != null) {
            sql.append(" AND DATA_STATUS = :dataStatus");
        }

        if(StringUtil.isNotEmpty(bean.getMinCustomNum())) {
            sql.append(" AND CUSTOM_NUM>= :minCustomNum");
        }

        if(StringUtil.isNotEmpty(bean.getMaxCustomNum())) {
            sql.append(" AND CUSTOM_NUM<= :maxCustomNum");
        }

        String sceneIds = bean.getSceneId();
        String sqlSceneIds = "";
        if(null != sceneIds && sceneIds.length() >= 1) {
            sceneIds = sceneIds.substring(0, sceneIds.length() - 1);
            String[] templateMenu = sceneIds.split(",");
            String[] orderStr = templateMenu;
            int orderByArr = templateMenu.length;

            for(int orderArr = 0; orderArr < orderByArr; ++orderArr) {
                String sf = orderStr[orderArr];
                sqlSceneIds = sqlSceneIds + " OR SENCET.SCENE_ID=\'" + sf + "\' ";
            }
        }

        if(StringUtil.isNotEmpty(bean.getSceneId())) {
            sql.append(" AND CCGI.CUSTOM_GROUP_ID IN  ( SELECT  SENCET.CUSTOM_GROUP_ID FROM CI_CUSTOM_SCENE_REL SENCET  WHERE SENCET.STATUS=1 ");
            sql.append(" AND ( SENCET.SCENE_ID=\'");
            sql.append(ServiceConstants.ALL_SCENE_ID + "\'");
            sql.append(sqlSceneIds);
            sql.append(" ))");
        }

        if(StringUtil.isNotEmpty(bean.getUpdateCycle())) {
            sql.append(" AND UPDATE_CYCLE = :updateCycle");
        }

        sql.append(" AND (UPDATE_CYCLE != 4 or (IS_LABEL_OFFLINE is null or IS_LABEL_OFFLINE = 0) )");
        String var16 = Configure.getInstance().getProperty("TEMPLATE_MENU");
        String var17;
        if(var16.equals("false")) {
            if(StringUtil.isNotEmpty(bean.getCreateUserId())) {
                sql.append(" AND (IS_PRIVATE = 0  or ( IS_PRIVATE = 1  AND  CREATE_USER_ID = :createUserId ) ) ");
            } else if(StringUtil.isNotEmpty(bean.getCreateCityId())) {
                sql.append(" AND (IS_PRIVATE = 0  or ( IS_PRIVATE = 1  AND  CREATE_CITY_ID = :createCityId ) ) ");
            }
        } else {
            var17 = Configure.getInstance().getProperty("CENTER_CITYID");
            if(StringUtil.isNotEmpty(bean.getCreateUserId())) {
                sql.append(" AND ( CREATE_USER_ID = :createUserId or ( IS_PRIVATE = 0  AND  ( CREATE_CITY_ID = :createCityId or  CREATE_CITY_ID = " + var17 + " ) ) ) ");
            } else if(StringUtil.isNotEmpty(bean.getCreateCityId())) {
                sql.append(" AND ( CREATE_CITY_ID = :createCityId or ( IS_PRIVATE = 0  AND   CREATE_CITY_ID = " + var17 + "  ) ) ");
            }
        }

        if(StringUtil.isNotEmpty(bean.getCityId())) {
            sql.append(" AND CREATE_CITY_ID = :cityId");
        }

        if(StringUtil.isNotEmpty(bean.getIsPrivate())) {
            sql.append(" AND IS_PRIVATE = :isPrivate");
        }

        SimpleDateFormat var18;
        String var22;
        if(StringUtil.isNotEmpty(bean.getStartDate()) || StringUtil.isNotEmpty(bean.getEndDate())) {
            var18 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat var19 = new SimpleDateFormat("yyyy-MM-dd");
            Date var23;
            if(StringUtil.isNotEmpty(bean.getStartDate())) {
                var22 = var19.format(bean.getStartDate());
                var23 = var18.parse(var22 + " 00:00:00");
                bean.setStartDate(var23);
                sql.append(" AND CREATE_TIME >= :startDate");
            }

            if(StringUtil.isNotEmpty(bean.getEndDate())) {
                var22 = var19.format(bean.getEndDate());
                var23 = var18.parse(var22 + " 23:59:59");
                bean.setEndDate(var23);
                sql.append(" AND CREATE_TIME <= :endDate");
            }
        }

        if("1".equals(bean.getDateType())) {
            var17 = DateUtil.getCurrentDay();
            sql.append(" AND CREATE_TIME > to_date(\'").append(var17).append("\',\'yyyy-MM-dd HH24:mi:ss\')");
        }

        if("4".equals(bean.getDateType())) {
            var17 = DateUtil.getFrontDay(6, DateUtil.date2String(new Date(), "yyyyMMdd"));
            sql.append(" AND CREATE_TIME > to_date(\'").append(var17).append("\',\'yyyy-MM-dd HH24:mi:ss\')");
        }

        String var20;
        if("2".equals(bean.getDateType())) {
            var18 = new SimpleDateFormat("yyyyMM");
            var20 = DateUtil.getFrontMonth(0, DateUtil.date2String(new Date(), "yyyyMM"));
            var22 = DateUtil.date2String(DateUtil.firstDayOfMonth(var18.parse(var20)), "yyyy-MM-dd HH:mm:ss");
            sql.append(" AND CREATE_TIME > to_date(\'").append(var22).append("\',\'yyyy-MM-dd HH24:mi:ss\')");
        }

        if("3".equals(bean.getDateType())) {
            var18 = new SimpleDateFormat("yyyyMM");
            var20 = DateUtil.getFrontMonth(2, DateUtil.date2String(new Date(), "yyyyMM"));
            var22 = DateUtil.date2String(DateUtil.firstDayOfMonth(var18.parse(var20)), "yyyy-MM-dd HH:mm:ss");
            sql.append(" AND CREATE_TIME > to_date(\'").append(var22).append("\',\'yyyy-MM-dd HH24:mi:ss\')");
        }

        if(bean.getCreateTypeId() != null && bean.getCreateTypeId().intValue() != 0) {
            sql.append(" AND CREATE_TYPE_ID = :createTypeId");
        }

        if(StringUtil.isNotEmpty(bean.getLabelName())) {
            sql.append(" AND CCGI.CUSTOM_GROUP_ID IN ").append("( ").append(" SELECT R.CUSTOM_ID ").append(" FROM CI_LABEL_INFO L,CI_LABEL_RULE R ").append(" WHERE R.ELEMENT_TYPE = 2 ").append(" AND R.CALCU_ELEMENT = ").append(this.getDataBaseAdapter().getIntToChar("L.LABEL_ID")).append(" AND L.LABEL_NAME LIKE \'%").append(bean.getLabelName()).append("%\')");
        }

        if(StringUtil.isNotEmpty(bean.getTemplateName())) {
            sql.append(" AND TEMPLATE_ID IN ").append("( ").append(" SELECT TEMPLATE_ID").append(" FROM CI_TEMPLATE_INFO").append(" WHERE TEMPLATE_NAME LIKE \'%").append(bean.getTemplateName()).append("%\')");
        }

        if(StringUtils.isNotEmpty(queryType) && "userAttention".equals(queryType)) {
            sql.append(" AND  T4.USER_ID = \'").append(PrivilegeServiceUtil.getUserId()).append("\' ");
        }

        var17 = "";
        if(pager != null && StringUtil.isNotEmpty(pager.getOrderBy())) {
            String[] var21 = pager.getOrderBy().split(",");
            String[] var24 = pager.getOrder().split(",");
            StringBuffer var25 = new StringBuffer("");
            if(var21.length != var24.length) {
                throw new CIServiceException("排序字段和排序方式个数不匹配！");
            }

            for(int i = 0; i < var21.length; ++i) {
                String orderSql;
                if(var21[i].equals("USECOUNT")) {
                    orderSql = "USE_TIMES";
                    var25.append("IS_SYS_RECOM " + var24[i] + ",");
                    var25.append(orderSql + " " + var24[i] + ",");
                } else if(var21[i].equals("USECOUNT_HOT")) {
                    orderSql = "USE_TIMES";
                    var25.append(orderSql + " " + var24[i] + ",");
                } else {
                    var25.append(var21[i] + " " + var24[i] + ",");
                }
            }

            var17 = " ORDER BY " + var25.substring(0, var25.toString().length() - 1);
        } else if(StringUtil.isNotEmpty(bean.getIsMyCustom()) && bean.getIsMyCustom().equals("true")) {
            var17 = " ORDER BY NEW_MODIFY_TIME DESC";
        }

        sql.append(var17);
        String sqlPage;
        if(pager.getPageSize() != 0) {
            sqlPage = this.getDataBaseAdapter().getPagedSql(sql.toString(), pager.getPageNum(), pager.getPageSize());
        } else {
            sqlPage = sql.toString();
        }

        this.log.debug("sql=" + sqlPage);
        return this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(CiCustomGroupInfo.class), new BeanPropertySqlParameterSource(bean));
    }

    public List<CiCustomGroupInfo> getUserUseCustomsersList(Pager pager, CiCustomGroupInfo bean, String queryType) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT CCGI.* ,CASE WHEN T3.SUMCC IS NULL THEN 0 ELSE T3.SUMCC END USE_TIMES");
        if(queryType == null || !"callByBackStage".equals(queryType)) {
            sql.append(", CASE WHEN T4.SUMCC IS NULL THEN \'false\' ELSE \'true\' END ISATTENTION ");
        }

        sql.append(" FROM CI_CUSTOM_GROUP_INFO CCGI LEFT JOIN (SELECT  T1.CUSTOM_ID,COUNT(T1.CUSTOM_ID) SUMCC FROM CI_USER_USE_CUSTOM T1 WHERE ");
        String sceneIds;
        String sqlSceneIds;
        String templateMenu;
        String orderByArr;
        if(StringUtil.isNotEmpty(bean.getDateType())) {
            Date dbType = new Date();
            sceneIds = "";
            sqlSceneIds = "";
            if("4".equals(bean.getDateType())) {
                templateMenu = DateUtil.date2String(dbType, "yyyy-MM-dd");
                orderByArr = DateUtil.getFrontDay(6, templateMenu, "yyyy-MM-dd");
                sceneIds = this.getDataBaseAdapter().getTimeStamp(orderByArr, "00", "00", "00");
                sqlSceneIds = this.getDataBaseAdapter().getTimeStamp(templateMenu, "23", "59", "59");
                sql.append(" T1.USE_TIME >= " + sceneIds + " AND T1.USE_TIME<=" + sqlSceneIds + " AND ");
            }

            if("2".equals(bean.getDateType())) {
                templateMenu = DateUtil.date2String(dbType, "yyyy-MM-dd");
                orderByArr = DateUtil.getFrontDay(30, templateMenu, "yyyy-MM-dd");
                sceneIds = this.getDataBaseAdapter().getTimeStamp(orderByArr, "00", "00", "00");
                sqlSceneIds = this.getDataBaseAdapter().getTimeStamp(templateMenu, "23", "59", "59");
                sql.append(" T1.USE_TIME >= " + sceneIds + " AND T1.USE_TIME<=" + sqlSceneIds + " AND ");
            }

            if("3".equals(bean.getDateType())) {
                templateMenu = DateUtil.date2String(dbType, "yyyy-MM-dd");
                orderByArr = DateUtil.getFrontDay(90, templateMenu, "yyyy-MM-dd");
                sceneIds = this.getDataBaseAdapter().getTimeStamp(orderByArr, "00", "00", "00");
                sqlSceneIds = this.getDataBaseAdapter().getTimeStamp(templateMenu, "23", "59", "59");
                sql.append(" T1.USE_TIME >= " + sceneIds + " AND T1.USE_TIME<=" + sqlSceneIds + " AND ");
            }
        }

        sql.append(" T1.IS_TEMPLATE = 0 GROUP BY T1.CUSTOM_ID) T3 ON CCGI.CUSTOM_GROUP_ID = T3.CUSTOM_ID");
        if(queryType == null || !"callByBackStage".equals(queryType)) {
            sql.append(" LEFT JOIN (SELECT  T11.USER_ID,T11.ATTENTION_TIME,T11.CUSTOM_GROUP_ID,COUNT(T11.CUSTOM_GROUP_ID) SUMCC FROM CI_USER_ATTENTION_CUSTOM T11 WHERE T11.USER_ID = \'" + PrivilegeServiceUtil.getUserId() + "\' GROUP BY T11.CUSTOM_GROUP_ID,T11.USER_ID,T11.ATTENTION_TIME) T4 ON CCGI.CUSTOM_GROUP_ID = T4.CUSTOM_GROUP_ID");
        }

        sql.append(" WHERE 1=1").append(" AND STATUS = ").append(1);
        if(StringUtil.isNotEmpty(bean.getModifyStartDate())) {
            sql.append(" AND NEW_MODIFY_TIME>= :modifyStartDate");
        }

        if(StringUtil.isNotEmpty(bean.getModifyEndDate())) {
            sql.append(" AND NEW_MODIFY_TIME<= :modifyEndDate");
        }

        String var15 = Configure.getInstance().getProperty("CI_DBTYPE");
        if(StringUtil.isNotEmpty(bean.getCustomGroupName())) {
            if("DB2".equalsIgnoreCase(var15)) {
                sql.append(" AND (").append(" (CUSTOM_GROUP_NAME LIKE \'%\'||").append(":customGroupName").append("||\'%\')").append(" OR (CUSTOM_GROUP_DESC LIKE \'%\'||").append(":customGroupName").append("||\'%\')").append(" OR (PROD_OPT_RULE_SHOW LIKE \'%\'||").append(":customGroupName").append("||\'%\')").append(" OR (LABEL_OPT_RULE_SHOW LIKE \'%\'||").append(":customGroupName").append("||\'%\')").append(" OR (CUSTOM_OPT_RULE_SHOW LIKE \'%\'||").append(":customGroupName").append("||\'%\')").append(" OR (KPI_DIFF_RULE LIKE \'%\'||").append(":customGroupName").append("||\'%\')").append(" ) ");
            } else {
                sql.append(" AND (").append(" (LOWER(CUSTOM_GROUP_NAME) LIKE LOWER(\'%\'||").append(":customGroupName").append("||\'%\'))").append(" OR (LOWER(CUSTOM_GROUP_DESC) LIKE LOWER(\'%\'||").append(":customGroupName").append("||\'%\'))").append(" OR (LOWER(PROD_OPT_RULE_SHOW) LIKE LOWER(\'%\'||").append(":customGroupName").append("||\'%\'))").append(" OR (LOWER(LABEL_OPT_RULE_SHOW) LIKE LOWER(\'%\'||").append(":customGroupName").append("||\'%\'))").append(" OR (LOWER(CUSTOM_OPT_RULE_SHOW) LIKE LOWER(\'%\'||").append(":customGroupName").append("||\'%\'))").append(" OR (LOWER(KPI_DIFF_RULE) LIKE LOWER(\'%\'||").append(":customGroupName").append("||\'%\'))").append(" ) ");
            }
        }

        if(StringUtil.isNotEmpty(bean.getCreateTime())) {
            sql.append(" AND CREATE_TIME = :createTime");
        }

        if(bean.getDataStatus() != null) {
            sql.append(" AND DATA_STATUS = :dataStatus");
        }

        if(StringUtil.isNotEmpty(bean.getMinCustomNum())) {
            sql.append(" AND CUSTOM_NUM>= :minCustomNum");
        }

        if(StringUtil.isNotEmpty(bean.getMaxCustomNum())) {
            sql.append(" AND CUSTOM_NUM<= :maxCustomNum");
        }

        sceneIds = bean.getSceneId();
        sqlSceneIds = "";
        String[] var17;
        if(null != sceneIds && sceneIds.length() >= 1) {
            sceneIds = sceneIds.substring(0, sceneIds.length() - 1);
            String[] var16 = sceneIds.split(",");
            var17 = var16;
            int orderArr = var16.length;

            for(int sf = 0; sf < orderArr; ++sf) {
                String i = var17[sf];
                sqlSceneIds = sqlSceneIds + " or senceT.scene_id=\'" + i + "\' ";
            }
        }

        if(StringUtil.isNotEmpty(bean.getSceneId())) {
            sql.append(" AND CCGI.CUSTOM_GROUP_ID IN  ( select  senceT.custom_group_id from ci_custom_scene_rel senceT  where senceT.status=1 ");
            sql.append(" and ( senceT.scene_id=\'");
            sql.append(ServiceConstants.ALL_SCENE_ID + "\'");
            sql.append(sqlSceneIds);
            sql.append(" ))");
        }

        if(StringUtil.isNotEmpty(bean.getUpdateCycle())) {
            sql.append(" AND update_cycle = :updateCycle");
        }

        if(StringUtil.isEmpty(bean.getIsMyCustom()) || !bean.getIsMyCustom().equals("true")) {
            sql.append(" AND (UPDATE_CYCLE != 4 or (IS_LABEL_OFFLINE is null or IS_LABEL_OFFLINE = 0) )");
        }

        templateMenu = Configure.getInstance().getProperty("TEMPLATE_MENU");
        if(templateMenu.equals("false")) {
            if(StringUtil.isNotEmpty(bean.getCreateUserId())) {
                sql.append(" AND (IS_PRIVATE = 0  or ( IS_PRIVATE = 1  AND  CREATE_USER_ID = :createUserId ) ) ");
            } else if(StringUtil.isNotEmpty(bean.getCreateCityId())) {
                sql.append(" AND (IS_PRIVATE = 0  or ( IS_PRIVATE = 1  AND  CREATE_CITY_ID = :createCityId ) ) ");
            }
        } else {
            orderByArr = Configure.getInstance().getProperty("CENTER_CITYID");
            if(StringUtil.isNotEmpty(bean.getCreateUserId())) {
                sql.append(" AND ( CREATE_USER_ID = :createUserId or ( IS_PRIVATE = 0  AND  ( CREATE_CITY_ID = :createCityId or  CREATE_CITY_ID = " + orderByArr + " ) ) ) ");
            } else if(StringUtil.isNotEmpty(bean.getCreateCityId())) {
                sql.append(" AND ( CREATE_CITY_ID = :createCityId or ( IS_PRIVATE = 0  AND   CREATE_CITY_ID = " + orderByArr + "  ) ) ");
            }
        }

        if(StringUtil.isNotEmpty(bean.getIsPrivate())) {
            sql.append(" AND IS_PRIVATE = :isPrivate");
        }

        if(StringUtil.isNotEmpty(bean.getStartDate()) || StringUtil.isNotEmpty(bean.getEndDate())) {
            SimpleDateFormat var19 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat var18 = new SimpleDateFormat("yyyy-MM-dd");
            String var21;
            Date var22;
            if(StringUtil.isNotEmpty(bean.getStartDate())) {
                var21 = var18.format(bean.getStartDate());
                var22 = var19.parse(var21 + " 00:00:00");
                bean.setStartDate(var22);
                sql.append(" AND CREATE_TIME >= :startDate");
            }

            if(StringUtil.isNotEmpty(bean.getEndDate())) {
                var21 = var18.format(bean.getEndDate());
                var22 = var19.parse(var21 + " 23:59:59");
                bean.setEndDate(var22);
                sql.append(" AND CREATE_TIME <= :endDate");
            }
        }

        if(bean.getCreateTypeId() != null && bean.getCreateTypeId().intValue() != 0) {
            sql.append(" AND CREATE_TYPE_ID = :createTypeId");
        }

        if(StringUtil.isNotEmpty(bean.getLabelName())) {
            sql.append(" AND CCGI.CUSTOM_GROUP_ID IN ").append("( ").append(" SELECT R.CUSTOM_ID ").append(" FROM CI_LABEL_INFO L,CI_LABEL_RULE R ").append(" WHERE R.ELEMENT_TYPE = 2 ").append(" AND R.CALCU_ELEMENT = ").append(this.getDataBaseAdapter().getIntToChar("L.LABEL_ID")).append(" AND L.LABEL_NAME LIKE \'%").append(bean.getLabelName()).append("%\')");
        }

        if(StringUtil.isNotEmpty(bean.getTemplateName())) {
            sql.append(" AND TEMPLATE_ID IN ").append("( ").append(" SELECT TEMPLATE_ID").append(" FROM CI_TEMPLATE_INFO").append(" WHERE TEMPLATE_NAME LIKE \'%").append(bean.getTemplateName()).append("%\')");
        }

        if(StringUtils.isNotEmpty(queryType) && "userAttention".equals(queryType)) {
            sql.append(" AND  T4.USER_ID = \'").append(PrivilegeServiceUtil.getUserId()).append("\' ");
        }

        if(StringUtil.isEmpty(pager.getOrderBy())) {
            sql.append(" ORDER BY NEW_MODIFY_TIME DESC");
        } else {
            var17 = pager.getOrderBy().split(",");
            String[] var20 = pager.getOrder().split(",");
            StringBuffer var23 = new StringBuffer("");
            if(var17.length != var20.length) {
                throw new CIServiceException("排序字段和排序方式个数不匹配！");
            }

            for(int var24 = 0; var24 < var17.length; ++var24) {
                String orderSql;
                if(var24 == var17.length - 1) {
                    if(var17[var24].equals("USECOUNT")) {
                        orderSql = "USE_TIMES";
                        var23.append("IS_SYS_RECOM " + var20[var24]);
                        var23.append("," + orderSql + " " + var20[var24]);
                    } else if(var17[var24].equals("USECOUNT_HOT")) {
                        orderSql = "USE_TIMES";
                        var23.append(orderSql + " " + var20[var24]);
                    } else {
                        var23.append(var17[var24] + " " + var20[var24]);
                    }
                } else {
                    if(var17[var24].equals("USECOUNT")) {
                        orderSql = "USE_TIMES";
                        var23.append("IS_SYS_RECOM " + var20[var24] + ",");
                        var23.append(orderSql + " " + var20[var24] + ",");
                    }

                    if(var17[var24].equals("USECOUNT_HOT")) {
                        orderSql = "USE_TIMES";
                        var23.append(orderSql + " " + var20[var24] + ",");
                    } else {
                        var23.append(var17[var24] + " " + var20[var24] + ",");
                    }
                }
            }

            sql.append(" ORDER BY " + var23.toString());
        }

        String sqlPage;
        if(pager.getPageSize() != 0) {
            sqlPage = this.getDataBaseAdapter().getPagedSql(sql.toString(), pager.getPageNum(), pager.getPageSize());
        } else {
            sqlPage = sql.toString();
        }

        this.log.debug("sql=" + sqlPage);
        return this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(CiCustomGroupInfo.class), new BeanPropertySqlParameterSource(bean));
    }

    public List<CiCustomGroupInfo> getCustomsersList(Pager pager, CiCustomGroupInfo bean) throws Exception {
        return this.getCustomsersList(pager, bean, (String)null);
    }

    public Long getUserAttentionCustomserCount(CiCustomGroupInfo bean) throws Exception {
        StringBuffer sqlSB = (new StringBuffer("select count(1) from  CI_CUSTOM_GROUP_INFO CCGI,CI_USER_ATTENTION_CUSTOM CUAC ")).append("where CUAC.USER_ID=\'" + PrivilegeServiceUtil.getUserId() + "\'").append(" AND CCGI.STATUS =").append(1).append(" AND CCGI.CUSTOM_GROUP_ID=CUAC.CUSTOM_GROUP_ID").append(" AND (CCGI.UPDATE_CYCLE != 4 or CCGI.IS_LABEL_OFFLINE != 1) ");
        this.log.debug("获得用户收藏客户群数量的sql=" + sqlSB.toString());
        return Long.valueOf(this.getSimpleJdbcTemplate().queryForLong(sqlSB.toString(), new BeanPropertySqlParameterSource(bean)));
    }

    public String getLableNameById(String customersId) throws Exception {
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("SELECT LABEL_NAME").append(" FROM CI_LABEL_INFO").append(" WHERE LABEL_ID IN ").append("(").append(" SELECT ").append(this.getDataBaseAdapter().getCharToInt("CALCU_ELEMENT")).append(" FROM CI_LABEL_RULE").append(" WHERE CUSTOM_ID = \'" + customersId + "\'").append(" AND ELEMENT_TYPE = ").append(2).append(" )").append(" ORDER BY LABEL_ID");
        List list = this.getSimpleJdbcTemplate().queryForList(sqlBuf.toString(), new Object[0]);
        StringBuffer namesBuff = new StringBuffer();
        String labelNames = null;
        if(list != null && list.size() != 0) {
            Iterator i$ = list.iterator();

            while(i$.hasNext()) {
                Map map = (Map)i$.next();
                namesBuff.append(map.get("LABEL_NAME")).append(",");
            }

            labelNames = namesBuff.substring(0, namesBuff.length() - 1);
        }

        return labelNames;
    }

    public List<CiCustomGroupInfo> indexQueryCustomersName(CiCustomGroupInfo ciCustomGroupInfo, Integer createTypeId) throws Exception {
        String userId = ciCustomGroupInfo.getCreateUserId();
        ArrayList customersNameList = new ArrayList();
        CiCustomGroupInfo tempCustomGroupInfo = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT CCGI.CUSTOM_GROUP_ID, CCGI.CUSTOM_GROUP_NAME,").append("CASE WHEN T3.SUMCC IS NULL THEN 0 ELSE T3.SUMCC END USE_TIMES FROM CI_CUSTOM_GROUP_INFO CCGI ").append("LEFT JOIN (SELECT T1.CUSTOM_ID, COUNT(T1.CUSTOM_ID) SUMCC ").append("FROM CI_USER_USE_CUSTOM T1 WHERE T1.IS_TEMPLATE = 0 ").append("GROUP BY T1.CUSTOM_ID) T3 ON  CCGI.CUSTOM_GROUP_ID = T3.CUSTOM_ID ").append("WHERE LOWER(CCGI.CUSTOM_GROUP_NAME) LIKE ").append("LOWER(\'%").append(ciCustomGroupInfo.getCustomGroupName()).append("%\') ");
        if(createTypeId != null) {
            sql.append(" AND CCGI.CREATE_TYPE_ID = ").append(createTypeId);
        }

        String templateMenu = Configure.getInstance().getProperty("TEMPLATE_MENU");
        String sqlPage;
        if(templateMenu.equals("false")) {
            if(StringUtil.isNotEmpty(userId)) {
                sql.append(" AND (CCGI.IS_PRIVATE = 0  or ( CCGI.IS_PRIVATE = 1  AND  CCGI.CREATE_USER_ID =\'").append(userId).append("\' ) ) ");
            } else if(StringUtil.isNotEmpty(ciCustomGroupInfo.getCreateCityId())) {
                sql.append(" AND (CCGI.IS_PRIVATE = 0  or ( CCGI.IS_PRIVATE = 1  AND  CCGI.CREATE_CITY_ID =\'").append(userId).append("\' ) ) ");
            }
        } else {
            sqlPage = Configure.getInstance().getProperty("CENTER_CITYID");
            if(StringUtil.isNotEmpty(userId)) {
                sql.append(" AND ( CCGI.CREATE_USER_ID =\'").append(userId).append("\' or ( CCGI.IS_PRIVATE = 0  AND  ( CCGI.CREATE_CITY_ID =\'").append(userId).append("\' or  CCGI.CREATE_CITY_ID = " + sqlPage + " ) ) ) ");
            } else if(StringUtil.isNotEmpty(ciCustomGroupInfo.getCreateCityId())) {
                sql.append(" AND ( CCGI.CREATE_CITY_ID =\'").append(userId).append("\' or ( CCGI.IS_PRIVATE = 0  AND   CCGI.CREATE_CITY_ID = " + sqlPage + "  ) ) ");
            }
        }

        sql.append(" AND CCGI.STATUS = ").append(1);
        this.log.debug("indexQueryCustomersName:" + sql);
        sqlPage = this.getDataBaseAdapter().getPagedSql(sql.toString(), 1, 10);
        List mapList = this.getSimpleJdbcTemplate().queryForList(sqlPage, new Object[0]);
        Iterator i$ = mapList.iterator();

        while(i$.hasNext()) {
            Map map = (Map)i$.next();
            tempCustomGroupInfo = new CiCustomGroupInfo();
            tempCustomGroupInfo.setCustomGroupId((String)map.get("CUSTOM_GROUP_ID"));
            tempCustomGroupInfo.setCustomGroupName((String)map.get("CUSTOM_GROUP_NAME"));
            customersNameList.add(tempCustomGroupInfo);
        }

        return customersNameList;
    }

    public int selectCustomerGroupIsPush(String customerGroupId) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(custom_group_id) AS num ");
        sql.append("FROM ci_custom_group_push_cycle ");
        sql.append("WHERE status=1 and custom_group_id=\'").append(customerGroupId).append("\'");
        String countSql = sql.toString();
        this.log.debug("count sql is: " + countSql);
        return this.getSimpleJdbcTemplate().queryForInt(countSql, new Object[0]);
    }

    public int selectCount(String countSql) throws Exception {
        int count = this.getBackSimpleJdbcTemplate().queryForInt(countSql, new Object[0]);
        return count;
    }

    public void selectValidate(String validateSql) throws Exception {
        this.getBackSimpleJdbcTemplate().queryForList(validateSql, new Object[0]);
    }

    public void batchUpdateSql(List<String> updateSqls) {
        Iterator i$ = updateSqls.iterator();

        while(i$.hasNext()) {
            String updateSql = (String)i$.next();
            this.getBackSimpleJdbcTemplate().update(updateSql, new Object[0]);
            this.log.debug("update sql is: " + updateSql);
        }

        this.log.debug("batchUpdateSql over");
    }

    public CiCustomGroupInfo queryCiCustomGroupInfoByListInfoId(String listInfoId) throws Exception {
        String sql = "select g.* from CI_CUSTOM_GROUP_INFO g, CI_CUSTOM_LIST_INFO l where g.CUSTOM_GROUP_ID = l.CUSTOM_GROUP_ID and l.LIST_TABLE_NAME = ?";
        CiCustomGroupInfo result = (CiCustomGroupInfo)this.getSimpleJdbcTemplate().queryForObject(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiCustomGroupInfo.class), new Object[]{listInfoId});
        return result;
    }

    public List<CiCustomGroupInfo> getSysRecommendCustomInfoList(CiCustomGroupInfo searchBeanCustom) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT CCGI.* ,CASE WHEN T3.SUMCC IS NULL THEN 0 ELSE T3.SUMCC END USE_TIMES");
        sql.append(", CASE WHEN T4.SUMCC IS NULL THEN \'false\' ELSE \'true\' END ISATTENTION ");
        sql.append(" FROM CI_CUSTOM_GROUP_INFO CCGI LEFT JOIN (SELECT  T1.CUSTOM_ID,COUNT(T1.CUSTOM_ID) SUMCC FROM CI_USER_USE_CUSTOM T1 WHERE T1.IS_TEMPLATE = 0 GROUP BY T1.CUSTOM_ID) T3 ON CCGI.CUSTOM_GROUP_ID = T3.CUSTOM_ID");
        sql.append(" LEFT JOIN (SELECT  T11.USER_ID,T11.CUSTOM_GROUP_ID,COUNT(T11.CUSTOM_GROUP_ID) SUMCC FROM CI_USER_ATTENTION_CUSTOM T11 WHERE T11.USER_ID = \'" + PrivilegeServiceUtil.getUserId() + "\' GROUP BY T11.CUSTOM_GROUP_ID,T11.USER_ID) T4 ON CCGI.CUSTOM_GROUP_ID = T4.CUSTOM_GROUP_ID");
        sql.append(" WHERE 1=1").append(" AND STATUS = ").append(1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String startMonth = DateUtil.getFrontMonth(2, DateUtil.date2String(new Date(), "yyyyMM"));
        String startDay = DateUtil.date2String(DateUtil.firstDayOfMonth(sdf.parse(startMonth)), "yyyy-MM-dd HH:mm:ss");
        sql.append(" AND CREATE_TIME > to_date(\'").append(startDay).append("\',\'yyyy-MM-dd HH24:mi:ss\')");
        if(StringUtil.isEmpty(searchBeanCustom.getIsMyCustom()) || !searchBeanCustom.getIsMyCustom().equals("true")) {
            sql.append(" AND (UPDATE_CYCLE != 4 or (IS_LABEL_OFFLINE is null or IS_LABEL_OFFLINE = 0) )");
        }

        String templateMenu = Configure.getInstance().getProperty("TEMPLATE_MENU");
        if(templateMenu.equals("false")) {
            if(StringUtil.isNotEmpty(searchBeanCustom.getCreateUserId())) {
                sql.append(" AND (IS_PRIVATE = 0  or ( IS_PRIVATE = 1  AND  CREATE_USER_ID = \'" + searchBeanCustom.getCreateUserId() + "\' ) ) ");
            } else if(StringUtil.isNotEmpty(searchBeanCustom.getCreateCityId())) {
                sql.append(" AND (IS_PRIVATE = 0  or ( IS_PRIVATE = 1  AND  CREATE_CITY_ID = \'" + searchBeanCustom.getCreateCityId() + "\' ) ) ");
            }
        } else {
            String root = Configure.getInstance().getProperty("CENTER_CITYID");
            if(StringUtil.isNotEmpty(searchBeanCustom.getCreateUserId())) {
                sql.append(" AND ( CREATE_USER_ID = \'" + searchBeanCustom.getCreateUserId() + "\' or ( IS_PRIVATE = " + 0 + "  AND  ( CREATE_CITY_ID = \'" + searchBeanCustom.getCreateCityId() + "\' or  CREATE_CITY_ID = " + root + " ) ) ) ");
            } else if(StringUtil.isNotEmpty(searchBeanCustom.getCreateCityId())) {
                sql.append(" AND ( CREATE_CITY_ID = \'" + searchBeanCustom.getCreateCityId() + "\' or ( IS_PRIVATE = " + 0 + "  AND   CREATE_CITY_ID = " + root + "  ) ) ");
            }
        }

        if(StringUtil.isNotEmpty(searchBeanCustom.getSceneId())) {
            sql.append(" AND CCGI.CUSTOM_GROUP_ID IN  ( select  senceT.custom_group_id from ci_custom_scene_rel senceT  where senceT.status=1 and  senceT.user_id = CCGI.CREATE_USER_ID");
            sql.append(" and (senceT.scene_id=\'");
            sql.append(searchBeanCustom.getSceneId());
            sql.append("\' or senceT.scene_id=\'");
            sql.append(ServiceConstants.ALL_SCENE_ID);
            sql.append("\'))");
        }

        if(StringUtil.isNotEmpty(searchBeanCustom.getCustomGroupId())) {
            sql.append(" AND CCGI.CUSTOM_GROUP_ID=");
            sql.append("\'").append(searchBeanCustom.getCustomGroupId()).append("\'");
        }

        if(searchBeanCustom.getIsAttention().equals("true")) {
            sql.append(" AND  T4.USER_ID = \'").append(PrivilegeServiceUtil.getUserId()).append("\' ");
        }

        sql.append(" AND CCGI.IS_PRIVATE = 0");
        sql.append(" AND CCGI.DATA_STATUS = 3");
        sql.append(" ORDER BY IS_SYS_RECOM DESC,USE_TIMES DESC ");
        String sqlPage = this.getDataBaseAdapter().getPagedSql(sql.toString(), 1, ServiceConstants.SHOW_SYS_RECOMMEND_CUSTOM);
        this.log.debug("sql=" + sqlPage);
        return this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(CiCustomGroupInfo.class), new BeanPropertySqlParameterSource(searchBeanCustom));
    }

    public CiCustomGroupInfo selectCustomGroupAndAttention(CiCustomGroupInfo info) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT CCGI.* , CASE WHEN T4.SUMCC IS NULL THEN \'false\' ELSE \'true\' END ISATTENTION ");
        sql.append(" FROM CI_CUSTOM_GROUP_INFO CCGI LEFT JOIN (SELECT  T11.USER_ID,T11.CUSTOM_GROUP_ID,COUNT(T11.CUSTOM_GROUP_ID) SUMCC ");
        sql.append("FROM CI_USER_ATTENTION_CUSTOM T11 WHERE T11.USER_ID = \'");
        sql.append(PrivilegeServiceUtil.getUserId());
        sql.append("\' GROUP BY T11.CUSTOM_GROUP_ID,T11.USER_ID) T4 ON CCGI.CUSTOM_GROUP_ID = T4.CUSTOM_GROUP_ID");
        sql.append(" WHERE 1=1").append(" AND STATUS = ").append(1);
        sql.append(" AND CCGI.CUSTOM_GROUP_ID=");
        sql.append("\'").append(info.getCustomGroupId()).append("\'");
        return (CiCustomGroupInfo)this.getSimpleJdbcTemplate().queryForObject(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiCustomGroupInfo.class), new Object[0]);
    }

    public int getDailyCustomersTotolCount(CiCustomGroupInfo bean) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(CUSTOM_GROUP_ID)").append(" FROM CI_CUSTOM_GROUP_INFO").append(" WHERE 1=1 ").append(" AND STATUS =").append(1).append(" AND CREATE_TYPE_ID = ").append(1);
        sql.append(" AND  CREATE_CITY_ID = ?");
        sql.append(" AND update_cycle = ?");
        sql.append(" and END_DATE >= ? and START_DATE < ?");
        Date nowDate = new Date();
        this.log.debug("sql=" + sql.toString());
        return this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new Object[]{bean.getCreateCityId(), bean.getUpdateCycle(), nowDate, nowDate});
    }

    public String getCiCustomExeTime() throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT EXE_TIME from CI_CUSTOM_EXE_TIME");
        this.log.debug("sql=" + sql.toString());
        return (String)this.getSimpleJdbcTemplate().queryForObject(sql.toString(), String.class, new Object[0]);
    }

    public void updateCiCustomExeTime(String ciCustomExeTime) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE CI_CUSTOM_EXE_TIME SET EXE_TIME = ?");
        this.log.debug("sql=" + sql.toString() + "参数：ciCustomExeTime：      " + ciCustomExeTime);
        this.getSimpleJdbcTemplate().update(sql.toString(), new Object[]{ciCustomExeTime});
    }

    public List<CiCustomGroupInfo> findCiCustomGroupInfo4TimingCreate(String ciCustomExeTime, Date nowDate) throws Exception {
        String dayCycleNowStr = DateUtil.date2String(nowDate, "HH:mm:ss");
        String monthCycleNowStr = DateUtil.date2String(nowDate, "dd HH:mm:ss");
        String dayCycleExeTime = ciCustomExeTime.substring(ciCustomExeTime.length() - 8);
        String monthCycleExeTime = ciCustomExeTime.substring(ciCustomExeTime.length() - 11);
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * from CI_CUSTOM_GROUP_INFO where STATUS = ").append(1).append(" and LIST_CREATE_TIME is not null and (CUSTOM_LIST_CREATE_FAILED = 0 or CUSTOM_LIST_CREATE_FAILED is null) ").append(" and ((UPDATE_CYCLE = 3 and LIST_CREATE_TIME > ? and LIST_CREATE_TIME<=?) or ").append(" (UPDATE_CYCLE = 2 and LIST_CREATE_TIME > ? and LIST_CREATE_TIME<=?))").append(" and (IS_LABEL_OFFLINE is null or IS_LABEL_OFFLINE = 0) and CREATE_TYPE_ID = 1");
        this.log.debug("findCiCustomGroupInfo4TimingCreate:" + sql + " 参数：dayCycleNowStr：      " + dayCycleNowStr + "  monthCycleNowStr：      " + monthCycleNowStr + "  dayCycleExeTime:    " + dayCycleExeTime + "  monthCycleExeTime:    " + monthCycleExeTime);
        List customersList = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiCustomGroupInfo.class), new Object[]{dayCycleExeTime, dayCycleNowStr, monthCycleExeTime, monthCycleNowStr});
        return customersList;
    }

    public int selectCountByJdbc(String countSql, String databaseUrl, String username, String password) throws Exception {
        int count = 0;
        Connection con = null;
        Statement stmt = null;

        try {
            con = JDBCUtil.getConnection(databaseUrl, username, password);
            stmt = con.createStatement();
            ResultSet e = stmt.executeQuery(countSql);
            if(e.next()) {
                count = e.getInt(1);
            }
        } catch (SQLException var48) {
            this.log.debug("JDBC查询SQl语句报错", var48);
            throw var48;
        } finally {
            if(stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException var46) {
                    this.log.debug("关闭Statement报错", var46);
                } finally {
                    if(con != null) {
                        try {
                            JDBCUtil.closeConnection();
                        } catch (Exception var45) {
                            this.log.debug("关闭Connection报错", var45);
                        }
                    }

                }
            }

        }

        return count;
    }

    public List<CiCustomGroupInfo> findRecommendCustomInfoList() {
        StringBuffer sqlB = new StringBuffer("select T1.*,case when T2.useTimes is null then 0 else   T2.useTimes end useTimes from ");
        sqlB.append("( select T.* from  CI_CUSTOM_GROUP_INFO T,ci_custom_label_scene_rel TT  where T.CUSTOM_GROUP_ID = tt.CUSTOM_GROUP_ID ) T1 ");
        sqlB.append("  left join  ");
        sqlB.append("(select count(1) useTimes,CUSTOM_ID from CI_USER_USE_CUSTOM group by CUSTOM_ID) T2 ");
        sqlB.append("on T1.CUSTOM_GROUP_ID = T2.CUSTOM_ID ");
        List list = this.getSimpleJdbcTemplate().query(sqlB.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiCustomGroupInfo.class), new Object[0]);
        return list;
    }

    public String selectOtherSysCustomGroupId(String customGroupId, String sysId) {
        StringBuffer sqlB = new StringBuffer("select out_group_id from ZJCOC_CUST_PUSH_RELA where custom_group_id=? and sys_id=? ");
        String result = (String)this.getSimpleJdbcTemplate().queryForObject(sqlB.toString(), String.class, new Object[]{customGroupId, sysId});
        return result;
    }

    public int queryForExist(String labelIds){
        StringBuffer labelId = new StringBuffer(" select count(1) from sccoc.tag_power_label where label_id in ( ");
        labelId.append(labelIds);
        labelId.append(" )");
        this.log.info("labelId:" + labelId);
        int Exist = this.getSimpleJdbcTemplate().queryForInt(labelId.toString(), new Object[0]);
        this.log.info("Exist:" + Exist);
        return Exist;
    }

    public int queryForPowerMatch(String userId, String labelIds){
        StringBuffer PwMatchSql = new StringBuffer(" select count(1) from sccoc.tag_power_label a, sccoc.tag_power_account b ");
        PwMatchSql.append(" where 1=1 and a.power = b.power ");
        PwMatchSql.append(" and a.label_id in ( ");
        PwMatchSql.append(labelIds);
        PwMatchSql.append(" ) and b.account = ? ");
        this.log.info("PwMatchSql:" + PwMatchSql);
        int Match = this.getSimpleJdbcTemplate().queryForInt(PwMatchSql.toString(), new Object[]{userId});
        this.log.info("Match:" + Match);
        return Match;
    }
}
