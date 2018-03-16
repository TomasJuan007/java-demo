package com.example.demo.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DestroyUtil {
    private static final String AGREEMENT = "I (speak out you name here) " +
            "take full responsibility for whatever happens running this util.";

    public static void main(String[] args) throws Exception {

        try {
            if (!args[0].startsWith(AGREEMENT)) {
                throw new Exception();
            }
            File file = new File(args[1]);

            trasverseDelete(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void trasverseDelete(File file) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();

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
