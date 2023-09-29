package com.example.englishapp;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class DictionaryCommandLine {
    public static void showAllWords() {
        int counter = 1;
        System.out.printf("%-3s|   %-10s|   %s\n", "No","English", "Vietnamese");
        for(Word word : Dictionary.dic) {
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
    public static void dictionaryAdvanced() throws FileNotFoundException {
        System.out.println("Welcome to My Application!");
        System.out.println("0. Exit");
        System.out.println("1. Show all words");
        System.out.println("2. Add word");
        System.out.println("3. Remove word");
        System.out.println("4. Look up");

        Scanner in = new Scanner(System.in);
        while(true) {
            int request = in.nextInt();
            switch (request) {
                case 0:
                    return;
                case 1:
                    showAllWords();
                    break;
                case 2:
                    DictionaryManagement.insertFromCommandline();
                    break;
                case 3:
                    DictionaryManagement.delete_word();
                    break;
                case 4:
                    DictionaryManagement.dictionaryLookup();
                    break;
            }
        }
    }
    public static void main(String[] args) throws FileNotFoundException {
        DictionaryManagement.insertFromFile();
        dictionaryAdvanced();
    }
}
