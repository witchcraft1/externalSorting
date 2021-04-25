package com.example.externalSort;

import java.util.List;

public class HeapSort {
    public static void sort(List<String> arr) {
        if(arr == null) return;

        int n = arr.size();

        // Build heap (rearrange array)
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(arr, n, i);

        // One by one extract an element from heap
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            String temp = arr.get(0);
            arr.set(0, arr.get(i));
            arr.set(i, temp);

            // call max heapify on the reduced heap
            heapify(arr, i, 0);
        }
    }

    // To heapify a subtree rooted with node i which is
    // an index in arr[]. n is size of heap
    static void heapify(List<String> arr, int n, int i) {
        int largest = i; // Initialize largest as root
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        // If left child is larger than root
        if (l < n && arr.get(l).compareTo(arr.get(largest)) > 0)
            largest = l;

        // If right child is larger than largest so far
        if (r < n && arr.get(r).compareTo(arr.get(largest)) > 0)
            largest = r;

        // If largest is not root
        if (largest != i) {
            String swap = arr.get(i);
            arr.set(i, arr.get(largest));
            arr.set(largest, swap);

            // Recursively heapify the affected sub-tree
            heapify(arr, n, largest);
        }
    }
}

