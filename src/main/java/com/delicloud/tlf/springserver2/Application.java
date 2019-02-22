package com.delicloud.tlf.springserver2;

import com.delicloud.tlf.springserver2.annotation.Autowired;
import com.delicloud.tlf.springserver2.annotation.SpringBootApplication;
import com.delicloud.tlf.springserver2.application.SpringApplication;
import com.delicloud.tlf.springserver2.container.annotation.AnnotationContainer;
import com.delicloud.tlf.springserver2.container.clazz.ClassContainer;
import com.delicloud.tlf.springserver2.factory.SpringBeanFactory;
import com.delicloud.tlf.springserver2.factory.SpringBeanFactoryHandle;
import com.delicloud.tlf.springserver2.ztest.Test;

import java.lang.reflect.Field;

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
//        System.out.println(AnnotationContainer.get());
//        System.out.println(ClassContainer.get());
//        System.out.println(SpringBeanFactory.getInstance().get());
    }

    public void test() {
        test.test();
    }

//    public static void main(String[] args) {
//        Field[] declaredFields = Application.class.getDeclaredFields();
//        System.out.println(declaredFields[0].getDeclaringClass().getName());
//    }
}
