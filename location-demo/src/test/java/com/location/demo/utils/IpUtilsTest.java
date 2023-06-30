package com.location.demo.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class IpUtilsTest {
    @Test
    public void testGetIpAddressInfo() {
        List<String> ipList = new ArrayList<>();
        ipList.add("1.119.142.106");
        ipList.add("1.119.145.146");
        ipList.add("1.119.157.42");
        ipList.add("1.119.166.58");
        ipList.add("1.119.197.50");
        ipList.add("1.145.239.91");
        ipList.add("1.147.78.105");
        ipList.add("1.152.21.163");
        ipList.add("1.152.31.82");
        ipList.add("1.160.149.73");
        ipList.add("1.162.132.183");
        ipList.add("1.162.164.124");
        ipList.add("1.163.214.65"); //TW
        for (String ip : ipList) {
            String i = IpUtils.getIpAddressInfo(ip);
            System.out.println(i);
        }
    }
}
