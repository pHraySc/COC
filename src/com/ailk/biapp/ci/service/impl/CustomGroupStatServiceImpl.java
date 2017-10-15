package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICustomGroupStatJDao;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ICustomGroupStatService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("customGroupStatService")
public class CustomGroupStatServiceImpl implements ICustomGroupStatService {
    private Logger log = Logger.getLogger(this.getClass());
    public static final byte[] LOCK = new byte[0];
    @Autowired
    private ICustomGroupStatJDao customGroupStatDao;

    public CustomGroupStatServiceImpl() {
    }

    public void statCustomGroupCustomNum(String dataDate, int updateCycle) throws CIServiceException {
        try {
            this.customGroupStatDao.statCustomGroupCustomNum(dataDate, updateCycle);
        } catch (Exception var4) {
            var4.printStackTrace();
            this.log.error("统计客户群客户数失败 updateCycle = " + updateCycle + " dataDate=" + dataDate, var4);
            throw new CIServiceException("统计客户群客户数失败 updateCycle = " + updateCycle + " dataDate=" + dataDate);
        }
    }

    public void statCustomGroupRingNum(String dataDate, int updateCycle) throws CIServiceException {
        try {
            this.customGroupStatDao.statCustomGroupRingNum(dataDate, updateCycle);
        } catch (Exception var4) {
            var4.printStackTrace();
            this.log.error("统计客户群的客户数环比增长量失败 updateCycle = " + updateCycle + " dataDate=" + dataDate, var4);
            throw new CIServiceException("统计客户群的客户数环比增长量失败 updateCycle = " + updateCycle + " dataDate=" + dataDate);
        }
    }

    public void statCustomGroupCustomNumById(String customGroupId, String listTableName, String dataDate) throws CIServiceException {
        this.log.info("statCustomGroupCustomNumById 同步out lock");
        byte[] var4 = LOCK;
        synchronized(LOCK) {
            this.log.info("statCustomGroupCustomNumById 同步in lock");

            try {
                this.customGroupStatDao.statCustomGroupCustomNumById(customGroupId, listTableName, dataDate);
            } catch (Exception var7) {
                var7.printStackTrace();
                this.log.error("统计指定客户群各维度客户数失败 customGroupId = " + customGroupId + " listTableName=" + listTableName + " dataDate=" + dataDate, var7);
                throw new CIServiceException("统计指定客户群各维度客户数失败 customGroupId = " + customGroupId + " listTableName=" + listTableName + " dataDate=" + dataDate);
            }
        }

        this.log.info("statCustomGroupCustomNumById 同步out lock2");
    }

    public int dataDateExist(String dataDate) throws CIServiceException {
        boolean dataDateNum = false;

        try {
            int dataDateNum1 = this.customGroupStatDao.dataDateExist(dataDate);
            return dataDateNum1;
        } catch (Exception var4) {
            var4.printStackTrace();
            this.log.error("查询是否存在传入月份的数据异常", var4);
            throw new CIServiceException("查询是否存在传入月份的数据异常");
        }
    }

    public void initCustomGroupNotExistStatData(String month, String customGroupId) throws CIServiceException {
    }

    public void deleteCustomGroupData(String month, String customGroupId) {
        try {
            this.customGroupStatDao.deleteCustomGroupData(customGroupId, month);
        } catch (Exception var4) {
            var4.printStackTrace();
            this.log.error("删除客户群数据异常", var4);
            throw new CIServiceException("删除客户群数据异常");
        }
    }
}
