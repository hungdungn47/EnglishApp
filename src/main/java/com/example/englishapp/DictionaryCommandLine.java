package com.example.englishapp;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class DictionaryCommandLine {
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
    public static void dictionaryBasic() throws FileNotFoundException {
        DictionaryManagement.insertFromFile();
        DictionaryManagement.insertFromCommandline();
        DictionaryManagement.delete_word();
        showAllWords();
    }
    public static void dictionaryExportToFile(){

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
        boolean check = true;
        while(true){
            System.out.println("Chọn chức năng: ");
            int request = in.nextInt();
            if(request == 0){
                return;
            }
            else if(request == 1){
                DictionaryManagement.insertFromCommandline();
                check = true;
            }
            //bug
            else if(request == 2){
                DictionaryManagement.delete_word();
            }
            //bug1

            else if(request == 3){
                DictionaryManagement.update_word();
            }
            else if(request == 4){
                if(check){
                    DictionaryManagement.insertFromFile();
                }
                showAllWords();
            }
            else if(request == 5){
                if(check){
                    DictionaryManagement.insertFromFile();
                }
                DictionaryManagement.dictionaryLookup();
            }
            else if(request == 6){
                if(check){
                    DictionaryManagement.insertFromFile();
                }
                DictionaryManagement.dictionarySearcher();
            }
            else {
                System.out.println("Action not supported");
            }
            check = false;
        }
    }
    public static void main(String[] args) throws FileNotFoundException {
        dictionaryAdvanced();
        System.out.println("Bye");
    }
}
