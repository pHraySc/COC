package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.model.CrmTypeModel;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ICiCustomListInfoJDao {
    int selectCustomersTotalCount(CiCustomListInfo var1);

    int selectBackCountBySql(String var1);

    int selectCustomersListInfoCount(CiCustomListInfo var1);

    int selectEndedCustomersListInfoCount(CiCustomListInfo var1);

    List<CiCustomListInfo> selectCustomersListInfo(int var1, int var2, CiCustomListInfo var3);

    void dropTable(String var1);

    String getSqlCreateAsTable(String var1, String var2, String var3);

    void addTableColumn(String var1, String var2, String var3) throws Exception;

    void executeInBackDataBase(String var1);

    void executeInFrontDataBase(String var1);

    int insertInBackDataBase(String var1) throws Exception;

    int insertInBackDataBase(String var1, String var2, String var3, String var4) throws Exception;

    Map<String, Object> selectBackForMap(String var1, Object... var2);

    List<CrmTypeModel> selectCrmTypeModel();

    List<Map<String, Object>> selectGroupListData(int var1, int var2, String var3);

    List<CiLabelInfo> selectLabelInfoByName(String var1, String var2, String var3);

    List<CiGroupAttrRel> selectGroupAttrRelByCustomeIds(String var1, Date var2, String var3);

    int selectCountPustReq(String var1, String var2);

    int selectCiLabelRuleCountByListTableName(String var1);

    List<CiLabelInfo> selectLabelInfoByNameOrIds(String var1, String var2, String var3);

    List<CiLabelInfo> selectLabelInfoTreeList();

    String getListTableNameByCroupIdAndDataDate(String var1, String var2);

    int selecrDistinctListNum(String var1);

    String selectListMaxNumSql(String var1, int var2, String var3);

    int selectCustomListCount(CiCustomListInfo var1);

    List<CiCustomListInfo> selectCustomList(int var1, int var2, CiCustomListInfo var3);

    void updateCiCustomListInfo(String var1, String var2, Integer var3);

    boolean tableExists(String var1, String var2);
}
