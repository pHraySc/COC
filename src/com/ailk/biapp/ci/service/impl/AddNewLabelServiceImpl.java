package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.IAddNewLabelHDAO;
import com.ailk.biapp.ci.service.IAddNewLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("addNewLabelService")
@Transactional
public class AddNewLabelServiceImpl implements IAddNewLabelService {
    @Autowired
    IAddNewLabelHDAO addNewLabelHDAO;

    public AddNewLabelServiceImpl() {
    }

    public void addNewLabel() throws Exception {
        this.addNewLabelHDAO.addNewLabel();
    }
}
