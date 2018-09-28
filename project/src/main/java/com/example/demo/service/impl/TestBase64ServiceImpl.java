package com.example.demo.service.impl;

import com.example.demo.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Created by etomhua on 11/24/2017.
 */
@Profile("testBase64")
@Service
public class TestBase64ServiceImpl implements TestService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public String doTest() {
        StringBuffer sb = new StringBuffer("Authorization: ");
        String auth = "dispatcher-api:dispatcher-api";
        String encodedAuth = Base64.getEncoder().encodeToString(
                auth.getBytes(Charset.forName("ISO-8859-1")));
        String authHeader = "Basic " + encodedAuth;
        sb.append(authHeader);

        logger.info(String.valueOf("/3/0/9".split("/").length));

        return sb.toString();
    }
}
