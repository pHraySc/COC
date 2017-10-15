package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.IVCiLabelCustomListJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.dao.impl.CiCustomListExeInfoJDaoImpl;
import com.ailk.biapp.ci.entity.VCiLabelCustomList;
import com.ailk.biapp.ci.model.Pager;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class VCiLabelCustomListJDaoImpl extends JdbcBaseDao implements IVCiLabelCustomListJDao {
    private Logger log = Logger.getLogger(CiCustomListExeInfoJDaoImpl.class);

    public VCiLabelCustomListJDaoImpl() {
    }

    public List<VCiLabelCustomList> selectLabelCustomAttentionList(String userId, Pager pager) {
        StringBuffer sql = new StringBuffer();
        sql.append("select LABEL_CUSTOM_ID,LABEL_CUSTOM_NAME,ATTENTION_TIME,TYPE_ID,USER_ID from ( ").append("select t1.LABEL_CUSTOM_ID,t1.LABEL_CUSTOM_NAME,t1.ATTENTION_TIME,t1.TYPE_ID ,t1.USER_ID from ").append("( SELECT  ").append(this.getDataBaseAdapter().getIntToChar("l.LABEL_ID")).append("  AS LABEL_CUSTOM_ID, l.LABEL_NAME AS LABEL_CUSTOM_NAME,v1.ATTENTION_TIME AS ATTENTION_TIME, v1.TYPE_ID AS TYPE_ID, v1.USER_ID AS USER_ID ").append("FROM  V_CI_LABEL_CUSTOM_LIST v1 JOIN CI_LABEL_INFO l ON ").append(this.getDataBaseAdapter().getIntToChar("l.LABEL_ID")).append(" = v1.LABEL_CUSTOM_ID )  ").append("t1 ").append("UNION all ").append("select t2.LABEL_CUSTOM_ID,t2.LABEL_CUSTOM_NAME,t2.ATTENTION_TIME,t2.TYPE_ID,t2.USER_ID from ").append("(SELECT c.CUSTOM_GROUP_ID   AS LABEL_CUSTOM_ID ,c.CUSTOM_GROUP_NAME AS LABEL_CUSTOM_NAME, v2.ATTENTION_TIME   AS ATTENTION_TIME ,v2.TYPE_ID AS TYPE_ID ,v2.USER_ID AS USER_ID ").append("FROM V_CI_LABEL_CUSTOM_LIST v2  JOIN  CI_CUSTOM_GROUP_INFO c ON c.CUSTOM_GROUP_ID = v2.LABEL_CUSTOM_ID  and c.STATUS = ").append(1).append(")  ").append("t2 ) WHERE USER_ID = ? order by ATTENTION_TIME desc");
        String sqlPage = "";
        if(pager.getPageSize() != 0) {
            sqlPage = this.getDataBaseAdapter().getPagedSql(sql.toString(), pager.getPageNum(), pager.getPageSize());
        } else {
            sqlPage = sql.toString();
        }

        this.log.debug("sql=" + sqlPage);
        return this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(VCiLabelCustomList.class), new Object[]{userId});
    }
}
