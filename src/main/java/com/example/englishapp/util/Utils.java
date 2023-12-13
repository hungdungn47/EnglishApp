package com.example.englishapp.util;

import com.example.englishapp.util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Utils extends DatabaseConnector {
    public static List<String> getFavoriteWords(String username) {
        List<String> favoriteWords = new ArrayList<>();

        try (PreparedStatement statement = getConnection().prepareStatement("SELECT word FROM FavoriteWords WHERE username = ?")) {

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
        try (PreparedStatement statement = getConnection().prepareStatement("INSERT INTO FavoriteWords (username, word) VALUES (?, ?)")) {

            statement.setString(1, username);
            statement.setString(2, word);
            statement.executeUpdate();
            System.out.println("New favorite word: " + word);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void removeFavoriteWord(String username, String word) {
        try (PreparedStatement statement = getConnection().prepareStatement("DELETE FROM FavoriteWords WHERE username = ? AND word = ?")) {

            statement.setString(1, username);
            statement.setString(2, word);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static List<String> getRecentWords(String username) {
        List<String> recentWords = new ArrayList<>();
        String sql = "SELECT word FROM RecentWords WHERE username = ? ORDER BY timestamp DESC LIMIT 50";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
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
        String selectQuery = "SELECT id FROM RecentWords ORDER BY timestamp LIMIT 1";
        String deleteQuery = "DELETE FROM RecentWords WHERE id = ?";

        try (PreparedStatement insertStatement = getConnection().prepareStatement(insertQuery);
             Statement selectStatement = getConnection().createStatement();
             PreparedStatement deleteStatement = getConnection().prepareStatement(deleteQuery)) {

            // Insert the new word
            insertStatement.setString(1, username);
            insertStatement.setString(2, word);
            insertStatement.executeUpdate();

            // Check if there are more than 50 rows and delete the oldest one(s)
            ResultSet oldestIdResult = selectStatement.executeQuery(selectQuery);
            if (oldestIdResult.next()) {
                int oldestId = oldestIdResult.getInt("id");
                deleteStatement.setInt(1, oldestId);
                deleteStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void removeRecentWord(String username, String word) {
        try (PreparedStatement statement = getConnection().prepareStatement("DELETE FROM RecentWords WHERE username = ? AND word = ?")) {

            statement.setString(1, username);
            statement.setString(2, word);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void addWord(String username, String word, String definition) {
        try (PreparedStatement statement = getConnection().prepareStatement("INSERT INTO AddedWords (username, word, definition) VALUES (?, ?, ?)")) {

            statement.setString(1, username);
            statement.setString(2, word);
            statement.setString(3, definition);
            statement.executeUpdate();
            System.out.println("New word: " + word + " means " + definition);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> getAddedWords(String username) {
        List<String[]> wordsAndDefinitions = new ArrayList<>();
        String selectQuery = "SELECT word, definition FROM AddedWords WHERE username = ?";

        try (PreparedStatement statement = getConnection().prepareStatement(selectQuery)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String word = resultSet.getString("word");
                String definition = resultSet.getString("definition");
                wordsAndDefinitions.add(new String[]{word, definition});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wordsAndDefinitions;
    }
    public static void deleteWord(String username, String word) {
        try (PreparedStatement statement = getConnection().prepareStatement("INSERT INTO DeletedWords (username, word) VALUES (?, ?)")) {

            statement.setString(1, username);
            statement.setString(2, word);
            statement.executeUpdate();
            System.out.println("word deleted: " + word);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static List<String> getDeletedWords(String username) {
        List<String> deletedWords = new ArrayList<>();
        String selectQuery = "SELECT word FROM DeletedWords WHERE username = ?";

        try (PreparedStatement statement = getConnection().prepareStatement(selectQuery)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String word = resultSet.getString("word");
                deletedWords.add(word);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return deletedWords;
    }
    public static void updateWord(String username, String word, String definition) {
        String insertQuery = "INSERT INTO UpdatedWords (username, word, definition) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE definition = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(insertQuery)) {
            pstmt.setString(1, username);
            pstmt.setString(2, word);
            pstmt.setString(3, definition);
            pstmt.setString(4, definition);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Handle the SQL exception
            e.printStackTrace();
        }
    }
    public static List<String[]> getUpdatedWords(String username) {
        List<String[]> wordsAndDefinitions = new ArrayList<>();
        String selectQuery = "SELECT word, definition FROM UpdatedWords WHERE username = ?";

        try (PreparedStatement statement = getConnection().prepareStatement(selectQuery)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String word = resultSet.getString("word");
                String definition = resultSet.getString("definition");
                wordsAndDefinitions.add(new String[]{word, definition});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wordsAndDefinitions;
    }
}
