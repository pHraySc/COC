package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.CiLabelRelModel;
import com.ailk.biapp.ci.service.ICiLabelRelAnalysisService;
import com.ailk.biapp.ci.service.ICiLabelUserUseService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.IdToName;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiLabelRelAnalysisAction extends CIBaseAction {
    private Logger log = Logger.getLogger(CiLabelRelAnalysisAction.class);
    @Autowired
    private ICiLabelRelAnalysisService ciLabelRelAnalysisService;
    @Autowired
    private ICiLabelUserUseService ciLabelUserUseService;
    private CiLabelRelModel ciLabelRelModel;
    private CiCustomGroupInfo ciCustomGroupInfo;
    private List<CiLabelRelModel> ciLabelRelModelList;

    public CiLabelRelAnalysisAction() {
    }

    public String toLabelRel() throws Exception {
        String userId = this.getUserId();
        boolean noLabelRel = false;

        try {
            List e = this.ciLabelRelAnalysisService.queryRelLabelByMainLabel(userId, this.ciLabelRelModel);
            byte noLabelRel1;
            if(null != e && e.size() > 0) {
                noLabelRel1 = 0;
            } else {
                noLabelRel1 = 1;
            }

            ActionContext ctx = ActionContext.getContext();
            ctx.put("noLabelRel", Integer.valueOf(noLabelRel1));
        } catch (Exception var5) {
            this.log.error("��ѯ����ǩ�Ĺ�����ǩ�쳣" + var5.getMessage());
        }

        return "labelRel";
    }

    public String toLabelRelMain() throws Exception {
        return "labelRelMain";
    }

    public String toLabelRelAnalysis() throws Exception {
        try {
            this.ciLabelRelAnalysisService.queryMainLabelUserNum(this.ciLabelRelModel);
            String e1 = CacheBase.getInstance().getLabelNameByKey("ALL_EFFECTIVE_LABEL_MAP", this.ciLabelRelModel.getMainLabelId());
            this.ciLabelRelModel.setMainLabelName(e1);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_REL_ANALYSIS_LINK", this.ciLabelRelModel.getMainLabelId().toString(), "", "��ǩ����->��ǩ���������鿴�ɹ�,����ǩId:" + this.ciLabelRelModel.getMainLabelId() + "��", OperResultEnum.Success, LogLevelEnum.Normal);
        } catch (Exception var2) {
            this.log.error("��ѯ����ǩ�û����쳣", var2);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_REL_ANALYSIS_LINK", this.ciLabelRelModel.getMainLabelId().toString(), "", "��ǩ����->��ǩ���������鿴ʧ��,����ǩId:" + this.ciLabelRelModel.getMainLabelId() + "��", OperResultEnum.Failure, LogLevelEnum.Normal);
        }

        return "labelRelAnalysis";
    }

    public void findMainLabelUserNum() throws Exception {
        DecimalFormat df = new DecimalFormat("###,###,###,###");
        boolean success = false;
        HashMap result = new HashMap();

        try {
            this.ciLabelRelAnalysisService.queryMainLabelUserNum(this.ciLabelRelModel);
            success = true;
            this.ciLabelRelModel.setMainLabelUserNumShow(df.format(this.ciLabelRelModel.getMainLabelUserNum()));
            if(this.ciLabelRelModel.getMainLabelUserNum().longValue() == 0L) {
                String response = "����ǩ������";
                result.put("message", response);
            }

            result.put("ciLabelRelModel", this.ciLabelRelModel);
            result.put("success", Boolean.valueOf(success));
        } catch (CIServiceException var7) {
            String e = "����ǩ������";
            this.log.error(e, var7);
            success = false;
            result.put("message", e);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("����json���쳣", var6);
            throw new CIServiceException(var6);
        }
    }

    public void findRelLabelData() throws Exception {
        boolean success = false;
        HashMap result = new HashMap();

        try {
            this.ciLabelRelAnalysisService.queryRelLabelData(this.ciLabelRelModel);

            try {
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_REL_ANALYSIS", this.ciLabelRelModel.getRelLabelId().toString(), IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", this.ciLabelRelModel.getRelLabelId()), "��ǩ��������ȡ���ɹ���������ǩ����:" + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", this.ciLabelRelModel.getMainLabelId().toString()) + ",����ǩID:" + this.ciLabelRelModel.getMainLabelId() + "��������ǩID:" + this.ciLabelRelModel.getRelLabelId() + "��", OperResultEnum.Success, LogLevelEnum.Medium);
            } catch (Exception var6) {
                var6.printStackTrace();
            }

            success = true;
            result.put("ciLabelRelModel", this.ciLabelRelModel);
            result.put("success", Boolean.valueOf(success));
        } catch (CIServiceException var7) {
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_REL_ANALYSIS", this.ciLabelRelModel.getRelLabelId().toString(), IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", this.ciLabelRelModel.getRelLabelId()), "��ǩ��������ȡ��ʧ�ܣ�������ǩ����:" + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", this.ciLabelRelModel.getMainLabelId().toString()) + ",����ǩID:" + this.ciLabelRelModel.getMainLabelId() + "��������ǩID:" + this.ciLabelRelModel.getRelLabelId() + "��", OperResultEnum.Failure, LogLevelEnum.Medium);
            String e = "��ù�����ǩ������ǩ�����û���ʧ��";
            this.log.error(e, var7);
            success = false;
            result.put("message", e);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var5) {
            this.log.error("����json���쳣", var5);
            throw new CIServiceException(var5);
        }
    }

    public void findRelLabelByDate() throws Exception {
        boolean success = false;
        HashMap result = new HashMap();

        try {
            List response = this.ciLabelRelAnalysisService.queryRelLabelByDate(this.ciLabelRelModel);
            success = true;
            result.put("relLabelList", response);
            result.put("success", Boolean.valueOf(success));
        } catch (CIServiceException var6) {
            String e = "�������ڲ�����ѡ�б�ǩ��ռ��ʧ��";
            this.log.error(e, var6);
            success = false;
            result.put("message", e);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var5) {
            this.log.error("����json���쳣", var5);
            throw new CIServiceException(var5);
        }
    }

    public void findLabelRel() throws Exception {
        String userId = this.getUserId();
        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "��ǩ������ѯ�ɹ���";

        try {
            this.ciLabelRelModelList = this.ciLabelRelAnalysisService.queryLabelRel(userId, this.ciLabelRelModel);
            returnMsg.put("ciLabelRelModelList", this.ciLabelRelModelList);
        } catch (Exception var8) {
            message = "��ǩ������ѯ�쳣";
            this.log.error(message, var8);
            success = false;
            var8.printStackTrace();
        }

        HttpServletResponse response = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("message", message);

        try {
            this.sendJson(response, JsonUtil.toJson(returnMsg));
        } catch (Exception var7) {
            this.log.error("����json���쳣", var7);
            throw new CIServiceException(var7);
        }
    }

    public String findRelLabelByMainLabel() throws Exception {
        String userId = this.getUserId();
        Random random = new Random();
        Double firstAngle = Double.valueOf(0.0D);
        Double angle = Double.valueOf(0.0D);
        this.ciLabelRelModel.setMainLabelName(IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", this.ciLabelRelModel.getMainLabelId()));
        this.ciLabelRelModel.setMainLabelUserNum(this.ciLabelRelAnalysisService.queryCustomNumByLabelId(this.ciLabelRelModel.getMainLabelId(), this.ciLabelRelModel.getDataDate()));
        this.ciLabelRelModelList = this.ciLabelRelAnalysisService.queryRelLabelByMainLabel(userId, this.ciLabelRelModel);
        ArrayList _40PerRelList = new ArrayList();
        ArrayList _70PerRelList = new ArrayList();
        ArrayList _100PerRelList = new ArrayList();

        for(int ctx = 0; ctx < this.ciLabelRelModelList.size(); ++ctx) {
            CiLabelRelModel labelInfo = (CiLabelRelModel)this.ciLabelRelModelList.get(ctx);
            labelInfo.setRelLabelName(IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", labelInfo.getRelLabelId()));
            if(ctx == 0) {
                firstAngle = angle = Double.valueOf(6.283185307179586D * random.nextDouble());
            } else {
                angle = Double.valueOf(firstAngle.doubleValue() + 6.283185307179586D / (double)this.ciLabelRelModelList.size() * (double)ctx);
            }

            if(Double.valueOf(labelInfo.getProportion()).doubleValue() <= 40.0D) {
                labelInfo.setRadius(Integer.valueOf(50));
                this.setCoordinate(labelInfo, Double.valueOf(0.4D), angle);
                _40PerRelList.add(labelInfo);
            }

            if(Double.valueOf(labelInfo.getProportion()).doubleValue() <= 70.0D && Double.valueOf(labelInfo.getProportion()).doubleValue() > 40.0D) {
                labelInfo.setRadius(Integer.valueOf(50));
                this.setCoordinate(labelInfo, Double.valueOf(0.6D), angle);
                _70PerRelList.add(labelInfo);
            }

            if(Double.valueOf(labelInfo.getProportion()).doubleValue() < 100.0D && Double.valueOf(labelInfo.getProportion()).doubleValue() > 70.0D) {
                labelInfo.setRadius(Integer.valueOf(50));
                this.setCoordinate(labelInfo, Double.valueOf(0.8D), angle);
                _100PerRelList.add(labelInfo);
            }
        }

        ActionContext var10 = ActionContext.getContext();
        var10.put("_40PerRelList", _40PerRelList);
        var10.put("_70PerRelList", _70PerRelList);
        var10.put("_100PerRelList", _100PerRelList);
        var10.put("ciLabelRelModelList", this.ciLabelRelModelList);
        CiLabelInfo var11 = CacheBase.getInstance().getEffectiveLabel(this.ciLabelRelModel.getMainLabelId().toString());
        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_REL_ANALYSIS_VIEW", this.ciLabelRelModel.getMainLabelId().toString(), var11.getLabelName(), "��ǩ����->��ǩ��������ͼ�鿴,����ǩId:" + this.ciLabelRelModel.getMainLabelId() + "��ǩ���ƣ�" + var11.getLabelName() + "��", OperResultEnum.Success, LogLevelEnum.Medium);
        return "labelRelChart";
    }

    protected void setCoordinate(CiLabelRelModel relModel, Double percent, Double angle) {
        double offset = 10.0D;
        double x = 0.0D;
        double y = 0.0D;
        x = (50.0D * (1.0D - percent.doubleValue()) + offset) * Math.cos(angle.doubleValue()) + 50.0D;
        y = (50.0D * (1.0D - percent.doubleValue()) + offset) * Math.sin(angle.doubleValue()) + 50.0D;
        relModel.setX(Integer.valueOf((int)Math.ceil(x)));
        relModel.setY(Integer.valueOf((int)Math.ceil(y)));
    }

    public String saveUserRelLabel() throws Exception {
        try {
            this.ciLabelUserUseService.addLabelUserUseLog(String.valueOf(this.ciLabelRelModel.getMainLabelId()), 5);
        } catch (Exception var13) {
            this.log.error("��¼��ǩʹ��ʧ��" + var13.getMessage());
            var13.printStackTrace();
        }

        String userId = this.getUserId();

        try {
            this.ciLabelRelAnalysisService.addUserRelLabel(userId, this.ciLabelRelModel);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_REL_SAVE", null != this.ciLabelRelModel.getMainLabelId()?this.ciLabelRelModel.getMainLabelId().toString():null, IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", this.ciLabelRelModel.getMainLabelId()), "�����û�ָ��������ǩ�͹�����ǩ�Ĺ�����ϵ�ɹ�������ǩ����:" + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", this.ciLabelRelModel.getMainLabelId()) + ",����ǩID:" + this.ciLabelRelModel.getMainLabelId() + "��������ǩID:" + this.ciLabelRelModel.getRelLabelIds() + "��", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var14) {
            this.log.error("�����û�ָ��������ǩ�͹�����ǩ�Ĺ�����ϵʧ��", var14);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_REL_SAVE", null != this.ciLabelRelModel.getMainLabelId()?this.ciLabelRelModel.getMainLabelId().toString():null, IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", this.ciLabelRelModel.getMainLabelId()), "�����û�ָ��������ǩ�͹�����ǩ�Ĺ�����ϵ�ɹ�������ǩ����:" + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", this.ciLabelRelModel.getMainLabelId()) + ",����ǩID:" + this.ciLabelRelModel.getMainLabelId() + "��������ǩID:" + this.ciLabelRelModel.getRelLabelIds() + "��", OperResultEnum.Failure, LogLevelEnum.Medium);
            throw new CIServiceException(var14);
        }

        ArrayList labelRelList = new ArrayList();

        try {
            this.ciLabelRelModel.setMainLabelName(IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", this.ciLabelRelModel.getMainLabelId()));
            String[] e = StringUtil.isEmpty(this.ciLabelRelModel.getRelLabelIds())?null:this.ciLabelRelModel.getRelLabelIds().split(",");
            String[] overlapUserNum = StringUtil.isEmpty(this.ciLabelRelModel.getOverlapUserNumStr())?null:this.ciLabelRelModel.getOverlapUserNumStr().split(",");
            if(null != e && e.length > 0) {
                for(int random = 0; random < e.length; ++random) {
                    CiLabelRelModel firstAngle = new CiLabelRelModel();
                    firstAngle.setMainLabelId(this.ciLabelRelModel.getMainLabelId());
                    firstAngle.setMainLabelUserNum(this.ciLabelRelModel.getMainLabelUserNum());
                    firstAngle.setDataDate(this.ciLabelRelModel.getDataDate());
                    firstAngle.setRelLabelId(Integer.valueOf(e[random]));
                    firstAngle.setOverlapUserNum(Long.valueOf(overlapUserNum[random]));
                    firstAngle.setRelLabelName(IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", firstAngle.getRelLabelId()));
                    labelRelList.add(firstAngle);
                }
            }

            Random var16 = new Random();
            Double var17 = Double.valueOf(0.0D);
            Double angle = Double.valueOf(0.0D);
            ArrayList _40PerRelList = new ArrayList();
            ArrayList _70PerRelList = new ArrayList();
            ArrayList _100PerRelList = new ArrayList();

            for(int ctx = 0; ctx < labelRelList.size(); ++ctx) {
                CiLabelRelModel relModel = (CiLabelRelModel)labelRelList.get(ctx);
                if(ctx == 0) {
                    var17 = angle = Double.valueOf(6.283185307179586D * var16.nextDouble());
                } else {
                    angle = Double.valueOf(var17.doubleValue() + 6.283185307179586D / (double)labelRelList.size() * (double)ctx);
                }

                if(Double.valueOf(relModel.getProportion()).doubleValue() <= 40.0D) {
                    relModel.setRadius(Integer.valueOf(50));
                    this.setCoordinate(relModel, Double.valueOf(0.4D), angle);
                    _40PerRelList.add(relModel);
                }

                if(Double.valueOf(relModel.getProportion()).doubleValue() <= 70.0D && Double.valueOf(relModel.getProportion()).doubleValue() > 40.0D) {
                    relModel.setRadius(Integer.valueOf(50));
                    this.setCoordinate(relModel, Double.valueOf(0.6D), angle);
                    _70PerRelList.add(relModel);
                }

                if(Double.valueOf(relModel.getProportion()).doubleValue() < 100.0D && Double.valueOf(relModel.getProportion()).doubleValue() > 70.0D) {
                    relModel.setRadius(Integer.valueOf(50));
                    this.setCoordinate(relModel, Double.valueOf(0.8D), angle);
                    _100PerRelList.add(relModel);
                }
            }

            ActionContext var18 = ActionContext.getContext();
            var18.put("_40PerRelList", _40PerRelList);
            var18.put("_70PerRelList", _70PerRelList);
            var18.put("_100PerRelList", _100PerRelList);
            var18.put("labelRelList", labelRelList);
        } catch (Exception var15) {
            this.log.error(var15);
            var15.printStackTrace();
        }

        return "labelRelChartForSaveCustom";
    }

    /** @deprecated */
    @Deprecated
    public String delUserRelLabel() throws Exception {
        String userId = this.getUserId();
        this.ciLabelRelAnalysisService.deleteUserRelLabel(userId, this.ciLabelRelModel);
        return null;
    }

    public void saveRelLabelAsCustomGroup() throws Exception {
        String userId = this.getUserId();
        this.ciCustomGroupInfo.setCustomGroupName(this.ciCustomGroupInfo.getCustomGroupName());
        this.ciCustomGroupInfo.setCustomGroupDesc(this.ciCustomGroupInfo.getCustomGroupDesc());
        String customerName = this.ciCustomGroupInfo.getCustomGroupName();
        HashMap returnMap = new HashMap();

        try {
            this.ciLabelRelAnalysisService.addRelLabelAsCustomGroup(userId, this.ciLabelRelModel, this.ciCustomGroupInfo);
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_ADD_LABEL_ANALYSIS", this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "������ǩ�������ǩ�Ĺ�ϵ����Ϊ�ͻ�Ⱥ�ɹ�������ǩ����:" + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", this.ciLabelRelModel.getMainLabelId()) + ",����ǩID:" + this.ciLabelRelModel.getMainLabelId() + "��������ǩID:" + this.ciLabelRelModel.getRelLabelId() + ",�ͻ�Ⱥ����:" + this.ciCustomGroupInfo.getCustomGroupDesc() + "��", OperResultEnum.Success, LogLevelEnum.Medium);
            returnMap.put("cmsg", "����ͻ�Ⱥ�ɹ�");
            returnMap.put("success", Boolean.valueOf(true));
        } catch (Exception var7) {
            this.log.error("������ǩ�������ǩ�Ĺ�ϵ����Ϊ�ͻ�Ⱥʧ��", var7);
            returnMap.put("cmsg", "����ͻ�Ⱥʧ��");
            returnMap.put("success", Boolean.valueOf(false));
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_ADD_LABEL_ANALYSIS", this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "������ǩ�������ǩ�Ĺ�ϵ����Ϊ�ͻ�Ⱥʧ�ܡ�����ǩ����:" + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", this.ciLabelRelModel.getMainLabelId()) + ",����ǩID:" + this.ciLabelRelModel.getMainLabelId() + "��������ǩID:" + this.ciLabelRelModel.getRelLabelId() + ",�ͻ�Ⱥ����:" + this.ciCustomGroupInfo.getCustomGroupDesc() + "��", OperResultEnum.Failure, LogLevelEnum.Medium);
            throw new CIServiceException(var7);
        }

        HttpServletResponse response = this.getResponse();
        returnMap.put("customerName", customerName);

        try {
            this.sendJson(response, JsonUtil.toJson(returnMap));
        } catch (Exception var6) {
            this.log.error("����json���쳣", var6);
            throw new CIServiceException(var6);
        }
    }

    public CiLabelRelModel getCiLabelRelModel() {
        return this.ciLabelRelModel;
    }

    public void setCiLabelRelModel(CiLabelRelModel ciLabelRelModel) {
        this.ciLabelRelModel = ciLabelRelModel;
    }

    public void setCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo) {
        this.ciCustomGroupInfo = ciCustomGroupInfo;
    }

    public CiCustomGroupInfo getCiCustomGroupInfo() {
        return this.ciCustomGroupInfo;
    }
}
