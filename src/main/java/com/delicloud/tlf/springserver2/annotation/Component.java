package com.delicloud.tlf.springserver2.annotation;

import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

/**
 * @author tlf
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface Component {
}
