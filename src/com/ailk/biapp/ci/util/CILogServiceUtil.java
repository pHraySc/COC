package com.ailk.biapp.ci.util;

import com.ailk.biapp.ci.constant.CommonConstants;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.DimOpLogTypeDetail;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ILoginService;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.ILogService;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.privilege.IUserSession;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.linkage.bi3.loggingComponent.client.imp.LogService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class CILogServiceUtil {
    private static CILogServiceUtil CILogServiceUtil = new CILogServiceUtil();
    private static ILogService logService = null;
    private static ILoginService loginService = null;
    private static Logger logger = Logger.getLogger(CILogServiceUtil.class);
    private static ThreadLocal<IUserSession> sessionThread = null;

    private CILogServiceUtil() {
    }

    public static synchronized CILogServiceUtil getLogServiceInstance() {
        sessionThread = new ThreadLocal();
        if("bj".equalsIgnoreCase(CommonConstants.base) && logService == null) {
            try {
                logService = (ILogService)SystemServiceLocator.getInstance().getService("logService");
            } catch (Exception var2) {
                logger.error("获取日志组件service错误", var2);
            }
        }

        try {
            loginService = (ILoginService)SystemServiceLocator.getInstance().getService("loginServiceImpl");
        } catch (Exception var1) {
            logger.error("获取登录日志service错误", var1);
        }

        return CILogServiceUtil;
    }

    public void log(String operation, String resurceId, String resourceName, String msg, OperResultEnum result, LogLevelEnum level) {
        long start = System.currentTimeMillis();

        try {
            logger.debug("into log method");
            boolean e = Boolean.valueOf(Configure.getInstance().getProperty("NEED_LOG")).booleanValue();
            if(!e) {
                return;
            }

            if(StringUtil.isEmpty(resurceId)) {
                resurceId = "查询";
            }

            if(StringUtil.isEmpty(resourceName)) {
                resourceName = "查询";
            }

            IUserSession userSession = (IUserSession)sessionThread.get();
            if(userSession == null) {
                logger.debug("session is null");
                userSession = PrivilegeServiceUtil.getUserSession();
            } else {
                logger.debug("session userId is :" + userSession.getUserID());
            }

            CacheBase cache = CacheBase.getInstance();
            DimOpLogTypeDetail opLogTypeDetail = cache.getDimOpLogTypeDetail(operation);
            if(userSession != null) {
                if("bj".equalsIgnoreCase(CommonConstants.base) && logService != null) {
                    String serverIp = Configure.getInstance().getProperty("HOST_ADDRESS");
                    String logDefine = logService.getLogDefineValue(1, opLogTypeDetail.getOpTypeCode());
                    level = level.getValue().equals(com.asiainfo.biframe.log.impl.LogLevelEnum.Medium.getValue())?LogLevelEnum.Normal:level;
                    logService.log(userSession.getSessionID(), userSession.getUserID(), userSession.getUserName(), serverIp, userSession.getClientIP(), opLogTypeDetail.getOpTypeValue(), logDefine, resurceId, resourceName, "CI_" + msg, (Map)null, (Map)null, result, com.asiainfo.biframe.log.LogLevelEnum.fromValue(level.getValue()));
                } else {
                    (new LogService()).log(userSession.getSessionID(), userSession.getUserID(), userSession.getUserName(), opLogTypeDetail.getOpTypeCode(), opLogTypeDetail.getResourceCode(), opLogTypeDetail.getResourceName(), resurceId, opLogTypeDetail.getResourceTypeCode(), (String)null, msg, com.asiainfo.biframe.log.impl.OperResultEnum.fromValue(result.getValue()), com.asiainfo.biframe.log.impl.LogLevelEnum.fromValue(level.getValue()), userSession.getClientIP(), (Map)null, (Map)null);
                }
            } else {
                logger.debug("userSession = null");
            }
        } catch (Throwable var15) {
            logger.error("log to logservice error", var15);
        }

        logger.debug("exit log method ,cost:" + (System.currentTimeMillis() - start));
    }

    public void log(String sessionID, String userID, String userName, String clientIP, String operation, String resurceId, String resourceName, String msg, OperResultEnum result, LogLevelEnum level) {
        logger.debug("enter many params log method");

        try {
            boolean e = Boolean.valueOf(Configure.getInstance().getProperty("NEED_LOG")).booleanValue();
            if(!e) {
                logger.debug("needLog false");
                return;
            }

            if(StringUtil.isEmpty(resurceId)) {
                resurceId = "查询";
            }

            if(StringUtil.isEmpty(resourceName)) {
                resourceName = "查询";
            }

            CacheBase cache = CacheBase.getInstance();
            DimOpLogTypeDetail opLogTypeDetail = cache.getDimOpLogTypeDetail(operation);
            if("bj".equalsIgnoreCase(CommonConstants.base) && logService != null) {
                String serverIp = Configure.getInstance().getProperty("HOST_ADDRESS");
                String logDefine = logService.getLogDefineValue(1, opLogTypeDetail.getOpTypeCode());
                level = level.getValue().equals(com.asiainfo.biframe.log.impl.LogLevelEnum.Medium.getValue())?LogLevelEnum.Normal:level;
                logService.log(sessionID, userID, userName, serverIp, clientIP, opLogTypeDetail.getOpTypeValue(), logDefine, resurceId, resourceName, "CI_" + msg, (Map)null, (Map)null, result, com.asiainfo.biframe.log.LogLevelEnum.fromValue(level.getValue()));
            } else {
                (new LogService()).log(sessionID, userID, userName, opLogTypeDetail.getOpTypeCode(), opLogTypeDetail.getResourceCode(), opLogTypeDetail.getResourceName(), resurceId, opLogTypeDetail.getResourceTypeCode(), (String)null, msg, com.asiainfo.biframe.log.impl.OperResultEnum.fromValue(result.getValue()), com.asiainfo.biframe.log.impl.LogLevelEnum.fromValue(level.getValue()), clientIP, (Map)null, (Map)null);
            }
        } catch (Throwable var16) {
            logger.error("log to logservice error", var16);
        }

        logger.debug("exit log method");
    }

    public void addUserSession(IUserSession userSession) {
        sessionThread.set(userSession);
    }

    public void loginLog(String sessionId, String userId, String userName, String clientAddress, String productId, HttpServletRequest request) {
        logger.debug("enter login log method");

        try {
            if("bj".equalsIgnoreCase(CommonConstants.base) && logService != null) {
                loginService.saveOrUpdateLoginHistory(sessionId, userId, clientAddress, productId, request);
            } else {
                (new LogService()).login(sessionId, userId, userName, CommonConstants.loginResourceType, com.asiainfo.biframe.log.impl.OperResultEnum.Success, com.asiainfo.biframe.log.impl.LogLevelEnum.Normal, clientAddress);
            }
        } catch (CIServiceException var8) {
            logger.error("log to loginService error", var8);
        }

        logger.debug("exit login log method");
    }
}
