package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.model.CityMap;
import com.ailk.biapp.ci.model.KpiMapData;
import com.ailk.biapp.ci.model.MapLimit;
import java.util.List;

public interface ICityMapService {
    CityMap renderCityMap(String var1, List<KpiMapData> var2, List<MapLimit> var3, List<String> var4);

    CityMap renderMap(String var1, List<KpiMapData> var2, List<String> var3);
}
