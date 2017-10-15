package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiProductVerifyInfoJDao;
import com.ailk.biapp.ci.entity.CiVerifyFilterProcess;
import com.ailk.biapp.ci.entity.CiVerifyMm;
import com.ailk.biapp.ci.entity.CiVerifyRuleInfo;
import com.ailk.biapp.ci.entity.CiVerifyRuleRel;
import com.ailk.biapp.ci.entity.DimVerifyIndexShow;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ICiProductVerifyInfoService;
import com.ailk.biapp.ci.util.DateUtil;

import edu.emory.mathcs.backport.java.util.Collections;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiProductVerifyInfoServiceImpl implements ICiProductVerifyInfoService {
    private Logger log = Logger.getLogger(CiProductVerifyInfoServiceImpl.class);
    @Autowired
    private ICiProductVerifyInfoJDao ciProductVerifyJDao;

    public CiProductVerifyInfoServiceImpl() {
    }

    public String queryRuleDesc(String offerId) throws CIServiceException {
        String ruleDesc = "";

        try {
            ruleDesc = this.ciProductVerifyJDao.selectRuleDesc(offerId);
            return ruleDesc;
        } catch (Exception var4) {
            this.log.error("", var4);
            throw new CIServiceException("查询产品规则报错");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiVerifyRuleRel> queryVerifyRuleRelList(String offerId) throws CIServiceException {
        List list = null;

        try {
            list = this.ciProductVerifyJDao.selectVerifyRuleRelList(offerId);
            return list;
        } catch (Exception var4) {
            this.log.error("", var4);
            throw new CIServiceException("查询产品规则信息表格报错");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiVerifyMm> queryVerifyMmList(String offerId) throws CIServiceException {
        ArrayList verifyMmList = null;

        try {
            List e = this.ciProductVerifyJDao.selectVerifyMmList(offerId);
            verifyMmList = new ArrayList();
            if(e != null && e.size() < 6) {
                CiVerifyMm verifyMm = (CiVerifyMm)e.get(0);
                String dataDate = verifyMm.getDataDate();

                for(int i = 6 - e.size(); i > 0; --i) {
                    String frontMonth = DateUtil.getFrontMonth(1, dataDate);
                    dataDate = frontMonth;
                    CiVerifyMm entity = new CiVerifyMm();
                    entity.setDataDate(frontMonth);
                    entity.setUpgradeMultiple(Double.valueOf(0.0D));
                    entity.setRecall(Double.valueOf(0.0D));
                    entity.setPotentialUserNum(Integer.valueOf(0));
                    verifyMmList.add(entity);
                }

                Collections.sort(verifyMmList, new Comparator() {
                    public int compare(CiVerifyMm m1, CiVerifyMm m2) {
                        return m1.getDataDate().compareTo(m2.getDataDate());
                    }

					@Override
					public int compare(Object o1, Object o2) {
						// TODO Auto-generated method stub
						return 0;
					}
                });
            }

            verifyMmList.addAll(e);
            e.clear();
            e = null;
            return verifyMmList;
        } catch (Exception var9) {
            this.log.error("查询产品规则历史信息报错", var9);
            throw new CIServiceException("查询产品规则历史信息报错");
        }
    }

    public List<CiVerifyMm> queryVerifyMmResultList(String offerId) throws CIServiceException {
        List list = null;

        try {
            DecimalFormat e = (DecimalFormat)NumberFormat.getPercentInstance();
            e.setMaximumFractionDigits(2);
            DecimalFormat format2 = (DecimalFormat)NumberFormat.getNumberInstance();
            format2.applyPattern("0.##");
            list = this.ciProductVerifyJDao.selectVerifyMmList(offerId);

            int i;
            for(i = 0; i < list.size(); ++i) {
                ((CiVerifyMm)list.get(i)).setRecallStr(e.format(((CiVerifyMm)list.get(i)).getRecall()));
                ((CiVerifyMm)list.get(i)).setUpgradeMultipleStr(format2.format(((CiVerifyMm)list.get(i)).getUpgradeMultiple()));
            }

            if(list.size() > 1) {
                for(i = 1; i < list.size(); ++i) {
                    if(((CiVerifyMm)list.get(i)).getStarsId().shortValue() > ((CiVerifyMm)list.get(i - 1)).getStarsId().shortValue()) {
                        ((CiVerifyMm)list.get(i)).setStartRise(1);
                    } else if(((CiVerifyMm)list.get(i)).getStarsId().intValue() == ((CiVerifyMm)list.get(i - 1)).getStarsId().intValue()) {
                        ((CiVerifyMm)list.get(i)).setStartRise(0);
                    } else {
                        ((CiVerifyMm)list.get(i)).setStartRise(-1);
                    }
                }
            }

            return list;
        } catch (Exception var6) {
            this.log.error("", var6);
            throw new CIServiceException("查询产品规则历史信息报错");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiVerifyFilterProcess> queryLabelProcess(String offerId) throws CIServiceException {
        List list = null;

        try {
            list = this.ciProductVerifyJDao.selectLabelProcess(offerId);
            return list;
        } catch (Exception var4) {
            this.log.error("", var4);
            throw new CIServiceException("查询产品规则历史信息");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiVerifyFilterProcess> queryLabelProcessLevel(String offerId, String level) throws CIServiceException {
        List list = null;

        try {
            list = this.ciProductVerifyJDao.selectLabelProcessLevel(offerId, level);
            return list;
        } catch (Exception var5) {
            this.log.error("", var5);
            throw new CIServiceException("查询产品规则历史信息");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiVerifyRuleRel> queryFinalPotentialUserRule(String offerId) throws CIServiceException {
        List list = null;

        try {
            list = this.ciProductVerifyJDao.selectFinalPotentialUserRule(offerId);
            return list;
        } catch (Exception var4) {
            this.log.error("", var4);
            throw new CIServiceException("查询产品规则历史信息");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiVerifyFilterProcess> queryIndexProcess(String offerId) throws CIServiceException {
        List list = null;

        try {
            list = this.ciProductVerifyJDao.selectIndexProcess(offerId);
            return list;
        } catch (Exception var4) {
            this.log.error("", var4);
            throw new CIServiceException("查询产品规则历史信息");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiVerifyFilterProcess> queryIndexProcessLevel(String offerId, String level) throws CIServiceException {
        List list = null;

        try {
            list = this.ciProductVerifyJDao.selectIndexProcessLevel(offerId, level);
            return list;
        } catch (Exception var5) {
            this.log.error("", var5);
            throw new CIServiceException("查询产品规则历史信息");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiVerifyRuleInfo> queryIndexRuleDescribe(String offerId) throws CIServiceException {
        List list = null;

        try {
            list = this.ciProductVerifyJDao.selectIndexRuleDescribe(offerId);
            return list;
        } catch (Exception var4) {
            this.log.error("", var4);
            throw new CIServiceException("查询产品规则历史信息");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiVerifyRuleInfo> queryLabelRule(String offerId) throws CIServiceException {
        List list = null;

        try {
            list = this.ciProductVerifyJDao.selectLabelRule(offerId);
            return list;
        } catch (Exception var4) {
            this.log.error("", var4);
            throw new CIServiceException("查询产品规则历史信息");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<DimVerifyIndexShow> queryIndexList() throws CIServiceException {
        List list = null;

        try {
            list = this.ciProductVerifyJDao.selectIndexList();
            return list;
        } catch (Exception var3) {
            this.log.error("", var3);
            throw new CIServiceException("查询指标选择报错");
        }
    }
}
