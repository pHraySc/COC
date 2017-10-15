package com.ailk.biapp.ci.dao;

public interface ICustomersAnalysisJDao {
    int getCustomersLabelJjCount(String var1, String var2);

    int getCustomersByLabel(String var1, String var2);

    int getCustomersLabelCjCount(String var1, String var2);

    int getLableCustomersCjCount(String var1, String var2);

    int getCustomersCount(String var1);

    void insertCustomersTable(String var1, String var2, String var3);
}
