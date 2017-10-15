package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiMdaSysTable;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.model.CiLabelCategoryRel;
import com.ailk.biapp.ci.model.LabelColumnTable;
import com.ailk.biapp.ci.model.TargetCustomersAttr;
import com.ailk.biapp.ci.model.TargetCustomersModel;
import java.util.List;
import java.util.Map;

public interface IMcdInterfaceJDao {
    List<TargetCustomersModel> getCustomerByUserId(String var1, String var2);

    TargetCustomersModel getCustomerById(String var1);

    TargetCustomersModel getCustomerById(String var1, String var2);

    List<TargetCustomersAttr> getTargetCustomersAttr(String var1);

    List<CiLabelCategoryRel> getLabelIdList(String var1);

    List<CiLabelCategoryRel> getCiLabelCategoryRelByLabelId(Integer var1);

    CiMdaSysTable getMdaSysTable(String var1);

    CiMdaSysTableColumn getMdaSysTableColumn(String var1);

    List<LabelColumnTable> getLabelColumnTableList(Map<String, Object> var1) throws Exception;

    List<String> getUserList(String var1, String var2, String var3, String var4);

    void updateUserList(String var1, String var2, String var3, String var4);

    List<Map<String, Object>> getExcuteSqlResultList(String var1);

    List<CiLabelInfo> getCiLabelInfoList(String var1);

    boolean getTableExists(String var1);
}
