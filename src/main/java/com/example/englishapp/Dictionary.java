package com.example.englishapp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Dictionary {
    public static Vector<Word> data = new Vector<Word>(10);

    /**
     * get all the word in English as a List of string.
     * @return list of all English word
     */
    public static List<String> get_target_list() {
        List<String> res = new ArrayList<>();
        for(Word word : data) {
            res.add(word.getWord_target());
        }
        return res;
    }

    /**
     * get definition (meaning) of an English word
     * @param target the word that need to get definition of
     * @return definition of target
     */
    public static String get_definition(String target) {
        for(Word word : data) {
            if(word.getWord_target().equals(target)) return word.getWord_explain();
        }
        return "Not in dictionary";
    }

    /**
     * Add new word into dictionary data
     * @param target English word
     * @param explain Vietnamese meaning
     */
    public void add_word(String target, String explain) {
        data.add(new Word(target, explain));
    }

    /**
     * Delete a word from data.
     * @param target word to be deleted
     */
    public void delete_word(String target) {
        for(int i = 0; i < data.size(); i++){
            if(data.get(i).getWord_target().equals(target)){
                data.remove(i);
            }
        }

    }
    public void test_update(){
        Word word = new Word("a", "a");
        data.set(1, word);
    }

    /**
     * Update English meaning of a word
     * @param first old word
     * @param last new word
     */
    public void update_word_target(String first, String last){
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getWord_target().equals(first)) {
                Word word = new Word(last, data.get(i).getWord_explain());
                data.set(i, word);
            }
        }
    }

    /**
     * Update Vietnamese meaning of a word
     * @param first old word
     * @param last new word
     */
    public void update_word_explain(String first, String last){
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getWord_explain().equals(first)) {
                Word word = new Word(data.get(i).getWord_target(), last);
                data.set(i, word);
            }
        }
    }
}
