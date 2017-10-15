package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiCustomFileRelHDao;
import com.ailk.biapp.ci.dao.ICiLoadCustomGroupFileJDao;
import com.ailk.biapp.ci.entity.CiCustomFileRel;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ICiCustomFileRelService;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiCustomFileRelServiceImpl implements ICiCustomFileRelService {
    private final Logger log = Logger.getLogger(CiCustomFileRelServiceImpl.class);
    @Autowired
    private ICiCustomFileRelHDao ciCustomFileRelHDao;
    @Autowired
    private ICiLoadCustomGroupFileJDao ciLoadCustomGroupFileJdao;

    public CiCustomFileRelServiceImpl() {
    }

    public void saveCustomFileRel(CiCustomFileRel bean) throws CIServiceException {
        try {
            this.ciCustomFileRelHDao.insertCustomFileRel(bean);
        } catch (Exception var3) {
            this.log.error("", var3);
            throw new CIServiceException("����ͻ�Ⱥ�ļ�ʧ��");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public CiCustomFileRel queryCustomFile(String fileId) throws CIServiceException {
        CiCustomFileRel customFile = null;

        try {
            customFile = this.ciCustomFileRelHDao.selectCustomFile(fileId);
            return customFile;
        } catch (Exception var4) {
            this.log.error("", var4);
            throw new CIServiceException("��ѯ�ͻ�Ⱥ�ļ�ʧ��");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public CiCustomFileRel queryCustomFileByCustomGroupId(String customGroupId) throws CIServiceException {
        CiCustomFileRel ciCustomFileRel = null;

        try {
            ciCustomFileRel = this.ciCustomFileRelHDao.selectCustomFileByCustomGroupId(customGroupId);
            return ciCustomFileRel;
        } catch (Exception var5) {
            String message = "��ѯ�ͻ�Ⱥ������ļ�ʧ��";
            this.log.error(message, var5);
            throw new CIServiceException(message, var5);
        }
    }

    public String createTable(String newTab, String tmpTable, String column, List<CiGroupAttrRel> ciGroupAttrRelList) throws CIServiceException {
        String tableName = "";

        try {
            tableName = this.ciLoadCustomGroupFileJdao.createTable(newTab, tmpTable, column);
            return tableName;
        } catch (Exception var8) {
            String message = "�����嵥��ʧ��";
            this.log.error(message, var8);
            throw new CIServiceException(message, var8);
        }
    }

    public List<String> addAttr2TmpTable(String newTab, List<CiGroupAttrRel> ciGroupAttrRelList) throws CIServiceException {
        new ArrayList();

        try {
            List alterSqlList = this.ciLoadCustomGroupFileJdao.addAttr2TmpTable(newTab, ciGroupAttrRelList);
            return alterSqlList;
        } catch (Exception var6) {
            String message = "���嵥��׷��������ʧ��";
            this.log.error(message, var6);
            throw new CIServiceException(message, var6);
        }
    }

    public void deleteAllData(String tableName) throws CIServiceException {
        try {
            this.ciLoadCustomGroupFileJdao.deleteAllData(tableName);
        } catch (Exception var4) {
            String message = "ɾ����ʧ��";
            this.log.error(message, var4);
            throw new CIServiceException(message, var4);
        }
    }

    public void batchUpdateMobileList(List<String[]> mobileList, String tabName, String columns) throws CIServiceException {
        try {
            this.ciLoadCustomGroupFileJdao.batchUpdateMobileList(mobileList, tabName, columns);
        } catch (Exception var6) {
            String message = "��������ͻ�Ⱥ�嵥��ʧ��";
            this.log.error(message, var6);
            throw new CIServiceException(message, var6);
        }
    }

    public int batchInsert2CustListTab(String tabName, String tabNameTmp, String column) throws CIServiceException {
        boolean rowNum = false;

        try {
            int rowNum1 = this.ciLoadCustomGroupFileJdao.batchInsert2CustListTab(tabName, tabNameTmp, column);
            return rowNum1;
        } catch (Exception var7) {
            String message = "��������ͻ�Ⱥ�嵥��ʧ��";
            this.log.error(message, var7);
            throw new CIServiceException(message, var7);
        }
    }
}
