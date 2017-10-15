package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.ICiTemplateInfoJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiTemplateInfo;
import com.ailk.biapp.ci.entity.DimScene;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.util.DateUtil;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiTemplateInfoJDaoImpl extends JdbcBaseDao implements ICiTemplateInfoJDao {
    private Logger log = Logger.getLogger(CiTemplateInfoJDaoImpl.class);

    public CiTemplateInfoJDaoImpl() {
    }

    public int selectTemplateInfoCount(CiTemplateInfo searchBean) throws Exception {
        String sql = "select count(T.TEMPLATE_ID) " + this.getFromSql(searchBean);
        this.log.debug("count sql is : " + sql);
        int count = this.getSimpleJdbcTemplate().queryForInt(sql, new BeanPropertySqlParameterSource(searchBean));
        return count;
    }

    public List<CiTemplateInfo> selectTemplateInfoList(Pager pager, CiTemplateInfo searchBean) throws Exception {
        String sql = "select T.*,CASE WHEN T3.SUMCC IS NULL THEN 0 ELSE T3.SUMCC END NUM " + this.getFromSql(searchBean);
        if(searchBean.getIsHotList() == ServiceConstants.IS_HOT_LIST) {
            sql = sql + " order by NUM desc";
        } else if(StringUtil.isEmpty(pager.getOrderBy())) {
            sql = sql + " order by T.NEW_MODIFY_TIME desc";
        } else {
            String[] sqlPage = pager.getOrderBy().split(",");
            String[] list = pager.getOrder().split(",");
            StringBuffer sf = new StringBuffer("");
            if(sqlPage.length != list.length) {
                throw new CIServiceException("排序字段和排序方式个数不匹配！");
            }

            for(int i = 0; i < sqlPage.length; ++i) {
                String orderSql;
                if(i == sqlPage.length - 1) {
                    if(sqlPage[i].equals("USECOUNT")) {
                        orderSql = "NUM";
                        sf.append(orderSql + " " + list[i]);
                    } else {
                        sf.append("T." + sqlPage[i] + " " + list[i]);
                    }
                } else if(sqlPage[i].equals("USECOUNT")) {
                    orderSql = "NUM";
                    sf.append(orderSql + " " + list[i] + ",");
                } else {
                    sf.append("T." + sqlPage[i] + " " + list[i] + ",");
                }
            }

            sql = sql + " order by " + sf.toString();
        }

        this.log.debug("select sql is : " + sql);
        String var9 = this.getDataBaseAdapter().getPagedSql(sql, pager.getPageNum(), pager.getPageSize());
        List var10 = this.getSimpleJdbcTemplate().query(var9, ParameterizedBeanPropertyRowMapper.newInstance(CiTemplateInfo.class), new BeanPropertySqlParameterSource(searchBean));
        return var10;
    }

    private String getFromSql(CiTemplateInfo searchBean) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("from CI_TEMPLATE_INFO T  ").append("LEFT JOIN (SELECT  T1.CUSTOM_ID,COUNT(T1.CUSTOM_ID) SUMCC FROM CI_USER_USE_CUSTOM T1 WHERE T1.IS_TEMPLATE = 1 GROUP BY T1.CUSTOM_ID) T3 ON T.TEMPLATE_ID = T3.CUSTOM_ID");
        if(searchBean.getIsHotList() == ServiceConstants.IS_HOT_LIST) {
            sql.append(" where T.STATUS = ").append(1);
            sql.append(" and T.IS_PRIVATE = 0");
        } else {
            sql.append(" where T.STATUS > ").append(0);
            if(StringUtil.isNotEmpty(searchBean.getUserId())) {
                sql.append(" and (T.USER_ID = :userId or T.IS_PRIVATE = 0) ");
            }
        }

        if(StringUtil.isNotEmpty(searchBean.getTemplateName())) {
            searchBean.setTemplateName("%" + searchBean.getTemplateName().replace("|", "||").replace("%", "|%").replace("_", "|_") + "%");
            sql.append(" and ( (T.TEMPLATE_NAME like :templateName  escape \'|\') ");
            sql.append(" or (T.TEMPLATE_DESC like :templateName  escape \'|\') ");
            sql.append(" or (T.LABEL_OPT_RULE_SHOW like :templateName  escape \'|\') ) ");
        }

        SimpleDateFormat sdf;
        String startDay;
        if(StringUtil.isNotEmpty(searchBean.getStartDate()) || StringUtil.isNotEmpty(searchBean.getEndDate())) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat startMonth = new SimpleDateFormat("yyyy-MM-dd");
            Date endDate;
            if(StringUtil.isNotEmpty(searchBean.getStartDate())) {
                startDay = startMonth.format(searchBean.getStartDate());
                endDate = sdf.parse(startDay + " 00:00:00");
                searchBean.setStartDate(endDate);
                sql.append(" and T.ADD_TIME >= :startDate");
            }

            if(StringUtil.isNotEmpty(searchBean.getEndDate())) {
                startDay = startMonth.format(searchBean.getEndDate());
                endDate = sdf.parse(startDay + " 23:59:59");
                searchBean.setEndDate(endDate);
                sql.append(" and T.ADD_TIME <= :endDate");
            }
        }

        if("1".equals(searchBean.getDateType())) {
            String sdf1 = DateUtil.getCurrentDay();
            sql.append(" AND T.ADD_TIME > to_date(\'").append(sdf1).append("\',\'yyyy-MM-dd HH24:mi:ss\')");
        }

        String startMonth1;
        if("2".equals(searchBean.getDateType())) {
            sdf = new SimpleDateFormat("yyyyMM");
            startMonth1 = DateUtil.getFrontMonth(0, DateUtil.date2String(new Date(), "yyyyMM"));
            startDay = DateUtil.date2String(DateUtil.firstDayOfMonth(sdf.parse(startMonth1)), "yyyy-MM-dd HH:mm:ss");
            System.out.println(startDay);
            sql.append(" AND T.ADD_TIME > to_date(\'").append(startDay).append("\',\'yyyy-MM-dd HH24:mi:ss\')");
        }

        if("3".equals(searchBean.getDateType())) {
            sdf = new SimpleDateFormat("yyyyMM");
            startMonth1 = DateUtil.getFrontMonth(2, DateUtil.date2String(new Date(), "yyyyMM"));
            startDay = DateUtil.date2String(DateUtil.firstDayOfMonth(sdf.parse(startMonth1)), "yyyy-MM-dd HH:mm:ss");
            sql.append(" AND T.ADD_TIME > to_date(\'").append(startDay).append("\',\'yyyy-MM-dd HH24:mi:ss\')");
        }

        if(StringUtil.isNotEmpty(searchBean.getLabelOptRuleShow())) {
            searchBean.setLabelOptRuleShow("%" + searchBean.getLabelOptRuleShow().replace("|", "||").replace("%", "|%").replace("_", "|_") + "%");
            sql.append(" and exists ").append("( ").append("SELECT R.CUSTOM_ID").append(" from CI_LABEL_INFO L,CI_LABEL_RULE R").append(" WHERE R.CUSTOM_TYPE = ").append(2).append(" and T.TEMPLATE_ID = R.CUSTOM_ID").append(" and R.ELEMENT_TYPE = ").append(2).append(" and R.CALCU_ELEMENT = ").append(this.getDataBaseAdapter().getIntToChar("L.LABEL_ID")).append(" and L.LABEL_NAME like :labelOptRuleShow escape \'|\')");
        }

        if(StringUtil.isNotEmpty(searchBean.getIsPrivate())) {
            sql.append(" and  T.IS_PRIVATE = :isPrivate ");
        }

        if(StringUtil.isNotEmpty(searchBean.getSceneId())) {
            sql.append(" and T.SCENE_ID = :sceneId ");
        }

        if(StringUtil.isNotEmpty(searchBean.getModifyStartDate())) {
            sql.append(" AND T.NEW_MODIFY_TIME>= :modifyStartDate");
        }

        if(StringUtil.isNotEmpty(searchBean.getModifyEndDate())) {
            sql.append(" AND T.NEW_MODIFY_TIME<= :modifyEndDate");
        }

        return sql.toString();
    }

    public List<CiTemplateInfo> selectTemplateInfoList(String userId) throws Exception {
        String sql = " SELECT * FROM CI_TEMPLATE_INFO i WHERE i.STATUS=1 AND i.USER_ID=? ";
        List templateInfoList = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiTemplateInfo.class), new Object[]{userId});
        return templateInfoList;
    }

    public List<CiTemplateInfo> selectTemplateInfoList(String userId, String templateName) throws Exception {
        String sql = " SELECT * FROM CI_TEMPLATE_INFO i WHERE i.STATUS=1 AND i.USER_ID=:userId AND i.TEMPLATE_NAME like :templateName ";
        templateName = templateName.trim();
        HashMap queryParam = new HashMap();
        queryParam.put("userId", userId);
        if(StringUtil.isNotEmpty(templateName)) {
            queryParam.put("templateName", "%" + templateName.replace("|", "||").replace("%", "|%").replace("_", "|_") + "%");
            sql = sql + " escape \'|\'";
        } else {
            queryParam.put("templateName", "%" + templateName + "%");
        }

        List templateInfoList = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiTemplateInfo.class), queryParam);
        return templateInfoList;
    }

    public DimScene selectSceneInfoById(String scenceId) {
        String sql = " select * from DIM_SCENE WHERE SCENE_ID=:scenceId ";
        HashMap queryParam = new HashMap();
        queryParam.put("scenceId", scenceId);
        List templateInfoList = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(DimScene.class), queryParam);
        return templateInfoList != null && templateInfoList.size() > 0?(DimScene)templateInfoList.get(0):null;
    }

    public List<CiTemplateInfo> indexQueryTemplateName(String name, String userId) throws Exception {
        ArrayList templateNameList = new ArrayList();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT TEMPLATE_NAME").append(" FROM CI_TEMPLATE_INFO").append(" WHERE LOWER(TEMPLATE_NAME) LIKE ").append("LOWER(\'%" + name + "%\')");
        if(StringUtil.isNotEmpty(userId)) {
            sql.append(" AND ( USER_ID = \'").append(userId).append("\' OR IS_PRIVATE = ").append(0).append(" ) ");
        }

        sql.append(" AND STATUS = ").append(1);
        String sqlPage = this.getDataBaseAdapter().getPagedSql(sql.toString(), 1, 9);
        List mapList = this.getSimpleJdbcTemplate().queryForList(sqlPage, new Object[0]);
        Iterator i$ = mapList.iterator();

        while(i$.hasNext()) {
            Map map = (Map)i$.next();
            CiTemplateInfo ciTemplateInfo = new CiTemplateInfo();
            ciTemplateInfo.setTemplateName((String)map.get("TEMPLATE_NAME"));
            templateNameList.add(ciTemplateInfo);
        }

        return templateNameList;
    }
}
