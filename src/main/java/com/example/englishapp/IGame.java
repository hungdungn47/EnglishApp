package com.example.englishapp;

import java.io.IOException;

public interface IGame {
    void insertScoreFromTxt() throws IOException;

    void ExportScoreToTxt();

    void UpdateScore();
}
