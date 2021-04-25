package com.example.externalSort;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Service {
    public static String getRandomString(){
        return "s".repeat((int)(Math.random()*9 + 1));
    }

    public static boolean saveXMbsOfDataIn(String filename, long mbs){
        long size = mbs*1024*1024;
        long memoryUsedBytes = 0;
        String s;
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/" + filename,true))) {
            for (;(memoryUsedBytes+=byteSizeOf(s = getRandomString())) < size;) {
                bw.write(s + "\n");
            }
            System.out.println(memoryUsedBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static  <T> boolean saveVectorIn(List<T> vector, String fileName) {
        if(vector == null) throw new NullPointerException();

        try(BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/" + fileName,true))) {
            for (T i:vector) {
                bw.write(i.toString() + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static List<String> getSubVectorFromFile(long maxAvailableSize, BufferedReader sc) throws IOException {
        if(maxAvailableSize <= 0) throw new IllegalArgumentException();

        String s;
        if((s = sc.readLine()) == null){
            return null;
        }
        ArrayList<String> vector = new ArrayList<>();
        long memoryUsedBytes = 0;
        while(s != null && (memoryUsedBytes+=byteSizeOf(s)) < maxAvailableSize){
            vector.add(s);
            s = sc.readLine();
        }
        System.out.println(vector.size());
        vector.trimToSize();
        return vector;
    }

    public  static long countLinesOfFile(String filename) throws IOException {
        FileReader input = new FileReader("src/main/resources/" + filename);
        LineNumberReader lineNumberReader = new LineNumberReader(input);
        while (lineNumberReader.skip(Long.MAX_VALUE) > 0);
        return lineNumberReader.getLineNumber() + 1;
    }

    public static int byteSizeOf(String s){
        return s.length();
    }
}
