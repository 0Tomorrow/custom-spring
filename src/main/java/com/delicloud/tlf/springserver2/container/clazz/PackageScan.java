package com.delicloud.tlf.springserver2.container.clazz;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author tlf
 */
class PackageScan {
    private Set<Class> eleStrategyList = new HashSet<>();

    /**
     * 默认使用的类加载器
     */
    private ClassLoader classLoader = PackageScan.class.getClassLoader();

    /**
     * 需要扫描的策略包名
     */
    private static String strategyPath;

    static Set<Class> packageScan(String packagePath) {
        strategyPath = packagePath;
        PackageScan packageScan = new PackageScan();
        packageScan.addClass();
        return packageScan.eleStrategyList;
    }

    /**
     * 获取包下所有实现了superStrategy的类并加入list
     */
    private void addClass() {
        URL url = classLoader.getResource(strategyPath.replace(".", "/"));
        String protocol = url.getProtocol();
        if ("file".equals(protocol)) {
            // 本地自己可见的代码
            findClassLocal(strategyPath);
        } else if ("jar".equals(protocol)) {
            // 引用jar包的代码
            findClassJar(strategyPath);
        }
    }

    private void findClassLocal(final String packName){
        URI url = null ;
        try {
            url = classLoader.getResource(packName.replace(".", "/")).toURI();
        } catch (URISyntaxException e1) {
            throw new RuntimeException("未找到策略资源");
        }

        File file = new File(url);
        file.listFiles(new FileFilter() {

            @Override
            public boolean accept(File chiFile) {
                if(chiFile.isDirectory()){
                    findClassLocal(packName+"."+chiFile.getName());
                }
                if(chiFile.getName().endsWith(".class")){
                    Class<?> clazz = null;
                    try {
                        clazz = classLoader.loadClass(packName + "." + chiFile.getName().replace(".class", ""));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    eleStrategyList.add(clazz);
                    return true;
                }
                return false;
            }
        });
    }

    private void findClassJar(final String packName) {
        String pathName = packName.replace(".", "/");
        JarFile jarFile = null;
        try {
            URL url = classLoader.getResource(pathName);
            JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
            jarFile = jarURLConnection.getJarFile();
        } catch (IOException e) {
            throw new RuntimeException("未找到策略资源");
        }

        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarEntryName = jarEntry.getName();

            if (jarEntryName.contains(pathName) && !jarEntryName.equals(pathName + "/")) {
                //递归遍历子目录
                if (jarEntry.isDirectory()) {
                    String clazzName = jarEntry.getName().replace("/", ".");
                    int endIndex = clazzName.lastIndexOf(".");
                    String prefix = null;
                    if (endIndex > 0) {
                        prefix = clazzName.substring(0, endIndex);
                    }
                    findClassJar(prefix);
                }
                if (jarEntry.getName().endsWith(".class")) {
                    Class<?> clazz = null;
                    try {
                        clazz = classLoader
                            .loadClass(jarEntry.getName().replace("/", ".").replace(".class", ""));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    eleStrategyList.add(clazz);
                }
            }

        }
    }
}
