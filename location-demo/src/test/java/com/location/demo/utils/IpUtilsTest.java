package com.location.demo.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class IpUtilsTest {
    @Test
    public void testGetIpAddressInfo() {
        List<String> ipList = new ArrayList<>();
        ipList.add("1.163.214.65"); //TW
        ipList.add("8.134.42.147");
        ipList.add("8.134.42.149");
        ipList.add("103.170.73.12");
        ipList.add("139.198.120.23");
        for (String ip : ipList) {
            String i = IpUtils.getIpAddressInfo(ip);
            System.out.println(i);
        }
    }
}
