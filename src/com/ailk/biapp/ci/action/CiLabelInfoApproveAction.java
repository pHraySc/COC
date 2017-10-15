package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.entity.CiApproveUserInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiPersonNotice;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiLabelInfoApproveService;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.service.ICiPersonNoticeService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiLabelInfoApproveAction extends CIBaseAction {
    private Logger log = Logger.getLogger(CiLabelInfoApproveAction.class);
    private CiLabelInfo searchConditionInfo;
    private Pager pager;
    private String labelIds;
    private String approveDesc;
    boolean approver = false;
    @Autowired
    private ICiLabelInfoApproveService ciLabelInfoApproveService;
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;
    @Autowired
    private ICiPersonNoticeService personNoticeService;

    public CiLabelInfoApproveAction() {
    }

    public boolean isApprover() {
        return this.approver;
    }

    public void setApprover(boolean approver) {
        this.approver = approver;
    }

    public String init() throws Exception {
        String createUserId = PrivilegeServiceUtil.getUserId();
        this.approver = this.ciLabelInfoService.judgeCurrentUserIsApprover(createUserId);
        return "index";
    }

    public String query() throws Exception {
        String createUserId = this.getUserId();
        if(this.searchConditionInfo == null) {
            this.searchConditionInfo = new CiLabelInfo();
        }

        this.searchConditionInfo.setCreateUserId(createUserId);
        if(this.pager == null) {
            this.pager = new Pager();
        }

        this.pager.setTotalSize((long)this.ciLabelInfoApproveService.queryTotolPageCount(this.searchConditionInfo, 2));
        this.pager = this.pager.pagerFlip();
        this.pager.setResult(this.ciLabelInfoApproveService.queryPageList(this.pager.getPageNum(), this.pager.getPageSize(), this.searchConditionInfo, 2));
        Date startDate = this.searchConditionInfo.getStartDate();
        Date endDate = this.searchConditionInfo.getEndDate();
        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_APPROVE_SELECT", "", "", "��ǩ������ѯ��������ǩ�ɹ�,����ǩ���� :" + this.searchConditionInfo.getLabelName() + "��" + "����״̬:������״̬��" + (startDate == null?"":"������ʼʱ��:" + DateUtil.date2String(startDate, "yyyy-MM-dd") + "��") + (endDate == null?"":"��������ʱ��:" + DateUtil.date2String(endDate, "yyyy-MM-dd")) + "��", OperResultEnum.Success, LogLevelEnum.Medium);
        return "query";
    }

    public String queryApproved() throws Exception {
        String createUserId = this.getUserId();
        if(this.searchConditionInfo == null) {
            this.searchConditionInfo = new CiLabelInfo();
        }

        this.searchConditionInfo.setCreateUserId(createUserId);
        if(this.pager == null) {
            this.pager = new Pager();
        }

        this.pager.setTotalSize((long)this.ciLabelInfoApproveService.queryTotolPageCount(this.searchConditionInfo, 3));
        this.pager = this.pager.pagerFlip();
        this.pager.setResult(this.ciLabelInfoApproveService.queryPageList(this.pager.getPageNum(), this.pager.getPageSize(), this.searchConditionInfo, 3));
        Date startDate = this.searchConditionInfo.getStartDate();
        Date endDate = this.searchConditionInfo.getEndDate();
        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_APPROVE_SELECT", "", "", "��ǩ������ѯ��������ǩ�ɹ�,����ǩ���� :" + this.searchConditionInfo.getLabelName() + "��" + "����״̬���Ѵ����ǩ��" + "������ʼʱ�䣺" + (startDate == null?"":DateUtil.date2String(startDate, "yyyy-MM-dd")) + "��" + "��������ʱ�䣺" + (endDate == null?"":DateUtil.date2String(endDate, "yyyy-MM-dd")) + "��", OperResultEnum.Success, LogLevelEnum.Medium);
        return "queryApproved";
    }

    public void approvePass() throws Exception {
        ArrayList labelIdList = new ArrayList();
        ArrayList labelNameList = new ArrayList();
        HttpServletResponse response = this.getResponse();
        HashMap returnMsg = new HashMap();

        try {
            String[] e = this.labelIds.split(",");
            String[] arr$ = e;
            int len$ = e.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String labelId = arr$[i$];
                if(!StringUtil.isEmpty(labelId)) {
                    String labelName = this.ciLabelInfoService.queryCiLabelInfoById(Integer.valueOf(Integer.parseInt(labelId))).getLabelName();
                    labelNameList.add(labelName);
                    labelIdList.add(Integer.valueOf(labelId.trim()));
                    String createUserId = PrivilegeServiceUtil.getUserId();
                    String nextRoleId = this.ciLabelInfoApproveService.approvePass(labelIdList, createUserId, this.approveDesc);
                    List approveUserInfoList = this.ciLabelInfoService.queryApproveUserInfoList(nextRoleId);
                    Date date = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String createTime = ft.format(date);
                    Iterator i$1 = approveUserInfoList.iterator();

                    while(i$1.hasNext()) {
                        CiApproveUserInfo approveUserInfo = (CiApproveUserInfo)i$1.next();
                        CiPersonNotice personNotice = new CiPersonNotice();
                        personNotice.setStatus(Integer.valueOf(1));
                        personNotice.setLabelId(Integer.valueOf(Integer.parseInt(labelId)));
                        personNotice.setNoticeName(labelName + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_LABEL_APPROVE);
                        personNotice.setNoticeDetail("���� " + PrivilegeServiceUtil.getUserDept(this.getUserId()) + "��" + this.getUserName() + "��" + createTime + "����ͨ������Ϊ ��" + labelName + "���ı�ǩ���뼰ʱ����");
                        personNotice.setNoticeSendTime(new Date());
                        personNotice.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_LABEL_APPROVE));
                        personNotice.setReadStatus(Integer.valueOf(1));
                        personNotice.setIsSuccess(Integer.valueOf(1));
                        String userId = approveUserInfo.getApproveUserId();
                        personNotice.setReleaseUserId(userId);
                        personNotice.setReceiveUserId(userId);
                        this.personNoticeService.addPersonNotice(personNotice);
                    }
                }
            }

            returnMsg.put("msg", "�����ɹ�");
            returnMsg.put("success", Boolean.valueOf(true));
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_APPROVE_OK", labelIdList.toString(), labelNameList.toString(), "��ǩ����ͨ���ɹ�����ǩId:" + labelIdList + ",��ǩ����:" + labelNameList, OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var22) {
            returnMsg.put("msg", "�������̳����˴�������ϵ����Ա");
            returnMsg.put("success", Boolean.valueOf(false));
            this.log.error("��ǩ�����쳣", var22);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_APPROVE_OK", labelIdList.toString(), labelNameList.toString(), "��ǩ����ͨ��ʱʧ�ܣ���ǩId:" + labelIdList + ",��ǩ����:" + labelNameList, OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        try {
            this.sendJson(response, JsonUtil.toJson(returnMsg));
        } catch (Exception var21) {
            this.log.error("����json���쳣", var21);
            throw new CIServiceException(var21);
        }
    }

    public void approveNoPass() throws Exception {
        ArrayList labelIdList = new ArrayList();
        ArrayList labelNameList = new ArrayList();
        String[] labelIdStr = this.labelIds.split(",");
        HashMap returnMsg = new HashMap();
        HttpServletResponse response = this.getResponse();
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = ft.format(date);

        try {
            String[] e = labelIdStr;
            int len$ = labelIdStr.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String labelId = e[i$];
                if(!StringUtil.isEmpty(labelId)) {
                    labelIdList.add(Integer.valueOf(labelId.trim()));
                    CiLabelInfo labelInfo = this.ciLabelInfoService.queryCiLabelInfoById(Integer.valueOf(Integer.parseInt(labelId)));
                    labelNameList.add(labelInfo.getLabelName());
                    CiPersonNotice personNotice = new CiPersonNotice();
                    personNotice.setStatus(Integer.valueOf(1));
                    personNotice.setLabelId(Integer.valueOf(Integer.parseInt(labelId)));
                    personNotice.setNoticeName(labelInfo.getLabelName() + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_LABEL_APPROVE);
                    personNotice.setNoticeDetail("���� " + PrivilegeServiceUtil.getUserDept(this.getUserId()) + "��" + this.getUserName() + "��" + createTime + "������ͨ������Ϊ��" + labelInfo.getLabelName() + "���ı�ǩ���뼰ʱ����");
                    personNotice.setNoticeSendTime(new Date());
                    personNotice.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_LABEL_APPROVE));
                    personNotice.setReadStatus(Integer.valueOf(1));
                    personNotice.setIsSuccess(Integer.valueOf(1));
                    String userId = labelInfo.getCreateUserId();
                    personNotice.setReleaseUserId(userId);
                    personNotice.setReceiveUserId(userId);
                    this.personNoticeService.addPersonNotice(personNotice);
                }
            }

            String var18 = PrivilegeServiceUtil.getUserId();
            this.ciLabelInfoApproveService.approveNotPass(labelIdList, var18, this.approveDesc);
            returnMsg.put("msg", "�����ɹ�");
            returnMsg.put("success", Boolean.valueOf(true));
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_APPROVE_NO", labelIdList.toString(), labelNameList.toString(), "��ǩ�����ܾ��ɹ�����ǩId:" + labelIdList + ",��ǩ����:" + labelNameList, OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var17) {
            returnMsg.put("msg", "�������̳����˴�������ϵ����Ա");
            returnMsg.put("success", Boolean.valueOf(false));
            this.log.error("��ǩ�����쳣", var17);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_APPROVE_NO", labelIdList.toString(), labelNameList.toString(), "��ǩ�����ܾ�ʱʧ�ܣ���ǩId:" + labelIdList + ",��ǩ����:" + labelNameList, OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        try {
            this.sendJson(response, JsonUtil.toJson(returnMsg));
        } catch (Exception var16) {
            this.log.error("����json���쳣", var16);
            throw new CIServiceException(var16);
        }
    }

    public String getLabelIds() {
        return this.labelIds;
    }

    public void setLabelIds(String labelIds) {
        this.labelIds = labelIds;
    }

    public String getApproveDesc() {
        return this.approveDesc;
    }

    public void setApproveDesc(String approveDesc) {
        this.approveDesc = approveDesc;
    }

    public CiLabelInfo getSearchConditionInfo() {
        return this.searchConditionInfo;
    }

    public void setSearchConditionInfo(CiLabelInfo searchConditionInfo) {
        this.searchConditionInfo = searchConditionInfo;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }
}
