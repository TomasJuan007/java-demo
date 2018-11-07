package com.example.demo.cli;

import org.junit.Test;

public class DestroyCmdProcessorTest {
    @Test
    public void destroy() {
        DestroyCmdProcessor processor = new DestroyCmdProcessor();
        processor.process(null);
    }
}
