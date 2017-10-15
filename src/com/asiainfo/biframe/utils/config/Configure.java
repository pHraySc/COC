
package com.asiainfo.biframe.utils.config;

import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

public final class Configure {
    private static Logger log = Logger.getLogger(Configure.class);
    private static Configure configure = new Configure();
    private static final String DEFAULT_CONFIG_TYPE = "ASIAINFO_PROPERTIES";
    private static Map<String, Object> modifiedTimeMap = new HashMap();
    private static Map<String, Object> fileNameMap = new HashMap();
    private static Map<String, Object> absPathMap = new HashMap();
    private static Map<String, Object> configMap = new HashMap();

    private Configure() {
    }

    public static Configure getInstance() {
        return configure;
    }

    public void setConfFileName(String fileName) throws Exception {
        this.initProperties("ASIAINFO_PROPERTIES", fileName);
    }

    public void addConfFileName(String configType, String fileName) throws Exception {
        if(!"ASIAINFO_PROPERTIES".equals(configType) || "ASIAINFO_PROPERTIES".equals(configType) && configMap.get(configType) == null) {
            this.initProperties(configType, fileName);
        }

    }

    public String getProperty(String strKey) {
        try {
            return this.getProperty("ASIAINFO_PROPERTIES", strKey);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public String getProperty(String configType, String strKey) throws Exception {
        if(StringUtil.isEmpty(configType)) {
            throw new Exception("----Configure--err-------:configType is null");
        } else {
            try {
                if(configMap.get(configType) == null) {
                    throw new Exception("----Configure--err-------:configType[" + configType + "]is not initialized");
                } else {
                    File excep = new File((String)fileNameMap.get(configType));
                    if(excep.lastModified() > ((Long)modifiedTimeMap.get(configType)).longValue()) {
                        this.initProperties(configType, (String)fileNameMap.get(configType));
                    }

                    Properties properties = (Properties)configMap.get(configType);
                    return StringUtil.obj2Str(properties.getProperty(strKey));
                }
            } catch (Exception var5) {
                var5.printStackTrace();
                return "";
            }
        }
    }

    private synchronized boolean initProperties(String configType, String fileName) throws Exception {
        if(StringUtil.isEmpty(configType)) {
            throw new Exception("----Configure--err-------:configType is null");
        } else if(StringUtil.isEmpty(fileName)) {
            throw new Exception("----Configure--err-------:fileName is null");
        } else {
            Properties props = new Properties();
            File fileObj = new File(fileName);
            String absPathStr = fileObj.getAbsolutePath();
            log.debug("fileName:" + fileName + "\r\n Absolute Path:" + absPathStr);
            if(!fileObj.exists()) {
                throw new Exception("parameter file not found:" + fileName + "\r\nAbsolute Path:" + absPathStr);
            } else {
                FileInputStream fis = new FileInputStream(fileName);
                props.load(fis);//º”‘ÿ≈‰÷√Œƒº˛
                fis.close();
                modifiedTimeMap.put(configType, Long.valueOf(fileObj.lastModified()));
                fileNameMap.put(configType, fileName);
                absPathMap.put(configType, absPathStr);
                Properties properties = (Properties)configMap.get(configType);
                if(properties != null) {
                    properties.putAll(props);
                    configMap.put(configType, properties);
                } else {
                    configMap.put(configType, props);
                }

                return true;
            }
        }
    }

    public String getAbsPath() {
        return this.getAbsPath("ASIAINFO_PROPERTIES");
    }

    public String getAbsPath(String configType) {
        return (String)absPathMap.get(configType);
    }
}
