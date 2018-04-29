package com.light.springboot.utils;

import java.io.*;
import java.util.*;

public class buildLadder {

    public static void find_ladder(String word1, String word2,Map word_list){

        List<String> used_words = new ArrayList<>();
        Queue<LinkedList<String>> ladders = new LinkedList<LinkedList<String>>();
        LinkedList<String> word_ladder = new LinkedList<String>();
        String this_word = "";
        StringBuilder sb;
        char letter;
        String new_letter;
        String changed_word;
        int word_len = word1.length();

        word_ladder.add(word1);
        used_words.add(word1);
        ladders.add(word_ladder);

        while (!ladders.isEmpty()){
            word_ladder = ladders.poll();
            this_word = word_ladder.getLast();
            for (int i=0; i<word_len; i++){

                letter = this_word.charAt(i);
                sb = new StringBuilder(this_word);
                for (int j =0; j<25; ++j){
                    letter = (char)((letter + 1-'a')%26 + 'a');
                    new_letter = Character.toString(letter);
                    sb.replace(i, i+1, new_letter);
                    changed_word = (sb.toString());

                    if (word_list.containsKey(changed_word)){
                        if (!used_words.contains(changed_word)){
                            used_words.add(changed_word);
                            if (changed_word.equals(word2)){
                                System.out.print("\nA ladder from "+word1+" to "+word2+" is: \n");
                                while (!word_ladder.isEmpty()){
                                    System.out.print(word_ladder.pop());
                                    System.out.print(" ");
                                }
                                System.out.print(word2);
                                return;
                            }
                            else{
                                LinkedList<String> copy_ladder = new LinkedList<String>(word_ladder);
                                copy_ladder.add(changed_word);
                                ladders.add(copy_ladder);
                            }
                        }
                    }
                }


            }
        }
        System.out.print("\nSorry, no ladder found in this dictionary.");


    }

    public static LinkedList<String> return_ladder(String word1, String word2,Map word_list){

        List<String> used_words = new ArrayList<>();
        Queue<LinkedList<String>> ladders = new LinkedList<LinkedList<String>>();
        LinkedList<String> word_ladder = new LinkedList<String>();
        String this_word = "";
        StringBuilder sb;
        char letter;
        String new_letter;
        String changed_word;
        int word_len = word1.length();

        word_ladder.add(word1);
        used_words.add(word1);
        ladders.add(word_ladder);

        while (!ladders.isEmpty()){
            word_ladder = ladders.poll();
            this_word = word_ladder.getLast();
            for (int i=0; i<word_len; i++){

                letter = this_word.charAt(i);
                sb = new StringBuilder(this_word);
                for (int j =0; j<25; ++j){
                    letter = (char)((letter + 1-'a')%26 + 'a');
                    new_letter = Character.toString(letter);
                    sb.replace(i, i+1, new_letter);
                    changed_word = (sb.toString());

                    if (word_list.containsKey(changed_word)){
                        if (!used_words.contains(changed_word)){
                            used_words.add(changed_word);
                            if (changed_word.equals(word2)){
                                System.out.print("\nLadder found");
                                word_ladder.add(changed_word);
                                return word_ladder;
                            }
                            else{
                                LinkedList<String> copy_ladder = new LinkedList<String>(word_ladder);
                                copy_ladder.add(changed_word);
                                ladders.add(copy_ladder);
                            }
                        }
                    }
                }


            }
        }
        System.out.print("\nNo ladder found");

    return new LinkedList<String>();
    }

    public static String find_ladder_for_web(String word1, String word2,Map word_list){

        List<String> used_words = new ArrayList<>();
        Queue<LinkedList<String>> ladders = new LinkedList<LinkedList<String>>();
        LinkedList<String> word_ladder = new LinkedList<String>();
        String this_word = "";
        StringBuilder sb;
        char letter;
        String new_letter;
        String changed_word;
        String message;
        int word_len = word1.length();

        word_ladder.add(word1);
        used_words.add(word1);
        ladders.add(word_ladder);

        while (!ladders.isEmpty()){
            word_ladder = ladders.poll();
            this_word = word_ladder.getLast();
            for (int i=0; i<word_len; i++){

                letter = this_word.charAt(i);
                sb = new StringBuilder(this_word);
                for (int j =0; j<25; ++j){
                    letter = (char)((letter + 1-'a')%26 + 'a');
                    new_letter = Character.toString(letter);
                    sb.replace(i, i+1, new_letter);
                    changed_word = (sb.toString());

                    if (word_list.containsKey(changed_word)){
                        if (!used_words.contains(changed_word)){
                            used_words.add(changed_word);
                            if (changed_word.equals(word2)){
                                message = "A ladder from "+word1+" to "+word2+" is: \n";
                                while (!word_ladder.isEmpty()){
                                   message += word_ladder.pop() + " ";
                                }
                                message +=word2;
                                return message;
                            }
                            else{
                                LinkedList<String> copy_ladder = new LinkedList<String>(word_ladder);
                                copy_ladder.add(changed_word);
                                ladders.add(copy_ladder);
                            }
                        }
                    }
                }


            }
        }
        return "\nSorry, no ladder found in the dictionary.";


    }

}
