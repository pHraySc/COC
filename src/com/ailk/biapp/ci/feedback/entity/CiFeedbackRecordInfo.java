package com.ailk.biapp.ci.feedback.entity;

import com.ailk.biapp.ci.feedback.entity.CiAttachmentFileInfo;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class CiFeedbackRecordInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer recordId;
    private Integer feedbackInfoId;
    private String replyInfo;
    private Timestamp replyTime;
    private Integer replyType;
    private Integer currDealStatusId;
    private String dealUserConnc;
    private String dealUser;
    private String userNme;
    private String replyName;
    private String day;
    private List<CiAttachmentFileInfo> attachmentList;

    public CiFeedbackRecordInfo() {
    }

    public CiFeedbackRecordInfo(Integer feedbackInfoId, String replyInfo, Timestamp replyTime, Integer replyType, Integer currDealStatusId, String dealUserConnc, String dealUser) {
        this.feedbackInfoId = feedbackInfoId;
        this.replyInfo = replyInfo;
        this.replyTime = replyTime;
        this.replyType = replyType;
        this.currDealStatusId = currDealStatusId;
        this.dealUserConnc = dealUserConnc;
        this.dealUser = dealUser;
    }

    public Integer getRecordId() {
        return this.recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getFeedbackInfoId() {
        return this.feedbackInfoId;
    }

    public void setFeedbackInfoId(Integer feedbackInfoId) {
        this.feedbackInfoId = feedbackInfoId;
    }

    public String getReplyInfo() {
        return this.replyInfo;
    }

    public void setReplyInfo(String replyInfo) {
        this.replyInfo = replyInfo;
    }

    public Timestamp getReplyTime() {
        return this.replyTime;
    }

    public void setReplyTime(Timestamp replyTime) {
        this.replyTime = replyTime;
    }

    public Integer getReplyType() {
        return this.replyType;
    }

    public void setReplyType(Integer replyType) {
        this.replyType = replyType;
    }

    public Integer getCurrDealStatusId() {
        return this.currDealStatusId;
    }

    public void setCurrDealStatusId(Integer currDealStatusId) {
        this.currDealStatusId = currDealStatusId;
    }

    public String getDealUserConnc() {
        return this.dealUserConnc;
    }

    public void setDealUserConnc(String dealUserConnc) {
        this.dealUserConnc = dealUserConnc;
    }

    public String getDealUser() {
        return this.dealUser;
    }

    public void setDealUser(String dealUser) {
        this.dealUser = dealUser;
    }

    public String getUserNme() {
        return this.userNme;
    }

    public void setUserNme(String userNme) {
        this.userNme = userNme;
    }

    public String getReplyName() {
        return this.replyName;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }

    public List<CiAttachmentFileInfo> getAttachmentList() {
        return this.attachmentList;
    }

    public void setAttachmentList(List<CiAttachmentFileInfo> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public String getDay() {
        return this.day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
