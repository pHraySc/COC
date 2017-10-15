package com.ailk.biapp.ci.util;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.SequenceUtil;
import com.asiainfo.biframe.utils.config.Configure;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CiCustomGroupInfoIdGenUseSequence {
    private static Logger log = Logger.getLogger(CiCustomGroupInfoIdGenUseSequence.class);
    public static String CI_CUSTOM_GROUP_INFO_ID_PREFIX = "KHQ";
    public static String CI_CUSTOM_GROUP_INFO_SEQUENCE_NAME = "ZJ_KHQ_CODE_SEQ";
    @Autowired
    private SequenceUtil sequenceUtil;

    public CiCustomGroupInfoIdGenUseSequence() {
    }

    public String getId(String userId) {
        String cityId = "x";
        String needCityIdStr = Configure.getInstance().getProperty("CUSTOM_GROUP_INFO_ID_NEED_CITYID");
        Boolean needCityId = Boolean.valueOf(true);
        if(StringUtils.isNotEmpty(needCityIdStr) && "true".equalsIgnoreCase(needCityIdStr)) {
            needCityId = Boolean.valueOf(true);
        } else {
            needCityId = Boolean.valueOf(false);
        }

        if(needCityId.booleanValue()) {
            try {
                cityId = PrivilegeServiceUtil.getCityId(userId);
            } catch (Exception var7) {
                var7.printStackTrace();
                log.error("调用权限接口，根据userId获取cityId报错，异常信息如下：" + var7.getMessage());
            }
        }

        Long seqVal = this.sequenceUtil.getNextValBySeqName(CI_CUSTOM_GROUP_INFO_SEQUENCE_NAME);
        String numCode = this.makeNumCode(seqVal);
        return CI_CUSTOM_GROUP_INFO_ID_PREFIX + cityId + numCode;
    }

    private String makeNumCode(Long seqVal) {
        StringBuffer numCode = new StringBuffer();
        if(seqVal != null) {
            String seqStrval = seqVal.toString();
            int length = seqStrval.length();
            int configLength = ServiceConstants.CUSTOM_GROUP_INFO_ID_NUM_CODE_LENGTH;

            for(int i = 0; i < configLength - length; ++i) {
                numCode.append("0");
            }

            numCode.append(seqStrval);
        }

        return numCode.toString();
    }
}
