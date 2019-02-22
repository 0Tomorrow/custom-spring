package com.delicloud.tlf.springserver2.container.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tlf
 */
public class ConfigContainer {
    private Map configMap = new ConcurrentHashMap();

    private void getConfigMap() {
        configMap = YmlScan.read();
    }

    public String getConfig(String configName) {
        if (null == configMap) {
            getConfigMap();
        }
        String[] strings = configName.split("\\.");
        Map map = (Map) (configMap.get("tempKey"));
        for (String key : strings) {
            if (map.get(key) instanceof Map) {
                map = (Map) (map.get(key));
            } else {
                return map.get(key) == null ? null : map.get(key).toString();
            }
        }
        return null;
    }

}
