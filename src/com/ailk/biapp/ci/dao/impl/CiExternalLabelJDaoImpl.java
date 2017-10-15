package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiExternalLabelJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.base.LabelInfo;
import com.ailk.biapp.ci.entity.base.SysInfo;
import com.asiainfo.biframe.utils.config.Configure;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiExternalLabelJDaoImpl extends JdbcBaseDao implements ICiExternalLabelJDao {
    protected Logger log = Logger.getLogger(CiExternalLabelJDaoImpl.class);

    public CiExternalLabelJDaoImpl() {
    }

    public List<LabelInfo> queryLabelCategoryList(SysInfo sysInfo) throws Exception {
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method queryLabelCategoryList dao start");
        String sql = "SELECT labelCate.CATEGORY_ID LABEL_ID,labelCate.CATEGORY_NAME LABEL_NAME,labelCate.PARENT_ID PARENT_ID from DIM_EXTERNAL_LABEL_CATEGORY labelCate where LOWER(SYS_ID)=?";
        List list = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(LabelInfo.class), new Object[]{sysInfo.getSysId().toLowerCase()});
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method queryLabelCategoryList dao end");
        return list;
    }

    public List<LabelInfo> queryLabelsByLeafCategoryId(SysInfo sysInfo, String labelCategoryId) throws Exception {
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method queryLabelsByCategoryId dao start");
        String sql = "SELECT label.label_id label_id,rel.EXTERNAL_LABEL_NAME label_Name,label.update_Cycle update_Cycle,label.label_type_id label_type_id, label.busi_legend busi_legend,label.apply_suggest apply_suggest,label.busi_caliber  busi_caliber,label.data_date data_date,0 is_Category,rel.CATEGORY_ID PARENT_ID FROM  ci_external_sys_label_rel rel JOIN  ci_label_info label ON rel.label_id = label.label_id AND rel.category_id=?";
        List list = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(LabelInfo.class), new Object[]{labelCategoryId});
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method queryLabelsByCategoryId dao end");
        return list;
    }

    public List<LabelInfo> queryLabelInfoList(SysInfo sysInfo) throws Exception {
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method queryLabelInfoList dao start");
        String sql = "SELECT label.label_id label_id,rel.EXTERNAL_LABEL_NAME label_Name,label.update_Cycle update_Cycle,label.label_type_id label_type_id, label.busi_legend busi_legend,label.apply_suggest apply_suggest,label.busi_caliber  busi_caliber,label.data_date data_date,0 is_Category,rel.CATEGORY_ID PARENT_ID FROM CI_LABEL_INFO label JOIN CI_EXTERNAL_SYS_LABEL_REL rel on(label.LABEL_ID = rel.LABEL_ID) JOIN DIM_EXTERNAL_LABEL_CATEGORY cate on(rel.CATEGORY_ID = cate.CATEGORY_ID) where LOWER(cate.SYS_ID) = ?";
        List list = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(LabelInfo.class), new Object[]{sysInfo.getSysId().toLowerCase()});
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method queryLabelInfoList dao end");
        return list;
    }

    public List<LabelInfo> queryLabelsByCategoryId(SysInfo sysInfo, String labelCategoryId) throws Exception {
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method queryLabelInfoList dao start");
        StringBuffer sql = new StringBuffer("SELECT label.label_id label_id,rel.EXTERNAL_LABEL_NAME label_Name,label.update_Cycle update_Cycle,label.label_type_id label_type_id, label.busi_legend busi_legend,label.apply_suggest apply_suggest,label.busi_caliber  busi_caliber,label.data_date data_date,0 is_Category,rel.CATEGORY_ID PARENT_ID FROM CI_LABEL_INFO label JOIN CI_EXTERNAL_SYS_LABEL_REL rel on(label.LABEL_ID = rel.LABEL_ID) JOIN (");
        String dbType = Configure.getInstance().getProperty("CI_DBTYPE");
        String cateSql = "";
        if("ORACLE".equalsIgnoreCase(dbType)) {
            cateSql = "SELECT cate.CATEGORY_ID, cate.PARENT_ID, cate.SYS_ID FROM DIM_EXTERNAL_LABEL_CATEGORY cate START WITH parent_id = ? AND LOWER(sys_id)= ? CONNECT BY PRIOR cate.CATEGORY_ID = parent_id";
        } else if("DB2".equalsIgnoreCase(dbType)) {
            cateSql = "WITH report(parent_id, category_id, sys_id) AS ( SELECT parent_id, category_id, sys_id FROM DIM_EXTERNAL_LABEL_CATEGORY WHERE parent_id = ? AND LOWER(sys_id)= ? UNION ALL SELECT b.parent_id, b.category_id, b.sys_id FROM report a, DIM_EXTERNAL_LABEL_CATEGORY b WHERE b.parent_id = a.category_id ) SELECT category_id FROM report";
        } else if("MYSQL".equalsIgnoreCase(dbType)) {
            ;
        }

        sql.append(cateSql);
        sql.append(") cate ON ( rel.CATEGORY_ID = cate.CATEGORY_ID)");
        List list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(LabelInfo.class), new Object[]{labelCategoryId, sysInfo.getSysId().toLowerCase()});
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method queryLabelInfoList dao end");
        return list;
    }
}
