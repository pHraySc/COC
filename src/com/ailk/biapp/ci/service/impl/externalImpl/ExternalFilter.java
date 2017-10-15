package com.ailk.biapp.ci.service.impl.externalImpl;

import com.ailk.biapp.ci.entity.base.SysInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.privilege.IUserPrivilegeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class ExternalFilter implements Filter {
    private static Logger log = Logger.getLogger(ExternalFilter.class);

    public ExternalFilter() {
    }

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Object[] params = invocation.getArguments();

        try {
            if(params != null) {
                for(int e = 0; e < params.length; ++e) {
                    Object param = params[e];
                    if(param instanceof SysInfo) {
                        SysInfo sysInfo = (SysInfo)param;
                        log.info("系统ID：" + sysInfo.getSysId() + ";系统名称：" + sysInfo.getSysName() + ";登录用户ID：" + sysInfo.getUserId() + ";");
                        IUserPrivilegeService userPrivilegeService = PrivilegeServiceUtil.getUserPrivilegeService();
                        IUser user = userPrivilegeService.getUser(sysInfo.getUserId());
                        if(StringUtils.isEmpty(sysInfo.getSysId())) {
                            throw new CIServiceException("外部系统标识（sys_id）不能为空！");
                        }

                        if(user == null || StringUtils.isEmpty(user.getUserid())) {
                            throw new CIServiceException("用户不存在！");
                        }
                    }
                }
            }
        } catch (Exception var9) {
            log.error(var9);
            throw new CIServiceException("校验用户异常！");
        }

        return invoker.invoke(invocation);
    }
}
