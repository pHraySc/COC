
package com.ailk.biapp.ci.market.dao.impl;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.market.dao.ICiCustomLabelSceneRelHDao;
import com.ailk.biapp.ci.market.entity.CiCustomLabelSceneRel;
import com.ailk.biapp.ci.market.entity.CiMarketScene;
import com.ailk.biapp.ci.market.entity.CiMarketTask;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiCustomLabelSceneHDaoImpl extends HibernateBaseDao<CiCustomLabelSceneRel, String> implements ICiCustomLabelSceneRelHDao {
    public CiCustomLabelSceneHDaoImpl() {
    }

    public void deleteCustomLabelRel(CiCustomLabelSceneRel ciCustomLabelSceneRel) {
        this.delete(ciCustomLabelSceneRel);
    }

    public void insertCustomLabelRel(CiCustomLabelSceneRel ciCustomLabelSceneRel) {
        this.save(ciCustomLabelSceneRel);
    }

    public List<CiCustomLabelSceneRel> queryCustomLabelSceneRels(CiCustomLabelSceneRel customLabelSceneRel) {
        StringBuffer hql = new StringBuffer("from CiCustomLabelSceneRel t where t.sceneId = ? and t.marketTaskId = ?");
        if(ServiceConstants.RECOM_SHOW_TYPE_LABEL == customLabelSceneRel.getShowType().intValue()) {
            hql.append("and t.labelId = ? ");
            return this.find(hql.toString(), new Object[]{customLabelSceneRel.getSceneId(), customLabelSceneRel.getMarketTaskId(), customLabelSceneRel.getLabelId()});
        } else {
            hql.append("and t.customGroupId = ? ");
            return this.find(hql.toString(), new Object[]{customLabelSceneRel.getSceneId(), customLabelSceneRel.getMarketTaskId(), customLabelSceneRel.getCustomGroupId()});
        }
    }

    public List<CiCustomLabelSceneRel> queryCustomLabelSceneRelsByType(String customOrLabelId, Integer showType) {
        StringBuffer hql = new StringBuffer("from CiCustomLabelSceneRel t where ");
        if(ServiceConstants.RECOM_SHOW_TYPE_LABEL == showType.intValue()) {
            hql.append(" t.labelId = ? ");
            return this.find(hql.toString(), new Object[]{Integer.valueOf(customOrLabelId)});
        } else {
            hql.append(" t.customGroupId = ? ");
            return this.find(hql.toString(), new Object[]{customOrLabelId});
        }
    }

    public List<CiMarketScene> selectMarketScene() {
        String hql = "from CiMarketScene scene order by scene.sort_num";
        return this.find(hql, new Object[0]);
    }

    public List<CiMarketTask> queryCustomLabelSceneRelsById(CiCustomLabelSceneRel customLabelSceneRel) {
        StringBuffer hql = new StringBuffer("from CiCustomLabelSceneRel t where ");
        if(ServiceConstants.RECOM_SHOW_TYPE_LABEL == customLabelSceneRel.getShowType().intValue()) {
            hql.append(" t.labelId = ? ");
            return this.find(hql.toString(), new Object[]{customLabelSceneRel.getLabelId()});
        } else {
            hql.append(" t.customGroupId = ? ");
            return this.find(hql.toString(), new Object[]{customLabelSceneRel.getCustomGroupId()});
        }
    }
}
