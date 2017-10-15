package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiCustomCampsegRelHDao;
import com.ailk.biapp.ci.dao.ICiCustomGroupFormJDao;
import com.ailk.biapp.ci.dao.ICiCustomListExeInfoHDao;
import com.ailk.biapp.ci.dao.ICiCustomListInfoHDao;
import com.ailk.biapp.ci.dao.ICiCustomListInfoJDao;
import com.ailk.biapp.ci.dao.ICiListFailureInfoHDao;
import com.ailk.biapp.ci.dao.ICustomProductMatchHDao;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiListFailureInfo;
import com.ailk.biapp.ci.service.ICustomersListInfoService;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomersListInfoServiceImpl implements ICustomersListInfoService {
    private static Logger log = Logger.getLogger(CustomersListInfoServiceImpl.class);
    @Autowired
    private ICiCustomListInfoHDao customListInfoHDao;
    @Autowired
    private ICiCustomListInfoJDao customListInfoJDao;
    @Autowired
    private ICiCustomListExeInfoHDao ciCustomListExeInfoHDao;
    @Autowired
    private ICustomProductMatchHDao customProductMatchHDao;
    @Autowired
    private ICiCustomCampsegRelHDao ciCustomCampsegRelHDao;
    @Autowired
    ICiCustomGroupFormJDao ciCustomGroupFormJDao;
    @Autowired
    ICiListFailureInfoHDao ciListFailureInfoHDao;

    public CustomersListInfoServiceImpl() {
    }

    public void removeCustomerListBeforeMonth(int month) {
        if(Math.abs(month) <= 6) {
            log.warn("不能删除6个月以内的清单！");
        } else {
            long start = System.currentTimeMillis();
            Calendar c = Calendar.getInstance();
            String currentMonth = CacheBase.getInstance().getNewLabelMonth();
            c.setTime(DateUtil.string2Date(currentMonth, "yyyyMM"));
            c.add(2, -Math.abs(month));
            String deleteMonth = DateUtil.date2String(c.getTime(), "yyyyMM");
            log.debug("当前数据日期：" + currentMonth);
            log.debug("删除" + month + "个月以前(含" + deleteMonth + ")的清单表...");
            List listInfos = this.customListInfoHDao.select(" from CiCustomListInfo where dataDate <= ?", new Object[]{deleteMonth});
            log.debug("list size:" + (listInfos == null?0:listInfos.size()));
            Iterator i$ = listInfos.iterator();

            while(i$.hasNext()) {
                CiCustomListInfo listInfo = (CiCustomListInfo)i$.next();
                log.debug("delete:" + listInfo.toString());
                String listTableName = listInfo.getListTableName();
                this.deleteListTable(listTableName);
                this.customProductMatchHDao.deleteByListTableName(listTableName);
                this.ciCustomListExeInfoHDao.deleteByListTableName(listTableName);
                this.ciCustomCampsegRelHDao.deleteByListTableName(listTableName);
                this.ciCustomGroupFormJDao.deleteByListTableName(listTableName);
                this.customListInfoHDao.delete(listInfo);
            }

            log.debug("删除" + month + "个月以前(含" + deleteMonth + ")的清单表完成，耗时：" + (System.currentTimeMillis() - start));
        }
    }

    private void deleteListTable(String listTableName) {
        long start = System.currentTimeMillis();
        String ciBackSchema = Configure.getInstance().getProperty("CI_SCHEMA");
        String dbType = Configure.getInstance().getProperty("CI_BACK_DBTYPE");
        String tableName = listTableName;
        if(StringUtil.isNotEmpty(ciBackSchema)) {
            tableName = ciBackSchema + "." + listTableName;
        }

        String sql = null;

        try {
            if("DB2".equalsIgnoreCase(dbType)) {
                sql = "ALTER TABLE " + tableName + " ACTIVATE NOT LOGGED INITIALLY";
            } else if("ORACLE".equalsIgnoreCase(dbType)) {
                sql = "truncate table " + tableName;
            }

            log.debug("exec pre sql:" + sql);
            this.customListInfoJDao.executeInBackDataBase(sql);
            sql = "drop table " + tableName;
            log.debug("exec sql:" + sql);
            this.customListInfoJDao.executeInBackDataBase(sql);
        } catch (Exception var9) {
            log.error("deleteListTable error, sql:" + sql + "," + var9.getMessage());
        }

        log.debug("cost :" + (System.currentTimeMillis() - start));
    }

    public CiCustomListInfo queryCustomerListInfoByListTableName(String listTableName) {
        return this.customListInfoHDao.selectById(listTableName);
    }

    public void updateCustomerListInfo(CiCustomListInfo customListInfo) {
        this.customListInfoHDao.updateCiCustomListInfo(customListInfo);
    }

    public void updateCustomerListInfo(String listTableName, String custGroupDate, Integer isstrengthen, String tabColumn, String labelColumn, Map<String, String[]> tabColumnContentMap) {
        String relatedColumn = Configure.getInstance().getProperty("RELATED_COLUMN");
        Iterator iterator = tabColumnContentMap.keySet().iterator();

        while(iterator.hasNext()) {
            String key = (String)iterator.next();
            String[] val = (String[])tabColumnContentMap.get(key);
            if(val != null && val.length > 1 && StringUtil.isNotEmpty(val[1])) {
                String sql = "UPDATE " + listTableName + " SET " + tabColumn + " = \'" + val[1] + "\'";
                if(StringUtil.isNotEmpty(val[0])) {
                    sql = sql + "," + labelColumn + " = \'" + val[0] + "\'";
                } else {
                    sql = sql + "," + labelColumn + " = \'\'";
                }

                sql = sql + " WHERE " + relatedColumn + " = \'" + key + "\'";
                log.info("更新清单表的sql：" + sql);
                this.customListInfoJDao.executeInBackDataBase(sql);
            }
        }

        log.info("更新清单表结束");
        this.customListInfoJDao.updateCiCustomListInfo(listTableName, custGroupDate, isstrengthen);
    }

    public void addListTableNameColumn(String listTableName, Map<String, String> addColumnMap) throws Exception {
        Iterator columnIte = addColumnMap.keySet().iterator();

        while(columnIte.hasNext()) {
            String column = (String)columnIte.next();
            Boolean isExist = Boolean.valueOf(this.customListInfoJDao.tableExists(listTableName, column));
            if(!isExist.booleanValue()) {
                this.customListInfoJDao.addTableColumn(listTableName, column, (String)addColumnMap.get(column));
            }
        }

    }

    public void updateCustomerListInfo(String listTableName, String custGroupDate, Integer isstrengthen) {
        this.customListInfoJDao.updateCiCustomListInfo(listTableName, custGroupDate, isstrengthen);
    }

    public void deleteCustomerListInfo(CiCustomListInfo customListInfo) {
        this.customListInfoHDao.delete(customListInfo);
    }

    public void saveCiListFailureInfo(CiListFailureInfo ciListFailureInfo) {
        this.ciListFailureInfoHDao.saveCiListFailureInfo(ciListFailureInfo);
    }
}
