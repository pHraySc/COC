package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiPersonNoticeHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiPersonNotice;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.springframework.stereotype.Repository;

@Repository
public class CiPersonNoticeHDaoImpl extends HibernateBaseDao<CiPersonNotice, String> implements ICiPersonNoticeHDao {
    public CiPersonNoticeHDaoImpl() {
    }

    public void save(CiPersonNotice bean) {
        super.save(bean);
    }

    public void update(CiPersonNotice bean) {
        super.save(bean);
    }

    public List<CiPersonNotice> selectByExample(CiPersonNotice bean) {
        Criteria c = this.getSession().createCriteria(CiPersonNotice.class);
        Example example = Example.create(bean);
        c.add(example);
        return c.list();
    }

    public CiPersonNotice selectById(String noticeId) {
        return (CiPersonNotice)this.get(noticeId);
    }

    public void updateCiPersonNoticeListRead(CiPersonNotice bean) {
        StringBuffer hql = new StringBuffer();
        hql.append("update CiPersonNotice t set t.readStatus=").append(2).append("  , t.isShowTip=").append(0).append(" where t.status=? and t.receiveUserId=? and t.readStatus=? ");
        this.batchExecute(hql.toString(), new Object[]{bean.getStatus(), bean.getReceiveUserId(), bean.getReadStatus()});
    }

    public void updateCiPersonNoticeListDelete(CiPersonNotice bean) {
        StringBuffer hql = new StringBuffer();
        hql.append("update CiPersonNotice t set t.status=").append(2).append(" where t.status=? and t.receiveUserId=? ");
        this.batchExecute(hql.toString(), new Object[]{bean.getStatus(), bean.getReceiveUserId()});
    }
}
