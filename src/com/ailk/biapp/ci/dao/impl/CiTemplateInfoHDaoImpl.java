package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiTemplateInfoHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiTemplateInfo;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiTemplateInfoHDaoImpl extends HibernateBaseDao<CiTemplateInfo, String> implements ICiTemplateInfoHDao {
    public CiTemplateInfoHDaoImpl() {
    }

    public CiTemplateInfo selectTemplateInfoById(String TemplateId) {
        CiTemplateInfo ciTemplateInfo = (CiTemplateInfo)this.get(TemplateId);
        return ciTemplateInfo;
    }

    public void deleteTemplateInfo(CiTemplateInfo ciTemplateInfo) {
        this.delete(ciTemplateInfo);
    }

    public void insertTemplateInfo(CiTemplateInfo ciTemplateInfo) {
        this.save(ciTemplateInfo);
    }

    public List<CiTemplateInfo> selectTemplateInfoListByName(String templateName, String userId) {
        String hql = "from CiTemplateInfo t where t.status = 1 and t.templateName = ? and (t.userId = ? or t.isPrivate = 0 )";
        List list = this.find(hql, new Object[]{templateName, userId});
        return list;
    }

    public List<CiTemplateInfo> selectTemplateInfoListByTemplateName(String templateName) {
        String hql = "from CiTemplateInfo t where t.status = 1 and t.templateName = ? ";
        List list = this.find(hql, new Object[]{templateName});
        return list;
    }
}
