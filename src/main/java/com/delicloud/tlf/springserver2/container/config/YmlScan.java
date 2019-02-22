package com.delicloud.tlf.springserver2.container.config;

import lombok.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tlf
 */
class YmlScan {
    private Map<String, Object> rootMap = new HashMap<>();

    static Map read() {
        return new YmlScan().find();
    }

    private Map find() {
        String filePath = this.getClass().getResource("/application.yml").getPath();
        rootMap.put("tempKey", null);
        List<LineInfo> lineList = null;
        try {
            lineList = readFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<List<LineInfo>> list = new ArrayList<>();
        lineList.forEach(line -> {
            List<LineInfo> rowList = new ArrayList<>();
            initList(rowList, 100);
            rowList.add(line.getSpaceCount(), line);
            list.add(line.getLineNum(), rowList);
        });
        List<List<Map<String, Object>>> mapList = toMap(list);
        for (int i = list.size() - 1; i >= 0; i--) {
            List<Map<String, Object>> mList = mapList.get(i);
            Map map = thisMap(mList);
            Map fatherMap = toFatherMap(i, mapList);
            if (fatherMap.get(key(fatherMap)) == null) {
                fatherMap.put(key(fatherMap), map);
            } else {
                ((HashMap) (fatherMap.get(key(fatherMap)))).putAll(map);
            }
            list.remove(i);
        }
        list.forEach(this::removeNull);
        return (Map) (rootMap.get("tempKey"));
    }

    private List<LineInfo> readFile(String filePath) throws IOException {
        List<LineInfo> list = new ArrayList<>();
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        int num = 0;
        while (br.ready()) {
            String lineString = br.readLine();
            LineInfo lineInfo = new LineInfo(lineString, num++);
            list.add(lineInfo);
        }
        return list;
    }

    private List<List<Map<String, Object>>> toMap(List<List<LineInfo>> lists) {
        List<List<Map<String, Object>>> mapList = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            for (int j = 0; j < lists.get(i).size(); j++) {
                LineInfo lineInfo = lists.get(i).get(j);
                if (lineInfo == null) {
                    continue;
                }
                Map<String, Object> map = new HashMap<>();
                map.put(lineInfo.getKey(), lineInfo.getValue());
                List list = new ArrayList();
                addNull(list, j);
                list.add(j, map);
                if (null != lists.get(i).get(j)) {
                    if (mapList.size() <= i || mapList.get(i) == null) {
                        mapList.add(i, list);
                    } else {
                        mapList.get(i).add(j, map);
                    }
                }
            }
        }
        return mapList;
    }

    private void addNull(List list, int count) {
        for (int i = 0; i < count; i++) {
            list.add(null);
        }
    }

    private String key(Map m) {
        if (null == m) {
            return null;
        }
        for (Object o : m.keySet()) {
            return null == o ? null : o.toString();
        }
        return null;
    }

    private Map thisMap(List<Map<String, Object>> list) {
        Map tempMap = null;
        for (int i = list.size() - 1; i >= 0; i--) {
            Map<String, Object> lineInfo = list.get(i);
            if (null != lineInfo) {
                Map lineMap = new HashMap();
                lineMap.put(key(lineInfo), tempMap == null ? lineInfo.get(key(lineInfo)) : tempMap);
                tempMap = lineMap;
            }
        }
        return tempMap;
    }

    private Map toFatherMap(int num, List<List<Map<String, Object>>> list) {
        int target = firstEle(list.get(num));
        for (int i = num; i >= 0; i--) {
            List<Map<String, Object>> lList = list.get(i);
            int count = firstEle(lList);
            if (count < target) {
                int a = target;
                while (true) {
                    if (lList.size() <= a || null == lList.get(a)) {
                        a--;
                        continue;
                    }
                    Map<String, Object> map = list.get(i).get(a);
                    return map;
                }
            }
        }
        return rootMap;
    }

    private int firstEle(List list) {
        int i = 0;
        for (Object o : list) {
            if (null != o) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private void initList(List<LineInfo> list, int count) {
        for (int i = 0; i < count; i++) {
            list.add(null);
        }
    }

    private void removeNull(List list) {
        int count = firstEle(list);
        while (list.remove(null)) {
        }
        for (int i = 0; i < count; i++) {
            list.add(0, null);
        }
    }

    @Data
    class LineInfo {
        private int lineNum;
        private int spaceCount;
        private String key;
        private String value;

        public LineInfo() {
        }

        LineInfo(String lineString, int lineNum) {
            this.lineNum = lineNum;
            setSpaceCount(lineString);
            setKeyAndValue(lineString);
        }

        private void setKeyAndValue(String lineString) {
            int i = lineString.indexOf(":");
            if (i == -1) {
                throw new RuntimeException("配置文件格式异常");
            }
            this.key = lineString.substring(0, i).trim();
            String value = lineString.substring(i + 1, lineString.length()).trim();
            this.value = "".equals(value) ? null : value;
        }

        private void setSpaceCount(String lineString) {
            int result = 0;
            for (char c : lineString.toCharArray()) {
                if (c == ' ') {
                    result++;
                }
            }
            this.spaceCount = result;
        }
    }
}
