package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ILogTransferJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LogTransferJDaoImpl extends JdbcBaseDao implements ILogTransferJDao {
    private Logger log = Logger.getLogger(this.getClass());

    public LogTransferJDaoImpl() {
    }

    public List selectLgkOperLog(String logTableName) throws CIServiceException {
        StringBuffer sql = new StringBuffer();
        String jndi = Configure.getInstance().getProperty("JNDI_WEBOS_CI");
        sql.append("SELECT t1.logid,t1.operatedate,t1.sessionid,t1.operatorid,t2.staff_name,t1.bassid,t1.operatertype, ");
        sql.append("t1.operatername,t1.appid,t1.appname,t1.resourceid,t1.resourcename,t1.extresourceid,t1.resourcetype, ");
        sql.append("t1.resourcepath,t1.msg,t1.operationresult,t1.loglevel,t1.tabheader,t1.clientaddress,t1.serveraddress, ");
        sql.append("t1.threadname,t1.traffic,t1.logserverip,t1.logserverport, ");
        sql.append("t2.third_dept_id,t2.third_dept_name,t2.second_dept_id,t2.second_dept_name ");
        sql.append("FROM ( ");
        sql.append("SELECT * FROM ").append(logTableName).append(" WHERE appid = \'COC\' ");
        sql.append(") t1, (SELECT DISTINCT t3.staff_id, t3.staff_name, CASE t3.parent_dep_id WHEN \'1\' THEN \'\'  ELSE t3.dep_id END  third_dept_id,   ");
        sql.append("CASE t3.parent_dep_id WHEN \'1\' THEN \'\'  ELSE T3.DEP_NAME END third_dept_name,   ");
        sql.append("CASE t3.parent_dep_id WHEN \'1\' THEN T3.DEP_ID ELSE t4.dep_id END  second_dept_id,   ");
        sql.append("CASE t3.parent_dep_id WHEN \'1\' THEN T3.DEP_NAME ELSE T4.DEP_NAME END second_dept_name   ");
        sql.append("FROM (select a.staff_id,a.staff_name,b.dep_id,b.dep_name,b.parent_dep_id ");
        sql.append("from lkg_staff a left join (select distinct dep_id,dep_name,parent_dep_id from lkg_param_post) b on a.dep_id=b.dep_id) t3 left join  ");
        sql.append("lkg_param_post t4 on ");
        sql.append("t3.parent_dep_id = t4.dep_id) t2  ");
        sql.append("WHERE upper(t1.operatorid) = upper(t2.staff_id)  ");
        this.log.debug("SQL--------->" + sql.toString());
        List result = this.getSimpleJdbcTemplate(jndi).queryForList(sql.toString(), new Object[0]);
        return result;
    }

    public List selectLgkLogin(String logTableName) {
        StringBuffer sql = new StringBuffer();
        String jndi = Configure.getInstance().getProperty("JNDI_WEBOS_CI");
        sql.append("SELECT t1.uniqueid,t1.hittime,t1.sessionid,t1.userid,t2.staff_name,t1.bassid,t1.operatertype, ");
        sql.append("t1.operatername,t1.appid,t1.appname,t1.resourceid,t1.resourcename,t1.extresourceid,t1.resourcetype, ");
        sql.append("t1.resourcepath,t1.msg,t1.operationresult,t1.loglevel,t1.tabheader,t1.clientaddress,t1.serveraddress, ");
        sql.append("t1.threadname,t1.traffic,t1.logserverip,t1.logserverport, ");
        sql.append("t2.third_dept_id,t2.third_dept_name,t2.second_dept_id,t2.second_dept_name ");
        sql.append("FROM ( ");
        sql.append("SELECT * FROM ").append(logTableName).append(" WHERE appid = \'COC\' AND resourceid=\'LOGIN\' ");
        sql.append(") t1, (SELECT DISTINCT t3.staff_id, t3.staff_name, CASE t3.parent_dep_id WHEN \'1\' THEN \'\'  ELSE t3.dep_id END  third_dept_id, ");
        sql.append("CASE t3.parent_dep_id WHEN \'1\' THEN \'\'  ELSE T3.DEP_NAME END third_dept_name,  ");
        sql.append("CASE t3.parent_dep_id WHEN \'1\' THEN T3.DEP_ID ELSE t4.dep_id END  second_dept_id,  ");
        sql.append("CASE t3.parent_dep_id WHEN \'1\' THEN T3.DEP_NAME ELSE T4.DEP_NAME END second_dept_name  ");
        sql.append("FROM (select a.staff_id,a.staff_name,b.dep_id,b.dep_name,b.parent_dep_id ");
        sql.append("from lkg_staff a left join (select distinct dep_id,dep_name,parent_dep_id from lkg_param_post) b on a.dep_id=b.dep_id) t3 left join ");
        sql.append("lkg_param_post t4 on ");
        sql.append("t3.parent_dep_id = t4.dep_id) t2 ");
        sql.append("WHERE upper(t1.userid) = upper(t2.staff_id) ");
        this.log.debug("selectLgkLogin SQL--------->" + sql.toString());
        List result = this.getSimpleJdbcTemplate(jndi).queryForList(sql.toString(), new Object[0]);
        return result;
    }

    public void exportCustomerCreateDetail2LogOverview(String date) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ci_log_overview ");
        sql.append("select a.log_date, ");
        sql.append("case a.create_type_id when ");
        sql.append(1);
        sql.append(" then \'").append("COC_CUSTOMER_MANAGE_ADD_LABEL").append("\'  when ");
        sql.append(7);
        sql.append(" then \'").append("COC_CUSTOMER_MANAGE_ADD_IMPORT").append("\' when ");
        sql.append(8);
        sql.append(" then \'").append("COC_CUSTOMER_MANAGE_ADD_PRODUCT");
        sql.append("\' else \'\' end create_type, ");
        sql.append("a.third_dept_id,a.third_dept_name,a.second_dept_id,a.second_dept_name, ");
        sql.append("count(a.log_date)  from ( SELECT ");
        sql.append("\'").append(date).append("\' ");
        sql.append("log_date, t.second_dept_id second_dept_id, ");
        sql.append("t.second_dept_name second_dept_name, ");
        sql.append("t.third_dept_id third_dept_id, ");
        sql.append("t.third_dept_name third_dept_name,  ");
        sql.append("create_type_id  FROM  CI_OPER_LOG_TEMP t,  ");
        sql.append("ci_custom_group_info t2 WHERE  ");
        sql.append("t.operatertype = \'").append("COC_CUSTOMER_MANAGE_ADD").append("\' ");
        sql.append("and t.extresourceid = t2.custom_group_id  ");
        sql.append("and substr(operatedate,1,8) = ");
        sql.append("\'").append(date).append("\' ");
        sql.append("ORDER BY t.second_dept_name, t.third_dept_name) a ");
        sql.append("where a.create_type_id in (");
        sql.append(1).append(",");
        sql.append(7).append(",");
        sql.append(8).append(") ");
        sql.append("group by a.log_date,a.second_dept_id,a.second_dept_name, ");
        sql.append("a.third_dept_id,a.third_dept_name,a.create_type_id ORDER BY  ");
        sql.append("a.second_dept_name, a.third_dept_name");
        this.log.debug("exportCustomerCreateDetail2LogOverview sql ->" + sql.toString());
        this.getSimpleJdbcTemplate().update(sql.toString(), new Object[0]);
    }

    public void exportCustomerCreateTotal2LogOverview(String date) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ci_log_overview ");
        sql.append("select data_date,\'").append("COC_CUSTOMER_MANAGE_ADD").append("\' ");
        sql.append("op_type_id,third_dept_id,third_dept_name,second_dept_id,second_dept_name,");
        sql.append("sum(op_times) from ci_log_overview where data_date=\'").append(date);
        sql.append("\' and op_type_id in(\'");
        sql.append("COC_CUSTOMER_MANAGE_ADD_LABEL");
        sql.append("\',\'").append("COC_CUSTOMER_MANAGE_ADD_PRODUCT");
        sql.append("\',\'").append("COC_CUSTOMER_MANAGE_ADD_IMPORT").append("\')");
        sql.append("group by data_date,third_dept_id,third_dept_name,second_dept_id,second_dept_name ");
        this.log.debug("exportCustomerCreateTotal2LogOverview sql ->" + sql.toString());
        this.getSimpleJdbcTemplate().update(sql.toString(), new Object[0]);
    }

    public void exportCustomerCreateDetail2LogUser(String date) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into CI_LOG_USER_DETAIL ");
        sql.append("select a.log_date,a.operatorid,a.operatorname, ");
        sql.append("case a.create_type_id when ").append(1);
        sql.append(" then \'").append("COC_CUSTOMER_MANAGE_ADD_LABEL").append("\'  ");
        sql.append("when ").append(7);
        sql.append(" then \'").append("COC_CUSTOMER_MANAGE_ADD_IMPORT").append("\' ");
        sql.append("when ").append(8);
        sql.append(" then \'").append("COC_CUSTOMER_MANAGE_ADD_PRODUCT").append("\' else \'\' end create_type, ");
        sql.append("a.third_dept_id,a.third_dept_name,a.second_dept_id,a.second_dept_name, ");
        sql.append("count(a.log_date) from ( SELECT   \'").append(date).append("\' log_date,  ");
        sql.append("t.operatorid,t.operatorname, t.second_dept_id second_dept_id, ");
        sql.append("t.second_dept_name second_dept_name, t.third_dept_id third_dept_id, ");
        sql.append("t.third_dept_name third_dept_name, create_type_id  ");
        sql.append("FROM CI_OPER_LOG_TEMP t, ci_custom_group_info t2  WHERE  ");
        sql.append("t.operatertype = \'").append("COC_CUSTOMER_MANAGE_ADD").append("\' ");
        sql.append("and    t.extresourceid = t2.custom_group_id  ");
        sql.append("and substr(operatedate,1,8) = \'").append(date).append("\' ");
        sql.append("ORDER BY t.second_dept_name,t.third_dept_name) a ");
        sql.append("where a.create_type_id in (");
        sql.append(1).append(",");
        sql.append(7).append(",");
        sql.append(8).append(") ");
        sql.append("group by a.log_date,a.second_dept_id,a.second_dept_name,a.third_dept_id, ");
        sql.append("a.third_dept_name,a.create_type_id,a.operatorid,a.operatorname ");
        sql.append("ORDER BY a.second_dept_name,a.third_dept_name ");
        this.log.debug("exportCustomerCreateDetail2LogUser sql ->" + sql.toString());
        this.getSimpleJdbcTemplate().update(sql.toString(), new Object[0]);
    }

    public void exportLabelAnalysisDetail2LogOverview(String date) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ci_log_overview ");
        sql.append("select a.log_date,a.operatertype,a.third_dept_id,a.third_dept_name, ");
        sql.append("a.second_dept_id,a.second_dept_name,count(a.log_date) ");
        sql.append("from ( select substr(operatedate,1,8) as log_date,operatertype, ");
        sql.append("third_dept_id,third_dept_name,second_dept_id,second_dept_name  ");
        sql.append("from CI_OPER_LOG_TEMP where operatertype in (\'");
        sql.append("COC_LABEL_CONTRAST_ANALYSIS").append("\', \'");
        sql.append("COC_LABEL_ANALYSIS_LINK").append("\',\'");
        sql.append("COC_LABEL_REL_ANALYSIS").append("\',\'");
        sql.append("COC_LABEL_DIFFERENTIAL").append("\')  ");
        sql.append("AND substr(operatedate,1,8) = \'").append(date).append("\' order by operatedate desc) a ");
        sql.append("group by  a.log_date,a.operatertype,a.third_dept_id,a.third_dept_name, ");
        sql.append("a.second_dept_id,a.second_dept_name ");
        this.log.debug("exportLabelAnalysisDetail2LogOverview sql ->" + sql.toString());
        this.getSimpleJdbcTemplate().update(sql.toString(), new Object[0]);
    }

    public void exportLabelAnalysisTotal2LogOverview(String date) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ci_log_overview ");
        sql.append("select data_date,\'").append("COC_LABEL_ANALYSIS");
        sql.append("\' op_type_id,third_dept_id,");
        sql.append("third_dept_name,second_dept_id,second_dept_name, ");
        sql.append("sum(op_times) from ci_log_overview ");
        sql.append("WHERE data_date=\'").append(date);
        sql.append("\' and op_type_id in(\'");
        sql.append("COC_LABEL_CONTRAST_ANALYSIS").append("\',");
        sql.append("\'");
        sql.append("COC_LABEL_REL_ANALYSIS").append("\',\'");
        sql.append("COC_LABEL_ANALYSIS_LINK");
        sql.append("\',\'").append("COC_LABEL_DIFFERENTIAL").append("\') ");
        sql.append("group by data_date,third_dept_id,third_dept_name,second_dept_id,second_dept_name ");
        this.log.debug("exportLabelAnalysisTotal2LogOverview sql ->" + sql.toString());
        this.getSimpleJdbcTemplate().update(sql.toString(), new Object[0]);
    }

    public void exportLabelAnalysisDetail2LogUser(String date) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into CI_LOG_USER_DETAIL ");
        sql.append("select a.log_date,a.operatorid,a.operatorname,a.operatertype,a.third_dept_id,a.third_dept_name,  ");
        sql.append("a.second_dept_id,a.second_dept_name,count(a.log_date)  ");
        sql.append("from ( select substr(operatedate,1,8) as log_date,operatorid,operatorname,operatertype,  ");
        sql.append("third_dept_id,third_dept_name,second_dept_id,second_dept_name from CI_OPER_LOG_TEMP  ");
        sql.append("where operatertype in (\'");
        sql.append("COC_LABEL_CONTRAST_ANALYSIS").append("\',  \'");
        sql.append("COC_LABEL_ANALYSIS_LINK").append("\',\'");
        sql.append("COC_LABEL_REL_ANALYSIS").append("\',\'");
        sql.append("COC_LABEL_DIFFERENTIAL").append("\')   ");
        sql.append("AND substr(operatedate,1,8) = \'").append(date);
        sql.append("\' order by operatedate desc) a  ");
        sql.append("group by  a.log_date,a.operatertype,a.operatorid,a.operatorname, ");
        sql.append("a.third_dept_id,a.third_dept_name, a.second_dept_id,a.second_dept_name  ");
        this.log.debug("exportLabelAnalysisDetail2LogUser sql ->" + sql.toString());
        this.getSimpleJdbcTemplate().update(sql.toString(), new Object[0]);
    }

    public void exportCustomerAnalysisDetail2LogOverview(String date) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ci_log_overview ");
        sql.append("select a.log_date,a.operatertype,a.third_dept_id,a.third_dept_name, ");
        sql.append("a.second_dept_id,a.second_dept_name,count(a.log_date) ");
        sql.append("from ( select substr(operatedate,1,8) as log_date,operatertype, ");
        sql.append("third_dept_id,third_dept_name,second_dept_id,second_dept_name  ");
        sql.append("from CI_OPER_LOG_TEMP where operatertype in (\'");
        sql.append("COC_CUSTOMER_CONTRAST_ANALYSIS").append("\', \'");
        sql.append("COC_CUSTOMER_REL_ANALYSIS").append("\',\'");
        sql.append("COC_INDEX_DIFFERENTIAL").append("\',\'");
        sql.append("COC_CUSTOMER_LABEL_DIFFERENTIAL").append("\')  ");
        sql.append("AND substr(operatedate,1,8) = \'").append(date).append("\' order by operatedate desc) a ");
        sql.append("group by  a.log_date,a.operatertype,a.third_dept_id,a.third_dept_name, ");
        sql.append("a.second_dept_id,a.second_dept_name ");
        this.log.debug("exportCustomerAnalysisDetail2LogOverview sql ->" + sql.toString());
        this.getSimpleJdbcTemplate().update(sql.toString(), new Object[0]);
    }

    public void exportCustomerAnalysisTotal2LogOverview(String date) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ci_log_overview ");
        sql.append("select data_date,\'").append("COC_CUSTOMER_ANALYSIS");
        sql.append("\' op_type_id,third_dept_id,");
        sql.append("third_dept_name,second_dept_id,second_dept_name, ");
        sql.append("sum(op_times) from ci_log_overview ");
        sql.append("WHERE data_date=\'").append(date);
        sql.append("\' and op_type_id in(\'");
        sql.append("COC_CUSTOMER_CONTRAST_ANALYSIS").append("\',");
        sql.append("\'");
        sql.append("COC_CUSTOMER_REL_ANALYSIS").append("\',\'");
        sql.append("COC_INDEX_DIFFERENTIAL");
        sql.append("\',\'").append("COC_CUSTOMER_LABEL_DIFFERENTIAL").append("\') ");
        sql.append("group by data_date,third_dept_id,third_dept_name,second_dept_id,second_dept_name ");
        this.log.debug("exportCustomerAnalysisTotal2LogOverview sql ->" + sql.toString());
        this.getSimpleJdbcTemplate().update(sql.toString(), new Object[0]);
    }

    public void exportCustomerAnalysisDetail2LogUser(String date) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into CI_LOG_USER_DETAIL ");
        sql.append("select a.log_date,a.operatorid,a.operatorname,a.operatertype,a.third_dept_id,a.third_dept_name,  ");
        sql.append("a.second_dept_id,a.second_dept_name,count(a.log_date)  ");
        sql.append("from ( select substr(operatedate,1,8) as log_date,operatorid,operatorname,operatertype,  ");
        sql.append("third_dept_id,third_dept_name,second_dept_id,second_dept_name from CI_OPER_LOG_TEMP  ");
        sql.append("where operatertype in (\'");
        sql.append("COC_CUSTOMER_CONTRAST_ANALYSIS").append("\',  \'");
        sql.append("COC_CUSTOMER_REL_ANALYSIS").append("\',\'");
        sql.append("COC_INDEX_DIFFERENTIAL").append("\',\'");
        sql.append("COC_CUSTOMER_LABEL_DIFFERENTIAL").append("\')   ");
        sql.append("AND substr(operatedate,1,8) = \'").append(date);
        sql.append("\' order by operatedate desc) a  ");
        sql.append("group by  a.log_date,a.operatertype,a.operatorid,a.operatorname, ");
        sql.append("a.third_dept_id,a.third_dept_name, a.second_dept_id,a.second_dept_name  ");
        this.log.debug("exportCustomerAnalysisDetail2LogUser sql ->" + sql.toString());
        this.getSimpleJdbcTemplate().update(sql.toString(), new Object[0]);
    }

    public void exportDateExplore2LogOverview(String date) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ci_log_overview ");
        sql.append("select a.log_date,");
        sql.append(" a.operatertype,a.third_dept_id, ");
        sql.append("a.third_dept_name,a.second_dept_id,a.second_dept_name,count(a.log_date) ");
        sql.append("from ( select substr(operatedate,1,8) as log_date,operatertype,third_dept_id, ");
        sql.append("third_dept_name,second_dept_id,second_dept_name from CI_OPER_LOG_TEMP  ");
        sql.append("where operatertype =\'").append("COC_DATA_EXPLORE").append("\'");
        sql.append("AND substr(operatedate,1,8) = \'").append(date).append("\' order by operatedate desc) a ");
        sql.append("group by  a.log_date,a.operatertype,a.third_dept_id,a.third_dept_name,a.second_dept_id,a.second_dept_name ");
        this.log.debug("exportDateExplore2LogOverview sql ->" + sql.toString());
        this.getSimpleJdbcTemplate().update(sql.toString(), new Object[0]);
    }

    public void exportDateExplore2LogUser(String date) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ci_log_user_detail ");
        sql.append("select a.log_date,a.operatorid,a.operatorname, a.operatertype,a.third_dept_id,a.third_dept_name, ");
        sql.append("a.second_dept_id,a.second_dept_name,count(a.log_date) ");
        sql.append("from (select substr(operatedate,1,8) as log_date,operatorid,operatorname, ");
        sql.append("operatertype,third_dept_id,third_dept_name,second_dept_id,second_dept_name  ");
        sql.append("from CI_OPER_LOG_TEMP where operatertype =\'").append("COC_DATA_EXPLORE").append("\'");
        sql.append(" AND substr(operatedate,1,8) = \'").append(date).append("\' order by operatedate desc) a ");
        sql.append(" group by  a.log_date,a.operatorid,a.operatorname, ");
        sql.append(" a.operatertype,a.third_dept_id,a.third_dept_name,a.second_dept_id,a.second_dept_name ");
        this.log.debug("exportDateExplore2LogOverview sql ->" + sql.toString());
        this.getSimpleJdbcTemplate().update(sql.toString(), new Object[0]);
    }

    public void exportMarketing2LogOverview(String date) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ci_log_overview ");
        sql.append("select a.log_date,\'");
        sql.append("COC_CUSTOMER_MATCH_ADD");
        sql.append("\' operatertype,a.third_dept_id, ");
        sql.append("a.third_dept_name,a.second_dept_id,a.second_dept_name,count(a.log_date) ");
        sql.append("from ( select substr(operatedate,1,8) as log_date,operatertype,third_dept_id, ");
        sql.append("third_dept_name,second_dept_id,second_dept_name from CI_OPER_LOG_TEMP  ");
        sql.append("where operatertype in (\'");
        sql.append("COC_CUSTOMER_MATCH_ADD");
        sql.append("\',\'");
        sql.append("COC_CUSTOMER_MARKETING_ADD");
        sql.append("\') ");
        sql.append("AND substr(operatedate,1,8) = \'").append(date).append("\' order by operatedate desc) a ");
        sql.append("group by  a.log_date,a.third_dept_id,a.third_dept_name,a.second_dept_id,a.second_dept_name ");
        this.log.debug("exportMarketing2LogOverview sql ->" + sql.toString());
        this.getSimpleJdbcTemplate().update(sql.toString(), new Object[0]);
    }

    public void exportMarketing2LogUser(String date) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ci_log_user_detail ");
        sql.append("select a.log_date,a.operatorid,a.operatorname,\'");
        sql.append("COC_CUSTOMER_MATCH_ADD").append("\' operatertype,a.third_dept_id,a.third_dept_name, ");
        sql.append("a.second_dept_id,a.second_dept_name,count(a.log_date) ");
        sql.append("from (select substr(operatedate,1,8) as log_date,operatorid,operatorname, ");
        sql.append("operatertype,third_dept_id,third_dept_name,second_dept_id,second_dept_name  ");
        sql.append("from CI_OPER_LOG_TEMP where operatertype in (\'").append("COC_CUSTOMER_MATCH_ADD").append("\',\'");
        sql.append("COC_CUSTOMER_MARKETING_ADD").append("\') ");
        sql.append("AND substr(operatedate,1,8) = \'").append(date).append("\' order by operatedate desc) a ");
        sql.append("group by  a.log_date,a.operatorid,a.operatorname, ");
        sql.append("a.third_dept_id,a.third_dept_name,a.second_dept_id,a.second_dept_name ");
        this.log.debug("exportMarketing2LogUser sql ->" + sql.toString());
        this.getSimpleJdbcTemplate().update(sql.toString(), new Object[0]);
    }

    public void exportMarketingCampaign2LogOverview(String date) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ci_log_overview ");
        sql.append("select a.log_date,operatertype,a.third_dept_id, ");
        sql.append("a.third_dept_name,a.second_dept_id,a.second_dept_name,count(a.log_date) ");
        sql.append("from ( select substr(operatedate,1,8) as log_date,operatertype,third_dept_id, ");
        sql.append("third_dept_name,second_dept_id,second_dept_name from CI_OPER_LOG_TEMP  ");
        sql.append("where operatertype = \'");
        sql.append("COC_MARKETING_CAMPAIGN_LINK").append("\' ");
        sql.append("AND substr(operatedate,1,8) = \'").append(date).append("\' order by operatedate desc) a ");
        sql.append("group by  a.log_date,operatertype,a.third_dept_id,a.third_dept_name,a.second_dept_id,a.second_dept_name ");
        this.log.debug("exportMarketingCampaign2LogOverview sql ->" + sql.toString());
        this.getSimpleJdbcTemplate().update(sql.toString(), new Object[0]);
    }

    public void exportMarketingCampaign2LogUser(String date) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ci_log_user_detail ");
        sql.append("select a.log_date,a.operatorid,a.operatorname,operatertype,a.third_dept_id,a.third_dept_name, ");
        sql.append("a.second_dept_id,a.second_dept_name,count(a.log_date) ");
        sql.append("from (select substr(operatedate,1,8) as log_date,operatorid,operatorname, ");
        sql.append("operatertype,third_dept_id,third_dept_name,second_dept_id,second_dept_name  ");
        sql.append("from CI_OPER_LOG_TEMP where operatertype=\'").append("COC_MARKETING_CAMPAIGN_LINK").append("\' ");
        sql.append("AND substr(operatedate,1,8) = \'").append(date).append("\' order by operatedate desc) a ");
        sql.append("group by  a.log_date,a.operatorid,a.operatorname, operatertype,");
        sql.append("a.third_dept_id,a.third_dept_name,a.second_dept_id,a.second_dept_name ");
        this.log.debug("exportMarketingCampaign2LogUser sql ->" + sql.toString());
        this.getSimpleJdbcTemplate().update(sql.toString(), new Object[0]);
    }

    public void exportLoginData2LogOverview(String date) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ci_log_overview ");
        sql.append("select a.log_date,\'").append("COC_LOGIN");
        sql.append("\' operatertype,a.third_dept_id,  ");
        sql.append("a.third_dept_name,a.second_dept_id,a.second_dept_name, ");
        sql.append("count(a.log_date) from ( select substr(hittime,1,8) as log_date,operatertype,third_dept_id,  ");
        sql.append("third_dept_name,second_dept_id,second_dept_name from  ");
        sql.append("CI_OPER_LOGIN_TEMP where  substr(hittime,1,8) = \'");
        sql.append(date).append("\' order by hittime desc) a  ");
        sql.append("group by  a.log_date,a.third_dept_id,a.third_dept_name,a.second_dept_id,a.second_dept_name  ");
        this.log.debug("exportLoginData2LogOverview sql ->" + sql.toString());
        this.getSimpleJdbcTemplate().update(sql.toString(), new Object[0]);
    }

    public void exportLoginData2LogUser(String date) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ci_log_user_detail ");
        sql.append("select a.log_date,a.userid,a.username,\'").append("COC_LOGIN");
        sql.append("\' operatertype, ");
        sql.append("a.third_dept_id, a.third_dept_name,a.second_dept_id,a.second_dept_name, ");
        sql.append("count(a.log_date) from ( select substr(hittime,1,8) as log_date, ");
        sql.append("userid,username,operatertype,third_dept_id,  ");
        sql.append("third_dept_name,second_dept_id,second_dept_name from CI_OPER_LOGIN_TEMP  ");
        sql.append("where  substr(hittime,1,8) = \'").append(date).append("\' order by hittime desc) a  ");
        sql.append("group by  a.log_date,a.userid,a.username,a.third_dept_id,a.third_dept_name, ");
        sql.append("a.second_dept_id,a.second_dept_name  ");
        this.log.debug("exportLoginData2LogUser sql ->" + sql.toString());
        this.getSimpleJdbcTemplate().update(sql.toString(), new Object[0]);
    }

    public boolean tableExists(String tableName, String dbType, String jndi) {
        boolean flag = true;
        StringBuffer sql = new StringBuffer();

        try {
            SimpleJdbcTemplate e = null;
            sql.append("SELECT COUNT(1) FROM ").append(tableName);
            if(StringUtil.isNotEmpty(jndi)) {
                e = this.getSimpleJdbcTemplate(jndi);
            } else {
                e = this.getSimpleJdbcTemplate();
            }

            e.getJdbcOperations().execute(sql.toString());
        } catch (Exception var7) {
            this.log.error("table " + tableName + " is not exists!");
            flag = false;
        }

        return flag;
    }

    public String selectMaxDate(String columnName, String tableName) {
        String result = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT MAX(");
        sql.append(columnName);
        sql.append(") MAX_DATE FROM ");
        sql.append(tableName);
        List list = this.getSimpleJdbcTemplate().queryForList(sql.toString(), new Object[0]);
        if(list != null && list.size() > 0) {
            Map item = (Map)list.get(0);
            result = (String)item.get("max_date");
        }

        return result;
    }

    public void deleteDirtyData(String dataDate) {
        StringBuffer deleteOperLogTempSql = new StringBuffer();
        StringBuffer deleteOperLoginTempSql = new StringBuffer();
        StringBuffer deleteOverviewSql = new StringBuffer();
        StringBuffer deleteUserDetailSql = new StringBuffer();
        deleteOperLogTempSql.append("delete from ci_oper_log_temp where substr(operatedate,1,8)=\'").append(dataDate).append("\'");
        deleteOperLoginTempSql.append("delete from ci_oper_login_temp where substr(hittime,1,8)=\'").append(dataDate).append("\'");
        deleteOverviewSql.append("delete from ci_log_overview where data_date=\'").append(dataDate).append("\'");
        deleteUserDetailSql.append("delete from ci_log_user_detail where data_date=\'").append(dataDate).append("\'");
        this.getSimpleJdbcTemplate().getJdbcOperations().execute(deleteOperLogTempSql.toString());
        this.getSimpleJdbcTemplate().getJdbcOperations().execute(deleteOperLoginTempSql.toString());
        this.getSimpleJdbcTemplate().getJdbcOperations().execute(deleteOverviewSql.toString());
        this.getSimpleJdbcTemplate().getJdbcOperations().execute(deleteUserDetailSql.toString());
    }
}
