package net.plang.HoWooAccount.common.sl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ServiceLocator {

    private Map<String, DataSource> cache;

    private Context envCtx;
    private static ServiceLocator instance;

    static {
        try {
            instance = new ServiceLocator();
            System.out.println("		@ ServiceLocator접근");
        } catch (ServiceLocatorException e) {
            e.printStackTrace();
        }

    }

    private ServiceLocator() {
        try {
            envCtx = new InitialContext();
            cache = Collections.synchronizedMap(new HashMap<String, DataSource>());

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceLocatorException(e.getMessage());
        }
    }

    public static ServiceLocator getInstance() {
        return instance;
    }

    public DataSource getDataSource(String jndiName) {

        DataSource dataSource;
        System.out.println("		@ 데이터 소스 객체를 얻기 위한 메서드 getDataSource");
        try {
            if (cache.containsKey(jndiName)) {
                dataSource = cache.get(jndiName);
            } else {
                dataSource = (DataSource) envCtx.lookup("java:comp/env/" + jndiName);
                cache.put(jndiName, dataSource);
            }
            System.out.println("		@ jndi로 server.xml에 있는 계정 정보를 찾는다");
            System.out.println("		@ jndi: " + jndiName);
        } catch (NamingException e) {
            throw new ServiceLocatorException(e.getMessage());

        }
        return dataSource;
    }

}

