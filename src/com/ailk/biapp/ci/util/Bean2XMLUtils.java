package com.ailk.biapp.ci.util;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.betwixt.BindingConfiguration;
import org.apache.commons.betwixt.IntrospectionConfiguration;
import org.apache.commons.betwixt.expression.Context;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.strategy.DecapitalizeNameMapper;
import org.apache.commons.betwixt.strategy.DefaultObjectStringConverter;
import org.apache.commons.betwixt.strategy.HyphenatedNameMapper;
import org.xml.sax.SAXException;

public class Bean2XMLUtils {
    private static final String xmlHead = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";

    public Bean2XMLUtils() {
    }

    public static String bean2XmlString(Object obj) throws IOException, SAXException, IntrospectionException, IllegalArgumentException {
        if(obj == null) {
            throw new IllegalArgumentException("给定的参数不能为null！");
        } else {
            StringWriter sw = new StringWriter();
            sw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
            BeanWriter writer = new BeanWriter(sw);
            IntrospectionConfiguration config = writer.getXMLIntrospector().getConfiguration();
            BindingConfiguration bc = writer.getBindingConfiguration();
            bc.setObjectStringConverter(new Bean2XMLUtils.DateConverter());
            bc.setMapIDs(false);
            config.setAttributesForPrimitives(false);
            config.setAttributeNameMapper(new HyphenatedNameMapper());
            config.setElementNameMapper(new DecapitalizeNameMapper());
            writer.enablePrettyPrint();
            writer.write(obj.getClass().getSimpleName(), obj);
            writer.close();
            return sw.toString();
        }
    }

    public static void main(String[] args) {
    }

    private static class DateConverter extends DefaultObjectStringConverter {
        private static final long serialVersionUID = -197858851188189916L;

        private DateConverter() {
        }

        public String objectToString(Object object, Class type, String flavour, Context context) {
            return object != null?(object instanceof Date?(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format((Date)object):object.toString()):"";
        }
    }
}
