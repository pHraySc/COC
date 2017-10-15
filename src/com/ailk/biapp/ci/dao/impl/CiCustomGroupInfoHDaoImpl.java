package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiCustomGroupInfoHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomGroupPushCycle;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.DateUtil;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jfree.util.Log;
import org.springframework.stereotype.Repository;

@Repository
public class CiCustomGroupInfoHDaoImpl extends HibernateBaseDao<CiCustomGroupInfo, String> implements ICiCustomGroupInfoHDao {
    public CiCustomGroupInfoHDaoImpl() {
    }

    public CiCustomGroupInfo selectCustomGroupById(String ciCustomGroupInfoId) {
        CiCustomGroupInfo ciCustomGroupInfo = (CiCustomGroupInfo)this.get(ciCustomGroupInfoId);
        return ciCustomGroupInfo;
    }

    public void deleteCustomGroup(CiCustomGroupInfo ciCustomGroupInfo) {
        this.delete(ciCustomGroupInfo);
    }

    public void insertCustomGroup(CiCustomGroupInfo ciCustomGroupInfo) {
        this.save(ciCustomGroupInfo);
    }

    public List<CiCustomGroupInfo> selectCiCustomGroupInfoListByName(String customGroupName, String userId, String createCityId) {
        List list = null;
        String hql = "from CiCustomGroupInfo c where c.status = 1 and c.customGroupName = ? ";
        if(StringUtils.isNotEmpty(userId)) {
            hql = hql + "and ( c.createUserId = ? or c.isPrivate = " + 0 + " )";
            if(StringUtils.isNotEmpty(createCityId)) {
                hql = hql + " and c.createCityId = ? ";
                list = this.find(hql, new Object[]{customGroupName, userId, createCityId});
            } else {
                list = this.find(hql, new Object[]{customGroupName, userId});
            }
        } else {
            list = this.find(hql, new Object[]{customGroupName});
        }

        return list;
    }

    public List<CiCustomGroupInfo> selectCiCustomGroupInfo4CycleCreate(int updateCycle, Date dataDate, String cityIds) {
        StringBuffer updateHql = new StringBuffer();
        StringBuffer queryHql = new StringBuffer();
        StringBuffer whereHql = new StringBuffer();
        whereHql.append(" where status = ").append(1).append(" and createTypeId = 1").append(" and (startDate <= ? or startDate is null) and (endDate >= ? or endDate is null)").append(" and dataDate <= ? and dataStatus <> ").append(2).append(" and dataStatus <> ").append(1).append(" and isHasList <> ").append(0).append(" and ((listCreateTime is null or listCreateTime = \'\') or (listCreateTime is not null and customListCreateFailed = ").append(1).append("))").append(" and (isLabelOffline is null or isLabelOffline = ").append(0).append(")").append(" and (updateCycle = ? ");
        String dataDateStr = "";
        if(3 == updateCycle) {
            dataDateStr = DateUtil.date2String(dataDate, "yyyyMMdd");
            whereHql.append(" or (updateCycle <>3 and isFirstFailed = 1))");
        } else if(2 == updateCycle) {
            dataDateStr = DateUtil.date2String(dataDate, "yyyyMM");
            whereHql.append(")");
        }

        if(StringUtils.isNotEmpty(cityIds)) {
            whereHql.append(" and createCityId in (").append(cityIds).append(") ");
        }

        queryHql.append(" from CiCustomGroupInfo").append(whereHql);
        Log.debug("取需要跑的周期性客户群，dataDateStr：" + dataDateStr + "   sql:" + queryHql);
        Log.info("取需要跑的周期性客户群，dataDateStr：" + dataDateStr + "   sql:" + queryHql);
        List list = this.find(queryHql.toString(), new Object[]{dataDate, dataDate, dataDateStr, Integer.valueOf(updateCycle)});
        updateHql.append(" update CiCustomGroupInfo set dataStatus=?").append(whereHql);
        return list;
    }

