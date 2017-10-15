package com.ailk.biapp.ci.model;

import com.ailk.biapp.ci.model.IndexValue;
import java.io.Serializable;
import java.util.List;

public class PersonLabelsAndIndexes implements Serializable {
    private static final long serialVersionUID = 1L;
    private String mobile;
    private List<String> labels;
    private List<IndexValue> indexes;

    public PersonLabelsAndIndexes() {
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<String> getLabels() {
        return this.labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<IndexValue> getIndexes() {
        return this.indexes;
    }

    public void setIndexes(List<IndexValue> indexes) {
        this.indexes = indexes;
    }
}
