package com.ailk.biapp.ci.service.impl.externalImpl;

import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.dao.ICiExternalLabelHDao;
import com.ailk.biapp.ci.dao.ICiExternalLabelJDao;
import com.ailk.biapp.ci.dao.ICiLabelInfoJDao;
import com.ailk.biapp.ci.entity.CiExternalSysLabelRel;
import com.ailk.biapp.ci.entity.base.EnumValue;
import com.ailk.biapp.ci.entity.base.LabelInfo;
import com.ailk.biapp.ci.entity.base.SysInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.service.ILabelInfoCacheService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.JsonUtil;
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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LabelInfoCacheServiceImpl implements ILabelInfoCacheService {
    private final Logger log = Logger.getLogger(LabelInfoCacheServiceImpl.class);
    @Autowired
    private ICiExternalLabelJDao ciExternalLabelJDao;
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;
    @Autowired
    private ICiLabelInfoJDao ciLabelInfoJDao;
    @Autowired
    private ICiExternalLabelHDao ciExternalLabelHDao;

    public LabelInfoCacheServiceImpl() {
    }

    public List<LabelInfo> queryLabelCategoryInfo(SysInfo sysInfo) throws Exception {
        this.log.info(" ========================>>>> init cache ��ѯ�����ǩ�б� start");
        this.log.info(JsonUtil.toJson(sysInfo));
        List labelInfos = null;

        try {
            labelInfos = this.ciExternalLabelJDao.queryLabelCategoryList(sysInfo);
        } catch (Exception var4) {
            this.log.error(var4);
            this.log.error("������б�ǩ����ʧ��");
            throw new CIServiceException("������б�ǩ����ʧ��");
        }

        this.log.info(" ========================>>>>labelInfos size:" + labelInfos.size() + " init cache ��ѯ�����ǩ�б� end");
        return labelInfos;
    }

    public List<LabelInfo> queryLabelInfo(SysInfo sysInfo, String labelCategoryId) throws Exception {
        this.log.info(" ========================>>>> init cache ��ѯ�ӱ�ǩ�б� start");
        this.log.info(JsonUtil.toJson(sysInfo));
        this.log.info("labelCategoryId:" + labelCategoryId);
        List labelInfos = null;

        try {
            labelInfos = this.ciExternalLabelJDao.queryLabelsByLeafCategoryId(sysInfo, labelCategoryId);
        } catch (Exception var7) {
            this.log.error("���ݱ�ǩ����ID��" + labelCategoryId + "ʧ��");
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_CHILD_LABEL", "", "��ѯ��ǩ�����µ��ӱ�ǩ�б�", sysInfo.getSysId() + sysInfo.getSysName() + "�û�" + sysInfo.getUserId() + "��ñ�ǩ�����µ���ͨ��ǩ�б�ʧ��", OperResultEnum.Failure, LogLevelEnum.Medium);
            throw new CIServiceException("���ݱ�ǩ����ID��" + labelCategoryId + "ʧ��");
        }

        if(labelInfos == null || labelInfos.size() == 0) {
            try {
                labelInfos = this.ciExternalLabelJDao.queryLabelsByCategoryId(sysInfo, labelCategoryId);
            } catch (Exception var6) {
                this.log.error("���ݱ�ǩ����ID��" + labelCategoryId + "ʧ��");
                CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_CHILD_LABEL", "", "��ѯ��ǩ�����µ��ӱ�ǩ�б�", sysInfo.getSysId() + sysInfo.getSysName() + "�û�" + sysInfo.getUserId() + "��ñ�ǩ�����µ���ͨ��ǩ�б�ʧ��", OperResultEnum.Failure, LogLevelEnum.Medium);
                this.log.error(var6);
                throw new CIServiceException("���ݱ�ǩ����ID��" + labelCategoryId + "ʧ��");
            }
        }

        if("true".equalsIgnoreCase(Configure.getInstance().getProperty("EXTERNAL_LABEL_ENUM_VALUE"))) {
            try {
                this.encapsulationlabelInfo(labelInfos);
            } catch (Exception var5) {
                CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_CHILD_LABEL", "", "��ѯ��ǩ�����µ��ӱ�ǩ�б�", sysInfo.getSysId() + sysInfo.getSysName() + "�û�" + sysInfo.getUserId() + "��ñ�ǩ�����µ���ͨ��ǩ�б�ʧ��", OperResultEnum.Failure, LogLevelEnum.Medium);
                this.log.error("��ö�ٱ�ǩ���ϸñ�ǩ��Ӧά�����ID������ʧ��");
                this.log.error(var5);
                throw new CIServiceException("��ö�ٱ�ǩ���ϸñ�ǩ��Ӧά�����ID������ʧ��");
            }
        }

        CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_CHILD_LABEL", "", "��ѯ��ǩ�����µ��ӱ�ǩ�б�", sysInfo.getSysId() + sysInfo.getSysName() + "�û�" + sysInfo.getUserId() + "��ñ�ǩ�����µ���ͨ��ǩ�б�ɹ�", OperResultEnum.Success, LogLevelEnum.Medium);
        this.log.info("��ѯ����ָ����ǩ����ı�ǩ��Ϣ�ɹ�,labelCategoryId��" + labelCategoryId + "-----------------size��" + labelInfos.size());
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " init cache ��ѯ�ӱ�ǩ�б� end");
        return labelInfos;
    }

    public List<LabelInfo> queryLabelInfo(SysInfo sysInfo) throws Exception {
        this.log.info("=================================>>>>  init Cache ȫ����ǩ start");
        this.log.info(JsonUtil.toJson(sysInfo));
        List labelInfoCategs = this.queryLabelCategoryInfo(sysInfo);
        List labelInfos = null;

        try {
            labelInfos = this.ciExternalLabelJDao.queryLabelInfoList(sysInfo);
        } catch (Exception var6) {
            this.log.error("���ȫ����ǩʧ�ܣ�");
            CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_LABEL", "", "��ѯ��ǩ�б�", sysInfo.getSysId() + sysInfo.getSysName() + "�û�" + sysInfo.getUserId() + "��ñ�ǩ�б�ʧ��", OperResultEnum.Failure, LogLevelEnum.Medium);
            throw new CIServiceException("���ȫ����ǩʧ�ܣ�");
        }

        if("true".equalsIgnoreCase(Configure.getInstance().getProperty("EXTERNAL_LABEL_ENUM_VALUE"))) {
            try {
                this.encapsulationlabelInfo(labelInfos);
            } catch (Exception var5) {
                CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_LABEL", "", "��ѯ��ǩ�б�", sysInfo.getSysId() + sysInfo.getSysName() + "�û�" + sysInfo.getUserId() + "��ñ�ǩ�б�ʧ��", OperResultEnum.Failure, LogLevelEnum.Medium);
                this.log.error("��ö�ٱ�ǩ���ϸñ�ǩ��Ӧά�����ID������ʧ��");
                this.log.error(var5);
                throw new CIServiceException("��ö�ٱ�ǩ���ϸñ�ǩ��Ӧά�����ID������ʧ��");
            }
        }

        labelInfos.addAll(labelInfoCategs);
        CILogServiceUtil.getLogServiceInstance().log("COC_EXTERNAL_QUERY_LABEL", "", "��ѯ��ǩ�б�", sysInfo.getSysId() + sysInfo.getSysName() + "�û�" + sysInfo.getUserId() + "��ñ�ǩ�б�ɹ�", OperResultEnum.Success, LogLevelEnum.Medium);
        this.log.info("��ѯ���б�ǩ����ǩ������Ϣ�ɹ�-------------size��" + labelInfos.size());
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " init Cache ȫ����ǩ end");
        return labelInfos;
    }

    public List<EnumValue> queryEnumValueList(SysInfo sysInfo, String labelId, Integer currPage, Integer pageSize) throws Exception {
        this.log.info("======================>>>> init Cache  invoke method queryEnumValueList start");
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
        this.log.info(sysInfo.getSysId() + "(" + sysInfo.getSysName() + ")" + sysInfo.getUserId() + " init cache invoke method queryLabelInfo end");
        return enumValueList;
    }

    private void encapsulationlabelInfo(List<LabelInfo> labelInfos) throws Exception {
        Iterator i$ = labelInfos.iterator();

        while(true) {
            LabelInfo labelInfo;
            do {
                if(!i$.hasNext()) {
                    return;
                }

                labelInfo = (LabelInfo)i$.next();
            } while(labelInfo.getLabelTypeId().intValue() != 5);

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
        }
    }

    public List<CiExternalSysLabelRel> queryExternalSysLabelRel() throws Exception {
        return this.ciExternalLabelHDao.selectExternalLabelRelList();
    }
}
