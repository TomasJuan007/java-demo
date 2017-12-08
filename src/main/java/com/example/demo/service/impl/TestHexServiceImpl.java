package com.example.demo.service.impl;

import com.example.demo.service.TestService;
import com.example.demo.util.Hex;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by etomhua on 11/24/2017.
 */
@Profile("testHex")
@Service
public class TestHexServiceImpl implements TestService {
    @Override
    public String doTest() {
        return new String(Hex.decodeHex(
                ("7B22656E64706F696E74223A2022657030303235" +
                        "31222C226F626A6563744964223A202233222C2269" +
                        "6E7374616E63654964223A202230222C227265736F" +
                        "757263654964223A202239222C22636F6E74656E74" +
                        "466F726D6174223A20224F5041515545227D").toCharArray()));
    }
}
