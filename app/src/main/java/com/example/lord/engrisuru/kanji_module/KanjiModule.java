package com.example.lord.engrisuru.kanji_module;

import com.example.lord.engrisuru.MainActivity;
import com.example.lord.engrisuru.ModuleSettings;
import com.example.lord.engrisuru.TranslationModule;
import com.example.lord.engrisuru.TranslationTask;
import com.example.lord.engrisuru.japanese.Kanji;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

        Kanji[] kanjiArray = MainActivity.db.kanjiDao().getKanjiByMinMaxGrade(1,1, n);
        for (int i = 0; i < n; i++) {
            answers[i] = kanjiArray[i].englishMeanings[0]; //TODO: use weighted selection
        }
        Kanji askedKanji = kanjiArray[0];
        List<String> answersList = Arrays.asList(answers);
        String question = Character.toString(askedKanji.character);
        String correctAnswer = answers[0];
        Collections.shuffle(answersList);
        return new KanjiTranslationTask(question, answers, correctAnswer, askedKanji);
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
    public void modifyDataByAnswer(TranslationTask task) {
        boolean correct = task.isAnswerCorrect(task.answer);
        Kanji askedKanji = ((KanjiTranslationTask)task).askedKanji;
        askedKanji.weight *= correct? .6 : 2;
//        Log.i(TAG, "modifyDataByAnswer: new weight is" + askedKanji.weight);
        executor.execute(() -> MainActivity.db.kanjiDao().updateWeight(askedKanji));
    }
}
