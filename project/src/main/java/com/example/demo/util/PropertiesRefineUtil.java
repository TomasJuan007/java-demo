package com.example.demo.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class PropertiesRefineUtil {

    //args[0]: data/util/application-kafka.properties
    //args[1]: /opt/jav-demo/project/conf/example.properties
    public static void main(String[] args) throws IOException {
        Map<String, String> result = new LinkedHashMap<>();
        Resource resource = new ClassPathResource(args[0]);
        BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()));
        String sCurrentLine;
        while ((sCurrentLine = reader.readLine()) != null) {
            String[] array = sCurrentLine.split("=");
            if (array.length == 2) {
                result.put(array[0].toUpperCase().replace('.','_'), array[1]);
            }
        }

        for (Map.Entry entry : result.entrySet()) {
            System.out.println("sed -i \"s#@" + entry.getKey() + "@${" + entry.getKey() + ":" + entry.getValue() + "}\" " + args[1]);
        }
    }
}
