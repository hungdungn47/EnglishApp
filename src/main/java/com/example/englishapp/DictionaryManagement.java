package com.example.englishapp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class DictionaryManagement {
    // Init a new dictionary
    public static Dictionary dictionary = new Dictionary();

    /**
     * import data from text file.
     * @throws FileNotFoundException when cannot find the file
     */
    public static void insertFromFile() throws FileNotFoundException {
        File file = new File("src/main/resources/data/words_alpha.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while(true) {
            try {
                if ((line = br.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //String[] row = line.split(",");
            dictionary.add_word(line, null);
        }
    }

    /**
     * import data from command line / user input.
     */
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
    public static void addWord(String target, String explain) {
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
    }

    /**
     * Look up word
     */
    public static void dictionaryLookup() {
        System.out.println("Muốn dịch từ gì: ");
        Scanner in = new Scanner(System.in);
        String temp = in.nextLine();
        for(Word word : Dictionary.data){
            if(word.getWord_target().equals(temp)){
                System.out.println(word.getWord_explain());
            }
        }
    }

    /**
     * delete a word
     */
    public static void delete_word(String temp){
//        System.out.println("Muốn xóa từ gì: ");
//        Scanner in = new Scanner(System.in);
//        String temp = in.nextLine();
        dictionary.delete_word(temp);
        FileWriter fw = null;
        try {
            fw = new FileWriter("src/main/resources/data/dictionaries.txt");
            // đường dẫn tương đối để lưu file
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(Word word : Dictionary.data){
            try {
                fw.write( word.getWord_target()+ "," + word.getWord_explain() + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * change a word in dictionary
     */
    public static void update_word(){
        System.out.println("Bạn muốn sửa gì: ");
        System.out.println("[1] word_target");
        System.out.println("[2] word_explain");

        Scanner in = new Scanner(System.in);
        int choose = in.nextInt();

        in.nextLine();
        if(choose == 1) {
            System.out.println("Nhập từ bạn muốn sửa: ");
            String first = in.nextLine();
            System.out.println("Bạn muốn sửa thành: ");
            String last = in.nextLine();
            dictionary.update_word_target(first, last);
        }
        else{
            System.out.println("Nhập từ bạn muốn sửa: ");
            String first = in.nextLine();
            System.out.println("Bạn muốn sửa thành: ");
            String last = in.nextLine();
            dictionary.update_word_explain(first, last);
        }

        DictionaryCommandLine.dictionaryExportToFile();
    }

    /**
     * search hint
     */
    public static void dictionarySearcher(){
        System.out.println("Nhập từ bạn muốn tra: ");
        Scanner in = new Scanner(System.in);
        String word_search = in.nextLine();
        for(Word word : Dictionary.data){
            if(word.getWord_target().startsWith(word_search)){
                System.out.println(word.getWord_target());
            }
        }
    }
    public static List<String> searchHint(String word_search) {
        List<String> res = new ArrayList<>();
        for(Word word : Dictionary.data){
            if(word.getWord_target().startsWith(word_search)){
                res.add(word.getWord_target());
            }
        }
        return res;
    }
}