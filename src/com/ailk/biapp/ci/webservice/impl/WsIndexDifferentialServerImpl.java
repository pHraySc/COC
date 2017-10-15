package com.ailk.biapp.ci.webservice.impl;

import com.ailk.biapp.ci.dao.ICiCustomListInfoHDao;
import com.ailk.biapp.ci.dao.ICiCustomPushReqHDao;
import com.ailk.biapp.ci.dao.ICiSysInfoHDao;
import com.ailk.biapp.ci.entity.CiCustomFileRel;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiCustomPushReq;
import com.ailk.biapp.ci.entity.CiSysInfo;
import com.ailk.biapp.ci.entity.CiTemplateInfo;
import com.ailk.biapp.ci.exception.*;
import com.ailk.biapp.ci.model.Column;
import com.ailk.biapp.ci.model.ReturnMessageModel;
import com.ailk.biapp.ci.service.ICiCustomFileRelService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.task.CustomGroupUpLoadFileThread;
import com.ailk.biapp.ci.util.FileUtil;
import com.ailk.biapp.ci.util.FtpUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.ThreadPool;
import com.ailk.biapp.ci.util.ZipUtils;
import com.ailk.biapp.ci.webservice.IIndexDifferentialServer;
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
    endpointInterface = "com.ailk.biapp.ci.webservice.IIndexDifferentialServer"
)
@Features(
    features = {"org.apache.cxf.feature.LoggingFeature"}
)
public class WsIndexDifferentialServerImpl implements IIndexDifferentialServer {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ICiSysInfoHDao ciSysInfoHDao;
    @Autowired
    private ICiCustomPushReqHDao ciCustomPushReqHDao;
    @Autowired
    private ICiCustomListInfoHDao ciCustomListInfoHDao;
    @Autowired
    private ICustomersManagerService customersService;
    @Autowired
    private ICiCustomFileRelService loadCustomGroupFileService;

    public WsIndexDifferentialServerImpl() {
    }

    public String getDataFromSA(String xmlBody) {
        this.log.info("从SA接收XML数据并解析");
        String interfaceAddress = null;
        String sendTime = null;
        String syncTaskId = null;
        String extSysSyncId = null;
        String taskName = null;
        String uploadFileName = null;
        String rowNumber = null;
        String applicant = null;
        String whereExplain = null;
        ArrayList colList = new ArrayList();
        String returnCode = null;
        String errorMsg = null;

        try {
            SAXReader req = new SAXReader();
            Document listTableName = req.read(new StringReader(xmlBody));
            Element ciCustomListInfo = listTableName.getRootElement();
            Element parentCustomId = ciCustomListInfo.element("Title");
            interfaceAddress = parentCustomId.elementTextTrim("InterfaceAddress");
            sendTime = parentCustomId.elementTextTrim("SendTime");
            Element logForXmlData = ciCustomListInfo.element("Data");
            syncTaskId = logForXmlData.elementTextTrim("SyncTaskId");
            extSysSyncId = logForXmlData.elementTextTrim("ExtSysSyncId");
            taskName = logForXmlData.elementTextTrim("TaskName");
            uploadFileName = logForXmlData.elementTextTrim("UploadFileName");
            rowNumber = logForXmlData.elementTextTrim("RowNumber");
            applicant = logForXmlData.elementTextTrim("Applicant");
            whereExplain = logForXmlData.elementTextTrim("WhereExplain");
            Iterator localFileUrl = logForXmlData.elementIterator("Columns");
            if(localFileUrl.hasNext()) {
                Element sysId = (Element)localFileUrl.next();
                Iterator ciSysInfo = sysId.elementIterator("Column");

                while(ciSysInfo.hasNext()) {
                    Element returnMessageModel = (Element)ciSysInfo.next();
                    Column interfaceAddressForSA = new Column();
                    interfaceAddressForSA.setColumnName(returnMessageModel.elementTextTrim("ColumnName"));
                    interfaceAddressForSA.setColumnType(returnMessageModel.elementTextTrim("ColumnType"));
                    interfaceAddressForSA.setColumnLength(returnMessageModel.elementTextTrim("ColumnLength"));
                    colList.add(interfaceAddressForSA);
                }
            }
        } catch (Exception var25) {
            this.log.error("解析XML错误", var25);
            errorMsg = "解析XML错误";
            returnCode = "0";
            var25.printStackTrace();
        }

        if(StringUtil.isEmpty(syncTaskId)) {
            errorMsg = "XML中没有任务ID";
            this.log.error(errorMsg);
            returnCode = "0";
            return returnCode;
        } else if(StringUtil.isEmpty(extSysSyncId)) {
            errorMsg = "XML中没有请求ID";
            this.log.error(errorMsg);
            returnCode = "0";
            return returnCode;
        } else {
            CiCustomPushReq req1 = this.ciCustomPushReqHDao.selectCiCustomPushReqById(extSysSyncId);
            if(req1 != null && !StringUtil.isEmpty(req1.getListTableName())) {
                String listTableName1 = req1.getListTableName();
                CiCustomListInfo ciCustomListInfo1 = this.ciCustomListInfoHDao.selectById(listTableName1);
                String parentCustomId1 = ciCustomListInfo1.getCustomGroupId();
                returnCode = "1";
                this.log.debug("解析XML成功");
                String logForXmlData1 = "interfaceAddress=[" + interfaceAddress + "]," + "sendTime=[" + sendTime + "]," + "syncTaskId=[" + syncTaskId + "]," + "extSysSyncId=[" + extSysSyncId + "]," + "taskName=[" + taskName + "]," + "whereExplain=[" + whereExplain + "]," + "uploadFileName=[" + uploadFileName + "]," + "rowNumber=[" + rowNumber + "]," + "applicant=[" + applicant + "]";
                this.log.debug(logForXmlData1);
                String localFileUrl1 = null;
                String sysId1 = Configure.getInstance().getProperty("FROM_SA_SYS_ID");
                if(StringUtil.isEmpty(sysId1)) {
                    errorMsg = "没有配置从SA获得信息的系统ID";
                    this.log.error(errorMsg);
                    returnCode = "0";
                    return returnCode;
                } else {
                    CiSysInfo ciSysInfo1 = null;

                    try {
                        ciSysInfo1 = this.ciSysInfoHDao.selectById(sysId1);
                        localFileUrl1 = this.dealFile(ciSysInfo1, uploadFileName);
                    } catch (CIServiceException var24) {
                        this.log.error(var24.getMessage());
                        errorMsg = var24.getMessage();
                        returnCode = "0";
                    }

                    if(StringUtil.isEmpty(localFileUrl1)) {
                        errorMsg = "没有生成本地文件！";
                        this.log.error(errorMsg);
                        returnCode = "0";
                    }

                    if("0".equals(returnCode)) {
                        return returnCode;
                    } else {
                        ReturnMessageModel returnMessageModel1 = new ReturnMessageModel();
                        returnMessageModel1.setSyncTaskId(syncTaskId);
                        returnMessageModel1.setErrorMsg(errorMsg);
                        String interfaceAddressForSA1 = ciSysInfo1.getWebserviceWSDL().replace("?wsdl", "");
                        returnMessageModel1.setInterfaceAddress(interfaceAddressForSA1);
                        this.importAndCreateCustomer(req1.getUserId(), parentCustomId1, whereExplain, taskName, localFileUrl1, returnMessageModel1);
                        return returnCode;
                    }
                }
            } else {
                errorMsg = "没有查询到有效的任务ID";
                this.log.error(errorMsg);
                returnCode = "0";
                return returnCode;
            }
        }
    }

