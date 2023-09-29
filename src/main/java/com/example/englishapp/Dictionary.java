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
}
