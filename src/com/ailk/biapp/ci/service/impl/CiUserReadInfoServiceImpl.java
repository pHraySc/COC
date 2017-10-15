package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiUserReadInfoHDao;
import com.ailk.biapp.ci.entity.CiUserReadInfo;
import com.ailk.biapp.ci.entity.CiUserReadInfoId;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ICiUserReadInfoService;
import com.ailk.biapp.ci.util.cache.CacheBase;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiUserReadInfoServiceImpl implements ICiUserReadInfoService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ICiUserReadInfoHDao ciUserReadInfoHDao;

    public CiUserReadInfoServiceImpl() {
    }

    public void addCiUserReadInfo(CiUserReadInfo ciUserReadInfo) throws CIServiceException {
        try {
            Date e = new Date();
            ciUserReadInfo.setReadTime(e);
            this.ciUserReadInfoHDao.insertCiUserReadInfo(ciUserReadInfo);
        } catch (Exception var3) {
            this.log.error("新增用户公告记录失败", var3);
            throw new CIServiceException();
        }
    }

    public void delete(List<String> announcementIdList, String userId) throws CIServiceException {
        try {
            CiUserReadInfo e = null;
            Iterator iter = announcementIdList.iterator();

            while(iter.hasNext()) {
                String announcementId = (String)iter.next();
                CiUserReadInfoId id = new CiUserReadInfoId();
                id.setAnnouncementId(announcementId);
                id.setUserId(userId);
                e = this.ciUserReadInfoHDao.selectCiUserReadInfoById(id);
                if(null == e) {
                    e = new CiUserReadInfo();
                    e.setId(id);
                    Date date = new Date();
                    e.setReadTime(date);
                }

                e.setStatus(Integer.valueOf(0));
                this.ciUserReadInfoHDao.insertCiUserReadInfo(e);
                CacheBase.getInstance().clearUserCache();
            }

        } catch (Exception var8) {
            this.log.error("删除公告失败", var8);
            throw new CIServiceException("删除公告失败");
        }
    }

    public void deleteCiUserReadInfo(CiUserReadInfo ciUserReadInfo) throws CIServiceException {
        try {
            this.ciUserReadInfoHDao.deleteCiUserReadInfo(ciUserReadInfo);
        } catch (Exception var3) {
            this.log.error("删除用户公告失败", var3);
            throw new CIServiceException();
        }
    }
}
