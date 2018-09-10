package com.example.demo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TfIdfServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(TfIdfServiceImpl.class);

    private static final int numLine = 5;

    private static ResourceLoader resourceLoader = new DefaultResourceLoader();
    private static final String sampleFilePath = "data/sample.txt";
    private static final String originalDataFilePath = "data/original";

    public static void main(String[] args) throws IOException {
        File sampleFile = resourceLoader.getResource(sampleFilePath).getFile();
        Scanner sc = new Scanner(sampleFile);
        tfIdf(sc);
    }

    private static void tfIdf(Scanner inputData) throws IOException {
        //get word map
        HashMap<String, Integer> wordMap = new HashMap<>();
        int totalCount = 0;
        for (int i = 0; i < TfIdfServiceImpl.numLine; i++) {
            String str = inputData.nextLine();
            String[] strArray = str.split(" ");
            totalCount += strArray.length;
            for (String aStrArray : strArray) {
                if (!wordMap.containsKey(aStrArray)) {
                    wordMap.put(aStrArray, 1);
                } else {
                    int v = wordMap.get(aStrArray);
                    wordMap.put(aStrArray, v + 1);
                }
            }
        }

        logger.info("TF map {}", wordMap.toString());

        //calculate tf
        HashMap<String, Double> tfMap = new HashMap<>();
        for (String key : wordMap.keySet()) {
            int v = wordMap.get(key);
            tfMap.put(key, 1.0 * v / totalCount);
        }

        logger.info("TF map {}", tfMap.toString());

        //calculate idf
        int totalClassNum = 1;
        HashMap<String, Integer> wordCountMap = new HashMap<>();
        Set<String> keySet = wordMap.keySet();
        for (String key : keySet) {
            wordCountMap.put(key, 1);
        }

        HashMap<String, Double> idfMap = new HashMap<>();
        File originalDataDirectory = resourceLoader.getResource(originalDataFilePath).getFile();
        if (originalDataDirectory.isDirectory()) {
            File[] originalDataFiles = originalDataDirectory.listFiles();
            assert originalDataFiles != null;
            totalClassNum = originalDataFiles.length;
            for (File originalDataFile : originalDataFiles) {
                boolean flag = true;
                BufferedReader reader = null;

                try {
                    reader = new BufferedReader(new FileReader(originalDataFile));

                    String line;
                    while (flag && (line = reader.readLine()) != null) {
                        for (String key : keySet) {
                            if (line.toLowerCase().contains(key.toLowerCase())) {
                                wordCountMap.put(key, wordCountMap.get(key)+1);
                                flag = false;
                                break;
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        assert reader != null;
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        for (String key : keySet) {
            idfMap.put(key, Math.log10(1.0 * (totalClassNum) / wordCountMap.get(key)));
        }

        logger.info("IDF map {}", idfMap.toString());

        //calculate tf-idf and sort
        HashMap<String, Double> tfIdfMap = new HashMap<>();
        Iterator it2 = tfMap.keySet().iterator();
        Iterator it3 = idfMap.keySet().iterator();
        while (it2.hasNext() && it3.hasNext()) {
            String key1 = (String) it2.next();
            String key2 = (String) it3.next();
            Double v = tfMap.get(key1)*idfMap.get(key2);
            tfIdfMap.put(key1, v);
        }
        List<Map.Entry<String, Double>> infoIds =
                new ArrayList<>(tfIdfMap.entrySet());
        infoIds.sort((o1, o2) -> {
            if (o2.getValue() - o1.getValue() > 0) {
                return 1;
            } else if (o2.getValue() - o1.getValue() < 0) {
                return -1;
            }
            return 0;
        });

        int selectNum;
        if (infoIds.size() < 5) {
            selectNum = infoIds.size();
        } else {
            selectNum = 5;
        }
        for (int q=0; q<selectNum; q++) {
            String id = infoIds.get(q).toString();
            int equalIndex = id.indexOf("=");
            char[] strChar = id.toCharArray();
            StringBuilder fea = new StringBuilder();
            for (int d=0; d<equalIndex; d++) {
                fea.append(strChar[d]);
            }
            System.out.println(fea);
        }
    }
}
