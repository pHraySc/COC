package com.ailk.biapp.ci.model;

import java.io.Serializable;

public class TemplateShortInfo implements Serializable {
    private static final long serialVersionUID = 5844327984469472932L;
    public String templateId;
    public String templateName;

    public String getTemplateId() {
        return this.templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return this.templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public TemplateShortInfo(String templateId, String templateName) {
        this.templateId = templateId;
        this.templateName = templateName;
    }

    public TemplateShortInfo() {
    }

    public String toString() {
        return "TemplateShortInfo [templateId=" + this.templateId + ", templateName=" + this.templateName + "]";
    }
}
