package com.ailk.biapp.ci.webservice;

import javax.xml.ws.WebServiceClient;

@WebServiceClient
public interface IWsCustomersCreateJobClient {
	void notifyCustomerClientJob(String var1, String var2, String var3, int var4, int var5, String var6);
}
