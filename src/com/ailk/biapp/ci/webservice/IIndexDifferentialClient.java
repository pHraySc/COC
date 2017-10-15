package com.ailk.biapp.ci.webservice;

import javax.xml.ws.WebServiceClient;

@WebServiceClient
public interface IIndexDifferentialClient {
	String sendDataToSA(String var1);
}
