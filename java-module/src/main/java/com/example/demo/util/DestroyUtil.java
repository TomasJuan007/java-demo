package com.example.demo.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class DestroyUtil {

    public static void destroy(String mode, String filepath, int depthInt, String endWith) {
        try {
            switch (mode) {
                case "PathWalk":
                    Path path = Paths.get(filepath);
                    List<File> fileList = Files.walk(path, depthInt, FileVisitOption.FOLLOW_LINKS)
                            .filter(e -> Files.isRegularFile(e) && e.getFileName().toString().endsWith(endWith))
                            .map(Path::toFile)
                            .collect(Collectors.toList());

                    for (File file : fileList) {
                        FileWriter fileWriter = new FileWriter(file);
                        fileWriter.write("");
                        fileWriter.close();
                    }
                    break;
                case "Transverse":
                    try {
                        File file = new File(filepath);

                        trasverseDelete(file);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void trasverseDelete(File file) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();

            if (files == null) {
                return;
            }

            for (File file1 : files) {
                trasverseDelete(file1);
            }
        } else {

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.close();
        }
    }
}
