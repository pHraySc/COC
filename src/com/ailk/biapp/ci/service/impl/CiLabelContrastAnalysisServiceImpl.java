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
            this.log.error("新增标签对比分析关联关系异常", var7);
            throw new CIServiceException("新增标签对比分析关联关系异常");
        }
    }

    public LabelShortInfo queryLabelBrandUserNum(String labelId, String dataDate) throws CIServiceException {
        LabelShortInfo labelInfo = null;

        try {
            labelInfo = this.ciLabelBrandUserNumJDao.getLabelBrandUserNum(labelId, dataDate);
            return labelInfo;
        } catch (Exception var5) {
            this.log.error("根据标签ID、统计月份查询标签关联用户数发生异常", var5);
            throw new CIServiceException("根据标签ID、统计月份查询标签关联用户数发生异常");
        }
    }

    public List<LabelShortInfo> queryCiUserLabelContrastList(String userId, String labelId, String dataDate) throws CIServiceException {
        List results = null;

        try {
            results = this.ciUserLabelContrastJDao.getCiUserLabelContrastList(userId, labelId, dataDate);
            return results;
        } catch (Exception var6) {
            this.log.error("根据用户ID、主标签ID 查询用户定义的标签对比关系发生异常", var6);
            throw new CIServiceException("根据用户ID、主标签ID 查询用户定义的标签对比关系发生异常");
        }
    }

    public List<LabelShortInfo> queryCiUserLabelContrastListNoData(String userId, String labelId) throws CIServiceException {
        List results = null;

        try {
            results = this.ciUserLabelContrastJDao.getCiUserLabelContrastList(userId, labelId);
            return results;
        } catch (Exception var5) {
            this.log.error("根据用户ID、主标签ID 查询用户定义的标签对比关系发生异常", var5);
            throw new CIServiceException("根据用户ID、主标签ID 查询用户定义的标签对比关系发生异常");
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
            this.log.error("删除标签对比关系发生异常", var5);
            throw new CIServiceException("删除标签对比关系发生异常");
        }
    }

    public int queryCountUserLabelContrast(String userId, String labelId) throws CIServiceException {
        byte count = 0;

        try {
            this.ciUserLabelContrastJDao.getCountUserLabelContrast(userId, labelId);
            return count;
        } catch (Exception var5) {
            this.log.error("根据用户ID、主标签ID 统计用户定义的标签对比关系数目发生异常", var5);
            throw new CIServiceException("根据用户ID、主标签ID 统计用户定义的标签对比关系数目发生异常");
        }
    }

    public void deleteCiUserLabelContrast(String userId, String labelId) throws CIServiceException {
        try {
            this.ciUserLabelContrastHDao.deleteCiUserLabelContrast(userId, labelId);
        } catch (Exception var4) {
            this.log.error("删除标签对比关系发生异常", var4);
            throw new CIServiceException("删除标签对比关系发生异常");
        }
    }

    public String queryLabelBrandUserNumById(String labelId, String dataDate) throws CIServiceException {
        String count = "-";

        try {
            count = this.ciLabelBrandUserNumJDao.getLabelBrandUserNumById(labelId, dataDate);
            return count;
        } catch (Exception var5) {
            this.log.error("根据标签ID、统计月份查询标签关联用户数发生异常", var5);
            throw new CIServiceException("根据标签ID、统计月份查询标签关联用户数发生异常");
        }
    }
}
