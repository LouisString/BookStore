package com.light.springboot.utils;

import java.io.*;
import java.util.*;

public class readFile {

    public static Map readTxtFile(String filePath){
        try {
            Map word_list = new HashMap();
            String encoding="GBK";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    word_list.put(lineTxt, 0);
                }
                read.close();
                return word_list;
            }else{
                return new HashMap();
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();

        }
        return new HashMap();
    }

}
