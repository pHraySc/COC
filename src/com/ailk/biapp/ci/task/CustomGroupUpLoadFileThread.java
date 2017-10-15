package com.ailk.biapp.ci.task;

import au.com.bytecode.opencsv.CSVReader;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.entity.CiCustomFileRel;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.entity.CiPersonNotice;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.ReturnMessageModel;
import com.ailk.biapp.ci.service.ICiCustomFileRelService;
import com.ailk.biapp.ci.service.ICiPersonNoticeService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.task.CiProductMatchThread;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.ThreadPool;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.ailk.biapp.ci.webservice.ICustomersPushedClient;
import com.ailk.biapp.ci.webservice.IIndexDifferentialClient;
import com.ailk.biapp.ci.webservice.impl.ICustomersPushedClientImpl;
import com.ailk.biapp.ci.webservice.impl.WsIndexDifferentialClientImpl;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.privilege.IUserSession;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Scope("prototype")
public class CustomGroupUpLoadFileThread extends Thread {
    private final Logger log = Logger.getLogger(CustomGroupUpLoadFileThread.class);
    private static String checkNumberFormat = Configure.getInstance().getProperty("CHECK_NUMBER_FORMAT");
    private CiCustomFileRel ciCustomFileRel;
    private ReturnMessageModel returnMessageModel;
    private CiCustomGroupInfo customGroupInfo;
    private Map<String, Integer> importResult;
    private IUserSession userSession;
    private boolean hasTitle;
    private List<CiGroupAttrRel> ciGroupAttrRelList;
    @Autowired
    private ICustomersManagerService customersService;
    @Autowired
    private ICiCustomFileRelService customFileRelService;
    @Autowired
    ICiPersonNoticeService ciPersonNoticeService;
    @Autowired
    private IIndexDifferentialClient wsIndexDifferentialClient;
    @Autowired
    private ICustomersPushedClient customersPushedClient;

    public CustomGroupUpLoadFileThread() {
    }

    public void saveCustomFileRel() throws CIServiceException {
        try {
            this.customFileRelService.saveCustomFileRel(this.ciCustomFileRel);
        } catch (Exception var2) {
            this.log.error("", var2);
            throw new CIServiceException("保存客户群文件失败");
        }
    }

    public CiCustomFileRel queryCustomFile(String fileId) throws CIServiceException {
        CiCustomFileRel customFile = null;

        try {
            customFile = this.customFileRelService.queryCustomFile(fileId);
            return customFile;
        } catch (Exception var4) {
            this.log.error("", var4);
            throw new CIServiceException("查询客户群文件失败");
        }
    }

    public void updateCustomGroupInfo(int customNum, String listTableName) throws Exception {
        String customGroupId = this.ciCustomFileRel.getCustomGroupId();
        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
        List customListInfoList = this.customersService.queryCiCustomListInfoByCGroupId(customGroupId);
        if(customGroupInfo != null && customListInfoList != null && customListInfoList.size() > 0) {
            CiCustomListInfo customListInfo = (CiCustomListInfo)customListInfoList.get(0);
            customListInfo.setCustomNum(Long.valueOf((long)customNum));
            customListInfo.setListTableName(listTableName);
            String dataDate = CacheBase.getInstance().getNewLabelDay();
            if(customListInfo.getDataDate() == null || "".equals(customListInfo.getDataDate())) {
                customListInfo.setDataDate(dataDate);
            }

            if(customNum == 0) {
                customListInfo.setDataStatus(Integer.valueOf(0));
                customGroupInfo.setDataStatus(Integer.valueOf(0));
            } else {
                customListInfo.setDataStatus(Integer.valueOf(3));
                customGroupInfo.setDataStatus(Integer.valueOf(3));
            }

            this.customersService.syncUpdateCiCustomListInfo(customListInfo);
            customGroupInfo.setDataTime(new Date());
            customGroupInfo.setDataDate(customListInfo.getDataDate());
            customGroupInfo.setMonthLabelDate(customListInfo.getMonthLabelDate());
            customGroupInfo.setCustomNum(customListInfo.getCustomNum());
            this.customersService.syncUpdateCiCustomGroupInfo(customGroupInfo);
            if(customGroupInfo.getProductAutoMacthFlag() != null && customGroupInfo.getProductAutoMacthFlag().intValue() == 1) {
                customGroupInfo.setProductAutoMacthFlag(Integer.valueOf(customGroupInfo.getProductAutoMacthFlag().intValue() + 2));
                this.customersService.syncUpdateCiCustomGroupInfo(customGroupInfo);
                CiProductMatchThread matcher = (CiProductMatchThread)SystemServiceLocator.getInstance().getService("ciProductMatchThread");
                matcher.setCiCustomListInfo(customListInfo);
                ThreadPool.getInstance().execute(matcher, true);
            }
        }

    }

