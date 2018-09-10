package com.example.demo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.*;
import java.util.*;

public class TfIdfServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(TfIdfServiceImpl.class);

    private static final int numLine = 5;
    private static final int[] a = {1, 2, 3, 4, 5};
    private static final int totalClassNum = 5;

    private static ResourceLoader resourceLoader = new DefaultResourceLoader();
    private static final String sampleFilePath = "data/sample.txt";
    private static final String originalDataFilePath = "data/originalData.txt";

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
        HashMap<String, Double> idfMap = new HashMap<>();
        for (String key : wordMap.keySet()) {
            int otherClassNum = 1;
            for (int m = 0; m < totalClassNum - 1; m++) {
                int beginning = a[m];
                int ending = a[m + 1];

                File originalDataFile = resourceLoader.getResource(originalDataFilePath).getFile();
                InputStreamReader dataReader = new InputStreamReader(
                        new FileInputStream(originalDataFile), "utf-8");
                BufferedReader sr = new BufferedReader(dataReader);
                String txt = sr.readLine();
                int next = 1;
                while (txt != null) {
                    if (next > beginning - 1 && next < ending) {
                        String[] str2Array = txt.split(" ");
                        int l;
                        for (l = 0; l < str2Array.length; l++) {
                            if (str2Array[l].equals(key)) {
                                otherClassNum++;
                                break;
                            }
                        }
                        if (l < str2Array.length) {
                            break;
                        }
                    }
                    txt = sr.readLine();
                    next++;
                }
            }
            idfMap.put(key, Math.log10(1.0 * (totalClassNum - 1) / otherClassNum));
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
