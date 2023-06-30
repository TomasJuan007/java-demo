package com.location.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Apnic2StdConverter {
    private static final Logger LOG = LoggerFactory.getLogger(Apnic2StdConverter.class);

    public static void main(String[] args) throws Exception {
        String apnicPath = Apnic2StdConverter.class.getResource("/ip2region/apnic/ip.apnic-source.txt").getPath();
        File file = new File(apnicPath);
        Scanner scanner = new Scanner(file);

        String tmpDir = Apnic2StdConverter.class.getResource("/ip2region/apnic").getPath();
        File outputFile = new File(tmpDir + File.separator + "ip.merge_apnic.txt");
        boolean success = outputFile.createNewFile();
        LOG.info(" create file success:{}", success);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            long i = -1L;
            while (scanner.hasNextLine()) {
                String a = scanner.nextLine();

                String[] split = a.split("\\|");
                if (split.length < 7) {
                    continue;
                }
                // Record: registry|cc|type|start|value|date|status
                String registry = split[0];
                String cc = split[1];
                String type = split[2];
                String start = split[3];
                String value = split[4];
                String date = split[5];
                String status = split[6];
                if (!"apnic".equals(registry) || !"ipv4".equals(type)) {
                    continue;
                }
                long length = Long.parseLong(value);
                long ipStart = ip2long(start);
                if (ipStart > i + 1) {
                    String paddingStart = long2ip(i + 1);
                    String paddingEnd = long2ip(ipStart - 1);
                    String line = paddingStart + "|" + paddingEnd + "|NA\n";
                    System.out.print(line);
                    fos.write(line.getBytes(StandardCharsets.UTF_8));
                }
                long ipEnd = ipStart + length - 1;
                String end = long2ip(ipEnd);
                i = ipEnd;

                String line = start + "|" + end + "|" + cc + "\n";
                System.out.print(line);
                fos.write(line.getBytes(StandardCharsets.UTF_8));
            }
            long last = ip2long("255.255.255.255");
            if (i < last) {
                String paddingStart = long2ip(i + 1);
                String line = paddingStart + "|255.255.255.255|NA\n";
                System.out.print(line);
                fos.write(line.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    public static long ip2long(String ip) {
        String[] p = ip.split("\\.");
        if (p.length != 4) {
            return 0L;
        } else {
            int p1 = Integer.valueOf(p[0]) << 24 & -16777216;
            int p2 = Integer.valueOf(p[1]) << 16 & 16711680;
            int p3 = Integer.valueOf(p[2]) << 8 & '\uff00';
            int p4 = Integer.valueOf(p[3]) << 0 & 255;
            return (long)(p1 | p2 | p3 | p4) & 4294967295L;
        }
    }

    public static String long2ip(long ip) {
        StringBuilder sb = new StringBuilder();
        sb.append(ip >> 24 & 255L).append('.').append(ip >> 16 & 255L).append('.').append(ip >> 8 & 255L).append('.').append(ip >> 0 & 255L);
        return sb.toString();
    }
}
