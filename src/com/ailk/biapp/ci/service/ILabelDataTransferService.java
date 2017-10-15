package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.exception.CIServiceException;

public interface ILabelDataTransferService {
    String[] importLabelInfo() throws CIServiceException;

    String[] increaseLabelInfo() throws CIServiceException;
}
