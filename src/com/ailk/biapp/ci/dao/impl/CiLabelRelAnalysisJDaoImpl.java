package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.ICiLabelRelAnalysisJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.model.CiLabelRelModel;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.util.DataBaseAdapter;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiLabelRelAnalysisJDaoImpl extends JdbcBaseDao implements ICiLabelRelAnalysisJDao {
    private Logger log = Logger.getLogger(this.getClass());
    private static final String LABEL_EXT_INFO_TALBE = "CI_LABEL_EXT_INFO";
    private static final String MDA_COLUMN = "CI_MDA_SYS_TABLE_COLUMN";
    private static final String MDA_TABLE = "CI_MDA_SYS_TABLE";
    private static final String USER_LABEL_REL_TABLE = "CI_USER_LABEL_REL";
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;

    public CiLabelRelAnalysisJDaoImpl() {
    }

    public void selectMainLabelUserNum(CiLabelRelModel ciLabelRelModel) throws Exception {
        ciLabelRelModel.setMainLabelUserNum(this.getCustomNumByLabelId(ciLabelRelModel.getMainLabelId(), ciLabelRelModel.getDataDate()));
    }

    /** @deprecated */
    @Deprecated
    public Long getOverlapUserNumByMainAndRelLabel(CiLabelRelModel ciLabelRelModel, String[] mainLabelCAndT, String[] relLabelCAndT) {
        Long overLapUserNum = Long.valueOf(0L);
        StringBuffer sql = new StringBuffer();
        String relTabelName = "";
        CiLabelInfo mainLabel = CacheBase.getInstance().getEffectiveLabel(String.valueOf(ciLabelRelModel.getRelLabelId()));
        CiLabelInfo relLabel = CacheBase.getInstance().getEffectiveLabel(String.valueOf(ciLabelRelModel.getRelLabelId()));
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(String.valueOf(ciLabelRelModel.getRelLabelId())).getUpdateCycle().intValue();
        if(1 == updateCycle) {
            relTabelName = relLabelCAndT[1] + "_" + CacheBase.getInstance().getNewLabelDay();
        } else if(2 == updateCycle) {
            relTabelName = relLabelCAndT[1] + "_" + ciLabelRelModel.getDataDate();
        }

        String phoneNoColumn = Configure.getInstance().getProperty("RELATED_COLUMN");
        if(!mainLabelCAndT[1].equals(relLabelCAndT[1])) {
            sql.append("SELECT COUNT(T1.").append(phoneNoColumn).append(") FROM ").append(mainLabelCAndT[1]).append("_").append(ciLabelRelModel.getDataDate()).append(" T1 ").append(" LEFT JOIN ").append(relTabelName).append(" T2 ").append(" ON (T1.").append(phoneNoColumn).append(" = T2.").append(phoneNoColumn).append(") ").append(" WHERE 1=1 ").append(" AND T1.").append(mainLabelCAndT[0]);
            if(mainLabel.getLabelTypeId().equals(Integer.valueOf(3))) {
                if(1 == mainLabel.getCiLabelExtInfo().getCiMdaSysTableColumn().getColumnDataTypeId().intValue()) {
                    sql.append(" = " + mainLabel.getCiLabelExtInfo().getAttrVal());
                } else if(2 == mainLabel.getCiLabelExtInfo().getCiMdaSysTableColumn().getColumnDataTypeId().intValue()) {
                    sql.append(" = \'" + mainLabel.getCiLabelExtInfo().getAttrVal() + "\'");
                }
            } else if(mainLabel.getLabelTypeId().equals(Integer.valueOf(1))) {
                sql.append(" = 1");
            }

            sql.append(" AND T2.").append(relLabelCAndT[0]);
            if(relLabel.getLabelTypeId().equals(Integer.valueOf(3))) {
                if(1 == relLabel.getCiLabelExtInfo().getCiMdaSysTableColumn().getColumnDataTypeId().intValue()) {
                    sql.append(" = " + relLabel.getCiLabelExtInfo().getAttrVal());
                } else if(2 == relLabel.getCiLabelExtInfo().getCiMdaSysTableColumn().getColumnDataTypeId().intValue()) {
                    sql.append(" = \'" + relLabel.getCiLabelExtInfo().getAttrVal() + "\'");
                }
            } else if(relLabel.getLabelTypeId().equals(Integer.valueOf(1))) {
                sql.append(" = 1");
            }
        } else {
            sql.append("SELECT COUNT(T1.").append(phoneNoColumn).append(") FROM ").append(mainLabelCAndT[1]).append("_").append(ciLabelRelModel.getDataDate()).append(" T1 ").append(" WHERE 1=1 ").append(" AND T1.").append(mainLabelCAndT[0]);
            if(mainLabel.getLabelTypeId().equals(Integer.valueOf(3))) {
                if(1 == mainLabel.getCiLabelExtInfo().getCiMdaSysTableColumn().getColumnDataTypeId().intValue()) {
                    sql.append(" = " + mainLabel.getCiLabelExtInfo().getAttrVal());
                } else if(2 == mainLabel.getCiLabelExtInfo().getCiMdaSysTableColumn().getColumnDataTypeId().intValue()) {
                    sql.append(" = \'" + mainLabel.getCiLabelExtInfo().getAttrVal() + "\'");
                }
            } else if(mainLabel.getLabelTypeId().equals(Integer.valueOf(1))) {
                sql.append(" = 1");
            }

            sql.append(" AND T1.").append(relLabelCAndT[0]);
            if(relLabel.getLabelTypeId().equals(Integer.valueOf(3))) {
                if(1 == relLabel.getCiLabelExtInfo().getCiMdaSysTableColumn().getColumnDataTypeId().intValue()) {
                    sql.append(" = " + relLabel.getCiLabelExtInfo().getAttrVal());
                } else if(2 == relLabel.getCiLabelExtInfo().getCiMdaSysTableColumn().getColumnDataTypeId().intValue()) {
                    sql.append(" = \'" + relLabel.getCiLabelExtInfo().getAttrVal() + "\'");
                }
            } else if(relLabel.getLabelTypeId().equals(Integer.valueOf(1))) {
                sql.append(" = 1");
            }
        }

        this.log.debug("getOverlapUserNumByMainAndRelLabel sql: " + sql);
        overLapUserNum = Long.valueOf(this.getBackSimpleJdbcTemplate().queryForLong(sql.toString(), new Object[0]));
        return overLapUserNum;
    }

    /** @deprecated */
    @Deprecated
    public String[] getColumnAndTableNameByLabelId(Integer labelId) {
        StringBuffer sql = new StringBuffer();
        String[] cAndTNames = new String[3];
        List list = null;
        sql.append("SELECT B.COLUMN_NAME,T.TABLE_NAME FROM ").append("CI_LABEL_EXT_INFO").append(" A LEFT JOIN ").append("CI_MDA_SYS_TABLE_COLUMN").append(" B ON (A.COLUMN_ID = B.COLUMN_ID) LEFT JOIN ").append("CI_MDA_SYS_TABLE").append(" T ON (B.TABLE_ID = T.TABLE_ID) WHERE A.LABEL_ID = ").append(labelId);
        list = this.getSimpleJdbcTemplate().queryForList(sql.toString(), new Object[0]);
        Map map;
        if(null != list && list.size() > 0) {
            for(Iterator iterator = list.iterator(); iterator.hasNext(); cAndTNames[1] = (String)map.get("TABLE_NAME")) {
                map = (Map)iterator.next();
                cAndTNames[0] = (String)map.get("COLUMN_NAME");
            }
        }

        return cAndTNames;
    }

    public Long getCustomNumByLabelId(Integer labelId, String dataDate) {
        Long userNum = this.ciLabelInfoService.getCustomNum(dataDate, Integer.valueOf(labelId.intValue()), Integer.valueOf(-1), Integer.valueOf(-1), (Long)null, (String)null);
        return userNum;
    }

    protected String getSqlCondition(Integer lableId, String dataDate) {
        StringBuffer condition = new StringBuffer();
        if(!StringUtil.isEmpty(dataDate)) {
            condition.append(" AND C.DATA_DATE = \'").append(dataDate).append("\'");
        }

        if(!StringUtil.isEmpty(lableId)) {
            condition.append(" AND C.LABEL_ID = ").append(lableId);
        }

        condition.append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CITY_ID = ").append(-1).append(" AND C.VIP_LEVEL_ID = ").append(-1);
        return condition.toString();
    }

    public List<CiLabelRelModel> selectRelLabelByMainLabel(String userId, CiLabelRelModel ciLabelRelModel) throws Exception {
        List list = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT C.ASSOCI_LABEL_ID AS REL_LABEL_ID FROM ").append("CI_USER_LABEL_REL").append(" C ").append(" WHERE C.STATUS = 1 AND C.MAIN_LABEL_ID = ").append(ciLabelRelModel.getMainLabelId()).append(" AND C.USER_ID = \'").append(userId).append("\'");
        this.log.debug("selectRelLabelByMainLabel sql : " + sql);
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLabelRelModel.class), new Object[0]);
        return list;
    }

    public void deleteUserLabelRel(String userId, Integer mainLabelId, String relLabelIds) {
        if(!"".equals(relLabelIds) && relLabelIds.endsWith(",")) {
            relLabelIds = relLabelIds.substring(0, relLabelIds.length() - 1);
        }

        String dbType = Configure.getInstance().getProperty("CI_DBTYPE");
        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(dbType);
        StringBuffer sql = new StringBuffer();
        sql.append(" UPDATE ").append("CI_USER_LABEL_REL").append(" SET STATUS = ").append(ServiceConstants.PUBLIC_STATUS_DEL).append(", DEL_TIME = ").append(dataBaseAdapter.getTimeStamp(DateUtil.getCurrentDay().substring(0, 10))).append(" WHERE USER_ID = \'").append(userId).append("\' ").append(" AND MAIN_LABEL_ID = ").append(mainLabelId).append(" AND ASSOCI_LABEL_ID NOT IN (").append(relLabelIds).append(") ");
        this.log.debug("deleteUserLabelRel sql : " + sql);
        this.getSimpleJdbcTemplate().update(sql.toString(), new Object[0]);
    }
}
