package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiUserAttentionCustomHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiUserAttentionCustom;
import com.ailk.biapp.ci.entity.CiUserAttentionCustomId;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiUserAttentionCustomHDaoImpl extends HibernateBaseDao<CiUserAttentionCustom, CiUserAttentionCustomId> implements ICiUserAttentionCustomHDao {
    public CiUserAttentionCustomHDaoImpl() {
    }

    public CiUserAttentionCustom selectById(CiUserAttentionCustomId id) {
        CiUserAttentionCustom ciUserAttentionCustom = (CiUserAttentionCustom)this.get(id);
        return ciUserAttentionCustom;
    }

    public List<CiUserAttentionCustom> select() {
        List ciUserAttentionCustomList = this.getAll();
        return ciUserAttentionCustomList;
    }

    public void insertOrUpdateCiUserAttentionCustom(CiUserAttentionCustom bean) {
        this.save(bean);
    }

    public void deleteCiUserAttentionCustom(CiUserAttentionCustom bean) {
        this.delete(bean);
    }
}
