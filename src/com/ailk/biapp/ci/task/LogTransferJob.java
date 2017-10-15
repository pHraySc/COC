package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.service.ILogTransferService;
import com.ailk.biapp.ci.util.DateUtil;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class LogTransferJob {
    private Logger log = Logger.getLogger(LogTransferJob.class);
    @Autowired
    private ILogTransferService logTransferService;

    public LogTransferJob() {
    }

    public void transportLogMain() throws Exception {
        String dateStr = DateUtil.date2String(new Date(), "yyyyMMdd");
        String preDate = DateUtil.getFrontDay(1, dateStr, "yyyyMMdd");
        this.log.debug("start transportLog......");
        String jndi = Configure.getInstance().getProperty("JNDI_WEBOS_CI");
        if(!StringUtil.isEmpty(jndi) && this.logTransferService.logTableIfExists(preDate)) {
            this.logTransferService.transportLogFromWebOS(preDate);
            this.logTransferService.logStatistics(preDate);
        }

        this.log.debug("end transportLog!");
    }

    public static void main(String[] args) {
        String date = "20140213";
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ci_log_overview ");
        sql.append("select data_date,\'").append("COC_CUSTOMER_ANALYSIS");
        sql.append("\' op_type_id,third_dept_id,");
        sql.append("third_dept_name,second_dept_id,second_dept_name, ");
        sql.append("sum(op_times) from ci_log_overview ");
        sql.append("WHERE data_date=\'").append(date);
        sql.append("\' and op_type_id in(\'");
        sql.append("COC_CUSTOMER_CONTRAST_ANALYSIS").append("\',");
        sql.append("\'");
        sql.append("COC_CUSTOMER_REL_ANALYSIS").append("\',\'");
        sql.append("COC_INDEX_DIFFERENTIAL");
        sql.append("\',\'").append("COC_CUSTOMER_LABEL_DIFFERENTIAL").append("\') ");
        sql.append("group by data_date,third_dept_id,third_dept_name,second_dept_id,second_dept_name ");
        System.out.println(sql.toString());
    }
}
