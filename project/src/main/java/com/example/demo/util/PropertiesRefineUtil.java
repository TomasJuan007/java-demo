package com.example.demo.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class PropertiesRefineUtil {

    private static final String propsPath = "application-kafka.properties";

    private static final String filePath = "/opt/java-demo/project/conf/example.properties";

    public static void main(String[] args) throws IOException {
        PropertiesRefineUtil.replace(propsPath, filePath);
    }

    public static void replace(String propsPath, String filePath) throws IOException {
        Map<String, String> result = new LinkedHashMap<>();
        Resource resource = new ClassPathResource(propsPath);
        BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()));
        String sCurrentLine;
        while ((sCurrentLine = reader.readLine()) != null) {
            String[] array = sCurrentLine.split("=");
            if (array.length == 2) {
                result.put(array[0].toUpperCase().replace('.','_'), array[1]);
            }
        }

        for (Map.Entry entry : result.entrySet()) {
            System.out.println("sed -i \"s#@" + entry.getKey() + "@${" + entry.getKey() + ":" + entry.getValue() + "}\" " + filePath);
        }
    }
}
