package com.vizorgames.interview.factory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanFactoryWithNameClass {


    private final static Map<String, Object> instances = new ConcurrentHashMap<>();

    static {
        try (Stream<Path> pathStream = Files.walk(Paths.get("./target"))) {
            List<String> classesList = pathStream
                    .filter(a -> a.getFileName().toString().contains(".class"))
                    .map(a -> a.toString())
                    .map(a -> a.substring(a.indexOf("classes/") + "classes/".length(), a.indexOf(".class")))
                    .map(a -> a.replaceAll("/", "."))
                    .collect(Collectors.toList());
            for (String className : classesList) {
                try {
                    Class<?> clazz = Class.forName(className);
                    instances.put(className.substring(className.lastIndexOf(".") + 1), (Object) clazz.newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static <T> Object getBean(String implementationClass) {
        if (instances.get(implementationClass) != null) {
            return instances.get(implementationClass);
        }
        return null;
    }
}
