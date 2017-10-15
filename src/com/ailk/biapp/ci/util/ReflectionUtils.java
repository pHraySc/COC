package com.ailk.biapp.ci.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

public class ReflectionUtils {
    private static final Logger LOG = Logger.getLogger(ReflectionUtils.class);

    public ReflectionUtils() {
    }

    public static Object invokeGetterMethod(Object target, String propertyName) {
        String getterMethodName = "get" + StringUtils.capitalize(propertyName);
        return invokeMethod(target, getterMethodName, new Class[0], new Object[0]);
    }

    public static void invokeSetterMethod(Object target, String propertyName, Object value) {
        invokeSetterMethod(target, propertyName, value, (Class)null);
    }

    public static void invokeSetterMethod(Object target, String propertyName, Object value, Class<?> propertyType) {
        Class type = propertyType != null?propertyType:value.getClass();
        String setterMethodName = "set" + StringUtils.capitalize(propertyName);
        invokeMethod(target, setterMethodName, new Class[]{type}, new Object[]{value});
    }

    public static Object getFieldValue(Object object, String fieldName) {
        Field field = getDeclaredField(object, fieldName);
        if(field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        } else {
            makeAccessible(field);
            Object result = null;

            try {
                result = field.get(object);
            } catch (IllegalAccessException var5) {
                LOG.error(" error{}", var5);
            }

            return result;
        }
    }

    public static void setFieldValue(Object object, String fieldName, Object value) {
        Field field = getDeclaredField(object, fieldName);
        if(field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        } else {
            makeAccessible(field);

            try {
                field.set(object, value);
            } catch (IllegalAccessException var5) {
                LOG.error("error{}", var5);
            }

        }
    }

    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        if(method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
        } else {
            method.setAccessible(true);

            try {
                return method.invoke(object, parameters);
            } catch (Exception var6) {
                throw convertReflectionExceptionToUnchecked(var6);
            }
        }
    }

    protected static Field getDeclaredField(Object object, String fieldName) {
        Assert.notNull(object, "object can not be null");
        Assert.hasText(fieldName, "fieldName");
        Class superClass = object.getClass();

        while(superClass != Object.class) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException var4) {
                superClass = superClass.getSuperclass();
            }
        }

        return null;
    }

    protected static void makeAccessible(Field field) {
        if(!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }

    }

    protected static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {
        Assert.notNull(object, "object can not be null");
        Class superClass = object.getClass();

        while(superClass != Object.class) {
            try {
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException var5) {
                superClass = superClass.getSuperclass();
            }
        }

        return null;
    }

    public static <T> Class<T> getSuperClassGenricType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    public static Class getSuperClassGenricType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if(!(genType instanceof ParameterizedType)) {
            LOG.warn(clazz.getSimpleName() + "\'s superclass not ParameterizedType");
            return Object.class;
        } else {
            Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
            if(index < params.length && index >= 0) {
                if(!(params[index] instanceof Class)) {
                    LOG.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
                    return Object.class;
                } else {
                    return (Class)params[index];
                }
            } else {
                LOG.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "\'s Parameterized Type: " + params.length);
                return Object.class;
            }
        }
    }

    public static List convertElementPropertyToList(Collection collection, String propertyName) {
        ArrayList list = new ArrayList();

        try {
            Iterator e = collection.iterator();

            while(e.hasNext()) {
                Object obj = e.next();
                list.add(PropertyUtils.getProperty(obj, propertyName));
            }

            return list;
        } catch (Exception var5) {
            throw convertReflectionExceptionToUnchecked(var5);
        }
    }

    public static String convertElementPropertyToString(Collection collection, String propertyName, String separator) {
        List list = convertElementPropertyToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }

    public static Object convertStringToObject(String value, Class<?> toType) {
        try {
            return ConvertUtils.convert(value, toType);
        } catch (Exception var3) {
            throw convertReflectionExceptionToUnchecked(var3);
        }
    }

    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        return (RuntimeException)(!(e instanceof IllegalAccessException) && !(e instanceof IllegalArgumentException) && !(e instanceof NoSuchMethodException)?(e instanceof InvocationTargetException?new RuntimeException("Reflection Exception.", ((InvocationTargetException)e).getTargetException()):(e instanceof RuntimeException?(RuntimeException)e:new RuntimeException("Unexpected Checked Exception.", e))):new IllegalArgumentException("Reflection Exception.", e));
    }

    static {
        DateConverter dc = new DateConverter();
        dc.setUseLocaleFormat(true);
        dc.setPatterns(new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"});
        ConvertUtils.register(dc, Date.class);
    }
}
