package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiLabelBrandUserNumJDao;
import com.ailk.biapp.ci.dao.ICiUserUseLabelJDao;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.LabelTrendInfo;
import com.ailk.biapp.ci.model.LabelTrendTableInfo;
import com.ailk.biapp.ci.service.ICiLabelTrendAnalysisService;
import com.ailk.biapp.ci.util.CIAlarmServiceUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("ciLabelTrendAnalysisService")
@Transactional
public class CiLabelTrendAnalysisServiceImpl implements ICiLabelTrendAnalysisService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ICiLabelBrandUserNumJDao ciLabelBrandUserNumJDao;
    @Autowired
    private ICiUserUseLabelJDao ciUserUseLabelJDao;

    public CiLabelTrendAnalysisServiceImpl() {
    }

    public ICiLabelBrandUserNumJDao getCiLabelBrandUserNumJDao() {
        return this.ciLabelBrandUserNumJDao;
    }

    public void setCiLabelBrandUserNumJDao(ICiLabelBrandUserNumJDao ciLabelBrandUserNumJDao) {
        this.ciLabelBrandUserNumJDao = ciLabelBrandUserNumJDao;
    }

    public ICiUserUseLabelJDao getCiUserUseLabelJDao() {
        return this.ciUserUseLabelJDao;
    }

    public void setCiUserUseLabelJDao(ICiUserUseLabelJDao ciUserUseLabelJDao) {
        this.ciUserUseLabelJDao = ciUserUseLabelJDao;
    }

    public List<LabelTrendInfo> queryLabelUserNumsTrendInfo(int currPage, int pageSize, String labelId) throws CIServiceException {
        List results = null;

        try {
            results = this.ciLabelBrandUserNumJDao.getLabelUserNumsTrendInfo(currPage, pageSize, labelId);
            return results;
        } catch (Exception var6) {
            this.log.error("根据标签ID 查询标签用户趋势分析数据异常", var6);
            throw new CIServiceException("根据标签ID 查询标签用户趋势分析数据异常");
        }
    }

    /** @deprecated */
    @Deprecated
    public List<LabelTrendTableInfo> queryLabelTrendTableInfo(int currPage, int pageSize, String labelId) throws CIServiceException {
        List results = null;

        try {
            results = this.ciLabelBrandUserNumJDao.getLabelTrendTableInfo(currPage, pageSize, labelId);
            return results;
        } catch (Exception var6) {
            this.log.error("根据标签 ID 查询标签使用趋势分析表格数据", var6);
            throw new CIServiceException("根据标签 ID 查询标签使用趋势分析表格数据");
        }
    }

    public String queryLabelBrandUserNumById(String labelId, String dataDate) throws CIServiceException {
        String count = "0";

        try {
            count = this.ciLabelBrandUserNumJDao.getLabelBrandUserNumById(labelId, dataDate);
            return count;
        } catch (Exception var5) {
            this.log.error("根据标签 ID 查询标签关联用户", var5);
            throw new CIServiceException("根据标签 ID 查询标签关联用户");
        }
    }

    public List<LabelTrendInfo> queryLabelUseTimesTrendInfo(int currPage, int pageSize, String labelId) throws CIServiceException {
        List results = null;

        try {
            results = this.ciUserUseLabelJDao.getLabelUseTimesTrendInfo(currPage, pageSize, labelId);
            return results;
        } catch (Exception var6) {
            this.log.error("根据标签 ID 查询标签使用次数趋势分析数据", var6);
            throw new CIServiceException("根据标签 ID 查询标签使用次数趋势分析数据");
        }
    }

    public List<LabelTrendTableInfo> queryLabelTrendTableInfo(int currPage, int pageSize, String labelId, String dataDate, String labelType) throws CIServiceException {
        List results = null;

        try {
            results = this.ciLabelBrandUserNumJDao.getLabelTrendTableInfo(currPage, pageSize, labelId, dataDate, labelType);
            if(null != results && results.size() > 0) {
                String e = PrivilegeServiceUtil.getUserId();
                boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
                boolean showAllAlarm = !needAuthority || needAuthority && PrivilegeServiceUtil.isAdminUser(e);
                if(showAllAlarm) {
                    e = null;
                }

                Iterator i$ = results.iterator();

                while(i$.hasNext()) {
                    LabelTrendTableInfo trendInfo = (LabelTrendTableInfo)i$.next();
                    String date = trendInfo.getDataDate();
                    if(StringUtil.isNotEmpty(date)) {
                        try {
                            boolean e1 = CIAlarmServiceUtil.haveAlarmRecords(date, e, "LabelAlarm", labelId);
                            if(e1) {
                                trendInfo.setAlarm("告警");
                            } else {
                                trendInfo.setAlarm("正常");
                            }
                        } catch (Exception var14) {
                            this.log.error("", var14);
                        }
                    }
                }
            }

            return results;
        } catch (Exception var15) {
            this.log.error("根据标签 ID 查询标签使用趋势分析表格数据", var15);
            throw new CIServiceException("根据标签 ID 查询标签使用趋势分析表格数据");
        }
    }

    public List<LabelTrendInfo> queryLabelUseTimesTrendInfo(String labelId, String dataDate, String labelType) throws CIServiceException {
        List results = null;

        try {
            results = this.ciUserUseLabelJDao.getLabelUseTimesTrendInfo(labelId, dataDate, labelType);
            return results;
        } catch (Exception var6) {
            this.log.error("根据标签 ID 查询标签使用次数趋势分析数据", var6);
            throw new CIServiceException("根据标签 ID 查询标签使用次数趋势分析数据");
        }
    }

    public List<LabelTrendInfo> queryLabelUserNumsTrendInfo(String labelId, String dataDate, String labelType) throws CIServiceException {
        List results = null;

        try {
            results = this.ciLabelBrandUserNumJDao.getLabelUserNumsTrendInfo(labelId, dataDate, labelType);
            return results;
        } catch (Exception var6) {
            this.log.error("根据标签ID 查询标签用户趋势分析数据异常", var6);
            throw new CIServiceException("根据标签ID 查询标签用户趋势分析数据异常");
        }
    }

    public long queryLabelTrednTableInfoNums(String labelId, String dataDate, String labelType) throws CIServiceException {
        long nums = 0L;

        try {
            nums = this.ciLabelBrandUserNumJDao.getLabelTrednTableInfoNums(labelId, dataDate, labelType);
            return nums;
        } catch (Exception var7) {
            this.log.error("根据标签 ID 查询标签使用趋势分析表格数据查询记录数", var7);
            throw new CIServiceException("根据标签 ID 查询标签使用趋势分析表格数据查询记录数");
        }
    }
}
