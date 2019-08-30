package com.example.lord.engrisuru;

/**
 * Created by lord on 23/02/18.
 */

class TranslationTask {
    String correctTranslation;
    String word;
    String[] translations;
    String answer;

    TranslationTask(String word_arg, String[] translations_arg, String correctTranslation_arg)
    {
        word = word_arg;
        translations = translations_arg;
        correctTranslation = correctTranslation_arg;
    }

    boolean isAnswerCorrect(String hypotheticalAnswer)
    {
        return correctTranslation.equals(hypotheticalAnswer.replace("\u00AD",""));
    }
}
