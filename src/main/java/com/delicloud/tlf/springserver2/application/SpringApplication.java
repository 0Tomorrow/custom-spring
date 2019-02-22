package com.delicloud.tlf.springserver2.application;

import com.delicloud.tlf.springserver2.factory.SpringBeanFactoryHandle;

/**
 * @author tlf
 */
public class SpringApplication {
    public static void run(Class application, String[] args) {
        SpringApplication.run();
    }

    private static void run() {
        new SpringBeanFactoryHandle().scanApplicationPackage();

    }
}
