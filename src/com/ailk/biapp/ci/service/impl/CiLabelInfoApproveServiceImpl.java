package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiApproveHistoryHDao;
import com.ailk.biapp.ci.dao.ICiApproveStatusHDao;
import com.ailk.biapp.ci.dao.ICiApproveUserInfoHDao;
import com.ailk.biapp.ci.dao.ICiLabelInfoJDao;
import com.ailk.biapp.ci.dao.IDimApproveLevelHDao;
import com.ailk.biapp.ci.dao.IDimApproveStatusHDao;
import com.ailk.biapp.ci.entity.CiApproveHistory;
import com.ailk.biapp.ci.entity.CiApproveStatus;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.DimApproveLevel;
import com.ailk.biapp.ci.entity.DimApproveStatus;
import com.ailk.biapp.ci.entity.DimLabelDataStatus;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ICiLabelInfoApproveService;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.asiainfo.biframe.utils.string.StringUtil;
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
public class CiLabelInfoApproveServiceImpl implements ICiLabelInfoApproveService {
    private Logger log = Logger.getLogger(CiLabelInfoApproveServiceImpl.class);
    @Autowired
    private ICiLabelInfoJDao ciLabelInfoJDao;
    @Autowired
    ICiApproveUserInfoHDao ciApproveUserInfoHDao;
    @Autowired
    private IDimApproveLevelHDao dimApproveLevelHDao;
    @Autowired
    private IDimApproveStatusHDao dimApproveStatusHDao;
    @Autowired
    private ICiApproveStatusHDao ciApproveStatusHDao;
    @Autowired
    private ICiApproveHistoryHDao ciApproveHistoryHDao;

    public CiLabelInfoApproveServiceImpl() {
    }

    public List<DimApproveStatus> queryDimApproveStatus() throws CIServiceException {
        return null;
    }

    public List<DimLabelDataStatus> queryDimLabelDataStatus() throws CIServiceException {
        return null;
    }

    public List<CiLabelInfo> queryPageList(int currPage, int pageSize, CiLabelInfo ciLabelInfo, int flag) throws CIServiceException {
        Object pageList = null;

        try {
            if(pageList != null) {
                Iterator e = ((List)pageList).iterator();

                while(e.hasNext()) {
                    CiLabelInfo label = (CiLabelInfo)e.next();
                    if(StringUtil.isNotEmpty(label.getCreateUserId())) {
                        label.setCreateUserName(PrivilegeServiceUtil.getUserById(label.getCreateUserId()).getUsername());
                    }

                    if(StringUtil.isNotEmpty(label.getLastApproveUserId())) {
                        label.setLastApproveUserName(PrivilegeServiceUtil.getUserById(label.getLastApproveUserId()).getUsername());
                    }
                }
            }

            return (List)pageList;
        } catch (Exception var8) {
            this.log.error("²éÑ¯·ÖÒ³¼ÇÂ¼Ê§°Ü", var8);
            throw new CIServiceException("²éÑ¯·ÖÒ³¼ÇÂ¼Ê§°Ü");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public int queryTotolPageCount(CiLabelInfo ciLabelInfo, int flag) throws CIServiceException {
        byte count = 0;
        return count;
    }

    public String approvePass(List<Integer> labelIds, String createUserId, String desc) throws CIServiceException {
        String approveRoleId = this.ciApproveUserInfoHDao.selectById(createUserId).getApproveRoleId();
        String nextRoleId = this.dimApproveLevelHDao.selectById(approveRoleId).getNextRoleId();
        if(!StringUtil.isNotEmpty(nextRoleId)) {
            nextRoleId = ((DimApproveLevel)this.dimApproveLevelHDao.selectListByProperty("approveRoleType", Integer.valueOf(2)).get(0)).getApproveRoleId();
        }

        String approveStatusId = this.dimApproveStatusHDao.getApproveStatuByApproveRole(nextRoleId, Integer.valueOf(1));

        try {
            Iterator e = labelIds.iterator();

            while(e.hasNext()) {
                Integer labelId = (Integer)e.next();
                new CiApproveStatus();
                CiApproveStatus ciApproveStatus = this.ciApproveStatusHDao.selectById(labelId);
                ciApproveStatus.setCurrApproveStatusId(approveStatusId);
                ciApproveStatus.setLastApproveUserId(createUserId);
                ciApproveStatus.setLastApproveRoleId(approveRoleId);
                ciApproveStatus.setLastApproveOpinion(desc);
                ciApproveStatus.setLastApproveUserId(createUserId);
                ciApproveStatus.setLastApproveTime(new Date());
                this.ciApproveStatusHDao.insertOrUpdateCiApproveStatus(ciApproveStatus);
                this.recordApproveProcess(labelId, approveRoleId, createUserId, new Date(), desc, 1);
            }

            return nextRoleId;
        } catch (Exception var10) {
            this.log.error("ÉóºËÍ¨¹ýÊ§°Ü", var10);
            throw new CIServiceException("ÉóºËÍ¨¹ýÊ§°Ü");
        }
    }

    public void approveNotPass(List<Integer> labelIds, String createUserId, String desc) throws CIServiceException {
        String approveRoleId = this.ciApproveUserInfoHDao.selectById(createUserId).getApproveRoleId();
        String approveStatusId = this.dimApproveStatusHDao.getApproveStatuByApproveRole(approveRoleId, Integer.valueOf(2));

        try {
            Iterator e = labelIds.iterator();

            while(e.hasNext()) {
                Integer labelId = (Integer)e.next();
                new CiApproveStatus();
                CiApproveStatus ciApproveStatus = this.ciApproveStatusHDao.selectById(labelId);
                ciApproveStatus.setCurrApproveStatusId(approveStatusId);
                ciApproveStatus.setLastApproveRoleId(approveRoleId);
                ciApproveStatus.setLastApproveOpinion(desc);
                ciApproveStatus.setLastApproveUserId(createUserId);
                ciApproveStatus.setLastApproveTime(new Date());
                this.ciApproveStatusHDao.insertOrUpdateCiApproveStatus(ciApproveStatus);
                this.recordApproveProcess(labelId, approveRoleId, createUserId, new Date(), desc, 0);
            }

        } catch (Exception var9) {
            this.log.error("ÉóºË²»Í¨¹ýÊ§°Ü", var9);
            throw new CIServiceException("ÉóºË²»Í¨¹ýÊ§°Ü");
        }
    }

    private void recordApproveProcess(Integer labelId, String approveRoleId, String approveUserId, Date approveTime, String approveOpinion, int approveResult) {
        CiApproveHistory ciApproveHistory = new CiApproveHistory();
        ciApproveHistory.setApproveRoleId(approveRoleId);
        ciApproveHistory.setApproveUserId(approveUserId);
        ciApproveHistory.setApproveTime(new Date());
        ciApproveHistory.setApproveOpinion(approveOpinion);
        ciApproveHistory.setApproveResult(Integer.valueOf(approveResult));
        this.ciApproveHistoryHDao.insertCiApproveHistory(ciApproveHistory);
    }
}
