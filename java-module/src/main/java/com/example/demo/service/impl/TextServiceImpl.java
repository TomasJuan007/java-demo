package com.example.demo.service.impl;

import com.example.demo.service.TextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class TextServiceImpl implements TextService {
    private static final Logger logger = LoggerFactory.getLogger(TextServiceImpl.class);

    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Override
    public HashMap<String, Double> getFirstFiveKeyWords(String sampleFilePath, String refDataPath) throws IOException {
        HashMap<String, Integer> wordMap = getWordMap(sampleFilePath);
        Set<String> keySet = wordMap.keySet();
        int totalCount = 0;
        for (String key : keySet) {
            totalCount += wordMap.get(key);
        }

        //calculate tf
        HashMap<String, Double> tfMap = getTfMap(wordMap, keySet, totalCount);

        //calculate idf
        HashMap<String, Double> idfMap = getIdfMap(refDataPath, keySet);

        //calculate tf-idf and sort
        HashMap<String, Double> tfIdfMap = calculateTfIdfMap(tfMap, idfMap);
        logger.info("TF*IDF map {}", tfIdfMap.toString());
        return tfIdfMap;
    }

    private HashMap<String, Integer> getWordMap(String filePath) throws IOException {
        HashMap<String, Integer> wordMap = new HashMap<>();

        File file = resourceLoader.getResource(filePath).getFile();
        BufferedReader br;
        br = new BufferedReader(new FileReader(file));
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            String[] strArray = inputLine.split(" ");
            for (int i=0; i<strArray.length; i++) {
                strArray[i] = strArray[i].toLowerCase();
                if (strArray[i].endsWith(",") || strArray[i].endsWith(".")) {
                    strArray[i] = strArray[i].substring(0, strArray[i].length()-1);
                }
            }
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

    private HashMap<String, Double> getTfMap(HashMap<String, Integer> wordMap, Set<String> keySet, int totalCount) {
        HashMap<String, Double> tfMap = new HashMap<>();
        for (String key : keySet) {
            int v = wordMap.get(key);
            tfMap.put(key, 1.0 * v / totalCount);
        }

        logger.info("TF map {}", tfMap.toString());
        return tfMap;
    }

    private HashMap<String, Double> getIdfMap(String filePath, Set<String> keySet) throws IOException {
        int totalClassNum = 1;
        HashMap<String, Integer> wordCountMap = new HashMap<>();

        for (String key : keySet) {
            wordCountMap.put(key, 1);
        }

        HashMap<String, Double> idfMap = new HashMap<>();
        File originalDataDirectory = resourceLoader.getResource(filePath).getFile();
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

    private HashMap<String, Double> calculateTfIdfMap(HashMap<String, Double> tfMap, HashMap<String, Double> idfMap) {
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
            logger.info(fea.toString());
        }
        return tfIdfMap;
    }
}
