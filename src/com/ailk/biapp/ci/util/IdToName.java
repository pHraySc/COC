package com.ailk.biapp.ci.util;

import com.ailk.biapp.ci.service.impl.CiLabelInfoServiceImpl;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;

public class IdToName {
    public IdToName() {
    }

    public static String getName(String cacheKey, Object id) {
        String name = "";
        CacheBase cb = CacheBase.getInstance();
        if(cb != null) {
            name = cb.getNameByKey(cacheKey, id);
            if(StringUtil.isEmpty(name)) {
                name = String.valueOf(id);
            }
        }

        return name;
    }

    public static String getLabelName(String cacheKey, Object id) {
        String name = "";
        CacheBase cb = CacheBase.getInstance();
        if(cb != null) {
            name = cb.getLabelNameByKey(cacheKey, id);
            if(StringUtil.isEmpty(name)) {
                name = String.valueOf(id);
            }
        }

        return name;
    }

    public static String getLabelName(Object id) {
        String name = "";
        CiLabelInfoServiceImpl ciLabelInfoService = null;

        try {
            ciLabelInfoService = (CiLabelInfoServiceImpl)SystemServiceLocator.getInstance().getService("ciLabelInfoServiceImpl");
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        if(id != null) {
            name = ciLabelInfoService.queryCiLabelInfoById(Integer.valueOf(Integer.parseInt((String)((String)id)))).getLabelName();
            if(StringUtil.isEmpty(name)) {
                name = String.valueOf(id);
            }
        }

        return name;
    }
}
