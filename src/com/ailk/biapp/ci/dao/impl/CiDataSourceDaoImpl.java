package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICidataSourceDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiDataSource;
import com.ailk.biapp.ci.model.Pager;
import com.asiainfo.biframe.utils.string.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by admin on 2017/4/24.
 */
@Repository("cidataSourceDao")
@Transactional
public class CiDataSourceDaoImpl extends JdbcBaseDao implements ICidataSourceDao {

    private Logger log = Logger.getLogger(CiDataSourceDaoImpl.class);


    public CiDataSourceDaoImpl() {
    }

    @Override
    public List<CiDataSource> getDataSource(Pager pager) {


        String sql = "select a.data_src_code,a.data_src_tab_name,e.label_id,e.label_name from sccoc.DIM_COC_INDEX_TABLE_INFO a \n" +
                "left join sccoc.DIM_COC_INDEX_INFO b on a.data_src_code=b.data_src_code \n" +
                "left join sccoc.DIM_COC_LABEL_COUNT_RULES  c on b.index_code=c.depend_index\n" +
                "left join  sccoc.CI_LABEL_EXT_INFO d on c.count_rules_code=d.count_rules_code\n" +
                "left join  sccoc.CI_LABEL_INFO  e on d.label_id=e.label_id\n" +
                "where e.label_id is not null order by a.data_src_code";

        String sqlPage;
        if (pager.getPageSize() != 0) {
            sqlPage = this.getDataBaseAdapter().getPagedSql(sql.toString(), pager.getPageNum(), pager.getPageSize());
        } else {
            sqlPage = sql.toString();
        }

        this.log.debug("sql=" + sqlPage);
        List dataSourceList = this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(CiDataSource.class), new Object[0]);
        this.log.info("Sc");
        Iterator iterator = dataSourceList.iterator();
        this.log.info("Sc");
//        while (iterator.hasNext()) {
//            CiDataSource dataSource = (CiDataSource) iterator.next();
//            this.log.info(dataSource.getDataSrcTabName());
//            if(this.tabIsExit(dataSource.getDataSrcTabName()))
//                dataSource.setIsMissing("Y");
//            else
//                dataSource.setIsMissing("N");
//        }
        this.log.info("Dao exit");
        return dataSourceList;
    }

    @Override
    public int getDataCount() {
        String sql = "select count(1) from sccoc.DIM_COC_INDEX_TABLE_INFO a \n" +
                "left join sccoc.DIM_COC_INDEX_INFO b on a.data_src_code=b.data_src_code \n" +
                "left join sccoc.DIM_COC_LABEL_COUNT_RULES  c on b.index_code=c.depend_index\n" +
                "left join  sccoc.CI_LABEL_EXT_INFO d on c.count_rules_code=d.count_rules_code\n" +
                "left join  sccoc.CI_LABEL_INFO  e on d.label_id=e.label_id\n" +
                "where e.label_id is not null";
        return this.getSimpleJdbcTemplate().queryForInt(sql, new Object[0]);
    }

    @Override
    public int getDataCountByDataSrcCode(CiDataSource dataSource) {
        HashMap paramMap = new HashMap();
        this.log.info("dataSrcCode" + dataSource.getDataSrcCode());
        String sql = "select count(1) from sccoc.DIM_COC_INDEX_TABLE_INFO a \n" +
                "left join sccoc.DIM_COC_INDEX_INFO b on a.data_src_code=b.data_src_code \n" +
                "left join sccoc.DIM_COC_LABEL_COUNT_RULES  c on b.index_code=c.depend_index\n" +
                "left join  sccoc.CI_LABEL_EXT_INFO d on c.count_rules_code=d.count_rules_code\n" +
                "left join  sccoc.CI_LABEL_INFO e on d.label_id=e.label_id where ";

        if (StringUtil.isNotEmpty(dataSource.getDataSrcCode())) {
            sql = sql + " a.data_src_code=:dataSrcCode and e.label_id is not null ";
            this.log.info("sql" + sql);
            paramMap.put("dataSrcCode", dataSource.getDataSrcCode());
        }
        return this.getSimpleJdbcTemplate().queryForInt(sql, paramMap);
    }

    @Override
    public String getTheNewestDataDate(String labelId) {
        String result = null;
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DATA_DATE ");
        sql.append(" FROM SCCOC.CI_LABEL_INFO ");
        sql.append(" WHERE LABEL_ID = ? ");
        this.log.info("sql = " + sql.toString());
        Map map = this.getSimpleJdbcTemplate().queryForMap(sql.toString(), new Object[]{labelId});
        this.log.info("labelId = " + labelId);
        if (map != null && map.size() > 0) {
            result = (String) map.get("data_date");
            this.log.info("dataDate = " + result);
        }

        return result;
    }

    @Override
    public List selectInfluencedLabelByDataSrcCode(Pager pager, CiDataSource dataSource) {

        HashMap paramMap = new HashMap();

        String sql = "select e.label_id,e.label_name from sccoc.DIM_COC_INDEX_TABLE_INFO a \n" +
                "left join sccoc.DIM_COC_INDEX_INFO b on a.data_src_code=b.data_src_code \n" +
                "left join sccoc.DIM_COC_LABEL_COUNT_RULES  c on b.index_code=c.depend_index\n" +
                "left join  sccoc.CI_LABEL_EXT_INFO d on c.count_rules_code=d.count_rules_code\n" +
                "left join  sccoc.CI_LABEL_INFO e on d.label_id=e.label_id where ";
        if (StringUtil.isNotEmpty(dataSource.getDataSrcCode())) {
            sql = sql + " a.data_src_code=:dataSrcCode and e.label_id is not null order by e.label_id desc";
            paramMap.put("dataSrcCode", dataSource.getDataSrcCode());
        }
        List resultList = this.getSimpleJdbcTemplate().queryForList(this.getDataBaseAdapter().getPagedSql(sql, pager.getPageNum(), pager.getPageSize()), paramMap);
        this.log.info("resultLis" + resultList);
        return resultList;
    }

    private boolean tabIsExit(String tabName) {
        this.log.info("Sc in");
        try {
            String sql = "select count(tabname) from syscat.tables where tabname='" + tabName.toUpperCase() + "'";
            int count = this.getSimpleJdbcTemplate().queryForInt(sql, new Object[0]);
            if (count == 1) {
                this.log.info("Sc out");
                return true;
            } else {
                this.log.info("Sc out");
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public List getDataSourceIncludeDate(){
        String sql = "select a.data_src_code,a.data_src_tab_name,e.label_id,e.label_name,e.data_date from sccoc.DIM_COC_INDEX_TABLE_INFO a \n" +
                "left join sccoc.DIM_COC_INDEX_INFO b on a.data_src_code=b.data_src_code \n" +
                "left join sccoc.DIM_COC_LABEL_COUNT_RULES  c on b.index_code=c.depend_index\n" +
                "left join  sccoc.CI_LABEL_EXT_INFO d on c.count_rules_code=d.count_rules_code\n" +
                "left join  sccoc.CI_LABEL_INFO  e on d.label_id=e.label_id\n" +
                "where e.label_id is not null order by a.data_src_code";
        List<Map<String, Object>> resultList = this.getSimpleJdbcTemplate().queryForList(sql, new Object[0]);
        this.log.info("resultList_1" + resultList.get(0));
        return resultList;
    }
}