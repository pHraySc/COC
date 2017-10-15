package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiUserAttentionLabelHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiUserAttentionLabel;
import com.ailk.biapp.ci.entity.CiUserAttentionLabelId;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiUserAttentionLabelHDaoImpl extends HibernateBaseDao<CiUserAttentionLabel, CiUserAttentionLabelId> implements ICiUserAttentionLabelHDao {
    public CiUserAttentionLabelHDaoImpl() {
    }

    public CiUserAttentionLabel selectById(CiUserAttentionLabelId tableId) {
        CiUserAttentionLabel ciUserAttentionLabel = (CiUserAttentionLabel)this.get(tableId);
        return ciUserAttentionLabel;
    }

    public List<CiUserAttentionLabel> select() {
        List ciUserAttentionLabel = this.getAll();
        return ciUserAttentionLabel;
    }

    public void insertOrUpdateCiUserAttentionLabel(CiUserAttentionLabel bean) {
        this.save(bean);
    }

    public void deleteCiUserAttentionLabel(CiUserAttentionLabel bean) {
        this.delete(bean);
    }
}
