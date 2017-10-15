package com.ailk.biapp.ci.webservice.impl;

import com.ailk.biapp.ci.dao.ICiSysInfoHDao;
import com.ailk.biapp.ci.entity.CiCustomFileRel;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiSysInfo;
import com.ailk.biapp.ci.entity.CiTemplateInfo;
import com.ailk.biapp.ci.exception.*;
import com.ailk.biapp.ci.model.Column;
import com.ailk.biapp.ci.model.ReturnMessageModel;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.task.CustomGroupUpLoadFileThread;
import com.ailk.biapp.ci.util.FileUtil;
import com.ailk.biapp.ci.util.FtpUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.ThreadPool;
import com.ailk.biapp.ci.util.ZipUtils;
import com.ailk.biapp.ci.webservice.ICustomersPushedServer;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.DES;
import com.asiainfo.biframe.utils.string.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.jws.WebService;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.cxf.feature.Features;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@WebService(
    endpointInterface = "com.ailk.biapp.ci.webservice.ICustomersPushedServer"
)
@Features(
    features = {"org.apache.cxf.feature.LoggingFeature"}
)
public class WsCustomersPushedServerImpl implements ICustomersPushedServer {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ICiSysInfoHDao ciSysInfoHDao;
    @Autowired
    private ICustomersManagerService customersService;

    public WsCustomersPushedServerImpl() {
    }

