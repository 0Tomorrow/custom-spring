package com.delicloud.tlf.springserver2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringServer2ApplicationTests {

    @Test
    public void contextLoads() {
    }

    public static void main(String[] args) {
        Map<String, List<String>> map = new HashMap<>();
        map.put("123", Stream.of("234").collect(Collectors.toList()));
        map.get("123").add("345");
        System.out.println(map.get("123"));
    }

}
