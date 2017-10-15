package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.model.CiTagTypeModel;
import java.util.List;

public interface ICiPeopleBaseService {
    Integer queryPhoneNumExists(String var1);

    List<CiTagTypeModel> queryProductNoTag(String var1);
}
