package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.util.cache.CacheBase;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class DailyHotLabelAndCustomJob {
	private Logger logger = Logger.getLogger(DailyHotLabelAndCustomJob.class);

	public DailyHotLabelAndCustomJob() {
	}

	public void refreshHotLabelCustomList() {
		String dataDate = CacheBase.getInstance().getNewLabelMonth();
		this.logger.info("DailyHotLabelAndCusomJob- start " + dataDate);
		CacheBase cache = CacheBase.getInstance();
		cache.refreshHoLabelAndCustomDaily();
		this.logger.info("DailyHotLabelAndCusomJob- end " + dataDate);
	}
}
