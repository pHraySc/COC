package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiUserUseCustomHDao;
import com.ailk.biapp.ci.dao.ICiUserUseCustomJDao;
import com.ailk.biapp.ci.entity.CiUserUseCustom;
import com.ailk.biapp.ci.entity.CiUserUseCustomId;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ICiCustomUserUseService;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("ciCustomUserUseService")
@Transactional
public class CiCustomUserUseServiceImpl implements ICiCustomUserUseService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ICiUserUseCustomJDao ciUserUseCustomJDao;
    @Autowired
    private ICiUserUseCustomHDao ciUserUseCustomHDao;

    public CiCustomUserUseServiceImpl() {
    }

    public void addCustomUserUseLog(String customId, int useTypeId, int isTemplate) {
        try {
            CiUserUseCustomId e = new CiUserUseCustomId();
            e.setCustomId(customId);
            String userId = PrivilegeServiceUtil.getUserId();
            String deptId = PrivilegeServiceUtil.getUserDeptId(userId).toString();
            e.setUserId(userId);
            e.setDeptId(deptId);
            e.setUseTime(new Date());
            CiUserUseCustom cuuc = new CiUserUseCustom();
            cuuc.setId(e);
            cuuc.setIsTemplate(Integer.valueOf(isTemplate));
            cuuc.setUseTypeId(Integer.valueOf(useTypeId));
            this.ciUserUseCustomHDao.insertOrUpdateCiUserUseCustom(cuuc);
        } catch (Exception var8) {
            this.log.error("保存客户群（模板）使用日志异常", var8);
            throw new CIServiceException("保存客户群（模板）使用日志异常");
        }
    }

    public void insertCiCustomMonthUseStat(String dataDate) {
        try {
            this.ciUserUseCustomJDao.deleteCiCustomUseStat(dataDate);
            this.ciUserUseCustomJDao.insertCiCustomUseStat(dataDate);
        } catch (Exception var3) {
            this.log.error("按月统计客户使用次数出错！", var3);
        }

    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public long getCustomUseTimesByID(String customId, int isTemplate) {
        return this.ciUserUseCustomJDao.getCustomUseTimesById(customId, isTemplate).longValue();
    }
}
