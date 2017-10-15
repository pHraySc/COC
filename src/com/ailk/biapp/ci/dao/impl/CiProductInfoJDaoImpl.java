package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiProductInfoJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiProductCategory;
import com.ailk.biapp.ci.entity.CiProductInfo;
import com.ailk.biapp.ci.model.ProductDetailInfo;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiProductInfoJDaoImpl extends JdbcBaseDao implements ICiProductInfoJDao {
    private static Logger log = Logger.getLogger(CiProductInfoJDaoImpl.class);

    public CiProductInfoJDaoImpl() {
    }

    public List<ProductDetailInfo> getEffectiveProduct(int currPage, int pageSize, String orderType, String productName, Integer topLabelId, String dataScope) {
        ArrayList labelIdList = new ArrayList();
        if(topLabelId != null) {
            this.getProductIds(topLabelId, labelIdList);
        }

        String labelIdsStr = labelIdList.toString();
        labelIdsStr = labelIdsStr.substring(1, labelIdsStr.length() - 1);
        productName = productName.toUpperCase();
        productName = this.replaceSpecialCharacter(productName);
        String sql = " SELECT * FROM  CI_PRODUCT_INFO f  where f.status = 2 and f.is_product = 1 and upper(f.product_name) like \'%" + productName + "%\' escape \'|\' ";
        if(StringUtil.isNotEmpty(labelIdsStr)) {
            sql = sql + " and f.product_id in (" + labelIdsStr + ")";
        }

        Date date = new Date();
        String startTime = "";
        String endTime = "";
        String sqlPag;
        String results;
        if(dataScope.equals("oneDay")) {
            sqlPag = DateUtil.date2String(date, "yyyy-MM-dd");
            results = DateUtil.getFrontDay(1, sqlPag, "yyyy-MM-dd");
            startTime = this.getDataBaseAdapter().getTimeStamp(results, "00", "00", "00");
            endTime = this.getDataBaseAdapter().getTimeStamp(sqlPag, "23", "59", "59");
            sql = sql + " and f.CREATE_TIME>=" + startTime + " AND f.CREATE_TIME<=" + endTime;
        }

        if(dataScope.equals("oneMonth")) {
            sqlPag = DateUtil.date2String(date, "yyyy-MM-dd");
            results = DateUtil.getFrontDay(30, sqlPag, "yyyy-MM-dd");
            startTime = this.getDataBaseAdapter().getTimeStamp(results, "00", "00", "00");
            endTime = this.getDataBaseAdapter().getTimeStamp(sqlPag, "23", "59", "59");
            sql = sql + " and f.CREATE_TIME>=" + startTime + " AND f.CREATE_TIME<=" + endTime;
        }

        if(dataScope.equals("threeMonth")) {
            sqlPag = DateUtil.date2String(date, "yyyy-MM-dd");
            results = DateUtil.getFrontDay(90, sqlPag, "yyyy-MM-dd");
            startTime = this.getDataBaseAdapter().getTimeStamp(results, "00", "00", "00");
            endTime = this.getDataBaseAdapter().getTimeStamp(sqlPag, "23", "59", "59");
            sql = sql + " and f.CREATE_TIME>=" + startTime + " AND f.CREATE_TIME<=" + endTime;
        }

        sql = sql + " order by f.CREATE_TIME";
        sqlPag = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        log.debug("sqlPag->" + sqlPag);
        List results1 = this.getSimpleJdbcTemplate().query(sqlPag, ParameterizedBeanPropertyRowMapper.newInstance(ProductDetailInfo.class), new Object[0]);
        return results1;
    }

    public long getEffectiveProductNum(String productName, Integer topLabelId, String dataScope) {
        ArrayList labelIdList = new ArrayList();
        if(topLabelId != null) {
            this.getProductIds(topLabelId, labelIdList);
        }

        String labelIdsStr = labelIdList.toString();
        labelIdsStr = labelIdsStr.substring(1, labelIdsStr.length() - 1);
        productName = productName.toUpperCase();
        productName = this.replaceSpecialCharacter(productName);
        String sql = " SELECT count(*) FROM  CI_PRODUCT_INFO f  where f.status = 2 and f.is_product = 1 and upper(f.product_name) like \'%" + productName + "%\' escape \'|\' ";
        if(StringUtil.isNotEmpty(labelIdsStr)) {
            sql = sql + " and f.product_id in (" + labelIdsStr + ")";
        }

        Date date = new Date();
        String startTime = "";
        String endTime = "";
        String count;
        String startDay;
        if(dataScope.equals("oneDay")) {
            count = DateUtil.date2String(date, "yyyy-MM-dd");
            startDay = DateUtil.getFrontDay(1, count, "yyyy-MM-dd");
            startTime = this.getDataBaseAdapter().getTimeStamp(startDay, "00", "00", "00");
            endTime = this.getDataBaseAdapter().getTimeStamp(count, "23", "59", "59");
            sql = sql + " and f.CREATE_TIME>=" + startTime + " AND f.CREATE_TIME<=" + endTime;
        }

        if(dataScope.equals("oneMonth")) {
            count = DateUtil.date2String(date, "yyyy-MM-dd");
            startDay = DateUtil.getFrontDay(30, count, "yyyy-MM-dd");
            startTime = this.getDataBaseAdapter().getTimeStamp(startDay, "00", "00", "00");
            endTime = this.getDataBaseAdapter().getTimeStamp(count, "23", "59", "59");
            sql = sql + " and f.CREATE_TIME>=" + startTime + " AND f.CREATE_TIME<=" + endTime;
        }

        if(dataScope.equals("threeMonth")) {
            count = DateUtil.date2String(date, "yyyy-MM-dd");
            startDay = DateUtil.getFrontDay(90, count, "yyyy-MM-dd");
            startTime = this.getDataBaseAdapter().getTimeStamp(startDay, "00", "00", "00");
            endTime = this.getDataBaseAdapter().getTimeStamp(count, "23", "59", "59");
            sql = sql + " and f.CREATE_TIME>=" + startTime + " AND f.CREATE_TIME<=" + endTime;
        }

        log.debug("sqlPag->" + sql);
        long count1 = this.getSimpleJdbcTemplate().queryForLong(sql, new Object[0]);
        return count1;
    }

    private String replaceSpecialCharacter(String productName) {
        if(StringUtil.isNotEmpty(productName)) {
            productName = productName.trim();
            return productName.replace("|", "||").replace("%", "|%").replace("_", "|_");
        } else {
            return "";
        }
    }

    private void getProductIds(Integer parentId, List<String> labelIdList) {
        CacheBase cache = CacheBase.getInstance();
        CopyOnWriteArrayList labelIds = cache.getKeyList("ALL_EFFECTIVE_PRODUCT_MAP");
        List secondLCategory = this.getSecondLCategoryIds(parentId);
        Iterator i$ = secondLCategory.iterator();

        while(i$.hasNext()) {
            CiProductCategory ciProductCategory = (CiProductCategory)i$.next();
            Integer categoryId = ciProductCategory.getCategoryId();
            Iterator i$1 = labelIds.iterator();

            while(i$1.hasNext()) {
                String idStr = (String)i$1.next();
                CiProductInfo ciProductInfo = cache.getEffectiveProduct(idStr);
                if(ciProductInfo.getCategoryId().compareTo(categoryId) == 0 && ciProductInfo.getIsProduct().intValue() == 1) {
                    labelIdList.add(idStr);
                }
            }
        }

    }

    private List<CiProductCategory> getSecondLCategoryIds(Integer firstLId) {
        ArrayList secondLCategory = new ArrayList();
        CacheBase cache = CacheBase.getInstance();
        CopyOnWriteArrayList categoryIds = cache.getKeyList("ALL_PRODUCT_CATEGORY_MAP");
        Iterator i$ = categoryIds.iterator();

        while(i$.hasNext()) {
            String idStr = (String)i$.next();
            CiProductCategory ciProductCategory = cache.getProductCategory(idStr);
            if(ciProductCategory.getParentId().compareTo(firstLId) == 0) {
                secondLCategory.add(ciProductCategory);
            }
        }

        return secondLCategory;
    }
}
