package com.ailk.biapp.ci.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class CILogInterceptor extends AbstractInterceptor {
    private static final long serialVersionUID = 1L;

    public CILogInterceptor() {
    }

    public String intercept(ActionInvocation invocation) throws Exception {
        Class actionClass = invocation.getProxy().getAction().getClass();
        String className = actionClass.getSimpleName();
        String methodName = invocation.getProxy().getMethod();
        Logger log = Logger.getLogger(actionClass);
        String result = "";
        log.info("======== Enter " + className + "." + methodName + "() method ========");
        result = invocation.invoke();
        log.info("======== Exit " + className + "." + methodName + "() method ========");
        return result;
    }
}
