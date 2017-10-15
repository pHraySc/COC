package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiLabelCountDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiDataSource;
import com.ailk.biapp.ci.entity.CilabelCount;
import com.ailk.biapp.ci.model.Pager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by admin on 2017/4/24.
 */
@Repository("ciLabelCountDao")
@Transactional
public class CiLabelCountDaoImpl extends JdbcBaseDao implements ICiLabelCountDao {

    private Logger log = Logger.getLogger(CiDataSourceDaoImpl.class);

    @Override
    public List<CilabelCount> getLabelCount(Pager pager) {
        String sql = "SELECT\n" +
                "C1.LABEL_ID as f_level_sort_id,C1.LABEL_NAME as f_level_sort_name,C1.DATA_STATUS_NAME as f_level_sort_status,\n" +
                "\n" +
                "C2.LABEL_ID as s_level_sort_id,C2.LABEL_NAME as s_level_sort_name,C2.DATA_STATUS_NAME as s_level_sort_status,\n" +
                "\n" +
                "C3.LABEL_ID as dad_label_id,C3.LABEL_NAME as dad_label_name,C3.BUSI_CALIBER as dad_label_busi_caliber,C3.CREATE_TIME as dad_label_create_time,\n" +
                "CASE C3.UPDATE_CYCLE WHEN 1 THEN '日周期' WHEN 2 THEN '月周期' ELSE '-' END as dad_label_update_cycle,\n" +
                "C3.DATA_DATE as dad_label_data_date,C3.CUSTOM_NUM as dad_label_custom_num,\n" +
                "\n" +
                "C4.LABEL_ID as son_label_id,C4.LABEL_NAME as son_label_name,C4.BUSI_CALIBER as son_label_busi_caliber,C4.CREATE_TIME as son_label_create_time,\n" +
                "CASE C4.UPDATE_CYCLE WHEN 1 THEN '日周期' WHEN 2 THEN '月周期' ELSE '-' END as son_label_update_cycle,\n" +
                "C4.DATA_DATE as son_label_data_date,C4.CUSTOM_NUM as son_label_custom_num\n" +
                "\n" +
                "FROM (SELECT CL1.*,CLE1.CUSTOM_NUM,DD.DATA_STATUS_NAME FROM SCCOC.CI_LABEL_INFO CL1 LEFT JOIN SCCOC.CI_LABEL_EXT_INFO CLE1 ON CL1.LABEL_ID=CLE1.LABEL_ID LEFT\n" +
                " JOIN SCCOC.DIM_LABEL_DATA_STATUS DD ON DD.DATA_STATUS_ID=CL1.DATA_STATUS_ID ) C1\n" +
                "LEFT JOIN (SELECT CL2.*,CLE2.CUSTOM_NUM,DD.DATA_STATUS_NAME FROM SCCOC.CI_LABEL_INFO CL2 LEFT JOIN SCCOC.CI_LABEL_EXT_INFO CLE2 ON CL2.LABEL_ID=CLE2.LABEL_ID\n" +
                " LEFT JOIN SCCOC.DIM_LABEL_DATA_STATUS DD ON DD.DATA_STATUS_ID=CL2.DATA_STATUS_ID ) C2 ON C2.PARENT_ID=C1.LABEL_ID\n" +
                "LEFT JOIN (SELECT CL3.*,CLE3.CUSTOM_NUM,DD.DATA_STATUS_NAME FROM SCCOC.CI_LABEL_INFO CL3 LEFT JOIN SCCOC.CI_LABEL_EXT_INFO CLE3 ON CL3.LABEL_ID=CLE3.LABEL_ID\n" +
                " LEFT JOIN SCCOC.DIM_LABEL_DATA_STATUS DD ON DD.DATA_STATUS_ID=CL3.DATA_STATUS_ID ) C3 ON C3.PARENT_ID=C2.LABEL_ID\n" +
                "LEFT JOIN (SELECT CL4.*,CLE4.CUSTOM_NUM,DD.DATA_STATUS_NAME FROM SCCOC.CI_LABEL_INFO CL4 LEFT JOIN SCCOC.CI_LABEL_EXT_INFO CLE4 ON CL4.LABEL_ID=CLE4.LABEL_ID\n" +
                " LEFT JOIN SCCOC.DIM_LABEL_DATA_STATUS DD ON DD.DATA_STATUS_ID=CL4.DATA_STATUS_ID ) C4 ON C4.PARENT_ID=C3.LABEL_ID\n" +
                "LEFT JOIN SCCOC.CI_LABEL_EXT_INFO C ON C.LABEL_ID=C1.LABEL_ID\n" +
                "WHERE C.LABEL_LEVEL=1 order by dad_label_data_date desc";
        String sqlPage;
        if(pager.getPageSize() != 0) {
            sqlPage = this.getDataBaseAdapter().getPagedSql(sql.toString(), pager.getPageNum(), pager.getPageSize());
        } else {
            sqlPage = sql.toString();
        }
        this.log.debug("sql=" + sqlPage);
        List CilabelCountList = this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(CilabelCount.class), new Object[0]);
        return CilabelCountList;
    }

    @Override
    public int getLabelCountNum() {
        String sql = "SELECT count(1)" +
                "FROM (SELECT CL1.*,CLE1.CUSTOM_NUM,DD.DATA_STATUS_NAME FROM SCCOC.CI_LABEL_INFO CL1 LEFT JOIN SCCOC.CI_LABEL_EXT_INFO CLE1 ON CL1.LABEL_ID=CLE1.LABEL_ID LEFT\n" +
                " JOIN SCCOC.DIM_LABEL_DATA_STATUS DD ON DD.DATA_STATUS_ID=CL1.DATA_STATUS_ID ) C1\n" +
                "LEFT JOIN (SELECT CL2.*,CLE2.CUSTOM_NUM,DD.DATA_STATUS_NAME FROM SCCOC.CI_LABEL_INFO CL2 LEFT JOIN SCCOC.CI_LABEL_EXT_INFO CLE2 ON CL2.LABEL_ID=CLE2.LABEL_ID\n" +
                " LEFT JOIN SCCOC.DIM_LABEL_DATA_STATUS DD ON DD.DATA_STATUS_ID=CL2.DATA_STATUS_ID ) C2 ON C2.PARENT_ID=C1.LABEL_ID\n" +
                "LEFT JOIN (SELECT CL3.*,CLE3.CUSTOM_NUM,DD.DATA_STATUS_NAME FROM SCCOC.CI_LABEL_INFO CL3 LEFT JOIN SCCOC.CI_LABEL_EXT_INFO CLE3 ON CL3.LABEL_ID=CLE3.LABEL_ID\n" +
                " LEFT JOIN SCCOC.DIM_LABEL_DATA_STATUS DD ON DD.DATA_STATUS_ID=CL3.DATA_STATUS_ID ) C3 ON C3.PARENT_ID=C2.LABEL_ID\n" +
                "LEFT JOIN (SELECT CL4.*,CLE4.CUSTOM_NUM,DD.DATA_STATUS_NAME FROM SCCOC.CI_LABEL_INFO CL4 LEFT JOIN SCCOC.CI_LABEL_EXT_INFO CLE4 ON CL4.LABEL_ID=CLE4.LABEL_ID\n" +
                " LEFT JOIN SCCOC.DIM_LABEL_DATA_STATUS DD ON DD.DATA_STATUS_ID=CL4.DATA_STATUS_ID ) C4 ON C4.PARENT_ID=C3.LABEL_ID\n" +
                "LEFT JOIN SCCOC.CI_LABEL_EXT_INFO C ON C.LABEL_ID=C1.LABEL_ID\n" +
                "WHERE C.LABEL_LEVEL=1";
        return this.getSimpleJdbcTemplate().queryForInt(sql, new Object[0]);
    }
}
