package com.delicloud.tlf.springserver2.exception;

/**
 * @author tlf
 */
public class SpringException extends RuntimeException {
    public SpringException(String msg) {
        super(msg);
    }

    public SpringException(Exception e) {
        super(e);
    }
}