    public void setCiCustomFileRel(CiCustomFileRel ciCustomFileRel) {
        this.ciCustomFileRel = ciCustomFileRel;
    }

    public void run() {
        String listTableNameTmp = "";
        boolean success = false;
        String customGroupId = null;
        this.importResult = new HashMap();

        try {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException var28) {
                this.log.error("InterruptedException", var28);
            }

            this.saveCustomFileRel();
            long e = System.currentTimeMillis();
            this.log.debug(">>CustomGroupUpLoadFileThread begining at " + CiUtil.convertLongMillsToStrTime(e));
            customGroupId = this.ciCustomFileRel.getCustomGroupId();
            List customListInfoList = this.customersService.queryCiCustomListInfoByCGroupId(customGroupId);
            this.customGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
            this.log.debug(">>customListInfoList size " + customListInfoList.size());
            String templetListTableName = Configure.getInstance().getProperty("CUST_LIST_TMP_TABLE");
            if(customListInfoList == null || customListInfoList.size() == 0) {
                this.log.debug(">> customListInfoList is null !");
                return;
            }

            CiCustomListInfo column = (CiCustomListInfo)customListInfoList.get(0);
            if(column != null && StringUtils.isNotBlank(column.getListTableName())) {
                listTableNameTmp = column.getListTableName() + "_tmp";
                String var31 = Configure.getInstance().getProperty("RELATED_COLUMN");
                this.log.debug(">>>>>>create temp table:" + listTableNameTmp);
                this.customFileRelService.createTable(listTableNameTmp, templetListTableName, var31, this.ciGroupAttrRelList);
                this.customFileRelService.addAttr2TmpTable(listTableNameTmp, this.ciGroupAttrRelList);
                StringBuffer columns = new StringBuffer(var31);
                int customNum;
                if(this.ciGroupAttrRelList != null && this.ciGroupAttrRelList.size() > 0) {
                    columns.append(",");

                    for(customNum = 0; customNum < this.ciGroupAttrRelList.size(); ++customNum) {
                        columns.append(((CiGroupAttrRel)this.ciGroupAttrRelList.get(customNum)).getId().getAttrCol());
                        if(customNum < this.ciGroupAttrRelList.size() - 1) {
                            columns.append(",");
                        }
                    }
                }

                customNum = this.dealImportFile(listTableNameTmp, columns.toString());
                String listTableName = listTableNameTmp.replace("_tmp", "");
                this.ciCustomFileRel.setFileEndTime(new Timestamp((new Date()).getTime()));
                this.saveCustomFileRel();
                this.updateCustomGroupInfo(customNum, listTableName);
                success = true;
                if(customNum == 0) {
                    success = false;
                }

                return;
            }

            this.log.debug(">> customListInfo.getListTableName is null !");
        } catch (Exception var29) {
            this.log.error("导入错误", var29);
            success = false;
            throw new CIServiceException("导入报错");
        } finally {
            if(StringUtil.isNotEmpty(listTableNameTmp)) {
                try {
                    this.customFileRelService.deleteAllData(listTableNameTmp);
                } catch (Exception var27) {
                    this.log.error("删除临时表报错 ", var27);
                }
            }

            CiPersonNotice personNotice = new CiPersonNotice();
            personNotice.setStatus(Integer.valueOf(1));
            personNotice.setCustomerGroupId(customGroupId);
            personNotice.setNoticeName(this.customGroupInfo.getCustomGroupName() + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_PUBLISH_CUSTOMERS_GENERATE);
            personNotice.setNoticeSendTime(new Date());
            personNotice.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_PUBLISH_CUSTOMERS_GENERATE));
            personNotice.setReadStatus(Integer.valueOf(1));
            personNotice.setReceiveUserId(this.customGroupInfo.getCreateUserId());
            if(this.returnMessageModel != null && StringUtil.isNotEmpty(this.returnMessageModel.getSyncTaskId())) {
                if(11 == this.customGroupInfo.getCreateTypeId().intValue()) {
                    personNotice.setNoticeName(this.customGroupInfo.getCustomGroupName() + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_OTHERSY_PUBLISH_CUSTOMERS_GENERATE);
                    personNotice.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_OTHERSY_PUBLISH_CUSTOMERS_GENERATE));
                }

                SimpleDateFormat var33 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String var35 = this.returnMessageModel.getInterfaceAddress();
                var35 = var35.replace("http://", "");
                String clientIP = var35.substring(0, var35.indexOf("/"));
                this.log.debug("sessionId: " + this.returnMessageModel.getSyncTaskId() + "; userID: " + this.customGroupInfo.getCreateUserId() + "; userName: " + this.customGroupInfo.getCreateUserName() + "; clientIP: " + clientIP + "; resourceId: " + this.customGroupInfo.getCustomGroupId() + "; resourceName: " + this.customGroupInfo.getCustomGroupName());
                String xmlBody;
                if(success) {
                    if(11 == this.customGroupInfo.getCreateTypeId().intValue()) {
                        personNotice.setNoticeDetail("客户群：" + this.customGroupInfo.getCustomGroupName() + "创建成功！（来自外部系统推送）");
                    } else {
                        personNotice.setNoticeDetail("指标微分创建客户群：" + this.customGroupInfo.getCustomGroupName() + "成功！（来自自助分析平台）");
                    }

                    personNotice.setIsSuccess(Integer.valueOf(1));
                    this.ciPersonNoticeService.addPersonNotice(personNotice);
                    this.returnMessageModel.setReturnCode("1");
                    this.returnMessageModel.setErrorMsg("");
                    xmlBody = var33.format(new Date());
                    this.returnMessageModel.setSendTime(xmlBody);
                    if(11 == this.customGroupInfo.getCreateTypeId().intValue()) {
                        CILogServiceUtil.getLogServiceInstance().log(this.returnMessageModel.getSyncTaskId(), this.customGroupInfo.getCreateUserId(), this.customGroupInfo.getCreateUserName(), clientIP, "COC_INDEX_DIFFERENTIAL", this.customGroupInfo.getCustomGroupId(), this.customGroupInfo.getCustomGroupName(), "客户群【" + this.customGroupInfo.getCustomGroupName() + "】成功", OperResultEnum.Success, LogLevelEnum.Medium);
                    } else {
                        CILogServiceUtil.getLogServiceInstance().log(this.returnMessageModel.getSyncTaskId(), this.customGroupInfo.getCreateUserId(), this.customGroupInfo.getCreateUserName(), clientIP, "COC_INDEX_DIFFERENTIAL", this.customGroupInfo.getCustomGroupId(), this.customGroupInfo.getCustomGroupName(), "指标微分客户群【" + this.customGroupInfo.getCustomGroupName() + "】成功", OperResultEnum.Success, LogLevelEnum.Medium);
                    }
                } else {
                    if(11 == this.customGroupInfo.getCreateTypeId().intValue()) {
                        personNotice.setNoticeDetail("客户群: " + this.customGroupInfo.getCustomGroupName() + "创建失败！（来自外部系统推送）");
                    } else {
                        personNotice.setNoticeDetail("指标微分创建客户群：" + this.customGroupInfo.getCustomGroupName() + "失败！（来自自助分析平台）");
                    }

                    personNotice.setIsSuccess(Integer.valueOf(0));
                    this.ciPersonNoticeService.addPersonNotice(personNotice);
                    this.returnMessageModel.setReturnCode("0");
                    if(StringUtil.isEmpty(this.returnMessageModel.getErrorMsg())) {
                        this.returnMessageModel.setErrorMsg("导入报错");
                    }

                    xmlBody = var33.format(new Date());
                    this.returnMessageModel.setSendTime(xmlBody);
                    if(11 == this.customGroupInfo.getCreateTypeId().intValue()) {
                        CILogServiceUtil.getLogServiceInstance().log(this.returnMessageModel.getSyncTaskId(), this.customGroupInfo.getCreateUserId(), this.customGroupInfo.getCreateUserName(), clientIP, "COC_INDEX_DIFFERENTIAL", "-1", this.customGroupInfo.getCustomGroupName(), "客户群【" + this.customGroupInfo.getCustomGroupName() + "】失败", OperResultEnum.Failure, LogLevelEnum.Medium);
                    } else {
                        CILogServiceUtil.getLogServiceInstance().log(this.returnMessageModel.getSyncTaskId(), this.customGroupInfo.getCreateUserId(), this.customGroupInfo.getCreateUserName(), clientIP, "COC_INDEX_DIFFERENTIAL", "-1", this.customGroupInfo.getCustomGroupName(), "指标微分客户群【" + this.customGroupInfo.getCustomGroupName() + "】失败", OperResultEnum.Failure, LogLevelEnum.Medium);
                    }
                }

                String res;
                if(11 == this.customGroupInfo.getCreateTypeId().intValue()) {
                    xmlBody = ICustomersPushedClientImpl.createXmlBody(this.returnMessageModel);
                    res = null;
                    if(StringUtil.isNotEmpty(xmlBody)) {
                        res = this.customersPushedClient.sendDataToSA(xmlBody);
                        this.log.debug("外部系统推送客户群后，webservice返回信息：" + res);
                    }
                } else {
                    xmlBody = WsIndexDifferentialClientImpl.createXmlBody(this.returnMessageModel);
                    res = null;
                    if(StringUtil.isNotEmpty(xmlBody)) {
                        res = this.wsIndexDifferentialClient.sendDataToSA(xmlBody);
                        this.log.debug("指标微分后，webservice返回信息：" + res);
                    }
                }
            } else if(success) {
                StringBuffer t2 = new StringBuffer();
                t2.append("导入方式创建客户群：" + this.customGroupInfo.getCustomGroupName() + "成功！").append("文件总记录数：").append(this.importResult.get("CUSTOM_IMPORT_TOTAL")).append("条；").append("导入成功数：").append(this.importResult.get("CUSTOM_IMPORT_SUCCESS")).append("条；").append("不符合格式记录数：").append(this.importResult.get("CUSTOM_IMPORT_FAIL")).append("条。");
                this.log.info("personNotice:" + t2.toString());
                personNotice.setNoticeDetail(t2.toString());
                personNotice.setIsSuccess(Integer.valueOf(1));
                this.ciPersonNoticeService.addPersonNotice(personNotice);
                CILogServiceUtil cILogServiceUtil = CILogServiceUtil.getLogServiceInstance();
                cILogServiceUtil.addUserSession(this.userSession);
                cILogServiceUtil.log("COC_CUSTOMER_MANAGE_IMPORT", this.customGroupInfo.getCustomGroupId(), this.customGroupInfo.getCustomGroupName(), "导入客户群【" + this.customGroupInfo.getCustomGroupName() + "】成功", OperResultEnum.Success, LogLevelEnum.Risk);
            } else {
                personNotice.setNoticeDetail("导入方式创建客户群：" + this.customGroupInfo.getCustomGroupName() + "失败！");
                personNotice.setIsSuccess(Integer.valueOf(0));
                this.ciPersonNoticeService.addPersonNotice(personNotice);
                CILogServiceUtil var32 = CILogServiceUtil.getLogServiceInstance();
                var32.addUserSession(this.userSession);
                var32.log("COC_CUSTOMER_MANAGE_IMPORT", "-1", this.customGroupInfo.getCustomGroupName(), "导入客户群失败", OperResultEnum.Failure, LogLevelEnum.Risk);
            }

            long var34 = System.currentTimeMillis();
            this.log.debug(">>CustomGroupUpLoadFileThread end at " + CiUtil.convertLongMillsToStrTime(var34));
        }

    }

    private int dealImportFile(String listTableNameTmp, String columns) {
        byte resultNum = 0;

        try {
            String e = this.ciCustomFileRel.getFileUrl();
            this.log.info(">>begin loading " + e + " at " + CiUtil.convertLongMillsToStrTime(System.currentTimeMillis()));
            int loadedRowNum = 0;
            if(e.toLowerCase().endsWith("txt") || e.toLowerCase().endsWith("csv")) {
                loadedRowNum = this.dealImportFileData2Db(listTableNameTmp, columns);
            }

            this.log.debug(">>end load " + e + " at " + CiUtil.convertLongMillsToStrTime(System.currentTimeMillis()));
            if(loadedRowNum == -1) {
                return resultNum;
            } else {
                int resultNum1 = this.batchInsert2CustListTab(listTableNameTmp, columns);
                CiUtil.deleteFile(e);
                this.log.debug(">>LoadCustomGroupFileServiceImpl end at " + CiUtil.convertLongMillsToStrTime(System.currentTimeMillis()));
                return resultNum1;
            }
        } catch (Exception var6) {
            this.log.error("dealImportFile", var6);
            throw new CIServiceException("导入数据报错");
        }
    }

    private int batchInsert2CustListTab(String listTableNameTmp, String column) throws Exception {
        String listTableName = listTableNameTmp.replace("_tmp", "");
        this.customFileRelService.createTable(listTableName, listTableNameTmp, column, (List)null);
        return this.customFileRelService.batchInsert2CustListTab(listTableName, listTableNameTmp, column);
    }

    private int dealImportFileData2Db(String tabName, String insertColumn) throws Exception {
        this.log.info("begin dealImportFileData2Db ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        CSVReader reader = null;
        ArrayList batchList = new ArrayList();
        int totalCount = 0;
        int currentRowNum = 0;
        int failRowNum = 0;
        int successRowNum = 0;
        String reg = "";
        Charset charset = CiUtil.getFileCharset(new File(this.ciCustomFileRel.getFileUrl()));
        reader = new CSVReader(new InputStreamReader(new FileInputStream(new File(this.ciCustomFileRel.getFileUrl())), charset));
        boolean firstLine = true;

        label158: {
            byte pattern;
            try {
                String[] nextLine;
                while((nextLine = reader.readNext()) != null) {
                    if(firstLine && this.hasTitle) {
                        firstLine = false;
                    } else {
                        String[] e = new String[this.getCiGroupAttrRelList().size() + 1];

                        for(int var22 = 0; var22 < this.getCiGroupAttrRelList().size() + 1; ++var22) {
                            if(nextLine.length > var22) {
                                if(var22 == 0) {
                                    e[var22] = nextLine[var22].trim();
                                } else {
                                    e[var22] = nextLine[var22];
                                }
                            } else {
                                e[var22] = null;
                            }
                        }

                        ++totalCount;
                        if(nextLine[0].startsWith("#")) {
                            ++failRowNum;
                        } else {
                            if("true".equals(checkNumberFormat)) {
                                reg = "^[0-9]{11}$";
                                Pattern var23 = Pattern.compile(reg);
                                Matcher matcher = var23.matcher(nextLine[0]);
                                if(!matcher.matches()) {
                                    this.log.info(nextLine[0] + "is not format");
                                    ++failRowNum;
                                    continue;
                                }
                            }

                            ++currentRowNum;
                            ++successRowNum;
                            batchList.add(e);
                            if(currentRowNum % 1000000 == 0) {
                                this.customFileRelService.batchUpdateMobileList(batchList, tabName, insertColumn);
                                batchList.clear();
                                currentRowNum = 0;
                            }
                        }
                    }
                }

                if(currentRowNum > 0) {
                    this.customFileRelService.batchUpdateMobileList(batchList, tabName, insertColumn);
                    batchList.clear();
                }
                break label158;
            } catch (Exception var20) {
                this.log.error("dealImportFileData2Db error:", var20);
                pattern = -1;
            } finally {
                if(reader != null) {
                    reader.close();
                    reader = null;
                }

            }

            return pattern;
        }

        this.importResult.put("CUSTOM_IMPORT_SUCCESS", Integer.valueOf(successRowNum));
        this.importResult.put("CUSTOM_IMPORT_FAIL", Integer.valueOf(failRowNum));
        this.importResult.put("CUSTOM_IMPORT_TOTAL", Integer.valueOf(totalCount));
        this.log.info("successRowNum:" + successRowNum + ";failRowNum:" + failRowNum + ";totalCount:" + totalCount);
        this.log.info("end dealImportFileData2Db ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        return currentRowNum;
    }

    public CiCustomFileRel queryCustomFileByCustomGroupId(String customGroupId) throws CIServiceException {
        CiCustomFileRel ciCustomFileRel = null;

        try {
            ciCustomFileRel = this.customFileRelService.queryCustomFileByCustomGroupId(customGroupId);
            return ciCustomFileRel;
        } catch (Exception var5) {
            String message = "查询客户群导入的文件失败";
            this.log.error(message, var5);
            throw new CIServiceException(message, var5);
        }
    }

    public ReturnMessageModel getReturnMessageModel() {
        return this.returnMessageModel;
    }

    public void setReturnMessageModel(ReturnMessageModel returnMessageModel) {
        this.returnMessageModel = returnMessageModel;
    }

    public CiCustomGroupInfo getCustomGroupInfo() {
        return this.customGroupInfo;
    }

    public void setCustomGroupInfo(CiCustomGroupInfo customGroupInfo) {
        this.customGroupInfo = customGroupInfo;
    }

    public String toString() {
        return "CustomGroupUpLoadFileThread [customGroupId=" + this.ciCustomFileRel.getCustomGroupId() + "]";
    }

    public void setUserSession(IUserSession userSession) {
        this.userSession = userSession;
    }

    public List<CiGroupAttrRel> getCiGroupAttrRelList() {
        return this.ciGroupAttrRelList;
    }

    public void setCiGroupAttrRelList(List<CiGroupAttrRel> ciGroupAttrRelList) {
        this.ciGroupAttrRelList = ciGroupAttrRelList;
    }

    public boolean isHasTitle() {
        return this.hasTitle;
    }

    public void setHasTitle(boolean hasTitle) {
        this.hasTitle = hasTitle;
    }
}
