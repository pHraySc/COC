package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.model.CityMap;
import com.ailk.biapp.ci.model.KpiMapData;
import com.ailk.biapp.ci.model.MapHilight;
import com.ailk.biapp.ci.model.MapLimit;
import com.ailk.biapp.ci.service.ICityMapService;
import com.ailk.biapp.ci.util.XMLConfigReader;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CityMapServiceImpl implements ICityMapService {
    private static Logger log = Logger.getLogger(ICityMapService.class);
    public static String[] COLOR_LIST = new String[]{"FF9900", "469FD0", "7CBB44", "E07034", "F1ED2A", "68D3E8", "93E686", "0372AB", "D92F73", "7D2C70", "C6010F", "FF6600", "FFCC00", "00CC00", "00FFFF", "000099", "6b1a99", "138150"};
    public String mapProvince = "beijing";

    public CityMapServiceImpl() {
    }

    public CityMap renderMap(String cityId, List<KpiMapData> lskpiData, List<String> cityList) {
        if(null == lskpiData) {
            return null;
        } else {
            this.mapProvince = "beijing";
            CityMap cityMap = new CityMap();
            cityMap.setCityid(cityId);
            cityMap.setFileName(this.mapProvince + "/999.jpg");
            cityMap.setAlwaysOn(Boolean.valueOf(true));
            ArrayList lsMapH = new ArrayList();
            String path = null;

            try {
                String res = "/config/aibi_ci/map/";
                log.debug("地图配置文件地址:" + res + this.mapProvince + "/" + "mapconfig_999.xml");
                URL maphl = CityMapServiceImpl.class.getResource(res + this.mapProvince + "/" + "mapconfig_999.xml");
                log.debug("using config file:" + maphl.getFile());
                path = (new File(maphl.getFile())).getCanonicalPath();
            } catch (Exception var15) {
                log.error("地图配置文件加载错误!", var15);
            }

            XMLConfigReader res1 = new XMLConfigReader(path);
            cityMap.setWidth(res1.getString("width", "value"));
            cityMap.setHeight(res1.getString("height", "value"));
            cityMap.setCenterLeft(res1.getString("centerLeft", "value"));
            cityMap.setCenterTop(res1.getString("centerTop", "value"));
            Iterator maphl2 = cityList.iterator();

            while(true) {
                while(maphl2.hasNext()) {
                    String mouset = (String)maphl2.next();
                    Iterator i$ = lskpiData.iterator();

                    while(i$.hasNext()) {
                        KpiMapData data = (KpiMapData)i$.next();
                        if(mouset.equals(data.getCityId())) {
                            MapHilight maphl1 = new MapHilight();
                            this.setMaphilight(maphl1, mouset, mouset, res1);
                            if(data.getColor() != null) {
                                maphl1.setFillColor(data.getColor());
                            } else {
                                maphl1.setFillColor(res1.getString("defaultFillColor", "value"));
                            }

                            HashMap mouset1 = new HashMap();
                            mouset1.put("tipInfo", data.getTipInfo());
                            maphl1.setToolTip(mouset1);
                            lsMapH.add(maphl1);
                            break;
                        }
                    }
                }

                MapHilight maphl3 = new MapHilight();
                this.setMaphilight(maphl3, "999", "999", res1);
                maphl3.setFillColor(res1.getString("defaultFillColor", "value"));
                HashMap mouset2 = new HashMap();
                mouset2.put("tipInfo", "查看全市趋势");
                maphl3.setToolTip(mouset2);
                lsMapH.add(maphl3);
                cityMap.setMaphilights(lsMapH);
                cityMap.setInfo("");
                cityMap.setTag("");
                return cityMap;
            }
        }
    }

    public CityMap renderCityMap(String cityId, List<KpiMapData> lskpiData, List<MapLimit> mapLimitList, List<String> cityList) {
        if(null == lskpiData) {
            return null;
        } else {
            this.mapProvince = "beijing";
            CityMap cityMap = new CityMap();
            cityMap.setCityid(cityId);
            cityMap.setFileName(this.mapProvince + "/9991.jpg");
            cityMap.setAlwaysOn(Boolean.valueOf(true));
            ArrayList lsMapH = new ArrayList();
            String path = null;

            try {
                String res = "/config/aibi_ci/map/";
                log.debug("地图配置文件地址:" + res + this.mapProvince + "/" + "mapconfig_9991.xml");
                URL i$ = CityMapServiceImpl.class.getResource(res + this.mapProvince + "/" + "mapconfig_9991.xml");
                log.debug("using config file:" + i$.getFile());
                path = (new File(i$.getFile())).getCanonicalPath();
            } catch (Exception var20) {
                log.error("地图配置文件加载错误!", var20);
            }

            XMLConfigReader res1 = new XMLConfigReader(path);
            cityMap.setWidth(res1.getString("width", "value"));
            cityMap.setHeight(res1.getString("height", "value"));
            cityMap.setCenterLeft(res1.getString("centerLeft", "value"));
            cityMap.setCenterTop(res1.getString("centerTop", "value"));
            if(mapLimitList == null || mapLimitList.isEmpty()) {
                mapLimitList = this.setDefaultMapLimit(res1);
            }

            Iterator i$2 = cityList.iterator();

            while(i$2.hasNext()) {
                String subCityId = (String)i$2.next();
                boolean findLimit = false;
                Iterator maphl = lskpiData.iterator();

                label45:
                while(maphl.hasNext()) {
                    KpiMapData data = (KpiMapData)maphl.next();
                    if(subCityId.equals(data.getCityId())) {
                        Double value = data.getValue();
                        MapHilight maphl1 = new MapHilight();
                        this.setMaphilight(maphl1, subCityId, subCityId, res1);
                        Iterator i$1 = mapLimitList.iterator();

                        MapLimit limit;
                        do {
                            if(!i$1.hasNext()) {
                                break label45;
                            }

                            limit = (MapLimit)i$1.next();
                        } while(limit.compare(value) <= 0);

                        maphl1.setFillColor(limit.getColor());
                        findLimit = true;
                        HashMap mouset = new HashMap();
                        mouset.put("tipInfo", data.getTipInfo());
                        maphl1.setToolTip(mouset);
                        lsMapH.add(maphl1);
                        break;
                    }
                }

                if(!findLimit) {
                    MapHilight maphl2 = new MapHilight();
                    this.setMaphilight(maphl2, subCityId, subCityId, res1);
                    maphl2.setFillColor(res1.getString("defaultFillColor", "value"));
                    lsMapH.add(maphl2);
                }
            }

            cityMap.setMaphilights(lsMapH);
            cityMap.setInfo("");
            cityMap.setTag("");
            cityMap.setGridInfo(this.getGridInfo(mapLimitList));
            return cityMap;
        }
    }

    private void setMaphilight(MapHilight maphl, String subCityId, String subCityName, XMLConfigReader res) {
        maphl.setId(subCityId);
        maphl.setCityName(subCityName);
        maphl.setCoords(res.getString(subCityId.trim(), "value"));
        maphl.setHerf("javascript:showCityDel(" + subCityId.trim() + ")");
        maphl.setShape(res.getString("shape", "value"));
        maphl.setFillOpacity(res.getString("fillOpacity", "value"));
        maphl.setStroke(Boolean.valueOf(res.getString("stroke", "value")));
        maphl.setStrokeColor(res.getString("strokeColor", "value"));
        maphl.setStrokeOpacity(res.getString("strokeOpacity", "value"));
        maphl.setStrokeWidth(res.getString("strokeWidth", "value"));
    }

    private List<MapLimit> setDefaultMapLimit(XMLConfigReader res) {
        ArrayList mapLimitList = new ArrayList();
        int level = Integer.parseInt(res.getString("level", "value"));

        for(int i = 0; i < level; ++i) {
            MapLimit mapLimit = new MapLimit();
            mapLimit.setColor(COLOR_LIST[i % COLOR_LIST.length]);
            mapLimit.setSeriesName(res.getString("level" + i, "name"));
            String[] maxminVal = res.getString("level" + i, "value").split(",");
            if(StringUtils.isNotBlank(maxminVal[0])) {
                mapLimit.setMinValue(Double.valueOf(maxminVal[0]));
            }

            if(StringUtils.isNotBlank(maxminVal[1])) {
                mapLimit.setMaxValue(Double.valueOf(maxminVal[1]));
            }

            mapLimitList.add(mapLimit);
        }

        return mapLimitList;
    }

    private String getGridInfo(List<MapLimit> mapLimitList) {
        StringBuilder info = new StringBuilder();
        info.append("<chart showShadow=\'0\' bgColor=\'ffffff\'  showValues=\'0\' decimals=\'0\' formatNumberScale=\'0\' labelDisplay=\'Rotate\' baseFontSize=\'12\' ");
        info.append(">");
        Iterator i$ = mapLimitList.iterator();

        while(i$.hasNext()) {
            MapLimit mapLimit = (MapLimit)i$.next();
            info.append(" <set  label=\'" + mapLimit.getSeriesName() + "\' color=\'" + mapLimit.getColor() + "\' isSliced=\'0\'/>");
        }

        info.append("</chart>");
        return info.toString();
    }

    public String getMapProvince() {
        return this.mapProvince;
    }

    public void setMapProvince(String mapProvince) {
        this.mapProvince = mapProvince;
    }
}
