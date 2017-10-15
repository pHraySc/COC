package com.ailk.biapp.ci.ia.service.impl;

import com.ailk.biapp.ci.dao.ICiCustomListInfoHDao;
import com.ailk.biapp.ci.dao.ICiGroupAttrRelJDao;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.ia.dao.ICiIaAnalyseAttrHDao;
import com.ailk.biapp.ci.ia.dao.ICiIaAnalyseAttrJDao;
import com.ailk.biapp.ci.ia.entity.CiIaAnalyseAttr;
import com.ailk.biapp.ci.ia.model.AttributeModel;
import com.ailk.biapp.ci.ia.service.IAnalysisAttrService;
import com.ailk.biapp.ci.ia.utils.Cache;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("analysisAttrService")
public class AnalysisAttrServiceImpl implements IAnalysisAttrService {
    private Logger log = Logger.getLogger(AnalysisAttrServiceImpl.class);
    @Autowired
    private ICiIaAnalyseAttrHDao ciIaAnalyseAttrHDao;
    @Autowired
    private ICiCustomListInfoHDao ciCustomListInfoHDao;
    @Autowired
    private ICiGroupAttrRelJDao ciGroupAttrRelJDao;
    @Autowired
    private ICiIaAnalyseAttrJDao ciIaAnalyseAttrJDao;

    public AnalysisAttrServiceImpl() {
    }

    public List<CiIaAnalyseAttr> selectEffectiveAttrBySource(int source) {
        return this.ciIaAnalyseAttrHDao.selectEffectiveAttrBySource(source);
    }

    public List<AttributeModel> getPublicAttr() {
        ArrayList attrList = new ArrayList();
        CopyOnWriteArrayList cacheList = Cache.getInstance().getObjectList("ANALYSE_ATTR");
        Iterator i$ = cacheList.iterator();

        while(i$.hasNext()) {
            CiIaAnalyseAttr attr = (CiIaAnalyseAttr)i$.next();
            AttributeModel model = new AttributeModel();
            model.setAttrId(attr.getAttrId().toString());
            model.setAttrName(attr.getAttrName().trim());
            model.setGuidelineTitle(attr.getGuidelineTitle());
            model.setGuidelineUnit(attr.getGuidelineUnit());
            model.setGuidelineMask(attr.getGuidelineMask());
            model.setAttrType(attr.getAttrType());
            model.setAttrSource(attr.getAttrSource());
            model.setIscalculate(attr.getIsCalculate());
            CiLabelInfo label = Cache.getInstance().getLabelInfo(attr.getAttrId().intValue());
            int labelTypeId = label.getLabelTypeId().intValue();
            model.setLabelTypeId(Integer.valueOf(labelTypeId));
            attrList.add(model);
        }

        return attrList;
    }

    public List<AttributeModel> getCustomAttr(String listTableName) {
        CiCustomListInfo listInfo = this.ciCustomListInfoHDao.selectById(listTableName);
        String groupId = listInfo.getCustomGroupId();
        Date createTime = listInfo.getDataTime();
        List attrList = null;

        try {
            attrList = this.ciGroupAttrRelJDao.selectCiGroupAttrRelList(groupId, createTime);
        } catch (Exception var11) {
            this.log.error("查询客户群属性表错误！", var11);
        }

        ArrayList resultAttrs = new ArrayList();
        if(attrList != null && attrList.size() > 0 && (attrList.size() != 1 || attrList.get(0) == null || ((CiGroupAttrRel)attrList.get(0)).getStatus() == null || ((CiGroupAttrRel)attrList.get(0)).getStatus().intValue() != 2)) {
            Iterator i$ = attrList.iterator();

            while(i$.hasNext()) {
                CiGroupAttrRel rel = (CiGroupAttrRel)i$.next();
                int attrType = this.getCustomAttrType(rel.getAttrColType(), listTableName, rel.getId().getAttrCol());
                AttributeModel model = new AttributeModel();
                model.setAttrId(rel.getId().getAttrCol());
                model.setAttrName(rel.getAttrColName());
                model.setAttrType(Integer.valueOf(attrType));
                model.setAttrSource(Integer.valueOf(2));
                model.setIscalculate(Integer.valueOf(2));
                resultAttrs.add(model);
            }
        }

        return resultAttrs;
    }

    private int getCustomAttrType(String colType, String listTableName, String attrCol) {
        if(colType.toUpperCase().contains("VARCHAR")) {
            return 2;
        } else {
            int listTableCount = this.ciIaAnalyseAttrJDao.selectListTableCount(listTableName);
            int label01Count = this.ciIaAnalyseAttrJDao.select01LabelCount(listTableName, attrCol);
            return listTableCount == label01Count?2:1;
        }
    }
}
