package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiGroupAttrRelHDao;
import com.ailk.biapp.ci.dao.ICiGroupAttrRelJDao;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ICiGroupAttrRelService;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiGroupAttrRelServiceImpl implements ICiGroupAttrRelService {
    private Logger log = Logger.getLogger(CiGroupAttrRelServiceImpl.class);
    @Autowired
    private ICiGroupAttrRelHDao ciGroupAttrRelHDao;
    @Autowired
    private ICiGroupAttrRelJDao ciGroupAttrRelJDao;

    public CiGroupAttrRelServiceImpl() {
    }

    public void saveCiGroupAttrRel(CiGroupAttrRel ciGroupAttrRel) throws CIServiceException {
        try {
            this.ciGroupAttrRelHDao.insertCiGroupAttrRelHDao(ciGroupAttrRel);
        } catch (Exception var3) {
            this.log.error(var3);
            throw new CIServiceException("保存客户群属性列失败");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiGroupAttrRel> queryGroupAttrRelList(String groupInfoId, Date listCreateTime) throws CIServiceException {
        new ArrayList();

        try {
            List attrRelList = this.ciGroupAttrRelJDao.selectCiGroupAttrRelList(groupInfoId, listCreateTime);
            Iterator e = attrRelList.iterator();

            while(e.hasNext()) {
                CiGroupAttrRel attrRel = (CiGroupAttrRel)e.next();
                if(attrRel.getAttrColName() != null && attrRel.getAttrColName().contains("\"")) {
                    attrRel.setAttrColName(attrRel.getAttrColName().replace("\"", "\\\""));
                }
            }

            return attrRelList;
        } catch (Exception var6) {
            this.log.error("", var6);
            throw new CIServiceException("查询客户群属性列集合失败");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiGroupAttrRel> queryNewestGroupAttrRelList(String groupInfoId) throws CIServiceException {
        List attrRelList = null;

        try {
            attrRelList = this.ciGroupAttrRelJDao.selectNewestCiGroupAttrRelList(groupInfoId);
            Iterator e = attrRelList.iterator();

            while(e.hasNext()) {
                CiGroupAttrRel attrRel = (CiGroupAttrRel)e.next();
                if(attrRel.getAttrColName().contains("\"")) {
                    attrRel.setAttrColName(attrRel.getAttrColName().replace("\"", "\\\""));
                }
            }

            return attrRelList;
        } catch (Exception var5) {
            this.log.error(var5);
            throw new CIServiceException("查询最新的客户群属性列集合失败");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiGroupAttrRel> queryNewestGroupAttrRelListBySort(String customGroupId) {
        List attrRelList = null;

        try {
            attrRelList = this.ciGroupAttrRelJDao.selectNewestCiGroupAttrRelBySort(customGroupId);
            Iterator e = attrRelList.iterator();

            while(e.hasNext()) {
                CiGroupAttrRel attrRel = (CiGroupAttrRel)e.next();
                if(attrRel.getAttrColName().contains("\"")) {
                    attrRel.setAttrColName(attrRel.getAttrColName().replace("\"", "\\\""));
                }
            }

            return attrRelList;
        } catch (Exception var5) {
            this.log.error(var5);
            throw new CIServiceException("查询最新的客户群属性列集合失败");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiGroupAttrRel> queryGroupAttrRelListBySourceId(String groupInfoId, String customSourceId) throws CIServiceException {
        List attrRelList = null;

        try {
            attrRelList = this.ciGroupAttrRelHDao.selectCiGroupAttrRelListBySourceId(groupInfoId, customSourceId);
            return attrRelList;
        } catch (Exception var5) {
            this.log.error(var5);
            throw new CIServiceException("根据客户群id和来源id查询客户群属性列集合失败");
        }
    }

    public void deleteGroupAttrRelListBySourceId(String groupInfoId) throws CIServiceException {
        try {
            this.ciGroupAttrRelHDao.deleteCiGroupAttrRelListBySourceId(groupInfoId);
        } catch (Exception var3) {
            this.log.error(var3);
            throw new CIServiceException("根据客户群id和来源id删除客户群属性列集合失败");
        }
    }

    public void modifyAllGroupAttrRel(List<CiGroupAttrRel> groupAttrRelList, String groupInfoId) throws CIServiceException {
        try {
            this.ciGroupAttrRelHDao.deleteCiGroupAttrRelListBySourceId(groupInfoId);
            Iterator e = groupAttrRelList.iterator();

            while(e.hasNext()) {
                CiGroupAttrRel attr = (CiGroupAttrRel)e.next();
                this.ciGroupAttrRelHDao.insertCiGroupAttrRelHDao(attr);
            }

        } catch (Exception var5) {
            this.log.error(var5);
            throw new CIServiceException("更新客户群属性列失败");
        }
    }

    public void updateCiGroupAttrRelListModifyTime(String groupInfoId, Date listCreateTime) throws Exception {
        try {
            this.ciGroupAttrRelJDao.updateCiGroupAttrRelListModifyTime(groupInfoId, listCreateTime);
        } catch (Exception var4) {
            this.log.error(var4);
            throw new CIServiceException("更新属性ModifyTime失败");
        }
    }

    public void deleteGroupAttrRelList(String customGroupId, Date attrModifyTime) {
        try {
            this.ciGroupAttrRelJDao.deleteGroupAttrRelList(customGroupId, attrModifyTime);
        } catch (Exception var4) {
            this.log.error(var4);
            throw new CIServiceException("删除冗余属性失败！");
        }
    }

    public void updateCiGroupAttrRelStatusByGroupInfoId(String groupInfoId) throws Exception {
        try {
            this.ciGroupAttrRelJDao.updateCiGroupAttrRelStatusByGroupInfoId(groupInfoId);
        } catch (Exception var3) {
            this.log.error(var3);
            throw new CIServiceException("更新属性Status为1失败");
        }
    }
}
