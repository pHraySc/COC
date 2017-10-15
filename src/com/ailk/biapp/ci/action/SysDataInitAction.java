package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.IAddNewLabelService;
import com.ailk.biapp.ci.service.ICiLabelUserUseService;
import com.ailk.biapp.ci.service.ICustomGroupStatService;
import com.ailk.biapp.ci.service.ILabelDataTransferService;
import com.ailk.biapp.ci.service.ILabelStatService;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.privilege.ICity;
import com.asiainfo.biframe.privilege.IUserPrivilegeService;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.DES;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class SysDataInitAction extends CIBaseAction {
    private Logger log = Logger.getLogger(SysDataInitAction.class);
    @Autowired
    private ILabelStatService labelStatService;
    @Autowired
    private ILabelDataTransferService labelDataTransferService;
    @Autowired
    private ICustomGroupStatService customGroupStatService;
    @Autowired
    public ICiLabelUserUseService ciLabelUserUseService;
    @Autowired
    public IAddNewLabelService addNewLabelService;
    private String month;
    private String day;
    private String customGroupId;
    private String desInput;

    public SysDataInitAction() {
    }

    public void updateDwNullDataTo0ForMonth() {
        boolean success = false;
        HashMap result = new HashMap();
        String message = "";

        try {
            this.labelStatService.updateDwNullDataTo0ForMonth(this.month);
            message = "����ͳ�����ݸ�ά��Ϊnull�����ݸ���Ϊ0�ɹ�";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var7) {
            message = "����ͳ�����ݸ�ά��Ϊnull�����ݸ���Ϊ0ʧ��";
            this.log.error(message, var7);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("����json���쳣", var6);
            throw new CIServiceException(var6);
        }
    }

    public void updateDwNullDataTo0ForDay() {
        boolean success = false;
        HashMap result = new HashMap();
        String message = "";

        try {
            this.labelStatService.updateDwNullDataTo0ForDay(this.day);
            message = "����ͳ�����ݸ�ά��Ϊnull�����ݸ���Ϊ0�ɹ�";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var7) {
            message = "����ͳ�����ݸ�ά��Ϊnull�����ݸ���Ϊ0ʧ��";
            this.log.error(message, var7);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("����json���쳣", var6);
            throw new CIServiceException(var6);
        }
    }

    public void initMonthLabelStatData() {
        boolean success = false;
        HashMap result = new HashMap();
        this.log.info("initMonthLabelStatData month: " + this.month);
        byte updateCycleMonth = 2;
        String message = "";

        try {
            int response = this.labelStatService.dataDateExist(this.month);
            this.log.info("initMonthLabelStatData dataDateNum: " + response);
            if(response <= 0) {
                this.labelStatService.statLabelCustomNum(this.month, updateCycleMonth);
                message = "�±�ǩͳ���������ɳɹ�";
            } else {
                message = "�����·������Ѿ�����";
            }

            try {
                CacheBase.getInstance().init(false);
            } catch (Exception var8) {
                var8.printStackTrace();
            }

            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var9) {
            message = "�±�ǩͳ����������ʧ��";
            this.log.error(message, var9);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("����json���쳣", var7);
            throw new CIServiceException(var7);
        }
    }

    public void initMonthLabelProportionData() {
        boolean success = false;
        HashMap result = new HashMap();
        this.log.info("initMonthLabelProportionData month: " + this.month);
        byte updateCycleMonth = 2;
        String message = "";

        try {
            this.labelStatService.statLabelProportion(this.month, updateCycleMonth);
            message = "�±�ǩռ���������ɳɹ�";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var8) {
            message = "�±�ǩռ����������ʧ��";
            this.log.error(message, var8);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("����json���쳣", var7);
            throw new CIServiceException(var7);
        }
    }

    public void initMonthLabelRingData() {
        boolean success = false;
        HashMap result = new HashMap();
        this.log.info("initMonthLabelStatData month: " + this.month);
        byte updateCycleMonth = 2;
        String message = "";

        try {
            this.labelStatService.statLabelRingNum(this.month, updateCycleMonth);
            message = "�±�ǩ�����������ɳɹ�";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var8) {
            message = "�±�ǩͳ����������ʧ��";
            this.log.error(message, var8);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("����json���쳣", var7);
            throw new CIServiceException(var7);
        }
    }

    public void initDayLabelRingData() {
        boolean success = false;
        HashMap result = new HashMap();
        this.log.info("initMonthLabelStatData day: " + this.day);
        byte updateCycleDay = 1;
        String message = "";

        try {
            this.labelStatService.statLabelRingNum(this.day, updateCycleDay);
            message = "�±�ǩ�����������ɳɹ�";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var8) {
            message = "�±�ǩͳ����������ʧ��";
            this.log.error(message, var8);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("����json���쳣", var7);
            throw new CIServiceException(var7);
        }
    }

    public void initDayLabelStatData() {
        boolean success = false;
        HashMap result = new HashMap();
        this.log.info("initDayLabelStatData day: " + this.day);
        byte updateCycleDay = 1;
        String message = "";

        try {
            int response = this.labelStatService.dataDateExist(this.day);
            this.log.info("initDayLabelStatData dataDateNum: " + response);
            if(response <= 0) {
                this.labelStatService.statLabelCustomNum(this.day, updateCycleDay);
                this.labelStatService.updateLabelTotalCustomNum(this.day, updateCycleDay);
                message = "�ձ�ǩͳ���������ɳɹ�";
            } else {
                message = "�������������Ѿ�����";
            }

            try {
                CacheBase.getInstance().init(false);
            } catch (Exception var8) {
                var8.printStackTrace();
            }

            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var9) {
            message = "�ձ�ǩͳ����������ʧ��";
            this.log.error(message, var9);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("����json���쳣", var7);
            throw new CIServiceException(var7);
        }
    }

    public void initLabelTotalCustomNum() {
        boolean success = false;
        HashMap result = new HashMap();
        this.log.info("initLabelTotalCustomNum day: " + this.day);
        byte updateCycleDay = 1;
        String needDayLabel = Configure.getInstance().getProperty("NEED_DAY_LABEL");
        String message = "";

        try {
            int response = this.labelStatService.dataDateExist(this.day);
            this.log.info("initDayLabelStatData dataDateNum: " + response);
            if("true".equals(needDayLabel)) {
                this.labelStatService.updateLabelTotalCustomNum(this.day, updateCycleDay);
            }

            this.labelStatService.updateLabelTotalCustomNum(this.month, updateCycleDay);
            message = "���±�ǩ�����û����ɹ�";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var9) {
            message = "���±�ǩ�����û���ʧ��";
            this.log.error(message, var9);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var8) {
            this.log.error("����json���쳣", var8);
            throw new CIServiceException(var8);
        }
    }

    public void initDayLabelProportionData() {
        boolean success = false;
        HashMap result = new HashMap();
        this.log.info("initDayLabelProportionData day: " + this.day);
        byte updateCycleDay = 1;
        String message = "";

        try {
            this.labelStatService.statLabelProportion(this.day, updateCycleDay);
            message = "�ձ�ǩռ���������ɳɹ�";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var8) {
            message = "�ձ�ǩռ����������ʧ��";
            this.log.error(message, var8);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("����json���쳣", var7);
            throw new CIServiceException(var7);
        }
    }

    public void initLabelData() {
        boolean success = false;
        HashMap result = new HashMap();
        String[] message = new String[0];

        try {
            message = this.labelDataTransferService.importLabelInfo();
            if("0".equals(message[0])) {
                success = false;
                result.put("msg", message[1]);
            } else {
                success = true;
                result.put("msg", "��ʼ���ɹ�");
            }

            result.put("success", Boolean.valueOf(success));
        } catch (Exception var7) {
            this.log.error(message, var7);
            success = false;
            result.put("msg", "��ʼ��ʧ��" + var7);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("����json���쳣", var6);
            throw new CIServiceException(var6);
        }
    }

    public void increaseLabelData() {
        boolean success = false;
        HashMap result = new HashMap();
        String[] message = new String[0];

        try {
            message = this.labelDataTransferService.increaseLabelInfo();
            if("0".equals(message[0])) {
                success = false;
                result.put("msg", message[1]);
            } else {
                success = true;
                result.put("msg", "��ʼ���ɹ�");
            }

            result.put("success", Boolean.valueOf(success));
        } catch (Exception var7) {
            this.log.error(message, var7);
            success = false;
            result.put("msg", "��ʼ��ʧ��" + var7);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("����json���쳣", var6);
            throw new CIServiceException(var6);
        }
    }

    public void initMonthCustomGroupStatData() {
        boolean success = false;
        HashMap result = new HashMap();
        byte updateCycleMonth = 2;
        String message = "";

        try {
            int response = this.customGroupStatService.dataDateExist(this.month);
            if(response <= 0) {
                this.customGroupStatService.statCustomGroupCustomNum(this.month, updateCycleMonth);
                message = "�¿ͻ�Ⱥͳ���������ɳɹ�";
            } else {
                message = "�����·������Ѿ�����";
            }

            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var8) {
            message = "�¿ͻ�Ⱥͳ����������ʧ��";
            this.log.error(message, var8);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("����json���쳣", var7);
            throw new CIServiceException(var7);
        }
    }

    public void initMonthCustomGroupRingData() {
        boolean success = false;
        HashMap result = new HashMap();
        byte updateCycleMonth = 2;
        String message = "";

        try {
            this.customGroupStatService.statCustomGroupRingNum(this.month, updateCycleMonth);
            message = "ͳ�ƿͻ�Ⱥ�û��������������ɹ�";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var8) {
            message = "ͳ�ƿͻ�Ⱥ�û�������������ʧ��";
            this.log.error(message, var8);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("����json���쳣", var7);
            throw new CIServiceException(var7);
        }
    }

    public void initDayCustomGroupStatData() {
        boolean success = false;
        HashMap result = new HashMap();
        byte updateCycleDay = 3;
        String message = "";

        try {
            int response = this.customGroupStatService.dataDateExist(this.day);
            if(response <= 0) {
                this.customGroupStatService.statCustomGroupCustomNum(this.day, updateCycleDay);
                message = "�տͻ�Ⱥͳ���������ɳɹ�";
            } else {
                message = "�������������Ѿ�����";
            }

            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var8) {
            message = "�տͻ�Ⱥͳ����������ʧ��";
            this.log.error(message, var8);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("����json���쳣", var7);
            throw new CIServiceException(var7);
        }
    }

    public void initMonthLabelUseTimesStatData() {
        boolean success = false;
        HashMap result = new HashMap();
        String message = "";

        try {
            this.ciLabelUserUseService.insertCiLabelMonthUseStat(this.month);
            message = "�±�ǩʹ�ô����������ɳɹ�";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var7) {
            message = "�±�ǩʹ�ô�����������ʧ��";
            this.log.error(message, var7);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("����json���쳣", var6);
            throw new CIServiceException(var6);
        }
    }

    public void initDayLabelUseTimesStatData() {
        boolean success = false;
        HashMap result = new HashMap();
        String message = "";

        try {
            this.ciLabelUserUseService.insertCiLabelDayUseStat(this.day);
            message = "�ձ�ǩʹ�ô����������ɳɹ�";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var7) {
            message = "�ձ�ǩʹ�ô�����������ʧ��";
            this.log.error(message, var7);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("����json���쳣", var6);
            throw new CIServiceException(var6);
        }
    }

    public void initCustomGroupNotExistStatData() {
        boolean success = false;
        HashMap result = new HashMap();
        String message = "";

        try {
            this.customGroupStatService.deleteCustomGroupData(this.month, this.customGroupId);
            this.customGroupStatService.initCustomGroupNotExistStatData(this.month, this.customGroupId);
            message = "��ʼ���ͻ�Ⱥ�����ڵ����ݳɹ�";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var7) {
            message = "��ʼ���ͻ�Ⱥ�����ڵ�����ʧ��";
            this.log.error(message, var7);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("����json���쳣", var6);
            throw new CIServiceException(var6);
        }
    }

    public void addNewLabelTest() {
        boolean success = false;
        HashMap result = new HashMap();
        String message = "";

        try {
            message = "������ǩ���Գɹ�";
            IUserPrivilegeService response = (IUserPrivilegeService)SystemServiceLocator.getInstance().getService("userPrivilegeService");
            List e = response.getCityByUser(this.getUserId());
            Iterator cities1 = e.iterator();

            while(cities1.hasNext()) {
                ICity city = (ICity)cities1.next();
                System.out.print(city.getCityId() + ",");
            }

            System.out.println();
            String cities11 = response.getDmCity(this.userId, (String)null, "CITY", "DBTYPE_APP");
            System.out.print(cities11);
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var9) {
            message = "������ǩ����ʧ��";
            this.log.error(message, var9);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var8) {
            this.log.error("����json���쳣", var8);
            throw new CIServiceException(var8);
        }
    }

    public void toEncrypt() throws Exception {
        HashMap result = new HashMap();
        String message = "";
        String desResult = "";

        try {
            desResult = DES.encrypt(this.desInput);
        } catch (Exception var7) {
            message = "�����ַ���" + this.desInput + "ʧ��";
            this.log.error(message, var7);
        }

        result.put("desResult", desResult);
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("����json���쳣", var6);
            throw new CIServiceException(var6);
        }
    }

    public String getDesInput() {
        return this.desInput;
    }

    public void setDesInput(String desInput) {
        this.desInput = desInput;
    }

    public String getCustomGroupId() {
        return this.customGroupId;
    }

    public void setCustomGroupId(String customGroupId) {
        this.customGroupId = customGroupId;
    }

    public String getMonth() {
        return this.month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return this.day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
