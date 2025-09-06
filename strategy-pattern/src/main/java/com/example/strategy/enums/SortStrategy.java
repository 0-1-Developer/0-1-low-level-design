package com.example.strategy.enums;

import java.util.*;

public enum SortStrategy {
    BUBBLE_SORT("Bubble Sort", "O(n²)", "O(1)") {
        @Override
        public <T extends Comparable<T>> void sort(List<T> list) {
            int n = list.size();
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - i - 1; j++) {
                    if (list.get(j).compareTo(list.get(j + 1)) > 0) {
                        Collections.swap(list, j, j + 1);
                    }
                }
            }
        }
    },
    
    QUICK_SORT("Quick Sort", "O(n log n) avg", "O(log n)") {
        @Override
        public <T extends Comparable<T>> void sort(List<T> list) {
            quickSort(list, 0, list.size() - 1);
        }
        
        private <T extends Comparable<T>> void quickSort(List<T> list, int low, int high) {
            if (low < high) {
                int pi = partition(list, low, high);
                quickSort(list, low, pi - 1);
                quickSort(list, pi + 1, high);
            }
        }
        
        private <T extends Comparable<T>> int partition(List<T> list, int low, int high) {
            T pivot = list.get(high);
            int i = (low - 1);
            
            for (int j = low; j < high; j++) {
                if (list.get(j).compareTo(pivot) <= 0) {
                    i++;
                    Collections.swap(list, i, j);
                }
            }
            Collections.swap(list, i + 1, high);
            return i + 1;
        }
    },
    
    MERGE_SORT("Merge Sort", "O(n log n)", "O(n)") {
        @Override
        public <T extends Comparable<T>> void sort(List<T> list) {
            if (list.size() <= 1) return;
            mergeSort(list, 0, list.size() - 1);
        }
        
        private <T extends Comparable<T>> void mergeSort(List<T> list, int left, int right) {
            if (left < right) {
                int mid = left + (right - left) / 2;
                mergeSort(list, left, mid);
                mergeSort(list, mid + 1, right);
                merge(list, left, mid, right);
            }
        }
        
        private <T extends Comparable<T>> void merge(List<T> list, int left, int mid, int right) {
            List<T> leftList = new ArrayList<>(list.subList(left, mid + 1));
            List<T> rightList = new ArrayList<>(list.subList(mid + 1, right + 1));
            
            int i = 0, j = 0, k = left;
            
            while (i < leftList.size() && j < rightList.size()) {
                if (leftList.get(i).compareTo(rightList.get(j)) <= 0) {
                    list.set(k++, leftList.get(i++));
                } else {
                    list.set(k++, rightList.get(j++));
                }
            }
            
            while (i < leftList.size()) {
                list.set(k++, leftList.get(i++));
            }
            
            while (j < rightList.size()) {
                list.set(k++, rightList.get(j++));
            }
        }
    },
    
    INSERTION_SORT("Insertion Sort", "O(n²)", "O(1)") {
        @Override
        public <T extends Comparable<T>> void sort(List<T> list) {
            for (int i = 1; i < list.size(); i++) {
                T key = list.get(i);
                int j = i - 1;
                
                while (j >= 0 && list.get(j).compareTo(key) > 0) {
                    list.set(j + 1, list.get(j));
                    j--;
                }
                list.set(j + 1, key);
            }
        }
    },
    
    HEAP_SORT("Heap Sort", "O(n log n)", "O(1)") {
        @Override
        public <T extends Comparable<T>> void sort(List<T> list) {
            int n = list.size();
            
            for (int i = n / 2 - 1; i >= 0; i--) {
                heapify(list, n, i);
            }
            
            for (int i = n - 1; i > 0; i--) {
                Collections.swap(list, 0, i);
                heapify(list, i, 0);
            }
        }
        
        private <T extends Comparable<T>> void heapify(List<T> list, int n, int i) {
            int largest = i;
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            
            if (left < n && list.get(left).compareTo(list.get(largest)) > 0) {
                largest = left;
            }
            
            if (right < n && list.get(right).compareTo(list.get(largest)) > 0) {
                largest = right;
            }
            
            if (largest != i) {
                Collections.swap(list, i, largest);
                heapify(list, n, largest);
            }
        }
    },
    
    TIM_SORT("Tim Sort", "O(n log n)", "O(n)") {
        @Override
        public <T extends Comparable<T>> void sort(List<T> list) {
            Collections.sort(list);
        }
    };
    
    private final String name;
    private final String timeComplexity;
    private final String spaceComplexity;
    
    SortStrategy(String name, String timeComplexity, String spaceComplexity) {
        this.name = name;
        this.timeComplexity = timeComplexity;
        this.spaceComplexity = spaceComplexity;
    }
    
    public abstract <T extends Comparable<T>> void sort(List<T> list);
    
    public String getName() {
        return name;
    }
    
    public String getTimeComplexity() {
        return timeComplexity;
    }
    
    public String getSpaceComplexity() {
        return spaceComplexity;
    }
    
    public static SortStrategy selectByDataSize(int size) {
        if (size < 10) {
            return INSERTION_SORT;
        } else if (size < 50) {
            return QUICK_SORT;
        } else if (size < 1000) {
            return MERGE_SORT;
        } else {
            return TIM_SORT;
        }
    }
    
    public static SortStrategy selectByStability(boolean needsStable) {
        return needsStable ? MERGE_SORT : QUICK_SORT;
    }
    
    public static SortStrategy selectByMemoryConstraint(boolean lowMemory) {
        return lowMemory ? HEAP_SORT : MERGE_SORT;
    }
}