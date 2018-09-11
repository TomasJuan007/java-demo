package com.example.demo.service.impl;

import com.example.demo.service.TextService;

import java.io.IOException;

public class TfIdfServiceImpl {

    private static final String sampleFilePath = "data/sample.txt";
    private static final String originalDataFilePath = "data/original";

    public static void main(String[] args) throws IOException {
        TextService textService = new TextServiceImpl();
        textService.getFirstFiveKeyWords(sampleFilePath, originalDataFilePath);
    }
}
