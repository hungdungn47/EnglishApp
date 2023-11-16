package com.example.englishapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    private static class TrieNode {
        private final Map<Character, TrieNode> children;
        private boolean isEnd;

        public TrieNode() {
            this.children = new HashMap<>();
            this.isEnd = false;
        }

        public void insert(String word) {
            TrieNode node = this;
            for (char c : word.toCharArray()) {
                node.children.putIfAbsent(c, new TrieNode());
                node = node.children.get(c);
            }
            node.isEnd = true;
        }

        public List<String> findAllWithPrefix(String prefix) {
            List<String> result = new ArrayList<>();
            TrieNode node = this;

            for (char c : prefix.toCharArray()) {
                if (!node.children.containsKey(c)) {
                    return result; // Prefix not found
                }
                node = node.children.get(c);
            }

            findAllWords(node, prefix, result);
            return result;
        }

        private void findAllWords(TrieNode node, String prefix, List<String> result) {
            if (node.isEnd) {
                result.add(prefix);
            }

            for (char c : node.children.keySet()) {
                findAllWords(node.children.get(c), prefix + c, result);
            }
        }
    }

    private final TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    public void insert(String word) {
        root.insert(word);
    }

    public List<String> findAllWithPrefix(String prefix) {
        return root.findAllWithPrefix(prefix);
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("xin chào");
        trie.insert("cám ơn");
        trie.insert("việt nam");
        trie.insert("phở");

        System.out.println("Words with prefix 'vi': " + trie.findAllWithPrefix("Vi"));
        System.out.println("Words with prefix 'ph': " + trie.findAllWithPrefix("ph"));
    }
}
