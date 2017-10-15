package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.service.ICiCustomUserUseService;
import com.ailk.biapp.ci.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomUseStatJob {
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private ICiCustomUserUseService ciCustomUserUseService;

	public CustomUseStatJob() {
	}

	public void monthCount() {
		this.log.info("CustomUseStatJob.monthCount() Method Start");
		String dataDate = DateUtil.getLastMonthStr();
		this.log.info("CustomUseStatJob Month Count DataDate=" + dataDate);
		this.ciCustomUserUseService.insertCiCustomMonthUseStat(dataDate);
		this.log.info("CustomUseStatJob.monthCount() Method End");
	}
}
