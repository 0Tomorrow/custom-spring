package com.delicloud.tlf.springserver2.container.annotation;

import com.delicloud.tlf.springserver2.container.clazz.ClassContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author tlf
 */
class AnnotationScan {
    private Set<Class> set;

    AnnotationScan(String packageScan) {
        set =  ClassContainer.get(packageScan);
    }

    Set<Class> scan(Class<? extends Annotation> annotation) {
        return set.stream().filter(clazz ->
            includeAnnotations(clazz.getAnnotations(), annotation)
        ).collect(Collectors.toSet());
    }

    private static boolean includeAnnotations(Annotation[] annotations, Class<? extends Annotation> annotation) {
        List<Annotation> list = Arrays.stream(annotations)
            .filter(c -> {
                String name = c.annotationType().getSimpleName();
                return !("Target" .equals(name) || "Documented" .equals(name) || "Retention" .equals(name));
            })
            .filter(c ->
                c.annotationType().getName().equals(annotation.getName()) || includeAnnotations(
                    c.annotationType(), annotation)
            )
            .collect(Collectors.toList());
        return !list.isEmpty();
    }

    private static boolean includeAnnotations(Class clazz, Class<? extends Annotation> annotation) {
        return includeAnnotations(clazz.getAnnotations(), annotation);
    }
}
