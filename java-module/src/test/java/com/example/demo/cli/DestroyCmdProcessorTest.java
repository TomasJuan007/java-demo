package com.example.demo.cli;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class DestroyCmdProcessorTest {
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
    public void destroy() {
        DestroyCmdProcessor processor = new DestroyCmdProcessor();
        processor.process(null);
        Assert.assertTrue(getStdout().contains("usage:"));
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
