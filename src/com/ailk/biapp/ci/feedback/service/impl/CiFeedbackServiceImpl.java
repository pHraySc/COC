package com.ailk.biapp.ci.feedback.service.impl;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.ICiCustomGroupInfoHDao;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.feedback.dao.ICiAttachmentFileInfoHDao;
import com.ailk.biapp.ci.feedback.dao.ICiFeedbackInfoHDao;
import com.ailk.biapp.ci.feedback.dao.ICiFeedbackInfoJDao;
import com.ailk.biapp.ci.feedback.dao.ICiFeedbackRecordInfoHDao;
import com.ailk.biapp.ci.feedback.dao.ICiFeedbackRecordInfoJDao;
import com.ailk.biapp.ci.feedback.dao.IDimDeclareDealStatusHDao;
import com.ailk.biapp.ci.feedback.entity.CiAttachmentFileInfo;
import com.ailk.biapp.ci.feedback.entity.CiFeedbackInfo;
import com.ailk.biapp.ci.feedback.entity.CiFeedbackRecordInfo;
import com.ailk.biapp.ci.feedback.entity.DimDeclareDealStatus;
import com.ailk.biapp.ci.feedback.service.ICiFeedbackService;
import com.ailk.biapp.ci.model.TreeNode;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.FileUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiFeedbackServiceImpl implements ICiFeedbackService {
    private Logger log = Logger.getLogger(CiFeedbackServiceImpl.class);
    @Autowired
    private ICiFeedbackInfoJDao ciFeedbackInfoJDao;
    @Autowired
    private ICiFeedbackInfoHDao ciFeedbackInfoHDao;
    @Autowired
    private ICiFeedbackRecordInfoHDao ciFeedbackRecordInfoHDao;
    @Autowired
    private ICiAttachmentFileInfoHDao ciAttachmentFileInfoHDao;
    @Autowired
    private IDimDeclareDealStatusHDao dimDeclareDealStatusHDao;
    @Autowired
    private ICiFeedbackRecordInfoJDao ciFeedbackRecordInfoJDao;
    @Autowired
    private ICiCustomGroupInfoHDao ciCustomGroupInfoHDao;

    public CiFeedbackServiceImpl() {
    }

    public int queryTotolPageCount(CiFeedbackInfo ciFeedbackInfo) throws CIServiceException {
        boolean count = false;

        try {
            int count1 = this.ciFeedbackInfoJDao.getFeedbackCountBySql(ciFeedbackInfo);
            return count1;
        } catch (Exception var4) {
            this.log.error("查询反馈信息总数错误", var4);
            throw new CIServiceException("查询反馈信息总数错误");
        }
    }

    public int queryHistoryTotalPageCount(CiFeedbackInfo ciFeedbackInfo) throws CIServiceException {
        boolean count = false;

        try {
            int count1 = this.ciFeedbackInfoJDao.getFeedbackHistoryCountBySql(ciFeedbackInfo);
            return count1;
        } catch (Exception var4) {
            this.log.error("查询历史反馈信息总数错误", var4);
            throw new CIServiceException("查询历史反馈信息总数错误");
        }
    }

    public List<CiFeedbackInfo> queryPageList(int currPage, int pageSize, CiFeedbackInfo ciFeedbackInfo) throws CIServiceException {
        List pageList = null;

        try {
            String e = PrivilegeServiceUtil.getUserId();
            pageList = this.ciFeedbackInfoJDao.getFeedbackPageListBySql(currPage, pageSize, ciFeedbackInfo);
            if(pageList != null) {
                Iterator iter = pageList.iterator();

                while(iter.hasNext()) {
                    CiFeedbackInfo feedbackInfo = (CiFeedbackInfo)iter.next();
                    if(StringUtil.isNotEmpty(feedbackInfo.getUserId())) {
                        feedbackInfo.setUserName(PrivilegeServiceUtil.getUserById(feedbackInfo.getUserId()).getUsername());
                        if(e.equals(feedbackInfo.getUserId())) {
                            feedbackInfo.setIsCreateUser(true);
                        } else {
                            feedbackInfo.setIsCreateUser(false);
                        }
                    }

                    if(StringUtil.isNotEmpty(feedbackInfo.getReplyUserId())) {
                        feedbackInfo.setReplyUserName(PrivilegeServiceUtil.getUserById(feedbackInfo.getReplyUserId()).getUsername());
                    }
                }
            }

            return pageList;
        } catch (Exception var8) {
            this.log.error("查询反馈信息分页记录错误", var8);
            throw new CIServiceException("查询反馈信息分页记录错误");
        }
    }

    public List<CiFeedbackInfo> queryHistoryPageList(int currPage, int pageSize, CiFeedbackInfo ciFeedbackInfo) throws CIServiceException {
        List pageList = null;

        try {
            pageList = this.ciFeedbackInfoJDao.getFeedbackHistorytListBySql(currPage, pageSize, ciFeedbackInfo);
            if(pageList != null) {
                Iterator e = pageList.iterator();

                while(e.hasNext()) {
                    CiFeedbackInfo feedbackInfo = (CiFeedbackInfo)e.next();
                    if(StringUtil.isNotEmpty(feedbackInfo.getUserId())) {
                        feedbackInfo.setUserName(PrivilegeServiceUtil.getUserById(feedbackInfo.getUserId()).getUsername());
                    }

                    if(StringUtil.isNotEmpty(feedbackInfo.getReplyUserId())) {
                        feedbackInfo.setReplyUserName(PrivilegeServiceUtil.getUserById(feedbackInfo.getReplyUserId()).getUsername());
                    }
                }
            }

            return pageList;
        } catch (Exception var7) {
            this.log.error("查询历史反馈信息分页记录错误", var7);
            throw new CIServiceException("查询历史反馈信息分页记录错误");
        }
    }

    public List<CiFeedbackRecordInfo> queryFeedbackRecord(Integer feedbackInfoId) throws CIServiceException {
        List pageList = null;
        new CiFeedbackInfo();

        try {
            CiFeedbackInfo ciFeedbackInfo = this.ciFeedbackInfoHDao.selectFeedbackInfoById(feedbackInfoId);
            pageList = this.ciFeedbackRecordInfoHDao.selectByFeedbackInfoId(feedbackInfoId);
            if(pageList != null && pageList.size() > 0) {
                String e = "";

                for(int i = 0; i < pageList.size(); ++i) {
                    CiFeedbackRecordInfo feedbackRecord = (CiFeedbackRecordInfo)pageList.get(i);
                    String recordInfoDay = DateUtil.timeStamp2String(feedbackRecord.getReplyTime(), "yyyy-MM-dd");
                    if(i == 0 && StringUtil.isEmpty(e)) {
                        e = recordInfoDay;
                        feedbackRecord.setDay(recordInfoDay);
                    } else if(!e.equals(recordInfoDay)) {
                        e = recordInfoDay;
                        feedbackRecord.setDay(recordInfoDay);
                    }

                    if(ServiceConstants.FEEDBACK_REPLY_TYPE_RAISE == feedbackRecord.getReplyType().intValue()) {
                        feedbackRecord.setUserNme(PrivilegeServiceUtil.getUserById(ciFeedbackInfo.getUserId()).getUsername());
                        List ciAttachmentFileList = this.ciAttachmentFileInfoHDao.selectByRecordId(feedbackRecord.getRecordId());
                        feedbackRecord.setAttachmentList(ciAttachmentFileList);
                    }

                    if(ServiceConstants.FEEDBACK_REPLY_TYPE_REPLY == feedbackRecord.getReplyType().intValue()) {
                        feedbackRecord.setReplyName(PrivilegeServiceUtil.getUserById(ciFeedbackInfo.getReplyUserId()).getUsername());
                    }
                }
            }

            return pageList;
        } catch (Exception var9) {
            this.log.error("查询反馈信息回复记录错误", var9);
            throw new CIServiceException("查询反馈信息回复记录记录错误");
        }
    }

    public void insertFeedbackInfo(CiFeedbackInfo ciFeedbackInfo) throws CIServiceException {
        try {
            this.ciFeedbackInfoHDao.insertOrUpdateCiFeedbackInfo(ciFeedbackInfo);
        } catch (Exception var3) {
            this.log.error("新增意见反馈错误", var3);
            throw new CIServiceException("新增意见反馈错误");
        }
    }

    public void insertRecordInfo(CiFeedbackRecordInfo ciFeedbackRecordInfo) throws CIServiceException {
        try {
            this.ciFeedbackRecordInfoHDao.insertCiFeedbackRecordInfo(ciFeedbackRecordInfo);
            List e = ciFeedbackRecordInfo.getAttachmentList();
            if(CollectionUtils.isNotEmpty(e)) {
                Iterator i$ = e.iterator();

                while(i$.hasNext()) {
                    CiAttachmentFileInfo attachment = (CiAttachmentFileInfo)i$.next();
                    Timestamp replyTime = new Timestamp((new Date()).getTime());
                    attachment.setRecordId(ciFeedbackRecordInfo.getRecordId());
                    String fileName = attachment.getFileUrl();
                    String toFileName = fileName.substring(0, fileName.lastIndexOf("."));
                    FileUtil.renameFile(fileName, toFileName);
                    attachment.setFileUrl(toFileName);
                    attachment.setFileUploadTime(replyTime);
                    this.ciAttachmentFileInfoHDao.insertCiAttachmentFileInfo(attachment);
                }
            }

        } catch (Exception var8) {
            this.log.error("插入反馈信息回复错误", var8);
            throw new CIServiceException("插入反馈信息回复错误");
        }
    }

    public CiFeedbackInfo queryByFeedbackInfoById(Integer feedbackInfoId) throws CIServiceException {
        new CiFeedbackInfo();

        try {
            CiFeedbackInfo ciFeedbackInfo = this.ciFeedbackInfoHDao.selectFeedbackInfoById(feedbackInfoId);
            return ciFeedbackInfo;
        } catch (Exception var4) {
            this.log.error("通过ID查询反馈信息错误", var4);
            throw new CIServiceException("通过ID反馈信息回复错误");
        }
    }

    public List<DimDeclareDealStatus> queryDeclareDealStatus() throws CIServiceException {
        List declareDealStatusList = null;

        try {
            declareDealStatusList = this.dimDeclareDealStatusHDao.selectAllStatus();
            return declareDealStatusList;
        } catch (Exception var3) {
            this.log.error("查询意见反馈状态错误", var3);
            throw new CIServiceException("查询意见反馈状态错误");
        }
    }

    public void statusAddToReply(Integer feedbackInfoId) throws CIServiceException {
        new CiFeedbackInfo();

        try {
            CiFeedbackInfo ciFeedbackInfo = this.ciFeedbackInfoHDao.selectFeedbackInfoById(feedbackInfoId);
            ciFeedbackInfo.setDealStatusId(Integer.valueOf(ServiceConstants.FEEDBACK_DEAL_STATUS_REPLY));
            this.ciFeedbackInfoHDao.insertOrUpdateCiFeedbackInfo(ciFeedbackInfo);
            CiFeedbackRecordInfo e = new CiFeedbackRecordInfo();
            e.setReplyTime(new Timestamp((new Date()).getTime()));
            e.setFeedbackInfoId(ciFeedbackInfo.getFeedbackInfoId());
            e.setCurrDealStatusId(Integer.valueOf(ServiceConstants.FEEDBACK_DEAL_STATUS_REPLY));
            this.insertRecordInfo(e);
        } catch (Exception var4) {
            this.log.error("开始处理反馈错误", var4);
            throw new CIServiceException("开始处理反馈错误");
        }
    }

    public void statusAddToCancel(Integer feedbackInfoId) throws CIServiceException {
        new CiFeedbackInfo();

        try {
            CiFeedbackInfo ciFeedbackInfo = this.ciFeedbackInfoHDao.selectFeedbackInfoById(feedbackInfoId);
            ciFeedbackInfo.setDealStatusId(Integer.valueOf(ServiceConstants.FEEDBACK_DEAL_STATUS_CANCEL));
            this.ciFeedbackInfoHDao.insertOrUpdateCiFeedbackInfo(ciFeedbackInfo);
            CiFeedbackRecordInfo e = new CiFeedbackRecordInfo();
            e.setReplyTime(new Timestamp((new Date()).getTime()));
            e.setFeedbackInfoId(ciFeedbackInfo.getFeedbackInfoId());
            this.insertRecordInfo(e);
        } catch (Exception var4) {
            this.log.error("取消反馈错误", var4);
            throw new CIServiceException("取消反馈错误");
        }
    }

    public void statusReplyToFinish(Integer feedbackInfoId) throws CIServiceException {
        new CiFeedbackInfo();

        try {
            CiFeedbackInfo ciFeedbackInfo = this.ciFeedbackInfoHDao.selectFeedbackInfoById(feedbackInfoId);
            ciFeedbackInfo.setDealStatusId(Integer.valueOf(ServiceConstants.FEEDBACK_DEAL_STATUS_FINISH));
            this.ciFeedbackInfoHDao.insertOrUpdateCiFeedbackInfo(ciFeedbackInfo);
            CiFeedbackRecordInfo e = new CiFeedbackRecordInfo();
            e.setReplyTime(new Timestamp((new Date()).getTime()));
            e.setFeedbackInfoId(ciFeedbackInfo.getFeedbackInfoId());
            e.setCurrDealStatusId(Integer.valueOf(ServiceConstants.FEEDBACK_DEAL_STATUS_FINISH));
            this.insertRecordInfo(e);
        } catch (Exception var4) {
            this.log.error("完成处理错误", var4);
            throw new CIServiceException("完成处理错误");
        }
    }

    public void statusFinishToClose(Integer feedbackInfoId) throws CIServiceException {
        new CiFeedbackInfo();

        try {
            CiFeedbackInfo ciFeedbackInfo = this.ciFeedbackInfoHDao.selectFeedbackInfoById(feedbackInfoId);
            ciFeedbackInfo.setDealStatusId(Integer.valueOf(ServiceConstants.FEEDBACK_DEAL_STATUS_CLOSE));
            this.ciFeedbackInfoHDao.insertOrUpdateCiFeedbackInfo(ciFeedbackInfo);
            CiFeedbackRecordInfo e = new CiFeedbackRecordInfo();
            e.setReplyTime(new Timestamp((new Date()).getTime()));
            e.setFeedbackInfoId(ciFeedbackInfo.getFeedbackInfoId());
            this.insertRecordInfo(e);
        } catch (Exception var4) {
            this.log.error("关闭反馈错误", var4);
            throw new CIServiceException("关闭反馈错误");
        }
    }

    public boolean hasSameFeedback(CiFeedbackInfo ciFeedbackInfo) throws CIServiceException {
        List list = this.ciFeedbackInfoHDao.selectByUserIdAndLabelCustomId(ciFeedbackInfo);
        return !CollectionUtils.isNotEmpty(list);
    }

    public String uploadFile(File file, String fileName) throws CIServiceException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(date);
        String path = "";
        String savePath = Configure.getInstance().getProperty("FEEDBACK_ATTACHMENT_UPLOAD_LOCATION") + dateNowStr;

        try {
            path = CiUtil.uploadFile(file, savePath, fileName, false);
            return path;
        } catch (Exception var9) {
            this.log.error("附件上传错误", var9);
            throw new CIServiceException("附件上传错误");
        }
    }

    public CiAttachmentFileInfo queryAttachmentFileById(Integer fileId) throws CIServiceException {
        new CiAttachmentFileInfo();

        try {
            CiAttachmentFileInfo ciAttachmentFileInfo = this.ciAttachmentFileInfoHDao.selectByFileId(fileId);
            return ciAttachmentFileInfo;
        } catch (Exception var4) {
            this.log.error("通过Id查询附件信息错误", var4);
            throw new CIServiceException("通过Id查询附件信息错误");
        }
    }

    public List<TreeNode> queryCustomerGroupTree(String userId) throws CIServiceException {
        ArrayList treeList = new ArrayList();

        try {
            List e = this.ciCustomGroupInfoHDao.selectPublicCustomGroup(userId);
            Iterator i$ = e.iterator();

            while(i$.hasNext()) {
                CiCustomGroupInfo customGroupInfo = (CiCustomGroupInfo)i$.next();
                TreeNode treeNode = new TreeNode();
                treeNode.setId(customGroupInfo.getCustomGroupId());
                treeNode.setpId(customGroupInfo.getParentCustomId());
                treeNode.setName(customGroupInfo.getCustomGroupName());
                treeList.add(treeNode);
            }

            return treeList;
        } catch (Exception var7) {
            this.log.error("查询公有客户群并生成树错误", var7);
            throw new CIServiceException("查询公有客户群并生成树错误");
        }
    }

    public void modifyFeedbackByFeedbackRecord() throws CIServiceException {
        try {
            new CiFeedbackInfo();
            List list = this.ciFeedbackRecordInfoJDao.getFeedbackRecordInfo();
            if(CollectionUtils.isNotEmpty(list)) {
                Iterator iter = list.iterator();

                while(iter.hasNext()) {
                    CiFeedbackRecordInfo ciFeedbackRecordInfo = (CiFeedbackRecordInfo)iter.next();
                    CiFeedbackInfo e = this.ciFeedbackInfoHDao.selectFeedbackInfoById(ciFeedbackRecordInfo.getFeedbackInfoId());
                    e.setDealStatusId(Integer.valueOf(ServiceConstants.FEEDBACK_DEAL_STATUS_CLOSE));
                    this.insertFeedbackInfo(e);
                    CiFeedbackRecordInfo clone = new CiFeedbackRecordInfo();
                    clone.setReplyTime(new Timestamp((new Date()).getTime()));
                    clone.setFeedbackInfoId(e.getFeedbackInfoId());
                    this.insertRecordInfo(clone);
                }
            }

        } catch (Exception var6) {
            this.log.error("修改意见反馈状态错误", var6);
            throw new CIServiceException("修改意见反馈状态错误");
        }
    }

    public void modifyFeedbackStatus() throws CIServiceException {
        try {
            new CiFeedbackInfo();
            List list = this.ciFeedbackRecordInfoJDao.getCancelOrCloseFeedbackList();
            if(CollectionUtils.isNotEmpty(list)) {
                Iterator iter = list.iterator();

                while(iter.hasNext()) {
                    CiFeedbackRecordInfo ciFeedbackRecordInfo = (CiFeedbackRecordInfo)iter.next();
                    CiFeedbackInfo e = this.ciFeedbackInfoHDao.selectFeedbackInfoById(ciFeedbackRecordInfo.getFeedbackInfoId());
                    e.setStatus(Integer.valueOf(ServiceConstants.FEEDBACK_STATUS_DEL));
                    this.insertFeedbackInfo(e);
                    List ciAttachmentList = this.ciAttachmentFileInfoHDao.selectByRecordId(ciFeedbackRecordInfo.getRecordId());
                    Iterator attachmentIter = ciAttachmentList.iterator();

                    while(attachmentIter.hasNext()) {
                        CiAttachmentFileInfo ciAttachmentFileInfo = (CiAttachmentFileInfo)attachmentIter.next();
                        CiUtil.deleteFile(ciAttachmentFileInfo.getFileUrl());
                    }
                }
            }

        } catch (Exception var8) {
            this.log.error("逻辑删除反馈信息错误", var8);
            throw new CIServiceException("逻辑删除反馈信息错误");
        }
    }

    public void cleanInvalidAttachement() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(date);
        String path = Configure.getInstance().getProperty("FEEDBACK_ATTACHMENT_UPLOAD_LOCATION") + dateNowStr;

        try {
            File[] e = FileUtil.getFileList(path);
            String str = ".tmp";

            for(int i = 0; i < e.length; ++i) {
                File file = e[i];
                String fileName = file.getName();
                String subStr = fileName.substring(fileName.lastIndexOf("."));
                if(str.equals(subStr)) {
                    CiUtil.deleteFile(file.getPath());
                }
            }

        } catch (Exception var11) {
            this.log.error("附件上传错误", var11);
            throw new CIServiceException("附件上传错误");
        }
    }

    public void deleteFeedbackInfo(CiFeedbackInfo ciFeedbackInfo) throws CIServiceException {
        this.ciFeedbackInfoHDao.deleteFeedbackInfo(ciFeedbackInfo);
    }
}
