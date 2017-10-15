package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.IDimCocLabelStatusJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.DimCocLabelStatus;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DimCocLabelStatusJDaoImpl extends JdbcBaseDao implements IDimCocLabelStatusJDao {
    public DimCocLabelStatusJDaoImpl() {
    }

    public List<DimCocLabelStatus> selectDimCocLabelStatusList(String dataDate) {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from DIM_COC_LABEL_STATUS where DATA_DATE = ? ").append("and DATA_STATUS = ").append(1);
        List list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(DimCocLabelStatus.class), new Object[]{dataDate});
        return list;
    }
}
