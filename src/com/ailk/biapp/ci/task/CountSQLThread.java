package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.util.SimpleCache;
import com.ailk.biapp.ci.util.ThreadPool;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class CountSQLThread extends Thread {
    private Logger log = Logger.getLogger(CountSQLThread.class);
    private String sql;
    private SimpleJdbcTemplate simpleJdbcTemplate;

    public CountSQLThread(SimpleJdbcTemplate simpleJdbcTemplate, String sql) {
        this.sql = sql;
        this.simpleJdbcTemplate = simpleJdbcTemplate;
    }

    public void run() {
        long start = System.currentTimeMillis();
        this.log.info("sql:" + this.sql);
        int count = this.simpleJdbcTemplate.queryForInt(this.sql, new Object[0]);
        SimpleCache.getInstance().put(this.sql, Integer.valueOf(count), 1800L);
        this.log.debug("count cost:" + (System.currentTimeMillis() - start));
    }

    public static void exeCount(SimpleJdbcTemplate simpleJdbcTemplate, String sql) {
        ThreadPool.getInstance().execute(new CountSQLThread(simpleJdbcTemplate, sql));
    }

    public String toString() {
        return "CountTaskThread [sql=" + this.sql + "]";
    }
}
