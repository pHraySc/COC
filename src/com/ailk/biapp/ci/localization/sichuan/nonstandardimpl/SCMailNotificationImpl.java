package com.ailk.biapp.ci.localization.sichuan.nonstandardimpl;

import com.ailk.biapp.ci.entity.CiPersonNotice;
import com.ailk.biapp.ci.entity.CiSysAnnouncement;
import com.ailk.biapp.ci.localization.nonstandard.IMailNotification;
import common.Logger;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("sCMailNotificationImpl")
@Transactional
@Scope("prototype")
public class SCMailNotificationImpl implements IMailNotification {
    private Logger log = Logger.getLogger(this.getClass());

    public SCMailNotificationImpl() {
    }

    public boolean personNoticeMail(CiPersonNotice ciPersonNotice) throws Exception {
        this.log.debug("����֪ͨ�ʼ����Ͳ������ԣ�������֪ͨID��" + ciPersonNotice.getNoticeId() + " | ����֪ͨ���� :" + ciPersonNotice.getNoticeName() + " | ����֪ͨ����ID��" + ciPersonNotice.getNoticeTypeId() + " | ����֪ͨ���ݣ�" + ciPersonNotice.getNoticeDetail() + " | ����֪ͨ����ʱ�䣺" + ciPersonNotice.getNoticeSendTime() + " | ��ǩID��" + ciPersonNotice.getLabelId() + " | �ͻ�ȺID��" + ciPersonNotice.getCustomerGroupId() + " | ״̬��" + ciPersonNotice.getStatus() + " | �������û�ID��" + ciPersonNotice.getReleaseUserId() + " | �ɹ�ʧ�ܱ�ʶ��" + ciPersonNotice.getIsSuccess() + " | �����û�ID��" + ciPersonNotice.getReceiveUserId() + " | ��ȡ״̬��" + ciPersonNotice.getReadStatus() + "��");
        return true;
    }

    public boolean sysAnnouncementMail(CiSysAnnouncement sysAnnouncement, List<String> receiverList) throws Exception {
        this.log.debug("ϵͳ�����ʼ����Ͳ������ԣ�������ID�� " + sysAnnouncement.getAnnouncementId() + " | ������⣺ " + sysAnnouncement.getAnnouncementName() + " | �������ݣ� " + sysAnnouncement.getAnnouncementDetail() + " | �������ͣ� " + sysAnnouncement.getTypeId() + " | �������ȼ��� " + sysAnnouncement.getPriorityId() + "��");
        return true;
    }
}
