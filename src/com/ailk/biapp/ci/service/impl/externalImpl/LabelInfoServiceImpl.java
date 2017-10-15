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
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_LABEL_CATEG", "", "���ȫ����ǩ����", sysInfo.getSysId() + sysInfo.getSysName() + "�û�" + sysInfo.getUserId() + "������б�ǩ����ɹ�", OperResultEnum.Success, LogLevelEnum.Medium);
            this.log.info("��ѯ��ǩ������Ϣ�ɹ�-----------------size��" + labelInfos == null?"null":Integer.valueOf(labelInfos.size()));
            this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " invoke method queryLabelCategoryInfo end");
            return labelInfos;
        } catch (Exception var4) {
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_LABEL_CATEG", "", "���ȫ����ǩ����", sysInfo.getSysId() + sysInfo.getSysName() + "�û�" + sysInfo.getUserId() + "������б�ǩ����ʧ��", OperResultEnum.Failure, LogLevelEnum.Medium);
            this.log.error(var4);
            this.log.error("������б�ǩ����ʧ��");
            throw new CIServiceException("������б�ǩ����ʧ��");
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
                    CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_CHILD_LABEL", "", "��ѯ��ǩ�����µ��ӱ�ǩ�б�", sysInfo.getSysId() + sysInfo.getSysName() + "�û�" + sysInfo.getUserId() + "��ñ�ǩ�����µ���ͨ��ǩ�б�ʧ��", OperResultEnum.Failure, LogLevelEnum.Medium);
                    this.log.error("��ö�ٱ�ǩ���ϸñ�ǩ��Ӧά�����ID������ʧ��");
                    this.log.error(var13);
                    throw new CIServiceException("���ݱ�ǩ����ID��" + labelCategoryId + "ʧ��");
                }
            }
        } catch (Exception var14) {
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_CHILD_LABEL", "", "��ѯ��ǩ�����µ��ӱ�ǩ�б�", sysInfo.getSysId() + sysInfo.getSysName() + "�û�" + sysInfo.getUserId() + "��ñ�ǩ�����µ���ͨ��ǩ�б�ʧ��", OperResultEnum.Failure, LogLevelEnum.Medium);
            this.log.error(var14);
            throw new CIServiceException("��ѯ����ָ����ǩ����ı�ǩ��Ϣʧ��");
        }

        CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_CHILD_LABEL", "", "��ѯ��ǩ�����µ��ӱ�ǩ�б�", sysInfo.getSysId() + sysInfo.getSysName() + "�û�" + sysInfo.getUserId() + "��ñ�ǩ�����µ���ͨ��ǩ�б�ɹ�", OperResultEnum.Success, LogLevelEnum.Medium);
        this.log.info("��ѯ����ָ����ǩ����ı�ǩ��Ϣ�ɹ�,labelCategoryId��" + labelCategoryId + "-----------------size��" + labelInfos == null?"null":Integer.valueOf(labelInfos.size()));
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
            this.log.error("���ȫ����ǩʧ�ܣ�");
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_LABEL", "", "��ѯ��ǩ�б�", sysInfo.getSysId() + sysInfo.getSysName() + "�û�" + sysInfo.getUserId() + "��ñ�ǩ�б�ʧ��", OperResultEnum.Failure, LogLevelEnum.Medium);
            throw new CIServiceException("���ȫ����ǩʧ�ܣ�");
        }

        CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_LABEL", "", "��ѯ��ǩ�б�", sysInfo.getSysId() + sysInfo.getSysName() + "�û�" + sysInfo.getUserId() + "��ñ�ǩ�б�ɹ�", OperResultEnum.Success, LogLevelEnum.Medium);
        this.log.info("��ѯ���б�ǩ����ǩ������Ϣ�ɹ�-------------size��" + labelInfos == null?"null":Integer.valueOf(labelInfos.size()));
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
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_ENUM_VALUE_LIST", "", "��ѯö�ٱ�ǩ", sysInfo.getSysId() + sysInfo.getSysName() + "�û�" + sysInfo.getUserId() + "��ҳ��ѯö�����б�ǩʧ��", OperResultEnum.Failure, LogLevelEnum.Medium);
            this.log.error("��ҳ��ѯö�����б�ǩ�쳣");
            this.log.error(var14);
            throw new CIServiceException("��ҳ��ѯö�����б�ǩ�쳣");
        }

        CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_ENUM_VALUE_LIST", "", "��ѯö�ٱ�ǩ", sysInfo.getSysId() + sysInfo.getSysName() + "�û�" + sysInfo.getUserId() + "��ҳ��ѯö�����б�ǩ�ɹ�", OperResultEnum.Success, LogLevelEnum.Medium);
        this.log.info("��ҳ��ѯ������ǩ��ö��ֵ-------------size��" + enumValueList.size());
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
            this.log.info(labelInfo.getLabelName() + labelInfo.getLabelId() + "----ö��ֵ�б�size:" + enumValueList.size());
        }
    }
}
