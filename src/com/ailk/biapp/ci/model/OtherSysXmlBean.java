package com.ailk.biapp.ci.model;

import com.ailk.biapp.ci.util.Bean2XMLUtils;
import java.io.Serializable;
import java.util.Date;

public class OtherSysXmlBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private OtherSysXmlBean.Data data = new OtherSysXmlBean.Data();
    private OtherSysXmlBean.Title title = new OtherSysXmlBean.Title();

    public OtherSysXmlBean() {
    }

    public OtherSysXmlBean.Data getData() {
        return this.data;
    }

    public void setData(OtherSysXmlBean.Data data) {
        this.data = data;
    }

    public OtherSysXmlBean.Title getTitle() {
        return this.title;
    }

    public void setTitle(OtherSysXmlBean.Title title) {
        this.title = title;
    }

    public String toString() {
        return "OtherSysXmlBean [data=" + this.data + ", title=" + this.title + "]";
    }

    public static void main(String[] arg) {
        OtherSysXmlBean bean = new OtherSysXmlBean();
        bean.getTitle().setTaskDesc("广告平台推送任务");
        bean.getTitle().setSendTime(new Date());
        bean.getData().setReqId("COC20130717151315349");
        bean.getData().setPlatformCode("COC");
        bean.getData().setUploadFileName("COC_UserId_20130717151315349.zip");
        bean.getData().setRowNumber(Long.valueOf("23456").toString());
        bean.getData().setUserId("admin");
        bean.getData().setMD5Check("b73723ebd0ad59a49d108b59c27aa7b5");
        bean.getData().setCustomName("客户群名称");
        bean.getData().setDataTime("201306");
        String xmlStr = null;

        try {
            xmlStr = Bean2XMLUtils.bean2XmlString(bean);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        System.out.println(xmlStr);
    }

    public class Data {
        private String reqId;
        private String platformCode;
        private String uploadFileName;
        private String MD5Check;
        private String rowNumber;
        private String userId;
        private String customName;
        private String dataTime;
        private String customDesc;
        private String customRules;
        private String usedLabelOrProduct;

        public Data() {
        }

        public String getReqId() {
            return this.reqId;
        }

        public void setReqId(String reqId) {
            this.reqId = reqId;
        }

        public String getPlatformCode() {
            return this.platformCode;
        }

        public void setPlatformCode(String platformCode) {
            this.platformCode = platformCode;
        }

        public String getUploadFileName() {
            return this.uploadFileName;
        }

        public void setUploadFileName(String uploadFileName) {
            this.uploadFileName = uploadFileName;
        }

        public String getMD5Check() {
            return this.MD5Check;
        }

        public void setMD5Check(String MD5Check) {
            this.MD5Check = MD5Check;
        }

        public String getRowNumber() {
            return this.rowNumber;
        }

        public void setRowNumber(String rowNumber) {
            this.rowNumber = rowNumber;
        }

        public String getUserId() {
            return this.userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCustomName() {
            return this.customName;
        }

        public void setCustomName(String customName) {
            this.customName = customName;
        }

        public String getDataTime() {
            return this.dataTime;
        }

        public void setDataTime(String dataTime) {
            this.dataTime = dataTime;
        }

        public String getCustomDesc() {
            return this.customDesc;
        }

        public void setCustomDesc(String customDesc) {
            this.customDesc = customDesc;
        }

        public String getCustomRules() {
            return this.customRules;
        }

        public void setCustomRules(String customRules) {
            this.customRules = customRules;
        }

        public String getUsedLabelOrProduct() {
            return this.usedLabelOrProduct;
        }

        public void setUsedLabelOrProduct(String usedLabelOrProduct) {
            this.usedLabelOrProduct = usedLabelOrProduct;
        }

        public String toString() {
            return "Data [reqId=" + this.reqId + ", platformCode=" + this.platformCode + ", uploadFileName=" + this.uploadFileName + ", MD5Check=" + this.MD5Check + ", rowNumber=" + this.rowNumber + ", userId=" + this.userId + ", customName=" + this.customName + ", dataTime=" + this.dataTime + ", customDesc=" + this.customDesc + ", customRules=" + this.customRules + ", usedLabelOrProduct=" + this.usedLabelOrProduct + "]";
        }
    }

    public class Title {
        private String taskDesc;
        private Date sendTime;

        public Title() {
        }

        public String getTaskDesc() {
            return this.taskDesc;
        }

        public void setTaskDesc(String taskDesc) {
            this.taskDesc = taskDesc;
        }

        public Date getSendTime() {
            return this.sendTime;
        }

        public void setSendTime(Date sendTime) {
            this.sendTime = sendTime;
        }

        public String toString() {
            return "Title [taskDesc=" + this.taskDesc + ", sendTime=" + this.sendTime + "]";
        }
    }
}
