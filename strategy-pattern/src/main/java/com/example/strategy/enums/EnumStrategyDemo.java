package com.example.strategy.enums;

import java.util.*;

public class EnumStrategyDemo {
    public static void main(String[] args) {
        System.out.println("=== Enum-Based Strategy Pattern Demo ===\n");
        
        System.out.println("1. Direct Enum Strategy Usage:");
        System.out.println("-------------------------------");
        
        List<Integer> data1 = Arrays.asList(64, 34, 25, 12, 22, 11, 90);
        testSort(SortStrategy.BUBBLE_SORT, new ArrayList<>(data1));
        testSort(SortStrategy.QUICK_SORT, new ArrayList<>(data1));
        testSort(SortStrategy.MERGE_SORT, new ArrayList<>(data1));
        
        System.out.println("\n2. Strategy Selection Based on Data Size:");
        System.out.println("------------------------------------------");
        
        List<Integer> smallData = Arrays.asList(5, 2, 8, 1, 9);
        SortStrategy smallStrategy = SortStrategy.selectByDataSize(smallData.size());
        System.out.printf("For %d elements, selected: %s%n", smallData.size(), smallStrategy.getName());
        testSort(smallStrategy, new ArrayList<>(smallData));
        
        List<Integer> largeData = generateRandomList(100);
        SortStrategy largeStrategy = SortStrategy.selectByDataSize(largeData.size());
        System.out.printf("For %d elements, selected: %s%n", largeData.size(), largeStrategy.getName());
        testSort(largeStrategy, new ArrayList<>(largeData));
        
        System.out.println("\n3. Strategy Selection Based on Requirements:");
        System.out.println("---------------------------------------------");
        
        List<Integer> data2 = Arrays.asList(5, 2, 8, 1, 9, 3, 7);
        
        SortStrategy stableSort = SortStrategy.selectByStability(true);
        System.out.println("Need stable sort: " + stableSort.getName());
        testSort(stableSort, new ArrayList<>(data2));
        
        SortStrategy memoryEfficientSort = SortStrategy.selectByMemoryConstraint(true);
        System.out.println("Need memory-efficient sort: " + memoryEfficientSort.getName());
        testSort(memoryEfficientSort, new ArrayList<>(data2));
        
        System.out.println("\n4. Comparing All Strategies:");
        System.out.println("-----------------------------");
        
        List<Integer> testData = generateRandomList(50);
        System.out.println("Original data: " + testData.subList(0, Math.min(10, testData.size())) + "...");
        System.out.println();
        
        for (SortStrategy strategy : SortStrategy.values()) {
            List<Integer> copy = new ArrayList<>(testData);
            long startTime = System.nanoTime();
            strategy.sort(copy);
            long endTime = System.nanoTime();
            
            System.out.printf("%-15s | Time: %d ns | Time Complexity: %-15s | Space: %-10s | Sorted: %s%n",
                            strategy.getName(),
                            (endTime - startTime),
                            strategy.getTimeComplexity(),
                            strategy.getSpaceComplexity(),
                            isSorted(copy));
        }
        
        System.out.println("\n5. String Sorting with Enum Strategies:");
        System.out.println("----------------------------------------");
        
        List<String> strings = Arrays.asList("zebra", "apple", "mango", "banana", "cherry");
        System.out.println("Original: " + strings);
        
        List<String> stringsCopy = new ArrayList<>(strings);
        SortStrategy.QUICK_SORT.sort(stringsCopy);
        System.out.println("Sorted: " + stringsCopy);
        
        System.out.println("\n6. Custom Object Sorting:");
        System.out.println("--------------------------");
        
        List<Person> people = Arrays.asList(
            new Person("Alice", 30),
            new Person("Bob", 25),
            new Person("Charlie", 35),
            new Person("David", 28)
        );
        
        System.out.println("Original people: " + people);
        List<Person> peopleCopy = new ArrayList<>(people);
        SortStrategy.MERGE_SORT.sort(peopleCopy);
        System.out.println("Sorted by age: " + peopleCopy);
        
        System.out.println("\n=== Demo Complete ===");
    }
    
    private static void testSort(SortStrategy strategy, List<Integer> data) {
        System.out.print("Using " + strategy.getName() + ": ");
        List<Integer> original = new ArrayList<>(data);
        strategy.sort(data);
        System.out.println(data + " (Sorted: " + isSorted(data) + ")");
    }
    
    private static boolean isSorted(List<? extends Comparable> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).compareTo(list.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }
    
    private static List<Integer> generateRandomList(int size) {
        Random random = new Random();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(random.nextInt(1000));
        }
        return list;
    }
    
    static class Person implements Comparable<Person> {
        private String name;
        private int age;
        
        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        @Override
        public int compareTo(Person other) {
            return Integer.compare(this.age, other.age);
        }
        
        @Override
        public String toString() {
            return name + "(" + age + ")";
        }
    }
}