package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiUserNoticeSetHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiUserNoticeSet;
import com.ailk.biapp.ci.entity.CiUserNoticeSetId;
import com.ailk.biapp.ci.entity.DimAnnouncementType;
import com.ailk.biapp.ci.entity.DimNoticeSendMode;
import com.ailk.biapp.ci.entity.DimNoticeType;
import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

@Repository
public class CiUserNoticeSetHDaoImpl extends HibernateBaseDao<CiUserNoticeSet, CiUserNoticeSetId> implements ICiUserNoticeSetHDao {
    private Logger log = Logger.getLogger(this.getClass());

    public CiUserNoticeSetHDaoImpl() {
    }

    public void insertUserNoticeSet(List<CiUserNoticeSet> noticeSets) throws CIServiceException {
        Iterator i$ = noticeSets.iterator();

        while(i$.hasNext()) {
            CiUserNoticeSet ciUserNoticeSet = (CiUserNoticeSet)i$.next();
            this.log.info("CiUserNoticeSetHDao Begin:" + ciUserNoticeSet);
            this.save(ciUserNoticeSet);
            this.log.info("CiUserNoticeSetHDao End");
        }

    }

    public List<CiUserNoticeSet> selectUserNoticeSet(String userId) throws CIServiceException {
        SimpleExpression criterion = Restrictions.eq("id.userId", userId);
        List list = this.find(new Criterion[]{criterion});
        return list;
    }

    public List<DimAnnouncementType> selectDimAnnouncementType() throws CIServiceException {
        String hql = "from DimAnnouncementType order by typeId";
        Session session = this.getSession();
        List result = null;

        try {
            Query e = session.createQuery(hql);
            result = e.list();
        } catch (Exception var9) {
            this.log.error("公告类型查询异常", var9);
            throw new CIServiceException("公告类型查询异常");
        } finally {
            if(null != session) {
                SessionFactoryUtils.releaseSession(session, this.getSessionFactory());
            }

        }

        return result;
    }

    public List<DimNoticeType> selectDimNoticeType() throws CIServiceException {
        String hql = "from DimNoticeType order by noticeTypeId";
        System.out.println(hql);
        Session session = this.getSession();
        List result = null;

        try {
            Query e = session.createQuery(hql);
            result = e.list();
        } catch (Exception var9) {
            this.log.error("个人通知类型查询异常", var9);
            throw new CIServiceException("个人通知类型查询异常");
        } finally {
            if(null != session) {
                SessionFactoryUtils.releaseSession(session, this.getSessionFactory());
            }

        }

        return result;
    }

    public void addUserNoticeSet(CiUserNoticeSet noticeSet) throws CIServiceException {
        this.log.info("CiUserNoticeSetHDao Begin:" + noticeSet);
        this.save(noticeSet);
        this.log.info("CiUserNoticeSetHDao End");
    }

    public List<CiUserNoticeSet> selectUserNoticeSet(String userId, String noticeType) throws CIServiceException {
        String hql = "FROM CiUserNoticeSet WHERE id.userId=:userId AND id.noticeType=:noticeType";
        HashMap param = new HashMap();
        param.put("userId", userId);
        param.put("noticeType", new Integer(noticeType));
        List list = this.find(hql, param);
        return list;
    }

    public List<CiUserNoticeSet> selectUserNoticeOpenClose(String userId, String noticeId, Integer noticeType, Integer isSuccess, String sendModeId) throws CIServiceException {
        String hql = "FROM CiUserNoticeSet WHERE id.userId=:userId AND id.noticeId=:noticeId AND id.noticeType=:noticeType AND id.isSuccess=:isSuccess AND id.sendModeId=:sendModeId";
        this.log.debug("select sql is : " + hql);
        HashMap param = new HashMap();
        param.put("userId", userId);
        param.put("noticeId", noticeId);
        param.put("noticeType", noticeType);
        param.put("isSuccess", isSuccess);
        param.put("sendModeId", sendModeId);
        List list = this.find(hql, param);
        return list;
    }

    public List<CiUserNoticeSet> selectUserNoticeSetInfo(String userId, String noticeType, int noticeTypeId) throws CIServiceException {
        String hql = "FROM CiUserNoticeSet WHERE id.userId=:userId AND id.noticeId=:noticeType AND id.noticeType=:noticeTypeId";
        HashMap param = new HashMap();
        param.put("userId", userId);
        param.put("noticeType", noticeType);
        param.put("noticeTypeId", Integer.valueOf(noticeTypeId));
        List list = this.find(hql, param);
        return list;
    }

    public List<DimNoticeSendMode> selectdimNoticeSendMode() throws CIServiceException {
        String hql = "from DimNoticeSendMode order by sortNum";
        Session session = this.getSession();
        List result = null;

        try {
            Query e = session.createQuery(hql);
            result = e.list();
        } catch (Exception var9) {
            this.log.error("通知发送方式查询异常", var9);
            throw new CIServiceException("通知发送方式查询异常");
        } finally {
            if(null != session) {
                SessionFactoryUtils.releaseSession(session, this.getSessionFactory());
            }

        }

        return result;
    }
}
