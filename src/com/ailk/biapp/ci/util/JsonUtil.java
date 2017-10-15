package com.ailk.biapp.ci.util;

import com.ailk.biapp.ci.model.JacksonMapper;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;

public class JsonUtil {
    public JsonUtil() {
    }

    public static String toJson(Object object) throws IOException {
        ObjectMapper mapper = JacksonMapper.getInstance();
        mapper.configure(Feature.FAIL_ON_EMPTY_BEANS, false);
        String json = getJsonStr(mapper, object);
        return json;
    }

    public static String toJson(Object object, DateFormat dateFormat) throws IOException {
        ObjectMapper mapper = JacksonMapper.getInstance();
        mapper.configure(Feature.FAIL_ON_EMPTY_BEANS, false);
        if(dateFormat != null) {
            mapper.setDateFormat(dateFormat);
        } else {
            mapper.configure(Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        }

        String json = getJsonStr(mapper, object);
        return json;
    }

    public static String getJsonStr(ObjectMapper mapper, Object object) throws IOException {
        StringWriter sw = new StringWriter();
        JsonGenerator gen = (new JsonFactory()).createJsonGenerator(sw);
        mapper.writeValue(gen, object);
        gen.close();
        String json = sw.toString();
        return json;
    }

    public static Object json2Bean(String json, Class<?> cls) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = JacksonMapper.getInstance();
        Object obj = mapper.readValue(json, cls);
        return obj;
    }
}
