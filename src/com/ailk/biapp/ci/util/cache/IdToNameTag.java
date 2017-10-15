package com.ailk.biapp.ci.util.cache;

import com.ailk.biapp.ci.util.cache.CacheBase;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class IdToNameTag extends TagSupport {
    private static final long serialVersionUID = 2936909963112232466L;
    private String tableName;
    private String id;

    public IdToNameTag() {
    }

    public int doEndTag() throws JspException {
        if(this.tableName != null && !"".equals(this.tableName)) {
            if(this.id != null && !"".equals(this.id)) {
                try {
                    Object e = CacheBase.getInstance().getKeyObject(this.tableName, this.id);
                    String name = CacheBase.getInstance().getNameByKey(this.tableName, e);
                    this.pageContext.getOut().print(name);
                } catch (IOException var3) {
                    throw new JspException(var3);
                }

                return super.doEndTag();
            } else {
                try {
                    this.pageContext.getOut().println("");
                } catch (IOException var4) {
                    var4.printStackTrace();
                }

                return super.doEndTag();
            }
        } else {
            throw new RuntimeException("tableName属性不能为空");
        }
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
