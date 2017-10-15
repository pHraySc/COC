package com.ailk.biapp.ci.webservice;

import com.ailk.biapp.ci.exception.CIServiceException;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface ICiCampaignsService {
    @WebMethod(
            operationName = "getCampaigns"
    )
    String getCampaigns(String var1, String var2, String var3, String var4, String var5) throws CIServiceException;
}