    public String getDataFromOtherSys(String xmlBody) {
        this.log.info("从SA接收XML数据并解析");
        String interfaceAddress = null;
        String sendTime = null;
        String syncTaskId = null;
        String taskId = null;
        String extSysSyncId = null;
        String taskName = null;
        String uploadFileName = null;
        String rowNumber = null;
        String applicant = null;
        String whereExplain = null;
        String taskDesc = null;
        ArrayList colList = new ArrayList();
        String returnCode = null;
        String errorMsg = null;

        try {
            SAXReader logForXmlData = new SAXReader();
            Document localFileUrl = logForXmlData.read(new StringReader(xmlBody));
            Element sysId = localFileUrl.getRootElement();
            Element CiSysInfo = sysId.element("Title");
            interfaceAddress = CiSysInfo.elementTextTrim("InterfaceAddress");
            sendTime = CiSysInfo.elementTextTrim("SendTime");
            Element groupInfo = sysId.element("Data");
            syncTaskId = groupInfo.elementTextTrim("SyncTaskId");
            taskId = groupInfo.elementTextTrim("TaskId");
            extSysSyncId = groupInfo.elementTextTrim("ExtSysSyncId");
            taskName = groupInfo.elementTextTrim("TaskName");
            uploadFileName = groupInfo.elementTextTrim("UploadFileName");
            rowNumber = groupInfo.elementTextTrim("RowNumber");
            applicant = groupInfo.elementTextTrim("Applicant");
            whereExplain = groupInfo.elementTextTrim("WhereExplain");
            taskDesc = groupInfo.elementTextTrim("TaskDesc");
            Iterator returnMessageModel = groupInfo.elementIterator("Columns");
            if(returnMessageModel.hasNext()) {
                Element interfaceAddressForSA = (Element)returnMessageModel.next();
                Iterator e = interfaceAddressForSA.elementIterator("Column");

                while(e.hasNext()) {
                    Element columnEle = (Element)e.next();
                    String columnName = columnEle.elementTextTrim("ColumnName");
                    Column column = new Column();
                    column.setColumnName(columnName);
                    column.setColumnType(columnEle.elementTextTrim("ColumnType"));
                    colList.add(column);
                }
            }
        } catch (Exception var30) {
            this.log.error("解析XML错误", var30);
            errorMsg = "解析XML错误";
            returnCode = "0";
            var30.printStackTrace();
        }

        if(StringUtil.isEmpty(syncTaskId)) {
            errorMsg = "XML中没有webservice交互ID";
            this.log.error(errorMsg);
            returnCode = "0";
            return returnCode;
        } else if(StringUtil.isEmpty(taskId)) {
            errorMsg = "XML中没有任务ID";
            this.log.error(errorMsg);
            returnCode = "0";
            return returnCode;
        } else {
            returnCode = "1";
            this.log.debug("解析XML成功");
            String logForXmlData1 = "interfaceAddress=[" + interfaceAddress + "]," + "sendTime=[" + sendTime + "]," + "syncTaskId=[" + syncTaskId + "]," + "taskId=[" + taskId + "]," + "extSysSyncId=[" + extSysSyncId + "]," + "taskName=[" + taskName + "]," + "uploadFileName=[" + uploadFileName + "]," + "rowNumber=[" + rowNumber + "]," + "applicant=[" + applicant + "]";
            this.log.debug(logForXmlData1);
            if(StringUtil.isEmpty(applicant)) {
                errorMsg = "没有操作人ID";
                this.log.error(errorMsg);
                returnCode = "0";
                return returnCode;
            } else {
                if(StringUtil.isNotEmpty(applicant)) {
                    applicant = applicant.toLowerCase();
                }

                String localFileUrl1 = null;
                String sysId1 = Configure.getInstance().getProperty("SA_FOR_MCD");
                if(StringUtil.isEmpty(sysId1)) {
                    errorMsg = "没有配置从SA获得信息的系统ID";
                    this.log.error(errorMsg);
                    returnCode = "0";
                    return returnCode;
                } else {
                    CiSysInfo CiSysInfo1 = null;

                    try {
                        CiSysInfo1 = this.ciSysInfoHDao.selectById(sysId1);
                        localFileUrl1 = this.dealFile(CiSysInfo1, uploadFileName);
                    } catch (CIServiceException var29) {
                        this.log.error(var29.getMessage());
                        errorMsg = var29.getMessage();
                        returnCode = "0";
                    }

                    if(StringUtil.isEmpty(localFileUrl1)) {
                        errorMsg = "没有生成本地文件！";
                        this.log.error(errorMsg);
                        returnCode = "0";
                    }

                    CiCustomGroupInfo groupInfo1 = new CiCustomGroupInfo();
                    groupInfo1.setCustomGroupName("SA推送_" + syncTaskId);
                    groupInfo1.setCustomGroupDesc(taskDesc);
                    groupInfo1.setKpiDiffRule(whereExplain);
                    groupInfo1.setCreateTime(new Timestamp((new Date()).getTime()));
                    groupInfo1.setCreateTypeId(Integer.valueOf(11));
                    groupInfo1.setCustomNum(Long.valueOf((long)Integer.parseInt(rowNumber)));
                    groupInfo1.setUpdateCycle(Integer.valueOf(1));
                    groupInfo1.setCreateUserName((String)null);
                    groupInfo1.setUpdateCycleStr((String)null);

                    try {
                        groupInfo1.setCreateUserName(PrivilegeServiceUtil.getUserById(applicant).getUsername());
                    } catch (Exception var28) {
                        errorMsg = "找不着对应的操作员";
                        this.log.error(errorMsg);
                        returnCode = "0";
                        this.log.error("根据登陆用户ID获取用户名称失败", var28);
                    }

                    groupInfo1.setCreateUserId(applicant);
                    if("0".equals(returnCode)) {
                        return returnCode;
                    } else {
                        ReturnMessageModel returnMessageModel1 = new ReturnMessageModel();
                        returnMessageModel1.setSyncTaskId(syncTaskId);
                        returnMessageModel1.setErrorMsg(errorMsg);
                        String interfaceAddressForSA1 = CiSysInfo1.getWebserviceWSDL().replace("?wsdl", "");
                        returnMessageModel1.setInterfaceAddress(interfaceAddressForSA1);

                        try {
                            this.importCustomListFile(localFileUrl1, applicant, groupInfo1, (CiCustomListInfo)null, returnMessageModel1);
                        } catch (Exception var27) {
                            this.log.error("客户清单数据导入数据库失败", var27);
                        }

                        return returnCode;
                    }
                }
            }
        }
    }

