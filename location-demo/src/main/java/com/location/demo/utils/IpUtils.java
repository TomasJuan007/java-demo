package com.location.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

import java.io.IOException;

@Slf4j
public class IpUtils {

    private static final String MAINLAND = "境内";
    private static final String NON_MAINLAND = "境外";

    public static String getIpAddressInfo(String ip) {
        // 1、创建 searcher 对象
        String dbPath = IpUtils.class.getResource("/ip2region/apnic/ip2region_apnic.xdb").getPath();
        Searcher searcher;
        try {
            searcher = Searcher.newWithFileOnly(dbPath);
        } catch (IOException e) {
            System.out.printf("failed to create searcher with `%s`: %s\n", dbPath, e);
            return MAINLAND;
        }
        // 2、查询
        try {
            String region = searcher.search(ip);
            if (region == null) {
                return MAINLAND;
            }
            if ("CN".equals(region)) {
                log.info("解析到的地址： {}", MAINLAND);
                return MAINLAND;
            }
            log.info("解析到的地址： {}", NON_MAINLAND);
            return NON_MAINLAND;
        } catch (Exception e) {
            System.out.printf("failed to search(%s): %s\n", ip, e);
        }

        // 3、关闭资源
        try {
            searcher.close();
        } catch (IOException e) {
            System.out.printf("failed to close searcher with `%s`: %s\n", dbPath, e);
        }

        // 备注：并发使用，每个线程需要创建一个独立的 searcher 对象单独使用。
        return MAINLAND;
    }
}
