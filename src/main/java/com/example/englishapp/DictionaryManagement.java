package com.example.englishapp;

import java.io.*;
import java.util.*;

public class DictionaryManagement {
    // store each language's word into a set in order to easily detect language
    public static Set<String> englishWords = new HashSet<>();
    public static Set<String> vietnameseWords = new HashSet<>();

    /**
     * import data from text file.
     * @throws FileNotFoundException when cannot find the file
     */
    public static void insertFromFile() throws IOException {
//        Scanner sc = new Scanner(new File("src/main/resources/data/english-vietnamese.txt"));
//        sc.useDelimiter("@");
//        sc.next();
//        while (sc.hasNext()) {
//            String line = sc.next();
//            int delimiter_pos = line.length() - 1;
//            boolean found_delimiter = false;
//            for(int i = 0; i < line.length(); i++) {
//                if(line.charAt(i) == '/') {
//                    delimiter_pos = i;
//                    found_delimiter = true;
//                    break;
//                }
//            }
//            if(!found_delimiter) {
//                for(int i = 0; i < line.length(); i++) {
//                    if(line.charAt(i) == '*') {
//                        delimiter_pos = i;
//                        found_delimiter = true;
//                        break;
//                    }
//                }
//            }
//            if(!found_delimiter) {
//                for(int i = 0; i < line.length(); i++) {
//                    if(line.charAt(i) == '-') {
//                        delimiter_pos = i;
//                        break;
//                    }
//                }
//            }
//            String word = line.substring(0, delimiter_pos);
//            String definition = line.substring(delimiter_pos);
//            Dictionary.data.add(new Word(word.trim(), definition));
//        }
//        sc.close();
        readDataFromHtml("E_V.txt");
        System.out.println(Dictionary.data.size());
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
            Dictionary.data.add(new Word(word, definition));

            if(Objects.equals(fileName, "E_V.txt")) {
                englishWords.add(word);
            } else {
                vietnameseWords.add(word);
            }
        }
    }
    public static boolean isEnglish(String word) {
        return englishWords.contains(word);
    }

    /**
     * import data from command line / user input.
     */
    public static void insertFromCommandline() {
        Scanner in = new Scanner(System.in);
        System.out.println("How many words do you want to add ?");
        int n = in.nextInt();
        in.nextLine();
        while(n > 0) {
            System.out.println("Please enter word, then enter meaning");
            String target = in.nextLine();
            String explain = in.nextLine();
            Dictionary.data.add(new Word(target, explain));
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
    public static void addWord(String target, String explain) {
        Dictionary.data.add(new Word(target, explain));
        DictionaryCommandLine.dictionaryExportToFile(true);
    }

    /**
     * Look up word
     */
    public static String dictionaryLookup(String target, int languageOption) {
        for(Word word : Dictionary.data) {
            if(target.equals(word.getWord_target())) {
                return word.getWord_explain();
            }
        }
        if(languageOption == Controller.EN_TO_VI) {
            try {
                return GoogleTranslatorAPI.translate("en", "vi", target);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                return GoogleTranslatorAPI.translate("vi", "en", target);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * delete a word
     */
    public static void delete_word(String target){
        for(int i = 0; i < Dictionary.data.size(); i++){
            if(Dictionary.data.get(i).getWord_target().equals(target)){
                Dictionary.data.remove(i);
            }
        }
        DictionaryCommandLine.dictionaryExportToFile(false);
    }

    /**
     * change a word in dictionary
     */
    public static void update_word(){
        System.out.println("Bạn muốn sửa gì: ");
        System.out.println("[1] word_target");
        System.out.println("[2] word_explain");

        Scanner in = new Scanner(System.in);
        int choose = in.nextInt();

        in.nextLine();
        if(choose == 1) {
            System.out.println("Nhập từ bạn muốn sửa: ");
            String first = in.nextLine();
            System.out.println("Bạn muốn sửa thành: ");
            String last = in.nextLine();
            Dictionary.update_word_target(first, last);
        }
        else{
            System.out.println("Nhập từ bạn muốn sửa: ");
            String first = in.nextLine();
            System.out.println("Bạn muốn sửa thành: ");
            String last = in.nextLine();
            Dictionary.update_word_explain(first, last);
        }

        DictionaryCommandLine.dictionaryExportToFile(false);
    }

    /**
     * search hint.
     */
    public static void dictionarySearcher(){
        System.out.println("Nhập từ bạn muốn tra: ");
        Scanner in = new Scanner(System.in);
        String word_search = in.nextLine();
        for(Word word : Dictionary.data){
            if(word.getWord_target().startsWith(word_search)){
                System.out.println(word.getWord_target());
            }
        }
    }

    /**
     * return list of hint words when searching.
     * @param word_search searching string
     * @return list
     */
    public static List<String> searchHint(String word_search) {
        List<String> res = new ArrayList<>();
        for(Word word : Dictionary.data){
            if(word.getWord_target().startsWith(word_search)){
                res.add(word.getWord_target());
            }
        }
        return res;
    }
}