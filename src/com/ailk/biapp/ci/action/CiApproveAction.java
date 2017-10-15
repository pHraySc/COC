package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.entity.*;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiApproveService;
import com.ailk.biapp.ci.service.ICiPersonNoticeService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.string.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class CiApproveAction extends CIBaseAction {
    private Logger log = Logger.getLogger(CiApproveAction.class);
    private CiApproveStatus searchApproveStatus;
    private Pager pager;
    private String statusIds;
    private String approveOpinion;
    private String showType;
    boolean approver = false;

    private List<DimApproveResourceType> resourceTypeList;
    @Autowired
    private ICiApproveService ciApproveService;
    @Autowired
    private ICiPersonNoticeService personNoticeService;

    public CiApproveAction() {
    }

    public void judgeCurrentUserIsApprover() {
        HttpServletResponse response = this.getResponse();
        HashMap returnMsg = new HashMap();
        String userId = this.getUserId();
        this.approver = this.ciApproveService.judgeCurrentUserIsApprover(userId);
        returnMsg.put("approver", Boolean.valueOf(this.approver));

        try {
            this.sendJson(response, JsonUtil.toJson(returnMsg));
        } catch (Exception var5) {
            this.log.error("����json���쳣", var5);
            throw new CIServiceException(var5);
        }
    }

    public String init() throws Exception {
        return "resourceApproveMain";
    }

    public String index() throws Exception {
        CacheBase cache = CacheBase.getInstance();
        this.resourceTypeList = (List<DimApproveResourceType>) cache.getObjectList("DIM_APPROVE_RESOURCE_TYPE");
        return "resourceApproveIndex";
    }

    public String query() throws Exception {
        String userId = this.getUserId();
        Map approveRoleIdContainer = this.queryDeptIdsAndApproveRoleIds(userId);
        if (null == this.searchApproveStatus) {
            this.searchApproveStatus = new CiApproveStatus();
        }

        this.searchApproveStatus.setUserId(userId);
        this.searchApproveStatus.setApproveRoleIdContainer(approveRoleIdContainer);
        if (this.pager == null) {
            this.pager = new Pager();
        }

        this.pager.setTotalSize((long) this.ciApproveService.queryTotolPageCount(this.searchApproveStatus));
        this.pager = this.pager.pagerFlip();
        this.pager.setResult(this.ciApproveService.queryPageList(this.pager.getPageNum(), this.pager.getPageSize(), this.searchApproveStatus));
        Date startDate = this.searchApproveStatus.getStartDate();
        Date endDate = this.searchApproveStatus.getEndDate();
        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_APPROVE_SELECT", "", "", "��Դ������ѯ��������Դ�ɹ�,����Դ���� :" + this.searchApproveStatus.getResourceName() + "��" + "����״̬������Դ��" + "������ʼʱ�䣺" + (startDate == null ? "" : DateUtil.date2String(startDate, "yyyy-MM-dd")) + "��" + "��������ʱ�䣺" + (endDate == null ? "" : DateUtil.date2String(endDate, "yyyy-MM-dd")) + "��", OperResultEnum.Success, LogLevelEnum.Medium);
        return "resourceApproveList";
    }

    public String queryApproved() throws Exception {
        String userId = this.getUserId();
        CiApproveHistory searchApproveHistory = new CiApproveHistory();
        searchApproveHistory.setUserId(userId);
        if (null != this.searchApproveStatus) {
            searchApproveHistory.setStartDate(this.searchApproveStatus.getStartDate());
            searchApproveHistory.setEndDate(this.searchApproveStatus.getEndDate());
            searchApproveHistory.setResourceName(this.searchApproveStatus.getResourceName());
            searchApproveHistory.setResourceTypeId(this.searchApproveStatus.getResourceTypeId());
        }

        if (this.pager == null) {
            this.pager = new Pager();
        }

        this.pager.setTotalSize((long) this.ciApproveService.queryApprovedTotolPageCount(searchApproveHistory));
        this.pager = this.pager.pagerFlip();
        this.pager.setResult(this.ciApproveService.queryApprovedPageList(this.pager.getPageNum(), this.pager.getPageSize(), searchApproveHistory));
        Date startDate = searchApproveHistory.getStartDate();
        Date endDate = searchApproveHistory.getEndDate();
        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_APPROVE_SELECT", "", "", "��Դ������ѯ��������Դ�ɹ�,����Դ���� :" + searchApproveHistory.getResourceName() + "��" + "����״̬���Ѵ�����Դ��" + "������ʼʱ�䣺" + (startDate == null ? "" : DateUtil.date2String(startDate, "yyyy-MM-dd")) + "��" + "��������ʱ�䣺" + (endDate == null ? "" : DateUtil.date2String(endDate, "yyyy-MM-dd")) + "��", OperResultEnum.Success, LogLevelEnum.Medium);
        return "resourceHasApprovedList";
    }

    public void approvePass() throws Exception {
        String userId = this.getUserId();
        ArrayList statusIdList = new ArrayList();
        ArrayList resourceNameList = new ArrayList();
        HttpServletResponse response = this.getResponse();
        HashMap returnMsg = new HashMap();

        try {
            String[] e = this.statusIds.split(",");
            String[] arr$ = e;
            int len$ = e.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                String statusId = arr$[i$];
                if (!StringUtil.isEmpty(statusId)) {
                    CiApproveStatus ciApproveStatus = this.ciApproveService.queryApproveStatusByStatusId(statusId);
                    String resourceName = ciApproveStatus.getResourceName();
                    resourceNameList.add(resourceName);
                    statusIdList.add(Integer.valueOf(statusId.trim()));
                    String nextRoleId = this.ciApproveService.approvePass(ciApproveStatus, userId, this.approveOpinion);
                    List approveUserInfoList = this.ciApproveService.queryApproveUserInfoList(nextRoleId);
                    Date date = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String createTime = ft.format(date);
                    Iterator i$1 = approveUserInfoList.iterator();

                    while (i$1.hasNext()) {
                        CiApproveUserInfo approveUserInfo = (CiApproveUserInfo) i$1.next();
                        CiPersonNotice personNotice = new CiPersonNotice();
                        personNotice.setStatus(Integer.valueOf(1));
                        personNotice.setLabelId(Integer.valueOf(Integer.parseInt(statusId)));
                        personNotice.setNoticeName(resourceName + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_LABEL_APPROVE);
                        personNotice.setNoticeDetail("���� " + PrivilegeServiceUtil.getUserDept(this.getUserId()) + "��" + this.getUserName() + "��" + createTime + "����ͨ������Ϊ ��" + resourceName + "������Դ���뼰ʱ����");
                        personNotice.setNoticeSendTime(new Date());
                        personNotice.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_LABEL_APPROVE));
                        personNotice.setReadStatus(Integer.valueOf(1));
                        personNotice.setIsSuccess(Integer.valueOf(1));
                        String approveUserId = approveUserInfo.getApproveUserId();
                        personNotice.setReleaseUserId(userId);
                        personNotice.setReceiveUserId(approveUserId);
                        this.personNoticeService.addPersonNotice(personNotice);
                    }
                }
            }

            returnMsg.put("msg", "�����ɹ�");
            returnMsg.put("success", Boolean.valueOf(true));
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_APPROVE_OK", statusIdList.toString(), resourceNameList.toString(), "��Դ����ͨ���ɹ�����ԴId:" + statusIdList + ",��ǩ����:" + resourceNameList, OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var23) {
            returnMsg.put("msg", "�������̳����˴�������ϵ����Ա");
            returnMsg.put("success", Boolean.valueOf(false));
            this.log.error("��Դ�����쳣", var23);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_APPROVE_OK", statusIdList.toString(), resourceNameList.toString(), "��Դ����ͨ��ʱʧ�ܣ���Դ״̬��ϢId:" + statusIdList + ",��Դ����:" + resourceNameList, OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        try {
            this.sendJson(response, JsonUtil.toJson(returnMsg));
        } catch (Exception var22) {
            this.log.error("����json���쳣", var22);
            throw new CIServiceException(var22);
        }
    }

    public void approveNotPass() throws Exception {
        String userId = this.getUserId();
        ArrayList statusIdList = new ArrayList();
        ArrayList resourceNameList = new ArrayList();
        HttpServletResponse response = this.getResponse();
        HashMap returnMsg = new HashMap();
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String approveTime = ft.format(date);

        try {
            String[] e = this.statusIds.split(",");
            String[] arr$ = e;
            int len$ = e.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                String statusId = arr$[i$];
                if (!StringUtil.isEmpty(statusId)) {
                    CiApproveStatus ciApproveStatus = this.ciApproveService.queryApproveStatusByStatusId(statusId);
                    String resourceName = ciApproveStatus.getResourceName();
                    resourceNameList.add(resourceName);
                    statusIdList.add(Integer.valueOf(statusId.trim()));
                    this.ciApproveService.approveNotPass(ciApproveStatus, userId, this.approveOpinion);
                    CiPersonNotice personNotice = new CiPersonNotice();
                    personNotice.setStatus(Integer.valueOf(1));
                    personNotice.setLabelId(Integer.valueOf(Integer.parseInt(statusId)));
                    personNotice.setNoticeName(resourceName + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_LABEL_APPROVE);
                    personNotice.setNoticeDetail("���� " + PrivilegeServiceUtil.getUserDept(this.getUserId()) + "��" + this.getUserName() + "��" + approveTime + "������ͨ������Ϊ ��" + resourceName + "������Դ���뼰ʱ����");
                    personNotice.setNoticeSendTime(new Date());
                    personNotice.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_LABEL_APPROVE));
                    personNotice.setReadStatus(Integer.valueOf(1));
                    personNotice.setIsSuccess(Integer.valueOf(1));
                    personNotice.setReleaseUserId(userId);
                    personNotice.setReceiveUserId(ciApproveStatus.getResourceCreateUserId());
                    this.personNoticeService.addPersonNotice(personNotice);
                }

                returnMsg.put("msg", "�����ɹ�");
                returnMsg.put("success", Boolean.valueOf(true));
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_APPROVE_OK", statusIdList.toString(), resourceNameList.toString(), "��Դ������ͨ���ɹ�����ԴId:" + statusIdList + ",��ǩ����:" + resourceNameList, OperResultEnum.Success, LogLevelEnum.Medium);
            }
        } catch (Exception var18) {
            returnMsg.put("msg", "�������̳����˴�������ϵ����Ա");
            returnMsg.put("success", Boolean.valueOf(false));
            this.log.error("��Դ�����쳣", var18);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_APPROVE_NO", statusIdList.toString(), resourceNameList.toString(), "��Դ������ͨ��ʱʧ�ܣ���Դ״̬��ϢId:" + statusIdList + ",��Դ����:" + resourceNameList, OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        try {
            this.sendJson(response, JsonUtil.toJson(returnMsg));
        } catch (Exception var17) {
            this.log.error("����json���쳣", var17);
            throw new CIServiceException(var17);
        }
    }

    private Map<String, Set<String>> queryDeptIdsAndApproveRoleIds(String userId) {
        HashMap approveRoleIdContainer = new HashMap();

        try {
            HashSet e = new HashSet();
            HashSet noneRoleIds = new HashSet();
            List list = this.ciApproveService.queryApproveUserInfoByUserId(userId);
            Iterator i$ = list.iterator();

            while (i$.hasNext()) {
                CiApproveUserInfo ciApproveUserInfo = (CiApproveUserInfo) i$.next();
                String deptId = ciApproveUserInfo.getDeptId();
                if (!deptId.equals("all")) {
                    e.add(ciApproveUserInfo.getApproveRoleId());
                } else {
                    noneRoleIds.add(ciApproveUserInfo.getApproveRoleId());
                }
            }

            approveRoleIdContainer.put("1", e);
            approveRoleIdContainer.put("0", noneRoleIds);
        } catch (Exception var9) {
            this.log.error("��Դ������װȨ��Map�쳣", var9);
        }

        return approveRoleIdContainer;
    }

    public CiApproveStatus getSearchApproveStatus() {
        return this.searchApproveStatus;
    }

    public void setSearchApproveStatus(CiApproveStatus searchApproveStatus) {
        this.searchApproveStatus = searchApproveStatus;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public String getShowType() {
        return this.showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getStatusIds() {
        return this.statusIds;
    }

    public void setStatusIds(String statusIds) {
        this.statusIds = statusIds;
    }

    public String getApproveOpinion() {
        return this.approveOpinion;
    }

    public void setApproveOpinion(String approveOpinion) {
        this.approveOpinion = approveOpinion;
    }

    public List<DimApproveResourceType> getResourceTypeList() {
        return this.resourceTypeList;
    }

    public void setResourceTypeList(List<DimApproveResourceType> resourceTypeList) {
        this.resourceTypeList = resourceTypeList;
    }
}
