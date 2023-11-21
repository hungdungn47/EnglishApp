package com.example.englishapp;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    static final String DB_URL = "jdbc:mysql://sql12.freesqldatabase.com/sql12662519";
    static final String USER = "sql12662519";
    static final String PASS = "EmA6Z8XLRD";
    public static void exportToFile(String filePath, boolean append, String word) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(filePath, append);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            fw.write(word + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void exportToFile(String filePath, boolean append, String word, String definition, String delimiter) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(filePath, append);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            fw.write(word + delimiter + definition + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<String> readWordListFromFile(String filePath) {
        List<String> res = new ArrayList<>();
        FileReader file = null;
        try {
            file = new FileReader(filePath);
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
            res.add(line);
        }
        return res;
    }
    public static List<String> getFavoriteWords(String username) {
        List<String> favoriteWords = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = conn.prepareStatement("SELECT word FROM FavoriteWords WHERE username = ?")) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String word = resultSet.getString("word");
                favoriteWords.add(word);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(favoriteWords);
        return favoriteWords;
    }
    public static void insertFavoriteWord(String username, String word) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = conn.prepareStatement("INSERT INTO FavoriteWords (username, word) VALUES (?, ?)")) {

            statement.setString(1, username);
            statement.setString(2, word);
            statement.executeUpdate();
            System.out.println("New favorite word: " + word);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void removeFavoriteWord(String username, String word) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = conn.prepareStatement("DELETE FROM FavoriteWords WHERE username = ? AND word = ?")) {

            statement.setString(1, username);
            statement.setString(2, word);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static boolean isFavoriteWord(String username, String word) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM FavoriteWords WHERE username = ? AND word = ?")) {

            statement.setString(1, username);
            statement.setString(2, word);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Return true if word exists in the favorite words of the user

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if there was an error or word doesn't exist
    }

    public static List<String> getRecentWords(String username) {
        List<String> recentWords = new ArrayList<>();
        String sql = "SELECT word FROM RecentWords WHERE username = ? ORDER BY timestamp DESC LIMIT 50";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String word = resultSet.getString("word");
                recentWords.add(word);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }

        return recentWords;
    }

    public static void insertRecentWord(String username, String word) {
        String insertQuery = "INSERT INTO RecentWords (username, word) VALUES (?, ?)";
        String deleteQuery = "DELETE FROM RecentWords WHERE id IN (SELECT id FROM RecentWords ORDER BY timestamp LIMIT 1)";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
             Statement deleteStatement = connection.createStatement()) {

            // Insert the new word
            insertStatement.setString(1, username);
            insertStatement.setString(2, word);
            insertStatement.executeUpdate();

            // Check if there are more than 50 rows and delete the oldest one(s)
            ResultSet countResult = deleteStatement.executeQuery("SELECT COUNT(*) FROM RecentWords");
            if (countResult.next() && countResult.getInt(1) > 50) {
                deleteStatement.executeUpdate(deleteQuery);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
