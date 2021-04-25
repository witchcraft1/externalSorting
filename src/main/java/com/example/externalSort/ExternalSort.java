package com.example.externalSort;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.externalSort.Service.getSubVectorFromFile;
import static com.example.externalSort.Service.saveVectorIn;

public class ExternalSort {
    private static String DEFAULT_PACKAGE = "src/main/resources/";

    public static boolean externalSort(String inputFile) throws IOException {
        File file = new File(DEFAULT_PACKAGE + inputFile);
        long size = file.length();//in bytes
        long maxAvailableSize = Runtime.getRuntime().maxMemory();//in bytes

        int chunksAmount = (int) Math.ceil(1.*size/maxAvailableSize);

        List<BufferedReader> readers = splitAndSort(file,chunksAmount,maxAvailableSize,10);

        MinHeap heap = new MinHeap(readers);

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(DEFAULT_PACKAGE + "finallySorted",true))) {
            String s;
            while ((s = heap.nextLine()) != null)
                bw.write(s + '\n');
        }
        return true;
    }

    private static List<BufferedReader> splitAndSort(File file, int chunksAmount, long maxAvailableSize, int bufferSize) throws IOException {
        ArrayList<BufferedReader> readers = new ArrayList<>(chunksAmount);

        BufferedReader mainReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)), bufferSize);
        for (int i = 0; i < chunksAmount; i++) {
            List<String> chunk = getSubVectorFromFile(maxAvailableSize, mainReader);

//            Collections.sort(chunk);
            HeapSort.sort(chunk);

            saveVectorIn(chunk, "sorted" + i);

            readers.add(new BufferedReader(new InputStreamReader(new FileInputStream(DEFAULT_PACKAGE + "sorted" + i))));
        }
        return readers;
    }


    public static void main(String[] args) throws IOException {
        long mbs = 30;

        ExternalSort externalSort = new ExternalSort();
        Service.saveXMbsOfDataIn("init_file", mbs);
        externalSort("init_file");
    }
}
