package com.ailk.biapp.ci.model;

public class CILogInfo {
    private static ThreadLocal<String> requestURI = new ThreadLocal();
    private static ThreadLocal<String> operateType = new ThreadLocal();
    private static ThreadLocal<String> operateCode = new ThreadLocal();

    public CILogInfo() {
    }

    public static String getRequestURI() {
        return (String)requestURI.get();
    }

    public static void setRequestURI(String requestURI) {
        CILogInfo.requestURI.set(requestURI);
    }

    public static String getOperateType() {
        return (String)operateType.get();
    }

    public static void setOperateType(String operateType) {
        CILogInfo.operateType.set(operateType);
    }

    public static String getOperateCode() {
        return (String)operateCode.get();
    }

    public static void setOperateCode(String operateType) {
        operateCode.set(operateType);
    }
}
