package com.light.springboot.utils;

import java.io.*;
import java.util.*;

public class inquireWords {
    public static List getWords(Map word_list){
        Scanner scanner;
        List<String> words = new ArrayList<>();

        System.out.print("\nInput the first word: ");
        scanner = new Scanner(System.in);
        String word1 = scanner.nextLine();

        while (!word_list.containsKey(word1)){
            System.out.print("The word is not in the dictionary! \nTry another one: ");
            scanner = new Scanner(System.in);
            word1 = scanner.nextLine();
        }

        int len1 = word1.length();

        System.out.print("Input the second word: ");
        scanner = new Scanner(System.in);
        String word2 = scanner.nextLine();

        while ( len1 != word2.length()){
            System.out.print("Two words must be in same length! \nInput again please: ");
            scanner = new Scanner(System.in);
            word2 = scanner.nextLine();
            while (!word_list.containsKey(word2)){
                System.out.print("The second word is not in the dictionary! \nTry another one: ");
                scanner = new Scanner(System.in);
                word2 = scanner.nextLine();
            }
        }

        while (!word_list.containsKey(word2)){
            System.out.print("The second word is not in the dictionary! \nTry another one: ");
            scanner = new Scanner(System.in);
            word2 = scanner.nextLine();
        }

        words.add(word1);
        words.add(word2);

        return words;
    }

}
