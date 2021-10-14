package com.vizorgames.interview.ioc;

import com.vizorgames.interview.annotation.Inject;
import com.vizorgames.interview.exception.ConstructorAmbiguityException;
import com.vizorgames.interview.exception.NoSuitableConstructorException;
import com.vizorgames.interview.factory.BeanFactory;

import javax.swing.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InjectorImpl implements Injector
{
    @Override
    public <T> Provider<T> getProvider(Class<T> type)
    {
        for (Constructor<?> constructor : type.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                try {
                    constructor.newInstance(Inject.class);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public <T> void bind(Class<T> base, Class<? extends T> impl)
    {

    }

    @Override
    public <T> void bindSingleton(Class<T> base, Class<? extends T> impl)
    {

    }
}
