package com.example.demo.service.impl;

import com.example.demo.service.TextService;
import org.junit.Test;

import java.io.IOException;

public class TextServiceImplTest {

    private static final String sampleFilePath = "data/text/sample.txt";
    private static final String originalDataFilePath = "data/text/original";

    @Test
    public void getFirstFiveKeyWords() throws IOException {
        TextService textService = new TextServiceImpl();
        textService.getFirstFiveKeyWords(sampleFilePath, originalDataFilePath);
    }
}
