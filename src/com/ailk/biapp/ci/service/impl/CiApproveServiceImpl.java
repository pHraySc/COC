package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.ICiApproveHistoryHDao;
import com.ailk.biapp.ci.dao.ICiApproveHistoryJDao;
import com.ailk.biapp.ci.dao.ICiApproveStatusHDao;
import com.ailk.biapp.ci.dao.ICiApproveStatusJDao;
import com.ailk.biapp.ci.dao.ICiApproveUserInfoHDao;
import com.ailk.biapp.ci.dao.IDimApproveLevelHDao;
import com.ailk.biapp.ci.dao.IDimApproveProcessHDao;
import com.ailk.biapp.ci.dao.IDimApproveStatusHDao;
import com.ailk.biapp.ci.entity.CiApproveHistory;
import com.ailk.biapp.ci.entity.CiApproveStatus;
import com.ailk.biapp.ci.entity.CiApproveUserInfo;
import com.ailk.biapp.ci.entity.CiPersonNotice;
import com.ailk.biapp.ci.entity.DimApproveLevel;
import com.ailk.biapp.ci.entity.DimApproveProcess;
import com.ailk.biapp.ci.entity.DimApproveStatus;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ICiApproveService;
import com.ailk.biapp.ci.service.ICiPersonNoticeService;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiApproveServiceImpl implements ICiApproveService {
    private Logger log = Logger.getLogger(CiApproveServiceImpl.class);
    @Autowired
    private ICiApproveHistoryJDao ciApproveHistoryJDao;
    @Autowired
    private ICiApproveUserInfoHDao ciApproveUserInfoHDao;
    @Autowired
    private ICiApproveStatusHDao ciApproveStatusHDao;
    @Autowired
    private IDimApproveStatusHDao dimApproveStatusHDao;
    @Autowired
    private IDimApproveLevelHDao dimApproveLevelHDao;
    @Autowired
    private ICiApproveHistoryHDao ciApproveHistoryHDao;
    @Autowired
    private ICiApproveStatusJDao ciApproveStatusJDao;
    @Autowired
    private IDimApproveProcessHDao dimApproveProcessHDao;
    @Autowired
    private ICiPersonNoticeService personNoticeService;

    public CiApproveServiceImpl() {
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public int queryTotolPageCount(CiApproveStatus ciApproveStatus) throws CIServiceException {
        boolean count = false;

        try {
            int count1 = this.ciApproveStatusJDao.getCountBySql(ciApproveStatus);
            return count1;
        } catch (Exception var4) {
            var4.printStackTrace();
            this.log.error("��ѯ����ǰ�û���������Դ��Ϣ��������", var4);
            throw new CIServiceException("��ѯ����ǰ�û���������Դ��Ϣ��������");
        }
    }

    public List<CiApproveStatus> queryPageList(int currPage, int pageSize, CiApproveStatus ciApproveStatus) throws CIServiceException {
        List pageList = null;

        try {
            pageList = this.ciApproveStatusJDao.getPageListBySql(currPage, pageSize, ciApproveStatus);
            if(pageList != null) {
                Iterator e = pageList.iterator();

                while(e.hasNext()) {
                    CiApproveStatus resource = (CiApproveStatus)e.next();
                    if(StringUtil.isNotEmpty(resource.getResourceCreateUserId())) {
                        resource.setResourceCreateUserName(PrivilegeServiceUtil.getUserById(resource.getResourceCreateUserId()).getUsername());
                    }

                    if(StringUtil.isNotEmpty(resource.getLastApproveUserId())) {
                        resource.setLastApproveUserName(PrivilegeServiceUtil.getUserById(resource.getLastApproveUserId()).getUsername());
                    }
                }
            }

            return pageList;
        } catch (Exception var7) {
            this.log.error("��ѯ��������Դ��Ϣ��ҳ��¼ʧ��", var7);
            throw new CIServiceException("��ѯ����Դ��Ϣ��ҳ��¼ʧ��");
        }
    }

    public int queryApprovedTotolPageCount(CiApproveHistory ciApproveHistory) throws CIServiceException {
        boolean count = false;

        try {
            int count1 = this.ciApproveHistoryJDao.getApprovedCountBySql(ciApproveHistory);
            return count1;
        } catch (Exception var4) {
            this.log.error("��ѯ��ǰ�û�������������Դ��Ϣ����ʧ��", var4);
            throw new CIServiceException("��ѯ��ǰ�û�������������Դ��Ϣ����ʧ��");
        }
    }

    public List<CiApproveHistory> queryApprovedPageList(int currPage, int pageSize, CiApproveHistory ciApproveHistory) throws CIServiceException {
        List pageList = null;

        try {
            pageList = this.ciApproveHistoryJDao.getApprovedPageListBySql(currPage, pageSize, ciApproveHistory);
            if(pageList != null) {
                Iterator e = pageList.iterator();

                while(e.hasNext()) {
                    CiApproveHistory resource = (CiApproveHistory)e.next();
                    if(StringUtil.isNotEmpty(resource.getResourceCreateUserId())) {
                        resource.setResourceCreateUserName(PrivilegeServiceUtil.getUserById(resource.getResourceCreateUserId()).getUsername());
                    }
                }
            }

            return pageList;
        } catch (Exception var7) {
            this.log.error("��ѯ��Դ������Ϣ��ҳ��¼ʧ��", var7);
            throw new CIServiceException("��ѯ��Դ������Ϣ��ҳ��¼ʧ��");
        }
    }

    public String approvePass(CiApproveStatus ciApproveStatus, String userId, String opinion) throws CIServiceException {
        String approveRoleId = this.dimApproveStatusHDao.selectByApproveStatusId(ciApproveStatus.getCurrApproveStatusId()).getApproveRoleId();
        String nextRoleId = this.dimApproveLevelHDao.selectByApproveRoleId(approveRoleId).getNextRoleId();
        if(!StringUtil.isNotEmpty(nextRoleId)) {
            nextRoleId = ((DimApproveLevel)this.dimApproveLevelHDao.selectListByProperty("approveRoleType", Integer.valueOf(2)).get(0)).getApproveRoleId();
        }

        String approveStatusId = this.dimApproveStatusHDao.getApproveStatuByApproveRole(nextRoleId, Integer.valueOf(1));

        try {
            ciApproveStatus.setCurrApproveStatusId(approveStatusId);
            ciApproveStatus.setLastApproveOpinion(opinion);
            ciApproveStatus.setLastApproveRoleId(approveRoleId);
            ciApproveStatus.setLastApproveUserId(userId);
            ciApproveStatus.setLastApproveTime(new Date());
            this.ciApproveStatusHDao.insertOrUpdateCiApproveStatus(ciApproveStatus);
            this.recordApproveHistory(ciApproveStatus, 1);
            return nextRoleId;
        } catch (Exception var8) {
            this.log.error("���ͨ��ʧ��", var8);
            throw new CIServiceException("���ͨ��ʧ��");
        }
    }

    public void approveNotPass(CiApproveStatus ciApproveStatus, String userId, String opinion) throws CIServiceException {
        String approveRoleId = this.dimApproveStatusHDao.selectByApproveStatusId(ciApproveStatus.getCurrApproveStatusId()).getApproveRoleId();
        String approveStatusId = this.dimApproveStatusHDao.getApproveStatuByApproveRole(approveRoleId, Integer.valueOf(2));

        try {
            ciApproveStatus.setCurrApproveStatusId(approveStatusId);
            ciApproveStatus.setLastApproveOpinion(opinion);
            ciApproveStatus.setLastApproveRoleId(approveRoleId);
            ciApproveStatus.setLastApproveUserId(userId);
            ciApproveStatus.setLastApproveTime(new Date());
            this.ciApproveStatusHDao.insertOrUpdateCiApproveStatus(ciApproveStatus);
            this.recordApproveHistory(ciApproveStatus, 0);
        } catch (Exception var7) {
            this.log.error("��˲�ͨ��ʧ��", var7);
            throw new CIServiceException("��˲�ͨ��ʧ��");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public CiApproveStatus queryApproveStatusByStatusId(String statusId) throws CIServiceException {
        new CiApproveStatus();

        try {
            CiApproveStatus ciApproveStatus = this.ciApproveStatusHDao.selectByStatusId(Integer.valueOf(statusId.trim()));
            return ciApproveStatus;
        } catch (Exception var4) {
            this.log.error("������Դ״̬��ϢId��ѯ��Դ״̬����", var4);
            throw new CIServiceException("������Դ״̬��ϢId��ѯ��Դ״̬����");
        }
    }

    public void insertOrSubmitApprove(CiApproveStatus ciApproveStatus, int flag) throws CIServiceException {
        try {
            /*
                ������ǩʱ:flag=3
                �޸ı�ǩʱ:flag=3
             */
            String e = null;
            String approveRoleId = null;
            String resourceTypeId = this.queryResourceTypeId(ciApproveStatus.getProcessId());
            if(1 == flag) {
                e = this.dimApproveStatusHDao.selectDraftStatus().getApproveStatusId();
            }

            if(2 == flag) {
                approveRoleId = this.dimApproveLevelHDao.selectByProcessIdAndApproveRoleType(ciApproveStatus.getProcessId(), Integer.valueOf(1));
                e = this.dimApproveStatusHDao.getApproveStatuByApproveRole(approveRoleId, Integer.valueOf(1));
            }

            if(3 == flag) {
                approveRoleId = this.dimApproveLevelHDao.selectByProcessIdAndApproveRoleType(ciApproveStatus.getProcessId(), Integer.valueOf(2));
                e = this.dimApproveStatusHDao.getApproveStatuByApproveRole(approveRoleId, Integer.valueOf(1));
            }

            if(4 == flag) {
                approveRoleId = this.dimApproveLevelHDao.selectByProcessIdAndApproveRoleType(ciApproveStatus.getProcessId(), Integer.valueOf(2));
                e = this.dimApproveStatusHDao.getApproveStatuByApproveRole(approveRoleId, Integer.valueOf(2));
            }

            List ciApproveStatusList = this.ciApproveStatusHDao.selectByResourceIdAndProcessId(ciApproveStatus.getResourceId(), ciApproveStatus.getProcessId());
            if(!ciApproveStatusList.isEmpty()) {
                CiApproveStatus dept = (CiApproveStatus)ciApproveStatusList.get(0);
                dept.setResourceName(ciApproveStatus.getResourceName());
                dept.setCurrApproveStatusId(e);
                this.ciApproveStatusHDao.insertOrUpdateCiApproveStatus(dept);
            } else {
                ciApproveStatus.setCurrApproveStatusId(e);
                ciApproveStatus.setResourceTypeId(resourceTypeId);
                this.ciApproveStatusHDao.insertOrUpdateCiApproveStatus(ciApproveStatus);
            }

            if(1 == flag || 2 == flag) {
                String dept1 = ciApproveStatus.getDeptId();
                List approveUserInfoList = this.ciApproveUserInfoHDao.select(approveRoleId, dept1);
                Date date = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String createTime = ft.format(date);
                Iterator i$ = approveUserInfoList.iterator();

                while(i$.hasNext()) {
                    CiApproveUserInfo approveUserInfo = (CiApproveUserInfo)i$.next();
                    CiPersonNotice personNotice = new CiPersonNotice();
                    personNotice.setStatus(Integer.valueOf(1));
                    personNotice.setLabelId(Integer.valueOf(Integer.parseInt(e)));
                    personNotice.setNoticeName(ciApproveStatus.getResourceName() + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_LABEL_APPROVE);
                    personNotice.setNoticeDetail("���ã���" + createTime + "��������Ϊ ��" + ciApproveStatus.getResourceName() + "������Դ���������룬�뼰ʱ����");
                    personNotice.setNoticeSendTime(new Date());
                    personNotice.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_LABEL_APPROVE));
                    personNotice.setReadStatus(Integer.valueOf(1));
                    personNotice.setIsSuccess(Integer.valueOf(1));
                    String approveUserId = approveUserInfo.getApproveUserId();
                    personNotice.setReleaseUserId(ciApproveStatus.getResourceCreateUserId());
                    personNotice.setReceiveUserId(approveUserId);
                    this.personNoticeService.addPersonNotice(personNotice);
                }
            }

        } catch (Exception var16) {
            this.log.error("�½���Դ������ύ��������", var16);
            throw new CIServiceException("�½���Դ������ύ��������");
        }
    }

    public boolean judgeCurrentUserIsApprover(String userId) throws CIServiceException {
        try {
            List e = this.ciApproveUserInfoHDao.selectByUserId(userId);
            if(CollectionUtils.isNotEmpty(e)) {
                Iterator i$ = e.iterator();

                while(i$.hasNext()) {
                    CiApproveUserInfo ciApproveUserInfo = (CiApproveUserInfo)i$.next();
                    List dimApproveLevelList = this.dimApproveLevelHDao.selectByRoleIdAndRoleType(ciApproveUserInfo.getApproveRoleId(), Integer.valueOf(1));
                    if(CollectionUtils.isNotEmpty(dimApproveLevelList)) {
                        return true;
                    }
                }
            }

            return false;
        } catch (Exception var6) {
            this.log.error("�жϵ�ǰ�û��Ƿ�������Ȩ�޳���", var6);
            throw new CIServiceException("�жϵ�ǰ�û��Ƿ�������Ȩ�޳���");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiApproveUserInfo> queryApproveUserInfoList(String approveRoleId) throws CIServiceException {
        List approveUserInfoList = null;

        try {
            String e = PrivilegeServiceUtil.getUserId();
            Integer deptId = PrivilegeServiceUtil.getUserDeptId(e);
            approveUserInfoList = this.ciApproveUserInfoHDao.select(approveRoleId, String.valueOf(deptId));
            return approveUserInfoList;
        } catch (Exception var5) {
            this.log.error("ͨ��������ɫId��ѯ��������Ϣʧ��", var5);
            throw new CIServiceException("ͨ��������ɫId��ѯ��������Ϣʧ��");
        }
    }

    public CiApproveStatus queryApproveStatusByResourceIdAndProcessId(String resourceId, String processId) throws CIServiceException {
        List list = null;

        try {
            list = this.ciApproveStatusHDao.selectByResourceIdAndProcessId(resourceId, processId);
            return CollectionUtils.isNotEmpty(list)?(CiApproveStatus)list.get(0):null;
        } catch (Exception var5) {
            this.log.error("ͨ����ԴId��processId��ѯ��Դ��ǰ����״̬ʧ��", var5);
            throw new CIServiceException("ͨ����ԴId��processId��ѯ��Դ��ǰ����״̬ʧ��");
        }
    }

    public List<CiApproveHistory> queryApproveHistoryByResourceIdAndProcessId(String resourceId, String processId) throws CIServiceException {
        List list = null;

        try {
            list = this.ciApproveHistoryHDao.selectByResourceIdAndProcessId(resourceId, processId);
            return list;
        } catch (Exception var5) {
            this.log.error("ͨ����ԴId��processId��ѯ��Դ������ʷʧ��", var5);
            throw new CIServiceException("ͨ����ԴId��processId��ѯ��Դ������ʷʧ��");
        }
    }

    public List<CiApproveUserInfo> queryApproveUserInfoByUserId(String approveUserId) throws CIServiceException {
        List list = null;

        try {
            list = this.ciApproveUserInfoHDao.selectByUserId(approveUserId);
            return list;
        } catch (Exception var4) {
            this.log.error("ͨ����ǰ��¼�û���ȡ����������Ȩ����Ϣʧ��", var4);
            throw new CIServiceException("ͨ����ǰ��¼�û���ȡ����������Ȩ����Ϣʧ��");
        }
    }

    public List<CiApproveUserInfo> queryApproveUserInfoByStatusIdAndDeptId(String approveStatusId, String deptId) throws CIServiceException {
        List approveUserInfoList = null;

        try {
            if(StringUtil.isNotEmpty(approveStatusId)) {
                DimApproveStatus e = this.dimApproveStatusHDao.selectByApproveStatusId(approveStatusId);
                if(2L == e.getSortNum().longValue()) {
                    return null;
                } else {
                    String approveRoleId = e.getApproveRoleId();
                    if(StringUtil.isEmpty(approveRoleId)) {
                        return null;
                    } else {
                        int approveRoleType = this.dimApproveLevelHDao.selectByApproveRoleId(approveRoleId).getApproveRoleType().intValue();
                        if(2 == approveRoleType) {
                            return null;
                        } else {
                            approveUserInfoList = this.ciApproveUserInfoHDao.select(approveRoleId, String.valueOf(deptId));
                            return approveUserInfoList;
                        }
                    }
                }
            } else {
                return null;
            }
        } catch (Exception var7) {
            this.log.error("ͨ����ǰ��¼�û���ȡ����������Ȩ����Ϣʧ��", var7);
            throw new CIServiceException("ͨ����ǰ��¼�û���ȡ����������Ȩ����Ϣʧ��");
        }
    }

    private String queryResourceTypeId(String processId) {
        try {
            new DimApproveProcess();
            DimApproveProcess e = this.dimApproveProcessHDao.selectById(processId);
            String resourceTypeId = e.getResourceTypeId();
            return resourceTypeId;
        } catch (Exception var4) {
            this.log.error("����processId��ȡresourceTypeId����", var4);
            throw new CIServiceException("����processId��ȡresourceTypeId����");
        }
    }

    private void recordApproveHistory(CiApproveStatus ciApproveStatus, int flag) {
        CiApproveHistory ciApproveHistory = new CiApproveHistory();
        ciApproveHistory.setResourceId(ciApproveStatus.getResourceId());
        ciApproveHistory.setResourceName(ciApproveStatus.getResourceName());
        ciApproveHistory.setResourceCreateUserId(ciApproveStatus.getResourceCreateUserId());
        ciApproveHistory.setApproveRoleId(ciApproveStatus.getLastApproveRoleId());
        ciApproveHistory.setApproveUserId(ciApproveStatus.getLastApproveUserId());
        ciApproveHistory.setApproveTime(ciApproveStatus.getLastApproveTime());
        ciApproveHistory.setApproveOpinion(ciApproveStatus.getLastApproveOpinion());
        ciApproveHistory.setApproveResult(Integer.valueOf(flag));
        ciApproveHistory.setProcessId(ciApproveStatus.getProcessId());
        ciApproveHistory.setResourceTypeId(ciApproveStatus.getResourceTypeId());
        this.ciApproveHistoryHDao.insertCiApproveHistory(ciApproveHistory);
    }
}
