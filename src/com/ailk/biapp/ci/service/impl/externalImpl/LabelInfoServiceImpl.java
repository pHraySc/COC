package com.ailk.biapp.ci.service.impl.externalImpl;

import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.dao.ICiLabelInfoJDao;
import com.ailk.biapp.ci.entity.CiExternalSysLabelRel;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.base.EnumValue;
import com.ailk.biapp.ci.entity.base.LabelInfo;
import com.ailk.biapp.ci.entity.base.SysInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.service.base.ILabelInfoService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.dimtable.model.DimTableDefine;
import com.asiainfo.biframe.dimtable.service.impl.DimTableServiceImpl;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LabelInfoServiceImpl implements ILabelInfoService {
    private final Logger log = Logger.getLogger(LabelInfoServiceImpl.class);
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;
    @Autowired
    private ICiLabelInfoJDao ciLabelInfoJDao;

    public LabelInfoServiceImpl() {
    }

    public List<LabelInfo> queryLabelCategoryInfo(SysInfo sysInfo) throws Exception {
        this.log.info(" =============>>>> invoke method queryLabelCategoryInfo start");
        this.log.info(JsonUtil.toJson(sysInfo));
        CopyOnWriteArrayList labelInfos = null;

        try {
            labelInfos = CacheBase.getInstance().getObjectList(sysInfo.getSysId().toLowerCase() + "_LABEL_CATEGORY_LIST");
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_LABEL_CATEG", "", "获得全部标签分类", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "获得所有标签分类成功", OperResultEnum.Success, LogLevelEnum.Medium);
            this.log.info("查询标签分类信息成功-----------------size：" + labelInfos == null?"null":Integer.valueOf(labelInfos.size()));
            this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method queryLabelCategoryInfo end");
            return labelInfos;
        } catch (Exception var4) {
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_LABEL_CATEG", "", "获得全部标签分类", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "获得所有标签分类失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            this.log.error(var4);
            this.log.error("获得所有标签分类失败");
            throw new CIServiceException("获得所有标签分类失败");
        }
    }

    public List<LabelInfo> queryLabelInfo(SysInfo sysInfo, String labelCategoryId) throws Exception {
        this.log.info(" =============>>>>  invoke method queryLabelInfo start");
        this.log.info(JsonUtil.toJson(sysInfo));
        this.log.info("labelCategoryId:" + labelCategoryId);
        ArrayList labelInfos = new ArrayList();

        try {
            CopyOnWriteArrayList e = CacheBase.getInstance().getObjectList(sysInfo.getSysId().toLowerCase() + "_LABEL_CATEGORY_LIST");
            CopyOnWriteArrayList externalLabelRelList = CacheBase.getInstance().getObjectList(sysInfo.getSysId().toLowerCase() + "_SYS_LABEL_REL_LIST");
            List sysLabelCategoryList = this.getSubCategoryList(e, labelCategoryId);
            Iterator e1;
            if(null != sysLabelCategoryList && sysLabelCategoryList.size() != 0) {
                e1 = sysLabelCategoryList.iterator();

                while(e1.hasNext()) {
                    LabelInfo ciExternalSysLabelRel1 = (LabelInfo)e1.next();
                    Iterator labelInfoTmp2 = externalLabelRelList.iterator();

                    while(labelInfoTmp2.hasNext()) {
                        CiExternalSysLabelRel ciLabelInfo2 = (CiExternalSysLabelRel)labelInfoTmp2.next();
                        if(ciExternalSysLabelRel1.getLabelId().equals(ciLabelInfo2.getId().getCategoryId())) {
                            LabelInfo labelInfoTmp1 = new LabelInfo();
                            CiLabelInfo ciLabelInfo1 = CacheBase.getInstance().getEffectiveLabel(ciLabelInfo2.getId().getLabelId());
                            labelInfoTmp1.setApplySuggest(ciLabelInfo1.getApplySuggest());
                            labelInfoTmp1.setBusiCaliber(ciLabelInfo1.getBusiCaliber());
                            labelInfoTmp1.setBusiLegend(ciLabelInfo1.getBusiLegend());
                            labelInfoTmp1.setIsCategory(0);
                            labelInfoTmp1.setLabelId(String.valueOf(ciLabelInfo1.getLabelId()));
                            labelInfoTmp1.setLabelName(ciLabelInfo2.getExternalLabelName());
                            labelInfoTmp1.setLabelTypeId(ciLabelInfo1.getLabelTypeId());
                            labelInfoTmp1.setParentId(ciLabelInfo2.getId().getCategoryId());
                            labelInfoTmp1.setUpdateCycle(ciLabelInfo1.getUpdateCycle());
                            labelInfoTmp1.setDataDate(ciLabelInfo1.getDataDate());
                            labelInfos.add(labelInfoTmp1);
                        }
                    }
                }
            } else {
                e1 = externalLabelRelList.iterator();

                while(e1.hasNext()) {
                    CiExternalSysLabelRel ciExternalSysLabelRel = (CiExternalSysLabelRel)e1.next();
                    if(labelCategoryId != null && labelCategoryId.equals(ciExternalSysLabelRel.getId().getCategoryId())) {
                        LabelInfo labelInfoTmp = new LabelInfo();
                        CiLabelInfo ciLabelInfo = CacheBase.getInstance().getEffectiveLabel(ciExternalSysLabelRel.getId().getLabelId());
                        labelInfoTmp.setApplySuggest(ciLabelInfo.getApplySuggest());
                        labelInfoTmp.setBusiCaliber(ciLabelInfo.getBusiCaliber());
                        labelInfoTmp.setBusiLegend(ciLabelInfo.getBusiLegend());
                        labelInfoTmp.setIsCategory(0);
                        labelInfoTmp.setLabelId(String.valueOf(ciLabelInfo.getLabelId()));
                        labelInfoTmp.setLabelName(ciExternalSysLabelRel.getExternalLabelName());
                        labelInfoTmp.setLabelTypeId(ciLabelInfo.getLabelTypeId());
                        labelInfoTmp.setParentId(ciExternalSysLabelRel.getId().getCategoryId());
                        labelInfoTmp.setUpdateCycle(ciLabelInfo.getUpdateCycle());
                        labelInfoTmp.setDataDate(ciLabelInfo.getDataDate());
                        labelInfos.add(labelInfoTmp);
                    }
                }
            }

            if("true".equalsIgnoreCase(Configure.getInstance().getProperty("EXTERNAL_LABEL_ENUM_VALUE"))) {
                try {
                    this.encapsulationlabelInfo(labelInfos);
                } catch (Exception var13) {
                    CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_CHILD_LABEL", "", "查询标签分类下的子标签列表", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "获得标签分类下的普通标签列表失败", OperResultEnum.Failure, LogLevelEnum.Medium);
                    this.log.error("给枚举标签存上该标签对应维表里的ID和名称失败");
                    this.log.error(var13);
                    throw new CIServiceException("根据标签分类ID：" + labelCategoryId + "失败");
                }
            }
        } catch (Exception var14) {
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_CHILD_LABEL", "", "查询标签分类下的子标签列表", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "获得标签分类下的普通标签列表失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            this.log.error(var14);
            throw new CIServiceException("查询所有指定标签分类的标签信息失败");
        }

        CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_CHILD_LABEL", "", "查询标签分类下的子标签列表", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "获得标签分类下的普通标签列表成功", OperResultEnum.Success, LogLevelEnum.Medium);
        this.log.info("查询所有指定标签分类的标签信息成功,labelCategoryId：" + labelCategoryId + "-----------------size：" + labelInfos == null?"null":Integer.valueOf(labelInfos.size()));
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method queryLabelInfo end");
        return labelInfos;
    }

    private List<LabelInfo> getSubCategoryList(List<LabelInfo> categoryList, String categoryId) {
        ArrayList labelInfoList = null;
        if(categoryList != null) {
            labelInfoList = new ArrayList();
            Iterator iterator = categoryList.iterator();

            while(iterator.hasNext()) {
                LabelInfo labelInfo = (LabelInfo)iterator.next();
                if(labelInfo.getParentId().equals(categoryId)) {
                    labelInfoList.add(labelInfo);
                    labelInfoList.addAll(this.getSubCategoryList(categoryList, labelInfo.getLabelId()));
                }
            }
        }

        return labelInfoList;
    }

    public List<LabelInfo> queryLabelInfo(SysInfo sysInfo) throws Exception {
        this.log.info("=============>>>>  invoke method queryLabelInfo start");
        this.log.info(JsonUtil.toJson(sysInfo));
        CopyOnWriteArrayList labelInfos = null;

        try {
            labelInfos = CacheBase.getInstance().getObjectList(sysInfo.getSysId().toLowerCase() + "_ALL_LABEL_LIST");
        } catch (Exception var4) {
            this.log.error("获得全量标签失败！");
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_LABEL", "", "查询标签列表", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "获得标签列表失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            throw new CIServiceException("获得全量标签失败！");
        }

        CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_LABEL", "", "查询标签列表", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "获得标签列表成功", OperResultEnum.Success, LogLevelEnum.Medium);
        this.log.info("查询所有标签及标签分类信息成功-------------size：" + labelInfos == null?"null":Integer.valueOf(labelInfos.size()));
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method queryLabelInfo end");
        return labelInfos;
    }

    public List<EnumValue> queryEnumValueList(SysInfo sysInfo, String labelId, Integer currPage, Integer pageSize) throws Exception {
        this.log.info("=============>>>>  invoke method queryEnumValueList start");
        this.log.info(JsonUtil.toJson(sysInfo));
        this.log.info("labelId:" + labelId);
        List list = null;
        ArrayList enumValueList = new ArrayList();
        Pager pager = new Pager();

        try {
            if(!StringUtil.isEmpty(labelId)) {
                String e = sysInfo.getUserId();
                long count = this.ciLabelInfoService.queryLabelDimValueCount(e, Integer.valueOf(labelId), (String)null, (String)null);
                pager.setTotalSize(count);
                pager.setPageSize(pageSize.intValue());
                pager.setPageNum(currPage.intValue());
                list = this.ciLabelInfoService.queryLabelDimValue(Integer.valueOf(labelId), pager, (String)null, e, (String)null);
                if(list != null) {
                    Iterator i$ = list.iterator();

                    while(i$.hasNext()) {
                        Map map = (Map)i$.next();
                        EnumValue enumValue = new EnumValue();
                        enumValue.setId(map.get("V_KEY").toString());
                        enumValue.setName(map.get("V_NAME").toString());
                        enumValueList.add(enumValue);
                    }
                }
            }
        } catch (Exception var14) {
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_ENUM_VALUE_LIST", "", "查询枚举标签", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "分页查询枚举所有标签失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            this.log.error("分页查询枚举所有标签异常");
            this.log.error(var14);
            throw new CIServiceException("分页查询枚举所有标签异常");
        }

        CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_ENUM_VALUE_LIST", "", "查询枚举标签", sysInfo.getSysId() + sysInfo.getSysName() + "用户" + sysInfo.getUserId() + "分页查询枚举所有标签成功", OperResultEnum.Success, LogLevelEnum.Medium);
        this.log.info("分页查询单个标签的枚举值-------------size：" + enumValueList.size());
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method queryLabelInfo end");
        return enumValueList;
    }

    private void encapsulationlabelInfo(List<LabelInfo> labelInfos) throws Exception {
        Iterator i$ = labelInfos.iterator();

        while(true) {
            LabelInfo labelInfo;
            do {
                do {
                    if(!i$.hasNext()) {
                        return;
                    }

                    labelInfo = (LabelInfo)i$.next();
                } while(labelInfo.getLabelTypeId() == null);
            } while(5 != labelInfo.getLabelTypeId().intValue());

            ArrayList enumValueList = new ArrayList();
            DimTableServiceImpl service = (DimTableServiceImpl)SystemServiceLocator.getInstance().getService("dimTable_serviceTarget");
            String transDimId = this.ciLabelInfoJDao.selectDimTransIdByLabelId(Integer.valueOf(labelInfo.getLabelId()));
            DimTableDefine define = service.findDefineById(transDimId);
            List dimTableVal = this.ciLabelInfoJDao.getAllDimDataByDefine(define, (String)null);
            if(dimTableVal != null) {
                Iterator i$1 = dimTableVal.iterator();

                while(i$1.hasNext()) {
                    Map map = (Map)i$1.next();
                    EnumValue enumValue = new EnumValue();
                    enumValue.setId(map.get("V_KEY").toString());
                    enumValue.setName(map.get("V_NAME").toString());
                    enumValueList.add(enumValue);
                }
            }

            labelInfo.setEnumValueList(enumValueList);
            this.log.info(labelInfo.getLabelName() + labelInfo.getLabelId() + "----枚举值列表size:" + enumValueList.size());
        }
    }
}
