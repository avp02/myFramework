package com.vizorgames.interview.factory;

import com.vizorgames.interview.annotation.Inject;
import com.vizorgames.interview.config.Configuration;
import com.vizorgames.interview.config.JavaConfiguration;
import com.vizorgames.interview.configurator.BeanConfigurator;
import com.vizorgames.interview.configurator.JavaBeanConfigurator;
import com.vizorgames.interview.context.ApplicationContext;
import com.vizorgames.interview.exception.BindingNotFoundException;
import com.vizorgames.interview.exception.ConstructorAmbiguityException;
import com.vizorgames.interview.exception.NoSuitableConstructorException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class BeanFactory {

    private final Configuration configuration;
    private final BeanConfigurator beanConfigurator;
    private final ApplicationContext applicationContext;

    public BeanFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.configuration = new JavaConfiguration();
        this.beanConfigurator = new JavaBeanConfigurator(configuration.getPackageToScan(), configuration.getInterfaceToImplementations());
    }

    public BeanConfigurator getBeanConfigurator() {
        return beanConfigurator;
    }

    public <T> T getBean(Class<T> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<? extends T> implementationClass = clazz;

        if (implementationClass.isInterface()) {
            implementationClass = beanConfigurator.getImplementationClass(clazz);
        }

        T bean = null;
        Constructor<?>[] declaredConstructors = implementationClass.getDeclaredConstructors();
        ArrayList<Object> beans = new ArrayList<>();
        try {
            for (Constructor<?> constr : declaredConstructors) {
                if (!constr.isAnnotationPresent(Inject.class)) {
                    bean = implementationClass.getDeclaredConstructor().newInstance();
                }

            }
        } catch (NoSuchMethodException ex) {
            throw new NoSuitableConstructorException();
        }

        int count = 0;

        for (Constructor<?> constructor : Arrays.stream(implementationClass.getDeclaredConstructors())
                .filter(a -> a.isAnnotationPresent(Inject.class)).collect(Collectors.toList())) { //TODO проверь правильная аннотация
            constructor.setAccessible(true);
            count++;
        }
        if (count > 1) {
            throw new ConstructorAmbiguityException();
        }
        for (Constructor<?> constr : declaredConstructors) {
            if (constr.isAnnotationPresent(Inject.class)) {

                Class<?>[] parameterTypes = constr.getParameterTypes();

                for (Class<?> param : parameterTypes) {
                    Object bean1 = applicationContext.getBeanContext(param);
                    beans.add(bean1);
                }
                try {
                    bean = (T) constr.newInstance(beans.toArray());
                } catch (Exception e) {
                    throw new BindingNotFoundException();
                }

            }
        }
        return bean;
    }
}
