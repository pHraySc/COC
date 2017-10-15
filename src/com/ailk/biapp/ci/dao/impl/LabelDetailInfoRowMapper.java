package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.model.LabelDetailInfo;
import com.ailk.biapp.ci.util.DateUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class LabelDetailInfoRowMapper implements ParameterizedRowMapper<LabelDetailInfo> {
    public LabelDetailInfoRowMapper() {
    }

    public LabelDetailInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        LabelDetailInfo labelInfo = new LabelDetailInfo();
        labelInfo.setLabelId((new Long(rs.getLong("LABEL_ID"))).toString());
        labelInfo.setLabelName(rs.getString("LABEL_NAME"));
        labelInfo.setEffecTime(DateUtil.date2String(rs.getDate("EFFEC_TIME"), "yyyy-MM-dd"));
        labelInfo.setFailTime(DateUtil.date2String(rs.getDate("FAIL_TIME"), "yyyy-MM-dd"));
        labelInfo.setCreateUserId(rs.getString("CREATE_USER_ID"));
        labelInfo.setPublishUserId(rs.getString("PUBLISH_USER_ID"));
        labelInfo.setTechCaliber(rs.getString("TECH_CALIBER"));
        labelInfo.setDataStatus(rs.getString("DATA_STATUS_NAME"));
        labelInfo.setUpdateCycle(rs.getString("UPDATE_CYCLE"));
        labelInfo.setCreateTime(DateUtil.date2String(rs.getDate("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss"));
        labelInfo.setPublishTime(DateUtil.date2String(rs.getDate("PUBLISH_TIME"), "yyyy-MM-dd HH:mm:ss"));
        labelInfo.setBusiCaliber(rs.getString("BUSI_CALIBER"));
        labelInfo.setCustomNum(rs.getString("CUSTOM_NUM"));
        labelInfo.setDataSource(rs.getString("DATA_SOURCE"));
        labelInfo.setMaxVal(Double.valueOf(rs.getDouble("MAX_VAL")));
        labelInfo.setMinVal(Double.valueOf(rs.getDouble("MIN_VAL")));
        labelInfo.setLabelTypeId(Integer.valueOf(rs.getInt("LABEL_TYPE_ID")));
        labelInfo.setBusiLegend(rs.getString("busi_legend"));
        labelInfo.setApplySuggest(rs.getString("apply_suggest"));
        labelInfo.setAttrVal(rs.getString("ATTR_VAL"));
        labelInfo.setIsAttention(rs.getString("ISATTENTION"));
        labelInfo.setIsSysRecom(Integer.valueOf(rs.getInt("IS_SYS_RECOM")));
        labelInfo.setDataDateStr(rs.getString("DATA_DATE"));
        return labelInfo;
    }
}
