package com.ailk.biapp.ci.webservice;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface ICustomerMarketCampaignWsServer {
	String sendCustomerMarketCampaignInfo(@WebParam(name = "infoXml",targetNamespace = "http://webservice.ci.biapp.ailk.com/") String var1);
}
