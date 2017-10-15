package com.ailk.biapp.ci.model;

import java.io.Serializable;
import java.util.List;

public class PersonCampaign implements Serializable {
    private static final long serialVersionUID = 1L;
    private String mobile;
    private List<String> campaigns;

    public PersonCampaign() {
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<String> getCampaigns() {
        return this.campaigns;
    }

    public void setCampaigns(List<String> campaigns) {
        this.campaigns = campaigns;
    }
}
