
package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiExternalSysLabelRel;
import com.ailk.biapp.ci.service.base.ILabelInfoService;
import java.util.List;

public interface ILabelInfoCacheService extends ILabelInfoService {
    List<CiExternalSysLabelRel> queryExternalSysLabelRel() throws Exception;
}
