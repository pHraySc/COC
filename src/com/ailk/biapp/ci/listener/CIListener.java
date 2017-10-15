package com.ailk.biapp.ci.listener;

import com.ailk.biapp.ci.util.CIActivator;
import com.asiainfo.biframe.manager.context.ContextManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CIListener implements ServletContextListener {
	public CIListener() {
	}

	public void contextInitialized(ServletContextEvent event) {
		try {
			CIActivator e = new CIActivator();
			e.start(new ContextManager());
		} catch (Exception var3) {
			var3.printStackTrace();
		}

	}

	public void contextDestroyed(ServletContextEvent event) {
	}
}
