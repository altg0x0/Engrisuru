package com.example.lord.engrisuru.kanji_module;

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
        Kanji[] allKanjiArray = MainActivity.db.kanjiDao().getKanjiByMinMaxGrade(1,1);
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