    public List<CiCustomGroupInfo> selectCiCustomGroupInfoListByCustomGroupInfo(CiCustomGroupInfo customGroupInfo, String orderByProperty, int orderByType) {
        Criteria c = this.getSession().createCriteria(CiCustomGroupInfo.class);
        Example example = Example.create(customGroupInfo).excludeProperty("customGroupName");
        c.add(example);
        String name = customGroupInfo.getCustomGroupName() == null?null:customGroupInfo.getCustomGroupName().trim();
        if(StringUtils.isNotEmpty(name)) {
            c.add(Restrictions.sqlRestriction("CUSTOM_GROUP_NAME like ? escape \'|\'", "%" + CiUtil.escapeSQLLike(name) + "%", Hibernate.STRING));
        }

        if(StringUtils.isNotEmpty(orderByProperty)) {
            if(1 == orderByType) {
                c.addOrder(Order.asc(orderByProperty));
            } else if(2 == orderByType) {
                c.addOrder(Order.desc(orderByProperty));
            }
        }

        return c.list();
    }

    public int resetProductAutoMacthFlag() {
        String hql = " update CiCustomGroupInfo set productAutoMacthFlag = productAutoMacthFlag-2 where productAutoMacthFlag > 1 ";
        int count = super.batchExecute(hql, new Object[0]);
        return count;
    }

    public void insertCustomGroupPushCycle(CiCustomGroupPushCycle pushCycle) {
        this.getSessionFactory().getCurrentSession().saveOrUpdate(pushCycle);
    }

    public void deleteCiCustomGroupPushCycleByCustomGroupId(String customGroupId) {
        this.getSessionFactory().getCurrentSession().createQuery("delete from CiCustomGroupPushCycle a where a.id.customGroupId=?").setString(0, customGroupId).executeUpdate();
    }

    public void deleteCiCustomGroupPushCycle(String customGroupId, Integer pushCycle) {
        this.getSessionFactory().getCurrentSession().createQuery("delete from CiCustomGroupPushCycle a where a.id.customGroupId=? and a.pushCycle=?").setString(0, customGroupId).setInteger(1, pushCycle.intValue()).executeUpdate();
    }

    public List<CiCustomGroupPushCycle> selectushCycleByGroupId(String customGroupId) {
        String hql = "from CiCustomGroupPushCycle a where a.id.customGroupId=? and a.pushCycle!=1";
        List result = this.find(hql, new Object[]{customGroupId});
        return result;
    }

    public List<CiCustomGroupPushCycle> selectPushCycleByGroupId(String customGroupId) {
        String hql = "from CiCustomGroupPushCycle a where a.id.customGroupId=?";
        List result = this.find(hql, new Object[]{customGroupId});
        return result;
    }

    public List<CiCustomGroupInfo> selectCiCustomGroupInfoListByShareName(String customGroupName) {
        List list = null;
        StringBuffer hql = new StringBuffer();
        hql.append("from CiCustomGroupInfo c where c.status = ").append(1).append(" and c.customGroupName = ? ");
        list = this.find(hql.toString(), new Object[]{customGroupName});
        return list;
    }

    public List<CiCustomGroupInfo> selectDelCustomGroup(String customIds) {
        StringBuffer hql = new StringBuffer();
        hql.append("from CiCustomGroupInfo c where c.status = ").append(0).append(" and c.customGroupId in (").append(customIds).append(") ");
        return this.find(hql.toString(), new Object[0]);
    }

    public List<CiCustomGroupInfo> selectCiCustomGroupInfoByIsFirstFailed(Integer isFirstFailed) {
        StringBuffer hql = new StringBuffer();
        hql.append("from CiCustomGroupInfo c where c.status=").append(1).append(" and c.isFirstFailed=?");
        return this.find(hql.toString(), new Object[]{isFirstFailed});
    }

    public void updateAllCiCustomGroupIsFirstFailed(Integer customGroupIsFirstSuccess) {
        StringBuffer hql = new StringBuffer();
        hql.append("update CiCustomGroupInfo c set c.isFirstFailed=? where c.status=").append(1);
        this.batchExecute(hql.toString(), new Object[]{customGroupIsFirstSuccess});
    }

    public void updateCiCustomGroupIsFirstFailed(String customGroupId, Integer customGroupIsFirstSuccess) {
        String hql = "update CiCustomGroupInfo c set c.isFirstFailed=? where c.customGroupId=?";
        this.batchExecute(hql, new Object[]{customGroupIsFirstSuccess, customGroupId});
    }

    public List<CiCustomGroupInfo> selectPublicCustomGroup(String userId) {
        StringBuffer hql = new StringBuffer();
        hql.append("from CiCustomGroupInfo c where c.isPrivate=").append(0).append(" and c.createUserId<>?");
        return this.find(hql.toString(), new Object[]{userId});
    }
}
