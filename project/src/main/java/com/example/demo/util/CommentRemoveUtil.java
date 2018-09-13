package com.example.demo.util;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CommentRemoveUtil {
    public static void main(String[] args) throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        File file = resourceLoader.getResource("data/util/cassandra.yaml").getFile();
        Scanner sc = new Scanner(file);
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (!line.trim().startsWith("#") && !"".endsWith(line)) {
                System.out.println(line);
            }
        }
    }
}
