package com.example.englishapp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Dictionary {
    public static Vector<Word> data = new Vector<Word>(1000);

    /**
     * get all the word in English as a List of string.
     *
     * @return list of all English word
     */
    public static List<String> get_target_list() {
        List<String> res = new ArrayList<>();
        for (Word word : data) {
            res.add(word.getWord_target());
        }
        return res;
    }

    /**
     * Update English meaning of a word
     *
     * @param first old word
     * @param last  new word
     */
    public static void update_word_target(String first, String last) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getWord_target().equals(first)) {
                data.set(i, new Word(last, data.get(i).getWord_explain()));
            }
        }
    }

    /**
     * Update Vietnamese meaning of a word
     *
     * @param first old word
     * @param last  new word
     */
    public static void update_word_explain(String first, String last) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getWord_explain().equals(first)) {
                data.set(i, new Word(data.get(i).getWord_target(), last));
            }
        }
    }
}
