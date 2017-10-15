package com.ailk.biapp.ci.dao;

import java.util.List;

public interface ILogTransferJDao {
    List selectLgkOperLog(String var1);

    List selectLgkLogin(String var1);

    void exportCustomerCreateDetail2LogOverview(String var1);

    void exportCustomerCreateTotal2LogOverview(String var1);

    void exportCustomerCreateDetail2LogUser(String var1);

    void exportLabelAnalysisDetail2LogOverview(String var1);

    void exportLabelAnalysisTotal2LogOverview(String var1);

    void exportLabelAnalysisDetail2LogUser(String var1);

    void exportCustomerAnalysisDetail2LogOverview(String var1);

    void exportCustomerAnalysisTotal2LogOverview(String var1);

    void exportCustomerAnalysisDetail2LogUser(String var1);

    void exportDateExplore2LogOverview(String var1);

    void exportDateExplore2LogUser(String var1);

    void exportMarketing2LogOverview(String var1);

    void exportMarketing2LogUser(String var1);

    void exportMarketingCampaign2LogOverview(String var1);

    void exportMarketingCampaign2LogUser(String var1);

    void exportLoginData2LogOverview(String var1);

    void exportLoginData2LogUser(String var1);

    boolean tableExists(String var1, String var2, String var3);

    void deleteDirtyData(String var1);
}
