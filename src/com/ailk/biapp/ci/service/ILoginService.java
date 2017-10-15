package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.exception.CIServiceException;
import javax.servlet.http.HttpServletRequest;

public interface ILoginService {
    void saveOrUpdateLoginHistory(String var1, String var2, String var3, String var4, HttpServletRequest var5) throws CIServiceException;
}
