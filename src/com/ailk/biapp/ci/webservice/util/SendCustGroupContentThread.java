package com.ailk.biapp.ci.webservice.util;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.IMcdInterfaceJDao;
import com.ailk.biapp.ci.model.CiLabelCategoryRel;
import com.ailk.biapp.ci.model.LabelColumnTable;
import com.ailk.biapp.ci.model.TargetCustomersModel;
import com.ailk.biapp.ci.service.ICustomersListInfoService;
import com.ailk.biapp.ci.util.DataBaseAdapter;
import com.ailk.biapp.ci.util.JsonUtil;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class SendCustGroupContentThread extends Thread {
    private Logger log = Logger.getLogger(SendCustGroupContentThread.class);
    private String custGroupId;
    private String contentType;
    private String custGroupDate;
    private String column = "contentId";
    private String column_label = "labelName";
    private String labelInfoCategoryId;
    @Autowired
    private IMcdInterfaceJDao iMcdInterfaceJDao;
    @Autowired
    private ICustomersListInfoService customersListInfoService;

    public SendCustGroupContentThread() {
    }

    public String getCustGroupId() {
        return this.custGroupId;
    }

    public void setCustGroupId(String custGroupId) {
        this.custGroupId = custGroupId;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCustGroupDate() {
        return this.custGroupDate;
    }

    public void setCustGroupDate(String custGroupDate) {
        this.custGroupDate = custGroupDate;
    }

    public String getLabelInfoCategoryId() {
        return this.labelInfoCategoryId;
    }

    public void setLabelInfoCategoryId(String labelInfoCategoryId) {
        this.labelInfoCategoryId = labelInfoCategoryId;
    }

    public void run() {
        Boolean result = Boolean.valueOf(true);
        HashMap mapval = new HashMap();
        String relatedColumn = Configure.getInstance().getProperty("RELATED_COLUMN");
        String dbType = Configure.getInstance().getProperty("CI_BACK_DBTYPE");
        Object labelColumnTableList = new ArrayList();
        this.log.info("mcd webservice invoke method sendCustGroupContentReq start,param is : " + this.custGroupId + "=" + this.contentType + "=" + this.custGroupDate);

        try {
            TargetCustomersModel e = null;
            String customers_list_tableName = "";
            if(StringUtil.isNotEmpty(this.custGroupDate)) {
                e = this.iMcdInterfaceJDao.getCustomerById(this.custGroupId, this.custGroupDate);
                customers_list_tableName = e.getTarget_customers_tab_name();
            }

            if(e != null && StringUtil.isNotEmpty(customers_list_tableName)) {
                Boolean valuelist = Boolean.valueOf(this.iMcdInterfaceJDao.getTableExists(customers_list_tableName));
                if(!valuelist.booleanValue()) {
                    e = null;
                }
            }

            if(e == null || StringUtil.isEmpty(customers_list_tableName)) {
                e = this.iMcdInterfaceJDao.getCustomerById(this.custGroupId);
                this.custGroupDate = e.getTarget_customers_base_month();
                customers_list_tableName = e.getTarget_customers_tab_name();
            }

            if(e == null) {
                this.log.error("客户群查询失败：======= custGroupId=" + this.custGroupId + "&custGroupDate =" + this.custGroupDate);
            }

            try {
                HashMap var24 = new HashMap();
                DataBaseAdapter mapVal = new DataBaseAdapter(dbType);
                var24.put(this.column, mapVal.getStrColumnType(32));
                var24.put(this.column_label, mapVal.getStrColumnType(128));
                this.customersListInfoService.addListTableNameColumn(customers_list_tableName, var24);
            } catch (Exception var21) {
                result = Boolean.valueOf(false);
                this.log.error("清单表添加列失败========清单表名：" + customers_list_tableName + var21.getMessage());
            }

            if(result.booleanValue()) {
                List var25 = this.iMcdInterfaceJDao.getCiLabelInfoList(this.labelInfoCategoryId);
                this.log.info("根据配置的所属标签类型parentId=" + this.labelInfoCategoryId + "查询结果数量" + var25.size());

                try {
                    HashMap var27 = new HashMap();
                    if(var25 != null && var25.size() > 0) {
                        var27.put("labelIdLevelDescs", var25);
                    }

                    labelColumnTableList = this.iMcdInterfaceJDao.getLabelColumnTableList(var27);
                } catch (Exception var20) {
                    result = Boolean.valueOf(false);
                    this.log.error("根据标签ids查询表和列失败========清单表名：" + customers_list_tableName + var20.getMessage());
                }
            }

            Object var26 = new ArrayList();
            if(result.booleanValue()) {
                try {
                    StringBuffer var28 = new StringBuffer("SELECT A." + relatedColumn);
                    StringBuffer tabSql = new StringBuffer(" FROM " + customers_list_tableName + " A ");
                    ArrayList labelId = new ArrayList();
                    Iterator r = ((List)labelColumnTableList).iterator();

                    while(true) {
                        while(r.hasNext()) {
                            LabelColumnTable labelName = (LabelColumnTable)r.next();
                            if(StringUtil.isNotEmpty(labelName.getColumnName()) && StringUtil.isNotEmpty(labelName.getTableName())) {
                                String size = labelName.getTableName();
                                var28.append(",(CASE " + size + "." + labelName.getColumnName() + " WHEN 1 THEN 1 ELSE 0 END) AS " + labelName.getColumnName());
                                if(!labelId.contains(size)) {
                                    tabSql.append(" LEFT JOIN ").append(size + " " + size).append(" ON A." + relatedColumn + "=" + size + "." + relatedColumn);
                                    labelId.add(size);
                                }
                            } else {
                                ((List)labelColumnTableList).remove(labelName);
                            }
                        }

                        var28.append(tabSql);
                        var26 = this.iMcdInterfaceJDao.getExcuteSqlResultList(var28.toString());
                        this.log.info("开始获取产品，查询清单表对应的标签值的大小========" + ((List)var26).size());
                        break;
                    }
                } catch (Exception var22) {
                    result = Boolean.valueOf(false);
                    this.log.error("开始获取产品，查询清单表对应的标签值失败！部分清单表或者清单表对应的字段不存在" + var22.getMessage());
                }
            }

            if(result.booleanValue()) {
                this.customersListInfoService.updateCustomerListInfo(customers_list_tableName, this.custGroupDate, ServiceConstants.ISSTRENGTHEN_ON);
                Iterator var30 = ((List)var26).iterator();

                while(true) {
                    if(!var30.hasNext()) {
                        this.log.info("开始获取产品内容结束");
                        break;
                    }

                    Map var29 = (Map)var30.next();
                    Integer var31 = null;
                    String var32 = "";
                    Random var33 = new Random();

                    for(int var34 = 0; var31 == null && var34 < ((List)labelColumnTableList).size(); ++var34) {
                        int descLabel = var33.nextInt(((List)labelColumnTableList).size());
                        LabelColumnTable contentMap = (LabelColumnTable)((List)labelColumnTableList).get(descLabel);
                        Object e1 = var29.get(contentMap.getColumnName().trim());
                        if(StringUtil.isNotEmpty(e1) && "1".equals("" + e1)) {
                            var31 = contentMap.getLabelId();
                            var32 = contentMap.getLabelName();
                        }
                    }

                    this.log.info("随机产生的labelId=" + var31);
                    String var35 = "";
                    if(var31 != null) {
                        this.log.info(var29.get(relatedColumn) + "随机产生的标签,labelId=" + var31);
                        List var36 = this.iMcdInterfaceJDao.getCiLabelCategoryRelByLabelId(var31);
                        if(var36 != null && var36.size() > 0) {
                            var35 = ((CiLabelCategoryRel)var36.get(0)).getDescLabel();
                            if(var35.indexOf("|") > -1) {
                                String[] var38 = var35.split("\\|");
                                int descLabelIndex = var33.nextInt(var38.length);
                                var35 = var38[descLabelIndex];
                            }
                        }
                    }

                    this.log.info("查询的descLabel=" + var35 + "contentType=" + this.contentType);
                    Object var37 = new HashMap();

                    try {
                        var37 = this.getPocContent(this.contentType, var35);
                    } catch (Exception var19) {
                        this.log.error("获取推荐产品报错========" + var19.getMessage());
                    }

                    if(var37 != null && StringUtil.isNotEmpty(((Map)var37).get("productId"))) {
                        mapval.put(var29.get(relatedColumn).toString(), new String[]{var32, ((Map)var37).get("productId").toString()});
                    }
                }
            }

            if(result.booleanValue()) {
                this.log.info("结果result======" + result + "====listTableName=" + e.getTarget_customers_tab_name() + "#####custGroupDate=" + this.custGroupDate + "####isStrengthen=" + ServiceConstants.ISSTRENGTHEN_YS);
                this.customersListInfoService.updateCustomerListInfo(customers_list_tableName, this.custGroupDate, ServiceConstants.ISSTRENGTHEN_YS, this.column, this.column_label, mapval);
            } else {
                this.log.info("结果result======" + result + "===listTableName=" + customers_list_tableName + "#####custGroupDate=" + this.custGroupDate + "####isStrengthen=" + ServiceConstants.ISSTRENGTHEN_FAIL);
                this.customersListInfoService.updateCustomerListInfo(customers_list_tableName, this.custGroupDate, ServiceConstants.ISSTRENGTHEN_FAIL);
            }
        } catch (Exception var23) {
            result = Boolean.valueOf(false);
            this.log.info("客户群内容偏好加强请接口失败" + var23.getMessage());
        }

        this.log.info("mcd webservice invoke method sendCustGroupContentReq end");
    }

    private Map<String, Object> getPocContent(String categoryId, String content) throws Exception {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        String pocWsdlUrl = Configure.getInstance().getProperty("WS_POC_URL");
        Client client = dcf.createClient(pocWsdlUrl.trim());
        Object[] obj = client.invoke("getRandomProductByCategory", new Object[]{categoryId, content});
        Map map = (Map)JsonUtil.json2Bean(obj[0].toString(), Map.class);
        Map list = (Map)map.get("data");
        return list;
    }
}
