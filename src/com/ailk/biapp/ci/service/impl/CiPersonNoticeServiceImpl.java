package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiPersonNoticeHDao;
import com.ailk.biapp.ci.dao.ICiPersonNoticeJDao;
import com.ailk.biapp.ci.dao.ICiUserNoticeSetHDao;
import com.ailk.biapp.ci.entity.CiPersonNotice;
import com.ailk.biapp.ci.entity.CiUserNoticeSet;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.localization.nonstandard.IMailNotification;
import com.ailk.biapp.ci.localization.nonstandard.ISmsNotification;
import com.ailk.biapp.ci.service.ICiPersonNoticeService;
import com.ailk.biapp.ci.service.ICiUserNoticeSetService;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiPersonNoticeServiceImpl implements ICiPersonNoticeService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ICiPersonNoticeHDao personNoticeHDao;
    @Autowired
    private ICiPersonNoticeJDao personNoticeJDao;
    @Autowired
    private ICiUserNoticeSetHDao ciUserNoticeSetHDao;
    @Autowired
    private ICiUserNoticeSetService ciUserNoticeSetService;

    public CiPersonNoticeServiceImpl() {
    }

    public void addPersonNotice(CiPersonNotice personNotice) throws CIServiceException {
        String message;
        try {
            personNotice.setIsShowTip(Integer.valueOf(1));
            this.personNoticeHDao.save(personNotice);
            String e = personNotice.getReceiveUserId();
            message = personNotice.getNoticeTypeId().toString();
            byte noticeTypeId = 2;
            List list = this.ciUserNoticeSetService.queryUserNoticeSetInfo(e, message, noticeTypeId);
            Iterator i$ = list.iterator();

            while(true) {
                while(true) {
                    CiUserNoticeSet ciUserNoticeSet;
                    int isSuccess;
                    int isSuccessTwo;
                    do {
                        do {
                            do {
                                if(!i$.hasNext()) {
                                    return;
                                }

                                ciUserNoticeSet = (CiUserNoticeSet)i$.next();
                            } while(!StringUtil.isNotEmpty(personNotice.getIsSuccess()));
                        } while(!StringUtil.isNotEmpty(ciUserNoticeSet.getId().getIsSuccess()));

                        isSuccess = personNotice.getIsSuccess().intValue() == 1?1:2;
                        isSuccessTwo = ciUserNoticeSet.getId().getIsSuccess().intValue();
                    } while(isSuccess != isSuccessTwo);

                    if(StringUtil.isEmpty(ciUserNoticeSet.getId().getSendModeId())) {
                        CacheBase.getInstance().addPersonNotice(personNotice);
                    } else {
                        try {
                            String e1 = ciUserNoticeSet.getId().getSendModeId();
                            if(e1.equals("2") || e1 == "2") {
                                this.personNoticeBySms(personNotice);
                            }

                            if(e1.equals("3") || e1 == "3") {
                                this.personNoticeByMail(personNotice);
                            }

                            if(e1.equals("1") || e1 == "1") {
                                CacheBase.getInstance().addPersonNotice(personNotice);
                            }
                        } catch (Exception var11) {
                            this.log.error("邮件或者短信配置错误", var11);
                        }
                    }
                }
            }
        } catch (Exception var12) {
            message = "保存个人通知错误";
            this.log.error(message, var12);
            throw new CIServiceException(message, var12);
        }
    }

    public void readPersonNotice(String noticeId) throws CIServiceException {
        if(noticeId.contains(",")) {
            String[] personNotice = noticeId.trim().split(",");
            if(personNotice != null && personNotice.length > 0) {
                for(int i = 0; i < personNotice.length; ++i) {
                    CiPersonNotice personNotice1 = this.personNoticeHDao.selectById(personNotice[i]);
                    personNotice1.setReadStatus(Integer.valueOf(2));
                    personNotice1.setIsShowTip(Integer.valueOf(0));
                    this.personNoticeHDao.update(personNotice1);
                }
            }
        } else {
            CiPersonNotice var5 = this.personNoticeHDao.selectById(noticeId);
            var5.setReadStatus(Integer.valueOf(2));
            var5.setIsShowTip(Integer.valueOf(0));
            this.personNoticeHDao.update(var5);
        }

    }

    public void delete(List<String> noticeIds) throws CIServiceException {
        try {
            CiPersonNotice e = null;
            Iterator iter = noticeIds.iterator();

            while(iter.hasNext()) {
                String noticeId = (String)iter.next();
                e = this.personNoticeHDao.selectById(noticeId);
                e.setStatus(Integer.valueOf(2));
                this.personNoticeHDao.update(e);
                CacheBase.getInstance().clearUserCache();
            }

        } catch (Exception var5) {
            this.log.error("删除标签失败", var5);
            throw new CIServiceException("删除标签失败");
        }
    }

    public int getCountOfPersonNotice(CiPersonNotice personNotice) throws CIServiceException {
        boolean count = false;

        try {
            int count1 = this.personNoticeJDao.getCountOfPersonNotice(personNotice);
            return count1;
        } catch (Exception var5) {
            String message = "查询个人通知条数错误";
            this.log.error(message, var5);
            throw new CIServiceException(message, var5);
        }
    }

    public List<CiPersonNotice> queryPagePersonNoticeList(int currPage, int pageSize, CiPersonNotice searchBean) throws CIServiceException {
        new ArrayList();

        try {
            List result = this.personNoticeJDao.queryPagePersonNoticeList(currPage, pageSize, searchBean);
            return result;
        } catch (Exception var7) {
            String message = "查询个人通知错误";
            this.log.error(message, var7);
            throw new CIServiceException(message, var7);
        }
    }

    public List<CiPersonNotice> queryPersonNoticeList(CiPersonNotice searchBean) throws CIServiceException {
        new ArrayList();

        try {
            List result = this.personNoticeJDao.queryPersonNoticeList(searchBean);
            return result;
        } catch (Exception var5) {
            String message = "查询个人通知错误";
            this.log.error(message, var5);
            throw new CIServiceException(message, var5);
        }
    }

    public void personNoticeBySms(CiPersonNotice personNotice) throws CIServiceException {
        String message;
        try {
            String e = Configure.getInstance().getProperty("SMS_NOTIFICATION_NONSTANDARD_CLASS_NAME");
            if(!StringUtil.isEmpty(e)) {
                message = "2";
                int flag = this.getPersonNoticeOpenClose(personNotice, message);
                if(StringUtil.isNotEmpty(e) && flag == 1) {
                    ISmsNotification smsNotificationImp = (ISmsNotification)SystemServiceLocator.getInstance().getService(e);
                    smsNotificationImp.personNoticeSms(personNotice);
                }

            }
        } catch (Exception var6) {
            message = "短信发送个人通知错误";
            this.log.error(message, var6);
            throw new CIServiceException(message, var6);
        }
    }

    public void personNoticeByMail(CiPersonNotice personNotice) throws CIServiceException {
        String message;
        try {
            String e = Configure.getInstance().getProperty("MAIL_NOTIFICATION_NONSTANDARD_CLASS_NAME");
            if(!StringUtil.isEmpty(e)) {
                message = "3";
                int flag = this.getPersonNoticeOpenClose(personNotice, message);
                if(StringUtil.isNotEmpty(e) && flag == 1) {
                    IMailNotification mailNotificationImp = (IMailNotification)SystemServiceLocator.getInstance().getService(e);
                    mailNotificationImp.personNoticeMail(personNotice);
                }

            }
        } catch (Exception var6) {
            message = "邮件发送个人通知错误";
            this.log.error(message, var6);
            throw new CIServiceException(message, var6);
        }
    }

    public int getPersonNoticeOpenClose(CiPersonNotice personNotice, String sendModeId) throws CIServiceException {
        boolean flag = false;
        String userId = personNotice.getReceiveUserId();
        String noticeId = personNotice.getNoticeTypeId().toString();
        int isSuccess = personNotice.getIsSuccess().intValue() == 1?1:2;
        List ciUserNoticeSetList = this.ciUserNoticeSetHDao.selectUserNoticeOpenClose(userId, noticeId, Integer.valueOf(2), Integer.valueOf(isSuccess), sendModeId);
        int flag1;
        if(null != ciUserNoticeSetList && ciUserNoticeSetList.size() != 0) {
            flag1 = ((CiUserNoticeSet)ciUserNoticeSetList.get(0)).getIsReceive().intValue();
        } else {
            flag1 = 1;
        }

        return flag1;
    }

    public void modifyShowTipStatus(String noticeId) throws CIServiceException {
        CiPersonNotice personNotice = this.personNoticeHDao.selectById(noticeId);
        personNotice.setIsShowTip(Integer.valueOf(0));
        this.personNoticeHDao.update(personNotice);
    }

    public void delete(String noticeId) throws CIServiceException {
        try {
            CiPersonNotice e = this.personNoticeHDao.selectById(noticeId);
            e.setStatus(Integer.valueOf(2));
            this.personNoticeHDao.update(e);
        } catch (Exception var3) {
            this.log.error("删除标签失败", var3);
            throw new CIServiceException("删除标签失败");
        }
    }

    public void readPersonNoticeAll(CiPersonNotice personNotice) throws CIServiceException {
        try {
            this.personNoticeHDao.updateCiPersonNoticeListRead(personNotice);
        } catch (Exception var3) {
            this.log.error("批量删除标签失败", var3);
            throw new CIServiceException("批量删除标签失败");
        }
    }

    public void deleteAll(CiPersonNotice personNotice) throws CIServiceException {
        try {
            this.personNoticeHDao.updateCiPersonNoticeListDelete(personNotice);
        } catch (Exception var3) {
            this.log.error("批量删除标签失败", var3);
            throw new CIServiceException("批量删除标签失败");
        }
    }
}
