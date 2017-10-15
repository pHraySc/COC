
package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiMdaSysTable;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.entity.CiPersonNotice;
import com.ailk.biapp.ci.entity.CiProductInfo;
import com.ailk.biapp.ci.entity.CustomProductMatch;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ICiMarketingStrategyService;
import com.ailk.biapp.ci.service.ICiPersonNoticeService;
import com.ailk.biapp.ci.service.ICiProductService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;

import edu.emory.mathcs.backport.java.util.Collections;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class CiProductMatchThread extends Thread {
    private Logger log = Logger.getLogger(CiProductMatchThread.class);
    @Autowired
    private ICiMarketingStrategyService ciMarketingStrategyServiceImpl;
    @Autowired
    private ICiProductService ciProductServiceImpl;
    @Autowired
    private ICustomersManagerService customersManagerService;
    @Autowired
    ICiPersonNoticeService ciPersonNoticeService;
    private CiCustomListInfo ciCustomListInfo;
    private List<CiProductInfo> products = null;
    private List<CustomProductMatch> matchResult = new ArrayList();

    public CiProductMatchThread() {
    }

    public void run() {
        if(this.ciCustomListInfo.getDataStatus().intValue() == 3) {
            if(this.ciCustomListInfo.getCustomNum().longValue() > 0L) {
                boolean flag = false;

                try {
                    this.ciMarketingStrategyServiceImpl.removeCustomProductMatchByListTableName(this.ciCustomListInfo.getListTableName());
                    this.products = this.ciProductServiceImpl.queryEffectiveCiProductInfo();
                    String e = this.getNewProductDate();
                    this.matchBySql(e);
                    if(this.matchResult.size() > 0) {
                        Collections.sort(this.matchResult, new Comparator() {
                            public int compare(CustomProductMatch o1, CustomProductMatch o2) {
                                return o2.getMatchPropotion().doubleValue() > o1.getMatchPropotion().doubleValue()?1:(o2.getMatchPropotion().doubleValue() < o1.getMatchPropotion().doubleValue()?-1:0);
                            }

							@Override
							public int compare(Object o1, Object o2) {
								// TODO Auto-generated method stub
								return 0;
							}
                        });

                        for(int i = 0; i < this.matchResult.size(); ++i) {
                            this.ciMarketingStrategyServiceImpl.saveCustomProductMatch((CustomProductMatch)this.matchResult.get(i));
                        }

                        this.log.debug("matchResult " + this.matchResult);
                    }

                    flag = true;
                } catch (Exception var4) {
                    flag = false;
                    this.log.error("匹配过程发生异常", var4);
                }

                this.updateMatchFlag();
                this.sendNotice(this.ciCustomListInfo, flag);
            }
        }
    }

    private void updateMatchFlag() {
        try {
            CiCustomGroupInfo e = this.customersManagerService.queryCiCustomGroupInfo(this.ciCustomListInfo.getCustomGroupId());
            if(e.getProductAutoMacthFlag() != null && e.getProductAutoMacthFlag().intValue() > 1) {
                e.setProductAutoMacthFlag(Integer.valueOf(e.getProductAutoMacthFlag().intValue() - 2));
                this.customersManagerService.syncUpdateCiCustomGroupInfo(e);
            }
        } catch (Exception var2) {
            this.log.error("updateMatchFlag error ", var2);
        }

    }

    private void matchBySql(String dataDate) {
        CacheBase cache = CacheBase.getInstance();
        HashMap tableColumns = new HashMap();
        Iterator productInterCount = this.products.iterator();

        while(productInterCount.hasNext()) {
            CiProductInfo i$ = (CiProductInfo)productInterCount.next();
            if(i$.getIsProduct() != null && i$.getIsProduct().intValue() == 1) {
                String productInfo = i$.getProductId().toString();
                CiProductInfo productIdStr = cache.getEffectiveProduct(productInfo);
                CiMdaSysTableColumn ciProductInfo = productIdStr.getCiMdaSysTableColumn();
                if(ciProductInfo != null) {
                    CiMdaSysTable column = ciProductInfo.getCiMdaSysTable();
                    if(column != null) {
                        if(tableColumns.containsKey(column.getTableName() + "_" + dataDate)) {
                            ((List)tableColumns.get(column.getTableName() + "_" + dataDate)).add(ciProductInfo.getColumnName());
                        } else {
                            ArrayList cols = new ArrayList();
                            cols.add(ciProductInfo.getColumnName());
                            tableColumns.put(column.getTableName() + "_" + dataDate, cols);
                        }
                    }
                }
            }
        }

        HashMap productInterCount1 = new HashMap();
        this.fillMap(tableColumns, productInterCount1);
        Iterator i$1 = this.products.iterator();

        while(i$1.hasNext()) {
            CiProductInfo productInfo1 = (CiProductInfo)i$1.next();
            if(productInfo1.getIsProduct() != null && productInfo1.getIsProduct().intValue() == 1) {
                String productIdStr1 = productInfo1.getProductId().toString();
                CiProductInfo ciProductInfo1 = cache.getEffectiveProduct(productIdStr1);
                CiMdaSysTableColumn column1 = ciProductInfo1.getCiMdaSysTableColumn();
                if(productInterCount1.get(column1.getColumnName().toString()) != null) {
                    this.addMatchResult(dataDate, productInfo1, ((Long)productInterCount1.get(column1.getColumnName().toString())).intValue());
                }
            }
        }

    }

    private void fillMap(Map<String, List<String>> tableColumns, Map<String, Long> productInterCount) {
        String primaryKey = Configure.getInstance().getProperty("RELATED_COLUMN");
        int colSize = 30;

        try {
            colSize = Integer.valueOf(Configure.getInstance().getProperty("PRODUCTMATCH_COLUMNS_IN_SQL")).intValue();
        } catch (NumberFormatException var13) {
            this.log.error("PRODUCTMATCH_COLUMNS_IN_SQL cannot format to int," + var13.getMessage());
        }

        Iterator i$ = tableColumns.keySet().iterator();

        while(i$.hasNext()) {
            String tableName = (String)i$.next();
            int i = 0;
            StringBuffer fromSql = (new StringBuffer()).append(" from ").append(this.ciCustomListInfo.getListTableName()).append(" a left join ").append(tableName).append(" b on a.").append(primaryKey).append("=b.").append(primaryKey);
            StringBuffer selectSql = new StringBuffer("select ");
            List cols = (List)tableColumns.get(tableName);

            for(Iterator i$1 = cols.iterator(); i$1.hasNext(); ++i) {
                String col = (String)i$1.next();
                selectSql.append(" sum( ").append("case when ").append(col).append("=1 then 1 else 0 end) as ").append(col.toUpperCase()).append(",");
                if(i % colSize == 0) {
                    i = 0;
                    this.getCount(selectSql.substring(0, selectSql.length() - 1) + fromSql.toString(), productInterCount);
                    selectSql = new StringBuffer("select ");
                }
            }

            if(i > 0) {
                this.getCount(selectSql.substring(0, selectSql.length() - 1) + fromSql.toString(), productInterCount);
            }
        }

    }

    private void getCount(String sql, Map<String, Long> productInterCount) {
        try {
            Map e = this.customersManagerService.query4Map(sql);
            if(e != null) {
                Iterator i$ = e.entrySet().iterator();

                while(i$.hasNext()) {
                    Entry data = (Entry)i$.next();
                    BigDecimal count = new BigDecimal(data.getValue().toString());
                    productInterCount.put(((String)data.getKey()).toString(), Long.valueOf(count.longValue()));
                }
            }
        } catch (Exception var7) {
            this.log.error("match error:", var7);
        }

    }

    public void sendNotice(CiCustomListInfo listInfo, boolean flag) {
        try {
            CiCustomGroupInfo e = this.customersManagerService.queryCiCustomGroupInfo(listInfo.getCustomGroupId());
            StringBuffer msg = new StringBuffer();
            msg.append("客户群：").append(e.getCustomGroupName()).append("  数据日期为").append(listInfo.getDataDate()).append("的清单策略匹配");
            CiPersonNotice ciPersonNotice = new CiPersonNotice();
            if(flag) {
                msg.append("完成");
                ciPersonNotice.setIsSuccess(Integer.valueOf(1));
            } else {
                msg.append("失败");
                ciPersonNotice.setIsSuccess(Integer.valueOf(0));
            }

            ciPersonNotice.setIsSuccess(Integer.valueOf(1));
            ciPersonNotice.setStatus(Integer.valueOf(1));
            ciPersonNotice.setCustomerGroupId(e.getCustomGroupId());
            ciPersonNotice.setNoticeName(e.getCustomGroupName() + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_PUBLISH_CUSTOMERS_MATCH_PRODUCT);
            ciPersonNotice.setNoticeDetail(msg.toString());
            ciPersonNotice.setNoticeSendTime(new Date());
            ciPersonNotice.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_PUBLISH_CUSTOMERS_MATCH_PRODUCT));
            ciPersonNotice.setReadStatus(Integer.valueOf(1));
            ciPersonNotice.setReceiveUserId(e.getCreateUserId());
            this.ciPersonNoticeService.addPersonNotice(ciPersonNotice);
        } catch (CIServiceException var6) {
            this.log.error("send notice error", var6);
        }

    }

    private void addMatchResult(String dataDate, CiProductInfo productInfo, int intersectCount) {
        if(intersectCount != 0) {
            BigDecimal a = new BigDecimal(this.ciCustomListInfo.getCustomNum().longValue());
            BigDecimal b = (new BigDecimal(intersectCount)).multiply(new BigDecimal(100));
            double c = b.divide(a, 2, RoundingMode.HALF_UP).doubleValue();
            CustomProductMatch match = new CustomProductMatch();
            match.setDataDate(dataDate);
            match.setListTableName(this.ciCustomListInfo.getListTableName());
            match.setMatchCustomNum(Integer.valueOf(intersectCount));
            match.setMatchPropotion(Double.valueOf(c));
            match.setProductId(productInfo.getProductId());
            this.matchResult.add(match);
        }
    }

    private String getNewProductDate() {
        return CacheBase.getInstance().getNewLabelMonth();
    }

    public CiCustomListInfo getCiCustomListInfo() {
        return this.ciCustomListInfo;
    }

    public void setCiCustomListInfo(CiCustomListInfo ciCustomListInfo) {
        try {
            this.ciCustomListInfo = ciCustomListInfo.clone();
        } catch (CloneNotSupportedException var3) {
            this.log.error("客户群清单对象clone异常", var3);
        }

    }

    public static void main(String[] args) {
        ArrayList products = new ArrayList();

        int i;
        for(i = 0; i < 400; ++i) {
            double x = 1.0D * Math.random();
            products.add(String.valueOf(x));
        }

        Collections.sort(products, new Comparator() {
            public int compare(String o1, String o2) {
                return (int)(Double.valueOf(o2).doubleValue() - Double.valueOf(o1).doubleValue());
            }

			@Override
			public int compare(Object o1, Object o2) {
				// TODO Auto-generated method stub
				return 0;
			}
        });

        for(i = 0; i < 10; ++i) {
            System.out.println((String)products.get(i));
        }

    }

    public String toString() {
        return "CiProductMatchThread [ciCustomListInfo=" + this.ciCustomListInfo.getListTableName() + "]";
    }
}
