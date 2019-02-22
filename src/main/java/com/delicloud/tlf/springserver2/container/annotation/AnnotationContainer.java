package com.delicloud.tlf.springserver2.container.annotation;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tlf
 */
public class AnnotationContainer {
    private static Map<String, Map<String, Set<Class>>> map = new ConcurrentHashMap<>();

    private static void scan(String scanPackage, Class<? extends Annotation> annotation) {
        Set<Class> classes = new AnnotationScan(scanPackage).scan(annotation);
        if (!map.containsKey(scanPackage)) {
            Map<String, Set<Class>> annotationMap = new ConcurrentHashMap<>();
            annotationMap.put(annotation.getName(), classes);
            map.put(scanPackage, annotationMap);
        } else {
            Map<String, Set<Class>> setMap = map.get(scanPackage);
            if (null != setMap) {
                setMap.put(annotation.getName(), classes);
            }
        }
    }

    public static Set<Class> get(String scanPackage, Class<? extends Annotation> annotation) {
        scan(scanPackage, annotation);
        return map.get(scanPackage).get(annotation.getName());
    }

    public static Set<Class> get(String[] scanPackages, Class<? extends Annotation> annotation) {
        Optional<Set<Class>> reduce = Arrays.stream(scanPackages)
            .map(scanPackage -> get(scanPackage, annotation))
            .reduce((s1, s2) -> {
                s1.addAll(s2);
                return s1;
            });
        return reduce.orElseGet(HashSet::new);
    }

    public static Map<String, Map<String, Set<Class>>> get() {
        return map;
    }
}
