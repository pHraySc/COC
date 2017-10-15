package com.ailk.biapp.ci.market.dao.impl;

import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.market.dao.ICiCustomLabelSceneRelJDao;
import com.ailk.biapp.ci.market.entity.CiCustomLabelSceneRel;
import com.ailk.biapp.ci.market.entity.CiMarketTask;
import com.ailk.biapp.ci.util.DateUtil;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiCustomLabelSceneRelJDaoImpl extends JdbcBaseDao implements ICiCustomLabelSceneRelJDao {
    protected Logger log = Logger.getLogger(CiCustomLabelSceneRelJDaoImpl.class);

    public CiCustomLabelSceneRelJDaoImpl() {
    }

    public void modifyLabelSceneRel(final List<CiLabelInfo> labelInfoList) throws Exception {
        long start = System.currentTimeMillis();
        this.getSimpleJdbcTemplate().getJdbcOperations().batchUpdate(" UPDATE CI_CUSTOM_LABEL_SCENE_REL SET AVG_SCORE=?, USE_TIMES=?, NEWEST_TIME=?,LABEL_CUSTOM_NAME=? WHERE LABEL_ID =? ", new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                CiLabelInfo labelInfo = (CiLabelInfo)labelInfoList.get(i);
                if(null == labelInfo.getAvgScore()) {
                    ps.setDouble(1, 0.0D);
                } else {
                    ps.setDouble(1, labelInfo.getAvgScore().doubleValue());
                }

                ps.setInt(2, labelInfo.getUseTimes().intValue());
                String dataDate = "";
                if(1 == labelInfo.getUpdateCycle().intValue()) {
                    dataDate = DateUtil.string2StringFormat(labelInfo.getDataDate(), "yyyyMMdd", "yyyy-MM-dd");
                } else {
                    dataDate = DateUtil.string2StringFormat(labelInfo.getDataDate(), "yyyyMM", "yyyy-MM");
                }

                ps.setString(3, dataDate);
                ps.setString(4, labelInfo.getLabelName());
                ps.setInt(5, labelInfo.getLabelId().intValue());
            }

            public int getBatchSize() {
                return labelInfoList.size();
            }
        });
        this.getSimpleJdbcTemplate().getJdbcOperations().execute("COMMIT");
        this.log.debug("batchUpdateValueList cost:" + (System.currentTimeMillis() - start) + "ms");
    }

    public void modifyCustomSceneRel(final List<CiCustomGroupInfo> groupInfoList) throws Exception {
        long start = System.currentTimeMillis();
        this.getSimpleJdbcTemplate().getJdbcOperations().batchUpdate(" UPDATE CI_CUSTOM_LABEL_SCENE_REL SET AVG_SCORE=?, USE_TIMES=?, NEWEST_TIME=?,LABEL_CUSTOM_NAME=? WHERE CUSTOM_GROUP_ID =? ", new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                CiCustomGroupInfo info = (CiCustomGroupInfo)groupInfoList.get(i);
                if(null == info.getAvgScore()) {
                    ps.setDouble(1, 0.0D);
                } else {
                    ps.setDouble(1, info.getAvgScore().doubleValue());
                }

                ps.setInt(2, info.getUseTimes());
                ps.setString(3, DateUtil.date2String(info.getNewModifyTime(), "yyyy-MM-dd HH:mm:ss"));
                ps.setString(4, info.getCustomGroupName());
                ps.setString(5, info.getCustomGroupId());
            }

            public int getBatchSize() {
                return groupInfoList.size();
            }
        });
        this.getSimpleJdbcTemplate().getJdbcOperations().execute("COMMIT");
        this.log.debug("batchUpdateValueList cost:" + (System.currentTimeMillis() - start) + "ms");
    }

    public List<CiCustomLabelSceneRel> selectCiCustomLabelSceneRel(int currentPage, int pageSize, CiCustomLabelSceneRel ciCustomLabelSceneRel) throws Exception {
        CiCustomLabelSceneRel param = new CiCustomLabelSceneRel();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM(SELECT abc.*,row_number() ").append("over(partition BY label_custom_name ").append(this.getDataBaseAdapter().getOverFunPostfix(" label_custom_name desc ")).append(") rank FROM( SELECT a.primary_key_id,a.scene_id,c.scene_name, ").append("a.market_task_id,b.market_task_name,a.label_id, ").append("a.custom_group_id,a.label_custom_name,a.avg_score, ").append("a.newest_time,a.use_times,a.sort_num,a.show_type  ").append("FROM ci_custom_label_scene_rel a LEFT JOIN ").append("ci_market_task b ON a.market_task_id = b.market_task_id ").append("LEFT JOIN ci_market_scene c ON ").append("a.scene_id = c.scene_id WHERE a.status = 1 ");
        if(StringUtils.isNotEmpty(ciCustomLabelSceneRel.getLabelCustomName())) {
            param.setLabelCustomName("%" + this.replaceSpecialCharacter(ciCustomLabelSceneRel.getLabelCustomName().toLowerCase()) + "%");
            sql.append(" and LOWER(a.label_custom_name) like :labelCustomName escape \'|\' ");
        }

        if(StringUtils.isNotEmpty(ciCustomLabelSceneRel.getSceneId())) {
            param.setSceneId(ciCustomLabelSceneRel.getSceneId());
            sql.append(" and a.scene_id=:sceneId ");
        }

        if(StringUtils.isNotEmpty(ciCustomLabelSceneRel.getMarketTaskId())) {
            param.setMarketTaskId(ciCustomLabelSceneRel.getMarketTaskId());
            sql.append(" and a.market_task_id in(").append(param.getMarketTaskId()).append(")");
        }

        sql.append(") abc )  WHERE rank =1 order by sort_num, avg_score desc, newest_time desc ");
        String sqlStr = this.getDataBaseAdapter().getPagedSql(sql.toString(), currentPage, pageSize);
        this.log.info("selectCiCustomLabelSceneRel sql:" + sqlStr);
        return this.getSimpleJdbcTemplate().query(sqlStr, ParameterizedBeanPropertyRowMapper.newInstance(CiCustomLabelSceneRel.class), new BeanPropertySqlParameterSource(param));
    }

    public int selectCiCustomLabelSceneRelCount(CiCustomLabelSceneRel ciCustomLabelSceneRel) {
        CiCustomLabelSceneRel param = new CiCustomLabelSceneRel();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT count(1) FROM(SELECT abc.*,row_number() ").append("over(partition BY label_custom_name ").append(this.getDataBaseAdapter().getOverFunPostfix(" label_custom_name desc ")).append(") rank FROM( SELECT a.primary_key_id,a.scene_id,c.scene_name, ").append("a.market_task_id,b.market_task_name,a.label_id, ").append("a.custom_group_id,a.label_custom_name,a.avg_score, ").append("a.newest_time,a.use_times,a.sort_num,a.show_type  ").append("FROM ci_custom_label_scene_rel a LEFT JOIN ").append("ci_market_task b ON a.market_task_id = b.market_task_id ").append("LEFT JOIN ci_market_scene c ON ").append("a.scene_id = c.scene_id WHERE a.status = 1 ");
        if(StringUtils.isNotEmpty(ciCustomLabelSceneRel.getLabelCustomName())) {
            param.setLabelCustomName("%" + this.replaceSpecialCharacter(ciCustomLabelSceneRel.getLabelCustomName().toLowerCase()) + "%");
            sql.append(" and LOWER(a.label_custom_name) like :labelCustomName escape \'|\' ");
        }

        if(StringUtils.isNotEmpty(ciCustomLabelSceneRel.getSceneId())) {
            param.setSceneId(ciCustomLabelSceneRel.getSceneId());
            sql.append(" and a.scene_id=:sceneId ");
        }

        if(StringUtils.isNotEmpty(ciCustomLabelSceneRel.getMarketTaskId())) {
            param.setMarketTaskId(ciCustomLabelSceneRel.getMarketTaskId());
            sql.append(" and a.market_task_id in(").append(param.getMarketTaskId()).append(")");
        }

        sql.append(") abc )  WHERE rank =1 ");
        this.log.info("selectCiCustomLabelSceneRelCount sql:" + sql.toString());
        return this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new BeanPropertySqlParameterSource(param));
    }

    public List<CiMarketTask> selectFistLevelMarketTask() {
        StringBuffer sql = new StringBuffer();
        sql.append("select market_task_id,market_task_name,market_task_desc,parent_id,sort_num ").append("from ci_market_task where parent_id is null order by sort_num");
        this.log.info("sql:" + sql.toString());
        return this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiMarketTask.class), new Object[0]);
    }

    public List<CiMarketTask> selectMarketTaskByParentId(String parentId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select market_task_id,market_task_name,market_task_desc,parent_id,sort_num ").append("from ci_market_task where parent_id=? order by sort_num");
        return this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiMarketTask.class), new Object[]{parentId});
    }

    private String replaceSpecialCharacter(String labelName) {
        if(StringUtil.isNotEmpty(labelName)) {
            labelName = labelName.trim();
            return labelName.replace("|", "||").replace("%", "|%").replace("_", "|_");
        } else {
            return "";
        }
    }
}
