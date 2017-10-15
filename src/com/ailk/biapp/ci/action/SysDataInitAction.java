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
            message = "将月统计数据各维度为null的数据更新为0成功";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var7) {
            message = "将月统计数据各维度为null的数据更新为0失败";
            this.log.error(message, var7);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void updateDwNullDataTo0ForDay() {
        boolean success = false;
        HashMap result = new HashMap();
        String message = "";

        try {
            this.labelStatService.updateDwNullDataTo0ForDay(this.day);
            message = "将日统计数据各维度为null的数据更新为0成功";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var7) {
            message = "将日统计数据各维度为null的数据更新为0失败";
            this.log.error(message, var7);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
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
                message = "月标签统计数据生成成功";
            } else {
                message = "最新月份数据已经存在";
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
            message = "月标签统计数据生成失败";
            this.log.error(message, var9);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
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
            message = "月标签占比数据生成成功";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var8) {
            message = "月标签占比数据生成失败";
            this.log.error(message, var8);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
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
            message = "月标签环比数据生成成功";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var8) {
            message = "月标签统计数据生成失败";
            this.log.error(message, var8);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
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
            message = "月标签环比数据生成成功";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var8) {
            message = "月标签统计数据生成失败";
            this.log.error(message, var8);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
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
                message = "日标签统计数据生成成功";
            } else {
                message = "最新日期数据已经存在";
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
            message = "日标签统计数据生成失败";
            this.log.error(message, var9);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
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
            message = "更新标签最新用户数成功";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var9) {
            message = "更新标签最新用户数失败";
            this.log.error(message, var9);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var8) {
            this.log.error("发送json串异常", var8);
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
            message = "日标签占比数据生成成功";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var8) {
            message = "日标签占比数据生成失败";
            this.log.error(message, var8);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
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
                result.put("msg", "初始化成功");
            }

            result.put("success", Boolean.valueOf(success));
        } catch (Exception var7) {
            this.log.error(message, var7);
            success = false;
            result.put("msg", "初始化失败" + var7);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
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
                result.put("msg", "初始化成功");
            }

            result.put("success", Boolean.valueOf(success));
        } catch (Exception var7) {
            this.log.error(message, var7);
            success = false;
            result.put("msg", "初始化失败" + var7);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
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
                message = "月客户群统计数据生成成功";
            } else {
                message = "最新月份数据已经存在";
            }

            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var8) {
            message = "月客户群统计数据生成失败";
            this.log.error(message, var8);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
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
            message = "统计客户群用户数环比增长量成功";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var8) {
            message = "统计客户群用户数环比增长量失败";
            this.log.error(message, var8);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
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
                message = "日客户群统计数据生成成功";
            } else {
                message = "最新日期数据已经存在";
            }

            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var8) {
            message = "日客户群统计数据生成失败";
            this.log.error(message, var8);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException(var7);
        }
    }

    public void initMonthLabelUseTimesStatData() {
        boolean success = false;
        HashMap result = new HashMap();
        String message = "";

        try {
            this.ciLabelUserUseService.insertCiLabelMonthUseStat(this.month);
            message = "月标签使用次数数据生成成功";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var7) {
            message = "月标签使用次数数据生成失败";
            this.log.error(message, var7);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void initDayLabelUseTimesStatData() {
        boolean success = false;
        HashMap result = new HashMap();
        String message = "";

        try {
            this.ciLabelUserUseService.insertCiLabelDayUseStat(this.day);
            message = "日标签使用次数数据生成成功";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var7) {
            message = "日标签使用次数数据生成失败";
            this.log.error(message, var7);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
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
            message = "初始化客户群不存在的数据成功";
            success = true;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var7) {
            message = "初始化客户群不存在的数据失败";
            this.log.error(message, var7);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void addNewLabelTest() {
        boolean success = false;
        HashMap result = new HashMap();
        String message = "";

        try {
            message = "新增标签测试成功";
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
            message = "新增标签测试失败";
            this.log.error(message, var9);
            success = false;
            result.put("msg", message);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var8) {
            this.log.error("发送json串异常", var8);
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
            message = "加密字符串" + this.desInput + "失败";
            this.log.error(message, var7);
        }

        result.put("desResult", desResult);
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
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
