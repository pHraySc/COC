package com.ailk.biapp.ci.webservice;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService(targetNamespace = "http://webservice.cm.biapp.ailk.com/")
@SOAPBinding(style = Style.RPC)
public interface IWsMcdInterfaceServer {
	String getCustomersList(@WebParam(name = "userId",targetNamespace = "http://webservice.cm.biapp.ailk.com/") String var1, @WebParam(name = "sysId",targetNamespace = "http://webservice.cm.biapp.ailk.com/") String var2);

	String getTargetCustomersObj(@WebParam(name = "id",targetNamespace = "http://webservice.cm.biapp.ailk.com/") String var1);

	String getTargetCustomersCycleObj(@WebParam(name = "id",targetNamespace = "http://webservice.cm.biapp.ailk.com/") String var1, @WebParam(name = "date",targetNamespace = "http://webservice.cm.biapp.ailk.com/") String var2);

	String sendCustGroupContentReq(@WebParam(name = "custGroupId",targetNamespace = "http://webservice.cm.biapp.ailk.com/") String var1, @WebParam(name = "contentType",targetNamespace = "http://webservice.cm.biapp.ailk.com/") String var2, @WebParam(name = "custGroupDate",targetNamespace = "http://webservice.cm.biapp.ailk.com/") String var3, @WebParam(name = "labelInfoCategoryId",targetNamespace = "http://webservice.cm.biapp.ailk.com/") String var4);

	String get(@WebParam(name = "custGroupId",targetNamespace = "http://webservice.cm.biapp.ailk.com/") String var1, @WebParam(name = "contentType",targetNamespace = "http://webservice.cm.biapp.ailk.com/") String var2, @WebParam(name = "custGroupDate",targetNamespace = "http://webservice.cm.biapp.ailk.com/") String var3);

	String getCiLabelInfoCategoryList();
}
