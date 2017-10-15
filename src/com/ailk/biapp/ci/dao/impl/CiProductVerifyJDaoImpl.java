package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiProductVerifyInfoJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiVerifyFilterProcess;
import com.ailk.biapp.ci.entity.CiVerifyMm;
import com.ailk.biapp.ci.entity.CiVerifyMmId;
import com.ailk.biapp.ci.entity.CiVerifyRuleInfo;
import com.ailk.biapp.ci.entity.CiVerifyRuleRel;
import com.ailk.biapp.ci.entity.DimVerifyIndexShow;
import com.ailk.biapp.ci.util.DateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiProductVerifyJDaoImpl extends JdbcBaseDao implements ICiProductVerifyInfoJDao {
    public CiProductVerifyJDaoImpl() {
    }

    public String selectRuleDesc(String offerId) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT RULE_DESC FROM CI_VERIFY_RULE_REL WHERE OFFER_ID =\'").append(offerId).append("\'");
        List list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiVerifyRuleRel.class), new Object[0]);
        return list.size() > 0?((CiVerifyRuleRel)list.get(0)).getRuleDesc():"";
    }

    public List<CiVerifyRuleRel> selectVerifyRuleRelList(String offerId) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT R.OFFER_ID,R.RULE_DESC,R.COUNT_RULES_CODE,M.RECALL,M.UPGRADE_MULTIPLE,M.POTENTIAL_USER_NUM,M.STARS_ID,M.DATA_DATE").append(" FROM CI_VERIFY_MM M,CI_VERIFY_RULE_REL R").append(" WHERE R.COUNT_RULES_CODE = M.COUNT_RULES_CODE AND R.OFFER_ID =\'").append(offerId).append("\'").append(" ORDER BY M.DATA_DATE DESC");
        String sqlHis = this.getDataBaseAdapter().getPagedSql(sql.toString(), 1, 6);
        List list = this.getSimpleJdbcTemplate().query(sqlHis, ParameterizedBeanPropertyRowMapper.newInstance(CiVerifyMm.class), new Object[0]);
        HashMap map = new HashMap();
        ArrayList resultList = new ArrayList();
        Iterator i$ = list.iterator();

        while(i$.hasNext()) {
            CiVerifyMm entry = (CiVerifyMm)i$.next();
            CiVerifyMmId id = new CiVerifyMmId();
            id.setCountRulesCode(entry.getCountRulesCode());
            id.setDataDate(entry.getDataDate());
            entry.setId(id);
            String starsHtml = "<ul class=\'starsList\'>";

            for(int ruleRel = 0; ruleRel < 5; ++ruleRel) {
                if(((double)entry.getStarsId().shortValue() - 1.0D) / 2.0D > (double)ruleRel) {
                    starsHtml = starsHtml + "<li>&nbsp;</li>";
                } else if(((double)entry.getStarsId().shortValue() - 1.0D) / 2.0D == (double)ruleRel) {
                    starsHtml = starsHtml + "<li class=\'half\'>&nbsp;</li>";
                } else if(((double)entry.getStarsId().shortValue() - 1.0D) / 2.0D < (double)ruleRel) {
                    starsHtml = starsHtml + "<li class=\'dis\'>&nbsp;</li>";
                }
            }

            starsHtml = starsHtml + "</ul>";
            entry.setStarsHtml(starsHtml);
            entry.setRecall(Double.valueOf(entry.getRecall().doubleValue() * 100.0D));
            entry.setRecallInt(Integer.valueOf(entry.getRecall().intValue()));
            if(map.containsKey(entry.getCountRulesCode())) {
                ((CiVerifyRuleRel)map.get(entry.getCountRulesCode())).getVerifyMmList().add(entry);
            } else {
                CiVerifyRuleRel var13 = new CiVerifyRuleRel();
                var13.setVerifyMmList(new ArrayList());
                var13.getVerifyMmList().add(entry);
                var13.setRuleDesc(entry.getRuleDesc());
                map.put(entry.getCountRulesCode(), var13);
            }
        }

        i$ = map.entrySet().iterator();

        while(i$.hasNext()) {
            Entry var12 = (Entry)i$.next();
            resultList.add(var12.getValue());
        }

        return resultList;
    }

    public List<CiVerifyMm> selectVerifyMmList(String offerId) throws Exception {
        Date today = new Date();
        String month = DateUtil.date2String(today, "yyyyMM");
        String frontMonth = DateUtil.getFrontMonth(1, month);
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT R.OFFER_ID,M.RECALL,M.UPGRADE_MULTIPLE,M.POTENTIAL_USER_NUM,M.NEW_ORDER_USER_NUM,M.DATA_DATE,M.STARS_ID  FROM CI_VERIFY_MM M,CI_VERIFY_RULE_REL R").append(" WHERE R.COUNT_RULES_CODE = M.COUNT_RULES_CODE AND R.OFFER_ID =\'").append(offerId).append("\'").append(" AND M.DATA_DATE <=\'").append(frontMonth).append("\' ORDER BY M.DATA_DATE");
        String sqlHis = this.getDataBaseAdapter().getPagedSql(sql.toString(), 1, 6);
        return this.getSimpleJdbcTemplate().query(sqlHis, ParameterizedBeanPropertyRowMapper.newInstance(CiVerifyMm.class), new Object[0]);
    }

    public List<CiVerifyFilterProcess> selectLabelProcess(String productId) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ").append(" e.datum_month,e.datum_new_user_num,e.label_select_conclusion,").append(" f.label_index_name,f.last_user_num,f.last_propotion,f.not_last_user_num,f.not_last_propotion,f.eigenvalue").append(" FROM").append(" CI_VERIFY_RULE_REL e, CI_VERIFY_filter_process f").append(" WHERE").append(" e.offer_id = \'" + productId + "\' AND f.process_type_id =\'L3\' AND e.COUNT_RULES_CODE = f.COUNT_RULES_CODE AND e.effect_flag =1").append(" order by f.eigenvalue desc ");
        return this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiVerifyFilterProcess.class), new Object[0]);
    }

    public List<CiVerifyFilterProcess> selectLabelProcessLevel(String productId, String level) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ").append(" e.datum_month,e.datum_new_user_num,").append(" f.label_index_name,f.last_user_num,f.last_propotion,f.not_last_user_num,f.not_last_propotion,f.eigenvalue").append(" FROM").append(" CI_VERIFY_RULE_REL e, CI_VERIFY_filter_process f").append(" WHERE").append(" e.offer_id = \'" + productId + "\' AND f.process_type_id =\'" + level + "\' AND e.COUNT_RULES_CODE = f.COUNT_RULES_CODE AND e.effect_flag =1");
        if(level.equalsIgnoreCase("L2")) {
            sql.append(" order by f.last_user_num desc ");
        } else {
            sql.append(" order by f.eigenvalue desc ");
        }

        return this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiVerifyFilterProcess.class), new Object[0]);
    }

    public List<CiVerifyRuleRel> selectFinalPotentialUserRule(String offerId) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ").append(" e.rule_desc,e.datum_recall,e.datum_upgrade_multiple,e.final_rule_conclusion").append(" FROM \tCI_VERIFY_RULE_REL e\t").append(" WHERE e.offer_id = \'" + offerId + "\'").append(" AND e.effect_flag = 1 ");
        return this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiVerifyRuleRel.class), new Object[0]);
    }

    public List<CiVerifyFilterProcess> selectIndexProcess(String offerId) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ").append(" e.datum_month,e.datum_new_user_num,e.index_select_conclusion,").append(" f.label_index_name,f.last_user_num,f.last_propotion,f.not_last_user_num,f.not_last_propotion,f.eigenvalue,f.index_interval").append(" FROM").append(" CI_VERIFY_RULE_REL e, CI_VERIFY_filter_process f").append(" WHERE").append(" e.offer_id = \'" + offerId + "\' AND f.process_type_id =\'I2\' AND e.COUNT_RULES_CODE = f.COUNT_RULES_CODE AND e.effect_flag =1").append(" order by f.eigenvalue desc ");
        return this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiVerifyFilterProcess.class), new Object[0]);
    }

    public List<CiVerifyFilterProcess> selectIndexProcessLevel(String offerId, String level) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ").append(" e.datum_month,e.datum_new_user_num,e.index_select_conclusion,").append(" f.label_index_name,f.last_user_num,f.last_propotion,f.not_last_user_num,f.not_last_propotion,f.eigenvalue,f.index_interval").append(" FROM").append(" CI_VERIFY_RULE_REL e, CI_VERIFY_filter_process f").append(" WHERE").append(" e.offer_id = \'" + offerId + "\' AND f.process_type_id =\'" + level + "\' AND e.COUNT_RULES_CODE = f.COUNT_RULES_CODE AND e.effect_flag =1").append(" order by f.eigenvalue desc ");
        return this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiVerifyFilterProcess.class), new Object[0]);
    }

    public List<CiVerifyRuleInfo> selectIndexRuleDescribe(String offerId) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ").append(" f.rule_condition").append(" FROM \tCI_VERIFY_RULE_REL e, CI_VERIFY_rule_info f\t").append(" WHERE e.offer_id = \'" + offerId + "\'").append(" AND e.COUNT_RULES_CODE = f.COUNT_RULES_CODE AND e.effect_flag = 1 AND f.label_index_flag = \'I\' ");
        return this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiVerifyRuleInfo.class), new Object[0]);
    }

    public List<CiVerifyRuleInfo> selectLabelRule(String offerId) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ").append(" e.datum_month,e.label_rule_conclusion,").append("  f.rule_condition,f.datum_recall,f.datum_upgrade_multiple,f.optimal_flag").append(" FROM \tCI_VERIFY_RULE_REL e,CI_VERIFY_rule_info f \t").append(" WHERE e.offer_id = \'" + offerId + "\'").append(" AND e.COUNT_RULES_CODE = f.COUNT_RULES_CODE").append(" AND e.effect_flag = 1 and f.label_index_flag = \'L\'");
        return this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiVerifyRuleInfo.class), new Object[0]);
    }

    public List<DimVerifyIndexShow> selectIndexList() throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM DIM_VERIFY_INDEX_SHOW");
        return this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(DimVerifyIndexShow.class), new Object[0]);
    }
}
