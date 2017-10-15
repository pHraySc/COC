package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiCustomProductRelJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.util.CiIdentifierGenerator;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

@Repository
public class CiCustomProductRelJDaoImpl extends JdbcBaseDao implements ICiCustomProductRelJDao {
    public CiCustomProductRelJDaoImpl() {
    }

    public void insertCustomProductRel(final String customGroupId, final String[] productIds, final String userId) throws Exception {
        StringBuffer sql = new StringBuffer();
        String columns = "USER_ID,CUSTOM_GROUP_ID,PRODUCT_ID,CREATE_DATE,STATUS";
        String valuePosition = columns.replaceAll("\\w+,", "?,");
        valuePosition = valuePosition.replaceAll("\\w+$", "?");
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
        sql.append("INSERT INTO CI_CUSTOM_PRODUCT_REL(").append(columns).append(") VALUES(").append(valuePosition).append(")");
        final String createDate = sf.format(new Date());
        String status = "1";
        this.getSimpleJdbcTemplate().getJdbcOperations().batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                CiIdentifierGenerator.randomUUID();
                String productId = productIds[i];
                ps.setString(1, userId);
                ps.setString(2, customGroupId);
                ps.setInt(3, Integer.valueOf(productId).intValue());
                ps.setString(4, createDate);
                ps.setString(5, "1");
            }

            public int getBatchSize() {
                return productIds.length;
            }
        });
    }
}
