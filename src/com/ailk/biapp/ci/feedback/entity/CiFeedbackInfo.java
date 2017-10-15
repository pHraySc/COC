package com.ailk.biapp.ci.feedback.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class CiFeedbackInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer feedbackInfoId;
    private String userId;
    private String labelCustomId;
    private Timestamp feedbackTime;
    private Integer feedbackType;
    private String feedbackTitle;
    private String feedbackInfo;
    private String replyUserId;
    private Integer dealStatusId;
    private Integer status;
    private String userName;
    private String replyUserName;
    private String labelName;
    private String customGroupName;
    private Date startDate;
    private Date endDate;
    private boolean canManageDeclare;
    private boolean isCreateUser;
    private int sign;

    public CiFeedbackInfo() {
    }

    public CiFeedbackInfo(String userId, String labelCustomId, Timestamp feedbackTime, Integer feedbackType, String feedbackTitle, String feedbackInfo, String replyUserId, Integer dealStatusId, Integer status) {
        this.userId = userId;
        this.labelCustomId = labelCustomId;
        this.feedbackTime = feedbackTime;
        this.feedbackType = feedbackType;
        this.feedbackTitle = feedbackTitle;
        this.feedbackInfo = feedbackInfo;
        this.replyUserId = replyUserId;
        this.dealStatusId = dealStatusId;
        this.status = status;
    }

    public Integer getFeedbackInfoId() {
        return this.feedbackInfoId;
    }

    public void setFeedbackInfoId(Integer feedbackInfoId) {
        this.feedbackInfoId = feedbackInfoId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLabelCustomId() {
        return this.labelCustomId;
    }

    public void setLabelCustomId(String labelCustomId) {
        this.labelCustomId = labelCustomId;
    }

    public Timestamp getFeedbackTime() {
        return this.feedbackTime;
    }

    public void setFeedbackTime(Timestamp feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public Integer getFeedbackType() {
        return this.feedbackType;
    }

    public void setFeedbackType(Integer feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getFeedbackTitle() {
        return this.feedbackTitle;
    }

    public void setFeedbackTitle(String feedbackTitle) {
        this.feedbackTitle = feedbackTitle;
    }

    public String getFeedbackInfo() {
        return this.feedbackInfo;
    }

    public void setFeedbackInfo(String feedbackInfo) {
        this.feedbackInfo = feedbackInfo;
    }

    public String getReplyUserId() {
        return this.replyUserId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
    }

    public Integer getDealStatusId() {
        return this.dealStatusId;
    }

    public void setDealStatusId(Integer dealStatusId) {
        this.dealStatusId = dealStatusId;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReplyUserName() {
        return this.replyUserName;
    }

    public void setReplyUserName(String replyUserName) {
        this.replyUserName = replyUserName;
    }

    public boolean getCanManageDeclare() {
        return this.canManageDeclare;
    }

    public void setCanManageDeclare(boolean canManageDeclare) {
        this.canManageDeclare = canManageDeclare;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getLabelName() {
        return this.labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public boolean getIsCreateUser() {
        return this.isCreateUser;
    }

    public void setIsCreateUser(boolean isCreateUser) {
        this.isCreateUser = isCreateUser;
    }

    public String getCustomGroupName() {
        return this.customGroupName;
    }

    public void setCustomGroupName(String customGroupName) {
        this.customGroupName = customGroupName;
    }

    public int getSign() {
        return this.sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }
}
