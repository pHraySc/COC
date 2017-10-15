package com.ailk.biapp.ci.service.base;

import com.ailk.biapp.ci.entity.base.EnumValue;
import com.ailk.biapp.ci.entity.base.LabelInfo;
import com.ailk.biapp.ci.entity.base.SysInfo;
import java.util.List;

public interface ILabelInfoService {
	List<LabelInfo> queryLabelCategoryInfo(SysInfo var1) throws Exception;

	List<LabelInfo> queryLabelInfo(SysInfo var1, String var2) throws Exception;

	List<LabelInfo> queryLabelInfo(SysInfo var1) throws Exception;

	List<EnumValue> queryEnumValueList(SysInfo var1, String var2, Integer var3, Integer var4) throws Exception;
}
