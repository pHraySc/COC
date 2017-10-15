package com.ailk.biapp.ci.util;

import com.ailk.biapp.ci.constant.CommonConstants;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.manager.context.ContextManager;
import com.asiainfo.biframe.service.IActivator;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.File;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class CIActivator implements IActivator {
    private static final Logger LOG = Logger.getLogger(CIActivator.class);

    public CIActivator() {
    }

    public void start(ContextManager context) throws Exception {
        LOG.info("CIActivator begin to execute");

        try {
            File e = new File(this.getClass().getResource("/config/aibi_ci/ci.properties").toURI());
            Configure.getInstance().setConfFileName(e.getPath());
            String province = Configure.getInstance().getProperty("PROVINCE");
            if(StringUtil.isNotEmpty(province)) {
                File base = new File(e.getParentFile().getAbsolutePath() + File.separator + e.getName().substring(0, e.getName().indexOf(".")) + "-" + province + ".properties");
                if(base.exists()) {
                    Configure.getInstance().setConfFileName(base.getPath());
                    LOG.info("CIActivator load properties from file [" + base.getAbsolutePath() + "]");
                } else {
                    LOG.warn("PROVINCE=" + province + " , but [" + base.getAbsolutePath() + "] is not exists.");
                }
            }

            LOG.debug("CENTER_CITYID:" + Configure.getInstance().getProperty("CENTER_CITYID"));
            LOG.debug("JNDI_CI:" + Configure.getInstance().getProperty("JNDI_CI"));
            LOG.debug("IS_DB_CHARSET_ISO8859:" + Configure.getInstance().getProperty("IS_DB_CHARSET_ISO8859"));
            LOG.info("CIActivator completed");
            String base1 = Configure.getInstance().getProperty("BASE");
            if(StringUtil.isNotEmpty(base1)) {
                CommonConstants.base = base1;
            }

            String loginResourceType = Configure.getInstance().getProperty("LOGIN_FOR_NJ");
            if(StringUtil.isNotEmpty(loginResourceType)) {
                CommonConstants.loginResourceType = loginResourceType;
            }

            String logoutResourceType = Configure.getInstance().getProperty("LOGOUT_FOR_NJ");
            if(StringUtil.isNotEmpty(logoutResourceType)) {
                CommonConstants.logoutResourceType = logoutResourceType;
            }

            String sqlParallel = Configure.getInstance().getProperty("SQL_PARALLEL");
            if(StringUtil.isNotEmpty(sqlParallel)) {
                CommonConstants.sqlParallel = sqlParallel;
            }

            String sqlAppend = Configure.getInstance().getProperty("SQL_APPEND");
            if(StringUtil.isNotEmpty(sqlAppend)) {
                CommonConstants.sqlAppend = sqlAppend;
            }

            String templetTableName = Configure.getInstance().getProperty("CUST_LIST_TMP_TABLE");
            if(StringUtils.isEmpty(ServiceConstants.MAINTABLE_KEYCOLUMN)) {
                ServiceConstants.MAINTABLE_KEYCOLUMN = Configure.getInstance().getProperty("RELATED_COLUMN");
            }

            if(StringUtil.isNotEmpty(templetTableName)) {
                CommonConstants.MAIN_COLUMN_TYPE = CiUtil.queryTempletTableNameKeyType(templetTableName, ServiceConstants.MAINTABLE_KEYCOLUMN);
            }

            CacheBase.getInstance().init(true);
            LOG.info("MAINTABLE_KEYCOLUMN:" + ServiceConstants.MAINTABLE_KEYCOLUMN);
        } catch (Exception var10) {
            LOG.error("initialized error:", var10);
            var10.printStackTrace();
        }

    }


    public void stop(ContextManager contextmanager) throws Exception {
    }
}
