package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiCustomListInfoHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.springframework.stereotype.Repository;

@Repository
public class CiCustomListInfoHDaoImpl extends HibernateBaseDao<CiCustomListInfo, String> implements ICiCustomListInfoHDao {
    public CiCustomListInfoHDaoImpl() {
    }

    public List<CiCustomListInfo> selectByCustomListInfo(CiCustomListInfo customListInfo) {
        Criteria c = this.getSession().createCriteria(CiCustomListInfo.class);
        Example example = Example.create(customListInfo);
        c.add(example);
        return c.list();
    }

    public List<CiCustomListInfo> selectByCustomGroupId(String customGroupId) {
        String hql = " from CiCustomListInfo where customGroupId = ?  order by dataDate desc";
        return this.find(hql, new Object[]{customGroupId});
    }

    public List<CiCustomListInfo> selectSuccessByCustomGroupId(String customGroupId) {
        String hql = " from CiCustomListInfo where customGroupId = ?  and dataStatus = 3 order by dataDate desc";
        return this.find(hql, new Object[]{customGroupId});
    }

    public CiCustomListInfo selectByCustomGroupIdAndDataDate(String customGroupId, String dataDate) {
        String hql = " from CiCustomListInfo where customGroupId = ?  and dataDate = ? order by dataDate desc";
        return (CiCustomListInfo)this.findUnique(hql, new Object[]{customGroupId, dataDate});
    }

    public void insertCiCustomListInfo(CiCustomListInfo ciCustomListInfo) {
        this.save(ciCustomListInfo);
    }

    public void updateCiCustomListInfo(CiCustomListInfo ciCustomListInfo) {
        this.save(ciCustomListInfo);
    }

    public CiCustomListInfo selectById(String listTableName) {
        return (CiCustomListInfo)this.get(listTableName);
    }

    public void deleteCiCustomListInfoByCustomerId(String id) {
        this.batchExecute("delete from CiCustomListInfo where customGroupId = ?", new Object[]{id});
    }

    public List<CiCustomListInfo> selectAll() {
        String hql = " from CiCustomListInfo lst where exists(select 1 from CiCustomGroupInfo ginfo where  lst.customGroupId = ginfo.customGroupId and ginfo.status = 1) order by customGroupId,listTableName desc ";
        return super.find(hql, new Object[0]);
    }

    public void delete(String listTableName) {
        this.batchExecute("delete from CiCustomListInfo where listTableName = ?", new Object[]{listTableName});
    }

    public List<CiCustomListInfo> select(String hql, Object... values) {
        return super.find(hql, values);
    }

    public List<CiCustomListInfo> selectByCustomGroupIdAndBeforeDataDate(String customGroupId, String dataDate) {
        String hql = " from CiCustomListInfo where customGroupId = ? and dataDate < ? order by dataDate desc";
        return this.find(hql, new Object[]{customGroupId, dataDate});
    }
}
