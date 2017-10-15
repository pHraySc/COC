package com.ailk.biapp.ci.util;

import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class SequenceUtil extends JdbcBaseDao {
    private Logger log = Logger.getLogger(this.getClass());

    public SequenceUtil() {
    }

    public Long getNextValBySeqName(String seqName) {
        String sql = "";
        long nextVal = 0L;

        try {
            sql = this.getDataBaseAdapter().getSequenceSql(seqName);
            this.log.debug("getNextValFromCiCustomGroupInfoSeq sql : " + sql);
            nextVal = this.getSimpleJdbcTemplate().queryForLong(sql, new Object[0]);
        } catch (Exception var6) {
            this.log.error("≤È—Ø–Ú¡–¥ÌŒÛ", var6);
        }

        return Long.valueOf(nextVal);
    }
}
