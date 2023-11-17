package com.example.englishapp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
    public static List<String> readWordListFromFile(String filePath) {
        List<String> res = new ArrayList<>();
        FileReader file = null;
        try {
            file = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedReader br = new BufferedReader(file);
        String line;
        while (true) {
            try {
                if ((line = br.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            res.add(line);
        }
        return res;
    }
}
