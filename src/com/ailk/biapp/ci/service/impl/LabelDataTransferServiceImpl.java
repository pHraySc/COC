package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ILabelDataTransferJDao;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ILabelDataTransferService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("labelDataTransferService")
@Transactional
public class LabelDataTransferServiceImpl implements ILabelDataTransferService {
    private Logger log = Logger.getLogger(LabelDataTransferServiceImpl.class);
    @Autowired
    private ILabelDataTransferJDao labelDataTransferJDao;

    public LabelDataTransferServiceImpl() {
    }

    public String[] importLabelInfo() throws CIServiceException {
        String[] message = new String[0];

        try {
            message = this.labelDataTransferJDao.importLabelInfo();
            return message;
        } catch (Exception var4) {
            String errorMsg = "导入标签信息失败";
            this.log.error(errorMsg + var4);
            var4.printStackTrace();
            throw new CIServiceException(errorMsg);
        }
    }

    public String[] increaseLabelInfo() throws CIServiceException {
        String[] message = new String[0];

        try {
            message = this.labelDataTransferJDao.increaseLabelInfo();
            return message;
        } catch (Exception var4) {
            String errorMsg = "增量导入标签信息失败";
            this.log.error(errorMsg + var4);
            var4.printStackTrace();
            throw new CIServiceException(errorMsg, var4);
        }
    }
}
