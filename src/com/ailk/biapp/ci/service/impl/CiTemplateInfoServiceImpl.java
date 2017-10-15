package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiLabelRuleHDao;
import com.ailk.biapp.ci.dao.ICiTemplateInfoHDao;
import com.ailk.biapp.ci.dao.ICiTemplateInfoJDao;
import com.ailk.biapp.ci.dao.ICiTemplateModifyInfoHDao;
import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiTemplateInfo;
import com.ailk.biapp.ci.entity.CiTemplateModifyInfo;
import com.ailk.biapp.ci.entity.DimScene;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiTemplateInfoService;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiTemplateInfoServiceImpl implements ICiTemplateInfoService {
    private Logger log = Logger.getLogger(CiTemplateInfoServiceImpl.class);
    @Autowired
    private ICiTemplateInfoJDao ciTemplateInfoJDao;
    @Autowired
    private ICiTemplateInfoHDao ciTemplateInfoHDao;
    @Autowired
    private ICiTemplateModifyInfoHDao ciTemplateModifyInfoHDao;
    @Autowired
    private ICiLabelRuleHDao ciLabelRuleHDao;

    public CiTemplateInfoServiceImpl() {
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public int queryTemplateInfoCount(CiTemplateInfo searchBean) throws CIServiceException {
        boolean count = false;

        try {
            int count1 = this.ciTemplateInfoJDao.selectTemplateInfoCount(searchBean);
            return count1;
        } catch (Exception var5) {
            String message = "查询模板数量错误";
            this.log.error(message, var5);
            throw new CIServiceException(message, var5);
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiTemplateInfo> queryTemplateInfoList(Pager pager, CiTemplateInfo searchBean) throws CIServiceException {
        List list = null;

        try {
            list = this.ciTemplateInfoJDao.selectTemplateInfoList(pager, searchBean);
            Iterator e = list.iterator();

            while(e.hasNext()) {
                CiTemplateInfo message1 = (CiTemplateInfo)e.next();
                IUser iuser = PrivilegeServiceUtil.getUserById(message1.getUserId());
                if(iuser != null) {
                    message1.setUserName(iuser.getUsername());
                }
            }

            return list;
        } catch (Exception var7) {
            String message = "查询模板列表错误";
            this.log.error(message, var7);
            throw new CIServiceException(message, var7);
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public CiTemplateInfo queryTemplateInfoById(String templateId) throws CIServiceException {
        CiTemplateInfo ciTemplateInfo = null;

        try {
            ciTemplateInfo = this.ciTemplateInfoHDao.selectTemplateInfoById(templateId);
            return ciTemplateInfo;
        } catch (Exception var5) {
            String message = "根据模板Id查询模板错误";
            this.log.error(message, var5);
            throw new CIServiceException(message, var5);
        }
    }

    public void addTemplateInfo(CiTemplateInfo ciTemplateInfo, List<CiLabelRule> ciLabelRuleList, String userId) throws CIServiceException {
        try {
            Date e = new Date();
            ciTemplateInfo.setUserId(userId);
            ciTemplateInfo.setAddTime(e);
            ciTemplateInfo.setNewModifyTime(e);
            ciTemplateInfo.setTemplateId((String)null);
            ciTemplateInfo.setStatus(Integer.valueOf(1));
            CiTemplateModifyInfo message1 = this.addCiTemplateModifyInfo(ciTemplateInfo, userId);
            message1.setModifyTime(e);
            this.addOrModifyTemplateInfo(ciTemplateInfo, message1, ciLabelRuleList);
        } catch (Exception var6) {
            String message = "创建模板错误";
            this.log.error(message, var6);
            throw new CIServiceException(message, var6);
        }
    }

    public void modifyTemplateInfo(CiTemplateInfo ciTemplateInfo, List<CiLabelRule> ciLabelRuleList, String userId) throws CIServiceException {
        try {
            Date e = new Date();
            ciTemplateInfo.setNewModifyTime(e);
            CiTemplateModifyInfo message1 = this.addCiTemplateModifyInfo(ciTemplateInfo, userId);
            message1.setModifyTime(new Date());
            this.addOrModifyTemplateInfo(ciTemplateInfo, message1, ciLabelRuleList);
        } catch (Exception var6) {
            String message = "修改模板错误";
            this.log.error(message, var6);
            throw new CIServiceException(message, var6);
        }
    }

    private CiTemplateModifyInfo addCiTemplateModifyInfo(CiTemplateInfo ciTemplateInfo, String userId) throws CIServiceException {
        CiTemplateModifyInfo history = new CiTemplateModifyInfo();
        history.setTemplateId(ciTemplateInfo.getTemplateId());
        history.setTemplateName(ciTemplateInfo.getTemplateName());
        history.setTemplateDesc(ciTemplateInfo.getTemplateDesc());
        history.setModifyUserId(userId);
        history.setLabelOptRuleShow(ciTemplateInfo.getLabelOptRuleShow());
        history.setSceneId(ciTemplateInfo.getSceneId());
        history.setIsPrivate(ciTemplateInfo.getIsPrivate());
        return history;
    }

    private void addOrModifyTemplateInfo(CiTemplateInfo ciTemplateInfo, CiTemplateModifyInfo history, List<CiLabelRule> ciLabelRuleList) throws CIServiceException {
        this.ciTemplateInfoHDao.insertTemplateInfo(ciTemplateInfo);
        String templateId = ciTemplateInfo.getTemplateId();
        if(history != null && StringUtil.isNotEmpty(history.getTemplateName())) {
            history.setTemplateId(templateId);
            this.ciTemplateModifyInfoHDao.insertCiTemplateModifyInfo(history);
        }

        if(ciLabelRuleList != null && ciLabelRuleList.size() != 0) {
            Iterator i$ = ciLabelRuleList.iterator();

            while(i$.hasNext()) {
                CiLabelRule rule = (CiLabelRule)i$.next();
                rule.setRuleId((String)null);
                rule.setCustomId(templateId);
                rule.setCustomType(Integer.valueOf(2));
                this.ciLabelRuleHDao.insertCiLabelRule(rule);
            }
        }

    }

    public boolean deleteTemplateInfo(CiTemplateInfo ciTemplateInfo) throws CIServiceException {
        boolean success = false;

        try {
            ciTemplateInfo.setStatus(Integer.valueOf(0));
            this.ciTemplateInfoHDao.insertTemplateInfo(ciTemplateInfo);
            success = true;
            return success;
        } catch (Exception var5) {
            String message = "根据模板Id删除模板错误";
            this.log.error(message, var5);
            throw new CIServiceException(message, var5);
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiTemplateInfo> queryTemplateInfoListByName(String templateName, String userId) throws CIServiceException {
        List list = null;

        try {
            list = this.ciTemplateInfoHDao.selectTemplateInfoListByName(templateName, userId);
            return list;
        } catch (Exception var6) {
            String message = "根据模板名称及用户Id查询模板错误";
            this.log.error(message, var6);
            throw new CIServiceException(message, var6);
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public Map<String, Object> isNameExist(CiTemplateInfo ciTemplateInfo, String userId) throws CIServiceException {
        boolean success = false;
        HashMap returnMap = new HashMap();
        String msg = "";

        try {
            String e = ciTemplateInfo.getTemplateName();
            if(StringUtil.isEmpty(userId)) {
                msg = "没有找到用户！";
            } else if(StringUtil.isEmpty(e)) {
                msg = "模板名称不能为空！";
            } else {
                success = true;
            }

            if(success) {
                if(StringUtil.isNotEmpty(ciTemplateInfo.getTemplateId()) && !ciTemplateInfo.getUserId().equals(userId)) {
                    userId = ciTemplateInfo.getUserId();
                }

                List list = this.queryTemplateInfoListByName(e, userId);
                if(list != null && list.size() > 0) {
                    CiTemplateInfo templateInfo = (CiTemplateInfo)list.get(0);
                    if(!templateInfo.getTemplateId().equals(ciTemplateInfo.getTemplateId())) {
                        msg = "输入的模板名称重名，请重新输入！";
                        success = false;
                    }
                }
            }

            returnMap.put("success", Boolean.valueOf(success));
            returnMap.put("msg", msg);
        } catch (Exception var9) {
            msg = "重名验证错误！";
            this.log.error(msg, var9);
            success = false;
            returnMap.put("success", Boolean.valueOf(success));
            returnMap.put("msg", msg);
        }

        return returnMap;
    }

    public List<CiTemplateInfo> queryTemplateInfoListByTemplateName(String templateName) throws CIServiceException {
        List list = null;

        try {
            list = this.ciTemplateInfoHDao.selectTemplateInfoListByTemplateName(templateName);
            return list;
        } catch (Exception var5) {
            String message = "根据模板名称查询模板错误";
            this.log.error(message, var5);
            throw new CIServiceException(message, var5);
        }
    }

    public void modifyTemplateInfoPublic(CiTemplateInfo ciTemplateInfo, String userId) throws CIServiceException {
        try {
            CiTemplateModifyInfo e = this.addCiTemplateModifyInfo(ciTemplateInfo, userId);
            e.setModifyTime(new Date());
            this.addOrModifyTemplateInfoPub(ciTemplateInfo, e);
        } catch (Exception var5) {
            String message = "共享模板错误";
            this.log.error(message, var5);
            throw new CIServiceException(message, var5);
        }
    }

    private void addOrModifyTemplateInfoPub(CiTemplateInfo ciTemplateInfo, CiTemplateModifyInfo history) throws CIServiceException {
        this.ciTemplateInfoHDao.insertTemplateInfo(ciTemplateInfo);
        String templateId = ciTemplateInfo.getTemplateId();
        if(history != null && StringUtil.isNotEmpty(history.getTemplateName())) {
            history.setTemplateId(templateId);
            this.ciTemplateModifyInfoHDao.insertCiTemplateModifyInfo(history);
        }

    }

    public DimScene querySceneInfoById(String scenceId) {
        DimScene result = this.ciTemplateInfoJDao.selectSceneInfoById(scenceId);
        return result;
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiTemplateInfo> indexQueryTemplateName(String name, String userId) throws CIServiceException {
        List templateNameList = null;

        try {
            templateNameList = this.ciTemplateInfoJDao.indexQueryTemplateName(name, userId);
            return templateNameList;
        } catch (Exception var5) {
            this.log.error("首页关联查询模板名称报错", var5);
            throw new CIServiceException("首页关联查询模板名称报错");
        }
    }
}
