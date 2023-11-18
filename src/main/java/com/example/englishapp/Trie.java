package com.example.englishapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    private static class TrieNode {
        private final Map<Integer, TrieNode> children;
        private boolean isEnd;

        public TrieNode() {
            this.children = new HashMap<>();
            this.isEnd = false;
        }

        public void insert(String word) {
            TrieNode node = this;
            for (int i = 0; i < word.length(); i++) {
                int codePoint = word.codePointAt(i);
                node.children.putIfAbsent(codePoint, new TrieNode());
                node = node.children.get(codePoint);
            }
            node.isEnd = true;
        }

        public List<String> findAllWithPrefix(String prefix) {
            List<String> result = new ArrayList<>();
            TrieNode node = this;
            for (int i = 0; i < prefix.length(); i++) {
                int codePoint = prefix.codePointAt(i);
                if (!node.children.containsKey(codePoint)) {
                    return result; // Prefix not found
                }
                node = node.children.get(codePoint);
            }
            findAllWords(node, prefix, result);
            return result;
        }

        private void findAllWords(TrieNode node, String prefix, List<String> result) {
            if (node.isEnd) {
                result.add(prefix);
            }

            for (int codePoint : node.children.keySet()) {
                findAllWords(node.children.get(codePoint), prefix + new String(Character.toChars(codePoint)), result);
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
    public void delete(String word) {
        delete(root, word, 0);
    }

    private boolean delete(TrieNode node, String word, int depth) {
        if (depth == word.length()) {
            if (!node.isEnd) {
                return false; // Word doesn't exist in Trie
            }
            node.isEnd = false;
            return node.children.isEmpty(); // Check if node has no children to delete
        }

        int codePoint = word.codePointAt(depth);
        TrieNode nextNode = node.children.get(codePoint);
        if (nextNode == null) {
            return false; // Word doesn't exist in Trie
        }

        boolean shouldDelete = delete(nextNode, word, depth + Character.charCount(codePoint));
        if (shouldDelete) {
            node.children.remove(codePoint);
            return node.children.isEmpty() && !node.isEnd; // Remove node if it's not the end of another word
        }
        return false;
    }
}
