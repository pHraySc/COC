package com.ailk.biapp.ci.market.task;

import com.ailk.biapp.ci.market.service.ICiMarketService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomLabelRereshJob {
    private Logger log = Logger.getLogger(CustomLabelRereshJob.class);
    @Autowired
    private ICiMarketService marketService;

    public CustomLabelRereshJob() {
    }

    public void refreshCutomLabelSceneInfo() {
        this.log.debug("Enter CustomLabelRereshJob refreshCutomLabelSceneInfo");
        this.marketService.updateLabelScoreAndTimesAndTime();
        this.marketService.updateCustomScoreAndTimesAndTime();
        this.log.debug("Exit CustomLabelRereshJob refreshCutomLabelSceneInfo");
    }
}
