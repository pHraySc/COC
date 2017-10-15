
package com.ailk.biapp.ci.webservice;

import javax.xml.namespace.QName;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.log4j.Logger;

import com.ailk.biapp.ci.exception.CIServiceException;

public class TestWebServiceServer {
	private static Logger log = Logger.getLogger(TestWebServiceServer.class);

	public TestWebServiceServer() {
	}

	public static void main(String[] args) {
		try {
			String e = "http://webservice.ci.biapp.ailk.com/";
			String methodName = "notifyExecuteServerJob";
			String wsdl = "http://localhost:9001/coc/services/wsCustomersCreateJobServer?wsdl";
			byte isExeTask = 1;
			byte customCycleType = 3;
			String cityIds = "999";
			Object[] res = callWebService(wsdl, e, methodName, isExeTask, customCycleType, cityIds);
			System.out.println(res[0]);
		} catch (Exception var8) {
			var8.printStackTrace();
			log.error("²âÊÔ´íÎó", var8);
		}

	}

	private static Object[] callWebService(String wsdl, String targetNamespace, String methodName, int isExeTask, int customCycleType, String cityIds) {
		try {
			JaxWsDynamicClientFactory e = JaxWsDynamicClientFactory.newInstance();
			Client client = e.createClient(wsdl);
			Object[] res = null;
			if(targetNamespace == null) {
				res = client.invoke(methodName, new Object[]{Integer.valueOf(isExeTask), Integer.valueOf(customCycleType), cityIds});
			} else {
				res = client.invoke(new QName(targetNamespace, methodName), new Object[]{Integer.valueOf(isExeTask), Integer.valueOf(customCycleType), cityIds});
			}

			return res;
		} catch (Exception var9) {
			log.error("¶¯Ì¬µ÷ÓÃwebservice Ê§°Ü,wsdl:" + wsdl + ",targetNamespace:" + targetNamespace + ",methodName:" + methodName + ",args:" + isExeTask + "," + customCycleType + "," + cityIds, var9);
			throw new CIServiceException("¶¯Ì¬µ÷ÓÃwebservice Ê§°Ü");
		}
	}
}
