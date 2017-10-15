package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.constant.CommonConstants;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.ICiGroupAttrRelJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomGroupPushCycle;
import com.ailk.biapp.ci.entity.CiCustomGroupPushCycleId;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiCustomPushReq;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.entity.CiLabelExtInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.entity.CiPersonNotice;
import com.ailk.biapp.ci.entity.CiProductInfo;
import com.ailk.biapp.ci.entity.CiSysInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.localization.nonstandard.ICustomerPush;
import com.ailk.biapp.ci.model.OtherSysXmlBean;
import com.ailk.biapp.ci.model.PowerShowRequestXmlBean;
import com.ailk.biapp.ci.model.StandardPushXmlBean;
import com.ailk.biapp.ci.model.StandardPushXmlBean.Data;
import com.ailk.biapp.ci.model.StandardPushXmlBean.Title;
import com.ailk.biapp.ci.model.StandardPushXmlBean.Data.Column;
import com.ailk.biapp.ci.service.ICiPersonNoticeService;
import com.ailk.biapp.ci.service.ICiSysInfoService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.Bean2XMLUtils;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.DESUtil;
import com.ailk.biapp.ci.util.DataBaseAdapter;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.FileUtil;
import com.ailk.biapp.ci.util.FtpUtil;
import com.ailk.biapp.ci.util.MD5Util;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.ailk.biapp.ci.webservice.ICustomerMarketCampaignWsServer;
import com.ailk.biapp.ci.webservice.IWsSendNoticeToVgopClient;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.DES;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

@Service
@Scope("prototype")
public class CustomerPublishThread extends Thread {
    private static Logger log = Logger.getLogger(CustomerPublishThread.class);
    @Autowired
    private ICiSysInfoService ciSysInfoService;
    @Autowired
    ICiPersonNoticeService ciPersonNoticeService;
    @Autowired
    private ICustomersManagerService customersManagerService;
    @Autowired
    private ICiGroupAttrRelJDao ciGroupAttrRelJDao;
    @Autowired
    private IWsSendNoticeToVgopClient sendNoticeClient;
    private CiCustomListInfo ciCustomListInfo;
    private CiCustomGroupInfo ciCustomGroupInfo;
    private CiSysInfo sysInfo;
    private CiCustomPushReq ciCustomPushReq;
    private String reqId;
    private String fileName;
    private String pushCycle;
    private List<CiCustomPushReq> customPushReqList;
    private String pushFailReason;

    public CustomerPublishThread() {
    }

    public void initParamter(List<CiCustomPushReq> customPushReqList, String pushCycle) {
        try {
            this.customPushReqList = customPushReqList;
            this.pushCycle = pushCycle;
        } catch (Exception var4) {
            log.error("init CustomerPublishThread error", var4);
            throw new CIServiceException("init CustomerPublishThread error");
        }
    }

    public void run() {
        log.info("Enter CustomerPublishThread.run()");

        try {
            Thread.sleep(5000L);
        } catch (InterruptedException var10) {
            log.error("InterruptedException", var10);
        }

        long start = System.currentTimeMillis();
        Iterator i$ = this.customPushReqList.iterator();

        label52:
        while(i$.hasNext()) {
            CiCustomPushReq customPushReq = (CiCustomPushReq)i$.next();
            this.fileName = "COC_" + customPushReq.getUserId() + "_" + customPushReq.getReqId().replace("COC", "");
            this.ciCustomListInfo = this.customersManagerService.queryCiCustomListInfoById(customPushReq.getListTableName());
            this.ciCustomGroupInfo = this.customersManagerService.queryCiCustomGroupInfo(this.ciCustomListInfo.getCustomGroupId());
            this.ciCustomPushReq = customPushReq;
            if(this.ciCustomListInfo != null && this.ciCustomListInfo.getDataStatus().intValue() == 3) {
                this.customersManagerService.deleteCiCustomGroupPushCycleByCustomGroupId(this.ciCustomGroupInfo.getCustomGroupId());
                List sysIds = customPushReq.getSysIds();
                if(sysIds == null) {
                    continue;
                }

                Iterator i = sysIds.iterator();

                while(i.hasNext()) {
                    String cycleId = (String)i.next();

                    try {
                        log.info("======================>>>调用的推送系统ID" + cycleId);
                        this.customPublish(cycleId, customPushReq.getUserId());
                    } catch (Exception var9) {
                        log.error("推送失败", var9);
                        log.info("======================>>>推送失败系统ID" + cycleId);
                        this.delNormal(0);
                        return;
                    }
                }

                int var11 = 0;

                while(true) {
                    if(var11 >= sysIds.size()) {
                        continue label52;
                    }

                    CiCustomGroupPushCycleId var12 = new CiCustomGroupPushCycleId(this.ciCustomGroupInfo.getCustomGroupId(), (String)sysIds.get(var11));
                    CiCustomGroupPushCycle pushCycleObj = new CiCustomGroupPushCycle(var12, Integer.valueOf(1));
                    pushCycleObj.setPushCycle(Integer.valueOf(this.pushCycle));
                    pushCycleObj.setIsPushed(Integer.valueOf(1));
                    pushCycleObj.setModifyTime(new Date());
                    this.customersManagerService.saveCiCustomGroupPushCycle(pushCycleObj);
                    ++var11;
                }
            }

            log.error("清单状态不正确");
            break;
        }

        log.debug("cost:" + (System.currentTimeMillis() - start) + "ms");
        log.info("Exist CustomerPublishThread.run()");
    }

    private void customPublish(String sysId, String pushUserId) {
        this.sysInfo = this.ciSysInfoService.queryById(sysId);
        this.sysInfo.setReqId(this.ciCustomPushReq.getReqId());
        if(this.sysInfo == null) {
            log.error("系统信息不存在");
        } else {
            this.ciCustomPushReq.setSysId(sysId);
            this.ciCustomPushReq.setReqId((String)null);
            this.customersManagerService.saveCiCustomPushReq(this.ciCustomPushReq);
            this.sysInfo.setReqId(this.ciCustomPushReq.getReqId());
            this.ciCustomPushReq.setStartTime(new Date());
            this.delNormal(2);
            boolean flag = false;
            Long duplicateNum = this.ciCustomListInfo.getDuplicateNum();
            if(duplicateNum != null && duplicateNum.longValue() != 0L) {
                this.pushFailReason = "客户群清单包含" + duplicateNum + "条重复记录";
                flag = false;
            } else if(this.sysInfo.getSysId().equals(Configure.getInstance().getProperty("MCD_SYS_ID"))) {
                flag = this.publish2McdByJdbc();
            } else if(this.sysInfo.getSysId().equals(Configure.getInstance().getProperty("OLD_CI_SYS_ID"))) {
                flag = this.publishToOldCi();
            } else if(StringUtil.isNotEmpty(this.sysInfo.getPushClassName())) {
                try {
                    ICustomerPush needTxtIds = (ICustomerPush)SystemServiceLocator.getInstance().getService(this.sysInfo.getPushClassName());
                    flag = needTxtIds.push(this.ciCustomGroupInfo, this.ciCustomListInfo, this.sysInfo);
                } catch (Exception var8) {
                    log.error("个性化推送失败：", var8);
                    flag = false;
                    this.delException("个性化推送失败：", var8);
                }
            } else {
                String needTxtIds1 = Configure.getInstance().getProperty("FTP_NEED_TXT_SYS_IDS");
                if(StringUtil.isNotEmpty(needTxtIds1)) {
                    String[] sysIdsArray = needTxtIds1.split(",");
                    List ids = Arrays.asList(sysIdsArray);
                    if(ids != null && ids.size() > 0 && ids.contains(this.sysInfo.getSysId())) {
                        flag = this.publishTxtByFtp();
                    }
                } else {
                    flag = this.publishByFtp(pushUserId, this.ciCustomPushReq);
                }
            }

            if(flag) {
                this.ciCustomPushReq.setEndTime(new Date());
                this.delNormal(3);
            } else {
                this.delNormal(0);
            }

            if(1 == this.sysInfo.getShowInPage().intValue()) {
                if(this.sysInfo.getSysId().equals(Configure.getInstance().getProperty("CI_SYS_INFO_SYS_ID"))) {
                    this.sendNoticeSC(this.ciCustomListInfo);
                } else {
                    this.sendNotice(this.ciCustomListInfo);
                }
            }

        }
    }

