package com.delicloud.tlf.springserver2.container.clazz;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tlf
 */
public class ClassContainer {
    private static Map<String, Set<Class>> map = new ConcurrentHashMap<>();

    private static void scan(String scanPackage) {
        Set<Class> classes = PackageScan.packageScan(scanPackage);
        if (!map.containsKey(scanPackage)) {
            map.put(scanPackage, classes);
        }
    }

    public static Set<Class> get(String scanPackage) {
        scan(scanPackage);
        return map.get(scanPackage);
    }

}
