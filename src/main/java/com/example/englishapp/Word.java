package com.example.englishapp;

public class Word {
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

    private String word_target;
    private String word_explain;
    public Word(String _word_target, String _word_explain) {
        this.word_target = _word_target;
        this.word_explain = _word_explain;
    }
}