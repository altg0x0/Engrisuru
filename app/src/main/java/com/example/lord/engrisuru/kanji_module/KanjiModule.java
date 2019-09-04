package com.example.lord.engrisuru.kanji_module;

import android.util.Log;

import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.lord.engrisuru.MainActivity;
import com.example.lord.engrisuru.ModuleSettings;
import com.example.lord.engrisuru.TranslationModule;
import com.example.lord.engrisuru.TranslationTask;
import com.example.lord.engrisuru.japanese.Kanji;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class KanjiModule extends TranslationModule {
    @Override
    public ModuleSettings getSettings() {
        return null;
    }

    @Override
    public void setSettings(ModuleSettings settings) {

    }

    @Override
    protected TranslationTask nextTranslation(int n) {
        String[] answers = new String[n];
//        long nanos = System.nanoTime();
//        MainActivity.db.query(new SimpleSQLiteQuery("select * from Kanji"));
//        long bananos = System.nanoTime();
//        Log.i("STOPWATCH", Long.toString((bananos - nanos) / 1000));


        Kanji[] allKanjiArray = MainActivity.db.kanjiDao().getKanjiByMinMaxGrade(1,11);
        List<Kanji> kanji = Arrays.asList(allKanjiArray);
        Collections.shuffle(kanji);
        for (int i = 0; i < n; i++) {
            answers[i] = allKanjiArray[i].englishMeanings[0]; //TODO: use weighted selection
        }
        int correctAnswerIndex = ThreadLocalRandom.current().nextInt(0, 8);
        String question = Character.toString(kanji.get(correctAnswerIndex).character);
        String correctAnswer = answers[correctAnswerIndex];
        return new TranslationTask(question, answers, correctAnswer);
    }

    @Override
    public boolean updateDatabase(boolean... params) {
        return false;
    }

    @Override
    public boolean exportModule() {
        return false;
    }

    @Override
    public boolean modifyDataByAnswer(TranslationTask task) {
        return false;
    }
}
