package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiSysAnnouncementHDao;
import com.ailk.biapp.ci.dao.ICiSysAnnouncementJDao;
import com.ailk.biapp.ci.dao.ICiUserNoticeSetJDao;
import com.ailk.biapp.ci.entity.CiSysAnnouncement;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.localization.nonstandard.IMailNotification;
import com.ailk.biapp.ci.localization.nonstandard.ISmsNotification;
import com.ailk.biapp.ci.service.ICiSysAnnouncementService;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiSysAnnouncementServiceImpl implements ICiSysAnnouncementService {
    private Logger log = Logger.getLogger(CiSysAnnouncementServiceImpl.class);
    @Autowired
    private ICiSysAnnouncementJDao ciSysAnnouncementJDao;
    @Autowired
    private ICiSysAnnouncementHDao ciSysAnnouncementHDao;
    @Autowired
    private ICiUserNoticeSetJDao ciUserNoticeSetJDao;

    public CiSysAnnouncementServiceImpl() {
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public int querySysAnnouncementCount(CiSysAnnouncement searchBean) throws CIServiceException {
        boolean count = false;

        try {
            int count1 = this.ciSysAnnouncementJDao.selectSysAnnouncementCount(searchBean);
            return count1;
        } catch (Exception var4) {
            this.log.error("查询总记录数失败", var4);
            throw new CIServiceException("查询总记录数失败");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiSysAnnouncement> querySysAnnouncementList(int currPage, int pageSize, CiSysAnnouncement searchBean) throws CIServiceException {
        List results = null;

        try {
            results = this.ciSysAnnouncementJDao.selectSysAnnouncementList(currPage, pageSize, searchBean);
            return results;
        } catch (Exception var6) {
            this.log.error("查询系统公告列表失败", var6);
            throw new CIServiceException("查询系统公告列表失败");
        }
    }

    public boolean addSysAnnouncement(CiSysAnnouncement sysAnnouncement) throws CIServiceException {
        boolean result = false;

        String message;
        try {
            this.ciSysAnnouncementHDao.insertCiSysAnnouncement(sysAnnouncement);
            CacheBase.getInstance().addSysAnnouncement(sysAnnouncement);
            result = true;
            String e = Configure.getInstance().getProperty("SYSANNOUNCEMENT_SMS_FLAG");
            if(StringUtil.isNotEmpty(e) && "true".equalsIgnoreCase(e)) {
                result = this.sysAnnouncementBySms(sysAnnouncement);
            }

            message = Configure.getInstance().getProperty("SYSANNOUNCEMENT_EMAIL_FLAG");
            if(StringUtil.isNotEmpty(message) && "true".equalsIgnoreCase(message)) {
                result = this.sysAnnouncementByMail(sysAnnouncement);
            }

            return result;
        } catch (Exception var5) {
            message = "保存系统公告相关信息错误";
            this.log.error(message, var5);
            throw new CIServiceException(message, var5);
        }
    }

    public boolean modifySysAnnouncement(CiSysAnnouncement sysAnnouncement) throws CIServiceException {
        boolean result = false;

        try {
            this.ciSysAnnouncementHDao.updateCiSysAnnouncement(sysAnnouncement);
            CacheBase.getInstance().clearUserCache();
            result = true;
            return result;
        } catch (Exception var5) {
            String message = "修改系统公告相关信息错误";
            this.log.error(message, var5);
            throw new CIServiceException(message, var5);
        }
    }

    public boolean deleteSysAnnouncement(CiSysAnnouncement sysAnnouncement) throws CIServiceException {
        boolean result = false;

        try {
            this.ciSysAnnouncementHDao.deleteCiSysAnnouncement(sysAnnouncement);
            CacheBase.getInstance().clearUserCache();
            result = true;
            return result;
        } catch (Exception var5) {
            String message = "删除系统公告相关信息错误";
            this.log.error(message, var5);
            throw new CIServiceException(message, var5);
        }
    }

    public boolean batchDeleteSysAnnouncementList(List<String> idList) throws CIServiceException {
        boolean result = false;

        String message;
        try {
            if(CollectionUtils.isNotEmpty(idList)) {
                Iterator e = idList.iterator();

                while(e.hasNext()) {
                    message = (String)e.next();
                    CiSysAnnouncement ciSysAnnouncement = new CiSysAnnouncement();
                    ciSysAnnouncement.setAnnouncementId(message);
                    this.ciSysAnnouncementHDao.deleteCiSysAnnouncement(ciSysAnnouncement);
                }
            }

            result = true;
            CacheBase.getInstance().clearUserCache();
            return result;
        } catch (Exception var6) {
            message = "删除系统公告相关信息错误";
            this.log.error(message, var6);
            throw new CIServiceException(message, var6);
        }
    }

    public List<CiSysAnnouncement> querySysAnnouncementListBySysAnnouncement(CiSysAnnouncement sysAnnouncement) throws CIServiceException {
        return this.ciSysAnnouncementHDao.selectCiSysAnnouncementList(sysAnnouncement);
    }

    public CiSysAnnouncement selectCiSysAnnouncementById(String id) throws CIServiceException {
        CiSysAnnouncement sysAnnouncement = null;

        try {
            sysAnnouncement = this.ciSysAnnouncementHDao.selectCiSysAnnouncementById(id);
            return sysAnnouncement;
        } catch (Exception var5) {
            String message = "根据公告id查询系统公告出错";
            this.log.error(message, var5);
            throw new CIServiceException(message, var5);
        }
    }

    public boolean sysAnnouncementBySms(CiSysAnnouncement sysAnnouncement) throws CIServiceException {
        boolean result = true;

        try {
            String e = Configure.getInstance().getProperty("SMS_NOTIFICATION_NONSTANDARD_CLASS_NAME");
            List message1 = this.getSysAnnouncementReceiverList(sysAnnouncement);
            if(StringUtil.isNotEmpty(e) && null != message1 && message1.size() > 0) {
                ISmsNotification smsNotificationImp = (ISmsNotification)SystemServiceLocator.getInstance().getService(e);
                result = smsNotificationImp.sysAnnouncementSms(sysAnnouncement, message1);
            }

            return result;
        } catch (Exception var6) {
            String message = "短信发送系统公告信息错误";
            this.log.error(message, var6);
            throw new CIServiceException(message, var6);
        }
    }

    public boolean sysAnnouncementByMail(CiSysAnnouncement sysAnnouncement) throws CIServiceException {
        boolean result = true;

        try {
            String e = Configure.getInstance().getProperty("MAIL_NOTIFICATION_NONSTANDARD_CLASS_NAME");
            List message1 = this.getSysAnnouncementReceiverList(sysAnnouncement);
            if(StringUtil.isNotEmpty(e) && null != message1 && message1.size() > 0) {
                IMailNotification mailNotificationImp = (IMailNotification)SystemServiceLocator.getInstance().getService(e);
                result = mailNotificationImp.sysAnnouncementMail(sysAnnouncement, message1);
            }

            return result;
        } catch (Exception var6) {
            String message = "邮件发送系统公告信息错误";
            this.log.error(message, var6);
            throw new CIServiceException(message, var6);
        }
    }

    public List<String> getSysAnnouncementReceiverList(CiSysAnnouncement sysAnnouncement) throws CIServiceException {
        return this.ciUserNoticeSetJDao.selectUserNoticeSetForUserId(sysAnnouncement);
    }

    public int querySysAnnouncementCountByUserId(String userId, CiSysAnnouncement searchBean) throws CIServiceException {
        boolean count = false;

        try {
            int count1 = this.ciSysAnnouncementJDao.selectSysAnnouncementCountByUserId(userId, searchBean);
            return count1;
        } catch (Exception var5) {
            this.log.error("查询用户未读公告总记录数失败", var5);
            throw new CIServiceException("查询用户未读公告总记录数失败");
        }
    }

    public List<CiSysAnnouncement> querySysAnnouncementListByUserId(String userId, CiSysAnnouncement searchBean, int currPage, int pageSize) throws CIServiceException {
        List results = null;

        try {
            results = this.ciSysAnnouncementJDao.selectSysAnnouncementListByUserId(userId, searchBean, currPage, pageSize);
            return results;
        } catch (Exception var7) {
            this.log.error("查询用户最近未读系统公告列表失败", var7);
            throw new CIServiceException("查询用户最近未读系统公告列表失败");
        }
    }

    public List<CiSysAnnouncement> queryCiUserReadInfoListByUserId(String userId, CiSysAnnouncement searchBean, int currPage, int pageSize) throws CIServiceException {
        List results = null;

        try {
            results = this.ciSysAnnouncementJDao.selectCiUserReadInfoListByUserId(userId, searchBean, currPage, pageSize);
            return results;
        } catch (Exception var7) {
            this.log.error("查询用户系统公告列表失败", var7);
            throw new CIServiceException("查询用户系统公告列表失败");
        }
    }

    public int queryCiUserReadInfoCountByUserId(String userId, CiSysAnnouncement searchBean) throws CIServiceException {
        boolean count = false;

        try {
            int count1 = this.ciSysAnnouncementJDao.selectCiUserReadInfoCountByUserId(userId, searchBean);
            return count1;
        } catch (Exception var5) {
            this.log.error("查询用户公告总记录数失败", var5);
            throw new CIServiceException("查询用户公告总记录数失败");
        }
    }
}
