package com.delicloud.tlf.springserver2.factory;

import com.delicloud.tlf.springserver2.exception.SpringException;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tlf
 */
public class SpringBeanFactory {
    private static SpringBeanFactory springBeanFactory;
    private Map<String, List<Object>> springBeans = new ConcurrentHashMap<>();

    private SpringBeanFactory() {
    }

    static SpringBeanFactory getInstance() {
        if (springBeanFactory == null) {
            springBeanFactory = new SpringBeanFactory();
        }
        return springBeanFactory;
    }

    public void addSpringBean(Map<Class, Object> beans) {
        beans.forEach(this::addSpringBean);
    }

    private void addSpringBean(Class clazz, Object bean) {
        if ("java.lang.Object".equals(clazz.getName())) {
            return;
        }
        if (springBeans.containsKey(clazz.getName())) {
            springBeans.get(clazz.getName()).add(bean);
        } else {
            springBeans.put(clazz.getName(), Stream.of(bean).collect(Collectors.toList()));
        }
    }

    void addSpringBean(Collection<Object> beans) {
        beans.forEach(this::addSpringBean);
    }

    private void addSpringBean(Object bean) {
        addParentAndInterface(bean.getClass(), bean);
        addSpringBean(bean.getClass(), bean);
    }

    <T> T getBean(Class<T> clazz) {
        return (T) (springBeans.get(clazz.getName()).get(0));
    }

    private void addParentAndInterface(Class clazz, Object bean) {
        Class[] interfaces = clazz.getInterfaces();
        Class superclass = clazz.getSuperclass();
        List<Class> list = Stream.of(interfaces).collect(Collectors.toList());
        if (null != superclass) {
            list.add(superclass);
        }
        list.forEach(cla -> {
            addSpringBean(cla, bean);
            addParentAndInterface(cla, bean);
        });
    }

    void injectDependency(Class<? extends Annotation> injectAnnotation) {
        springBeans.forEach((key, value) -> {
            if (value.size() > 1) {
                throw new SpringException("implementation more than one");
            }
            if (value.size() == 0) {
                throw new SpringException("no implementation ");
            }
        });
        springBeans.values().stream()
            .map(list -> list.get(0))
            .distinct()
            .forEach(object -> {
                Arrays.stream(object.getClass().getDeclaredFields())
                        .filter(field -> field.getAnnotation(injectAnnotation) != null)
                        .forEach(field -> {
                            try {
                                System.out.println(springBeans.get(field.getType().getName()).get(0));
                                field.set(object, springBeans.get(field.getType().getName()).get(0));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        });
                }
            );
//        springBeans.keySet()
//            .forEach(System.out::println);
    }

    public Map<String, List<Object>> get() {
        return springBeans;
    }



}
