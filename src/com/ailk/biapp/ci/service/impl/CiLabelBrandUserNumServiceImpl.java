package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiLabelBrandUserNumJDao;
import com.ailk.biapp.ci.entity.CiLabelBrandUserNum;
import com.ailk.biapp.ci.service.ICiLabelBrandUserNumService;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiLabelBrandUserNumServiceImpl implements ICiLabelBrandUserNumService {
    @Autowired
    private ICiLabelBrandUserNumJDao labelBrandUserNumJDao;

    public CiLabelBrandUserNumServiceImpl() {
    }

    public List<CiLabelBrandUserNum> getLabelBrandUserNumListByDataDate(String dataDate) {
        return this.labelBrandUserNumJDao.getLabelBrandUserNumListByDataDate(dataDate);
    }

    public List<CiLabelBrandUserNum> getAllCityLabelBrandUserNumListByDataDate(String dataDate) {
        return this.labelBrandUserNumJDao.getAllCityLabelBrandUserNumListByDataDate(dataDate);
    }

    public List<CiLabelBrandUserNum> getLabelBrandUserNumList(CiLabelBrandUserNum ciLabelBrandUserNum) {
        return this.labelBrandUserNumJDao.getLabelBrandUserNumList(ciLabelBrandUserNum);
    }

    public Map<String, Map<String, CiLabelBrandUserNum>> getUserIdAndlabelIdToLabelStatsMap(List<CiLabelBrandUserNum> allLabelBrandUserNumList, Set<String> userIdSet) {
        HashMap labelIdAndCityIdToLabelStatsMap = new HashMap();
        if(!CollectionUtils.isEmpty(allLabelBrandUserNumList) && !CollectionUtils.isEmpty(userIdSet)) {
            Iterator userIdAndlabelIdToLabelStatsMap = allLabelBrandUserNumList.iterator();

            String userId;
            while(userIdAndlabelIdToLabelStatsMap.hasNext()) {
                CiLabelBrandUserNum e = (CiLabelBrandUserNum)userIdAndlabelIdToLabelStatsMap.next();
                userId = String.valueOf(e.getLabelId());
                String cityIdSet = String.valueOf(e.getCityId());
                Map isAdminUser = MapUtils.getMap(labelIdAndCityIdToLabelStatsMap, userId, new HashMap());
                isAdminUser.put(cityIdSet, e);
                labelIdAndCityIdToLabelStatsMap.put(userId, isAdminUser);
            }

            HashMap userIdAndlabelIdToLabelStatsMap1 = new HashMap();

            try {
                Iterator e1 = userIdSet.iterator();

                while(e1.hasNext()) {
                    userId = (String)e1.next();
                    HashSet cityIdSet1 = new HashSet();
                    boolean isAdminUser1 = false;
                    isAdminUser1 = PrivilegeServiceUtil.isAdminUser(userId);
                    boolean isProvinceUser = PrivilegeServiceUtil.isProvinceUser(userId);
                    if(!isAdminUser1 && !isProvinceUser) {
                        cityIdSet1 = new HashSet(PrivilegeServiceUtil.getUserCityIds(userId));
                    } else {
                        cityIdSet1.add(String.valueOf(-1));
                    }

                    Map labelIdToLabelStatsMap = MapUtils.getMap(userIdAndlabelIdToLabelStatsMap1, userId, new HashMap());

                    for(Iterator entryIter = labelIdAndCityIdToLabelStatsMap.entrySet().iterator(); entryIter.hasNext(); userIdAndlabelIdToLabelStatsMap1.put(userId, labelIdToLabelStatsMap)) {
                        Entry entry = (Entry)entryIter.next();
                        String labelId = (String)entry.getKey();
                        Map cityIdToLabelStatsMap = (Map)entry.getValue();
                        if(cityIdSet1.size() == 1) {
                            labelIdToLabelStatsMap.put(labelId, cityIdToLabelStatsMap.get(cityIdSet1.toArray()[0]));
                        } else {
                            long currentVal = 0L;
                            long ringNum = 0L;
                            double parentVal = 0.0D;
                            Iterator ciLabelBrandUserNum = cityIdSet1.iterator();

                            while(ciLabelBrandUserNum.hasNext()) {
                                String cityId = (String)ciLabelBrandUserNum.next();
                                CiLabelBrandUserNum ciLabelBrandUserNum1 = (CiLabelBrandUserNum)cityIdToLabelStatsMap.get(cityId);
                                if(null != ciLabelBrandUserNum1) {
                                    currentVal += ciLabelBrandUserNum1.getCustomNum().longValue();
                                    ringNum += ciLabelBrandUserNum1.getRingNum().longValue();
                                    if(0.0D != ciLabelBrandUserNum1.getProportion().doubleValue()) {
                                        parentVal += (double)ciLabelBrandUserNum1.getCustomNum().longValue() / ciLabelBrandUserNum1.getProportion().doubleValue();
                                    }
                                }
                            }

                            CiLabelBrandUserNum ciLabelBrandUserNum2 = new CiLabelBrandUserNum();
                            ciLabelBrandUserNum2.setCustomNum(Long.valueOf(currentVal));
                            ciLabelBrandUserNum2.setRingNum(Long.valueOf(ringNum));
                            if(0.0D != parentVal) {
                                ciLabelBrandUserNum2.setProportion(Double.valueOf((double)currentVal / parentVal));
                            }

                            ciLabelBrandUserNum2.setLabelId(Integer.valueOf(labelId));
                            labelIdToLabelStatsMap.put(labelId, ciLabelBrandUserNum2);
                        }
                    }
                }
            } catch (Exception var24) {
                var24.printStackTrace();
            }

            return userIdAndlabelIdToLabelStatsMap1;
        } else {
            return labelIdAndCityIdToLabelStatsMap;
        }
    }
}
