package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiLabelInfoJDao;
import com.ailk.biapp.ci.dao.ILabelStatDao;
import com.ailk.biapp.ci.entity.DimCocLabelStatus;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ILabelStatService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("labelStatService")
public class LabelStatServiceImpl implements ILabelStatService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ILabelStatDao labelStatDao;
    @Autowired
    private ICiLabelInfoJDao labelInfoJDao;

    public LabelStatServiceImpl() {
    }

    public void statLabelCustomNum(String dataDate, int updateCycle) throws CIServiceException {
        try {
            this.labelStatDao.statLabelCustomNum(dataDate, updateCycle);
        } catch (Exception var4) {
            var4.printStackTrace();
            this.log.error("统计标签客户数失败 updateCycle = " + updateCycle + " dataDate=" + dataDate, var4);
            throw new CIServiceException("统计标签客户数失败 updateCycle = " + updateCycle + " dataDate=" + dataDate);
        }
    }

    public void statLabelProportion(String dataDate, int updateCycle) throws CIServiceException {
        try {
            this.labelStatDao.statLabelProportion(dataDate, updateCycle);
        } catch (Exception var4) {
            var4.printStackTrace();
            this.log.error("统计标签的客户数占比失败 updateCycle = " + updateCycle + " dataDate=" + dataDate, var4);
            throw new CIServiceException("统计标签的客户数占比失败 updateCycle = " + updateCycle + " dataDate=" + dataDate);
        }
    }

    public void statLabelRingNum(String dataDate, int updateCycle) throws CIServiceException {
        try {
            this.labelStatDao.statLabelRingNum(dataDate, updateCycle);
        } catch (Exception var4) {
            var4.printStackTrace();
            this.log.error("统计标签的客户数环比增长量失败 updateCycle = " + updateCycle + " dataDate=" + dataDate, var4);
            throw new CIServiceException("统计标签的客户数环比增长量失败 updateCycle = " + updateCycle + " dataDate=" + dataDate);
        }
    }

    public void updateLabelTotalCustomNum(String dataDate, int updateCycle) throws CIServiceException {
        try {
            this.labelStatDao.updateLabelTotalCustomNum(dataDate, updateCycle);
        } catch (Exception var4) {
            var4.printStackTrace();
            this.log.error("更新标签最新总客户数失败 updateCycle = " + updateCycle + " dataDate=" + dataDate, var4);
            throw new CIServiceException("更新标签最新总客户数失败 updateCycle = " + updateCycle + " dataDate=" + dataDate);
        }
    }

    public int dataDateExist(String dataDate) throws CIServiceException {
        boolean dataDateNum = false;

        try {
            int dataDateNum1 = this.labelStatDao.dataDateExist(dataDate);
            return dataDateNum1;
        } catch (Exception var4) {
            var4.printStackTrace();
            this.log.error("查询是否存在传入月份的数据异常", var4);
            throw new CIServiceException("查询是否存在传入月份的数据异常");
        }
    }

    public void updateDwNullDataTo0ForMonth(String month) throws CIServiceException {
        try {
            this.labelStatDao.updateDwNullDataTo0ForMonth(month);
        } catch (Exception var3) {
            var3.printStackTrace();
            this.log.error("将月统计数据各维度为null的数据更新为0异常", var3);
            throw new CIServiceException("将月统计数据各维度为null的数据更新为0异常");
        }
    }

    public void updateDwNullDataTo0ForDay(String day) throws CIServiceException {
        try {
            this.labelStatDao.updateDwNullDataTo0ForDay(day);
        } catch (Exception var3) {
            var3.printStackTrace();
            this.log.error("将日统计数据各维度为null的数据更新为0异常", var3);
            throw new CIServiceException("将日统计数据各维度为null的数据更新为0异常");
        }
    }

    public void updateLabelStatDateStatus(int updateCycle) throws CIServiceException {
        try {
            this.labelStatDao.updateLabelStatDateStatus(updateCycle);
        } catch (Exception var3) {
            var3.printStackTrace();
            this.log.error("更新最新数据日期的是否统计过用户数异常", var3);
            throw new CIServiceException("更新最新数据日期的是否统计过用户数异常");
        }
    }

    public int updateCiLabelInfoDataDate(List<DimCocLabelStatus> labelStatList, String dataDate) {
        return this.labelInfoJDao.updateCiLabelInfoDataDate(labelStatList, dataDate);
    }
}
