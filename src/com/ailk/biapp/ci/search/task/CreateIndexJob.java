package com.ailk.biapp.ci.search.task;

import com.ailk.biapp.ci.search.service.ISearchService;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateIndexJob {
    private Logger logger = Logger.getLogger(CreateIndexJob.class);
    @Autowired
    private ISearchService searchService;

    public CreateIndexJob() {
    }

    public void createIndex() {
        this.logger.info("CreateIndexJob-start ");
        String searchByDBFlag = Configure.getInstance().getProperty("SEARCH_BY_DB_FLAG");
        if(StringUtil.isNotEmpty(searchByDBFlag) && "false".equalsIgnoreCase(searchByDBFlag)) {
            try {
                this.searchService.createAllLabelIndex();
            } catch (Exception var3) {
                this.logger.error("创建索引出错", var3);
                var3.printStackTrace();
            }
        }

        this.logger.info("CreateIndexJob-end ");
    }
}
