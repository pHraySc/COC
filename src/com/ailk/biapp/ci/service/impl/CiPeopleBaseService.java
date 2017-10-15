package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiTagInfoJDao;
import com.ailk.biapp.ci.entity.CiTagInfo;
import com.ailk.biapp.ci.model.CiTagTypeModel;
import com.ailk.biapp.ci.service.ICiPeopleBaseService;
import com.ailk.biapp.ci.util.DateUtil;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiPeopleBaseService implements ICiPeopleBaseService {
    @Autowired
    private ICiTagInfoJDao ciTagInfoJDao;

    public CiPeopleBaseService() {
    }

    public Integer queryPhoneNumExists(String phoneNum) {
        return this.ciTagInfoJDao.selectPhoneNumExists(phoneNum);
    }

    public List<CiTagTypeModel> queryProductNoTag(String phoneNum) {
        new ArrayList();
        new ArrayList();
        ArrayList effectiveTagInfoList = new ArrayList();
        new HashMap();
        HashMap ciTagInfoMap = new HashMap();
        Map allProductNoTag = this.ciTagInfoJDao.selectProductNoTag(phoneNum);
        List allTagInfoList = this.ciTagInfoJDao.selectAllTag();
        List result = this.ciTagInfoJDao.selectAllTagType();
        Iterator i;
        CiTagInfo item;
        if(allTagInfoList != null && allTagInfoList.size() > 0) {
            i = allTagInfoList.iterator();

            while(i.hasNext()) {
                item = (CiTagInfo)i.next();
                String list = item.getTagId();
                Object obj = allProductNoTag.get(list);
                String value = this.Obj2String(obj);
                if(value != null) {
                    if("1".equals(item.getIsIdentify())) {
                        if("1".equals(value)) {
                            effectiveTagInfoList.add(item);
                        }
                    } else {
                        if(item.getTagUnit() == null) {
                            item.setTagUnit("");
                        }

                        item.setTagName(item.getTagName() + "£º" + value + item.getTagUnit());
                        effectiveTagInfoList.add(item);
                    }
                }
            }
        }

        i = effectiveTagInfoList.iterator();

        while(i.hasNext()) {
            item = (CiTagInfo)i.next();
            Object var13 = (List)ciTagInfoMap.get(item.getTagType());
            if(var13 == null) {
                var13 = new ArrayList();
            }

            ((List)var13).add(item);
            ciTagInfoMap.put(item.getTagType(), var13);
        }

        if(result != null && result.size() > 0) {
            for(int var12 = 0; var12 < result.size(); ++var12) {
                ((CiTagTypeModel)result.get(var12)).setCiTagInfoList((List)ciTagInfoMap.get(((CiTagTypeModel)result.get(var12)).getId()));
            }
        }

        return result;
    }

    private String Obj2String(Object obj) {
        if(obj == null) {
            return null;
        } else {
            String result = null;
            if(obj instanceof Date) {
                result = DateUtil.date2String((Date)obj, "yyyy-MM-dd");
            } else if(obj instanceof BigDecimal) {
                result = ((BigDecimal)obj).toString();
            } else if(obj instanceof Timestamp) {
                Timestamp ts = (Timestamp)obj;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                result = sdf.format(ts);
            } else {
                result = (String)obj;
            }

            return result;
        }
    }
}
