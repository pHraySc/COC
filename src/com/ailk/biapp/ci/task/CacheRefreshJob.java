package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.service.ILabelStatService;
import com.ailk.biapp.ci.util.cache.CacheBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheRefreshJob {
    @Autowired
    private ILabelStatService labelStatService;

    public CacheRefreshJob() {
    }

    public void refreshUserNoticeCache() {
        CacheBase.getInstance().clearUserCache();
        CacheBase.getInstance().refreshSysNoticeCache();
    }

    public void refreshLabelAndProductCache() {
        CacheBase cache = CacheBase.getInstance();
        cache.refreshNewestDayAndMonth();
        cache.refreshLabelAndProduct();
        String dataDate = cache.getNewLabelMonth();
        byte updateCycle = 2;
        this.labelStatService.updateLabelTotalCustomNum(dataDate, updateCycle);
    }
}
