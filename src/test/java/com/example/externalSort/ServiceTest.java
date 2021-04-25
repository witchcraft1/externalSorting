package com.example.externalSort;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.util.List;


public class ServiceTest {
    private static String DEFAULT_PACKAGE ;
    @BeforeAll
    static void initDefaultPackage(){
        DEFAULT_PACKAGE = "src/test/resources/";
    }
    @RepeatedTest(100)
    public void testGetRandomString(){
        String randomTestedString = Service.getRandomString();
        int testedStringLength = randomTestedString.length();
        Assertions.assertNotNull(randomTestedString);
        Assertions.assertTrue(testedStringLength > 0 && testedStringLength < 10);
    }

    @ParameterizedTest
    @ValueSource(strings = {"","f","line1\nline2","null", "                     "})
    public void testBytesIn(String string){
        Assertions.assertEquals(Service.bytesIn(string), string.getBytes().length);
    }

    @Test
    public void testSaveXMbsOfDataIn(){
        int mbs = 30;

        File savedFile = Service.saveXMbsOfDataIn("tested_file", mbs);
        Assertions.assertNotNull(savedFile);

        long fileMbs = savedFile.length()/(1024 * 1024);
        Assertions.assertEquals(mbs, fileMbs);
    }

    @Test
    public void testSaveVectorIn(){
        boolean didSaveVector = Service.saveVectorIn(List.of(1, 2, 3), "test_save_int_vector");

        Assertions.assertTrue(didSaveVector);
    }

    @Test
    public void testGetSubVectorFromFile() throws IOException {
        String filename = "test_get_subvector";

        List<String> list = List.of("5", "4", "3", "22", "0");
        Service.saveVectorIn(list, DEFAULT_PACKAGE, filename);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(DEFAULT_PACKAGE + filename)));

        Assertions.assertEquals(list,
                Service.getSubVectorFromFile(
                        9999,
                        reader
                ));
    }
}
