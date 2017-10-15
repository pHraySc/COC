package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiUserUseLabelHDao;
import com.ailk.biapp.ci.dao.ICiUserUseLabelJDao;
import com.ailk.biapp.ci.entity.CiUserUseLabel;
import com.ailk.biapp.ci.entity.CiUserUseLabelId;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.LabelUseLogInfo;
import com.ailk.biapp.ci.service.ICiLabelUserUseService;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.asiainfo.biframe.privilege.IUser;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("ciLabelUserUseService")
@Transactional
public class CiLabelUserUseServiceImpl implements ICiLabelUserUseService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ICiUserUseLabelHDao ciUserUseLabeHJDao;
    @Autowired
    private ICiUserUseLabelJDao ciUserUseLabelJDao;

    public CiLabelUserUseServiceImpl() {
    }

    public ICiUserUseLabelJDao getCiUserUseLabelJDao() {
        return this.ciUserUseLabelJDao;
    }

    public void setCiUserUseLabelJDao(ICiUserUseLabelJDao ciUserUseLabelJDao) {
        this.ciUserUseLabelJDao = ciUserUseLabelJDao;
    }

    public ICiUserUseLabelHDao getCiUserUseLabeHJDao() {
        return this.ciUserUseLabeHJDao;
    }

    public void setCiUserUseLabeHJDao(ICiUserUseLabelHDao ciUserUseLabeHJDao) {
        this.ciUserUseLabeHJDao = ciUserUseLabeHJDao;
    }

    public void addLabelUserUseLog(String labelId, int typeId) {
        try {
            String e = PrivilegeServiceUtil.getUserId();
            String deptId = PrivilegeServiceUtil.getUserDeptId(e).toString();
            CiUserUseLabel entity = new CiUserUseLabel();
            CiUserUseLabelId id = new CiUserUseLabelId();
            id.setUserId(e);
            id.setDeptId(deptId);
            id.setLabelId(new Integer(labelId));
            id.setUseTime(new Date());
            entity.setId(id);
            entity.setLabelUseTypeId(Integer.valueOf(typeId));
            this.ciUserUseLabeHJDao.insertOrUpdateCiUserUseLabel(entity);
        } catch (Exception var7) {
            this.log.error("�����ǩʹ����־�쳣", var7);
            throw new CIServiceException("�����ǩʹ����־�쳣");
        }
    }

    public List<LabelUseLogInfo> queryLabelUseLogInfos(int currPage, int pageSize, String labelId) {
        List results = null;

        try {
            results = this.ciUserUseLabelJDao.getLabelUseLogInfos(currPage, pageSize, labelId);
            Iterator e = results.iterator();

            while(e.hasNext()) {
                LabelUseLogInfo labelUseLogInfo = (LabelUseLogInfo)e.next();

                try {
                    labelUseLogInfo.setUserName(PrivilegeServiceUtil.getUserById(labelUseLogInfo.getUserId()).getUsername());
                } catch (Exception var9) {
                    this.log.warn("��ȡʹ������Ϣ����UserId=" + labelUseLogInfo.getUserId());
                }

                try {
                    labelUseLogInfo.setDeptName(PrivilegeServiceUtil.getUserDept(labelUseLogInfo.getUserId()));
                } catch (Exception var8) {
                    this.log.warn("��ȡʹ���߲�����Ϣ����UserId=" + labelUseLogInfo.getUserId());
                }
            }

            return results;
        } catch (Exception var10) {
            this.log.error("��ǩʹ����־��ѯ�쳣", var10);
            throw new CIServiceException("��ǩʹ����־��ѯ�쳣");
        }
    }

    public String queryLastLabelUseLogDesc(String labelId) {
        String logInfo = null;

        try {
            LabelUseLogInfo e = this.ciUserUseLabelJDao.getLastLabelUseLog(labelId);
            if(null != e) {
                String userId = e.getUserId();
                String userName = "";

                try {
                    IUser useTime = PrivilegeServiceUtil.getUserById(userId);
                    userName = useTime.getUsername();
                } catch (Exception var10) {
                    this.log.warn("��ȡʹ������Ϣ����UserId=" + userId);
                }

                Date useTime1 = e.getUseTime();
                Date now = new Date();
                long minutes = (now.getTime() - useTime1.getTime()) / 60000L;
                new String();
                logInfo = userName + "," + minutes + "����ǰʹ�ù�";
            }

            return logInfo;
        } catch (Exception var11) {
            this.log.error("��ǩ����ʹ����־��ѯ�쳣", var11);
            throw new CIServiceException("��ǩ����ʹ����־��ѯ�쳣");
        }
    }

    public void insertCiLabelDayUseStat(String dataDate) {
        try {
            this.ciUserUseLabelJDao.deleteCiLabelUseStat(dataDate);
            this.ciUserUseLabelJDao.insertCiLabelDayUseStat(dataDate);
        } catch (Exception var3) {
            this.log.error("ͳ���û��ձ�ǩʹ�ô����쳣", var3);
        }

    }

    public void insertCiLabelMonthUseStat(String dataDate) {
        try {
            this.ciUserUseLabelJDao.deleteCiLabelUseStat(dataDate);
            this.ciUserUseLabelJDao.insertCiLabelMonthUseStat(dataDate);
        } catch (Exception var3) {
            this.log.error("ͳ���û��±�ǩʹ�ô����쳣", var3);
        }

    }

    public long getLabelUseTimesByID(String labelId) {
        return this.ciUserUseLabelJDao.getLabelUseTimesByID(labelId);
    }
}
