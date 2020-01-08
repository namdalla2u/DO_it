package net.plang.HoWooAccount.common.servlet.context;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;


public class ApplicationContext {
    private HashMap<String, Object> map;

    @SuppressWarnings("deprecation")
    public ApplicationContext(ServletConfig config, ServletContext application) {
        System.out.println("		@ ApplicationContext접근");
        map = new HashMap<String, Object>();
        String path = config.getInitParameter("configFile"); //web.xml에 입력된  /initparam/configFile의 value

        System.out.println("		@ web.xml에서 입력된 configFile:" + path);

        String rPath = application.getRealPath(path); //절대경로를 읽어옴. 
//        System.out.println("rPath:"+rPath);
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(rPath)); //프로퍼티파일 로드

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<String> set = properties.stringPropertyNames();
        for (String key : set) {
            String value = properties.getProperty(key);

            System.out.println("		@ 프로퍼티: " + value);

            Object obj = null;
            try {
                obj = Class.forName(value).newInstance();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            map.put(key, obj);
        }
    }

    public Object getBean(String beanName) {

        System.out.println("		@ Bean 네임: " + beanName);
        return map.get(beanName);
    }

}
