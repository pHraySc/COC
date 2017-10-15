package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.model.GridDemoModel;
import com.ailk.biapp.ci.util.DateUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class GridDemoRowMapper implements ParameterizedRowMapper<GridDemoModel> {
    public GridDemoRowMapper() {
    }

    public GridDemoModel mapRow(ResultSet rs, int arg1) throws SQLException {
        GridDemoModel demo = new GridDemoModel();
        demo.setTopicName(rs.getString("TOPIC_NAME"));
        demo.setCreateUserId(rs.getString("CREATE_USER_ID"));
        demo.setCategoryName(rs.getString("CATEGORY_NAME"));
        demo.setCycleTypeId(rs.getString("CYCLE_TYPE_ID"));
        demo.setCreateDate(DateUtil.date2String(rs.getDate("CREATE_DATE")));
        return demo;
    }
}
