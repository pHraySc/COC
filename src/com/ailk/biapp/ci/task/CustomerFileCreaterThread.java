package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.ICiGroupAttrRelJDao;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.entity.CiPersonNotice;
import com.ailk.biapp.ci.localization.nonstandard.ICustomerListFileCreate;
import com.ailk.biapp.ci.service.ICiPersonNoticeService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.FileUtil;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.File;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class CustomerFileCreaterThread extends Thread {
    private final Logger log = Logger.getLogger(CustomerFileCreaterThread.class);
    private String listTableName;
    private String downloadUserCityId;
    @Autowired
    private ICustomersManagerService customersService;
    @Autowired
    private ICiPersonNoticeService ciPersonNoticeService;
    @Autowired
    private ICiGroupAttrRelJDao ciGroupAttrRelJDao;

    public CustomerFileCreaterThread() {
    }

    public void run() {
        CiCustomListInfo customListInfo = this.customersService.queryCiCustomListInfoById(this.listTableName);
        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(customListInfo.getCustomGroupId());
        customListInfo.setFileCreateStatus(Integer.valueOf(2));
        this.customersService.syncUpdateCiCustomListInfo(customListInfo);
        boolean needCutFlag = false;
        boolean result = false;
        String createCityId = customGroupInfo.getCreateCityId();
        boolean isCenterCityUser = false;
        String centerCity = Configure.getInstance().getProperty("CENTER_CITYID");
        if(StringUtil.isNotEmpty(this.downloadUserCityId)) {
            isCenterCityUser = this.downloadUserCityId.equals(centerCity);
        }

        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        if(needAuthority && createCityId.equals(centerCity) && !isCenterCityUser) {
            needCutFlag = true;
        }

        String keyColumn = Configure.getInstance().getProperty("RELATED_COLUMN");
        String title = Configure.getInstance().getProperty("RELATED_COLUMN_CN_NAME");
        if(StringUtil.isEmpty(title)) {
            title = "手机号码";
        }

        String customGroupId = customListInfo.getCustomGroupId();
        List attrRelList = null;

        try {
            attrRelList = this.ciGroupAttrRelJDao.selectCiGroupAttrRelList(customGroupId, customListInfo.getDataTime());
        } catch (Exception var26) {
            this.log.error("查询客户群属性表错误！", var26);
            result = false;
        }

        if(attrRelList != null && attrRelList.size() > 0 && (attrRelList.size() != 1 || attrRelList.get(0) == null || ((CiGroupAttrRel)attrRelList.get(0)).getStatus() == null || ((CiGroupAttrRel)attrRelList.get(0)).getStatus().intValue() != 2)) {
            for(int fileLocalPath = 0; fileLocalPath < attrRelList.size(); ++fileLocalPath) {
                keyColumn = keyColumn + ",";
                keyColumn = keyColumn + ((CiGroupAttrRel)attrRelList.get(fileLocalPath)).getId().getAttrCol();
                title = title + ",";
                title = title + ((CiGroupAttrRel)attrRelList.get(fileLocalPath)).getAttrColName().replaceAll(",", "，");
            }
        }

        String var27 = Configure.getInstance().getProperty("CUSTOM_FILE_DOWN_PATH");
        String fileName = null;
        String nonstandardClassName = Configure.getInstance().getProperty("CUSTOMER_LIST_FILE_CREATE_NONSTANDARD_CLASS_NAME");
        if(StringUtil.isNotEmpty(nonstandardClassName)) {
            try {
                ICustomerListFileCreate personNotice = (ICustomerListFileCreate)SystemServiceLocator.getInstance().getService(nonstandardClassName);
                fileName = personNotice.createCustomerListFile(keyColumn, this.listTableName, var27);
                if(StringUtil.isEmpty(fileName)) {
                    result = false;
                } else {
                    result = true;
                }
            } catch (Exception var25) {
                this.log.error("非标准清单文件生成错误", var25);
                var25.printStackTrace();
                result = false;
            }
        } else {
            StringBuffer var28 = new StringBuffer();
            var28.append(" SELECT ").append(keyColumn).append(" FROM ").append(this.listTableName);
            String resultMsg = var28.toString();
            String csvFile;
            String zipFile;
            String csvFileObj;
            if(needCutFlag) {
                csvFile = Configure.getInstance().getProperty("DW_LABEL_FORM_TABLE");
                zipFile = Configure.getInstance().getProperty("CITY_ID");
                csvFileObj = Configure.getInstance().getProperty("RELATED_COLUMN");
                var28.setLength(0);
                var28.append("select needCutResult.* from ").append(csvFile).append(customGroupInfo.getDataDate()).append(" dwLabelFormTable  inner join (").append(resultMsg).append(") needCutResult on ").append(" dwLabelFormTable.").append(csvFileObj).append(" = needCutResult.").append(csvFileObj).append(" where dwLabelFormTable.").append(zipFile).append(" = ").append(this.downloadUserCityId);
                resultMsg = var28.toString();
            }

            csvFile = var27 + File.separator + this.listTableName + ".csv";
            zipFile = var27 + File.separator + this.listTableName + ".zip";

            try {
                this.log.debug("sql2file:" + csvFile);
                csvFileObj = Configure.getInstance().getProperty("EXPORT_TO_FILE_PAGESIZE");
                int num = 100000;
                if(StringUtil.isNotEmpty(csvFileObj)) {
                    num = Integer.valueOf(csvFileObj).intValue();
                }

                this.log.debug("export page size is : " + num);
                String jndiName = Configure.getInstance().getProperty("JNDI_CI");
                if(StringUtils.isNotEmpty(Configure.getInstance().getProperty("JNDI_CI_BACK"))) {
                    jndiName = Configure.getInstance().getProperty("JNDI_CI_BACK");
                }

                FileUtil.sql2File(resultMsg, jndiName, (Object[])null, title, keyColumn, csvFile, "GBK", (String)null, false, (String)null, num);
            } catch (Exception var24) {
                this.log.error("导出清单文件错误" + resultMsg + csvFile + zipFile, var24);
                result = false;
            }

            try {
                this.log.debug("zipfile:" + zipFile);
                FileUtil.zipFileUnPassword(csvFile, zipFile, "UTF-8");
                result = true;
            } catch (Exception var23) {
                this.log.error("压缩文件出错：" + zipFile, var23);
                result = false;
            }

            fileName = this.listTableName + ".zip";
            File var32 = new File(csvFile);
            if(result && var32.exists()) {
                var32.delete();
            }
        }

        CiCustomListInfo var29;
        if(result) {
            customListInfo.setFileCreateStatus(Integer.valueOf(3));
            var29 = this.customersService.queryCiCustomListInfoById(this.listTableName);
            customListInfo.setFileApproveStatus(var29.getFileApproveStatus());
            this.customersService.syncUpdateCiCustomListInfo(customListInfo);
        } else {
            customListInfo.setFileCreateStatus(Integer.valueOf(4));
            var29 = this.customersService.queryCiCustomListInfoById(this.listTableName);
            customListInfo.setFileApproveStatus(var29.getFileApproveStatus());
            this.customersService.syncUpdateCiCustomListInfo(customListInfo);
        }

        CiPersonNotice var30 = new CiPersonNotice();
        var30.setStatus(Integer.valueOf(1));
        var30.setCustomerGroupId(customGroupInfo.getCustomGroupId());
        var30.setNoticeName(customGroupInfo.getCustomGroupName() + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_CREATE_CUSTOMERS_FILE_GENERATE);
        var30.setNoticeSendTime(new Date());
        var30.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_CUSTOM_FILE_CREATE));
        var30.setReadStatus(Integer.valueOf(1));
        var30.setReceiveUserId(customGroupInfo.getCreateUserId());
        if(result) {
            StringBuffer var31 = new StringBuffer();
            var31.append("客户群【" + customGroupInfo.getCustomGroupName() + "】的清单文件：" + fileName + " 创建成功！");
            this.log.info("personNotice:" + var31.toString());
            var30.setNoticeDetail(var31.toString());
            var30.setIsSuccess(Integer.valueOf(1));
            this.ciPersonNoticeService.addPersonNotice(var30);
        } else {
            var30.setNoticeDetail("客户群【" + customGroupInfo.getCustomGroupName() + "】的清单文件：" + fileName + " 创建失败！");
            var30.setIsSuccess(Integer.valueOf(0));
            this.ciPersonNoticeService.addPersonNotice(var30);
        }

    }

    public String getListTableName() {
        return this.listTableName;
    }

    public void setListTableName(String listTableName) {
        this.listTableName = listTableName;
    }

    public String getDownloadUserCityId() {
        return this.downloadUserCityId;
    }

    public void setDownloadUserCityId(String downloadUserCityId) {
        this.downloadUserCityId = downloadUserCityId;
    }
}
