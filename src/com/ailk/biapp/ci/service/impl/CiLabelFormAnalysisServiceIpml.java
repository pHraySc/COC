package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiLabelFormAnalysisJDao;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.CiBrandHisModel;
import com.ailk.biapp.ci.model.CiCityHisModel;
import com.ailk.biapp.ci.model.CiLabelFormModel;
import com.ailk.biapp.ci.model.CiLabelFormTrendModel;
import com.ailk.biapp.ci.model.CiVipHisModel;
import com.ailk.biapp.ci.service.ICiLabelFormAnalysisService;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.IdToName;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CiLabelFormAnalysisServiceIpml implements ICiLabelFormAnalysisService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ICiLabelFormAnalysisJDao ciLabelFormAnalysisJDao;

    public CiLabelFormAnalysisServiceIpml() {
    }

    public List<CiLabelFormModel> queryBrandFormChartData(CiLabelFormModel ciLabelFormModel) throws CIServiceException {
        List modelList = null;
        String[] colors = CiUtil.getColors();
        int totalNum = 0;

        try {
            modelList = this.ciLabelFormAnalysisJDao.selectBrandFormChartData(ciLabelFormModel);
            if(null != modelList) {
                for(int e = 0; e < modelList.size(); ++e) {
                    ((CiLabelFormModel)modelList.get(e)).setColor(colors[e]);
                    totalNum += ((CiLabelFormModel)modelList.get(e)).getCustomNum().intValue();
                    ((CiLabelFormModel)modelList.get(e)).setBrand_name(IdToName.getName("DIM_BRAND", ((CiLabelFormModel)modelList.get(e)).getBrandId()));
                }
            }

            ciLabelFormModel.setCustomNum(Integer.valueOf(totalNum));
            return modelList;
        } catch (Exception var6) {
            var6.printStackTrace();
            this.log.error("��ȡƷ�ƹ��ɷ�������ʧ��", var6);
            throw new CIServiceException("��ȡƷ�ƹ��ɷ�������ʧ��");
        }
    }

    public List<CiLabelFormModel> querySubBrandFormChartData(CiLabelFormModel ciLabelFormModel) throws CIServiceException {
        List modelList = null;

        try {
            modelList = this.ciLabelFormAnalysisJDao.selectSubBrandFormChartData(ciLabelFormModel);
            if(null != modelList) {
                for(int e = 0; e < modelList.size(); ++e) {
                    ((CiLabelFormModel)modelList.get(e)).setBrand_name(IdToName.getName("DIM_BRAND", ((CiLabelFormModel)modelList.get(e)).getBrandId()));
                }
            }

            return modelList;
        } catch (Exception var4) {
            var4.printStackTrace();
            this.log.error("��ȡ��Ʒ�ƹ��ɷ�������ʧ��", var4);
            throw new CIServiceException("��ȡ��Ʒ�ƹ��ɷ�������ʧ��");
        }
    }

    public List<CiLabelFormModel> queryCityFormChartData(CiLabelFormModel ciLabelFormModel) throws CIServiceException {
        List modelList = null;
        String[] colors = CiUtil.getColors();
        int totalNum = 0;

        try {
            modelList = this.ciLabelFormAnalysisJDao.selectCityFormChartData(ciLabelFormModel);
            if(null != modelList) {
                for(int e = 0; e < modelList.size(); ++e) {
                    ((CiLabelFormModel)modelList.get(e)).setColor(colors[e]);
                    totalNum += ((CiLabelFormModel)modelList.get(e)).getCustomNum().intValue();
                    ((CiLabelFormModel)modelList.get(e)).setCityName(IdToName.getName("DIM_CITY", ((CiLabelFormModel)modelList.get(e)).getCityId()));
                }
            }

            ciLabelFormModel.setCustomNum(Integer.valueOf(totalNum));
            return modelList;
        } catch (Exception var6) {
            var6.printStackTrace();
            this.log.error("��ȡ���򹹳ɷ�������ʧ��", var6);
            throw new CIServiceException("��ȡ���򹹳ɷ�������ʧ��");
        }
    }

    public List<CiLabelFormModel> queryVipFormChartData(CiLabelFormModel ciLabelFormModel) throws CIServiceException {
        List modelList = null;
        String[] colors = CiUtil.getColors();
        int totalNum = 0;

        try {
            modelList = this.ciLabelFormAnalysisJDao.selectVipFormChartData(ciLabelFormModel);
            if(null != modelList) {
                for(int e = 0; e < modelList.size(); ++e) {
                    ((CiLabelFormModel)modelList.get(e)).setColor(colors[e]);
                    totalNum += ((CiLabelFormModel)modelList.get(e)).getCustomNum().intValue();
                    ((CiLabelFormModel)modelList.get(e)).setVipLevelName(IdToName.getName("DIM_VIP_LEVEL", ((CiLabelFormModel)modelList.get(e)).getVipLevelId()));
                }
            }

            ciLabelFormModel.setCustomNum(Integer.valueOf(totalNum));
            return modelList;
        } catch (Exception var6) {
            var6.printStackTrace();
            this.log.error("��ȡVIP�ȼ����ɷ�������ʧ��", var6);
            throw new CIServiceException("��ȡVIP�ȼ����ɷ�������ʧ��");
        }
    }

    public List<CiLabelFormTrendModel> queryBrandTrendChartData(CiLabelFormModel ciLabelFormModel) throws CIServiceException {
        List modelList = null;
        String[] colors = CiUtil.getColors();

        try {
            modelList = this.ciLabelFormAnalysisJDao.selectBrandTrendChartData(ciLabelFormModel);
            if(null != modelList) {
                for(int e = 0; e < modelList.size(); ++e) {
                    ((CiLabelFormTrendModel)modelList.get(e)).setColor(colors[e]);
                    ((CiLabelFormTrendModel)modelList.get(e)).setBrandName(IdToName.getName("DIM_BRAND", ((CiLabelFormTrendModel)modelList.get(e)).getBrandId()));
                }
            }

            return modelList;
        } catch (Exception var5) {
            var5.printStackTrace();
            this.log.error("��ȡƷ�ƹ��ɷ�����������ʧ��", var5);
            throw new CIServiceException("��ȡƷ�ƹ��ɷ�����������ʧ��");
        }
    }

    public List<CiLabelFormTrendModel> queryCityTrendChartData(CiLabelFormModel ciLabelFormModel) throws CIServiceException {
        List modelList = null;
        String[] colors = CiUtil.getColors();

        try {
            modelList = this.ciLabelFormAnalysisJDao.selectCityTrendChartData(ciLabelFormModel);
            if(null != modelList) {
                for(int e = 0; e < modelList.size(); ++e) {
                    ((CiLabelFormTrendModel)modelList.get(e)).setColor(colors[e]);
                    ((CiLabelFormTrendModel)modelList.get(e)).setCityName(IdToName.getName("DIM_CITY", ((CiLabelFormTrendModel)modelList.get(e)).getCityId()));
                }
            }

            return modelList;
        } catch (Exception var5) {
            var5.printStackTrace();
            this.log.error("��ȡ����ͻ�����ʷ��������ʧ��", var5);
            throw new CIServiceException("��ȡ����ͻ�����ʷ��������ʧ��");
        }
    }

    public List<CiLabelFormTrendModel> queryVipTrendChartData(CiLabelFormModel ciLabelFormModel) throws CIServiceException {
        List modelList = null;
        String[] colors = CiUtil.getColors();

        try {
            modelList = this.ciLabelFormAnalysisJDao.selectVipTrendChartData(ciLabelFormModel);
            if(null != modelList) {
                for(int e = 0; e < modelList.size(); ++e) {
                    ((CiLabelFormTrendModel)modelList.get(e)).setColor(colors[e]);
                    ((CiLabelFormTrendModel)modelList.get(e)).setVipLevelName(IdToName.getName("DIM_VIP_LEVEL", ((CiLabelFormTrendModel)modelList.get(e)).getVipLevelId()));
                }
            }

            return modelList;
        } catch (Exception var5) {
            var5.printStackTrace();
            this.log.error("��ȡVIP�ȼ����ɷ�����������ʧ��", var5);
            throw new CIServiceException("��ȡVIP�ȼ����ɷ�����������ʧ��");
        }
    }

    public List<CiBrandHisModel> queryBrandHistoryData(int currentPage, int pageSize, List<String> dateList, CiLabelFormModel ciLabelFormModel) throws CIServiceException {
        List modelList = null;

        try {
            modelList = this.ciLabelFormAnalysisJDao.selectBrandHistoryData(currentPage, pageSize, dateList, ciLabelFormModel);
            return modelList;
        } catch (Exception var7) {
            var7.printStackTrace();
            this.log.error("��ȡƷ�ƹ��ɷ�����ʷ����ʧ��", var7);
            throw new CIServiceException("��ȡƷ�ƹ��ɷ�����ʷ����ʧ��");
        }
    }

    public List<CiVipHisModel> queryVipHistoryData(List<String> dateList, CiLabelFormModel ciLabelFormModel) throws CIServiceException {
        List modelList = null;

        try {
            modelList = this.ciLabelFormAnalysisJDao.selectVipHistoryData(dateList, ciLabelFormModel);
            return modelList;
        } catch (Exception var5) {
            var5.printStackTrace();
            this.log.error("��ȡVIP�ȼ����ɷ�����ʷ����ʧ��", var5);
            throw new CIServiceException("��ȡVIP�ȼ����ɷ�����ʷ����ʧ��");
        }
    }

    public List<CiLabelFormModel> queryCityTrendChartDataByCityId(CiLabelFormModel ciLabelFormModel) throws CIServiceException {
        List modelList = null;

        try {
            modelList = this.ciLabelFormAnalysisJDao.selectCityTrendChartDataByCityId(ciLabelFormModel);
            if(null != modelList) {
                for(int e = 0; e < modelList.size(); ++e) {
                    ((CiLabelFormModel)modelList.get(e)).setCityName(IdToName.getName("DIM_CITY", ((CiLabelFormModel)modelList.get(e)).getCityId()));
                }
            }

            return modelList;
        } catch (Exception var4) {
            var4.printStackTrace();
            this.log.error("��ȡָ������Ľ������¿ͻ�����������ʧ��", var4);
            throw new CIServiceException("��ȡָ������Ľ������¿ͻ�����������ʧ��");
        }
    }

    public List<CiLabelFormModel> queryMoreCityTrendChartDataByCityId(CiLabelFormModel ciLabelFormModel) throws CIServiceException {
        List modelList = null;

        try {
            modelList = this.ciLabelFormAnalysisJDao.selectMoreCityTrendChartDataByCityId(ciLabelFormModel);
            if(null != modelList) {
                for(int e = 0; e < modelList.size(); ++e) {
                    ((CiLabelFormModel)modelList.get(e)).setCityName(IdToName.getName("DIM_CITY", ((CiLabelFormModel)modelList.get(e)).getCityId()));
                }
            }

            return modelList;
        } catch (Exception var4) {
            var4.printStackTrace();
            this.log.error("��ȡָ������Ľ������¿ͻ�����������ʧ��", var4);
            throw new CIServiceException("��ȡָ������Ľ������¿ͻ�����������ʧ��");
        }
    }

    public List<CiLabelFormModel> queryVipTrendChartDataByVipLevelId(CiLabelFormModel ciLabelFormModel) throws CIServiceException {
        List modelList = null;

        try {
            modelList = this.ciLabelFormAnalysisJDao.selectVipTrendChartDataByVipLevelId(ciLabelFormModel);
            if(null != modelList) {
                for(int e = 0; e < modelList.size(); ++e) {
                    ((CiLabelFormModel)modelList.get(e)).setVipLevelName(IdToName.getName("DIM_VIP_LEVEL", ((CiLabelFormModel)modelList.get(e)).getVipLevelId()));
                }
            }

            return modelList;
        } catch (Exception var4) {
            var4.printStackTrace();
            this.log.error("��ȡָ��VIP�ȼ��Ľ������¿ͻ�����������ʧ��", var4);
            throw new CIServiceException("��ȡָ��VIP�ȼ��Ľ������¿ͻ�����������ʧ��");
        }
    }

    public List<CiCityHisModel> queryCityHistoryData(List<String> dateList, CiLabelFormModel ciLabelFormModel) throws CIServiceException {
        List modelList = null;

        try {
            modelList = this.ciLabelFormAnalysisJDao.selectCityHistoryData(dateList, ciLabelFormModel);
            return modelList;
        } catch (Exception var5) {
            var5.printStackTrace();
            this.log.error("��ȡ���򹹳ɷ�����ʷ����ʧ��", var5);
            throw new CIServiceException("��ȡ���򹹳ɷ�����ʷ����ʧ��");
        }
    }

    public long queryBrandHistoryDataCount(CiLabelFormModel ciLabelFormModel) throws CIServiceException {
        long count = 0L;

        try {
            count = this.ciLabelFormAnalysisJDao.selectBrandHistoryDataCount(ciLabelFormModel);
            return count;
        } catch (Exception var5) {
            var5.printStackTrace();
            this.log.error("��ȡƷ�ƹ��ɷ�����ʷ����ʧ��", var5);
            throw new CIServiceException("��ȡƷ�ƹ��ɷ�����ʷ����ʧ��");
        }
    }

    public long queryCityHistoryDataCount(CiLabelFormModel ciLabelFormModel) throws CIServiceException {
        long count = 0L;

        try {
            count = this.ciLabelFormAnalysisJDao.selectCityHistoryDataCount(ciLabelFormModel);
            return count;
        } catch (Exception var5) {
            var5.printStackTrace();
            this.log.error("��ȡ���򹹳ɷ�����ʷ����ʧ��", var5);
            throw new CIServiceException("��ȡ���򹹳ɷ�����ʷ����ʧ��");
        }
    }

    public long queryVipHistoryDataCount(CiLabelFormModel ciLabelFormModel) throws CIServiceException {
        long count = 0L;

        try {
            count = this.ciLabelFormAnalysisJDao.selectVipHistoryDataCount(ciLabelFormModel);
            return count;
        } catch (Exception var5) {
            var5.printStackTrace();
            this.log.error("��ȡVIP�ȼ����ɷ�����ʷ����ʧ��", var5);
            throw new CIServiceException("��ȡVIP�ȼ����ɷ�����ʷ����ʧ��");
        }
    }
}
