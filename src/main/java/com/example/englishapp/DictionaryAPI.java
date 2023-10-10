package com.example.englishapp;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class DictionaryAPI {
    public static void main(String[] args) {

    }
    public static Word translate(String word) {
        try {
            URL url = new URL("https://api.dictionaryapi.dev/api/v2/entries/en/" + word);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            int responseCode = con.getResponseCode();

            if(responseCode != 200) {
                throw new RuntimeException("HttpResponseCode" + responseCode);
            } else {
                StringBuilder information = new StringBuilder();
                Scanner in = new Scanner(url.openStream());

                while(in.hasNext()) {
                    information.append(in.nextLine());
                }
                in.close();

                JSONParser parser = new JSONParser();
                JSONArray dataObject = (JSONArray) parser.parse(String.valueOf(information));

                JSONObject wordData = (JSONObject) dataObject.get(0);
                JSONArray meanings = (JSONArray) wordData.get("meanings");
                String phonetic = (String) wordData.get("phonetic");

                JSONObject meaning = (JSONObject) meanings.get(0);
                Word new_word = new Word(word, null);
                String partOfSpeech = (String) meaning.get("partOfSpeech");

                JSONArray list_of_definition = (JSONArray) meaning.get("definitions");
                JSONObject def = (JSONObject) list_of_definition.get(0);

                new_word.phonetic = phonetic;
                new_word.partOfSpeech = partOfSpeech;
                System.out.println(new_word.partOfSpeech);
                Map<String, String> mp = new HashMap<>();
                mp.put("definition", (String) def.get("definition"));
                mp.put("example", (String) def.get("example"));
                new_word.definitions.add(mp);
                return new_word;

//                for(int i = 0; i < meanings.size(); i++) {
//                    JSONObject meaning = (JSONObject) meanings.get(i);
//                    JSONArray defs = (JSONArray) meaning.get("definitions");
//                    JSONObject def = (JSONObject) defs.get(0);
//                    String definition = (String) def.get("definition");
//                    return definition;
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
