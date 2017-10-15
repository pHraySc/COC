package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.base.LabelInfo;
import com.ailk.biapp.ci.entity.base.SysInfo;
import java.util.List;

public interface ICiExternalLabelJDao {
    List<LabelInfo> queryLabelCategoryList(SysInfo var1) throws Exception;

    List<LabelInfo> queryLabelsByCategoryId(SysInfo var1, String var2) throws Exception;

    List<LabelInfo> queryLabelsByLeafCategoryId(SysInfo var1, String var2) throws Exception;

    List<LabelInfo> queryLabelInfoList(SysInfo var1) throws Exception;
}