    public boolean publishTxtByFtp() {
        String customRules = (StringUtil.isEmpty(this.ciCustomGroupInfo.getCustomOptRuleShow())?"":this.ciCustomGroupInfo.getCustomOptRuleShow()) + (StringUtil.isEmpty(this.ciCustomGroupInfo.getKpiDiffRule())?"":this.ciCustomGroupInfo.getKpiDiffRule()) + (StringUtil.isEmpty(this.ciCustomGroupInfo.getProdOptRuleShow())?"":this.ciCustomGroupInfo.getProdOptRuleShow()) + (StringUtil.isEmpty(this.ciCustomGroupInfo.getLabelOptRuleShow())?"":this.ciCustomGroupInfo.getLabelOptRuleShow());
        String usedLabelOrProduct = this.queryUsedLabelOrProduct(this.ciCustomGroupInfo);
        if(StringUtil.isEmpty(usedLabelOrProduct)) {
            usedLabelOrProduct = "该客户群创建过程中没有使用标签或者产品";
        } else {
            usedLabelOrProduct = usedLabelOrProduct.replaceAll("\r", "").replaceAll("\n", "");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String customDesc = this.ciCustomGroupInfo.getCustomGroupDesc() == null?"":this.ciCustomGroupInfo.getCustomGroupDesc();
        String sql = "select \'" + dateFormat.format(new Date()) + "\' as PUSHTIME," + Configure.getInstance().getProperty("RELATED_COLUMN") + ",\'" + this.ciCustomListInfo.getCustomGroupId() + "\' as CUSTOMID," + "\'" + this.ciCustomGroupInfo.getCustomGroupName().replaceAll("\r", "").replaceAll("\n", "") + "\' as CUSTOMNAME," + "\'" + this.ciCustomListInfo.getDataDate() + "\' as DATATIME," + "\'" + customDesc + "\' as CUSTOMDESC," + "\'" + customRules.replaceAll("\r", "").replaceAll("\n", "") + "\' as CUSTOMRULES," + "\'" + usedLabelOrProduct + "\' as USEDLABELORPRODUCT from " + this.ciCustomListInfo.getListTableName();
        String txtFile = this.sysInfo.getLocalPath() + File.separator + this.fileName + ".txt";

        try {
            log.debug("sql2file:" + txtFile);
            String e = Configure.getInstance().getProperty("EXPORT_TO_FILE_PAGESIZE");
            int num = 10000;
            if(StringUtil.isNotEmpty(e)) {
                num = Integer.valueOf(e).intValue();
            }

            log.debug("export page size is :" + num);
            StringBuffer keyColumn = new StringBuffer("");
            keyColumn.append("PUSHTIME,").append(Configure.getInstance().getProperty("RELATED_COLUMN") + ",").append("CUSTOMID,").append("CUSTOMNAME,").append("DATATIME,").append("CUSTOMDESC,").append("CUSTOMRULES,").append("USEDLABELORPRODUCT");
            String jndiName = Configure.getInstance().getProperty("JNDI_CI");
            if(StringUtil.isNotEmpty(Configure.getInstance().getProperty("JNDI_CI_BACK"))) {
                jndiName = Configure.getInstance().getProperty("JNDI_CI_BACK");
            }

            FileUtil.sql2Txt(sql, jndiName, (Object[])null, "手机号码", keyColumn.toString(), txtFile, "GBK", (String)null, false, (String)null, num);
        } catch (Exception var13) {
            log.error("导出清单文件错误" + sql + txtFile, var13);
            this.delException("导出清单文件错误", var13);
            return false;
        }

        try {
            log.debug("ftp :" + this.sysInfo.getFtpServerIp() + ":" + this.sysInfo.getFtpPort() + this.sysInfo.getFtpPath());
            boolean result = FtpUtil.ftp(this.sysInfo.getFtpServerIp(), this.sysInfo.getFtpPort(), this.sysInfo.getFtpUser(), DES.decrypt(this.sysInfo.getFtpPwd()), txtFile, this.sysInfo.getFtpPath() + "/");
            if(!result) {
                this.delException("FTP出错", new CIServiceException("ftp error"));
            }

            result = (new File(txtFile)).delete();
            return result;
        } catch (Exception var12) {
            log.error("FTP出错" + this.sysInfo + txtFile, var12);
            this.delException("FTP出错", var12);
            return false;
        }
    }

    public boolean publishByFtp(String pushUserId, CiCustomPushReq ciCustomPushReq) {
        String keyColumn = Configure.getInstance().getProperty("RELATED_COLUMN");
        StringBuffer sb4Col = new StringBuffer(keyColumn);
        String title = Configure.getInstance().getProperty("RELATED_COLUMN_CN_NAME");
        if(StringUtil.isEmpty(title)) {
            title = "手机号码";
        }

        StringBuffer sb4Title = new StringBuffer(title);
        List attrRelList = null;

        try {
            attrRelList = this.ciGroupAttrRelJDao.selectCiGroupAttrRelList(this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomListInfo.getDataTime());
        } catch (Exception var29) {
            log.error("查询客户群属性表错误！", var29);
        }

        ArrayList resultAttrs = new ArrayList();
        if(attrRelList != null && attrRelList.size() > 0 && (attrRelList.size() != 1 || attrRelList.get(0) == null || ((CiGroupAttrRel)attrRelList.get(0)).getStatus() == null || ((CiGroupAttrRel)attrRelList.get(0)).getStatus().intValue() != 2)) {
            Iterator cols = attrRelList.iterator();

            while(cols.hasNext()) {
                CiGroupAttrRel sql = (CiGroupAttrRel)cols.next();
                sb4Col.append(",").append(sql.getId().getAttrCol());
                sb4Title.append(",").append(sql.getAttrColName().replaceAll(",", "，"));
                resultAttrs.add(sql);
            }
        }

        String cols1 = sb4Col.toString();
        String sql1 = " select " + cols1 + " from " + this.ciCustomListInfo.getListTableName();
        boolean needCutFlag = false;
        String createCityId = this.ciCustomGroupInfo.getCreateCityId();
        String downloadUserCityId = null;

        try {
            downloadUserCityId = PrivilegeServiceUtil.getCityId(pushUserId);
        } catch (Exception var28) {
            var28.printStackTrace();
            log.error("根据userId获得cityId失败");
        }

        boolean isCenterCityUser = false;
        String centerCity = Configure.getInstance().getProperty("CENTER_CITYID");
        if(StringUtil.isNotEmpty(downloadUserCityId)) {
            isCenterCityUser = downloadUserCityId.equals(centerCity);
        }

        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        if(needAuthority && createCityId.equals(centerCity) && !isCenterCityUser) {
            needCutFlag = true;
        }

        String csvFile;
        String zipFile;
        String zipFileTmp;
        if(needCutFlag) {
            csvFile = Configure.getInstance().getProperty("DW_LABEL_FORM_TABLE");
            zipFile = Configure.getInstance().getProperty("CITY_ID");
            zipFileTmp = Configure.getInstance().getProperty("RELATED_COLUMN");
            StringBuffer result = new StringBuffer();
            result.append("select needCutResult.* from ").append(csvFile).append(this.ciCustomGroupInfo.getDataDate()).append(" dwLabelFormTable inner join (").append(sql1).append(") needCutResult on ").append(" dwLabelFormTable.").append(zipFileTmp).append(" = needCutResult.").append(zipFileTmp).append(" where dwLabelFormTable.").append(zipFile).append(" = ").append(downloadUserCityId);
            sql1 = result.toString();
        }

        csvFile = this.sysInfo.getLocalPath() + File.separator + this.fileName + ".csv";
        zipFile = this.sysInfo.getLocalPath() + File.separator + this.fileName + ".zip";
        zipFileTmp = this.sysInfo.getLocalPath() + File.separator + this.fileName + "_tmp.zip";

        String xmlFile;
        try {
            log.debug("sql2file:" + csvFile);
            xmlFile = Configure.getInstance().getProperty("EXPORT_TO_FILE_PAGESIZE");
            int e = 10000;
            if(StringUtil.isNotEmpty(xmlFile)) {
                e = Integer.valueOf(xmlFile).intValue();
            }

            log.debug("export page size is : " + e);
            String res = Configure.getInstance().getProperty("JNDI_CI");
            if(StringUtil.isNotEmpty(Configure.getInstance().getProperty("JNDI_CI_BACK"))) {
                res = Configure.getInstance().getProperty("JNDI_CI_BACK");
            }

            FileUtil.sql2File(sql1, res, (Object[])null, sb4Title.toString(), cols1, csvFile, "GBK", (String)null, false, (String)null, e);
            boolean pushXmlBody = (new File(csvFile)).exists();
            if(!pushXmlBody) {
                FileUtil.createFile(csvFile, "");
            }
        } catch (Exception var33) {
            log.error("导出清单文件错误" + sql1 + csvFile + zipFile, var33);
            this.delException("导出清单文件错误", var33);
            return false;
        }

        String e1;
        try {
            log.debug("zipfile:" + zipFile);
            if(this.sysInfo.getIsNeedDes() != null && 1 == this.sysInfo.getIsNeedDes().intValue()) {
                FileUtil.zipFileUnPassword(csvFile, zipFileTmp, "UTF-8");
            } else {
                FileUtil.zipFileUnPassword(csvFile, zipFile, "UTF-8");
            }
        } catch (Exception var32) {
            e1 = null;
            if(this.sysInfo.getIsNeedDes() != null && 1 == this.sysInfo.getIsNeedDes().intValue()) {
                e1 = csvFile + " " + zipFileTmp;
            } else {
                e1 = csvFile + " " + zipFile;
            }

            log.error("压缩文件出错：" + e1, var32);
            this.delException("压缩文件出错", var32);
            return false;
        }

        boolean result1;
        if(this.sysInfo.getIsNeedDes() != null && 1 == this.sysInfo.getIsNeedDes().intValue()) {
            try {
                xmlFile = this.sysInfo.getDesKey();
                log.debug(xmlFile);
                if(StringUtil.isEmpty(xmlFile)) {
                    e1 = "数据库中未定义密钥";
                    log.error(e1);
                    throw new CIServiceException(e1);
                }

                log.debug("key : " + xmlFile);
                DESUtil e2 = new DESUtil(xmlFile);
                e2.encryptFile(zipFileTmp, zipFile);
            } catch (Exception var31) {
                log.error("加密文件出错：" + zipFile, var31);
                this.delException("加密文件出错", var31);
                return false;
            }

            result1 = (new File(zipFileTmp)).delete();
        }

        xmlFile = this.sysInfo.getLocalPath() + File.separator + this.fileName + ".xml";
        if(this.sysInfo.getIsNeedXml() != null && 1 == this.sysInfo.getIsNeedXml().intValue()) {
            try {
                log.debug(">>create xml " + xmlFile + " start");
                this.createOtherSysXmlFile(xmlFile, zipFile);
                log.debug(">>create xml " + xmlFile + " end");
            } catch (Exception var27) {
                log.error("创建XML出错：" + xmlFile, var27);
                this.delException("创建XML出错", var27);
                return false;
            }
        }

        try {
            log.debug("ftp :" + this.sysInfo.getFtpServerIp() + ":" + this.sysInfo.getFtpPort() + this.sysInfo.getFtpPath());
            result1 = FtpUtil.ftp(this.sysInfo.getFtpServerIp(), this.sysInfo.getFtpPort(), this.sysInfo.getFtpUser(), DES.decrypt(this.sysInfo.getFtpPwd()), zipFile, this.sysInfo.getFtpPath() + "/");
            if(!result1) {
                this.delException("FTP出错", new CIServiceException("ftp error"));
            }

            result1 = (new File(zipFile)).delete();
            if(result1) {
                result1 = (new File(csvFile)).delete();
            }
        } catch (Exception var30) {
            log.error("FTP出错" + this.sysInfo + zipFile, var30);
            this.delException("FTP出错", var30);
            return false;
        }

        if(this.sysInfo.getIsNeedXml() != null && 1 == this.sysInfo.getIsNeedXml().intValue()) {
            try {
                log.debug("ftp :" + this.sysInfo.getFtpServerIp() + ":" + this.sysInfo.getFtpPort() + this.sysInfo.getFtpPath());
                result1 = FtpUtil.ftp(this.sysInfo.getFtpServerIp(), this.sysInfo.getFtpPort(), this.sysInfo.getFtpUser(), DES.decrypt(this.sysInfo.getFtpPwd()), xmlFile, this.sysInfo.getFtpPath() + "/");
                if(!result1) {
                    this.delException("FTP出错", new CIServiceException("ftp error"));
                }

                result1 = (new File(xmlFile)).delete();
            } catch (Exception var26) {
                log.error("XML FTP出错" + this.sysInfo + xmlFile, var26);
                this.delException("XML FTP出错", var26);
                return false;
            }
        }

        if(StringUtil.isNotEmpty(this.sysInfo.getWebserviceWSDL()) && StringUtil.isNotEmpty(this.sysInfo.getWebserviceMethod())) {
            try {
                e1 = null;
                Object[] e3;
                if(StringUtil.isNotEmpty(this.sysInfo.getWebserviceArgs())) {
                    String[] res1 = this.sysInfo.getWebserviceArgs().split(",");
                    e3 = this.getArgs(res1);
                } else {
                    StandardPushXmlBean res2 = this.createStandardPushXmlBean(pushUserId, ciCustomPushReq, resultAttrs);
                    String pushXmlBody1 = Bean2XMLUtils.bean2XmlString(res2);
                    log.debug("StandardPushXml : " + pushXmlBody1);
                    e3 = new Object[]{pushXmlBody1};
                }

                Object[] res3 = callWebService(this.sysInfo.getWebserviceWSDL(), this.sysInfo.getWebserviceTargetNamespace(), this.sysInfo.getWebserviceMethod(), e3);
                result1 = Boolean.valueOf(res3[0].toString()).booleanValue();
            } catch (Exception var25) {
                log.error("调用webService出错" + this.sysInfo, var25);
                this.delException("调用webService出错", var25);
                return false;
            }
        }

        return result1;
    }

    private StandardPushXmlBean createStandardPushXmlBean(String userId, CiCustomPushReq ciCustomPushReq, List<CiGroupAttrRel> resultAttrs) {
        StandardPushXmlBean bean = new StandardPushXmlBean();
        Title title = bean.getTitle();
        title.setTaskDesc(this.sysInfo.getSysName() + "推送任务");
        title.setSendTime(new Date());
        Data data = bean.getData();
        data.setReqId(ciCustomPushReq.getReqId());
        data.setPlatformCode("COC");
        data.setRowNumber(this.ciCustomGroupInfo.getCustomNum());
        data.setUserId(userId);
        data.setUploadFileName(this.fileName + ".zip");
        data.setUploadFileType("zip");
        data.setUploadFileDesc("清单文件");
        int dataCycle = this.ciCustomGroupInfo.getUpdateCycle().intValue();
        data.setDataCycle(Integer.valueOf(dataCycle));
        String dataCycleDesc = "";
        if(1 == dataCycle) {
            dataCycleDesc = "一次性";
        } else if(2 == dataCycle) {
            dataCycleDesc = "月周期";
        } else if(3 == dataCycle) {
            dataCycleDesc = "日周期";
        }

        data.setDataCycleDesc(dataCycleDesc);
        data.setCustomGroupId(this.ciCustomGroupInfo.getCustomGroupId());
        data.setCustomGroupName(this.ciCustomGroupInfo.getCustomGroupName());
        data.setCustomGroupDesc(this.ciCustomGroupInfo.getCustomGroupDesc());
        data.setDataDate(this.ciCustomGroupInfo.getDataDate());
        String keyColumn = Configure.getInstance().getProperty("RELATED_COLUMN");
        String keyTitle = Configure.getInstance().getProperty("RELATED_COLUMN_CN_NAME");
        ArrayList columns = new ArrayList();
        Column col = data.newColumn();
        col.setColumnName(keyColumn);
        col.setColumnCnName(keyTitle);
        col.setColumnDataType(CommonConstants.MAIN_COLUMN_TYPE);
        col.setIsPrimaryKey(Integer.valueOf(1));
        columns.add(col);
        Iterator i$ = resultAttrs.iterator();

        while(i$.hasNext()) {
            CiGroupAttrRel rel = (CiGroupAttrRel)i$.next();
            Column column = data.newColumn();
            column.setColumnName(rel.getId().getAttrCol());
            column.setColumnCnName(rel.getAttrColName());
            column.setColumnDataType(rel.getAttrColType());
            column.setIsPrimaryKey(Integer.valueOf(0));
            columns.add(column);
        }

        data.setColumns(columns);
        return bean;
    }

    private boolean createOtherSysXmlFile(String xmlFile, String zipFile) {
        OtherSysXmlBean bean = new OtherSysXmlBean();
        bean.getTitle().setTaskDesc(this.sysInfo.getSysName() + "推送任务");
        bean.getTitle().setSendTime(new Date());
        bean.getData().setReqId(this.ciCustomPushReq.getReqId());
        bean.getData().setPlatformCode("COC");
        File zip = new File(zipFile);
        String fileName = zip.getName();
        bean.getData().setUploadFileName(fileName);
        bean.getData().setRowNumber(this.ciCustomListInfo.getCustomNum().toString());
        bean.getData().setUserId(this.ciCustomPushReq.getUserId());
        bean.getData().setMD5Check(MD5Util.getFileMD5String(zip));
        bean.getData().setCustomName(this.ciCustomGroupInfo.getCustomGroupName());
        bean.getData().setDataTime(this.ciCustomListInfo.getDataDate());
        bean.getData().setCustomDesc(StringUtil.isEmpty(this.ciCustomGroupInfo.getCustomGroupDesc())?"":this.ciCustomGroupInfo.getCustomGroupDesc());
        String customRules = (StringUtil.isEmpty(this.ciCustomGroupInfo.getCustomOptRuleShow())?"":this.ciCustomGroupInfo.getCustomOptRuleShow()) + (StringUtil.isEmpty(this.ciCustomGroupInfo.getKpiDiffRule())?"":this.ciCustomGroupInfo.getKpiDiffRule()) + (StringUtil.isEmpty(this.ciCustomGroupInfo.getProdOptRuleShow())?"":this.ciCustomGroupInfo.getProdOptRuleShow()) + (StringUtil.isEmpty(this.ciCustomGroupInfo.getLabelOptRuleShow())?"":this.ciCustomGroupInfo.getLabelOptRuleShow());
        bean.getData().setCustomRules(customRules);
        String usedLabelOrProduct = this.queryUsedLabelOrProduct(this.ciCustomGroupInfo);
        if(StringUtil.isEmpty(usedLabelOrProduct)) {
            usedLabelOrProduct = "该客户群创建过程中没有使用标签或者产品";
        }

        bean.getData().setUsedLabelOrProduct(usedLabelOrProduct);
        String xmlStr = "";
        boolean result = false;

        try {
            xmlStr = Bean2XMLUtils.bean2XmlString(bean);
            log.debug(xmlStr);
        } catch (IOException var12) {
            log.error("生成xml文件，uploadXmlFile IOException", var12);
            this.delException("生成xml文件，uploadXmlFile IOException", var12);
            return false;
        } catch (SAXException var13) {
            log.error("生成xml文件，uploadXmlFile SAXException", var13);
            this.delException("生成xml文件，uploadXmlFile SAXException", var13);
            return false;
        } catch (IntrospectionException var14) {
            log.error("生成xml文件，uploadXmlFile IntrospectionException", var14);
            this.delException("生成xml文件，uploadXmlFile IntrospectionException", var14);
            return false;
        } catch (Exception var15) {
            log.error("生成xml文件，uploadXmlFile", var15);
            this.delException("生成xml文件，uploadXmlFile", var15);
            return false;
        }

        try {
            FileUtil.writeByteFile(xmlStr.getBytes("UTF-8"), new File(xmlFile));
            return result;
        } catch (Exception var11) {
            log.error("生成xml文件，uploadXmlFile", var11);
            this.delException("生成xml文件，uploadXmlFile", var11);
            return false;
        }
    }

    private String queryUsedLabelOrProduct(CiCustomGroupInfo ciCustomGroupInfo) {
        List ciLabelRuleList = this.customersManagerService.queryCiLabelRuleList(ciCustomGroupInfo.getCustomGroupId(), Integer.valueOf(1));
        StringBuffer sb = new StringBuffer("");
        CacheBase cache = CacheBase.getInstance();

        for(int i = 0; i < ciLabelRuleList.size(); ++i) {
            CiLabelRule ciLabelRule = (CiLabelRule)ciLabelRuleList.get(i);
            int elementType = ciLabelRule.getElementType().intValue();
            String productIdStr;
            if(elementType == 2) {
                productIdStr = ciLabelRule.getCalcuElement();
                CiLabelInfo ciProductInfo = cache.getEffectiveLabel(productIdStr);
                CiLabelExtInfo column = ciProductInfo.getCiLabelExtInfo();
                CiMdaSysTableColumn column1 = column.getCiMdaSysTableColumn();
                sb.append(ciProductInfo.getLabelName() + ":" + "[" + column1.getColumnName() + "]");
            } else if(elementType == 4) {
                productIdStr = ciLabelRule.getCalcuElement();
                CiProductInfo var13 = cache.getEffectiveProduct(productIdStr);
                CiMdaSysTableColumn var12 = var13.getCiMdaSysTableColumn();
                sb.append(var13.getProductName() + ":" + "[" + var12.getColumnName() + "]");
            }

            if((elementType == 2 || elementType == 4) && i != ciLabelRuleList.size() - 1) {
                sb.append(",");
            }
        }

        log.debug("推送的客户群使用的标签或者产品：" + sb.toString());
        return sb.toString();
    }

    private void delException(String msg, Exception e) {
        this.ciCustomPushReq.setStatus(Integer.valueOf(0));
        String expctionInfo = "";

        try {
            StringWriter e2 = new StringWriter();
            PrintWriter pw = new PrintWriter(e2);
            e.printStackTrace(pw);
            expctionInfo = e2.toString();
        } catch (Exception var6) {
            log.error(" get message error");
        }

        expctionInfo = msg + expctionInfo;
        if(expctionInfo.length() > 2048) {
            expctionInfo = expctionInfo.substring(0, 1024);
        }

        this.ciCustomPushReq.setExeInfo(expctionInfo);
        this.customersManagerService.saveCiCustomPushReq(this.ciCustomPushReq);
    }

    private void delNormal(int status) {
        this.ciCustomPushReq.setStatus(Integer.valueOf(status));
        this.customersManagerService.saveCiCustomPushReq(this.ciCustomPushReq);
    }

    public void sendNotice(CiCustomListInfo listInfo) {
        try {
            CiCustomGroupInfo e = this.customersManagerService.queryCiCustomGroupInfo(listInfo.getCustomGroupId());
            StringBuffer msg = new StringBuffer();
            msg.append("客户群：").append(e.getCustomGroupName()).append("  数据日期为").append(listInfo.getDataDate()).append("的清单推送至 ").append(this.sysInfo.getSysName());
            CiPersonNotice ciPersonNotice = new CiPersonNotice();
            if(this.ciCustomPushReq.getStatus().intValue() == 3) {
                msg.append("完成。");
                ciPersonNotice.setIsSuccess(Integer.valueOf(1));
            } else {
                msg.append("失败。");
                ciPersonNotice.setIsSuccess(Integer.valueOf(0));
            }

            if(this.ciCustomPushReq.getStatus().intValue() != 3 && StringUtil.isNotEmpty(this.pushFailReason)) {
                msg.append("失败原因：").append(this.pushFailReason);
            }

            ciPersonNotice.setStatus(Integer.valueOf(1));
            ciPersonNotice.setCustomerGroupId(e.getCustomGroupId());
            ciPersonNotice.setNoticeName(e.getCustomGroupName() + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_PUBLISH_CUSTOMERS_PUBLISH);
            ciPersonNotice.setNoticeDetail(msg.toString());
            ciPersonNotice.setNoticeSendTime(new Date());
            ciPersonNotice.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_PUBLISH_CUSTOMERS_PUBLISH));
            ciPersonNotice.setReadStatus(Integer.valueOf(1));
            ciPersonNotice.setReceiveUserId(e.getCreateUserId());
            this.ciPersonNoticeService.addPersonNotice(ciPersonNotice);
            this.sendPushNoticBySysId(listInfo);
        } catch (CIServiceException var5) {
            log.error("send notice error", var5);
        }

    }

    public void sendNoticeSC(CiCustomListInfo listInfo) {
        try {
            CiCustomGroupInfo e = this.customersManagerService.queryCiCustomGroupInfo(listInfo.getCustomGroupId());
            StringBuffer msg = new StringBuffer();
            msg.append("客户群：").append(e.getCustomGroupName()).append("  数据日期为").append(listInfo.getDataDate()).append("的清单推送至 ").append(this.sysInfo.getSysName());
            CiPersonNotice ciPersonNotice = new CiPersonNotice();
            if(this.ciCustomPushReq.getStatus().intValue() == 3) {
                msg.append("完成。");
                ciPersonNotice.setIsSuccess(Integer.valueOf(1));
            } else {
                msg.append("失败，" + DateUtil.date2String(new Date(), "yyyy-MM-dd") + "已经推送过该客户群。");
                ciPersonNotice.setIsSuccess(Integer.valueOf(0));
            }

            if(this.ciCustomPushReq.getStatus().intValue() != 3 && StringUtil.isNotEmpty(this.pushFailReason)) {
                msg.append("失败原因：").append(this.pushFailReason);
            }

            ciPersonNotice.setStatus(Integer.valueOf(1));
            ciPersonNotice.setCustomerGroupId(e.getCustomGroupId());
            ciPersonNotice.setNoticeName(e.getCustomGroupName() + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_PUBLISH_CUSTOMERS_PUBLISH);
            ciPersonNotice.setNoticeDetail(msg.toString());
            ciPersonNotice.setNoticeSendTime(new Date());
            ciPersonNotice.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_PUBLISH_CUSTOMERS_PUBLISH));
            ciPersonNotice.setReadStatus(Integer.valueOf(1));
            ciPersonNotice.setReceiveUserId(e.getCreateUserId());
            this.ciPersonNoticeService.addPersonNotice(ciPersonNotice);
            this.sendPushNoticBySysId(listInfo);
        } catch (CIServiceException var5) {
            log.error("send notice error", var5);
        }

    }

    private void sendPushNoticBySysId(CiCustomListInfo listInfo) {
        if("vgop".equalsIgnoreCase(this.ciCustomGroupInfo.getSysId())) {
            String wsdl = Configure.getInstance().getProperty("VGOP_PUSH_NOTICE_WSDL");
            String targetNamespace = Configure.getInstance().getProperty("VGOP_PUSH_NOTICE_TARGET_NAMESPACE");
            String methodName = Configure.getInstance().getProperty("VGOP_PUSH_NOTICE_METHOD_NAME");
            String customGroupId = listInfo.getCustomGroupId();
            String sysId = Configure.getInstance().getProperty("VGOP_CLWX_SYS_ID");
            String clwxCustomGroupId = this.customersManagerService.queryOtherSysCustomGroupId(customGroupId, sysId);
            Integer status = Integer.valueOf(1);
            if(this.ciCustomPushReq.getStatus().intValue() == 3) {
                status = Integer.valueOf(1);
            } else {
                status = Integer.valueOf(0);
            }

            this.sendNoticeClient.sendPushNotice(wsdl, targetNamespace, methodName, customGroupId, clwxCustomGroupId, String.valueOf(listInfo.getCustomNum()), String.valueOf(status));
        }

    }

    private Object[] getArgs(String[] argNames) {
        Object[] args = new Object[argNames.length];

        for(int i = 0; i < argNames.length; ++i) {
            String name = argNames[i];
            String value = "";

            try {
                BeanUtils.getProperty(this.ciCustomGroupInfo, name);
            } catch (Exception var9) {
                try {
                    BeanUtils.getProperty(this.ciCustomGroupInfo, name);
                } catch (Exception var8) {
                    log.error("获取属性的值error,name=" + name, var8);
                }
            }

            args[i] = value;
        }

        return args;
    }

    public boolean isExistsSame() {
        CiCustomPushReq queryParam = new CiCustomPushReq();
        queryParam.setUserId(this.ciCustomGroupInfo.getCreateUserId());
        queryParam.setSysId(this.ciCustomPushReq.getSysId());
        queryParam.setListTableName(this.ciCustomListInfo.getListTableName());
        queryParam.setStatus(Integer.valueOf(3));
        List pushreq = this.customersManagerService.queryCiCustomPushReq(queryParam);
        return pushreq != null && pushreq.size() > 0;
    }

    private boolean publish2McdByJdbc() {
        long t1 = System.currentTimeMillis();
        final String keyColumn = Configure.getInstance().getProperty("RELATED_COLUMN");
        String sql = " select distinct " + keyColumn + " from " + this.ciCustomListInfo.getListTableName() + " order by " + keyColumn;
        short pageSize = 10000;
        String currentSql = "";

        boolean e1;
        try {
            String e = Configure.getInstance().getProperty("MCD_SCHEMA");
            JdbcBaseDao var37 = (JdbcBaseDao)SystemServiceLocator.getInstance().getService("jdbcBaseDao");
            SimpleJdbcTemplate mcdJdbcTemplate = var37.getSimpleJdbcTemplate(Configure.getInstance().getProperty("JNDI_MCD"));
            DataBaseAdapter mcdDataBaseAdapter = var37.getDataBaseAdapter(Configure.getInstance().getProperty("MCD_DBTYPE"));
            StringBuffer existSqlBuf = new StringBuffer();
            existSqlBuf.append("select count(1) from ").append(e).append(".MTL_CUST_GROUP where CUST_GROUP_ID  = ? ");
            String existsSql = existSqlBuf.toString();
            if(mcdJdbcTemplate.queryForInt(existsSql, new Object[]{this.ciCustomListInfo.getListTableName()}) > 0) {
                this.ciCustomPushReq.setExeInfo("已经推送过的客户群清单");
                this.delNormal(3);
                boolean var38 = true;
                return var38;
            }

            String templateId = CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L);
            String mcdTemplateTable = Configure.getInstance().getProperty("MCD_TMP_TABLE");
            String mcdDataTableName = "mtl_cuser_" + this.ciCustomListInfo.getListTableName();
            String nameForCreate = mcdDataTableName;
            if(StringUtil.isNotEmpty(e)) {
                nameForCreate = e + "." + mcdDataTableName;
            }

            String createsql = mcdDataBaseAdapter.getCreateAsTableSql(nameForCreate, mcdTemplateTable, Configure.getInstance().getProperty("MCD_TABLESPACE"));
            log.debug("createsql:" + createsql);
            mcdJdbcTemplate.getJdbcOperations().execute(createsql);
            String mcdKeyColumn = Configure.getInstance().getProperty("MCD_TMP_TABLE_KEYCOLUMN");
            StringBuffer insertDataSqlBuf = new StringBuffer();
            insertDataSqlBuf.append("insert into ").append(e).append(".").append(mcdDataTableName).append(" (").append(mcdKeyColumn).append(",FILE_IDX_FLAG) values(?,?)");
            String insertDataSql = insertDataSqlBuf.toString();
            log.debug("insertDataSql:" + insertDataSql);
            int start = 1;

            String insetCusomerSql;
            while(true) {
                insetCusomerSql = var37.getBackDataBaseAdapter().getPagedSql(sql, start, pageSize);
                log.debug("sql2mcdDB==>pagedSQL: " + insetCusomerSql);
                final List desc = var37.getBackSimpleJdbcTemplate().queryForList(insetCusomerSql, new Object[0]);
                if(desc == null) {
                    break;
                }

                mcdJdbcTemplate.getJdbcOperations().batchUpdate(insertDataSql, new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        String tempArr = (String)((Map)desc.get(i)).get(keyColumn.toLowerCase());
                        if(tempArr == null) {
                            tempArr = "";
                        }

                        ps.setString(1, tempArr);
                        ps.setInt(2, 0);
                    }

                    public int getBatchSize() {
                        return desc.size();
                    }
                });
                if(desc.size() < pageSize) {
                    break;
                }

                ++start;
            }

            insetCusomerSql = "INSERT INTO " + e + ".MTL_CUST_GROUP ( CUST_GROUP_ID, CUST_GROUP_NAME, CREATE_USER_ID, CITY_ID, CREATE_DATE, CUST_GROUP_TYPE, CUST_GROUP_NUM, CUST_GROUP_ACCESS_TOKEN, CUST_GROUP_STATUS, CUST_GROUP_DESC, CUST_GROUP_TAB_NAME, CUST_TYPE_ID, EVALUATE_FLAG, CUST_GEN_RULE,active_templet_id ) VALUES ( ?, ?, ?, ?, ?, 10, ?, 1, 1, ?, ?, 1, 0,? ,?)";
            String var39 = "";
            if(this.ciCustomGroupInfo.getCustomGroupDesc() != null) {
                var39 = this.ciCustomGroupInfo.getCustomGroupDesc().length() > 1000?this.ciCustomGroupInfo.getCustomGroupDesc().substring(0, 1000):this.ciCustomGroupInfo.getCustomGroupDesc();
            }

            String rule = this.getCustomerRule();
            log.debug("insetCusomerSql:" + insetCusomerSql);
            String sql2 = "insert into " + e + ".mtl_templet_active ( active_templet_id, atemplet_name, create_userid, atemplet_desc, atemplet_type)" + " values (?,?,?,\'\',2)";
            log.debug("currentSql:" + sql2);
            mcdJdbcTemplate.update(sql2, new Object[]{templateId, this.ciCustomGroupInfo.getCustomGroupName(), this.ciCustomGroupInfo.getCreateUserId()});
            String sql3 = "insert into " + e + ".mtl_templet_active_field ( column_name, source_name, column_type, active_templet_id,column_cname,column_flag)" + " values (?,?,2,?,\'用户号码\',0)";
            log.debug("currentSql:" + sql3);
            mcdJdbcTemplate.update(sql3, new Object[]{mcdKeyColumn, mcdDataTableName, templateId});
            String sql4 = "insert into " + e + ".mtl_camp_data_source(source_name,source_cname,source_desc, source_type,source_status,create_time)" + " values (?, ?, \'\', 3, 1, ? )";
            log.debug("currentSql:" + sql4);
            mcdJdbcTemplate.update(sql4, new Object[]{mcdDataTableName, this.ciCustomGroupInfo.getCustomGroupName(), new Date()});
            String sql5 = "insert into " + e + ".mtl_camp_datasrc_column ( column_name, source_name, column_type, column_cname, column_class, column_desc, column_serno, column_datatype, column_status, column_flag ) " + " values (?,?,2,\'用户号码\',2,\'用户号码\',1,\'VARCHAR(15)\',1,0)";
            log.debug("currentSql:" + sql5);
            mcdJdbcTemplate.update(sql5, new Object[]{mcdKeyColumn, mcdDataTableName});

            try {
                mcdJdbcTemplate.update(insetCusomerSql, new Object[]{this.ciCustomListInfo.getListTableName(), this.ciCustomGroupInfo.getCustomGroupName(), this.ciCustomGroupInfo.getCreateUserId(), "999", DateUtil.date2String(this.ciCustomGroupInfo.getCreateTime(), "yyyy-MM-dd"), this.ciCustomListInfo.getCustomNum(), var39, mcdDataTableName, rule, templateId});
            } catch (Exception var34) {
                log.error("publishByJdbc error:", var34);
                if(this.isExistsSame()) {
                    this.ciCustomPushReq.setExeInfo("已经推送过的客户群清单");
                    this.delNormal(3);
                } else {
                    this.delException(sql5, var34);
                }
            }

            e1 = true;
        } catch (Exception var35) {
            log.error("publishByJdbc( ) error:", var35);
            this.delException(currentSql, var35);
            boolean jdbcBaseDao = false;
            return jdbcBaseDao;
        } finally {
            log.info("The cost of publishByJdbc is :  " + (System.currentTimeMillis() - t1) + "ms");
        }

        return e1;
    }

    private String getCustomerRule() {
        String rule = "";
        if(StringUtil.isNotEmpty(this.ciCustomGroupInfo.getCustomOptRuleShow())) {
            rule = this.ciCustomGroupInfo.getCustomOptRuleShow();
        }

        if(StringUtil.isNotEmpty(this.ciCustomGroupInfo.getLabelOptRuleShow())) {
            rule = this.ciCustomGroupInfo.getLabelOptRuleShow();
        }

        if(StringUtil.isNotEmpty(this.ciCustomGroupInfo.getProdOptRuleShow())) {
            rule = this.ciCustomGroupInfo.getProdOptRuleShow();
        }

        if(StringUtil.isNotEmpty(this.ciCustomGroupInfo.getKpiDiffRule())) {
            rule = this.ciCustomGroupInfo.getKpiDiffRule();
        }

        if(rule.length() > 1000) {
            rule = rule.substring(0, 1000);
        }

        return rule;
    }

    public static void main(String[] args) {
        try {
            System.out.println(DES.encrypt("ci"));
            PowerShowRequestXmlBean e = new PowerShowRequestXmlBean();
            e.getTitle().setInterfaceAddress("10.,11..1.");
            e.getTitle().setSendTime(new Date());
            e.getData().setSyncTaskId("taskId");
            e.getData().setPlatformCode("COC");
            e.getData().setUploadFileName("djafdjajfsdlaf.zip");
            e.getData().setRowNumber(Long.valueOf("122222222222").toString());
            e.getData().setApplicant("admin");
            Calendar c = Calendar.getInstance();
            c.add(5, -3);
            e.getData().setExpireDate(DateUtil.date2String(c.getTime(), "yyyy-MM-dd"));
            e.getData().setLinkId("1");
            e.getData().setTarget("客户微分");
            com.ailk.biapp.ci.model.PowerShowRequestXmlBean.Column column = new com.ailk.biapp.ci.model.PowerShowRequestXmlBean.Column();
            column.setColumnName("手机号码");
            column.setColumnLength("11");
            e.getData().addColumn(column);
            String xmlStr = Bean2XMLUtils.bean2XmlString(e);
            System.out.println(xmlStr);
            FileUtil.writeByteFile(xmlStr.getBytes("UTF-8"), new File("xmlfile.xml"));
            CiCustomGroupInfo info = new CiCustomGroupInfo();
            info.setCustomGroupName("testttt");
            String res2 = BeanUtils.getProperty(info, "customGroupName");
            System.out.println(res2);
            String wsdl = "http://localhost:8080/COC/services/iCustomerMarketCampaignWsServer?wsdl";
            String targetNamespace = "http://webservice.ci.biapp.ailk.com/";
            String methodName = "sendCustomerMarketCampaignInfo";
            Object[] a = new Object[]{"<?xml version=\'1.0\' encoding=\'UTF-8\'?>                <Root><CreateCampaign><CampsegId>1000999</CampsegId><CampsegName>测试营销活动1</CampsegName><CustgroupId>CI_CUSER_20131024153054500</CustgroupId><CustgroupName>test客户群001</CustgroupName><CampsegCreateUser>admin</CampsegCreateUser><ApproveEndTime>2013-12-12 21:00:00</ApproveEndTime></CreateCampaign></Root>"};
            Object[] res = callWebService(wsdl, targetNamespace, methodName, a);
            System.out.println(res[0]);
            JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
            factoryBean.setServiceClass(ICustomerMarketCampaignWsServer.class);
            factoryBean.setAddress("http://localhost:8080/COC/services/iCustomerMarketCampaignWsServer");
            ICustomerMarketCampaignWsServer impl = (ICustomerMarketCampaignWsServer)factoryBean.create();
            String ab = impl.sendCustomerMarketCampaignInfo(a[0].toString());
            System.out.println(ab);
            JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
            Client client = dcf.createClient("http://localhost:8080/COC/services/iCustomerMarketCampaignWsServer?wsdl");
            Object[] objects = client.invoke("sendCustomerMarketCampaignInfo", new Object[]{a[0]});
            System.out.println(objects[0].toString());
        } catch (Exception var18) {
            var18.printStackTrace();
            log.error("", var18);
        }

    }

    private static Object[] callWebService(String wsdl, String targetNamespace, String methodName, Object[] args) {
        try {
            JaxWsDynamicClientFactory e = JaxWsDynamicClientFactory.newInstance();
            Client client = e.createClient(wsdl);
            Object[] res = null;
            if(targetNamespace == null) {
                res = client.invoke(methodName, args);
            } else {
                res = client.invoke(new QName(targetNamespace, methodName), args);
            }

            return res;
        } catch (Exception var7) {
            var7.printStackTrace();
            log.error("动态调用webservice 失败,wsdl:" + wsdl + ",targetNamespace:" + targetNamespace + ",methodName:" + methodName + ",args:" + args, var7);
            throw new CIServiceException("动态调用webservice 失败");
        }
    }

    public String toString() {
        return "CustomerPublishThread [reqId=" + this.reqId + "]";
    }

    private boolean publishToOldCi() {
        long t1 = System.currentTimeMillis();
        String exportSQL = Configure.getInstance().getProperty("OLD_CI_EXPORT_SQL");
        exportSQL = exportSQL.replace("${listTableName}", this.ciCustomListInfo.getListTableName());
        exportSQL = exportSQL.replace("${dataDate}", CacheBase.getInstance().getNewLabelMonth());
        log.debug("publishToOldCi ==>exportSQL: " + exportSQL);
        short pageSize = 10000;
        String currentSql = "";
        String province = Configure.getInstance().getProperty("PROVINCE");

        boolean oldciFront;
        try {
            JdbcBaseDao e = (JdbcBaseDao)SystemServiceLocator.getInstance().getService("jdbcBaseDao");
            SimpleJdbcTemplate var43 = e.getSimpleJdbcTemplate(Configure.getInstance().getProperty("OLD_CI_JNDI"));
            SimpleJdbcTemplate oldciBack = e.getSimpleJdbcTemplate(Configure.getInstance().getProperty("OLD_CI_BACK_JNDI"));
            DataBaseAdapter oldciBackDBAdapter = e.getDataBaseAdapter(Configure.getInstance().getProperty("OLD_CI_BACK_DBTYPE"));
            String oldCiTemplateTable = Configure.getInstance().getProperty("OLD_CI_TMP_TABLE");
            String oldCiDataTableName = oldCiTemplateTable.substring(0, oldCiTemplateTable.lastIndexOf("_")) + "_" + CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L);
            String nameWithSchema = oldCiDataTableName.toLowerCase();
            String oldCISchema = Configure.getInstance().getProperty("OLD_CI_BACK_SCHEMA");
            if(StringUtil.isNotEmpty(oldCISchema)) {
                nameWithSchema = oldCISchema + "." + nameWithSchema;
            }

            String createsql = oldciBackDBAdapter.getCreateAsTableSql(nameWithSchema, oldCiTemplateTable, Configure.getInstance().getProperty("OLD_CI_TABLESPACE_BACK"));
            log.debug("publishToOldCi createsql:" + createsql);
            oldciBack.getJdbcOperations().execute(createsql);
            String createsql2 = oldciBackDBAdapter.getCreateAsTableSql(nameWithSchema + "_tmp", oldCiTemplateTable, Configure.getInstance().getProperty("OLD_CI_TABLESPACE_BACK"));
            log.debug("publishToOldCi createsql:" + createsql2);
            oldciBack.getJdbcOperations().execute(createsql2);
            String oldciKeyCoulmn = Configure.getInstance().getProperty("OLD_CI_KEYCOLUMN");
            final String ciKeyCoulmn = Configure.getInstance().getProperty("RELATED_COLUMN");
            StringBuffer insertDataSqlBuf = new StringBuffer();
            insertDataSqlBuf.append("insert into ").append(nameWithSchema).append("_tmp").append(" (").append(oldciKeyCoulmn).append(") values(?)");
            String insertDataSql = insertDataSqlBuf.toString();
            log.debug("publishToOldCi insertDataSql:" + insertDataSql);
            int start = 1;

            String insertRelTableSql;
            while(true) {
                insertRelTableSql = e.getBackDataBaseAdapter().getPagedSql(exportSQL, start, pageSize);
                log.debug("publishToOldCi ==>pagedSQL: " + insertRelTableSql);
                final List dropTableSql = e.getBackSimpleJdbcTemplate().queryForList(insertRelTableSql, new Object[0]);
                if(dropTableSql == null) {
                    break;
                }

                oldciBack.getJdbcOperations().batchUpdate(insertDataSql, new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        String tempArr = (String)((Map)dropTableSql.get(i)).get(ciKeyCoulmn.toLowerCase());
                        ps.setString(1, tempArr);
                    }

                    public int getBatchSize() {
                        return dropTableSql.size();
                    }
                });
                if(dropTableSql.size() < pageSize) {
                    break;
                }

                ++start;
            }

            insertRelTableSql = Configure.getInstance().getProperty("OLD_CI_INSERT_DATA_SQL");
            insertRelTableSql = insertRelTableSql.replace("${ciListTableName}", nameWithSchema);
            insertRelTableSql = insertRelTableSql.replace("${ciListTableNameTmp}", nameWithSchema + "_tmp");
            insertRelTableSql = insertRelTableSql.replace("${dataDate}", CacheBase.getInstance().getNewLabelMonth());
            log.debug("publishToOldCi insertRelTableSql:" + insertRelTableSql);
            oldciBack.getJdbcOperations().execute(insertRelTableSql);
            String var44 = " drop table " + nameWithSchema + "_tmp";
            log.debug("publishToOldCi dropTableSql:" + var44);
            oldciBack.getJdbcOperations().execute(var44);
            String insetCusomerSql = Configure.getInstance().getProperty("OLD_CI_INSERT_GROUPINFO_SQL");
            StringBuffer desc = new StringBuffer();
            String customRules = (StringUtil.isEmpty(this.ciCustomGroupInfo.getCustomOptRuleShow())?"":this.ciCustomGroupInfo.getCustomOptRuleShow()) + (StringUtil.isEmpty(this.ciCustomGroupInfo.getKpiDiffRule())?"":this.ciCustomGroupInfo.getKpiDiffRule()) + (StringUtil.isEmpty(this.ciCustomGroupInfo.getProdOptRuleShow())?"":this.ciCustomGroupInfo.getProdOptRuleShow()) + (StringUtil.isEmpty(this.ciCustomGroupInfo.getLabelOptRuleShow())?"":this.ciCustomGroupInfo.getLabelOptRuleShow());
            desc.append("<br/>规则：" + customRules);
            desc.append("<br/>").append(StringUtil.isEmpty(this.ciCustomGroupInfo.getCustomGroupDesc())?"":this.ciCustomGroupInfo.getCustomGroupDesc());
            String usedLabelOrProduct = this.queryUsedLabelOrProduct(this.ciCustomGroupInfo);
            if(StringUtil.isEmpty(usedLabelOrProduct)) {
                usedLabelOrProduct = "该客户群创建过程中没有使用标签或者产品";
            }

            desc.append("<br/>使用的标签或者产品:").append(usedLabelOrProduct);
            String _desc = desc.toString();
            if(desc.length() > 1500) {
                _desc = _desc.substring(0, 1500);
            }

            log.debug("publishToOldCi insetCusomerSql:" + insetCusomerSql);

            try {
                String e1 = this.ciCustomListInfo.getDataDate();
                String baseMonth = DateUtil.dateFormat(e1);
                if(baseMonth.length() == "yyyy-MM".length()) {
                    baseMonth = baseMonth + "-01";
                }

                String baseDay = null;
                if(e1 != null && e1.length() == "yyyyMMdd".length()) {
                    baseDay = DateUtil.dateFormat(e1);
                }

                if("jilin".equals(province)) {
                    List cityList = PrivilegeServiceUtil.getUserCityIds(this.ciCustomGroupInfo.getCreateUserId());
                    StringBuffer cityBuf = new StringBuffer();
                    Iterator cityIds = cityList.iterator();

                    while(cityIds.hasNext()) {
                        String cityId = (String)cityIds.next();
                        cityBuf.append(cityId).append(",");
                    }

                    String var46 = cityBuf.substring(0, cityBuf.length() - 1);
                    baseDay = "";
                    var43.update(insetCusomerSql, new Object[]{CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L), this.ciCustomGroupInfo.getCustomGroupName() + this.ciCustomListInfo.getDataDate(), this.ciCustomGroupInfo.getCreateUserId(), new Date(), var46, this.ciCustomListInfo.getCustomNum(), baseMonth, baseDay, this.ciCustomGroupInfo.getCustomGroupDesc(), oldCiDataTableName.toLowerCase()});
                } else {
                    var43.update(insetCusomerSql, new Object[]{CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L), this.ciCustomGroupInfo.getCustomGroupName() + this.ciCustomListInfo.getDataDate(), this.ciCustomGroupInfo.getCreateUserId(), new Date(), this.ciCustomListInfo.getCustomNum(), baseMonth, baseDay == null?"":baseDay, this.ciCustomGroupInfo.getCustomGroupDesc(), oldCiDataTableName, "由COC推送来的客户群", _desc, this.ciCustomGroupInfo.getCreateTime()});
                }
            } catch (Exception var40) {
                log.error("publishToOldCi error:", var40);
                if(this.isExistsSame()) {
                    this.ciCustomPushReq.setExeInfo("已经推送过的客户群清单");
                    this.delNormal(3);
                } else {
                    this.delException(var44, var40);
                }
            }

            boolean var45 = true;
            return var45;
        } catch (Exception var41) {
            log.error("publishToOldCi( ) error:", var41);
            this.delException(currentSql, var41);
            oldciFront = false;
        } finally {
            log.info("The cost of publishToOldCi is :  " + (System.currentTimeMillis() - t1) + "ms");
        }

        return oldciFront;
    }

    public String getPushFailReason() {
        return this.pushFailReason;
    }

    public void setPushFailReason(String pushFailReason) {
        this.pushFailReason = pushFailReason;
    }
}
