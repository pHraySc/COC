package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.model.TemplateShortInfo;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class TemplateShortInfoRowMapper implements ParameterizedRowMapper<TemplateShortInfo> {
    public TemplateShortInfoRowMapper() {
    }

    public TemplateShortInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        TemplateShortInfo templateInfo = new TemplateShortInfo();
        templateInfo.setTemplateId(rs.getString("TEMPLATE_ID"));
        templateInfo.setTemplateName(rs.getString("TEMPLATE_NAME"));
        return templateInfo;
    }
}
