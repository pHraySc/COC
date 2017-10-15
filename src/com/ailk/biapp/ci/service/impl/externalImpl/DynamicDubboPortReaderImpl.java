package com.ailk.biapp.ci.service.impl.externalImpl;

import com.ailk.biapp.ci.exception.CIServiceException;
import com.alibaba.dubbo.config.ProtocolConfig;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class DynamicDubboPortReaderImpl implements ApplicationContextAware {
    private static Map<Integer, Integer> ports = null;
    private static final String PROTOCOL_NAME = "dubbo";
    private ConfigurableApplicationContext applicationContext = null;

    public DynamicDubboPortReaderImpl() {
    }

    public void init() {
        Integer port = this.getDubboPort();
        if(port.intValue() == -1) {
            throw new CIServiceException("Dubbox没有可用端口!");
        } else {
            this.updateProtocolMessage("dubbo", port.intValue());
        }
    }

    private Integer getDubboPort() {
        Integer result = Integer.valueOf(-1);
        Iterator i$ = ports.keySet().iterator();

        while(i$.hasNext()) {
            Integer key = (Integer)i$.next();
            Integer value = (Integer)ports.get(key);
            if(value.intValue() == 0) {
                result = key;
                ports.put(key, Integer.valueOf(1));
                break;
            }
        }

        return result;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext)applicationContext;
    }

    public void updateProtocolMessage(String protocolConfig, int port) {
        if(!this.applicationContext.containsBean(protocolConfig)) {
            System.out.println("没有【" + protocolConfig + "】协议");
        }

        ProtocolConfig protocolConfigSource = (ProtocolConfig)this.applicationContext.getBean(protocolConfig);
        protocolConfigSource.setPort(Integer.valueOf(port));
    }

    public ConfigurableApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    public void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    static {
        ports = new HashMap();
        ports.put(Integer.valueOf(20881), Integer.valueOf(0));
        ports.put(Integer.valueOf(20882), Integer.valueOf(0));
        ports.put(Integer.valueOf(20883), Integer.valueOf(0));
        ports.put(Integer.valueOf(20884), Integer.valueOf(0));
        ports.put(Integer.valueOf(20885), Integer.valueOf(0));
        ports.put(Integer.valueOf(20886), Integer.valueOf(0));
        ports.put(Integer.valueOf(20887), Integer.valueOf(0));
    }
}
