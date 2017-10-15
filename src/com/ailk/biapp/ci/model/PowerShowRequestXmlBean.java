package com.ailk.biapp.ci.model;

import com.ailk.biapp.ci.util.Bean2XMLUtils;
import com.ailk.biapp.ci.util.DateUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PowerShowRequestXmlBean {
    private PowerShowRequestXmlBean.Data data = new PowerShowRequestXmlBean.Data();
    private PowerShowRequestXmlBean.Title title = new PowerShowRequestXmlBean.Title();

    public PowerShowRequestXmlBean() {
    }

    public PowerShowRequestXmlBean.Data getData() {
        return this.data;
    }

    public void setData(PowerShowRequestXmlBean.Data data) {
        this.data = data;
    }

    public PowerShowRequestXmlBean.Title getTitle() {
        return this.title;
    }

    public void setTitle(PowerShowRequestXmlBean.Title title) {
        this.title = title;
    }

    public void setInterfaceAddress(String interfaceAddress) {
        this.title.interfaceAddress = interfaceAddress;
    }

    public String toString() {
        return "PowerShowRequestXmlBean [data=" + this.data + ", title=" + this.title + "]";
    }

    public static void main(String[] arg) {
        PowerShowRequestXmlBean bean = new PowerShowRequestXmlBean();
        bean.getTitle().setInterfaceAddress("10.,11..1.");
        bean.getTitle().setSendTime(new Date());
        bean.getData().setSyncTaskId("taskId");
        bean.getData().setPlatformCode("COC");
        bean.getData().setUploadFileName("djafdjajfsdlaf.zip");
        bean.getData().setRowNumber(Long.valueOf("122222222222").toString());
        bean.getData().setApplicant("admin");
        Calendar c = Calendar.getInstance();
        c.add(5, -3);
        bean.getData().setExpireDate(DateUtil.date2String(c.getTime(), "yyyy-MM-dd"));
        bean.getData().setLinkId("1");
        bean.getData().setTarget("客户微分");
        PowerShowRequestXmlBean.Column column = new PowerShowRequestXmlBean.Column();
        column.setColumnName("手机号码");
        column.setColumnLength("11");
        bean.getData().addColumn(column);
        PowerShowRequestXmlBean.Column column2 = new PowerShowRequestXmlBean.Column();
        column2.setColumnType("NUMBER");
        column2.setColumnName("消费值");
        column2.setColumnLength("11");
        bean.getData().addColumn(column2);
        String xmlStr = null;

        try {
            xmlStr = Bean2XMLUtils.bean2XmlString(bean);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        System.out.println(xmlStr);
    }

    public static class Column {
        private String columnName;
        private String columnType;
        private String columnLength;

        public Column() {
        }

        public String getColumnName() {
            return this.columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnType() {
            return this.columnType;
        }

        public void setColumnType(String columnType) {
            this.columnType = columnType;
        }

        public String getColumnLength() {
            return this.columnLength;
        }

        public void setColumnLength(String columnLength) {
            this.columnLength = columnLength;
        }

        public String toString() {
            return "Column [columnName=" + this.columnName + ", columnType=" + this.columnType + ", columnLength=" + this.columnLength + "]";
        }
    }

    public class Data {
        private String syncTaskId;
        private String platformCode;
        private String dataBaseCode;
        private String uploadFileName;
        private String rowNumber;
        private String applicant;
        private String uploadFileDesc;
        private String expireDate;
        private String linkId;
        private String target;
        private List<PowerShowRequestXmlBean.Column> columns = new ArrayList();

        public Data() {
        }

        public String toString() {
            return "Data [syncTaskId=" + this.syncTaskId + ", platformCode=" + this.platformCode + ", dataBaseCode=" + this.dataBaseCode + ", uploadFileName=" + this.uploadFileName + ", rowNumber=" + this.rowNumber + ", applicant=" + this.applicant + ", uploadFileDesc=" + this.uploadFileDesc + ", expireDate=" + this.expireDate + ", linkId=" + this.linkId + ", target=" + this.target + ", columns=" + this.columns + "]";
        }

        public List<PowerShowRequestXmlBean.Column> getColumns() {
            return this.columns;
        }

        public void setColumns(List<PowerShowRequestXmlBean.Column> columns) {
            this.columns = columns;
        }

        public void addColumn(PowerShowRequestXmlBean.Column column) {
            this.columns.add(column);
        }

        public String getSyncTaskId() {
            return this.syncTaskId;
        }

        public void setSyncTaskId(String syncTaskId) {
            this.syncTaskId = syncTaskId;
        }

        public String getPlatformCode() {
            return this.platformCode;
        }

        public void setPlatformCode(String platformCode) {
            this.platformCode = platformCode;
        }

        public String getDataBaseCode() {
            return this.dataBaseCode;
        }

        public void setDataBaseCode(String dataBaseCode) {
            this.dataBaseCode = dataBaseCode;
        }

        public String getUploadFileName() {
            return this.uploadFileName;
        }

        public void setUploadFileName(String uploadFileName) {
            this.uploadFileName = uploadFileName;
        }

        public String getRowNumber() {
            return this.rowNumber;
        }

        public void setRowNumber(String rowNumber) {
            this.rowNumber = rowNumber;
        }

        public String getApplicant() {
            return this.applicant;
        }

        public void setApplicant(String applicant) {
            this.applicant = applicant;
        }

        public String getUploadFileDesc() {
            return this.uploadFileDesc;
        }

        public void setUploadFileDesc(String uploadFileDesc) {
            this.uploadFileDesc = uploadFileDesc;
        }

        public String getExpireDate() {
            return this.expireDate;
        }

        public void setExpireDate(String expireDate) {
            this.expireDate = expireDate;
        }

        public String getLinkId() {
            return this.linkId;
        }

        public void setLinkId(String linkId) {
            this.linkId = linkId;
        }

        public String getTarget() {
            return this.target;
        }

        public void setTarget(String target) {
            this.target = target;
        }
    }

    public class Title {
        private String interfaceAddress;
        private Date sendTime;

        public Title() {
        }

        public String getInterfaceAddress() {
            return this.interfaceAddress;
        }

        public void setInterfaceAddress(String interfaceAddress) {
            this.interfaceAddress = interfaceAddress;
        }

        public Date getSendTime() {
            return this.sendTime;
        }

        public void setSendTime(Date sendTime) {
            this.sendTime = sendTime;
        }

        public String toString() {
            return "Title [interfaceAddress=" + this.interfaceAddress + ", sendTime=" + this.sendTime + "]";
        }
    }
}
