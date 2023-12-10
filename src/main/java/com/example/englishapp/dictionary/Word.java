package com.example.englishapp.dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Word {
    private String word_target;
    private String word_explain;

    public String getWord_target() {
        return word_target;
    }

    public String getWord_explain() {
        return word_explain;
    }

    public Word(String _word_target, String _word_explain) {
        this.word_target = _word_target;
        this.word_explain = _word_explain;
    }
}