    private void importCustomListFile(String localFileUrl, String userId, CiCustomGroupInfo groupInfo, CiCustomListInfo customListInfo, ReturnMessageModel returnMessageModel) {
        this.log.info("Begin import customer list from SA");

        try {
            CiCustomFileRel e = new CiCustomFileRel();
            boolean customCreateSuccess = false;
            File file = new File(localFileUrl);
            String newName = file.getName();
            String ext = newName.substring(newName.lastIndexOf(".") + 1, newName.length());
            if(file != null) {
                customCreateSuccess = this.customersService.addCiCustomGroupInfo(groupInfo, (List)null, (List)null, (CiTemplateInfo)null, userId, false, (List)null);
                String customGroupId = groupInfo.getCustomGroupId();
                e.setCustomGroupId(customGroupId);
                e.setFileUrl(localFileUrl);
                e.setFileName(newName);
                e.setFileType(ext);
                e.setFileStartTime(new Timestamp((new Date()).getTime()));
                if(customCreateSuccess) {
                    CustomGroupUpLoadFileThread upLoadThread = null;

                    try {
                        upLoadThread = (CustomGroupUpLoadFileThread)SystemServiceLocator.getInstance().getService("customGroupUpLoadFileThread");
                        upLoadThread.setCiCustomFileRel(e);
                        upLoadThread.setReturnMessageModel(returnMessageModel);
                        upLoadThread.setCustomGroupInfo(groupInfo);
                        ThreadPool.getInstance().execute(upLoadThread);
                    } catch (Exception var14) {
                        this.log.error("SA推送客户群异常", var14);
                    }
                }
            }
        } catch (Exception var15) {
            this.log.error("SA返回清单创建客户群异常", var15);
        }

        this.log.info("End import customer list from SA");
    }

    private String dealFile(CiSysInfo ciSysInfo, String uploadFileName) throws CIServiceException {
        FtpUtil ftpUtil = new FtpUtil();
        String localFileUrl = null;

        try {
            if(!StringUtil.isEmpty(ciSysInfo) && !StringUtil.isEmpty(uploadFileName)) {
                if(!this.connectToFtpServer(ciSysInfo, ftpUtil)) {
                    throw new CIServiceException("连接FTP失败[ip=" + ciSysInfo.getFtpServerIp() + ";user=" + ciSysInfo.getFtpUser() + ";path=" + ciSysInfo.getFtpPath() + "]");
                } else if(!this.checkNameExist(uploadFileName, ftpUtil)) {
                    throw new CIServiceException("FTP中没有找到文件[fileName=" + uploadFileName + "]");
                } else {
                    String e = ciSysInfo.getLocalPath();
                    localFileUrl = e + System.getProperty("file.separator") + uploadFileName;
                    if(this.downloadFileFromFtp(uploadFileName, localFileUrl, ftpUtil)) {
                        String zippedFileUrl = this.unzipFileForFtpDownLoad(uploadFileName, localFileUrl, e, ftpUtil);
                        localFileUrl = zippedFileUrl;
                        if(StringUtil.isEmpty(zippedFileUrl)) {
                            throw new CIServiceException("解压本地文件错误[zipfile=" + zippedFileUrl + "]!");
                        }
                    } else {
                        localFileUrl = null;
                    }

                    return localFileUrl;
                }
            } else {
                throw new CIServiceException("没有找到FTP环境");
            }
        } catch (Exception var7) {
            this.log.error(var7.getMessage());
            throw new CIServiceException(var7.getMessage());
        }
    }

    private boolean connectToFtpServer(CiSysInfo ciSysInfo, FtpUtil ftpUtil) {
        int port = NumberUtils.toInt(ciSysInfo.getFtpPort(), 21);

        try {
            String e = DES.decrypt(ciSysInfo.getFtpPwd());
            return ftpUtil.connectServer(ciSysInfo.getFtpServerIp(), port, ciSysInfo.getFtpUser(), e, ciSysInfo.getFtpPath());
        } catch (SocketException var5) {
            this.log.error("连接Ftp，Socket异常", var5);
            return false;
        } catch (IOException var6) {
            this.log.error("连接Ftp，IO异常", var6);
            return false;
        } catch (Exception var7) {
            this.log.error("连接异常", var7);
            return false;
        }
    }

    private boolean checkNameExist(String fileName, FtpUtil ftpUtil) {
        try {
            long e1 = -1L;
            e1 = ftpUtil.checkFile(fileName);
            return e1 != -1L;
        } catch (IOException var5) {
            this.log.error("", var5);
            ftpUtil.closeServer();
            return false;
        }
    }

    private boolean downloadFileFromFtp(String fileName, String localFileUrl, FtpUtil ftpUtil) {
        boolean downLoadResponse = false;

        try {
            downLoadResponse = ftpUtil.download(fileName, localFileUrl);
        } catch (IOException var6) {
            this.log.error("FTP下载失败", var6);
            ftpUtil.closeServer();
        }

        return downLoadResponse;
    }

