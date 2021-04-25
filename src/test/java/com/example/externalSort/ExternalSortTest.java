package com.example.externalSort;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExternalSortTest {
    private static String DEFAULT_PACKAGE ;
    @BeforeAll
    static void initDefaultPackage(){
        DEFAULT_PACKAGE = "src/test/resources/";
    }

    @Test
    public void testExternalSort() throws IOException {
        File file = new File(DEFAULT_PACKAGE + "test_ext_sort");
        File sortedFile = new File(DEFAULT_PACKAGE + "finallySorted");

        List<String> list = new ArrayList<>(Arrays.asList("443", "412", "231", "674", "21", "11", "1"));
        Service.saveVectorIn(list, DEFAULT_PACKAGE,file.getName());

        Assertions.assertTrue(ExternalSort.externalSortIn(file, DEFAULT_PACKAGE));

        List<String> fromFile = Service.getSubVectorFromFile(
                99999,
                new BufferedReader(new InputStreamReader(new FileInputStream(sortedFile)))
        );

        Collections.sort(list);
        Assertions.assertEquals(list, fromFile);
    }

    @Test
    public void testSortedFileSize(){
        File file = new File(DEFAULT_PACKAGE + "test_ext_sort");
        File sortedFile = new File(DEFAULT_PACKAGE + "finallySorted");
        Assertions.assertEquals(file.length(), sortedFile.length());
    }
}
