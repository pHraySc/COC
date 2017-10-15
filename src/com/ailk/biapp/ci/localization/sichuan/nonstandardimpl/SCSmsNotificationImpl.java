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
        this.log.debug("����֪ͨ���ŷ��Ͳ������ԣ�������֪ͨID��" + ciPersonNotice.getNoticeId() + " | ����֪ͨ���� :" + ciPersonNotice.getNoticeName() + " | ����֪ͨ����ID��" + ciPersonNotice.getNoticeTypeId() + " | ����֪ͨ���ݣ�" + ciPersonNotice.getNoticeDetail() + " | ����֪ͨ����ʱ�䣺" + ciPersonNotice.getNoticeSendTime() + " | ��ǩID��" + ciPersonNotice.getLabelId() + " | �ͻ�ȺID��" + ciPersonNotice.getCustomerGroupId() + " | ״̬��" + ciPersonNotice.getStatus() + " | �������û�ID��" + ciPersonNotice.getReleaseUserId() + " | �ɹ�ʧ�ܱ�ʶ��" + ciPersonNotice.getIsSuccess() + " | �����û�ID��" + ciPersonNotice.getReceiveUserId() + " | ��ȡ״̬��" + ciPersonNotice.getReadStatus() + "��");
        return true;
    }

    public boolean sysAnnouncementSms(CiSysAnnouncement sysAnnouncement, List<String> receiverList) throws Exception {
        this.log.debug("ϵͳ������ŷ��Ͳ������ԣ�������ID�� " + sysAnnouncement.getAnnouncementId() + " | ������⣺ " + sysAnnouncement.getAnnouncementName() + " | �������ݣ� " + sysAnnouncement.getAnnouncementDetail() + " | �������ͣ� " + sysAnnouncement.getTypeId() + " | �������ȼ��� " + sysAnnouncement.getPriorityId() + "��");
        return true;
    }
}
