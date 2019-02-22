package com.delicloud.tlf.springserver2.annotation;

import java.lang.annotation.*;

/**
 * @author tlf
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
}
