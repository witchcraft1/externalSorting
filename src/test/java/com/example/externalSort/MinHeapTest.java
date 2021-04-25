package com.example.externalSort;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MinHeapTest {
    private static MinHeap minHeap;
    private static String DEFAULT_PACKAGE ;

    @BeforeAll
    public static void initMinHeap()  {
        DEFAULT_PACKAGE = "src/test/resources/";
    }

    @Test
    public void testNextLine() throws IOException {
        File emptyFile = new File(DEFAULT_PACKAGE + "empty_file");
        emptyFile.createNewFile();

        MinHeap minHeap = new MinHeap(List.of(
                new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(DEFAULT_PACKAGE + "empty_file")))
                ));

        Assertions.assertNull(minHeap.nextLine());
    }

    @Test
    public void testMergingUsingMinHeap() throws IOException {
        List<String> list1 = new ArrayList<>(Arrays.asList("1", "11", "111", "22", "221", "24","555"));
        List<String> list2 = new ArrayList<>(Arrays.asList("115", "325", "33"));

        Service.saveVectorIn(list1, DEFAULT_PACKAGE, "test_min_heap_list1");
        Service.saveVectorIn(list2, DEFAULT_PACKAGE, "test_min_heap_list2");

        MinHeap minHeap = new MinHeap(List.of(
                new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(DEFAULT_PACKAGE + "test_min_heap_list1"))),
                new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(DEFAULT_PACKAGE + "test_min_heap_list2")))
        ));

        List<String> expectedList = new ArrayList<>();
        String s;
        while ((s = minHeap.nextLine()) != null){
            expectedList.add(s);
        }

        list1.addAll(list2);
        Collections.sort(list1);

        Assertions.assertEquals(list1, expectedList);
    }
}
