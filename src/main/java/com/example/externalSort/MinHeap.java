package com.example.externalSort;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class MinHeap {
    private static Comparator<String> DEFAULT_COMPARATOR_STR = String::compareTo;

    private List<BufferedReader> readers;
    private PriorityQueue<LineWrapper> minHeap;
    public MinHeap(List<BufferedReader> readers){
        this(readers,DEFAULT_COMPARATOR_STR);
    }

    public MinHeap(List<BufferedReader> readers, Comparator<String> comparator){
        this.readers = readers;
        Comparator<LineWrapper> lineComparator = (lineWr1, lineWr2) -> comparator.compare(lineWr1.line, lineWr2.line);
        minHeap = new PriorityQueue<>(readers.size(), lineComparator);
    }

    public String nextLine() throws IOException {
        if(minHeap.isEmpty()){
            for (int i = 0; i < readers.size(); i++) {
                addToHeap(readers.get(i).readLine(), i);
            }
        }

        if(minHeap.isEmpty()) return null;

        LineWrapper lineWrapper = minHeap.remove();
        int index = lineWrapper.index;
        addToHeap(readers.get(index).readLine(), index);

        return lineWrapper.line;
    }

    private void addToHeap(String line, int fileIndex) {
        if (line != null) minHeap.add(new LineWrapper(line, fileIndex));
    }

    private static class LineWrapper{
        private String line;
        private int index;

        public LineWrapper(String line, int index) {
            this.line = line;
            this.index = index;
        }
    }
}
