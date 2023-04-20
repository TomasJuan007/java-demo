package com.example.demo;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by etomhua on 12/7/2017.
 */
public class H2Test {
    private static String rootPath;

    @BeforeClass
    public static void beforeClass() {
        URL rootUrl = ClassLoader.getSystemResource("");
        rootPath = rootUrl.getPath();
        int beginIndex = rootPath.indexOf("/");
        int endIndex = rootPath.lastIndexOf("/");
        rootPath = rootPath.substring(beginIndex+1,endIndex);
    }

    @Test
    public void h2Test() throws Exception {
        Class.forName("org.h2.Driver");
        try (Connection conn = DriverManager
                .getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL;INIT=runscript from '"
                        + rootPath + "/create.sql'", "sa", "");
                Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from test");
            if (!resultSet.next())
                Assert.fail();
            resultSet.close();
        }
    }
}