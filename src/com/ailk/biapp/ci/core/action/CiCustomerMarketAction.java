package com.ailk.biapp.ci.core.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.market.service.ICiMarketService;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.*;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;

@Controller
public class CiCustomerMarketAction extends CIBaseAction
{

	private Pager pager;
	private String dataDate;
	private List firstLevelScenes;
	private List customCreateTypes;
	private List customDataStatus;
	private Map cityMap;
	private CiCustomGroupInfo ciCustomGroupInfo;
	private List ciCustomGroupInfoList;
	private Integer havePushFun;
	private ICustomersManagerService customersService;
	private ICiMarketService ciMarketService;

	public CiCustomerMarketAction()
	{
		cityMap = new LinkedHashMap();
	}

	public String customerMarketIndex()
		throws Exception
	{
		CacheBase cache = CacheBase.getInstance();
		firstLevelScenes = ciMarketService.findFirstLevelScenes();
		customCreateTypes = cache.getObjectList("DIM_CUSTOM_CREATE_TYPE");
		customDataStatus = cache.getObjectList("DIM_CUSTOM_DATA_STATUS");
		Map cityMapT = cache.getCityMap();
		cityMap.put(Configure.getInstance().getProperty("CENTER_CITYID"), "省中心");
		List onlyCityIds = cache.getKeyList("CITY_IDS_LIST");
		String s;
		Object cityName;
		for (Iterator i$ = onlyCityIds.iterator(); i$.hasNext(); cityMap.put(s, cityName.toString()))
		{
			s = (String)i$.next();
			cityName = cityMapT.get(Integer.valueOf(s));
		}

		CILogServiceUtil.getLogServiceInstance().log("COC_HOME_CUTOMER_MARKET_LINK", "", "", "用户群集市链接跳转成功", OperResultEnum.Success, LogLevelEnum.Normal);
		return "customerMarketIndex";
	}

	public String customersList()
		throws Exception
	{
		if (ciCustomGroupInfo == null)
			ciCustomGroupInfo = new CiCustomGroupInfo();
		if (StringUtils.isBlank(ciCustomGroupInfo.getDataDate()))
			newLabelMonth = getNewLabelMonth();
		String userId = getUserId();
		String createCityId = PrivilegeServiceUtil.getCityIdFromSession();
		String root = Configure.getInstance().getProperty("CENTER_CITYID");
		if (!PrivilegeServiceUtil.isAdminUser(userId))
		{
			ciCustomGroupInfo.setCreateUserId(userId);
			ciCustomGroupInfo.setCreateCityId(createCityId);
		} else
		if (!createCityId.equals(root))
			ciCustomGroupInfo.setCreateCityId(createCityId);
		if (StringUtil.isNotEmpty(ciCustomGroupInfo.getIsMyCustom()) && ciCustomGroupInfo.getIsMyCustom().equals("true"))
			ciCustomGroupInfo.setUserId(getUserId());
		if (pager == null)
			pager = new Pager();
		Pager p2 = new Pager(customersService.queryCustomersCount(ciCustomGroupInfo));
		pager.setTotalPage(p2.getTotalPage());
		pager.setTotalSize(p2.getTotalSize());
		pager = pager.pagerFlip();
		pager.setOrderBy("NEW_MODIFY_TIME");
		pager.setOrder("desc");
		ciCustomGroupInfoList = customersService.queryCustomersList(pager, newLabelMonth, ciCustomGroupInfo);
		if (ciCustomGroupInfoList != null)
		{
			Iterator i$ = ciCustomGroupInfoList.iterator();
			do
			{
				if (!i$.hasNext())
					break;
				CiCustomGroupInfo info = (CiCustomGroupInfo)i$.next();
				String offsetStr = customersService.queryCustomerOffsetStr(info);
				info.setKpiDiffRule((new StringBuilder()).append(info.getKpiDiffRule() != null ? info.getKpiDiffRule() : "").append(offsetStr).toString());
				if (info.getMonthLabelDate() != null && !info.getMonthLabelDate().equals(""))
					info.setMonthLabelDate(DateUtil.string2StringFormat(info.getMonthLabelDate(), "yyyyMM", "yyyy-MM"));
				if (info.getDayLabelDate() != null && !info.getDayLabelDate().equals(""))
					info.setDayLabelDate(DateUtil.string2StringFormat(info.getDayLabelDate(), "yyyyMMdd", "yyyy-MM-dd"));
				if ((info.getMonthLabelDate() == null || info.getMonthLabelDate().equals("")) && (info.getDayLabelDate() == null || info.getDayLabelDate().equals("")))
					if (info.getDataDate() == null)
						info.setDataDateStr("");
					else
						info.setDataDateStr(ciMarketService.queryCustomDataDate(info));
			} while (true);
		}
		dataDate = getNewLabelMonth();
		List ciSysInfoRightList = customersService.queryCiSysInfo(1);
		if (ciSysInfoRightList != null && ciSysInfoRightList.size() > 0)
			havePushFun = Integer.valueOf(1);
		else
			havePushFun = Integer.valueOf(0);
		return "customerMarketList";
	}

	public List getFirstLevelScenes()
	{
		return firstLevelScenes;
	}

	public void setFirstLevelScenes(List firstLevelScenes)
	{
		this.firstLevelScenes = firstLevelScenes;
	}

	public List getCustomCreateTypes()
	{
		return customCreateTypes;
	}

	public void setCustomCreateTypes(List customCreateTypes)
	{
		this.customCreateTypes = customCreateTypes;
	}

	public List getCustomDataStatus()
	{
		return customDataStatus;
	}

	public void setCustomDataStatus(List customDataStatus)
	{
		this.customDataStatus = customDataStatus;
	}

	public Map getCityMap()
	{
		return cityMap;
	}

	public void setCityMap(Map cityMap)
	{
		this.cityMap = cityMap;
	}

	public Pager getPager()
	{
		return pager;
	}

	public void setPager(Pager pager)
	{
		this.pager = pager;
	}

	public String getDataDate()
	{
		return dataDate;
	}

	public void setDataDate(String dataDate)
	{
		this.dataDate = dataDate;
	}

	public CiCustomGroupInfo getCiCustomGroupInfo()
	{
		return ciCustomGroupInfo;
	}

	public void setCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo)
	{
		this.ciCustomGroupInfo = ciCustomGroupInfo;
	}

	public Integer getHavePushFun()
	{
		return havePushFun;
	}

	public void setHavePushFun(Integer havePushFun)
	{
		this.havePushFun = havePushFun;
	}

	public List getCiCustomGroupInfoList()
	{
		return ciCustomGroupInfoList;
	}

	public void setCiCustomGroupInfoList(List ciCustomGroupInfoList)
	{
		this.ciCustomGroupInfoList = ciCustomGroupInfoList;
	}
}
