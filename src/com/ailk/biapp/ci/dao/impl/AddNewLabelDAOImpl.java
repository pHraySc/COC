package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.IAddNewLabelHDAO;
import com.ailk.biapp.ci.dao.ICiLabelExtInfoHDao;
import com.ailk.biapp.ci.dao.ICiLabelInfoHDao;
import com.ailk.biapp.ci.dao.ICiMdaColumnHDao;
import com.ailk.biapp.ci.dao.ICiMdaTableHDao;
import com.ailk.biapp.ci.entity.CiLabelExtInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiMdaSysTable;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("addNewLabelDAO")
public class AddNewLabelDAOImpl implements IAddNewLabelHDAO {
    @Autowired
    private ICiLabelInfoHDao ciLabelInfoHDao;
    @Autowired
    private ICiLabelExtInfoHDao ciLabelExtInfoHDao;
    @Autowired
    private ICiMdaTableHDao ciMdaTableHDao;
    @Autowired
    private ICiMdaColumnHDao ciMdaColumnHDao;

    public AddNewLabelDAOImpl() {
    }

    public void addNewLabel() throws Exception {
        CiLabelInfo ciLabelInfo = new CiLabelInfo();
        ciLabelInfo.setLabelName("测试标签新建");
        ciLabelInfo.setCreateTime(DateUtil.string2Date("2013-04-01", "yyyy-MM-dd"));
        ciLabelInfo.setEffecTime(DateUtil.string2Date("2013-04-01", "yyyy-MM-dd"));
        ciLabelInfo.setPublishTime(DateUtil.string2Date("2013-04-01", "yyyy-MM-dd"));
        ciLabelInfo.setFailTime(DateUtil.string2Date("2099-06-30", "yyyy-MM-dd"));
        ciLabelInfo.setLabelTypeId(Integer.valueOf(1));
        ciLabelInfo.setParentId(Integer.valueOf(1111111));
        ciLabelInfo.setDataStatusId(Integer.valueOf(2));
        ciLabelInfo.setBusiLegend("测试标签新建");
        ciLabelInfo.setApplySuggest("测试标签新建");
        ciLabelInfo.setBusiCaliber("测试标签新建");
        ciLabelInfo.setCreateUserId("admin");
        ciLabelInfo.setPublishUserId("admin");
        ciLabelInfo.setUpdateCycle(Integer.valueOf(1));
        this.ciLabelInfoHDao.insertCiLabelInfo(ciLabelInfo);
        ciLabelInfo.setSortNum(ciLabelInfo.getLabelId());
        this.ciLabelInfoHDao.updateCiLabelInfo(ciLabelInfo);
        CiLabelExtInfo ciLabelExtInfo = new CiLabelExtInfo();
        ciLabelExtInfo.setLabelId(ciLabelInfo.getLabelId());
        ciLabelExtInfo.setIsStatUserNum(Integer.valueOf(1));
        ciLabelExtInfo.setIsLeaf(Integer.valueOf(1));
        ciLabelExtInfo.setLabelLevel(Integer.valueOf(3));
        CiMdaSysTable table = new CiMdaSysTable();
        table.setTableName("test_add_new_label");
        this.ciMdaTableHDao.insertTableInfo(table);
        CiMdaSysTableColumn column = new CiMdaSysTableColumn();
        column.setColumnName("test_add_new_label");
        column.setCiMdaSysTable(table);

        try {
            this.ciMdaColumnHDao.insertColumnInfo(column);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        ciLabelExtInfo.setCiMdaSysTableColumn(column);
        ciLabelExtInfo.setMaxVal(Double.valueOf(1.0D));
        ciLabelExtInfo.setMinVal(Double.valueOf(0.0D));
        this.ciLabelExtInfoHDao.insertCiLabelExtInfo(ciLabelExtInfo);
    }
}
