package com.ailk.biapp.ci.feedback.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiPersonNotice;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.feedback.entity.CiAttachmentFileInfo;
import com.ailk.biapp.ci.feedback.entity.CiFeedbackInfo;
import com.ailk.biapp.ci.feedback.entity.CiFeedbackRecordInfo;
import com.ailk.biapp.ci.feedback.entity.DimDeclareDealStatus;
import com.ailk.biapp.ci.feedback.service.ICiFeedbackService;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiPersonNoticeService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.http.ResponseEncodingUtil;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiFeedbackAction extends CIBaseAction {
    private Logger log = Logger.getLogger(CiFeedbackAction.class);
    private CiFeedbackInfo feedbackInfo;
    private CiFeedbackRecordInfo feedbackRecordInfo;
    private CiAttachmentFileInfo ciAttachmentFileInfo;
    private List<CiFeedbackRecordInfo> feedbackRecordList;
    private List<DimDeclareDealStatus> dimDeclareDealStatusList;
    private List<CiAttachmentFileInfo> attachmentFileList;
    private Pager pager;
    private boolean canManageDeclare = false;
    private Integer feedbackInfoId;
    private File files;
    private String filesFileName;
    private Integer fileId;
    private String source;
    @Autowired
    private ICiFeedbackService ciFeedbackService;
    @Autowired
    private ICustomersManagerService customersManagerService;
    @Autowired
    private ICiPersonNoticeService personNoticeService;

    public CiFeedbackAction() {
    }

    public String init() {
        this.getRequest().setAttribute("source", this.source);
        return "feedbackMain";
    }

    public String historyIndex() {
        if(PrivilegeServiceUtil.getUserId().equals(Configure.getInstance().getProperty("ADMIN_ID"))) {
            this.canManageDeclare = true;
        }

        this.dimDeclareDealStatusList = this.ciFeedbackService.queryDeclareDealStatus();
        return "feedbackHistoryIndex";
    }

    public String index() {
        if(PrivilegeServiceUtil.getUserId().equals(Configure.getInstance().getProperty("ADMIN_ID"))) {
            this.canManageDeclare = true;
        }

        this.dimDeclareDealStatusList = this.ciFeedbackService.queryDeclareDealStatus();
        return "feedbackIndex";
    }

    public String findAddFeedbackPage() {
        return "addFeedback";
    }

    public String query() throws Exception {
        String userId = this.getUserId();
        if(null == this.feedbackInfo) {
            this.feedbackInfo = new CiFeedbackInfo();
        }

        if(!this.canManageDeclare) {
            this.feedbackInfo.setUserId(userId);
        }

        this.feedbackInfo.setCanManageDeclare(this.canManageDeclare);
        if(this.pager == null) {
            this.pager = new Pager();
        }

        this.pager.setTotalSize((long)this.ciFeedbackService.queryTotolPageCount(this.feedbackInfo));
        this.pager = this.pager.pagerFlip();
        this.pager.setResult(this.ciFeedbackService.queryPageList(this.pager.getPageNum(), this.pager.getPageSize(), this.feedbackInfo));
        return "feedbackList";
    }

    public String queryHistory() throws Exception {
        String userId = this.getUserId();
        if(null == this.feedbackInfo) {
            this.feedbackInfo = new CiFeedbackInfo();
        }

        if(!this.canManageDeclare) {
            this.feedbackInfo.setUserId(userId);
        }

        this.feedbackInfo.setCanManageDeclare(this.canManageDeclare);
        if(this.pager == null) {
            this.pager = new Pager();
        }

        this.pager.setTotalSize((long)this.ciFeedbackService.queryHistoryTotalPageCount(this.feedbackInfo));
        this.pager = this.pager.pagerFlip();
        this.pager.setResult(this.ciFeedbackService.queryHistoryPageList(this.pager.getPageNum(), this.pager.getPageSize(), this.feedbackInfo));
        return "feedbackHistoryList";
    }

    public String queryFeedbackInfoById() throws Exception {
        try {
            if(PrivilegeServiceUtil.getUserId().equals(Configure.getInstance().getProperty("ADMIN_ID"))) {
                this.canManageDeclare = true;
            }

            this.feedbackInfo = this.ciFeedbackService.queryByFeedbackInfoById(this.feedbackInfoId);
            this.getRequest().setAttribute("source", this.source);
            return "feedbackRecord";
        } catch (Exception var2) {
            this.log.error("ͨ���������ID��ѯ���������Ϣ����", var2);
            throw new CIServiceException(var2);
        }
    }

    public String showFeedbackDetail() throws Exception {
        try {
            this.feedbackInfo = this.ciFeedbackService.queryByFeedbackInfoById(this.feedbackInfoId);
            this.getRequest().setAttribute("source", this.source);
            return "showFeedbackDetail";
        } catch (Exception var2) {
            this.log.error("ͨ���������ID��ѯ���������Ϣ����", var2);
            throw new CIServiceException(var2);
        }
    }

    public void queryFeedbackRecord() throws Exception {
        HashMap feedbackRecord = new HashMap();
        HttpServletResponse response = this.getResponse();
        this.feedbackRecordList = this.ciFeedbackService.queryFeedbackRecord(this.feedbackInfoId);
        feedbackRecord.put("feedbackRecordList", this.feedbackRecordList);

        try {
            this.sendJson(response, JsonUtil.toJson(feedbackRecord));
        } catch (Exception var4) {
            this.log.error("���ͻظ���¼json���쳣", var4);
            throw new CIServiceException(var4);
        }
    }

    public void startDealFeedback() throws Exception {
        try {
            this.ciFeedbackService.statusAddToReply(this.feedbackInfoId);
            CiFeedbackInfo e = this.ciFeedbackService.queryByFeedbackInfoById(this.feedbackInfoId);
            CiPersonNotice personNotice = new CiPersonNotice();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            personNotice.setReleaseUserId(PrivilegeServiceUtil.getUserId());
            personNotice.setReceiveUserId(e.getUserId());
            personNotice.setNoticeDetail("���� ����" + dateFormat.format(e.getFeedbackTime()) + "�ύ�����������" + e.getFeedbackTitle() + "���Ѿ����ڴ����У������ĵȺ�������лл��");
            personNotice.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_FEEDBACK_REPLY));
            personNotice.setNoticeName(e.getFeedbackTitle() + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_FEEDBACK_REPLY);
            if(ServiceConstants.FEEDBACK_TYPE_CUSTOM == e.getFeedbackType().intValue()) {
                personNotice.setCustomerGroupId(e.getLabelCustomId());
            } else if(ServiceConstants.FEEDBACK_TYPE_LABEL == e.getFeedbackType().intValue()) {
                personNotice.setLabelId(Integer.valueOf(Integer.parseInt(e.getLabelCustomId())));
            }

            this.sendNotice(personNotice);
        } catch (Exception var4) {
            this.log.error("��ʼ����������", var4);
            throw new CIServiceException(var4);
        }
    }

    public void cancelFeedback() throws Exception {
        try {
            this.ciFeedbackService.statusAddToCancel(this.feedbackInfoId);
            CiFeedbackInfo e = this.ciFeedbackService.queryByFeedbackInfoById(this.feedbackInfoId);
            Timestamp replyTime = new Timestamp((new Date()).getTime());
            CiPersonNotice personNotice = new CiPersonNotice();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            personNotice.setReleaseUserId(PrivilegeServiceUtil.getUserId());
            personNotice.setReceiveUserId(Configure.getInstance().getProperty("ADMIN_ID"));
            personNotice.setNoticeDetail("���� " + PrivilegeServiceUtil.getUserDept(PrivilegeServiceUtil.getUserId()) + "��" + this.getUserName() + "��" + dateFormat.format(replyTime) + "�Ա���Ϊ��" + e.getFeedbackTitle() + "�������������ȡ������֪��");
            personNotice.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_FEEDBACK_CANCLE));
            personNotice.setNoticeName(e.getFeedbackTitle() + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_FEEDBACK_CANCLE);
            if(ServiceConstants.FEEDBACK_TYPE_CUSTOM == e.getFeedbackType().intValue()) {
                personNotice.setCustomerGroupId(e.getLabelCustomId());
            } else if(ServiceConstants.FEEDBACK_TYPE_LABEL == e.getFeedbackType().intValue()) {
                personNotice.setLabelId(Integer.valueOf(Integer.parseInt(e.getLabelCustomId())));
            }

            this.sendNotice(personNotice);
        } catch (Exception var5) {
            this.log.error("ȡ����������", var5);
            throw new CIServiceException(var5);
        }
    }

    public void finishFeedback() throws Exception {
        try {
            this.ciFeedbackService.statusReplyToFinish(this.feedbackInfoId);
            CiFeedbackInfo e = this.ciFeedbackService.queryByFeedbackInfoById(this.feedbackInfoId);
            CiPersonNotice personNotice = new CiPersonNotice();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            personNotice.setReleaseUserId(PrivilegeServiceUtil.getUserId());
            personNotice.setReceiveUserId(e.getUserId());
            personNotice.setNoticeDetail("���� ����" + dateFormat.format(e.getFeedbackTime()) + "�ύ�����������" + e.getFeedbackTitle() + "���Ѿ���ɴ����뼰ʱ����");
            personNotice.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_FEEDBACK_REPLY));
            personNotice.setNoticeName(e.getFeedbackTitle() + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_FEEDBACK_REPLY);
            if(ServiceConstants.FEEDBACK_TYPE_CUSTOM == e.getFeedbackType().intValue()) {
                personNotice.setCustomerGroupId(e.getLabelCustomId());
            } else if(ServiceConstants.FEEDBACK_TYPE_LABEL == e.getFeedbackType().intValue()) {
                personNotice.setLabelId(Integer.valueOf(Integer.parseInt(e.getLabelCustomId())));
            }

            this.sendNotice(personNotice);
        } catch (Exception var4) {
            this.log.error("ȡ����������", var4);
            throw new CIServiceException(var4);
        }
    }

    public void fallbackFeedback() {
        try {
            CiFeedbackInfo e = this.ciFeedbackService.queryByFeedbackInfoById(this.feedbackInfoId);
            e.setDealStatusId(Integer.valueOf(ServiceConstants.FEEDBACK_DEAL_STATUS_ADD));
            CiPersonNotice personNotice = new CiPersonNotice();
            Timestamp replyTime = new Timestamp((new Date()).getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            personNotice.setReleaseUserId(PrivilegeServiceUtil.getUserId());
            personNotice.setReceiveUserId(e.getReplyUserId());
            personNotice.setNoticeDetail("���� " + PrivilegeServiceUtil.getUserDept(PrivilegeServiceUtil.getUserId()) + "��" + this.getUserName() + "��" + dateFormat.format(replyTime) + "��������������ı���Ϊ��" + e.getFeedbackTitle() + "��������������뼰ʱ����");
            personNotice.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_FEEDBACK_REPLY));
            personNotice.setNoticeName(e.getFeedbackTitle() + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_FEEDBACK_REPLY);
            if(ServiceConstants.FEEDBACK_TYPE_CUSTOM == e.getFeedbackType().intValue()) {
                personNotice.setCustomerGroupId(e.getLabelCustomId());
            } else if(ServiceConstants.FEEDBACK_TYPE_LABEL == e.getFeedbackType().intValue()) {
                personNotice.setLabelId(Integer.valueOf(Integer.parseInt(e.getLabelCustomId())));
            }

            this.sendNotice(personNotice);
            this.ciFeedbackService.insertFeedbackInfo(e);
        } catch (Exception var5) {
            this.log.error("ȡ����������", var5);
            throw new CIServiceException(var5);
        }
    }

    public void closeFeedback() throws Exception {
        try {
            this.ciFeedbackService.statusFinishToClose(this.feedbackInfoId);
        } catch (Exception var2) {
            this.log.error("ȡ����������", var2);
            throw new CIServiceException(var2);
        }
    }

    public void findAllEffectiveLabelList() throws Exception {
        HashMap result = new HashMap();
        boolean success = false;

        try {
            CacheBase e = CacheBase.getInstance();
            CopyOnWriteArrayList list = e.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
            ArrayList labelList = new ArrayList();
            int levelType = 3;
            String levelTypeStr = Configure.getInstance().getProperty("LABEL_CATEGORY_TYPE");
            if(StringUtil.isNotEmpty(levelTypeStr)) {
                levelType = Integer.valueOf(Configure.getInstance().getProperty("LABEL_CATEGORY_TYPE")).intValue();
            } else {
                this.log.error("������LABEL_CATEGORY_TYPEδ����");
            }

            Iterator response = list.iterator();

            while(response.hasNext()) {
                CiLabelInfo e1 = (CiLabelInfo)response.next();
                if(e1.getCiLabelExtInfo().getLabelLevel().intValue() > levelType) {
                    labelList.add(e1);
                }
            }

            success = true;
            result.put("labelList", labelList);
            result.put("success", Boolean.valueOf(success));
            HttpServletResponse response1 = this.getResponse();

            try {
                this.sendJson(response1, JsonUtil.toJson(result));
            } catch (Exception var10) {
                this.log.error("����json���쳣", var10);
                throw new CIServiceException(var10);
            }
        } catch (Exception var11) {
            this.log.error("CiFeedbackAction.findSimilarityLabelNameList method �쳣", var11);
            throw new CIServiceException(var11);
        }
    }

    public void findCustomerGroupTree() throws Exception {
        List treeList = null;
        boolean success = false;
        HashMap result = new HashMap();

        try {
            String response = PrivilegeServiceUtil.getUserId();
            treeList = this.ciFeedbackService.queryCustomerGroupTree(response);
            success = true;
            result.put("treeList", treeList);
            result.put("success", Boolean.valueOf(success));
        } catch (CIServiceException var7) {
            String e = "����ҵĿͻ�Ⱥ��ʧ��";
            this.log.error(e, var7);
            success = false;
            result.put("msg", e);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("����json���쳣", var6);
            throw new CIServiceException(var6);
        }
    }

    public void saveAttenchments() {
        HashMap result = new HashMap();
        String resultStr = "";
        boolean success = true;
        String msg = null;
        String filesContentType = "";

        try {
            this.ciAttachmentFileInfo = new CiAttachmentFileInfo();
            filesContentType = this.filesFileName.substring(this.filesFileName.lastIndexOf("."));
            this.ciAttachmentFileInfo.setFileType(filesContentType);
            this.ciAttachmentFileInfo.setFileOldName(this.filesFileName);
            String e = (new Date()).getTime() + this.filesFileName;
            this.ciAttachmentFileInfo.setFileName(e);
            String fileUrl = this.ciFeedbackService.uploadFile(this.files, e + ".tmp");
            this.ciAttachmentFileInfo.setFileUrl(fileUrl);
            msg = "�����ϴ��ɹ���";
        } catch (Exception var18) {
            this.log.error("�ϴ��ļ������쳣", var18);
            success = false;
            msg = "�ϴ��ļ��������ʹ��������ϴ��ļ���";
        } finally {
            result.put("attachment", this.ciAttachmentFileInfo);
            result.put("data", resultStr);
            result.put("success", Boolean.valueOf(success));
            result.put("msg", msg);
            HttpServletResponse response = this.getResponse();

            try {
                this.sendJson(response, JsonUtil.toJson(result));
            } catch (Exception var17) {
                this.log.error("���ݴ����쳣��", var17);
                throw new CIServiceException();
            }
        }

    }

    public void feedbackReply() throws Exception {
        String reciveUserId = "";

        try {
            CiFeedbackInfo e = this.ciFeedbackService.queryByFeedbackInfoById(this.feedbackRecordInfo.getFeedbackInfoId());
            Timestamp replyTime = new Timestamp((new Date()).getTime());
            this.feedbackRecordInfo.setReplyTime(replyTime);
            if(this.feedbackInfo.getCanManageDeclare()) {
                this.feedbackRecordInfo.setReplyType(Integer.valueOf(ServiceConstants.FEEDBACK_REPLY_TYPE_REPLY));
                reciveUserId = e.getUserId();
            } else {
                this.feedbackRecordInfo.setReplyType(Integer.valueOf(ServiceConstants.FEEDBACK_REPLY_TYPE_RAISE));
                reciveUserId = e.getReplyUserId();
            }

            this.feedbackRecordInfo.setAttachmentList(this.attachmentFileList);
            this.feedbackRecordInfo.setCurrDealStatusId(e.getDealStatusId());
            CiPersonNotice personNotice = new CiPersonNotice();
            personNotice.setReleaseUserId(PrivilegeServiceUtil.getUserId());
            personNotice.setReceiveUserId(reciveUserId);
            personNotice.setNoticeDetail("���� " + PrivilegeServiceUtil.getUserDept(PrivilegeServiceUtil.getUserId()) + "��" + this.getUserName() + "��" + replyTime + "�Ա���Ϊ ��" + e.getFeedbackTitle() + "����������������˻ظ����뼰ʱ����");
            personNotice.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_FEEDBACK_REPLY));
            personNotice.setNoticeName(e.getFeedbackTitle() + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_FEEDBACK_REPLY);
            if(ServiceConstants.FEEDBACK_TYPE_CUSTOM == e.getFeedbackType().intValue()) {
                personNotice.setCustomerGroupId(e.getLabelCustomId());
            } else if(ServiceConstants.FEEDBACK_TYPE_LABEL == e.getFeedbackType().intValue()) {
                personNotice.setLabelId(Integer.valueOf(Integer.parseInt(e.getLabelCustomId())));
            }

            this.sendNotice(personNotice);
            this.ciFeedbackService.insertRecordInfo(this.feedbackRecordInfo);
        } catch (Exception var5) {
            this.log.error("���淴���ظ���Ϣ����", var5);
            throw new CIServiceException(var5);
        }
    }

    public void saveFeedback() throws Exception {
        HashMap returnMsg = new HashMap();
        HttpServletResponse response = this.getResponse();
        String userId = PrivilegeServiceUtil.getUserId();
        Date date = new Date();
        Timestamp feedbackTime = new Timestamp(date.getTime());
        String replyUserId = "";
        boolean hasSameFeedback = true;

        try {
            this.feedbackInfo.setUserId(userId);
            if(ServiceConstants.FEEDBACK_TYPE_SYSTEM != this.feedbackInfo.getFeedbackType().intValue()) {
                hasSameFeedback = this.ciFeedbackService.hasSameFeedback(this.feedbackInfo);
            }

            if(hasSameFeedback) {
                this.feedbackInfo.setFeedbackTime(feedbackTime);
                this.feedbackInfo.setDealStatusId(Integer.valueOf(ServiceConstants.FEEDBACK_DEAL_STATUS_ADD));
                this.feedbackInfo.setStatus(Integer.valueOf(ServiceConstants.FEEDBACK_STATUS_VAL));
                if(ServiceConstants.FEEDBACK_TYPE_CUSTOM == this.feedbackInfo.getFeedbackType().intValue() && this.feedbackInfo.getIsCreateUser()) {
                    replyUserId = this.customersManagerService.queryCiCustomGroupInfoForCache(this.feedbackInfo.getLabelCustomId()).getCreateUserId();
                } else {
                    replyUserId = Configure.getInstance().getProperty("ADMIN_ID");
                }

                this.feedbackInfo.setReplyUserId(replyUserId);
                this.ciFeedbackService.insertFeedbackInfo(this.feedbackInfo);
                CiFeedbackRecordInfo e = new CiFeedbackRecordInfo();
                e.setFeedbackInfoId(this.feedbackInfo.getFeedbackInfoId());
                e.setReplyInfo(this.feedbackInfo.getFeedbackInfo());
                e.setReplyTime(feedbackTime);
                e.setCurrDealStatusId(Integer.valueOf(ServiceConstants.FEEDBACK_DEAL_STATUS_ADD));
                e.setReplyType(Integer.valueOf(ServiceConstants.FEEDBACK_REPLY_TYPE_RAISE));
                e.setAttachmentList(this.attachmentFileList);
                this.ciFeedbackService.insertRecordInfo(e);
                CiPersonNotice personNotice = new CiPersonNotice();
                personNotice.setReleaseUserId(userId);
                personNotice.setReceiveUserId(replyUserId);
                personNotice.setNoticeDetail("���� " + PrivilegeServiceUtil.getUserDept(userId) + "��" + this.getUserName() + "��" + feedbackTime + "����˱���Ϊ ��" + this.feedbackInfo.getFeedbackTitle() + "��������������뼰ʱ����");
                personNotice.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_FEEDBACK_ADD));
                personNotice.setNoticeName(this.feedbackInfo.getFeedbackTitle() + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_FEEDBACK_ADD);
                if(ServiceConstants.FEEDBACK_TYPE_CUSTOM == this.feedbackInfo.getFeedbackType().intValue()) {
                    personNotice.setCustomerGroupId(this.feedbackInfo.getLabelCustomId());
                } else if(ServiceConstants.FEEDBACK_TYPE_LABEL == this.feedbackInfo.getFeedbackType().intValue()) {
                    personNotice.setLabelId(Integer.valueOf(Integer.parseInt(this.feedbackInfo.getLabelCustomId())));
                }

                this.sendNotice(personNotice);
                returnMsg.put("msg", "�����ɹ�");
                returnMsg.put("success", Boolean.valueOf(true));
            } else {
                returnMsg.put("msg", "���Ըñ�ǩ/�ͻ�Ⱥ�ķ��������ڴ����У������ĵȴ���");
                returnMsg.put("success", Boolean.valueOf(false));
            }
        } catch (Exception var11) {
            returnMsg.put("msg", "����ʧ��");
            returnMsg.put("success", Boolean.valueOf(false));
            this.log.error("�ύ�����쳣", var11);
            this.ciFeedbackService.deleteFeedbackInfo(this.feedbackInfo);
        }

        try {
            this.sendJson(response, JsonUtil.toJson(returnMsg));
        } catch (Exception var10) {
            this.log.error("����json���쳣", var10);
            throw new CIServiceException(var10);
        }
    }

    public void downLoadAttachments() {
        ServletOutputStream out = null;
        FileInputStream fileInputStream = null;
        HttpServletResponse response = this.getResponse();
        HttpServletRequest request = this.getRequest();
        new CiAttachmentFileInfo();

        try {
            CiAttachmentFileInfo attachmentFile = this.ciFeedbackService.queryAttachmentFileById(this.fileId);
            String e = attachmentFile.getFileName();
            String fileUrl = attachmentFile.getFileUrl();
            String mpmPath = Configure.getInstance().getProperty("SYS_COMMON_DOWN_PATH");
            if(!mpmPath.endsWith(File.separator)) {
                mpmPath = mpmPath + File.separator;
            }

            mpmPath = request.getSession().getServletContext().getRealPath("/") + File.separator + mpmPath;
            File pathFile = new File(mpmPath);
            if(pathFile.exists() || pathFile.mkdirs()) {
                String clientLanguage = request.getHeader("Accept-Language");
                String guessCharset = ResponseEncodingUtil.getGuessCharset(clientLanguage);
                String fileNameEncode = ResponseEncodingUtil.encodingFileName(e, guessCharset);
                response.addHeader("Content-Disposition", "attachment; filename=" + fileNameEncode);
                this.log.debug("offline download from web server================the file path is: " + fileUrl);
                response.setContentType("application/octet-stream;charset=" + guessCharset);
                out = response.getOutputStream();
                fileInputStream = new FileInputStream(fileUrl);
                boolean bytesRead = false;
                byte[] buffer = new byte[1024];

                int bytesRead1;
                while((bytesRead1 = fileInputStream.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, bytesRead1);
                }

                return;
            }
        } catch (IOException var30) {
            this.log.error(var30);
            return;
        } finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException var29) {
                    this.log.error(var29);
                }
            }

            if(fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException var28) {
                    this.log.error(var28);
                }
            }

        }

    }

    private void sendNotice(CiPersonNotice personNotice) {
        try {
            personNotice.setStatus(Integer.valueOf(1));
            personNotice.setNoticeSendTime(new Date());
            personNotice.setReadStatus(Integer.valueOf(1));
            personNotice.setIsSuccess(Integer.valueOf(1));
            this.personNoticeService.addPersonNotice(personNotice);
        } catch (Exception var3) {
            this.log.error("�����������֪ͨ����", var3);
            throw new CIServiceException(var3);
        }
    }

    public CiFeedbackInfo getFeedbackInfo() {
        return this.feedbackInfo;
    }

    public void setFeedbackInfo(CiFeedbackInfo feedbackInfo) {
        this.feedbackInfo = feedbackInfo;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public boolean getCanManageDeclare() {
        return this.canManageDeclare;
    }

    public void setCanManageDeclare(boolean canManageDeclare) {
        this.canManageDeclare = canManageDeclare;
    }

    public Integer getFeedbackInfoId() {
        return this.feedbackInfoId;
    }

    public void setFeedbackInfoId(Integer feedbackInfoId) {
        this.feedbackInfoId = feedbackInfoId;
    }

    public CiFeedbackRecordInfo getFeedbackRecordInfo() {
        return this.feedbackRecordInfo;
    }

    public void setFeedbackRecordInfo(CiFeedbackRecordInfo feedbackRecordInfo) {
        this.feedbackRecordInfo = feedbackRecordInfo;
    }

    public List<CiFeedbackRecordInfo> getFeedbackRecordList() {
        return this.feedbackRecordList;
    }

    public void setFeedbackRecordList(List<CiFeedbackRecordInfo> feedbackRecordList) {
        this.feedbackRecordList = feedbackRecordList;
    }

    public List<DimDeclareDealStatus> getDimDeclareDealStatusList() {
        return this.dimDeclareDealStatusList;
    }

    public void setDimDeclareDealStatusList(List<DimDeclareDealStatus> dimDeclareDealStatusList) {
        this.dimDeclareDealStatusList = dimDeclareDealStatusList;
    }

    public void setFiles(File files) {
        this.files = files;
    }

    public void setFilesFileName(String filesFileName) {
        this.filesFileName = filesFileName;
    }

    public CiAttachmentFileInfo getCiAttachmentFileInfo() {
        return this.ciAttachmentFileInfo;
    }

    public void setCiAttachmentFileInfo(CiAttachmentFileInfo ciAttachmentFileInfo) {
        this.ciAttachmentFileInfo = ciAttachmentFileInfo;
    }

    public List<CiAttachmentFileInfo> getAttachmentFileList() {
        return this.attachmentFileList;
    }

    public void setAttachmentFileList(List<CiAttachmentFileInfo> attachmentFileList) {
        this.attachmentFileList = attachmentFileList;
    }

    public Integer getFileId() {
        return this.fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
