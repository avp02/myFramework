package com.vizorgames.interview.context;

import com.vizorgames.interview.factory.BeanFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {
    private BeanFactory beanFactory;
    private final Map<Class, Object> beanMap = new ConcurrentHashMap<>();

    public ApplicationContext() {

    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory=beanFactory;
    }

    public <T> T getBeanContext(Class<T> clazz) {
        if (beanMap.containsKey(clazz)) {
            return (T) beanMap.get(clazz);
        }
        T bean = null;
        try {
            bean = beanFactory.getBean(clazz);
            beanMap.put(clazz, bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }
}
