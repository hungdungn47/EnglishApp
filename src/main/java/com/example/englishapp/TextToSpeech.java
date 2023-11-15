package com.example.englishapp;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

public class TextToSpeech {
    public static void pronounce(String word, String lang) {
        String[] words = encodeValue(word).split(" ");
        try{
            StringBuilder url = new StringBuilder("https://translate.google.com/translate_tts?ie=UTF-8&client=tw-ob&tl="
                    + lang + "&q=");
            for(String s : words) {
                url.append(s);
                url.append("-");
            }
            url.deleteCharAt(url.length() - 1);
            URLConnection conn = new URI(url.toString()).toURL().openConnection();
            InputStream is = conn.getInputStream();
            System.out.println(url.toString());

            OutputStream outstream = new FileOutputStream(new File("src/main/resources/audio/file.mp3"));
            byte[] buffer = new byte[4096];
            int len;
            while ((len = is.read(buffer)) > 0) {
                outstream.write(buffer, 0, len);
            }
            outstream.close();
            String bip = "src/main/resources/audio/file.mp3";
            Media hit = new Media(new File(bip).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String encodeValue(String value) {
        return java.net.URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}