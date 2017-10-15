package com.ailk.biapp.ci.localization.hubei.nonstandardimpl;

import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiSysInfo;
import com.ailk.biapp.ci.localization.nonstandard.ICustomerPush;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("hBCustomerPushImpl")
@Transactional
@Scope("prototype")
public class HBCustomerPushImpl implements ICustomerPush {
    private static Logger log = Logger.getLogger(HBCustomerPushImpl.class);

    public HBCustomerPushImpl() {
    }

    public boolean push(CiCustomGroupInfo ciCustomGroupInfo, CiCustomListInfo ciCustomListInfo, CiSysInfo sysInfo) {
        boolean flag = true;
        String randomCode = CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L);
        String customerPushSchema = Configure.getInstance().getProperty("CUSTOMER_PUSH_SCHEMA").trim();

        JdbcBaseDao jdbcBaseDao;
        try {
            jdbcBaseDao = (JdbcBaseDao)SystemServiceLocator.getInstance().getService("jdbcBaseDao");
            StringBuffer e = (new StringBuffer()).append("CREATE TABLE ").append(customerPushSchema).append(".MTL_CUSER_").append(randomCode).append(" LIKE ").append(customerPushSchema).append(".MTL_CUSER_TEMPLATE");
            jdbcBaseDao.getBackSimpleJdbcTemplate().getJdbcOperations().execute(e.toString());
            jdbcBaseDao.getBackSimpleJdbcTemplate().getJdbcOperations().execute("COMMIT");
        } catch (Exception var20) {
            flag = false;
            log.error("个性化推送失败：", var20);
            var20.printStackTrace();
        }

        if(flag) {
            try {
                String var22 = PrivilegeServiceUtil.getUserById(ciCustomGroupInfo.getCreateUserId()).getCityid();
                String listTabelName = "mtl_cuser_" + randomCode;
                String createTime = DateUtil.date2String(ciCustomGroupInfo.getCreateTime(), "yyyy-MM-dd");
                StringBuffer insertsbff = (new StringBuffer()).append("INSERT INTO ").append(customerPushSchema).append(".MTL_CUST_GROUP(CUST_GROUP_ID,CUST_GROUP_NAME,CREATE_USER_ID,CITY_ID,CREATE_DATE,CUST_GROUP_TYPE,").append("CUST_GROUP_NUM,CUST_GROUP_STATUS,CUST_GROUP_ACCESS_TOKEN,CUST_GROUP_DESC,CUST_GROUP_TAB_NAME,CUST_TYPE_ID,CUST_FREQUENCY) ").append(" VALUES(?,?,?,?,?,3,?,0,0,?,?,\'1\',?)");
                jdbcBaseDao = (JdbcBaseDao)SystemServiceLocator.getInstance().getService("jdbcBaseDao");
                jdbcBaseDao.getSimpleJdbcTemplate().update(insertsbff.toString(), new Object[]{randomCode, ciCustomGroupInfo.getCustomGroupName(), ciCustomGroupInfo.getCreateUserId(), var22, createTime, ciCustomGroupInfo.getCustomNum(), ciCustomGroupInfo.getCustomGroupDesc(), listTabelName, ciCustomGroupInfo.getUpdateCycle()});
                String ciCuser = ciCustomListInfo.getListTableName();
                final String keyColumn = Configure.getInstance().getProperty("RELATED_COLUMN");
                int start = 1;
                short pageSize = 10000;
                StringBuffer insertCusersbff = (new StringBuffer()).append("INSERT INTO ").append(customerPushSchema).append(".MTL_CUSER_").append(randomCode).append("(PRODUCT_NO)").append(" VALUES(?)");
                StringBuffer exportSQL = (new StringBuffer()).append("select distinct ").append(keyColumn).append(" from ").append(ciCuser);

                while(true) {
                    String updateSql = jdbcBaseDao.getBackDataBaseAdapter().getPagedSql(exportSQL.toString(), start, pageSize);
                    log.debug("pagedSQL: " + updateSql);
                    final List datas = jdbcBaseDao.getBackSimpleJdbcTemplate().queryForList(updateSql, new Object[0]);
                    if(datas == null) {
                        break;
                    }

                    jdbcBaseDao.getBackSimpleJdbcTemplate().getJdbcOperations().batchUpdate(insertCusersbff.toString(), new BatchPreparedStatementSetter() {
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            String tempArr = (String)((Map)datas.get(i)).get(keyColumn.toLowerCase());
                            ps.setString(1, tempArr);
                        }

                        public int getBatchSize() {
                            return datas.size();
                        }
                    });
                    if(datas.size() < pageSize) {
                        break;
                    }

                    ++start;
                }

                StringBuffer var23 = (new StringBuffer()).append("UPDATE ").append(customerPushSchema).append(".MTL_CUST_GROUP SET CUST_GROUP_STATUS = 1 where CUST_GROUP_ID = ?");
                jdbcBaseDao.getSimpleJdbcTemplate().update(var23.toString(), new Object[]{randomCode});
            } catch (Exception var21) {
                flag = false;
                log.error("个性化推送失败：", var21);
                var21.printStackTrace();
            }
        }

        return flag;
    }
}
