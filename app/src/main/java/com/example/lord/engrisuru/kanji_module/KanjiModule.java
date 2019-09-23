package com.example.lord.engrisuru.kanji_module;

import com.example.lord.engrisuru.Utils;
import com.example.lord.engrisuru.abstract_module.ModuleSettings;
import com.example.lord.engrisuru.abstract_module.TranslationModule;
import com.example.lord.engrisuru.abstract_module.TranslationTask;
import com.example.lord.engrisuru.db.EngrisuruDatabase;
import com.example.lord.engrisuru.db.kanji.KanjiGentleModeEntry;
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
        KanjiModuleTaskType taskType = Utils.randomChoice(getSettings().taskTypes);
        Kanji[] kanjiArray = getSettings().gentleMode ?
                EngrisuruDatabase.getInstance().kanjiDao().getKanjiByMinMaxGradeGentleMode(getSettings().minGrade, getSettings().maxGrade, n) :
                EngrisuruDatabase.getInstance().kanjiDao().getKanjiByMinMaxGrade(getSettings().minGrade, getSettings().maxGrade, n);
        for (int i = 0; i < n; i++) {
            switch (taskType) {
                case ONYOMI_READINGS:
                    answers[i] = kanjiArray[i].onyomiReadings[0];
                    break;
                case ENGLISH_MEANINGS:
                    answers[i] = kanjiArray[i].englishMeanings[0];
                    break;
                case KUNYOMI_READINGS:
                    answers[i] = kanjiArray[i].kunyomiReadings[0];
                    break;
            }
        }
        Kanji askedKanji = kanjiArray[0];
        List<String> answersList = Arrays.asList(answers);
        String question = Character.toString(askedKanji.character);
        String correctAnswer = answers[0];
        Collections.shuffle(answersList);
        return new KanjiTranslationTask(question, answers, correctAnswer, askedKanji);
    }

    // TODO: delete this method
    @Override
    public boolean updateDatabase(boolean... params) {
        return false;
    }

    @Override
    public boolean exportModule() {
        return EngrisuruDatabase.exportDatabase();
    }

    @Override
    public boolean importModule() {
        return EngrisuruDatabase.importDatabase();
    }

    @Override
    public void modifyDataByAnswer(TranslationTask task) {
        boolean correct = task.isAnswerCorrect(task.answer);
        Kanji askedKanji = ((KanjiTranslationTask) task).askedKanji;
        askedKanji.weight *= correct ? .6 : 1.5;
//        Log.i(TAG, "modifyDataByAnswer: new weight is" + askedKanji.weight);
        executor.execute(() -> KanjiGentleModeEntry.updateForKanji(askedKanji));
        executor.execute(() -> EngrisuruDatabase.getInstance().kanjiDao().updateWeight(askedKanji));
    }
}
