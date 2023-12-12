package com.example.englishapp.dictionary;

import com.example.englishapp.dictionary.Dictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EnglishVietnameseDictionary extends Dictionary {
    public void readDataFromHtml() throws IOException {
        FileReader file = new FileReader("src/main/resources/data/E_V.txt" );
        BufferedReader br = new BufferedReader(file);
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("<html>");
            String word = parts[0];
            String definition = "<html>" + parts[1];
            this.addWord(word, definition);
        }
    }
}
