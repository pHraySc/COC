package com.ailk.biapp.ci.feedback.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class CiAttachmentFileInfo implements Serializable {
    private Integer fileId;
    private Integer recordId;
    private String fileName;
    private String fileOldName;
    private String fileUrl;
    private String fileType;
    private Timestamp fileUploadTime;

    public CiAttachmentFileInfo() {
    }

    public CiAttachmentFileInfo(Integer recordId, String fileName, String fileOldName, String fileUrl, String fileType, Timestamp fileStartTime, Timestamp fileEndTime) {
        this.recordId = recordId;
        this.fileName = fileName;
        this.fileOldName = fileOldName;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.fileUploadTime = fileStartTime;
    }

    public Integer getFileId() {
        return this.fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getRecordId() {
        return this.recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileOldName() {
        return this.fileOldName;
    }

    public void setFileOldName(String fileOldName) {
        this.fileOldName = fileOldName;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileType() {
        return this.fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Timestamp getFileUploadTime() {
        return this.fileUploadTime;
    }

    public void setFileUploadTime(Timestamp fileUploadTime) {
        this.fileUploadTime = fileUploadTime;
    }
}
