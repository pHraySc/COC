package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.service.ICustomGroupStatService;
import com.ailk.biapp.ci.task.AbstractJob;
import com.ailk.biapp.ci.util.cache.CacheBase;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DailyCustomGroupStatJob extends AbstractJob {
	private Logger logger = Logger.getLogger(DailyCustomGroupStatJob.class);
	@Autowired
	private ICustomGroupStatService customGroupStatService;

	public DailyCustomGroupStatJob() {
	}

	public void work() {
		this.logger.info("DailyCustomGroupStatJob- start");
		String dataDate = CacheBase.getInstance().getNewLabelDay();
		if(!StringUtils.isEmpty(super.getDataTime())) {
			dataDate = super.getDataTime();
		}

		byte updateCycle = 1;
		int dataDateNum = this.customGroupStatService.dataDateExist(dataDate);
		if(dataDateNum <= 0) {
			this.customGroupStatService.statCustomGroupCustomNum(dataDate, updateCycle);
			this.customGroupStatService.statCustomGroupRingNum(dataDate, updateCycle);
		}

		this.logger.info("DailyCustomGroupStatJob- exit");
	}
}
