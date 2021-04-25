package com.example.externalSort;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ExternalSort {
    private Integer getRandomFloatingNumber(){
        return (int)(Math.random()*1000);
    }
    private String getRandomString(){
        return "s".repeat((int)(Math.random()*9 + 1));
    }

    public boolean saveXMbsOfDataIn(String filename, long mbs){
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
    public <T> boolean saveVectorIn(List<T> vector, String fileName) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/" + fileName,true))) {
            for (T i:vector) {
                bw.write(i.toString() + '\n');//TODO add + '\n'
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<String> getSubVectorFromFile(String filename, long maxAvailableSize, BufferedReader sc) throws IOException {
        if(maxAvailableSize <= 0) throw new IllegalArgumentException();

        String s;
        if((s = sc.readLine()) == null){
            return null;
        }
        ArrayList<String> vector = new ArrayList<>(/*530_000*//*140_000*/);//TODO
        long memoryUsedBytes = 0;
        while(s != null && (memoryUsedBytes+=byteSizeOf(s)) < maxAvailableSize){
            vector.add(s);
            s = sc.readLine();
        }
        System.out.println(vector.size());
        vector.trimToSize();
        return vector;
    }

    public boolean externalSort(String inputFile) throws IOException {

        File file = new File("src/main/resources/" + inputFile);
        long size = file.length();//in bytes
        long maxAvailableSize = Runtime.getRuntime().maxMemory();//in bytes

        int chunksAmount = (int) Math.ceil(1.*size/maxAvailableSize);

        List<BufferedReader> scanners = new ArrayList<>();
        try {
            scanners.add(new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8), 10));
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        }

        for (int i = 0; i < chunksAmount; i++) {
            List<String> chunk = getSubVectorFromFile(inputFile, maxAvailableSize, scanners.get(0));//TODO save separately in variable

//            Collections.sort(chunk);
            HeapSort.sort(chunk);

            saveVectorIn(chunk, "sorted" + i);

            scanners.add(new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/sorted" + i))));
        }
        scanners.remove(0);

        int dataSlice = (int)Math.floor(1.*maxAvailableSize/(chunksAmount + 1));

        List<List<String>> lists = new ArrayList<>(chunksAmount);
        List<String> output = new ArrayList<>();//TODO: initialize constructor with defined size using maxAvailableSize
        for (int i = 0; i < chunksAmount; i++) {
            lists.add(getSubVectorFromFile("sorted" + i, dataSlice, scanners.get(i) ));
        }

        while(!lists.isEmpty()) {
            int index = 0;
            String min = lists.get(index).get(0);
            for (int j = 1; j < scanners.size(); j++) {
//                if (min > lists.get(j).get(0)) {
                if(min.compareTo(lists.get(j).get(0)) > 0){
                    min = lists.get(j).get(0);
                    index = j;
                }
            }
            output.add(lists.get(index).remove(0));
            if(output.size() == dataSlice){
                saveVectorIn(output, "finallySorted");
                output.clear();
            }
            if(lists.get(index).isEmpty()){
                List<String> subVectorFromFile = getSubVectorFromFile("sorted" + index, dataSlice, scanners.get(index));
                if(subVectorFromFile == null){
                    lists.remove(index);
                    scanners.remove(index);
                }else{
                    lists.set(index, subVectorFromFile);
                }
            }
        }
        return saveVectorIn(output,"finallySorted");
    }

    public static long countLinesOfFile(String filename) throws IOException {
        FileReader input = new FileReader("src/main/resources/" + filename);
        LineNumberReader lineNumberReader = new LineNumberReader(input);
        while (lineNumberReader.skip(Long.MAX_VALUE) > 0);
        return lineNumberReader.getLineNumber() + 1;
    }

    public int byteSizeOf(String s){
        return s.length();
    }

    public static void main(String[] args) throws IOException {
        long mbs = 30;

        ExternalSort externalSort = new ExternalSort();
        externalSort.saveXMbsOfDataIn("init_file", mbs);
        externalSort.externalSort("init_file");
    }
}
