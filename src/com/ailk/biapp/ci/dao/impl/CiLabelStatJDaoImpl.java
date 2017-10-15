package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiLabelStatJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.util.DateUtil;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class CiLabelStatJDaoImpl extends JdbcBaseDao implements ICiLabelStatJDao {
    private Logger log = Logger.getLogger(this.getClass());

    public CiLabelStatJDaoImpl() {
    }

    public Long getCustomNum(String dataDate, Integer labelId, List<String> cityList, Integer vipLevelId, Integer brandId) {
        long retCustomNum = 0L;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT SUM(CUSTOM_NUM) AS CUSTOM_NUM FROM ").append(DateUtil.getLabelStatTableName(dataDate)).append(" WHERE LABEL_ID =? AND DATA_DATE = ? AND VIP_LEVEL_ID =? AND BRAND_ID =?");
        if (cityList != null && cityList.size() > 0) {
            long count1;
            String var13;
            if (cityList.size() == 1) {
                var13 = (String) cityList.get(0);
                sql.append(" AND CITY_ID =  ? ");
                count1 = this.getSimpleJdbcTemplate().queryForLong(sql.toString(), new Object[]{labelId, dataDate, vipLevelId, brandId, var13});
                retCustomNum = count1;
            } else {
                var13 = "";

                for (int var12 = 0; var12 < cityList.size(); ++var12) {
                    var13 = var13 + "\'" + (String) cityList.get(var12) + "\'" + (var12 == cityList.size() - 1 ? "" : ",");
                }

                sql.append(" AND CITY_ID IN (").append(var13).append(") ");
                count1 = this.getSimpleJdbcTemplate().queryForLong(sql.toString(), new Object[]{labelId, dataDate, vipLevelId, brandId});
                retCustomNum = count1;
            }
        } else {
            if (cityList != null && cityList.size() != 0) {
                throw new CIServiceException("该用户没有分配地域权限");
            }

            sql.append(" AND CITY_ID = ? ");
            long count = this.getSimpleJdbcTemplate().queryForLong(sql.toString(), new Object[]{labelId, dataDate, vipLevelId, brandId, Integer.valueOf(-1)});
            retCustomNum = count;
        }
        this.log.info("cityList:" + cityList.size() + " " + cityList);
        this.log.debug("getCustomNum sql:" + sql.toString());
        return Long.valueOf(retCustomNum);
    }
}
