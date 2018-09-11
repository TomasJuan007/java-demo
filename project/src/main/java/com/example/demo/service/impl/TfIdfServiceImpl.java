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

    private static int totalCount = 0;
    private static Set<String> keySet;

    private static ResourceLoader resourceLoader = new DefaultResourceLoader();
    private static final String sampleFilePath = "data/sample.txt";
    private static final String originalDataFilePath = "data/original";

    public static void main(String[] args) throws IOException {
        File sampleFile = resourceLoader.getResource(sampleFilePath).getFile();
        tfIdf(sampleFile);
    }

    private static void tfIdf(File sampleFile) throws IOException {
        //get word map
        HashMap<String, Integer> wordMap = getWordMap(sampleFile);
        keySet = wordMap.keySet();

        //calculate tf
        HashMap<String, Double> tfMap = getTfMap(wordMap, totalCount);

        //calculate idf
        HashMap<String, Double> idfMap = getIdfMap(originalDataFilePath);

        //calculate tf-idf and sort
        HashMap<String, Double> tfIdfMap = getTfIdfMap(tfMap, idfMap);
        logger.info("TF*IDF map {}", tfIdfMap.toString());
    }

    private static HashMap<String,Double> getTfIdfMap(HashMap<String,Double> tfMap, HashMap<String,Double> idfMap) {
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
        return tfIdfMap;
    }

    private static HashMap<String,Double> getIdfMap(String originalDataFilePath) throws IOException {
        int totalClassNum = 1;
        HashMap<String, Integer> wordCountMap = new HashMap<>();

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

                BufferedReader reader = null;

                try {
                    reader = new BufferedReader(new FileReader(originalDataFile));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        for (String key : keySet) {
                            boolean flag = false;
                            if (line.toLowerCase().contains(key.toLowerCase())) {
                                flag = true;
                            }
                            if (flag) {
                                wordCountMap.put(key, wordCountMap.get(key) + 1);
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
        return idfMap;
    }

    private static HashMap<String,Double> getTfMap(HashMap<String,Integer> wordMap, int totalCount) {
        HashMap<String, Double> tfMap = new HashMap<>();
        for (String key : keySet) {
            int v = wordMap.get(key);
            tfMap.put(key, 1.0 * v / totalCount);
        }

        logger.info("TF map {}", tfMap.toString());
        return tfMap;
    }

    private static HashMap<String, Integer> getWordMap(File sampleFile) throws IOException {
        //get word map
        HashMap<String, Integer> wordMap = new HashMap<>();

        BufferedReader br;
        br = new BufferedReader(new FileReader(sampleFile));
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            String[] strArray = inputLine.split(" ");
            for (int i=0; i<strArray.length; i++) {
                strArray[i] = strArray[i].toLowerCase();
                if (strArray[i].endsWith(",") || strArray[i].endsWith(".")) {
                    strArray[i] = strArray[i].substring(0, strArray[i].length()-1);
                }
            }
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

        logger.info("word map {}", wordMap.toString());
        return wordMap;
    }
}
