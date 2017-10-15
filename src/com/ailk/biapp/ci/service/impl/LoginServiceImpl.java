package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ILoginJDao;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ILoginService;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginServiceImpl implements ILoginService {
    private Logger log = Logger.getLogger(LoginServiceImpl.class);
    @Autowired
    private ILoginJDao loginDao;

    public LoginServiceImpl() {
    }

    public void saveOrUpdateLoginHistory(String sessionId, String userId, String clientAddress, String productId, HttpServletRequest request) throws CIServiceException {
        try {
            String e = request.getHeader("user-agent");
            String browserVersion = this.loginDao.checkBrowse(e);
            String serverAddress = Configure.getInstance().getProperty("HOST_ADDRESS");
            if(StringUtil.isEmpty(serverAddress)) {
                serverAddress = "localhost";
            }

            this.loginDao.saveOrUpdateLoginHistory(sessionId, userId, clientAddress, serverAddress, browserVersion, productId);
        } catch (Exception var9) {
            this.log.error(var9);
            throw new CIServiceException(var9);
        }
    }
}
