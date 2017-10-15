package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiUserNoticeSetHDao;
import com.ailk.biapp.ci.entity.CiUserNoticeSet;
import com.ailk.biapp.ci.entity.CiUserNoticeSetId;
import com.ailk.biapp.ci.entity.DimAnnouncementType;
import com.ailk.biapp.ci.entity.DimNoticeSendMode;
import com.ailk.biapp.ci.entity.DimNoticeType;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ICiUserNoticeSetService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("ciUserNoticeSetService")
@Transactional
public class CiUserNoticeSetServiceImpl implements ICiUserNoticeSetService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ICiUserNoticeSetHDao ciUserNoticeSetHDao;

    public CiUserNoticeSetServiceImpl() {
    }

    public void addUserNoticeSet(List<CiUserNoticeSet> noticeSets) throws CIServiceException {
        try {
            this.ciUserNoticeSetHDao.insertUserNoticeSet(noticeSets);
        } catch (Exception var4) {
            String error = "保存用户接收系统和个人通知设定失败";
            this.log.error(error, var4);
            throw new CIServiceException(error);
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiUserNoticeSet> queryUserNoticeSet(String userId) throws CIServiceException {
        ArrayList list = new ArrayList();
        List userNoticeList = null;
        List announcementList = null;
        List noticeList = null;

        try {
            userNoticeList = this.ciUserNoticeSetHDao.selectUserNoticeSet(userId);
            announcementList = this.ciUserNoticeSetHDao.selectDimAnnouncementType();
            noticeList = this.ciUserNoticeSetHDao.selectDimNoticeType();
            Iterator e;
            String receive;
            int id;
            CiUserNoticeSet i;
            CiUserNoticeSetId temp;
            CiUserNoticeSet tempId;
            CiUserNoticeSetId id1;
            if(null != announcementList && announcementList.size() > 0) {
                e = announcementList.iterator();

                while(e.hasNext()) {
                    DimAnnouncementType var15 = (DimAnnouncementType)e.next();
                    receive = "1";
                    id = var15.getHasSuccessFail().intValue();
                    if(id == 1) {
                        i = new CiUserNoticeSet();
                        temp = new CiUserNoticeSetId();
                        temp.setUserId(userId);
                        temp.setNoticeType(Integer.valueOf(1));
                        temp.setNoticeId(var15.getTypeId().toString());
                        temp.setIsSuccess(Integer.valueOf(1));
                        temp.setSendModeId(receive);
                        i.setId(temp);
                        i.setTypeName(var15.getTypeName() + "--成功");
                        i.setTypeDesc(var15.getTypeDesc());
                        i.setIsReceive(Integer.valueOf(1));
                        list.add(i);
                        tempId = new CiUserNoticeSet();
                        id1 = new CiUserNoticeSetId();
                        id1.setUserId(userId);
                        id1.setNoticeType(Integer.valueOf(1));
                        id1.setNoticeId(var15.getTypeId().toString());
                        id1.setIsSuccess(Integer.valueOf(2));
                        id1.setSendModeId(receive);
                        tempId.setId(id1);
                        tempId.setTypeName(var15.getTypeName() + "--失败");
                        tempId.setTypeDesc(var15.getTypeDesc());
                        tempId.setIsReceive(Integer.valueOf(1));
                        list.add(tempId);
                    } else {
                        i = new CiUserNoticeSet();
                        temp = new CiUserNoticeSetId();
                        temp.setUserId(userId);
                        temp.setNoticeType(Integer.valueOf(1));
                        temp.setNoticeId(var15.getTypeId().toString());
                        temp.setIsSuccess(Integer.valueOf(1));
                        temp.setSendModeId(receive);
                        i.setId(temp);
                        i.setTypeName(var15.getTypeName());
                        i.setTypeDesc(var15.getTypeDesc());
                        i.setIsReceive(Integer.valueOf(1));
                        list.add(i);
                    }
                }
            }

            if(null != noticeList && noticeList.size() > 0) {
                e = noticeList.iterator();

                while(e.hasNext()) {
                    DimNoticeType var16 = (DimNoticeType)e.next();
                    receive = "1";
                    id = var16.getHasSuccessFail().intValue();
                    if(id == 1) {
                        i = new CiUserNoticeSet();
                        temp = new CiUserNoticeSetId();
                        temp.setUserId(userId);
                        temp.setNoticeType(Integer.valueOf(2));
                        temp.setNoticeId(var16.getNoticeTypeId().toString());
                        temp.setIsSuccess(Integer.valueOf(1));
                        temp.setSendModeId(receive);
                        i.setId(temp);
                        i.setTypeName(var16.getNoticeTypeName() + "--成功");
                        i.setTypeDesc(var16.getNoticeTypeDesc());
                        i.setIsReceive(Integer.valueOf(1));
                        list.add(i);
                        tempId = new CiUserNoticeSet();
                        id1 = new CiUserNoticeSetId();
                        id1.setUserId(userId);
                        id1.setNoticeType(Integer.valueOf(2));
                        id1.setNoticeId(var16.getNoticeTypeId().toString());
                        id1.setIsSuccess(Integer.valueOf(2));
                        id1.setSendModeId(receive);
                        tempId.setId(id1);
                        tempId.setTypeName(var16.getNoticeTypeName() + "--失败");
                        tempId.setTypeDesc(var16.getNoticeTypeDesc());
                        tempId.setIsReceive(Integer.valueOf(1));
                        list.add(tempId);
                    } else {
                        i = new CiUserNoticeSet();
                        temp = new CiUserNoticeSetId();
                        temp.setUserId(userId);
                        temp.setNoticeType(Integer.valueOf(2));
                        temp.setNoticeId(var16.getNoticeTypeId().toString());
                        temp.setIsSuccess(Integer.valueOf(1));
                        temp.setSendModeId(receive);
                        i.setId(temp);
                        i.setTypeName(var16.getNoticeTypeName());
                        i.setTypeDesc(var16.getNoticeTypeDesc());
                        i.setIsReceive(Integer.valueOf(1));
                        list.add(i);
                    }
                }
            }

            if(null != userNoticeList && userNoticeList.size() > 0) {
                e = userNoticeList.iterator();

                while(true) {
                    while(true) {
                        CiUserNoticeSet var17;
                        int var18;
                        do {
                            if(!e.hasNext()) {
                                return list;
                            }

                            var17 = (CiUserNoticeSet)e.next();
                            var18 = var17.getIsReceive().intValue();
                        } while(var18 != 0);

                        CiUserNoticeSetId var19 = var17.getId();

                        for(int var20 = 0; var20 < list.size(); ++var20) {
                            CiUserNoticeSet var21 = (CiUserNoticeSet)list.get(var20);
                            CiUserNoticeSetId var22 = var21.getId();
                            if(var19.equals(var22)) {
                                var21.setIsReceive(Integer.valueOf(0));
                                break;
                            }
                        }
                    }
                }
            } else {
                return list;
            }
        } catch (Exception var14) {
            String error = "获取用户接收系统和个人通知设定失败";
            this.log.error(error, var14);
            throw new CIServiceException(error);
        }
    }

    public List<CiUserNoticeSet> queryUserNoticeSet(String userId, String noticeType, String sendModeId) throws CIServiceException {
        ArrayList list = new ArrayList();
        List userNoticeList = null;
        List announcementList = null;
        List noticeList = null;

        try {
            userNoticeList = this.ciUserNoticeSetHDao.selectUserNoticeSet(userId);
            Iterator e;
            int receive;
            CiUserNoticeSet id;
            CiUserNoticeSetId i;
            CiUserNoticeSet temp;
            CiUserNoticeSetId tempId;
            if(noticeType.equals("1")) {
                announcementList = this.ciUserNoticeSetHDao.selectDimAnnouncementType();
                if(null != announcementList && announcementList.size() > 0) {
                    e = announcementList.iterator();

                    while(e.hasNext()) {
                        DimAnnouncementType var16 = (DimAnnouncementType)e.next();
                        receive = var16.getHasSuccessFail().intValue();
                        if(receive == 1) {
                            id = new CiUserNoticeSet();
                            i = new CiUserNoticeSetId();
                            i.setUserId(userId);
                            i.setNoticeType(Integer.valueOf(1));
                            i.setNoticeId(var16.getTypeId().toString());
                            i.setSendModeId(sendModeId);
                            i.setIsSuccess(Integer.valueOf(1));
                            id.setId(i);
                            id.setTypeName(var16.getTypeName() + "--成功");
                            id.setTypeDesc(var16.getTypeDesc());
                            id.setIsReceive(Integer.valueOf(1));
                            list.add(id);
                            temp = new CiUserNoticeSet();
                            tempId = new CiUserNoticeSetId();
                            tempId.setUserId(userId);
                            tempId.setNoticeType(Integer.valueOf(1));
                            tempId.setNoticeId(var16.getTypeId().toString());
                            tempId.setSendModeId(sendModeId);
                            tempId.setIsSuccess(Integer.valueOf(2));
                            temp.setId(tempId);
                            temp.setTypeName(var16.getTypeName() + "--失败");
                            temp.setTypeDesc(var16.getTypeDesc());
                            temp.setIsReceive(Integer.valueOf(1));
                            list.add(temp);
                        } else {
                            id = new CiUserNoticeSet();
                            i = new CiUserNoticeSetId();
                            i.setUserId(userId);
                            i.setNoticeType(Integer.valueOf(1));
                            i.setNoticeId(var16.getTypeId().toString());
                            i.setSendModeId(sendModeId);
                            i.setIsSuccess(Integer.valueOf(1));
                            id.setId(i);
                            id.setTypeName(var16.getTypeName());
                            id.setTypeDesc(var16.getTypeDesc());
                            id.setIsReceive(Integer.valueOf(1));
                            list.add(id);
                        }
                    }
                }
            }

            if(noticeType.equals("2")) {
                noticeList = this.ciUserNoticeSetHDao.selectDimNoticeType();
                if(null != noticeList && noticeList.size() > 0) {
                    e = noticeList.iterator();

                    while(e.hasNext()) {
                        DimNoticeType var17 = (DimNoticeType)e.next();
                        receive = var17.getHasSuccessFail().intValue();
                        if(receive == 1) {
                            id = new CiUserNoticeSet();
                            i = new CiUserNoticeSetId();
                            i.setUserId(userId);
                            i.setNoticeType(Integer.valueOf(2));
                            i.setNoticeId(var17.getNoticeTypeId().toString());
                            i.setSendModeId(sendModeId);
                            i.setIsSuccess(Integer.valueOf(1));
                            id.setId(i);
                            id.setTypeName(var17.getNoticeTypeName() + "--成功");
                            id.setTypeDesc(var17.getNoticeTypeDesc());
                            id.setIsReceive(Integer.valueOf(1));
                            list.add(id);
                            temp = new CiUserNoticeSet();
                            tempId = new CiUserNoticeSetId();
                            tempId.setUserId(userId);
                            tempId.setNoticeType(Integer.valueOf(2));
                            tempId.setNoticeId(var17.getNoticeTypeId().toString());
                            tempId.setSendModeId(sendModeId);
                            tempId.setIsSuccess(Integer.valueOf(2));
                            temp.setId(tempId);
                            temp.setTypeName(var17.getNoticeTypeName() + "--失败");
                            temp.setTypeDesc(var17.getNoticeTypeDesc());
                            temp.setIsReceive(Integer.valueOf(1));
                            list.add(temp);
                        } else {
                            id = new CiUserNoticeSet();
                            i = new CiUserNoticeSetId();
                            i.setUserId(userId);
                            i.setNoticeType(Integer.valueOf(2));
                            i.setNoticeId(var17.getNoticeTypeId().toString());
                            i.setSendModeId(sendModeId);
                            i.setIsSuccess(Integer.valueOf(1));
                            id.setId(i);
                            id.setTypeName(var17.getNoticeTypeName());
                            id.setTypeDesc(var17.getNoticeTypeDesc());
                            id.setIsReceive(Integer.valueOf(1));
                            list.add(id);
                        }
                    }
                }
            }

            if(null != userNoticeList && userNoticeList.size() > 0) {
                e = userNoticeList.iterator();

                while(true) {
                    while(true) {
                        CiUserNoticeSet var18;
                        do {
                            if(!e.hasNext()) {
                                return list;
                            }

                            var18 = (CiUserNoticeSet)e.next();
                            receive = var18.getIsReceive().intValue();
                        } while(receive != 0);

                        CiUserNoticeSetId var19 = var18.getId();

                        for(int var20 = 0; var20 < list.size(); ++var20) {
                            temp = (CiUserNoticeSet)list.get(var20);
                            tempId = temp.getId();
                            if(var19.equals(tempId)) {
                                temp.setIsReceive(Integer.valueOf(0));
                                break;
                            }
                        }
                    }
                }
            } else {
                return list;
            }
        } catch (Exception var15) {
            String error = "获取用户接收系统和个人通知设定失败";
            this.log.error(error, var15);
            throw new CIServiceException(error);
        }
    }

    public void addUserNoticeSet(CiUserNoticeSet noticeSet) throws CIServiceException {
        try {
            this.ciUserNoticeSetHDao.addUserNoticeSet(noticeSet);
        } catch (Exception var4) {
            String error = "保存用户接收系统和个人通知设定失败";
            this.log.error(error, var4);
            throw new CIServiceException(error);
        }
    }

    public List<CiUserNoticeSet> queryUserNoticeSetInfo(String userId, String noticeType, int noticeTypeId) throws CIServiceException {
        ArrayList list = new ArrayList();
        List userNoticeList = null;
        List announcementList = null;
        List noticeList = null;
        List noticeSendModeList = null;

        try {
            noticeSendModeList = this.ciUserNoticeSetHDao.selectdimNoticeSendMode();
            userNoticeList = this.ciUserNoticeSetHDao.selectUserNoticeSetInfo(userId, noticeType, noticeTypeId);
            Iterator e;
            String receive;
            Iterator id;
            DimNoticeSendMode i;
            String temp;
            int tempId;
            CiUserNoticeSet set;
            CiUserNoticeSetId id1;
            CiUserNoticeSet set1;
            if(noticeTypeId == 1) {
                announcementList = this.ciUserNoticeSetHDao.selectDimAnnouncementType();
                if(null != announcementList && announcementList.size() > 0) {
                    e = announcementList.iterator();

                    label106:
                    while(true) {
                        DimAnnouncementType var21;
                        do {
                            if(!e.hasNext()) {
                                break label106;
                            }

                            var21 = (DimAnnouncementType)e.next();
                            receive = var21.getTypeId().toString();
                        } while(!receive.equals(noticeType) && receive != noticeType);

                        id = noticeSendModeList.iterator();

                        while(id.hasNext()) {
                            i = (DimNoticeSendMode)id.next();
                            temp = i.getSendModeId();
                            tempId = var21.getHasSuccessFail().intValue();
                            if(tempId == 1) {
                                set = new CiUserNoticeSet();
                                id1 = new CiUserNoticeSetId();
                                id1.setUserId(userId);
                                id1.setNoticeType(Integer.valueOf(1));
                                id1.setNoticeId(var21.getTypeId().toString());
                                id1.setSendModeId(temp);
                                id1.setIsSuccess(Integer.valueOf(1));
                                set.setId(id1);
                                set.setTypeName(var21.getTypeName() + "--成功");
                                set.setTypeDesc(var21.getTypeDesc());
                                set.setIsReceive(Integer.valueOf(1));
                                list.add(set);
                                set1 = new CiUserNoticeSet();
                                id1 = new CiUserNoticeSetId();
                                id1.setUserId(userId);
                                id1.setNoticeType(Integer.valueOf(1));
                                id1.setNoticeId(var21.getTypeId().toString());
                                id1.setSendModeId(temp);
                                id1.setIsSuccess(Integer.valueOf(2));
                                set1.setId(id1);
                                set1.setTypeName(var21.getTypeName() + "--失败");
                                set1.setTypeDesc(var21.getTypeDesc());
                                set1.setIsReceive(Integer.valueOf(1));
                                list.add(set1);
                            } else {
                                set = new CiUserNoticeSet();
                                id1 = new CiUserNoticeSetId();
                                id1.setUserId(userId);
                                id1.setNoticeType(Integer.valueOf(1));
                                id1.setNoticeId(var21.getTypeId().toString());
                                id1.setSendModeId(temp);
                                id1.setIsSuccess(Integer.valueOf(1));
                                set.setId(id1);
                                set.setTypeName(var21.getTypeName());
                                set.setTypeDesc(var21.getTypeDesc());
                                set.setIsReceive(Integer.valueOf(1));
                                list.add(set);
                            }
                        }
                    }
                }
            }

            if(noticeTypeId == 2) {
                noticeList = this.ciUserNoticeSetHDao.selectDimNoticeType();
                if(null != noticeList && noticeList.size() > 0) {
                    e = noticeList.iterator();

                    label86:
                    while(true) {
                        DimNoticeType var22;
                        do {
                            if(!e.hasNext()) {
                                break label86;
                            }

                            var22 = (DimNoticeType)e.next();
                            receive = var22.getNoticeTypeId().toString();
                        } while(!receive.equals(noticeType) && receive != noticeType);

                        id = noticeSendModeList.iterator();

                        while(id.hasNext()) {
                            i = (DimNoticeSendMode)id.next();
                            temp = i.getSendModeId();
                            tempId = var22.getHasSuccessFail().intValue();
                            if(tempId == 1) {
                                set = new CiUserNoticeSet();
                                id1 = new CiUserNoticeSetId();
                                id1.setUserId(userId);
                                id1.setNoticeType(Integer.valueOf(2));
                                id1.setNoticeId(var22.getNoticeTypeId().toString());
                                id1.setSendModeId(temp);
                                id1.setIsSuccess(Integer.valueOf(1));
                                set.setId(id1);
                                set.setTypeName(var22.getNoticeTypeName() + "--成功");
                                set.setTypeDesc(var22.getNoticeTypeDesc());
                                set.setIsReceive(Integer.valueOf(1));
                                list.add(set);
                                set1 = new CiUserNoticeSet();
                                id1 = new CiUserNoticeSetId();
                                id1.setUserId(userId);
                                id1.setNoticeType(Integer.valueOf(2));
                                id1.setNoticeId(var22.getNoticeTypeId().toString());
                                id1.setSendModeId(temp);
                                id1.setIsSuccess(Integer.valueOf(2));
                                set1.setId(id1);
                                set1.setTypeName(var22.getNoticeTypeName() + "--失败");
                                set1.setTypeDesc(var22.getNoticeTypeDesc());
                                set1.setIsReceive(Integer.valueOf(1));
                                list.add(set1);
                            } else {
                                set = new CiUserNoticeSet();
                                id1 = new CiUserNoticeSetId();
                                id1.setUserId(userId);
                                id1.setNoticeType(Integer.valueOf(2));
                                id1.setNoticeId(var22.getNoticeTypeId().toString());
                                id1.setSendModeId(temp);
                                id1.setIsSuccess(Integer.valueOf(1));
                                set.setId(id1);
                                set.setTypeName(var22.getNoticeTypeName());
                                set.setTypeDesc(var22.getNoticeTypeDesc());
                                set.setIsReceive(Integer.valueOf(1));
                                list.add(set);
                            }
                        }
                    }
                }
            }

            if(null != userNoticeList && userNoticeList.size() > 0) {
                e = userNoticeList.iterator();

                while(true) {
                    while(true) {
                        CiUserNoticeSet var23;
                        int var24;
                        do {
                            if(!e.hasNext()) {
                                return list;
                            }

                            var23 = (CiUserNoticeSet)e.next();
                            var24 = var23.getIsReceive().intValue();
                        } while(var24 != 0);

                        CiUserNoticeSetId var25 = var23.getId();

                        for(int var26 = 0; var26 < list.size(); ++var26) {
                            CiUserNoticeSet var27 = (CiUserNoticeSet)list.get(var26);
                            CiUserNoticeSetId var28 = var27.getId();
                            if(var25.equals(var28)) {
                                var27.setIsReceive(Integer.valueOf(0));
                                break;
                            }
                        }
                    }
                }
            } else {
                return list;
            }
        } catch (Exception var20) {
            String error = "获取用户个人通知设置信息失败";
            this.log.error(error, var20);
            throw new CIServiceException(error);
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiUserNoticeSet> queryUserNoticeSetInit(String userId, String sendModeId) throws CIServiceException {
        ArrayList list = new ArrayList();
        List userNoticeList = null;
        List announcementList = null;
        List noticeList = null;

        try {
            userNoticeList = this.ciUserNoticeSetHDao.selectUserNoticeSet(userId);
            announcementList = this.ciUserNoticeSetHDao.selectDimAnnouncementType();
            noticeList = this.ciUserNoticeSetHDao.selectDimNoticeType();
            Iterator e;
            int receive;
            CiUserNoticeSet id;
            CiUserNoticeSetId i;
            CiUserNoticeSet temp;
            CiUserNoticeSetId tempId;
            if(null != announcementList && announcementList.size() > 0) {
                e = announcementList.iterator();

                while(e.hasNext()) {
                    DimAnnouncementType var15 = (DimAnnouncementType)e.next();
                    receive = var15.getHasSuccessFail().intValue();
                    if(receive == 1) {
                        id = new CiUserNoticeSet();
                        i = new CiUserNoticeSetId();
                        i.setUserId(userId);
                        i.setNoticeType(Integer.valueOf(1));
                        i.setNoticeId(var15.getTypeId().toString());
                        i.setSendModeId(sendModeId);
                        i.setIsSuccess(Integer.valueOf(1));
                        id.setId(i);
                        id.setTypeName(var15.getTypeName() + "--成功");
                        id.setTypeDesc(var15.getTypeDesc());
                        id.setIsReceive(Integer.valueOf(1));
                        list.add(id);
                        temp = new CiUserNoticeSet();
                        tempId = new CiUserNoticeSetId();
                        tempId.setUserId(userId);
                        tempId.setNoticeType(Integer.valueOf(1));
                        tempId.setNoticeId(var15.getTypeId().toString());
                        tempId.setSendModeId(sendModeId);
                        tempId.setIsSuccess(Integer.valueOf(2));
                        temp.setId(tempId);
                        temp.setTypeName(var15.getTypeName() + "--失败");
                        temp.setTypeDesc(var15.getTypeDesc());
                        temp.setIsReceive(Integer.valueOf(1));
                        list.add(temp);
                    } else {
                        id = new CiUserNoticeSet();
                        i = new CiUserNoticeSetId();
                        i.setUserId(userId);
                        i.setNoticeType(Integer.valueOf(1));
                        i.setNoticeId(var15.getTypeId().toString());
                        i.setSendModeId(sendModeId);
                        i.setIsSuccess(Integer.valueOf(1));
                        id.setId(i);
                        id.setTypeName(var15.getTypeName());
                        id.setTypeDesc(var15.getTypeDesc());
                        id.setIsReceive(Integer.valueOf(1));
                        list.add(id);
                    }
                }
            }

            if(null != noticeList && noticeList.size() > 0) {
                e = noticeList.iterator();

                while(e.hasNext()) {
                    DimNoticeType var16 = (DimNoticeType)e.next();
                    receive = var16.getHasSuccessFail().intValue();
                    if(receive == 1) {
                        id = new CiUserNoticeSet();
                        i = new CiUserNoticeSetId();
                        i.setUserId(userId);
                        i.setNoticeType(Integer.valueOf(2));
                        i.setNoticeId(var16.getNoticeTypeId().toString());
                        i.setSendModeId(sendModeId);
                        i.setIsSuccess(Integer.valueOf(1));
                        id.setId(i);
                        id.setTypeName(var16.getNoticeTypeName() + "--成功");
                        id.setTypeDesc(var16.getNoticeTypeDesc());
                        id.setIsReceive(Integer.valueOf(1));
                        list.add(id);
                        temp = new CiUserNoticeSet();
                        tempId = new CiUserNoticeSetId();
                        tempId.setUserId(userId);
                        tempId.setNoticeType(Integer.valueOf(2));
                        tempId.setNoticeId(var16.getNoticeTypeId().toString());
                        tempId.setSendModeId(sendModeId);
                        tempId.setIsSuccess(Integer.valueOf(2));
                        temp.setId(tempId);
                        temp.setTypeName(var16.getNoticeTypeName() + "--失败");
                        temp.setTypeDesc(var16.getNoticeTypeDesc());
                        temp.setIsReceive(Integer.valueOf(1));
                        list.add(temp);
                    } else {
                        id = new CiUserNoticeSet();
                        i = new CiUserNoticeSetId();
                        i.setUserId(userId);
                        i.setNoticeType(Integer.valueOf(2));
                        i.setNoticeId(var16.getNoticeTypeId().toString());
                        i.setSendModeId(sendModeId);
                        i.setIsSuccess(Integer.valueOf(1));
                        id.setId(i);
                        id.setTypeName(var16.getNoticeTypeName());
                        id.setTypeDesc(var16.getNoticeTypeDesc());
                        id.setIsReceive(Integer.valueOf(1));
                        list.add(id);
                    }
                }
            }

            if(null != userNoticeList && userNoticeList.size() > 0) {
                e = userNoticeList.iterator();

                while(true) {
                    while(true) {
                        CiUserNoticeSet var17;
                        do {
                            if(!e.hasNext()) {
                                return list;
                            }

                            var17 = (CiUserNoticeSet)e.next();
                            receive = var17.getIsReceive().intValue();
                        } while(receive != 0);

                        CiUserNoticeSetId var18 = var17.getId();

                        for(int var19 = 0; var19 < list.size(); ++var19) {
                            temp = (CiUserNoticeSet)list.get(var19);
                            tempId = temp.getId();
                            if(var18.equals(tempId)) {
                                temp.setIsReceive(Integer.valueOf(0));
                                break;
                            }
                        }
                    }
                }
            } else {
                return list;
            }
        } catch (Exception var14) {
            String error = "获取用户接收系统和个人通知设定失败";
            this.log.error(error, var14);
            throw new CIServiceException(error);
        }
    }
}
