package com.example.englishapp;

import javax.print.DocFlavor;
import java.util.*;

public class Dictionary {
    private Trie searcher = new Trie();
    public Vector<Word> data = new Vector<Word>(1000);
    private final Map<String, Word> mp = new TreeMap<>();

    /**
     * get all the word in English as a List of string.
     *
     * @return list of all English word
     */
    public List<String> getWordList() {
        return new ArrayList<>(mp.keySet());
    }

    public void addWord(String target, String explain) {
        searcher.insert(target);
        mp.put(target, new Word(target, explain));
    }
    public void deleteWord(String target) {
        mp.remove(target);
    }
    public boolean contains(String word) {
        return mp.containsKey(word);
    }
    public String getDefinition(String target) {
        return mp.get(target).getWord_explain();
    }

    /**
     * Update English meaning of a word
     *
     * @param first old word
     * @param last  new word
     */
    public void update_word_target(String first, String last) {
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
    public void update_word_explain(String first, String last) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getWord_explain().equals(first)) {
                data.set(i, new Word(data.get(i).getWord_target(), last));
            }
        }
    }
    public List<String> searchHint(String prefix) {
        return searcher.findAllWithPrefix(prefix);
    }
    public void updateWord(String word, String newDefinition) {
        if(mp.containsKey(word)) {
            mp.put(word, new Word(word, newDefinition));
        }
    }
}
