package com.ailk.biapp.ci.service;

public interface ILogTransferService {
    void logStatistics(String var1);

    void transportLogFromWebOS(String var1);

    boolean logTableIfExists(String var1);

    void removeDirtyData(String var1);
}
