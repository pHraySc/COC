package com.ailk.biapp.ci.webservice;

import javax.xml.ws.WebServiceClient;

@WebServiceClient
public interface ICustomersPushedClient {
	String sendDataToSA(String var1);
}
