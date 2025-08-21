package edu.dsa.clinic.adt;

public class DoubleLinkedListTest {
    public static void main(String[] args) {
        DoubleLinkedList<String> list = new DoubleLinkedList<>();

        list.add("banana");
        list.add("apple");
        list.add("cherry");
        list.add("apple");
        list.add("date");

        System.out.println("Before sort:");
        for (String s : list) {
            System.out.println(s);
        }

        list.sort(String::compareTo); // ascending order

        System.out.println("\nAfter sort (ascending):");
        for (String s : list) {
            System.out.println(s);
        }

        list.sort((a, b) -> b.compareTo(a)); // descending order

        System.out.println("\nAfter sort (descending):");
        for (String s : list) {
            System.out.println(s);
        }
    }
}
