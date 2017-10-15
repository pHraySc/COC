package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.CiCustomGroupForm;
import com.ailk.biapp.ci.entity.DimCity;
import com.ailk.biapp.ci.model.CiCustomersFormTrendModel;
import com.ailk.biapp.ci.model.CiLabelFormModel;
import com.ailk.biapp.ci.model.CiLabelFormTrendModel;
import com.ailk.biapp.ci.model.CityMap;
import com.ailk.biapp.ci.model.KpiMapData;
import com.ailk.biapp.ci.service.ICiLabelFormAnalysisService;
import com.ailk.biapp.ci.service.ICityMapService;
import com.ailk.biapp.ci.service.ICustomersAnalysisService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiMapAction extends CIBaseAction {
    @Autowired
    private ICityMapService cityMapService;
    @Autowired
    private ICiLabelFormAnalysisService ciLabelFormAnalysisService;
    @Autowired
    private ICustomersAnalysisService customersAnalysisService;
    private ArrayList<String> cityNameList = new ArrayList();
    private ArrayList<String> cityValueList = new ArrayList();
    private String cityId;
    private String kpiType;
    private List<CiLabelFormTrendModel> labelFormTrendList;
    private List<CiCustomersFormTrendModel> customGroupFormList;
    private CiLabelFormModel ciLabelFormModel;
    private CiCustomGroupForm ciCustomGroupForm;
    private Map<String, CiLabelFormTrendModel> labelFormTrendMap = new HashMap();
    private Map<String, CiCustomersFormTrendModel> customGroupFormMap = new HashMap();
    private List<DimCity> cityList;

    public CiMapAction() {
    }

    public String showMap() throws Exception {
        String currentCityId = StringUtils.isBlank(this.cityId)?this.getUserCityId():this.cityId;
        this.labelFormTrendList = this.ciLabelFormAnalysisService.queryCityTrendChartData(this.ciLabelFormModel);
        Iterator cityList = this.labelFormTrendList.iterator();

        while(cityList.hasNext()) {
            CiLabelFormTrendModel cityMap = (CiLabelFormTrendModel)cityList.next();
            this.labelFormTrendMap.put(cityMap.getCityId().toString(), cityMap);
        }

        List cityList1 = this.subCitys(currentCityId);
        CityMap cityMap1 = null;

        try {
            cityMap1 = this.cityMapService.renderMap(currentCityId, this.getKpiData(cityList1), cityList1);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        this.getContext().put("rootCityId", "999");
        this.getContext().put("cityMap", cityMap1);
        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_CITY_CHART_VIEW", this.ciLabelFormModel.getLabelId().toString(), "", "标签分析->标签地域构成图查看（地图显示）,【标签Id:" + this.ciLabelFormModel.getLabelId() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "showMap";
    }

    public String showCustomerMap() throws Exception {
        String currentCityId = StringUtils.isBlank(this.cityId)?this.getUserCityId():this.cityId;
        this.customGroupFormList = this.customersAnalysisService.queryCityTrendChartData(this.ciCustomGroupForm);
        Iterator cityList = this.customGroupFormList.iterator();

        while(cityList.hasNext()) {
            CiCustomersFormTrendModel cityMap = (CiCustomersFormTrendModel)cityList.next();
            this.customGroupFormMap.put(cityMap.getCityId().toString(), cityMap);
        }

        List cityList1 = this.subCitys(currentCityId);
        CityMap cityMap1 = null;

        try {
            cityMap1 = this.cityMapService.renderMap(currentCityId, this.getCustomersGroupKpiData(cityList1), cityList1);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        this.getContext().put("rootCityId", "999");
        this.getContext().put("cityMap", cityMap1);
        return "showMap";
    }

    private List<String> subCitys(String cityId) {
        ArrayList cityIdList = new ArrayList();
        CacheBase cache = CacheBase.getInstance();
        this.cityList =(List<DimCity>) cache.getObjectList("DIM_CITY");
        Iterator i$ = this.cityList.iterator();

        while(i$.hasNext()) {
            DimCity city = (DimCity)i$.next();
            cityIdList.add(city.getCityId().toString());
        }

        return cityIdList;
    }

    private List<KpiMapData> getKpiData(List<String> cityList) {
        ArrayList results = new ArrayList();
        Iterator i$ = cityList.iterator();

        while(i$.hasNext()) {
            String cityId = (String)i$.next();
            if(this.labelFormTrendMap.containsKey(cityId)) {
                KpiMapData data = new KpiMapData();
                data.setCityId(cityId);
                data.setTipInfo(((CiLabelFormTrendModel)this.labelFormTrendMap.get(cityId)).getCityName().toString());
                data.setColor(((CiLabelFormTrendModel)this.labelFormTrendMap.get(cityId)).getColor());
                results.add(data);
            }
        }

        return results;
    }

    private List<KpiMapData> getCustomersGroupKpiData(List<String> cityList) {
        ArrayList results = new ArrayList();
        Iterator i$ = cityList.iterator();

        while(i$.hasNext()) {
            String cityId = (String)i$.next();
            KpiMapData data = new KpiMapData();
            data.setCityId(cityId);
            data.setTipInfo(((CiCustomersFormTrendModel)this.customGroupFormMap.get(cityId)).getCityName().toString());
            data.setColor(((CiCustomersFormTrendModel)this.customGroupFormMap.get(cityId)).getColor());
            results.add(data);
        }

        return results;
    }

    public ICityMapService getCityMapService() {
        return this.cityMapService;
    }

    public void setCityMapService(ICityMapService cityMapService) {
        this.cityMapService = cityMapService;
    }

    public String getCityId() {
        return this.cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public ArrayList<String> getCityNameList() {
        return this.cityNameList;
    }

    public void setCityNameList(ArrayList<String> cityNameList) {
        this.cityNameList = cityNameList;
    }

    public ArrayList<String> getCityValueList() {
        return this.cityValueList;
    }

    public void setCityValueList(ArrayList<String> cityValueList) {
        this.cityValueList = cityValueList;
    }

    public void setKpiType(String kpiType) {
        this.kpiType = kpiType;
    }

    public String getKpiType() {
        return this.kpiType;
    }

    public List<CiLabelFormTrendModel> getLabelFormTrendList() {
        return this.labelFormTrendList;
    }

    public void setLabelFormTrendList(List<CiLabelFormTrendModel> labelFormTrendList) {
        this.labelFormTrendList = labelFormTrendList;
    }

    public CiLabelFormModel getCiLabelFormModel() {
        return this.ciLabelFormModel;
    }

    public void setCiLabelFormModel(CiLabelFormModel ciLabelFormModel) {
        this.ciLabelFormModel = ciLabelFormModel;
    }

    public Map<String, CiLabelFormTrendModel> getLabelFormTrendMap() {
        return this.labelFormTrendMap;
    }

    public void setLabelFormTrendMap(Map<String, CiLabelFormTrendModel> labelFormTrendMap) {
        this.labelFormTrendMap = labelFormTrendMap;
    }

    public CiCustomGroupForm getCiCustomGroupForm() {
        return this.ciCustomGroupForm;
    }

    public void setCiCustomGroupForm(CiCustomGroupForm ciCustomGroupForm) {
        this.ciCustomGroupForm = ciCustomGroupForm;
    }

    public List<CiCustomersFormTrendModel> getCustomGroupFormList() {
        return this.customGroupFormList;
    }

    public void setCustomGroupFormList(List<CiCustomersFormTrendModel> customGroupFormList) {
        this.customGroupFormList = customGroupFormList;
    }

    public Map<String, CiCustomersFormTrendModel> getCustomGroupFormMap() {
        return this.customGroupFormMap;
    }

    public void setCustomGroupFormMap(Map<String, CiCustomersFormTrendModel> customGroupFormMap) {
        this.customGroupFormMap = customGroupFormMap;
    }
}
