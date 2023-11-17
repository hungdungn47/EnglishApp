package com.example.englishapp;

import java.io.*;
import java.util.*;

public class DictionaryManagement {
    // store each language's word into a set in order to easily detect language
    public static Dictionary enViDic = new Dictionary();
    public static Dictionary viEnDic = new Dictionary();

    /**
     * import data from text file.
     *
     * @throws FileNotFoundException when cannot find the file
     */
    public static void insertFromFile() throws IOException {
        readDataFromHtml("E_V.txt");
        readDataFromHtml("V_E.txt");
    }

    public static void readDataFromHtml(String fileName) throws IOException {
        FileReader file = new FileReader("src/main/resources/data/" + fileName);
        BufferedReader br = new BufferedReader(file);
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("<html>");
            String word = parts[0];
            String definition = "<html>" + parts[1];

            if (Objects.equals(fileName, "E_V.txt")) {
                enViDic.addWord(word, definition);
            } else {
                viEnDic.addWord(word, definition);
            }
        }
    }
    public static void readUpdatedWord(String username) {
        FileReader file = null;
        try {
            file = new FileReader("src/main/resources/data/UpdatedWord/" + username + "UpdatedWords.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedReader br = new BufferedReader(file);
        String line;
        while (true) {
            try {
                if ((line = br.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String[] parts = line.split("<html>");
            String word = parts[0];
            String newDefinition = "<html>" + parts[1];
            enViDic.updateWord(word, newDefinition);
            viEnDic.updateWord(word, newDefinition);
        }
    }
    public static void readAddedAndDeletedWord() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("src/main/resources/data/WordAdded/" + Login.getUsername() + "wordsAdded.txt"));
        while (sc.hasNext()) {
            String temp = sc.next();
            int index = 0;
            for (int i = 0; i < temp.length(); i++) {
                if (temp.charAt(i) == ':') {
                    index = i;
                    break;
                }
            }
            String target = temp.substring(0, index);
            String explain = temp.substring(index + 1);
            enViDic.addWord(target, explain);
        }
        Scanner file2 = new Scanner(new File("src/main/resources/data/WordDeleted/" + Login.getUsername() + "wordsDeleted.txt"));
        while (file2.hasNext()) {
            String temp = file2.next();
            enViDic.deleteWord(temp);
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

    /**
     * delete a word
     */
    public static void delete_word(String target) {
        enViDic.deleteWord(target);
        update_worddeleted_file(Login.getUsername(), target);
    }

    public static void addWord(String target, String explain) {
        enViDic.addWord(target, explain);
        update_wordadd_file(Login.getUsername(), target, explain);
    }

    public static void update_worddeleted_file(String user, String target) {
        String filePath = "src/main/resources/data/WordDeleted/" + user + "wordsDeleted.txt";
        Utils.exportToFile(filePath, true, target);
//        FileWriter fw = null;
//        try {
//            fw = new FileWriter("src/main/resources/data/WordDeleted/" + user + "wordsDeleted.txt");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        try {
//            fw.write(target + "\n");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        try {
//            fw.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static void update_wordadd_file(String user, String target, String explain) {
        String filePath = "src/main/resources/data/WordAdded/" + user + "wordsAdded.txt";
        Utils.exportToFile(filePath, true, target, explain, ":");
//        FileWriter fw = null;
//        try {
//            fw = new FileWriter("src/main/resources/data/WordAdded/" + user + "wordsAdded.txt");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        try {
//            fw.write(target + ":" + explain + "\n");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        try {
//            fw.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    /**
     * Look up word
     */
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

    /**
     * change a word in dictionary
     */
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