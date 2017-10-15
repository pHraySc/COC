package com.ailk.biapp.ci.exception;

import com.asiainfo.biframe.exception.ServiceException;

public class CIServiceException extends ServiceException {
    private static final long serialVersionUID = 1L;
    public static String MSG_SAVE_FAIL = "保存失败！";
    public static String MSG_SAVE_SUCESS = "保存成功！";

    public CIServiceException() {
    }

    public CIServiceException(String message) {
        super(message);
    }

    public CIServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CIServiceException(Throwable cause) {
        super(cause);
    }
}
