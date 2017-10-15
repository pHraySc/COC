<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xmlns:aop="http://www.springframework.org/schema/aop"
		xmlns:tx="http://www.springframework.org/schema/tx"
		xmlns:jaxws="http://cxf.apache.org/jaxws"
		xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.0.xsd
		http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.0.xsd
		http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.0.xsd
 		http://cxf.apache.org/jaxws http://cxf.apache.org/schemas/jaxws.xsd">
    <import resource="classpath:META-INF/cxf/cxf.xml"/>
  	<import resource="classpath:META-INF/cxf/cxf-extension-soap.xml"/>
  	<import resource="classpath:META-INF/cxf/cxf-servlet.xml"/>

	<jaxws:client id="utils_uniTouchClientSms" address="http://127.0.0.1:8080/unitouch/servicesC/taskSmsService"
		serviceClass="com.asiainfo.biframe.utils.webservice.ITaskSmsService">
        <jaxws:dataBinding>
       		<bean class="org.apache.cxf.aegis.databinding.AegisDatabinding" />
    	</jaxws:dataBinding>
	</jaxws:client>
	<jaxws:client id="utils_uniTouchClientEmail" address="http://127.0.0.1:8080/unitouch/servicesC/taskEmailService"
		serviceClass="com.asiainfo.biframe.utils.webservice.ITaskEmailService">
		<jaxws:dataBinding>
       		<bean class="org.apache.cxf.aegis.databinding.AegisDatabinding" />
    	</jaxws:dataBinding>
	</jaxws:client>
	<jaxws:client id="utils_uniTouchClientMms" address="http://127.0.0.1:8080/unitouch/servicesC/taskMmsService"
		serviceClass="com.asiainfo.biframe.utils.webservice.uniTouch.ITaskMmsService">
		<jaxws:dataBinding>
       		<bean class="org.apache.cxf.aegis.databinding.AegisDatabinding" />
    	</jaxws:dataBinding>
	</jaxws:client>
</beans>

