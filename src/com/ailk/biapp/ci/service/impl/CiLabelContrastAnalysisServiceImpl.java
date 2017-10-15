package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiLabelBrandUserNumJDao;
import com.ailk.biapp.ci.dao.ICiUserLabelContrastHDao;
import com.ailk.biapp.ci.dao.ICiUserLabelContrastJDao;
import com.ailk.biapp.ci.entity.CiUserLabelContrast;
import com.ailk.biapp.ci.entity.CiUserLabelContrastId;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.LabelShortInfo;
import com.ailk.biapp.ci.service.ICiLabelContrastAnalysisService;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("ciLabelContrastAnalysisService")
@Transactional
public class CiLabelContrastAnalysisServiceImpl implements ICiLabelContrastAnalysisService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ICiUserLabelContrastHDao ciUserLabelContrastHDao;
    @Autowired
    private ICiUserLabelContrastJDao ciUserLabelContrastJDao;
    @Autowired
    private ICiLabelBrandUserNumJDao ciLabelBrandUserNumJDao;

    public CiLabelContrastAnalysisServiceImpl() {
    }

    public void setCiUserLabelContrastHDao(ICiUserLabelContrastHDao ciUserLabelContrastHDao) {
        this.ciUserLabelContrastHDao = ciUserLabelContrastHDao;
    }

    public void setCiUserLabelContrastJDao(ICiUserLabelContrastJDao ciUserLabelContrastJDao) {
        this.ciUserLabelContrastJDao = ciUserLabelContrastJDao;
    }

    public void addCiUserLabelContrast(String userId, String labelId, String contrastLabelId) throws CIServiceException {
        CiUserLabelContrast entity = new CiUserLabelContrast();
        CiUserLabelContrastId id = new CiUserLabelContrastId();

        try {
            id.setContrastLabelId(contrastLabelId);
            id.setLabelId(labelId);
            id.setUserId(userId);
            entity.setId(id);
            entity.setStatus(Integer.valueOf(1));
            this.ciUserLabelContrastHDao.insertCiUserLabelContrast(entity);
        } catch (Exception var7) {
            this.log.error("������ǩ�Աȷ���������ϵ�쳣", var7);
            throw new CIServiceException("������ǩ�Աȷ���������ϵ�쳣");
        }
    }

    public LabelShortInfo queryLabelBrandUserNum(String labelId, String dataDate) throws CIServiceException {
        LabelShortInfo labelInfo = null;

        try {
            labelInfo = this.ciLabelBrandUserNumJDao.getLabelBrandUserNum(labelId, dataDate);
            return labelInfo;
        } catch (Exception var5) {
            this.log.error("���ݱ�ǩID��ͳ���·ݲ�ѯ��ǩ�����û��������쳣", var5);
            throw new CIServiceException("���ݱ�ǩID��ͳ���·ݲ�ѯ��ǩ�����û��������쳣");
        }
    }

    public List<LabelShortInfo> queryCiUserLabelContrastList(String userId, String labelId, String dataDate) throws CIServiceException {
        List results = null;

        try {
            results = this.ciUserLabelContrastJDao.getCiUserLabelContrastList(userId, labelId, dataDate);
            return results;
        } catch (Exception var6) {
            this.log.error("�����û�ID������ǩID ��ѯ�û�����ı�ǩ�Աȹ�ϵ�����쳣", var6);
            throw new CIServiceException("�����û�ID������ǩID ��ѯ�û�����ı�ǩ�Աȹ�ϵ�����쳣");
        }
    }

    public List<LabelShortInfo> queryCiUserLabelContrastListNoData(String userId, String labelId) throws CIServiceException {
        List results = null;

        try {
            results = this.ciUserLabelContrastJDao.getCiUserLabelContrastList(userId, labelId);
            return results;
        } catch (Exception var5) {
            this.log.error("�����û�ID������ǩID ��ѯ�û�����ı�ǩ�Աȹ�ϵ�����쳣", var5);
            throw new CIServiceException("�����û�ID������ǩID ��ѯ�û�����ı�ǩ�Աȹ�ϵ�����쳣");
        }
    }

    public void deleteUserLabelContrast(String userId, String labelId, String contrastLabelId) throws CIServiceException {
        try {
            CiUserLabelContrastId e = new CiUserLabelContrastId();
            e.setUserId(userId);
            e.setLabelId(labelId);
            e.setContrastLabelId(contrastLabelId);
            this.ciUserLabelContrastHDao.deleteCiUserLabelContrast(e);
        } catch (Exception var5) {
            this.log.error("ɾ����ǩ�Աȹ�ϵ�����쳣", var5);
            throw new CIServiceException("ɾ����ǩ�Աȹ�ϵ�����쳣");
        }
    }

    public int queryCountUserLabelContrast(String userId, String labelId) throws CIServiceException {
        byte count = 0;

        try {
            this.ciUserLabelContrastJDao.getCountUserLabelContrast(userId, labelId);
            return count;
        } catch (Exception var5) {
            this.log.error("�����û�ID������ǩID ͳ���û�����ı�ǩ�Աȹ�ϵ��Ŀ�����쳣", var5);
            throw new CIServiceException("�����û�ID������ǩID ͳ���û�����ı�ǩ�Աȹ�ϵ��Ŀ�����쳣");
        }
    }

    public void deleteCiUserLabelContrast(String userId, String labelId) throws CIServiceException {
        try {
            this.ciUserLabelContrastHDao.deleteCiUserLabelContrast(userId, labelId);
        } catch (Exception var4) {
            this.log.error("ɾ����ǩ�Աȹ�ϵ�����쳣", var4);
            throw new CIServiceException("ɾ����ǩ�Աȹ�ϵ�����쳣");
        }
    }

    public String queryLabelBrandUserNumById(String labelId, String dataDate) throws CIServiceException {
        String count = "-";

        try {
            count = this.ciLabelBrandUserNumJDao.getLabelBrandUserNumById(labelId, dataDate);
            return count;
        } catch (Exception var5) {
            this.log.error("���ݱ�ǩID��ͳ���·ݲ�ѯ��ǩ�����û��������쳣", var5);
            throw new CIServiceException("���ݱ�ǩID��ͳ���·ݲ�ѯ��ǩ�����û��������쳣");
        }
    }
}
