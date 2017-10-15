package com.ailk.biapp.ci.dataservice.service.impl;

import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.dataservice.service.ICiCustomer2MongoService;
import com.ailk.biapp.ci.dataservice.service.impl.CiLabelsAndIndexesServiceImpl;
import com.ailk.biapp.ci.model.CiCustomCampsegRel;
import com.asiainfo.biframe.utils.config.Configure;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CiCustomer2MongoServiceImpl implements ICiCustomer2MongoService {
    private final Logger log = Logger.getLogger(CiLabelsAndIndexesServiceImpl.class);
    private JdbcBaseDao jdbcBaseDao = new JdbcBaseDao();
    private SimpleJdbcTemplate simpleJdbcTemplate = null;

    public CiCustomer2MongoServiceImpl() {
    }

    private void init() {
        if(this.simpleJdbcTemplate == null) {
            String jndi = "java:comp/env/jdbc/CI";

            try {
                jndi = Configure.getInstance().getProperty("CI", "JNDI_CI");
            } catch (Exception var3) {
                this.log.error("get jndi error", var3);
            }

            this.simpleJdbcTemplate = this.jdbcBaseDao.getSimpleJdbcTemplate(jndi);
        }

    }

    public List<CiCustomCampsegRel> queryNeedProcessCiCustomCampsegRel() {
        this.init();
        String sql = "SELECT CUSTOM_GROUP_ID , CAMPSEG_ID, CAMPSEG_NAME, CAMPSEG_CREATE_USER, APPROVE_END_TIME, CREATE_TIME, DEAL_FLAG FROM CI_CUSTOM_CAMPSEG_REL where DEAL_FLAG is null or DEAL_FLAG = 0 ";
        this.log.debug("select sql:" + sql);
        return this.simpleJdbcTemplate.query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiCustomCampsegRel.class), new Object[0]);
    }

    public boolean updateDeelStatus(CiCustomCampsegRel rel, int status) {
        boolean flag = false;
        this.init();
        String sql = "update CI_CUSTOM_CAMPSEG_REL set DEAL_FLAG = ?,CREATE_TIME = ? where CUSTOM_GROUP_ID = ? and CAMPSEG_ID = ?";

        try {
            this.log.debug("update sql:" + sql);
            this.simpleJdbcTemplate.update(sql, new Object[]{Integer.valueOf(status), new Date(), rel.getCustomGroupId(), rel.getCampsegId()});
            flag = true;
        } catch (Exception var6) {
            this.log.error("updateDeelFlag", var6);
        }

        return flag;
    }

    public boolean process(CiCustomCampsegRel rel) {
        boolean flag = false;
        this.init();
        rel.getCustomGroupId();

        try {
            String e = Configure.getInstance().getProperty("CI", "RELATED_COLUMN");

            for(int i = 0; i < 10; ++i) {
                String tableName = "CI_CUSTOM_CAMPSEG_REL_DETAIL_" + i;
                StringBuffer selectSQL = (new StringBuffer()).append("SELECT a.").append(e).append(", b.CAMPSEG_ID, b.APPROVE_END_TIME FROM ").append(rel.getCustomGroupId()).append(" a LEFT JOIN CI_CUSTOM_CAMPSEG_REL b ON 1=1 WHERE b.CUSTOM_GROUP_ID=\'").append(rel.getCustomGroupId()).append("\' AND b.CAMPSEG_ID =\'").append(rel.getCampsegId()).append("\' and a.").append(e).append(" like \'%").append(i).append("\'");
                String insertSql = (new StringBuffer("insert into ")).append(tableName).append(" ( ").append(" product_no,CAMPSEG_ID,APPROVE_END_TIME").append(") ").append(selectSQL).toString();
                this.log.debug("insertSql:" + insertSql);
                String alterSql = "ALTER TABLE " + tableName + " ACTIVATE NOT LOGGED INITIALLY";
                this.log.debug("alterSql:" + alterSql);

                try {
                    this.simpleJdbcTemplate.getJdbcOperations().execute(alterSql);
                } catch (Exception var10) {
                    this.log.error(tableName + " may not exists," + var10.getMessage());
                }

                this.simpleJdbcTemplate.getJdbcOperations().execute(insertSql);
            }

            flag = true;
        } catch (Exception var11) {
            this.log.error("process error", var11);
        }

        return flag;
    }

    public boolean toWidthTable() {
        boolean flag = false;
        this.init();

        try {
            String e = "ALTER TABLE CI_PERSON_CAMPAIGNS ACTIVATE NOT LOGGED INITIALLY";
            this.log.debug("alterSql:" + e);

            try {
                this.simpleJdbcTemplate.getJdbcOperations().execute(e);
            } catch (Exception var10) {
                this.log.error("CI_PERSON_CAMPAIGNS may not exists," + var10.getMessage());
            }

            String dropSql = "drop table CI_PERSON_CAMPAIGNS";
            this.log.debug("dropSql:" + dropSql);

            try {
                this.simpleJdbcTemplate.getJdbcOperations().execute(dropSql);
            } catch (Exception var9) {
                this.log.error("CI_PERSON_CAMPAIGNS may not exists," + var9.getMessage());
            }

            String createSql = this.jdbcBaseDao.getDataBaseAdapter("DB2").getSqlCreateAsTable("CI_PERSON_CAMPAIGNS", "CI_PERSON_CAMPAIGNS_XX", "PRODUCT_NO");
            this.log.debug("createSql:" + createSql);
            this.simpleJdbcTemplate.getJdbcOperations().execute(createSql);

            for(int i = 0; i < 10; ++i) {
                String tableName = "CI_CUSTOM_CAMPSEG_REL_DETAIL_" + i;
                this.log.debug("alterSql:" + e);

                try {
                    this.simpleJdbcTemplate.getJdbcOperations().execute(e);
                } catch (Exception var8) {
                    this.log.error("ALTER ERROR " + e);
                }

                String sql = " insert into CI_PERSON_CAMPAIGNS  select  t1.product_no,  max(case when t1.rn =1 then CAMPSEG_ID end) as camp1,  max(case when t1.rn =2 then CAMPSEG_ID end) as camp2,  max(case when t1.rn =3 then CAMPSEG_ID end) as camp3,  max(case when t1.rn =4 then CAMPSEG_ID end) as camp4,  max(case when t1.rn =5 then CAMPSEG_ID end) as camp5,  max(case when t1.rn =6 then CAMPSEG_ID end) as camp6,  max(case when t1.rn =7 then CAMPSEG_ID end) as camp7,  max(case when t1.rn =8 then CAMPSEG_ID end) as camp8,  max(case when t1.rn =9 then CAMPSEG_ID end) as camp9,  max(case when t1.rn =10 then CAMPSEG_ID end) as camp10  from (  select product_no, CAMPSEG_ID ,rownumber() over( partition by product_no order by APPROVE_END_TIME desc  ) as rn  from " + tableName + "  ) as t1 where t1.rn <11 group by t1.product_no";
                this.log.debug("insertSql:" + sql);
                this.simpleJdbcTemplate.getJdbcOperations().execute(sql);
            }

            flag = true;
        } catch (Exception var11) {
            this.log.error("process", var11);
        }

        return flag;
    }
}
