package com.ailk.biapp.ci.model;

import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.LabelElement;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class LabelElementFactory {
    private Logger log = Logger.getLogger(LabelElementFactory.class);
    public static final Map<Integer, String> labelTypeMap = new HashMap() {
        private static final long serialVersionUID = 1L;

        {
            this.put(Integer.valueOf(1), "com.ailk.biapp.ci.model.sub.SignLabel");
            this.put(Integer.valueOf(2), "com.ailk.biapp.ci.model.sub.ScoreLabel");
            this.put(Integer.valueOf(3), "com.ailk.biapp.ci.model.sub.AttrLabel");
            this.put(Integer.valueOf(4), "com.ailk.biapp.ci.model.sub.KpiLabel");
            this.put(Integer.valueOf(5), "com.ailk.biapp.ci.model.sub.EnumLabel");
            this.put(Integer.valueOf(6), "com.ailk.biapp.ci.model.sub.DateLabel");
            this.put(Integer.valueOf(7), "com.ailk.biapp.ci.model.sub.TextLabel");
        }
    };
    private LabelElement labelElement;

    public LabelElementFactory() {
    }

    public LabelElement getLabelElement() {
        return this.labelElement;
    }

    public void setLabelElement(Integer labelTypeId) {
        try {
            String e = (String)labelTypeMap.get(labelTypeId);
            Class msg1 = Class.forName(e);
            this.labelElement = (LabelElement)msg1.newInstance();
        } catch (Exception var4) {
            String msg = "计算标签不存在";
            this.log.error(msg, var4);
            throw new CIServiceException(msg, var4);
        }
    }
}
