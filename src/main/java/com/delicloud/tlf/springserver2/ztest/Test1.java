package com.delicloud.tlf.springserver2.ztest;

import com.delicloud.tlf.springserver2.annotation.Component;

/**
 * @author tlf
 */
@Component
public class Test1 implements Test {

    @Override
    public void test() {
        System.out.println("this is test1");
    }
}
