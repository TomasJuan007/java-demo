package com.example.demo.cli;

import org.junit.Test;

public class EncryptionCmdProcessorTest {
    @Test
    public void process() {
        EncryptionCmdProcessor processor = new EncryptionCmdProcessor();
        processor.process(null);
        String[] args = new String[2];
        processor = new EncryptionCmdProcessor();

        args[0] = "-p";
        args[1] = "123456";
        processor.process(args);

        args[0] = "-c";
        args[1] = "encrypted:64c5fd2979a86168";
        processor.process(args);
    }
}
