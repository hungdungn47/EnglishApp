package com.example.englishapp.game;

import java.io.IOException;

public interface IGame {
    void insertScoreFromTxt() throws IOException;

    void ExportScoreToTxt();

    void UpdateScore();
}
