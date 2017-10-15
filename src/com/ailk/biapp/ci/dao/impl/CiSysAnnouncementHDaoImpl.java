package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiSysAnnouncementHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiSysAnnouncement;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.springframework.stereotype.Repository;

@Repository
public class CiSysAnnouncementHDaoImpl extends HibernateBaseDao<CiSysAnnouncement, String> implements ICiSysAnnouncementHDao {
    public CiSysAnnouncementHDaoImpl() {
    }

    public void insertCiSysAnnouncement(CiSysAnnouncement sysAnnouncement) {
        this.save(sysAnnouncement);
    }

    public void updateCiSysAnnouncement(CiSysAnnouncement sysAnnouncement) {
        this.save(sysAnnouncement);
    }

    public void deleteCiSysAnnouncement(CiSysAnnouncement sysAnnouncement) {
        CiSysAnnouncement obj = this.selectCiSysAnnouncementById(sysAnnouncement.getAnnouncementId());
        obj.setStatus(Integer.valueOf(0));
        this.save(obj);
    }

    public List<CiSysAnnouncement> selectCiSysAnnouncementList(CiSysAnnouncement sysAnnouncement) {
        Criteria c = this.getSession().createCriteria(CiSysAnnouncement.class);
        Example example = Example.create(sysAnnouncement);
        c.add(example);
        return c.list();
    }

    public CiSysAnnouncement selectCiSysAnnouncementById(String id) {
        CiSysAnnouncement sysAnnouncement = (CiSysAnnouncement)this.get(id);
        return sysAnnouncement;
    }
}
