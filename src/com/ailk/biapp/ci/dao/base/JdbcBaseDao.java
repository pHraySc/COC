package com.ailk.biapp.ci.dao.base;

import com.ailk.biapp.ci.task.CountSQLThread;
import com.ailk.biapp.ci.util.DataBaseAdapter;
import com.ailk.biapp.ci.util.DataSourceFactory;
import com.ailk.biapp.ci.util.SimpleCache;
import com.asiainfo.biframe.utils.config.Configure;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcBaseDao {
    private Logger log = Logger.getLogger(JdbcBaseDao.class);
    private static DataBaseAdapter dataBaseAdapter = null;
    private SimpleJdbcTemplate simpleJdbcTemplate;
    private static DataBaseAdapter backDataBaseAdapter = null;
    private SimpleJdbcTemplate backSimpleJdbcTemplate;

    public JdbcBaseDao() {
    }

    @Autowired
    public void setJDBCDataSource(@Qualifier("ciDataSource") DataSource datasource) {
        this.simpleJdbcTemplate = new SimpleJdbcTemplate(datasource);
    }

    @Autowired
    public void setBackJDBCDataSource(@Qualifier("ciBackDataSource") DataSource datasource) {
        this.backSimpleJdbcTemplate = new SimpleJdbcTemplate(datasource);
    }

    public DataBaseAdapter getDataBaseAdapter() {
        if(dataBaseAdapter == null) {
            dataBaseAdapter = this.getDataBaseAdapter(Configure.getInstance().getProperty("CI_DBTYPE"));
        }

        return dataBaseAdapter;
    }

    public SimpleJdbcTemplate getSimpleJdbcTemplate() {
        return this.simpleJdbcTemplate;
    }

    public DataBaseAdapter getBackDataBaseAdapter() {
        if(backDataBaseAdapter == null) {
            backDataBaseAdapter = this.getDataBaseAdapter(Configure.getInstance().getProperty("CI_BACK_DBTYPE"));
        }

        return backDataBaseAdapter;
    }

    public SimpleJdbcTemplate getBackSimpleJdbcTemplate() {
        return this.backSimpleJdbcTemplate;
    }

    public DataBaseAdapter getDataBaseAdapter(String dbType) {
        return new DataBaseAdapter(dbType);
    }

    public SimpleJdbcTemplate getSimpleJdbcTemplate(String jndi) {
        return new SimpleJdbcTemplate(DataSourceFactory.getDataSource(jndi));
    }

    public JdbcTemplate getJdbcTemplate(String jndi) {
        return new JdbcTemplate(DataSourceFactory.getDataSource(jndi));
    }

    public int selectBackCountBySql(String sql) {
        boolean count = false;
        Object o = SimpleCache.getInstance().get(sql);
        int count1;
        if(o != null && o instanceof Integer) {
            count1 = ((Integer)o).intValue();
            this.log.debug("get count from cache," + sql);
            CountSQLThread.exeCount(this.getBackSimpleJdbcTemplate(), sql);
        } else {
            this.log.debug("selectBackCountBySql:" + sql);
            count1 = this.getBackSimpleJdbcTemplate().queryForInt(sql.toString(), new Object[0]);
        }

        SimpleCache.getInstance().put(sql, Integer.valueOf(count1), 1800L);
        return count1;
    }
}
