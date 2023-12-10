package com.example.englishapp.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DailyRandomWordGenerator {
    private static final String DATA_FILE_PATH = "src/main/resources/data/daily_words_data.txt";

    private final List<String> wordList;
    private LocalDate currentDate;
    private List<Integer> indices;

    public DailyRandomWordGenerator(List<String> wordList) {
        this.wordList = wordList;
        loadStoredData();
        if(currentDate.equals(LocalDate.MIN)) {
            generateDailyWords();
        }
    }

    public List<String> getDailyWords() {
        if (!currentDate.equals(LocalDate.now())) {
            currentDate = LocalDate.now();
            generateDailyWords();
            saveDataToFile();
        }
        return getWordsFromIndices();
    }

    private void generateDailyWords() {
        SecureRandom random = new SecureRandom();
        indices = new ArrayList<>();
        while (indices.size() < Math.min(5, wordList.size())) {
            int randomIndex = random.nextInt(wordList.size());
            if (!indices.contains(randomIndex)) {
                indices.add(randomIndex);
            }
        }
        Collections.shuffle(indices, random);
    }

    private List<String> getWordsFromIndices() {
        List<String> dailyWords = new ArrayList<>();
        for (int index : indices) {
            dailyWords.add(wordList.get(index));
        }
        return dailyWords;
    }

    private void loadStoredData() {
        try {
            List<String> lines = Files.readAllLines(Path.of(DATA_FILE_PATH));
            if (lines.size() == 2) {
                currentDate = LocalDate.parse(lines.get(0));
                indices = parseIndices(lines.get(1));
            } else {
                initializeDefaultValues();
            }
        } catch (IOException e) {
            initializeDefaultValues();
        }
    }

    private void saveDataToFile() {
        try {
            List<String> lines = new ArrayList<>();
            lines.add(currentDate.toString());
            lines.add(indicesToString());
            Files.write(Path.of(DATA_FILE_PATH), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeDefaultValues() {
        currentDate = LocalDate.MIN;
        indices = new ArrayList<>();
    }

    private List<Integer> parseIndices(String indicesString) {
        List<Integer> parsedIndices = new ArrayList<>();
        String[] parts = indicesString.split(",");
        for (String part : parts) {
            parsedIndices.add(Integer.parseInt(part));
        }
        return parsedIndices;
    }

    private String indicesToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indices.size(); i++) {
            sb.append(indices.get(i));
            if (i < indices.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
