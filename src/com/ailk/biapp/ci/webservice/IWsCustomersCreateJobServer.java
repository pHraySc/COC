
package com.ailk.biapp.ci.webservice;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface IWsCustomersCreateJobServer {
	void notifyExecuteServerJob(@WebParam(name = "isExeTask") int var1, @WebParam(name = "customCycleType") int var2, @WebParam(name = "cityIds") String var3);
}
