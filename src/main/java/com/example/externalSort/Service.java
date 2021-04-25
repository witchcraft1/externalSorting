package com.example.externalSort;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Service {
    private static final String DEFAULT_PACKAGE = "src/main/resources/";

    public static String getRandomString(){
        return "s".repeat((int)(Math.random()*9 + 1));
    }

    public static File saveXMbsOfDataIn(String filename, long mbs){
        File file = new File(DEFAULT_PACKAGE + filename);

        long size = mbs*1024*1024;
        long memoryUsedBytes = 0;
        String s;
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file,true))) {
            while (memoryUsedBytes < size) {
                memoryUsedBytes+= bytesIn(s = getRandomString() + "\n");
                bw.write(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    public static  <T> boolean saveVectorIn(List<T> vector, String packageName, String fileName) {
        if(vector == null) throw new NullPointerException();

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(packageName + fileName,true))) {
            for (T i:vector) {
                bw.write(i.toString() + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static  <T> boolean saveVectorIn(List<T> vector, String fileName) {
        return saveVectorIn(vector, DEFAULT_PACKAGE, fileName);
    }

    public static List<String> getSubVectorFromFile(long maxAvailableSize, BufferedReader sc) throws IOException {
        if(maxAvailableSize <= 0) throw new IllegalArgumentException();

        String s;
        if((s = sc.readLine()) == null){
            return null;
        }
        ArrayList<String> vector = new ArrayList<>();
        long memoryUsedBytes = 0;
        while(s != null && (memoryUsedBytes+= bytesIn(s)) < maxAvailableSize){
            vector.add(s);
            s = sc.readLine();
        }
        System.out.println(vector.size());
        vector.trimToSize();
        return vector;
    }

    public  static long countLinesOfFile(String filename) throws IOException {
        FileReader input = new FileReader(DEFAULT_PACKAGE + filename);
        LineNumberReader lineNumberReader = new LineNumberReader(input);
        while (lineNumberReader.skip(Long.MAX_VALUE) > 0);
        return lineNumberReader.getLineNumber() + 1;
    }

    public static int bytesIn(String s){
        return s.length();
    }
}