    private String unzipFileForFtpDownLoad(String fileName, String localFileUrl, String uploadFilePath, FtpUtil ftpUtil) {
        String zippedFileUrl = "";
        if(fileName.toLowerCase().endsWith(".zip")) {
            try {
                zippedFileUrl = ZipUtils.unzipSingleFile(localFileUrl, uploadFilePath);

                try {
                    FileUtil.deleteFile(localFileUrl, true);
                } catch (Exception var7) {
                    throw new CIServiceException("删除文件错误", var7);
                }
            } catch (IOException var8) {
                this.log.error("解压文件 " + fileName + " 失败", var8);
                ftpUtil.closeServer();
                throw new CIServiceException("解压文件 " + fileName + " 失败", var8);
            }
        } else if(!fileName.toLowerCase().endsWith(".txt") && !fileName.toLowerCase().endsWith(".csv")) {
            ftpUtil.closeServer();
            throw new CIServiceException("文件类型错误");
        }

        return zippedFileUrl;
    }

    public static String createXmlBody(String interfaceAddress, String sendTime, String syncTaskId, String extSysSyncId, String taskName, String uploadFileName, String rowNumber, String applicant, List<Column> colList) {
        String xmlBody = null;
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<BODY>");
        sb.append("<Title>");
        sb.append("<InterfaceAddress>");
        sb.append(interfaceAddress);
        sb.append("</InterfaceAddress>");
        sb.append("<SendTime>");
        sb.append(sendTime);
        sb.append("</SendTime>");
        sb.append("</Title>");
        sb.append("<Data>");
        sb.append("<SyncTaskId>");
        sb.append(syncTaskId);
        sb.append("</SyncTaskId>");
        sb.append("<ExtSysSyncId>");
        sb.append(extSysSyncId);
        sb.append("</ExtSysSyncId>");
        sb.append("<TaskName>");
        sb.append(taskName);
        sb.append("</TaskName>");
        sb.append("<UploadFileName>");
        sb.append(uploadFileName);
        sb.append("</UploadFileName>");
        sb.append("<RowNumber>");
        sb.append(rowNumber);
        sb.append("</RowNumber>");
        sb.append("<Applicant>");
        sb.append(applicant);
        sb.append("</Applicant>");
        sb.append("<Columns>");
        Iterator i$ = colList.iterator();

        while(i$.hasNext()) {
            Column c = (Column)i$.next();
            sb.append("<Column>");
            sb.append("<ColumnName>");
            sb.append(c.getColumnName());
            sb.append("</ColumnName>");
            sb.append("<ColumnType>");
            sb.append(c.getColumnType());
            sb.append("</ColumnType>");
            sb.append("<ColumnLength>");
            sb.append(c.getColumnLength());
            sb.append("</ColumnLength>");
            sb.append("</Column>");
        }

        sb.append("</Columns>");
        sb.append("</Data>");
        sb.append("</BODY>");
        xmlBody = sb.toString();
        System.out.println("发送的消息体为：" + xmlBody);
        return xmlBody;
    }

    public static void main(String[] args) {
        String interfaceAddress = "http://1.1.1.1:8080/WebServiceServer";
        String sendTime = "2012-09-26 16:02:29";
        String syncTaskId = "SYNC1348207541730";
        Object extSysSyncId = null;
        String taskName = "SYNC1348207541730";
        String uploadFileName = "SYNC1348207541730.zip";
        String rowNumber = "24620";
        String applicant = "ADMIN";
        ArrayList colList = new ArrayList();
        Column column = new Column();
        column.setColumnName("测试1");
        column.setColumnLength("10");
        column.setColumnType("1");
        Column column2 = new Column();
        column2.setColumnName("测试2");
        column2.setColumnLength("20");
        column2.setColumnType("2");
        Column column3 = new Column();
        column3.setColumnName("测试3");
        column3.setColumnLength("30");
        column3.setColumnType("3");
        colList.add(column);
        colList.add(column2);
        colList.add(column3);
        String xmlBody = createXmlBody(interfaceAddress, sendTime, syncTaskId, (String)extSysSyncId, taskName, uploadFileName, rowNumber, applicant, colList);
        WsCustomersPushedServerImpl impl = new WsCustomersPushedServerImpl();
        impl.getDataFromOtherSys(xmlBody);
    }
}
