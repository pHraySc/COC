package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiLabelStatusHistoryHDao;
import com.ailk.biapp.ci.dao.ICiLabelStatusHistoryJDao;
import com.ailk.biapp.ci.entity.CiLabelStatusHistory;
import com.ailk.biapp.ci.entity.CiSysAnnouncement;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ICiLabelStatusHistoryService;
import com.ailk.biapp.ci.service.ICiSysAnnouncementService;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiLabelStatusHistoryServiceImpl implements ICiLabelStatusHistoryService {
    private Logger log = Logger.getLogger(CiLabelStatusHistoryServiceImpl.class);
    @Autowired
    private ICiLabelStatusHistoryHDao ciLabelStatusHistoryHDao;
    @Autowired
    private ICiSysAnnouncementService announcementService;
    @Autowired
    private ICiLabelStatusHistoryJDao ciLabelStatusHistoryJDao;

    public CiLabelStatusHistoryServiceImpl() {
    }

    public void addCiLabelStatusHistory(CiLabelStatusHistory ciLabelStatusHistory) throws CIServiceException {
    }

    public void modifyCiLabelStatusHistory(CiLabelStatusHistory ciLabelStatusHistory) throws CIServiceException {
        ciLabelStatusHistory.setHasSendNotice(Integer.valueOf(1));
        this.ciLabelStatusHistoryHDao.updateCiLabelStatusHistory(ciLabelStatusHistory);
    }

    public int modifyBatchCiLabelStatusHistory(String labelIds) throws CIServiceException {
        return this.ciLabelStatusHistoryJDao.updateCiLabelStatusHistoryHasSendNotice(labelIds);
    }

    public void addSysAnnouncemnetByLabelIds() {
        try {
            List e = this.queryCiLabelStatusHistoryList();
            CacheBase cacheBase = CacheBase.getInstance();
            StringBuffer sb = new StringBuffer();
            StringBuffer labelIds = new StringBuffer();
            int _index = 0;

            for(Iterator sysAnnouncement = e.iterator(); sysAnnouncement.hasNext(); ++_index) {
                CiLabelStatusHistory date = (CiLabelStatusHistory)sysAnnouncement.next();
                int date2 = date.getId().getLabelId().intValue();
                String df = cacheBase.getLabelNameByKey("ALL_EFFECTIVE_LABEL_MAP", Integer.valueOf(date2));
                if(_index > 0) {
                    sb.append("," + df);
                    labelIds.append(",\'" + date2 + "\'");
                } else {
                    sb.append(df);
                    labelIds.append("\'" + date2 + "\'");
                }
            }

            if(StringUtil.isNotEmpty(sb.toString())) {
                CiSysAnnouncement var17 = new CiSysAnnouncement();
                var17.setAnnouncementId((String)null);
                var17.setAnnouncementDetail("重新生成数据的标签有：" + sb.toString() + "；如您的客户群中使用了以上标签作为规则，请慎重使用！");
                var17.setAnnouncementName("系统标签数据重新生成");
                Date var18 = new Date();
                Date var19 = DateUtil.addDays(var18, 1);
                SimpleDateFormat var20 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = format.format(var19);
                Date effectiveDate = var20.parse(dateStr + " 00:00:00");
                Timestamp effectiveTime = new Timestamp(effectiveDate.getTime());
                var17.setEffectiveTime(effectiveTime);
                var17.setPriorityId(Integer.valueOf(0));
                var17.setReleaseDate(new Timestamp(var18.getTime()));
                var17.setStatus(Integer.valueOf(1));
                var17.setTypeId(Integer.valueOf(1));
                boolean flag = this.announcementService.addSysAnnouncement(var17);
                if(!flag) {
                    this.log.error("系统发送公告失败");
                    throw new CIServiceException("系统发送公告失败");
                }

                int _effectInt = this.modifyBatchCiLabelStatusHistory(labelIds.toString());
                if(_effectInt == 0) {
                    this.log.equals("更新标签已发公告状态失败");
                    throw new CIServiceException("更新标签已发公告状态失败");
                }
            }
        } catch (Exception var16) {
            this.log.error("系统发送公告失败", var16);
        }

    }

    public List<CiLabelStatusHistory> queryCiLabelStatusHistoryList() throws CIServiceException {
        return this.ciLabelStatusHistoryHDao.selectCiLabelStatusHistoryListByNotice();
    }
}
