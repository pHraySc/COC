package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiUserCustomRelHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiUserCustomRel;
import com.ailk.biapp.ci.entity.CiUserCustomRelId;
import com.ailk.biapp.ci.util.DateUtil;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
public class CiUserCustomRelHDaoImpl extends HibernateBaseDao<CiUserCustomRel, String> implements ICiUserCustomRelHDao {
    public CiUserCustomRelHDaoImpl() {
    }

    public void insertCiUserCustomRel(String customId, String labelIds, String userId, String date) throws Exception {
        String[] labelIdArr = labelIds.split(",");
        String[] arr$ = labelIdArr;
        int len$ = labelIdArr.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String labelId = arr$[i$];
            CiUserCustomRel ciUserCustomRel = new CiUserCustomRel();
            ciUserCustomRel.setCreateTime(DateUtil.date2String(new Date()));
            ciUserCustomRel.setStatus(Integer.valueOf(1));
            CiUserCustomRelId id = new CiUserCustomRelId();
            id.setUserId(userId);
            id.setAssociLabelId(labelId);
            id.setCustomGroupId(customId);
            ciUserCustomRel.setId(id);
            this.save(ciUserCustomRel);
        }

    }

    public List<CiUserCustomRel> selectCiUserCustomRel(String customId, String userId) throws Exception {
        String hql = "from CiUserCustomRel WHERE c.status=1 and id.userId = :userId AND id.customGroupId = :customGroupId";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setString("userId", userId);
        query.setString("customGroupId", customId);
        return query.list();
    }
}
