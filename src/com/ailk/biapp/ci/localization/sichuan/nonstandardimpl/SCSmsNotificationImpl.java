package com.ailk.biapp.ci.localization.sichuan.nonstandardimpl;

import com.ailk.biapp.ci.entity.CiPersonNotice;
import com.ailk.biapp.ci.entity.CiSysAnnouncement;
import com.ailk.biapp.ci.localization.nonstandard.ISmsNotification;
import common.Logger;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("sCSmsNotificationImpl")
@Transactional
@Scope("prototype")
public class SCSmsNotificationImpl implements ISmsNotification {
    private Logger log = Logger.getLogger(this.getClass());

    public SCSmsNotificationImpl() {
    }

    public boolean personNoticeSms(CiPersonNotice ciPersonNotice) throws Exception {
        this.log.debug("个人通知短信发送参数测试：【个人通知ID：" + ciPersonNotice.getNoticeId() + " | 个人通知标题 :" + ciPersonNotice.getNoticeName() + " | 个人通知类型ID：" + ciPersonNotice.getNoticeTypeId() + " | 个人通知内容：" + ciPersonNotice.getNoticeDetail() + " | 个人通知发送时间：" + ciPersonNotice.getNoticeSendTime() + " | 标签ID：" + ciPersonNotice.getLabelId() + " | 客户群ID：" + ciPersonNotice.getCustomerGroupId() + " | 状态：" + ciPersonNotice.getStatus() + " | 发布人用户ID：" + ciPersonNotice.getReleaseUserId() + " | 成功失败标识：" + ciPersonNotice.getIsSuccess() + " | 接收用户ID：" + ciPersonNotice.getReceiveUserId() + " | 读取状态：" + ciPersonNotice.getReadStatus() + "】");
        return true;
    }

    public boolean sysAnnouncementSms(CiSysAnnouncement sysAnnouncement, List<String> receiverList) throws Exception {
        this.log.debug("系统公告短信发送参数测试：【公告ID： " + sysAnnouncement.getAnnouncementId() + " | 公告标题： " + sysAnnouncement.getAnnouncementName() + " | 公告内容： " + sysAnnouncement.getAnnouncementDetail() + " | 公告类型： " + sysAnnouncement.getTypeId() + " | 公告优先级： " + sysAnnouncement.getPriorityId() + "】");
        return true;
    }
}
