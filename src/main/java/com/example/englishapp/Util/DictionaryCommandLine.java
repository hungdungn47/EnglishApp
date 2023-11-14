package com.example.englishapp.Util;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class
DictionaryCommandLine {
    /**
     * Display all words in dictionary on console
     */
    public static void showAllWords() {
        int counter = 1;
        System.out.printf("%-3s|   %-10s|   %s\n", "No","English", "Vietnamese");
        for(Word word : Dictionary.data) {
            System.out.printf("%-3s|   %-10s|   %s", Integer.toString(counter), word.getWord_target(),word.getWord_explain());
            System.out.println();
            counter++;
        }
    }
    public static void dictionaryBasic() throws IOException {
        DictionaryManagement.insertFromFile();
        DictionaryManagement.insertFromCommandline();
        showAllWords();
    }
    public static void dictionaryExportToFile(boolean append){
        FileWriter fw = null;
        try {
            fw = new FileWriter("src/main/resources/data/dictionaries.txt", append);
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
    public static void changeFavoriteWords(String fileName, List<String> favoriteWords){
        FileWriter fw = null;
        String filePath = "src/main/resources/data/favoriteWords/" + fileName;
        try {
            fw = new FileWriter(filePath, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(String s :favoriteWords) {
            try {
                fw.write(s + "\n");
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
     * Display a menu for user
     * @throws FileNotFoundException when cannot find data file
     */
    public static void dictionaryAdvanced() throws FileNotFoundException {
        System.out.println("Welcome to My Application!");
        System.out.println("[0] Exit");
        System.out.println("[1] Add word");
        System.out.println("[2] Remove word");
        System.out.println("[3] Update / Sửa từ");
        System.out.println("[4] Display / Show all words");
        System.out.println("[5] Lookup");
        System.out.println("[6] Search");
        System.out.println("[7] Game");
        System.out.println("[8] Import from file");
        System.out.println("[9] Export to file");

        Scanner in = new Scanner(System.in);
        while(true){
            System.out.println("Chọn chức năng: ");
            int request = in.nextInt();
            in.nextLine();
            if(request == 0){
                return;
            }
            else if(request == 1){
                DictionaryManagement.insertFromCommandline();
            }
            else if(request == 2){
                System.out.println("Muốn xóa từ gì: ");
                String temp = in.nextLine();
                DictionaryManagement.delete_word(temp);
            }
            else if(request == 3){
                DictionaryManagement.update_word();
            }
            else if(request == 4){
                showAllWords();
            }
            else if(request == 5){
                System.out.println("Enter the word you wanna look up:");
                String target = in.nextLine();
                System.out.println(DictionaryManagement.dictionaryLookup(target, 0));
            }
            else if(request == 6){
                DictionaryManagement.dictionarySearcher();
            }
            else {
                System.out.println("Action not supported");
            }
        }
    }
    public static void main(String[] args) throws IOException {
        DictionaryManagement.insertFromFile();
        dictionaryAdvanced();
        System.out.println("Bye");
    }
}
