package com.example.demo.util;

import org.apache.commons.cli.*;

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
    private static final String AGREEMENT = "I (speak out you name here) " +
            "take full responsibility for whatever happens running this util.";

    public static void main(String[] args) throws Exception {

        if (!args[0].startsWith(AGREEMENT)) {
            throw new Exception();
        }

        CommandLineParser cliParser = new DefaultParser();
        try {
            CommandLine line = cliParser.parse(getOptions(), args);

            String mode = line.getOptionValue("mode");
            String filepath = line.getOptionValue("filepath");
            String depth = line.getOptionValue("depth", "1");
            int depthInt = Integer.parseInt(depth);
            String endWith = line.getOptionValue("endWith");

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
        } catch (MissingOptionException e) {
            System.out.println(e.getMessage());
            showUsage();
        }
    }

    private static Options getOptions() {
        Option mode = Option.builder("m").longOpt("mode").numberOfArgs(1).required(true).type(String.class)
                .desc("mode, [PathWalk, Transverse]").build();
        Option filepath = Option.builder("fp").longOpt("filepath").numberOfArgs(1).required(true).type(String.class)
                .desc("file path, e.g. C:\\Users\\ETOMHUA\\Desktop\\dump").build();
        Option depth = Option.builder("d").longOpt("depth").numberOfArgs(1).required(false).type(String.class)
                .desc("depth for PathWalk mode").build();
        Option endWith = Option.builder("ew").longOpt("endWith").numberOfArgs(1).required(false).type(String.class)
                .desc("endWith pattern for PathWalk mode").build();

        Options options = new Options();
        options.addOption(mode);
        options.addOption(filepath);
        options.addOption(depth);
        options.addOption(endWith);
        return options;
    }

    private static void showUsage() {
        String usage = "destroy file in file system";
        String hint = "HINT: make sure that you've read the agreement." +
                "(\"I (speak out you name here) take full responsibility for whatever happens running this util.\")";
        System.out.println(usage);
        System.out.println(hint);
        for (Option option : getOptions().getOptions()) {
            showArgHelp(option);
        }
    }

    private static void showArgHelp(Option option) {
        String shortOpt = option.getOpt() == null ? "" : "-" + option.getOpt();
        String longOpt = option.getLongOpt() == null ? "" : "--" + option.getLongOpt() + ",";
        String mandatory = option.isRequired() ? "mandatory," : "";
        String desc = option.getDescription();
        System.out.printf("%6s  %-18s %s\n", shortOpt, longOpt, mandatory + desc);
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
