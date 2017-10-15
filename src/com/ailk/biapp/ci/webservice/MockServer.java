
package com.ailk.biapp.ci.webservice;

import javax.jws.WebService;

@WebService
public interface MockServer {
	String getDataFromCOC(String var1);
}
