package com.example.englishapp.dictionary;

import com.example.englishapp.Controller;
import com.example.englishapp.util.GoogleTranslatorAPI;
import com.example.englishapp.util.Login;
import com.example.englishapp.util.Utils;

import java.io.*;
import java.util.*;

public class DictionaryManagement {
    public static com.example.englishapp.dictionary.Dictionary enViDic = new EnglishVietnameseDictionary();
    public static Dictionary viEnDic = new VietnameseEnglishDictionary();

    /**
     * import data from text file.
     *
     * @throws FileNotFoundException when cannot find the file
     */
    public static void insertFromFile() throws IOException {
        enViDic.readDataFromHtml();
        viEnDic.readDataFromHtml();
    }

//    public static void readDataFromHtml(String fileName) throws IOException {
//        FileReader file = new FileReader("src/main/resources/data/" + fileName);
//        BufferedReader br = new BufferedReader(file);
//        String line;
//        while ((line = br.readLine()) != null) {
//            String[] parts = line.split("<html>");
//            String word = parts[0];
//            String definition = "<html>" + parts[1];
//
//            if (Objects.equals(fileName, "E_V.txt")) {
//                enViDic.addWord(word, definition);
//            } else {
//                viEnDic.addWord(word, definition);
//            }
//        }
//    }
    public static void readUpdatedWord() {
        List<String[]> updatedWords = Utils.getUpdatedWords(Login.getUsername());
        for(String[] word : updatedWords) {
            enViDic.updateWord(word[0], word[1]);
            viEnDic.updateWord(word[0], word[1]);
        }
        System.out.println("read updated words");
    }
    public static void readAddedAndDeletedWord() throws IOException {
        List<String> deletedWords = Utils.getDeletedWords(Login.getUsername());
        for(String s : deletedWords) {
            enViDic.deleteWord(s);
        }
        List<String[]> addedWords = Utils.getAddedWords(Login.getUsername());
        for(String[] parts : addedWords) {
            enViDic.addWord(parts[0], parts[1]);
        }
    }

    public static boolean isEnglish(String word) {
        return enViDic.contains(word);
    }

    /**
     * import data from command line / user input.
     */
    public static void insertFromCommandline() {
        Scanner in = new Scanner(System.in);
        System.out.println("How many words do you want to add ?");
        int n = in.nextInt();
        in.nextLine();
        while (n > 0) {
            System.out.println("Please enter word, then enter meaning");
            String target = in.nextLine();
            String explain = in.nextLine();
            enViDic.addWord(target, explain);
            FileWriter fw = null;
            try {
                fw = new FileWriter("src/main/resources/data/dictionaries.txt", true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                fw.write(target + "," + explain + "\n");
                fw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            n--;
        }
    }

    public static void delete_word(String target) {
        enViDic.deleteWord(target);
        Utils.deleteWord(Login.getUsername(), target);
    }

    public static void addWord(String target, String explain) {
        enViDic.addWord(target, explain);
        Utils.addWord(Login.getUsername(), target, explain);
    }
    public static String TranslateParagraph(String target, String lang_from, String lang_to){
        try {
            return GoogleTranslatorAPI.translate(lang_from, lang_to, target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String dictionaryLookup(String target, int languageOption) {
        if (languageOption == Controller.EN_TO_VI) {
            if(enViDic.contains(target)) {
                return enViDic.getDefinition(target);
            }
            try {
                return GoogleTranslatorAPI.translate("en", "vi", target);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if(languageOption == Controller.VI_TO_EN) {
            if(viEnDic.contains(target)) {
                return viEnDic.getDefinition(target);
            }
            try {
                return GoogleTranslatorAPI.translate("vi", "en", target);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(enViDic.contains(target)) {
            return enViDic.getDefinition(target);
        }
        if(viEnDic.contains(target)) {
            return viEnDic.getDefinition(target);
        }
        try {
            return GoogleTranslatorAPI.translate("en", "vi", target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void update_word() {
        System.out.println("Bạn muốn sửa gì: ");
        System.out.println("[1] word_target");
        System.out.println("[2] word_explain");

        Scanner in = new Scanner(System.in);
        int choose = in.nextInt();

        in.nextLine();
        if (choose == 1) {
            System.out.println("Nhập từ bạn muốn sửa: ");
            String first = in.nextLine();
            System.out.println("Bạn muốn sửa thành: ");
            String last = in.nextLine();
            enViDic.update_word_target(first, last);
        } else {
            System.out.println("Nhập từ bạn muốn sửa: ");
            String first = in.nextLine();
            System.out.println("Bạn muốn sửa thành: ");
            String last = in.nextLine();
            enViDic.update_word_explain(first, last);
        }

        DictionaryCommandLine.dictionaryExportToFile(false);
    }

    /**
     * search hint.
     */
    public static void dictionarySearcher() {
        System.out.println("Nhập từ bạn muốn tra: ");
        Scanner in = new Scanner(System.in);
        String word_search = in.nextLine();
        for (Word word : enViDic.data) {
            if (word.getWord_target().startsWith(word_search)) {
                System.out.println(word.getWord_target());
            }
        }
    }

    /**
     * return list of hint words when searching.
     *
     * @param prefix searching string
     * @return list
     */
    public static List<String> searchHint(String prefix, int languageOption) {
        if(languageOption == Controller.EN_TO_VI) {
            return enViDic.searchHint(prefix);
        } else {
            return viEnDic.searchHint(prefix);
        }
    }
}