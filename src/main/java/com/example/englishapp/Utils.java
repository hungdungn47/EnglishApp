package com.example.englishapp;

import java.io.FileWriter;
import java.io.IOException;

public class Utils {
    public static void exportToFile(String filePath, boolean append, String word) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(filePath, append);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            fw.write(word + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void exportToFile(String filePath, boolean append, String word, String definition, String delimiter) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(filePath, append);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            fw.write(word + delimiter + definition + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
