package com.delicloud.tlf.springserver2.util;

import java.util.Collection;

/**
 * @author tlf
 */
public class CollectionUtil {
    public static <T> T getFirst(Collection<T> list) {
        if (list == null) {
            return null;
        }
        if (list.size() == 0) {
            return null;
        }
        return list.iterator().next();
    }
}