    private String dealFile(CiSysInfo ciSysInfo, String uploadFileName) throws CIServiceException {
        FtpUtil ftpUtil = new FtpUtil();
        String localFileUrl = null;

        try {
            if(ciSysInfo != null && !StringUtil.isEmpty(uploadFileName)) {
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

    private void importAndCreateCustomer(String userId, String parentCustomId, String whereExplain, String taskName, String localFileUrl, ReturnMessageModel returnMessageModel) {
        this.log.info("Begin import customer list from SA");

        try {
            CiCustomFileRel e = new CiCustomFileRel();
            boolean customCreateSuccess = false;
            File file = new File(localFileUrl);
            String newName = file.getName();
            String name = newName.substring(0, newName.lastIndexOf("."));
            String ext = newName.substring(newName.lastIndexOf(".") + 1, newName.length());
            CiCustomGroupInfo ciCustomGroupInfo = new CiCustomGroupInfo();
            ciCustomGroupInfo.setCustomGroupName("指标微分群_" + name);
            ciCustomGroupInfo.setCustomGroupDesc("指标微分群_" + name + "，来自自助分析平台推送，任务名称：" + taskName);
            ciCustomGroupInfo.setCreateTypeId(Integer.valueOf(10));
            ciCustomGroupInfo.setParentCustomId(parentCustomId);
            ciCustomGroupInfo.setKpiDiffRule(whereExplain);
            ciCustomGroupInfo.setProductAutoMacthFlag(Integer.valueOf(0));
            ciCustomGroupInfo.setCreateUserId(userId);
            ciCustomGroupInfo.setCreateUserName(PrivilegeServiceUtil.getUserById(userId).getUsername());
            customCreateSuccess = this.customersService.addCiCustomGroupInfo(ciCustomGroupInfo, (List)null, (List)null, (CiTemplateInfo)null, userId, false, (List)null);
            String customGroupId = ciCustomGroupInfo.getCustomGroupId();
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
                    upLoadThread.setCustomGroupInfo(ciCustomGroupInfo);
                    ThreadPool.getInstance().execute(upLoadThread);
                } catch (Exception var17) {
                    this.log.error("SA导入客户群异常", var17);
                }
            }
        } catch (Exception var18) {
            this.log.error("SA返回清单创建客户群异常", var18);
        }

        this.log.info("End import customer list from SA");
    }

    private boolean connectToFtpServer(CiSysInfo sysInfo, FtpUtil ftpUtil) {
        int port = NumberUtils.toInt(sysInfo.getFtpPort(), 21);

        try {
            String e = DES.decrypt(sysInfo.getFtpPwd());
            return ftpUtil.connectServer(sysInfo.getFtpServerIp(), port, sysInfo.getFtpUser(), e, sysInfo.getFtpPath());
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
}
