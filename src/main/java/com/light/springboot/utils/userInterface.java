package com.light.springboot.utils;

import java.util.*;

public class userInterface {
    public static void main(String []args) {
        boolean want_more = true;
        String ans;
        Scanner scanner;
        Map word_list = getWordlist.getDictionaty();
        List<String> words;
        String word1, word2;

        while (want_more){

            words = inquireWords.getWords(word_list);
            word1 = words.get(0);
            word2 = words.get(1);
            buildLadder process = new buildLadder();
            process.find_ladder(word1, word2, word_list);

            System.out.print("\n\nWant another try? Type 'y' or 'n': ");
            scanner = new Scanner(System.in);
            ans = scanner.nextLine();
            if (ans.equals("y")){
                continue;
            }
            else if (ans.equals("n") ){
                want_more = false;
            }
            else{
                System.out.print("Unexpected answer. Regard as 'n'.\n");
                want_more = false;
            }
        }
        System.out.print("\nThanks for using.");
    }


}
