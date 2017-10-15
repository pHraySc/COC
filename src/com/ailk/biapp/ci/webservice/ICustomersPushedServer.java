package com.ailk.biapp.ci.webservice;

import javax.jws.WebService;

@WebService
public interface ICustomersPushedServer {
	String getDataFromOtherSys(String var1);
}
