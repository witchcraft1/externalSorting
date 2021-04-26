package com.example.externalSort;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.externalSort.Service.getSubVectorFromFile;
import static com.example.externalSort.Service.saveVectorIn;

public class ExternalSort {
    private static final String DEFAULT_PACKAGE = "src/main/resources/";

    public static boolean externalSort(File file) throws IOException {
        return externalSortIn(file,DEFAULT_PACKAGE);
    }

    public static boolean externalSortIn(File file, String packageName) throws IOException{
        //        File file = new File(DEFAULT_PACKAGE + inputFile);
        if(file == null) throw new NullPointerException();

        long size = file.length();//in bytes
        long maxAvailableSize = Runtime.getRuntime().freeMemory()/5;//in bytes

        int chunksAmount = (int) Math.ceil(1.*size/maxAvailableSize);

        List<BufferedReader> readers = splitAndSort(file, packageName, chunksAmount,maxAvailableSize,10);

        MinHeap heap = new MinHeap(readers);

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(packageName + "finallySorted",true))) {
            String s;
            while ((s = heap.nextLine()) != null)
                bw.write(s + '\n');
        }
        return true;
    }

    private static List<BufferedReader> splitAndSort(File file, String dirName, int chunksAmount, long maxAvailableSize, int bufferSize) throws IOException {
        ArrayList<BufferedReader> readers = new ArrayList<>(chunksAmount);

        BufferedReader mainReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)), bufferSize);
        for (int i = 0; i < chunksAmount; i++) {
            List<String> chunk = getSubVectorFromFile(maxAvailableSize, mainReader);

//            Collections.sort(chunk);
            HeapSort.sort(chunk);

            saveVectorIn(chunk, dirName,"sorted" + i);

            readers.add(new BufferedReader(new InputStreamReader(new FileInputStream(dirName + "sorted" + i))));
        }
        return readers;
    }


    public static void main(String[] args) throws IOException {
        long mbs = 30;

        File savedFile = Service.saveXMbsOfDataIn("init_file", mbs);
        externalSort(savedFile);
    }
}
