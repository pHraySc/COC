
package com.ailk.biapp.ci.webservice;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService(
		targetNamespace = "http://webservice.cm.biapp.ailk.com"
)
@SOAPBinding(
		style = Style.RPC
)
public interface IWsCustomerGroupCreateServer {
	String createCustomerGroup(@WebParam(name = "xmlBody") String var1);
}
