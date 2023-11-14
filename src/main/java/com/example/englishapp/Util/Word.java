package com.example.englishapp.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Word {
    public String partOfSpeech;
    public List<Map<String, String>> definitions = new ArrayList<>();
    private String word_target;
    private String word_explain;
    public String phonetic;
    public String getWord_target() {
        return word_target;
    }

    public void setWord_target(String word_target) {
        this.word_target = word_target;
    }

    public String getWord_explain() {
        return word_explain;
    }

    public void setWord_explain(String word_explain) {
        this.word_explain = word_explain;
    }
    public Word(String _word_target, String _word_explain) {
        this.word_target = _word_target;
        this.word_explain = _word_explain;
    }
}