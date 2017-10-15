package com.ailk.biapp.ci.localization.sichuan.nonstandardimpl;

import com.ailk.biapp.ci.localization.nonstandard.ICustomerListFileCreate;
import com.ailk.biapp.ci.util.FileUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.File;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("sCCustomerListFileCreateImpl")
@Transactional
@Scope("prototype")
public class SCCustomerListFileCreateImpl implements ICustomerListFileCreate {
    private Logger log = Logger.getLogger(this.getClass());

    public SCCustomerListFileCreateImpl() {
    }

    public String createCustomerListFile(String keyColumn, String listTableName, String fileLocalPath) throws Exception {
        String title = "手机号码,地市,区县";
        String column = keyColumn + ",CITY_NAME,COUNTY_NAME";
        String dataDate = CacheBase.getInstance().getNewLabelMonth();
        String dwFormTableName = Configure.getInstance().getProperty("DW_LABEL_FORM_TABLE");
        String localSchema = Configure.getInstance().getProperty("LOCAL_SCHEMA");
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DISTINCT D.").append(keyColumn).append(",CASE WHEN C.GROUP_NAME IS NULL THEN \'地市不详\' ELSE C.GROUP_NAME END AS CITY_NAME,").append("CASE WHEN E.GROUP_NAME IS NULL THEN \'区县不详\' ELSE E.GROUP_NAME END AS COUNTY_NAME FROM ").append("(SELECT  A.").append(keyColumn).append(",CHAR(B.CITY_ID) AS CITY_ID,CHAR(B.COUNTY_ID) AS COUNTY_ID FROM ").append(listTableName).append(" A LEFT JOIN ").append(dwFormTableName).append(dataDate).append(" B ON A.").append(keyColumn).append(" = B.").append(keyColumn).append(")D").append(" LEFT JOIN (SELECT * FROM ").append(localSchema).append(".POWER_AREAINFO WHERE LVL_ID IN (1,2)) C").append(" ON C.GROUP_ID=D.CITY_ID ").append(" LEFT JOIN (SELECT * FROM ").append(localSchema).append(".POWER_AREAINFO WHERE LVL_ID IN (1,3)) E").append(" ON E.GROUP_ID=D.COUNTY_ID ");
        String csvFile = fileLocalPath + File.separator + listTableName + ".csv";
        String zipFile = fileLocalPath + File.separator + listTableName + ".zip";

        String fileName;
        try {
            this.log.debug("sql2file:" + csvFile);
            fileName = Configure.getInstance().getProperty("EXPORT_TO_FILE_PAGESIZE");
            int num = 100000;
            if(StringUtil.isNotEmpty(fileName)) {
                num = Integer.valueOf(fileName).intValue();
            }

            this.log.debug("export page size is : " + num);
            String jndiName = Configure.getInstance().getProperty("JNDI_CI");
            if(StringUtils.isNotEmpty(Configure.getInstance().getProperty("JNDI_CI_BACK"))) {
                jndiName = Configure.getInstance().getProperty("JNDI_CI_BACK");
            }

            FileUtil.sql2File(sql.toString(), jndiName, (Object[])null, title, column, csvFile, "GBK", (String)null, false, (String)null, num);
        } catch (Exception var16) {
            this.log.error("导出清单文件错误" + sql + csvFile + zipFile, var16);
        }

        try {
            this.log.debug("zipfile:" + zipFile);
            FileUtil.zipFileUnPassword(csvFile, zipFile, "UTF-8");
        } catch (Exception var15) {
            this.log.error("压缩文件出错：" + zipFile, var15);
        }

        fileName = listTableName + ".zip";
        return fileName;
    }
}
