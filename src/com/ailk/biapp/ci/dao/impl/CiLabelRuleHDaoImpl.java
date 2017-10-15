package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiLabelRuleHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiLabelRule;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiLabelRuleHDaoImpl extends HibernateBaseDao<CiLabelRule, String> implements ICiLabelRuleHDao {
    public CiLabelRuleHDaoImpl() {
    }

    public void insertCiLabelRule(CiLabelRule ciLabelRule) {
        this.save(ciLabelRule);
    }

    public void deleteCiLabelRuleListByCustomerOrTemplateId(String id, Integer customType) {
        String sql = "delete from CiLabelRule rule where rule.customId = ? and rule.customType = ?";
        this.batchExecute(sql, new Object[]{id, customType});
    }

    public List<CiLabelRule> selectCiLabelRuleList(String id, Integer customType) {
        List ciLabelRuleList = null;
        String hql = "from CiLabelRule rule where rule.customId = ? and rule.customType = ? order by rule.sortNum asc";
        ciLabelRuleList = this.find(hql, new Object[]{id, customType});
        return ciLabelRuleList;
    }

    public List<CiLabelRule> selectCiLabelRuleListByLabelId(String labelId, Integer customType) {
        List ciLabelRuleList = null;
        String hql = "from CiLabelRule rule where rule.calcuElement = ? and rule.customType = ?";
        ciLabelRuleList = this.find(hql, new Object[]{labelId, customType});
        return ciLabelRuleList;
    }
}
