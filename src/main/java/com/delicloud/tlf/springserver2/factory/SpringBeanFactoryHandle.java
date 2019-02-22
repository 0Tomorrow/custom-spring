package com.delicloud.tlf.springserver2.factory;

import com.delicloud.tlf.springserver2.annotation.Autowired;
import com.delicloud.tlf.springserver2.annotation.Component;
import com.delicloud.tlf.springserver2.annotation.SpringBootApplication;
import com.delicloud.tlf.springserver2.container.annotation.AnnotationContainer;
import com.delicloud.tlf.springserver2.exception.SpringException;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author tlf
 */
public class SpringBeanFactoryHandle {
    private Class mainApplicationClass;

    public void scanApplicationPackage() {
        Set<Class> classes = AnnotationContainer.get(getScanPackage(), Component.class);
        List<Object> beanList = classes.stream()
            .filter(clazz -> !clazz.isAnnotation())
            .filter(clazz -> !clazz.isInterface())
            .map(clazz -> {
                try {
                    return clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new SpringException(e);
                }
            })
            .collect(Collectors.toList());
        SpringBeanFactory.getInstance().addSpringBean(beanList);
        SpringBeanFactory.getInstance().injectDependency(Autowired.class);
    }

    public static <T> T getBean(Class<T> clazz) {
        return SpringBeanFactory.getInstance().getBean(clazz);
    }

    private String[] getScanPackage() {
        if (null == mainApplicationClass) {
            mainApplicationClass = deduceMainApplicationClass();
        }
        SpringBootApplication annotation = (SpringBootApplication) mainApplicationClass.getAnnotation(SpringBootApplication.class);
        String[] strings = annotation.scanBasePackages();
        if (strings.length == 0) {
            return new String[]{mainApplicationClass.getPackage().getName()};
        }
        return strings;
    }

    private Class<?> deduceMainApplicationClass() {
        try {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if ("main".equals(stackTraceElement.getMethodName())) {
                    return Class.forName(stackTraceElement.getClassName());
                }
            }
        } catch (ClassNotFoundException ex) {
            throw new SpringException(ex);
        }
        throw new SpringException("未找到main方法");
    }
}
