package com.example.demo.service;

import java.io.IOException;
import java.util.HashMap;

public interface TextService {
    HashMap<String, Double> getFirstFiveKeyWords(String sampleFilePath, String refDataPath) throws IOException;
}
