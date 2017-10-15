package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiCustomSceneRel;
import com.ailk.biapp.ci.entity.CiCustomSceneRelId;
import com.ailk.biapp.ci.entity.DimScene;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.Column;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.task.CustomerFileCreaterThread;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.ThreadPool;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.http.ResponseEncodingUtil;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CustomersFileAction extends CIBaseAction {
    private Logger log = Logger.getLogger(CustomersFileAction.class);
    private String listTableName;
    private File file;
    private Pager pager;
    private String fileFileName;
    private String fileContentType;
    private CiCustomListInfo ciCustomListInfo;
    private CiCustomGroupInfo ciCustomGroupInfo;
    private List<String> attrList;
    private String customerName;
    private String isHaveAttrTitle;
    @Autowired
    private ICustomersManagerService customersService;
    private List<DimScene> dimScenes;
    private String sceneIds;

    public CustomersFileAction() {
    }

    public String importInit() throws CIServiceException {
        CacheBase cache = CacheBase.getInstance();
        this.dimScenes =(List<DimScene>) cache.getObjectList("DIM_SCENE");
        return "importInit";
    }

    public void downImportTempletFile() {
        ServletOutputStream out = null;
        FileInputStream fileInputStream = null;
        String fileName = Configure.getInstance().getProperty("SYS_COMMON_DOWN_NAME");
        String localFilePath = "";

        try {
            HttpServletResponse e = this.getResponse();
            HttpServletRequest request = this.getRequest();
            String mpmPath = Configure.getInstance().getProperty("SYS_COMMON_DOWN_PATH");
            if(!mpmPath.endsWith(File.separator)) {
                mpmPath = mpmPath + File.separator;
            }

            mpmPath = request.getSession().getServletContext().getRealPath("/") + File.separator + mpmPath;
            File pathFile = new File(mpmPath);
            if(pathFile.exists() || pathFile.mkdirs()) {
                localFilePath = mpmPath + fileName;
                String clientLanguage = request.getHeader("Accept-Language");
                String guessCharset = ResponseEncodingUtil.getGuessCharset(clientLanguage);
                String fileNameEncode = ResponseEncodingUtil.encodingFileName(fileName, guessCharset);
                e.addHeader("Content-Disposition", "attachment; filename=" + fileNameEncode);
                this.log.debug("offline download from web server================the file path is: " + localFilePath);
                e.setContentType("application/octet-stream;charset=" + guessCharset);
                out = e.getOutputStream();
                fileInputStream = new FileInputStream(localFilePath);
                boolean bytesRead = false;
                byte[] buffer = new byte[1024];

                int bytesRead1;
                while((bytesRead1 = fileInputStream.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, bytesRead1);
                }

                return;
            }
        } catch (IOException var29) {
            this.log.error(var29);
            return;
        } finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException var28) {
                    ;
                }
            }

            if(fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException var27) {
                    ;
                }
            }

        }

    }

    public String uploadCustomGroupFile() throws CIServiceException {
        String forward = "importSuccess";

        try {
            ArrayList e = new ArrayList();
            if(this.attrList != null) {
                Iterator createCityId = this.attrList.iterator();

                while(createCityId.hasNext()) {
                    String sceneList = (String)createCityId.next();
                    Column userId = new Column();
                    if(StringUtil.isNotEmpty(sceneList)) {
                        userId.setColumnName(sceneList);
                        e.add(userId);
                    }
                }
            }

            this.customerName = this.ciCustomGroupInfo.getCustomGroupName();
            String var10 = PrivilegeServiceUtil.getCityIdFromSession();
            this.ciCustomGroupInfo.setCreateCityId(var10);
            ArrayList var11 = new ArrayList();
            String var12 = this.getUserId();
            if(StringUtil.isNotEmpty(this.sceneIds)) {
                String[] ctx = this.sceneIds.split(",");

                for(int dataDate = 0; dataDate < ctx.length; ++dataDate) {
                    if(StringUtil.isNotEmpty(ctx[dataDate].trim())) {
                        CiCustomSceneRel item = new CiCustomSceneRel();
                        item.setId(new CiCustomSceneRelId());
                        item.getId().setSceneId(ctx[dataDate].trim());
                        item.getId().setUserId(var12);
                        item.setStatus(1);
                        var11.add(item);
                    }
                }

                this.ciCustomGroupInfo.setSceneList(var11);
            }

            ActionContext var13 = ActionContext.getContext();
            var13.put("customerName", this.customerName);
            String var14 = this.ciCustomGroupInfo.getDataDate();
            if(StringUtil.isNotEmpty(var14)) {
                if(var14.length() == 10) {
                    this.ciCustomGroupInfo.setDataDate(DateUtil.string2StringFormat(var14, "yyyy-MM-dd", "yyyyMMdd"));
                } else if(var14.length() == 7) {
                    this.ciCustomGroupInfo.setDataDate(DateUtil.string2StringFormat(var14, "yyyy-MM", "yyyyMM"));
                }
            }

            if(this.file != null && this.file.length() > 0L) {
                this.customersService.importCustomListFile(this.file, this.fileFileName, var12, this.ciCustomGroupInfo, e, Integer.valueOf(this.isHaveAttrTitle));
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_IMPORT", this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "导入创建客户群【" + this.ciCustomGroupInfo.getCustomGroupName() + "】成功！", OperResultEnum.Success, LogLevelEnum.Risk);
            }
        } catch (Exception var9) {
            this.log.error("导入失败", var9);
            forward = "importFail";
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_IMPORT", "-1", "-1", "导入客户群失败", OperResultEnum.Failure, LogLevelEnum.Risk);
        }

        return forward;
    }

    public void createCustomersFile() throws CIServiceException {
        HashMap result = new HashMap();
        CustomerFileCreaterThread upLoadThread = null;
        String msg = null;

        try {
            upLoadThread = (CustomerFileCreaterThread)SystemServiceLocator.getInstance().getService("customerFileCreaterThread");
            upLoadThread.setListTableName(this.listTableName);
            String response = null;

            try {
                response = PrivilegeServiceUtil.getCityId(this.getUserId());
            } catch (Exception var7) {
                var7.printStackTrace();
                this.log.error("根据userId获得cityId失败");
            }

            upLoadThread.setDownloadUserCityId(response);
            ThreadPool.getInstance().execute(upLoadThread);
            msg = "正在创建客户群清单文件，完成时会在个人通知中通知您。";
            result.put("success", Boolean.valueOf(true));
        } catch (Exception var8) {
            msg = "创建客户群清单文件失败";
            result.put("success", Boolean.valueOf(false));
            this.log.error(msg, var8);
        }

        result.put("msg", msg);
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void downCustomersFile() throws CIServiceException {
        String filelocalPath = Configure.getInstance().getProperty("CUSTOM_FILE_DOWN_PATH");
        String zipFile = filelocalPath + File.separator + this.listTableName + ".zip";
        File f = new File(zipFile);
        this.getResponse().setContentType("txt/plain,charset=UTF-8");
        this.getResponse().setHeader("Content-Disposition", "attachment;filename=" + f.getName());
        FileInputStream fis = null;
        ServletOutputStream os = null;

        try {
            fis = new FileInputStream(f);
            os = this.getResponse().getOutputStream();
            byte[] e = new byte[1024];
            boolean length = false;

            int length1;
            while((length1 = fis.read(e)) != -1) {
                os.write(e, 0, length1);
                os.flush();
            }

            CILogServiceUtil.getLogServiceInstance().log("COC_CUS_LIST_DOWNLOAD", "客户群清单下载", zipFile, "客户群清单下载成功", OperResultEnum.Success, LogLevelEnum.Risk);
        } catch (FileNotFoundException var17) {
            this.log.error("客户群清单文件没有找到", var17);
            CILogServiceUtil.getLogServiceInstance().log("COC_CUS_LIST_DOWNLOAD", "客户群清单下载", zipFile, "客户群清单下载失败：客户群清单文件没有找到", OperResultEnum.Failure, LogLevelEnum.Risk);
            throw new CIServiceException(var17);
        } catch (IOException var18) {
            this.log.error("IO异常", var18);
            CILogServiceUtil.getLogServiceInstance().log("COC_CUS_LIST_DOWNLOAD", "客户群清单下载", zipFile, "客户群清单下载失败：IO异常", OperResultEnum.Failure, LogLevelEnum.Risk);
            throw new CIServiceException(var18);
        } finally {
            try {
                if(fis != null) {
                    fis.close();
                }

                if(os != null) {
                    os.close();
                }
            } catch (IOException var16) {
                this.log.error("关闭异常", var16);
                throw new CIServiceException(var16);
            }

        }
    }

    public String init() {
        return "customFileListMain";
    }

    public String index() {
        return "customFileListIndex";
    }

    public String customFileList() {
        String msg = "";
        if(this.pager == null) {
            this.pager = new Pager();
        }

        try {
            this.ciCustomListInfo.setCreateUserId(PrivilegeServiceUtil.getUserId());
            Pager e = new Pager((long)this.customersService.queryCustomListCount(this.ciCustomListInfo));
            this.pager.setTotalPage(e.getTotalPage());
            this.pager.setTotalSize(e.getTotalSize());
            this.pager = this.pager.pagerFlip();
            this.pager.setResult(this.customersService.queryCustomList(this.pager.getPageNum(), this.pager.getPageSize(), this.ciCustomListInfo));
        } catch (Exception var3) {
            msg = "查询客户群清单列表报错";
            this.log.error(msg, var3);
        }

        return "customFileList";
    }

    public String getListTableName() {
        return this.listTableName;
    }

    public void setListTableName(String listTableName) {
        this.listTableName = listTableName;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileFileName() {
        return this.fileFileName;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public String getFileContentType() {
        return this.fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public CiCustomGroupInfo getCiCustomGroupInfo() {
        return this.ciCustomGroupInfo;
    }

    public void setCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo) {
        this.ciCustomGroupInfo = ciCustomGroupInfo;
    }

    public List<String> getAttrList() {
        return this.attrList;
    }

    public void setAttrList(List<String> attrList) {
        this.attrList = attrList;
    }

    public CiCustomListInfo getCiCustomListInfo() {
        return this.ciCustomListInfo;
    }

    public void setCiCustomListInfo(CiCustomListInfo ciCustomListInfo) {
        this.ciCustomListInfo = ciCustomListInfo;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<DimScene> getDimScenes() {
        return this.dimScenes;
    }

    public void setDimScenes(List<DimScene> dimScenes) {
        this.dimScenes = dimScenes;
    }

    public String getSceneIds() {
        return this.sceneIds;
    }

    public void setSceneIds(String sceneIds) {
        this.sceneIds = sceneIds;
    }

    public String getIsHaveAttrTitle() {
        return this.isHaveAttrTitle;
    }

    public void setIsHaveAttrTitle(String isHaveAttrTitle) {
        this.isHaveAttrTitle = isHaveAttrTitle;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }
}
