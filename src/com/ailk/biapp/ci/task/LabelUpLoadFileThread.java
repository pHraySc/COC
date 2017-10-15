package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.entity.CiPersonNotice;
import com.ailk.biapp.ci.model.CiConfigLabeInfo;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.service.ICiPersonNoticeService;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.DateUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Scope("prototype")
public class LabelUpLoadFileThread extends Thread {
    private final Logger log = Logger.getLogger(LabelUpLoadFileThread.class);
    private String userId;
    private Integer deptId;
    private String filePath;
    private List<CiConfigLabeInfo> list;
    @Autowired
    private ICiLabelInfoService labelInfoService;
    @Autowired
    private ICiPersonNoticeService personNoticeService;

    public LabelUpLoadFileThread() {
    }

    public void run() {
        long t1 = System.currentTimeMillis();
        this.log.debug(">>LabelUpLoadFileThread begining at " + CiUtil.convertLongMillsToStrTime(t1));
        boolean success = true;
        int successCount = 0;
        int labelCount = 0;

        try {
            for(Iterator e = this.list.iterator(); e.hasNext(); ++successCount) {
                CiConfigLabeInfo configLabelInfo = (CiConfigLabeInfo)e.next();
                ++labelCount;
                configLabelInfo.setUserId(this.userId);
                configLabelInfo.setDeptId(this.deptId.toString());
                configLabelInfo.setLabelCreateType(Integer.valueOf(2));
                configLabelInfo.setCreateTime(new Date());
                configLabelInfo.setEffecTime(DateUtil.string2Date("2013-06-30", "yyyy-MM-dd"));
                configLabelInfo.setFailTime(DateUtil.string2Date("2099-06-30", "yyyy-MM-dd"));
                configLabelInfo.setCreateTableType("1");
                this.labelInfoService.saveConfigLabelInfo(configLabelInfo, (List)null);
                this.labelInfoService.publishLabel(configLabelInfo, this.userId);
            }
        } catch (Exception var19) {
            success = false;
            this.log.error("导入到第" + labelCount + "行数据 导入失败！！！！！", var19);
        } finally {
            Date date = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String importTime = ft.format(date);
            CiPersonNotice personNotice = new CiPersonNotice();
            StringBuffer resultMsg = new StringBuffer();
            if(success) {
                resultMsg.append("标签导入成功！");
            } else {
                resultMsg.append("标签导入失败！");
            }

            resultMsg.append("文件总记录数：").append(labelCount).append("条；").append("导入成功数：").append(successCount).append("条；").append("时间：").append(importTime);
            this.log.info("personNotice:" + resultMsg.toString());
            personNotice.setStatus(Integer.valueOf(1));
            personNotice.setNoticeName(ServiceConstants.PERSON_NOTICE_TYPE_STRING_UPLOAD_LABEL_FILE);
            personNotice.setNoticeDetail(resultMsg.toString());
            personNotice.setNoticeSendTime(new Date());
            personNotice.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_UPLOAD_LABEL_FILE));
            personNotice.setReadStatus(Integer.valueOf(1));
            personNotice.setIsSuccess(Integer.valueOf(1));
            personNotice.setReleaseUserId(this.userId);
            personNotice.setReceiveUserId(this.userId);
            this.personNoticeService.addPersonNotice(personNotice);
            long t2 = System.currentTimeMillis();
            this.log.debug(">>LabelUpLoadFileThread end at " + CiUtil.convertLongMillsToStrTime(t2));
        }

    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getDeptId() {
        return this.deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<CiConfigLabeInfo> getList() {
        return this.list;
    }

    public void setList(List<CiConfigLabeInfo> list) {
        this.list = list;
    }
}
