package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiUserUseLabelHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiUserUseLabel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiUserUseLabelHDaoImpl extends HibernateBaseDao<CiUserUseLabel, String> implements ICiUserUseLabelHDao {
    public CiUserUseLabelHDaoImpl() {
    }

    public CiUserUseLabel selectById(String tableId) {
        CiUserUseLabel ciUserUseLabel = (CiUserUseLabel)this.get(tableId);
        return ciUserUseLabel;
    }

    public List<CiUserUseLabel> select() {
        List ciUserUseLabel = this.getAll();
        return ciUserUseLabel;
    }

    public void insertOrUpdateCiUserUseLabel(CiUserUseLabel bean) {
        this.save(bean);
    }
}
