package com.ailk.biapp.ci.util;

import com.asiainfo.bi.license.LicenseVerifier;
import com.asiainfo.biframe.utils.string.StringUtil;
import org.apache.log4j.Logger;

public class LicenseVerifyUtil {
    private static Logger log = Logger.getLogger(LicenseVerifyUtil.class);

    public LicenseVerifyUtil() {
    }

    public static boolean verifyLicense(String fileName) {
        boolean b = false;

        try {
            LicenseVerifier e = new LicenseVerifier();
            String rs = null;
            if(StringUtil.isEmpty(fileName)) {
                rs = e.verifyProp();
            } else {
                rs = e.verifyProp(fileName);
            }

            if(StringUtil.isEmpty(rs)) {
                b = true;
            }
        } catch (Exception var4) {
            log.error("License—È÷§“Ï≥£", var4);
            b = false;
        }

        return b;
    }

    public static void main(String[] args) {
    }
}
