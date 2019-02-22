package com.delicloud.tlf.springserver2;

import com.delicloud.tlf.springserver2.annotation.Autowired;
import com.delicloud.tlf.springserver2.annotation.SpringBootApplication;
import com.delicloud.tlf.springserver2.application.SpringApplication;
import com.delicloud.tlf.springserver2.factory.SpringBeanFactoryHandle;
import com.delicloud.tlf.springserver2.ztest.Test;

/**
 * @author tlf
 */
@SpringBootApplication
public class Application {

    @Autowired
    public Test test;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        Application bean = SpringBeanFactoryHandle.getBean(Application.class);
        bean.test();
    }

    public void test() {
        test.test();
    }
}
