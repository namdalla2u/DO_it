package net.plang.HoWooAccount.common.util;

import net.sf.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanCreator {
    private static BeanCreator ourInstance = new BeanCreator();

    public static BeanCreator getInstance() {
        return ourInstance;
    }

    public <T> T create(JSONObject jsonObject, Class<T> beanClass) { // json, empBean.class
        T instance = null; //empBean

        try {
            instance = beanClass.newInstance();

            for (Object key : jsonObject.keySet()) { //empName
                String firstUpperKey = key.toString().substring(0, 1).toUpperCase() + key.toString().substring(1);
                String methodName = "set" + firstUpperKey; // setEmpCode

                Method method = null;
                try {
                    method = beanClass.getMethod(methodName, String.class);
                } catch (NoSuchMethodException e) {
                    System.out.println("존재하지 않는 빈네임 : " + firstUpperKey);
                }

                if (method != null)
                    method.invoke(instance, jsonObject.get(key).toString()); 
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return instance;
    }
    
    private BeanCreator() {
    }
}
