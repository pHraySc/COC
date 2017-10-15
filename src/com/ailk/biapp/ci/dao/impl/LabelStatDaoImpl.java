package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiLabelRelAnalysisJDao;
import com.ailk.biapp.ci.dao.ILabelStatDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.model.CiLabelFormModel;
import com.ailk.biapp.ci.util.DateUtil;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LabelStatDaoImpl extends JdbcBaseDao implements ILabelStatDao {
    private Logger logger = Logger.getLogger(LabelStatDaoImpl.class);
    private static final String TABLE_CI_LABEL_INFO = "CI_LABEL_INFO";
    private static final String TABLE_CI_LABEL_EXT_INFO = "CI_LABEL_EXT_INFO";
    private static final String TABLE_LABEL_STAT_INFO = "CI_LABEL_BRAND_USER_NUM";
    private static final String TABLE_DW_USER_COUNT = "DW_COC_LABEL_USER_00";
    private static final String TABLE_NEWEST_LABEL_DATE = "CI_NEWEST_LABEL_DATE";
    @Autowired
    private ICiLabelRelAnalysisJDao ciLabelRelAnalysisJDao;

    public LabelStatDaoImpl() {
    }

    /** @deprecated */
    @Deprecated
    public void statLabelCustomNum(String dataDate, int updateCycle) throws Exception {
        StringBuffer allLabelSql = new StringBuffer();
        allLabelSql.append(" SELECT T.LABEL_ID FROM ").append("CI_LABEL_INFO").append(" T ").append(" LEFT JOIN ").append("CI_LABEL_EXT_INFO").append(" T1 ON (T.LABEL_ID = T1.LABEL_ID) ").append(" WHERE T.DATA_STATUS_ID = ").append(2).append(" AND T.UPDATE_CYCLE = ").append(updateCycle).append(" AND T1.IS_STAT_USER_NUM = ").append(1);
        this.logger.debug("allLabelSql : " + allLabelSql);
        List allLabelList = this.getSimpleJdbcTemplate().queryForList(allLabelSql.toString(), new Object[0]);
        StringBuffer insertCustomNumSql = new StringBuffer();
        insertCustomNumSql.append("INSERT INTO ").append("CI_LABEL_BRAND_USER_NUM").append(" (DATA_DATE,LABEL_ID,CITY_ID,VIP_LEVEL_ID,BRAND_ID,CUSTOM_NUM,RING_NUM,PROPORTION) ");
        String[] labelCAndT = new String[0];
        HashMap param = new HashMap();
        Iterator iterator = allLabelList.iterator();

        while(iterator.hasNext()) {
            try {
                Map e = (Map)iterator.next();
                Integer labelId = (Integer)e.get("LABEL_ID");
                labelCAndT = this.ciLabelRelAnalysisJDao.getColumnAndTableNameByLabelId(labelId);
                if(null != labelCAndT && labelCAndT.length > 0) {
                    String tableName = labelCAndT[1] + "_COUNT" + "_" + dataDate;
                    StringBuffer brandSql = new StringBuffer();
                    brandSql.append(insertCustomNumSql).append(" SELECT \'").append(dataDate).append("\' AS DATA_DATE,").append(labelId).append(" AS LABEL_ID,").append(-1).append(" AS CITY_ID,").append(-1).append(" AS VIP_LEVEL_ID,BRAND_ID,SUM(").append(labelCAndT[0]).append(") AS CUSTOM_NUM,0 AS RING_NUM,0.0 AS PROPORTION FROM ").append(tableName).append(" GROUP BY BRAND_ID ");
                    StringBuffer subBrandSql = new StringBuffer();
                    subBrandSql.append(insertCustomNumSql).append(" SELECT \'").append(dataDate).append("\' AS DATA_DATE,").append(labelId).append(" AS LABEL_ID,").append(-1).append(" AS CITY_ID,").append(-1).append(" AS VIP_LEVEL_ID,SUB_BRAND_ID AS BRAND_ID,SUM(").append(labelCAndT[0]).append(") AS CUSTOM_NUM,0 AS RING_NUM,0.0 AS PROPORTION FROM ").append(tableName).append(" GROUP BY SUB_BRAND_ID HAVING SUB_BRAND_ID > 0  ");
                    StringBuffer citySql = new StringBuffer();
                    citySql.append(insertCustomNumSql).append(" SELECT \'").append(dataDate).append("\' AS DATA_DATE,").append(labelId).append(" AS LABEL_ID,COUNTY_ID,").append(-1).append(" AS VIP_LEVEL_ID,").append(-1).append(" AS BRAND_ID,SUM(").append(labelCAndT[0]).append(") AS CUSTOM_NUM,0 AS RING_NUM,0.0 AS PROPORTION FROM ").append(tableName).append(" GROUP BY COUNTY_ID ");
                    StringBuffer vipSql = new StringBuffer();
                    vipSql.append(insertCustomNumSql).append(" SELECT \'").append(dataDate).append("\' AS DATA_DATE,").append(labelId).append(" AS LABEL_ID,").append(-1).append(" AS CITY_ID,VIP_SCALE_ID,").append(-1).append(" AS BRAND_ID,SUM(").append(labelCAndT[0]).append(") AS CUSTOM_NUM,0 AS RING_NUM,0.0 AS PROPORTION FROM ").append(tableName).append(" GROUP BY VIP_SCALE_ID ");
                    StringBuffer labelAllSql = new StringBuffer();
                    labelAllSql.append(insertCustomNumSql).append(" SELECT \'").append(dataDate).append("\' AS DATA_DATE,").append(labelId).append(" AS LABEL_ID,").append(-1).append(" AS CITY_ID,").append(-1).append(" AS VIP_LEVEL_ID, ").append(-1).append(" AS BRAND_ID,SUM(").append(labelCAndT[0]).append(") AS CUSTOM_NUM,0 AS RING_NUM,0.0 AS PROPORTION FROM ").append(tableName);
                    this.getSimpleJdbcTemplate().update(brandSql.toString(), param);
                    this.getSimpleJdbcTemplate().update(subBrandSql.toString(), param);
                    this.getSimpleJdbcTemplate().update(citySql.toString(), param);
                    this.getSimpleJdbcTemplate().update(vipSql.toString(), param);
                    this.getSimpleJdbcTemplate().update(labelAllSql.toString(), param);
                }
            } catch (Exception var17) {
                this.logger.error("统计标签的客户数失败 " + var17.getMessage());
            }
        }

    }

    /** @deprecated */
    @Deprecated
    public void statLabelProportion(String dataDate, int updateCycle) throws Exception {
        StringBuffer allLabelSql = new StringBuffer();
        allLabelSql.append(" SELECT T.LABEL_ID,T.PARENT_ID FROM ").append("CI_LABEL_INFO").append(" T ").append(" LEFT JOIN ").append("CI_LABEL_EXT_INFO").append(" T1 ON (T.LABEL_ID = T1.LABEL_ID) ").append(" WHERE T.DATA_STATUS_ID = ").append(2).append(" AND T.UPDATE_CYCLE = ").append(updateCycle).append(" AND T1.IS_STAT_USER_NUM = ").append(1).append(" AND T1.LABEL_LEVEL > 3");
        this.logger.debug("allLabelSql : " + allLabelSql);
        List allLabelList = this.getSimpleJdbcTemplate().queryForList(allLabelSql.toString(), new Object[0]);
        StringBuffer updateSql = new StringBuffer();
        updateSql.append("UPDATE ").append("CI_LABEL_BRAND_USER_NUM").append(" SET PROPORTION = :proportion WHERE LABEL_ID =:labelId ").append(" AND DATA_DATE = \'").append(dataDate).append("\' ").append(" AND CITY_ID = ").append(-1).append(" AND BRAND_ID = ").append(-1).append(" AND VIP_LEVEL_ID = ").append(-1);
        this.logger.debug("updateSql : " + updateSql);
        StringBuffer labelExistSql = new StringBuffer();
        labelExistSql.append(" SELECT LABEL_ID FROM ").append("CI_LABEL_BRAND_USER_NUM").append(" WHERE LABEL_ID =:tempLabelId ").append(" AND DATA_DATE = \'").append(dataDate).append("\' ").append(" AND CITY_ID = ").append(-1).append(" AND BRAND_ID = ").append(-1).append(" AND VIP_LEVEL_ID = ").append(-1);
        this.logger.debug("labelExistSql : " + labelExistSql);
        StringBuffer parentCustomNumSql = new StringBuffer();
        parentCustomNumSql.append(" SELECT SUM(T2.CUSTOM_NUM) AS CUSTOM_NUM FROM ").append("CI_LABEL_INFO").append(" T1 LEFT JOIN ").append("CI_LABEL_BRAND_USER_NUM").append(" T2 ON (T1.LABEL_ID = T2.LABEL_ID) ").append(" WHERE T1.PARENT_ID =:parentId AND T2.DATA_DATE = \'").append(dataDate).append("\' ").append(" AND T2.CITY_ID = ").append(-1).append(" AND T2.BRAND_ID = ").append(-1).append(" AND T2.VIP_LEVEL_ID = ").append(-1);
        this.logger.debug("parentCustomNumSql : " + parentCustomNumSql);
        StringBuffer insertParentSql = new StringBuffer();
        insertParentSql.append(" INSERT INTO ").append("CI_LABEL_BRAND_USER_NUM").append("(DATA_DATE,LABEL_ID,CITY_ID,VIP_LEVEL_ID,BRAND_ID,CUSTOM_NUM,RING_NUM,PROPORTION) ").append(" VALUES(\'").append(dataDate).append("\',:tempLabelId,").append(-1).append(",").append(-1).append(",").append(-1).append(",").append(":customNum,").append("0,0.0 )");
        this.logger.debug("insertParentSql : " + insertParentSql);
        HashMap param = new HashMap();
        List labelExistList = null;
        List parentLabelExistList = null;
        if(null != allLabelList && allLabelList.size() > 0) {
            Iterator iterator = allLabelList.iterator();

            while(iterator.hasNext()) {
                try {
                    Map e = (Map)iterator.next();
                    Integer labelId = (Integer)e.get("LABEL_ID");
                    Integer parentId = (Integer)e.get("PARENT_ID");
                    param.put("labelId", labelId);
                    param.put("parentId", parentId);
                    Long labelCustomNum = Long.valueOf(0L);
                    Long parentCustomNum = Long.valueOf(0L);
                    param.put("tempLabelId", parentId);
                    parentLabelExistList = this.getSimpleJdbcTemplate().queryForList(labelExistSql.toString(), param);
                    param.put("tempLabelId", labelId);
                    labelExistList = this.getSimpleJdbcTemplate().queryForList(labelExistSql.toString(), param);
                    if(null != parentLabelExistList && parentLabelExistList.size() > 0) {
                        parentCustomNum = this.ciLabelRelAnalysisJDao.getCustomNumByLabelId(parentId, dataDate);
                    } else {
                        param.put("tempLabelId", parentId);
                        parentCustomNum = Long.valueOf(this.getSimpleJdbcTemplate().queryForLong(parentCustomNumSql.toString(), param));
                        param.put("customNum", parentCustomNum);
                        this.getSimpleJdbcTemplate().update(insertParentSql.toString(), param);
                    }

                    if(null != labelExistList && labelExistList.size() > 0) {
                        labelCustomNum = this.ciLabelRelAnalysisJDao.getCustomNumByLabelId(labelId, dataDate);
                    } else {
                        param.put("tempLabelId", labelId);
                        param.put("customNum", labelCustomNum);
                        this.getSimpleJdbcTemplate().update(insertParentSql.toString(), param);
                    }

                    String proportion = "";
                    this.logger.info("标签客户数 : " + labelCustomNum);
                    this.logger.info("父标签客户数 : " + parentCustomNum);
                    double temp = 0.0D;
                    if(parentCustomNum.longValue() > 0L) {
                        temp = Double.valueOf((double)labelCustomNum.longValue()).doubleValue() / Double.valueOf((double)parentCustomNum.longValue()).doubleValue();
                        DecimalFormat df = new DecimalFormat("0.0000");
                        proportion = df.format(temp);
                        param.put("proportion", proportion);
                        this.getSimpleJdbcTemplate().update(updateSql.toString(), param);
                    }
                } catch (Exception var22) {
                    this.logger.error("统计标签客户数占比失败 " + var22.getMessage());
                }
            }
        } else {
            this.logger.debug("allLabelList is null");
        }

    }

    /** @deprecated */
    @Deprecated
    public void statLabelRingNum(String dataDate, int updateCycle) throws Exception {
        StringBuffer allLabelSql = new StringBuffer();
        allLabelSql.append(" SELECT LABEL_ID, CUSTOM_NUM FROM ").append("CI_LABEL_BRAND_USER_NUM").append(" T ").append(" WHERE T.DATA_DATE = \'").append(dataDate).append("\' ").append(" AND T.CITY_ID = ").append(-1).append(" AND T.BRAND_ID = ").append(-1).append(" AND VIP_LEVEL_ID = ").append(-1);
        this.logger.debug(" allLabelSql : " + allLabelSql);
        List allLabelList = this.getSimpleJdbcTemplate().queryForList(allLabelSql.toString(), new Object[0]);
        StringBuffer updateRingSql = new StringBuffer();
        updateRingSql.append(" UPDATE ").append("CI_LABEL_BRAND_USER_NUM").append(" SET RING_NUM = :ringNum ").append(" WHERE LABEL_ID = :labelId ").append(" AND DATA_DATE = \'").append(dataDate).append("\' ").append(" AND CITY_ID = ").append(-1).append(" AND BRAND_ID = ").append(-1).append(" AND VIP_LEVEL_ID = ").append(-1);
        Integer labelId = null;
        Integer customNum = Integer.valueOf(0);
        Long lastCustomNum = Long.valueOf(0L);
        Integer ringNum = Integer.valueOf(0);
        String frontDate = "";
        if(updateCycle == 1) {
            frontDate = DateUtil.getFrontDay(1, dataDate);
        } else if(updateCycle == 2) {
            frontDate = DateUtil.getFrontMonth(1, dataDate);
        }

        HashMap param = new HashMap();
        Iterator iterator = allLabelList.iterator();

        while(iterator.hasNext()) {
            try {
                Map e = (Map)iterator.next();
                labelId = (Integer)e.get("LABEL_ID");
                customNum = (Integer)e.get("CUSTOM_NUM");
                param.put("labelId", labelId);
                lastCustomNum = this.ciLabelRelAnalysisJDao.getCustomNumByLabelId(labelId, frontDate);
                if(null == lastCustomNum) {
                    ringNum = null;
                } else {
                    ringNum = Integer.valueOf(customNum.intValue() - lastCustomNum.intValue());
                }

                param.put("ringNum", ringNum);
                this.getSimpleJdbcTemplate().update(updateRingSql.toString(), param);
            } catch (Exception var14) {
                this.logger.error("统计标签客户数环比增长量失败 " + var14.getMessage());
            }
        }

    }

    public void updateLabelTotalCustomNum(String dataDate, int updateCycle) throws Exception {
        String labelStatTableName = DateUtil.getLabelStatTableName(dataDate);
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT LABEL_ID, CUSTOM_NUM FROM ").append(labelStatTableName).append(" WHERE BRAND_ID = ").append(-1).append(" AND VIP_LEVEL_ID = ").append(-1).append(" AND CITY_ID = ? ").append(" AND DATA_DATE = \'").append(dataDate).append("\' ");
        this.logger.info(sql.toString());
        List list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLabelFormModel.class), new Object[]{String.valueOf(-1)});
        StringBuffer updateSql = new StringBuffer();
        updateSql.append(" UPDATE ").append("CI_LABEL_EXT_INFO").append(" SET CUSTOM_NUM = :customNum WHERE LABEL_ID = :labelId");
        HashMap param = new HashMap();
        if(null != list && list.size() > 0) {
            Iterator i$ = list.iterator();

            while(i$.hasNext()) {
                CiLabelFormModel model = (CiLabelFormModel)i$.next();
                param.put("labelId", model.getLabelId());
                param.put("customNum", Integer.valueOf(model.getCustomNum() == null?0:model.getCustomNum().intValue()));

                try {
                    this.getSimpleJdbcTemplate().update(updateSql.toString(), param);
                } catch (Exception var11) {
                    this.logger.error("更新标签的最新总客户数失败 " + var11.getMessage());
                }
            }
        }

    }

    /** @deprecated */
    @Deprecated
    public int dataDateExist(String dataDate) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT COUNT(1) FROM ").append("CI_LABEL_BRAND_USER_NUM").append(" WHERE DATA_DATE = \'").append(dataDate).append("\' ");
        this.logger.debug(" label dataDateExist sql : " + sql);
        int dataDateNum = this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new Object[0]);
        return dataDateNum;
    }

    /** @deprecated */
    @Deprecated
    public void updateDwNullDataTo0ForMonth(String month) throws Exception {
        for(int i = 1; i < 6; ++i) {
            StringBuffer brandSql = new StringBuffer();
            brandSql.append(" UPDATE ").append("DW_COC_LABEL_USER_00").append(i).append("_COUNT_").append(month).append(" SET BRAND_ID = 0 WHERE BRAND_ID IS NULL");
            StringBuffer subBrandSql = new StringBuffer();
            subBrandSql.append(" UPDATE ").append("DW_COC_LABEL_USER_00").append(i).append("_COUNT_").append(month).append(" SET SUB_BRAND_ID = 0 WHERE SUB_BRAND_ID IS NULL");
            StringBuffer countySql = new StringBuffer();
            countySql.append(" UPDATE ").append("DW_COC_LABEL_USER_00").append(i).append("_COUNT_").append(month).append(" SET COUNTY_ID = 0 WHERE COUNTY_ID IS NULL");
            StringBuffer vipSql = new StringBuffer();
            vipSql.append(" UPDATE ").append("DW_COC_LABEL_USER_00").append(i).append("_COUNT_").append(month).append(" SET VIP_SCALE_ID = 0 WHERE VIP_SCALE_ID IS NULL");
            this.getSimpleJdbcTemplate().update(brandSql.toString(), new Object[0]);
            this.getSimpleJdbcTemplate().update(subBrandSql.toString(), new Object[0]);
            this.getSimpleJdbcTemplate().update(countySql.toString(), new Object[0]);
            this.getSimpleJdbcTemplate().update(vipSql.toString(), new Object[0]);
        }

    }

    /** @deprecated */
    @Deprecated
    public void updateDwNullDataTo0ForDay(String day) throws Exception {
        for(int i = 1; i < 6; ++i) {
            StringBuffer brandSql = new StringBuffer();
            brandSql.append(" UPDATE ").append("DW_COC_LABEL_USER_00").append(i).append("_COUNT_").append(day).append(" SET BRAND_ID = 0 WHERE BRAND_ID IS NULL");
            StringBuffer subBrandSql = new StringBuffer();
            subBrandSql.append(" UPDATE ").append("DW_COC_LABEL_USER_00").append(i).append("_COUNT_").append(day).append(" SET SUB_BRAND_ID = 0 WHERE SUB_BRAND_ID IS NULL");
            StringBuffer countySql = new StringBuffer();
            countySql.append(" UPDATE ").append("DW_COC_LABEL_USER_00").append(i).append("_COUNT_").append(day).append(" SET COUNTY_ID = 0 WHERE COUNTY_ID IS NULL");
            StringBuffer vipSql = new StringBuffer();
            vipSql.append(" UPDATE ").append("DW_COC_LABEL_USER_00").append(i).append("_COUNT_").append(day).append(" SET VIP_SCALE_ID = 0 WHERE VIP_SCALE_ID IS NULL");
            this.getSimpleJdbcTemplate().update(brandSql.toString(), new Object[0]);
            this.getSimpleJdbcTemplate().update(subBrandSql.toString(), new Object[0]);
            this.getSimpleJdbcTemplate().update(countySql.toString(), new Object[0]);
            this.getSimpleJdbcTemplate().update(vipSql.toString(), new Object[0]);
        }

    }

    public void updateLabelStatDateStatus(int updateCycle) throws Exception {
        String column = "";
        if(updateCycle == 2) {
            column = "MONTH_NEWEST_STATUS";
        } else if(updateCycle == 1) {
            column = "DAY_NEWEST_STATUS";
        }

        StringBuffer sql = new StringBuffer();
        sql.append(" UPDATE ").append("CI_NEWEST_LABEL_DATE").append(" set ").append(column).append(" = 1");
        this.getSimpleJdbcTemplate().update(sql.toString(), new Object[0]);
    }
}
