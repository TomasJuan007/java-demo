package com.example.demo.util;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CommentRemoveUtil {
    private static final ResourceLoader resourceLoader = new DefaultResourceLoader();

    private static final String targetFile = "data/util/cassandra.yaml";

    public static void main(String[] args) throws IOException {
        CommentRemoveUtil.refine(targetFile);
    }

    public static void refine(String targetFile) throws IOException {
        File file = resourceLoader.getResource(targetFile).getFile();
        Scanner sc = new Scanner(file);
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (!line.trim().startsWith("#") && !"".endsWith(line)) {
                System.out.println(line);
            }
        }
    }
}
