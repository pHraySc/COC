package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.model.LabelShortInfo;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class LabelShortInfoRowMapper implements ParameterizedRowMapper<LabelShortInfo> {
    public LabelShortInfoRowMapper() {
    }

    public LabelShortInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        LabelShortInfo labelInfo = new LabelShortInfo();
        labelInfo.setLabelId(rs.getString("LABEL_ID"));
        labelInfo.setLabelName(rs.getString("LABEL_NAME"));
        return labelInfo;
    }
}
