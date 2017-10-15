package com.ailk.biapp.ci.util.cache;

import com.ailk.biapp.ci.util.cache.CacheBase;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

public class DCSelectTag extends TagSupport {
    private static final long serialVersionUID = 2936909963112232466L;
    private String tableName;
    private Object selectName;
    private Object selectId;
    private Object selectClass;
    private String selectValue;
    private String nullName;
    private String nullValue;

    public DCSelectTag() {
    }

    public int doEndTag() throws JspException {
        if(this.tableName != null && !"".equals(this.tableName)) {
            try {
                String e = "<select id=\'" + this.selectId + "\' name =\'" + this.selectName + "\' class=\'" + this.selectClass + "\'>";
                e = e + "<option value=\'" + this.nullValue + "\'>" + this.nullName + "</option>";
                CopyOnWriteArrayList keyList = CacheBase.getInstance().getKeyList(this.tableName);
                Iterator i$ = keyList.iterator();

                while(i$.hasNext()) {
                    Object key = i$.next();
                    String name = CacheBase.getInstance().getNameByKey(this.tableName, key);
                    if(key.toString().equals(this.selectValue)) {
                        e = e + "<option value=\'" + key.toString() + "\' selected>" + name + "</option>";
                    } else {
                        e = e + "<option value=\'" + key.toString() + "\'>" + name + "</option>";
                    }
                }

                e = e + "</select>";
                this.pageContext.getOut().print(e);
                return super.doEndTag();
            } catch (IOException var6) {
                throw new JspException(var6);
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

    public Object getSelectName() {
        return this.selectName;
    }

    public void setSelectName(Object selectName) {
        try {
            this.selectName = ExpressionEvaluatorManager.evaluate("selectName", selectName.toString(), Object.class, this, this.pageContext);
        } catch (JspException var3) {
            var3.printStackTrace();
        }

    }

    public String getSelectValue() {
        return this.selectValue;
    }

    public void setSelectValue(String selectValue) {
        this.selectValue = selectValue;
    }

    public String getNullName() {
        return this.nullName;
    }

    public void setNullName(String nullName) {
        this.nullName = nullName;
    }

    public String getNullValue() {
        return this.nullValue;
    }

    public void setNullValue(String nullValue) {
        this.nullValue = nullValue;
    }

    public Object getSelectId() {
        return this.selectId;
    }

    public void setSelectId(Object selectId) {
        this.selectId = selectId;
    }

    public Object getSelectClass() {
        return this.selectClass;
    }

    public void setSelectClass(Object selectClass) {
        try {
            this.selectClass = ExpressionEvaluatorManager.evaluate("selectClass", selectClass.toString(), Object.class, this, this.pageContext);
        } catch (JspException var3) {
            var3.printStackTrace();
        }

    }
}
