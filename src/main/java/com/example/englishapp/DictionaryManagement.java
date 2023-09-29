package com.example.englishapp;

import java.io.*;
import java.util.Scanner;
public class DictionaryManagement {
    public static Dictionary dictionary = new Dictionary();
    public static void insertFromFile() throws FileNotFoundException {
        File file = new File("src/main/resources/data/dictionaries.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while(true) {
            try {
                if ((line = br.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String[] row = line.split(",");
            dictionary.add_word(row[0], row[1]);
        }
    }
    public static void insertFromCommandline() {
        Scanner in = new Scanner(System.in);
        System.out.println("How many words do you want to add ?");
        int n = in.nextInt();
        String store_cache_junk = in.nextLine();
        while(n > 0) {
            System.out.println("Please enter word, then enter meaning");
            String target = in.nextLine();
            String explain = in.nextLine();
            dictionary.add_word(target, explain);
            FileWriter fw = null;
            try {
                fw = new FileWriter("src/main/resources/data/dictionaries.txt", true);
                // đường dẫn tương đối để lưu file
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                fw.write(target + "," + explain + "\n");
                fw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            n--;
        }
    }
    public static void dictionaryLookup() {
        System.out.println("Muốn dịch từ gì: ");
        Scanner in = new Scanner(System.in);
        String temp = in.nextLine();
        for(Word word : dictionary.dic){
            if(word.getWord_target().equals(temp)){
                System.out.println(word.getWord_explain());
            }
        }
    }
    public static void delete_word(){
        System.out.println("Muốn xóa từ gì: ");
        Scanner in = new Scanner(System.in);
        String temp = in.nextLine();
        dictionary.delete_word(temp);
    }
    public static void update_word(){
        System.out.println("Bạn muốn sửa gì: ");
        System.out.println("[t] word_target");
        System.out.println("[e] word_explain");

        Scanner in = new Scanner(System.in);
        String choose = in.nextLine();

        System.out.println("Nhập từ bạn muốn sửa: ");
        String first = in.nextLine();
        System.out.println("Bạn muốn sửa thành: ");
        String last = in.nextLine();
        dictionary.test_update();
        if(choose.equals("t")){
            dictionary.update_word_target(first, last);
        }
        else if(choose.equals("e")){
            dictionary.update_word_explain(first, last);
        }
        else {
            return;
        }
    }
    public static void dictionarySearcher(){
        System.out.println("Nhập từ bạn muốn tra: ");
        Scanner in = new Scanner(System.in);
        String word_search = in.nextLine();
        for(Word word : Dictionary.dic){
            if(word.getWord_target().startsWith(word_search)){
                System.out.println(word.getWord_target());
            }
        }
    }
}