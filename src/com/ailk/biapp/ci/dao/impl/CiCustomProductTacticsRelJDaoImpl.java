package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiCustomProductTacticsRelJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiCustomProductTacticsRel;
import java.util.List;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiCustomProductTacticsRelJDaoImpl extends JdbcBaseDao implements ICiCustomProductTacticsRelJDao {
    public CiCustomProductTacticsRelJDaoImpl() {
    }

    public List<CiCustomProductTacticsRel> selectCustomProductTacticsRelList(String tacticId) throws Exception {
        CiCustomProductTacticsRel bean = new CiCustomProductTacticsRel();
        bean.setTacticId(tacticId);
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT I.CUSTOM_GROUP_NAME,R.PRODUCT_ID FROM CI_CUSTOM_PRODUCT_TACTICS_REL R").append(" LEFT JOIN CI_CUSTOM_GROUP_INFO I ON R.CUSTOM_GROUP_ID = I.CUSTOM_GROUP_ID").append(" WHERE R.TACTIC_ID = :tacticId AND R.STATUS = \'1\'");
        return this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiCustomProductTacticsRel.class), new BeanPropertySqlParameterSource(bean));
    }
}
