package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiTemplateInfo;
import com.ailk.biapp.ci.entity.DimScene;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiTemplateInfoService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TemplateManageAction extends CIBaseAction {
    private Logger log = Logger.getLogger(TemplateManageAction.class);
    @Autowired
    private ICiTemplateInfoService ciTemplateInfoService;
    @Autowired
    private ICustomersManagerService customersService;
    private Pager pager;
    private String currentPage;
    private CiTemplateInfo ciTemplateInfo;
    private List<CiTemplateInfo> ciTemplateInfoList;
    private CiTemplateInfo searchBean;
    private String templateIds;
    private String templateId;
    private CiCustomGroupInfo ciCustomGroupInfo;
    private List<CiLabelRule> ciLabelRuleList;
    private List<DimScene> dimSceneList;
    private String isLatestTemplateFlag;
    private String isSysRecommendTemplateFlag;
    private List<DimScene> dimScenes;
    private List<CiTemplateInfo> ciTemplateInfoHotList;

    public TemplateManageAction() {
    }

    public void findTemplateTotalNum() throws Exception {
        if(this.searchBean == null) {
            this.searchBean = new CiTemplateInfo();
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        boolean success = false;
        HashMap result = new HashMap();
        String msg = "";
        int totalPage = 0;
        Long totalNum = Long.valueOf(0L);

        try {
            if(StringUtil.isNotEmpty(this.isLatestTemplateFlag) && (this.isLatestTemplateFlag.equals("true") || this.isLatestTemplateFlag == "true")) {
                Date response = new Date();
                Date e = DateUtil.getStartDate(response, 15);
                if(e != null) {
                    this.searchBean.setModifyStartDate(e);
                }

                if(response != null) {
                    this.searchBean.setModifyEndDate(response);
                }
            }

            if(StringUtil.isNotEmpty(this.isSysRecommendTemplateFlag) && (this.isSysRecommendTemplateFlag.equals("true") || this.isSysRecommendTemplateFlag == "true")) {
                this.searchBean.setIsHotList(ServiceConstants.IS_HOT_LIST);
            }

            int response1 = this.ciTemplateInfoService.queryTemplateInfoCount(this.searchBean);
            if(StringUtil.isNotEmpty(this.isSysRecommendTemplateFlag) && (this.isSysRecommendTemplateFlag.equals("true") || this.isSysRecommendTemplateFlag == "true") && response1 >= ServiceConstants.SHOW_RECOMMEND_TEMPLATE_NUM) {
                response1 = ServiceConstants.SHOW_RECOMMEND_TEMPLATE_NUM;
            }

            String e1 = this.getUserId();
            if(!PrivilegeServiceUtil.isAdminUser(e1)) {
                this.searchBean.setUserId(e1);
            }

            byte pageSize = 5;
            totalNum = Long.valueOf((long)response1);
            totalPage = (int)Math.ceil((double)response1 / (double)pageSize);
            this.pager.setTotalPage(totalPage);
            this.pager.setPageSize(pageSize);
            success = true;
            CILogServiceUtil.getLogServiceInstance().log("COC_TEMPLATE_MANAGE_SELECT", "", "", "�ҵ�ģ���ѯ�ɹ�,��ѯ������ģ������ :" + this.searchBean.getTemplateName() + ",ʹ�õı�ǩ:" + this.searchBean.getLabelOptRuleShow() + ",����ʱ���:" + this.searchBean.getStartDate() + " �� " + this.searchBean.getEndDate() + "��", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var10) {
            msg = "��ѯģ���б���ҳ������";
            this.log.error(msg, var10);
            success = false;
            CILogServiceUtil.getLogServiceInstance().log("COC_TEMPLATE_MANAGE_SELECT", (String)null, (String)null, "�ҵ�ģ���ѯʧ��,��ѯ������ģ������ :" + this.searchBean.getTemplateName() + ",ʹ�õı�ǩ:" + this.searchBean.getLabelOptRuleShow() + ",����ʱ���:" + this.searchBean.getStartDate() + " �� " + this.searchBean.getEndDate() + "��", OperResultEnum.Success, LogLevelEnum.Medium);
        }

        result.put("success", Boolean.valueOf(success));
        if(success) {
            result.put("totalPage", Integer.valueOf(totalPage));
            result.put("totalSize", totalNum);
        } else {
            result.put("msg", msg);
        }

        HttpServletResponse response2 = this.getResponse();

        try {
            this.sendJson(response2, JsonUtil.toJson(result));
        } catch (Exception var9) {
            this.log.error("����json���쳣", var9);
            throw new CIServiceException(var9);
        }
    }

    public String findTemplateList() throws Exception {
        if(this.searchBean == null) {
            this.searchBean = new CiTemplateInfo();
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        try {
            if(StringUtil.isNotEmpty(this.isLatestTemplateFlag) && (this.isLatestTemplateFlag.equals("true") || this.isLatestTemplateFlag == "true")) {
                Date e = new Date();
                Date msg1 = DateUtil.getStartDate(e, 15);
                if(msg1 != null) {
                    this.searchBean.setModifyStartDate(msg1);
                }

                if(e != null) {
                    this.searchBean.setModifyEndDate(e);
                }
            }

            if(StringUtil.isNotEmpty(this.isSysRecommendTemplateFlag) && (this.isSysRecommendTemplateFlag.equals("true") || this.isSysRecommendTemplateFlag == "true")) {
                this.pager.setTotalSize((long)ServiceConstants.SHOW_RECOMMEND_TEMPLATE_NUM);
                this.searchBean.setIsHotList(ServiceConstants.IS_HOT_LIST);
            }

            String e1 = this.getUserId();
            if(!PrivilegeServiceUtil.isAdminUser(e1)) {
                this.searchBean.setUserId(e1);
            }

            int msg2 = this.pager.getTotalPage();
            if(StringUtil.isEmpty(this.currentPage)) {
                this.currentPage = "1";
            }

            int curPage = Integer.valueOf(this.currentPage).intValue();
            this.pager.setPageNum(curPage);
            if(curPage <= msg2) {
                byte pageSize = 5;
                this.pager.setPageSize(pageSize);
                this.pager.setTotalPage(msg2);
                this.ciTemplateInfoList = this.ciTemplateInfoService.queryTemplateInfoList(this.pager, this.searchBean);
            }
        } catch (Exception var5) {
            String msg = "��ѯģ���б����";
            this.log.error(msg, var5);
            throw new CIServiceException(var5);
        }

        return StringUtil.isNotEmpty(this.isLatestTemplateFlag) && (this.isLatestTemplateFlag.equals("true") || this.isLatestTemplateFlag == "true")?"loadPagedTemplateSearchList":(StringUtil.isNotEmpty(this.isSysRecommendTemplateFlag) && (this.isSysRecommendTemplateFlag.equals("true") || this.isSysRecommendTemplateFlag == "true")?"loadPagedTemplateSearchList":"findMore");
    }

    public void saveTemplate() throws Exception {
        Map returnMap = null;

        try {
            String response = this.getUserId();
            returnMap = this.ciTemplateInfoService.isNameExist(this.ciTemplateInfo, response);
            if(((Boolean)returnMap.get("success")).booleanValue()) {
                HttpSession e1 = this.getSession();
                List ciLabelRuleList = (List)e1.getAttribute("sessionModelList");
                this.ciTemplateInfo.setIsPrivate(Integer.valueOf(1));
                this.ciTemplateInfoService.addTemplateInfo(this.ciTemplateInfo, ciLabelRuleList, response);
                String msg = "�����ɹ���";
                returnMap.put("msg", msg);
                CILogServiceUtil.getLogServiceInstance().log("COC_TEMPLATE_MANAGE_ADD", this.ciTemplateInfo.getTemplateId(), this.ciTemplateInfo.getTemplateName(), "����ģ�塾" + this.ciTemplateInfo.getTemplateName() + "���ɹ�", OperResultEnum.Success, LogLevelEnum.Medium);
            }
        } catch (Exception var7) {
            String e = "����ģ��";
            this.log.error(e + " : " + this.ciTemplateInfo.getTemplateName() + " ʧ��", var7);
            returnMap.put("msg", e + "ʧ��");
            returnMap.put("success", Boolean.valueOf(false));
            CILogServiceUtil.getLogServiceInstance().log("COC_TEMPLATE_MANAGE_ADD", "-1", "����ģ�����", "����ģ�塾" + this.ciTemplateInfo.getTemplateName() + "��ʧ��", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMap));
        } catch (Exception var6) {
            this.log.error("����json���쳣", var6);
            throw new CIServiceException(var6);
        }
    }

    public String preEditTemplate() throws Exception {
        boolean success = false;
        String msg = "";
        CiTemplateInfo template = null;

        try {
            template = this.ciTemplateInfoService.queryTemplateInfoById(this.ciTemplateInfo.getTemplateId());
            if(template == null || !template.getUserId().equals(this.getUserId()) && !PrivilegeServiceUtil.isAdminUser(this.getUserId())) {
                success = false;
                msg = "û��Ȩ���޸�ģ�壡";
            } else {
                success = true;
            }

            if(success && 0 == template.getStatus().intValue()) {
                success = false;
                msg = "�����޸���ɾ����ģ�壡";
            }
        } catch (Exception var5) {
            this.log.error("Ԥ�༭ģ��ʧ�ܣ�templateId = " + this.ciTemplateInfo.getTemplateId(), var5);
            success = false;
            msg = var5.getMessage();
            throw new CIServiceException(msg, var5);
        }

        if(success) {
            this.ciTemplateInfo = template;
            return "toEditPage";
        } else {
            throw new CIServiceException(msg);
        }
    }

    public void editTemplate() throws Exception {
        Map returnMap = null;

        try {
            String response = this.getUserId();
            int e1 = this.ciTemplateInfo.getIsPrivate().intValue();
            if(e1 == 0) {
                returnMap = this.isTemplateNameExist();
                if(returnMap.get("cmsg") != null || !"".equals(returnMap.get("cmsg"))) {
                    returnMap.put("msg", returnMap.get("cmsg"));
                }
            } else {
                returnMap = this.ciTemplateInfoService.isNameExist(this.ciTemplateInfo, response);
            }

            if(((Boolean)returnMap.get("success")).booleanValue()) {
                this.ciTemplateInfoService.modifyTemplateInfo(this.ciTemplateInfo, (List)null, response);
                String msg = "�޸ĳɹ���";
                returnMap.put("msg", msg);
                CILogServiceUtil.getLogServiceInstance().log("COC_TEMPLATE_MANAGE_UPDATE", this.ciTemplateInfo.getTemplateId(), this.ciTemplateInfo.getTemplateName(), "�޸�ģ�塾" + this.ciTemplateInfo.getTemplateName() + "���ɹ�", OperResultEnum.Success, LogLevelEnum.Medium);
            }
        } catch (Exception var6) {
            String e = "�޸�ģ��";
            this.log.error(e + " : " + this.ciTemplateInfo.getTemplateName() + " ʧ��", var6);
            returnMap.put("msg", e + "ʧ��");
            returnMap.put("success", Boolean.valueOf(false));
            CILogServiceUtil.getLogServiceInstance().log("COC_TEMPLATE_MANAGE_UPDATE", this.ciTemplateInfo.getTemplateId(), this.ciTemplateInfo.getTemplateName(), "�޸�ģ�塾" + this.ciTemplateInfo.getTemplateName() + "��ʧ��", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMap));
        } catch (Exception var5) {
            this.log.error("����json���쳣", var5);
            throw new CIServiceException(var5);
        }
    }

    public void deleteTemplate() throws Exception {
        boolean success = false;
        String msg = "";
        HashMap result = new HashMap();
        CiTemplateInfo template = null;

        try {
            template = this.ciTemplateInfoService.queryTemplateInfoById(this.ciTemplateInfo.getTemplateId());
            String response = template.getTemplateName();
            String e = template.getTemplateId();
            if(template == null || !template.getUserId().equals(this.getUserId()) && !PrivilegeServiceUtil.isAdminUser(this.getUserId())) {
                success = false;
                msg = "ɾ��ģ��ʧ�ܣ����Ǵ����˻��߹���Ա������ɾ����ģ�壡";
            } else {
                this.ciTemplateInfoService.deleteTemplateInfo(template);
                success = true;
                msg = "ɾ��ģ��ɹ���";
                CILogServiceUtil.getLogServiceInstance().log("COC_TEMPLATE_MANAGE_DELETE", e, response, "ɾ��ģ�塾" + response + "���ɹ�", OperResultEnum.Success, LogLevelEnum.Medium);
            }
        } catch (Exception var8) {
            msg = "ɾ��ģ�����";
            this.log.error(msg, var8);
            success = false;
            CILogServiceUtil.getLogServiceInstance().log("COC_TEMPLATE_MANAGE_DELETE", this.ciTemplateInfo.getTemplateId(), template != null?template.getTemplateName():this.ciTemplateInfo.getTemplateId(), "ɾ��ģ�塾" + template != null?template.getTemplateName():this.ciTemplateInfo.getTemplateId() + "��ʧ�ܣ�ɾ��ģ�����", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        result.put("msg", msg);
        result.put("success", Boolean.valueOf(success));
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("����json���쳣", var7);
            throw new CIServiceException(var7);
        }
    }

    public void deleteSomeTemplates() throws Exception {
        boolean success = false;
        String msg = "";
        HashMap result = new HashMap();
        StringBuffer sb = new StringBuffer();
        ArrayList delIds = new ArrayList();
        String noDelete = "";

        try {
            boolean response = PrivilegeServiceUtil.isAdminUser(this.getUserId());
            if(StringUtil.isNotEmpty(this.templateIds)) {
                String[] e = this.templateIds.split(",");

                for(int i = 0; i < e.length; ++i) {
                    CiTemplateInfo template = this.ciTemplateInfoService.queryTemplateInfoById(e[i]);
                    String name = template.getTemplateName();
                    if(template == null || !template.getUserId().equals(this.getUserId()) && !response) {
                        sb.append(name + ",");
                    } else {
                        this.ciTemplateInfoService.deleteTemplateInfo(template);
                        delIds.add(e[i]);
                    }
                }

                noDelete = sb.toString();
                if(StringUtil.isNotEmpty(noDelete)) {
                    if(delIds.size() > 0) {
                        msg = "����ɾ���ɹ���ģ�壺" + noDelete + "����ɾ�������Ǵ����˻��߹���Ա������ɾ����ģ�壡";
                    } else {
                        msg = "ģ�壺" + noDelete + "����ɾ�������Ǵ����˻��߹���Ա������ɾ����ģ�壡";
                    }
                } else {
                    msg = "��ѡģ��ɾ���ɹ���";
                }

                success = true;
                CILogServiceUtil.getLogServiceInstance().log("COC_TEMPLATE_MANAGE_DELETE", "-1", "����ɾ��ģ��", "ģ��ɾ���ɹ���" + msg + " ɾ��ģ��id��" + this.templateIds + "��", OperResultEnum.Success, LogLevelEnum.Medium);
            } else {
                msg = "ɾ��ʱ������ѡ��һ��ģ��";
                success = false;
            }
        } catch (Exception var13) {
            msg = "ɾ��ģ�����";
            this.log.error(msg, var13);
            success = false;
            CILogServiceUtil.getLogServiceInstance().log("COC_TEMPLATE_MANAGE_DELETE", "-1", "���ģ��", "ɾ��ģ�塾" + this.templateIds + "��ʧ��", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        result.put("msg", msg);
        result.put("success", Boolean.valueOf(success));
        if(success && delIds.size() > 0) {
            result.put("delIds", delIds);
        }

        HttpServletResponse var14 = this.getResponse();

        try {
            this.sendJson(var14, JsonUtil.toJson(result));
        } catch (Exception var12) {
            this.log.error("����json���쳣", var12);
            throw new CIServiceException(var12);
        }
    }

    public void createCustomer() throws Exception {
        String msg = "";
        String validateSql = "";
        boolean flag = false;
        Map returnMap = null;

        try {
            String response = this.getUserId();
            this.ciCustomGroupInfo.setIsPrivate(Integer.valueOf(1));
            returnMap = this.customersService.isNameExist(this.ciCustomGroupInfo, response);
            if(((Boolean)returnMap.get("success")).booleanValue()) {
                List e = this.customersService.queryCiLabelRuleList(this.ciCustomGroupInfo.getTemplateId(), Integer.valueOf(2));
                List newList = this.customersService.getNewCiLabelRuleList(e);
                String lastDate = this.getNewLabelDay();
                validateSql = this.customersService.getValidateSqlStr(newList, this.ciCustomGroupInfo.getDataDate(), lastDate, this.getUserId());
                this.log.debug("validate SQL : " + validateSql);
                flag = this.customersService.queryValidateSql(validateSql);
                if(!flag) {
                    msg = "SQL��֤δͨ����ѡ����·����ݻ�δ���ɣ�";
                    returnMap.put("cmsg", msg);
                    returnMap.put("success", Boolean.valueOf(false));
                } else {
                    this.customersService.addCiCustomGroupInfo(this.ciCustomGroupInfo, e, (List)null, (CiTemplateInfo)null, response, false, (List)null);
                    msg = "�����ɹ���";
                    returnMap.put("cmsg", msg);
                    returnMap.put("success", Boolean.valueOf(true));
                    CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_ADD_TEMPLATE", this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "ģ�塾ģ��ID:" + this.ciCustomGroupInfo.getTemplateId() + "�������ͻ�Ⱥ��" + this.ciCustomGroupInfo.getCustomGroupName() + "���ɹ�", OperResultEnum.Success, LogLevelEnum.Medium);
                }
            }
        } catch (Exception var10) {
            msg = "�����ͻ�Ⱥ����";
            this.log.error(msg, var10);
            returnMap.put("cmsg", msg + "ʧ��");
            returnMap.put("success", Boolean.valueOf(false));
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_ADD_TEMPLATE", "-1", this.ciCustomGroupInfo.getCustomGroupName(), "ģ�塾ģ��ID:" + this.ciCustomGroupInfo.getTemplateId() + "�������ͻ�Ⱥ��" + this.ciCustomGroupInfo.getCustomGroupName() + "��ʧ��:�����ͻ�Ⱥ����", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMap));
        } catch (Exception var9) {
            this.log.error("����json���쳣", var9);
            throw new CIServiceException(var9);
        }
    }

    private Map<String, Object> isTemplateNameExist() {
        boolean success = false;
        HashMap returnMap = new HashMap();
        String msg = "";

        try {
            String e = this.ciTemplateInfo.getTemplateName();
            if(StringUtil.isEmpty(e)) {
                msg = "ģ�����Ʋ���Ϊ�գ�";
            } else {
                success = true;
            }

            if(success) {
                List list = this.ciTemplateInfoService.queryTemplateInfoListByTemplateName(e);
                if(list != null && list.size() > 0) {
                    CiTemplateInfo info = (CiTemplateInfo)list.get(0);
                    if(!this.ciTemplateInfo.getTemplateId().equals(info.getTemplateId())) {
                        msg = "�����ģ����������������������";
                        success = false;
                    }
                }
            }
        } catch (Exception var7) {
            msg = "������֤����";
            this.log.error(msg, var7);
            success = false;
            returnMap.put("success", Boolean.valueOf(success));
            returnMap.put("cmsg", msg);
        }

        returnMap.put("success", Boolean.valueOf(success));
        returnMap.put("cmsg", msg);
        return returnMap;
    }

    public void saveShareTemplate() {
        String msg = "";
        Map returnMap = null;
        boolean flag = false;

        try {
            returnMap = this.isTemplateNameExist();
            if(((Boolean)returnMap.get("success")).booleanValue()) {
                String response = this.getUserId();
                String e = this.ciTemplateInfo.getTemplateId();
                CiTemplateInfo templateInfo = this.ciTemplateInfoService.queryTemplateInfoById(e);
                templateInfo.setIsPrivate(Integer.valueOf(0));
                templateInfo.setTemplateName(this.ciTemplateInfo.getTemplateName());
                templateInfo.setNewModifyTime(new Date());
                this.ciTemplateInfoService.modifyTemplateInfoPublic(templateInfo, response);
                flag = true;
                msg = "ģ�干��ɹ���";
                returnMap.put("cmsg", msg);
                returnMap.put("success", Boolean.valueOf(flag));
            }
        } catch (Exception var8) {
            this.log.error("ģ�干��ʧ��", var8);
            flag = false;
            msg = "ģ�干��ʧ�ܣ�";
            returnMap.put("cmsg", msg);
            returnMap.put("success", Boolean.valueOf(flag));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMap));
        } catch (Exception var7) {
            this.log.error("����json���쳣", var7);
            throw new CIServiceException(var7);
        }
    }

    public String findLatestTemplateList() throws Exception {
        if(this.searchBean == null) {
            this.searchBean = new CiTemplateInfo();
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        Date modifyEndDate = new Date();
        Date modifyStartDate = DateUtil.getStartDate(modifyEndDate, 15);
        if(modifyStartDate != null) {
            this.searchBean.setModifyStartDate(modifyStartDate);
        }

        if(modifyEndDate != null) {
            this.searchBean.setModifyEndDate(modifyEndDate);
        }

        try {
            String e = this.getUserId();
            if(!PrivilegeServiceUtil.isAdminUser(e)) {
                this.searchBean.setUserId(e);
            }

            int msg1 = this.pager.getTotalPage();
            if(StringUtil.isEmpty(this.currentPage)) {
                this.currentPage = "1";
            }

            int curPage = Integer.valueOf(this.currentPage).intValue();
            this.pager.setPageNum(curPage);
            if(curPage <= msg1) {
                byte pageSize = 5;
                this.pager.setPageSize(pageSize);
                this.pager.setTotalPage(msg1);
                this.ciTemplateInfoList = this.ciTemplateInfoService.queryTemplateInfoList(this.pager, this.searchBean);
            }

            return "findMore";
        } catch (Exception var7) {
            String msg = "��ѯģ���б����";
            this.log.error(msg, var7);
            throw new CIServiceException(var7);
        }
    }

    public void findLatestTemplateTotalNum() throws Exception {
        if(this.searchBean == null) {
            this.searchBean = new CiTemplateInfo();
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        Date modifyEndDate = new Date();
        Date modifyStartDate = DateUtil.getStartDate(modifyEndDate, 15);
        if(modifyStartDate != null) {
            this.searchBean.setModifyStartDate(modifyStartDate);
        }

        if(modifyEndDate != null) {
            this.searchBean.setModifyEndDate(modifyEndDate);
        }

        boolean success = false;
        HashMap result = new HashMap();
        String msg = "";
        int totalPage = 0;

        try {
            String response = this.getUserId();
            if(!PrivilegeServiceUtil.isAdminUser(response)) {
                this.searchBean.setUserId(response);
            }

            byte e = 5;
            int totalSize = this.ciTemplateInfoService.queryTemplateInfoCount(this.searchBean);
            totalPage = (int)Math.ceil((double)totalSize / (double)e);
            this.pager.setTotalPage(totalPage);
            this.pager.setPageSize(e);
            success = true;
            CILogServiceUtil.getLogServiceInstance().log("COC_TEMPLATE_MANAGE_SELECT", "", "", "�ҵ�ģ���ѯ�ɹ�,��ѯ������ģ������ :" + this.searchBean.getTemplateName() + ",ʹ�õı�ǩ:" + this.searchBean.getLabelOptRuleShow() + ",����ʱ���:" + this.searchBean.getStartDate() + " �� " + this.searchBean.getEndDate() + "��", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var11) {
            msg = "��ѯģ���б���ҳ������";
            this.log.error(msg, var11);
            success = false;
            CILogServiceUtil.getLogServiceInstance().log("COC_TEMPLATE_MANAGE_SELECT", (String)null, (String)null, "�ҵ�ģ���ѯʧ��,��ѯ������ģ������ :" + this.searchBean.getTemplateName() + ",ʹ�õı�ǩ:" + this.searchBean.getLabelOptRuleShow() + ",����ʱ���:" + this.searchBean.getStartDate() + " �� " + this.searchBean.getEndDate() + "��", OperResultEnum.Success, LogLevelEnum.Medium);
        }

        result.put("success", Boolean.valueOf(success));
        if(success) {
            result.put("totalPage", Integer.valueOf(totalPage));
        } else {
            result.put("msg", msg);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var10) {
            this.log.error("����json���쳣", var10);
            throw new CIServiceException(var10);
        }
    }

    public void findTemplateById() {
        HashMap result = new HashMap();
        String sceneId = null;
        String sceneName = null;

        try {
            this.ciTemplateInfo = this.ciTemplateInfoService.queryTemplateInfoById(this.templateId);
            String response = this.ciTemplateInfo.getSceneId();
            if(!StringUtil.isEmpty(response)) {
                DimScene e = this.ciTemplateInfoService.querySceneInfoById(response);
                if(e != null) {
                    sceneId = e.getSceneId();
                    sceneName = e.getSceneName();
                }
            }
        } catch (Exception var7) {
            this.log.error("��ѯģ�������쳣", var7);
            throw new CIServiceException(var7);
        }

        result.put("sceneId", sceneId);
        result.put("sceneName", sceneName);
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("����json���쳣", var6);
            throw new CIServiceException(var6);
        }
    }

    public String loadPagedTemplateIndex() throws Exception {
        CacheBase cache = CacheBase.getInstance();
        this.dimScenes =(List<DimScene>) cache.getObjectList("DIM_SCENE");
        return "loadTemplateIndex";
    }

    public String loadHotShareTemplate() throws Exception {
        if(this.searchBean == null) {
            this.searchBean = new CiTemplateInfo();
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        try {
            if(StringUtil.isNotEmpty(this.isLatestTemplateFlag) && (this.isLatestTemplateFlag.equals("true") || this.isLatestTemplateFlag == "true")) {
                Date e = new Date();
                Date msg1 = DateUtil.getStartDate(e, 15);
                if(msg1 != null) {
                    this.searchBean.setModifyStartDate(msg1);
                }

                if(e != null) {
                    this.searchBean.setModifyEndDate(e);
                }
            }

            String e1 = this.getUserId();
            if(!PrivilegeServiceUtil.isAdminUser(e1)) {
                this.searchBean.setUserId(e1);
            }

            this.pager.setPageNum(1);
            this.pager.setPageSize(ServiceConstants.SHOW_HOT_TEMPLATE_NUM);
            this.searchBean.setIsHotList(ServiceConstants.IS_HOT_LIST);
            this.ciTemplateInfoHotList = this.ciTemplateInfoService.queryTemplateInfoList(this.pager, this.searchBean);
            return "loadHotShareTemplateList";
        } catch (Exception var3) {
            String msg = "��ѯģ���б����";
            this.log.error(msg, var3);
            throw new CIServiceException(var3);
        }
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public CiTemplateInfo getCiTemplateInfo() {
        return this.ciTemplateInfo;
    }

    public void setCiTemplateInfo(CiTemplateInfo ciTemplateInfo) {
        this.ciTemplateInfo = ciTemplateInfo;
    }

    public CiTemplateInfo getSearchBean() {
        return this.searchBean;
    }

    public void setSearchBean(CiTemplateInfo searchBean) {
        this.searchBean = searchBean;
    }

    public List<CiLabelRule> getCiLabelRuleList() {
        return this.ciLabelRuleList;
    }

    public void setCiLabelRuleList(List<CiLabelRule> ciLabelRuleList) {
        this.ciLabelRuleList = ciLabelRuleList;
    }

    public String getTemplateIds() {
        return this.templateIds;
    }

    public void setTemplateIds(String templateIds) {
        this.templateIds = templateIds;
    }

    public List<CiTemplateInfo> getCiTemplateInfoList() {
        return this.ciTemplateInfoList;
    }

    public void setCiTemplateInfoList(List<CiTemplateInfo> ciTemplateInfoList) {
        this.ciTemplateInfoList = ciTemplateInfoList;
    }

    public CiCustomGroupInfo getCiCustomGroupInfo() {
        return this.ciCustomGroupInfo;
    }

    public void setCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo) {
        this.ciCustomGroupInfo = ciCustomGroupInfo;
    }

    public String getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public List<DimScene> getDimSceneList() {
        return this.dimSceneList;
    }

    public void setDimSceneList(List<DimScene> dimSceneList) {
        this.dimSceneList = dimSceneList;
    }

    public String getIsLatestTemplateFlag() {
        return this.isLatestTemplateFlag;
    }

    public void setIsLatestTemplateFlag(String isLatestTemplateFlag) {
        this.isLatestTemplateFlag = isLatestTemplateFlag;
    }

    public String getTemplateId() {
        return this.templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public List<DimScene> getDimScenes() {
        return this.dimScenes;
    }

    public void setDimScenes(List<DimScene> dimScenes) {
        this.dimScenes = dimScenes;
    }

    public String getIsSysRecommendTemplateFlag() {
        return this.isSysRecommendTemplateFlag;
    }

    public void setIsSysRecommendTemplateFlag(String isSysRecommendTemplateFlag) {
        this.isSysRecommendTemplateFlag = isSysRecommendTemplateFlag;
    }

    public List<CiTemplateInfo> getCiTemplateInfoHotList() {
        return this.ciTemplateInfoHotList;
    }

    public void setCiTemplateInfoHotList(List<CiTemplateInfo> ciTemplateInfoHotList) {
        this.ciTemplateInfoHotList = ciTemplateInfoHotList;
    }
}
