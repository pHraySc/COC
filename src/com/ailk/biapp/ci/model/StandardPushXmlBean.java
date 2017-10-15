package com.ailk.biapp.ci.model;

import com.ailk.biapp.ci.util.Bean2XMLUtils;
import com.asiainfo.biframe.utils.config.Configure;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StandardPushXmlBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private StandardPushXmlBean.Data data = new StandardPushXmlBean.Data();
    private StandardPushXmlBean.Title title = new StandardPushXmlBean.Title();

    public StandardPushXmlBean() {
    }

    public StandardPushXmlBean.Data getData() {
        return this.data;
    }

    public void setData(StandardPushXmlBean.Data data) {
        this.data = data;
    }

    public StandardPushXmlBean.Title getTitle() {
        return this.title;
    }

    public void setTitle(StandardPushXmlBean.Title title) {
        this.title = title;
    }

    public String toString() {
        return "StandardPushXmlBean [data=" + this.data + ", title=" + this.title + "]";
    }

    public static void main(String[] arg) {
        StandardPushXmlBean bean = new StandardPushXmlBean();
        bean.getTitle().setTaskDesc("广告平台推送任务");
        bean.getTitle().setSendTime(new Date());
        bean.getData().setReqId("COC20130717151315349");
        bean.getData().setPlatformCode("COC");
        bean.getData().setRowNumber(Long.valueOf(23456L));
        bean.getData().setUserId("admin");
        bean.getData().setUploadFileName("COC_UserId_20130717151315349.zip");
        bean.getData().setUploadFileType("zip");
        bean.getData().setUploadFileDesc("描述一下");
        bean.getData().setDataCycle(Integer.valueOf(2));
        bean.getData().setDataCycleDesc("月周期");
        bean.getData().setCustomGroupId("KHQ57100000388");
        bean.getData().setCustomGroupName("客户群名称");
        bean.getData().setCustomGroupDesc("客户群描述");
        bean.getData().setDataDate("201408");
        String product_no = Configure.getInstance().getProperty("RELATED_COLUMN");
        ArrayList columns = new ArrayList();
        StandardPushXmlBean.Data.Column col1 = bean.getData().newColumn();
        col1.setColumnName(product_no);
        col1.setColumnCnName("手机号");
        col1.setColumnDataType("varchar2(32)");
        col1.setIsPrimaryKey(Integer.valueOf(1));
        columns.add(col1);
        StandardPushXmlBean.Data.Column col2 = bean.getData().newColumn();
        col2.setColumnName(product_no);
        col2.setColumnCnName("手机号");
        col2.setColumnDataType("varchar2(32)");
        col2.setIsPrimaryKey(Integer.valueOf(1));
        columns.add(col2);
        bean.getData().setColumns(columns);
        String xmlStr = null;

        try {
            xmlStr = Bean2XMLUtils.bean2XmlString(bean);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        System.out.println(xmlStr);
    }

    public class Data {
        private String reqId;
        private String platformCode;
        private Long rowNumber;
        private String userId;
        private String uploadFileName;
        private String uploadFileType;
        private String uploadFileDesc;
        private Integer dataCycle;
        private String dataCycleDesc;
        private String customGroupId;
        private String customGroupName;
        private String customGroupDesc;
        private String dataDate;
        private List<StandardPushXmlBean.Data.Column> columns;
        private String customRules;
        private String crtPersnName;
        private Date crtTime;
        private Date effective_time;
        private Date fail_time;
        private String pushToUserIds;
        private String createCityId;

        public Data() {
        }

        public void addColumn(StandardPushXmlBean.Data.Column column) {
            if(this.columns == null) {
                this.columns = new ArrayList();
            }

            this.columns.add(column);
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

        public Long getRowNumber() {
            return this.rowNumber;
        }

        public void setRowNumber(Long rowNumber) {
            this.rowNumber = rowNumber;
        }

        public String getUserId() {
            return this.userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUploadFileName() {
            return this.uploadFileName;
        }

        public void setUploadFileName(String uploadFileName) {
            this.uploadFileName = uploadFileName;
        }

        public String getUploadFileType() {
            return this.uploadFileType;
        }

        public void setUploadFileType(String uploadFileType) {
            this.uploadFileType = uploadFileType;
        }

        public String getUploadFileDesc() {
            return this.uploadFileDesc;
        }

        public void setUploadFileDesc(String uploadFileDesc) {
            this.uploadFileDesc = uploadFileDesc;
        }

        public Integer getDataCycle() {
            return this.dataCycle;
        }

        public void setDataCycle(Integer dataCycle) {
            this.dataCycle = dataCycle;
        }

        public String getDataCycleDesc() {
            return this.dataCycleDesc;
        }

        public void setDataCycleDesc(String dataCycleDesc) {
            this.dataCycleDesc = dataCycleDesc;
        }

        public String getCustomGroupId() {
            return this.customGroupId;
        }

        public void setCustomGroupId(String customGroupId) {
            this.customGroupId = customGroupId;
        }

        public String getCustomGroupName() {
            return this.customGroupName;
        }

        public void setCustomGroupName(String customGroupName) {
            this.customGroupName = customGroupName;
        }

        public String getCustomGroupDesc() {
            return this.customGroupDesc;
        }

        public void setCustomGroupDesc(String customGroupDesc) {
            this.customGroupDesc = customGroupDesc;
        }

        public String getDataDate() {
            return this.dataDate;
        }

        public void setDataDate(String dataDate) {
            this.dataDate = dataDate;
        }

        public List<StandardPushXmlBean.Data.Column> getColumns() {
            return this.columns;
        }

        public void setColumns(List<StandardPushXmlBean.Data.Column> columns) {
            this.columns = columns;
        }

        public String getCustomRules() {
            return this.customRules;
        }

        public void setCustomRules(String customRules) {
            this.customRules = customRules;
        }

        public String getCrtPersnName() {
            return this.crtPersnName;
        }

        public void setCrtPersnName(String crtPersnName) {
            this.crtPersnName = crtPersnName;
        }

        public Date getCrtTime() {
            return this.crtTime;
        }

        public void setCrtTime(Date crtTime) {
            this.crtTime = crtTime;
        }

        public Date getEffective_time() {
            return this.effective_time;
        }

        public void setEffective_time(Date effective_time) {
            this.effective_time = effective_time;
        }

        public Date getFail_time() {
            return this.fail_time;
        }

        public void setFail_time(Date fail_time) {
            this.fail_time = fail_time;
        }

        public String getPushToUserIds() {
            return this.pushToUserIds;
        }

        public void setPushToUserIds(String pushToUserIds) {
            this.pushToUserIds = pushToUserIds;
        }

        public String getCreateCityId() {
            return this.createCityId;
        }

        public void setCreateCityId(String createCityId) {
            this.createCityId = createCityId;
        }

        public String toString() {
            return "Data [reqId=" + this.reqId + ", platformCode=" + this.platformCode + ", rowNumber=" + this.rowNumber + ", userId=" + this.userId + ", uploadFileName=" + this.uploadFileName + ", uploadFileType=" + this.uploadFileType + ", uploadFileDesc=" + this.uploadFileDesc + ", dataCycle=" + this.dataCycle + ", dataCycleDesc=" + this.dataCycleDesc + ", customGroupId=" + this.customGroupId + ", customGroupName=" + this.customGroupName + ", customGroupDesc=" + this.customGroupDesc + ", dataDate=" + this.dataDate + ", columns=" + this.columns + "]";
        }

        public StandardPushXmlBean.Data.Column newColumn() {
            StandardPushXmlBean.Data.Column col = new StandardPushXmlBean.Data.Column();
            return col;
        }

        public class Column {
            private String columnName;
            private String columnCnName;
            private String columnDataType;
            private String columnLength;
            private Integer isPrimaryKey;

            public Column() {
            }

            public String getColumnName() {
                return this.columnName;
            }

            public void setColumnName(String columnName) {
                this.columnName = columnName;
            }

            public String getColumnCnName() {
                return this.columnCnName;
            }

            public void setColumnCnName(String columnCnName) {
                this.columnCnName = columnCnName;
            }

            public String getColumnDataType() {
                return this.columnDataType;
            }

            public void setColumnDataType(String columnDataType) {
                this.columnDataType = columnDataType;
            }

            public Integer getIsPrimaryKey() {
                return this.isPrimaryKey;
            }

            public void setIsPrimaryKey(Integer isPrimaryKey) {
                this.isPrimaryKey = isPrimaryKey;
            }

            public String getColumnLength() {
                return this.columnLength;
            }

            public void setColumnLength(String columnLength) {
                this.columnLength = columnLength;
            }

            public String toString() {
                return "Column [columnName=" + this.columnName + ", columnCnName=" + this.columnCnName + ", columnDataType=" + this.columnDataType + ", isPrimaryKey=" + this.isPrimaryKey + "]";
            }
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
