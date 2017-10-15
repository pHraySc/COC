package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiCustomSceneHDao;
import com.ailk.biapp.ci.entity.CiCustomSceneRel;
import com.ailk.biapp.ci.service.ICiCustomSceneService;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiCustomSceneServiceImpl implements ICiCustomSceneService {
    private Logger log = Logger.getLogger(CiCustomSceneServiceImpl.class);
    @Autowired
    private ICiCustomSceneHDao ciCustomSceneHDao;

    public CiCustomSceneServiceImpl() {
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiCustomSceneRel> queryCustomScenesListByCustomId(String customGroupId) {
        List sceneRelList = null;

        try {
            sceneRelList = this.ciCustomSceneHDao.getCustomScenesByCustomId(customGroupId);
        } catch (Exception var4) {
            this.log.error("根据客户群ID查询场景列表失败！", var4);
        }

        return sceneRelList;
    }
}
