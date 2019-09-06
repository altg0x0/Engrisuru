package com.example.lord.engrisuru.kanji_module;

import com.example.lord.engrisuru.MainActivity;
import com.example.lord.engrisuru.abstract_module.ModuleSettings;
import com.example.lord.engrisuru.abstract_module.TranslationModule;
import com.example.lord.engrisuru.abstract_module.TranslationTask;
import com.example.lord.engrisuru.japanese.Kanji;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KanjiModule extends TranslationModule {
    private KanjiModuleSettings _settings;

    @Override
    public KanjiModuleSettings getSettings() {
        return _settings;
    }

    @Override
    public void applySettings(ModuleSettings settings) {
        _settings = (KanjiModuleSettings) settings;
    }

    @Override
    protected TranslationTask nextTranslation(int n) {
        String[] answers = new String[n];
        Kanji[] kanjiArray = MainActivity.db.kanjiDao().getKanjiByMinMaxGrade(getSettings().minGrade, getSettings().maxGrade, n);
        for (int i = 0; i < n; i++) {
            answers[i] = kanjiArray[i].kunyomiReadings[0];
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
        askedKanji.weight *= correct? .6 : 1.2;
//        Log.i(TAG, "modifyDataByAnswer: new weight is" + askedKanji.weight);
        executor.execute(() -> MainActivity.db.kanjiDao().updateWeight(askedKanji));
    }
}
