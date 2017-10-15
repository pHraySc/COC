package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiUserLabelRelJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiUserLabelRelId;
import com.ailk.biapp.ci.model.LabelShortInfo;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository("ciUserLabelRelJDao")
public class CiUserLabelRelJDaoImpl extends JdbcBaseDao implements ICiUserLabelRelJDao {
    private static Logger log = Logger.getLogger(CiUserLabelRelJDaoImpl.class);

    public CiUserLabelRelJDaoImpl() {
    }

    public List<LabelShortInfo> getCiUserLabelRelList(String userId, Integer labelId) {
        String sql = " SELECT l.LABEL_ID,l.LABEL_NAME FROM CI_LABEL_INFO l,  (SELECT r.ASSOCI_LABEL_ID FROM CI_USER_LABEL_REL r WHERE r.STATUS=1 AND r.MAIN_LABEL_ID = :mainLabelId AND r.USER_ID=:userId ) t WHERE l.LABEL_ID = t.ASSOCI_LABEL_ID ";
        CiUserLabelRelId queryId = new CiUserLabelRelId();
        queryId.setMainLabelId(labelId);
        queryId.setUserId(userId);
        List results = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(LabelShortInfo.class), new BeanPropertySqlParameterSource(queryId));
        return results;
    }

    public List<LabelShortInfo> getLabelRelRecommend(int currPage, int pageSize, Integer labelId) {
        String sql = " SELECT c.LABEL_ID,c.UPDATE_CYCLE,c.LABEL_NAME,c.TIMES FROM ( SELECT l.LABEL_ID,l.UPDATE_CYCLE,l.LABEL_NAME,COUNT(*) AS TIMES FROM CI_LABEL_INFO l, (SELECT r.ASSOCI_LABEL_ID FROM CI_USER_LABEL_REL r WHERE r.STATUS=1 AND r.MAIN_LABEL_ID = :mainLabelId) t WHERE l.LABEL_ID = t.ASSOCI_LABEL_ID AND l.DATA_STATUS_ID=2 GROUP BY LABEL_ID,LABEL_NAME,UPDATE_CYCLE ) c ORDER BY TIMES DESC ";
        String sqlPag = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        log.debug(sqlPag);
        CiUserLabelRelId queryId = new CiUserLabelRelId();
        queryId.setMainLabelId(labelId);
        List results = this.getSimpleJdbcTemplate().query(sqlPag, ParameterizedBeanPropertyRowMapper.newInstance(LabelShortInfo.class), new BeanPropertySqlParameterSource(queryId));
        return results;
    }

    public List<LabelShortInfo> getLabelRelRecommend(int currPage, int pageSize, Integer labelId, String queryLabelName) {
        String sql = " SELECT c.LABEL_ID,c.UPDATE_CYCLE,c.LABEL_NAME,c.TIMES FROM ( SELECT l.LABEL_ID,l.UPDATE_CYCLE,l.LABEL_NAME,COUNT(*) AS TIMES FROM CI_LABEL_INFO l, (SELECT r.ASSOCI_LABEL_ID FROM CI_USER_LABEL_REL r WHERE r.STATUS=1 AND r.MAIN_LABEL_ID = :mainLabelId) t WHERE l.LABEL_ID = t.ASSOCI_LABEL_ID AND l.DATA_STATUS_ID=2 AND l.LABEL_NAME like :queryLabelName GROUP BY LABEL_ID,LABEL_NAME,UPDATE_CYCLE ) c ORDER BY TIMES DESC ";
        String sqlPag = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        log.debug(sqlPag);
        HashMap queryParam = new HashMap();
        queryParam.put("mainLabelId", labelId);
        queryParam.put("queryLabelName", "%" + queryLabelName + "%");
        List results = this.getSimpleJdbcTemplate().query(sqlPag, ParameterizedBeanPropertyRowMapper.newInstance(LabelShortInfo.class), queryParam);
        return results;
    }
}
