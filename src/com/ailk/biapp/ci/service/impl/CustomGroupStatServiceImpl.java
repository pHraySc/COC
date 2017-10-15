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
            this.log.error("ͳ�ƿͻ�Ⱥ�ͻ���ʧ�� updateCycle = " + updateCycle + " dataDate=" + dataDate, var4);
            throw new CIServiceException("ͳ�ƿͻ�Ⱥ�ͻ���ʧ�� updateCycle = " + updateCycle + " dataDate=" + dataDate);
        }
    }

    public void statCustomGroupRingNum(String dataDate, int updateCycle) throws CIServiceException {
        try {
            this.customGroupStatDao.statCustomGroupRingNum(dataDate, updateCycle);
        } catch (Exception var4) {
            var4.printStackTrace();
            this.log.error("ͳ�ƿͻ�Ⱥ�Ŀͻ�������������ʧ�� updateCycle = " + updateCycle + " dataDate=" + dataDate, var4);
            throw new CIServiceException("ͳ�ƿͻ�Ⱥ�Ŀͻ�������������ʧ�� updateCycle = " + updateCycle + " dataDate=" + dataDate);
        }
    }

    public void statCustomGroupCustomNumById(String customGroupId, String listTableName, String dataDate) throws CIServiceException {
        this.log.info("statCustomGroupCustomNumById ͬ��out lock");
        byte[] var4 = LOCK;
        synchronized(LOCK) {
            this.log.info("statCustomGroupCustomNumById ͬ��in lock");

            try {
                this.customGroupStatDao.statCustomGroupCustomNumById(customGroupId, listTableName, dataDate);
            } catch (Exception var7) {
                var7.printStackTrace();
                this.log.error("ͳ��ָ���ͻ�Ⱥ��ά�ȿͻ���ʧ�� customGroupId = " + customGroupId + " listTableName=" + listTableName + " dataDate=" + dataDate, var7);
                throw new CIServiceException("ͳ��ָ���ͻ�Ⱥ��ά�ȿͻ���ʧ�� customGroupId = " + customGroupId + " listTableName=" + listTableName + " dataDate=" + dataDate);
            }
        }

        this.log.info("statCustomGroupCustomNumById ͬ��out lock2");
    }

    public int dataDateExist(String dataDate) throws CIServiceException {
        boolean dataDateNum = false;

        try {
            int dataDateNum1 = this.customGroupStatDao.dataDateExist(dataDate);
            return dataDateNum1;
        } catch (Exception var4) {
            var4.printStackTrace();
            this.log.error("��ѯ�Ƿ���ڴ����·ݵ������쳣", var4);
            throw new CIServiceException("��ѯ�Ƿ���ڴ����·ݵ������쳣");
        }
    }

    public void initCustomGroupNotExistStatData(String month, String customGroupId) throws CIServiceException {
    }

    public void deleteCustomGroupData(String month, String customGroupId) {
        try {
            this.customGroupStatDao.deleteCustomGroupData(customGroupId, month);
        } catch (Exception var4) {
            var4.printStackTrace();
            this.log.error("ɾ���ͻ�Ⱥ�����쳣", var4);
            throw new CIServiceException("ɾ���ͻ�Ⱥ�����쳣");
        }
    }
}
