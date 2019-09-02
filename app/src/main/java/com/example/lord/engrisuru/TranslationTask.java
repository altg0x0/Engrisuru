package com.example.lord.engrisuru;

/**
 * Created by lord on 23/02/18.
 */

public class TranslationTask {
    public String correctTranslation;
    public String word;
    String[] translations;
    public String answer;

    public TranslationTask(String word_arg, String[] translations_arg, String correctTranslation_arg)
    {
        word = word_arg;
        translations = translations_arg;
        correctTranslation = correctTranslation_arg;
    }

    public boolean isAnswerCorrect(String hypotheticalAnswer)
    {
        return correctTranslation.equals(hypotheticalAnswer.replace("\u00AD",""));
    }
}
