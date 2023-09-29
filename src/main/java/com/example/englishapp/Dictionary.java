package com.example.englishapp;

import java.io.*;
import java.util.Vector;

public class Dictionary {
    public static Vector<Word> dic = new Vector<Word>(10);

    public void add_word(String target, String explain) {
        dic.add(new Word(target, explain));
    }
    public void delete_word(String target) {
        for(int i = 0; i < dic.size(); i++){
            if(dic.get(i).getWord_target().equals(target)){
                dic.remove(i);
            }
        }

    }
    public void test_update(){
        Word word = new Word("a", "a");
        dic.set(1, word);
    }
    public void update_word_target(String first, String last){
        for (int i = 0; i < dic.size(); i++) {
            if (dic.get(i).getWord_target().equals(first)) {
                Word word = new Word(last, dic.get(i).getWord_explain());
                dic.set(i, word);
            }
        }
    }
    public void update_word_explain(String first, String last){
        for (int i = 0; i < dic.size(); i++) {
            if (dic.get(i).getWord_explain().equals(first)) {
                Word word = new Word(dic.get(i).getWord_target(), last);
                dic.set(i, word);
            }
        }
    }
}
