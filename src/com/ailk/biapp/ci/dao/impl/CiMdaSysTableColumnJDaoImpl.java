package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiMdaSysTableColumnJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiMdaSysTableColumnJDaoImpl extends JdbcBaseDao implements ICiMdaSysTableColumnJDao {
    protected Logger log = Logger.getLogger(CiMdaSysTableColumnJDaoImpl.class);

    public CiMdaSysTableColumnJDaoImpl() {
    }

    public List<CiMdaSysTableColumn> selectCiMdaSysTableColumnList(Integer labelId) throws Exception {
        String sql = "select t1.* from CI_MDA_SYS_TABLE_COLUMN t1,CI_LABEL_EXT_INFO t2,CI_LABEL_VERTICAL_COLUMN_REL t3 where t1.column_id = t3.column_id and t2.label_id =t3.label_id and t2.label_id = ?";
        this.log.debug("labelId: " + labelId + " ,取得标签元数据列列表的sql：" + sql);
        List ciMdaSysTableColumnList = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiMdaSysTableColumn.class), new Object[]{labelId});
        this.log.debug("标签元数据列列表的size：" + ciMdaSysTableColumnList.size());
        return ciMdaSysTableColumnList;
    }
}
