package com.example.demo.cli;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class EncryptionCmdProcessorTest {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private PipedOutputStream pipeOut;
    private PipedInputStream pipeIn;

    @Before
    public void before() {
        pipeOut = new PipedOutputStream();
        try {
            pipeIn = new PipedInputStream(pipeOut);
        } catch (IOException ignored) {

        }
        System.setOut(new PrintStream(pipeOut));
    }

    @After
    public void after() {
        if (pipeIn != null) {
            try {
                pipeIn.close();
            } catch (IOException ignored) {

            }
        }
        if (pipeOut != null) {
            try {
                pipeOut.close();
            } catch (IOException ignored) {

            }
        }
    }

    @Test
    public void processNull() {
        EncryptionCmdProcessor processor = new EncryptionCmdProcessor();
        processor.process(new String[]{"-h"});
        Assert.assertNotNull(getStdout());
    }

    @Test
    public void encode() {
        EncryptionCmdProcessor processor = new EncryptionCmdProcessor();
        String[] args = new String[]{"-p", "123456"};
        processor.process(args);
        Assert.assertEquals("Cipher Text: [encrypted:64c5fd2979a86168]" + LINE_SEPARATOR, getStdout());
    }

    @Test
    public void decode() {
        EncryptionCmdProcessor processor = new EncryptionCmdProcessor();
        String[] args = new String[]{"-c", "encrypted:64c5fd2979a86168"};
        processor.process(args);
        Assert.assertEquals("Plain Text: [123456]" + LINE_SEPARATOR, getStdout());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private String getStdout() {
        try {
            int size = pipeIn.available();
            byte[] outBytes = new byte[size];
            pipeIn.read(outBytes);
            return new String(outBytes);
        } catch (IOException ignored) {

        }
        return "";
    }
}
