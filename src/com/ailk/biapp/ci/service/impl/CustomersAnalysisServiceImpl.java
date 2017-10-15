package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiCustomGroupFormJDao;
import com.ailk.biapp.ci.dao.ICiCustomListInfoHDao;
import com.ailk.biapp.ci.dao.ICiCustomListInfoJDao;
import com.ailk.biapp.ci.dao.ICiLabelBrandUserNumJDao;
import com.ailk.biapp.ci.dao.ICiLoadCustomGroupFileJDao;
import com.ailk.biapp.ci.dao.ICiUserCustomRelHDao;
import com.ailk.biapp.ci.dao.ICiUserCustomRelJDao;
import com.ailk.biapp.ci.dao.ICustomersAnalysisJDao;
import com.ailk.biapp.ci.dao.ICustomersCompareAnalysisHDao;
import com.ailk.biapp.ci.dao.ICustomersCompareAnalysisJDao;
import com.ailk.biapp.ci.entity.CiCustomGroupForm;
import com.ailk.biapp.ci.entity.CiUserCustomContrast;
import com.ailk.biapp.ci.entity.CiUserCustomContrastId;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.CiBrandHisModel;
import com.ailk.biapp.ci.model.CiCityHisModel;
import com.ailk.biapp.ci.model.CiCustomRelModel;
import com.ailk.biapp.ci.model.CiCustomersFormTrendModel;
import com.ailk.biapp.ci.model.CiVipHisModel;
import com.ailk.biapp.ci.model.CustomGroupContrastDetailInfo;
import com.ailk.biapp.ci.model.LabelShortInfo;
import com.ailk.biapp.ci.model.LabelTrendInfo;
import com.ailk.biapp.ci.service.ICustomersAnalysisService;
import com.ailk.biapp.ci.util.CIAlarmServiceUtil;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.IdToName;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomersAnalysisServiceImpl implements ICustomersAnalysisService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ICustomersAnalysisJDao customersAnalysisJDao;
    @Autowired
    private ICiUserCustomRelHDao customersAnalysisHDao;
    @Autowired
    private ICiCustomListInfoHDao customListInfoHDao;
    @Autowired
    private ICiCustomListInfoJDao customListInfoJDao;
    @Autowired
    private ICiCustomGroupFormJDao customGroupFormJDao;
    @Autowired
    private ICiLoadCustomGroupFileJDao LoadCustomGroupFileJDao;
    @Autowired
    private ICustomersCompareAnalysisJDao customersCompareAnalysisJDao;
    @Autowired
    private ICustomersCompareAnalysisHDao customersCompareAnalysisHDao;
    @Autowired
    private ICiUserCustomRelJDao ciUserCustomRelJDao;
    @Autowired
    private ICiLabelBrandUserNumJDao ciLabelBrandUserNumJDao;

    public CustomersAnalysisServiceImpl() {
    }

    public ICiLabelBrandUserNumJDao getCiLabelBrandUserNumJDao() {
        return this.ciLabelBrandUserNumJDao;
    }

    public void setCiLabelBrandUserNumJDao(ICiLabelBrandUserNumJDao ciLabelBrandUserNumJDao) {
        this.ciLabelBrandUserNumJDao = ciLabelBrandUserNumJDao;
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public int queryAssociationAnalysis(String fromSql, String tableName) throws CIServiceException {
        int count = this.customersAnalysisJDao.getCustomersLabelJjCount(fromSql, tableName);
        return count;
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public void queryRelAnalysis(String fromSql, String tableName, CiCustomRelModel ciCustomRelModel) throws CIServiceException {
        int customersLabelJjCount = this.customersAnalysisJDao.getCustomersLabelJjCount(fromSql, tableName);
        int customersLabelCount = this.customersAnalysisJDao.getCustomersByLabel(fromSql, tableName);
        ciCustomRelModel.setOverlapUserNum(Integer.valueOf(customersLabelJjCount));
        ciCustomRelModel.setRelLabelUserNum(Integer.valueOf(customersLabelCount));
        ciCustomRelModel.getProportion();
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiCustomRelModel> queryRelCustom(String userId, CiCustomRelModel ciCustomRelModel) throws CIServiceException {
        List list = null;

        try {
            list = this.ciUserCustomRelJDao.selectRelCustom(userId, ciCustomRelModel);
            Iterator e = list.iterator();

            while(e.hasNext()) {
                CiCustomRelModel relModel = (CiCustomRelModel)e.next();
                relModel.setRelLabelName(IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", relModel.getRelLabelId()));
            }

            return list;
        } catch (Exception var6) {
            this.log.error("", var6);
            throw new CIServiceException("获取用户指定标签的所有关联标签的关联结果数据报错");
        }
    }

    public void saveAssociationAnalysis(String customId, String labelIds, String userId, String date) throws CIServiceException {
        try {
            this.ciUserCustomRelJDao.deleteRelCustom(userId, customId, labelIds);
            this.customersAnalysisHDao.insertCiUserCustomRel(customId, labelIds, userId, date);
        } catch (Exception var6) {
            this.log.error("", var6);
            throw new CIServiceException("保存客户群关联分析报错");
        }
    }

    public void saveCompareAnalysis(String customId, String labelId, String userId) throws CIServiceException {
        try {
            CiUserCustomContrast e = new CiUserCustomContrast();
            CiUserCustomContrastId id = new CiUserCustomContrastId();
            id.setContrastLabelId(labelId);
            id.setCustomGroupId(customId);
            id.setUserId(userId);
            e.setId(id);
            e.setStatus(Integer.valueOf(1));
            this.customersCompareAnalysisHDao.insertCiUserCustomContrast(e);
        } catch (Exception var6) {
            this.log.error("", var6);
            throw new CIServiceException("删除客户群对比分析报错");
        }
    }

    public void delUserCustomContrast(String customGroupId, String labelId, String userId) throws CIServiceException {
        try {
            CiUserCustomContrastId e = new CiUserCustomContrastId();
            e.setContrastLabelId(labelId);
            e.setCustomGroupId(customGroupId);
            e.setUserId(userId);
            this.customersCompareAnalysisHDao.deleteCiUserCustomContrast(e);
        } catch (Exception var5) {
            this.log.error("", var5);
            throw new CIServiceException("删除客户群对比分析报错");
        }
    }

    public void updateCiUserCustomContrast(String userId, String customGroupId) throws CIServiceException {
        try {
            this.customersCompareAnalysisHDao.updateCiUserCustomContrast(userId, customGroupId);
        } catch (Exception var4) {
            this.log.error("", var4);
            throw new CIServiceException("删除客户群对比分析关系报错");
        }
    }

    public List<LabelShortInfo> queryCustomersCompareAnalysis(String customId, String userId, String dataDate) throws CIServiceException {
        try {
            List resultLabelShortInfoList = this.customersCompareAnalysisJDao.getCustomersCompareAnalysis(customId, userId, dataDate);
            return resultLabelShortInfoList;
        } catch (Exception var6) {
            this.log.error("", var6);
            throw new CIServiceException("查询客户群对比分析标签集合报错");
        }
    }

    public String saveCustomersAnalsisRelation(String tabName, String selectPhoneNoSql, String date) throws CIServiceException {
        String newTabName = "";

        try {
            String e = Configure.getInstance().getProperty("CUST_LIST_TMP_TABLE");
            tabName = e.replace("YYMMDDHHMISSTTTTTT", "") + CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L);
            String column = Configure.getInstance().getProperty("RELATED_COLUMN");
            this.LoadCustomGroupFileJDao.createTable(newTabName, e, column);
            this.customersAnalysisJDao.insertCustomersTable(selectPhoneNoSql, newTabName, tabName);
            return tabName;
        } catch (Exception var7) {
            this.log.error("", var7);
            throw new CIServiceException("关联分析生成客户群报错");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public CustomGroupContrastDetailInfo queryCompareAnalysis(String selectPhoneNoSql, String tableName, CustomGroupContrastDetailInfo customGroupContrast) throws CIServiceException {
        try {
            Integer e = Integer.valueOf(customGroupContrast.getCustomGroupNum());
            Integer labelNum = customGroupContrast.getContrastLabelNum();
            boolean customersLabelJjCount = false;
            boolean customersAndLabelCount = false;
            boolean customersLabelCjCount = false;
            boolean labelCustomersCjCount = false;
            int customersLabelJjCount1;
            int customersAndLabelCount1;
            int customersLabelCjCount1;
            int labelCustomersCjCount1;
            if(e.intValue() != 0 && labelNum.intValue() != 0) {
                customersLabelJjCount1 = this.customersAnalysisJDao.getCustomersLabelJjCount(selectPhoneNoSql, tableName);
                customersAndLabelCount1 = e.intValue() + labelNum.intValue() - customersLabelJjCount1;
                customersLabelCjCount1 = e.intValue() - customersLabelJjCount1;
                labelCustomersCjCount1 = labelNum.intValue() - customersLabelJjCount1;
            } else {
                customersLabelJjCount1 = 0;
                customersAndLabelCount1 = e.intValue() + labelNum.intValue();
                customersLabelCjCount1 = e.intValue() == 0?0:e.intValue();
                labelCustomersCjCount1 = labelNum.intValue() == 0?0:labelNum.intValue();
            }

            String intersectionRate = CiUtil.percentStr(customersLabelJjCount1, customersAndLabelCount1, "0.00%");
            String mainDifferenceRate = CiUtil.percentStr(customersLabelCjCount1, customersAndLabelCount1, "0.00%");
            String contrastDifferenceRate = CiUtil.percentStr(labelCustomersCjCount1, customersAndLabelCount1, "0.00%");
            customGroupContrast.setIntersectionRate(intersectionRate.equals("0")?"0.00%":intersectionRate);
            customGroupContrast.setIntersectionNum(customersLabelJjCount1 + "");
            customGroupContrast.setMainDifferenceRate(mainDifferenceRate.equals("0")?"0.00%":mainDifferenceRate);
            customGroupContrast.setMainDifferenceNum(customersLabelCjCount1 + "");
            customGroupContrast.setContrastDifferenceRate(contrastDifferenceRate.equals("0")?"0.00%":contrastDifferenceRate);
            customGroupContrast.setContrastDifferenceNum(labelCustomersCjCount1 + "");
            return customGroupContrast;
        } catch (Exception var13) {
            this.log.error("", var13);
            throw new CIServiceException("查询客户群对比分析报错");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<LabelTrendInfo> queryTrendAnalysis(String customId, String dataDate, int cycle) throws CIServiceException {
        List trendAnalysisList = null;

        try {
            trendAnalysisList = this.customGroupFormJDao.selectCustomersTotalByCustomerList(customId, dataDate, cycle, "asc");
            return trendAnalysisList;
        } catch (Exception var6) {
            this.log.error("", var6);
            throw new CIServiceException("查询客户群清单报错");
        }
    }

    public int selectCustomersTotalByTrendAnalysisCount(String customId, String dataDate, int cycle) throws CIServiceException {
        boolean count = false;

        try {
            int count1 = this.customGroupFormJDao.selectCustomersTotalByCustomerListCount(customId, dataDate, cycle);
            return count1;
        } catch (Exception var6) {
            this.log.error("", var6);
            throw new CIServiceException("查询客户群趋势分析总数报错");
        }
    }

    public List<LabelTrendInfo> queryTrendAnalysis(String customId, String dataDate, int cycle, int pageNum, int pageSize) throws CIServiceException {
        List trendAnalysisList = null;

        try {
            trendAnalysisList = this.customGroupFormJDao.selectCustomersTotalByCustomerListPage(customId, dataDate, cycle, "desc", Integer.valueOf(pageNum), Integer.valueOf(pageSize));
            if(trendAnalysisList != null && trendAnalysisList.size() > 0) {
                String e = PrivilegeServiceUtil.getUserId();
                Iterator i$ = trendAnalysisList.iterator();

                while(i$.hasNext()) {
                    LabelTrendInfo trendInfo = (LabelTrendInfo)i$.next();
                    String date = trendInfo.getDataDate();
                    if(StringUtil.isNotEmpty(date)) {
                        try {
                            boolean e1 = CIAlarmServiceUtil.haveAlarmRecords(date, e, "CustomersAlarm", customId);
                            if(e1) {
                                trendInfo.setAlarm("告警");
                            } else {
                                trendInfo.setAlarm("正常");
                            }
                        } catch (Exception var12) {
                            this.log.error("", var12);
                        }
                    }
                }
            }

            return trendAnalysisList;
        } catch (Exception var13) {
            this.log.error("", var13);
            throw new CIServiceException("查询客户群清单报错");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiCustomGroupForm> queryBrandFormChartData(CiCustomGroupForm ciCustomGroupForm) throws CIServiceException {
        ArrayList list = new ArrayList();
        String[] colors = CiUtil.getColors();
        int totalNum = 0;

        try {
            List e = this.customGroupFormJDao.queryBrandFormChartData(ciCustomGroupForm);

            for(int i = 0; i < e.size(); ++i) {
                CiCustomersFormTrendModel trendModel = (CiCustomersFormTrendModel)e.get(i);
                if(trendModel.getCustomGroupFormList() != null && trendModel.getCustomGroupFormList().size() != 0) {
                    CiCustomGroupForm customGroupForm = (CiCustomGroupForm)trendModel.getCustomGroupFormList().get(0);
                    if(customGroupForm != null) {
                        customGroupForm.setColor(colors[i]);
                        list.add(customGroupForm);
                        totalNum += customGroupForm.getCustomNum().intValue();
                        customGroupForm.setBrand_name(IdToName.getName("DIM_BRAND", customGroupForm.getBrandId()));
                    }
                }
            }

            ciCustomGroupForm.setCustomNum(Integer.valueOf(totalNum));
            return list;
        } catch (Exception var9) {
            var9.printStackTrace();
            this.log.error(var9);
            throw new CIServiceException("查询客户群品牌构成分析数据报错");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiCustomGroupForm> querySubBrandFormChartData(CiCustomGroupForm ciCustomGroupForm) throws CIServiceException {
        List list = null;

        try {
            list = this.customGroupFormJDao.selectSubBrandFormChartData(ciCustomGroupForm);

            for(int e = 0; e < list.size(); ++e) {
                ((CiCustomGroupForm)list.get(e)).setBrand_name(IdToName.getName("DIM_BRAND", ((CiCustomGroupForm)list.get(e)).getBrandId()));
            }

            return list;
        } catch (Exception var4) {
            this.log.error("", var4);
            throw new CIServiceException("查询客户群品牌构成分析数据报错");
        }
    }

    public int queryBrandHistoryDataCount(CiCustomGroupForm ciCustomGroupForm) throws CIServiceException {
        boolean count = false;

        try {
            int count1 = this.customGroupFormJDao.selectBrandHistoryDataCount(ciCustomGroupForm);
            return count1;
        } catch (Exception var4) {
            this.log.error("", var4);
            throw new CIServiceException("查询客户群品牌数量报错");
        }
    }

    public int queryCityHistoryDataCount(CiCustomGroupForm ciCustomGroupForm) throws CIServiceException {
        boolean count = false;

        try {
            int count1 = this.customGroupFormJDao.selectCityHistoryDataCount(ciCustomGroupForm);
            return count1;
        } catch (Exception var4) {
            this.log.error("", var4);
            throw new CIServiceException("查询客户群地域数量报错");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiBrandHisModel> selectBrandHistoryData(int currentPage, int pageSize, List<String> dateList, CiCustomGroupForm ciCustomGroupForm) throws CIServiceException {
        List list = null;

        try {
            list = this.customGroupFormJDao.selectBrandHistoryData(currentPage, pageSize, dateList, ciCustomGroupForm);
            return list;
        } catch (Exception var7) {
            this.log.error("", var7);
            throw new CIServiceException("查询客户群品牌构成分析数据报错");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiCustomGroupForm> queryCityFormChartData(CiCustomGroupForm ciCustomGroupForm) throws CIServiceException {
        ArrayList modelList = new ArrayList();
        String[] colors = CiUtil.getColors();
        int totalNum = 0;

        try {
            List e = this.customGroupFormJDao.selectCityFormChartData(ciCustomGroupForm);
            if(null != modelList) {
                for(int i = 0; i < e.size(); ++i) {
                    CiCustomersFormTrendModel trendModel = (CiCustomersFormTrendModel)e.get(i);
                    if(trendModel.getCustomGroupFormList() != null && trendModel.getCustomGroupFormList().size() != 0) {
                        CiCustomGroupForm customGroupForm = (CiCustomGroupForm)trendModel.getCustomGroupFormList().get(0);
                        if(customGroupForm != null) {
                            customGroupForm.setColor(colors[i]);
                            modelList.add(customGroupForm);
                            totalNum += customGroupForm.getCustomNum().intValue();
                            customGroupForm.setCityName(IdToName.getName("DIM_CITY", customGroupForm.getCityId()));
                        }
                    }
                }
            }

            ciCustomGroupForm.setCustomNum(Integer.valueOf(totalNum));
            return modelList;
        } catch (Exception var9) {
            this.log.error("", var9);
            throw new CIServiceException("查询客户群品牌构成分析数据报错");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiCustomersFormTrendModel> queryCityTrendChartData(CiCustomGroupForm ciCustomGroupForm) throws CIServiceException {
        List modelList = null;
        String[] colors = CiUtil.getColors();

        try {
            modelList = this.customGroupFormJDao.selectCityTrendChartData(ciCustomGroupForm);
            if(null != modelList) {
                for(int e = 0; e < modelList.size(); ++e) {
                    ((CiCustomersFormTrendModel)modelList.get(e)).setColor(colors[e]);
                    ((CiCustomersFormTrendModel)modelList.get(e)).setCityName(IdToName.getName("DIM_CITY", ((CiCustomersFormTrendModel)modelList.get(e)).getCityId()));
                }
            }

            return modelList;
        } catch (Exception var5) {
            this.log.error("", var5);
            throw new CIServiceException("查询全部地域历史趋势数据报错");
        }
    }

    public List<CiCityHisModel> queryCityHistoryData(List<String> dateList, CiCustomGroupForm ciCustomGroupForm) throws CIServiceException {
        List list = null;

        try {
            list = this.customGroupFormJDao.selectCityHistoryData(dateList, ciCustomGroupForm);
            return list;
        } catch (Exception var5) {
            this.log.error("", var5);
            throw new CIServiceException("查询地域分析历史表格数据报错");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiCustomGroupForm> queryCityTrendChartDataByCityId(CiCustomGroupForm ciCustomGroupForm) throws CIServiceException {
        List modelList = null;
        String[] colors = CiUtil.getColors();

        try {
            modelList = this.customGroupFormJDao.selectCityTrendChartDataByCityId(ciCustomGroupForm);
            if(null != modelList) {
                for(int e = 0; e < modelList.size(); ++e) {
                    ((CiCustomGroupForm)modelList.get(e)).setColor(colors[e]);
                    ((CiCustomGroupForm)modelList.get(e)).setCityName(IdToName.getName("DIM_CITY", ((CiCustomGroupForm)modelList.get(e)).getCityId()));
                }
            }

            return modelList;
        } catch (Exception var5) {
            this.log.error("", var5);
            throw new CIServiceException("查询全部地域历史趋势数据报错");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiCustomGroupForm> queryMoreCityTrendChartDataByCityId(CiCustomGroupForm ciCustomGroupForm) throws CIServiceException {
        List modelList = null;
        String[] colors = CiUtil.getColors();

        try {
            modelList = this.customGroupFormJDao.selectMoreCityTrendChartDataByCityId(ciCustomGroupForm);
            if(null != modelList) {
                for(int e = 0; e < modelList.size(); ++e) {
                    ((CiCustomGroupForm)modelList.get(e)).setColor(colors[e]);
                    ((CiCustomGroupForm)modelList.get(e)).setCityName(IdToName.getName("DIM_CITY", ((CiCustomGroupForm)modelList.get(e)).getCityId()));
                }
            }

            return modelList;
        } catch (Exception var5) {
            this.log.error("", var5);
            throw new CIServiceException("查询全部地域历史趋势数据报错");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiCustomersFormTrendModel> queryBrandTrendChartData(CiCustomGroupForm ciCustomGroupForm) throws CIServiceException {
        List list = null;
        String[] colors = CiUtil.getColors();

        try {
            list = this.customGroupFormJDao.selectBrandTrendChartData(ciCustomGroupForm);
            if(null != list) {
                for(int e = 0; e < list.size(); ++e) {
                    ((CiCustomersFormTrendModel)list.get(e)).setColor(colors[e]);
                    ((CiCustomersFormTrendModel)list.get(e)).setBrandName(IdToName.getName("DIM_BRAND", ((CiCustomersFormTrendModel)list.get(e)).getBrandId()));
                }
            }

            return list;
        } catch (Exception var5) {
            this.log.error("", var5);
            throw new CIServiceException("查询客户群品牌构成分析数据报错");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiCustomGroupForm> queryVipFormChartData(CiCustomGroupForm ciCustomGroupForm) throws CIServiceException {
        ArrayList list = new ArrayList();
        String[] colors = CiUtil.getColors();
        int totalNum = 0;

        try {
            List e = this.customGroupFormJDao.selectVipFormChartData(ciCustomGroupForm);
            if(null != e) {
                for(int i = 0; i < e.size(); ++i) {
                    CiCustomersFormTrendModel trendModel = (CiCustomersFormTrendModel)e.get(i);
                    if(trendModel.getCustomGroupFormList() != null && trendModel.getCustomGroupFormList().size() != 0) {
                        CiCustomGroupForm customGroupForm = (CiCustomGroupForm)trendModel.getCustomGroupFormList().get(0);
                        if(customGroupForm != null) {
                            customGroupForm.setColor(colors[i]);
                            list.add(customGroupForm);
                            totalNum += customGroupForm.getCustomNum().intValue();
                            customGroupForm.setVipLevelName(IdToName.getName("DIM_VIP_LEVEL", customGroupForm.getVipLevelId()));
                        }
                    }
                }
            }

            ciCustomGroupForm.setCustomNum(Integer.valueOf(totalNum));
            return list;
        } catch (Exception var9) {
            this.log.error("", var9);
            throw new CIServiceException("查询客户群品牌构成分析数据报错");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiCustomersFormTrendModel> queryVipTrendChartData(CiCustomGroupForm ciCustomGroupForm) throws CIServiceException {
        List list = null;
        String[] colors = CiUtil.getColors();

        try {
            list = this.customGroupFormJDao.selectVipTrendChartData(ciCustomGroupForm);
            if(null != list) {
                for(int e = 0; e < list.size(); ++e) {
                    ((CiCustomersFormTrendModel)list.get(e)).setColor(colors[e]);
                    ((CiCustomersFormTrendModel)list.get(e)).setVipLevelName(IdToName.getName("DIM_VIP_LEVEL", ((CiCustomersFormTrendModel)list.get(e)).getVipLevelId()));
                }
            }

            return list;
        } catch (Exception var5) {
            this.log.error("", var5);
            throw new CIServiceException("获取VIP等级构成历史趋势数据报错");
        }
    }

    public int queryVipHistoryDataCount(CiCustomGroupForm ciCustomGroupForm) throws CIServiceException {
        boolean count = false;

        try {
            int count1 = this.customGroupFormJDao.selectVipHistoryDataCount(ciCustomGroupForm);
            return count1;
        } catch (Exception var4) {
            this.log.error("", var4);
            throw new CIServiceException("查询VIP等级数量数据报错");
        }
    }

    public List<CiVipHisModel> queryVipHistoryData(List<String> dateList, CiCustomGroupForm ciCustomGroupForm) throws CIServiceException {
        List list = null;

        try {
            list = this.customGroupFormJDao.selectVipHistoryData(dateList, ciCustomGroupForm);
            return list;
        } catch (Exception var5) {
            this.log.error("", var5);
            throw new CIServiceException("查询VIP历史趋势数据报错");
        }
    }

    public List<CiCustomGroupForm> queryVipTrendChartDataByVipLevelId(CiCustomGroupForm ciCustomGroupForm) throws CIServiceException {
        List list = null;

        try {
            list = this.customGroupFormJDao.selectVipTrendChartDataByVipLevelId(ciCustomGroupForm);
            if(null != list) {
                for(int e = 0; e < list.size(); ++e) {
                    ((CiCustomGroupForm)list.get(e)).setVipLevelName(IdToName.getName("DIM_VIP_LEVEL", ((CiCustomGroupForm)list.get(e)).getVipLevelId()));
                }
            }

            return list;
        } catch (Exception var4) {
            this.log.error("", var4);
            throw new CIServiceException("查询指定VIP等级的历史趋势数据报错");
        }
    }

    public ICustomersAnalysisJDao getCustomersAnalysisJDao() {
        return this.customersAnalysisJDao;
    }

    public void setCustomersAnalysisJDao(ICustomersAnalysisJDao customersAnalysisJDao) {
        this.customersAnalysisJDao = customersAnalysisJDao;
    }

    public ICiCustomListInfoHDao getCustomListInfoHDao() {
        return this.customListInfoHDao;
    }

    public void setCustomListInfoHDao(ICiCustomListInfoHDao customListInfoHDao) {
        this.customListInfoHDao = customListInfoHDao;
    }

    public ICiCustomListInfoJDao getCustomListInfoJDao() {
        return this.customListInfoJDao;
    }

    public void setCustomListInfoJDao(ICiCustomListInfoJDao customListInfoJDao) {
        this.customListInfoJDao = customListInfoJDao;
    }

    public ICiCustomGroupFormJDao getCustomGroupFormJDao() {
        return this.customGroupFormJDao;
    }

    public void setCustomGroupFormJDao(ICiCustomGroupFormJDao customGroupFormJDao) {
        this.customGroupFormJDao = customGroupFormJDao;
    }

    public ICiLoadCustomGroupFileJDao getLoadCustomGroupFileJDao() {
        return this.LoadCustomGroupFileJDao;
    }

    public void setLoadCustomGroupFileJDao(ICiLoadCustomGroupFileJDao loadCustomGroupFileJDao) {
        this.LoadCustomGroupFileJDao = loadCustomGroupFileJDao;
    }

    public ICustomersCompareAnalysisJDao getCustomersCompareAnalysisJDao() {
        return this.customersCompareAnalysisJDao;
    }

    public void setCustomersCompareAnalysisJDao(ICustomersCompareAnalysisJDao customersCompareAnalysisJDao) {
        this.customersCompareAnalysisJDao = customersCompareAnalysisJDao;
    }

    public ICustomersCompareAnalysisHDao getCustomersCompareAnalysisHDao() {
        return this.customersCompareAnalysisHDao;
    }

    public void setCustomersCompareAnalysisHDao(ICustomersCompareAnalysisHDao customersCompareAnalysisHDao) {
        this.customersCompareAnalysisHDao = customersCompareAnalysisHDao;
    }

    public ICiUserCustomRelHDao getCustomersAnalysisHDao() {
        return this.customersAnalysisHDao;
    }

    public void setCustomersAnalysisHDao(ICiUserCustomRelHDao customersAnalysisHDao) {
        this.customersAnalysisHDao = customersAnalysisHDao;
    }

    public ICiUserCustomRelJDao getCiUserCustomRelJDao() {
        return this.ciUserCustomRelJDao;
    }

    public void setCiUserCustomRelJDao(ICiUserCustomRelJDao ciUserCustomRelJDao) {
        this.ciUserCustomRelJDao = ciUserCustomRelJDao;
    }

    public String queryLabelBrandUserNumById(String labelId, String dataDate) throws CIServiceException {
        String count = "-";

        try {
            count = this.ciLabelBrandUserNumJDao.getLabelBrandUserNumById(labelId, dataDate);
            return count;
        } catch (Exception var5) {
            this.log.error("根据标签ID、统计月份查询标签关联用户数发生异常", var5);
            throw new CIServiceException("根据标签ID、统计月份查询标签关联用户数发生异常");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public String getCustomersTrendChartJson(String customGroupId, String dataDate, int cycle) throws CIServiceException {
        List trendAnalysisList = this.queryTrendAnalysis(customGroupId, dataDate, cycle);
        StringBuffer categoriesSB = new StringBuffer("[");
        StringBuffer seriesSB = new StringBuffer("[");
        if(null != trendAnalysisList && trendAnalysisList.size() > 0) {
            Iterator categoriesStr = trendAnalysisList.iterator();

            while(categoriesStr.hasNext()) {
                LabelTrendInfo seriesStr = (LabelTrendInfo)categoriesStr.next();
                String dateTime = seriesStr.getDataDate();
                categoriesSB.append(dateTime).append(",");
                seriesSB.append(seriesStr.getValue()).append(",");
            }
        }

        String categoriesStr1 = categoriesSB.toString().substring(0, categoriesSB.length() - 1) + "]";
        String seriesStr1 = seriesSB.toString().substring(0, seriesSB.length() - 1) + "]";
        return categoriesStr1 + "|" + seriesStr1;
    }
}
