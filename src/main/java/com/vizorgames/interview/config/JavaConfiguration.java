package com.vizorgames.interview.config;

import com.vizorgames.interview.ioc.Injector;

import java.util.Map;

public class JavaConfiguration implements Configuration{
    @Override
    public String getPackageToScan() {
        return "com.vizorgames.interview"; //TODO посмотри тот ли пакет
    }

    @Override
    public Map<Class, Class> getInterfaceToImplementations() {
        return Map.of(Injector.class, Injector.class);
    }
}
