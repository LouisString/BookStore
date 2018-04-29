package com.light.springboot.utils;

import java.util.*;

public class getWordlist {
    public static Map getDictionaty(){
        boolean get_ans = false;
        boolean get_path = true;

        String ans;
        String filePath;
        Map word_list = new HashMap();
        Scanner scanner;

        System.out.print("Default dictionary file is dictionary.txt." +
                "\nIf you want to change, please enter 'y';" +
                "\nElse, please enter 'n': ");
        while (!get_ans){
            scanner = new Scanner(System.in);
            ans = scanner.next();
            if (ans.equals("y")){
                get_ans = true;
                get_path = false;
            }
            else if (ans.equals("n") ){
                get_ans = true;
            }
            else{
                System.out.print("Unexpected answer. Type again: ");
            }
        }

        if (get_path){
            filePath = "/dictionary.txt";
            readFile file = new readFile();
            word_list = file.readTxtFile(filePath);
        }
        else{
            System.out.print("Input the file name (with suffix): ");
            while (!get_path){
                scanner = new Scanner(System.in);
                filePath = scanner.next();
                readFile file = new readFile();
                word_list = file.readTxtFile(filePath);
                if (word_list.isEmpty()){
                    System.out.print("No such file or it contains no words. Try another one: ");
                }
                else{
                    get_path = true;
                }
            }
        }
        return word_list;
    }

    public static Map getDictionatyForWeb(){
        //idea 会认为 “static/dictionary.txt” 是正确路径，但其实是错的
        String filePath= "/dictionary.txt";
        Map word_list = new HashMap();
        readFile file = new readFile();
        word_list = file.readTxtFile(filePath);
        return word_list;
    }
}
