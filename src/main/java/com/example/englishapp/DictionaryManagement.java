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
                fw = new FileWriter("D:\\Coding\\Java\\OOP\\EnglishApp\\src\\main\\java\\com\\example\\englishapp\\data\\dictionaries.txt", true);
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
}
