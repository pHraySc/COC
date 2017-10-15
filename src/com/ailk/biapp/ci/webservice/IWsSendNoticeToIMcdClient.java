package com.ailk.biapp.ci.webservice;

import javax.xml.ws.WebServiceClient;

@WebServiceClient
public interface IWsSendNoticeToIMcdClient {
	void sendNotice(String var1, String var2, String var3, String var4, String var5, String var6);
}
