package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiLogStatAnalysisJDao;
import com.ailk.biapp.ci.entity.DimOpLogType;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.CiLogStatAnalysisModel;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.model.TreeNode;
import com.ailk.biapp.ci.service.ICiLogStatAnalysisService;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.WeekUtil;
import com.asiainfo.biframe.privilege.IUserCompany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiLogStatAnalysisServiceImpl implements ICiLogStatAnalysisService {
    @Autowired
    private ICiLogStatAnalysisJDao ciLogStatAnalysisJDao;

    public CiLogStatAnalysisServiceImpl() {
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiLogStatAnalysisModel> findAnaOpTypeInThePeriod(CiLogStatAnalysisModel logStatAnalysisModel) throws Exception {
        return this.ciLogStatAnalysisJDao.findAnaOpTypeInThePeriod(logStatAnalysisModel);
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiLogStatAnalysisModel> findAllLogViewDataInThePeriod(Pager pager, CiLogStatAnalysisModel logStatAnalysisModel) throws Exception {
        List resultList = this.ciLogStatAnalysisJDao.findAllLogViewDataInThePeriod(pager, logStatAnalysisModel);
        CiLogStatAnalysisModel total = new CiLogStatAnalysisModel();
        Integer op1 = Integer.valueOf(0);
        Integer op2 = Integer.valueOf(0);
        Integer op3 = Integer.valueOf(0);
        Integer op4 = Integer.valueOf(0);
        Integer op5 = Integer.valueOf(0);
        Integer op6 = Integer.valueOf(0);
        Integer op7 = Integer.valueOf(0);
        Iterator i$ = resultList.iterator();

        while(i$.hasNext()) {
            CiLogStatAnalysisModel c = (CiLogStatAnalysisModel)i$.next();
            if(c.getOpTimes1() != null) {
                op1 = Integer.valueOf(op1.intValue() + Integer.parseInt(c.getOpTimes1()));
            }

            if(c.getOpTimes2() != null) {
                op2 = Integer.valueOf(op2.intValue() + Integer.parseInt(c.getOpTimes2()));
            }

            if(c.getOpTimes3() != null) {
                op3 = Integer.valueOf(op3.intValue() + Integer.parseInt(c.getOpTimes3()));
            }

            if(c.getOpTimes4() != null) {
                op4 = Integer.valueOf(op4.intValue() + Integer.parseInt(c.getOpTimes4()));
            }

            if(c.getOpTimes5() != null) {
                op5 = Integer.valueOf(op5.intValue() + Integer.parseInt(c.getOpTimes5()));
            }

            if(c.getOpTimes6() != null) {
                op6 = Integer.valueOf(op6.intValue() + Integer.parseInt(c.getOpTimes6()));
            }

            if(c.getOpTimes7() != null) {
                op7 = Integer.valueOf(op7.intValue() + Integer.parseInt(c.getOpTimes7()));
            }
        }

        total.setOpTimes1(op1.toString());
        total.setOpTimes2(op2.toString());
        total.setOpTimes3(op3.toString());
        total.setOpTimes4(op4.toString());
        total.setOpTimes5(op5.toString());
        total.setOpTimes6(op6.toString());
        total.setOpTimes7(op7.toString());
        resultList.add(total);
        return resultList;
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<Map<String, Object>> findOneDeptOpLogTrend(CiLogStatAnalysisModel logStatAnalysisModel) throws Exception {
        ArrayList deptMultOpTypesChartList = new ArrayList();
        List deptMultOpTypesChartListTemp = this.ciLogStatAnalysisJDao.findOneDeptOpLogTrend(logStatAnalysisModel);
        List dateList = DateUtil.dateList(logStatAnalysisModel.getBeginDate(), logStatAnalysisModel.getEndDate());
        Object[] keys = null;
        if(deptMultOpTypesChartListTemp.size() > 0) {
            Map i = (Map)deptMultOpTypesChartListTemp.get(0);
            Set date = i.keySet();
            keys = date.toArray();
        } else {
            String[] var12 = logStatAnalysisModel.getOpTypeId().split(",");
            int var14 = Math.min(var12.length, 10);
            keys = new Object[var14 + 3];
            keys[0] = "DATA_DATE";
            keys[1] = "SECOND_DEPT_ID";
            keys[2] = "SECOND_DEPT_NAME";

            for(int flag = 0; flag < var14; ++flag) {
                keys[flag + 3] = "OP_TIMES_" + (flag + 1);
            }
        }

        label77:
        for(int var13 = 0; var13 < dateList.size(); ++var13) {
            String var15 = (String)dateList.get(var13);
            boolean var16 = false;
            Iterator iterator = deptMultOpTypesChartListTemp.iterator();

            while(true) {
                String dateStr;
                do {
                    Map map;
                    if(!iterator.hasNext()) {
                        if(!var16) {
                            HashMap var17 = new HashMap();
                            var17.put("DATA_DATE", var15);

                            for(int var18 = 0; var18 < keys.length; ++var18) {
                                dateStr = keys[var18].toString();
                                if(!dateStr.equals("DATA_DATE") && dateStr != "DATA_DATE") {
                                    if(!dateStr.equals("SECOND_DEPT_ID") && !dateStr.equals("SECOND_DEPT_NAME")) {
                                        var17.put(dateStr, "0");
                                    } else {
                                        var17.put(dateStr, " ");
                                    }
                                }
                            }

                            deptMultOpTypesChartList.add(var17);
                            continue label77;
                        } else {
                            iterator = deptMultOpTypesChartListTemp.iterator();

                            while(true) {
                                do {
                                    if(!iterator.hasNext()) {
                                        continue label77;
                                    }

                                    map = (Map)iterator.next();
                                    dateStr = (String)map.get("DATA_DATE");
                                } while(!dateStr.equals(var15) && dateStr != var15);

                                deptMultOpTypesChartList.add(map);
                            }
                        }
                    }

                    map = (Map)iterator.next();
                    dateStr = (String)map.get("DATA_DATE");
                } while(!dateStr.equals(var15) && dateStr != var15);

                var16 = true;
            }
        }

        return deptMultOpTypesChartList;
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<Map<String, Object>> findLogUserDetilList(Pager pager, CiLogStatAnalysisModel logStatAnalysisModel) throws Exception {
        return this.ciLogStatAnalysisJDao.findLogUserDetilList(pager, logStatAnalysisModel);
    }

    public int findLogUserDetilListCount(CiLogStatAnalysisModel logStatAnalysisModel) throws Exception {
        return this.ciLogStatAnalysisJDao.findLogUserDetilListCount(logStatAnalysisModel);
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiLogStatAnalysisModel> findOneOpLogTypeSpread(CiLogStatAnalysisModel logStatAnalysisModel) throws Exception {
        return this.ciLogStatAnalysisJDao.findOneOpLogTypeSpread(logStatAnalysisModel);
    }

    public List<CiLogStatAnalysisModel> findOpTypeChar(CiLogStatAnalysisModel ciLogStatAnalysisModel) throws Exception {
        List list = this.ciLogStatAnalysisJDao.findOpTypeChar(ciLogStatAnalysisModel);
        List dateList = DateUtil.dateList(ciLogStatAnalysisModel.getBeginDate(), ciLogStatAnalysisModel.getEndDate());
        ArrayList dateList2 = new ArrayList();
        Iterator i$ = list.iterator();

        while(i$.hasNext()) {
            CiLogStatAnalysisModel s = (CiLogStatAnalysisModel)i$.next();
            dateList2.add(s.getDataDate());
            s.setWeek(WeekUtil.isWeekend(s.getDataDate(), "yyyyMMdd"));
        }

        dateList.removeAll(dateList2);
        i$ = dateList.iterator();

        while(i$.hasNext()) {
            String s1 = (String)i$.next();
            CiLogStatAnalysisModel model = new CiLogStatAnalysisModel();
            model.setDataDate(s1);
            model.setOpTimes("0");
            model.setWeek(WeekUtil.isWeekend(model.getDataDate(), "yyyyMMdd"));
            list.add(model);
        }

        Collections.sort(list);
        return list;
    }

    public List<DimOpLogType> findOpTypeListByParentId(String parentId) throws Exception {
        return this.ciLogStatAnalysisJDao.findOpTypeListByParentId(parentId);
    }

    public List<TreeNode> queryDeptTree() throws CIServiceException {
        ArrayList treeNodeList = new ArrayList();

        try {
            List e = PrivilegeServiceUtil.getAllUserCompany();
            if(null != e) {
                Iterator i$ = e.iterator();

                while(i$.hasNext()) {
                    IUserCompany company = (IUserCompany)i$.next();
                    TreeNode treeNode = this.newTreeNode(company);
                    treeNodeList.add(treeNode);
                }
            }
        } catch (Exception var6) {
            Log.error("获取全部部门信息失败", var6);
            var6.printStackTrace();
        }

        return treeNodeList;
    }

    private TreeNode newTreeNode(IUserCompany company) {
        TreeNode treeNode = new TreeNode();
        treeNode.setId(company.getDeptid() + "");
        treeNode.setName(company.getTitle());
        treeNode.setTip(company.getTitle());
        treeNode.setClick(Boolean.TRUE);
        if(null != company.getParentid() && company.getParentid().intValue() != -1) {
            treeNode.setpId(company.getParentid() + "");
            treeNode.setIsParent(Boolean.FALSE);
            treeNode.setOpen(Boolean.FALSE);
        } else {
            treeNode.setpId("0");
            treeNode.setOpen(Boolean.TRUE);
            treeNode.setIsParent(Boolean.TRUE);
        }

        return treeNode;
    }

    public List<CiLogStatAnalysisModel> findAllSecondLogViewDataInThePeriod(Pager pager, CiLogStatAnalysisModel logStatAnalysisModel) throws Exception {
        return this.ciLogStatAnalysisJDao.findAllSecondLogViewDataInThePeriod(pager, logStatAnalysisModel);
    }

    public int findAllSecondLogViewDataInThePeriodCount(CiLogStatAnalysisModel logStatAnalysisModel) throws Exception {
        return this.ciLogStatAnalysisJDao.findAllSecondLogViewDataInThePeriodCount(logStatAnalysisModel);
    }

    public List<CiLogStatAnalysisModel> findtitle(CiLogStatAnalysisModel logStatAnalysisModel) throws Exception {
        return this.ciLogStatAnalysisJDao.findtitle(logStatAnalysisModel);
    }

    public List<CiLogStatAnalysisModel> findAllSecondLogViewDataInThePeriod(CiLogStatAnalysisModel logStatAnalysisModel) throws Exception {
        return this.ciLogStatAnalysisJDao.findAllSecondLogViewDataInThePeriod(logStatAnalysisModel);
    }

    public List<Map<String, Object>> findLogUserDetilList(CiLogStatAnalysisModel logStatAnalysisModel) throws Exception {
        return this.ciLogStatAnalysisJDao.findLogUserDetilList(logStatAnalysisModel);
    }
}
